package cn.honry.portal.main.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.business.Json;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.SysRole;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.dao.EmployeeInInterDAO;
import cn.honry.inner.system.role.service.RoleInInterService;
import cn.honry.portal.main.vo.DeptParentVO;
import cn.honry.portal.main.vo.DeptSubclassVO;
import cn.honry.portal.main.vo.RoleParentVO;
import cn.honry.portal.main.vo.RoleSubclassVO;
import cn.honry.shiro.UsernamePasswordRealm;
import cn.honry.utils.Constants;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author sunshuo
 * @version 2011-4-23 11:44:14
 */
@Controller
@ParentPackage("global")
@Scope("prototype")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Actions({ 
		@Action(value = ("/mainAction"), 
			results = { //
				@Result(name="timeoutURL",location="/WEB-INF/pages/main/jump.jsp"),     //超时页面
				@Result(name = "loginURL", location = "/login.jsp"),   //登录页
				@Result(name="creatingURL",location="/WEB-INF/pages/main/create.jsp"),     //建设中
				@Result(name = "firstURL", location = "/WEB-INF/pages/main/first.jsp"),   //首页
				@Result(name = "queryResult", location = "/WEB-INF/pages/main/main.jsp"),   //框架
				@Result(name = "platform", location = "/WEB-INF/pages/main/platform.jsp"),   //框架
				@Result(name = "roledept", location = "/WEB-INF/pages/main/roledept.jsp"),   //框架
			}
		)})
