package cn.honry.statistics.bi.bistac.outpatientEmergency.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.bi.bistac.outpatientEmergency.service.OutpatientEmergencyService;
import cn.honry.statistics.bi.bistac.outpatientEmergency.vo.OutpatientEmergencyVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/OutpatientEmergency")
public class OutpatientEmergencyAction {
	// 记录异常日志
	private Logger logger = Logger.getLogger(OutpatientEmergencyAction.class);
	//今天	
	private String todayTime;
	@Resource
	private OutpatientEmergencyService outpatientEmergencyService;
	public void setOutpatientEmergencyService(OutpatientEmergencyService outpatientEmergencyService) {
		this.outpatientEmergencyService = outpatientEmergencyService;
	}
	@Resource
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	public String getTodayTime() {
		return todayTime;
	}

	public void setTodayTime(String todayTime) {
		this.todayTime = todayTime;
	}

	/** 
	* @Description: 门急诊疾病统计页面 
	* @return String    返回类型 
	* @author zx 
	* @date 2017年11月13日
	*/
	@Action(value = "outpatientEmergencyeList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/outpatientEmergency/outpatientEmergency.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String operationCostList(){
		todayTime=DateUtils.formatDateY_M_D(new Date());
		return "list";
	}
	@Action(value = "listStatisticsQuery", results = { @Result(name = "json", type = "json") })
	public void listStatisticsQuery(){
		LinkedHashMap<String, Object> dataMap = new LinkedHashMap<String, Object>();
		List<Object> yList = new ArrayList<Object>();
		List<String> xList = new ArrayList<String>();
		try {
			String sTime = ServletActionContext.getRequest().getParameter("time");
			OutpatientEmergencyVo outpatient= outpatientEmergencyService.getDataInfoByTime(sTime);
			if(outpatient!=null){
				yList.add(StringUtils.isNoneBlank(outpatient.getWSNum())?outpatient.getWSNum():0);
				yList.add(StringUtils.isNoneBlank(outpatient.getFSDCNum())?outpatient.getFSDCNum():0);
				yList.add(StringUtils.isNoneBlank(outpatient.getJXWXYNum())?outpatient.getJXWXYNum():0);
				yList.add(StringUtils.isNoneBlank(outpatient.getJFTNum())?outpatient.getJFTNum():0);
				yList.add(StringUtils.isNoneBlank(outpatient.getHXDGRNum())?outpatient.getHXDGRNum():0);
				yList.add(StringUtils.isNoneBlank(outpatient.getNXGBBNum())?outpatient.getNXGBBNum():0);
				yList.add(StringUtils.isNoneBlank(outpatient.getJFZNum())?outpatient.getJFZNum():0);
				yList.add(StringUtils.isNoneBlank(outpatient.getGXBNum())?outpatient.getGXBNum():0);
				yList.add(StringUtils.isNoneBlank(outpatient.getJJZDNum())?outpatient.getJJZDNum():0);
				yList.add(StringUtils.isNoneBlank(outpatient.getGXYNum())?outpatient.getGXYNum():0);
				yList.add(StringUtils.isNoneBlank(outpatient.getQTNum())?outpatient.getQTNum():0);
			}
			xList.add("外伤");
			xList.add("发热待查");
			xList.add("急性胃肠炎");
			xList.add("急腹痛原因待查");
			xList.add("呼吸道感染");
			xList.add("脑血管病变");
			xList.add("急腹症");
			xList.add("冠心病");
			xList.add("急性酒精中毒");
			xList.add("高血压");
			xList.add("其他");
			dataMap.put("state","success");
			dataMap.put("data", outpatient);
			dataMap.put("xdata", xList);
			dataMap.put("ydata", yList);
		} catch (Exception e) {
			dataMap.put("state","faile");
			dataMap.put("data", new OutpatientEmergencyVo());
			logger.error(e);
			logger.error("TJFXGL_MJZJBTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MJZJBTJ", "门诊统计分析_门急诊疾病统计", "2","0"), e);
		}
		String json = JSONUtils.toJson(dataMap);
		WebUtils.webSendJSON(json);
	}
}
