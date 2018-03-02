package cn.honry.statistics.bi.bistac.totalUnDrugUsed.dao.impl;

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
import cn.honry.statistics.bi.bistac.totalUnDrugUsed.dao.TotalUnDrugUsedDao;
@Repository("totalUnDrugUsedDao")
@SuppressWarnings({ "all" })
public class TotalUnDrugUsedDaoImpl extends  HibernateEntityDao<Dashboard> implements TotalUnDrugUsedDao{
	SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfMonth=new SimpleDateFormat("yyyy-MM");
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Override
	public void saveUnDrugTotalToDBYear() {
		List<String> list1=new ArrayList<String>();
		list1.add("yyyy");
		list1.add("yyyy-mm");
		list1.add("yyyy-mm-dd");
		for(String date:list1){
		StringBuffer buffer= new StringBuffer();
		buffer.append("select c.FEE_STAT_NAME as stat, sum(nvl(ti.tot_cost,0)) as douValue,ti.name as name ");
		buffer.append("from (select t1.tot_cost,t1.fee_code,t1.send_flag,to_char(t1.fee_date,'"+date+"') as name ");
		buffer.append("from t_inpatient_itemlist_now t1 ");
		buffer.append("union all ");
		buffer.append("select t2.tot_cost,t2.fee_code,t2.send_flag ,to_char(t2.fee_date,'"+date+"') as name ");
		buffer.append("from t_inpatient_itemlist t2) ti ");
		buffer.append("left join t_charge_minfeetostat c on ti.fee_code = c.minfee_code and c.report_code = 'ZY01' ");
		buffer.append("where ti.send_flag = 1 ");
		buffer.append("group by c.FEE_STAT_NAME,ti.name ");
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
						new MongoBasicDao().update("UNDRUGTOTALFORAPP", document1, document, true);
					}
			}
		}
	}

	@Override
	public void saveUnDrugTotalTopFiveDeptToDBYear() {
		List<String> list1=new ArrayList<String>();
		list1.add("yyyy");
		list1.add("yyyy-mm");
		list1.add("yyyy-mm-dd");
		for(String date:list1){
		StringBuffer buffer=new StringBuffer(400);
		buffer.append("select b.douValue as douValue,b.name as name,(select e.dept_name from t_department e where e.dept_code=b.dept  ) as dept  from ( ");
		buffer.append("select  sum(nvl(ti.tot_cost,0)) as douValue,ti.name as name ,ti.dept as dept ");
		buffer.append("from ( ");
		buffer.append("select t1.tot_cost,t1.fee_code,t1.send_flag,to_char(t1.fee_date,'"+date+"') as name,t1.execute_deptcode as dept ");
		buffer.append("from t_inpatient_itemlist_now t1 ");
		buffer.append("union all ");
		buffer.append("select t2.tot_cost,t2.fee_code,t2.send_flag,to_char(t2.fee_date,'"+date+"') as name,t2.execute_deptcode as dept ");
		buffer.append("from t_inpatient_itemlist t2 ) ti ");
		buffer.append("where ti.send_flag = 1 ");
		buffer.append("group by ti.name ,ti.dept ) b  ");
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
					new MongoBasicDao().update("TOPDEPTUNDRUGTOTALFORAPP", document1, document, true);
				}
		 }
		}
	}



	@Override
	public void saveUnDrugTotalTopFiveDocToDBMonth() {
		List<String> list1=new ArrayList<String>();
		list1.add("yyyy");
		list1.add("yyyy-mm");
		list1.add("yyyy-mm-dd");
		for(String date:list1){
		StringBuffer buffer=new StringBuffer(500);
		buffer.append("select b.douValue as douValue,b.name as name,(select e.employee_name from t_employee e where e.employee_code=b.doc) as doctor from ( ");
		buffer.append("select  sum(nvl(ti.tot_cost,0)) as douValue,ti.name as name ,ti.doc as doc ");
		buffer.append("from ( ");
		buffer.append("select t1.tot_cost,t1.fee_code,t1.send_flag,to_char(t1.fee_date,'"+date+"') as name,t1.recipe_doccode as doc ");
		buffer.append("from t_inpatient_itemlist_now t1 ");
		buffer.append("union all ");
		buffer.append("select t2.tot_cost,t2.fee_code,t2.send_flag,to_char(t2.fee_date,'"+date+"') as name,t2.recipe_doccode as doc ");
		buffer.append("from t_inpatient_itemlist t2 ) ti ");
		buffer.append("where ti.send_flag = 1 ");
		buffer.append("group by ti.name ,ti.doc ) b ");
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
					new MongoBasicDao().update("TOPDOCUNDRUGTOTALFORAPP", document1, document, true);
				}
		}
		}
		
	}
