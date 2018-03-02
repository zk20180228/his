package cn.honry.portal.login.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.business.Json;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.SysRole;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.loginLog.service.LoginLogService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inner.system.role.service.RoleInInterService;
import cn.honry.inner.system.user.service.UserInInterService;
import cn.honry.portal.login.service.UserLoginService;
import cn.honry.portal.main.vo.DeptSubclassVO;
import cn.honry.portal.main.vo.RoleSubclassVO;
import cn.honry.portal.until.AES;
import cn.honry.shiro.CaptchaUsernamePasswordToken;
import cn.honry.shiro.UsernamePasswordRealm;
import cn.honry.sms.clientN.Client;
import cn.honry.utils.Constants;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.MD5;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.UUIDGenerator;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author 孙硕
 * @version 创建时间：2011-5-23 上午11:44:14 类说明
 */
@Controller
@ParentPackage("struts-default")
@Scope("prototype")
@Actions({ @Action(value = "/userLoginAction",results = { @Result(name = "login", type="redirect", location = "/login.jsp") })})
@SuppressWarnings({ "all" })
public class UserLoginAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier(value = "userLoginService")
	private UserLoginService userLoginService;
	
	@Resource
	private RoleInInterService roleInInterService;
	@Resource
	private DeptInInterService deptInInterService;
	@Resource
	private UserInInterService userInInterService;
	@Resource
	private ParameterInnerService parameterInnerService;
	
	@Resource
	private EmployeeInInterService employeeInInterService;
	@Autowired
	@Qualifier(value = "loginLogService")
	private LoginLogService loginLogService;//mongodb版登录日志服务类
	public void setLoginLogService(LoginLogService loginLogService) {
		this.loginLogService = loginLogService;
	}
	@Resource
	private RedisUtil redis;
	@Resource
	private UsernamePasswordRealm usernamePasswordRealm;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private String txtAccount;
	private String txtPassword;
	private boolean rememberMe;
	private String captcha;
	private String userMacAddr;
	private String userKey;
	private String lRole;
	private String lDept;
	private String ssoToken;
	private String currentUserAccount;
