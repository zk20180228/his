package cn.honry.statistics.sys.stop.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.regisdocscheinfo.vo.RegisDocScheInfoVo;
import cn.honry.statistics.sys.stop.dao.OutpatientStopDao;
import cn.honry.statistics.sys.stop.service.OutpatientStopService;
import cn.honry.statistics.sys.stop.vo.OutPatientVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
/**
 * 门诊停诊原因统计表service实现层
 * @author  lyy
 * @createDate： 2016年6月23日 上午10:52:40 
 * @modifier lyy
 * @modifyDate：2016年6月23日 上午10:52:40
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@Service(value="outpatientStopService")
@Transactional
@SuppressWarnings({"all"})
public class OutpatientStopServiceImpl implements OutpatientStopService{
	
	@Autowired
	@Qualifier(value="outpatientStopDao")
	private OutpatientStopDao outpatientStopDao;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Override
	public OutPatientVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(OutPatientVo arg0) {
	}

	@Override
	public List<OutPatientVo> getPageOutpatientStop(String firstData, String endData)throws Exception {
		
		// 获取门诊数据保留时间
		String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
		
		// 获得当前时间
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dTime;
		try {
			dTime = df.parse(df.format(new Date()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		// 获得在线库数据应保留最小时间
		Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
		
		Date sTime = DateUtils.parseDateY_M_D(firstData);
		Date eTime = DateUtils.parseDateY_M_D(endData);
		List<String> tnL = new ArrayList<String>();
		
		//判断查询类型
		if(DateUtils.compareDate(sTime, cTime)==-1){
			if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
				
				//获取需要查询的全部分区
				tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_SCHEDULE",firstData,endData);
			}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
				
				//获得时间差(年)
				int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),firstData);
				//获取相差年分的分区集合，默认加1
				tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_SCHEDULE",yNum+1);
				tnL.add(0,"T_REGISTER_SCHEDULE_NOW");
			}
		}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
			
			tnL.add("T_REGISTER_SCHEDULE_NOW");
		}
		
		Map<String, String> stopReason = outpatientStopDao.getStopReason();
		List<OutPatientVo> voList = outpatientStopDao.getOutpatientStop(stopReason, tnL, firstData, endData);
		
		return voList;
	}

	@Override
	public FileUtil export(List<OutPatientVo> outPatientList, FileUtil fileUtil) throws Exception{
		for (OutPatientVo patinentVo : outPatientList) {
			String record="";
			record=CommonStringUtils.trimToEmpty(patinentVo.getDeptName())+",";
			record+=patinentVo.getSumSick()+",";
			record+=patinentVo.getSumEvection()+",";
			record+=patinentVo.getSumMeet()+",";
			record+=patinentVo.getSumOther()+",";
			record+=patinentVo.getSum()+",";
			
			fileUtil.write(record);
			
		}
		return fileUtil;
	}

}