/****************************日**********************************************************************************/
	@Override
	public void initUnDrugTotalForOracleByOneDay(List<String> tnL,
			String begin, String end, Integer hours) {
		StringBuffer buffer= new StringBuffer();
		buffer.append("select c.FEE_STAT_NAME as stat, sum(nvl(ti.tot_cost,0)) as douValue,ti.name as name from ( ");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
				
			}
			buffer.append("select t"+i+".tot_cost,t"+i+".fee_code,t"+i+".send_flag,to_char(t"+i+".fee_date,'yyyy-mm-dd') as name ");
			buffer.append("from "+tnL.get(i)+" t"+i+" ");	
			buffer.append("where t"+i+".fee_date >=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date <to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		}
		buffer.append(") ti left join t_charge_minfeetostat c on ti.fee_code = c.minfee_code and c.report_code = 'ZY01' ");
		buffer.append("where ti.send_flag = 1 ");
		buffer.append("group by c.FEE_STAT_NAME,ti.name ");
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
								DBCursor cursor = new MongoBasicDao().findAlldata("UNDRUGTOTALFORAPP", bdObject);
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
						new MongoBasicDao().update("UNDRUGTOTALFORAPP", document1, document, true);
						}
					}
			}
		
	}

	@Override
	public void initUnDrugForOracleWithDept(List<String> tnL, String begin,
			String end, Integer hours) {
		StringBuffer buffer=new StringBuffer(400);
		buffer.append("select b.douValue as douValue,b.name as name,(select e.dept_name from t_department e where e.dept_code=b.dept  ) as dept  from ( ");
		buffer.append("select  sum(nvl(ti.tot_cost,0)) as douValue,ti.name as name ,ti.dept as dept ");
		buffer.append("from ( ");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("select t"+i+".tot_cost,t"+i+".fee_code,t"+i+".send_flag,to_char(t"+i+".fee_date,'yyyy-mm-dd') as name,t"+i+".execute_deptcode as dept ");
			buffer.append("from "+tnL.get(i)+" t"+i+" ");
			buffer.append("where t"+i+".fee_date >=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date <to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		}
		buffer.append(") ti where ti.send_flag = 1 ");
		buffer.append("group by ti.name ,ti.dept ) b  ");
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
							DBCursor cursor = new MongoBasicDao().findAlldata("TOPDEPTUNDRUGTOTALFORAPP",bdObject);
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
					new MongoBasicDao().update("TOPDEPTUNDRUGTOTALFORAPP", document1, document, true);
					}
					}
		 }
		
	}

	@Override
	public void initUnDrugForOracleWithDoc(List<String> tnL, String begin,
			String end, Integer hours) {
		StringBuffer buffer=new StringBuffer(400);
		buffer.append("select b.douValue as douValue,b.name as name,(select e.employee_name from t_employee e where e.employee_code=b.doc) as doctor from ( ");
		buffer.append("select  sum(nvl(ti.tot_cost,0)) as douValue,ti.name as name ,ti.doc as doc ");
		buffer.append("from ( ");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("select t"+i+".tot_cost,t"+i+".fee_code,t"+i+".send_flag,to_char(t"+i+".fee_date,'yyyy-mm-dd') as name,t"+i+".recipe_doccode as doc ");
			buffer.append("from "+tnL.get(i)+" t"+i+" ");
			buffer.append("where t"+i+".fee_date >=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date <to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		}
		buffer.append(" ) ti where ti.send_flag = 1 ");
		buffer.append("group by ti.name ,ti.doc ) b ");
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
								bdObject.append("doctor", vo.getDoctor());
								DBCursor cursor = new MongoBasicDao().findAlldata("TOPDOCUNDRUGTOTALFORAPP", bdObject);
								DBObject dbCursor;
								Double dou=vo.getDouValue();
								while(cursor.hasNext()){
									 dbCursor = cursor.next();
									dou+=(Double) dbCursor.get("douValue");
									}
								vo.setDouValue(dou);
						 }
						Document document1 = new Document();
						document1.append("doctor", vo.getDoctor());//科室
						document1.append("name", vo.getName());//药品统计时间
						Document document = new Document();
						document.append("name", vo.getName());
						document.append("douValue", vo.getDouValue());//金额
						document1.append("doctor", vo.getDoctor());//科室
						new MongoBasicDao().update("TOPDOCUNDRUGTOTALFORAPP", document1, document, true);
						}
					}
			}
	}
