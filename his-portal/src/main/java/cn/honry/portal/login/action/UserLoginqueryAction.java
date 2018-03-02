package cn.honry.portal.login.action;


import java.io.PrintWriter;
import java.util.List;

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

import cn.honry.base.bean.model.UserLogin;
import cn.honry.portal.login.service.UserLoginService;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 *  
 * @Description： 登录日志表
 * @Author：wujiao
 * @CreateDate：2015-8-17 下午05:12:16  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/sys")
public class UserLoginqueryAction extends ActionSupport implements ModelDriven<UserLogin> {

	
	private static final long serialVersionUID = 1L;
	private  UserLogin userLogin=new UserLogin();
	
	private UserLoginService userLoginService;
	private String page;// 分页
	private String rows;
	private List<UserLogin> userLoginlList;// 员工列表

	@Autowired
    @Qualifier(value = "userLoginService")
	public void setUserLoginService(UserLoginService userLoginService) {
		this.userLoginService = userLoginService;
	}
   private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	@Override
	public UserLogin getModel() {
		return userLogin;
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

	/**  
	 *  
	 * @Description：  登录日志列表
	 * @Author：wujiao
	 * @CreateDate：2015-8-17 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
//@RequiresPermissions(value={"a:function:view","b:function:view"},logical=Logical.OR) 多栏目同权限查询
	@RequiresPermissions(value={"DLRZ:function:view"}) 
	@Action(value = "listUserLogin", results = { @Result(name = "list", location = "/WEB-INF/pages/sys/user/userLogin.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listUserLogin() {
		//User user = WebUtils.getSessionUser();
		return "list";
	}
	/**  
	 *  
	 * @Description：  查询登录日志信息
	 * @Author:wujiao
	 * @CreateDate：2015-8-17 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"DLRZ:function:query"}) 
	@Action(value = "queryUserLogin", results = { @Result(name = "json", type = "json") })
	public void queryUserLogin() {
		userLoginlList = userLoginService.getPage(page,rows,userLogin);
		int total = userLoginService.getTotal(userLogin);
		Gson gson = new GsonBuilder()
		.registerTypeAdapterFactory(HibernateCascade.FACTORY)
		.setDateFormat("yyyy-MM-dd HH:mm:ss")  
		.create();
		String json = gson.toJson(userLoginlList);
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write("{\"total\":" + total + ",\"rows\":" + json + "}");
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
			
		}
	}
	/**  
	 *  
	 * @Description：  登录日志信息跳转浏览页面
	 * @Author：wujiao
	 * @CreateDate：2015-6-3 下午05:11:17  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-3 下午05:11:17  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"DLRZ:function:view"}) 
	@Action(value = "viewUserLogin", results = { @Result(name = "view", location = "/WEB-INF/pages/sys/user/userLoginView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewUserLogin() {
		String uid = ServletActionContext.getRequest().getParameter("uid");
		userLogin = userLoginService.get(uid);
		ServletActionContext.getRequest().setAttribute("userLogin", userLogin);
		return "view";
	}
}
