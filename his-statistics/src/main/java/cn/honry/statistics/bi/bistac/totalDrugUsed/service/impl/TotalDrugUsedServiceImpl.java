package cn.honry.statistics.bi.bistac.totalDrugUsed.service.impl;

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

import cn.honry.inner.statistics.totalDrugIncome.dao.InnerTotalDrugUsedDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.totalDrugUsed.dao.TotalDrugUsedDao;
import cn.honry.statistics.bi.bistac.totalDrugUsed.service.TotalDrugUsedService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
@Service("totalDrugUsedService")
public class TotalDrugUsedServiceImpl implements TotalDrugUsedService{
	static SimpleDateFormat sdFull=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static Calendar ca=Calendar.getInstance();
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier("totalDrugUsedDao")
	private TotalDrugUsedDao totalDrugUsedDao;
	
	public void setTotalDrugUsedDao(TotalDrugUsedDao totalDrugUsedDao) {
		this.totalDrugUsedDao = totalDrugUsedDao;
	}
	@Autowired
	@Qualifier(value="innerTotalDrugUsedDao")
	private InnerTotalDrugUsedDao innerTotalDrugUsedDao;
	public void setInnerTotalDrugUsedDao(InnerTotalDrugUsedDao innerTotalDrugUsedDao) {
		this.innerTotalDrugUsedDao = innerTotalDrugUsedDao;
	}

	@Override
	public boolean initDrugDB() {
		boolean sign =false;
			sign=totalDrugUsedDao.saveDrugTotalToDBYear();
		return sign;
	}

	@Override
	public void initDrugTopFiveDeptDB() {
		totalDrugUsedDao.saveDrugTotalTopFiveDeptToDBYear();
	}

	@Override
	public void initDrugTopFiveDocDB() {
		totalDrugUsedDao.saveDrugTotalTopFiveDocToDBYear();
	}

	@Override
	public void initGroupDrugByOneDay( String startTime,
			String endTime) {
		boolean sign= betweenDateSign(startTime, endTime);
		Map<String,String> map;
		Integer hours=null;
		if(!sign){
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}else{
			hours=1;
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}
		//循环时间段
		for(String dateArr:map.keySet()){
			startTime=dateArr;//查询的时间段
			endTime=map.get(dateArr);//查询的时间段
			List<String> tnL=pattentByTable(startTime,endTime);
			if(tnL==null){
				continue;
			}
			totalDrugUsedDao.initGroupDrugByOneDay(tnL, startTime, endTime, hours);
		}
	}

	@Override
	public void InitDeptDrugByOneDay( String startTime,
			String endTime) {
		boolean sign= betweenDateSign(startTime, endTime);
		Map<String,String> map;
		Integer hours=null;
		if(!sign){
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}else{
			hours=1;
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}
		//循环时间段
		for(String dateArr:map.keySet()){
			startTime=dateArr;//查询的时间段
			endTime=map.get(dateArr);//查询的时间段
			List<String> tnL=pattentByTable(startTime,endTime);
			if(tnL==null){
				continue;
			}
			totalDrugUsedDao.InitDeptDrugByOneDay(tnL, startTime, endTime, hours);
		}
		
	}

	@Override
	public void InitDocDrugByOneDay( String startTime, String endTime) {
		boolean sign= betweenDateSign(startTime, endTime);
		Map<String,String> map;
		Integer hours=null;
		if(!sign){
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}else{
			hours=1;
			map=queryBetweenTime(startTime, endTime, hours, new HashMap<String,String>());
		}
		//循环时间段
		for(String dateArr:map.keySet()){
			startTime=dateArr;//查询的时间段
			endTime=map.get(dateArr);//查询的时间段
			List<String> tnL=pattentByTable(startTime,endTime);
			if(tnL==null){
				continue;
			}
			totalDrugUsedDao.InitDocDrugByOneDay(tnL, startTime, endTime, hours);
		}
		
	}
	/**
	 * 返回分区表
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<String> pattentByTable(String startTime, String endTime){
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_MEDICINELIST",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_MEDICINELIST",yNum+1);
					tnL.add(0,"T_INPATIENT_MEDICINELIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_MEDICINELIST_NOW");
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
	public void initMonth(String begin, String end) {
		totalDrugUsedDao.initGroupDrugByOneDay(begin, end);
		totalDrugUsedDao.InitDeptDrugByOneDay(begin, end);
		totalDrugUsedDao.InitDocDrugByOneDay(begin, end);
	}

	@Override 
	public void initYear(String begin, String end) {
		totalDrugUsedDao.initGroupDrugYear(begin, end);
		totalDrugUsedDao.InitDeptDrugYear(begin, end);
		totalDrugUsedDao.InitDocDrugYear(begin, end);
		
	}

	@Override
	public void init_YPSRFXHZ(String begin, String end, Integer type) {
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
			String menuAlias="YPSRFXHZ";
			if(1==type){//日数据 dateformate:yyyy-MM-dd
				String his="HIS";
					innerTotalDrugUsedDao.init_YPSRFXHZ_Dept(menuAlias, his, begin);
					innerTotalDrugUsedDao.init_YPSRFXHZ_Doc(menuAlias, his, begin);
					innerTotalDrugUsedDao.init_YPSRFXHZ_Total(menuAlias, his, begin);
			}else if(2==type){//月数据 dateformate:yyyy-MM 
				Date beginDate=DateUtils.parseDateY_M(begin);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				Date endDate=DateUtils.parseDateY_M(end);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY_M(ca.getTime());
					innerTotalDrugUsedDao.init_YPSRFXHZ_Dept_MoreDay(menuAlias, "2", begin);
					innerTotalDrugUsedDao.init_YPSRFXHZ_Doc_MoreDay(menuAlias, "2", begin);
					innerTotalDrugUsedDao.init_YPSRFXHZ_Total_MoreDay(menuAlias, "2", begin);
					ca.add(Calendar.MONTH, 1);//下一月
				}
			}else if(3==type){//年数据dateformate:yyyy
				Date beginDate=DateUtils.parseDateY(begin);
				Date endDate=DateUtils.parseDateY(end);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY(ca.getTime());
					innerTotalDrugUsedDao.init_YPSRFXHZ_Dept_MoreDay(menuAlias, "3", begin);
					innerTotalDrugUsedDao.init_YPSRFXHZ_Doc_MoreDay(menuAlias, "3", begin);
					innerTotalDrugUsedDao.init_YPSRFXHZ_Total_MoreDay(menuAlias, "3", begin);
					ca.add(Calendar.YEAR, 1);
				}
			}
		}
		
		
	}
}
