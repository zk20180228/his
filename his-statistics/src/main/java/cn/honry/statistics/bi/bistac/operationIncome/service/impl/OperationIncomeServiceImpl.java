package cn.honry.statistics.bi.bistac.operationIncome.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inner.statistics.operationIncome.dao.InnerOperationIncomeDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.operationIncome.dao.OperationIncomeDao;
import cn.honry.statistics.bi.bistac.operationIncome.service.OperationIncomeService;
import cn.honry.statistics.bi.bistac.operationIncome.vo.OperationIncomeVo;
import cn.honry.statistics.bi.bistac.operationNum.vo.OperationNumsVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Service("operationIncomeService")
@Transactional
@SuppressWarnings({ "all" })
public class OperationIncomeServiceImpl implements OperationIncomeService {

	@Autowired
	@Qualifier(value = "operationIncomeDao")
	private OperationIncomeDao operationIncomeDao;
	
	@Autowired
	@Qualifier(value = "innerOperationIncomeDao")
	private InnerOperationIncomeDao innerOperationIncomeDao;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;

	/**  
	 * 
	 * 获得总手术收入
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void getIncomeSum(String dateType, String date) {
	}

	/**  
	 * 
	 * 初始化手术收入统计（门诊+住院）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void initOperationIncome(String startDate,String endDate) {
		boolean times=timeDifference(startDate,endDate);
		Map<String,String> dateTime;
		if(!times){
			dateTime=queryBetweenTime(startDate,endDate,0,new HashMap<String, String>());
		}else{
			dateTime=queryBetweenTime(startDate,endDate,1,new HashMap<String, String>());
		}
		for(String begin:dateTime.keySet()){
			List<String> itemtnL = null;
			List<String> feetnL = null;
			try{
				//1.转换查询时间
				Date sTime = DateUtils.parseDateY_M_D(begin);
				Date eTime = DateUtils.parseDateY_M_D(dateTime.get(begin));
				//2.获取住院数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
				if(dateNum.equals("1")){
					dateNum="30";
				}
				//3.获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				//4.获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
				itemtnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						itemtnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",begin,dateTime.get(begin));
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
						//获取相差年分的分区集合，默认加1
						itemtnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
						itemtnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					itemtnL.add("T_INPATIENT_ITEMLIST_NOW");
				}
			}catch(Exception e){
				itemtnL = new ArrayList<String>();
			}
			
			try{
				//1.转换查询时间
				Date sTime = DateUtils.parseDateY_M_D(begin);
				Date eTime = DateUtils.parseDateY_M_D(dateTime.get(begin));
				//2.获取住院数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
				//3.获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				//4.获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
				feetnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						feetnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",begin,dateTime.get(begin));
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
						//获取相差年分的分区集合，默认加1
						feetnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
						feetnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					feetnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}catch(Exception e){
				feetnL =new ArrayList<String>();
			}
			if(!times){
				operationIncomeDao.saveOperationIncomeToDB(begin,dateTime.get(begin),itemtnL, feetnL,0);
			}else{
				operationIncomeDao.saveOperationIncomeToDB(begin,dateTime.get(begin),itemtnL, feetnL,1);
			}
			
		}
	}

	/**  
	 * 
	 * 初始化手术收入统计（手术类别）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void initOperationIncomeOpType(String startDate,String endDate) {
		operationIncomeDao.saveOperOpTypeToDB();
		boolean times=timeDifference(startDate,endDate);
		Map<String,String> dateTime;
		if(!times){
			dateTime=queryBetweenTime(startDate,endDate,0,new HashMap<String, String>());
		}else{
			dateTime=queryBetweenTime(startDate,endDate,1,new HashMap<String, String>());
		}
		for(String begin:dateTime.keySet()){
			List<String> itemtnL = null;
			List<String> feetnL = null;
			try{
				//1.转换查询时间
				Date sTime = DateUtils.parseDateY_M_D(begin);
				Date eTime = DateUtils.parseDateY_M_D(dateTime.get(begin));
				//2.获取住院数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
				if(dateNum.equals("1")){
					dateNum="30";
				}
				//3.获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				//4.获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
				itemtnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						itemtnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",begin,dateTime.get(begin));
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
						//获取相差年分的分区集合，默认加1
						itemtnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
						itemtnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					itemtnL.add("T_INPATIENT_ITEMLIST_NOW");
				}
			}catch(Exception e){
				itemtnL = new ArrayList<String>();
			}
			
			try{
				//1.转换查询时间
				Date sTime = DateUtils.parseDateY_M_D(begin);
				Date eTime = DateUtils.parseDateY_M_D(dateTime.get(begin));
				//2.获取住院数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
				//3.获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				//4.获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
				feetnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						feetnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",begin,dateTime.get(begin));
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
						//获取相差年分的分区集合，默认加1
						feetnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
						feetnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					feetnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}catch(Exception e){
				feetnL =new ArrayList<String>();
			}
			if(!times){
				operationIncomeDao.saveOperationOpTypeToDB(begin,dateTime.get(begin),itemtnL, feetnL,0);
			}else{
				operationIncomeDao.saveOperationOpTypeToDB(begin,dateTime.get(begin),itemtnL, feetnL,1);
			}
		}
		
	}

	/**  
	 * 
	 * 初始化手术收入统计（科室前5）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void initOperationDeptTopFive(String startDate,String endDate) {
		boolean times=timeDifference(startDate,endDate);
		Map<String,String> dateTime;
		if(!times){
			dateTime=queryBetweenTime(startDate,endDate,0,new HashMap<String, String>());
		}else{
			dateTime=queryBetweenTime(startDate,endDate,1,new HashMap<String, String>());
		}
		for(String begin:dateTime.keySet()){
			List<String> itemtnL = null;
			List<String> feetnL = null;
			try{
				//1.转换查询时间
				Date sTime = DateUtils.parseDateY_M_D(begin);
				Date eTime = DateUtils.parseDateY_M_D(dateTime.get(begin));
				//2.获取住院数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
				if(dateNum.equals("1")){
					dateNum="30";
				}
				//3.获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				//4.获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
				itemtnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						itemtnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",begin,dateTime.get(begin));
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
						//获取相差年分的分区集合，默认加1
						itemtnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
						itemtnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					itemtnL.add("T_INPATIENT_ITEMLIST_NOW");
				}
			}catch(Exception e){
				itemtnL = new ArrayList<String>();
			}
			
			try{
				//1.转换查询时间
				Date sTime = DateUtils.parseDateY_M_D(begin);
				Date eTime = DateUtils.parseDateY_M_D(dateTime.get(begin));
				//2.获取住院数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
				//3.获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				//4.获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
				feetnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						feetnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",begin,dateTime.get(begin));
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
						//获取相差年分的分区集合，默认加1
						feetnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
						feetnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					feetnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}catch(Exception e){
				feetnL =new ArrayList<String>();
			}
			if(!times){
				operationIncomeDao.saveOperTopFiveDeptToDB(begin,dateTime.get(begin),itemtnL, feetnL,0);
			}else{
				operationIncomeDao.saveOperTopFiveDeptToDB(begin,dateTime.get(begin),itemtnL, feetnL,1);
			}
		}
		
	}

	/**  
	 * 
	 * 初始化手术收入统计（医生前5）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void initOperationDocTopFive(String startDate,String endDate) {
		boolean times=timeDifference(startDate,endDate);
		Map<String,String> dateTime;
		if(!times){
			dateTime=queryBetweenTime(startDate,endDate,0,new HashMap<String, String>());
		}else{
			dateTime=queryBetweenTime(startDate,endDate,1,new HashMap<String, String>());
		}
		for(String begin:dateTime.keySet()){
			List<String> itemtnL = null;
			List<String> feetnL = null;
			try{
				//1.转换查询时间
				Date sTime = DateUtils.parseDateY_M_D(begin);
				Date eTime = DateUtils.parseDateY_M_D(dateTime.get(begin));
				//2.获取住院数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
				if(dateNum.equals("1")){
					dateNum="30";
				}
				//3.获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				//4.获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
				itemtnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						itemtnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",begin,dateTime.get(begin));
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
						//获取相差年分的分区集合，默认加1
						itemtnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
						itemtnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					itemtnL.add("T_INPATIENT_ITEMLIST_NOW");
				}
			}catch(Exception e){
				itemtnL = new ArrayList<String>();
			}
			
			try{
				//1.转换查询时间
				Date sTime = DateUtils.parseDateY_M_D(begin);
				Date eTime = DateUtils.parseDateY_M_D(dateTime.get(begin));
				//2.获取住院数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
				//3.获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				//4.获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
				feetnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						feetnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",begin,dateTime.get(begin));
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
						//获取相差年分的分区集合，默认加1
						feetnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
						feetnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					feetnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}catch(Exception e){
				feetnL =new ArrayList<String>();
			}
			if(!times){
				operationIncomeDao.saveOperTopFiveDocToDB(begin,dateTime.get(begin),itemtnL, feetnL,0);
			}else{
				operationIncomeDao.saveOperTopFiveDocToDB(begin,dateTime.get(begin),itemtnL, feetnL,1);
			}
		}
	}

	/**  
	 * 
	 * 初始化同环比
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void saveOperYoyRatioToDB(String startDate,String endDate) {
		boolean times=timeDifference(startDate,endDate);
		Map<String,String> dateTime;
		if(!times){
			dateTime=queryBetweenTime(startDate,endDate,0,new HashMap<String, String>());
		}else{
			dateTime=queryBetweenTime(startDate,endDate,1,new HashMap<String, String>());
		}
		for(String begin:dateTime.keySet()){
			List<String> itemtnL = null;
			List<String> feetnL = null;
			try{
				//1.转换查询时间
				Date sTime = DateUtils.parseDateY_M_D(begin);
				Date eTime = DateUtils.parseDateY_M_D(dateTime.get(begin));
				//2.获取住院数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
				if(dateNum.equals("1")){
					dateNum="30";
				}
				//3.获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				//4.获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
				itemtnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						itemtnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",begin,dateTime.get(begin));
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
						//获取相差年分的分区集合，默认加1
						itemtnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
						itemtnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					itemtnL.add("T_INPATIENT_ITEMLIST_NOW");
				}
			}catch(Exception e){
				itemtnL = new ArrayList<String>();
			}
			
			try{
				//1.转换查询时间
				Date sTime = DateUtils.parseDateY_M_D(begin);
				Date eTime = DateUtils.parseDateY_M_D(dateTime.get(begin));
				//2.获取住院数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
				//3.获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				//4.获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
				feetnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						feetnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",begin,dateTime.get(begin));
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
						//获取相差年分的分区集合，默认加1
						feetnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
						feetnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					feetnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}catch(Exception e){
				feetnL =new ArrayList<String>();
			}
			if(!times){
				operationIncomeDao.saveOperYoyRatioToDB(begin,dateTime.get(begin),itemtnL, feetnL,0);
			}else{
				operationIncomeDao.saveOperYoyRatioToDB(begin,dateTime.get(begin),itemtnL, feetnL,1);
			}
		}
	}
	
	
	public  Map<String,String> queryBetweenTime(String begin,String end,int flg,Map<String,String> map){
		SimpleDateFormat sdFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar ca=Calendar.getInstance();
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

	@Override
	public void initOperationIncomeMonth(String startDate, String endDate) {
		operationIncomeDao.initOperationIncomeMonth(startDate,endDate);
	}

	@Override
	public void initOperationIncomeYear(String startDate, String endDate) {
		operationIncomeDao.initOperationIncomeYear(startDate,endDate);
	}

	@Override
	public void initOperIncomeTypeMonth(String startDate, String endDate) {
		operationIncomeDao.initOperIncomeTypeMonth(startDate,endDate);
	}

	@Override
	public void initOperIncomeTypeYear(String startDate, String endDate) {
		operationIncomeDao.initOperIncomeTypeYear(startDate,endDate);
	}

	@Override
	public void initOperTopFiveDeptMonth(String startDate, String endDate) {
		operationIncomeDao.initOperTopFiveDeptMonth(startDate,endDate);
		
	}

	@Override
	public void initOperTopFiveDeptYear(String startDate, String endDate) {
		operationIncomeDao.initOperTopFiveDeptYear(startDate,endDate);
		
	}

	@Override
	public void initOperTopFiveDocMonth(String startDate, String endDate) {
		operationIncomeDao.initOperTopFiveDocMonth(startDate,endDate);
		
	}

	@Override
	public void initOperTopFiveDocYear(String startDate, String endDate) {
		operationIncomeDao.initOperTopFiveDocYear(startDate,endDate);
	}

	@Override
	public void initOperIncomYoyRatioMonth(String startDate, String endDate) {
		operationIncomeDao.initOperIncomYoyRatioMonth(startDate,endDate);
		
	}

	@Override
	public void initOperIncomYoyRatioYear(String startDate, String endDate) {
		operationIncomeDao.initOperIncomYoyRatioYear(startDate,endDate);
		
	}

	/**  
	 * 
	 * 手术收入统计（门诊住院）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param response
	 * @param searchTime yyyy-MM-dd月份加为两位日为两位 
	 * @param dateSign 1查询年收入表 2查询月收入 
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public List<OperationIncomeVo> queryOperationNums(String searchTime,
			String dateSign) {
		//从mongodb查询门诊住院手术收入统计
		List<OperationIncomeVo> list= operationIncomeDao.queryOperationNums(searchTime,dateSign);
//		//没有查到则从数据库查询
//		if(list.size()==0){
//			//将传人时间拆分成时间map
//			Map<String,String> map=getTimes(searchTime,dateSign);
//			String startDate=map.get("start");
//			String endDate =map.get("end");
//			List<String> itemList=getItemZoneName(startDate,endDate);
//			List<String> feeList=getFeeDetialZoneName(startDate,endDate);
//			//从数据库查询数据权限
//			list= operationIncomeDao.queryOperationNumsToDB(itemList,feeList,startDate,endDate,dateSign);
//		}
		if(list.size()>0){
			List<String> list1=new ArrayList<String>();
			list1.add("门诊");
			list1.add("住院");
			for(int i=0;i<list.size();i++){
				if(list1.contains(list.get(i).getName())){
					list1.remove(list.get(i).getName());
				}
			}
			for(int i=0;i<list1.size();i++){
				OperationIncomeVo vo=new OperationIncomeVo();
				vo.setName(list1.get(i));
				vo.setTotalAmount("0");
				list.add(vo);
			}
		}
		return list;
	}
	
	/**
	 * 查询手术收入统计（手术类别）
	 * @author zhuxiaolu 
	 * @param searchTime 时间
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @author zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return  List<OperationIncomeVo>
	 */
	@Override
	public List<OperationIncomeVo> queryOperationOpType(String searchTime,String dateSign) {
		List<OperationIncomeVo> list= operationIncomeDao.queryOperationOpType(searchTime,dateSign);
//		if(list.size()==0){
//			Map<String,String> map=getTimes(searchTime,dateSign);
//			String startDate=map.get("start");
//			String endDate =map.get("end");
//			List<String> itemList=getItemZoneName(startDate,endDate);
//			List<String> feeList=getFeeDetialZoneName(startDate,endDate);
//			list= operationIncomeDao.queryOperationOpTypeToDB(itemList,feeList,startDate,endDate,dateSign);
//		}
		return list;
	}
	

