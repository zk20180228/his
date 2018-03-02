package cn.honry.statistics.deptstat.operationDeptLevel.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.deptstat.illMedicalRecoder.vo.IllMedicalRecoderVo;
import cn.honry.statistics.deptstat.operationDeptLevel.dao.OperationDeptLevelDao;
import cn.honry.statistics.deptstat.operationDeptLevel.vo.OperationDeptLevelVo;
import cn.honry.statistics.deptstat.outPatientMessage.vo.OutPatientMessageVo;
import cn.honry.statistics.deptstat.peopleNumOfOperation.vo.PeopleNumOfOperationVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.ShiroSessionUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("operationDeptLevelDao")
@SuppressWarnings({ "all" })
public class OperationDeptLevelDaoImpl extends HibernateEntityDao<OperationDeptLevelVo> implements OperationDeptLevelDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Autowired
	@Qualifier(value="dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	
	public void setDataJurisInInterDAO(DataJurisInInterDAO dataJurisInInterDAO) {
		this.dataJurisInInterDAO = dataJurisInInterDAO;
	}
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}

	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	/**  
	 * 手术科室手术分级统计信息查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月17日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月17日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public List<OperationDeptLevelVo> queryOperationDeptLevel(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows) {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT * from (");
		sql.append("SELECT  ROWNUM AS n, deptName,levelA,levelB,levelC,levelD,sum,transOut, sumAndTransOut,to_char(percentA,'900.00') percentA,to_char(percentB,'900.00') percentB,to_char(percentC,'900.00') percentC,to_char(percentD,'900.00') percentD FROM( ");
		sql.append("Select deptName As deptName, levelA As levelA, levelB As levelB, levelC As levelC,levelD As levelD, sum As sum,transOut As transOut,"
				+ "sumAndTransOut As sumAndTransOut,percentA As percentA,percentB As percentB,percentC As percentC,percentD As percentD "
				+ "From (select decode(AA.dept_code,'sum','sum',(select pp.DEPT_NAME from t_department pp where pp.DEPT_CODE= AA.dept_code)) deptName,"
				+ "AA.levelA,levelB,levelC,levelD,sum sum,decode(AA.dept_code,'sum',transOut2,transOut)  transOut,"
				+ "decode(AA.dept_code,'sum',(sum+transOut2),(sum+transOut))  sumAndTransOut, "
				+ "decode(AA.dept_code,'sum',round((levelA * 100) / (sum), 2),round((levelA * 100) / (sum), 2))  percentA,"
				+ "decode(AA.dept_code,'sum',round((levelB * 100) / (sum), 2),round((levelB * 100) / (sum), 2))  percentB,"
				+ "decode(AA.dept_code,'sum',round((levelC * 100) / (sum), 2),round((levelC * 100) / (sum), 2))  percentC,"
				+ "decode(AA.dept_code,'sum',round((levelD * 100) / (sum), 2),round((levelD * 100) / (sum), 2))  percentD "
				+ "from (select nvl(dept_code,'sum') dept_code,"
				+ "sum(decode(levell, '一级', 1, 0)) levelA,"
				+ "sum(decode(levell, '二级', 1, 0)) levelB,"
				+ "sum(decode(levell, '三级', 1, 0)) levelC,"
				+ "sum(decode(levell, '四级', 1, 0)) levelD,"
				+ "count(1) sum,"
				+ "(select sum(cg.out_transfer) from t_inpatient_dayreport cg "
				+ "where cg.date_stat>=TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')"
				+ "and cg.date_stat<=TO_DATE('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and cg.dept_code=cc.dept_code ) transOut,"
				+ "(select sum(cg.out_transfer) from t_inpatient_dayreport cg "
				+ "where cg.date_stat>=TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')"
				+ "and cg.date_stat<=TO_DATE('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')  ) transOut2 "
				+ "from (select AA.dept_Code, BB.* "
				+ "from (select cg.inpatient_no, cg.dept_code "
				+ "from t_emr_base cg where cg.out_date >= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') "
				+ "and cg.out_date <= TO_DATE('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')"
				+ "and cg.case_stus in ('3', '4') and cg.operation_code is not null) AA "
				+ "left join (select * from (select BB.*, row_number() over(partition by BB.inpatient_no order by BB.oper_type desc) as row_index "
				+ "from (select p.inpatient_no, p.oper_type, fun_splitstring(p.operation_cnname, '|', 1) name1,"
				+ " fun_splitstring(p.operation_cnname,  '|', 2) levell, p.operation_code, p.FIR_DOCD from t_operation_detail p) BB) AA "
				+ " where AA.row_index = 1) BB on AA.inpatient_no = BB.inpatient_no) cc group by cc.dept_code) AA ");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(deptCode)){
			deptCode=deptCode.replace(",", "','");
			sql.append(" where AA.dept_code in('"+deptCode+"')");
		}else{
			sql.append(" where AA.dept_code in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+")");
		}
		sql.append( ") "); 
		sql.append(") where rownum <= :page * :rows )   where n > (:page - 1) * :rows");
		
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		paraMap.put("page", start);
		paraMap.put("rows", count);
		List<OperationDeptLevelVo> OperationDeptLevelVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<OperationDeptLevelVo>() {
			@Override
			public OperationDeptLevelVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				OperationDeptLevelVo vo = new OperationDeptLevelVo();
				vo.setDeptName(rs.getString("deptName"));
				vo.setLevelA(rs.getString("levelA"));
				vo.setLevelB(rs.getString("levelB"));
				vo.setLevelC(rs.getString("levelC"));
				vo.setLevelD(rs.getString("levelD"));
				vo.setPercentA(rs.getString("percentA"));
				vo.setPercentB(rs.getString("percentB"));
				vo.setPercentC(rs.getString("percentC"));
				vo.setPercentD(rs.getString("percentD"));
				vo.setSum(rs.getString("sum"));
				vo.setTransOut(rs.getString("transOut"));
				vo.setSumAndTransOut(rs.getString("sumAndTransOut"));
				return vo;
			}
			
		});
		if(OperationDeptLevelVoList.size()>0){
			return OperationDeptLevelVoList;
		}
		return new ArrayList<OperationDeptLevelVo>();
	}
	
	/**  
	 * 手术科室手术分级统计信息查询总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月17日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月17日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public int getTotalOperationDeptLevel(String startTime,String endTime,String deptCode,String menuAlias) {
		StringBuffer sql=new StringBuffer();
		sql.append("Select nvl(count(1),0) total From (select decode(AA.dept_code,'sum','sum',(select pp.DEPT_NAME from t_department pp where pp.DEPT_CODE= AA.dept_code)) deptName,"
				+ "AA.levelA,levelB,levelC,levelD,sum sum,decode(AA.dept_code,'sum',transOut2,transOut)  transOut,"
				+ "decode(AA.dept_code,'sum',(sum+transOut2),(sum+transOut))  sumAndTransOut, "
				+ "decode(AA.dept_code,'sum',round((levelA * 100) / (sum), 2),round((levelA * 100) / (sum), 2))  percentA,"
				+ "decode(AA.dept_code,'sum',round((levelB * 100) / (sum), 2),round((levelB * 100) / (sum), 2))  percentB,"
				+ "decode(AA.dept_code,'sum',round((levelC * 100) / (sum), 2),round((levelC * 100) / (sum), 2))  percentC,"
				+ "decode(AA.dept_code,'sum',round((levelD * 100) / (sum), 2),round((levelD * 100) / (sum), 2))  percentD "
				+ "from (select nvl(dept_code,'sum') dept_code,"
				+ "sum(decode(levell, '一级', 1, 0)) levelA,"
				+ "sum(decode(levell, '二级', 1, 0)) levelB,"
				+ "sum(decode(levell, '三级', 1, 0)) levelC,"
				+ "sum(decode(levell, '四级', 1, 0)) levelD,"
				+ "count(1) sum,"
				+ "(select sum(cg.out_transfer) from t_inpatient_dayreport cg "
				+ "where cg.date_stat>=TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')"
				+ "and cg.date_stat<=TO_DATE('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and cg.dept_code=cc.dept_code ) transOut,"
				+ "(select sum(cg.out_transfer) from t_inpatient_dayreport cg "
				+ "where cg.date_stat>=TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')"
				+ "and cg.date_stat<=TO_DATE('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')  ) transOut2 "
				+ "from (select AA.dept_Code, BB.* "
				+ "from (select cg.inpatient_no, cg.dept_code "
				+ "from t_emr_base cg where cg.out_date >= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') "
				+ "and cg.out_date <= TO_DATE('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')"
				+ "and cg.case_stus in ('3', '4') and cg.operation_code is not null) AA "
				+ "left join (select * from (select BB.*, row_number() over(partition by BB.inpatient_no order by BB.oper_type desc) as row_index "
				+ "from (select p.inpatient_no, p.oper_type, fun_splitstring(p.operation_cnname, '|', 1) name1,"
				+ " fun_splitstring(p.operation_cnname,  '|', 2) levell, p.operation_code, p.FIR_DOCD from t_operation_detail p) BB) AA "
				+ " where AA.row_index = 1) BB on AA.inpatient_no = BB.inpatient_no) cc group by cc.dept_code) AA ");
		Map<String,String> map=new HashMap<String,String>();
		if(StringUtils.isNotBlank(deptCode)){
			deptCode=deptCode.replace(",", "','");
			sql.append(" where AA.dept_code in('"+deptCode+"')");
		}else{
			sql.append(" where AA.dept_code in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+")");
		}
		sql.append( ") "); 
		return namedParameterJdbcTemplate.query(sql.toString(),map ,new RowMapper<Integer>(){
			@Override
			public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt("total");
			}
			
		}).get(0);
	}

	@Override
	public List<OperationDeptLevelVo> queryOperationDeptLevelForDB(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows) {
		
		BasicDBObject bdObject = new BasicDBObject();
		List<OperationDeptLevelVo> list=new ArrayList<OperationDeptLevelVo>();
		if(StringUtils.isNotBlank(deptCode)){
			BasicDBList deptList=new BasicDBList();
			String[] deptArr=deptCode.split(",");
			for(int i=0,len=deptArr.length;i<len;i++){
				deptList.add(new BasicDBObject("deptCode", deptArr[i]));
			}
			bdObject.append("$or", deptList);
		}
		DBCursor cursor = new MongoBasicDao().findAlldata("SSKSSSFJTJ", bdObject);
		DBObject dbCursor;
		while(cursor.hasNext()){
			OperationDeptLevelVo vo=new  OperationDeptLevelVo();
			 dbCursor = cursor.next();
			 vo.setDeptName((String)dbCursor.get("deptName")); 
			 vo.setLevelA((String)dbCursor.get("levelA")); 
			 vo.setLevelB((String)dbCursor.get("levelB")); 
			 vo.setLevelC((String)dbCursor.get("levelC")); 
			 vo.setLevelD((String)dbCursor.get("levelD")); 
			 vo.setPercentA((String)dbCursor.get("percentA")); 
			 vo.setPercentB((String)dbCursor.get("percentB")); 
			 vo.setPercentC((String)dbCursor.get("percentC")); 
			 vo.setPercentD((String)dbCursor.get("percentD")); 
			 vo.setSum((String)dbCursor.get("sum")); 
			 vo.setTransOut((String)dbCursor.get("transOut")); 
			 vo.setSumAndTransOut((String)dbCursor.get("sumAndTransOut")); 
			list.add(vo);
			}
		return list;
	}

	@Override
	public Map<String,Object> queryOperationDeptLevelMong(String startTime, String endTime, String deptCode,
			String menuAlias, String page, String rows) {
		Map<String,Object> map=new HashMap<String, Object>();
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		
		BasicDBList mongoDeptList = new BasicDBList();
		
		bdObjectTimeS.put("deptDate",new BasicDBObject("$gte",startTime));
		condList.add(bdObjectTimeS);
		bdObjectTimeE.put("deptDate",new BasicDBObject("$lte",endTime));
		condList.add(bdObjectTimeE);
		bdObject.put("$and", condList);
		if(StringUtils.isBlank(deptCode)){//如果科室为空 查询授权科室
			String jobNo=ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
			List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,jobNo);
				for(int i = 0,len=deptList.size();i<len;i++){
					mongoDeptList.add(new BasicDBObject("deptName",deptList.get(i).getDeptName()));
				}
				bdObject.put("$or", mongoDeptList);
		}else{
			String[] dept= deptCode.split(",");
			for(String dep:dept){
				mongoDeptList.add(new BasicDBObject("deptName",dep));
			}
			bdObject.put("$or", mongoDeptList);
		}
		DBCursor cursor = new MongoBasicDao().findAlldataBySort("SSKSSSFJTJ_DAY", bdObject,"deptName");
		
		
		DBObject dbCursor;
		String key;//主键
		String dateSign=null;//时间key
		
		List<Integer> value;//值
		List<Integer> valueVo;//
		List<Integer> tempValue;//中间list
		Double totalPatient;
		Map<String,List<Integer>> valueMap=new LinkedHashMap<String,List<Integer>>();
		while(cursor.hasNext()){
				 	dbCursor = cursor.next();
				 	
				 	valueVo=new ArrayList<Integer>(7);
					 String deptName = (String) dbCursor.get("deptName") ;//科室
					 String finalDate=(String) dbCursor.get("finalDate") ;//时间
					 
					 valueVo.add(Integer.parseInt((String)dbCursor.get("levelA"))) ;//一级
					 valueVo.add(Integer.parseInt((String)dbCursor.get("levelB"))) ;//二级
					 valueVo.add(Integer.parseInt((String)dbCursor.get("levelC"))) ;//三级
					 valueVo.add(Integer.parseInt((String)dbCursor.get("levelD")));//四级
					 
					 valueVo.add(Integer.parseInt((String)dbCursor.get("sumLevel")));//手术总人数
					 valueVo.add(Integer.parseInt((String)dbCursor.get("transOut"))) ;//装出
					 valueVo.add(Integer.parseInt((String)dbCursor.get("sumAndTransOut")));//合计
					 key=deptName;
					 if(valueMap.containsKey(key)){
						 tempValue=new ArrayList<Integer>(7);
						 value=valueMap.get(key);
						 for(int i=0,len=value.size();i<len;i++){
							tempValue.add(value.get(i)+valueVo.get(i)); 
						 }
						 valueMap.put(key, tempValue);
						 tempValue=null;
					 }else{
						 valueMap.put(key, valueVo);
						 valueVo=null;
					 }
					 
			}
		value=null;
		valueVo=null;
		
		int total=0;
		Integer start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
		Integer end=Integer.parseInt(page)*Integer.parseInt(rows);
		Integer sumLevl=1;//手术总人数
		List<OperationDeptLevelVo> list=new ArrayList<OperationDeptLevelVo>();
		for(String keys:valueMap.keySet()){
			if(total>=start&&total<end){
				tempValue=valueMap.get(keys);
				OperationDeptLevelVo vo=new OperationDeptLevelVo();
				if(tempValue.get(4)!=null&&tempValue.get(4)!=0){
					sumLevl=tempValue.get(4);
					vo.setSum(sumLevl.toString());
				}else{
					vo.setSum("0");
				}
				vo.setDeptName(keys);
				vo.setLevelA(tempValue.get(0).toString());
				vo.setLevelB(tempValue.get(1).toString());
				vo.setLevelC(tempValue.get(2).toString());
				vo.setLevelD(tempValue.get(3).toString());
				
				vo.setPercentA(NumberUtil.init().format(tempValue.get(0)*100/sumLevl,2));
				vo.setPercentB(NumberUtil.init().format(tempValue.get(1)*100/sumLevl,2));
				vo.setPercentC(NumberUtil.init().format(tempValue.get(2)*100/sumLevl,2));
				vo.setPercentD(NumberUtil.init().format(tempValue.get(3)*100/sumLevl,2));
				vo.setTransOut(tempValue.get(5).toString());
				vo.setSumAndTransOut(tempValue.get(6).toString());
				list.add(vo);
				if(total+1==end){
					break;
				}
			}
			total++;
		}
		map.put("total", valueMap.size());
		map.put("rows", list);
		return map;
	}
}
