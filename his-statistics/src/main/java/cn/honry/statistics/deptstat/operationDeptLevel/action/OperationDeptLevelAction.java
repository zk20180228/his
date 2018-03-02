package cn.honry.statistics.deptstat.operationDeptLevel.action;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.deptstat.operationDeptLevel.service.OperationDeptLevelService;
import cn.honry.statistics.deptstat.outPatientMessage.action.OutPatientMessageAction;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/operationDeptLevel")
public class OperationDeptLevelAction {
	
	@Autowired
	@Qualifier(value = "operationDeptLevelService")
	private OperationDeptLevelService operationDeptLevelService;
	public void setOperationDeptLevelService(
			OperationDeptLevelService operationDeptLevelService) {
		this.operationDeptLevelService = operationDeptLevelService;
	}
	
	private Logger logger=Logger.getLogger(OutPatientMessageAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}

	 /** 
     * 栏目别名,在主界面中点击栏目时传到action的参数
     */
	private String deptCode;//科室
	private String menuAlias;
	private String startTime;
	private String endTime;
	private String page;//页数
	private String rows;//每页数
	
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	/**  
	 *  
	 * @Description：  获取list页面
	 * @Author：ZXL
	 * @CreateDate：2016年6月22日 上午9:47:41 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@SuppressWarnings("deprecation")
	@RequiresPermissions(value={"SSKSSSFJTJ:function:view"})
	@Action(value = "listOperationDeptLevel", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/operation/operationDeptLevel.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listOperationDeptLevel() {
		/**
		 * 获取当月第一天至当天时间段
		 */
		Date date = new Date();
		startTime = DateUtils.formatDateYM(date)+"-01";
		endTime = DateUtils.formatDateY_M_D(date);
		ServletActionContext.getRequest().setAttribute("startTime", startTime);
		ServletActionContext.getRequest().setAttribute("endTime", endTime);
		return "list";
		
	}
	
	/**  
	 * 手术科室手术分级统计信息查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月17日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月17日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	
	@SuppressWarnings("deprecation")
	@Action(value="queryOperationDeptLevel")
	public void queryOperationDeptLevel(){
		try {
			if(StringUtils.isBlank(startTime)){
				Date date = new Date();
				startTime = DateUtils.formatDateYM(date)+"-01";
			}
			if(StringUtils.isBlank(endTime)){
				Date date = new Date();
				endTime = DateUtils.formatDateY_M_D(date);
			}
			Map<String, Object> map=operationDeptLevelService.queryOperationDeptLevel(startTime,endTime,deptCode,menuAlias,page,rows);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("KSTJ_SSKSSSFJTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_SSKSSSFJTJ", "科室统计_手术科室手术分级统计", "2", "0"), e);
		}
	}
	
}
