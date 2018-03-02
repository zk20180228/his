package cn.honry.statistics.leaveOrder.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientExecdrugNow;
import cn.honry.base.bean.model.InpatientExecundrugNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.leaveOrder.dao.LeaveOrderDao;
import cn.honry.statistics.leaveOrder.service.LeaveOrderService;
import cn.honry.statistics.leaveOrder.vo.InpatientExecUnDrugVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Service("leaveOrderService")
@Transactional
@SuppressWarnings({ "all" })
public class LeaveOrderServiceImpl implements LeaveOrderService{
	
	@Autowired
	@Qualifier(value = "leaveOrderDao")
	private LeaveOrderDao leaveOrderDao;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	/**查询医嘱流水号   查询出对应数据条数来判断
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	@Override
	public List<InpatientInfoNow> queryLSHList(String queryBlh,String queryLsh, String startTime, String endTime) {
		//先查询住院信息 新老表   查询已出院患者的住院流水号条数
		List<InpatientInfoNow> infoList=leaveOrderDao.queryInfoNows(queryBlh,queryLsh, startTime, endTime);
		return infoList;
	}
	/**
	 * GH  根据弹窗选择的流水号查询 医嘱执行单药品数据
	 * 2017年2月23日11:40:58
	 */
	
	@Override
	public List<InpatientExecdrugNow> queryDrugLists(String queryLsh, String page,String rows,String startTime,String endTime ) {
		List<String> tnL = null;
		try{
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
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_EXECDRUG",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_EXECDRUG",yNum+1);
					tnL.add(0,"T_INPATIENT_EXECDRUG_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_EXECDRUG_NOW");
			}
		}catch(Exception e){
			e.printStackTrace();
			tnL = null;
		}
		return leaveOrderDao.queryDrugList(tnL,queryLsh,startTime,endTime,page,rows);
	}
	@Override
	public int queryDrugListsTotal(String queryLsh, String startTime,
			String endTime) {
		List<String> tnL = null;
		try{
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
			if(DateUtils.compareDate(sTime, cTime)==-1){ 
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_EXECDRUG",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_EXECDRUG",yNum+1);
					tnL.add(0,"T_INPATIENT_EXECDRUG_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_EXECDRUG_NOW");
			}
		}catch(Exception e){
			e.printStackTrace();
			tnL = null;
		}
		return leaveOrderDao.queryDrugTotal(tnL,queryLsh, startTime, endTime);
	}
	/**
	 * GH  根据弹窗选择的流水号查询 医嘱执行单 非 药品数据
	 * 2017年2月23日11:40:58
	 */
	
	@Override
	public List<InpatientExecundrugNow>  queryUnDrugList(String queryLsh,String page,String rows,String startTime,String endTime ) {
		List<String> tnL = null;
		try{
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
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_EXECUNDRUG",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_EXECUNDRUG",yNum+1);
					tnL.add(0,"T_INPATIENT_EXECUNDRUG_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_EXECUNDRUG_NOW");
			}
		}catch(Exception e){
			e.printStackTrace();
			tnL = null;
		}
		return leaveOrderDao.queryUnDrugList(tnL,queryLsh,startTime,endTime,page,rows);
	}
	@Override
	public int queryUnDrugListTotal(String queryLsh, String startTime,String endTime) {
		List<String> tnL = null;
		try{
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
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_EXECUNDRUG",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_EXECUNDRUG",yNum+1);
					tnL.add(0,"T_INPATIENT_EXECUNDRUG_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_EXECUNDRUG_NOW");
			}
		}catch(Exception e){
			e.printStackTrace();
			tnL = null;
		}
		return leaveOrderDao.queryUnDrugTotal(tnL,queryLsh, startTime, endTime);
	}
}
