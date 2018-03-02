package cn.honry.statistics.deptstat.operationProportion.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugChecklogs;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.deptstat.operationProportion.dao.OperationProportionDao;
import cn.honry.statistics.deptstat.operationProportion.vo.OperationProportionVo;
import cn.honry.statistics.doctor.wordLoadDoctorTotal.vo.WordLoadVO;
import cn.honry.statistics.drug.inventoryLog.vo.InventoryLogVo;
import cn.honry.utils.HisParameters;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


@Repository("operationProportionDao")
@SuppressWarnings({"all"})
public class OperationProportionDaoImpl extends HibernateEntityDao<DrugChecklogs> implements OperationProportionDao{
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/***
	 * 盘点日志查询(统计)记录
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@Override
	public List<InventoryLogVo> queryInventoryLog(List<String> tnL,String Stime, String Etime,
			String dept, String page, String rows,String drug) throws Exception{
		if(tnL==null||tnL.size()<0){
			return new ArrayList<InventoryLogVo>();
		}
		String sql=querySql(tnL,Stime,Etime,dept,drug);
		sql="SELECT　* FROM (SELECT　t1.*,ROWNUM as n FROM ( "+sql+"  ) t1 where ROWNUM<=:row  )　WHERE n>:page ORDER BY createtime DESC";
		Map paraMap=new HashMap();
		
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		if(StringUtils.isNotBlank(Stime)){
			paraMap.put("begin", Stime);
		}
		if(StringUtils.isNotBlank(Etime)){
			paraMap.put("end", Etime);
		}
		if(StringUtils.isNotBlank(dept)){
			paraMap.put("dept", dept);
		}
		if(StringUtils.isNotBlank(drug)){
			paraMap.put("drug", drug);
		}
		paraMap.put("row", count*start);
		paraMap.put("page", (start - 1) * count);
		List<InventoryLogVo> DrugChecklogsList= namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InventoryLogVo>() {
			@Override
			public InventoryLogVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				InventoryLogVo vo = new InventoryLogVo();
				vo.setCheckCode(rs.getString("checkCode"));
				vo.setDeptName(rs.getString("deptName"));
				vo.setDrugCode(rs.getString("drugCode"));
				vo.setBatchNo(rs.getString("batchNo"));
				vo.setTradeName(rs.getString("tradeName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setRetailPrice(rs.getDouble("retailPrice"));
				vo.setPackUnit(rs.getString("packUnit"));
				vo.setPackQty(rs.getDouble("packQty"));
				vo.setAdjustNum(rs.getDouble("adjustNum"));
				vo.setAdjustUnit(rs.getString("adjustUnit"));
				vo.setPlaceNo(rs.getString("placeNo"));
				vo.setRemark(rs.getString("remark"));
				vo.setCreateuser(rs.getString("createuser"));
				vo.setCreatetime(rs.getTimestamp("createtime"));
				vo.setUserName(rs.getString("userName"));
				return vo;
			}
			
		});
		
		if(DrugChecklogsList!=null&&DrugChecklogsList.size()>0){
			return DrugChecklogsList;
		}
		return new ArrayList<InventoryLogVo>();
	}
	/***
	 * 盘点日志查询(统计)条数
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@Override
	public int queryInventoryLogTotle(List<String> tnL,String Stime, String Etime, String dept,String drug) throws Exception{
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		String sql=" SELECT COUNT(1) AS specs FROM ( "+querySql(tnL,Stime,Etime,dept,drug).toString()+" )";
		Map paraMap=new HashMap();
		if(StringUtils.isNotBlank(Stime)){
			paraMap.put("begin", Stime);
		}
		if(StringUtils.isNotBlank(Etime)){
			paraMap.put("end", Etime);
		}
		if(StringUtils.isNotBlank(dept)){
			paraMap.put("dept", dept);
		}
		if(StringUtils.isNotBlank(drug)){
			paraMap.put("drug", drug);
		}
		List<InventoryLogVo> DrugChecklogsList= namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InventoryLogVo>() {

			@Override
			public InventoryLogVo mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				InventoryLogVo vo=new InventoryLogVo();
				vo.setSpecs(arg0.getString("specs"));
				return vo;
			}
			
		});
		if(null!=DrugChecklogsList&&DrugChecklogsList.size()>0){
			return Integer.parseInt(DrugChecklogsList.get(0).getSpecs());
		}
		return 0;
	}
	
	/***
	 * 拼接sql
	 * @Description:
	 * @author: zpty 
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	private String querySql(List<String> tnL,String Stime,String Etime, String dept,String drug){
		final StringBuffer sb = new StringBuffer();
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			sb.append("select (select ch.CHECK_CODE from T_DRUG_CHECKSTATIC ch where ch.id in (select c.check_id from T_DRUG_CHECKDETAIL c where c.DRUG_DEPT_CODE=t").append(i).append(".DRUG_DEPT_CODE and c.DRUG_CODE=t").append(i).append(".DRUG_CODE and c.del_flg=0 and c.stop_flg=0) and ch.del_flg=0 and ch.stop_flg=0) as checkCode,");
			sb.append("t").append(i).append(".DRUG_DEPT_CODE  as deptName,");
			sb.append("t").append(i).append(".DRUG_CODE  as drugCode,");
			sb.append("t").append(i).append(".BATCH_NO as batchNo,t").append(i).append(".TRADE_NAME as tradeName,t").append(i).append(".SPECS as specs,t").append(i).append(".RETAIL_PRICE as retailPrice,");
			sb.append("t").append(i).append(".PACK_UNIT as packUnit,");
			sb.append("t").append(i).append(".PACK_QTY as packQty,t").append(i).append(".ADJUST_NUM as adjustNum,");
			sb.append("t").append(i).append(".ADJUST_UNIT as adjustUnit,");
			sb.append("t").append(i).append(".PLACE_NO placeNo,t").append(i).append(".REMARK as remark,t").append(i).append(".CREATEUSER as createuser,t").append(i).append(".CREATETIME as createtime,");
			sb.append("t").append(i).append(".CREATEUSER  as userName");
			sb.append(" from ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t").append(i).append(" where t").append(i).append(".del_flg=0 and t").append(i).append(".stop_flg=0 ");
			if(StringUtils.isNotBlank(Stime)){
				sb.append(" and trunc(t").append(i).append(".CREATETIME,'dd') >= to_date(:begin,'yyyy-MM-dd hh24:mi:ss')");
			}
			if(StringUtils.isNotBlank(Etime)){
				sb.append(" and trunc(t").append(i).append(".CREATETIME,'dd') <= to_date(:end,'yyyy-MM-dd hh24:mi:ss') ");
			}
			if(StringUtils.isNotBlank(dept)){
				sb.append(" and t").append(i).append(".drug_dept_code = :dept ");
			}
			if(StringUtils.isNotBlank(drug)){
				sb.append(" and t").append(i).append(".DRUG_CODE = :drug ");
			}
		}
		return sb.toString();

	}
	
	/**  
	 *  
	 * @Description：  科室下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> getComboboxdept() throws Exception{
		String hql=" FROM SysDepartment d where d.stop_flg = 0 and d.del_flg = 0 and d.deptType in('P','PI') ";
		List<SysDepartment> deptList=this.find(hql, null);
		if(deptList!=null && deptList.size()>0){
			return deptList;
		}	
		return new ArrayList<SysDepartment>();
	}
	
	/**  
	 *  
	 * @Description：  药品下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DrugInfo> getComboboxdrug() throws Exception {
		String hql="FROM DrugInfo d where d.stop_flg = 0 and d.del_flg = 0";
		List<DrugInfo> drugList=this.find(hql, null);
		if(drugList!=null && drugList.size()>0){
			return drugList;
		}	
		return new ArrayList<DrugInfo>();
	}

	/**
	 * @Description:导出 
	 * @Author： lt @CreateDate： 2015-9-10
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 **/
	@Override
	public List<InventoryLogVo> queryInvLogExp(List<String> tnL,String Stime, String Etime,
			String dept, String drug) throws Exception{
		if(tnL==null||tnL.size()<0){
			return new ArrayList<InventoryLogVo>();
		}
		String sql=" SELECT * FROM ( "+querySql(tnL,Stime,Etime,dept,drug).toString()+" )";
		Map paraMap=new HashMap();
		
		if(StringUtils.isNotBlank(Stime)){
			paraMap.put("begin", Stime);
		}
		if(StringUtils.isNotBlank(Etime)){
			paraMap.put("end", Etime);
		}
		if(StringUtils.isNotBlank(dept)){
			paraMap.put("dept", dept);
		}
		if(StringUtils.isNotBlank(drug)){
			paraMap.put("drug", drug);
		}
		List<InventoryLogVo> DrugChecklogsList= namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InventoryLogVo>() {
			@Override
			public InventoryLogVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				InventoryLogVo vo = new InventoryLogVo();
				vo.setCheckCode(rs.getString("checkCode"));
				vo.setDeptName(rs.getString("deptName"));
				vo.setDrugCode(rs.getString("drugCode"));
				vo.setBatchNo(rs.getString("batchNo"));
				vo.setTradeName(rs.getString("tradeName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setRetailPrice(rs.getDouble("retailPrice"));
				vo.setPackUnit(rs.getString("packUnit"));
				vo.setPackQty(rs.getDouble("packQty"));
				vo.setAdjustNum(rs.getDouble("adjustNum"));
				vo.setAdjustUnit(rs.getString("adjustUnit"));
				vo.setPlaceNo(rs.getString("placeNo"));
				vo.setRemark(rs.getString("remark"));
				vo.setCreateuser(rs.getString("createuser"));
				vo.setCreatetime(rs.getTimestamp("createtime"));
				vo.setUserName(rs.getString("userName"));
				return vo;
			}
			
		});
		
		if(DrugChecklogsList!=null&&DrugChecklogsList.size()>0){
			return DrugChecklogsList;
		}
		return new ArrayList<InventoryLogVo>();
	}

