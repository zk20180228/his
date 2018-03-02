package cn.honry.statistics.bi.bistac.monthlyDashboard.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inner.statistics.monthlyDashboard.dao.InnerMonthLyDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.monthlyDashboard.dao.MonthlyDashboardDao;
import cn.honry.statistics.bi.bistac.monthlyDashboard.service.MonthlyDashboardService;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.MonthlyDashboardVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Service("monthlyDashboardService")
@Transactional
@SuppressWarnings({ "all" })
public class MonthlyDashboardServiceImpl implements MonthlyDashboardService{
	static SimpleDateFormat sdFull=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static Calendar ca=Calendar.getInstance();
	@Autowired
	@Qualifier(value = "monthlyDashboardDao")
	private MonthlyDashboardDao monthlyDashboardDao;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value="innerMonthLyDao")
	private InnerMonthLyDao innerMonthLyDao;
	/**  
	 * 
	 * 月出院人数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月31日 上午10:35:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月31日 上午10:35:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public List<MonthlyDashboardVo> queryInpatientInfoNowGo(String date, String deptName) throws Exception {
		return monthlyDashboardDao.queryInpatientInfoNowGo(date, deptName);
		
	}
	
	@Override
	public MonthlyDashboardVo queryLastYearGo( String date,String deptName) {
		//获取当前表最大时间及最小时间
		List<String> tnL = null;
		String begin=null;
		String end=null;
		Date d;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(begin);
			Date eTime = DateUtils.parseDateY_M_D(end);
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",begin,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",yNum+1);
					tnL.add(0,"T_INPATIENT_INFO_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_INFO_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**  
	 * 
	 * 月手术例数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月31日 上午10:35:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月31日 上午10:35:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public MonthlyDashboardVo queryOperationApply(String date, String deptCode) throws Exception {
		return monthlyDashboardDao.queryOperationApply(date,deptCode);
	}
	/**  
	 * 
	 * 平均住院日
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月1日 上午10:39:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月1日 上午10:39:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */	
	@Override
	public List<MonthlyDashboardVo> queryInpatientInfo(String deptName) throws Exception {
		return monthlyDashboardDao.queryInpatientInfo(deptName);
	}
	/**  
	 * 
	 * 做手术人均住院日
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月1日 上午10:39:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月1日 上午10:39:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */	
	@Override
	public List<MonthlyDashboardVo> queryOperationApplyInfo(String deptName) throws Exception {
		return monthlyDashboardDao.queryOperationApplyInfo(deptName);
	}
	/**  
	 * 
	 * 使用中床位、总床位
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 上午11:42:19 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 上午11:42:19 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public MonthlyDashboardVo queryBed(String date, String deptCode) throws Exception {
		return monthlyDashboardDao.queryBed(date, deptCode);
	}
	/**  
	 * 
	 * 治疗数量
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 下午2:26:04 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 下午2:26:04 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public List<MonthlyDashboardVo> queryTreatment(String date, String deptName) throws Exception {
		//获取当前表最大时间及最小时间
		List<String> tnL = null;
		String begin=null;
		String end=null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(begin);
			Date eTime = DateUtils.parseDateY_M_D(end);
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",begin,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",yNum+1);
					tnL.add(0,"T_INPATIENT_INFO_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_INFO_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return monthlyDashboardDao.queryTreatment(tnL, date, deptName,begin,end);
	}
	/**  
	 * 
	 * 住院费用
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 下午2:26:04 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 下午2:26:04 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public List<MonthlyDashboardVo> queryHospExpenses(String startDate,String endDate, String deptName) throws Exception {
		return monthlyDashboardDao.queryHospExpenses( deptName, endDate);
		
	}
	/**  
	 * 
	 * 住院费用mongdb没有数据时查分区
	 * @Author: wangshujuan
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public List<MonthlyDashboardVo> queryHospExpenses2(String startDate,String endDate, String deptName) throws Exception {
		//获取当前表最大时间及最小时间
		List<String> tnL = null;
		String begin=null;
		String end=null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(startDate);
			Date eTime = DateUtils.parseDateY_M_D(endDate);
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",startDate,endDate);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startDate);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",yNum+1);
					tnL.add(0,"T_INPATIENT_FEEINFO_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_FEEINFO_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return monthlyDashboardDao.queryHospExpenses2(tnL, startDate, endDate);
	}
		
	@Override
	public List<Dashboard> querySurgeryForDB(String date, String dept) throws Exception {
		return monthlyDashboardDao.querySurgeryForDB(date, dept);
	}


	@Override
	public List<Dashboard> queryTreatmentFormDB(String date, String dept) throws Exception {
		return monthlyDashboardDao.queryTreatmentFromDB(date, dept);
	}
	
	@Override
	public List<Dashboard> queryTreatment2(String dateS,String dateE, String dept) throws Exception {
		List<String> tnL = null;
		try{
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(dateS);
			Date eTime = DateUtils.parseDateY_M_D(dateE);
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
		//判断查询类型
		if(DateUtils.compareDate(sTime, cTime)==-1){
			if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
				//获取需要查询的全部分区
				tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",dateS,dateE);
			}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
				//获得时间差(年)
				int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),dateS);
				//获取相差年分的分区集合，默认加1
				tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",yNum+1);
				tnL.add(0,"T_INPATIENT_INFO_NOW");
			}
		}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
			tnL.add("T_INPATIENT_INFO_NOW");
		}	
		
		}catch(Exception e){
			e.printStackTrace();
			tnL = null;
		}
		return monthlyDashboardDao.svaeTreatment(tnL, dateS, dateE,dept);
	}
	@Override
	public List<Dashboard> queryWardApply(String date, String deptCode) throws Exception {
		return monthlyDashboardDao.queryWardApplyFromDB(date, deptCode);
	}
	@Override
	public boolean initDB() throws Exception {
		boolean sign;
		sign=monthlyDashboardDao.saveSurgeryToDB();
		sign=monthlyDashboardDao.svaeWardApplyToDB();
		sign=monthlyDashboardDao.saveHospExpensesToDBYear();
		sign=monthlyDashboardDao.saveHospExpensesToDBDay();
		return sign;
	}

	@Override
	public boolean initDBOneDay() throws Exception {
		boolean sign;
		sign=monthlyDashboardDao.saveInpatientInfoToDBOneDay();//月出院更新
		sign=monthlyDashboardDao.saveHospExpensesToDBOneDay();//住院费用
		sign=monthlyDashboardDao.svaeTreatmentToDBOneDay();//治疗数量
		sign=monthlyDashboardDao.saveSurgeryToDBOneDay();//手术例数
		sign=monthlyDashboardDao.svaeWardApplyToDBOneDay();//病床使用率
		return sign;
	}

	@Override
	public boolean initDBTwo() throws Exception {
		boolean sign=false;
		sign=monthlyDashboardDao.saveInpatientInfoToDB();
		sign=monthlyDashboardDao.saveHospExpensesToDBMonth();
		sign=monthlyDashboardDao.svaeTreatmentToDB();
		return false;
	}

	@Override
	public Boolean isCollection(String name) throws Exception {
		return monthlyDashboardDao.isCollection(name);
	}

	@Override
	public List<Dashboard> queryWardApplyFromOracle(String firstData,
			String endData, String deptCode) throws Exception {
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(firstData);
			Date eTime = DateUtils.parseDateY_M_D(endData);
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",firstData,endData);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),firstData);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",yNum+1);
					tnL.add(0,"T_INPATIENT_INFO_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_INFO_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return monthlyDashboardDao.queryWardApplyFromOracle(tnL,deptCode,firstData,endData);
	}

	@Override
	public List<MonthlyDashboardVo> queryInpatientInfoFromOracle(
			String startTime, String endTime, String deptName) throws Exception {
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(startTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",yNum+1);
					tnL.add(0,"T_INPATIENT_INFO_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_INFO_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return monthlyDashboardDao.queryInpatientInfoFromOracle(tnL,deptName,startTime,endTime);
	}
/**
 * @throws Exception **********************************************************************************************************************/
	@Override
	public void initHospExpensesToDBMonth(String startTime, String endTime) throws Exception {
		boolean sign= betweenDateSign(startTime, endTime);
		Map<String,String> map;
		Integer hours=null;
		if(!sign){
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}else{
			hours=1;
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}
		String[] tables={"T_INPATIENT_FEEINFO","T_INPATIENT_FEEINFO_NOW"};
		//循环时间段
		for(String dateArr:map.keySet()){
			startTime=dateArr;//查询的时间段
			endTime=map.get(dateArr);//查询的时间段
			List<String> tnL=returnPartitions(startTime,endTime,tables);
			if(tnL==null){
				continue;
			}
			monthlyDashboardDao.initHospExpensesToDBDay(tnL, startTime, endTime, hours);
		}
		
	}
	
	public List<String> returnPartitions(String startTime,String endTime,String[] tables){
		List<String> tnL = null;
		Date sTime = DateUtils.parseDateY_M_D(startTime);
		Date eTime = DateUtils.parseDateY_M_D(endTime);
		try {
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if(dateNum.equals("1")){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,tables[0],startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,tables[0],yNum+1);
					tnL.add(0,tables[1]);
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add(tables[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = new ArrayList<String>();
		}
		return tnL;
	}

	public static boolean betweenDateSign(String begin,String end){
		boolean sign=false;
		try {
	if(StringUtils.isNotBlank(begin)){
		String[] dateone=begin.split(" ");
		String[] dateArr=dateone[0].split("-");
		String[] dateArr1=dateone[1].split(":");
		Date date1= sdFull.parse(end);//结束时间
		ca.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1])-1, Integer.parseInt(dateArr[2]),Integer.parseInt(dateArr1[0]),Integer.parseInt(dateArr1[1]),Integer.parseInt(dateArr1[2]));
		ca.add(Calendar.HOUR, 2);
		Calendar ca1=Calendar.getInstance();
		ca1.set(Calendar.HOUR, 0);
		ca1.set(Calendar.MINUTE, 0);
		ca1.set(Calendar.SECOND, 0);
		if(ca.getTime().getTime()>=date1.getTime()&&ca.getTime().getTime()>ca1.getTime().getTime()){
			sign=true;
		}
			}
		} catch (ParseException e) {
		}
		
		return sign;
		
	}
	public  static Map<String,String> queryBetweenTime(String begin,String end,Integer hours,Map<String,String> map){
		
		 try {
		if(StringUtils.isNotBlank(begin)){
			Date date= sdFull.parse(begin);//开始时间
			String[] dateone=begin.split(" ");
			String[] dateArr=dateone[0].split("-");
			String[] dateArr1=dateone[1].split(":");
			Date date1= sdFull.parse(end);//结束时间
			if(date.getTime()>=date1.getTime()){//如果开始时间大于结束时间  结束
				return map;
			}else{
				ca.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1])-1, Integer.parseInt(dateArr[2]),Integer.parseInt(dateArr1[0]),Integer.parseInt(dateArr1[1]),Integer.parseInt(dateArr1[2]));
				if(hours==null){//小时为空 按天查
					String key=sdFull.format(ca.getTime());
					ca.add(Calendar.DATE, 1);//开始时间加1天
					begin=sdFull.format(ca.getTime());
					map.put(key,begin);
				}else{//按小时查
					String key=sdFull.format(ca.getTime());
					ca.add(Calendar.HOUR, hours);
					begin=sdFull.format(ca.getTime());
					map.put(key,begin);
				}
				return queryBetweenTime(begin,end,hours,map);
			}
		}
		} catch (ParseException e) {
		}//开始时间
		return map;
	}

	@Override
	public void initSurgeryToDBDay(String startTime, String endTime) throws Exception {
		boolean sign= betweenDateSign(startTime, endTime);
		Map<String,String> map;
		Integer hours=null;
		if(!sign){
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}else{
			hours=1;
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}
		for(String dateArr:map.keySet()){
			startTime=dateArr;//查询的时间段
			endTime=map.get(dateArr);//查询的时间段
			List<String> tnL=ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OPERATION_APPLY",startTime,endTime);
			if(tnL==null){
				continue;
			}
			monthlyDashboardDao.initSurgeryToDBDay(tnL, startTime, endTime, hours);
		}
		
	}

	@Override
	public void initTreatmentToDBDay(String startTime, String endTime) throws Exception {
		boolean sign= betweenDateSign(startTime, endTime);
		Map<String,String> map;
		Integer hours=null;
		if(!sign){
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}else{
			hours=1;
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}
		String[] tables={"T_INPATIENT_INFO","T_INPATIENT_INFO_NOW"};
		//循环时间段
		for(String dateArr:map.keySet()){
			startTime=dateArr;//查询的时间段
			endTime=map.get(dateArr);//查询的时间段
			List<String> tnL=returnPartitions(startTime,endTime,tables);
			if(tnL==null){
				continue;
			}
			monthlyDashboardDao.initTreatmentToDBDay(tnL, startTime, endTime, hours);
		}
	}

	@Override
	public void initWardApplyToDBDay(String startTime, String endTime) throws Exception {
		boolean sign= betweenDateSign(startTime, endTime);
		Map<String,String> map;
		Integer hours=null;
		if(!sign){
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}else{
			hours=1;
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}
		String[] tables={"T_INPATIENT_INFO","T_INPATIENT_INFO_NOW"};
		//循环时间段
		for(String dateArr:map.keySet()){
			startTime=dateArr;//查询的时间段
			endTime=map.get(dateArr);//查询的时间段
			List<String> tnL=returnPartitions(startTime,endTime,tables);
			if(tnL==null){
				continue;
			}
			monthlyDashboardDao.initWardApplyToDBDay(tnL, startTime, endTime, hours);
		}
		
	}

	@Override
	public void InitInpatientInfoToDBOneDay(String startTime, String endTime) throws Exception {
		boolean sign= betweenDateSign(startTime, endTime);
		Map<String,String> map;
		Integer hours=null;
		if(!sign){
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}else{
			hours=1;
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}
		String[] tables={"T_INPATIENT_INFO","T_INPATIENT_INFO_NOW"};
		//循环时间段
		for(String dateArr:map.keySet()){
			startTime=dateArr;//查询的时间段
			endTime=map.get(dateArr);//查询的时间段
			List<String> tnL=returnPartitions(startTime,endTime,tables);
			if(tnL==null){
				continue;
			}
			monthlyDashboardDao.InitInpatientInfoToDBOneDay(tnL, startTime, endTime, hours);
		}
		
	}

	@Override
	public void initMonth(String begin, String end) throws Exception {
		monthlyDashboardDao.initHospExpensesToDBDay(begin, end);
		
	}

	@Override
	public void initYear(String begin, String end) throws Exception {
		monthlyDashboardDao.initHospExpensesYear(begin, end);
		
	}

	@Override
	public void init_MYZHYBP(String begin, String end, Integer type) {
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
			String menuAlias="MYZHYBP";
			if(1==type){//日数据 dateformate:yyyy-MM-dd
				innerMonthLyDao.init_MYZHYBP_HospExpenses(menuAlias, "HIS", begin);
			}else if(2==type){//月数据 dateformate:yyyy-MM 
				Date beginDate=DateUtils.parseDateY_M(begin);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				Date endDate=DateUtils.parseDateY_M(end);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY_M(ca.getTime());
					innerMonthLyDao.init_MYZHYBP_HospExpenses_MoreDay(menuAlias, "2", begin);
					innerMonthLyDao.init_MYZHYBP_Inpatient(menuAlias, null, begin);
					innerMonthLyDao.init_MYZHYBP_Surgery(menuAlias, null, begin);
					innerMonthLyDao.init_MYZHYBP_Treatment(menuAlias, null, begin);
					innerMonthLyDao.init_MYZHYBP_WardApply(menuAlias, null, begin);
					ca.add(Calendar.MONTH, 1);//下一月
				}
			}else if(3==type){//年数据dateformate:yyyy
				Date beginDate=DateUtils.parseDateY(begin);
				Date endDate=DateUtils.parseDateY(end);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY(ca.getTime());
					innerMonthLyDao.init_MYZHYBP_HospExpenses_MoreDay(menuAlias, "3", begin);
					ca.add(Calendar.YEAR, 1);
				}
			}
		}
		
	}

}