/**********************月*************************************************************************************************/
	@Override
	public void initGroupUnDrugByOneDay(String begin, String end) {
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
			DBCursor cursor = new MongoBasicDao().findAlldata("UNDRUGTOTALFORAPP", bdObject);
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
				new MongoBasicDao().update("UNDRUGTOTALFORAPP", doucment1, document, true);
				
			}
		}	
		
	}

	@Override
	public void InitDeptUnDrugByOneDay(String begin, String end) {
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
			DBCursor cursor = new MongoBasicDao().findAlldata("TOPDEPTUNDRUGTOTALFORAPP", bdObject);
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
				new MongoBasicDao().update("TOPDEPTUNDRUGTOTALFORAPP", doucment1, document, true);
				
			}
		}
		
	}

	@Override
	public void InitDocUnDrugByOneDay(String begin, String end) {
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
			DBCursor cursor = new MongoBasicDao().findAlldata("TOPDOCUNDRUGTOTALFORAPP", bdObject);
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
				new MongoBasicDao().update("TOPDOCUNDRUGTOTALFORAPP", doucment1, document, true);
				
			}
		}
		
	}
	/********************年********************************************************************************************/
	@Override
	public void initGroupUnDrugYear(String begin, String end) {
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
			DBCursor cursor = new MongoBasicDao().findAlldata("UNDRUGTOTALFORAPP", bdObject);
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
				new MongoBasicDao().update("UNDRUGTOTALFORAPP", doucment1, document, true);
				
			}
		}	

		
	}
	@Override
	public void InitDeptUnDrugYear(String begin, String end) {
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
			DBCursor cursor = new MongoBasicDao().findAlldata("TOPDEPTUNDRUGTOTALFORAPP", bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				ListTotalIncomeStaticVo voOne=new  ListTotalIncomeStaticVo();
				 dbCursor = cursor.next();
				 Double value = (Double) dbCursor.get("value") ;//金额
				if(value!=null){
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
				new MongoBasicDao().update("TOPDEPTUNDRUGTOTALFORAPP", doucment1, document, true);
				
			}
		}
		
	}

	@Override
	public void InitDocUnDrugYear(String begin, String end) {
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
			DBCursor cursor = new MongoBasicDao().findAlldata("TOPDOCUNDRUGTOTALFORAPP", bdObject);
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
				new MongoBasicDao().update("TOPDOCUNDRUGTOTALFORAPP", doucment1, document, true);
				
			}
		}
		
	}

/***********************计算时间方法*********************************************************************************************/
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
