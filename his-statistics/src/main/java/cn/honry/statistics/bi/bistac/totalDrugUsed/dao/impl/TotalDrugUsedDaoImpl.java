package cn.honry.statistics.bi.bistac.totalDrugUsed.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.bson.Document;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.vo.ListTotalIncomeStaticVo;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.MonthlyDashboardVo;
import cn.honry.statistics.bi.bistac.totalDrugUsed.dao.TotalDrugUsedDao;
@Repository("totalDrugUsedDao")
@SuppressWarnings({ "all" })
public class TotalDrugUsedDaoImpl extends  HibernateEntityDao<Dashboard> implements TotalDrugUsedDao{
	SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfMonth=new SimpleDateFormat("yyyy-MM");

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Override
	public boolean saveDrugTotalToDBYear() {
		boolean sign =false;
		List<String> list1=new ArrayList<String>();
		list1.add("yyyy");
		list1.add("yyyy-mm");
		list1.add("yyyy-mm-dd");
		for(String date:list1){
			StringBuffer buffer=new StringBuffer(500);
			buffer.append("select c.stat as stat,c.name as name,sum(c.douValue) as douValue  from( ");
			buffer.append("select b.code_name as stat,to_char(ti.name,'"+date+"') AS name,sum(ti.value) as douValue from  ");
			buffer.append("( select t.drug_code as drugCode ,t.fee_date as name,t.tot_cost as value ");
			buffer.append("from t_inpatient_medicinelist_now t  ");
			buffer.append("union all ");
			buffer.append("select t1.drug_code as stat ,t1.fee_date as name,t1.tot_cost as value ");
			buffer.append("from t_inpatient_medicinelist t1 ) ti ");
			buffer.append("left join t_drug_info d on ti.drugCode = d.drug_code ");
			buffer.append("left join t_business_dictionary b on d.DRUG_TYPE = b.code_encode ");
			buffer.append("where b.code_type = 'drugType' group by ti.name,ti.drugCode,b.code_name ");
			buffer.append(") c group by  c.stat, c.name ");
			List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
					.addScalar("stat").addScalar("name").addScalar("douValue",Hibernate.DOUBLE)
					.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
				if(list!=null && list.size()>0){
					 List<DBObject> userList = new ArrayList<DBObject>();
						for(Dashboard vo:list){
							Document document1 = new Document();
							document1.append("stat", vo.getStat());//药品分类
							document1.append("name", vo.getName());//药品统计时间
							Document document = new Document();
							document.append("stat", vo.getStat());
							document.append("name", vo.getName());
							document.append("douValue", vo.getDouValue());//金额
							new MongoBasicDao().update("DRUGTOTALFORAPP", document1, document, true);
						}
						sign=true;
				}
		}
		return sign;
	}


	@Override
	public void saveDrugTotalTopFiveDeptToDBYear() {
		List<String> list1=new ArrayList<String>();
		list1.add("yyyy");
		list1.add("yyyy-mm");
		list1.add("yyyy-mm-dd");
		for(String date:list1){
		 StringBuffer buffer=new StringBuffer(500);
		 buffer.append("select  f.name as name,f.douValue as douValue,(select e.dept_name from t_department e where e.dept_code=f.dept  ) as dept from( ");
		 buffer.append("select c.name as name,sum(c.douValue) as douValue,c.dept as dept  from( ");
		 buffer.append("select to_char(ti.name,'"+date+"') AS name,sum(ti.value) as douValue,ti.dept as dept from ");
		 buffer.append("(select t.fee_date as name,t.tot_cost as value,t.execute_deptCode as dept ");
		 buffer.append("from t_inpatient_medicinelist_now t ");
		 buffer.append("union all ");
		 buffer.append("select t1.fee_date as name,t1.tot_cost as value,t1.execute_deptCode as dept ");
		 buffer.append("from t_inpatient_medicinelist t1) ti ");
		 buffer.append("group by ti.name,ti.dept ");
		 buffer.append(") c group by c.name ,c.dept ");
		 buffer.append(") f ");
		 List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
					.addScalar("name").addScalar("douValue",Hibernate.DOUBLE).addScalar("dept")
					.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
		 if(list!=null && list.size()>0){
			 List<DBObject> userList = new ArrayList<DBObject>();
				for(Dashboard vo:list){
					Document document1 = new Document();
					document1.append("dept", vo.getDept());//科室
					document1.append("name", vo.getName());//药品统计时间
					Document document = new Document();
					document.append("name", vo.getName());
					document.append("douValue", vo.getDouValue());//金额
					document1.append("dept", vo.getDept());//科室
					new MongoBasicDao().update("TOPDEPTDRUGTOTALFORAPP", document1, document, true);
				}
		 }
		}
	}


