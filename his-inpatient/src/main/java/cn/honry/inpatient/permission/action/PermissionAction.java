package cn.honry.inpatient.permission.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientPermission;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inpatient.info.service.InpatientInfoService;
import cn.honry.inpatient.permission.service.PermissionService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**   
*  
* @className： permission
* @description：医嘱授权action
* @author：tcj
* @createDate：2015-12-21 上午11:50  
* @modifyRmk：  
* @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/permission")
public class PermissionAction extends ActionSupport implements ModelDriven<InpatientPermission> {
	
	private Logger logger=Logger.getLogger(PermissionAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}



	/**
	 * 登录科室类别标识，1为门诊，2为住院，3为其他
	 */
	
	private String state;
	/**
	 * 住院流水号
	 */
	private String inpatientNo;
	/**
	 * 医嘱授权表主键ID
	 */
	private String manId;
	/**
	 * 需要删除的医嘱授权记录的主键ID的list拼接字符串
	 */
	private String ids;
	/**
	 * 当前登录科室ID
	 */
	private String deptId;
	/**
	 * 6为病历号
	 */
	private String mid;
	/**
	 * 所选择的科室的Id
	 */
	private String departmentId;
	/**
	 * 查询列表时的住院流水号
	 */
	private String inno;
	/**
	 * 注入patinentInnerService公共接口
	 */
	private PatinentInnerService patinentInnerService;
	@Autowired
	@Qualifier(value = "patinentInnerService")
	public PatinentInnerService getPatinentInnerService() {
		return patinentInnerService;
	}
	public void setPatinentInnerService(PatinentInnerService patinentInnerService) {
		this.patinentInnerService = patinentInnerService;
	}
	
