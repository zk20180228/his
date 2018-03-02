package cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.dao.OutpatientIndicatorsDao;
import cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.service.OutpatientIndicatorsService;
import cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.vo.OutpatientIndicatorsVO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
@Service("outpatientIndicatorsService")
public class OutpatientIndicatorsServiceImpl implements
		OutpatientIndicatorsService {
	@Autowired
	@Qualifier(value="outpatientIndicatorsDao")
	private OutpatientIndicatorsDao outpatientIndicatorsDao;
	public void setOutpatientIndicatorsDao(
			OutpatientIndicatorsDao outpatientIndicatorsDao) {
		this.outpatientIndicatorsDao = outpatientIndicatorsDao;
	}
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Override
	public Map<String, List<OutpatientIndicatorsVO>> findOutpatientIndicators(
			String sTime, String eTime,boolean flag) {
		//科室map
		Map<String, String> map = deptInInterService.querydeptCodeAndNameMap();
		Map<String, List<OutpatientIndicatorsVO>> returnMap = new HashMap<String, List<OutpatientIndicatorsVO>>();
		//挂号总人数map
		Map<String,Double> totalRegMap = new HashMap<String, Double>();
		Map<String,Double> totalRigisterSeeMap = new HashMap<String, Double>();
		//日平均门诊人次
		List<OutpatientIndicatorsVO> dayAverageTimeList = new ArrayList<OutpatientIndicatorsVO>();
		//门诊人均诊察人次
		List<OutpatientIndicatorsVO> averageSeeTimeList = new ArrayList<OutpatientIndicatorsVO>();
		//人均门诊费用
		List<OutpatientIndicatorsVO> averageFeeList = new ArrayList<OutpatientIndicatorsVO>();
		List<OutpatientIndicatorsVO> emergencyList = new ArrayList<OutpatientIndicatorsVO>();
		String regisgerMain = null;//挂号主表
		String schedule = null;//排班表
		String feedetial = null;//处方明细表
		if(flag){
			regisgerMain = this.getPartation("T_REGISTER_MAIN", sTime, eTime);
			schedule = this.getPartation("T_REGISTER_SCHEDULE", sTime, eTime);
			feedetial = this.getPartation("T_OUTPATIENT_FEEDETAIL", sTime, eTime);
		}else{
			regisgerMain = "T_REGISTER_MAIN_NOW";
			schedule = "T_REGISTER_SCHEDULE_NOW";
			feedetial = "T_OUTPATIENT_FEEDETAIL_NOW";
		}
		//挂号总人数
		List<OutpatientIndicatorsVO> totalRigister = outpatientIndicatorsDao.queryTotalOutpatientClinicVisits(regisgerMain, sTime, eTime, false,false);
		List<OutpatientIndicatorsVO> clinicVisits = outpatientIndicatorsDao.queryTotalOutpatientClinicVisits(regisgerMain, sTime, eTime, false,true);
		totalRigister.addAll(clinicVisits);//添加院区数据
		//就诊总人数
		List<OutpatientIndicatorsVO> totalRigisterSee = outpatientIndicatorsDao.queryTotalOutpatientClinicVisits(regisgerMain, sTime, eTime, true,false);
		List<OutpatientIndicatorsVO> visits = outpatientIndicatorsDao.queryTotalOutpatientClinicVisits(regisgerMain, sTime, eTime, true,true);
		totalRigisterSee.addAll(visits);//添加院区数据
		//门诊工作总时数
		List<OutpatientIndicatorsVO> workTotalTime = outpatientIndicatorsDao.queryClinicWorkTotalTime(schedule, sTime, eTime,false);
		List<OutpatientIndicatorsVO> totalTime = outpatientIndicatorsDao.queryClinicWorkTotalTime(schedule, sTime, eTime,true);
		workTotalTime.addAll(totalTime);//添加院区数据
		//门诊总收入
		List<OutpatientIndicatorsVO> totalOutpatientIncome = outpatientIndicatorsDao.queryTotalOutpatientIncome(feedetial, sTime, eTime,false);
		List<OutpatientIndicatorsVO> income = outpatientIndicatorsDao.queryTotalOutpatientIncome(feedetial, sTime, eTime,true);
		totalOutpatientIncome.addAll(income);//添加院区数据
		//门诊入院人数
		List<OutpatientIndicatorsVO> outpatientAndEmergencyTime = outpatientIndicatorsDao.queryTotalOutpatientAndEmergencyTime(regisgerMain, sTime, eTime,false);
		List<OutpatientIndicatorsVO> emergencyTime = outpatientIndicatorsDao.queryTotalOutpatientAndEmergencyTime(regisgerMain, sTime, eTime,true);
		outpatientAndEmergencyTime.addAll(emergencyTime);//添加院区数据
		for (OutpatientIndicatorsVO vo : totalRigisterSee) {
			totalRigisterSeeMap.put(vo.getDeptCode(), vo.getNumerator());
		}
		for (OutpatientIndicatorsVO vo : totalRigister) {
			totalRegMap.put(vo.getDeptCode(), vo.getNumerator());
			vo.setDenominator(vo.getNumerator());
			vo.setNumerator(totalRigisterSeeMap.get(vo.getDeptCode())==null?0.0:totalRigisterSeeMap.get(vo.getDeptCode()));
			vo.setDeptName(map.get(vo.getDeptCode()));
			averageSeeTimeList.add(vo);
		}
		//日平均门诊人次
		for (OutpatientIndicatorsVO vo : workTotalTime) {
			vo.setNumerator(totalRegMap.get(vo.getDeptCode())==null?0.0:totalRegMap.get(vo.getDeptCode()));
			vo.setDeptName(map.get(vo.getDeptCode()));
			dayAverageTimeList.add(vo);
		}
		//人均门诊费用
		for (OutpatientIndicatorsVO vo : totalOutpatientIncome) {
			vo.setDenominator(totalRegMap.get(vo.getDeptCode())==null?0.0:totalRegMap.get(vo.getDeptCode()));
			vo.setDeptName(map.get(vo.getDeptCode()));
			averageFeeList.add(vo);
		}
		//每百 门急诊入院人数
		for (OutpatientIndicatorsVO vo : outpatientAndEmergencyTime) {
			vo.setDenominator(totalRegMap.get(vo.getDeptCode())==null?0.0:totalRegMap.get(vo.getDeptCode()));
			vo.setDeptName(map.get(vo.getDeptCode()));
			emergencyList.add(vo);
		}
		returnMap.put("RPJMZRC", dayAverageTimeList);//日平均门诊人次
		returnMap.put("MZRJZCRC", averageSeeTimeList);//门诊人均诊察人次
		returnMap.put("RJMZFY", averageFeeList);//人均门诊费用
		returnMap.put("RYRS", emergencyList);//每百  门急诊入院人数
		return returnMap;
	}
	private String getPartation(String tableName,String sTime,String eTime){
		Date date = DateUtils.parseDateY_M_D(eTime);
		Date addDay = DateUtils.addDay(date, -1);
		String y_M_D = DateUtils.formatDateY_M_D(addDay);
		List<String> list = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,tableName,sTime,y_M_D);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return tableName;
		}
	}
}
