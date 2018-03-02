package cn.honry.mobile.moLoginLog.action;

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

import cn.honry.base.bean.model.LoginLog;
import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.inner.system.user.service.UserInInterService;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.mobile.moLoginLog.service.MoLoginLogService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/mosys/moLoginLogAction")
public class MoLoginLogAction extends ActionSupport{
	/** 
	* 日志文件参数 
	*/ 
	private Logger logger=Logger.getLogger(MoLoginLogAction.class);
	/** 
	* 登录日志service 
	*/ 
	@Autowired
	@Qualifier(value = "moLoginLogService")
	private MoLoginLogService moLoginLogService;
	public void setMoLoginLogService(MoLoginLogService moLoginLogService) {
		this.moLoginLogService = moLoginLogService;
	}
	/** 
	* 异常日志service 
	*/ 
	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService  exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	@Autowired
	@Qualifier(value = "userInInterService")
	private UserInInterService userInInterService;
	public void setUserInInterService(UserInInterService userInInterService) {
		this.userInInterService = userInInterService;
	}
	
	private String menuAlias;
	
	private String userMessageMap;
	
	private String userId;
	
	private String loginTime;
	
	private String page;
	
	private String rows;
	
	/** 
	* @Description: 登录日志访问地址 
	* @return String    返回类型 
	* @author zx 
	* @date 2017年7月3日
	*/
	@RequiresPermissions(value={"MODLRZ:function:view"})
	@Action(value = "loginLogList", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/loginLog/loginLogList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String loginLogList() {
		return "list";
	}
	/** 
	* @Description: 登录日志的查询 
	* @param request
	* @param response
	* @param page 当前页
	* @param rows 每页显示的记录数
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月3日
	*/
	@RequiresPermissions(value={"MODLRZ:function:view"})
	@Action(value = "queryLoginLog")
	public void queryLoginLog(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			LoginLog userLogin = new LoginLog();
			//用户的map
			userLogin.setUserId(userId);
			if(StringUtils.isNotBlank(loginTime)){
				userLogin.setLoginTime(DateUtils.parseDateY_M_D(loginTime));
			 }
			//分页查询登录日志
	 		JSONArray jsonArray = moLoginLogService.getLoginLogByPage(page,rows,userLogin,userMessageMap);
			//登陆日志总条数
	 		Long total = moLoginLogService.getTotalByPage(userLogin);
	 		map.put("total", total);
	 		map.put("rows", jsonArray);
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.error("MODLRZ", ex);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("MODLRZ","登录日志","2","1"), ex);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/** 
	* @Description: 查询所有的用户(用于渲染和保存到mongdb中) 
	* @param request
	* @param response
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月3日
	*/
	@RequiresPermissions(value={"MODLRZ:function:view"})
	@Action(value = "queryUserAllMap")
	public void queryUserAllMap() {
		//查询所有的用户
		Map<String,String> map = userInInterService.getUserMap();
		String jsonString = JSONUtils.toJson(map);
		WebUtils.webSendJSON(jsonString);
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getUserMessageMap() {
		return userMessageMap;
	}
	public void setUserMessageMap(String userMessageMap) {
		this.userMessageMap = userMessageMap;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
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
	
}
