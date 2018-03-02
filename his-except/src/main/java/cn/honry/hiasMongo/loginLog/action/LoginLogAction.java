package cn.honry.hiasMongo.loginLog.action;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

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
import cn.honry.base.bean.model.UserLogin;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.hiasMongo.loginLog.service.LoginLogService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/except/HIASMongo")
public class LoginLogAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(LoginLogAction.class);
	
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}

	private HttpServletRequest request = ServletActionContext.getRequest();
	@Autowired
	@Qualifier(value = "loginLogService")
	private LoginLogService loginLogService;
	
	private String page;//起始页数
	private String rows;//数据列数 
	
	private String menuAlias;
	
	public void setLoginLogService(LoginLogService loginLogService) {
		this.loginLogService = loginLogService;
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
	
	@RequiresPermissions(value={"DLRZX:function:view"})
	@Action(value = "loginLogList", results = { @Result(name = "list", location = "/WEB-INF/pages/sys/loginLog/loginLogList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String loginLogList() {
		return "list";
	}
	
	@RequiresPermissions(value={"DLRZX:function:query"})
	@Action(value = "queryLoginLog")
	public void queryLoginLog(){
		UserLogin userLogin = new UserLogin();
		String ud = request.getParameter("userMessageMap");
		userLogin.setUserId(request.getParameter("userId"));
		if(StringUtils.isNotBlank(request.getParameter("loginTime"))){
			userLogin.setLoginTime(DateUtils.parseDateY_M_D(request.getParameter("loginTime")));
		}
		JSONArray jsonArray = loginLogService.getLoginLogByPage(page,rows,userLogin,ud);
		Long total = loginLogService.getTotalByPage(userLogin);
		try {
			WebUtils.webSendJSON("{\"total\":" + total + ",\"rows\":" + jsonArray + "}");
		}
		catch (Exception ex) {
			ex.printStackTrace();
			logger.error("XTGL_DLRZX", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("XTGL_DLRZX", "系统管理_登录日志（新）", "2", "0"), ex);
		}
	}

	
}
