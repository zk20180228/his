package cn.honry.inner.statistics.inpatientStatistics.dao.impl;

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

import org.bson.Document;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.inpatientStatistics.dao.InitInpatientStatisticsDao;
import cn.honry.inner.statistics.inpatientStatistics.vo.InpatientStatisticsVo;
import cn.honry.inner.statistics.registerInfoGzltj.dao.InnerRegisterInfoGzltjDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.utils.JSONUtils;
@Repository("initInpatientStatisticsDao")
@SuppressWarnings({"all"})
public class InitInpatientStatisticsDaoImpl extends HibernateDaoSupport implements InitInpatientStatisticsDao{
		private final DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		@Resource(name = "sessionFactory")
		public void setSuperSessionFactory(SessionFactory sessionFactory) {
			super.setSessionFactory(sessionFactory);
		}
		@Autowired
		@Qualifier(value = "innerRegisterInfoGzltjDao")
		private InnerRegisterInfoGzltjDao innerRegisterInfoGzltjDao;
		
		@Autowired
		@Qualifier(value="wordLoadDocDao")
		private WordLoadDocDao wordLoadDocDao;
		public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
			this.wordLoadDocDao = wordLoadDocDao;
		}
		@Override
		public void init_ZYRSTJ(String menuAlias, String typeOf, String firstData) {
			Date beginDate=new Date();
			Integer type=Integer.parseInt(typeOf);
			String time=null;
			String endData=null;
			if(type==1){//日
				time=firstData;
				firstData=time+" 00:00:00";
				endData=time+" 23:59:59";
			}else if(type==2){//月
				time=firstData.substring(0, 7);
				firstData=time+"-01 00:00:00";
//				endData+=" 23:59:59";
				endData=returnEndTime(time)+" 23:59:59";
			}else if(type==3){//年
				time=firstData.substring(0, 4);
				firstData=time+"-01-01 00:00:00";
				endData=time+"-12-31 23:59:59";
			}
			String sql="select pp.dept_code code,sum(num) total,(select sum(num1) from (select count(1) num1 from t_inpatient_info t where "
					+ "t.in_date <= to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') and t.out_date >= to_date('"+firstData+"', 'yyyy-mm-dd HH24:mi:ss')"
					+ " union all select count(1) num1 from t_inpatient_info_now t where t.in_date <= to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "and t.out_date >= to_date('"+firstData+"', 'yyyy-mm-dd HH24:mi:ss')  union all select count(1) num1  from t_inpatient_info_now t "
					+ "where t.in_date <= to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') and t.in_state='I') ) totals from (select count(1) num,dept_code "
					+ "from t_inpatient_info t where t.in_date <= to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') and t.out_date >= to_date('"+firstData+"',"
					+ " 'yyyy-mm-dd HH24:mi:ss') group by t.dept_code union all select count(1) num,t.dept_code from t_inpatient_info_now t where t.in_date <= "
					+ "to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') and t.out_date >= to_date('"+firstData+"', 'yyyy-mm-dd HH24:mi:ss') group by t.dept_code "
					+ "union all select count(1) num,t.dept_code  from t_inpatient_info_now t where t.in_date <= to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss')"
					+ " and t.in_state='I' group by t.dept_code)pp group by pp.dept_code";
			List<InpatientStatisticsVo> list = this.getSession().createSQLQuery(sql).addScalar("code").addScalar("total",Hibernate.INTEGER).addScalar("totals",Hibernate.INTEGER)
			.setResultTransformer(Transformers.aliasToBean(InpatientStatisticsVo.class)).list();
			
			DBObject query = new BasicDBObject();
			query.put("workdate", time);//移除数据条件
			
			if(type==1){//日
				new MongoBasicDao().remove("ZYRSTJ_DAY", query);//删除原来的数据
			}else if(type==2){//月
				new MongoBasicDao().remove("ZYRSTJ_MONTH", query);//删除原来的数据
			}else if(type==3){//年
				new MongoBasicDao().remove("ZYRSTJ_YEAR", query);//删除原来的数据
			}
			
			if(list!=null && list.size()>0){
				List<DBObject> userList = new ArrayList<DBObject>();
				for(InpatientStatisticsVo vo:list){
						BasicDBObject obj = new BasicDBObject();
						obj.append("total", vo.getTotal());//医生姓名
						obj.append("totals", vo.getTotals());//药物处方数
						obj.append("deptCode",vo.getCode());//抗菌药物处方数
						obj.append("workdate",time);//抗菌药物处方比例
						userList.add(obj);
				}
				
				if(type==1){//日
					new MongoBasicDao().insertDataByList("ZYRSTJ_DAY", userList);
				}else if(type==2){//月
					new MongoBasicDao().insertDataByList("ZYRSTJ_MONTH", userList);
				}else if(type==3){//年
					new MongoBasicDao().insertDataByList("ZYRSTJ_YEAR", userList);
				}
			}
			if(!"timing".equals(menuAlias)){//如果栏目名不为timing  表示不级联更新
				init_ZYRSTJ("timing","2",time);
				init_ZYRSTJ("timing","3",time);
			}
				SimpleDateFormat mat=new SimpleDateFormat("yyyy-MM-dd");
				if(type==1){//日
					wordLoadDocDao.saveMongoLog(beginDate, "ZYRSTJ_DAY", list, time);
				}else if(type==2){//月
					wordLoadDocDao.saveMongoLog(beginDate, "ZYRSTJ_MONTH", list, time);
				}else if(type==3){//年
					wordLoadDocDao.saveMongoLog(beginDate, "ZYRSTJ_YEAR", list, time);
				}
		}

		
		private List<Map<String,String>> getTimes(String date) {
			try{
				List<Map<String,String>> list= new ArrayList<>();
				Map<String,String> map1=new HashMap<>();
				Map<String,String> map2=new HashMap<>();
				Map<String,String> map3=new HashMap<>();
				String time1=date;
				String time2=date.substring(0, 7);
				String time3=date.substring(0, 4);
				//获取日 开始和结束时间
				String stime=date+" 00:00:00";
				String etime=date+" 23:59:59";
				map1.put("stime", stime);
				map1.put("etime", etime);
				map1.put("time",time1);
				map1.put("name","ZYRSTJ_DAY");
				list.add(map1);
				//获取当月开始结束时间
				stime=date.substring(0, 7)+"-01 00:00:00";
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date date1 = format.parse(date);
				// 获取Calendar
				Calendar calendar = Calendar.getInstance();
				// 设置时间,当前时间不用设置
				calendar.setTime(date1);
				// 设置日期为本月最大日期
				calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
				etime = format.format(calendar.getTime());
				etime=etime+" 23:59:59";
				map2.put("stime", stime);
				map2.put("etime", etime);
				map1.put("time",time2);
				map1.put("name","ZYRSTJ_MONTH");
				list.add(map2);
				//获取当前时间类型
				stime=date.substring(0, 4)+"-01-01 00:00:00";
				etime=date.substring(0, 4)+"-12-31 23:59:59";
				map3.put("stime", stime);
				map3.put("etime", etime);
				map3.put("time",time3);
				map3.put("name","ZYRSTJ_YEAR");
				return list;
			}catch(Exception e){
				e.printStackTrace();
				return null;
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
				end=df.format(ca.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return end;
		}
		@Override
		public void inHostNumber(String menuAlias, String type, String date)
				throws Exception {
			Date beginDate=new Date();
			String firstData=null;
			String endData=null;
			String inhostNumsName="ZYRSTJ_MOBILE";//在院人数
			String nurseLevlName="ZYLEVL_MOBILE";//护理级别
			date.substring(0, 13-Integer.parseInt(type)*3);
			if("1".equals(type)){//日
				inhostNumsName+="_DAY";
				nurseLevlName+="_DAY";
				firstData=date+" 00:00:00";
				endData=date+" 23:59:59";
			}else if("2".equals(type)){//月
				date=date.substring(0,7);
				inhostNumsName+="_MONTH";
				nurseLevlName+="_MONTH";
				firstData=date+"-01 00:00:00";
				endData=returnEndTime(date)+" 23:59:59";
			}else if("3".equals(type)){//年
				date=date.substring(0,4);
				inhostNumsName+="_YEAR";
				nurseLevlName+="_YEAR";
				firstData=date+"-01-01 00:00:00";
				endData=date+"-12-31 23:59:59";
			}
			String sql="select dd.dept_area_name name,sum(num) total from (select count(1) num,dept_code "
					+ "from t_inpatient_info t where t.in_date <= to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') and t.out_date >= to_date('"+firstData+"',"
					+ " 'yyyy-mm-dd HH24:mi:ss') group by t.dept_code union all select count(1) num,t.dept_code from t_inpatient_info_now t where t.in_date <= "
					+ "to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') and t.out_date >= to_date('"+firstData+"', 'yyyy-mm-dd HH24:mi:ss') group by t.dept_code "
					+ "union all select count(1) num,t.dept_code  from t_inpatient_info_now t where t.in_date <= to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss')"
					+ " and t.in_state in ('I') group by t.dept_code)pp ,t_department dd where pp.dept_code =dd.dept_code group by dd.dept_area_name";
			List<InpatientStatisticsVo> list = this.getSession().createSQLQuery(sql).addScalar("name").addScalar("total",Hibernate.INTEGER)
					.setResultTransformer(Transformers.aliasToBean(InpatientStatisticsVo.class)).list();
			DBObject query = new BasicDBObject();
			query.put("searchDate", date);//移除数据条件
			new MongoBasicDao().remove(inhostNumsName, query);//删除原来的数据
			if(list.size()>0){
				String numbersArea=JSONUtils.toJson(list);
				 	Document document1 = new Document();
					document1.append("searchDate",date);//统计时间
					Document document = new Document();
					document.append("searchDate",date);//统计时间
					document.append("value", numbersArea);//数据
				new MongoBasicDao().update(inhostNumsName, document1, document, true);
			}
			nurseLevel(firstData,endData,nurseLevlName,date);//处理护理级别数据
			if(!"timing".equals(menuAlias)){//如果栏目名不为timing  表示不级联更新
				init_ZYRSTJ("timing","2",date);
				init_ZYRSTJ("timing","3",date);
			}
			wordLoadDocDao.saveMongoLog(beginDate, inhostNumsName, list, date);
		}
		
		
		private void nurseLevel(String firstData,String endData,String mongoName,String date){
			String sql="select pp.name ,to_char(count(pp.name)) value from (select t.tend name "
					+ "from t_inpatient_info t where t.in_date <= to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') and t.out_date >= to_date('"+firstData+"',"
					+ " 'yyyy-mm-dd HH24:mi:ss')  union all select t.tend name from t_inpatient_info_now t where t.in_date <= "
					+ "to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss') and t.out_date >= to_date('"+firstData+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "union all select t.tend name  from t_inpatient_info_now t where t.in_date <= to_date('"+endData+"', 'yyyy-mm-dd HH24:mi:ss')"
					+ " and t.in_state in ('I') )pp where pp.name is not null group by pp.name   order by decode(name,'一级护理',1,'二级护理',2,'三级护理',3，'四级护理',4,'特级护理',5) nulls last";
			List<InpatientStatisticsVo> list = this.getSession().createSQLQuery(sql).addScalar("name").addScalar("value")
					.setResultTransformer(Transformers.aliasToBean(InpatientStatisticsVo.class)).list();
			DBObject query = new BasicDBObject();
			query.put("searchDate", date);//移除数据条件
			new MongoBasicDao().remove(mongoName, query);//删除原来的数据
			if(list.size()>0){
				String numbersArea=JSONUtils.toJson(list);
				 	Document document1 = new Document();
					document1.append("searchDate",date);//统计时间
					Document document = new Document();
					document.append("searchDate",date);//统计时间
					document.append("value", numbersArea);//数据
				new MongoBasicDao().update(mongoName, document1, document, true);
			}
		}
}