	/**
	 * 查询手术收入统计（科室TOP5）mongodb
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @author zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return  List<OperationIncomeVo>
	 */
	@Override
	public List<OperationIncomeVo> queryOperationTOPFiveDept(String searchTime,
			String dateSign) {
		List<OperationIncomeVo> list= operationIncomeDao.queryOperationTOPFiveDept(searchTime,dateSign);
//		if(list.size()==0){
//			Map<String,String> map=getTimes(searchTime,dateSign);
//			String startDate=map.get("start");
//			String endDate =map.get("end");
//			List<String> itemList=getItemZoneName(startDate,endDate);
//			List<String> feeList=getFeeDetialZoneName(startDate,endDate);
//			list= operationIncomeDao.queryTOPFiveDeptToDB(itemList,feeList,startDate,endDate,dateSign);
//		}
		return list;
	}

	/**
	 * 查询手术收入统计（医生TOP5）mongodb
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @author zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return  List<OperationIncomeVo>
	 */
	@Override
	public List<OperationIncomeVo> queryOperationTOPFiveDoc(String searchTime,
			String dateSign) {
		List<OperationIncomeVo> list= operationIncomeDao.queryOperationTOPFiveDoc(searchTime,dateSign);
//		if(list.size()==0){
//			Map<String,String> map=getTimes(searchTime,dateSign);
//			String startDate=map.get("start");
//			String endDate =map.get("end");
//			List<String> itemList=getItemZoneName(startDate,endDate);
//			List<String> feeList=getFeeDetialZoneName(startDate,endDate);
//			list= operationIncomeDao.queryTOPFiveDocDB(itemList,feeList,startDate,endDate,dateSign);
//		}
		return list;
	}
	
