package cn.honry.inner.statistics.outpatientUseMedic.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
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

import cn.honry.base.bean.model.MongoLog;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.monthlyDashboard.vo.MonthlyDashboardVo;
import cn.honry.inner.statistics.outpatientUseMedic.dao.InitUseMedicDao;
import cn.honry.inner.statistics.outpatientUseMedic.vo.OutpatientUseMedicVo;
import cn.honry.inner.statistics.registerInfoGzltj.dao.InnerRegisterInfoGzltjDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.Dashboard;
import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.WordLoadVO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
@Repository("initUseMedicDao")
@SuppressWarnings("all")
public class InitUseMedicDaoImpl extends HibernateDaoSupport implements InitUseMedicDao {
	private final String[] outpathientFeedetail={"T_OUTPATIENT_FEEDETAIL_NOW","T_OUTPATIENT_FEEDETAIL"};//处方明细表T_OUTPATIENT_FEEDETAIL
	private final String MZ="MZ";
	private final DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	/**
	 * 门诊用药监控 药占比(日)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	@Override
	public void init_MZYYJK_YZB(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> tnL=wordLoadDocDao.returnInTables(begin, end, outpathientFeedetail, MZ);
			if(tnL!=null&&tnL.size()>0){
				final StringBuffer sb = new StringBuffer();
				sb.append(" select nvl(sum(decode(t.Drug_Flag, '1', t.tot_cost, 0)),0) as drugFee,nvl(sum(t.tot_cost),0) as totCost ");
				sb.append(" from "+tnL.get(0)+" t ");
				sb.append(" where t.fee_date > to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" and t.fee_date <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" and t.PAY_FLAG != 0  and t.TRANS_TYPE=1 and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0 ");
				SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
				queryObject.addScalar("drugFee",Hibernate.DOUBLE).addScalar("totCost",Hibernate.DOUBLE);
				queryObject.setParameter("begin", begin);
				queryObject.setParameter("end", end);
				List<OutpatientUseMedicVo> list =queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
				DBObject query = new BasicDBObject();
				query.put("selectTime", date);//移除数据条件
				new MongoBasicDao().remove(menuAlias+"_YZB_DAY", query);//删除原来的数据
				if(list!=null && list.size()>0){
					List<DBObject> voList = new ArrayList<DBObject>();
					for(OutpatientUseMedicVo vo:list){
						BasicDBObject obj = new BasicDBObject();
						obj.append("selectTime", date);
						obj.append("totCost", vo.getTotCost());
						obj.append("drugFee", vo.getDrugFee());
						voList.add(obj);
					 }
					new MongoBasicDao().insertDataByList(menuAlias+"_YZB_DAY", voList);//添加新数据
					if(!"HIS".equals(type)){
						init_MZYYJK_YZB_MONTH(menuAlias,"2",date);//月更新药占比
					}
				}
				wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_YZB_DAY", list, date);
			}
		}
	}
	/**
	 * 门诊用药监控 药占比(月)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	@Override
	public void init_MZYYJK_YZB_MONTH(String menuAlias, String type, String date){
		if ("2".equals(type)) {
			Date beginDate=new Date();
			BasicDBObject bdObject = new BasicDBObject();
			Double drugFee = 0.0d;
			Double totCost = 0.0d;
			Integer total= 0;
			String queryMongo=menuAlias+"_YZB_DAY";//查询的表
			String saveMongo=menuAlias+"_YZB_MONTH";//保存的表
			String temp=date.substring(0,7);//截取月时间
			String begin=temp+"-01";//开始时间
			String end=returnEndTime(date);//计算一个月最后日期
			BasicDBList dateList=new BasicDBList();
			BasicDBObject data1= new BasicDBObject();//查询开始时间
			BasicDBObject data2= new BasicDBObject();//查询结束时间
			data1.append("selectTime", new BasicDBObject("$gte",begin));
			data2.append("selectTime", new BasicDBObject("$lte",end));
			dateList.add(data1);
			dateList.add(data2);
			bdObject.put("$and", dateList);
			DBCursor cursor=new MongoBasicDao().findAlldata(queryMongo, bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				dbCursor = cursor.next();
				drugFee += (Double) dbCursor.get("drugFee");
				totCost += (Double) dbCursor.get("totCost");
			}
			DBObject query = new BasicDBObject();
			query.put("selectTime", temp);//移除数据条件	
			new MongoBasicDao().remove(saveMongo, query);
			
			List<DBObject> voList = new ArrayList<DBObject>();
			BasicDBObject obj = new BasicDBObject();
			obj.append("selectTime", date);
			obj.append("totCost", totCost);
			obj.append("drugFee", drugFee);
			voList.add(obj);
			new MongoBasicDao().insertDataByList(saveMongo, voList);//添加新数据
			wordLoadDocDao.saveMongoLog(beginDate, saveMongo,voList, date);
		}
	}
	/**
	 * 门诊用药监控 用药天数(日)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	@Override
	public void init_MZYYJK_YYTS(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> tnL=wordLoadDocDao.returnInTables(begin, end, outpathientFeedetail, MZ);
			final StringBuffer sb = new StringBuffer();
			sb.append(" select nvl(count(CLINIC_CODE),0) as total ,nvl(sum(days),0) as days from( ");
			sb.append(" select o.CLINIC_CODE as CLINIC_CODE, ");
//			if (DateUtils.parseDateY_M_D_H_M(begin).getTime()>DateUtils.parseDateY_M_D_H_M("2014-11-01 00:00:00").getTime()) {
				sb.append(" nvl(avg(nvl(case  when o.ext_flag3 = 0 then (case ");
				sb.append("  when t.FREQUENCY_UNIT = 'D' then (decode(o.pack_qty,NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/ decode(o.base_dose,NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM)))) ");
				sb.append("  when t.FREQUENCY_UNIT = 'T' then (decode(o.pack_qty,NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/ decode(o.base_dose,NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM)))) ");
				sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.pack_qty, NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) *decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" /(decode(o.dose_once,NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" /(decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))) / 24)");
				sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.pack_qty,NULL,1, DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7  ");
				sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM, 0,1, t.FREQUENCY_NUM)))) ");
				sb.append(" end) else(case");
				sb.append(" when t.FREQUENCY_UNIT = 'D' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1, t.FREQUENCY_NUM)))) ");
				sb.append(" when t.FREQUENCY_UNIT = 'T' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1, t.FREQUENCY_NUM)))) ");
				sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))  / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1, DECODE(t.FREQUENCY_NUM,0, 1, t.FREQUENCY_NUM))) / 24)");
				sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7 ");
				sb.append(" / (decode(o.dose_once, NULL, 1,DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1, DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" /(decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))))");
				sb.append(" end) end, 1)),0) as days ");
//			}else{
//				//时间小于2014-11-01 00:00:00 （T_OUTPATIENT_FEEDETAIL）（qty 数量 就是最小单位的总数量）
//				sb.append(" nvl(avg(nvl(  case ");
//				sb.append(" when t.FREQUENCY_UNIT = 'D' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
//				sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose)))");
//				sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1, t.FREQUENCY_NUM)))) ");
//				sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
//				sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))  / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
//				sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1, DECODE(t.FREQUENCY_NUM,0, 1, t.FREQUENCY_NUM))) / 24) ");
//				sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7  ");
//				sb.append(" / (decode(o.dose_once, NULL, 1,DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1, DECODE(o.base_dose, 0, 1, o.base_dose))) ");
//				sb.append(" /(decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM)))) ");
//				sb.append(" end,  1)),0) as days");
//			}
			sb.append(" from "+tnL.get(0)+" o");
			sb.append(" left join T_BUSINESS_FREQUENCY t on o.FREQUENCY_CODE = t.FREQUENCY_ENCODE ");
			sb.append(" where o.REG_DATE > to_date(:begin,'yyyy-mm-dd hh24:mi:ss')");
			sb.append(" and o.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss')");
			sb.append(" and o.PAY_FLAG != 0 and o.DRUG_FLAG = '1' and o.TRANS_TYPE = 1  and o.CANCEL_FLAG = 1 and o.DAYS = 1 and o.stop_flg = 0 and o.del_flg = 0 and t.stop_flg = 0  and t.del_flg = 0  and (o.reg_dpcd = t.createdept or t.createdept = 'ROOT') ");
			sb.append(" group by o.CLINIC_CODE)");
			SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
			queryObject.addScalar("days",Hibernate.DOUBLE).addScalar("total",Hibernate.INTEGER);
			queryObject.setParameter("begin", begin);
			queryObject.setParameter("end", end);
			List<OutpatientUseMedicVo> list =queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
			DBObject query = new BasicDBObject();
			query.put("selectTime", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_YYTS_DAY", query);//删除原来的数据
			if(list!=null && list.size()>0){
				List<DBObject> voList = new ArrayList<DBObject>();
				for(OutpatientUseMedicVo vo:list){
					BasicDBObject obj = new BasicDBObject();
					obj.append("selectTime", date);
					obj.append("days", vo.getDays());
					obj.append("total", vo.getTotal());
					voList.add(obj);
				 }
				new MongoBasicDao().insertDataByList(menuAlias+"_YYTS_DAY", voList);//添加新数据
				if(!"HIS".equals(type)){
					init_MZYYJK_YYTS_MONTH(menuAlias,"2",date);//月更新用药天数
				}
			}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_YYTS_DAY", list, date);
		}
	}
	/**
	 * 门诊用药监控 用药天数(月)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	@Override
	public void init_MZYYJK_YYTS_MONTH(String menuAlias, String type,
			String date) {
		if ("2".equals(type)) {
			Date beginDate=new Date();
			BasicDBObject bdObject = new BasicDBObject();
			Double days = 0.0d;
			Integer total= 0;
			String queryMongo=menuAlias+"_YYTS_DAY";//查询的表
			String saveMongo=menuAlias+"_YYTS_MONTH";//保存的表
			String temp=date.substring(0,7);//截取月时间
			String begin=temp+"-01";//开始时间
			String end=returnEndTime(date);//计算一个月最后日期
			BasicDBList dateList=new BasicDBList();
			BasicDBObject data1= new BasicDBObject();//查询开始时间
			BasicDBObject data2= new BasicDBObject();//查询结束时间
			data1.append("selectTime", new BasicDBObject("$gte",begin));
			data2.append("selectTime", new BasicDBObject("$lte",end));
			dateList.add(data1);
			dateList.add(data2);
			bdObject.put("$and", dateList);
			DBCursor cursor=new MongoBasicDao().findAlldata(queryMongo, bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				dbCursor = cursor.next();
				days += (Double) dbCursor.get("days");
				total+=(Integer) dbCursor.get("total");	
			}
			DBObject query = new BasicDBObject();
			query.put("selectTime", temp);//移除数据条件	
			new MongoBasicDao().remove(saveMongo, query);
			
			List<DBObject> voList = new ArrayList<DBObject>();
			BasicDBObject obj = new BasicDBObject();
			obj.append("selectTime", date);
			obj.append("avgDays", total==0?0:days/total);
			voList.add(obj);
			new MongoBasicDao().insertDataByList(saveMongo, voList);//添加新数据
			wordLoadDocDao.saveMongoLog(beginDate, saveMongo,voList, date);
		}
	}
	/**
	 * 门诊用药监控 科室用药金额(日)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	@Override
	public void init_MZYYJK_KSYYJE(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> tnL=wordLoadDocDao.returnInTables(begin, end, outpathientFeedetail, MZ);
			final StringBuffer sb = new StringBuffer();
			sb.append("select a.totCost as totCost, a.num as num,o.DEPT_NAME as regDpcdName  from ( ");
			sb.append(" select nvl(sum(t.tot_cost),0) as totCost,t.DOCT_DEPT ,");
			if (DateUtils.parseDateY_M_D_H_M(begin).getTime()>DateUtils.parseDateY_M_D_H_M("2014-11-01 00:00:00").getTime()) {
				sb.append(" nvl(sum((case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end)),0) as num  ");
			}else{
				sb.append(" nvl(sum((decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)))),0) as num  ");
			}
			sb.append(" from "+tnL.get(0)+" t where ");
			sb.append(" t.REG_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss')");
			sb.append(" and t.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss')");
			sb.append(" and t.PAY_FLAG != 0 and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0");
			sb.append(" group by DOCT_DEPT ");
			sb.append(" order by totCost desc) a");
			sb.append(" left join T_DEPARTMENT o on a.DOCT_DEPT = o.DEPT_CODE ");
			SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
			queryObject.addScalar("totCost",Hibernate.DOUBLE).addScalar("regDpcdName",Hibernate.STRING).addScalar("num",Hibernate.DOUBLE);
			queryObject.setParameter("begin", begin);
			queryObject.setParameter("end", end);
			List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
			DBObject query = new BasicDBObject();
			query.put("selectTime", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_KSYYJE_DAY", query);//删除原来的数据
			if(list!=null && list.size()>0){
				List<DBObject> voList = new ArrayList<DBObject>();
				for(OutpatientUseMedicVo vo:list){
					BasicDBObject obj = new BasicDBObject();
					obj.append("selectTime", date);
					obj.append("regDpcdName", vo.getRegDpcdName());
					obj.append("totCost", vo.getTotCost());
					obj.append("num", vo.getNum());
					voList.add(obj);
				 }
				new MongoBasicDao().insertDataByList(menuAlias+"_KSYYJE_DAY", voList);//添加新数据
				if(!"HIS".equals(type)){
					init_MZYYJK_KSYYJE_MONTH(menuAlias,"2",date);//月更新科室用药金额(日)
				}
			}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_KSYYJE_DAY", list, date);
			
		}
	}
	/**
	 * 门诊用药监控 科室用药金额(月)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	@Override
	public void init_MZYYJK_KSYYJE_MONTH(String menuAlias, String type, String date) {
		if ("2".equals(type)) {
			Date beginDate=new Date();
			BasicDBObject bdObject = new BasicDBObject();
			String queryMongo=menuAlias+"_KSYYJE_DAY";//查询的表
			String saveMongo=menuAlias+"_KSYYJE_MONTH";//保存的表
			String temp=date.substring(0,7);//截取月时间
			String begin=temp+"-01";//开始时间
			String end=returnEndTime(date);//计算一个月最后日期
			BasicDBList dateList=new BasicDBList();
			BasicDBObject data1= new BasicDBObject();//查询开始时间
			BasicDBObject data2= new BasicDBObject();//查询结束时间
			data1.append("selectTime", new BasicDBObject("$gte",begin));
			data2.append("selectTime", new BasicDBObject("$lte",end));
			dateList.add(data1);
			dateList.add(data2);
			bdObject.put("$and", dateList);
			DBCursor cursor=new MongoBasicDao().findAlldata(queryMongo, bdObject);
			DBObject dbCursor;
			Map<String,List<Double>> map=new HashMap<String, List<Double>>();
			List<Double> list=null;
			while(cursor.hasNext()){
				dbCursor = cursor.next();
				Double totCost=(Double) dbCursor.get("totCost");	
				Double num=(Double) dbCursor.get("num");
				String regDpcdName=(String)dbCursor.get("regDpcdName");
				boolean flag = map.containsKey(regDpcdName);
				if (flag==true) {
					list=new ArrayList<Double>();
					List<Double> dList = map.get(regDpcdName);
					list.add(dList.get(0)+totCost);
					list.add(dList.get(1)+num);
					map.put(regDpcdName, list);
				}else{
					list=new ArrayList<Double>();
					list.add(totCost);
					list.add(num);
					map.put(regDpcdName, list);
				}
			}
			DBObject query = new BasicDBObject();
			query.put("selectTime", temp);//移除数据条件	
			new MongoBasicDao().remove(saveMongo, query);
			
			List<DBObject> voList = new ArrayList<DBObject>();
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Entry entry = (Entry)it.next();
				BasicDBObject obj = new BasicDBObject();
				obj.append("selectTime", date);
				obj.append("regDpcdName",entry.getKey());
				obj.append("totCost", map.get(entry.getKey()).get(0) );
				obj.append("num", map.get(entry.getKey()).get(1));
				voList.add(obj);
			}
			new MongoBasicDao().insertDataByList(saveMongo, voList);//添加新数据
			wordLoadDocDao.saveMongoLog(beginDate, saveMongo,voList, date);
		}
	}
	/**
	 * 门诊用药监控 门诊月药品金额，用药数量，人次(日)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	@Override
	public void init_MZYYJK_YPJE(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> tnL=wordLoadDocDao.returnInTables(begin, end, outpathientFeedetail, MZ);
			final StringBuffer sb = new StringBuffer();
			sb.append("select a.total as total,a.totCost as totCost,a.num as num,b.code_name as type from (");
			sb.append(" select nvl(sum(total),0) as total,nvl(sum(totCost),0) as totCost, nvl(sum(num),0) as num, DRUG_TYPE from ( ");
			sb.append(" select count(distinct t.clinic_code) as total, sum(t.tot_cost) as totCost,d.DRUG_TYPE,");
			if (DateUtils.parseDateY_M_D_H_M(begin).getTime()>DateUtils.parseDateY_M_D_H_M("2014-11-01 00:00:00").getTime()) {
				sb.append(" sum(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as num  ");
			}else{
				sb.append(" sum(decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty))) as num  ");
			}
			sb.append(" from "+tnL.get(0)+" t left join t_drug_info d on t.ITEM_CODE = d.DRUG_CODE where ");
			sb.append(" t.REG_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss')");
			sb.append(" and t.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss')");
			sb.append(" and t.PAY_FLAG != 0  and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1 and t.stop_flg = 0 and t.del_flg = 0 group by d.DRUG_TYPE)");
			sb.append(" group by DRUG_TYPE )a");
			sb.append(" left join t_business_dictionary b on  b.CODE_ENCODE = a.DRUG_TYPE where b.code_type='drugType'");
			SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
			queryObject.addScalar("num",Hibernate.DOUBLE).addScalar("totCost",Hibernate.DOUBLE).addScalar("total",Hibernate.INTEGER).addScalar("type",Hibernate.STRING);
			queryObject.setParameter("begin", begin);
			queryObject.setParameter("end", end);
			List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
			
			DBObject query = new BasicDBObject();
			query.put("selectTime", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_YPJE_DAY", query);//删除原来的数据
			if(list!=null && list.size()>0){
				List<DBObject> voList = new ArrayList<DBObject>();
				for(OutpatientUseMedicVo vo:list){
					BasicDBObject obj = new BasicDBObject();
					obj.append("selectTime", date);
					obj.append("totCost", vo.getTotCost());
					obj.append("total", vo.getTotal());
					obj.append("num", vo.getNum());
					obj.append("type", vo.getType());
					voList.add(obj);
				 }
				new MongoBasicDao().insertDataByList(menuAlias+"_YPJE_DAY", voList);//添加新数据
				if(!"HIS".equals(type)){
					init_MZYYJK_YPJE_MONTH(menuAlias,"2",date);//月更新 医生用药金额(日)
				}
			}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_YPJE_DAY", list, date);
		}
	}
	/**
	 * 门诊用药监控 门诊月药品金额，用药数量，人次(月)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	@Override
	public void init_MZYYJK_YPJE_MONTH(String menuAlias, String type,
			String date) {
		if ("2".equals(type)) {
			Date beginDate=new Date();
			BasicDBObject bdObject = new BasicDBObject();
			String queryMongo=menuAlias+"_YPJE_DAY";//查询的表
			String saveMongo=menuAlias+"_YPJE_MONTH";//保存的表
			String temp=date.substring(0,7);//截取月时间
			String begin=temp+"-01";//开始时间
			String end=returnEndTime(date);//计算一个月最后日期
			BasicDBList dateList=new BasicDBList();
			BasicDBObject data1= new BasicDBObject();//查询开始时间
			BasicDBObject data2= new BasicDBObject();//查询结束时间
			data1.append("selectTime", new BasicDBObject("$gte",begin));
			data2.append("selectTime", new BasicDBObject("$lte",end));
			dateList.add(data1);
			dateList.add(data2);
			bdObject.put("$and", dateList);
			DBCursor cursor=new MongoBasicDao().findAlldata(queryMongo, bdObject);
			DBObject dbCursor;
			Map<String,List<Object>> map=new HashMap<String, List<Object>>();
			List<Object> list=null;
			while(cursor.hasNext()){
				dbCursor = cursor.next();
				Double totCost=(Double) dbCursor.get("totCost");	
				Double num=(Double) dbCursor.get("num");
				Integer total=(Integer) dbCursor.get("total");
				String types=(String)dbCursor.get("type");
				boolean flag = map.containsKey(types);
				if (flag==true) {
					list=new ArrayList<Object>();
					List<Object> dList = map.get(types);
					list.add((Double)dList.get(0)+totCost);
					list.add((Integer)dList.get(1)+total);
					list.add((Double)dList.get(2)+num);
					map.put(types, list);
				}else{
					list=new ArrayList<Object>();
					list.add(totCost);
					list.add(total);
					list.add(num);
					map.put(types, list);
				}
			}
			DBObject query = new BasicDBObject();
			query.put("selectTime", temp);//移除数据条件	
			new MongoBasicDao().remove(saveMongo, query);
			
			List<DBObject> voList = new ArrayList<DBObject>();
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Entry entry = (Entry)it.next();
				BasicDBObject obj = new BasicDBObject();
				obj.append("selectTime", date);
				obj.append("type",entry.getKey());
				obj.append("totCost", map.get(entry.getKey()).get(0) );
				obj.append("total", map.get(entry.getKey()).get(1));
				obj.append("num", map.get(entry.getKey()).get(2));
				voList.add(obj);
			}
			new MongoBasicDao().insertDataByList(saveMongo, voList);//添加新数据
			wordLoadDocDao.saveMongoLog(beginDate, saveMongo,voList, date);
		}
	}
	/**
	 * 门诊用药监控 医生用药金额(日)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	@Override
	public void init_MZYYJK_YSYYJE(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> tnL=wordLoadDocDao.returnInTables(begin, end, outpathientFeedetail, MZ);
			final StringBuffer sb = new StringBuffer();
			sb.append("select a.totCost as totCost, a.num as num, o.employee_name as doctCodeName  from ( ");
			sb.append(" select nvl(sum(t.tot_cost),0) as totCost, t.doct_code, ");
			if (DateUtils.parseDateY_M_D_H_M(begin).getTime()>DateUtils.parseDateY_M_D_H_M("2014-11-01 00:00:00").getTime()) {
				sb.append(" nvl(sum((case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end)),0) as num  ");
			}else{
				sb.append(" nvl(sum((decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)))),0) as num  ");
			}
			sb.append(" from "+tnL.get(0)+" t where ");
			sb.append(" t.REG_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss')");
			sb.append(" and t.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss')");
			sb.append(" and t.PAY_FLAG != 0 and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0");
			sb.append(" group by doct_code");
			sb.append(" order by totCost desc) a");
			sb.append(" left join T_EMPLOYEE o on a.doct_code = o.employee_jobno ");
			SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
			queryObject.addScalar("totCost",Hibernate.DOUBLE).addScalar("doctCodeName",Hibernate.STRING).addScalar("num",Hibernate.DOUBLE);
			queryObject.setParameter("begin", begin);
			queryObject.setParameter("end", end);
			List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
			DBObject query = new BasicDBObject();
			query.put("selectTime", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_YSYYJE_DAY", query);//删除原来的数据
			if(list!=null && list.size()>0){
				List<DBObject> voList = new ArrayList<DBObject>();
				for(OutpatientUseMedicVo vo:list){
					BasicDBObject obj = new BasicDBObject();
					obj.append("selectTime", date);
					obj.append("doctCodeName", vo.getDoctCodeName());
					obj.append("totCost", vo.getTotCost());
					obj.append("num", vo.getNum());
					voList.add(obj);
				 }
				new MongoBasicDao().insertDataByList(menuAlias+"_YSYYJE_DAY", voList);//添加新数据
				if(!"HIS".equals(type)){
					init_MZYYJK_YSYYJE_MONTH(menuAlias,"2",date);//月更新 医生用药金额(日)
				}
			}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_YSYYJE_DAY", list, date);
		}
	}
	/**
	 * 门诊用药监控 医生用药金额(月)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	@Override
	public void init_MZYYJK_YSYYJE_MONTH(String menuAlias, String type,
			String date) {
		if ("2".equals(type)) {
			Date beginDate=new Date();
			BasicDBObject bdObject = new BasicDBObject();
			String queryMongo=menuAlias+"_YSYYJE_DAY";//查询的表
			String saveMongo=menuAlias+"_YSYYJE_MONTH";//保存的表
			String temp=date.substring(0,7);//截取月时间
			String begin=temp+"-01";//开始时间
			String end=returnEndTime(date);//计算一个月最后日期
			BasicDBList dateList=new BasicDBList();
			BasicDBObject data1= new BasicDBObject();//查询开始时间
			BasicDBObject data2= new BasicDBObject();//查询结束时间
			data1.append("selectTime", new BasicDBObject("$gte",begin));
			data2.append("selectTime", new BasicDBObject("$lte",end));
			dateList.add(data1);
			dateList.add(data2);
			bdObject.put("$and", dateList);
			DBCursor cursor=new MongoBasicDao().findAlldata(queryMongo, bdObject);
			DBObject dbCursor;
			Map<String,List<Double>> map=new HashMap<String, List<Double>>();
			List<Double> list=null;
			while(cursor.hasNext()){
				dbCursor = cursor.next();
				Double totCost=(Double) dbCursor.get("totCost");	
				Double num=(Double) dbCursor.get("num");
				String doctCodeName=(String)dbCursor.get("doctCodeName");
				boolean flag = map.containsKey(doctCodeName);
				if (flag==true) {
					list=new ArrayList<Double>();
					List<Double> dList = map.get(doctCodeName);
					list.add(dList.get(0)+totCost);
					list.add(dList.get(1)+num);
					map.put(doctCodeName, list);
				}else{
					list=new ArrayList<Double>();
					list.add(totCost);
					list.add(num);
					map.put(doctCodeName, list);
				}
			}
			DBObject query = new BasicDBObject();
			query.put("selectTime", temp);//移除数据条件	
			new MongoBasicDao().remove(saveMongo, query);
			
			List<DBObject> voList = new ArrayList<DBObject>();
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Entry entry = (Entry)it.next();
				BasicDBObject obj = new BasicDBObject();
				obj.append("selectTime", date);
				obj.append("doctCodeName",entry.getKey());
				obj.append("totCost", map.get(entry.getKey()).get(0) );
				obj.append("num", map.get(entry.getKey()).get(1));
				voList.add(obj);
			}
			new MongoBasicDao().insertDataByList(saveMongo, voList);//添加新数据
			wordLoadDocDao.saveMongoLog(beginDate, saveMongo,voList, date);
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
}
