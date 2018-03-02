package cn.honry.statistics.bi.bistac.operationIncome.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.bi.bistac.operationIncome.dao.OperationIncomeDao;
import cn.honry.statistics.bi.bistac.operationIncome.vo.OperationIncomeVo;
import cn.honry.statistics.bi.bistac.operationNum.vo.OperationNumsVo;
import cn.honry.utils.NumberUtil;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("operationIncomeDao")
@SuppressWarnings({ "all" })
public class OperationIncomeDaoImpl extends HibernateEntityDao<OperationIncomeVo> implements OperationIncomeDao{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private MongoBasicDao mbDao =null;
	SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfMonth=new SimpleDateFormat("yyyy-MM");
	

	/**  
	 * 
	 * 初始化手术收入统计（门诊+住院）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void saveOperationIncomeToDB(String startDate,String endDate, List<String> itemtnL, List<String> feetnL,int flg) {
			mbDao = new MongoBasicDao();
			StringBuffer buffer=new StringBuffer(2000);
			buffer.append("select sum(nvl(outInSum1,0)) as outInSum , finalDate,classType from( ");
			for(int i=0;i<feetnL.size();i++){
				if(i>0){
					buffer.append(" Union All ");
				}
				buffer.append("select  t"+i+".tot_cost outInSum1,to_char(t"+i+".fee_date,'yyyy-mm-dd') as finalDate ,'MZ01' AS classType ");
				buffer.append("from "+feetnL.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code and c.report_code='MZ01' and c.fee_stat_code = '10'");
				buffer.append("where t"+i+".stop_flg = 0 and t"+i+".del_flg = 0 and t"+i+".trans_type = 1 and t"+i+".pay_flag = 1 and t"+i+".fee_date between to_date('"+startDate+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+endDate+"','yyyy-MM-dd HH24:MI:SS') ");
			}
			if(itemtnL!=null&&feetnL!=null&&itemtnL.size()>0&&feetnL.size()>0){
				buffer.append(" union All ");
			}
			for(int i=0;i<itemtnL.size();i++){
				if(i>0){
					buffer.append(" Union All ");
				}
				buffer.append(" select n"+i+".tot_cost as outInSum1,to_char(n"+i+".fee_date,'yyyy-mm-dd') as finalDate,'ZY01' AS classType ");
				buffer.append("from "+itemtnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code and c.report_code='ZY01' and c.fee_stat_code = '10'");
				buffer.append("where n"+i+".stop_flg = 0 and n"+i+".del_flg = 0 and n"+i+".trans_type = 1 and  n"+i+".SEND_FLAG = 1 and n"+i+".SEND_FLAG = 1 and  n"+i+".fee_date between to_date('"+startDate+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+endDate+"','yyyy-MM-dd HH24:MI:SS') ");
			}
			buffer.append(") group by finalDate,classType ");
			List<OperationIncomeVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<OperationIncomeVo>() {
				@Override
				public OperationIncomeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					OperationIncomeVo vo = new OperationIncomeVo();
					vo.setOutInSum(rs.getDouble("outInSum"));
					vo.setFinalDate(rs.getString("finalDate"));
					vo.setClassType(rs.getString("classType"));
					return vo;
				}
			});
	 		if(list!=null && list.size()>0){
				for(OperationIncomeVo vo:list){
					 double mzNums=0;
					 if(flg==1){
						 BasicDBObject bdObject = new BasicDBObject();
						 bdObject.append("finalDate",vo.getFinalDate());
						 bdObject.append("classType", vo.getClassType());
						 DBCursor cursor = mbDao.findAlldata("OPERATIONINCOMEMORZNEW", bdObject);
						 DBObject dbCursor;
							while(cursor.hasNext()){
								 dbCursor = cursor.next();
								 mzNums =(Double)dbCursor.get("outInSum");//金额
							}
					 }	
					BasicDBObject bdObject1 = new BasicDBObject();
					Document document1 = new Document();
					document1.append("finalDate", vo.getFinalDate());
					document1.append("classType", vo.getClassType());//门诊住院区分标记
					Document document = new Document();
					if(vo.getOutInSum()!=null){
						document.append("finalDate", vo.getFinalDate());//日期
						document.append("classType", vo.getClassType());//门诊住院区分标记
						document.append("outInSum", vo.getOutInSum()+mzNums);//门诊、住院金额
						mbDao.update("OPERATIONINCOMEMORZNEW", document1, document, true);
					}
				}
			}
	}

	/**  
	 * 
	 * 初始化手术收入统计（手术类别）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void saveOperationOpTypeToDB(String startDate,String endDate, List<String> itemtnL, List<String> feetnL,int flg) {
			mbDao = new MongoBasicDao();
			StringBuffer buffer=new StringBuffer();
			buffer.append("select encode as operationTypeCode  ,name as operationTypeName,finalDate ,sum(nvl(outInSum1,0)) as outInSum  from ( ");
			for(int i=0;i<feetnL.size();i++){
				if(i>0){
					buffer.append(" Union All ");
				}
				buffer.append(" select t.code_encode as encode,t.code_name as name, t"+i+".tot_cost outInSum1,to_char(t"+i+".fee_date,'yyyy-MM-dd') as finalDate ");
				buffer.append(" from t_business_dictionary t left join  T_OPERATION_APPLY app on t.code_encode = app.op_type  and app.stop_flg = 0 and app.del_flg = 0 ");
				buffer.append(" left join  "+feetnL.get(i)+" t"+i+" on t"+i+".clinic_code = app.clinic_code  and t"+i+".pay_flag = '1' and t"+i+".trans_type = 1  and t"+i+".del_flg = 0 and t"+i+".stop_flg = 0");
				buffer.append("  and  t"+i+".fee_date between to_date('"+startDate+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+endDate+"','yyyy-MM-dd HH24:MI:SS') ");
				buffer.append(" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code and c.report_code='MZ01' and c.fee_stat_code = '10' ");
				buffer.append(" where t.code_type = 'operatetype' ");
			}
			if(itemtnL!=null&&feetnL!=null&&itemtnL.size()>0&&feetnL.size()>0){
				buffer.append(" union All ");
			}
			for(int i=0;i<itemtnL.size();i++){
				if(i>0){
					buffer.append(" Union All ");
				}
				buffer.append(" select t.code_encode as encode,t.code_name as name, n"+i+".tot_cost as outInSum1,to_char(n"+i+".fee_date,'yyyy-MM-dd') as finalDate ");
				buffer.append(" from t_business_dictionary t left join  T_OPERATION_APPLY app on t.code_encode = app.op_type and app.stop_flg = 0 and app.del_flg = 0 ");
				buffer.append(" left join  "+itemtnL.get(i)+" n"+i+" on  n"+i+".inpatient_no = app.clinic_code  and  n"+i+".del_flg=0 and  n"+i+".stop_flg=0 and  n"+i+".send_flag=1 and  n"+i+".trans_type = 1 ");
				buffer.append(" and n"+i+".fee_date between to_date('"+startDate+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+endDate+"','yyyy-MM-dd HH24:MI:SS') ");
				buffer.append(" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code and c.report_code='ZY01' and c.fee_stat_code = '10' ");
				buffer.append(" where t.code_type = 'operatetype' ");
			}
			buffer.append(") group by encode,name,finalDate ");
			List<OperationIncomeVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<OperationIncomeVo>() {
				@Override
				public OperationIncomeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					OperationIncomeVo vo = new OperationIncomeVo();
					vo.setOperationTypeCode(rs.getString("operationTypeCode"));
					vo.setOperationTypeName(rs.getString("operationTypeName"));
					vo.setOutInSum(rs.getDouble("outInSum"));
					vo.setFinalDate(rs.getString("finalDate"));
					
					return vo;
				}
			});
	 		if(list!=null && list.size()>0){
				for(OperationIncomeVo vo:list){
					double sNums=0;
					if(flg==1){
						 BasicDBObject bdObject = new BasicDBObject();
						 bdObject.append("finalDate", vo.getFinalDate());
						 bdObject.append("operationTypeCode", vo.getOperationTypeCode());
						 DBCursor cursor = mbDao.findAlldata("OPERINCOMEOPTYPENEW", bdObject);
						 DBObject dbCursor;
							List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
							while(cursor.hasNext()){
								OperationNumsVo voOne=new  OperationNumsVo();
								dbCursor = cursor.next();
								sNums =(Double)dbCursor.get("outInSum");//金额
							}
					 }	
					BasicDBObject bdObject1 = new BasicDBObject();
					Document document1 = new Document();
					document1.append("finalDate", vo.getFinalDate());
					document1.append("operationTypeCode", vo.getOperationTypeCode());//手术类别code
					document1.append("operationTypeName", vo.getOperationTypeName());//手术类别name
					Document document = new Document();
					if(vo.getOutInSum()!=null){
						document.append("outInSum", vo.getOutInSum()+sNums);//门诊、住院金额
						document.append("finalDate", vo.getFinalDate());//日期
						document.append("operationTypeCode", vo.getOperationTypeCode());//手术类别code
						document.append("operationTypeName", vo.getOperationTypeName());//手术类别name
						mbDao.update("OPERINCOMEOPTYPENEW", document1, document, true);
					}
					
					
				}
	 		}
	}

	/**  
	 * 
	 * 初始化手术类别
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void saveOperOpTypeToDB() {
		mbDao = new MongoBasicDao();
		StringBuffer buffer=new StringBuffer();
		buffer.append("select t.code_encode,t.code_name,t.code_type from t_business_dictionary t  where t.code_type = 'operatetype'  and t.del_flg=0 and t.stop_flg=0 ");
		List<BusinessDictionary> list =  namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<BusinessDictionary>() {
			@Override
			public BusinessDictionary mapRow(ResultSet rs, int rowNum) throws SQLException {
				BusinessDictionary vo = new BusinessDictionary();
				vo.setEncode(rs.getString("code_encode"));
				vo.setName(rs.getString("code_name"));
				vo.setType(rs.getString("code_type"));
				return vo;
			}
		});
		 if(list!=null && list.size()>0){
			 new MongoBasicDao().deleteData("OPERATIONTYPE");//删除原来的数据
			 List<DBObject> numsList = new ArrayList<DBObject>();
			 for(BusinessDictionary vo:list){
				 if(StringUtils.isNotBlank(vo.getName())){
						BasicDBObject obj = new BasicDBObject();
						obj.append("encode", vo.getEncode());
						obj.append("name", vo.getName());
						obj.append("type", vo.getType());
						numsList.add(obj);
					}
			}
			new MongoBasicDao().insertDataByList("OPERATIONTYPE", numsList);		
		 }
	}

	/**  
	 * 
	 * 初始化手术收入统计（科室前5）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void saveOperTopFiveDeptToDB(String startDate,String endDate, List<String> itemtnL, List<String> feetnL,int flg) {
			 StringBuffer buffer=new StringBuffer();
			 mbDao = new MongoBasicDao();
			 buffer.append("select * from( ");
			 buffer.append(" select  f.finalDate as finalDate,f.totCost as outInSum,(select e.dept_name from t_department e where e.dept_code=f.execDept  ) as execDept, f.execDept AS deptCode,row_number()over(partition by f.finalDate order by f.totCost desc ) as rn from( ");
			 buffer.append(" select sum(nvl(tot_cost, 0)) as totCost, execDept,  finalDate  from (");
			 for(int i=0;i<itemtnL.size();i++){
					if(i>0){
						buffer.append(" Union All ");
					}
					 buffer.append("select t"+i+".tot_cost, t"+i+".execute_deptcode as execDept,to_char(t"+i+".fee_date,'yyyy-MM-dd') as finalDate  from  "+itemtnL.get(i)+" t"+i);
					 buffer.append(" join t_charge_minfeetostat c on c.minfee_code = t"+i+".fee_code and c.report_code = 'ZY01'  and c.fee_stat_code = '10'  where t"+i+".del_flg = 0 and t"+i+".stop_flg = 0  and t"+i+".send_flag = 1  and t"+i+".trans_type = 1 ");
					 buffer.append(" and t"+i+".fee_date between to_date('"+startDate+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+endDate+"','yyyy-MM-dd HH24:MI:SS') ");
				}
				if(feetnL!=null&&itemtnL!=null&&feetnL.size()>0&&itemtnL.size()>0){
					buffer.append(" union All ");
				}
				for(int i=0;i<feetnL.size();i++){
					if(i>0){
						buffer.append(" Union All ");
					}
					 buffer.append(" select n"+i+".tot_cost,n"+i+".exec_dpcd as execDept,to_char(n"+i+".fee_date,'yyyy-MM-dd')  as finalDate  from "+feetnL.get(i)+" n"+i+" join t_charge_minfeetostat c   on c.minfee_code = n"+i+".fee_code");
					 buffer.append(" and c.report_code = 'MZ01' and c.fee_stat_code = '10' where n"+i+".del_flg = 0 and n"+i+".stop_flg = 0  and n"+i+".pay_flag = 1 and n"+i+".trans_type = 1");
					 buffer.append(" and n"+i+".fee_date between to_date('"+startDate+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+endDate+"','yyyy-MM-dd HH24:MI:SS') ");
				}
			 buffer.append("  )group by execDept, finalDate ) f)e ");
			 List<OperationIncomeVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<OperationIncomeVo>() {
					@Override
					public OperationIncomeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
						OperationIncomeVo vo = new OperationIncomeVo();
						vo.setDeptCode(rs.getString("deptCode"));
						vo.setExecDept(rs.getString("execDept"));
						vo.setFinalDate(rs.getString("finalDate"));
						vo.setOutInSum(rs.getDouble("outInSum"));
						return vo;
					}
				});
			 
			 if(list!=null&&list.size()>0){
					for(OperationIncomeVo vo:list){
						double sNums=0;
						if(flg==1){
							 BasicDBObject bdObject = new BasicDBObject();
							 bdObject.append("finalDate", vo.getFinalDate());
							 bdObject.append("execDept", vo.getExecDept());
							 bdObject.append("deptCode", vo.getDeptCode());
							 DBCursor cursor = mbDao.findAlldata("OPERINCOMETOPFIVEDEPTNEW", bdObject);
							 DBObject dbCursor;
								List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
								while(cursor.hasNext()){
									OperationNumsVo voOne=new  OperationNumsVo();
									dbCursor = cursor.next();
									sNums =(Double)dbCursor.get("outInSum");//金额
								}
						 }	
						Document document1 = new Document();
						document1.append("deptCode", vo.getDeptCode());//科室
						document1.append("execDept", vo.getExecDept());//科室
						document1.append("finalDate", vo.getFinalDate());//药品统计时间
						Document document = new Document();
						if(vo.getOutInSum()!=null){
							document.append("outInSum", vo.getOutInSum()+sNums);//金额
							document.append("execDept", vo.getExecDept());//科室
							document.append("deptCode", vo.getDeptCode());//科室
							document.append("finalDate", vo.getFinalDate());//药品统计时间
							mbDao.update("OPERINCOMETOPFIVEDEPTNEW", document1, document, true);
						}
					}
			}
	}

	/**  
	 * 
	 * 初始化手术收入统计（医生前5）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void saveOperTopFiveDocToDB(String startDate,String endDate, List<String> itemtnL, List<String> feetnL,int flg) {
			StringBuffer buffer=new StringBuffer();
			mbDao = new MongoBasicDao();
			 buffer.append("select * from( ");
			 buffer.append(" select  f.finalDate as finalDate,f.totCost as outInSum,(select e.employee_name from t_employee e where e.employee_jobno=f.docCode  ) as docName,f.docCode AS docCode,row_number()over(partition by f.finalDate order by f.totCost desc ) as rn from( ");
			 buffer.append(" select sum(nvl(tot_cost, 0)) as totCost, docCode,  finalDate  from (");
			 for(int i=0;i<itemtnL.size();i++){
					if(i>0){
						buffer.append(" Union All ");
					}
					 buffer.append("select t"+i+".tot_cost, t"+i+".RECIPE_DOCCODE as docCode,to_char(t"+i+".fee_date,'yyyy-MM-dd') as finalDate  from  "+itemtnL.get(i)+" t"+i);
					 buffer.append(" join t_charge_minfeetostat c on c.minfee_code = t"+i+".fee_code and c.report_code = 'ZY01'  and c.fee_stat_code = '10'  where t"+i+".del_flg = 0 and t"+i+".stop_flg = 0  and t"+i+".send_flag = 1  and t"+i+".trans_type = 1 ");
					 buffer.append(" and t"+i+".fee_date between to_date('"+startDate+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+endDate+"','yyyy-MM-dd HH24:MI:SS') ");
				}
				if(feetnL!=null&&itemtnL!=null&&feetnL.size()>0&&itemtnL.size()>0){
					buffer.append(" union All ");
				}
				for(int i=0;i<feetnL.size();i++){
					if(i>0){
						buffer.append(" Union All ");
					}
					 buffer.append(" select n"+i+".tot_cost,n"+i+".DOCT_CODE as docCode,to_char(n"+i+".fee_date,'yyyy-MM-dd')  as finalDate  from "+feetnL.get(i)+" n"+i+" join t_charge_minfeetostat c   on c.minfee_code = n"+i+".fee_code");
					 buffer.append(" and c.report_code = 'MZ01' and c.fee_stat_code = '10' where n"+i+".del_flg = 0 and n"+i+".stop_flg = 0  and n"+i+".pay_flag = 1 and n"+i+".trans_type = 1");
					 buffer.append(" and n"+i+".fee_date between to_date('"+startDate+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+endDate+"','yyyy-MM-dd HH24:MI:SS') ");
				}
				buffer.append("  )group by docCode, finalDate ) f)e ");
			 List<OperationIncomeVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<OperationIncomeVo>() {
					@Override
					public OperationIncomeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
						OperationIncomeVo vo = new OperationIncomeVo();
						vo.setDocName(rs.getString("docName"));
						vo.setDocCode(rs.getString("docCode"));
						vo.setFinalDate(rs.getString("finalDate"));
						vo.setOutInSum(rs.getDouble("outInSum"));
						return vo;
					}
				});
			 
			 if(list!=null && list.size()>0){
					for(OperationIncomeVo vo:list){
						double sNums=0;
						if(flg==1){
							 BasicDBObject bdObject = new BasicDBObject();
							 bdObject.append("finalDate", vo.getFinalDate());
							 bdObject.append("docName", vo.getDocName());
							 bdObject.append("docCode", vo.getDocCode());
							 DBCursor cursor = mbDao.findAlldata("OPERINCOMETOPFIVEDOCNEW", bdObject);
							 DBObject dbCursor;
								List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
								while(cursor.hasNext()){
									OperationNumsVo voOne=new  OperationNumsVo();
									dbCursor = cursor.next();
									sNums =(Double)dbCursor.get("outInSum");//金额
								}
						 }	
						Document document1 = new Document();
						document1.append("docName", vo.getDocName());//医生
						document1.append("docCode", vo.getDocCode());//医生
						document1.append("finalDate", vo.getFinalDate());//药品统计时间
						Document document = new Document();
						if(vo.getOutInSum()!=null){
							document.append("outInSum", vo.getOutInSum()+sNums);//金额
							document.append("docName",  vo.getDocName());//医生
							document.append("docCode", vo.getDocCode());//医生
							document.append("finalDate", vo.getFinalDate());//药品统计时间
							mbDao.update("OPERINCOMETOPFIVEDOCNEW", document1, document, true);
						}
						
						
					}
			}
		
	}

	/**  
	 * 
	 * 初始化同环比
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void saveOperYoyRatioToDB(String startDate,String endDate, List<String> itemtnL, List<String> feetnL,int flg) {
			StringBuffer buffer=new StringBuffer();
			mbDao = new MongoBasicDao();
			buffer.append("select sum(nvl(outInSum1,0)) as outInSum , finalDate from( ");
			for(int i=0;i<feetnL.size();i++){
				if(i>0){
					buffer.append(" Union All ");
				}
				buffer.append("select  t"+i+".tot_cost outInSum1,to_char(t"+i+".fee_date,'yyyy-MM-dd') as finalDate ");
				buffer.append("from "+feetnL.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code and c.report_code='MZ01' and c.fee_stat_code = '10'");
				buffer.append("where t"+i+".stop_flg = 0 and t"+i+".del_flg = 0 and t"+i+".trans_type = 1 and t"+i+".pay_flag = 1 and  t"+i+".fee_date between to_date('"+startDate+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+endDate+"','yyyy-MM-dd HH24:MI:SS') ");
			}
			if(itemtnL!=null&&feetnL!=null&&itemtnL.size()>0&&feetnL.size()>0){
				buffer.append(" union All ");
			}
			for(int i=0;i<itemtnL.size();i++){
				if(i>0){
					buffer.append(" Union All ");
				}
				buffer.append(" select n"+i+".tot_cost as outInSum1,to_char(n"+i+".fee_date,'yyyy-MM-dd') as finalDate  ");
				buffer.append("from "+itemtnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code and c.report_code='ZY01' and c.fee_stat_code = '10'");
				buffer.append("where n"+i+".stop_flg = 0 and n"+i+".del_flg = 0 and n"+i+".trans_type = 1 and n"+i+".SEND_FLAG = 1 and  n"+i+".fee_date between to_date('"+startDate+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+endDate+"','yyyy-MM-dd HH24:MI:SS') ");
			}
			buffer.append(") group by finalDate ");
			List<OperationIncomeVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<OperationIncomeVo>() {
				@Override
				public OperationIncomeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					OperationIncomeVo vo = new OperationIncomeVo();
					vo.setFinalDate(rs.getString("finalDate"));
					vo.setOutInSum(rs.getDouble("outInSum"));
					return vo;
				}
			});
			 if(list!=null && list.size()>0){
				 double nums=0;
				 if(flg==1){
					 BasicDBObject bdObject = new BasicDBObject();
					 bdObject.append("finalDate", list.get(0).getFinalDate());
					 DBCursor cursor = mbDao.findAlldata("OPERINCOMESUMNEW", bdObject);
					 DBObject dbCursor;
						List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
						while(cursor.hasNext()){
							OperationNumsVo voOne=new  OperationNumsVo();
							 dbCursor = cursor.next();
							 nums =(Double)dbCursor.get("outInSum");//例数
						}
				 }	
				for(OperationIncomeVo vo:list){
					Document document1 = new Document();
					document1.append("finalDate", vo.getFinalDate());//药品统计时间
					Document document = new Document();
					if(vo.getOutInSum()!=null){
						document.append("outInSum", vo.getOutInSum()+nums);//金额
						document.append("finalDate", vo.getFinalDate());//药品统计时间
						mbDao.update("OPERINCOMESUMNEW", document1, document, true);
					}
				}
			}
	}

	/**
	 * 初始化手术收入统计（门诊+住院）按月份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public void initOperationIncomeMonth(String startDate, String endDate) {
		mbDao = new MongoBasicDao();
		List<String> list=reMonthDay(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
			String temp;//月数据
			Double dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,7);
				DBCursor cursor = mbDao.findAlldata("OPERATIONINCOMEMORZNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Double value = (Double) dbCursor.get("outInSum") ;//金额
					 String name = (String) dbCursor.get("classType");//类别
					 temp1=temp+"&"+name;
					 if(map.containsKey(temp1)){//如果key存在 比较name
							 dou=map.get(temp1);
							 dou+=value;
							 map.put(temp1,dou);
					 }else{//如果key不存在   添加到map1中
						 map.put(temp1, value);
					 }

				}
			}
			String [] newList=new String[2];
			for(String key:map.keySet()){
				newList=key.split("&");
				if(newList.length==2){
					Document document1 = new Document();
					document1.append("finalDate", newList[0]);
					document1.append("classType", newList[1]);//类别  门诊住院
					Document document = new Document();
					document.append("outInSum",map.get(key));//金额
					document.append("finalDate", newList[0]);//时间
					document.append("classType", newList[1]);//类别  门诊住院
					mbDao.update("OPERATIONINCOMEMORZMONTHNEW", document1, document, true);
				}
			}
		}
		
	}

	/**
	 * 初始化手术收入统计（门诊+住院）按年份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public void initOperationIncomeYear(String startDate, String endDate) {
		mbDao = new MongoBasicDao();
		List<String> list=reYearMonth(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
			String temp;//月数据
			Double dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,4);
				DBCursor cursor = mbDao.findAlldata("OPERATIONINCOMEMORZMONTHNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Double value = (Double) dbCursor.get("outInSum") ;//金额
					 String name = (String) dbCursor.get("classType");//类别
					 temp1=temp+"&"+name;
					 if(map.containsKey(temp1)){//如果key存在 比较name
							 dou=map.get(temp1);
							 dou+=value;
							 map.put(temp1,dou);
					 }else{//如果key不存在   添加到map1中
						 map.put(temp1, value);
					 }

				}
			}
			String [] newList=new String[2];
			for(String key:map.keySet()){
				newList=key.split("&");
				if(newList.length==2){
					Document document1 = new Document();
					document1.append("finalDate", newList[0]);
					document1.append("classType", newList[1]);//类别  门诊住院
					Document document = new Document();
					document.append("outInSum",map.get(key));//金额
					document.append("finalDate", newList[0]);//时间
					document.append("classType", newList[1]);//类别  门诊住院
					mbDao.update("OPERATIONINCOMEMORZYEARNEW", document1, document, true);
				}
			}
		}
		
	}

	public List<String> reMonthDay(String begin,String end,List<String> list){
		 if(begin!=null){
			 Date date;
			 Date endTime;
			try {
				 date = sd.parse(begin);
				 endTime=sd.parse(end);
				 begin=sdf.format(date);
				 String[] dateArr=begin.split("-");
				 Calendar ca=Calendar.getInstance();
				 ca.set(Integer.parseInt(dateArr[0]),Integer.parseInt(dateArr[1])-1,Integer.parseInt(dateArr[2]));
				if(date.getTime()>=endTime.getTime()){
					return list;
				}else{
					begin=sdf.format(ca.getTime());
					ca.add(Calendar.DATE, 1);
					list.add(begin);
					begin=sd.format(ca.getTime());
					return reMonthDay(begin,end,list);
				}
				
			} catch (ParseException e) {
				return list;
			}
		 }else{
			 return new ArrayList<String>();
		 }
	}
	public List<String> reYearMonth(String begin,String end,List<String> list){
		 if(begin!=null){
			 Date date;
			 Date endTime;
			try {
				 date = sd.parse(begin);
				 endTime=sd.parse(end);
				 begin=sdfMonth.format(date);//
				 String[] dateArr=begin.split("-");
				 Calendar ca=Calendar.getInstance();
				 ca.set(Calendar.YEAR, Integer.parseInt(dateArr[0]));
				 ca.set(Calendar.MONTH,Integer.parseInt(dateArr[1])-1);
				if(date.getTime()>=endTime.getTime()){
					return list;
				}else{
					begin=sdfMonth.format(ca.getTime());
					ca.add(Calendar.MONTH, 1);
					list.add(begin);
					begin=sd.format(ca.getTime());
					return reYearMonth(begin,end,list);
				}
			}catch (ParseException e) {
				return list;
			}
		 }else{
			 return new ArrayList<String>();
		 }
	}

	/**
	 * 初始化手术收入统计（手术类别）按月份份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public void initOperIncomeTypeMonth(String startDate, String endDate) {
		mbDao = new MongoBasicDao();
		List<String> list=reMonthDay(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
			String temp;//月数据
			Double dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,7);
				DBCursor cursor = mbDao.findAlldata("OPERINCOMEOPTYPENEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Double value = (Double) dbCursor.get("outInSum") ;//金额
					 String name = (String) dbCursor.get("operationTypeName");//类别
					 String typeCode = (String) dbCursor.get("operationTypeCode");//类别code
					 temp1=temp+"&"+name+"&"+typeCode;
					 if(map.containsKey(temp1)){//如果key存在 比较name
							 dou=map.get(temp1);
							 dou+=value;
							 map.put(temp1,dou);
					 }else{//如果key不存在   添加到map1中
						 map.put(temp1, value);
					 }

				}
			}
			String [] newList=new String[3];
			for(String key:map.keySet()){
				newList=key.split("&");
				if(newList.length==3){
					Document document1 = new Document();
					document1.append("finalDate", newList[0]);
					document1.append("operationTypeName", newList[1]);//手术类别  
					document1.append("operationTypeCode", newList[2]);//手术类别code
					Document document = new Document();
					document.append("outInSum",map.get(key));//金额
					document.append("finalDate", newList[0]);//时间
					document.append("operationTypeName", newList[1]);//手术类别  
					document.append("operationTypeCode", newList[2]);//手术
					mbDao.update("OPERINCOMEOPTYPEMONTHNEW", document1, document, true);
				}
			}
		}
		
	}

	/**
	 * 初始化手术收入统计（手术类别）按年份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public void initOperIncomeTypeYear(String startDate, String endDate) {
		mbDao = new MongoBasicDao();
		List<String> list=reYearMonth(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
			String temp;//月数据
			Double dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,4);
				DBCursor cursor = mbDao.findAlldata("OPERINCOMEOPTYPEMONTHNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Double value = (Double) dbCursor.get("outInSum") ;//金额
					 String name = (String) dbCursor.get("operationTypeName");//类别
					 String typeCode = (String) dbCursor.get("operationTypeCode");//类别code
					 temp1=temp+"&"+name+"&"+typeCode;
					 if(map.containsKey(temp1)){//如果key存在 比较name
							 dou=map.get(temp1);
							 dou+=value;
							 map.put(temp1,dou);
					 }else{//如果key不存在   添加到map1中
						 map.put(temp1, value);
					 }

				}
			}
			String [] newList=new String[3];
			for(String key:map.keySet()){
				newList=key.split("&");
				if(newList.length==3){
					Document document1 = new Document();
					document1.append("finalDate", newList[0]);
					document1.append("operationTypeName", newList[1]);//手术类别  
					document1.append("operationTypeCode", newList[2]);//手术类别code
					Document document = new Document();
					document.append("outInSum",map.get(key));//金额
					document.append("finalDate", newList[0]);//时间
					document.append("operationTypeName", newList[1]);//手术类别  
					document.append("operationTypeCode", newList[2]);//手术
					mbDao.update("OPERINCOMEOPTYPEYEARNEW", document1, document, true);
				}
			}
		}
		
	}

	/**
	 * 初始化手术收入统计（科室前五）按月份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public void initOperTopFiveDeptMonth(String startDate, String endDate) {
		mbDao = new MongoBasicDao();
		List<String> list=reMonthDay(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
			String temp;//月数据
			Double dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,7);
				DBCursor cursor = mbDao.findAlldata("OPERINCOMETOPFIVEDEPTNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Double value = (Double) dbCursor.get("outInSum") ;//金额
					 String name = (String) dbCursor.get("execDept");//科室名称
					 String deptCode = (String) dbCursor.get("deptCode");//科室code
					 temp1=temp+"&"+name+"&"+deptCode;
					 if(map.containsKey(temp1)){//如果key存在 比较name
							 dou=map.get(temp1);
							 dou+=value;
							 map.put(temp1,dou);
					 }else{//如果key不存在   添加到map1中
						 map.put(temp1, value);
					 }

				}
			}
			String [] newList=new String[3];
			for(String key:map.keySet()){
				newList=key.split("&");
				if(newList.length==3){
					Document document1 = new Document();
					document1.append("finalDate", newList[0]);
					document1.append("execDept", newList[1]);//科室名称
					document1.append("deptCode", newList[2]);//科室code
					Document document = new Document();
					document.append("outInSum",map.get(key));//金额
					document.append("finalDate", newList[0]);//时间
					document.append("execDept", newList[1]);//科室名称
					document.append("deptCode", newList[2]);//科室code
					mbDao.update("OPERINCOMETOPFIVEDEPTMONTHNEW", document1, document, true);
				}
			}
		}
		
	}
	/**
	 * 初始化手术收入统计（科室前五）按年份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public void initOperTopFiveDeptYear(String startDate, String endDate) {
		mbDao = new MongoBasicDao();
		List<String> list=reYearMonth(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
			String temp;//月数据
			Double dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,4);
				DBCursor cursor = mbDao.findAlldata("OPERINCOMETOPFIVEDEPTMONTHNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Double value = (Double) dbCursor.get("outInSum") ;//金额
					 String name = (String) dbCursor.get("execDept");//科室名称
					 String deptCode = (String) dbCursor.get("deptCode");//科室code
					 temp1=temp+"&"+name+"&"+deptCode;
					 if(map.containsKey(temp1)){//如果key存在 比较name
							 dou=map.get(temp1);
							 dou+=value;
							 map.put(temp1,dou);
					 }else{//如果key不存在   添加到map1中
						 map.put(temp1, value);
					 }

				}
			}
			String [] newList=new String[3];
			for(String key:map.keySet()){
				newList=key.split("&");
				if(newList.length==3){
					Document document1 = new Document();
					document1.append("finalDate", newList[0]);
					document1.append("execDept", newList[1]);//科室名称
					document1.append("deptCode", newList[2]);//科室code
					Document document = new Document();
					document.append("outInSum",map.get(key));//金额
					document.append("finalDate", newList[0]);//时间
					document.append("execDept", newList[1]);//科室名称
					document.append("deptCode", newList[2]);//科室code
					mbDao.update("OPERINCOMETOPFIVEDEPTYEARNEW", document1, document, true);
				}
			}
		}
		
	}
	/**
	 * 初始化手术收入统计（医生前五）按月份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public void initOperTopFiveDocMonth(String startDate, String endDate) {
		mbDao = new MongoBasicDao();
		List<String> list=reMonthDay(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
			String temp;//月数据
			Double dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,7);
				DBCursor cursor = mbDao.findAlldata("OPERINCOMETOPFIVEDOCNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Double value = (Double) dbCursor.get("outInSum") ;//金额
					 String name = (String) dbCursor.get("docName");//医生名称
					 String docCode = (String) dbCursor.get("docCode");//医生code
					 temp1=temp+"&"+name+"&"+docCode;
					 if(map.containsKey(temp1)){//如果key存在 比较name
							 dou=map.get(temp1);
							 dou+=value;
							 map.put(temp1,dou);
					 }else{//如果key不存在   添加到map1中
						 map.put(temp1, value);
					 }

				}
			}
			String [] newList=new String[3];
			for(String key:map.keySet()){
				newList=key.split("&");
				if(newList.length==3){
					Document document1 = new Document();
					document1.append("finalDate", newList[0]);
					document1.append("docName", newList[1]);//医生名称
					document1.append("docCode", newList[2]);//医生code
					Document document = new Document();
					document.append("outInSum",map.get(key));//金额
					document.append("finalDate", newList[0]);//时间
					document.append("docName", newList[1]);//医生名称
					document.append("docCode", newList[2]);//医生code
					mbDao.update("OPERINCOMETOPFIVEDOCMONTHNEW", document1, document, true);
				}
			}
		}
		
	}

	/**
	 * 初始化手术收入统计（医生前五）按年份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public void initOperTopFiveDocYear(String startDate, String endDate) {
		mbDao = new MongoBasicDao();
		List<String> list=reYearMonth(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
			String temp;//月数据
			Double dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,4);
				DBCursor cursor = mbDao.findAlldata("OPERINCOMETOPFIVEDOCMONTHNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Double value = (Double) dbCursor.get("outInSum") ;//金额
					 String name = (String) dbCursor.get("docName");//医生名称
					 String docCode = (String) dbCursor.get("docCode");//医生code
					 temp1=temp+"&"+name+"&"+docCode;
					 if(map.containsKey(temp1)){//如果key存在 比较name
							 dou=map.get(temp1);
							 dou+=value;
							 map.put(temp1,dou);
					 }else{//如果key不存在   添加到map1中
						 map.put(temp1, value);
					 }

				}
			}
			String [] newList=new String[3];
			for(String key:map.keySet()){
				newList=key.split("&");
				if(newList.length==3){
					Document document1 = new Document();
					document1.append("finalDate", newList[0]);
					document1.append("docName", newList[1]);//医生名称
					document1.append("docCode", newList[2]);//医生code
					Document document = new Document();
					document.append("outInSum",map.get(key));//金额
					document.append("finalDate", newList[0]);//时间
					document.append("docName", newList[1]);//医生名称
					document.append("docCode", newList[2]);//医生code
					mbDao.update("OPERINCOMETOPFIVEDOCYEARNEW", document1, document, true);
				}
			}
		}
		
	}
	

	/**
	 * 初始化手术收入统计（同环比）按月份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public void initOperIncomYoyRatioMonth(String startDate, String endDate) {
		mbDao = new MongoBasicDao();
		List<String> list=reMonthDay(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
			String temp;//月数据
			Double dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,7);
				DBCursor cursor = mbDao.findAlldata("OPERINCOMESUMNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Double value = (Double) dbCursor.get("outInSum") ;//金额
					 temp1=temp;
					 if(map.containsKey(temp1)){//如果key存在 比较name
							 dou=map.get(temp1);
							 dou+=value;
							 map.put(temp1,dou);
					 }else{//如果key不存在   添加到map1中
						 map.put(temp1, value);
					 }

				}
			}
			String [] newList=new String[1];
			for(String key:map.keySet()){
				newList=key.split("&");
				if(newList.length==1){
					Document document1 = new Document();
					document1.append("finalDate", newList[0]);
					Document document = new Document();
					document.append("outInSum",map.get(key));//金额
					document.append("finalDate", newList[0]);//时间
					mbDao.update("OPERINCOMESUMMONTHNEW", document1, document, true);
				}
			}
		}
		
	}

	/**
	 * 初始化手术收入统计（同环比）按年份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public void initOperIncomYoyRatioYear(String startDate, String endDate) {
		mbDao = new MongoBasicDao();
		List<String> list=reYearMonth(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
			String temp;//月数据
			Double dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,4);
				DBCursor cursor = mbDao.findAlldata("OPERINCOMESUMMONTHNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Double value = (Double) dbCursor.get("outInSum") ;//金额
					 temp1=temp;
					 if(map.containsKey(temp1)){//如果key存在 比较name
							 dou=map.get(temp1);
							 dou+=value;
							 map.put(temp1,dou);
					 }else{//如果key不存在   添加到map1中
						 map.put(temp1, value);
					 }

				}
			}
			String [] newList=new String[1];
			for(String key:map.keySet()){
				newList=key.split("&");
				if(newList.length==1){
					Document document1 = new Document();
					document1.append("finalDate", newList[0]);
					Document document = new Document();
					document.append("outInSum",map.get(key));//金额
					document.append("finalDate", newList[0]);//时间
					mbDao.update("OPERINCOMESUMYEARNEW", document1, document, true);
				}
			}
		}
	}

	/**  
	 * 
	 * 手术收入统计（住院门诊）mongodb
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	@Override
	public List<OperationIncomeVo> queryOperationNums(String searchTime,
			String dateSign) {
		mbDao = new MongoBasicDao();
		String tableName;//查询表
		if("1".equals(dateSign )){
			tableName="SSSRTJ_OPERINCOMEMZ_YEAR";
		}else if("2".equals(dateSign )){//月
			tableName="SSSRTJ_OPERINCOMEMZ_MONTH";
		}else{//日
			tableName="SSSRTJ_OPERINCOMEMZ_DAY";
		}
		List<OperationIncomeVo> list1=new ArrayList<OperationIncomeVo>();
		boolean flg=mbDao.isCollection(tableName);
		if(flg){
			BasicDBObject bdObject = new BasicDBObject();
			bdObject.append("finalDate", searchTime);
			DBCursor cursor = mbDao.findAlldata(tableName, bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				OperationIncomeVo voOne=new  OperationIncomeVo();
				 dbCursor = cursor.next();
				 String outInSum =NumberUtil.init().format((Double)dbCursor.get("outInSum"), 2);//住院、门诊手术费用
				 String classType =(String)dbCursor.get("classType");//科室
				 if("MZ01".equals(classType)){
					 voOne.setName("门诊");
				 }else{
					 voOne.setName("住院");
				 }
				 voOne.setTotalAmount(outInSum);	
				 list1.add(voOne);
			}
		}
		return list1;
	}

	/**  
	 * 
	 * 手术收入统计（手术类别）mongodb
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	@Override
	public List<OperationIncomeVo> queryOperationOpType(String searchTime,
			String dateSign) {
		mbDao = new MongoBasicDao();
		String tableName;//查询表
		if("1".equals(dateSign )){
			tableName="SSSRTJ_OPERINCOMETYPE_YEAR";
		}else if("2".equals(dateSign )){//月
			tableName="SSSRTJ_OPERINCOMETYPE_MONTH";
		}else{//日
			tableName="SSSRTJ_OPERINCOMETYPE_DAY";
		}
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("finalDate", searchTime);
		DBCursor cursor = mbDao.findAlldata(tableName, bdObject);
		DBObject dbCursor;
		List<OperationIncomeVo> list1=new ArrayList<OperationIncomeVo>();
		
		while(cursor.hasNext()){
			 OperationIncomeVo voOne=new  OperationIncomeVo();
			 dbCursor = cursor.next();
			 String outInSum =NumberUtil.init().format((Double)dbCursor.get("outInSum"), 2);//住院、门诊手术费用
			 String name =(String) dbCursor.get("operationTypeName");//手术类别名称
			 voOne.setName(name);
			 voOne.setTotalAmount(outInSum);	
			list1.add(voOne);
		}
		return list1;
	}

	 /**  
	 * 
	 * 手术收入统计（手术类别）从数据库
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param startDate开始时间
	 * @param endDate 结束时间
	 * @param dateSign 区分标记 年、月、日
	 * @param itemtnL 非药品明细分区
	 * @param feetnL 费用明细分区
	 * @param flg  区分按小时还是按天更新  
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	@Override
	public List<OperationIncomeVo> queryOperationOpTypeToDB(List<String> itemList, List<String> feeList, String startDate,String endDate, String dateSign) {
		StringBuffer buffer=new StringBuffer();
		String timeType="";
		if("1".equals(dateSign)){
			timeType="yyyy";
		}else if("2".equals(dateSign)){
			timeType="yyyy-MM";
		}else{
			timeType="yyyy-MM-dd";
		}
		buffer.append("select encode as operationTypeCode  ,name as operationTypeName,finalDate ,sum(nvl(outInSum1,0)) as outInSum  from ( ");
		for(int i=0;i<feeList.size();i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append(" select t.code_encode as encode,t.code_name as name, t"+i+".tot_cost outInSum1,to_char(t"+i+".fee_date,:timeType) as finalDate ");
			buffer.append(" from t_business_dictionary t left join  T_OPERATION_APPLY app on t.code_encode = app.op_type  and app.stop_flg = 0 and app.del_flg = 0 ");
			buffer.append(" left join  "+feeList.get(i)+" t"+i+" on t"+i+".clinic_code = app.clinic_code  and t"+i+".pay_flag = '1' and t"+i+".trans_type = 1  and t"+i+".del_flg = 0 and t"+i+".stop_flg = 0");
			buffer.append("  and  t"+i+".fee_date between to_date(:startDate,'yyyy-MM-dd HH24:MI:SS') and to_date(:endDate,'yyyy-MM-dd HH24:MI:SS') ");
			buffer.append(" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code and c.report_code='MZ01' and c.fee_stat_code = '10' ");
			buffer.append(" where t.code_type = 'operatetype' ");
		}
		if(itemList!=null&&feeList!=null&&itemList.size()>0&&feeList.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0;i<itemList.size();i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append(" select t.code_encode as encode,t.code_name as name, n"+i+".tot_cost as outInSum1,to_char(n"+i+".fee_date,:timeType) as finalDate ");
			buffer.append(" from t_business_dictionary t left join  T_OPERATION_APPLY app on t.code_encode = app.op_type and app.stop_flg = 0 and app.del_flg = 0 ");
			buffer.append(" left join  "+itemList.get(i)+" n"+i+" on  n"+i+".inpatient_no = app.clinic_code  and  n"+i+".del_flg=0 and  n"+i+".stop_flg=0 and  n"+i+".send_flag=1 and  n"+i+".trans_type = 1 ");
			buffer.append(" and n"+i+".fee_date between to_date(:startDate,'yyyy-MM-dd HH24:MI:SS') and to_date(:endDate,'yyyy-MM-dd HH24:MI:SS') ");
			buffer.append(" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code and c.report_code='ZY01' and c.fee_stat_code = '10' ");
			buffer.append(" where t.code_type = 'operatetype' ");
		}
		buffer.append(") group by encode,name,finalDate ");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("timeType", timeType);
		List<OperationIncomeVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<OperationIncomeVo>() {
			@Override
			public OperationIncomeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OperationIncomeVo vo = new OperationIncomeVo();
				vo.setName(rs.getString("operationTypeName"));
				vo.setTotalAmount(NumberUtil.init().format(rs.getDouble("outInSum"),2));
				return vo;
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OperationIncomeVo>();
	}


	 /**  
	 * 
	 * 查询手术收入统计（门诊住院）从数据库
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param startDate开始时间
	 * @param endDate 结束时间
	 * @param dateSign 区分标记 年、月、日
	 * @param itemtnL 非药品明细分区
	 * @param feetnL 费用明细分区
	 * @param flg  区分按小时还是按天更新  
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	@Override
	public List<OperationIncomeVo> queryOperationNumsToDB(List<String> itemList, List<String> feeList, String startDate,String endDate, String dateSign) {
		StringBuffer buffer=new StringBuffer(2000);
		String timeType="";
		if("1".equals(dateSign)){
			timeType="yyyy";
		}else if("2".equals(dateSign)){
			timeType="yyyy-MM";
		}else{
			timeType="yyyy-MM-dd";
		}
		buffer.append("select sum(nvl(outInSum1,0)) as outInSum , finalDate,classType from( ");
		for(int i=0;i<feeList.size();i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select  t"+i+".tot_cost outInSum1,to_char(t"+i+".fee_date,:timeType) as finalDate ,'MZ01' AS classType ");
			buffer.append("from "+feeList.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code and c.report_code='MZ01' and c.fee_stat_code = '10'");
			buffer.append("where t"+i+".stop_flg = 0 and t"+i+".del_flg = 0 and t"+i+".trans_type = 1 and t"+i+".pay_flag = 1 and t"+i+".fee_date between to_date(:startDate,'yyyy-MM-dd HH24:MI:SS') and to_date(:endDate,'yyyy-MM-dd HH24:MI:SS') ");
		}
		if(itemList!=null&&feeList!=null&&itemList.size()>0&&feeList.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0;i<itemList.size();i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append(" select n"+i+".tot_cost as outInSum1,to_char(n"+i+".fee_date,:timeType) as finalDate,'ZY01' AS classType ");
			buffer.append("from "+itemList.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code and c.report_code='ZY01' and c.fee_stat_code = '10'");
			buffer.append("where n"+i+".stop_flg = 0 and n"+i+".del_flg = 0 and n"+i+".trans_type = 1 and  n"+i+".SEND_FLAG = 1 and n"+i+".SEND_FLAG = 1 and  n"+i+".fee_date between to_date(:startDate,'yyyy-MM-dd HH24:MI:SS') and to_date(:endDate,'yyyy-MM-dd HH24:MI:SS') ");
		}
		buffer.append(") group by finalDate,classType ");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("timeType", timeType);
		List<OperationIncomeVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<OperationIncomeVo>() {
			@Override
			public OperationIncomeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OperationIncomeVo vo = new OperationIncomeVo();
				vo.setTotalAmount(NumberUtil.init().format(rs.getDouble("outInSum"),2));
				if("ZY01".equals(rs.getString("classType"))){
					vo.setName("住院");
				}else{
					vo.setName("门诊");
				}
				return vo;
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OperationIncomeVo>();
	}
	/**  
	 * 
	 * 查询科室前五从mongodb
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	@Override
	public List<OperationIncomeVo> queryOperationTOPFiveDept(String searchTime,
			String dateSign) {
		mbDao = new MongoBasicDao();
		String tableName;//查询表
		if("1".equals(dateSign )){
			tableName="SSSRTJ_OPERINCOMEDEPT_YEAR";
		}else if("2".equals(dateSign )){//月
			tableName="SSSRTJ_OPERINCOMEDEPT_MONTH";
		}else{//日
			tableName="SSSRTJ_OPERINCOMEDEPT_DAY";
		}
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("finalDate", searchTime);
		DBCursor cursor = mbDao.findAlldataBySort(tableName, bdObject, "outInSum");
		DBObject dbCursor;
		List<OperationIncomeVo> list1=new ArrayList<OperationIncomeVo>();
		int count=5;
		while(cursor.hasNext()){
			 OperationIncomeVo voOne=new  OperationIncomeVo();
			 dbCursor = cursor.next();
			 String outInSum =NumberUtil.init().format((Double)dbCursor.get("outInSum"), 2);//住院、门诊手术费用
			 String deptName =(String) dbCursor.get("deptName");//科室名称
			 voOne.setName(deptName);
			 voOne.setTotalAmount(outInSum);	
			 if(deptName!=null){
				 count--;
				 list1.add(voOne);
			 }
			 if(count==0){
					break;
			 }
			
		}
		return list1;
	}
	/**  
	 * 
	 * 查询医生前五从mongodb
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	@Override
	public List<OperationIncomeVo> queryOperationTOPFiveDoc(String searchTime,
			String dateSign) {
		mbDao = new MongoBasicDao();
		String tableName;//查询表
		if("1".equals(dateSign )){
			tableName="SSSRTJ_OPERINCOMEDOC_YEAR";
		}else if("2".equals(dateSign )){//月
			tableName="SSSRTJ_OPERINCOMEDOC_MONTH";
		}else{//日
			tableName="SSSRTJ_OPERINCOMEDOC_DAY";
		}
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("finalDate", searchTime);
		DBCursor cursor = mbDao.findAlldataBySort(tableName, bdObject, "outInSum");
		DBObject dbCursor;
		List<OperationIncomeVo> list1=new ArrayList<OperationIncomeVo>();
		int count=5;
		while(cursor.hasNext()){
			 OperationIncomeVo voOne=new  OperationIncomeVo();
			 dbCursor = cursor.next();
			 String outInSum =NumberUtil.init().format((Double)dbCursor.get("outInSum"), 2);//住院、门诊手术费用
			 String docName =(String) dbCursor.get("docName");//科室名称
			 voOne.setName(docName);
			 voOne.setTotalAmount(outInSum);	
			 if(docName!=null){
				 count--;
				 list1.add(voOne);
			 }
			 if(count==0){
					break;
			 }
		}
		return list1;
	}

	 /**  
	 * 
	 * 数据库查询科室前五
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param startDate开始时间
	 * @param endDate 结束时间
	 * @param dateSign 区分标记 年、月、日
	 * @param itemtnL 非药品明细分区
	 * @param feetnL 费用明细分区
	 * @param flg  区分按小时还是按天更新  
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	@Override
	public List<OperationIncomeVo> queryTOPFiveDeptToDB(List<String> itemList,List<String> feeList, String startDate, String endDate,
			String dateSign) {
		 String timeType="";
		 if("1".equals(dateSign)){
			timeType="yyyy";
		 }else if("2".equals(dateSign)){
			timeType="yyyy-MM";
		 }else{
			timeType="yyyy-MM-dd";
		 }
		 StringBuffer buffer=new StringBuffer();
		 buffer.append("select * from( ");
		 buffer.append(" select  f.finalDate as finalDate,f.totCost as outInSum,(select e.dept_name from t_department e where e.dept_code=f.execDept  ) as execDept, f.execDept AS deptCode,row_number()over(partition by f.finalDate order by f.totCost desc ) as rn from( ");
		 buffer.append(" select sum(nvl(tot_cost, 0)) as totCost, execDept,  finalDate  from (");
		 for(int i=0;i<itemList.size();i++){
				if(i>0){
					buffer.append(" Union All ");
				}
				 buffer.append("select t"+i+".tot_cost, t"+i+".execute_deptcode as execDept,to_char(t"+i+".fee_date,:timeType) as finalDate  from  "+itemList.get(i)+" t"+i);
				 buffer.append(" join t_charge_minfeetostat c on c.minfee_code = t"+i+".fee_code and c.report_code = 'ZY01'  and c.fee_stat_code = '10'  where t"+i+".del_flg = 0 and t"+i+".stop_flg = 0  and t"+i+".send_flag = 1  and t"+i+".trans_type = 1 ");
				 buffer.append(" and t"+i+".fee_date between to_date(:startDate,'yyyy-MM-dd HH24:MI:SS') and to_date(:endDate,'yyyy-MM-dd HH24:MI:SS') ");
			}
			if(feeList!=null&&itemList!=null&&feeList.size()>0&&itemList.size()>0){
				buffer.append(" union All ");
			}
			for(int i=0;i<feeList.size();i++){
				if(i>0){
					buffer.append(" Union All ");
				}
				 buffer.append(" select n"+i+".tot_cost,n"+i+".exec_dpcd as execDept,to_char(n"+i+".fee_date,:timeType)  as finalDate  from "+feeList.get(i)+" n"+i+" join t_charge_minfeetostat c   on c.minfee_code = n"+i+".fee_code");
				 buffer.append(" and c.report_code = 'MZ01' and c.fee_stat_code = '10' where n"+i+".del_flg = 0 and n"+i+".stop_flg = 0  and n"+i+".pay_flag = 1 and n"+i+".trans_type = 1");
				 buffer.append(" and n"+i+".fee_date between to_date(:startDate,'yyyy-MM-dd HH24:MI:SS') and to_date(:endDate,'yyyy-MM-dd HH24:MI:SS') ");
			}
		 buffer.append("  )group by execDept, finalDate ) f)e where rn<6  ");
		 Map<String,Object> paramMap = new HashMap<String, Object>();
		 paramMap.put("startDate", startDate);
		 paramMap.put("endDate", endDate);
		 paramMap.put("timeType", timeType);
		 List<OperationIncomeVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<OperationIncomeVo>() {
				@Override
				public OperationIncomeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					OperationIncomeVo vo = new OperationIncomeVo();
					vo.setName(rs.getString("execDept"));
					vo.setTotalAmount(NumberUtil.init().format(rs.getDouble("outInSum"),2));
					return vo;
				}
			});
		if(list!=null&&list.size()>0){
				return list;
		}
		return new ArrayList<OperationIncomeVo>();
	}

	 /**  
	 * 
	 * 数据库查询医生前五
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param startDate开始时间
	 * @param endDate 结束时间
	 * @param dateSign 区分标记 年、月、日
	 * @param itemtnL 非药品明细分区
	 * @param feetnL 费用明细分区
	 * @param flg  区分按小时还是按天更新  
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	@Override
	public List<OperationIncomeVo> queryTOPFiveDocDB(List<String> itemList,
			List<String> feeList, String startDate, String endDate,
			String dateSign) {
		 String timeType="";
		 if("1".equals(dateSign)){
			timeType="yyyy";
		 }else if("2".equals(dateSign)){
			timeType="yyyy-MM";
		 }else{
			timeType="yyyy-MM-dd";
		 }
		 StringBuffer buffer=new StringBuffer();
		 buffer.append("select * from( ");
		 buffer.append(" select  f.finalDate as finalDate,f.totCost as outInSum,(select e.employee_name from t_employee e where e.employee_jobno=f.docCode  ) as docName,f.docCode AS docCode,row_number()over(partition by f.finalDate order by f.totCost desc ) as rn from( ");
		 buffer.append(" select sum(nvl(tot_cost, 0)) as totCost, docCode,  finalDate  from (");
		 for(int i=0;i<itemList.size();i++){
				if(i>0){
					buffer.append(" Union All ");
				}
				 buffer.append("select t"+i+".tot_cost, t"+i+".RECIPE_DOCCODE as docCode,to_char(t"+i+".fee_date,:timeType) as finalDate  from  "+itemList.get(i)+" t"+i);
				 buffer.append(" join t_charge_minfeetostat c on c.minfee_code = t"+i+".fee_code and c.report_code = 'ZY01'  and c.fee_stat_code = '10'  where t"+i+".del_flg = 0 and t"+i+".stop_flg = 0  and t"+i+".send_flag = 1  and t"+i+".trans_type = 1 ");
				 buffer.append(" and t"+i+".fee_date between to_date(:startDate,'yyyy-MM-dd HH24:MI:SS') and to_date(:endDate,'yyyy-MM-dd HH24:MI:SS') ");
			}
			if(feeList!=null&&itemList!=null&&itemList.size()>0&&feeList.size()>0){
				buffer.append(" union All ");
			}
			for(int i=0;i<feeList.size();i++){
				if(i>0){
					buffer.append(" Union All ");
				}
				 buffer.append(" select n"+i+".tot_cost,n"+i+".DOCT_CODE as docCode,to_char(n"+i+".fee_date,:timeType)  as finalDate  from "+feeList.get(i)+" n"+i+" join t_charge_minfeetostat c   on c.minfee_code = n"+i+".fee_code");
				 buffer.append(" and c.report_code = 'MZ01' and c.fee_stat_code = '10' where n"+i+".del_flg = 0 and n"+i+".stop_flg = 0  and n"+i+".pay_flag = 1 and n"+i+".trans_type = 1");
				 buffer.append(" and n"+i+".fee_date between to_date(:startDate,'yyyy-MM-dd HH24:MI:SS') and to_date(:endDate,'yyyy-MM-dd HH24:MI:SS') ");
		 }
		 buffer.append("  )group by docCode, finalDate ) f)e where rn<6 ");
		 Map<String,Object> paramMap = new HashMap<String, Object>();
		 paramMap.put("startDate", startDate);
		 paramMap.put("endDate", endDate);
		 paramMap.put("timeType", timeType);
		 List<OperationIncomeVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<OperationIncomeVo>() {
				@Override
				public OperationIncomeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					OperationIncomeVo vo = new OperationIncomeVo();
					vo.setName(rs.getString("docName"));
					vo.setTotalAmount(NumberUtil.init().format(rs.getDouble("outInSum"),2));
					return vo;
				}
			});
		 if(list!=null&&list.size()>0){
				return list;
		}
		return new ArrayList<OperationIncomeVo>();
	}
	
	/**  
	 * 
	 * 查询环环比从mongodb
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	@Override
	public List<OperationIncomeVo> queryRatioCount(String searchTime,
			String dateSign) {
		String tableName;//查询表
		if("1".equals(dateSign )){
			tableName="SSSRTJ_OPERINCOMEYOYRATIO_YEAR";
		}else if("2".equals(dateSign )){//月
			tableName="SSSRTJ_OPERINCOMEYOYRATIO_MONTH";
		}else{//日
			tableName="SSSRTJ_OPERINCOMEYOYRATIO_DAY";
		}
		String[] startDate=this.RatioDate(searchTime, dateSign);;//开始时间
		if(searchTime==null){
			return new ArrayList<OperationIncomeVo>();
		}
		int eLength=searchTime.length();
		if("1".equals(dateSign)){
			if(eLength!=4){
				return new ArrayList<OperationIncomeVo>();
			}
		}else if("2".equals(dateSign)){//月
			if(eLength!=7){
				return new ArrayList<OperationIncomeVo>();
			}
		}else{//日
			if(eLength!=10){
				return new ArrayList<OperationIncomeVo>();
			}
		}
		
		List<OperationIncomeVo> list1=new ArrayList<OperationIncomeVo>();
		for(String vo:startDate){
			BasicDBObject bdObject = new BasicDBObject();
			mbDao = new MongoBasicDao();
			bdObject.append("finalDate", vo);
			DBCursor cursor = mbDao.findAlldata(tableName, bdObject);
			DBObject dbCursor;
			if(!cursor.hasNext()){
				OperationIncomeVo voOne=new  OperationIncomeVo();
				voOne.setTotalAmount("0");
				voOne.setName(vo);
				list1.add(voOne);
				continue;
			}
			OperationIncomeVo voOne=new  OperationIncomeVo();
			Double dou=0.00;
			voOne.setName(vo);
			Double doubValue;
			while(cursor.hasNext()){
				 dbCursor = cursor.next();
				 doubValue=(Double)dbCursor.get("outInSum");
				 dou+=doubValue;
				}
			voOne.setTotalAmount(NumberUtil.init().format(dou, 2));
			list1.add(voOne);
		}
		return list1;
	}


	 /**  
	 * 
	 * 查询同环比从mongodb
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	@Override
	public List<OperationIncomeVo> queryYoyCount(String searchTime,String dateSign) {
		String tableName;//查询表
		if("1".equals(dateSign )){
			tableName="SSSRTJ_OPERINCOMEYOYRATIO_YEAR";
		}else if("2".equals(dateSign )){//月
			tableName="SSSRTJ_OPERINCOMEYOYRATIO_MONTH";
		}else{//日
			tableName="SSSRTJ_OPERINCOMEYOYRATIO_DAY";
		}
		if(searchTime==null){
			return new ArrayList<OperationIncomeVo>();
		}
		int eLength=searchTime.length();
		if("2".equals(dateSign)){//月
			if(eLength!=7){
				return new ArrayList<OperationIncomeVo>();
			}
		}else{//日
			if(eLength!=10){
				return new ArrayList<OperationIncomeVo>();
			}
		}
		String[] startDate=this.yoyDate(searchTime, dateSign);//6月内时间数组
		List<OperationIncomeVo> list=new ArrayList<OperationIncomeVo>();
		
		for(String dateVo:startDate){
			BasicDBObject bdObject = new BasicDBObject();
			mbDao = new MongoBasicDao();
			bdObject.append("finalDate", dateVo);
			DBCursor cursor = mbDao.findAlldata(tableName, bdObject);
			DBObject dbCursor;
			if(!cursor.hasNext()){
				OperationIncomeVo voOne=new  OperationIncomeVo();
				voOne.setTotalAmount("0");
				voOne.setName(dateVo);
				list.add(voOne);
				continue;
			}
			OperationIncomeVo voOne=new  OperationIncomeVo();
			Double dou=0.00;
			voOne.setName(dateVo);
			Double doubValue;
			while(cursor.hasNext()){
				 dbCursor = cursor.next();
				 doubValue=(Double)dbCursor.get("outInSum");
				 dou+=doubValue;
				}
			voOne.setTotalAmount(NumberUtil.init().format(dou, 2));
			list.add(voOne);
		}
		return list;
	}
	
	
	/**
	 *  获取时间（环比）
	 * @param date
	 * @param dateSing
	 * @return
	 */
	public String[] RatioDate(String date,String dateSing){
		 Calendar ca = Calendar .getInstance();
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		 SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM");
		 SimpleDateFormat sdf2=new SimpleDateFormat("yyyy");
		 try {
			 if("1".equals(dateSing)){
			
				date=sdf.format(sdf2.parse(date));
			
			 }else if("2".equals(dateSing)){
				 date=sdf.format(sdf1.parse(date)); 
			 }
		 } catch (ParseException e) {
			} 
		 String[] dateOne =date.split("-");
		 String[] strArr=new String[6];
		 ca.set(Integer.parseInt(dateOne[0]), Integer.parseInt(dateOne[1])-1, Integer.parseInt(dateOne[2]));
		for(int i=0;i<6;i++){
			if("1".equals(dateSing)){
				strArr[i]=sdf2.format(ca.getTime());
				ca.add(Calendar.YEAR, -1);
			}else if("2".equals(dateSing)){
				strArr[i]=sdf1.format(ca.getTime());
				ca.add(Calendar.MONTH, -1);
			}else{
				strArr[i]=sdf.format(ca.getTime());
				ca.add(Calendar.DATE, -1);
			}
		}
		 return strArr;
	}
	
