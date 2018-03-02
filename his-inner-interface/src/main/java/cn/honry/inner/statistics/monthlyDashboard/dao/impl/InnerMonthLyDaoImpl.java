package cn.honry.inner.statistics.monthlyDashboard.dao.impl;

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
import org.bson.Document;
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
import cn.honry.inner.statistics.monthlyDashboard.dao.InnerMonthLyDao;
import cn.honry.inner.statistics.monthlyDashboard.vo.MonthlyDashboardVo;
import cn.honry.inner.statistics.registerInfoGzltj.dao.InnerRegisterInfoGzltjDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.Dashboard;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
@Repository("innerMonthLyDao")
@SuppressWarnings("all")
public class InnerMonthLyDaoImpl extends  HibernateEntityDao<MonthlyDashboardVo> implements InnerMonthLyDao {
	private final String[] inpatientMedi={"T_INPATIENT_ITEMLIST_NOW","T_INPATIENT_ITEMLIST"};//住院主表
	private final String[] inpatientFee={"T_INPATIENT_FEEINFO_NOW","T_INPATIENT_FEEINFO"};//住院费用明细
	private final String[] inpatientInfo={"T_INPATIENT_INFO_NOW","T_INPATIENT_INFO"};
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
	public void init_MYZHYBP_HospExpenses(String menuAlias, String type,String date) {
		if(StringUtils.isNotBlank(date)){
		Date beginDate=new Date();
		List<MonthlyDashboardVo> list=null; 
		String begin=date+" 00:00:00";
		String end=date+" 23:59:59";
		List<String> tnL=wordLoadDocDao.returnInTables(begin, end, inpatientFee, "ZY");
		if(tnL!=null&&tnL.size()>0){
		StringBuffer buffer=new StringBuffer(200);
		buffer.append("select sum(f.totCost0) as totCost0 ,f.yearAndMonth as yearAndMonth from( ");
		buffer.append("select b.totCost AS totCost0,to_char(b.mon, 'yyyy-mm-dd') AS yearAndMonth from ( ");
		for (int i = 0; i < tnL.size(); i++) {
			if (i != 0) {
				buffer.append(" union all ");
			}
			buffer.append("select t"+ i +".TOT_COST as totCost,");
			buffer.append("t" + i + ".fee_date as mon ");
			buffer.append("from " + tnL.get(i) + " t" + i);
			buffer.append(" where t" + i+ ".fee_date >=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t" + i+ ".fee_date <=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss')");
		}
		buffer.append(" ) b ) f group by f.yearAndMonth");
		list = super.getSession().createSQLQuery(buffer.toString())
				.addScalar("yearAndMonth").addScalar("totCost0", Hibernate.DOUBLE)
				.setResultTransformer(Transformers.aliasToBean(MonthlyDashboardVo.class))
				.list();
		DBObject query = new BasicDBObject();
		query.put("yearAndMonth", date);//移除数据条件
		new MongoBasicDao().remove(menuAlias+"_HOSPEXPENSES_DAY", query);//删除原来的数据
		
		if (list!=null && list.size() > 0) {
			List<DBObject> userList = new ArrayList<DBObject>();
			for (MonthlyDashboardVo vo : list) {
				if(vo.getTotCost0()!=null){
					BasicDBObject obj = new BasicDBObject();
					obj.append("totCost0", vo.getTotCost0());
					obj.append("yearAndMonth", vo.getYearAndMonth());
					userList.add(obj);	
				}
				}
			new MongoBasicDao().insertDataByList(menuAlias+"_HOSPEXPENSES_DAY", userList);
			if(!"HIS".equals(type)){
				init_MYZHYBP_HospExpenses_MoreDay(menuAlias,"2",date);
				init_MYZHYBP_HospExpenses_MoreDay(menuAlias,"3",date);
			}
			}
		wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_HOSPEXPENSES_DAY", list, date);	
		}
		
		}
	}
	public void init_MYZHYBP_HospExpenses_MoreDay(String menuAlias, String type, String date){
		Date beginDate=new Date();
		
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Double> map=new HashMap<String,Double>();
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
			queryMongo=menuAlias+"_HOSPEXPENSES_DAY";
			saveMongo=menuAlias+"_HOSPEXPENSES_MONTH";
			end=returnEndTime(date).split(" ")[0];
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_HOSPEXPENSES_MONTH";
			saveMongo=menuAlias+"_HOSPEXPENSES_YEAR";
		}
		
