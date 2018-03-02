package cn.honry.mobile.exceptionLog.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/mosys/moExceptionLog")
public class ExceptionLogAction extends ActionSupport{

	private Logger logger = Logger.getLogger(ExceptionLogAction.class);
	
	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	
	private String menuAlias;
	
	private String page;
	
	private String rows;
	
	private String codeName;
	
	private String warnLevelSelect;
	
	private String warnTypeSelect;
	
	private String startWarnDate;
	
	private String endWarnDate;
	
	private String processStatus;

	private String id;
	
	@RequiresPermissions(value={"MOYCRZ:function:view"})
	@Action(value = "toView", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/exception/exceptionLog.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toView(){
		return "list";
	}
	
	@RequiresPermissions(value={"MOYCRZ:function:query"})
	@Action(value = "queryMobileException")
	public void queryMobileException(){
		Map<String, Object> map = new HashMap<String, Object>();
		try { 
			RecordToMobileException hiasException = new RecordToMobileException();
			hiasException.setCodeName(codeName);
			hiasException.setWarnLevel(warnLevelSelect);
			hiasException.setWarnType(warnTypeSelect);
			if(StringUtils.isNotBlank(startWarnDate)){//告警查询起始时间
				hiasException.setWarnDate(DateUtils.parseDateY_M_D(startWarnDate));
			}
			if(StringUtils.isNotBlank(endWarnDate)){//告警查询截止时间，暂存放在处理时间处
				Calendar c = Calendar.getInstance();
			    Date date=null; 
				date = new SimpleDateFormat("yy-MM-dd").parse(endWarnDate); 
				c.setTime(date);
				int day=c.get(Calendar.DATE);
				c.set(Calendar.DATE,day+1); 
				String dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()); 
				hiasException.setProcessTime(DateUtils.parseDateY_M_D(dayAfter));
			}
			hiasException.setProcessStatus(processStatus);
			JSONArray jsonArray = exceptionLogService.getHIASExceptionByPage(page, rows, hiasException);
			Long total = exceptionLogService.getTotalByPage(hiasException);
			map.put("total", total);
			map.put("rows", jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MOYCRZ", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("MOYCRZ","异常日志","2","1"), e);
		} 
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	@Action(value = "startDeal")
	public void startDeal(){
		String res = "success";
		try{
			exceptionLogService.startDeal(id);
		}catch(Exception e){
			res = "error";
			e.printStackTrace();
			logger.error("MOYCRZ", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("MOYCRZ","异常日志","2","1"), e);
		}
		WebUtils.webSendString(res);
	}
	
	
	@Action(value = "endDeal")
	public void endDeal(){
		String res = "success";
		try{
			exceptionLogService.endDeal(id);
		}catch(Exception e){
			res = "error";
			e.printStackTrace();
			logger.error("MOYCRZ", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("MOYCRZ","异常日志","2","1"), e);
		}
		WebUtils.webSendString(res);
	}
	
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
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
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getWarnLevelSelect() {
		return warnLevelSelect;
	}
	public void setWarnLevelSelect(String warnLevelSelect) {
		this.warnLevelSelect = warnLevelSelect;
	}
	public String getWarnTypeSelect() {
		return warnTypeSelect;
	}
	public void setWarnTypeSelect(String warnTypeSelect) {
		this.warnTypeSelect = warnTypeSelect;
	}
	public String getStartWarnDate() {
		return startWarnDate;
	}
	public void setStartWarnDate(String startWarnDate) {
		this.startWarnDate = startWarnDate;
	}
	public String getEndWarnDate() {
		return endWarnDate;
	}
	public void setEndWarnDate(String endWarnDate) {
		this.endWarnDate = endWarnDate;
	}
	public String getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
