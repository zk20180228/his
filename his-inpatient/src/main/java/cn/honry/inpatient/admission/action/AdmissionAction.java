package cn.honry.inpatient.admission.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientPostureInfo;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.VidBedInfo;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inpatient.admission.service.AdmissionService;
import cn.honry.inpatient.admission.vo.AdmissionVO;
import cn.honry.inpatient.info.service.InpatientInfoService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 * @className：AdmissionAction
 * @Description：  住院接诊Action 
 * @Author：hedong
 * @CreateDate：2015-12-4 上午10:39:08 
 * @Modifier：
 * @ModifyDate：
 * @ModifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/admission")
public class AdmissionAction extends ActionSupport implements ModelDriven<AdmissionVO>{
	private Logger logger=Logger.getLogger(AdmissionAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	//住院主表model
	private AdmissionVO admissionVO = new AdmissionVO();
	@Override
	public AdmissionVO getModel() {
		return admissionVO;
	}
	
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
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
	/**
	 *患者病历号 
	 *@author lyy
	 */
	private String medicalId;
	public String getMedicalId() {
		return medicalId;
	}
	public void setMedicalId(String medicalId) {
		this.medicalId = medicalId;
	}
	/**
	 * 当前登录病区
	 */
	private String nurdept;
	public String getNurdept() {
		return nurdept;
	}
	public void setNurdept(String nurdept) {
		this.nurdept = nurdept;
	}
	/**
	 * 当前时间
	 */
	private Date now;
	public Date getNow() {
		return now;
	}
	public void setNow(Date now) {
		this.now = now;
	}
	/**
	 * 页面输入的六位病历号
	 */
	private String medId;
	public String getMedId() {
		return medId;
	}
	public void setMedId(String medId) {
		this.medId = medId;
	}
	
	/**
	 * 编码表中传入类型type
	 */
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	private String q;
	
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	/**
	 * 病床和病房
	 */
	private String bed;
	private String bedWard;
	
	public String getBed() {
		return bed;
	}
	public void setBed(String bed) {
		this.bed = bed;
	}
	public String getBedWard() {
		return bedWard;
	}
	public void setBedWard(String bedWard) {
		this.bedWard = bedWard;
	}

	@Autowired
	@Qualifier(value="admissionService")
	private AdmissionService admissionService;
	
	public void setAdmissionService(AdmissionService admissionService) {
		this.admissionService = admissionService;
	}
	@Autowired 
	@Qualifier(value = "inpatientInfoService")
	private InpatientInfoService inpatientInfoService;
	
	public void setInpatientInfoService(InpatientInfoService inpatientInfoService) {
		this.inpatientInfoService = inpatientInfoService;
	}
	/**  
	 * @Description：  跳转至出院结算列表页面
	 * @Author：hedong
	 * @CreateDate：2015-12-4 上午10:45:08 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"ZYZYJZ:function:view"})
	@Action(value = "listAdmission", results = { @Result(name = "admissionList", location = "/WEB-INF/pages/inpatient/admission/admissionList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listAdmission() {
		  try {
			String d= DateUtils.formatDateY_M_D(new Date());
			  Date noww = DateUtils.parseDate(d, DateUtils.DATE_FORMAT);
			  now=noww;
			  SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			  if(dept!=null){
				  nurdept=dept.getDeptCode();//当前登录人科室
				}else{
					ServletActionContext.getRequest().setAttribute("deptFlg", "false");
				}
		} catch (Exception e) {
			logger.error("ZYGL_ZYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
		}
		return "admissionList";
	}
	/**  
	 * @Description： 根据住院号获取住院登记中的患者信息及登记信息
	 * @Author：hedong
	 * @CreateDate：2015-12-4 上午10:45:08 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryInfoByPatientNo", results = { @Result(name = "json", type = "json") })
	public void queryInfoByPatientNo() throws Exception{
		String medicalrecordId = request.getParameter("medicalrecordId");
		AdmissionVO	vo = null;
		if(null!=medicalrecordId){
			try {
				vo = admissionService.getVOByPatientNo(medicalrecordId);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("ZY_ZYZYJZ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZY_ZYZYJZ", "住院管理_住院接诊", "2", "0"), e);
			}
			String json = JSONUtils.toJson(vo);
			WebUtils.webSendJSON(json);
		}else{
			String json = JSONUtils.toJson(vo);
			WebUtils.webSendJSON(json);
		}
	}
	/**  
	 * @Description:查询患者预约的病床是否存在
	 */
	@Action(value = "isExistBed", results = { @Result(name = "json", type = "json") })
	public void isExistBed(){
		try {
			String 	state = admissionService.isExistBed(bed,bedWard,medId);
			state = JSONUtils.toJson(state);
			WebUtils.webSendJSON(state);
		} catch (Exception e) {
			logger.error("ZYGL_ZYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
		}
	}
	/**
	 * @Description:保存
	 * @Author：  hedong
	 * @CreateDate： 2015-12-04
	 * @param @throws Exception   
	 * @return String  
	 * @version 1.0
	 * @throws IOException 
	**/
	@RequiresPermissions(value={"ZYZYJZ:function:save"})
	@Action(value = "saveAdmission", results = { @Result(name = "json", type = "json") })
	public void saveAdmission() throws IOException {
			try {
				WebUtils.webSendString(admissionService.saveAdmission(admissionVO));
			} catch (Exception e) {
				System.out.println(e.getMessage());
				WebUtils.webSendJSON("error");
				logger.error("ZYGL_ZYJZ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
			}
	}
	/**
	 * @Description:查询患者生命体征信息 
	 **/
	@Action(value = "queryPostureInfo")
	public void queryPostureInfo() throws IOException {
		try {
			String medicalrecordId = patinentInnerService.getMedicalrecordId(medId);
			List<InpatientPostureInfo> inpatientPostureInfo=admissionService.queryPostureInfo(medicalrecordId);
			String json=JSONUtils.toJson(inpatientPostureInfo);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
		}
	}
	/**  
	 * @Description：住院部医生查询(combobox下拉可以搜索)
	 * @Author：hedong
	 * @CreateDate：2015-12-7 上午11:45:08 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value="likeZYDepartmentDoctors",results={@Result(name="json",type="json")} )
	public void likeZYDepartmentDoctors(){
		try {
			List<SysEmployee> zyDepartmentDoctors = admissionService.getZYDepartmentDoctors("住院部");
			String json=JSONUtils.toJson(zyDepartmentDoctors);
			WebUtils.webSendString(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
			System.out.println(e.getMessage());
		}
	}
	
	/**  
	 * @Description：跳转至床位窗口页面
	 * @Author：hedong
	 * @CreateDate：2015-12-8下午16：:08
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "bedList", results = { @Result(name = "bedList", location = "/WEB-INF/pages/inpatient/admission/bedWin.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String bedList() {
		return "bedList";
	}
	
	/**  
	 * @Description：根据部门id查询床位信息至床位弹出窗口
	 * @Author：hedong
	 * @CreateDate：2015-12-8下午16：:08
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "searchBedInfoWithDeptId", results = { @Result(name = "json", type = "json") })
	public void searchBedInfoWithDeptId(){
		try {
			String id = ServletActionContext.getRequest().getParameter("id");
			String status = ServletActionContext.getRequest().getParameter("status");
			List<VidBedInfo> inpatientInfoList=admissionService.getBedInfoWithDeptId(id,status);
			String json = JSONUtils.toJson(inpatientInfoList,DateUtils.DATE_FORMAT);
			WebUtils.webSendString(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description：根据床位id获得床位信息 用于判断床位状态
	 * @Author：hedong
	 * @CreateDate：2015-12-8下午18：:08
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getBedState", results = { @Result(name = "json", type = "json") })
	public void getBedState(){
		try {
			String bedId = ServletActionContext.getRequest().getParameter("bedId");
			BusinessHospitalbed businessHospitalbed =admissionService.getBedState(bedId);
			String json = JSONUtils.toJson(businessHospitalbed,DateUtils.DATE_FORMAT);
			WebUtils.webSendString(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
			e.printStackTrace();
		}
	}
	@Action(value = "getMedById")
	public void getMedById() {
		
		try {
			//前台传来的是就诊卡号，因牵扯较多，未另建变量
			String medicalrecordId = request.getParameter("medicalrecordId");
			if(StringUtils.isNotBlank(medicalrecordId)){
				medicalrecordId=medicalrecordId.trim();
			}		
			
			//通过接口查询就诊卡号对应的病历号
			List<InpatientInfoNow> infoVo = admissionService.getQueryInfo(medicalrecordId);
			for (InpatientInfoNow inpatientInfo : infoVo) {
				inpatientInfo.setRemark(inpatientInfo.getReportAge()+inpatientInfo.getReportAgeunit());
			}
			String json=JSONUtils.toJson(infoVo);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description：  查询患者的住院信息（登记、接诊，以及相应的时间）
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午16:40
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryInpatientState", results = { @Result(name = "json", type = "json") })
	public void queryInpatientState(){
		try{
			String medicalrecordId = ServletActionContext.getRequest().getParameter("medicalrecordId");
			String result=inpatientInfoService.queryMedicalrecordIdDate(medicalrecordId);
			Map<String,String> map=new HashMap<String,String>();
			if("I".equals(result)){
				map.put("resMsg", "I");
				map.put("resCode", "患者处于住院状态，无需办理住院接诊");
			}else{
				map.put("resMsg", "no");
			}
			String json=JSONUtils.toJson(map, DateUtils.DATE_FORMAT);
			WebUtils.webSendString(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
		}
	}
	/**  
	 * @Description：查询责任护士下拉框
	 * @Author：涂川江
	 * @CreateDate：2015-12-8下午18：:08
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryZerenhushi", results = { @Result(name = "json", type = "json") })
	public void queryZerenhushi(){
		try {
			List<SysEmployee> sysl=admissionService.queryZerenhushi();
			String json=JSONUtils.toExposeJson(sysl, false, null, "name","jobNo");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**  
	 * @Description： 查询患者是否存在
	 * @Author：tcj
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryPatientexist", results = { @Result(name = "json", type = "json") })
	public void queryPatientexist(){
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			String medicalrecordId =ServletActionContext.getRequest().getParameter("medicalrecordId");
			List<Patient> patientList=admissionService.queryPatientexist(medicalrecordId);
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
			String json=JSONUtils.toJson(map, DateUtils.DATE_FORMAT);
			WebUtils.webSendString(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**  
	 * @Description： 查询住院医生下拉框
	 * @Author：tcj
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "findZhuyuanDoc")
	public void findZhuyuanDoc(){
		try {
			String name=ServletActionContext.getRequest().getParameter("q");
			String type=ServletActionContext.getRequest().getParameter("type");
			List<SysEmployee> syslist=admissionService.findZhuyuanDoc(name,type,request.getParameter("page"),request.getParameter("rows"));
			int total = admissionService.getTotalemp(name,type);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", syslist);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**  
	 * @Description： 查询住院医生下拉框
	 * @Author：tcj
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryPatientStatInfo")
	public void queryPatientStatInfo(){
		
		try {
			//通过接口查询就诊卡号对应的病历号
			String medicalrecordId = medId;
			if(StringUtils.isNotBlank(medicalrecordId)){
				medicalrecordId=medicalrecordId.trim();
				}
			String state=admissionService.queryPatientStatInfo(medicalrecordId);
			String json ="";
			if("I".equals(state)){
				List<InpatientInfoNow> infoVo = admissionService.querAdmiss(medicalrecordId);
				json=JSONUtils.toJson(infoVo);
				WebUtils.webSendJSON(json);
			}else{
				json=JSONUtils.toJson(state);
				WebUtils.webSendJSON(json);
			}
		} catch (Exception e) {
			logger.error("ZYGL_ZYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
			e.printStackTrace();
		}
		
	}
	
	/**  
	 * @Description： 查询住院医生下拉框
	 * @Author：tcj
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryZhuyuanDoc")
	public void queryZhuyuanDoc(){
		try {
			List<SysEmployee> syslist=admissionService.queryZhuyuanDoc(q);
			String json=JSONUtils.toJson(syslist);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description：渲染床位状态下拉框
	 * @Author：zhuxiaolu
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "findBedState")
	public void findBedState(){
		try {
			List<BusinessDictionary> list=innerCodeService.getDictionary(type);
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
		}
	}
	
	/**
	 * 
	 * 
	 * <p>员工渲染 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午3:13:54 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午3:13:54 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "getEmpList", results = { @Result(name = "json", type = "json") })
	public void getEmpList(){
		try {
			Map<String, String> map = employeeInInterService.queryEmpCodeAndNameMap();
			String joString = JSONUtils.toJson(map);
			WebUtils.webSendJSON(joString);
		} catch (Exception e) {
			logger.error("ZYGL_ZYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYJZ", "住院管理_住院接诊", "2", "0"), e);
		}
	}
}
