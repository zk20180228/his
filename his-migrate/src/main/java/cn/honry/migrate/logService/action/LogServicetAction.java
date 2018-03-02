package cn.honry.migrate.logService.action;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.base.bean.model.LogServiceVo;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.ServiceManagement;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.migrate.serviceManagement.service.ServiceManagementService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

import freemarker.template.utility.DateUtil;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/migrate/ServiceManagement")
@SuppressWarnings({"all"})	
public class LogServicetAction extends ActionSupport{
	private ServiceManagementService serviceManagementService;
	@Autowired
	@Qualifier(value = "serviceManagementService")
	public void setServiceManagementService(
			ServiceManagementService serviceManagementService) {
		this.serviceManagementService = serviceManagementService;
	}
	private Logger logger=Logger.getLogger(LogServicetAction.class);
		@Autowired
		@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	//分页
	private String page;
	private String rows;
	private String code;
	private String menuAlias;//权限
	private String serviceName;//服务名称
	private String ip;
	private String Stime;
	private String Etime;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * 跳转到服务管理页面
	 * @return
	 */
	@RequiresPermissions(value={"FWGL:function:view"})
	@Action(value = "listLogService", results = { @Result(name = "list", location = "/WEB-INF/pages/migrate/serviceManagement/logService.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listServiceManagement() {
		return "list";
	}
	/**  
	 * 服务管理日志列表(查询)
	 * @Author: XCL
	 * @CreateDate: 2017年9月28日 下午4:09:43 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月28日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	
	@Action(value="queryLogService")
	public void queryServiceManagement(){
		try {
			List<LogServiceVo> list = serviceManagementService.queryLogService(serviceName, Stime, Etime, page, rows, menuAlias);
			int total=serviceManagementService.totalService(serviceName, Stime, Etime);
			Map<Object, Object> map=new HashMap<Object,Object>();
			map.put("total",total);
			map.put("rows", list);
			String json = JSONUtils.toJson(map,"yyyy-MM-dd HH:mm:ss");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("CSJKGL_FWGL_RZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_FWGL_RZ", "厂商接口管理_服务管理_日志", "2", "0"), e);
		}
	}






}