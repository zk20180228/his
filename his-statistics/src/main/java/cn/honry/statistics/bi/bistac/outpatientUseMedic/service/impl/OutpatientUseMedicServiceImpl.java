package cn.honry.statistics.bi.bistac.outpatientUseMedic.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.monthlyDashboard.dao.InnerMonthLyDao;
import cn.honry.inner.statistics.outpatientUseMedic.dao.InitUseMedicDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.MonthlyDashboardVo;
import cn.honry.statistics.bi.bistac.outpatientStac.dao.OutpatientStacVoDao;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.dao.OutpatientUseMedicDao;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.service.OutpatientUseMedicService;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.vo.OutpatientUseMedicVo;
import cn.honry.statistics.bi.bistac.treatmentEffect.vo.TreatmentEffectVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Service("outpatientUseMedicService")
@Transactional
@SuppressWarnings({ "all" })
public class OutpatientUseMedicServiceImpl implements OutpatientUseMedicService{
	@Autowired
	@Qualifier(value = "outpatientUseMedicDao")
	private OutpatientUseMedicDao outpatientUseMedicDao;
	@Autowired
	@Qualifier(value="initUseMedicDao")
	private InitUseMedicDao initUseMedicDao;
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	//把数据存入mongodb中
	public static final String TABLENAME1 = "MZYYJK_YZB_MONTH";//药占比
	public static final String TABLENAME2 = "MZYYJK_YYTS_MONTH";//门诊用药天数
	public static final String TABLENAME3 = "MZYYJK_YSYYJE_MONTH";//医生用药金额表
	public static final String TABLENAME4 = "MZYYJK_KSYYJE_MONTH";//科室用药金额表
	public static final String TABLENAME6 = "MZYYJK_YPJE_MONTH";//门诊月药品金额，用药数量，人次表
	/**  
	 * 
	 * 门诊药品收入 和 门诊总收入
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月12日 下午5:33:37 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月12日 下午5:33:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public OutpatientUseMedicVo queryCost(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> tnL = null;
		String begin=null;
		String end=null;
		try {
			Date stime = format.parse(date);
			Calendar a=Calendar.getInstance();
		    a.setTime(stime); 
		    a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
		    a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
		    int MaxDate=a.get(Calendar.DATE);
		    begin=date+"-01 00:00:00";
			end=date+"-"+MaxDate+" 23:59:59";
			Date sTime = format2.parse(begin);
			Date eTime = format2.parse(end);
			
			//获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			//获得当前时间
			Date dTime = format2.parse(format2.format(new Date()));
			//获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
				if(DateUtils.compareDate(eTime, cTime)==-1){//当结束时间小于挂号主表中的最小时间时，只查询分区表
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",begin,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
					tnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return outpatientUseMedicDao.queryCost(tnL, begin, end);
	}
	/**  
	 * 
	 * 最近12月的人均药费
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月13日 上午9:57:27 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月13日 上午9:57:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<OutpatientUseMedicVo> queryStatisticsCost(String startTime, String endTime) {
		SimpleDateFormat format = null;
		List<String> tnL = null;
		String begin=null;
		String end=null;
		try {
			format = new SimpleDateFormat("yyyy-MM");
			Date stime = format.parse(endTime);
			Calendar a=Calendar.getInstance();
		    a.setTime(stime); 
		    a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
		    a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
		    int MaxDate=a.get(Calendar.DATE);
		    begin=startTime+"-01";
			end=endTime+"-"+MaxDate;
			
			//转换查询时间
			Date sTime = DateUtils.parseDateY_M_D(begin);
			Date eTime = DateUtils.parseDateY_M_D(end);
			//获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			//获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",begin,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
					tnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
			}
			return outpatientUseMedicDao.queryStatisticsCost(tnL, begin, end);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**  
	 * 
	 * 医生用药前5名
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月13日 下午4:41:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月13日 下午4:41:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<OutpatientUseMedicVo> queryDoctCost(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		List<String> tnL = null;
		String begin=null;
		String end=null;
		try {
			Date stime = format.parse(date);
			Calendar a=Calendar.getInstance();
		    a.setTime(stime); 
		    a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
		    a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
		    int MaxDate=a.get(Calendar.DATE);
		    begin=date+"-01";
			end=date+"-"+MaxDate;
			Date sTime = format2.parse(begin);
			Date eTime = format2.parse(end);
			
			//获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			//获得当前时间
			Date dTime = format2.parse(format2.format(new Date()));
			//获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
			
			tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime,cTime)==-1){
				if(DateUtils.compareDate(eTime,cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",begin,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
					tnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return outpatientUseMedicDao.queryDoctCost(tnL, begin, end);
	}
	/**  
	 * 
	 * 科室用药前5名
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月13日 下午4:41:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月13日 下午4:41:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<OutpatientUseMedicVo> queryDeptCost(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		List<String> tnL = null;
		String begin=null;
		String end=null;
		try {
			Date stime = format.parse(date);
			Calendar a=Calendar.getInstance();
		    a.setTime(stime); 
		    a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
		    a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
		    int MaxDate=a.get(Calendar.DATE);
		    begin=date+"-01";
			end=date+"-"+MaxDate;
			Date sTime = format2.parse(begin);
			Date eTime = format2.parse(end);
			
			//获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			//获得当前时间
			Date dTime = format2.parse(format2.format(new Date()));
			//获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
			
			tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",begin,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
					tnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return outpatientUseMedicDao.queryDeptCost(tnL, begin, end);
	}
	/**  
	 * 
	 * 最近12月的人均要用药天数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月13日 上午9:57:27 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月13日 上午9:57:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<OutpatientUseMedicVo> queryMedicationDays(String startTime, String endTime) {
		SimpleDateFormat format = null;
		SimpleDateFormat format2 = null;
		List<String> tnL = null;
		String begin=null;
		String end=null; 
		try {
			format = new SimpleDateFormat("yyyy-MM");
			format2 = new SimpleDateFormat("yyyy-MM-dd");
			
			Date stime = format.parse(endTime);
			Calendar a=Calendar.getInstance();
		    a.setTime(stime); 
		    a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
		    a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
		    int MaxDate=a.get(Calendar.DATE);
		    begin=startTime+"-01";
			end=endTime+"-"+MaxDate;
			//转换查询时间
			Date sTime = format2.parse(begin);
			Date eTime = format2.parse(end);
			//获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			//获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
			
			tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",begin,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(format2.format(cTime),begin);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
					tnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
			}
			return outpatientUseMedicDao.queryMedicationDays(tnL, begin, end);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/*******************************************************mongodb*************************************************/
	/**  
	 * 
	 * 从mongodb中查询门诊药品收入 和 门诊总收入
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月11日 下午3:03:15 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月11日 下午3:03:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public OutpatientUseMedicVo queryCostByMongo(String date) {
		List<OutpatientUseMedicVo> vos=new ArrayList<OutpatientUseMedicVo>();
		OutpatientUseMedicVo vo=null;
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("selectTime", date);
		DBCursor cursor = new MongoBasicDao().findAlldata(TABLENAME1, bdObject);
		while(cursor.hasNext()){
			vo = new OutpatientUseMedicVo();
			DBObject dbCursor = cursor.next();
			Double  drugFee =(Double) dbCursor.get("drugFee");
			Double totCost = (Double) dbCursor.get("totCost");
			vo.setDrugFee(drugFee);
			vo.setTotCost(totCost);
			vos.add(vo);
		}
		if(vos!=null&&vos.size()>0){
			return vos.get(0);
		}
		return new OutpatientUseMedicVo();
	}
	/**  
	 * 
	 * 从mongodb中查询最近12月的人均要用药天数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月12日 下午8:19:30 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月12日 下午8:19:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public OutpatientUseMedicVo queryMedicationDaysByMongo(String date) {
		List<OutpatientUseMedicVo> vos=new ArrayList<OutpatientUseMedicVo>();
		OutpatientUseMedicVo vo=null;
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("selectTime", date);
		DBCursor cursor = new MongoBasicDao().findAlldata(TABLENAME2, bdObject);
		while(cursor.hasNext()){
			vo = new OutpatientUseMedicVo();
			DBObject dbCursor = cursor.next();
			Double  avgDays =(Double) dbCursor.get("avgDays");
			vo.setAvgDays(avgDays);
			vos.add(vo);
		}
		if(vos!=null&&vos.size()>0){
			return vos.get(0);
		}else{
			return new OutpatientUseMedicVo();
		}
	}
	/**  
	 * 
	 * 从mongodb中查询医生用药前5名
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月15日 上午11:48:41 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月15日 上午11:48:41 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<OutpatientUseMedicVo> queryDoctCostByMongo(String date) {
		List<OutpatientUseMedicVo> vos=new ArrayList<OutpatientUseMedicVo>();
		List<OutpatientUseMedicVo> lists=new ArrayList<OutpatientUseMedicVo>();
		OutpatientUseMedicVo vo=null;
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("selectTime", date);
		DBCursor cursor = new MongoBasicDao().findAlldata(TABLENAME3, bdObject);
		while(cursor.hasNext()){
			vo = new OutpatientUseMedicVo();
			DBObject dbCursor = cursor.next();
			String doctCodeName =(String) dbCursor.get("doctCodeName");
			Double num =(Double) dbCursor.get("num");
			Double totCost =(Double) dbCursor.get("totCost");
			vo.setDoctCodeName(doctCodeName);
			vo.setNum(num);
			vo.setTotCost(totCost);
			vos.add(vo);
		}
		Collections.sort(vos, new Comparator<OutpatientUseMedicVo>(){ 
			public int compare(OutpatientUseMedicVo arg0, OutpatientUseMedicVo arg1) { 
				return (int) (arg1.getTotCost()-arg0.getTotCost()) ; 
			} 
		});
		int size = vos.size();
		if (size>=5) {
			lists.add(vos.get(0));
			lists.add(vos.get(1));
			lists.add(vos.get(2));
			lists.add(vos.get(3));
			lists.add(vos.get(4));
		}else{
			for (int i = 0; i < size; i++) {
				lists.add(vos.get(i));
			}
		}
		if(lists!=null&&lists.size()>0){
			return lists;
		}else{
			return new ArrayList<OutpatientUseMedicVo>();
		}
	}
	/**  
	 * 
	 * 从mongodb中查询科室用药前5名
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月15日 下午2:36:51 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月15日 下午2:36:51 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<OutpatientUseMedicVo> queryDeptCostByMongo(String date) {
		List<OutpatientUseMedicVo> vos=new ArrayList<OutpatientUseMedicVo>();
		List<OutpatientUseMedicVo> lists=new ArrayList<OutpatientUseMedicVo>();
		OutpatientUseMedicVo vo=null;
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("selectTime", date);
		DBCursor cursor = new MongoBasicDao().findAlldata(TABLENAME4, bdObject);
		while(cursor.hasNext()){
			vo = new OutpatientUseMedicVo();
			DBObject dbCursor = cursor.next();
			String regDpcdName =(String) dbCursor.get("regDpcdName");
			Double num =(Double) dbCursor.get("num");
			Double totCost =(Double) dbCursor.get("totCost");
			vo.setRegDpcdName(regDpcdName);
			vo.setNum(num);
			vo.setTotCost(totCost);
			vos.add(vo);
		}
		Collections.sort(vos, new Comparator<OutpatientUseMedicVo>(){ 
			public int compare(OutpatientUseMedicVo arg0, OutpatientUseMedicVo arg1) { 
				return (int) (arg1.getTotCost()-arg0.getTotCost()) ; 
			} 
		});
		int size = vos.size();
		if (size>=5) {
			lists.add(vos.get(0));
			lists.add(vos.get(1));
			lists.add(vos.get(2));
			lists.add(vos.get(3));
			lists.add(vos.get(4));
		}else{
			for (int i = 0; i < size; i++) {
				lists.add(vos.get(i));
			}
		}
		if(lists!=null&&lists.size()>0){
			return lists;
		}else{
			return new ArrayList<OutpatientUseMedicVo>();
		}
	}
	/**  
	 * 
	 * 从mongodb中查询门诊月药品金额，用药数量，人次
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月15日 下午4:08:17 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月15日 下午4:08:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<OutpatientUseMedicVo> queryStatisticsCostByMongo(String date) {
		List<OutpatientUseMedicVo> vos=new ArrayList<OutpatientUseMedicVo>();
		OutpatientUseMedicVo vo=null;
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("selectTime", date);
		DBCursor cursor = new MongoBasicDao().findAlldata(TABLENAME6, bdObject);
		while(cursor.hasNext()){
			vo = new OutpatientUseMedicVo();
			DBObject dbCursor = cursor.next();
			String selectTime=(String) dbCursor.get("selectTime");
			Double  totCost =(Double) dbCursor.get("totCost");
			Double  num =(Double) dbCursor.get("num");
			Integer  total =(Integer) dbCursor.get("total");
			String type=(String) dbCursor.get("type");
			vo.setSelectTime(selectTime);
			vo.setTotal(total);
			vo.setTotCost(totCost);
			vo.setNum(num);
			vo.setType(type);
			vos.add(vo);
		}
		if(vos!=null&&vos.size()>0){
			return vos;
		}else{
			return new ArrayList<OutpatientUseMedicVo>();
		}
	}
	/**  
	 * 
	 * 初始化数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年9月8日 下午2:35:34 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年9月8日 下午2:35:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public void init_MZYYJK(String begin, String end, Integer type)
			throws Exception {
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
			String menuAlias="MZYYJK";
			if(1==type){//日数据 dateformate:yyyy-MM-dd
				initUseMedicDao.init_MZYYJK_YZB(menuAlias, "HIS", begin);
				initUseMedicDao.init_MZYYJK_YYTS(menuAlias, "HIS", begin);
				initUseMedicDao.init_MZYYJK_KSYYJE(menuAlias, "HIS", begin);
				initUseMedicDao.init_MZYYJK_YSYYJE(menuAlias, "HIS", begin);
				initUseMedicDao.init_MZYYJK_YPJE(menuAlias, "HIS", begin);
			}else if(2==type){//月数据 dateformate:yyyy-MM 
				Date beginDate=DateUtils.parseDateY_M(begin);
				Date endDate=DateUtils.parseDateY_M(end);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY_M(ca.getTime());
					initUseMedicDao.init_MZYYJK_YZB_MONTH(menuAlias, "2", begin);
					initUseMedicDao.init_MZYYJK_YYTS_MONTH(menuAlias, "2", begin);
					initUseMedicDao.init_MZYYJK_KSYYJE_MONTH(menuAlias, "2", begin);
					initUseMedicDao.init_MZYYJK_YSYYJE_MONTH(menuAlias, "2", begin);
					initUseMedicDao.init_MZYYJK_YPJE_MONTH(menuAlias, "2", begin);
					ca.add(Calendar.MONTH, 1);//下一月
				}
			}
		}
	}
}
