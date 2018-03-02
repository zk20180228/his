package cn.honry.statistics.bi.bistac.outpatientStac.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.bi.bistac.outpatientStac.dao.OutpatientStacVoDao;
import cn.honry.statistics.bi.bistac.outpatientStac.service.OutpatientStacVoService;
import cn.honry.statistics.bi.bistac.outpatientStac.vo.BusinessContractunitVo;
import cn.honry.statistics.bi.bistac.outpatientStac.vo.OutpatientStacVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
@Service("outpatientStacVoService")
@Transactional
@SuppressWarnings({ "all" })
public class OutpatientStacVoServiceImpl implements OutpatientStacVoService{
	@Autowired
	@Qualifier(value = "outpatientStacVoDao")
	private OutpatientStacVoDao outpatientStacVoDao;
	@Override
	public OutpatientStacVo queryRegistrationVo() {
		return outpatientStacVoDao.queryRegistrationVo();
	}

	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	@Override
	public OutpatientStacVo queryOperationApplyVo() {
		return outpatientStacVoDao.queryOperationApplyVo();
	}

	@Override
	public OutpatientStacVo queryInpatientInfoNowVo() {
		return outpatientStacVoDao.queryInpatientInfoNowVo();
	}

	@Override
	public OutpatientStacVo queryBusinessHospitalbedVo() {
		return outpatientStacVoDao.queryBusinessHospitalbedVo();
	}

	@Override
	public OutpatientStacVo queryOutpatientFeedetailNowCostVoD() {
		return outpatientStacVoDao.queryOutpatientFeedetailNowCostVoD();
	}

	@Override
	public OutpatientStacVo queryOutpatientFeedetailNowCostVoM() {
		//获取当前表最大时间及最小时间
//		OutpatientStacVo outpatientStacVo = outpatientStacVoDao.findMaxMin();		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String stime=dateFormat.format(new Date())+"-01";
		String etime=dateFormat2.format(new Date());
		Date sTime = null;
		Date eTime = null;
		List<String> tnL = new ArrayList<String>();
		try {
			sTime = dateFormat2.parse(stime);
			eTime = dateFormat2.parse(etime);
		/***************获得在线库数据应保留最小时间**********************/
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			Date cTime = DateUtils.addDay(dTime,-(Integer.parseInt(dateNum)*30)+1);
	//		System.out.println("1 ----->"+stime+"---- "+etime+" ---"+sTime+" ----"+eTime);
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",stime,etime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(dateFormat.format(cTime),stime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
					tnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outpatientStacVoDao.queryOutpatientFeedetailNowCostVoM(tnL,stime,etime);
	}

	@Override
	public OutpatientStacVo queryOutpatientFeedetailNowCostVoY() {
		//获取当前表最大时间及最小时间
//		OutpatientStacVo outpatientStacVo = outpatientStacVoDao.findMaxMin();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String stime=dateFormat.format(new Date())+"-01-01";
		String etime=dateFormat2.format(new Date());
		Date sTime = null;
		Date eTime = null;
		List<String> tnL = new ArrayList<String>();
		try {
			sTime = dateFormat2.parse(stime);
			eTime = dateFormat2.parse(etime);
			/***************获得在线库数据应保留最小时间**********************/
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			Date cTime = DateUtils.addDay(dTime,-(Integer.parseInt(dateNum)*30)+1);
	//		System.out.println("2 ----->"+stime+" ----"+etime+" ----"+sTime+" ----"+eTime);
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",stime,etime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(dateFormat.format(cTime),stime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
					tnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outpatientStacVoDao.queryOutpatientFeedetailNowCostVoY(tnL,stime, etime);
	}

	@Override
	public OutpatientStacVo queryInpatientFeeInfoNowCostD() {
		return outpatientStacVoDao.queryInpatientFeeInfoNowCostD();
	}

	@Override
	public OutpatientStacVo queryInpatientFeeInfoNowCostM() {
		
//		//获取当前表最大时间及最小时间
//		OutpatientStacVo outpatientStacVo = outpatientStacVoDao.findMaxMinZ();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String stime=dateFormat.format(new Date())+"-01";
		String etime=dateFormat2.format(new Date());
		Date sTime = null;
		Date eTime = null;
		List<String> tnL = new ArrayList<String>();
		try {
			sTime = dateFormat2.parse(stime);
			eTime = dateFormat2.parse(etime);
			/***************获得在线库数据应保留最小时间**********************/
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
	//		System.out.println(" 3----->"+stime+"--- "+etime+" ----"+sTime+"--- "+eTime);
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",stime,etime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(dateFormat.format(cTime),stime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",yNum+1);
					tnL.add(0,"T_INPATIENT_FEEINFO_NOW");
				}
		}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
			tnL.add("T_INPATIENT_FEEINFO_NOW");
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outpatientStacVoDao.queryInpatientFeeInfoNowCostM(tnL,stime, etime);
	}

	@Override
	public OutpatientStacVo queryInpatientFeeInfoNowCostY() {
		//获取当前表最大时间及最小时间
//		OutpatientStacVo outpatientStacVo = outpatientStacVoDao.findMaxMinZ();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String stime=dateFormat.format(new Date())+"-01-01";
		String etime=dateFormat2.format(new Date());
		Date sTime = null;
		Date eTime = null;
		List<String> tnL = new ArrayList<String>();
		try {
			sTime = dateFormat2.parse(stime);
			eTime = dateFormat2.parse(etime);
			/***************获得在线库数据应保留最小时间**********************/
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			Date cTime = DateUtils.addDay(dTime,-(Integer.parseInt(dateNum)*30)+1);
	//		System.out.println(" 4----->"+stime+"---- "+etime+" ---"+sTime+" ----"+eTime);
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",stime,etime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(dateFormat.format(cTime),stime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",yNum+1);
					tnL.add(0,"T_INPATIENT_FEEINFO_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_FEEINFO_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outpatientStacVoDao.queryInpatientFeeInfoNowCostY(tnL,stime, etime);
	}


	@Override
	public List<BusinessContractunitVo> queryBusinessContractunit() {
		return outpatientStacVoDao.queryBusinessContractunit();
	}
	@Override
	public List<BusinessContractunitVo> queryInOutNum() {
		return outpatientStacVoDao.queryInOutNum();
	}


	@Override
	public int queryBusinessContractunitTotal(String encode) {
		return outpatientStacVoDao.queryBusinessContractunitTotal(encode);
	}


	@Override
	public int queryBusinessContractunitTotalJi(String encode) {
		return outpatientStacVoDao.queryBusinessContractunitTotalJi(encode);
	}


	@Override
	public int queryBusinessContractunitTotalGo(String encode) {
		return outpatientStacVoDao.queryBusinessContractunitTotalGo(encode);
	}


	@Override
	public int queryBusinessContractunitTotalNew(String encode) {
		return outpatientStacVoDao.queryBusinessContractunitTotalNew(encode);
	}
	
}