	@Override
	public void saveDrugTotalTopFiveDocToDBYear() {
		List<String> list1=new ArrayList<String>();
		list1.add("yyyy");
		list1.add("yyyy-mm");
		list1.add("yyyy-mm-dd");
		for(String date:list1){
			
		StringBuffer buffer=new StringBuffer(400);
		buffer.append("select  f.name as name,f.douValue,(select e.employee_name from t_employee e where e.employee_code=f.doc  ) as doctor from( ");
		buffer.append("select c.name as name,sum(c.douValue) as douValue,c.doc as doc  from( ");
		buffer.append("select to_char(ti.name,'"+date+"') AS name,sum(ti.value) as douValue,ti.doc as doc from ");
		buffer.append("(select t.fee_date as name,t.tot_cost as value,t.recipe_doccode as doc ");
		buffer.append("from t_inpatient_medicinelist_now t ");
		buffer.append("union all ");
		buffer.append("select t1.fee_date as name,t1.tot_cost as value,t1.recipe_doccode as doc ");
		buffer.append("from t_inpatient_medicinelist_now t1) ti ");
		buffer.append("group by ti.name,ti.doc ) c group by  c.name ,c.doc ) f ");
		 List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
					.addScalar("name").addScalar("douValue",Hibernate.DOUBLE).addScalar("doctor")
					.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
		if(list!=null && list.size()>0){
			 List<DBObject> userList = new ArrayList<DBObject>();
				for(Dashboard vo:list){
					Document document1 = new Document();
					document1.append("doctor", vo.getDoctor());//科室
					document1.append("name", vo.getName());//药品统计时间
					Document document = new Document();
					document.append("name", vo.getName());
					document.append("douValue", vo.getDouValue());//金额
					document1.append("doctor", vo.getDoctor());//科室
					new MongoBasicDao().update("TOPDOCDRUGTOTALFORAPP", document1, document, true);
				}
		 }
		}
	}

/******************************日*******************************************************************************/
	@Override
	public void initGroupDrugByOneDay(List<String> tnL, String begin,
			String end, Integer hours) {
		
		StringBuffer buffer=new StringBuffer(500);
		buffer.append("select c.stat as stat,c.name as name,sum(c.douValue) as douValue  from( ");
		buffer.append("select b.code_name as stat,to_char(ti.name,'yyyy-mm-dd') AS name,sum(ti.value) as douValue from ( ");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("select t"+i+".drug_code as drugCode ,t"+i+".fee_date as name,t"+i+".tot_cost as value ");
			buffer.append("from "+tnL.get(i)+" t"+i+"  ");
			buffer.append("where t"+i+".fee_date >=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date <to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		}
		buffer.append(") ti left join t_drug_info d on ti.drugCode = d.drug_code ");
		buffer.append("left join t_business_dictionary b on d.DRUG_TYPE = b.code_encode ");
		buffer.append("where b.code_type = 'drugType' group by ti.name,ti.drugCode,b.code_name ");
		buffer.append(") c group by  c.stat, c.name ");
		
		List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
				.addScalar("stat").addScalar("name").addScalar("douValue",Hibernate.DOUBLE)
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
			if(list!=null && list.size()>0){
				 List<DBObject> userList = new ArrayList<DBObject>();
					for(Dashboard vo:list){
						if(vo.getDouValue()!=null){
							if(hours!=null){
								BasicDBObject bdObject = new BasicDBObject();
								bdObject.append("name", vo.getName());
								bdObject.append("stat", vo.getStat());
								DBCursor cursor = new MongoBasicDao().findAlldata("DRUGTOTALFORAPP", bdObject);
								DBObject dbCursor;
								Double dou=vo.getDouValue();
								while(cursor.hasNext()){
									 dbCursor = cursor.next();
									 dou+=(Double) dbCursor.get("douValue");
									}
								vo.setDouValue(dou);
							}
							Document document1 = new Document();
							document1.append("stat", vo.getStat());//药品分类
							document1.append("name", vo.getName());//药品统计时间
							Document document = new Document();
							document.append("stat", vo.getStat());
							document.append("name", vo.getName());
							document.append("douValue", vo.getDouValue());//金额
							new MongoBasicDao().update("DRUGTOTALFORAPP", document1, document, true);
						}
					}
						
			}
	}


	@Override
	public void InitDeptDrugByOneDay(List<String> tnL, String begin,
			String end, Integer hours) {
		StringBuffer buffer=new StringBuffer(500);
		 buffer.append("select  f.name as name,f.douValue as douValue,(select e.dept_name from t_department e where e.dept_code=f.dept  ) as dept from( ");
		 buffer.append("select c.name as name,sum(c.douValue) as douValue,c.dept as dept  from( ");
		 buffer.append("select to_char(ti.name,'yyyy-mm-dd') AS name,sum(ti.value) as douValue,ti.dept as dept from ( ");
		 for(int i=0,len=tnL.size();i<len;i++){
			 if(i>0){
				 buffer.append(" union all ");
			 }
			 buffer.append("select t"+i+".fee_date as name,t"+i+".tot_cost as value,t"+i+".execute_deptCode as dept ");
			 buffer.append("from "+tnL.get(i)+" t"+i+" ");
			 buffer.append("where t"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		 }
		 buffer.append(") ti group by ti.name,ti.dept ");
		 buffer.append(") c group by c.name ,c.dept ");
		 buffer.append(") f ");
		 List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
					.addScalar("name").addScalar("douValue",Hibernate.DOUBLE).addScalar("dept")
					.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
		 if(list!=null && list.size()>0){
			 List<DBObject> userList = new ArrayList<DBObject>();
				for(Dashboard vo:list){
					if(vo.getDouValue()!=null){
						if(hours!=null){
							BasicDBObject bdObject=new BasicDBObject();
							bdObject.append("name", vo.getName());
							bdObject.append("dept", vo.getDept());
							DBCursor cursor = new MongoBasicDao().findAlldata("TOPDEPTDRUGTOTALFORAPP",bdObject);
							DBObject dbCursor;
							Double dou=vo.getDouValue();
							while(cursor.hasNext()){
								 dbCursor = cursor.next();
								 dou+=(Double) dbCursor.get("douValue");
								}
							vo.setDouValue(dou);
						}
					Document document1 = new Document();
					document1.append("dept", vo.getDept());//科室
					document1.append("name", vo.getName());//药品统计时间
					Document document = new Document();
					document.append("name", vo.getName());
					document.append("douValue", vo.getDouValue());//金额
					document1.append("dept", vo.getDept());//科室
					new MongoBasicDao().update("TOPDEPTDRUGTOTALFORAPP", document1, document, true);
					}
				}
		 }
	
	}


	@Override
	public void InitDocDrugByOneDay(List<String> tnL, String begin, String end,
			Integer hours) {
		StringBuffer buffer=new StringBuffer(400);
		buffer.append("select  f.name as name,f.douValue,(select e.employee_name from t_employee e where e.employee_code=f.doc  ) as doctor from( ");
		buffer.append("select c.name as name,sum(c.douValue) as douValue,c.doc as doc  from( ");
		buffer.append("select to_char(ti.name,'yyyy-mm-dd') AS name,sum(ti.value) as douValue,ti.doc as doc from ( ");
		for(int i=0,len=tnL.size();i<len;i++){
			 if(i>0){
				 buffer.append(" union all ");
			 }
			 buffer.append("select t"+i+".fee_date as name,t"+i+".tot_cost as value,t"+i+".recipe_doccode as doc ");
			 buffer.append("from "+tnL.get(i)+" t"+i+" ");
			 buffer.append("where t"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		 }
		buffer.append(") ti group by ti.name,ti.doc ) c group by  c.name ,c.doc ) f ");
		 List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
					.addScalar("name").addScalar("douValue",Hibernate.DOUBLE).addScalar("doctor")
					.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
		if(list!=null && list.size()>0){
			 List<DBObject> userList = new ArrayList<DBObject>();
				for(Dashboard vo:list){
					if(vo.getDouValue()!=null){
						if(hours!=null){
							BasicDBObject bdObject=new BasicDBObject();
							bdObject.append("name", vo.getName());
							bdObject.append("doctor", vo.getDept());
							DBCursor cursor = new MongoBasicDao().findAlldata("TOPDOCDRUGTOTALFORAPP", bdObject);
							DBObject dbCursor;
							Double dou=vo.getDouValue();
							while(cursor.hasNext()){
								 dbCursor = cursor.next();
								dou+=(Double) dbCursor.get("douValue");
								}
							vo.setDouValue(dou);
						}
					
					Document document1 = new Document();
					document1.append("doctor", vo.getDoctor());//药品
					document1.append("name", vo.getName());//药品统计时间
					Document document = new Document();
					document.append("name", vo.getName());
					document.append("douValue", vo.getDouValue());//金额
					document1.append("doctor", vo.getDoctor());//科室
					new MongoBasicDao().update("TOPDOCDRUGTOTALFORAPP", document1, document, true);
					}
				}
		 }
		
	}
/************************月*********************************************************************************************************************************************/
@Override
public void initGroupDrugByOneDay( String begin,String end){
		List<String> list=reMonthDay(begin,end,new ArrayList<String>());
		if(list!=null && list.size()>0){
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
		String temp;//月数据
		Double dou;
		String temp1;//key
		for(String st:list){//获取时间段天数
			bdObject.append("name", st);
			temp=st.substring(0,7);
			DBCursor cursor = new MongoBasicDao().findAlldata("DRUGTOTALFORAPP", bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				 dbCursor = cursor.next();
				 Double value = (Double) dbCursor.get("douValue") ;//金额
				 String name = (String) dbCursor.get("stat");//统计大类名称
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
			String[] stArr=new String[2];
			for(String key:map.keySet()){
				stArr=key.split("&");
				Document doucment1=new Document();
				doucment1.append("name",stArr[0]);
				doucment1.append("stat",stArr[1] );
				Document document = new Document();
				document.append("douValue",map.get(key));
				document.append("stat", stArr[1]);
				document.append("name", stArr[0]);
				new MongoBasicDao().update("DRUGTOTALFORAPP", doucment1, document, true);
			}
		}	
}
	@Override
	public void InitDeptDrugByOneDay( String begin,String end){
		List<String> list=reMonthDay(begin,end,new ArrayList<String>());
		if(list!=null && list.size()>0){
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
		String temp;//月数据
		Double dou;
		String temp1;//key
		for(String st:list){//获取时间段天数
			bdObject.append("name", st);
			temp=st.substring(0,7);
			DBCursor cursor = new MongoBasicDao().findAlldata("TOPDEPTDRUGTOTALFORAPP", bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				ListTotalIncomeStaticVo voOne=new  ListTotalIncomeStaticVo();
				 dbCursor = cursor.next();
				 Double value = (Double) dbCursor.get("douValue") ;//金额
				 String name = (String) dbCursor.get("dept");//科室
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
			String[] stArr=new String[2];
			for(String key:map.keySet()){
				stArr=key.split("&");
				Document doucment1=new Document();
				doucment1.append("name",stArr[0]);
				doucment1.append("dept",stArr[1] );
				Document document = new Document();
				document.append("douValue",map.get(key));
				document.append("dept", stArr[1]);
				document.append("name", stArr[0]);
				new MongoBasicDao().update("TOPDEPTDRUGTOTALFORAPP", doucment1, document, true);
				
			}
		}
		
	}
	@Override
	public void InitDocDrugByOneDay(String begin, String end){
		List<String> list=reMonthDay(begin,end,new ArrayList<String>());
		if(list!=null && list.size()>0){
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
		String temp;//月数据
		Double dou;
		String temp1;//key
		for(String st:list){//获取时间段天数
			bdObject.append("name", st);
			temp=st.substring(0,7);
			DBCursor cursor = new MongoBasicDao().findAlldata("TOPDOCDRUGTOTALFORAPP", bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				ListTotalIncomeStaticVo voOne=new  ListTotalIncomeStaticVo();
				 dbCursor = cursor.next();
				 Double value = (Double) dbCursor.get("douValue") ;//金额
				 String name = (String) dbCursor.get("doctor");//科室
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
			String[] stArr=new String[2];
			for(String key:map.keySet()){
				stArr=key.split("&");
				Document doucment1=new Document();
				doucment1.append("name",stArr[0]);
				doucment1.append("doctor",stArr[1] );
				Document document = new Document();
				document.append("douValue",map.get(key));
				document.append("doctor", stArr[1]);
				document.append("name", stArr[0]);
				new MongoBasicDao().update("TOPDOCDRUGTOTALFORAPP", doucment1, document, true);
				
			}
		}
	}
/*************************年**********************************************************************************************************************************************/
	@Override
	public void initGroupDrugYear( String begin,String end){
		List<String> list=reYearMonth(begin,end,new ArrayList<String>());
		if(list!=null && list.size()>0){
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
		String temp;//月数据
		Double dou;
		String temp1;//key
		for(String st:list){//获取时间段天数
			bdObject.append("name", st);
			temp=st.substring(0,4);
			DBCursor cursor = new MongoBasicDao().findAlldata("DRUGTOTALFORAPP", bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				ListTotalIncomeStaticVo voOne=new  ListTotalIncomeStaticVo();
				 dbCursor = cursor.next();
				 Double value = (Double) dbCursor.get("douValue") ;//金额
				 String name = (String) dbCursor.get("stat");//统计大类名称
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
			String[] stArr=new String[2];
			for(String key:map.keySet()){
				stArr=key.split("&");
				Document doucment1=new Document();
				doucment1.append("name",stArr[0]);
				doucment1.append("stat",stArr[1] );
				Document document = new Document();
				document.append("douValue",map.get(key));
				document.append("stat", stArr[1]);
				document.append("name", stArr[0]);
				new MongoBasicDao().update("DRUGTOTALFORAPP", doucment1, document, true);
				
			}
		}	

	}
	@Override
	public void InitDeptDrugYear( String begin,String end){
		List<String> list=reYearMonth(begin,end,new ArrayList<String>());
		if(list!=null && list.size()>0){
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
		String temp;//月数据
		Double dou;
		String temp1;//key
		for(String st:list){//获取时间段天数
			bdObject.append("name", st);
			temp=st.substring(0,4);
			DBCursor cursor = new MongoBasicDao().findAlldata("TOPDEPTDRUGTOTALFORAPP", bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				ListTotalIncomeStaticVo voOne=new  ListTotalIncomeStaticVo();
				 dbCursor = cursor.next();
				 Double value = (Double) dbCursor.get("value") ;//金额
				 String name = (String) dbCursor.get("dept");//科室
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
			String[] stArr=new String[2];
			for(String key:map.keySet()){
				stArr=key.split("&");
				Document doucment1=new Document();
				doucment1.append("name",stArr[0]);
				doucment1.append("dept",stArr[1] );
				Document document = new Document();
				document.append("value",map.get(key));
				document.append("dept", stArr[1]);
				document.append("name", stArr[0]);
				new MongoBasicDao().update("TOPDEPTDRUGTOTALFORAPP", doucment1, document, true);
				
			}
		}
	}
	@Override
	public void InitDocDrugYear(String begin, String end){
		List<String> list=reYearMonth(begin,end,new ArrayList<String>());
		if(list!=null && list.size()>0){
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
		String temp;//月数据
		Double dou;
		String temp1;//key
		for(String st:list){//获取时间段天数
			bdObject.append("name", st);
			temp=st.substring(0,4);
			DBCursor cursor = new MongoBasicDao().findAlldata("TOPDOCDRUGTOTALFORAPP", bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				ListTotalIncomeStaticVo voOne=new  ListTotalIncomeStaticVo();
				 dbCursor = cursor.next();
				 Double value = (Double) dbCursor.get("douValue") ;//金额
				 String name = (String) dbCursor.get("doctor");//科室
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
			String[] stArr=new String[2];
			for(String key:map.keySet()){
				stArr=key.split("&");
				Document doucment1=new Document();
				doucment1.append("name",stArr[0]);
				doucment1.append("doctor",stArr[1] );
				Document document = new Document();
				document.append("douValue",map.get(key));
				document.append("doctor", stArr[1]);
				document.append("name", stArr[0]);
				new MongoBasicDao().update("TOPDOCDRUGTOTALFORAPP", doucment1, document, true);
				
			}
		}
	}
/**************************时间计算方法**********************************************************************************************************************************************/
	//获取日期每个月的每天
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
					begin=sdf.format(ca.getTime());
					list.add(begin);
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
	//获取每年的每月
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
					begin=sdfMonth.format(ca.getTime());
					list.add(begin);
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
}