@SuppressWarnings({ "all" })
public class MainAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7794147270348287489L;
	
	public static final String SESSION_USER = "user";
	
	private String menuJson="";

	private String rnd="";
	private long userId;
	private String userName;
	private long menuId;
	private boolean canRead;
	private boolean canEdit;
	private boolean canCheck;
	private boolean canAdmin;
	@Resource
	private RedisUtil redis;
	private String today;
	
	private long loginStartTime=ShiroSessionUtils.getCurrentUserLoginStartTime();
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	@Resource
	private RoleInInterService roleInInterService;
	@Resource
	private DeptInInterService deptInInterService;
	
	@Resource
	private UsernamePasswordRealm usernamePasswordRealm;
	@Resource
	private RedisUtil redisUtil;
	@Resource
	private EmployeeInInterDAO employeeInInterDAO;
	private String spreadStat;
	
	public long getLoginStartTime() {
		return loginStartTime;
	}

	public void setLoginStartTime(long loginStartTime) {
		this.loginStartTime = loginStartTime;
	}

	public void setMenuJson(String menuJson) {
		this.menuJson = menuJson;
	}

	public String getMenuJson() {
		return menuJson;
	}
	
	public String getMenuString(){
		return null;
	}

	//收缩展开
	public void rememberSpread(){
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String stat = request.getParameter("stat");		
		redisUtil.hset("honry_user_"+user.getAccount(),"stat",stat);//这里加上'honry_user_'前缀是为了在缓存管理界面对账户进行统一管理
	}
	//切换字体
	public void changeFontSize(){
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String changeFontSize = request.getParameter("changeFontSize");		
		redisUtil.hset("honry_user_"+user.getAccount(), "font", changeFontSize);		
		//redisUtil.set(fontkey,changeFontSize,(long) -1);
		WebUtils.getSession().setAttribute("fontSize",changeFontSize);
	}
	//换肤
	public void changeSkin(){
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String easyuiThemeName = request.getParameter("easyuiThemeName");		
		redisUtil.hset("honry_user_"+user.getAccount(),"themes",easyuiThemeName);
		WebUtils.getSession().setAttribute("themes",easyuiThemeName);
	}
	
	public String getMenu(){
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String userAccount = "honry_user_"+user.getAccount();
		if(!redisUtil.exists(userAccount)){			
			redisUtil.hset(userAccount,"themes","0");
			redisUtil.hset(userAccount, "font", "0");
			redisUtil.hset(userAccount, "stat", "0");
		}		
		WebUtils.getSession().setAttribute("themes",redisUtil.hget(userAccount,"themes"));
		WebUtils.getSession().setAttribute("fontSize",redisUtil.hget(userAccount,"font"));
		spreadStat = (String)redisUtil.hget(userAccount,"stat");
		try {
			SysRole role = ShiroSessionUtils.getCurrentUserLoginRoleFromShiroSession();
			if(role!=null){
				if("superManager".equals(role.getAlias())){
					return "queryResult";	
				}
				SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
				if(dept!=null){
					return "queryResult";	
				}else{
					SecurityUtils.getSubject().logout();
					return "login";
				}
			}else{
				SecurityUtils.getSubject().logout();
				return "login";
			}
		} catch (Exception e) {
			SecurityUtils.getSubject().logout();
			return "login";
		}
	}
	
	public void getLogInf(){
		String roleId = request.getParameter("roleId");
		String deptId = request.getParameter("deptId");
		String login = request.getParameter("login");
		Json json=new Json();
		try{
			SysRole role = roleInInterService.getRoleById(roleId);
			SysDepartment dept = deptInInterService.getDeptById(deptId);
			if(role==null){
				json.setStatus(false);	
	            json.setMessage(Constants.ROLE_IS_NULL);
			}else if(role.getDel_flg()==1){
				json.setStatus(false);	
	            json.setMessage(Constants.ROLE_IS_DEL);
			}else if(role.getStop_flg()==1){
				json.setStatus(false);	
	            json.setMessage(Constants.ROLE_IS_STOP);
			}else if(!"superManager".equals(role.getAlias())&&dept==null){
				json.setStatus(false);	
	            json.setMessage(Constants.DEPT_IS_NULL);
			}else if(!"superManager".equals(role.getAlias())&&dept.getDel_flg()==1||"superManager".equals(role.getAlias())&&dept!=null&&dept.getDel_flg()==1){
				json.setStatus(false);	
	            json.setMessage(Constants.DEPT_IS_DEL);
			}else if(!"superManager".equals(role.getAlias())&&dept.getStop_flg()==1||"superManager".equals(role.getAlias())&&dept!=null&&dept.getStop_flg()==1){
				json.setStatus(false);	
	            json.setMessage(Constants.DEPT_IS_STOP);
			}else{
				SecurityUtils.getSubject().getSession().setAttribute(SessionUtils.SESSIONLOGINROLE,role);
				if(dept!=null){
					SecurityUtils.getSubject().getSession().setAttribute(SessionUtils.SESSIONLOGINDEPARTMENT,dept);
					if(StringUtils.isNotBlank(dept.getDeptType())&&dept.getDeptType().equals("N")){
						SecurityUtils.getSubject().getSession().setAttribute(SessionUtils.SESSIONLOGINNURSINGSTATION,dept);
					}else{
						SysDepartment nursDept = deptInInterService.getNursingStationByLoginDeptId(dept.getId());
						SecurityUtils.getSubject().getSession().setAttribute(SessionUtils.SESSIONLOGINNURSINGSTATION,nursDept);
					}
				}else{
					SecurityUtils.getSubject().getSession().setAttribute(SessionUtils.SESSIONLOGINDEPARTMENT,null);
					SecurityUtils.getSubject().getSession().setAttribute(SessionUtils.SESSIONLOGINNURSINGSTATION,null);
				}
				usernamePasswordRealm.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipal().toString());
				SecurityUtils.getSubject().isPermitted("栏目别名:function:权限名称");
				json.setStatus(true);
			}
		}catch(Exception e){
			json.setStatus(false);	
            json.setMessage(Constants.UNKNOWN_EXCEPTION);
		}
		Gson gson = new Gson();  
		String jsonStr = gson.toJson(json);  
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			//System.out.println(jsonStr);
			out.write(jsonStr);
		}catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return;
	}
	
	
	/**
	 * 选择平台
	 * @return
	 */
	public String platform(){
		return "platform";
	}
	
	/**
	 * 选择角色和部门
	 * @return
	 */
	public String roleDeptSelect(){
//		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
//		if("honry".equals(user.getAccount())){
//			boolean isHaveDept = deptInInterService.isHaveDept(user.getAccount());
//			if(!isHaveDept){
//				usernamePasswordRealm.clearAllCachedAuthorizationInfo();
//				SysRole role = new SysRole();
//				role.setAlias("superManager");
//				SecurityUtils.getSubject().getSession().setAttribute(SessionUtils.SESSIONLOGINROLE,role);
//				return "queryResult";
//			}
//		}
//		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
//		if("admin".equals(user.getAccount())){
//			usernamePasswordRealm.clearAllCachedAuthorizationInfo();
//			SecurityUtils.getSubject().isPermitted("栏目别名:function:权限名称");
//			SysRole role = new SysRole();
//			role.setAlias("CJGLY");
//			SecurityUtils.getSubject().getSession().setAttribute(SessionUtils.SESSIONLOGINROLE,role);
//			SysDepartment dept = new SysDepartment();
//			dept.setId("");
//			SecurityUtils.getSubject().getSession().setAttribute(SessionUtils.SESSIONLOGINDEPARTMENT,dept);
//			return "queryResult";
//		}
		return "roledept";
	}
	
	@Action(value = "findRolebyLoginUser", results = { @Result(name = "json", type = "json") })
	public void findRolebyLoginUser() {	
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		List<SysRole> roleList = roleInInterService.findRoleByUserId(user.getId());
		List<RoleParentVO> roleParentVOList = new ArrayList<RoleParentVO>();
		if(roleList!=null&&roleList.size()>0){
			RoleParentVO roleParentVO = null;
			List<RoleSubclassVO> roleSubclassVOList = null;
			RoleSubclassVO roleSubclassVO = null;
			for (int i = 0; i < roleList.size(); i+=5) {
				roleParentVO = new RoleParentVO();
				roleParentVO.setId("role"+i);
				if(i==0){
					roleParentVO.setAttribute("");
				}else{
					roleParentVO.setAttribute("display:none");
				}
				roleSubclassVOList = new ArrayList<RoleSubclassVO>();
				for(int j = 0; j < 5; j++){
					roleSubclassVO = new RoleSubclassVO();
					if(i+j+1<=roleList.size()){
						roleSubclassVO.setId(roleList.get(i+j).getId());
						roleSubclassVO.setName(roleList.get(i+j).getName());
						String sIcon=roleList.get(i+j).getIcon();
						if(sIcon!=null && !sIcon.equals("")){
							roleSubclassVO.setIcon(sIcon);
						}else{
							roleSubclassVO.setIcon("role-O");
						}
						if("superManager".equals(roleList.get(i+j).getAlias())){
							roleSubclassVO.setAdmin(true);
						}
						roleSubclassVO.setParameter("cursor:pointer;");
					}else{
						roleSubclassVO.setId("");
						roleSubclassVO.setName("");
						roleSubclassVO.setIcon("role-O");
						roleSubclassVO.setParameter("visibility:hidden");
					}
					roleSubclassVOList.add(roleSubclassVO);
				}
				roleParentVO.setRoleSubclassVO(roleSubclassVOList);
				roleParentVOList.add(roleParentVO);
			}
		}
		Gson gson = new GsonBuilder()
		.registerTypeAdapterFactory(HibernateCascade.FACTORY)
		.create();
		String json = gson.toJson(roleParentVOList);
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@Action(value = "findDeptbyRole", results = { @Result(name = "json", type = "json") })
	public void findDeptbyRole() {	
		List<SysDepartment> deptList = deptInInterService.queryDepts(ShiroSessionUtils.getCurrentUserFromShiroSession().getId());
		List<DeptParentVO> deptParentVOList = new ArrayList<DeptParentVO>();
		if(deptList!=null&&deptList.size()>0){
			DeptParentVO deptParentVO = null;
			List<DeptSubclassVO> deptSubclassVOList = null;
			DeptSubclassVO deptSubclassVO = null;
			for (int i = 0; i < deptList.size(); i+=5) {
				deptParentVO = new DeptParentVO();
				deptParentVO.setId("dept"+i);
				if(i==0){
					deptParentVO.setAttribute("");
				}else{
					deptParentVO.setAttribute("display:none");
				}
				deptSubclassVOList = new ArrayList<DeptSubclassVO>();
				for(int j = 0; j < 5; j++){
					deptSubclassVO = new DeptSubclassVO();
					if(i+j+1<=deptList.size()){
						deptSubclassVO.setId(deptList.get(i+j).getId());
						deptSubclassVO.setName(deptList.get(i+j).getDeptName());
						String sType=deptList.get(i+j).getDeptType();
						if(sType==null || sType.equals("")) sType="O";
						deptSubclassVO.setIcon("dept-"+sType);
						deptSubclassVO.setParameter("cursor:pointer;");
					}else{
						deptSubclassVO.setId("");
						deptSubclassVO.setName("");
						deptSubclassVO.setIcon("dept-O");
						deptSubclassVO.setParameter("visibility:hidden");
					}
					deptSubclassVOList.add(deptSubclassVO);
				}
				deptParentVO.setDeptSubclassVO(deptSubclassVOList);
				deptParentVOList.add(deptParentVO);
			}
		}
		Gson gson = new GsonBuilder()
		.registerTypeAdapterFactory(HibernateCascade.FACTORY)
		.create();
		String json = gson.toJson(deptParentVOList);
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public String returnLoginURL(){
		if (getSessionUser()!=null) {
			WebUtils.getSession().setAttribute(WebUtils.SESSION_USER,null);
			WebUtils.getSession().setAttribute(WebUtils.SESSION_PLATFORM,null);
		}
		return "loginURL";
	}
	
	public String returnCreatingURL(){
		return "creatingURL";
	}
	
	public String returnFirstURL(){
		return "firstURL";
	}

	public void refreshSession(){
		User user = getSessionUser();
		if (user != null) {
			WebUtils.getSession().setAttribute(WebUtils.SESSION_USER,user);
			WebUtils.getSession().setAttribute(WebUtils.SESSION_PLATFORM,"3");
		}
	}

	public static User getSessionUser() {
		User user = (User) ActionContext.getContext().getSession().get(SESSION_USER);
		return user != null ? user : null;
	}
	
	public String getRnd() {
		return rnd;
	}

	public void setRnd(String rnd) {
		this.rnd = rnd;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getMenuId() {
		return menuId;
	}

	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}

	public boolean isCanRead() {
		return canRead;
	}

	public void setCanRead(boolean canRead) {
		this.canRead = canRead;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public boolean isCanCheck() {
		return canCheck;
	}

	public void setCanCheck(boolean canCheck) {
		this.canCheck = canCheck;
	}

	public boolean isCanAdmin() {
		return canAdmin;
	}

	public void setCanAdmin(boolean canAdmin) {
		this.canAdmin = canAdmin;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}

	public String getSpreadStat() {
		return spreadStat;
	}

	public void setSpreadStat(String spreadStat) {
		this.spreadStat = spreadStat;
	}


}