//	@Resource//hzq-->引用redis保存登录用的token
//	private RedisUtil redis;
	private String mobile;//手机号
	private String msgcode;//短信验证码
	private long v;
	public long getV() {
		return v;
	}
	public void setV(long v) {
		this.v = v;
	}
	public void getUserInfo() {
		Subject subject=SecurityUtils.getSubject();
		if(subject.isAuthenticated()){
			subject.logout();
		}
		CaptchaUsernamePasswordToken token=new CaptchaUsernamePasswordToken();
		token.setUsername(txtAccount);
		token.setPassword(new MD5().MD5Encode(txtPassword).toCharArray());
		token.setRememberMe(rememberMe);
		Json json=new Json();
		json.setTitle("登录提示");
		try{
			subject.login(token);
			SysRole role = roleInInterService.getRoleById(lRole);
			SysDepartment dept = deptInInterService.getDeptById(lDept);
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
				//根据所登陆的用户查询该用户所属部门
				User currentUser=ShiroSessionUtils.getCurrentUserFromShiroSession();
				if(currentUser!=null){
					SysDepartment department = employeeInInterService.findEmployeesByUserId(currentUser.getId());
					SysEmployee employee = employeeInInterService.findEmpByUserId(currentUser.getId());//员工
					if(department!=null){
						subject.getSession().setAttribute(SessionUtils.SESSIONDEPARTMENT, department);
						subject.getSession().setAttribute("username",currentUser.getName());
						//20160323zpty加入根据登录用户查询该用户对应的员工
						subject.getSession().setAttribute(SessionUtils.SESSIONEMPLOYEE, employee);
					}
					if(!txtAccount.equals("admin")){
						if(employee==null||employee.getId()==null||employee.getId().equals("")){
							json.setStatus(false);	
							json.setMessage("该用户没有相应的员工信息，请重新登录");
						}
					}
				}
				
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String loginDate = sf.format(new Date());
				Document doc = new Document();
				doc.put("LOGIN_USERID",currentUser.getId());//获取用户id
				doc.put("LOGIN_IP",request.getRemoteAddr());//获取用户的ip地址
				doc.put("LOGIN_HTTP",request.getHeaderNames().nextElement().toString());//获取登录客户端类型
				doc.put("LOGIN_SESSION",request.getSession().getId());//获取登录客户的sessionid
				doc.put("LOGIN_TIME",loginDate);//登录时间
				//hedong 将用户登录信息保存至mongodb数据库
				loginLogService.insertLoginByMongo(doc);
//				//生成token 用于子系统登录验证
//				long timeout = SecurityUtils.getSubject().getSession().getTimeout();
//				String uuid = UUIDGenerator.getUUID();
//				System.err.println("#####################");
//				System.err.println("uuid:"+uuid);
//				System.err.println("#####################");
//				redis.hset("sso",txtAccount,uuid);
				
				/**
				 * hzq 向redis中保存用户账户和token信息
				 * eg: sso 52094 9f85c798e1a248d8b40811e1b7e087d9
				 */
//				redis.hset("sso", currentUser.getAccount(), UUID.randomUUID().toString().replace("-", ""));
//				redis.expire(currentUser.getAccount(), 36000);
				
			}
		}catch (UnknownSessionException use) {
			json.setStatus(false);	
			json.setMessage(Constants.UNKNOWN_SESSION_EXCEPTION);
		}
		catch(UnknownAccountException ex){
			json.setStatus(false);	
			json.setMessage(Constants.UNKNOWN_ACCOUNT_EXCEPTION);
		}
		catch (IncorrectCredentialsException ice) {
			json.setStatus(false);	
			json.setMessage(Constants.INCORRECT_CREDENTIALS_EXCEPTION);
		} 
		catch (LockedAccountException lae) {
			json.setStatus(false);	
			json.setMessage(Constants.LOCKED_ACCOUNT_EXCEPTION);
		}
		catch (AuthenticationException ae) {
			json.setStatus(false);	
			json.setMessage(Constants.AUTHENTICATION_EXCEPTION);
		} 
		catch(Exception e){
			json.setStatus(false);	
			json.setMessage(Constants.UNKNOWN_EXCEPTION);
		}
		Gson gson = new Gson();  
		String jsonStr = gson.toJson(json);  
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(jsonStr);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return;
	}
	
	/**
	 * 登出
	 * @return
	 */
	public String loginout(){
		redis.hDel("sso", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		SecurityUtils.getSubject().logout();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
		v = System.currentTimeMillis();
		return "login";
	}
	
	public void getDeptAndRoleInfo() {
		Map<String, String> map = new HashMap<String, String>();
		//判断redis中是否存在该帐号密码错误信息
		if(redis.exists("hias"+txtAccount)){
			//密码错误次数
			int num = (int) redis.get("hias"+txtAccount);
			if(num<3){
				getDeptAndRoleInfoByAccount(txtAccount);
			}else{//超过三次账号锁定
				Long second = redis.ttl(("hias"+txtAccount).getBytes());
				long days = second / 86400;            //转换天数
		        second = second % 86400;            //剩余秒数
		        long hours = second / 3600;            //转换小时
		        second = second % 3600;                //剩余秒数
		        long minutes = second /60;            //转换分钟
		        second = second % 60;                //剩余秒数
		        if(days>0){
					map.put("message", "账户已锁定，请"+days + "天" + hours + "小时" + minutes + "分" + second + "秒"+"后再试");
		        }else{
					map.put("message", "账户已锁定，请"+hours + "小时" + minutes + "分" + second + "秒"+"后再试");
		        }
				map.put("status", "0");
				String json = JSONUtils.toJson(map);
				WebUtils.webSendJSON(json);
			}
		}else{
			getDeptAndRoleInfoByAccount(txtAccount);
		}
		
	}
	public void getDeptAndRoleInfoByAccount(String txtAccount){
		Map<String, String> map = new HashMap<String, String>();
		Subject subject=SecurityUtils.getSubject();
		User user = userInInterService.getByAccount(txtAccount);
		String jsonString;
		if (user != null) {
			if(StringUtils.isBlank(txtPassword)){
				map.put("status", "1");
				List<SysDepartment> departments = deptInInterService.queryDepts(user.getId());
				if(departments != null && departments.size() > 0){
					List<DeptSubclassVO> dList = new ArrayList<DeptSubclassVO>();
					DeptSubclassVO vo = null;
					for(SysDepartment dept : departments){
						vo = new DeptSubclassVO();
						vo.setId(dept.getId());
						vo.setName(dept.getDeptName());
						vo.setIcon("dept-"+dept.getDeptType());
						dList.add(vo);
					}
					jsonString = JSONUtils.toJson(dList);
				}else {
					jsonString = "";
				}
				map.put("depts", jsonString);
				List<SysRole> roles = roleInInterService.findRoleByUserId(user.getId());
				if(roles != null){
					List<RoleSubclassVO> rList = new ArrayList<RoleSubclassVO>();
					RoleSubclassVO vo = null;
					for(SysRole sysRole : roles){
						vo = new RoleSubclassVO();
						vo.setId(sysRole.getId());
						vo.setName(sysRole.getName());
						vo.setIcon(StringUtils.isNotBlank(sysRole.getIcon())?sysRole.getIcon():"role-O");
						vo.setAdmin("superManager".equals(sysRole.getAlias())?true:false);
						rList.add(vo);
					}
					jsonString = JSONUtils.toJson(rList);
				}else {
					jsonString = "";
				}
				map.put("roles", jsonString);
			}else {
				String passWord = new MD5().MD5Encode(txtPassword);
				if(passWord.equals(user.getPassword())){//密码正确
					map.put("status", "1");
					//登录成功删除redis中的数据
					if(redis.exists("hias"+user.getAccount())){
						redis.remove("hias"+user.getAccount());
					}
					
				}else {//密码错误
					if(redis.exists("hias"+user.getAccount())){
						int num = (int) redis.get("hias"+user.getAccount());
						if((num+1)<3){
							redis.set("hias"+user.getAccount(),num+1);
							//未超过三次时过期时间为一小时
							redis.expire("hias"+user.getAccount(), 3600);
							map.put("status", "0");
							map.put("message", "密码错误,您还有"+(3-(num+1))+"次机会输入!");
						}else{
							redis.set("hias"+user.getAccount(),num+1);
							//超过三次时过期时间为五小时
							redis.expire("hias"+user.getAccount(), 18000);
							map.put("status", "0");
							map.put("message", "账户已锁定，请五小时后再试");
						}
						
					}else{
						redis.set("hias"+user.getAccount(),1);
						redis.expire("hias"+user.getAccount(), 3600);
						map.put("status", "0");
						map.put("message", "密码错误,您还有2次机会输入!");
					}
				}
			}
		}else {
			map.put("status", "0");
			map.put("message", "您输入的用户名不存在！");
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * <p>生成token </p>
	 * @Author: mrb
	 * @CreateDate: 2017年10月19日 下午2:14:31 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年10月19日 下午2:14:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	public void thirdAuthen(){
		String token = "error";
		User user = userInInterService.getByAccount(txtAccount);
		if(StringUtils.isNotBlank(txtPassword)){
			String passWord = new MD5().MD5Encode(txtPassword);
			if(passWord.equals(user.getPassword())){
				token = "success";
			}
		}
		WebUtils.webSendJSON(JSONUtils.toJson(token));
	}
	/**  
	 * <p>token验证 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年10月19日 下午2:54:02 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年10月19日 下午2:54:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	public void checkToken(){
		String code = "error";
		if(StringUtils.isNotBlank(currentUserAccount)){
			User user = userInInterService.getByAccount(currentUserAccount);
			if(user!=null&&StringUtils.isNotBlank(user.getAccount())){
				Object object = redis.hget("sso",currentUserAccount);//服务器中的token
				if(StringUtils.isNotBlank(ssoToken)&&ssoToken.equals(object)){//判断服务器的token和传来的token是否相等
					code = "success";
				}
			}
			
		}
		WebUtils.webSendJSON(JSONUtils.toJson(code));
	}
	public void outOrIn(){
		Map<String,String> map = new HashMap<String, String>();
		String chargeINorOUT = HisParameters.chargeINorOUT(request);
		if("out".equals(chargeINorOUT)){
			String onoff = (String)redis.hget("sys-parameters-hias_sms_on-off", HisParameters.CURRENTHOSPITALCODE);
			if(StringUtils.isNotBlank(onoff)){
				if("1".equals(onoff)){//关闭
					map.put("status", "in");
				}else{
					User currentUser=userInInterService.getByAccount(txtAccount);
					SysEmployee employee = employeeInInterService.findEmpByUserId(currentUser.getId());//员工
					map.put("mobile", employee.getMobile());
					map.put("status", chargeINorOUT);
				}
			}else{
				String parameterByCode = parameterInnerService.getParameterByCode("hias_sms_on-off");
				if(StringUtils.isNotBlank(parameterByCode)){
					if("1".equals(parameterByCode)){//关闭
						map.put("status", "in");
					}else{
						User currentUser=userInInterService.getByAccount(txtAccount);
						SysEmployee employee = employeeInInterService.findEmpByUserId(currentUser.getId());//员工
						map.put("mobile", employee.getMobile());
						map.put("status", chargeINorOUT);
					}
				}else{
					User currentUser=userInInterService.getByAccount(txtAccount);
					SysEmployee employee = employeeInInterService.findEmpByUserId(currentUser.getId());//员工
					map.put("mobile", employee.getMobile());
					map.put("status", chargeINorOUT);
				}
			}
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**  
	 * <p>生成短信验证码 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月2日 下午7:29:24 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月2日 下午7:29:24 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	public void createAuthCode(){
		Map<String,String> map = new HashMap<String, String>();
		Random random = new Random();
		String code = "";
		for (int i = 0; i < 6; i++) {
			int nextInt = random.nextInt(10);
			code += nextInt;
		}
		redis.set("HIAS"+request.getRequestedSessionId()+mobile, code);
		redis.expire("HIAS"+request.getRequestedSessionId()+mobile, 360);
		System.err.println("code:"+code);
		Client client = Client.getInstance();
		List<String > mobiles = new ArrayList<String>();
		mobiles.add(mobile);
		String msg = client.send(mobiles, "您正在登录HIAS平台，短信验证码为:"+code, 5, HisParameters.SMSUUID, HisParameters.SMSUSER, HisParameters.SMSPASSWORD);
		String regex = "<code>(.*)</code>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(msg);
		if(matcher.find()){
			String group = matcher.group(1);
			if("1".equals(group)){
				map.put("resCode", "success");
				map.put("resMsg", "验证码已发送至手机,请注意查看...");
			}else{
				map.put("resCode", "error");
				map.put("resMsg", "验证码发送失败,请重新发送...");
			}
		}else{
			map.put("resCode", msg);
			map.put("resMsg", msg);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**  
	 * <p>校验短信验证码 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月2日 下午7:29:40 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月2日 下午7:29:40 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	public void checkAuthCode(){
		Map<String,String> map = new HashMap<String, String>();
		String code = (String) redis.get("HIAS"+request.getRequestedSessionId()+mobile);
		System.err.println("checkcode:"+code);
		if(StringUtils.isNotBlank(msgcode)&&msgcode.equals(code)){
			map.put("resCode", "success");
		}else{
			map.put("resCode", "error");
		}
		redis.remove("HIAS"+request.getRequestedSessionId()+mobile);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	public String getTxtAccount() {
		return txtAccount;
	}

	public void setTxtAccount(String txtAccount) {
		this.txtAccount = txtAccount;
	}

	public String getTxtPassword() {
		return txtPassword;
	}

	public void setTxtPassword(String txtPassword) {
		this.txtPassword = txtPassword;
	}


	public boolean isRememberMe() {
		return rememberMe;
	}


	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	public String getlRole() {
		return lRole;
	}

	public void setlRole(String lRole) {
		this.lRole = lRole;
	}

	public String getlDept() {
		return lDept;
	}

	public void setlDept(String lDept) {
		this.lDept = lDept;
	}

	public String getSsoToken() {
		return ssoToken;
	}

	public void setSsoToken(String ssoToken) {
		this.ssoToken = ssoToken;
	}

	public String getCurrentUserAccount() {
		return currentUserAccount;
	}

	public void setCurrentUserAccount(String currentUserAccount) {
		this.currentUserAccount = currentUserAccount;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMsgcode() {
		return msgcode;
	}

	public void setMsgcode(String msgcode) {
		this.msgcode = msgcode;
	}

}