	/**  
	 * 
	 * 非药品明细分区
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午5:52:52 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午5:52:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startDate 开始时间
	 * @param:endDate 结束时间
	 * @throws:
	 * @return: 
	 *
	 */
	public List<String> getItemZoneName(String startDate,String endDate) {
		List<String> itemtnL = null;
		try{
			//1.转换查询时间
			Date sTime = DateUtils.parseDateY_M_D(startDate);
			Date eTime = DateUtils.parseDateY_M_D(endDate);
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if(dateNum.equals("1")){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			itemtnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					itemtnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",startDate,endDate);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startDate);
					//获取相差年分的分区集合，默认加1
					itemtnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					itemtnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				itemtnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		}catch(Exception e){
			itemtnL = new ArrayList<String>();
		}
		return itemtnL;
	}
	
	/**  
	 * 
	 * 费用明细分区
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午5:52:52 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午5:52:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startDate 开始时间
	 * @param:endDate 结束时间
	 * @throws:
	 * @return: 
	 *
	 */
	public List<String> getFeeDetialZoneName(String startDate,String endDate) {
		List<String> feetnL=null;
		try{
			//1.转换查询时间
			Date sTime = DateUtils.parseDateY_M_D(startDate);
			Date eTime = DateUtils.parseDateY_M_D(endDate);
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			feetnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					feetnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",startDate,endDate);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startDate);
					//获取相差年分的分区集合，默认加1
					feetnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
					feetnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				feetnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
			}
		}catch(Exception e){
			feetnL =new ArrayList<String>();
		}
		return feetnL;
	}
	

