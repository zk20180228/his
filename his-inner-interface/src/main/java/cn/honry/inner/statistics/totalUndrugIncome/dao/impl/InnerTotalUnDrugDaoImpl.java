package cn.honry.inner.statistics.totalUndrugIncome.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.MongoLog;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.registerInfoGzltj.dao.InnerRegisterInfoGzltjDao;
import cn.honry.inner.statistics.totalUndrugIncome.dao.InnerTotalUnDrugDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.Dashboard;
import cn.honry.utils.DateUtils;
@Repository("innerTotalUnDrugDao")
@SuppressWarnings("all")
public class InnerTotalUnDrugDaoImpl extends  HibernateEntityDao<Dashboard> implements InnerTotalUnDrugDao{
	private final String[] inpatientMedi={"T_INPATIENT_ITEMLIST_NOW","T_INPATIENT_ITEMLIST"};//住院主表
	private final String ZY="ZY";
	private final DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value = "innerRegisterInfoGzltjDao")
	private InnerRegisterInfoGzltjDao innerRegisterInfoGzltjDao;
	
	@Override
	public void init_FYPSRFXHZ_Total(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
		Date beginDate=new Date();//开始计时
		
		String begin=date+" 00:00:00";//开始时间
		String end=date+" 23:59:59";//结束时间
		List<String> tnL=wordLoadDocDao.returnInTables(begin, end, inpatientMedi, ZY);
		List<Dashboard> list=null;
		if(tnL!=null&&tnL.size()>0){
			StringBuffer buffer=new StringBuffer(500);
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
			buffer.append("where ti.send_flag = 1  and c.fee_stat_name is not null ");
			buffer.append("group by c.FEE_STAT_NAME,ti.name ");
			
			list=super.getSession().createSQLQuery(buffer.toString())
					.addScalar("stat").addScalar("name").addScalar("douValue",Hibernate.DOUBLE)
					.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
			
			DBObject query = new BasicDBObject();
			query.put("name", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_TOTAL_DAY", query);//删除原来的数据

			
			if(list!=null && list.size()>0){
					 List<DBObject> userList = new ArrayList<DBObject>();
						for(Dashboard vo:list){
							if(vo.getDouValue()!=null){
								BasicDBObject obj = new BasicDBObject();
								obj.append("stat", vo.getStat());
								obj.append("name", vo.getName());
								obj.append("douValue", vo.getDouValue());//金额
								userList.add(obj);
							}
						}
					new MongoBasicDao().insertDataByList(menuAlias+"_TOTAL_DAY", userList);
					if(!"HIS".equals(type)){
						init_FYPSRFXHZ_Total_MoreDay(menuAlias,"2",date);
						init_FYPSRFXHZ_Total_MoreDay(menuAlias,"3",date);
					}
				}
		}
		wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_TOTAL_DAY", list, date);
		
		}
		
	}
	@Override
	public void init_FYPSRFXHZ_Total_MoreDay(String menuAlias, String type, String date){
		Date beginDate=new Date();
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
		String temp;//月数据
		Double dou;
		String temp1;//key
		
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		if("2".equals(type)){
			//计算最后一个月日期
			temp=date.substring(0,7);
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_TOTAL_DAY";
			saveMongo=menuAlias+"_TOTAL_MONTH";
			end=returnEndTime(date);
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_TOTAL_MONTH";
			saveMongo=menuAlias+"_TOTAL_YEAR";
		}
		
			BasicDBList dateList=new BasicDBList();
			BasicDBObject data1= new BasicDBObject();//查询开始时间
			BasicDBObject data2= new BasicDBObject();//查询结束时间
			
		    data1.append("name", new BasicDBObject("$gte",begin));
		    data2.append("name", new BasicDBObject("$lte",end));
		    dateList.add(data1);
		    dateList.add(data2);
		    bdObject.put("$and", dateList);
			DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
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
			DBObject query = new BasicDBObject();
			query.put("name", temp);//移除数据条件
			new MongoBasicDao().remove(saveMongo, query);//删除原来的数据
			
			List<DBObject> userList = new ArrayList<DBObject>();
			String[] stArr=null;
			for(String key:map.keySet()){
				stArr=key.split("&");
				BasicDBObject obj = new BasicDBObject();
				obj.append("douValue",map.get(key));
				obj.append("stat", stArr[1]);
				obj.append("name", stArr[0]);
				userList.add(obj);
			}
			map=null;
			new MongoBasicDao().insertDataByList(saveMongo, userList);
			wordLoadDocDao.saveMongoLog(beginDate, saveMongo, userList, date);
			
	}
	
	
	
	@Override
	public void init_FYPSRFXHZ_Dept(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
		Date beginDate=new Date();
		
		String begin=date+" 00:00:00";//开始时间
		String end=date+" 23:59:59";//结束时间
		List<String> tnL=wordLoadDocDao.returnInTables(begin, end, inpatientMedi, ZY);
		List<Dashboard> list=new ArrayList<Dashboard>();
		if(tnL!=null&&tnL.size()>0){
		
		 StringBuffer buffer=new StringBuffer(500);
		 buffer.append("select b.douValue as douValue,b.name as name,(select e.dept_name from t_department e where e.dept_code=b.dept  ) as dept  from ( ");
			buffer.append("select  sum(nvl(ti.tot_cost,0)) as douValue,ti.name as name ,ti.dept as dept ");
			buffer.append("from ( ");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("select t"+i+".tot_cost,t"+i+".fee_code,t"+i+".send_flag,to_char(t"+i+".fee_date,'yyyy-mm-dd') as name,t"+i+".execute_deptcode as dept ");
				buffer.append("from "+tnL.get(i)+" t"+i+" ");
				buffer.append("where t"+i+".fee_date >=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date <=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			}
			buffer.append(") ti where ti.send_flag = 1 ");
			buffer.append("group by ti.name ,ti.dept ) b  where b.name is not null  ");
		 list=super.getSession().createSQLQuery(buffer.toString())
					.addScalar("name").addScalar("douValue",Hibernate.DOUBLE).addScalar("dept")
					.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
		 	
		 
		 	DBObject query = new BasicDBObject();
			query.put("name", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_DEPT_DAY", query);//删除原来的数据
		 
		 if(list!=null && list.size()>0){
			 List<DBObject> userList = new ArrayList<DBObject>();
				for(Dashboard vo:list){
					if(vo.getDouValue()!=null){
						BasicDBObject obj = new BasicDBObject();
						obj.append("name", vo.getName());
						obj.append("douValue", vo.getDouValue());//金额
						obj.append("dept", vo.getDept());//科室
						userList.add(obj);
						}
					
					}
				
				new MongoBasicDao().insertDataByList(menuAlias+"_DEPT_DAY", userList);
				if(!"HIS".equals(type)){
					init_FYPSRFXHZ_Dept_MoreDay(menuAlias,"2",date);
					init_FYPSRFXHZ_Dept_MoreDay(menuAlias,"3",date);
				}
				}
		 }
		wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_DEPT_DAY", list, date);
		}
		
	}
	@Override
	public void init_FYPSRFXHZ_Dept_MoreDay(String menuAlias, String type, String date){
		Date beginDate=new Date();
		
		String temp;//月数据
		Double dou;
		String temp1;//key
		
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		if("2".equals(type)){
			//计算最后一个月日期
			temp=date.substring(0,7);
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_DEPT_DAY";
			saveMongo=menuAlias+"_DEPT_MONTH";
			end=returnEndTime(date);
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_DEPT_MONTH";
			saveMongo=menuAlias+"_DEPT_YEAR";
		}
		
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
			BasicDBList dateList=new BasicDBList();
			BasicDBObject data1= new BasicDBObject();//查询开始时间
			BasicDBObject data2= new BasicDBObject();//查询结束时间
			
		    data1.append("name", new BasicDBObject("$gte",begin));
		    data2.append("name", new BasicDBObject("$lte",end));
		    dateList.add(data1);
		    dateList.add(data2);
		    bdObject.put("$and", dateList);
			DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
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
			DBObject query = new BasicDBObject();
			query.put("name", temp);//移除数据条件
			new MongoBasicDao().remove(saveMongo, query);//删除原来的数据
			
			List<DBObject> userList = new ArrayList<DBObject>();
			String[] stArr;
			for(String key:map.keySet()){
				BasicDBObject obj = new BasicDBObject();
				stArr=key.split("&");
				obj.append("douValue",map.get(key));
				obj.append("dept", stArr[1]);
				obj.append("name", stArr[0]);
				userList.add(obj);
			}
			new MongoBasicDao().insertDataByList(saveMongo, userList);
			wordLoadDocDao.saveMongoLog(beginDate, saveMongo, userList, date);
		
	}
	@Override
	public void init_FYPSRFXHZ_Doc(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
		Date beginDate=new Date();
		String begin=date+" 00:00:00";//开始时间
		String end=date+" 23:59:59";//结束时间
		List<String> tnL=wordLoadDocDao.returnInTables(begin, end, inpatientMedi, ZY);
		List<Dashboard> list=new ArrayList<Dashboard>();
		
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
		buffer.append("group by ti.name ,ti.doc ) b  where b.name is not null ");
		list=super.getSession().createSQLQuery(buffer.toString())
					.addScalar("name").addScalar("douValue",Hibernate.DOUBLE).addScalar("doctor")
					.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
		DBObject query = new BasicDBObject();
		query.put("name", date);//移除数据条件
		new MongoBasicDao().remove(menuAlias+"_DOC_DAY", query);//删除原来的数据
		
		if(list!=null && list.size()>0){
			 List<DBObject> userList = new ArrayList<DBObject>();
				for(Dashboard vo:list){
					if(vo.getDouValue()!=null){
					BasicDBObject obj = new BasicDBObject();
					obj.append("name", vo.getName());
					obj.append("douValue", vo.getDouValue());//金额
					obj.append("doctor", vo.getDoctor());//科室
					userList.add(obj);
					}
				}
				new MongoBasicDao().insertDataByList(menuAlias+"_DOC_DAY", userList);
				if(!"HIS".equals(type)){
				init_FYPSRFXHZ_Doc_MoreDay(menuAlias,"2",date);
				init_FYPSRFXHZ_Doc_MoreDay(menuAlias,"3",date);
				}
		 }
		wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_DOC_DAY", list, date);
		}
	}
	
	@Override
	public void init_FYPSRFXHZ_Doc_MoreDay(String menuAlias, String type, String date){
		Date beginDate=new Date();		
		String temp;//月数据
		Double dou;
		String temp1;//key
		
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		if("2".equals(type)){
			//计算最后一个月日期
			temp=date.substring(0,7);
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_DOC_DAY";
			saveMongo=menuAlias+"_DOC_MONTH";
			end=returnEndTime(date);
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_DOC_MONTH";
			saveMongo=menuAlias+"_DOC_YEAR";
		}
		
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
			BasicDBList dateList=new BasicDBList();
			BasicDBObject data1= new BasicDBObject();//查询开始时间
			BasicDBObject data2= new BasicDBObject();//查询结束时间
			
		    data1.append("name", new BasicDBObject("$gte",begin));
		    data2.append("name", new BasicDBObject("$lte",end));
		    dateList.add(data1);
		    dateList.add(data2);
		    bdObject.put("$and", dateList);
			DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
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
			
			DBObject query = new BasicDBObject();
			query.put("name", temp);//移除数据条件
			new MongoBasicDao().remove(saveMongo, query);//删除原来的数据
			
			List<DBObject> userList = new ArrayList<DBObject>();
			String[] stArr;
			for(String key:map.keySet()){
				BasicDBObject obj = new BasicDBObject();
				stArr=key.split("&");
				obj.append("douValue",map.get(key));
				obj.append("doctor", stArr[1]);
				obj.append("name", stArr[0]);
				userList.add(obj);
			}
			map=null;
			new MongoBasicDao().insertDataByList(saveMongo, userList);
			
			wordLoadDocDao.saveMongoLog(beginDate, saveMongo, userList, date);
	}
	public String returnEndTime(String date){
		String end=null;
		date=date.substring(0,7)+"-01";
		Calendar ca=Calendar.getInstance(Locale.CHINESE);
		try {
			ca.setTime(df.parse(date));
			ca.add(Calendar.MONTH, 1);
			ca.add(Calendar.DATE, -1);
			end=df.format(ca.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return end;
	}

}
