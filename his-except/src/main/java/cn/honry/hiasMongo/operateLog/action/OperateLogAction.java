package cn.honry.hiasMongo.operateLog.action;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

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
import cn.honry.hiasMongo.operateLog.service.OperateLogService;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/except/HIASMongo")
public class OperateLogAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private Logger logger=Logger.getLogger(OperateLogAction.class);
	
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	@Autowired
	@Qualifier(value = "operateLogService")
	private OperateLogService operateLogService;
	
	private String page;//起始页数
	private String rows;//数据列数 
	
	private String menuAlias;

	public void setOperateLogService(OperateLogService operateLogService) {
		this.operateLogService = operateLogService;
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
	
	@RequiresPermissions(value={"CZRZX:function:view"})
	@Action(value = "operateLogList", results = { @Result(name = "list", location = "/WEB-INF/pages/sys/operateLog/operationList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String operateLogList() {
		return "list";
	}
	
	@RequiresPermissions(value={"CZRZX:function:query"})
	@Action(value = "queryOperateLog")
	public void queryOperateLog(){
		try{
			String ud = request.getParameter("userMessageMap");
			String operateName = request.getParameter("queryName");
			JSONArray jsonArray = operateLogService.getOperateLogByPage(page, rows, operateName, ud);
			Long total = operateLogService.getTotalByPage(operateName);
			WebUtils.webSendJSON("{\"total\":" + total + ",\"rows\":" + jsonArray + "}");
		}catch(Exception e){ 
			e.printStackTrace();
			logger.error("XTGL_CZRZX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("XTGL_CZRZX", "系统管理_操作日志（新）", "2", "0"), e);
		}
	}

}