			BasicDBList dateList=new BasicDBList();
			BasicDBObject data1= new BasicDBObject();//查询开始时间
			BasicDBObject data2= new BasicDBObject();//查询结束时间
			
			    data1.append("yearAndMonth", new BasicDBObject("$gte",begin));
			    data2.append("yearAndMonth", new BasicDBObject("$lte",end));
			    dateList.add(data1);
			    dateList.add(data2);
			    bdObject.put("$and", dateList);
				DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Double  value = (Double) dbCursor.get("totCost0") ;//费用
			        temp1=temp;
					 if(map.containsKey(temp1)){//如果key存在 比较name
						dou=map.get(temp1);
						dou+=value;
						map.put(temp1, dou);
					 }else{//如果key不存在   添加到map1中
				    	map.put(temp1, value);
				    }
				  }
				DBObject query = new BasicDBObject();
				query.put("yearAndMonth", temp);//移除数据条件
				new MongoBasicDao().remove(saveMongo, query);//删除原来的数据
				
				List<DBObject> userList = new ArrayList<DBObject>();
				for(String key:map.keySet()){
						BasicDBObject obj = new BasicDBObject();
						obj.append("totCost0",map.get(key));
						obj.append("yearAndMonth", key);
						userList.add(obj);
				}
			new MongoBasicDao().insertDataByList(saveMongo, userList);
			
			wordLoadDocDao.saveMongoLog(beginDate, saveMongo, userList, date);
			
	}
	
	@Override
	public void init_MYZHYBP_Surgery(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			List<Dashboard> list=null; 
			String temp=date.substring(0,7);
			String begin=temp+"-01 00:00:00";
			String end=returnEndTime(date);
			List<String> tnL=ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OPERATION_APPLY",begin,end);
			List<String> inpatiionTnL=wordLoadDocDao.returnInTables(begin, end, inpatientInfo, "ZY");
			if(tnL!=null&&tnL.size()>0&&inpatiionTnL!=null&&inpatiionTnL.size()>0){
				StringBuffer buffer=new StringBuffer(700);
				buffer.append("select TO_Char(sum(b.num)) AS value,b.dat AS name,b.dept AS dept,TO_CHAR(trunc(sum(b.days)/sum(b.num),2)) AS inhost from ( ");
				for(int i=0,len=tnL.size();i<len;i++){
					buffer.append("select nvl(count(t"+i+".op_id) , 0) as num,to_char(t"+i+".pre_date,'yyyy-mm') as dat,t"+i+".op_doctordept as dept,nvl(i.out_date,sysdate)-nvl(i.in_date,sysdate-2) as days ");
					buffer.append("from "+tnL.get(i)+" t"+i+" left join (");
					for(int j=0,len1=inpatiionTnL.size();j<len1;j++){
						if(j>0){
							buffer.append(" union all ");
						}
						buffer.append("select t"+j+".inpatient_no inpatient_no,t"+j+".in_date in_date,t"+j+".out_date out_date from "+inpatiionTnL.get(j)+" t"+j);
					}
					buffer.append(" ) i on t"+i+".clinic_code = i.inpatient_no  where t"+i+".stop_flg = 0 and t"+i+".del_flg = 0 and t"+i+".status = 4 ");
					buffer.append(" and t"+i+".pre_date between to_date('" + begin
							+ "','yyyy-mm-dd HH24:mi:ss') and to_date('"
							+ end + "','yyyy-mm-dd HH24:mi:ss') ");
					buffer.append("group by t"+i+".pre_date,t"+i+".op_doctordept,i.in_date,i.out_date ");
				}
				buffer.append(" ) b group by b.dat,b.dept ");
				list = super
						.getSession()
						.createSQLQuery(buffer.toString())
						.addScalar("value")
						.addScalar("name")
						.addScalar("dept")
						.addScalar("inhost")
						.setResultTransformer(Transformers.aliasToBean(Dashboard.class))
						.list();
				
				DBObject query = new BasicDBObject();
				query.put("name", temp);//移除数据条件
				new MongoBasicDao().remove(menuAlias+"_SURGERY_MONTH", query);//删除原来的数据
				
				if (list!=null && list.size() > 0) {
					List<DBObject> userList = new ArrayList<DBObject>();
					for (Dashboard vo : list) {
						if(vo.getValue()!=null&&vo.getInhost()!=null){
							BasicDBObject obj = new BasicDBObject();
							obj.append("dept", vo.getDept());// 科室
							obj.append("value", vo.getValue());// 手术例数统计
							obj.append("name", vo.getName());// 时间
							obj.append("inHost", vo.getInhost());// 住院时间
							userList.add(obj);
					 }
					}
					new MongoBasicDao().insertDataByList(menuAlias+"_SURGERY_MONTH", userList);
				}
				wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_SURGERY_MONTH", list, date);
			}
			
			}
	}

	@Override
	public void init_MYZHYBP_Treatment(String menuAlias, String type,
			String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			List<Dashboard> list=null; 
			String temp=date.substring(0,7);
			String begin=temp+"-01 00:00:00";
			String end=returnEndTime(date);
			List<String> tnL=wordLoadDocDao.returnInTables(begin, end, inpatientInfo, "ZY");
			if(tnL!=null&&tnL.size()>0){
			StringBuffer buffer=new StringBuffer(770);
			buffer.append("select b.stat AS stat,TO_Char(sum(b.con)) AS value,b.dat AS name,b.dept AS dept from ( ");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("select  decode(nvl(t"+i+".diag_outstate,0),0,'治愈',1,'好转',2,'未愈',3,'死亡',4,'其他') as stat,");
				buffer.append("count(nvl(t"+i+".diag_outstate,0)) as con,to_char(t"+i+".out_date,'yyyy-mm') as dat,t"+i+".dept_name as dept ");
				buffer.append("from "+tnL.get(i)+" t"+i+" where t"+i+".in_state in('O','B') and t"+i+".stop_flg=0 and t"+i+".del_flg=0 ");
				buffer.append("and t"+i+".out_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".out_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("group by t"+i+".out_date,t"+i+".diag_outstate,t"+i+".dept_name ");
			}
			buffer.append(") b group by b.dat,b.con,b.stat,b.dept ");
			 list = super
					.getSession()
					.createSQLQuery(buffer.toString())
					.addScalar("value")
					.addScalar("name")
					.addScalar("dept")
					.addScalar("stat")
					.setResultTransformer(Transformers.aliasToBean(Dashboard.class))
					.list();
			 
			 	DBObject query = new BasicDBObject();
				query.put("name", temp);//移除数据条件
				new MongoBasicDao().remove(menuAlias+"_TREATMENT_MONTH", query);//删除原来的数据
			 
			if (list!=null && list.size() > 0) {
				List<DBObject> userList = new ArrayList<DBObject>();
				for (Dashboard vo : list) {
					if(vo.getValue()!=null){
						BasicDBObject obj = new BasicDBObject();
						obj.append("stat", vo.getStat());// 状态
						obj.append("dept", vo.getDept());// 科室
						obj.append("value", vo.getValue());// 治疗数量
						obj.append("name", vo.getName());// 时间
						userList.add(obj);
					}
				}
				new MongoBasicDao().insertDataByList(menuAlias+"_TREATMENT_MONTH", userList);
			}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_TREATMENT_MONTH", list, date);
		}
	}
	}

	@Override
	public void init_MYZHYBP_WardApply(String menuAlias, String type,
			String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			List<Dashboard> list=null; 
			String temp=date.substring(0,7);
			String begin=temp+"-01 00:00:00";
			String end=returnEndTime(date);
			List<String> tnL=wordLoadDocDao.returnInTables(begin, end, inpatientInfo, "ZY");
			if(tnL!=null&&tnL.size()>0){
			StringBuffer buffer=new StringBuffer(500);
			buffer.append("select round(sum(c.con)*100 /(select distinct count(1) from t_business_hospitalbed),2) AS douValue, c.dat AS name,c.dept AS dept,to_CHar(trunc(sum(c.days) / sum(c.con), 2)) as inhost from( ");
			buffer.append("select count(b.dat) as con,b.dat as dat,b.dept as dept,b.days as days from( ");
			 for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				 buffer.append("select  TO_CHAR(t"+i+".in_date,'yyyy-mm') as dat,t"+i+".dept_code as dept, nvl(t"+i+".out_date,sysdate)-t"+i+".in_date as days from "+tnL.get(i)+" t"+i+" where  t"+i+".in_date between to_date('"
						+ begin
						+ "','yyyy-mm-dd HH24:mi:ss') and to_date('"
						+ end
						+ "','yyyy-mm-dd HH24:mi:ss')  ");
			 }
			buffer.append(") b group by b.dat,b.dept,b.days ");
			buffer.append(") c group by c.dat ,c.dept ");
			list = super
					.getSession()
					.createSQLQuery(buffer.toString())
					.addScalar("douValue",Hibernate.DOUBLE)
					.addScalar("name")
					.addScalar("dept")
					.addScalar("inhost")
					.setResultTransformer(Transformers.aliasToBean(Dashboard.class))
					.list();
			
			DBObject query = new BasicDBObject();
			query.put("name", temp);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_WARDAPPLY_MONTH",query);//删除原来的数据
			
			if (list!=null && list.size() > 0) {
				List<DBObject> userList = new ArrayList<DBObject>();
				for (Dashboard vo : list) {
					if(vo.getDouValue()!=null&&vo.getInhost()!=null){
					 BasicDBObject bdObject = new BasicDBObject();
					 bdObject.append("dept", vo.getDept());// 科室
					 bdObject.append("value", vo.getDouValue().toString());// 病床使用率
					 bdObject.append("name", vo.getName());// 时间
					 bdObject.append("inhost", vo.getInhost());// 在院时间
					 userList.add(bdObject);
					}
				 }
				new MongoBasicDao().insertDataByList(menuAlias+"_WARDAPPLY_MONTH", userList);
			}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_WARDAPPLY_MONTH", list, date);
		}
	}
	}

	@Override
	public void init_MYZHYBP_Inpatient(String menuAlias, String type,
			String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			List<MonthlyDashboardVo> list=null; 
			String temp=date.substring(0,7);
			String begin=temp+"-01 00:00:00";
			String end=returnEndTime(date);
			List<String> tnL=wordLoadDocDao.returnInTables(begin, end, inpatientInfo, "ZY");
			if(tnL!=null&&tnL.size()>0){
			final StringBuffer buffer = new StringBuffer(370);
			buffer.append("select TO_Char(Sum(b.totCost)) AS countLeave, b.mon AS yearAndMonth,to_char(floor(sum(b.days)/sum(b.totcost))) as average  from ( ");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
			buffer.append("select count(1) as totCost, to_char(t"+i+".in_date, 'yyyy-mm') as mon,trunc(decode(sign(t"+i+".out_date-t"+i+".in_date),-1,t"+i+".in_date + 2,t"+i+".out_date) - t"+i+".in_date,2) as days ");
			buffer.append("from "+tnL.get(i)+" t"+i+" ");
			buffer.append("where t"+i+".in_state = 'O' and t"+i+".in_date between to_date('"
					+ begin
					+ "','yyyy-mm-dd HH24:mi:ss') and to_date('"
					+ end
					+ "','yyyy-mm-dd HH24:mi:ss') group by t"+i+".in_date,t"+i+".out_date ");
			}
			buffer.append(") b group by b.mon");
			
			list = super
					.getSession()
					.createSQLQuery(buffer.toString())
					.addScalar("countLeave")
					.addScalar("yearAndMonth")
					.addScalar("average")
					.setResultTransformer(
					Transformers.aliasToBean(MonthlyDashboardVo.class))
					.list();
			DBObject query = new BasicDBObject();
			query.put("yearAndMonth", temp);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_INPATIENT_MONTH",query);//删除原来的数据
			
			if (list!=null && list.size() > 0) {
				List<DBObject> userList = new ArrayList<DBObject>();
				for (MonthlyDashboardVo vo : list) {
					if(vo.getCountLeave()!=null&&vo.getAverage()!=null){
						BasicDBObject bdObject = new BasicDBObject();
						bdObject.append("countLeave", vo.getCountLeave());
						bdObject.append("yearAndMonth", vo.getYearAndMonth());
						bdObject.append("average", vo.getAverage());
						userList.add(bdObject);
					}
				}
				new MongoBasicDao().insertDataByList(menuAlias+"_INPATIENT_MONTH", userList);
			}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_INPATIENT_MONTH", list, date);
		}
		}
	}
	public String returnEndTime(String date){
		String end=null;
		date=date.substring(0,7)+"-01";
		Calendar ca=Calendar.getInstance(Locale.CHINESE);
		try {
			ca.setTime(df.parse(date));
			ca.add(Calendar.MONTH, 1);
			ca.add(Calendar.DATE, -1);
			end=df.format(ca.getTime())+" 23:59:59";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return end;
	}
}