	 /***
	 * 人员service实现层
	 */
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	
	 /***
	 * 科室service实现层
	 */
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	
	private String q;
	
	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getInno() {
		return inno;
	}
	public void setInno(String inno) {
		this.inno = inno;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getManId() {
		return manId;
	}
	public void setManId(String manId) {
		this.manId = manId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	@Autowired
	@Qualifier(value = "permissionService")
	private PermissionService permissionService;
	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	@Autowired
	@Qualifier(value = "inpatientInfoService")
	private InpatientInfoService inpatientInfoService;
	
	public void setInpatientInfoService(InpatientInfoService inpatientInfoService) {
		this.inpatientInfoService = inpatientInfoService;
	}

	private static final long serialVersionUID = 1L;
	private InpatientPermission inpatientPermission=new InpatientPermission();
	@Override
	public InpatientPermission getModel() {
		return inpatientPermission;
	}
	
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**  
	 * @Description：  获取医嘱授权list页面
	 * @Author：TCJ
	 * @CreateDate：2015-12-21 下午 15：40  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"YZSQGL:function:view"})
	@Action(value = "listPermission", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/permission/permissionList.jsp"),@Result(name = "view", location = "/WEB-INF/pages/inpatient/permission/noSelectDept.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listPermission(){
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(null==dept){
			return "view";
		}else{
			deptId=dept.getDeptCode();
			return "list";
		}
		
	}
	/**  
	 * @Description：  跳转添加、修改页面
	 * @Author：TCJ
	 * @CreateDate：2015-12-1 下午 15：40  
	 * @ModifyRmk：  
	 * @version 1.0
	 */

	@Action(value = "listAddUpdate", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/permission/permissionViewList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listAddUpdate(){
		
		try {
			SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			
			//查询科室类型
			state=permissionService.queryDeptType(dept.getId());
		} catch (Exception e) {
			logger.error("ZTYSZ_YZSQGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZTYSZ_YZSQGL", "住院医生站_医嘱授权管理", "2", "0"), e);
		}
		return "list";
	}
	/**  
	 * @Description：通过病历号 查询医嘱授权历史记录
	 * @Author：TCJ
	 * @CreateDate：2015-12-21 下午 20：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryListById")
	public void queryListById(){
		try {
			List<InpatientPermission> inpatientPermissionList=permissionService.queryListById(inno);
			int total = permissionService.getQueryListByIdCount(inno);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", inpatientPermissionList);
			String json = JSONUtils.toJson(map, "yyyy-MM-dd");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZTYSZ_YZSQGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZTYSZ_YZSQGL", "住院医生站_医嘱授权管理", "2", "0"), e);
		}
	}
	/**  
	 * @Description：通过病历号 查询医嘱授权记录信息
	 * @Author：TCJ
	 * @CreateDate：2015-12-22 下午 15 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryPermissionById")
	public void queryPermissionById(){
		try {
			InpatientPermission inpatientPermissionList=permissionService.queryPermissionById(manId);
			String json=JSONUtils.toJson(inpatientPermissionList,DateUtils.DATETIME_FORMAT_HM);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZTYSZ_YZSQGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZTYSZ_YZSQGL", "住院医生站_医嘱授权管理", "2", "0"), e);
		}
	}
	/**  
	 * @Description：通过就诊卡号号查询患者信息
	 * @Author：TCJ
	 * @CreateDate：2015-12-21 下午 20：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryFormById")
	public void quertFormById(){
		try {
			if(StringUtils.isNotBlank(mid)){
				mid=mid.trim();
			}
			//通过接口查询就诊卡号对应的病历号
			String medicalrecordId = patinentInnerService.getMedicalrecordId(mid);
			List<InpatientInfoNow> inpatientInfo = permissionService.queryByMedicall(medicalrecordId);
			String json=JSONUtils.toJson(inpatientInfo);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZTYSZ_YZSQGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZTYSZ_YZSQGL", "住院医生站_医嘱授权管理", "2", "0"), e);
		}
	}
	/**  
	 * @Description：通过病历号查询患者信息
	 * @Author：TCJ
	 * @CreateDate：2015-12-21 下午 20：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryFormByIdM")
	public void queryFormByIdM(){
		try {
			if(StringUtils.isNotBlank(mid)){
				mid=mid.trim();
			}		
			//通过接口查询就诊卡号对应的病历号
			List<InpatientInfoNow> inpatientInfo = permissionService.queryByMedicall(mid);
			String json=JSONUtils.toJson(inpatientInfo);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZTYSZ_YZSQGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZTYSZ_YZSQGL", "住院医生站_医嘱授权管理", "2", "0"), e);
		}
	}
	/**  
	 * @Description：通过Id删除医嘱授权记录
	 * @Author：TCJ
	 * @CreateDate：2015-12-22 上午11:50  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "delById")
	public void delById(){
		Map<String,String> resMap=new HashMap<String,String>();
		try {
			permissionService.delById(ids);
			resMap.put("resCode", "success");
			resMap.put("resMsg", "删除成功!");
		} catch (Exception e) {
			resMap.put("resCode", "error");
			resMap.put("resMsg", "删除失败!");
			logger.error("ZTYSZ_YZSQGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZTYSZ_YZSQGL", "住院医生站_医嘱授权管理", "2", "0"), e);
		}
		String json=JSONUtils.toJson(resMap);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * @Description：添加或修改医嘱授权记录
	 * @Author：TCJ
	 * @CreateDate：2015-12-22 下午 18 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "saveOrUpdateList",results = { @Result(name = "json", type = "json") })
	public void saveOrUpdateList(){
		
		try {
			permissionService.saveOrUpdateList(inpatientPermission);
			WebUtils.webSendString("success");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			WebUtils.webSendJSON("error");
			logger.error("ZTYSZ_YZSQGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZTYSZ_YZSQGL", "住院医生站_医嘱授权管理", "2", "0"), e);
		}
	}
	/**  
	 * @Description：根据科室类型查询相应的科室list
	 * @Author：TCJ
	 * @CreateDate：2016-3-29 下午 18 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryDeptByType")
	public void queryDeptByType(){
		try {
			List<SysDepartment> syslist=permissionService.queryDeptByType(state,q);
			String json=JSONUtils.toExposeJson(syslist, true, null, "deptCode","deptName");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZTYSZ_YZSQGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZTYSZ_YZSQGL", "住院医生站_医嘱授权管理", "2", "0"), e);
		}
	}
	
	/**
	 * 查询员工下拉框（渲染）
	 * @author tcj
	 * @param 
	 * @return
	 */
	@Action(value = "employeeCombo" )
	public void employeeCombo(){
		try {
			List<SysEmployee> sysemp=permissionService.employeeCombobox(departmentId,q);
			String json=JSONUtils.toExposeJson(sysemp, false, null, "jobNo","name");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZTYSZ_YZSQGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZTYSZ_YZSQGL", "住院医生站_医嘱授权管理", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：查询患者信息
	 * @Author：TCJ
	 * @CreateDate：2016-3-29 下午 18 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryPatientInfo")
	public void queryPatientInfo(){
		try {
			Patient pa=permissionService.queryPatientInfo(mid);
			String json=JSONUtils.toJson(pa);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZTYSZ_YZSQGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZTYSZ_YZSQGL", "住院医生站_医嘱授权管理", "2", "0"), e);
		}
	}
	/**  
	 * 科室与医生联动（医生可登陆的科室）
	 * @Author：TCJ
	 * @version 1.0
	 */
	@Action(value = "queryLoginUserDept")
	public void queryLoginUserDept(){
		try {
			List<SysEmployee> sysempl=permissionService.queryLoginUserDept(departmentId);
			Map<String, String> map=new HashMap<String, String>();
			for(SysEmployee dept:sysempl){
				map.put(dept.getJobNo(), dept.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZTYSZ_YZSQGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZTYSZ_YZSQGL", "住院医生站_医嘱授权管理", "2", "0"), e);
		}
	}
	
	/**
	 * userMap查询
	 * @Author：tuchuanjiang	
	 * @version 1.0
	 */
	@Action(value = "queryUser")
	public void queryUser(){
		Map<String, String> userlist=employeeInInterService.queryEmpCodeAndNameMap();
		String json=JSONUtils.toJson(userlist);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * deptMap查询
	 * @Author：tuchuanjiang	
	 * @version 1.0
	 */
	@Action(value = "queryDept")
	public void queryDept(){
		Map<String, String> userlist=deptInInterService.querydeptCodeAndNameMap();
		String json=JSONUtils.toJson(userlist);
		WebUtils.webSendJSON(json);
	}
	
}
