package cn.honry.statistics.bi.bistac.kpi.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.MongoLog;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.bi.bistac.kpi.dao.KpiDao;
import cn.honry.statistics.bi.bistac.kpi.service.KpiService;
import cn.honry.statistics.bi.bistac.kpi.vo.KpiVo;
import cn.honry.statistics.bi.bistac.mongoDataInit.dao.MongoDataInitDao;
import cn.honry.statistics.bi.bistac.outpatientDocRecipe.vo.OutpatientDocRecipeVo;
import cn.honry.statistics.bi.bistac.outpatientDocRecipe.vo.StatisticsVo;
import cn.honry.statistics.finance.inoutstore.dao.InOutStoreDao;
import cn.honry.statistics.util.customVo.CustomVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
@Service("kpiService")
@Transactional
@SuppressWarnings({"all"})
public class KpiServiceImpl implements KpiService{
	@Autowired
	@Qualifier("kpiDao")
	private KpiDao kpiDao;
	@Resource
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value = "mongoDataInitDao")
	private MongoDataInitDao mongoDataInitDao;
	@Override
	public KpiVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(KpiVo arg0) {
	}

	@Override
	public int[] getTotalTime(String danw, String time) {
		String first ="";
		String end ="";
		Date sTime = null;
		Date eTime = null;
		if("0".equals(danw)){
			first = time+"-01-01 00:00:00";
			end = time+"-12-31 23:59:59";
			sTime = DateUtils.parseDateY_M_D(first);
			eTime = DateUtils.parseDateY_M_D(end);
		}else if("1".equals(danw)||danw.endsWith("2")){
	        int year =Integer.parseInt(time.split("-")[0]);
	        first = year+"-01-01 00:00:00";
	        end = year+"-12-31 23:59:59";
	        sTime = DateUtils.parseDateY_M_D(first);
	        eTime = DateUtils.parseDateY_M_D(end);
		}
		List<String> tnL = getTnL(sTime,eTime,first,end);
		return kpiDao.getTotalTime(tnL,danw,time);
	}
	@Override
	public List<Object[]> queryEverMonth(String danw, String time) {
		String first ="";
		String end ="";
		Date sTime = null;
		Date eTime = null;
		if("0".equals(danw)){
			first = time+"-01-01 00:00:00";
			end = time+"-12-31 23:59:59";
			sTime = DateUtils.parseDateY_M_D(first);
			eTime = DateUtils.parseDateY_M_D(end);
		}else if("1".equals(danw)||danw.endsWith("2")){
//			Calendar cal = Calendar.getInstance();  
//	        int year = cal.get(Calendar.YEAR);
		    int year =Integer.parseInt(time.split("-")[0]);
	        first = year+"-01-01 00:00:00";
	        end = year+"-12-31 23:59:59";
	        sTime = DateUtils.parseDateY_M_D(first);
	        eTime = DateUtils.parseDateY_M_D(end);
		}
		List<String> tnL = getTnL(sTime,eTime,first,end);
		return kpiDao.queryEverMonth(tnL,danw,time);
	}

	@Override
	public int[] compareToBefore(String danw, String time) {
		Date sTime =null;
		Date eTime = null;
		String first="";
		String end="";
		if("0".equals(danw)){
			int data = Integer.parseInt(time)-1;
			time = data+"";
			first = time+"-01-01 00:00:00";
			end = time+"-12-31 23:59:59";
			sTime = DateUtils.parseDateY_M_D(first);
			eTime = DateUtils.parseDateY_M_D(end);
		}else if("1".equals(danw)||danw.endsWith("2")){
//			Calendar cal = Calendar.getInstance();  
//	        int year = cal.get(Calendar.YEAR)-1;
		    int year =Integer.parseInt(time.split("-")[0])-1;
	        first = year+"-01-01 00:00:00";
	        end = year+"-12-31 23:59:59";
	        sTime = DateUtils.parseDateY_M_D(first);
	        eTime = DateUtils.parseDateY_M_D(end);
		}
		List<String> tnL = getTnL(sTime,eTime,first,end);
		return kpiDao.compareToBefore(tnL,danw,time);
	}

	@Override
	public List<Object[]> everMonthToCom(String danw, String time) {
		Date sTime =null;
		Date eTime = null;
		String first="";
		String end="";
		if("0".equals(danw)){
			int data = Integer.parseInt(time)-1;
			time = data+"";
			first = time+"-01-01 00:00:00";
			end = time+"-12-31 23:59:59";
			sTime = DateUtils.parseDateY_M_D(first);
			eTime = DateUtils.parseDateY_M_D(end);
		}else if("1".equals(danw)||danw.endsWith("2")){
//			Calendar cal = Calendar.getInstance();  
//	        int year = cal.get(Calendar.YEAR)-1;
	        int year =Integer.parseInt(time.split("-")[0])-1;
	        first = year+"-01-01 00:00:00";
	        end = year+"-12-31 23:59:59";
	        sTime = DateUtils.parseDateY_M_D(first);
	        eTime = DateUtils.parseDateY_M_D(end);
		}
		List<String> tnL = getTnL(sTime,eTime,first,end);
		return kpiDao.everMonthToCom(tnL,danw,time);
	}
	
	public List<String> getTnL(Date sTime,Date eTime,String first,String end){
		//获取当前表最大时间及最小时间
		//StatVo statVo = kpiDao.findMaxMin();
		
		List<String> tnL = new ArrayList<String>();
		//获取门诊数据保留时间
		String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
		//获得当前时间
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dTime=null;
		try {
			dTime = df.parse(df.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//获得在线库数据应保留最小时间
		Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
		//判断查询类型
		if(DateUtils.compareDate(sTime, cTime)==-1){
			if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
				//获取需要查询的全部分区
				tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",first,end);
			}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
				//获得时间差(年)
				int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),first);
				//获取相差年分的分区集合，默认加1
				tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",yNum+1);
				tnL.add(0,"T_REGISTER_MAIN_NOW");
			}
		}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
			tnL.add("T_REGISTER_MAIN_NOW");
		}	
		return tnL;
	}
	@Override
	public int getMJZCount() {
		return kpiDao.getMJZCount();
	}

	@Override
	public void init_MZKPI(String beginDate, String endDate, Integer type) throws Exception {
		//表名
		String collectionName = null;
		//mong日志
		MongoLog mong = new MongoLog();
		//结果list
		List<KpiVo> resultList = new ArrayList<KpiVo>();
		mong.setId(null);
		mong.setCountStartTime(new Date());
		mong.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		mong.setCreateTime(new Date());
		mong.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		String[] st = beginDate.split("-");
		String[] et = endDate.split("-");
		final String sTime = st.length==2?beginDate+"-01":st.length==1?beginDate+"-01-01":beginDate;
		final String eTime = et.length==2?endDate+"-01":et.length==1?endDate+"-01-01":endDate;
		mong.setEndTime(DateUtils.parseDateY_M_D(eTime));
		mong.setStartTime(DateUtils.parseDateY_M_D(sTime));
		if("1".equals(type+"")){//日
			mong.setMenuType("MZKPI_DAY");
			collectionName = "MZKPI_DAY";
		}else if("2".equals(type+"")){//月
			mong.setMenuType("MZKPI_MONTH");
			collectionName = "MZKPI_MONTH";
		}else if("3".equals(type+"")){//年
			mong.setMenuType("MZKPI_YEAR");
			collectionName = "MZKPI_YEAR";
		}
		Integer size =null;
		if(type==null ||type==1){
			//按日统计
			resultList =kpiDao.initMZKPI(beginDate, endDate);
			if(resultList!=null&&resultList.size()>0){
				size =  resultList.size();
				DBObject obj = new BasicDBObject();
				obj.put("date", beginDate);
				new MongoBasicDao().remove("MZKPI_DAY",obj);
				long  totalNum=0;
				long outNum=0;
				long zjNum=0;
				long jzShNum=0;
				long jzZlNum=0;
				totalNum = Long.parseLong(resultList.get(0).getNumFix());
				outNum = Long.parseLong(resultList.get(1).getNumFix());
				zjNum = Long.parseLong(resultList.get(2).getNumFix());
				jzShNum = Long.parseLong(resultList.get(3).getNumFix());
				jzZlNum = Long.parseLong(resultList.get(4).getNumFix());
				Document doc = new Document();
				doc.append("date", beginDate)
				.append("totalNum", totalNum)
				.append("outNum", outNum)
				.append("zjNum", zjNum)
				.append("jzShNum", jzShNum)
				.append("jzZlNum",jzZlNum);
				new MongoBasicDao().insertData(collectionName, doc);
			}
		}else if(type==2){
			//按月统计
			Date date1 = DateUtils.parseDateY_M(beginDate);
			Date date2 = DateUtils.parseDateY_M(endDate);
			while(DateUtils.compareDate(date1, date2)<=0){
				String sdate = DateUtils.formatDateY_M(date1);//当前月
				Date nextDate = DateUtils.addMonth(date1, 1);//下一月
				String edate = DateUtils.formatDateY_M(nextDate);
				BasicDBList dblist = new BasicDBList();
				BasicDBObject sdb = new BasicDBObject();
				sdb.put("date", new BasicDBObject("$gte", sdate));
				BasicDBObject edb = new BasicDBObject();
				edb.put("date", new BasicDBObject("$lt", edate));
				dblist.add(sdb);
				dblist.add(edb);
				BasicDBObject where = new BasicDBObject();
				where.put("$and", dblist);
				DBCursor cursor = new MongoBasicDao().findAlldata("MZKPI_DAY", where);
				size= (int) new MongoBasicDao().findAllCountBy("MZKPI_DAY", where).longValue();
				DBObject obj = new BasicDBObject();
				obj.put("date", sdate);
				new MongoBasicDao().remove(collectionName,obj);
				
				long  totalNum=0;
				long outNum=0;
				long zjNum=0;
				long jzShNum=0;
				long jzZlNum=0;
				while(cursor.hasNext()){
					DBObject next = cursor.next();
					totalNum += (Long)next.get("totalNum");
					outNum += (Long)next.get("outNum");
					zjNum += (Long)next.get("zjNum");
					jzShNum += (Long)next.get("jzShNum");
					jzZlNum += (Long)next.get("jzZlNum");
					
				}
				Document doc = new Document();
				doc.append("date", sdate)
				.append("totalNum", totalNum)
				.append("outNum", outNum)
				.append("zjNum", zjNum)
				.append("jzShNum", jzShNum)
				.append("jzZlNum",jzZlNum);
				new MongoBasicDao().insertData(collectionName, doc);
				date1=nextDate;//nextDate-->2010-02-01
			}
		}else{//按年统计
			Date date1 = DateUtils.parseDateY(beginDate);
			Date date2 = DateUtils.parseDateY(endDate);
			while(DateUtils.compareDate(date1, date2)<=0){
				String sdate = DateUtils.formatDateY(date1);//当前年
				Date nextDate = DateUtils.addYear(date1, 1);//下一年
				String edate = DateUtils.formatDateY(nextDate);
				BasicDBList dblist = new BasicDBList();
				BasicDBObject sdb = new BasicDBObject();
				sdb.put("date", new BasicDBObject("$gte", sdate));
				BasicDBObject edb = new BasicDBObject();
				edb.put("date", new BasicDBObject("$lt", edate));
				dblist.add(sdb);
				dblist.add(edb);
				BasicDBObject where = new BasicDBObject();
				where.put("$and", dblist);
				DBCursor cursor = new MongoBasicDao().findAlldata("MZKPI_MONTH", where);
				size= (int) new MongoBasicDao().findAllCountBy("MZKPI_MONTH", where).longValue();
				DBObject obj = new BasicDBObject();
				obj.put("date", sdate);
				new MongoBasicDao().remove(collectionName,obj);
				long  totalNum=0;
				long outNum=0;
				long zjNum=0;
				long jzShNum=0;
				long jzZlNum=0;
				while(cursor.hasNext()){
					DBObject next = cursor.next();
					totalNum += (Long)next.get("totalNum");
					outNum += (Long)next.get("outNum");
					zjNum += (Long)next.get("zjNum");
					jzShNum += (Long)next.get("jzShNum");
					jzZlNum += (Long)next.get("jzZlNum");
				}
				Document doc = new Document();
				doc.append("date", sdate)
				.append("totalNum", totalNum)
				.append("outNum", outNum)
				.append("zjNum", zjNum)
				.append("jzShNum", jzShNum)
				.append("jzZlNum",jzZlNum);
				new MongoBasicDao().insertData(collectionName, doc);
				date1=nextDate;//nextDate-->2010-02-01
			}
		}
		mong.setCountEndTime(new Date());
		mong.setTotalNum(size);
		mong.setState(1);
		mongoDataInitDao.saveMongoLog(mong);
	}

	@Override
	public Map<String, Object> queryAllData(String danw, String time) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		String cxSTime="";
		String cxETime="";
		String cxSTimeYed="";
		String cxETimeYed="";
		String fromName="";
		String monthTime="";
		List<KpiVo> voList = new ArrayList<KpiVo>();
		if("0".equals(danw)){//年
			monthTime = time;
			Date nowDate = new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
			String str=sdf.format(nowDate);
			String[] s = str.split("-");
			if(time.equals(s[0])){
				cxSTime =time+"-01";
				cxETime = s[0]+"-"+s[1];
				cxSTimeYed=(Integer.parseInt(time)-1)+"-01";
				cxETimeYed=(Integer.parseInt(s[0])-1)+"-"+s[1];
				fromName = "MZKPI_MONTH";
			}else{
				cxSTime = time;
				cxETime = time;
				cxSTimeYed =(Integer.parseInt(time)-1)+"";
				cxETimeYed =(Integer.parseInt(time)-1)+"";
				fromName = "MZKPI_YEAR";
			}
			BasicDBList dblist = new BasicDBList();
			BasicDBObject sdb = new BasicDBObject();
			BasicDBObject edb = new BasicDBObject();
			sdb.put("date", new BasicDBObject("$gte", cxSTime));
			edb.put("date", new BasicDBObject("$lte", cxETime));
			dblist.add(sdb);
			dblist.add(edb);
			BasicDBObject where = new BasicDBObject();
			where.put("$and", dblist);
			//根据科室医生和时间查询数据
			DBCursor cursor = new MongoBasicDao().findAlldataBySort(fromName,where,"date");
			long  totalNum=0;
			long outNum=0;
			long zjNum=0;
			long jzShNum=0;
			long jzZlNum=0;
			while(cursor.hasNext()){
				DBObject next = cursor.next();
				totalNum += (Long)next.get("totalNum");
				outNum += (Long)next.get("outNum");
				zjNum += (Long)next.get("zjNum");
				jzShNum += (Long)next.get("jzShNum");
				jzZlNum += (Long)next.get("jzZlNum");
			}
			KpiVo vo = new KpiVo();
			vo.setJzShNum(jzShNum+"");
			vo.setJzZlNum(jzZlNum+"");
			vo.setOutNum(outNum+"");
			vo.setTotalNum(totalNum+"");
			vo.setZjNum(zjNum+"");
			map.put("measure", vo);
//			voList.add(vo);
			//去年数据
			BasicDBList dblistYed = new BasicDBList();
			BasicDBObject ddb = new BasicDBObject();
			BasicDBObject erdb = new BasicDBObject();
			ddb.put("date", new BasicDBObject("$gte", cxSTimeYed));
			erdb.put("date", new BasicDBObject("$lte", cxETimeYed));
			dblistYed.add(ddb);
			dblistYed.add(erdb);
			BasicDBObject whereYed = new BasicDBObject();
			whereYed.put("$and", dblistYed);
			//根据科室医生和时间查询数据
			DBCursor cursorYed = new MongoBasicDao().findAlldataBySort(fromName,whereYed,"date");
			long  totalNumYed=0;
			long outNumYed=0;
			long zjNumYed=0;
			long jzShNumYed=0;
			long jzZlNumYed=0;
			while(cursorYed.hasNext()){
				DBObject next = cursorYed.next();
				totalNumYed += (Long)next.get("totalNum");
				outNumYed += (Long)next.get("outNum");
				zjNumYed += (Long)next.get("zjNum");
				jzShNumYed += (Long)next.get("jzShNum");
				jzZlNumYed += (Long)next.get("jzZlNum");
			}
			KpiVo voYed = new KpiVo();
			voYed.setJzShNum(jzShNum-jzShNumYed+"");
			voYed.setJzZlNum(jzZlNum-jzZlNumYed+"");
			voYed.setOutNum(outNum-outNumYed+"");
			voYed.setTotalNum(totalNum-totalNumYed+"");
			voYed.setZjNum(zjNum-zjNumYed+"");
			map.put("increase", voYed);
//			voList.add(voYed);
		}else if("1".equals(danw)||danw.endsWith("2")){//月
			String[] t = time.split("-");
			monthTime = t[0];
			String timeYed =(Integer.parseInt(t[0])-1)+"-"+t[1];
			BasicDBList dblist = new BasicDBList();
			BasicDBObject sdb = new BasicDBObject();
			BasicDBObject edb = new BasicDBObject();
			sdb.put("date", new BasicDBObject("$gte", time));
			edb.put("date", new BasicDBObject("$lte", time));
			dblist.add(sdb);
			dblist.add(edb);
			BasicDBObject where = new BasicDBObject();
			where.put("$and", dblist);
			//根据科室医生和时间查询数据
			DBCursor cursor = new MongoBasicDao().findAlldataBySort("MZKPI_MONTH",where,"date");
			long  totalNum=0;
			long outNum=0;
			long zjNum=0;
			long jzShNum=0;
			long jzZlNum=0;
			while(cursor.hasNext()){
				DBObject next = cursor.next();
				totalNum += (Long)next.get("totalNum");
				outNum += (Long)next.get("outNum");
				zjNum += (Long)next.get("zjNum");
				jzShNum += (Long)next.get("jzShNum");
				jzZlNum += (Long)next.get("jzZlNum");
			}
			KpiVo vo = new KpiVo();
			vo.setJzShNum(jzShNum+"");
			vo.setJzZlNum(jzZlNum+"");
			vo.setOutNum(outNum+"");
			vo.setTotalNum(totalNum+"");
			vo.setZjNum(zjNum+"");
//			voList.add(vo);
			map.put("measure", vo);
			//去年数据
			BasicDBList dblistYed = new BasicDBList();
			BasicDBObject ddb = new BasicDBObject();
			BasicDBObject erdb = new BasicDBObject();
			ddb.put("date", new BasicDBObject("$gte", timeYed));
			erdb.put("date", new BasicDBObject("$lte", timeYed));
			dblistYed.add(ddb);
			dblistYed.add(erdb);
			BasicDBObject whereYed = new BasicDBObject();
			whereYed.put("$and", dblistYed);
			//根据科室医生和时间查询数据
			DBCursor cursorYed = new MongoBasicDao().findAlldataBySort("MZKPI_MONTH",whereYed,"date");
			long  totalNumYed=0;
			long outNumYed=0;
			long zjNumYed=0;
			long jzShNumYed=0;
			long jzZlNumYed=0;
			while(cursorYed.hasNext()){
				DBObject next = cursorYed.next();
				totalNumYed += (Long)next.get("totalNum");
				outNumYed += (Long)next.get("outNum");
				zjNumYed += (Long)next.get("zjNum");
				jzShNumYed += (Long)next.get("jzShNum");
				jzZlNumYed += (Long)next.get("jzZlNum");
			}
			KpiVo voYed = new KpiVo();
			voYed.setJzShNum(jzShNum-jzShNumYed+"");
			voYed.setJzZlNum(jzZlNum-jzZlNumYed+"");
			voYed.setOutNum(outNum-outNumYed+"");
			voYed.setTotalNum(totalNum-totalNumYed+"");
			voYed.setZjNum(zjNum-zjNumYed+"");
			map.put("increase", voYed);
//			voList.add(voYed);
		}
		long[]  totalNumArr= new long[12];
		long[] outNumArr=new long[12];
		long[] zjNumArr=new long[12];
		long[] jzShNumArr=new long[12];
		long[] jzZlNumArr=new long[12];
		Date date1 = DateUtils.parseDateY_M(monthTime+"-01");
		Date date2 = DateUtils.parseDateY_M(monthTime+"-12");
		int i=0;
		while(DateUtils.compareDate(date1, date2)<=0){
			String sdate = DateUtils.formatDateY_M(date1);//当前月
			Date nextDate = DateUtils.addMonth(date1, 1);//下一月
			String edate = DateUtils.formatDateY_M(nextDate);
			BasicDBList dblist = new BasicDBList();
			BasicDBObject sdb = new BasicDBObject();
			sdb.put("date", new BasicDBObject("$gte", sdate));
			BasicDBObject edb = new BasicDBObject();
			edb.put("date", new BasicDBObject("$lte", sdate));
			dblist.add(sdb);
			dblist.add(edb);
			BasicDBObject where = new BasicDBObject();
			where.put("$and", dblist);
			DBCursor cursorYed = new MongoBasicDao().findAlldataBySort("MZKPI_MONTH",where,"date");
			if(cursorYed.hasNext()){
				DBObject next = cursorYed.next();
				totalNumArr[i]=(Long) next.get("totalNum");
				outNumArr[i]=(Long)next.get("outNum");
				zjNumArr[i]=(Long)next.get("zjNum");
				jzShNumArr[i]=(Long)next.get("jzShNum");
				jzZlNumArr[i]=(Long)next.get("jzZlNum");
			}else{
				totalNumArr[i]=0;
				outNumArr[i]=0;
				zjNumArr[i]=0;
				jzShNumArr[i]=0;
				jzZlNumArr[i]=0;
			}
			i++;
			date1=nextDate;//nextDate-->2010-02-01
		}
		map.put("totalNumArr", totalNumArr);
		map.put("outNumArr", outNumArr);
		map.put("zjNumArr", zjNumArr);
		map.put("jzShNumArr", jzShNumArr);
		map.put("jzZlNumArr", jzZlNumArr);
		long[]  totalNumArrmid= new long[12];
		long[] outNumArrmid=new long[12];
		long[] zjNumArrmid=new long[12];
		long[] jzShNumArrmid=new long[12];
		long[] jzZlNumArrmid=new long[12];
		Date date1ed = DateUtils.parseDateY_M((Integer.parseInt(monthTime)-1)+"-01");
		Date date2ed = DateUtils.parseDateY_M((Integer.parseInt(monthTime)-1)+"-12");
		int k=0;
		while(DateUtils.compareDate(date1ed, date2ed)<=0){
			String sdate = DateUtils.formatDateY_M(date1ed);//当前月
			Date nextDate = DateUtils.addMonth(date1ed, 1);//下一月
			String edate = DateUtils.formatDateY_M(nextDate);
			BasicDBList dblist = new BasicDBList();
			BasicDBObject sdb = new BasicDBObject();
			sdb.put("date", new BasicDBObject("$gte", sdate));
			BasicDBObject edb = new BasicDBObject();
			edb.put("date", new BasicDBObject("$lte", sdate));
			dblist.add(sdb);
			dblist.add(edb);
			BasicDBObject where = new BasicDBObject();
			where.put("$and", dblist);
			DBCursor cursorYed = new MongoBasicDao().findAlldataBySort("MZKPI_MONTH",where,"date");
			if(cursorYed.hasNext()){
				DBObject next = cursorYed.next();
				totalNumArrmid[k]=(Long)next.get("totalNum");
				outNumArrmid[k]=(Long)next.get("outNum");
				zjNumArrmid[k]=(Long)next.get("zjNum");
				jzShNumArrmid[k]=(Long)next.get("jzShNum");
				jzZlNumArrmid[k]=(Long)next.get("jzZlNum");
			}else{
				totalNumArrmid[k]=0;
				outNumArrmid[k]=0;
				zjNumArrmid[k]=0;
				jzShNumArrmid[k]=0;
				jzZlNumArrmid[k]=0;
			}
			k++;
			date1ed=nextDate;//nextDate-->2010-02-01
		}
		long[]  totalNumArred= new long[12];
		long[] outNumArred=new long[12];
		long[] zjNumArred=new long[12];
		long[] jzShNumArred=new long[12];
		long[] jzZlNumArred=new long[12];
		for(int m=0;m<12;m++){
			totalNumArred[m] =totalNumArr[m]-totalNumArrmid[m];
			outNumArred[m] =outNumArr[m]-outNumArrmid[m];
			zjNumArred[m] =zjNumArr[m]-zjNumArrmid[m];
			jzShNumArred[m] =jzShNumArr[m]-jzShNumArrmid[m];
			jzZlNumArred[m] =jzZlNumArr[m]-jzZlNumArrmid[m];
		}
		map.put("totalNumArred", totalNumArred);
		map.put("outNumArred", outNumArred);
		map.put("zjNumArred", zjNumArred);
		map.put("jzShNumArred", jzShNumArred);
		map.put("jzZlNumArred", jzZlNumArred);
		return map;
	}
}
