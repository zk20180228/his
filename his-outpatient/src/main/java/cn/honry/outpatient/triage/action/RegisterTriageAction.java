package cn.honry.outpatient.triage.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.outpatient.transfuse.action.TransfuseAction;
import cn.honry.outpatient.triage.service.RegisterTriageService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**  
 *  
 * @Description：  门诊分诊
 * @Author：liudelin
 * @CreateDate：2015-6-23 下午05:08:16  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/register")
@Namespace(value = "/outpatient/triage")
public class RegisterTriageAction extends ActionSupport {
	
	// 记录异常日志
	private Logger logger = Logger.getLogger(TransfuseAction.class);
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//new挂号信息表实体类
	private RegistrationNow registration;
	
	private RegisterTriageService registerTriageService;
	@Autowired
	@Qualifier(value = "registerTriageService")
	public void setRegisterTriageService(RegisterTriageService registerTriageService) {
		this.registerTriageService = registerTriageService;
	}
	
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	
	/**
	 * 当前登陆科室是否为可分诊分诊台(不是分诊台0 是无维护科室的分诊台1 是有维护科室的分诊台2)
	 */
	private String loginFlag;
	/**
	 * 分页
	 */
	private String page;
	/**
	 * 分页
	 */
	private String rows;
	/**
	 * 医生
	 */
	private String emp;
	/**
	 * 部门
	 */
	private String dep;
	/**
	 * 午别
	 */
	private String midday;
	/**
	 * 患者就诊卡号/病例号/门诊号
	 */
	private String queryNo;
	/**
	 * 患者挂号json
	 */
	private String infoJson;
	/**
	 * 患者挂号json
	 */
	private String registerInfoJson;
	/**
	 * 系统参数是否现登记再进行分诊，1列表显示已分诊，0显示所有  /保存时用来区分是等级还是保存0：登记，1：保存
	 */
	private String flag;
	/**
	 * 当前登陆科室是否不为空  1：不空；0：空
	 */
	private String deptFlag;
	
	public String getEmp() {
		return emp;
	}
	public void setEmp(String emp) {
		this.emp = emp;
	}
	public String getDep() {
		return dep;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public void setDep(String dep) {
		this.dep = dep;
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
	
	public RegistrationNow getRegistration() {
		return registration;
	}
	public void setRegistration(RegistrationNow registration) {
		this.registration = registration;
	}
	
	public String getMidday() {
		return midday;
	}
	public void setMidday(String midday) {
		this.midday = midday;
	}
	public String getQueryNo() {
		return queryNo;
	}
	public void setQueryNo(String queryNo) {
		this.queryNo = queryNo;
	}
	public String getInfoJson() {
		return infoJson;
	}
	public void setInfoJson(String infoJson) {
		this.infoJson = infoJson;
	}
	
	public String getRegisterInfoJson() {
		return registerInfoJson;
	}
	public void setRegisterInfoJson(String registerInfoJson) {
		this.registerInfoJson = registerInfoJson;
	}
	
	public String getDeptFlag() {
		return deptFlag;
	}
	public void setDeptFlag(String deptFlag) {
		this.deptFlag = deptFlag;
	}
	
	public String getLoginFlag() {
		return loginFlag;
	}
	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}
	/**  
	 *  
	 * @Description：  门诊分诊列表(医生)
	 * @Author：liudelin
	 * @CreateDate：2015-6-23 下午05:16:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"MZFZ:function:view"}) 
	@Action(value = "listRegisterTriage", results = { @Result(name = "list", location = "/WEB-INF/pages/register/triage/registerTriageList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listRegisterTriage() {
		try {
			flag = registerTriageService.getmzfzdj().toString();
			loginFlag = registerTriageService.queryDept();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZHSZ_MZFZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZFZ", "门诊护士站_门诊分诊", "2", "0"), e);
		}
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  科室及医生树
	 * @Author：liudelin
	 * @CreateDate：2015-11-27 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "deptTreeTriage", results = { @Result(name = "json", type = "json") })
	public void deptTreeTriage() {
		try {
			List<TreeJson> treeDepar =  registerTriageService.deptTreeTriage();
			String json =JSONUtils.toJson(treeDepar);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZHSZ_MZFZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZFZ", "门诊护士站_门诊分诊", "2", "0"), e);
		}
	}
	
	/** 通过门诊号查询患者挂号信息
	* @Title: queryTriagePatientByClinicCode 
	* @Description: 通过门诊号查询患者挂号信息
	* @author dtl 
	* @date 2016年11月3日
	*/
	@RequiresPermissions(value={"MZFZ:function:query"}) 
	@Action(value="queryTriagePatientByClinicCode")
	public void queryTriagePatientByClinicCode(){
		try {
			RegistrationNow registration = registerTriageService.queryTriagePatientByClinicCode(queryNo);
			String json = JSONUtils.toJson(registration);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZHSZ_MZFZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZFZ", "门诊护士站_门诊分诊", "2", "0"), e);
		}
	}
	/**
	 * @Description：  患者列表
	 * @Author：ldl
	 * @CreateDate：2015-12-1
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZFZ:function:query"}) 
	@Action(value="queryTriagePatient")
	public void queryTriagePatient(){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			List<RegistrationNow> deptContactList = new ArrayList<RegistrationNow>();
			int total = 0;
			String flg = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
			if(StringUtils.isNotBlank(flg)){
				if(registration == null){
					registration =  new RegistrationNow();
				}
				if(StringUtils.isNotBlank(emp)){
					registration.setDoctCode(emp);
				}
				if(StringUtils.isNotBlank(dep)){
					registration.setDeptCode(dep);
				}
				if(StringUtils.isNotBlank(midday)){
					registration.setNoonCode(Integer.valueOf(midday));
				}
				if(StringUtils.isNotBlank(queryNo)){
					registration.setQueryNo(queryNo);
				}
				deptContactList = registerTriageService.getPage(registration, page, rows, flag);
				total = registerTriageService.getTotal(registration, flag);
			}
			map.put("total", total);
			map.put("rows", deptContactList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZHSZ_MZFZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZFZ", "门诊护士站_门诊分诊", "2", "0"), e);
		}
	}
	
	/**保存
	 * @Description: 保存
	 * @Author: dutianliang
	 * @CreateDate:2016年4月7日
	 * @Version: V 1.0
	 */
	@RequiresPermissions(value={"MZFZ:function:save"}) 
	@Action(value="saveTriagePatient")
	public void saveTriagePatient(){
		String res = "error";
		try {
			if("1".equals(flag)){
				registerTriageService.saveTriagePatient(registration);
				res = "success";
			}
			if("0".equals(flag)){
				registerTriageService.saveTriagePatient(registerInfoJson);
				res = "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZHSZ_MZFZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZFZ", "门诊护士站_门诊分诊", "2", "0"), e);
		}
		WebUtils.webSendString(res);
	}
	
	
	/**确定当前登陆科室是否为可分诊分诊台
	 * @Description: 确定当前登陆科室是否为可分诊分诊台
	 * @Author: dutianliang
	 * @CreateDate: 2016年5月13日
	 * @Version: V 1.0
	 */
	@Action(value="queryDept")
	public void queryDept(){
		try {
			String flag = registerTriageService.queryDept();
			WebUtils.webSendString(flag);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZHSZ_MZFZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZFZ", "门诊护士站_门诊分诊", "2", "0"), e);
		}
	}
}