	/**
	 * 获取时间（同比）
	 * @param date
	 * @param dateSing
	 * @return
	 */
	public String[] yoyDate(String date,String dateSing){
		String [] strArr=new String[6];
		String[] dateArr=date.split("-");
		int dateTemp=Integer.parseInt(dateArr[0]);
		for(int i=0;i<6;i++){
			if("2".equals(dateSing)){//月同比
				strArr[i]=(dateTemp-i)+"-"+dateArr[1];
			}else{
				strArr[i]=(dateTemp-i)+"-"+dateArr[1]+"-"+dateArr[2];
			}
		}
		
		return strArr;
	}

	 /**  
	 * 
	 * 数据库查询同环比
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param startDate开始时间
	 * @param endDate 结束时间
	 * @param dateSign 区分标记 年、月、日
	 * @param itemtnL 非药品明细分区
	 * @param feetnL 费用明细分区
	 * @param flg  区分按小时还是按天更新  
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	@Override
	public OperationIncomeVo queryYoyCountToDB(List<String> itemtnL,
			List<String> feetnL, String startDate, String endDate, String dateSign) {
		StringBuffer buffer=new StringBuffer();
		String dateFormat="";
		if("1".equals(dateSign)){
			dateFormat="yyyy";
		}else if("2".equals(dateSign)){
			dateFormat="yyyy-MM";
		}else{
			dateFormat="yyyy-MM-dd";
		}
		buffer.append("select sum(nvl(outInSum1,0)) as outInSum , finalDate from( ");
		for(int i=0;i<feetnL.size();i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select  t"+i+".tot_cost outInSum1,to_char(t"+i+".fee_date,'"+dateFormat+"') as finalDate ");
			buffer.append("from "+feetnL.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code and c.report_code='MZ01' and c.fee_stat_code = '10'");
			buffer.append("where t"+i+".stop_flg = 0 and t"+i+".del_flg = 0 and t"+i+".trans_type = 1 and t"+i+".pay_flag = 1 and  t"+i+".fee_date >= to_date('"+startDate+"','yyyy-MM-dd HH24:MI:SS') and t"+i+".fee_date <to_date('"+endDate+"','yyyy-MM-dd HH24:MI:SS') ");
		}
		if(itemtnL!=null&&feetnL!=null&&itemtnL.size()>0&&feetnL.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0;i<itemtnL.size();i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append(" select n"+i+".tot_cost as outInSum1,to_char(n"+i+".fee_date,'"+dateFormat+"') as finalDate  ");
			buffer.append("from "+itemtnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code and c.report_code='ZY01' and c.fee_stat_code = '10'");
			buffer.append("where n"+i+".stop_flg = 0 and n"+i+".del_flg = 0 and n"+i+".trans_type = 1 and n"+i+".SEND_FLAG = 1 and  n"+i+".fee_date >= to_date('"+startDate+"','yyyy-MM-dd HH24:MI:SS') and n"+i+".fee_date <to_date('"+endDate+"','yyyy-MM-dd HH24:MI:SS') ");
		}
		buffer.append(") group by finalDate ");
		List<OperationIncomeVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<OperationIncomeVo>() {
			@Override
			public OperationIncomeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OperationIncomeVo vo = new OperationIncomeVo();
				vo.setName(rs.getString("finalDate"));
				vo.setTotalAmount(NumberUtil.init().format(rs.getDouble("outInSum"),2));
				return vo;
			}
		});
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		OperationIncomeVo vo = new OperationIncomeVo();
		vo.setTotalAmount("0");
		if("1".equals(dateSign)){
			vo.setName(startDate.substring(0, 4));
		}else if("2".equals(dateSign)){
			vo.setName(startDate.substring(0, 7));
		}else{
			vo.setName(startDate.substring(0, 10));
		}
		return vo;
	}
}


