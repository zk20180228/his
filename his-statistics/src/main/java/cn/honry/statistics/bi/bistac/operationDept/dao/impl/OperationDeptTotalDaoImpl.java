package cn.honry.statistics.bi.bistac.operationDept.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.bson.Document;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.statistics.bi.bistac.operationDept.dao.OperationDeptTotalDao;
import cn.honry.statistics.bi.bistac.operationDept.vo.OperationDeptTotalVo;
import cn.honry.statistics.bi.bistac.operationDept.vo.OperationDocTotalVo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
@Repository("operationDeptTotalDao")
public class OperationDeptTotalDaoImpl extends HibernateEntityDao<OperationDeptTotalVo> implements OperationDeptTotalDao {
	/**
	 * mongoDB表名称
	 */
	private static final String OPERATIONDEPTTOTALTABLEBYDAY = "OPERATIONDEPTTOTALTABLEBYDAY";//手术科室统计表（Y-M-D）
	private static final String OPERATIONDEPTTOTALTABLEBYMOUTH = "OPERATIONDEPTTOTALTABLEBYMOUTH";//手术科室统计表（Y-M）
	private static final String OPERATIONDEPTTOTALTABLEBYYEAR = "OPERATIONDEPTTOTALTABLEBYYEAR";//手术科室统计表（Y）
	private static final String OPERATIONDOCTOTALTABLEBYDAY = "OPERATIONDOCTOTALTABLEBYDAY";//手术医生统计表（Y-M-D）
	private static final String OPERATIONDOCTOTALTABLEBYMOUTH = "OPERATIONDOCTOTALTABLEBYMOUTH";//手术医生统计表（Y-M）
	private static final String OPERATIONDOCTOTALTABLEBYYEAR = "OPERATIONDOCTOTALTABLEBYYEAR";//手术医生统计表（Y）
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/**手术科室按天统计*/
	private final String[] inItem={"T_INPATIENT_ITEMLIST_NOW","T_INPATIENT_ITEMLIST"};
	private final String[] outFee={"T_OUTPATIENT_FEEDETAIL_NOW","T_OUTPATIENT_FEEDETAIL"};
	@Override
	public void initOperationDeptTotalByDay(String startTime,String endTime) {
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL=wordLoadDocDao.returnInTables(startTime, endTime, inItem, "ZY");
		maintnl=wordLoadDocDao.returnInTables(startTime, endTime, outFee, "MZ");
		StringBuffer buffer = new StringBuffer(2000);
		buffer.append("select sum(nvl(fee.tot_cost, 0)) as ssgzlcost,");
		buffer.append(" count(1) as ssgzlnum,");
		buffer.append(" d.dept_name as dept_name ");
		buffer.append(" FROM T_DEPARTMENT d , ");
		buffer.append(" (select rc.clinic_code, rc.EXEC_DEPT execDept,rc.operation_id operationId ");
		buffer.append(" from T_OPERATION_RECORD rc ");
		buffer.append(" where rc.createtime >=to_date('"+startTime+"', 'yyyy-MM-dd HH24:MI:ss') ");
		buffer.append(" and rc.createtime <to_date('"+endTime+"', 'yyyy-MM-dd HH24:MI:ss') ");
		buffer.append(" AND rc.YNVALID = 1");
		buffer.append(" AND rc.del_flg = 0");
		buffer.append(" AND rc.stop_flg = 0) rc ");
		buffer.append(" LEFT JOIN(");
		for (int i = 0; i < tnL.size(); i++) {
			if(i>0){
				buffer.append(" union all");
			}
			buffer.append(" select sum(t"+i+".tot_cost) as tot_cost,");
			buffer.append(" t"+i+".OPERATION_ID operationId, ");
			buffer.append(" t"+i+".inpatient_no as cliniCode");
			buffer.append(" from "+tnL.get(i)+" t"+i);
			buffer.append(" join t_charge_minfeetostat c on c.minfee_code = t"+i+".fee_code");
			buffer.append(" and c.report_code = 'ZY01'");
			buffer.append(" and c.fee_stat_code = '10'");
			buffer.append(" where t"+i+".trans_type = 1");
			buffer.append(" and t"+i+".del_flg = 0");
			buffer.append(" and t"+i+".stop_flg = 0");
			buffer.append(" group by t"+i+".OPERATION_ID, t"+i+".inpatient_no ");
		}
		if(maintnl.size()>0){
			buffer.append(" union all");
		}
		for (int i = 0; i < maintnl.size(); i++) {
			if(i>0){
				buffer.append(" union all");
			}
			buffer.append(" select sum(t1"+i+".tot_cost) as tot_cost,");
			buffer.append(" null as operationId,");
			buffer.append(" t1"+i+".clinic_code cliniCode ");
			buffer.append(" from "+maintnl.get(i)+" t1"+i);
			buffer.append(" join t_charge_minfeetostat c on c.minfee_code = t1"+i+".fee_code");
			buffer.append(" and c.report_code = 'MZ01'");
			buffer.append(" and c.fee_stat_code = '10'");
			buffer.append(" where t1"+i+".trans_type = 1");
			buffer.append(" and t1"+i+".del_flg = 0");
			buffer.append(" and t1"+i+".stop_flg = 0");
			buffer.append(" group by t1"+i+".clinic_code ");
			
		}
		buffer.append(" ) fee ON rc.clinic_code =fee.cliniCode and fee.operationId =rc.operationId ");
		buffer.append(" WHERE fee.tot_cost!=0 and d.DEPT_CODE=rc.execDept ");
		buffer.append(" GROUP BY d.DEPT_Name ");
		List<OperationDeptTotalVo> list = namedParameterJdbcTemplate.query(buffer.toString(), new RowMapper<OperationDeptTotalVo>(){
			@Override
			public OperationDeptTotalVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				OperationDeptTotalVo vo  = new OperationDeptTotalVo();
				vo.setDept_name(rs.getString("dept_name"));
				vo.setSsGzlCost(rs.getDouble("ssGzlCost"));
				vo.setSsGzlNum(rs.getInt("ssGzlNum"));
				return vo;
			}
		});
//		List<OperationDeptTotalVo> list1 = new ArrayList<OperationDeptTotalVo>();
//		for (OperationDeptTotalVo operationDeptTotalVo : list) {
//			if(operationDeptTotalVo.getInpatient_date()!=null&&!"".equals(operationDeptTotalVo.getInpatient_date())){
//				list1.add(operationDeptTotalVo);
//			}
//		}
		String date=startTime.substring(0,10);
		DBObject query = new BasicDBObject();
		query.put("inpatient_date", date);//移除数据条件
		new MongoBasicDao().remove(OPERATIONDEPTTOTALTABLEBYDAY, query);
		if(list!=null&&list.size()>0){
			for (OperationDeptTotalVo vo : list) {
				Document doucment1=new Document();
				doucment1.append("dept_name", vo.getDept_name());
				doucment1.append("inpatient_date", date);
				Document doucment=new Document();
				doucment.append("dept_name", vo.getDept_name());
				doucment.append("inpatient_date", date);
				doucment.append("ssGzlCost", vo.getSsGzlCost());
				doucment.append("ssGzlNum", vo.getSsGzlNum());
				new MongoBasicDao().update(OPERATIONDEPTTOTALTABLEBYDAY, doucment1, doucment, true);
			}
		}
	}
	/**手术科室按月统计*/
	@Override
	public void initOperationDeptTotalByMonth(String startTime, String endTime) {
		List<OperationDeptTotalVo> vos = new ArrayList<OperationDeptTotalVo>();
		DBCursor cursor=null;
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList();
		bdObjectTimeS.append("inpatient_date", new BasicDBObject("$gte",startTime+"-01"));
		condList.add(bdObjectTimeS);
		bdObjectTimeE.append("inpatient_date", new BasicDBObject("$lt",endTime+"-01"));
		condList.add(bdObjectTimeE);
		bdObject.put("$and", condList);
		cursor=new MongoBasicDao().findAlldata(OPERATIONDEPTTOTALTABLEBYDAY,bdObject);
		while(cursor.hasNext()){
			OperationDeptTotalVo vo = new OperationDeptTotalVo();
			DBObject dbObject = cursor.next();
			String dept_name = dbObject.get("dept_name").toString();
			String inpatient_date = dbObject.get("inpatient_date").toString();
			String ssGzlCost = dbObject.get("ssGzlCost").toString();
			String ssGzlNum = dbObject.get("ssGzlNum").toString();
			vo.setDept_name(dept_name);
			vo.setInpatient_date(inpatient_date.substring(0, 7));
			vo.setSsGzlCost(Double.valueOf(ssGzlCost));
			vo.setSsGzlNum(Integer.parseInt(ssGzlNum));
			vos.add(vo);
		}
		Map<String,OperationDeptTotalVo> map = new HashMap<String,OperationDeptTotalVo>();
		for (OperationDeptTotalVo vo : vos) {
			String key = vo.getInpatient_date()+"_"+vo.getDept_name();
			if(map.get(key)==null){
				map.put(key, vo);
			}else{
				map.get(key).setSsGzlCost(vo.getSsGzlCost()+map.get(key).getSsGzlCost());
				map.get(key).setSsGzlNum(vo.getSsGzlNum()+map.get(key).getSsGzlNum());
			}
			map.get(key).setInpatient_date(vo.getInpatient_date().substring(0, 7));
		}
		DBObject query = new BasicDBObject();
		query.put("inpatient_date", startTime);//移除数据条件
		new MongoBasicDao().remove(OPERATIONDEPTTOTALTABLEBYMOUTH, query);
		Set<String> keySet = map.keySet();
		for (String string : keySet) {
//			System.out.println(string+"-->"+map.get(string));
//			Double ssgzlcost = 0D;
//			int ssgzlnum = 0;
			OperationDeptTotalVo vo = map.get(string);
//			BasicDBObject bob = new BasicDBObject();
//			bob.append("inpatient_date", vo.getInpatient_date());
//			bob.append("dept_name", vo.getDept_name());
//			DBCursor cursor1 = new MongoBasicDao().findAlldata(OPERATIONDEPTTOTALTABLEBYMOUTH, bob);
//			DBObject dbCursor;
//			while(cursor1.hasNext()){
//				dbCursor = cursor1.next();
//				if(dbCursor.get("ssGzlnum")!=null){
//					ssgzlnum = (Integer)dbCursor.get("ssGzlnum");//例数
//				}
//				if(dbCursor.get("ssGzlcost")!=null){
//					ssgzlcost = (Double) dbCursor.get("ssGzlcost");//金额
//				}
//			}
			Document doucment1=new Document();
			doucment1.append("dept_name", vo.getDept_name());
			doucment1.append("inpatient_date", vo.getInpatient_date());
			Document doucment=new Document();
			doucment.append("dept_name", vo.getDept_name());
			doucment.append("inpatient_date", vo.getInpatient_date());
			doucment.append("ssGzlCost", vo.getSsGzlCost());
			doucment.append("ssGzlNum", vo.getSsGzlNum());
			new MongoBasicDao().update(OPERATIONDEPTTOTALTABLEBYMOUTH, doucment1, doucment, true);
		}
		
	}
	/**手术科室按年统计*/
	@Override
	public void initOperationDeptTotalByYear(String startTime, String endTime) {
		List<OperationDeptTotalVo> vos = new ArrayList<OperationDeptTotalVo>();
		DBCursor cursor=null;
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList();
		bdObjectTimeS.append("inpatient_date", new BasicDBObject("$gte",startTime.substring(0, 4)+"-01"));
		condList.add(bdObjectTimeS);
		bdObjectTimeE.append("inpatient_date", new BasicDBObject("$lt",endTime.substring(0, 4)+"-01"));
		condList.add(bdObjectTimeE);
		bdObject.put("$and", condList);
		cursor=new MongoBasicDao().findAlldata(OPERATIONDEPTTOTALTABLEBYMOUTH,bdObject);
		while(cursor.hasNext()){
			OperationDeptTotalVo vo = new OperationDeptTotalVo();
			DBObject dbObject = cursor.next();
			String dept_name = dbObject.get("dept_name").toString();
			String inpatient_date = dbObject.get("inpatient_date").toString();
			String ssGzlCost = dbObject.get("ssGzlCost").toString();
			String ssGzlNum = dbObject.get("ssGzlNum").toString();
			vo.setDept_name(dept_name);
			vo.setInpatient_date(inpatient_date);
			vo.setSsGzlCost(Double.valueOf(ssGzlCost));
			vo.setSsGzlNum(Integer.parseInt(ssGzlNum));
			vos.add(vo);
		}
		Map<String,OperationDeptTotalVo> map = new HashMap<String,OperationDeptTotalVo>();
		for (OperationDeptTotalVo vo : vos) {
			String key = vo.getInpatient_date() + "_" + vo.getDept_name();
			if(map.get(key)==null){
				map.put(key, vo);
			}else{
				map.get(key).setSsGzlCost(vo.getSsGzlCost()+map.get(key).getSsGzlCost());
				map.get(key).setSsGzlNum(vo.getSsGzlNum()+map.get(key).getSsGzlNum());
			}
			map.get(key).setInpatient_date(vo.getInpatient_date().substring(0,4));
		}
		DBObject query = new BasicDBObject();
		query.put("inpatient_date", startTime);//移除数据条件
		new MongoBasicDao().remove(OPERATIONDEPTTOTALTABLEBYYEAR, query);
		Set<String> keySet = map.keySet();
		for (String string : keySet) {
//			System.out.println(string+"-->"+map.get(string));
//			Double ssgzlcost = 0D;
//			int ssgzlnum = 0;
			OperationDeptTotalVo vo = map.get(string);
//			BasicDBObject bob = new BasicDBObject();
//			bob.append("inpatient_date", vo.getInpatient_date());
//			bob.append("dept_name", vo.getDept_name());
//			DBCursor cursor1 = new MongoBasicDao().findAlldata(OPERATIONDEPTTOTALTABLEBYYEAR, bob);
//			DBObject dbCursor;
//			while(cursor1.hasNext()){
//				dbCursor = cursor1.next();
//				if(dbCursor.get("ssGzlnum")!=null){
//					ssgzlnum = (Integer)dbCursor.get("ssGzlnum");//例数
//				}
//				if(dbCursor.get("ssGzlcost")!=null){
//					ssgzlcost = (Double) dbCursor.get("ssGzlcost");//金额
//				}
//			}
			Document doucment1=new Document();
			doucment1.append("dept_name", vo.getDept_name());
			doucment1.append("inpatient_date", vo.getInpatient_date());
			Document doucment=new Document();
			doucment.append("dept_name", vo.getDept_name());
			doucment.append("inpatient_date", vo.getInpatient_date());
			doucment.append("ssGzlCost", vo.getSsGzlCost());
			doucment.append("ssGzlNum", vo.getSsGzlNum());
			new MongoBasicDao().update(OPERATIONDEPTTOTALTABLEBYYEAR, doucment1, doucment, true);
		}
		
	}
	/**手术医生按天统计*/
	public void initOperationDocTotalByDay(String startTime,String endTime) {
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL=wordLoadDocDao.returnInTables(startTime, endTime, inItem, "ZY");
		maintnl=wordLoadDocDao.returnInTables(startTime, endTime, outFee, "MZ");
		StringBuffer buffer = new StringBuffer(2000);
		buffer.append("SELECT SUM(NVL(fee.tot_cost, 0)) AS ysgzlcost,");
		buffer.append(" COUNT(1) AS ysgzlnum,");
		buffer.append(" e.employee_name as name ");
		buffer.append(" FROM t_employee e , ");
		buffer.append(" (select rc.clinic_code, rc.ops_docd,rc.operation_id operationId ");
		buffer.append(" from T_OPERATION_RECORD rc ");
		buffer.append(" where rc.createtime >=to_date('"+startTime+"', 'yyyy-MM-dd HH24:MI:ss') ");
		buffer.append(" and rc.createtime <to_date('"+endTime+"', 'yyyy-MM-dd HH24:MI:ss') ");
		buffer.append(" AND rc.YNVALID = 1");
		buffer.append(" AND rc.del_flg = 0");
		buffer.append(" AND rc.stop_flg = 0) rc ");
		buffer.append(" LEFT JOIN(");
		for (int i = 0; i < tnL.size(); i++) {
			if(i>0){
				buffer.append(" union all");
			}
			buffer.append(" select sum(t"+i+".tot_cost) as tot_cost,");
			buffer.append(" t"+i+".OPERATION_ID operationId, ");
			buffer.append(" t"+i+".inpatient_no as cliniCode ");
			buffer.append(" from "+tnL.get(i)+" t"+i);
			buffer.append(" join t_charge_minfeetostat c on c.minfee_code = t"+i+".fee_code");
			buffer.append(" and c.report_code = 'ZY01'");
			buffer.append(" and c.fee_stat_code = '10'");
			buffer.append(" where t"+i+".trans_type = 1");
			buffer.append(" and t"+i+".del_flg = 0");
			buffer.append(" and t"+i+".stop_flg = 0");
			buffer.append(" group by t"+i+".OPERATION_ID, t"+i+".inpatient_no ");
		}
		if(maintnl.size()>0){
			buffer.append(" union all");
		}
		for (int i = 0; i < maintnl.size(); i++) {
			if(i>0){
				buffer.append(" union all");
			}
			buffer.append(" select  sum(t1"+i+".tot_cost) as tot_cost,");
			buffer.append(" null as operationId,");
			buffer.append(" t1"+i+".clinic_code cliniCode");
			buffer.append(" from "+maintnl.get(i)+" t1"+i);
			buffer.append(" join t_charge_minfeetostat c on c.minfee_code = t1"+i+".fee_code");
			buffer.append(" and c.report_code = 'MZ01'");
			buffer.append(" and c.fee_stat_code = '10'");
			buffer.append(" where t1"+i+".trans_type = 1");
			buffer.append(" and t1"+i+".del_flg = 0");
			buffer.append(" and t1"+i+".stop_flg = 0");
			buffer.append(" group by t1"+i+".clinic_code ");
			
		}
		buffer.append(" ) fee ON rc.clinic_code =fee.cliniCode and fee.operationId =rc.operationId ");
		buffer.append(" WHERE fee.tot_cost!=0 and e.employee_jobno=rc.ops_docd ");
		buffer.append(" GROUP BY e.employee_name ");
		List<OperationDocTotalVo> list = namedParameterJdbcTemplate.query(buffer.toString(), new RowMapper<OperationDocTotalVo>(){
			@Override
			public OperationDocTotalVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				OperationDocTotalVo vo  = new OperationDocTotalVo();
					vo.setName(rs.getString("name"));
					vo.setYsgzlcost(rs.getDouble("ysgzlcost"));
					vo.setYsgzlnum(rs.getInt("ysgzlnum"));
				return vo;
			}
		});
		String date=startTime.substring(0,10);
		DBObject query = new BasicDBObject();
		query.put("inpatient_date", date);//移除数据条件
		new MongoBasicDao().remove(OPERATIONDOCTOTALTABLEBYDAY, query);
		
		if(list!=null&&list.size()>0){
			for (OperationDocTotalVo vo : list) {
				Document doucment1=new Document();
				doucment1.append("dept_name", vo.getName());
				doucment1.append("inpatient_date", date);
				Document doucment=new Document();
				doucment.append("dept_name", vo.getName());
				doucment.append("inpatient_date", date);
				doucment.append("ysgzlcost", vo.getYsgzlcost());
				doucment.append("ysgzlnum", vo.getYsgzlnum());
				new MongoBasicDao().update(OPERATIONDOCTOTALTABLEBYDAY, doucment1, doucment, true);
			}
		}
	}
	/**手术医生按月统计*/
	@Override
	public void initOperationDocTotalByMonth(String startTime, String endTime) {
		List<OperationDeptTotalVo> vos = new ArrayList<OperationDeptTotalVo>();
		DBCursor cursor=null;
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList();
		bdObjectTimeS.append("inpatient_date", new BasicDBObject("$gte",startTime+"-01"));
		condList.add(bdObjectTimeS);
		bdObjectTimeE.append("inpatient_date", new BasicDBObject("$lt",endTime+"-01"));
		condList.add(bdObjectTimeE);
		bdObject.put("$and", condList);
		cursor=new MongoBasicDao().findAlldata(OPERATIONDOCTOTALTABLEBYDAY,bdObject);
		while(cursor.hasNext()){
			OperationDeptTotalVo vo = new OperationDeptTotalVo();
			DBObject dbObject = cursor.next();
			String dept_name = dbObject.get("dept_name").toString();
			String inpatient_date = dbObject.get("inpatient_date").toString();
			String ssGzlCost = dbObject.get("ysgzlcost").toString();
			String ssGzlNum = dbObject.get("ysgzlnum").toString();
			vo.setDept_name(dept_name);
			vo.setInpatient_date(inpatient_date.substring(0,7));
			vo.setSsGzlCost(Double.valueOf(ssGzlCost));
			vo.setSsGzlNum(Integer.parseInt(ssGzlNum));
			vos.add(vo);
		}
		Map<String,OperationDeptTotalVo> map = new HashMap<String,OperationDeptTotalVo>();
		for (OperationDeptTotalVo vo : vos) {
			String key = vo.getInpatient_date()+"_"+vo.getDept_name();
			if(map.get(key)==null){
				map.put(key, vo);
			}else{
				map.get(key).setSsGzlCost(vo.getSsGzlCost()+map.get(key).getSsGzlCost());
				map.get(key).setSsGzlNum(vo.getSsGzlNum()+map.get(key).getSsGzlNum());
			}
			map.get(key).setInpatient_date(vo.getInpatient_date().substring(0, 7));
		}
		DBObject query = new BasicDBObject();
		query.put("inpatient_date", startTime);//移除数据条件
		new MongoBasicDao().remove(OPERATIONDOCTOTALTABLEBYMOUTH, query);
		Set<String> keySet = map.keySet();
		for (String string : keySet) {
//			System.out.println(string+"-->"+map.get(string));
//			Double ssgzlcost = 0D;
//			int ssgzlnum = 0;
			OperationDeptTotalVo vo = map.get(string);
//			BasicDBObject bob = new BasicDBObject();
//			bob.append("inpatient_date", vo.getInpatient_date());
//			bob.append("dept_name", vo.getDept_name());
//			DBCursor cursor1 = new MongoBasicDao().findAlldata(OPERATIONDOCTOTALTABLEBYMOUTH, bob);
//			DBObject dbCursor;
//			while(cursor1.hasNext()){
//				dbCursor = cursor1.next();
//				if(dbCursor.get("ysgzlnum")!=null){
//					ssgzlnum = (Integer)dbCursor.get("ysgzlnum");//例数
//				}
//				if(dbCursor.get("ysgzlcost")!=null){
//					ssgzlcost = (Double) dbCursor.get("ysgzlcost");//金额
//				}
//			}
			Document doucment1=new Document();
			doucment1.append("dept_name", vo.getDept_name());
			doucment1.append("inpatient_date", vo.getInpatient_date());
			Document doucment=new Document();
			doucment.append("dept_name", vo.getDept_name());
			doucment.append("inpatient_date", vo.getInpatient_date());
			doucment.append("ysgzlcost", vo.getSsGzlCost());
			doucment.append("ysgzlnum", vo.getSsGzlNum());
			new MongoBasicDao().update(OPERATIONDOCTOTALTABLEBYMOUTH, doucment1, doucment, true);
		}
		
	}
	/**手术医生按年统计*/
	@Override
	public void initOperationDocTotalByYear(String startTime, String endTime) {
		List<OperationDeptTotalVo> vos = new ArrayList<OperationDeptTotalVo>();
		DBCursor cursor=null;
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList();
		bdObjectTimeS.append("inpatient_date", new BasicDBObject("$gte",startTime.substring(0, 4)+"-01"));
		condList.add(bdObjectTimeS);
		bdObjectTimeE.append("inpatient_date", new BasicDBObject("$lt",endTime.substring(0, 4)+"-01"));
		condList.add(bdObjectTimeE);
		bdObject.put("$and", condList);
		cursor=new MongoBasicDao().findAlldata(OPERATIONDOCTOTALTABLEBYMOUTH,bdObject);
		while(cursor.hasNext()){
			OperationDeptTotalVo vo = new OperationDeptTotalVo();
			DBObject dbObject = cursor.next();
			String dept_name = dbObject.get("dept_name").toString();
			String inpatient_date = dbObject.get("inpatient_date").toString();
			String ssGzlCost = dbObject.get("ysgzlcost").toString();
			String ssGzlNum = dbObject.get("ysgzlnum").toString();
			vo.setDept_name(dept_name);
			vo.setInpatient_date(inpatient_date.substring(0, 4));
			vo.setSsGzlCost(Double.valueOf(ssGzlCost));
			vo.setSsGzlNum(Integer.parseInt(ssGzlNum));
			vos.add(vo);
		}
		Map<String,OperationDeptTotalVo> map = new HashMap<String,OperationDeptTotalVo>();
		for (OperationDeptTotalVo vo : vos) {
			String key = vo.getInpatient_date() +"_"+ vo.getDept_name();
			if(map.get(key)==null){
				map.put(key, vo);
			}else{
				map.get(key).setSsGzlCost(vo.getSsGzlCost()+map.get(key).getSsGzlCost());
				map.get(key).setSsGzlNum(vo.getSsGzlNum()+map.get(key).getSsGzlNum());
			}
			map.get(key).setInpatient_date(vo.getInpatient_date().substring(0,4));
		}
		DBObject query = new BasicDBObject();
		query.put("inpatient_date", startTime);//移除数据条件
		new MongoBasicDao().remove(OPERATIONDOCTOTALTABLEBYYEAR, query);
		Set<String> keySet = map.keySet();
		for (String string : keySet) {
//			System.out.println(string+"-->"+map.get(string));
//			Double ssgzlcost = 0D;
//			int ssgzlnum = 0;
			OperationDeptTotalVo vo = map.get(string);
//			BasicDBObject bob = new BasicDBObject();
//			bob.append("inpatient_date", vo.getInpatient_date());
//			bob.append("dept_name", vo.getDept_name());
//			DBCursor cursor1 = new MongoBasicDao().findAlldata(OPERATIONDOCTOTALTABLEBYYEAR, bob);
//			DBObject dbCursor;
//			while(cursor1.hasNext()){
//				dbCursor = cursor1.next();
//				if(dbCursor.get("ysgzlnum")!=null){
//					ssgzlnum = (Integer)dbCursor.get("ysgzlnum");//例数
//				}
//				if(dbCursor.get("ysgzlcost")!=null){
//					ssgzlcost = (Double) dbCursor.get("ysgzlcost");//金额
//				}
//			}
			Document doucment1=new Document();
			doucment1.append("dept_name", vo.getDept_name());
			doucment1.append("inpatient_date", vo.getInpatient_date());
			Document doucment=new Document();
			doucment.append("dept_name", vo.getDept_name());
			doucment.append("inpatient_date", vo.getInpatient_date());
			doucment.append("ysgzlcost", vo.getSsGzlCost());
			doucment.append("ysgzlnum", vo.getSsGzlNum());
			new MongoBasicDao().update(OPERATIONDOCTOTALTABLEBYYEAR, doucment1, doucment, true);
		}
		
	}
	/** 日期时间差（true 两个小时以内，false 两个小时以外）*/
	public boolean timeDifference(String startDate,String endDate) {
		 SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
		 try {
			 Date begins = fmt.parse(startDate);
			 if(fmt.format(begins).toString().equals(fmt.format(new Date()).toString())){
		    	fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
					 Date begin = fmt.parse(startDate);
					 Date end = fmt.parse(endDate);
					 long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒
				 	 long hour=between%(24*3600)/3600;
				     if(hour<=2){
				         return true;
				     }
				 	 return false;
				  
		      }
		  } catch (ParseException e) {
		  }
	     return false;
	}
	
}