	/**
	 * 查询手术收入统计（同比）mongodb
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @author zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return  List<OperationIncomeVo>
	 */
	@Override
	public List<OperationIncomeVo> queryYoyCount(String searchTime,
			String dateSign) {
		List<OperationIncomeVo> list=  operationIncomeDao.queryYoyCount(searchTime,dateSign);
		if(list!=null&&list.size()==0){
			Map<String,String> timeMap=this.queryBetweenTime(searchTime, dateSign,"TB");//6月内时间数组
			for(String begin:timeMap.keySet()){
				List<String> itemList=getItemZoneName(begin,timeMap.get(begin));
				List<String> feeList=getFeeDetialZoneName(begin,timeMap.get(begin));
				OperationIncomeVo vo=operationIncomeDao.queryYoyCountToDB(itemList,feeList,begin,timeMap.get(begin),dateSign);
				list.add(vo);
			}
			
		}
		Collections.sort(list, new Comparator(){  
	        @Override  
	        public int compare(Object o1, Object o2) {  
	        	OperationIncomeVo stu1=(OperationIncomeVo)o1;  
	        	OperationIncomeVo stu2=(OperationIncomeVo)o2;  
	            return stu1.getName().compareTo(stu2.getName());  
	        }             
	    }); 
		return list;
	}

