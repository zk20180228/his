package cn.honry.statistics.finance.coststatistics.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.coststatistics.dao.CostStatisticsDAO;
import cn.honry.statistics.finance.coststatistics.service.CostStatisticsService;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;
/**
 * 病人费用汇总查询service实现类
 * @author  lyy
 * @createDate： 2016年6月24日 上午11:01:39 
 * @modifier lyy
 * @modifyDate：2016年6月24日 上午11:01:39
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@Service(value="costStatisticsService")
@Transactional
@SuppressWarnings({"all"})
public class CostStatisticsServiceImpl implements CostStatisticsService {
	@Autowired
	@Qualifier(value="costStatisticsDAO")
	private CostStatisticsDAO costStatisticsDAO;
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	/**
	 * @see 渲染科室
	 */
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	/**收费员渲染**/
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	@Override
	public InpatientFeeInfo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {

	}

	@Override
	public void saveOrUpdate(InpatientFeeInfo arg0) {
	}

	@Override
	public List<InpatientFeeInfo> getPageCostStatistics(String firstData, String endData, String inpatientNo,String page, String rows) {
		List<String> tnL=getTnl(firstData,  endData);
		return costStatisticsDAO.getPageCostStatistics(tnL,firstData,endData,inpatientNo,page,rows);
	}

	public List<String> getTnl(String firstData, String endData){
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",firstData,endData);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),firstData);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",yNum+1);
					tnL.add(0,"T_INPATIENT_FEEINFO_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_FEEINFO_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return tnL;
	}
	
	@Override
	public int getTotalCostStatistics(String firstData, String endData, String inpatientNo) {
		String redKey = "BRFYHZTJ:"+firstData+"_"+endData+"_"+inpatientNo;
		redKey=redKey.replaceAll(",", "-");
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum==null){
			List<String> tnL=getTnl(firstData,  endData);
			totalNum = costStatisticsDAO.getTotalCostStatistics(tnL,firstData,endData,inpatientNo);
			redisUtil.set(redKey, totalNum);
		}
		String val = parameterInnerDAO.getParameterByCode("deadTime");
		if(StringUtils.isNotBlank(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
		
		return totalNum;
	}

	@Override
	public List<InpatientFeeInfo> queryCostStatistice(String firstData, String endData, String inpatientNo) {
		List<String> tnL=getTnl(firstData,  endData);
		return costStatisticsDAO.queryCostStatistice(tnL,firstData,endData,inpatientNo);
	}

	@Override
	public FileUtil export(List<InpatientFeeInfo> list, FileUtil fUtil) {
		//员工渲染
		Map<String,String> empMap= employeeInInterService.queryEmpCodeAndNameMap();
		//查询科室用作渲染
		Map<String,String> deptCodeAndName=deptInInterService.querydeptCodeAndNameMap();
		for(InpatientFeeInfo vo:list){
			if(empMap.containsKey(vo.getFeeOpercode())){
				vo.setFeeOpercode(empMap.get(vo.getFeeOpercode()));
			}
			if(empMap.containsKey(vo.getRecipeDoccode())){
				vo.setRecipeDoccode(empMap.get(vo.getRecipeDoccode()));
			}
			if(StringUtils.isNotBlank(deptCodeAndName.get(vo.getExecuteDeptcode()))){
				vo.setExecuteDeptcode(deptCodeAndName.get(vo.getExecuteDeptcode()));
			}
			if(StringUtils.isNotBlank(vo.getRecipeDeptcode())){
				vo.setRecipeDeptcode(deptCodeAndName.get(vo.getRecipeDeptcode()));
			}
		}
		for (InpatientFeeInfo info : list) {
			String record="";
			record+=CommonStringUtils.trimToEmpty(info.getInpatientNo())+",";
			record+=CommonStringUtils.trimToEmpty(info.getName())+",";
			record+=CommonStringUtils.trimToEmpty(info.getFeeCode())+",";
			record+=info.getTotCost()+",";
			record+=CommonStringUtils.trimToEmpty(info.getRecipeDeptcode())+",";
			record+=CommonStringUtils.trimToEmpty(info.getRecipeDoccode())+",";
			record+=CommonStringUtils.trimToEmpty(info.getExecuteDeptcode())+",";
			record+=CommonStringUtils.trimToEmpty(info.getFeeOpercode())+",";
			record+= info.getFeeDate()+",";
			try {
				fUtil.write(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}
		return fUtil;
	}

	@Override
	public List<InpatientInfoNow> queryInpatientInfo(String medicalrecordId) {
		return costStatisticsDAO.queryInpatientInfo(medicalrecordId);
	}

	@Override
	public String getMedicalrecordId(String idCard) {
        return costStatisticsDAO.getMedicalrecordId(idCard);
	}
	
}
