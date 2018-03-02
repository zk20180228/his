package cn.honry.statistics.bi.bistac.operationNum.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.bi.bistac.deptAndFeeData.vo.StatisticsVo;
import cn.honry.statistics.bi.bistac.operationNum.dao.OperationNumsDao;
import cn.honry.statistics.bi.bistac.operationNum.vo.OperationNumsVo;
import cn.honry.utils.DateUtils;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("operationNumsDao")
@SuppressWarnings({ "all" })
public class OperationNumsDaoImpl extends HibernateEntityDao<OperationNumsVo>  implements OperationNumsDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	SimpleDateFormat sdFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfMonth=new SimpleDateFormat("yyyy-MM");

	Calendar ca=Calendar.getInstance();
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 *  初始化手术例数（门诊、住院、介入）
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperationNumsToDB(String startDate,String endDate) {
		boolean times=timeDifference(startDate,endDate);
		Map<String,String> dateTime;
		if(!times){
			dateTime=queryBetweenTime(startDate,endDate,0,new HashMap<String, String>());
		}else{
			dateTime=queryBetweenTime(startDate,endDate,1,new HashMap<String, String>());
		}
		for(String begin:dateTime.keySet()){
			 StringBuffer buffer=new StringBuffer();
			 buffer.append(" select count(1) as nums, finalDate ,pasource from (select t.pasource,  to_char(t.createtime, 'yyyy-MM-dd') as finalDate ");
			 buffer.append(" from t_operation_record t where t.ynvalid = 1 and t.stop_flg = 0  and t.del_flg = 0  and t.createtime between ");
			 buffer.append(" to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+dateTime.get(begin)+"','yyyy-MM-dd HH24:MI:SS')) group by finalDate,pasource");
			 List<OperationNumsVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<OperationNumsVo>() {
					@Override
					public OperationNumsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
						OperationNumsVo vo = new OperationNumsVo();
						vo.setNums(rs.getInt("nums"));
						vo.setFinalDate(rs.getString("finalDate"));
						vo.setPasource(rs.getString("pasource"));
						return vo;
					}
				});
			 if(list!=null && list.size()>0){
				 Integer mzNums=0;
				 Integer zyNums=0;
				 if(times){
					 BasicDBObject bdObject = new BasicDBObject();
					 bdObject.append("finalDate", list.get(0).getFinalDate());
					 DBCursor cursor = new MongoBasicDao().findAlldata("OPERATIONNUMNEW", bdObject);
					 DBObject dbCursor;
						while(cursor.hasNext()){
							 dbCursor = cursor.next();
							 if("1".equals((String)dbCursor.get("pasource"))){
								 mzNums =(Integer)dbCursor.get("nums");//例数
							 }else{
								 zyNums =(Integer)dbCursor.get("nums");//例数
							 }
							 
						}
				 }	
				 for(OperationNumsVo vo:list){
					 Document document1 = new Document();
						document1.append("finalDate", vo.getFinalDate());//统计时间
						document1.append("pasource", vo.getPasource());//来源
						Document document = new Document();
						document.append("finalDate", vo.getFinalDate());//统计时间
						document.append("pasource", vo.getPasource());//来源
						if("1".equals(vo.getPasource())){
							document.append("nums", vo.getNums()+mzNums);//例数
						}else{
							document.append("nums", vo.getNums()+zyNums);//例数
						}
						new MongoBasicDao().update("OPERATIONNUMNEW", document1, document, true);
				}
			}
		}
	}
	
	/**
	 *  初始化手术例数（科室TOP5）
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperationNumsTopDeptToDB(String startDate,String endDate) {
		boolean times=timeDifference(startDate,endDate);
		Map<String,String> dateTime;
		if(!times){
			dateTime=queryBetweenTime(startDate,endDate,0,new HashMap<String, String>());
		}else{
			dateTime=queryBetweenTime(startDate,endDate,1,new HashMap<String, String>());
		}
		for(String begin:dateTime.keySet()){
			 StringBuffer buffer=new StringBuffer();
			 buffer.append(" select d.dept_name as deptName, t3.dept_code as deptCode, t3.createtime as finalDate, t3.num as nums from (select t2.dept_code,t2.createtime, t2.num,");
			 buffer.append(" row_number() over(partition by t2.createtime order by t2.num desc) row_num from (select t1.dept_code, count(1) as num,");
			 buffer.append(" t1.createtime from (select to_char(t.createtime, 'yyyy-MM-dd') as createtime,t.dept_code from t_operation_record t ");
			 buffer.append(" where t.del_flg = 0  and t.stop_flg = 0 and t.ynvalid = '1' and t.createtime between ");
			 buffer.append(" to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+dateTime.get(begin)+"','yyyy-MM-dd HH24:MI:SS')) t1 group by t1.dept_code, t1.createtime) t2) t3 ");
			 buffer.append(" left join t_department d on t3.dept_code = d.dept_code  ");
			 List<OperationNumsVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<OperationNumsVo>() {
					@Override
					public OperationNumsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
						OperationNumsVo vo = new OperationNumsVo();
						vo.setNums(rs.getInt("nums"));
						vo.setFinalDate(rs.getString("finalDate"));
						vo.setDeptName(rs.getString("deptName"));
						vo.setDeptCode(rs.getString("deptCode"));
						return vo;
					}
				});
			 
			 if(list!=null && list.size()>0){
				 List<OperationNumsVo> userList = new ArrayList<OperationNumsVo>();
					for(OperationNumsVo vo:list){
						Integer sNums=0;
						if(times){
							 BasicDBObject bdObject = new BasicDBObject();
							 bdObject.append("finalDate", vo.getFinalDate());
							 bdObject.append("deptName", vo.getDeptName());
							 bdObject.append("deptCode", vo.getDeptCode());
							 DBCursor cursor = new MongoBasicDao().findAlldata("OPERATIONNUMTOPDEPTNEW", bdObject);
							 DBObject dbCursor;
								List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
								while(cursor.hasNext()){
									OperationNumsVo voOne=new  OperationNumsVo();
									dbCursor = cursor.next();
									sNums =(Integer)dbCursor.get("nums");//例数
								}
						 }	
						Document document1 = new Document();
						document1.append("finalDate", vo.getFinalDate());//统计时间
						document1.append("deptName", vo.getDeptName());//科室
						document1.append("deptCode", vo.getDeptCode());
						Document document = new Document();
						document.append("finalDate", vo.getFinalDate());//统计时间
						document.append("deptName", vo.getDeptName());//科室
						document.append("nums", vo.getNums()+sNums);//例数
						document.append("deptCode", vo.getDeptCode());
						new MongoBasicDao().update("OPERATIONNUMTOPDEPTNEW", document1, document, true);
					}
			}
		}
	}
	
	/**
	 *  初始化手术例数（医生TOP5）
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperationNumsTopDocToDB(String startDate,String endDate) {
		boolean times=timeDifference(startDate,endDate);
		Map<String,String> dateTime;
		if(!times){
			dateTime=queryBetweenTime(startDate,endDate,0,new HashMap<String, String>());
		}else{
			dateTime=queryBetweenTime(startDate,endDate,1,new HashMap<String, String>());
		}
		for(String begin:dateTime.keySet()){
			 StringBuffer buffer=new StringBuffer();
			 buffer.append(" select e.employee_name as docName, t3.createtime as finalDate , t3.num as nums,t3.OPS_DOCD as docCode  from (select t2.ops_docd,");
			 buffer.append(" t2.createtime,t2.num,row_number() over(partition by t2.createtime order by t2.num desc) row_num  from (");
			 buffer.append(" select t1.ops_docd, count(1) as num, t1.createtime from (select to_char(t.createtime, 'yyyy-MM-dd') as createtime,");
			 buffer.append(" t.ops_docd  from t_operation_record t where t.del_flg = 0 and t.stop_flg = 0 and t.ynvalid = '1'  and t.createtime between ");
			 buffer.append(" to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+dateTime.get(begin)+"','yyyy-MM-dd HH24:MI:SS') ) t1 group by");
			 buffer.append(" t1.ops_docd, t1.createtime) t2) t3 left join t_employee e on t3.OPS_DOCD = e.employee_jobno  ");
			 List<OperationNumsVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<OperationNumsVo>() {
					@Override
					public OperationNumsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
						OperationNumsVo vo = new OperationNumsVo();
						vo.setNums(rs.getInt("nums"));
						vo.setFinalDate(rs.getString("finalDate"));
						vo.setDocName(rs.getString("docName"));
						vo.setDocCode(rs.getString("docCode"));
						return vo;
					}
				});
			 
			 if(list!=null && list.size()>0){
				 List<OperationNumsVo> userList = new ArrayList<OperationNumsVo>();
				 for(OperationNumsVo vo:list){
					    Integer sNums=0;
						if(times){
							 BasicDBObject bdObject = new BasicDBObject();
							 bdObject.append("finalDate", vo.getFinalDate());
							 bdObject.append("docCode", vo.getDocCode());
							 bdObject.append("docName", vo.getDeptName());
							 DBCursor cursor = new MongoBasicDao().findAlldata("OPERATIONNUMTOPDOCNEW", bdObject);
							 DBObject dbCursor;
							 while(cursor.hasNext()){
								dbCursor = cursor.next();
								sNums =(Integer)dbCursor.get("nums");//例数
							 }
						 }	
						Document document1 = new Document();
						document1.append("finalDate", vo.getFinalDate());//统计时间
						document1.append("docName", vo.getDocName());//医生
						document1.append("docCode", vo.getDocCode());//医生code
						Document document = new Document();
						document.append("finalDate", vo.getFinalDate());//统计时间
						document.append("docName", vo.getDocName());//医生
						document.append("nums", vo.getNums()+sNums);//例数
						document.append("docCode", vo.getDocCode());//医生code
						new MongoBasicDao().update("OPERATIONNUMTOPDOCNEW", document1, document, true);
					}
			}
		}
	}
	
	/**
	 *  初始化手术例数（同环比）
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsYoyRatioToDB(String startDate,String endDate) {
		boolean times=timeDifference(startDate,endDate);
		Map<String,String> dateTime;
		if(!times){
			dateTime=queryBetweenTime(startDate,endDate,0,new HashMap<String, String>());
		}else{
			dateTime=queryBetweenTime(startDate,endDate,1,new HashMap<String, String>());
		}
		
		for(String begin:dateTime.keySet()){
			 StringBuffer buffer=new StringBuffer();
			 buffer.append("select count(1) as nums, finalDate from (select  to_char(t.createtime, 'yyyy-MM-dd') as finalDate ");
			 buffer.append(" from t_operation_record t where t.ynvalid = 1 and t.stop_flg = 0  and t.del_flg = 0  and t.createtime between to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+dateTime.get(begin)+"','yyyy-MM-dd HH24:MI:SS')) group by finalDate");
			 List<OperationNumsVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<OperationNumsVo>() {
					@Override
					public OperationNumsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
						OperationNumsVo vo = new OperationNumsVo();
						vo.setNums(rs.getInt("nums"));
						vo.setFinalDate(rs.getString("finalDate"));
						return vo;
					}
				});
			 
			 if(list!=null && list.size()>0){
				 Integer nums=0;
				 if(times){
					 BasicDBObject bdObject = new BasicDBObject();
					 bdObject.append("finalDate", list.get(0).getFinalDate());
					 DBCursor cursor = new MongoBasicDao().findAlldata("OPERATIONNUMYOYRATIONNEW", bdObject);
					 DBObject dbCursor;
						List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
						while(cursor.hasNext()){
							OperationNumsVo voOne=new  OperationNumsVo();
							 dbCursor = cursor.next();
							 nums =(Integer)dbCursor.get("nums");//例数
						}
				 }	
				 for(OperationNumsVo vo:list){
						Document document1 = new Document();
						document1.append("finalDate", vo.getFinalDate());//统计时间
						Document document = new Document();
						document.append("finalDate", vo.getFinalDate());//统计时间
						document.append("nums", vo.getNums()+nums);//例数
						new MongoBasicDao().update("OPERATIONNUMYOYRATIONNEW", document1, document, true);
				}
			}
		} 
	}
	
	public  Map<String,String> queryBetweenTime(String begin,String end,int flg,Map<String,String> map){
		
		 try {
			Date date= sdFull.parse(begin);//开始时间
			String[] dateone=begin.split(" ");
			String[] dateArr=dateone[0].split("-");
			String[] dateArr1=dateone[1].split(":");
			Date date1= sdFull.parse(end);//结束时间
			if(date.getTime()>=date1.getTime()){//如果开始时间大于结束时间  结束
				return map;
			}else{
				ca.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1])-1, Integer.parseInt(dateArr[2]),Integer.parseInt(dateArr1[0]),Integer.parseInt(dateArr1[1]),Integer.parseInt(dateArr1[2]));
				if(flg==0){//小时为空 按天查
					String key=sdFull.format(ca.getTime());
					ca.add(Calendar.DATE, 1);//开始时间加1天
					begin=sdFull.format(ca.getTime());
					map.put(key,begin );
				}else{//按小时查
					String key=sdFull.format(ca.getTime());
					ca.add(Calendar.HOUR,1);
					begin=sdFull.format(ca.getTime());
					map.put(key,begin);
				}
				return queryBetweenTime(begin,end,flg,map);
			}
		} catch (ParseException e) {
		}//开始时间
		return map;
	}
	
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
	
	/**
	 *  初始化手术例数（门诊、住院、介入）按月份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperationNumsMonthToDB(String startDate, String endDate) {
		List<String> list=reMonthDay(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Integer> map=new HashMap<String,Integer>();//保存日期时间费用名称
			String temp;//月数据
			Integer dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,7);
				DBCursor cursor = new MongoBasicDao().findAlldata("OPERATIONNUMNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Integer value = (Integer) dbCursor.get("nums") ;//金额
					 String name = (String) dbCursor.get("pasource");//住院门诊
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
					document1.append("pasource", newList[1]);//统计时间
					Document document = new Document();
					document.append("nums",map.get(key));//例数
					document.append("finalDate", newList[0]);//时间
					document.append("pasource", newList[1]);//门诊
					new MongoBasicDao().update("OPERATIONNUMMONTHNEW", document1, document, true);
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
	 *  初始化手术例数（门诊、住院、介入）按年
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperationNumsYearToDB(String startDate, String endDate) {
		List<String> list=reYearMonth(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Integer> map=new HashMap<String,Integer>();//保存日期时间费用名称
			String temp;//月数据
			Integer dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,4);
				DBCursor cursor = new MongoBasicDao().findAlldata("OPERATIONNUMMONTHNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Integer value = (Integer) dbCursor.get("nums") ;//金额
					 String name = (String) dbCursor.get("pasource");//住院门诊
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
					document1.append("pasource", newList[1]);//统计时间
					Document document = new Document();
					document.append("nums",map.get(key));//例数
					document.append("finalDate", newList[0]);//时间
					document.append("pasource", newList[1]);//门诊
					new MongoBasicDao().update("OPERATIONNUMYEARNEW", document1, document, true);
				}
			}
		}
	}
	/**
	 *  初始化手术例数科室前五按月份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsTopDeptMonthToDB(String startDate, String endDate) {
		List<String> list=reMonthDay(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Integer> map=new HashMap<String,Integer>();//保存日期时间费用名称
			String temp;//月数据
			Integer dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,7);
				DBCursor cursor = new MongoBasicDao().findAlldata("OPERATIONNUMTOPDEPTNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Integer value = (Integer) dbCursor.get("nums") ;//例数
					 String name = (String) dbCursor.get("deptName");//科室名称
					 String deptCode = (String) dbCursor.get("deptCode");//科室名称
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
					document1.append("deptName", newList[1]);//统计时间
					document1.append("deptCode", newList[2]);//统计时间
					Document document = new Document();
					document.append("nums",map.get(key));//例数
					document.append("finalDate", newList[0]);//时间
					document.append("deptName", newList[1]);//科室名称
					document.append("deptCode", newList[2]);//统计时间
					new MongoBasicDao().update("OPERATIONNUMTOPDEPTMONTHNEW", document1, document, true);
				}
				
			}
		}
		
	}
	/**
	 *  初始化手术例数科室前五年份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsTopDeptYearToDB(String startDate, String endDate) {
		List<String> list=reYearMonth(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Integer> map=new HashMap<String,Integer>();//保存日期时间费用名称
			String temp;//月数据
			Integer dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,4);
				DBCursor cursor = new MongoBasicDao().findAlldata("OPERATIONNUMTOPDEPTMONTHNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Integer value = (Integer) dbCursor.get("nums") ;//金额
					 String name = (String) dbCursor.get("deptName");//科室名称
					 String deptCode = (String) dbCursor.get("deptCode");//科室名称
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
					document1.append("deptName", newList[1]);//统计时间
					document1.append("deptCode", newList[2]);//统计时间
					Document document = new Document();
					document.append("nums",map.get(key));//例数
					document.append("finalDate", newList[0]);//时间
					document.append("deptName", newList[1]);//科室名称
					document.append("deptCode", newList[2]);//统计时间
					new MongoBasicDao().update("OPERATIONNUMTOPDEPTYEARNEW", document1, document, true);
				}
			}
		}
	}
	
	/**
	 *  初始化手术例数医生前五按月份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsTopDocMonthToDB(String startDate, String endDate) {
		List<String> list=reMonthDay(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Integer> map=new HashMap<String,Integer>();//保存日期时间费用名称
			String temp;//月数据
			Integer dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,7);
				DBCursor cursor = new MongoBasicDao().findAlldata("OPERATIONNUMTOPDOCNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Integer value = (Integer) dbCursor.get("nums") ;//例数
					 String name = (String) dbCursor.get("docCode");//名称
					 String docName = (String) dbCursor.get("docName");//名称
					 temp1=temp+"&"+name+"&"+docName;
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
					document1.append("docCode", newList[1]);//医生code
					document1.append("docName", newList[2]);//医生名称
					Document document = new Document();
					document.append("nums",map.get(key));//例数
					document.append("finalDate", newList[0]);//时间
					document1.append("docCode", newList[1]);//医生code
					document1.append("docName", newList[2]);//医生名称
					new MongoBasicDao().update("OPERATIONNUMTOPDOCMONTHNEW", document1, document, true);
				}
			}
		}
		
	}
	/**
	 *  初始化手术例数医生前五按年份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsTopDocYearToDB(String startDate, String endDate) {
		List<String> list=reYearMonth(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Integer> map=new HashMap<String,Integer>();//保存日期时间费用名称
			String temp;//月数据
			Integer dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,4);
				DBCursor cursor = new MongoBasicDao().findAlldata("OPERATIONNUMTOPDOCMONTHNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Integer value = (Integer) dbCursor.get("nums") ;//金额
					 String name = (String) dbCursor.get("docCode");//名称
					 String docName = (String) dbCursor.get("docName");//名称
					 temp1=temp+"&"+name+"&"+docName;
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
					document1.append("docCode", newList[1]);//医生code
					document1.append("docName", newList[2]);//医生名称
					Document document = new Document();
					document.append("nums",map.get(key));//例数
					document.append("finalDate", newList[0]);//时间
					document1.append("docCode", newList[1]);//医生code
					document1.append("docName", newList[2]);//医生名称
					new MongoBasicDao().update("OPERATIONNUMTOPDOCYEARNEW", document1, document, true);
				}
			}
		}
		
	}
	
	/**
	 * 初始化手术例数同环比按月份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsYoyRatioMonthToDB(String startDate, String endDate) {
		List<String> list=reMonthDay(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Integer> map=new HashMap<String,Integer>();//保存日期时间费用名称
			String temp;//月数据
			Integer dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,7);
				DBCursor cursor = new MongoBasicDao().findAlldata("OPERATIONNUMYOYRATIONNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Integer value = (Integer) dbCursor.get("nums") ;//例数
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
					document.append("nums",map.get(key));//例数
					document.append("finalDate", newList[0]);//时间
					new MongoBasicDao().update("OPERATIONNUMYOYRATIONMONTHNEW", document1, document, true);
				}
			}
		}
		
		
	}
	/**
	 *初始化手术例数同环比按年份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsYoyRatioYearToDB(String startDate, String endDate) {
		List<String> list=reYearMonth(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Integer> map=new HashMap<String,Integer>();//保存日期时间费用名称
			String temp;//月数据
			Integer dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,4);
				DBCursor cursor = new MongoBasicDao().findAlldata("OPERATIONNUMYOYRATIONMONTHNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Integer value = (Integer) dbCursor.get("nums") ;//金额
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
					document.append("nums",map.get(key));//例数
					document.append("finalDate", newList[0]);//时间
					new MongoBasicDao().update("OPERATIONNUMYOYRATIONYEARNEW", document1, document, true);
				}
			}
		}
		
		
	}
	/**
	 *初始化手术例数同环比按天
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsOpTypeToDB(String startDate, String endDate) {
		boolean times=timeDifference(startDate,endDate);
		Map<String,String> dateTime;
		if(!times){
			dateTime=queryBetweenTime(startDate,endDate,0,new HashMap<String, String>());
		}else{
			dateTime=queryBetweenTime(startDate,endDate,1,new HashMap<String, String>());
		}
		
		for(String begin:dateTime.keySet()){
			 StringBuffer buffer=new StringBuffer();
			 buffer.append("select count(1) as nums, finalDate ,opType , b.code_name as opTypeName  from (select  to_char(t.createtime, 'yyyy-MM-dd') as finalDate,t.ops_kind as opType ");
			 buffer.append(" from t_operation_record t where t.ynvalid = 1 and t.stop_flg = 0  and t.del_flg = 0  and t.createtime between ");
			 buffer.append(" to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+dateTime.get(begin)+"','yyyy-MM-dd HH24:MI:SS')) ");
			 buffer.append("left join t_business_dictionary b on opType=b.code_encode  and b.stop_flg=0 and b.del_flg=0 and b.code_type= 'operatetype' ");
			 buffer.append(" group by finalDate,opType, b.code_name");
			 List<OperationNumsVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<OperationNumsVo>() {
					@Override
					public OperationNumsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
						OperationNumsVo vo = new OperationNumsVo();
						vo.setNums(rs.getInt("nums"));
						vo.setOpType(rs.getString("opType"));
						vo.setFinalDate(rs.getString("finalDate"));
						vo.setOpTypeName(rs.getString("opTypeName"));
						return vo;
					}
				});
			 
			 if(list!=null && list.size()>0){
				 for(OperationNumsVo vo:list){
					 Integer nums=0;
					 if(times){
						 BasicDBObject bdObject = new BasicDBObject();
						 bdObject.append("finalDate", vo.getFinalDate());
						 bdObject.append("opType", vo.getOpType());
						 bdObject.append("opTypeName", vo.getOpTypeName());
						 DBCursor cursor = new MongoBasicDao().findAlldata("OPERATIONNUMOPTYPENEW", bdObject);
						 DBObject dbCursor;
							List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
							while(cursor.hasNext()){
								OperationNumsVo voOne=new  OperationNumsVo();
								 dbCursor = cursor.next();
								 nums =(Integer)dbCursor.get("nums");//例数
							}
					   }
						Document document1 = new Document();
						document1.append("finalDate", vo.getFinalDate());//统计时间
						document1.append("opType", vo.getOpType());
						document1.append("opTypeName", vo.getOpTypeName());
						Document document = new Document();
						document.append("finalDate", vo.getFinalDate());//统计时间
						document.append("nums", vo.getNums()+nums);//例数
						document.append("opType", vo.getOpType());
						document.append("opTypeName", vo.getOpTypeName());
						new MongoBasicDao().update("OPERATIONNUMOPTYPENEW", document1, document, true);
				}
			}
		}
		
	}

	/**
	 *初始化手术例数同环比按月份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsOpTypeMonthToDB(String startDate, String endDate) {
		List<String> list=reMonthDay(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Integer> map=new HashMap<String,Integer>();//保存日期时间费用名称
			String temp;//月数据
			Integer dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,7);
				DBCursor cursor = new MongoBasicDao().findAlldata("OPERATIONNUMOPTYPENEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Integer value = (Integer) dbCursor.get("nums") ;//例数
					 String name = (String) dbCursor.get("opType");//类别code
					 String opTypeName = (String) dbCursor.get("opTypeName");//名称
					 temp1=temp+"&"+name+"&"+opTypeName;
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
				Document document1 = new Document();
				document1.append("finalDate", newList[0]);
				document1.append("opType", newList[1]);
				document1.append("opTypeName", newList[2]);
				Document document = new Document();
				document.append("nums",map.get(key));//例数
				document.append("finalDate", newList[0]);//时间
				document.append("opType", newList[1]);
				document.append("opTypeName", newList[2]);
				new MongoBasicDao().update("OPERATIONNUMOPTYPEMONORYEARNEW", document1, document, true);
			}
		}
		
	}
	/**
	 *初始化手术例数同环比按年份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsOpTypeYearToDB(String startDate, String endDate) {
		List<String> list=reYearMonth(startDate,endDate,new ArrayList<String>());
		if(list!=null && list.size()>0){
			List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Integer> map=new HashMap<String,Integer>();//保存日期时间费用名称
			String temp;//月数据
			Integer dou;
			String temp1;
			for(String st:list){//获取时间段天数
				bdObject.append("finalDate", st);
				temp=st.substring(0,4);
				DBCursor cursor = new MongoBasicDao().findAlldata("OPERATIONNUMOPTYPEMONORYEARNEW", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Integer value = (Integer) dbCursor.get("nums") ;//例数
					 String name = (String) dbCursor.get("opType");//类别code
					 String opTypeName = (String) dbCursor.get("opTypeName");//名称
					 temp1=temp+"&"+name+"&"+opTypeName;
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
				Document document1 = new Document();
				document1.append("finalDate", newList[0]);
				document1.append("opType", newList[1]);
				document1.append("opTypeName", newList[2]);
				Document document = new Document();
				document.append("nums",map.get(key));//例数
				document.append("finalDate", newList[0]);//时间
				document.append("opType", newList[1]);
				document.append("opTypeName", newList[2]);
				new MongoBasicDao().update("OPERATIONNUMOPTYPEMONORYEARNEW", document1, document, true);
			}
		}
		
	}
	 /**
	 *查询在做或已完成（当天）
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public List<OperationNumsVo> getDoingOrFinish(String startDate,String endDate) {
		StringBuffer sb=new StringBuffer();
		sb.append("select count(sumNums) as sumNums ,finalType , districtname ,district from (select t.op_id as sumNums , 'zz' as finalType,d.DEPT_AREA_NAME as districtname,d.DEPT_AREA_CODE as district    from t_operation_apply t ");
		sb.append("  left join t_department d on t.exec_dept=d.dept_code  where t.pre_date + t.duration / 24 >  sysdate  ");
		sb.append(" and t.pre_date between  to_date(:startDate, 'yyyy-MM-dd HH24:MI:SS') and  to_date(:endDate, 'yyyy-MM-dd HH24:MI:SS ') ");
		sb.append(" and t.status != 4 and t.isfinished = 0 and t.stop_flg = 0  and t.del_flg = 0");
		sb.append(" union all select t.id as sumNums ,'wc' as finalType,d.DEPT_AREA_NAME as districtname,d.DEPT_AREA_CODE as district    from T_OPERATION_RECORD t ");
		sb.append(" left join t_department d on t.exec_dept=d.dept_code where t.createtime between");
		sb.append(" to_date(:startDate, 'yyyy-MM-dd HH24:MI:SS') and  to_date(:endDate, 'yyyy-MM-dd HH24:MI:SS ')");
		sb.append(" and t.ynvalid = 1 and t.stop_flg = 0 and t.del_flg = 0  )group by districtname ,finalType,district");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		List<OperationNumsVo> list =  namedParameterJdbcTemplate.query(sb.toString(),paramMap,new RowMapper<OperationNumsVo>() {
				@Override
				public OperationNumsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					OperationNumsVo vo = new OperationNumsVo();
					vo.setNums(rs.getInt("sumNums"));
					vo.setFinalType(rs.getString("finalType"));
					vo.setDeptName(rs.getString("districtname"));
					vo.setDistrict(rs.getString("district"));
					vo.setDistrictname(rs.getString("districtname"));
					return vo;
				}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OperationNumsVo>();
	}
	
	/**
	 * 手术例数统计（住院门诊）mongodb
	 *  @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public List<OperationNumsVo> queryTotalCount(String searchTime,
			String dateSign) {
		BasicDBObject bdObject = new BasicDBObject();
		String tableName;//查询表
		if("1".equals(dateSign )){
			tableName="SSLSTJ_OPERNUMSMZJ_YEAR";
		}else if("2".equals(dateSign )){//月
			tableName="SSLSTJ_OPERNUMSMZJ_MONTH";
		}else{//日
			tableName="SSLSTJ_OPERNUMSMZJ_DAY";
		}
		bdObject.append("finalDate", searchTime);
		DBCursor cursor = new MongoBasicDao().findAlldata(tableName, bdObject);
		DBObject dbCursor;
		List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
		
		while(cursor.hasNext()){
			OperationNumsVo voOne=new  OperationNumsVo();
			 dbCursor = cursor.next();
			 Integer nums =(Integer)dbCursor.get("nums");//例数
			 String classType =(String)dbCursor.get("pasource");//来源
			 if("1".equals(classType)){
				 voOne.setName("门诊");
			 }else{
				 voOne.setName("住院");
			 }
			 voOne.setNums(nums);
			 list1.add(voOne);
		}
		return list1;
	}

	/**
	 * 手术例数统计（科室top5）mongodb
	 *  @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public List<OperationNumsVo> queryOperationNumsTopDept(String searchTime,
			String dateSign) {
		String tableName;//查询表
		if("1".equals(dateSign )){
			tableName="SSLSTJ_OPERNUMSDEPT_YEAR";
		}else if("2".equals(dateSign )){//月
			tableName="SSLSTJ_OPERNUMSDEPT_MONTH";
		}else{//日
			tableName="SSLSTJ_OPERNUMSDEPT_DAY";
		}
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("finalDate", searchTime);
		DBCursor cursor = new MongoBasicDao().findAlldataBySort(tableName, bdObject,"nums");
		DBObject dbCursor;
		List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
		int count=5;
		while(cursor.hasNext()){
			OperationNumsVo voOne=new  OperationNumsVo();
			 dbCursor = cursor.next();
			 Integer nums =(Integer)dbCursor.get("nums");//例数
			 String deptName =(String) dbCursor.get("deptName");//科室名称
			 voOne.setName(deptName);
			 voOne.setNums(nums);
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
	 * 手术例数统计（医生top5）mongodb
	 * @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public List<OperationNumsVo> queryOperationNumsTopDoc(String searchTime,
			String dateSign) {
		String tableName;//查询表
		if("1".equals(dateSign )){
			tableName="SSLSTJ_OPERNUMSDOC_YEAR";
		}else if("2".equals(dateSign )){//月
			tableName="SSLSTJ_OPERNUMSDOC_MONTH";
		}else{//日
			tableName="SSLSTJ_OPERNUMSDOC_DAY";
		}
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("finalDate", searchTime);
		DBCursor cursor = new MongoBasicDao().findAlldataBySort(tableName, bdObject,"nums");
		DBObject dbCursor;
		List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
		int count=5;
		while(cursor.hasNext()){
			 OperationNumsVo voOne=new  OperationNumsVo();
			 dbCursor = cursor.next();
			 Integer nums =(Integer)dbCursor.get("nums");//例数
			 String docName =(String) dbCursor.get("docName");//医生名称
			 voOne.setName(docName);
			 voOne.setNums(nums);
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
	 * 手术例数统计（手术类别）mongodb
	 * @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public List<OperationNumsVo> queryNumsOpType(String searchTime,
			String dateSign) {
		String tableName;//查询表
		if("1".equals(dateSign )){//年月
			tableName="SSLSTJ_OPERNUMSTYPE_YEAR";
		}else if("2".equals(dateSign )){
			tableName="SSLSTJ_OPERNUMSTYPE_MONTH";
		}else{//日
			tableName="SSLSTJ_OPERNUMSTYPE_DAY";
		}
		if(searchTime==null){
			return new ArrayList<OperationNumsVo>();
		}
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("finalDate", searchTime);
		DBCursor cursor = new MongoBasicDao().findAlldata(tableName, bdObject);
		DBObject dbCursor;
		List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
		while(cursor.hasNext()){
			 OperationNumsVo voOne=new  OperationNumsVo();
			 dbCursor = cursor.next();
			 Integer nums =(Integer)dbCursor.get("nums");//例数
			 String opTypeName =(String) dbCursor.get("opTypeName");//手术类别名称
			 voOne.setName(opTypeName);
			 voOne.setNums(nums);
			 list1.add(voOne);
		}
		return list1;
	}
	/**
	 * 手术例数统计（手术类别）数据库
	 * @author zhuxiaolu 
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @param endDate 结束时间
	 * @param startDate 开始时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public List<OperationNumsVo> queryNumsOpTypeToDB(List<String> tnL,
			String startDate, String endDate, String dateSign) {
		 String timeType="";
		 if("1".equals(dateSign)){
			 timeType="yyyy";
		 }else if("2".equals(dateSign)){
			 timeType="yyyy-MM";
		 }else{
			 timeType="yyyy-MM-dd";
		 }
		 StringBuffer buffer=new StringBuffer();
		 buffer.append("select count(1) as nums, finalDate ,opType , b.code_name as opTypeName  from ( ");
		 for(int i=0;i<tnL.size();i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			 buffer.append("select  to_char(t"+i+".createtime, :timeType) as finalDate,t"+i+".ops_kind as opType ");
			 buffer.append(" from "+tnL.get(i)+" t"+i+" where t"+i+".ynvalid = 1 and t"+i+".stop_flg = 0  and t"+i+".del_flg = 0  and t"+i+".createtime between ");
			 buffer.append(" to_date(:startDate,'yyyy-MM-dd HH24:MI:SS') and to_date(:endDate,'yyyy-MM-dd HH24:MI:SS') ");
			 
		 }
		 buffer.append(" ) left join t_business_dictionary b on opType=b.code_encode  and b.stop_flg=0 and b.del_flg=0 and b.code_type= 'operatetype' ");
		 buffer.append(" group by finalDate,opType, b.code_name");
		 Map<String,Object> paramMap = new HashMap<String, Object>();
		 paramMap.put("startDate", startDate);
		 paramMap.put("endDate", endDate);
		 paramMap.put("timeType", timeType);
		 List<OperationNumsVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<OperationNumsVo>() {
				@Override
				public OperationNumsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					OperationNumsVo vo = new OperationNumsVo();
					vo.setNums(rs.getInt("nums"));
					vo.setName(rs.getString("opTypeName"));
					return vo;
				}
		});
		if(list!=null&&list.size()>0){
				return list;
		}
		return new ArrayList<OperationNumsVo>();
	}
	/**
	 * 手术例数统计（医生前五）数据库
	 * @author zhuxiaolu 
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @param endDate 结束时间
	 * @param startDate 开始时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public List<OperationNumsVo> queryNumsTopDocToDB(List<String> tnL,
			String startDate, String endDate, String dateSign) {
		 String timeType="";
		 if("1".equals(dateSign)){
			 timeType="yyyy";
		 }else if("2".equals(dateSign)){
			 timeType="yyyy-MM";
		 }else{
			 timeType="yyyy-MM-dd";
		 }
		 StringBuffer buffer=new StringBuffer();
		 buffer.append(" select e.employee_name as docName, t3.createtime as finalDate , t3.num as nums,t3.OPS_DOCD as docCode  from (select t2.ops_docd,");
		 buffer.append(" t2.createtime,t2.num,row_number() over(partition by t2.createtime order by t2.num desc) row_num  from (");
		 buffer.append("select t1.ops_docd, count(1) as num, t1.createtime from (");
		 for(int i=0;i<tnL.size();i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			 buffer.append(" select to_char(t"+i+".createtime, :timeType) as createtime, t"+i+".ops_docd  from "+tnL.get(i)+" t"+i+" where");
			 buffer.append(" t"+i+".del_flg = 0 and t"+i+".stop_flg = 0 and t"+i+".ynvalid = '1'  and t"+i+".createtime between ");
			 buffer.append(" to_date(:startDate,'yyyy-MM-dd HH24:MI:SS') and to_date(:endDate,'yyyy-MM-dd HH24:MI:SS') ");
		 }
		 buffer.append(" ) t1 group by");
		 buffer.append(" t1.ops_docd, t1.createtime) t2) t3 left join t_employee e on t3.OPS_DOCD = e.employee_jobno  where t3.row_num < 6 ");
		 Map<String,Object> paramMap = new HashMap<String, Object>();
		 paramMap.put("startDate", startDate);
		 paramMap.put("endDate", endDate);
		 paramMap.put("timeType", timeType);
		 List<OperationNumsVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<OperationNumsVo>() {
				@Override
				public OperationNumsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					OperationNumsVo vo = new OperationNumsVo();
					vo.setNums(rs.getInt("nums"));
					vo.setName(rs.getString("docName"));
					return vo;
				}
		});
		if(list!=null&&list.size()>0){
				return list;
		}
		return new ArrayList<OperationNumsVo>();
	}
	
	/**
	 * 手术例数统计（科室前五）数据库
	 * @author zhuxiaolu 
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @param endDate 结束时间
	 * @param startDate 开始时间
	 * @param tnL 手术表分区
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public List<OperationNumsVo> queryNumsTopDeptToDB(List<String> tnL,
			String startDate, String endDate, String dateSign) {
		 String timeType="";
		 if("1".equals(dateSign)){
			 timeType="yyyy";
		 }else if("2".equals(dateSign)){
			 timeType="yyyy-MM";
		 }else{
			 timeType="yyyy-MM-dd";
		 }
		 StringBuffer buffer=new StringBuffer();
		 buffer.append(" select d.dept_name as deptName, t3.dept_code as deptCode, t3.createtime as finalDate, t3.num as nums from ");
		 buffer.append(" (select t2.dept_code,t2.createtime, t2.num,row_number() over(partition by t2.createtime order by t2.num desc) row_num from ");
		 buffer.append(" (select t1.dept_code, count(1) as num,t1.createtime from ( ");
		 for(int i=0;i<tnL.size();i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			 buffer.append(" select to_char(t"+i+".createtime, :timeType) as createtime,t"+i+".dept_code from "+tnL.get(i)+" t"+i);
			 buffer.append(" where t"+i+".del_flg = 0  and t"+i+".stop_flg = 0 and t"+i+".ynvalid = '1' and t"+i+".createtime between ");
			 buffer.append(" to_date(:startDate,'yyyy-MM-dd HH24:MI:SS') and to_date(:endDate,'yyyy-MM-dd HH24:MI:SS')");
		 }
		 buffer.append(" ) t1 group by t1.dept_code, t1.createtime) t2) t3 ");
		 buffer.append(" left join t_department d on t3.dept_code = d.dept_code  where t3.row_num < 6");
		 Map<String,Object> paramMap = new HashMap<String, Object>();
		 paramMap.put("startDate", startDate);
		 paramMap.put("endDate", endDate);
		 paramMap.put("timeType", timeType);
		 List<OperationNumsVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<OperationNumsVo>() {
			 @Override
				public OperationNumsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					OperationNumsVo vo = new OperationNumsVo();
					vo.setNums(rs.getInt("nums"));
					vo.setName(rs.getString("deptName"));
					return vo;
				}
			});
		if(list!=null&&list.size()>0){
				return list;
		}
		return new ArrayList<OperationNumsVo>();
	}

	/**
	 * 手术例数统计（门诊住院）数据库
	 * @author zhuxiaolu 
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @param endDate 结束时间
	 * @param startDate 开始时间
	 * @param tnL 手术表分区
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public List<OperationNumsVo> queryTotalCountToDB(List<String> tnL,
			String startDate, String endDate, String dateSign) {
		 String timeType="";
		 if("1".equals(dateSign)){
			 timeType="yyyy";
		 }else if("2".equals(dateSign)){
			 timeType="yyyy-MM";
		 }else{
			 timeType="yyyy-MM-dd";
		 }
		 StringBuffer buffer=new StringBuffer();
		 buffer.append("select count(1) as nums, finalDate ,pasource from (");
		 for(int i=0;i<tnL.size();i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			 buffer.append(" select t"+i+".pasource,  to_char(t"+i+".createtime, :timeType) as finalDate ");
			 buffer.append(" from "+tnL.get(i)+" t"+i+" where t"+i+".ynvalid = 1 and t"+i+".stop_flg = 0  and t"+i+".del_flg = 0  and t"+i+".createtime between ");
			 buffer.append(" to_date(:startDate,'yyyy-MM-dd HH24:MI:SS') and to_date(:endDate,'yyyy-MM-dd HH24:MI:SS')");
		 }
		 buffer.append(" ) group by finalDate,pasource");
		 Map<String,Object> paramMap = new HashMap<String, Object>();
		 paramMap.put("startDate", startDate);
		 paramMap.put("endDate", endDate);
		 paramMap.put("timeType", timeType);
		 List<OperationNumsVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<OperationNumsVo>() {
				@Override
				public OperationNumsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					//0整体 2郑东 3惠济 0-2-3=河医院区
					OperationNumsVo vo = new OperationNumsVo();
					vo.setMzssNum(0);//门诊手术例数
					vo.setZyssNum(0);//住院手术例数
					vo.setDistrictname("");//院区
					vo.setNums(rs.getInt("nums"));
					vo.setFinalDate(rs.getString("finalDate"));//手术时间
					if("1".equals(rs.getString("pasource"))){
						vo.setName("门诊");
					}else{
						vo.setName("住院");
					}
					return vo;
				}
			});
		if(list!=null&&list.size()>0){
				return list;
		}
		return new ArrayList<OperationNumsVo>();
	}
	
	/**
	 * 查询已完成手术例数（昨天）
	 * @author zhuxiaolu 
	 * @param endDate 结束时间
	 * @param startDate 开始时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public List<OperationNumsVo> getYesterDayFinish(String startDate,
			String endDate) {
		StringBuffer sb=new StringBuffer();
		sb.append("  select count(t.id) as sumNums ,d.DEPT_AREA_NAME as districtname,d.DEPT_AREA_CODE as district    from t_operation_record t ");
		sb.append(" left join t_department d on t.exec_dept=d.dept_code where t.createtime between");
		sb.append(" to_date(:startDate, 'yyyy-MM-dd HH24:MI:SS') and  to_date(:endDate, 'yyyy-MM-dd HH24:MI:SS ')");
		sb.append(" and t.ynvalid = 1 and t.stop_flg = 0 and t.del_flg = 0  ");
		sb.append(" group by DEPT_AREA_NAME ,DEPT_AREA_CODE");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		List<OperationNumsVo> list =  namedParameterJdbcTemplate.query(sb.toString(),paramMap,new RowMapper<OperationNumsVo>() {
				@Override
				public OperationNumsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					OperationNumsVo vo = new OperationNumsVo();
					vo.setNums(rs.getInt("sumNums"));
					vo.setDeptName(rs.getString("districtname"));
					vo.setDistrict(rs.getString("district"));
					vo.setDistrictname(rs.getString("districtname"));
					return vo;
				}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OperationNumsVo>();
	}

	/**
	 * 手术例数统计环比mongodb
	 * @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public List<OperationNumsVo> queryRatioCount(String searchTime,
			String dateSign) {
		String tableName;//查询表
		if("1".equals(dateSign )){
			tableName="SSLSTJ_OPERNUMSYR_YEAR";
		}else if("2".equals(dateSign )){//月
			tableName="SSLSTJ_OPERNUMSYR_MONTH";
		}else{//日
			tableName="SSLSTJ_OPERNUMSYR_DAY";
		}
		String[] startDate=this.RatioDate(searchTime, dateSign);//开始时间
		if(searchTime==null){
			return new ArrayList<OperationNumsVo>();
		}
		int eLength=searchTime.length();
		if("1".equals(dateSign)){
			if(eLength!=4){
				return new ArrayList<OperationNumsVo>();
			}
		}else if("2".equals(dateSign)){//月
			if(eLength!=7){
				return new ArrayList<OperationNumsVo>();
			}
		}else{//日
			if(eLength!=10){
				return new ArrayList<OperationNumsVo>();
			}
		}
		BasicDBObject bdObject = new BasicDBObject();
		List<OperationNumsVo> list1=new ArrayList<OperationNumsVo>();
		for(String vo:startDate){
			bdObject.append("finalDate", vo);
			DBCursor cursor = new MongoBasicDao().findAlldata(tableName, bdObject);
			DBObject dbCursor;
			if(!cursor.hasNext()){
				 OperationNumsVo voOne=new  OperationNumsVo();
				 voOne.setNums(0);
			     voOne.setName(vo);
			     list1.add(voOne);
				continue;
			}
			while(cursor.hasNext()){
				OperationNumsVo voOne=new  OperationNumsVo();
				 dbCursor = cursor.next();
				 Integer nums =(Integer)dbCursor.get("nums");//例数
				 String finalDate =(String)dbCursor.get("finalDate");//日期
				 voOne.setNums(nums);
			     voOne.setName(finalDate);
				list1.add(voOne);
			}
		}
		return list1;
	}

	/**
	 * 手术例数统计同比mongodb
	 * @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public List<OperationNumsVo> queryYoyCount(String searchTime,
			String dateSign) {
		String tableName;//查询表
		if("1".equals(dateSign )){
			tableName="SSLSTJ_OPERNUMSYR_YEAR";
		}else if("2".equals(dateSign )){//月
			tableName="SSLSTJ_OPERNUMSYR_MONTH";
		}else{//日
			tableName="SSLSTJ_OPERNUMSYR_DAY";
		}
		if(searchTime==null){
			return new ArrayList<OperationNumsVo>();
		}
		int eLength=searchTime.length();
		if("2".equals(dateSign)){//月
			if(eLength!=7){
				return new ArrayList<OperationNumsVo>();
			}
		}else{//日
			if(eLength!=10){
				return new ArrayList<OperationNumsVo>();
			}
		}
		String[] startDate=this.yoyDate(searchTime, dateSign);//6月内时间数组
		List<OperationNumsVo> list=new ArrayList<OperationNumsVo>();
		BasicDBObject bdObject = new BasicDBObject();
		for(String dateVo:startDate){
			bdObject.append("finalDate", dateVo);
			DBCursor cursor = new MongoBasicDao().findAlldata(tableName, bdObject);
			DBObject dbCursor;
			if(!cursor.hasNext()){
				 OperationNumsVo voOne=new  OperationNumsVo();
				 voOne.setNums(0);
			     voOne.setName(dateVo);
				 list.add(voOne);
				continue;
			}
			while(cursor.hasNext()){
				 OperationNumsVo voOne=new  OperationNumsVo();
				 dbCursor = cursor.next();
				 Integer nums =(Integer)dbCursor.get("nums");//例数
				 String finalDate =(String)dbCursor.get("finalDate");//科室
				 voOne.setNums(nums);
			     voOne.setName(finalDate);
				 list.add(voOne);
			}
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
	 * 手术例数统计同比数据库
	 * @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public OperationNumsVo queryYoyCountToDB(String begin, String endTime,
			String dateSign) {
		String dateFormat="";
		if("1".equals(dateSign)){
			dateFormat="yyyy";
		}else if("2".equals(dateSign)){
			dateFormat="yyyy-MM";
		}else{
			dateFormat="yyyy-MM-dd";
		}
		 StringBuffer buffer=new StringBuffer();
		 buffer.append("select count(1) as nums, finalDate from (select  to_char(t.createtime, '"+dateFormat+"') as finalDate ");
		 buffer.append(" from t_operation_record t where t.ynvalid = 1 and t.stop_flg = 0  and t.del_flg = 0  and ");
		 buffer.append("  t.createtime>= to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and t.createtime< to_date('"+endTime+"','yyyy-MM-dd HH24:MI:SS')) group by finalDate ");
		 List<OperationNumsVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<OperationNumsVo>() {
				@Override
				public OperationNumsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					OperationNumsVo vo = new OperationNumsVo();
					vo.setNums(rs.getInt("nums"));
					vo.setName(rs.getString("finalDate"));
					return vo;
				}
			});
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
			OperationNumsVo vo = new OperationNumsVo();
			vo.setNums(0);
			if("1".equals(dateSign)){
				vo.setName(begin.substring(0, 4));
			}else if("2".equals(dateSign)){
				vo.setName(begin.substring(0, 7));
			}else{
				vo.setName(begin.substring(0, 10));
			}
			return vo;
	}
	/**
	 * 手术例数统计(普通或门诊)数据库
	 * @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public List<OperationNumsVo> queryJzOrPtCountToDB(List<String> tnL,
			String startDate, String endDate, String dateSign) {
		 StringBuffer buffer=new StringBuffer();
		 buffer.append("select  sum(sumNums) as nums, type from ( ");
		 for(int i=0;i<tnL.size();i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			 buffer.append("select  count(t"+i+".id) as sumNums ,'jz' as type ");
			 buffer.append(" from "+tnL.get(i)+" t"+i+" inner  join t_department d on t"+i+".exec_dept = d.dept_code and  d.dept_name like '急诊%'");
			 buffer.append(" where t"+i+".ynvalid = 1 and t"+i+".stop_flg = 0  and t"+i+".del_flg = 0  and t"+i+".createtime between ");
			 buffer.append(" to_date(:startDate,'yyyy-MM-dd HH24:MI:SS') and to_date(:endDate,'yyyy-MM-dd HH24:MI:SS') ");
			 buffer.append(" union all select  count(t"+i+".id) as sumNums ,'pt' as type ");
			 buffer.append(" from "+tnL.get(i)+" t"+i+" inner  join t_department d on t"+i+".exec_dept = d.dept_code and  d.dept_name not like '急诊%'");
			 buffer.append(" where t"+i+".ynvalid = 1 and t"+i+".stop_flg = 0  and t"+i+".del_flg = 0  and t"+i+".createtime between ");
			 buffer.append(" to_date(:startDate,'yyyy-MM-dd HH24:MI:SS') and to_date(:endDate,'yyyy-MM-dd HH24:MI:SS') ");
		 }
		 buffer.append(" ) group by type");
		 Map<String,Object> paramMap = new HashMap<String, Object>();
		 paramMap.put("startDate", startDate);
		 paramMap.put("endDate", endDate);
		 List<OperationNumsVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<OperationNumsVo>() {
				@Override
				public OperationNumsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					OperationNumsVo vo = new OperationNumsVo();
					vo.setNums(rs.getInt("nums"));
					if("jz".equals(rs.getString("type"))){
						vo.setName("急诊");
					}else{
						vo.setName("普通");
					}
					return vo;
				}
		});
		if(list!=null&&list.size()>0){
				return list;
		}
		return new ArrayList<OperationNumsVo>();
	}
	/**
	 * 查询手术例数
	 * @param startDate
	 * @param timeType 查询时间格式
	 * 			1：YYYY-dd-mm 按天
	 * 			2：YYYY-dd	    按月
	 * 			3：YYYY		    按年
	 * @return
	 */
	public List<OperationNumsVo> query(String startDate,Integer timeType){
		try{
			String startTime = "";
			String endTime = "";
			switch (timeType) {
			case 1:
				startTime = startDate;
				endTime = DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(startTime),1));
				break;
			case 2:
				startTime = startDate.substring(0, 7)+"-01";
				endTime = DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.parseDateY_M_D(startTime),1));
				break;
			case 3:
				startTime = startDate.substring(0, 4)+"-01-01";
				endTime = DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(startTime),1));
				break;
			default:
				break;
			}
			
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("select yq as district,nvl(sum(t.ZYSS_NUM),0) as zyssNum,nvl(sum(t.mzss_num),0) as mzssNum from T_BUSINESS_YZCX_ZHCX t  ");
			buffer.append("where t.oper_date >= to_date('"+startTime+"','yyyy-MM-dd') ");
			buffer.append("and t.oper_date < to_date('"+endTime+"','yyyy-MM-dd') ");
			buffer.append("group by yq ");
			 
			Map<String,Object> paramMap = new HashMap<String, Object>();
			List<OperationNumsVo> list =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<OperationNumsVo>() {
				@Override
				public OperationNumsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					OperationNumsVo vo = new OperationNumsVo();
					vo.setDistrict(rs.getString("district"));
					vo.setZyssNum(rs.getInt("zyssNum"));
					vo.setMzssNum(rs.getInt("mzssNum"));
					return vo;
				}
			});
			if(list!=null&&list.size()>0){
					return list;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ArrayList<OperationNumsVo>();
	}

	@Override
	public List<StatisticsVo> queryMom(String startDate, Integer timeType) {
		try{
			String startTime = "";
			String endTime = "";
			String type = "";
			
			switch (timeType) {
			case 1:
				startTime = startDate;
				endTime = DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(startTime),-6));
				type = "yyyy-MM-dd";
				break;
			case 2:
				startTime = DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.parseDateY_M_D(startDate.substring(0, 7)+"-01"), 1));
				endTime = DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.parseDateY_M_D(startTime),-6));
				startTime = startTime.substring(0, 7);
				endTime = endTime.substring(0, 7);
				type = "yyyy-mm";
				break;
			case 3:
				startTime = DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(startDate.substring(0, 4)+"-01-01"), 1));
				endTime = DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(startTime),-6));
				startTime = startTime.substring(0, 4);
				endTime = endTime.substring(0, 4);
				type = "yyyy";
				break;
			default:
				break;
			}
			
			StringBuffer sb = new StringBuffer();
			sb.append("select to_char(t.oper_date,'"+type+"') as name ,(sum(t.ZYSS_NUM)+sum(t.mzss_num)) as value from T_BUSINESS_YZCX_ZHCX t ");
			sb.append("where t.yq = 0 ");
			sb.append("and t.oper_date > to_date('"+endTime+"', '"+type+"') ");
			sb.append("and t.oper_date <= to_date('"+startTime+"', '"+type+"') ");
			sb.append("group by to_char(t.oper_date,'"+type+"') ");
			sb.append("order by to_char(t.oper_date,'"+type+"') ");
			
			Map<String,Object> paramMap = new HashMap<String, Object>();
			List<StatisticsVo> list =  namedParameterJdbcTemplate.query(sb.toString(),paramMap,new RowMapper<StatisticsVo>() {
				@Override
				public StatisticsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					StatisticsVo vo = new StatisticsVo();
					vo.setName(rs.getString("name"));
					vo.setValue(rs.getDouble("value"));
					return vo;
				}
			});
			if(list!=null&&list.size()>0){
				if(list.size()>6){
					list.remove(6);
				}
				return list;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ArrayList<StatisticsVo>();
	}

	@Override
	public List<StatisticsVo> queryYoy(String startDate, String endDate) {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("select nvl((sum(t.ZYSS_NUM) + sum(t.mzss_num)),0) as value ");
			sb.append("from T_BUSINESS_YZCX_ZHCX t ");
			sb.append("where t.yq = 0 ");
			sb.append("and t.oper_date > to_date('"+startDate+"', 'yyyy-MM-dd') ");
			sb.append("and t.oper_date <= to_date('"+endDate+"', 'yyyy-MM-dd') ");
			
			Map<String,Object> paramMap = new HashMap<String, Object>();
			List<StatisticsVo> list =  namedParameterJdbcTemplate.query(sb.toString(),paramMap,new RowMapper<StatisticsVo>() {
				@Override
				public StatisticsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					StatisticsVo vo = new StatisticsVo();
//					vo.setName(rs.getString("name"));
					vo.setValue(rs.getDouble("value"));
					return vo;
				}
			});
			if(list!=null&&list.size()>0){
					return list;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ArrayList<StatisticsVo>();
	}

	@Override
	public List<OperationNumsVo> queryForMongoDB(String startDate,
			Integer timeType) {
		String queryCollection="SSLSTJ_OPERNUMSDEPT_";
		String queryMzZyCollection="SSLSTJ_OPERNUMSMZJ_";
		if(1==timeType){
			queryCollection+="DAY";
			queryMzZyCollection+="DAY";
		}else if(2==timeType){
			queryCollection+="MONTH";
			queryMzZyCollection+="MONTH";
		}else{
			queryCollection+="YEAR";
			queryMzZyCollection+="YEAR";
		}
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("finalDate", startDate);
		DBCursor cursor=new MongoBasicDao().findAlldata(queryCollection, bdObject);
		DBObject dbCursor;
		String deptCodes=null;//获取科室
		//获取三个院区数据
		List<OperationNumsVo> list= new ArrayList<OperationNumsVo>();
		while(cursor.hasNext()){
			OperationNumsVo vo=new OperationNumsVo();
			dbCursor=cursor.next();
			String deptCode=(String) dbCursor.get("deptCode");
			
			if(StringUtils.isBlank(deptCodes)){
				deptCodes=deptCode;
			}else{
				deptCodes+=",";
				deptCodes+=deptCode;
			}
			vo.setDeptCode(deptCode);
			vo.setNums((Integer)dbCursor.get("nums"));
			list.add(vo);
		}
		//获取院区
		Map<String,OperationNumsVo> voMap=new HashMap<String,OperationNumsVo>();
		if(StringUtils.isNotBlank(deptCodes)){
			deptCodes=deptCodes.replace(",", "','");
			String sql="select t.dept_code deptCode,t.dept_area_code area from t_department t where t.dept_code in('"+deptCodes+"') ";
			List<HashMap<String, String>> Listmap=namedParameterJdbcTemplate.query(sql, new RowMapper<HashMap<String, String>>(){
				@Override
				public HashMap mapRow(ResultSet rs, int rowNum) throws SQLException {
					HashMap<String, String> mapDept=new HashMap<String, String>();
					mapDept.put(rs.getString("deptCode"), rs.getString("area"));
					return mapDept;
				}
				
			});
			Map<String,String> map=new HashMap<String,String>();
			for(HashMap<String, String> vo:Listmap){
				map.putAll(vo);
			}
			for(OperationNumsVo vo:list){
				String deptArea=map.get(vo.getDeptCode());
				if(StringUtils.isNotBlank(deptArea)){
					OperationNumsVo voArea=voMap.get(deptArea);
					if(voArea==null){
						voArea=new OperationNumsVo();
						voArea.setDistrict(deptArea);
						voArea.setMzssNum(0);
						voArea.setZyssNum(vo.getNums());
					}else{
						voArea.setZyssNum(voArea.getZyssNum()+vo.getNums());
					}
					voMap.put(deptArea, voArea);
				}
			}
		}
		list.clear();
		if(!voMap.isEmpty()){
			for(String key:voMap.keySet()){
				list.add(voMap.get(key));
			}
		}
		//获取门诊住院
		DBCursor cursor1=new MongoBasicDao().findAlldata(queryMzZyCollection, bdObject);
		OperationNumsVo vo=new OperationNumsVo();
		vo.setDistrict("0");
		while(cursor1.hasNext()){
			dbCursor=cursor1.next();
			String pasource=(String) dbCursor.get("pasource");
			if("1".equals(pasource)){
				vo.setMzssNum((Integer)dbCursor.get("nums"));
			}else if("2".equals(pasource)){
				vo.setZyssNum((Integer)dbCursor.get("nums"));
			}
		}
		if(vo.getMzssNum()==null){
			vo.setMzssNum(0);
		}
		if(vo.getZyssNum()==null){
			vo.setZyssNum(0);
		}
		list.add(vo);
		return list;
	}

	@Override
	public List<StatisticsVo> queryMoy(String mongoCollection, String begin,String end,Integer timeType) throws ParseException {
		BasicDBObject bdObject = new BasicDBObject();
		String datePoint=null;
//		if(StringUtils.isNotBlank(end)){
//			BasicDBList dateList=new BasicDBList();
//			BasicDBObject data1= new BasicDBObject();//查询开始时间
//			BasicDBObject data2= new BasicDBObject();//查询结束时间
//			data1.append("finalDate", new BasicDBObject("$gt",end));
//			data2.append("finalDate", new BasicDBObject("$lte",begin));
//			datePoint=begin;
//			dateList.add(data1);
//			dateList.add(data2);
//			bdObject.put("$and", dateList);
//		}else{
			bdObject.append("finalDate", begin);
//		}
		DBObject match = new BasicDBObject("$match", bdObject); 
		DBObject groupFields = new BasicDBObject("_id", new BasicDBObject("finalDate", "$finalDate")); 
		groupFields.put("nums", new BasicDBObject("$sum","$nums"));
		
		DBObject group = new BasicDBObject("$group", groupFields);
		AggregationOutput output =new MongoBasicDao().findGroupBy(mongoCollection, match, group);
		Iterator<DBObject> it = output.results().iterator();
		List<StatisticsVo> list=new ArrayList<StatisticsVo>();
		StatisticsVo vo=new StatisticsVo();
		while(it.hasNext()){
			BasicDBObject dbo = ( BasicDBObject ) it.next();
			BasicDBObject keyValus = (BasicDBObject)dbo.get("_id");
			
			vo.setName(keyValus.getString("finalDate"));
			vo.setValue(dbo.getDouble("nums"));
			
		}
		list.add(vo);
		if(list.size()>0){
			return list;
		}
		vo.setValue(0.0d);
		list.add(vo);
		return list;
	}
	
}