	/**
	 * 查询手术收入统计（环比）
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @author zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return  List<OperationIncomeVo>
	 */
	@Override
	public List<OperationIncomeVo> queryRatioCount(String searchTime,
			String dateSign) {
		//从mongodb 获取环比数据
		List<OperationIncomeVo> list=  operationIncomeDao.queryRatioCount(searchTime,dateSign);
		if(list!=null&&list.size()==0){
			Map<String,String> timeMap=this.queryBetweenTime(searchTime, dateSign,"HB");//6月内时间数组
			for(String begin:timeMap.keySet()){
				List<String> itemList=getItemZoneName(begin,timeMap.get(begin));
				List<String> feeList=getFeeDetialZoneName(begin,timeMap.get(begin));
				//从数据库 获取环比数据
				OperationIncomeVo vo=operationIncomeDao.queryYoyCountToDB(itemList,feeList,begin,timeMap.get(begin),dateSign);
				list.add(vo);
			}
			
		}
		Collections.sort(list, new Comparator(){  
	        @Override  
	        public int compare(Object o1, Object o2) {  
	        	OperationIncomeVo stu1=(OperationIncomeVo)o1;  
	        	OperationIncomeVo stu2=(OperationIncomeVo)o2;  
	            return stu1.getName().compareTo(stu2.getName());  
	        }             
	    }); 
		return list;
	}