	@Override
	public StatVo findMaxMin() throws Exception{
		final String sql = "SELECT MAX(mn.CREATETIME) AS eTime ,MIN(mn.CREATETIME) AS sTime FROM T_DRUG_CHECKLOGS mn";
		StatVo vo = (StatVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public StatVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (StatVo) queryObject.setResultTransformer(Transformers.aliasToBean(StatVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	@Override
	public List<OperationProportionVo> queryOperationProportion(
			List<String> tnL, String firstData, String endData,String page,String rows,String dept) throws Exception{
		firstData+=" 00:00:00";
		endData+=" 23:59:59";
		int p=Integer.parseInt(page);
		int r=Integer.parseInt(rows);
		int s=(p-1)*r+1;
		int e=s+19;
		StringBuffer sb = new StringBuffer();
		sb.append("select E.*  from ( select F.*,rownum as rn from ( select (select t.dept_name from t_department t where t.dept_code=code) as deptName,(select t.DEPT_REGISTERORDER  from t_department t where t.dept_code=code ) num, code as deptCode,total,nvl(total1,0)  total1,nvl(total2,0) total2  from "
				+ " (  select D.*,B.TOTAL2   FROM  ( select s.DEPT_code code, count(1) as total2 from  (select distinct t.dept_code,t.inpatient_no from  (");
		sb.append(" select t.inpatient_no,t.in_state,t.dept_code,t.out_date from t_inpatient_info t  union all select t1.inpatient_no,t1.in_state,t1.dept_code,t1.out_date from t_inpatient_info_now t1 ");
		sb.append(" ) t, T_OPERATION_RECORD r where t.in_state in ('O') and t.inpatient_no = r.clinic_code and r.YNVALID = '1' and "
				+ " t.out_date>to_date('"+firstData+"','yyyy-MM-dd hh24:mi:ss') and  t.out_date<to_date('"+endData+"','yyyy-MM-dd hh24:mi:ss')) s "
				+ "group by s.dept_code) B right join (select  C.*,A.total1 from  ( select s.old_dept_code code, count(1) as total1 from(");
		sb.append(" select t.inpatient_no,t.in_state,t.dept_code,t.out_date from t_inpatient_info t  union all select t1.inpatient_no,t1.in_state,t1.dept_code,t1.out_date from t_inpatient_info_now t1 ");
		sb.append(") t , T_INPATIENT_SHIFTAPPLY s where t.in_state in ('O') and t.inpatient_no = s.inpatient_no and s.shift_state = '2' and "
				+ " t.out_date>to_date('"+firstData+"','yyyy-MM-dd hh24:mi:ss') and  t.out_date<to_date('"+endData+"','yyyy-MM-dd hh24:mi:ss') "
				+ "group by s.old_dept_code) A right join ( select t.DEPT_code code, count(1) as total from (");
		sb.append(" select t.inpatient_no,t.in_state,t.dept_code,t.out_date from t_inpatient_info t  union all select t1.inpatient_no,t1.in_state,t1.dept_code,t1.out_date from t_inpatient_info_now t1 ");
		sb.append(")t where t.in_state in ('O') and  t.out_date>to_date('"+firstData+"','yyyy-MM-dd hh24:mi:ss') and  t.out_date<to_date('"+endData+"',"
				+ "'yyyy-MM-dd hh24:mi:ss') ");
		if(StringUtils.isNotBlank(dept)){
			sb.append(" and t.dept_code in ("+dept+")");
		}
		sb.append(" group by dept_code) C on C.code=A.code ) D  on D.code=B.code) order by num ) F)  E  where  E.rn between  "+s+"  and  "+e);
		String sql=sb.toString();
		List<OperationProportionVo> list = this.getSession().createSQLQuery(sql).addScalar("deptCode").addScalar("total",Hibernate.INTEGER).addScalar("total1",Hibernate.INTEGER)
		.addScalar("total2",Hibernate.INTEGER).setResultTransformer(Transformers.aliasToBean(OperationProportionVo.class)).list();
		return list;
	}
	@Override
	public int queryOperationProportionTotal(List<String> tnL,
			String firstData, String endData,String codeList) throws Exception{
		firstData+=" 00:00:00";
		endData+=" 23:59:59";
		StringBuffer sb = new StringBuffer();
		sb.append("select t.DEPT_code deptcode, count(1) as total from( ");
		sb.append(" select t.inpatient_no,t.in_state,t.dept_code,t.out_date from t_inpatient_info t  union all select t1.inpatient_no,t1.in_state,t1.dept_code,t1.out_date from t_inpatient_info_now t1 ");
		sb.append("  ) t where t.in_state in ('O')  and  t.out_date>to_date('"+firstData+"','yyyy-MM-dd hh24:mi:ss') and "
				+ " t.out_date<to_date('"+endData+"','yyyy-MM-dd hh24:mi:ss') group by dept_code ");
		String sql=sb.toString();
		if(StringUtils.isNotBlank(codeList)){
			sql="select * from  ("+sql+") where deptcode in("+codeList+")";
		}
		List<OperationProportionVo> list = this.getSession().createSQLQuery(sql).addScalar("deptCode").addScalar("total",Hibernate.INTEGER)
		.setResultTransformer(Transformers.aliasToBean(OperationProportionVo.class)).list();
		return list.size();
	}
	@Override
	public List<SysDepartment> queryDeptList() {
		String hql="from SysDepartment where del_flg=0 and stop_flg=0";
		List<SysDepartment> dl=super.find(hql, null);
		if(dl!=null&&dl.size()>0){
			return dl;
		}
		return null;
	}
	@Override
	public List<OperationProportionVo> queryOperationProportionFromDB(String time, List<String> dept, String page, String rows) throws Exception{
		Map map=new HashMap();
		map.put("total", 0);
		map.put("rows", new ArrayList<WordLoadVO>());
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBList mongoDeptList = new BasicDBList();
		List<OperationProportionVo> list=new ArrayList<OperationProportionVo>();
		if(dept.size()>0){
			for(String d:dept){
				mongoDeptList.add(new BasicDBObject("deptCode",d));
			}
			bdObject.put("$or", mongoDeptList);
		}else{
			return list;
		}
		bdObject.append("workdate", time);
//		DBCursor cursor = new MongoBasicDao().findAllDataSortBy("SSZBTJ", "_id", bdObject,Integer.parseInt(rows),Integer.parseInt(page));
		DBCursor cursor = new MongoBasicDao().findAlldata("SSZBTJ", bdObject);
		DBObject dbCursor;
		while(cursor.hasNext()){
			OperationProportionVo voOne=new  OperationProportionVo();
			 dbCursor = cursor.next();
				 Integer total =(Integer)dbCursor.get("total") ;//出院人数
				 Integer total1=(Integer)dbCursor.get("total1");//转出人数
				 Integer total2=(Integer)dbCursor.get("total2");//手术人数
				 String deptName=(String)dbCursor.get("deptName") ;//科室名称
				 String deptCode=(String)dbCursor.get("deptCode") ;//科室编码
				 Double proportion=(Double)dbCursor.get("proportion") ;//手术占比
				 voOne.setDeptName(deptName);
				 voOne.setDeptCode(deptCode);
				 voOne.setTotal(total);
				 voOne.setTotal1(total1);
				 voOne.setTotal2(total2);
				 voOne.setProportion(proportion);
				 list.add(voOne);
		  }
		return list;
	}
	@Override
	public void saveOrUpdateToDB(String firstData, String endData) throws Exception{
		firstData+=" 00:00:00";
		endData+=" 23:59:59";
		StringBuffer sb = new StringBuffer();
		sb.append("select (select t.dept_name from t_department t where t.dept_code=code) as deptName, (select t.DEPT_REGISTERORDER  from t_department t where t.dept_code=code ) num,code as deptCode,total,nvl(total1,0)  total1,nvl(total2,0) total2,rownum as rn from "
				+ " (  select D.*,B.TOTAL2   FROM  ( select s.DEPT_code code, count(1) as total2 from  (select distinct t.dept_code,t.inpatient_no from (");
		sb.append(" select t.inpatient_no,t.in_state,t.dept_code,t.out_date from t_inpatient_info t  union all select t1.inpatient_no,t1.in_state,t1.dept_code,t1.out_date from t_inpatient_info_now t1 ");
		sb.append(" ) t, T_OPERATION_RECORD r where t.in_state in ('O') and t.inpatient_no = r.clinic_code and r.YNVALID = '1' and "
				+ " t.out_date>=to_date('"+firstData+"','yyyy-MM-dd hh24:mi:ss') and  t.out_date<=to_date('"+endData+"','yyyy-MM-dd hh24:mi:ss')) s "
				+ "group by s.dept_code) B right join (select  C.*,A.total1 from  ( select s.old_dept_code code, count(1) as total1 from(");
		sb.append(" select t.inpatient_no,t.in_state,t.dept_code,t.out_date from t_inpatient_info t  union all select t1.inpatient_no,t1.in_state,t1.dept_code,t1.out_date from t_inpatient_info_now t1 ");
		sb.append(") t , T_INPATIENT_SHIFTAPPLY s where t.in_state in ('O') and t.inpatient_no = s.inpatient_no and s.shift_state = '2' and "
				+ " t.out_date>=to_date('"+firstData+"','yyyy-MM-dd hh24:mi:ss') and  t.out_date<=to_date('"+endData+"','yyyy-MM-dd hh24:mi:ss') "
				+ "group by s.old_dept_code) A right join ( select t.DEPT_code code, count(1) as total from (");
		sb.append(" select t.inpatient_no,t.in_state,t.dept_code,t.out_date from t_inpatient_info t  union all select t1.inpatient_no,t1.in_state,t1.dept_code,t1.out_date from t_inpatient_info_now t1 ");
		sb.append(")t where t.in_state in ('O') and  t.out_date>=to_date('"+firstData+"','yyyy-MM-dd hh24:mi:ss') and  t.out_date<=to_date('"+endData+"',"
				+ "'yyyy-MM-dd hh24:mi:ss') ");
		sb.append(" group by dept_code) C on C.code=A.code ) D  on D.code=B.code) order by num");
		String sql=sb.toString();
		
		List<OperationProportionVo> list = this.getSession().createSQLQuery(sql).addScalar("deptCode").addScalar("total",Hibernate.INTEGER).addScalar("total1",Hibernate.INTEGER).addScalar("deptName")
		.addScalar("total2",Hibernate.INTEGER).setResultTransformer(Transformers.aliasToBean(OperationProportionVo.class)).list();
		
		DBObject query = new BasicDBObject();
		query.put("workdate", firstData.substring(0, 7));//移除数据条件
		new MongoBasicDao().remove("SSZBTJ", query);
		
		if(list!=null && list.size()>0){
			for(OperationProportionVo vo:list){
				DecimalFormat df = new DecimalFormat("#.00");
				Document doucment1=new Document();
				doucment1.append("workdate", firstData.substring(0, 7));
				doucment1.append("deptCode", vo.getDeptCode());
				Document document = new Document();
				document.append("total", vo.getTotal());
				document.append("total1", vo.getTotal1());
				document.append("total2", vo.getTotal2());
				document.append("workdate",firstData.substring(0, 7));
				document.append("deptCode", vo.getDeptCode());
				document.append("deptName",vo.getDeptName());
				if(vo.getTotal()==null||vo.getTotal()==0){
					document.append("proportion",-1.0);
				}else{
					document.append("proportion",Double.valueOf(df.format(((double)(vo.getTotal2())/vo.getTotal())*100)));
				}
				new MongoBasicDao().update("SSZBTJ", doucment1, document, true);
			}
			
		}
	}
}
