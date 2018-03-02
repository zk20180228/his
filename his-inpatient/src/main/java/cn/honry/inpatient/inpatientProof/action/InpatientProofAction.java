package cn.honry.inpatient.inpatientProof.action;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.Logical;
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

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.InpatientProof;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.baseinfo.hospital.service.HospitalInInterService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inpatient.inpatientProof.service.InpatientProofService;
import cn.honry.inpatient.inpatientProof.vo.PoorfBill;
import cn.honry.inpatient.inpatientProof.vo.proReg;
import cn.honry.report.service.IReportService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


/**   
*  
* @className：InpatientProofAction
* @description：  住院证明
* @author：wujiao
* @createDate：2015-6-24 上午10:56:35  
* @modifier：姓名
* @modifyDate：2015-6-24 上午10:56:35  
* @modifyRmk：  
* @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/InpatientProof")
public class InpatientProofAction extends ActionSupport implements ModelDriven<InpatientProof>{
	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	
	/**
	 * 住院证明service
	 * @param inpatientProofService
	 */
	@Autowired
	@Qualifier(value = "inpatientProofService")
	public void setInpatientProofService(InpatientProofService inpatientProofService) {
		this.inpatientProofService = inpatientProofService;
	}
	
	/**
	 * 注入inpatientInfoInInterService公共接口
	 */
	private InpatientInfoInInterService inpatientInfoInInterService;
	@Autowired
	@Qualifier(value = "inpatientInfoInInterService")
	public void setInpatientInfoInInterService(
			InpatientInfoInInterService inpatientInfoInInterService) {
		this.inpatientInfoInInterService = inpatientInfoInInterService;
	}
	
	/**
	 * 注入patinentInnerService公共接口
	 */
	@Autowired
	@Qualifier(value = "patinentInnerService")
	private PatinentInnerService patinentInnerService;
	public void setPatinentInnerService(PatinentInnerService patinentInnerService) {
		this.patinentInnerService = patinentInnerService;
	}

	/**
	 * 住院证明list
	 */
	private List<InpatientProof> inpatientProofList;
	
	/***
	 * 注入员工Service
	 */
	@Autowired
	@Qualifier(value="employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	
	/***
	 * 注入员工Service
	 */
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	
	/**获取医院名称**/
	@Autowired
	@Qualifier(value="hospitalInInterService")
	private HospitalInInterService hospitalInInterService;
	public void setHospitalInInterService(
			HospitalInInterService hospitalInInterService) {
		this.hospitalInInterService = hospitalInInterService;
	}
	
	/**
	 * 获取jasper路径
	 */
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	
	private InpatientProofService inpatientProofService;
	
	private InpatientProof inpatientProof=new InpatientProof();
	
	private Logger logger=Logger.getLogger(InpatientProofAction.class);
	
	/**
	 * 获取请求
	 */
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	/**
	 * 医生List
	 */
	private List<SysEmployee> employeeList;
	
	/**
	 * 挂号信息 患者信息List
	 */
	private List<proReg> proRegList;
	
	/**
	 * 病历号
	 */
	private String midicalrecordId;
	
	private String type;
	
	/**
	 * 查询科室下的员工（科室code）
	 */
	private String departmentCode;
	
	/**
	 * 门诊号（预约号）
	 * @return
	 */
	private String regisNo;
	
	/**
	 * 下拉框即时弹框
	 * @return
	 */
	private String q;
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getRegisNo() {
		return regisNo;
	}
	public void setRegisNo(String regisNo) {
		this.regisNo = regisNo;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getMidicalrecordId() {
		return midicalrecordId;
	}
	public void setMidicalrecordId(String midicalrecordId) {
		this.midicalrecordId = midicalrecordId;
	}
	@Override
	public InpatientProof getModel() {
		return inpatientProof;
	}
	public List<InpatientProof> getInpatientProofList() {
		return inpatientProofList;
	}
	public void setInpatientProofList(List<InpatientProof> inpatientProofList) {
		this.inpatientProofList = inpatientProofList;
	}
	public List<SysEmployee> getEmployeeList() {
		return employeeList;
	}
	public void setEmployeeList(List<SysEmployee> employeeList) {
		this.employeeList = employeeList;
	}
	public List<proReg> getProRegList() {
		return proRegList;
	}
	public void setProRegList(List<proReg> proRegList) {
		this.proRegList = proRegList;
	}
	
	/**  
	 * 页面跳转
	 * 跳转到开立住院证页面
	 * @Description：  获取list页面
	 * @Author：wujiao
	 * @CreateDate：2015-6-24 上午10:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-24 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws ParseException 
	 */
	@RequiresPermissions(value={"KLZYZM:function:view","MZKLZYZ:function:view"},logical=Logical.OR) 
	@Action(value = "listProof", results = { @Result(name = "list", location = "/WEB-INF/pages/register/inpatientProof/proofList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listProof() {
		
		try {
			SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
			if(dept!=null){
				ServletActionContext.getRequest().setAttribute("dddd", dept.getId());
			}else{
				ServletActionContext.getRequest().setAttribute("deptFlg", "false");
			}
			ServletActionContext.getRequest().setAttribute("name", user.getName());
			Date date = new Date();
			String	endTime = DateUtils.formatDateY_M_D_H_M_S(date);
			ServletActionContext.getRequest().setAttribute("now", endTime);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
		return "list";
	}
	
	/** 
	 * 根据就诊卡号查询病人信息
	 * @Description：  查询病人信息
	 * @Author：wujiao
	 * @CreateDate：2015-6-24 上午10:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-24 上午10:56:35  
	 * @TODO：没有按照代码规范提交，岳工催着要，影响他的业务开发 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"KLZYZM:function:view","MZKLZYZ:function:view"},logical=Logical.OR) 
	@Action(value = "queryProof", results = { @Result(name = "json", type = "json") })
	public void queryProof() {
		InpatientProof prooflist = null;
		try {
			 prooflist = inpatientProofService.getProof(inpatientProof.getId());
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
		String json=JSONUtils.toJson(prooflist);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 住院证明保存&修改
	 * @Description：住院证明的添加&修改
	 * @Author：wujiao
	 * @CreateDate：2015-6-24 上午10:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-24 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "editInfoPreregisters",results = { @Result(name = "json", type = "json") })
	public void editInfoPreregisters() throws Exception {
		try{
			inpatientProofService.saveOrUpdatePreregister(inpatientProof);
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
		String json=JSONUtils.toJson(inpatientProof);
		WebUtils.webSendJSON(json);
	}
	
	/** 
	 * 根据挂号预约号（门诊号）查询患者信息
	 * @Description：  根据挂号预约号（门诊号）查询患者信息
	 * @Author：tcj
	 * @CreateDate：2015-12-3
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryMedicalrecordId")
	public void queryMedicalrecordId() {
		try {
			RegistrationNow registerInfo = inpatientProofService.queryMedicalrecordId(regisNo);
			String json=JSONUtils.toJson(registerInfo,DateUtils.DATE_FORMAT);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：  根据病例号查询患者信息和挂号信息
	 * @Author：tcj
	 * @CreateDate：2015-12-7
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"KLZYZM:function:view","MZKLZYZ:function:view"},logical=Logical.OR) 
	@Action(value = "queryInfoListHis") 
	public void queryInfoListHis() {
		
		try {
			//前台传来的是就诊卡号，因牵扯较多，未另建变量
			String medicalrecordId=ServletActionContext.getRequest().getParameter("medicalrecordId");
			if(StringUtils.isNotBlank(medicalrecordId)){
				medicalrecordId=medicalrecordId.trim();
			}
			
			//通过接口查询就诊卡号对应的病历号
			List<proReg> proRegList = inpatientProofService.queryInfoListHis(medicalrecordId);
			for(int i=0;i<proRegList.size();i++){
				int num=Integer.valueOf(inpatientProofService.queryShoufeiState(proRegList.get(i).getNo()));
				proRegList.get(i).setState(num);
			}
			String json=JSONUtils.toJson(proRegList,DateUtils.DATETIME_FORMAT);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}
	/**  
	 * @Description：  根据传过来的号码（病历号、门诊号、就诊卡号、医疗证号）查询患者信息
	 * @Author：tcj
	 * @CreateDate：2015-12-26
	 * @TODO：没有按照代码规范提交，岳工催着要，影响他的业务开发 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"KLZYZM:function:view","MZKLZYZ:function:view"},logical=Logical.OR) 
	@Action(value = "queryPatientInfo", results = { @Result(name = "json", type = "json") })
	public void queryPatientInfo(){
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			String medicalrecordId =ServletActionContext.getRequest().getParameter("medicalrecordId");
			List<Patient> patientList=inpatientProofService.queryByNumber(medicalrecordId);
			String result="none";
			for(int i=0;i<patientList.size();i++){
				if(StringUtils.isNotBlank(patientList.get(i).getId())){
					result="success";
				}
			}
			if("success".equals(result)){
				map.put("resMsg", "success");
				map.put("resCode", "查询成功");
			}else{
				map.put("resMsg", "error");
				map.put("resCode", "该患者不存在，请核对输入的号码");
			
			}
			String json =JSONUtils.toJson(map, DateUtils.DATE_FORMAT);
			WebUtils.webSendString(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}
	/**  
	 * @Description：  根据传过来的数据（病例号、门诊号、就诊卡号、医疗证号）查询患者7日内的挂号记录
	 * @Author：tcj
	 * @CreateDate：2015-12-26
	 * @TODO：没有按照代码规范提交，岳工催着要，影响他的业务开发 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"KLZYZM:function:view","MZKLZYZ:function:view"},logical=Logical.OR) 
	@Action(value = "queryDateInfo")
	public void queryDateInfo(){
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			String medicalrecordId =ServletActionContext.getRequest().getParameter("medicalrecordId");
			List<RegistrationNow> reg = inpatientProofService.queryDateInfo(medicalrecordId);
			if(reg!=null && reg.size()>1){
				map.put("resMsg", "success");
				map.put("resCode", reg);
			
			}else if(reg.size()==1){
				map.put("resMsg", "one");
				map.put("resCode", reg);
			}else{
				map.put("resMsg", "error");
				map.put("resCode", "患者没有有效的挂号记录");
			}
			String json=JSONUtils.toJson(map,DateUtils.DATE_FORMAT);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}
	/**
	 * 根据病历号查询患者当日的开证记录
	 * @CreateDate：2015-12-25
	 * @param medicalrecordId
	 * @TODO：没有按照代码规范提交，岳工催着要，影响他的业务开发 
	 * @return
	 */
	@RequiresPermissions(value={"KLZYZM:function:view","MZKLZYZ:function:view"},logical=Logical.OR) 
	@Action(value = "queryProofDate", results = { @Result(name = "json", type = "json") })
	public void queryProofDate(){
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			String medicalrecordId =ServletActionContext.getRequest().getParameter("zjhm");
			List<InpatientProof> ipf =inpatientProofService.queryProofDate(medicalrecordId);
			if(ipf!=null && ipf.size()>0){
				map.put("resMsg", "success");
				map.put("resCode", "该患者今天已经开立住院证明，不得重复开立");
			}else{
				map.put("resMsg", "error");
				map.put("resCode", "可以正常开立证明");
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendString(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}
	/**
	 * 查询病区
	 * @CreateDate：2016-1-5
	 * @param departmentId
	 * @return
	 */
	@Action(value = "querybingqu", results = { @Result(name = "json", type = "json") })
	public void querybingqu(){
		try {
			List<SysDepartment> list = inpatientProofService.querybingqu(departmentCode,q);
			String json=JSONUtils.toExposeJson(list, false, null, "deptCode","deptName");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}
	
	/**
	 * 住院证查询病区
	 * @CreateDate：2016-1-5
	 * @param departmentId
	 * @return
	 */
	@Action(value = "queryKeshi", results = { @Result(name = "json", type = "json") })
	public void queryKeshi(){
		try {
			List<SysDepartment> list = inpatientProofService.queryKeshi(departmentCode,q);
			String json=JSONUtils.toExposeJson(list, false, null, "deptCode","deptName");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}
	
	/**
	 * 查询该患者是否处于住院状态(登记，接诊)、是否有处于有效状态的住院证
	 * @CreateDate：2016-2-20
	 * @param 
	 * @TODO：没有按照代码规范提交，岳工催着要，影响他的业务开发 
	 * @return
	 */
	@RequiresPermissions(value={"KLZYZM:function:view","MZKLZYZ:function:view","ZYDJXXGL:function:view"},logical=Logical.OR) 
	@Action(value = "queryInpatientInfo", results = { @Result(name = "json", type = "json") })
	public void queryInpatientInfo(){
		try {
			String mid=ServletActionContext.getRequest().getParameter("mid");
			String result=inpatientProofService.queryInpatientInfo(mid);
			Map<String,String> map=new HashMap<String,String>();
			if("none".equals(result)){
				map.put("resMsg", "success");
				map.put("resMsg", "success");
			}else if("yesRI".equals(result)){
				map.put("resMsg", "yesRI");
				map.put("resCode", "患者正在住院，无需办理住院证明");
			}else if("yes0".equals(result)){
				map.put("resMsg", "yes0");
				map.put("resCode", "患者已经办理住院证明，无需重复办理");
			}
			String json=JSONUtils.toJson(map, DateUtils.DATE_FORMAT);
			WebUtils.webSendString(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}
	/**
	 * 通过挂号记录查询证明表里是否有证明记录（参数:病历号）
	 * @CreateDate：2016-3-16
	 * @param 
	 * @return
	 */
	@Action(value = "searchProofByResinfo" )
	public void searchProofByResinfo(){
		try {
			List<InpatientProof> ipl=inpatientProofService.searchProofByResinfo(midicalrecordId,regisNo);
			Map<String,Object> map=new HashMap<String,Object>();
			if(ipl!=null&&ipl.size()>0){
				String infoState=inpatientProofService.queryPatientStateBymz(midicalrecordId);
				if("I".equals(infoState)){
					map.put("key", "I");
					map.put("mes", "该患者处于在院接诊状态");
					map.put("value",ipl.get(0));
				}else if("R".equals(infoState)){
					map.put("key", "R");
					map.put("mes", "该患者处于住院登记状态");
					map.put("value",ipl.get(0));
				}else if("B".equals(infoState)){
					map.put("key", "B");
					map.put("mes", "该患者处于出院登记状态");
					map.put("value",ipl.get(0));
				}else if("O".equals(infoState)){
					map.put("key", "O");
					map.put("mes", "该患者处于出院结算状态");
					map.put("value",ipl.get(0));
				}else if("P".equals(infoState)){
					map.put("key", "P");
					map.put("mes", "该患者处于预约出院状态");
					map.put("value",ipl.get(0));
				}else if("N".equals(infoState)){
					map.put("key", "N");
					map.put("mes", "该患者处于无费退院状态");
					map.put("value",ipl.get(0));
				}else{
					map.put("key", "one");
					map.put("value",ipl.get(0));
				}
			}else{
				map.put("key", "none");
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}
	/**
	 * 住院科室下拉框
	 * @CreateDate：2016-1-5
	 * @param departmentId
	 * @return
	 */
	@Action(value = "queryInHosDept", results = { @Result(name = "json", type = "json") })
	public void queryInHosDept(){
		try {
			List<SysDepartment> list = inpatientProofService.queryInHosDept(q);
			String json=JSONUtils.toExposeJson(list, false, null, "deptCode","deptName");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}
	/**
	 * 查询员工下拉框（渲染）
	 * @author tcj
	 * @param 
	 * @return
	 */
	@Action(value = "employeeComboboxProof" )
	public void employeeComboboxProof(){
		List<SysEmployee> sysemp=inpatientInfoInInterService.employeeComboboxProof(departmentCode,null);
		String json=JSONUtils.toExposeJson(sysemp, false, null, "jobNo","name");
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询员工下拉框（渲染）
	 * @author tcj
	 * @param 
	 * @return
	 */
	@Action(value = "employeeCombobox" )
	public void employeeCombobox(){
		List<SysEmployee> sysl=employeeInInterService.getEmpByType(type);
		String json=JSONUtils.toJson(sysl);
		WebUtils.webSendJSON(json);

	}
	
	/**
	 * 
	 * 
	 * <p>查询员工map（code，name） </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月3日 下午4:32:17 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月3日 下午4:32:17 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="queryEmpMapPublic")
	public void queryEmpMapPublic(){
		Map<String, String> map = employeeInInterService.queryEmpCodeAndNameMap();
		String joString = JSONUtils.toJson(map);
		WebUtils.webSendJSON(joString);
	}

	/**
	 * 
	 * 
	 * <p>查询科室map（code，name） </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月3日 下午4:32:30 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月3日 下午4:32:30 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="queryDeptMapPublic")
	public void queryDeptMapPublic(){
		try {
			List<SysDepartment> deptlist=inpatientInfoInInterService.queryDeptMapPublic();
			Map<String,String> map=new HashMap<String,String>();
			for(SysDepartment dept:deptlist){
				map.put(dept.getDeptCode(), dept.getDeptName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}

	/**
	 * 
	 * 
	 * <p>查询科室map（code，name） </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月3日 下午4:32:44 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月3日 下午4:32:44 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="queryDeptMapPublicByRe")
	public void queryDeptMapPublicByRe(){
		try {
			Map<String, String> map=deptInInterService.querydeptCodeAndNameMap();
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}
	
	/**
	 * 
	 * 
	 * <p>查询科室List </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月3日 下午4:32:56 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月3日 下午4:32:56 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="querydeptListForcomboboxPublic")
	public void querydeptListForcomboboxPublic(){
		try {
			List<SysDepartment> bcl=inpatientInfoInInterService.queryDeptMapPublic();
			String json=JSONUtils.toExposeJson(bcl, false, null, "deptCode","name");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}

	/**
	 * 
	 * 
	 * <p>查询合同单位List（code，name） </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月3日 下午4:33:07 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月3日 下午4:33:07 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="queryContractunitListForcombobox")
	public void queryContractunitListForcombobox(){
		try {
			List<BusinessContractunit> bcl=inpatientInfoInInterService.queryContractunitListForcombobox();
			String json=JSONUtils.toExposeJson(bcl, false, null, "encode","name");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}

	/**
	 * 
	 * 
	 * <p>查询合同单位Map（code，name） </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月3日 下午4:33:19 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月3日 下午4:33:19 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="queryContractunitMapPublic")
	public void queryContractunitMapPublic(){
		try {
			List<BusinessContractunit> bcl=inpatientInfoInInterService.queryContractunitListForcombobox();
			Map<String,String> map=new HashMap<String,String>();
			for(BusinessContractunit bc:bcl){
				map.put(bc.getEncode(), bc.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}
	
	/**
	 * 
	 * 
	 * <p>查询证件类型List（code，name） </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月3日 下午4:33:31 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月3日 下午4:33:31 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="queryCertypeListForcomboboxPublic")
	public void queryCertypeListForcomboboxPublic(){
		try {
			String type = "certificate";
			String encode=null;
			List<BusinessDictionary> bcl=inpatientInfoInInterService.queryDictionaryListForcomboboxPublic(type, encode);
			String json=JSONUtils.toExposeJson(bcl, false, null, "encode","name");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}

	/**
	 * 
	 * 
	 * <p>查询挂号类别Map </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月3日 下午4:33:43 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月3日 下午4:33:43 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="queryRegisterTypeMapPublic")
	public void queryRegisterTypeMapPublic(){
		try {
			String type="registerType";
			String encode=null;
			List<BusinessDictionary> bcl=inpatientInfoInInterService.queryDictionaryListForcomboboxPublic(type, encode);
			Map<String,String> map=new HashMap<String,String>();
			for(BusinessDictionary crt:bcl){
				map.put(crt.getEncode(), crt.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("MZYSZ_KLZYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
		}
	}
	
	/**
	 * 
	 * 
	 * <p>住院证打印打印 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月3日 下午4:34:54 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月3日 下午4:34:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception:
	 *
	 */
	@Action(value = "iReportInpatientProof")
	public void iReportInpatientProof() throws Exception {
		  try{
			   //jasper文件名称 不含后缀
			   String fileName = request.getParameter("fileName");
			   
			   //jasper文件所用到的参数 RECIPE_NO
			   String ids= request.getParameter("CERTIFICATES_NO");
			   String bir =request.getParameter("age");
			   String root_path = request.getSession().getServletContext().getRealPath("/");
			   root_path = root_path.replace('\\', '/');
			   String reportFilePath = root_path + webPath +fileName+".jasper";
			   List<PoorfBill> list=inpatientProofService.queryProofByIds(ids);
			   JRDataSource jrd=new JRBeanCollectionDataSource(list);
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   parameters.put("ids", ids);
			   parameters.put("H_NAME", hospitalInInterService.getPresentHospital().getName());
			   parameters.put("AGE",java.net.URLDecoder.decode(bir,"UTF-8"));
			   parameters.put("SUBREPORT_DIR", root_path + webPath);
			   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
			 }catch(Exception e){
				  logger.error("MZYSZ_KLZYZ", e);
				  hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_KLZYZ", "门诊医生站_开立住院证", "2", "0"), e);
			 }

	}
	public static String calAge(Date birthday) {
		try {
			int yearOfAge = 0;
			int monthOfAge = 0;
			int dayOfAge = 0;
			Calendar cal = Calendar.getInstance();
			long nowMillis = cal.getTimeInMillis();
			long birthdayMillis = birthday.getTime();
			if (nowMillis < birthdayMillis) {
				return null;
			}
			
			int yearNow = cal.get(Calendar.YEAR);
			int monthNow = cal.get(Calendar.MONTH) + 1;
			int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

			cal.setTime(birthday);
			int yearBirth = cal.get(Calendar.YEAR);
			int monthBirth = cal.get(Calendar.MONTH) + 1;
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

			if (yearNow == yearBirth) {
				monthOfAge = monthNow - monthBirth;
				if (monthNow == monthBirth) {
					dayOfAge = dayOfMonthNow - dayOfMonthBirth;
				} else {
					if (dayOfMonthNow < dayOfMonthBirth) {
						monthOfAge--;
					}
					if (monthOfAge == 0) {
						dayOfAge = (int) TimeUnit.MILLISECONDS.toDays(nowMillis - birthdayMillis);
					}
				}
			} else {
				yearOfAge = yearNow - yearBirth;
				if (monthNow < monthBirth) {
					yearOfAge--;
					if (yearOfAge == 0) {
						monthOfAge = 12 - monthBirth + monthNow;
						if (dayOfMonthNow >= dayOfMonthBirth) {
							monthOfAge++;
						}
					}
				} else if (monthNow == monthBirth) {
					if (dayOfMonthNow < dayOfMonthBirth) {
						yearOfAge--;
						if (yearOfAge == 0) {
							monthOfAge = 11;
						}
					}
				}
			}
			if (yearOfAge > 0) {
				return yearOfAge + "岁";
			} else if (monthOfAge > 0) {
				return monthOfAge + "月";
			} else if (dayOfAge > 0) {
				return dayOfAge + "天";
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}