	/**
	 * 根据传入时间获取他的同比、环比时间（l六个）
	 * @param begin yyyy  yyyy-MM yyyy-MM-dd
	 * @param dateSign 1 2 3
	 * @param type TB HB TB没有年同比
	 * @return
	 */
	public  static Map<String,String> queryBetweenTime(String begin,String dateSign,String type){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
		String key;
		if("TB".equals(type)){
			if("2".equals(dateSign)){
				String[] date=begin.split("-");
				Calendar ca=Calendar.getInstance();
				ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1])-1,1,0,0,0);
				for(int i=0;i<6;i++){
					key=sdf.format(ca.getTime());
					ca.add(Calendar.MONTH, 1);
					map.put(key, sdf.format(ca.getTime()));
					ca.add(Calendar.MONTH, -1);
					ca.add(Calendar.YEAR, -1);
				}
			}else if("3".equals(dateSign)){
				String[] date=begin.split("-");
				Calendar ca=Calendar.getInstance();
				ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]), 0, 0, 0);
				for(int i=0;i<6;i++){
					key=sdf.format(ca.getTime());
					ca.add(Calendar.DATE, 1);
					map.put(key, sdf.format(ca.getTime()));
					ca.add(Calendar.DATE, -1);
					ca.add(Calendar.YEAR, -1);
				}
			}
			return map;
		}else if("HB".equals(type)){
			if("1".equals(dateSign)){
				String[] date=begin.split("-");
				Calendar ca=Calendar.getInstance();
				ca.set(Integer.parseInt(date[0]),0,1,0,0,0);
				for(int i=0;i<6;i++){
					key=sdf.format(ca.getTime());
					ca.add(Calendar.YEAR, 1);
					map.put(key, sdf.format(ca.getTime()));
					ca.add(Calendar.YEAR, -2);
				}
			}else if("2".equals(dateSign)){
				String[] date=begin.split("-");
				Calendar ca=Calendar.getInstance();
				ca.set(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,1,0,0,0);
				for(int i=0;i<6;i++){
					key=sdf.format(ca.getTime());
					ca.add(Calendar.MONTH, 1);
					ca.add(Calendar.DATE, 0);
					map.put(key, sdf.format(ca.getTime()));
					ca.add(Calendar.MONTH, -2);
				} 
			}else if("3".equals(dateSign)){
				String[] date=begin.split("-");
				Calendar ca=Calendar.getInstance();
				ca.set(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,Integer.parseInt(date[2]),0,0,0);
				for(int i=0;i<6;i++){
					key=sdf.format(ca.getTime());
					ca.add(Calendar.DATE, 1);
					map.put(key, sdf.format(ca.getTime()));
					ca.add(Calendar.DATE, -2);
				}
			}
			return map;
		}
		return new HashMap<String,String>();
	}
	
	/**  
	 * 
	 * 将传入某一个时间差分成时间map，存放开始结束时间
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午5:13:14 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午5:13:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public Map<String,String> getTimes(String searchTime,String dateSign) {
		Map<String,String> map=new HashMap<String, String>();
		String begin="";
		String end="";
		if("1".equals(dateSign)){
			begin=searchTime+"-01-01 00:00:00";
			if(DateUtils.formatDateY(new Date()).equals(searchTime)){
				end=DateUtils.formatDateY_M_D_H_M_S(new Date());
			}else{
				end=searchTime+"-12-31 23:59:59";
			}
		}else if("2".equals(dateSign)){
			begin=searchTime+"-01";
			if(DateUtils.formatDateY_M(new Date()).equals(searchTime)){
				end=DateUtils.formatDateY_M_D_H_M_S(new Date());
			}else{
				SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
				try {
					Date date = sdf.parse(begin);
					Calendar calendar = Calendar.getInstance();
			        calendar.setTime(date);
			        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
			        end=DateUtils.formatDateY_M_D(calendar.getTime());
				} catch (ParseException e) {
				}
			}
			begin=searchTime+"-01 00:00:00";
		}else{
			begin=searchTime+" 00:00:00";
			if(DateUtils.formatDateY_M_D(new Date()).equals(searchTime)){
				end=DateUtils.formatDateY_M_D_H_M_S(new Date());
			}else{
				end=searchTime+" 23:59:59";
			}
		}
		map.put("start", begin);
		map.put("end", end);
		return map;
	}

	@Override
	public void initDate(String beginDate, String endDate, Integer type) {
		if(StringUtils.isNotBlank(beginDate)&&StringUtils.isNotBlank(endDate)&&type!=null){
			String menuAlias="SSSRTJ";
			if(1==type){//日数据 dateformate:yyyy-MM-dd
				String his="HIS";
				innerOperationIncomeDao.initOperIncomeMZ(menuAlias, his, beginDate);
				innerOperationIncomeDao.initOperIncomeType(menuAlias, his, beginDate);
				innerOperationIncomeDao.initOperIncomeDept(menuAlias, his, beginDate);
				innerOperationIncomeDao.initOperIncomeDoc(menuAlias, his, beginDate);
				innerOperationIncomeDao.initOperIncomeYoyRatio(menuAlias, his, beginDate);
			}else if(2==type){//月数据 dateformate:yyyy-MM 
				Date begin=DateUtils.parseDateY_M(beginDate);
				Calendar ca=Calendar.getInstance();
				ca.setTime(begin);
				Date end=DateUtils.parseDateY_M(endDate);
				while(DateUtils.compareDate(ca.getTime(), end)<=0){
					beginDate=DateUtils.formatDateY_M(ca.getTime());
					innerOperationIncomeDao.initOperIncomeMZYearOrMonth(menuAlias, "2", beginDate);
					innerOperationIncomeDao.initOperIncomeTypeYearOrMonth(menuAlias, "2", beginDate);
					innerOperationIncomeDao.initOperIncomeDeptYearOrMonth(menuAlias, "2", beginDate);
					innerOperationIncomeDao.initOperIncomeDocYearOrMonth(menuAlias, "2", beginDate);
					innerOperationIncomeDao.initOperIncomeYoyRatioYearOrMonth(menuAlias, "2", beginDate);
					ca.add(Calendar.MONTH, 1);//下一月
				}
			}else if(3==type){//年数据dateformate:yyyy
				Date begin=DateUtils.parseDateY(beginDate);
				Date end=DateUtils.parseDateY(endDate);
				Calendar ca=Calendar.getInstance();
				ca.setTime(begin);
				while(DateUtils.compareDate(ca.getTime(), end)<=0){
					beginDate=DateUtils.formatDateY(ca.getTime());
					innerOperationIncomeDao.initOperIncomeMZYearOrMonth(menuAlias, "3", beginDate);
					innerOperationIncomeDao.initOperIncomeTypeYearOrMonth(menuAlias, "3", beginDate);
					innerOperationIncomeDao.initOperIncomeDeptYearOrMonth(menuAlias, "3", beginDate);
					innerOperationIncomeDao.initOperIncomeDocYearOrMonth(menuAlias, "3", beginDate);
					innerOperationIncomeDao.initOperIncomeYoyRatioYearOrMonth(menuAlias, "3", beginDate);
					ca.add(Calendar.YEAR, 1);
				}
			}
		}
		
	}
}
