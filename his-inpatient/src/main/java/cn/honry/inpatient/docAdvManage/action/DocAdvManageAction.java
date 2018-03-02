package cn.honry.inpatient.docAdvManage.action;

import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.apache.shiro.SecurityUtils;
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

import cn.honry.base.bean.model.BusinessAdvdrugnature;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.EmrMain;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysDruggraDecontraStrank;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.frequency.service.FrequencyInInterService;
import cn.honry.inner.baseinfo.hospital.service.HospitalInInterService;
import cn.honry.inner.baseinfo.stack.service.StackInInterService;
import cn.honry.inner.drug.drugInfo.service.DrugInfoInInerService;
import cn.honry.inner.drug.stockInfo.service.BusinessStockInfoInInterService;
import cn.honry.inner.emr.emrMain.service.EmrMainInnerService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inpatient.docAdvManage.service.DocAdvManageService;
import cn.honry.inpatient.docAdvManage.vo.AdviceLong;
import cn.honry.inpatient.docAdvManage.vo.ProInfoVo;
import cn.honry.inpatient.docAdvManage.vo.UnitsVo;
import cn.honry.inpatient.doctorAdvice.action.DoctorAdviceAction;
import cn.honry.inpatient.exitNoFee.service.ExitNoFeeService;
import cn.honry.inpatient.inprePay.service.InprePayService;
import cn.honry.report.service.IReportService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;

/**
 * ClassName: DocAdvManageAction.java 
 * @Description: 医嘱管理action
 * @author yeguanqun
 * @date 2015-12-22
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/inpatient/docAdvManage")
@SuppressWarnings({"all"})
public class DocAdvManageAction extends ActionSupport{
	private Logger logger=Logger.getLogger(DocAdvManageAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	
	@Autowired
	@Qualifier(value = "docAdvManageService")
	private DocAdvManageService docAdvManageService;	
	public void setDocAdvManageService(DocAdvManageService docAdvManageService) {
		this.docAdvManageService = docAdvManageService;
	}
	@Autowired
	@Qualifier(value = "exitNoFeeService")
	private ExitNoFeeService exitNoFeeService;	
	public void setExitNoFeeService(ExitNoFeeService exitNoFeeService) {
		this.exitNoFeeService = exitNoFeeService;
	}
	@Autowired
	@Qualifier(value="businessStockInfoInInterService")
	private BusinessStockInfoInInterService  businessStockInfoInInterService;
	public void setBusinessStockInfoInInterService(
			BusinessStockInfoInInterService businessStockInfoInInterService) {
		this.businessStockInfoInInterService = businessStockInfoInInterService;
	}
	@Autowired
	@Qualifier(value = "frequencyInInterService")
	private FrequencyInInterService frequencyInInterService;	
	public void setFrequencyInInterService(FrequencyInInterService frequencyInInterService) {
		this.frequencyInInterService = frequencyInInterService;
	}
	/** 
	* @Fields emrMainInnerService : 电子病历接口 
	*/ 
	@Autowired
	@Qualifier(value = "emrMainInnerService")
	private EmrMainInnerService emrMainInnerService;
	public void setEmrMainInnerService(EmrMainInnerService emrMainInnerService) {
		this.emrMainInnerService = emrMainInnerService;
	}
	
	@Autowired
	@Qualifier(value = "stackInInterService")
	private StackInInterService stackInInterService;	
	public void setStackInInterService(StackInInterService stackInInterService) {
		this.stackInInterService = stackInInterService;
	}
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier(value = "drugInfoInInerService")
	private DrugInfoInInerService drugInfoInInerService;
	public void setDrugInfoInInerService(DrugInfoInInerService drugInfoInInerService) {
		this.drugInfoInInerService = drugInfoInInerService;
	}
	@Autowired
	@Qualifier(value = "inpatientInfoInInterService")
	private InpatientInfoInInterService inpatientInfoInInterService;
	public void setInpatientInfoInInterService(
			InpatientInfoInInterService inpatientInfoInInterService) {
		this.inpatientInfoInInterService = inpatientInfoInInterService;
	}
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	@Autowired
	@Qualifier(value = "inprePayService")
	private InprePayService inprePayService;
	public void setInprePayService(InprePayService inprePayService) {
		this.inprePayService = inprePayService;
	}
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	/**获取医院名称**/
	@Autowired
	@Qualifier(value="hospitalInInterService")
	private HospitalInInterService hospitalInInterService;
	public void setHospitalInInterService(
			HospitalInInterService hospitalInInterService) {
		this.hospitalInInterService = hospitalInInterService;
	}
	@Resource
	private RedisUtil redis;
	/**
	 * 医嘱信息实体类
	 */
	private InpatientOrderNow inpatientOrder;
	/**
	 * 医嘱类型实体类
	 */
	private InpatientKind inpatientKind;
	/**
	 * 科室信息实体类
	 */
	private SysDepartment departmentSerch;
	/**
	 * 用户信息实体类
	 */
	private User user;
	/**
	 * 项目信息显示Vo
	 */
	private ProInfoVo proInfoVo;
	private String currentLoginDept;
	
	public String getCurrentLoginDept() {
		return currentLoginDept;
	}
	public void setCurrentLoginDept(String currentLoginDept) {
		this.currentLoginDept = currentLoginDept;
	}
	public InpatientOrderNow getInpatientOrder() {
		return inpatientOrder;
	}
	public void setInpatientOrder(InpatientOrderNow inpatientOrder) {
		this.inpatientOrder = inpatientOrder;
	}
	public InpatientKind getInpatientKind() {
		return inpatientKind;
	}
	public void setInpatientKind(InpatientKind inpatientKind) {
		this.inpatientKind = inpatientKind;
	}
	public SysDepartment getDepartmentSerch() {
		return departmentSerch;
	}
	public void setDepartmentSerch(SysDepartment departmentSerch) {
		this.departmentSerch = departmentSerch;
	}	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public ProInfoVo getProInfoVo() {
		return proInfoVo;
	}
	public void setProInfoVo(ProInfoVo proInfoVo) {
		this.proInfoVo = proInfoVo;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private String page;//起始页数
	private String rows;//数据列数
	private String id;
	/**药房Id**/
	private String pharmacyId;
	//住院流水号
	private String inpatientNo;
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	/** 
	* @Fields emrId : 电子病历ID
	*/ 
	private String emrId;
	/** 
	 * @Fields medicalrecordId : 患者病历号 
	 */ 
	private String medicalrecordId;
	/** 
	* @Fields emrName : 电子病历查询参数 
	*/ 
	private String emrName;
	/** 
	 * @Fields name : 电子病历患者姓名 
	 */ 
	private String name;
	
	public String getEmrId() {
		return emrId;
	}
	public void setEmrId(String emrId) {
		this.emrId = emrId;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getEmrName() {
		return emrName;
	}
	public void setEmrName(String emrName) {
		this.emrName = emrName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String menuAlias;
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
	private String recordId;
	
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPharmacyId() {
		return pharmacyId;
	}
	public void setPharmacyId(String pharmacyId) {
		this.pharmacyId = pharmacyId;
	}
	/**
	 * 页面传的医嘱json数据
	 */
	private String adviceJson;
	
	public String getAdviceJson() {
		return adviceJson;
	}
	public void setAdviceJson(String adviceJson) {
		this.adviceJson = adviceJson;
	}
	/**
	 * 下拉列表参数
	 */
	private String q;
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	/**
	  * 是否是收费类型   (1 收费  2是医嘱)
	  */
  private String drugType;
	/**
	 * 医嘱类型    需传是住院还是门诊
	 */
	private String type; 
	public String getDrugType() {
		return drugType;
	}
	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	//开始时间
	private String startTime;
	//结束时间
	private String endTime;
	//条件参数
	private String condition;
	//门诊号
	private String clinicCode;
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/***
	 * 公共编码资料service实现层
	 */
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
    public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	
	
	/**
	 * @Description:获取医嘱管理页面
	 * @Author：  yeguanqun
	 * @CreateDate： 2015-12-21
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"YZGLX:function:view"})
	@Action(value="docAdvManageInfo",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/docAdvManage/docAdvManage.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String docAdvManageInfo()throws Exception{
		try {
			user = (User)SessionUtils.getCurrentUserFromShiroSession();
			departmentSerch = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
		return "list";
	}
	/**
	 * 查询医嘱资料
	 * 
	 */
	@Action(value="queryInpatientOrder")
	public void queryInpatientOrder(){
		try {
			int decmpsState = inpatientOrder.getDecmpsState();
			List<InpatientOrder> inpatientOrderList = docAdvManageService.queryInpatientOrder(String.valueOf(decmpsState),inpatientOrder.getInpatientNo(),recordId);
			String json = JSONUtils.toJson(inpatientOrderList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询公费患者项目范围的特殊字符标识
	 * 
	 */
	@Action(value="queryDrugOrNodrug",results={@Result(name="json",type="json")} )
	public void queryDrugOrNodrug(){		
		try {
			String itemType="2";
			String json = docAdvManageService.queryDrugOrNodrug(itemType);
			WebUtils.webSendString(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}

	/**
	 * 查询计量单位资料
	 * 
	 */
	@Action(value="queryUnits")
	public void queryUnits(){
		try {
			List<UnitsVo> unitsList = docAdvManageService.queryUnits();
			String json = JSONUtils.toJson(unitsList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询频次资料
	 * 
	 */
	@Action(value="queryFrequencyList")
	public void queryFrequencyList(){
		try {
			List<BusinessFrequency> frequencyList = docAdvManageService.queryFrequencyList();
			String json = JSONUtils.toJson(frequencyList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：医嘱管理-跳转到项目信息页面
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-27 上午11:34:22  
	 * @Modifier：yeguanqun
	 * @ModifyDate：2015-12-27 上午11:34:22  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryDrugOrUndrugInfo", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManage/proInfo.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryDrugOrUndrugInfo() {
		try {
			User user1 = (User)SessionUtils.getCurrentUserFromShiroSession();//获取登录人
			SysEmployee curEmployee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
			SysDepartment  loginDept = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室
			String name="";
			String sysTypeName="";
			name=java.net.URLDecoder.decode(inpatientOrder.getItemName(), "UTF-8");
			sysTypeName=java.net.URLDecoder.decode(inpatientOrder.getClassName(), "UTF-8");
			
			inpatientOrder.setItemName(name);
			inpatientOrder.setClassName(sysTypeName);
			inpatientOrder.setDocCode(curEmployee.getJobNo());
			inpatientOrder.setDocName(user1.getName());
			inpatientOrder.setListDpcd(loginDept.getDeptCode());
			inpatientOrder.setRecUsercd(curEmployee.getJobNo());
			inpatientOrder.setRecUsernm(user1.getName());
		} catch (Exception e) {	
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}	
		return "list";
	}
	/**
	 * 查询项目信息资料
	 * 
	 */
	@Action(value="queryDrugOrUndrugInfos")
	public void queryDrugOrUndrugInfos(){
		try {
			List<ProInfoVo> sysInfoList = docAdvManageService.querySysInfo(page,rows,q,inpatientOrder.getClassCode(),inpatientOrder.getClassName(),inpatientOrder.getTypeCode(),null);
			int total = docAdvManageService.querySysInfoTotal(inpatientOrder.getItemName(),inpatientOrder.getClassCode(),inpatientOrder.getClassName(),inpatientOrder.getTypeCode(),null);
			Map<String, Object> outmap=new HashMap<String, Object>();
			outmap.put("total", total);
			outmap.put("rows", sysInfoList);
			String json=JSONUtils.toJson(outmap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询包装单位信息资料
	 * 
	 */
	@Action(value="queryDrugpackagingunit")
	public void queryDrugpackagingunit(){
		try {
			Map<String,String> drugpackagingunitMap = innerCodeService.getBusDictionaryMap("packunit");
			String json = JSONUtils.toJson(drugpackagingunitMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询最小单位信息资料
	 * 
	 */
	@Action(value="queryMinunit")
	public void queryMinunit(){
		try {
			Map<String,String> drugpackagingunitMap = innerCodeService.getBusDictionaryMap("minunit");
			String json = JSONUtils.toJson(drugpackagingunitMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询非药品单位信息资料
	 * 
	 */
	@Action(value="queryNonmedicineencoding")
	public void queryNonmedicineencoding(){
		try {
			Map<String,String> nonmedicineencodingMap = docAdvManageService.queryNonmedicineencoding();
			String json = JSONUtils.toJson(nonmedicineencodingMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询剂量单位信息资料（暂废）
	 * 
	 */
	@Action(value="queryDrugdoseunit")
	public void queryDrugdoseunit(){
		
		try {
			Map<String,String> drugdoseunitMap = innerCodeService.getBusDictionaryMap("doseUnit");
			String json = JSONUtils.toJson(drugdoseunitMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询药品等级信息资料
	 * 
	 */
	@Action(value="queryDruggrade")
	public void queryDruggrade(){
		try {
			Map<String,String> codeDruggradeMap = docAdvManageService.queryDruggrade();
			String json = JSONUtils.toJson(codeDruggradeMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询执行科室信息资料
	 * 
	 */
	@Action(value="queryImplDepartment")
	public void queryImplDepartment(){
		try {
			Map<String,String> implDepartmentMap = deptInInterService.querydeptCodeAndNameMap();
			String json = JSONUtils.toJson(implDepartmentMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询疾病分类信息资料
	 * 
	 */
	@Action(value="queryDiseasetype")
	public void queryDiseasetype(){
		try {
			List<BusinessDictionary> diseasetypeList = innerCodeService.getDictionary("diseasetype");
			String json = JSONUtils.toJson(diseasetypeList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询库存信息资料
	 * 
	 */
	@Action(value="queryDrugStorage")
	public void queryDrugStorage(){
		try {
			Map<String,String> drugStorageMap = docAdvManageService.queryDrugStorage();
			String json = JSONUtils.toJson(drugStorageMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询系统类别信息资料
	 * 
	 */
	@Action(value="querySystemtype")
	public void querySystemtype(){
		try {
			Map<String,String> systemtypeMap = docAdvManageService.querySystemtype();
			String json = JSONUtils.toJson(systemtypeMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询频次信息资料
	 * 
	 */
	@Action(value="queryFrequency")
	public void queryFrequency(){	
		try {
			Map<String,String> frequencyMap = docAdvManageService.queryFrequency();
			String json = JSONUtils.toJson(frequencyMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询用法信息资料
	 * 
	 */
	@Action(value="queryDrugUsemode")
	public void queryDrugUsemode(){
		try {
			Map<String,String> usemodeMap = docAdvManageService.queryDrugUsemode();
			String json = JSONUtils.toJson(usemodeMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description： 保存医嘱列表新增行数据
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-29 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"YZGLX:function:save"})
	@Action(value = "saveInpatientOrder", results = { @Result(name = "json", type = "json") })
	public void saveInpatientOrder() throws Exception {
		Map<String,Object> map=new HashMap<String,Object>();
		InpatientOrderNow inpatientOrder = new InpatientOrderNow();
		PrintWriter out = WebUtils.getResponse().getWriter();
		request.setCharacterEncoding("gb2312");
		String str = request.getParameter("str");
		String inpatientNo = request.getParameter("inpatientNo");
		String patientNo = request.getParameter("patientNo");
		String deptCode = request.getParameter("deptCode");		
		String nurseCellCode = request.getParameter("nurseCellCode");
		String babyFlag = request.getParameter("babyFlag");
		String decmpsState = request.getParameter("decmpsState");
		int happenNo=0;
		if("1".equals(babyFlag)){
			happenNo=0;
		}
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
		try{
			JSONArray jsonArray = JSONArray.fromObject(str);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObj = JSONObject.fromObject(jsonArray.getString(i));
				String itemType = jsonObj.getString("itemType");
				if("1".equals(itemType)){
					String itemCode = jsonObj.getString("itemCode");
					String itemName = jsonObj.getString("itemName");
					String pharmacyCode = jsonObj.getString("pharmacyCode");//扣库科室
					String typeCode = jsonObj.getString("typeCode");//医嘱类型
					String usageCode = jsonObj.getString("usageCode");//用法
					String result = docAdvManageService.dispensingSf(itemCode,pharmacyCode,typeCode,usageCode);
					if("1".equals(result)){
						map.put("resMsg", "error");
						map.put("resCode", itemName+"已不存在!");
						break;
					}else if("success".equals(result)){
					}else{
						map.put("resMsg", "error");
						map.put("resCode", itemName+result);
						break;
					}
					double qtyTot = 0;
					if(StringUtils.isNotBlank(jsonObj.getString("qtyTot"))){
						qtyTot = Double.parseDouble(jsonObj.getString("qtyTot"));//总量
					}
					//判断药品状态是否可用及库存是否充足
					List<String> drugCodes= new ArrayList<>();//申请药品编码list
					drugCodes.add(itemCode);
					List<Double> applyNums= new ArrayList<>();//申请数量list
					applyNums.add(qtyTot);
					
					List<Integer> showFlags=new ArrayList<>();//申请数量list
					showFlags.add(1);
					Map<String, Object> valiuDrugMap = businessStockInfoInInterService.ynValiuDrug(pharmacyCode, drugCodes, applyNums, showFlags, false, true, true);
					String flag = valiuDrugMap.get("valiuFlag").toString();
					if("0".equals(flag)){//库存不足
						map.put("resMsg", "error");
						map.put("resCode", valiuDrugMap.get("failMesgs").toString());
					}
				}
			}
			if(map.size()>0 &&"error".equals(map.get("resMsg"))){				
				out.write(gson.toJson(map));
			}else{
				inpatientOrder.setInpatientNo(inpatientNo);//住院号
				inpatientOrder.setPatientNo(patientNo);//住院病历号
				inpatientOrder.setDeptCode(deptCode);
				inpatientOrder.setNurseCellCode(nurseCellCode);//住院护理站代码 	
				inpatientOrder.setBabyFlag(Integer.valueOf(babyFlag));//婴儿标记
				inpatientOrder.setHappenNo(happenNo);//婴儿序号
				inpatientOrder.setDecmpsState(Integer.parseInt(decmpsState));
				inpatientOrder.setHospitalId(HisParameters.CURRENTHOSPITALID);
				inpatientOrder.setAreaCode(inprePayService.getDeptArea(deptCode));
				map = docAdvManageService.saveOrUpdateInpatientOrder(inpatientOrder,str);
				out.write(gson.toJson(map));
				out.close();
			}
		}catch(Exception e){
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);		
		}
	}
	/**
	 * 查询药品等级医生职级对照
	 * 
	 */
	@Action(value="queryDruggraDecontraStrank")
	public void queryDruggraDecontraStrank(){
		try {
			String userId = request.getParameter("userId");
			String drugGrade = request.getParameter("drugGrade");
			List<SysDruggraDecontraStrank> druggraDecontraStrank = docAdvManageService.queryDruggraDecontraStrank(userId, drugGrade);
			String json = JSONUtils.toJson(druggraDecontraStrank);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询长期医嘱限制性药品性质表
	 * 
	 */
	@Action(value="queryAdvdrugnature")
	public void queryAdvdrugnature(){		
		try {
			List<BusinessAdvdrugnature> advdrugnature = docAdvManageService.queryAdvdrugnature(proInfoVo.getDrugNature());
			String json = JSONUtils.toJson(advdrugnature);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询历史医嘱资料
	 * 
	 */
	@Action(value="queryInpatientOrderHis",results={@Result(name="json",type="json")} )
	public void queryInpatientOrderHis(){
		try {
			Map<String, Object> map=new HashMap<String, Object>();
			int total = 0;
			List<InpatientOrderNow> inpatientOrderList = null;
			if(StringUtils.isBlank(inpatientOrder.getInpatientNo())){
				inpatientOrderList = new ArrayList<InpatientOrderNow>();
			}else{
				total = docAdvManageService.getTotal(inpatientOrder,recordId);
				inpatientOrderList = docAdvManageService.getPage(page,rows,inpatientOrder,recordId);
			}
			map.put("total", total);
			map.put("rows", inpatientOrderList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 跳转到科室查询界面
	 * 
	 */
	@Action(value = "queryExeDept", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManage/exeDept.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryExeDept() {	
		String deptName=inpatientOrder.getExecDpnm();
		String name="";
		try {
			name = new String(deptName.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
		inpatientOrder.setExecDpnm(name);
		return "list";
	}
	/**
	 * 查询科室信息
	 * 
	 */
	@Action(value="querySysDepartment",results={@Result(name="json",type="json")} )
	public void querySysDepartment(){
		try {
			Map<String, Object> map=new HashMap<String, Object>();
			if(departmentSerch==null){
				departmentSerch = new SysDepartment();
			}
			List<SysDepartment> departmentList = docAdvManageService.queryPage(page,rows,departmentSerch);
			int total = docAdvManageService.queryTotal(departmentSerch);
			map.put("total", total);
			map.put("rows", departmentList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 医生职级是否受到开药职级限制
	 * 
	 */
	@Action(value="queryAuditInfo")
	public void queryAuditInfo(){		
		try {
			String userId = request.getParameter("userId");
			int a = 0;
			if(StringUtils.isBlank(userId)){
				a = 0;
			}else{
				a = docAdvManageService.queryAuditInfo(userId, "yzshzj");
			}
			WebUtils.webSendString(a+"");
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * @Description:获取审核医嘱页面
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-1-13
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"YZGLX:function:view"})
	@Action(value="auditAdviceInfo",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/docAdvManage/auditAdvice.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String auditAdviceInfo()throws Exception{
		return "list";
	}
	/**  
	 *  
	 * @Description： 上级医生审核医嘱
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-14 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "auditAdvice", results = { @Result(name = "json", type = "json") })
	public void auditAdvice() throws Exception {
		String auditValue = request.getParameter("auditValue");
		String id = request.getParameter("id");
		String adExplain = request.getParameter("adExplain");
		try{
			InpatientOrderNow inpatientOrderNow = new InpatientOrderNow();
			inpatientOrderNow.setId(id);
			inpatientOrderNow.setMoStat(Integer.parseInt(auditValue));
			if(StringUtils.isNotBlank(adExplain)){
				inpatientOrderNow.setMoNote1(adExplain);
			}			
			docAdvManageService.updateMoStat(inpatientOrderNow);
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendString("error");
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：  删除医嘱(作废)
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-17 10:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"YZGLX:function:delete"}) 
	@Action(value = "delDocAdvInfo",  interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void delDocAdvInfo() throws Exception {
		Map<String,String> resMap=new HashMap<String,String>();
		try{
			docAdvManageService.delDocAdvInfo(inpatientOrder.getCombNo());
			resMap.put("resCode", "success");
			resMap.put("resMsg", "删除成功!");
		}catch(Exception e){			
			resMap.put("resCode", "error");
			resMap.put("resMsg", "删除失败!");
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
			
		}
		String json=JSONUtils.toJson(resMap);
		WebUtils.webSendJSON(json);
	}
	/**
	 * @Description:获取停止医嘱页面
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-1-14
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"YZGLX:function:view"})
	@Action(value="stopAdviceInfo",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/docAdvManage/stopAdvice.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String stopAdviceInfo()throws Exception{
		return "list";
	}
	
	/**  
	 *  
	 * @Description： 作废医嘱
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-15 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "stopAdvice", results = { @Result(name = "json", type = "json") })
	public void stopAdvice() throws Exception {
		String timeFlag = request.getParameter("timeFlag");
		String advStopTime = request.getParameter("advStopTime");
		String stopTime =advStopTime+":00";	
		String advStopReasonId = request.getParameter("advStopReasonId");
		String advStopReason = request.getParameter("advStopReason");
		try{
			docAdvManageService.obsoleteAdvice(inpatientOrder,timeFlag,stopTime,advStopReasonId,advStopReason,adviceJson);
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendString("error");
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * @Description:获取特殊频次页面
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-1-18
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"YZGLX:function:view"})
	@Action(value="speFreInfo",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/docAdvManage/speFreInfo.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String speFreInfo()throws Exception{
		try {
			String frequencyName = inpatientOrder.getFrequencyName();
			String execTimes = inpatientOrder.getExecTimes();
			String execDose = inpatientOrder.getExecDose();
			double doseOnce = inpatientOrder.getDoseOnce();
			if(StringUtils.isNotBlank(execTimes) && !"undefined".equals(execTimes)){
				String[] arytimes = execTimes.split(",");//调用API方法按照逗号分隔字符串
				String arytimes1=arytimes[0];	
				request.setAttribute("arytimes1",arytimes1);
				if(arytimes.length==2){
					String arytimes2=arytimes[1];
					request.setAttribute("arytimes2",arytimes2);
				}
				if(arytimes.length==3){
					String arytimes2=arytimes[1];
					request.setAttribute("arytimes2",arytimes2);
					String arytimes3=arytimes[2];
					request.setAttribute("arytimes3",arytimes3);
				}
				if(arytimes.length==4){
					String arytimes2=arytimes[1];
					request.setAttribute("arytimes2",arytimes2);
					String arytimes3=arytimes[2];
					request.setAttribute("arytimes3",arytimes3);
					String arytimes4=arytimes[3];
					request.setAttribute("arytimes4",arytimes4);
				}
				if(arytimes.length==5){
					String arytimes2=arytimes[1];
					request.setAttribute("arytimes2",arytimes2);
					String arytimes3=arytimes[2];
					request.setAttribute("arytimes3",arytimes3);
					String arytimes4=arytimes[3];
					request.setAttribute("arytimes4",arytimes4);
					String arytimes5=arytimes[4];
					request.setAttribute("arytimes5",arytimes5);
				}																							
			}
			if(StringUtils.isNotBlank(execDose) &&!"undefined".equals(execDose)){
				String[] arydose = execDose.split(",");//调用API方法按照逗号分隔字符串
				String arydose1=arydose[0];	
				request.setAttribute("arydose1",arydose1);
				if(arydose.length==2){
					String arydose2=arydose[1];
					request.setAttribute("arydose2",arydose2);
				}
				if(arydose.length==3){
					String arydose2=arydose[1];
					request.setAttribute("arydose2",arydose2);
					String arydose3=arydose[2];
					request.setAttribute("arydose3",arydose3);
				}
				if(arydose.length==4){
					String arydose2=arydose[1];
					request.setAttribute("arydose2",arydose2);
					String arydose3=arydose[2];
					request.setAttribute("arydose3",arydose3);
					String arydose4=arydose[3];
					request.setAttribute("arydose4",arydose4);
				}
				if(arydose.length==5){
					String arydose2=arydose[1];
					request.setAttribute("arydose2",arydose2);
					String arydose3=arydose[2];
					request.setAttribute("arydose3",arydose3);
					String arydose4=arydose[3];
					request.setAttribute("arydose4",arydose4);
					String arydose5=arydose[4];
					request.setAttribute("arydose5",arydose5);
				}																							
			}									
			String name="";
				name=new String(frequencyName.getBytes("ISO-8859-1"),"UTF-8");			
			inpatientOrder.setFrequencyName(name);	
			if((execTimes==null&&execDose==null)||("".equals(execTimes)&&"".equals(execDose))||("undefined".equals(execTimes)&&"undefined".equals(execDose))){
				List<BusinessFrequency> freList = docAdvManageService.queryBusinessFrequency(inpatientOrder.getFrequencyCode());
				String[] ary = null;
				if(freList!=null && freList.size()>0){
					if(freList.get(0).getPeriod()!=null){
						ary = freList.get(0).getPeriod().split(",");//调用API方法按照逗号分隔字符串
					}			
				}				
				if(ary!=null && ary.length>0){
					if(ary.length==1){
						request.setAttribute("arytimes1",ary[0]);
						request.setAttribute("arydose1",doseOnce);
					}
					if(ary.length==2){
						request.setAttribute("arytimes1",ary[0]);
						request.setAttribute("arytimes2",ary[1]);
						request.setAttribute("arydose1",doseOnce);
						request.setAttribute("arydose2",doseOnce);
					}
					if(ary.length==3){
						request.setAttribute("arytimes1",ary[0]);
						request.setAttribute("arytimes2",ary[1]);
						request.setAttribute("arytimes3",ary[2]);
						request.setAttribute("arydose1",doseOnce);
						request.setAttribute("arydose2",doseOnce);
						request.setAttribute("arydose3",doseOnce);
					}
					if(ary.length==4){
						request.setAttribute("arytimes1",ary[0]);
						request.setAttribute("arytimes2",ary[1]);
						request.setAttribute("arytimes3",ary[2]);
						request.setAttribute("arytimes4",ary[3]);
						request.setAttribute("arydose1",doseOnce);
						request.setAttribute("arydose2",doseOnce);
						request.setAttribute("arydose3",doseOnce);
						request.setAttribute("arydose4",doseOnce);
					}
					if(ary.length==5){
						request.setAttribute("arytimes1",ary[0]);
						request.setAttribute("arytimes2",ary[1]);
						request.setAttribute("arytimes3",ary[2]);
						request.setAttribute("arytimes4",ary[3]);
						request.setAttribute("arytimes5",ary[4]);
						request.setAttribute("arydose1",doseOnce);
						request.setAttribute("arydose2",doseOnce);
						request.setAttribute("arydose3",doseOnce);
						request.setAttribute("arydose4",doseOnce);
						request.setAttribute("arydose5",doseOnce);
					}
				}
			}
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
		return "list";
	}
	/**  
	 *  
	 * @Description：  医嘱管理-打开组套详情
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-20 上午11:34:22   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "viewStackInfo", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManage/stackInfo.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewStackInfo() {	
		try {
			User user1 = (User)SessionUtils.getCurrentUserFromShiroSession();//获取登录人
			SysEmployee curEmployee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
			SysDepartment  loginDept = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室
			inpatientOrder.setDocCode(curEmployee.getJobNo());
			inpatientOrder.setDocName(user1.getName());
			inpatientOrder.setListDpcd(loginDept.getDeptCode());
			inpatientOrder.setRecUsercd(curEmployee.getJobNo());
			inpatientOrder.setRecUsernm(user1.getName());
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
		return "list";
	}
	/**  
	 *  
	 * @Description：  医嘱管理-打开组套详情(新)
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-20 上午11:34:22   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "viewStackInfoNew", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManageNew/stackInfoNew.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewStackInfoNew() {	
		try {
			User user1 = (User)SessionUtils.getCurrentUserFromShiroSession();//获取登录人
			SysEmployee curEmployee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
			SysDepartment  loginDept = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室
			inpatientOrder.setDocCode(curEmployee.getJobNo());
			inpatientOrder.setDocName(user1.getName());
			inpatientOrder.setListDpcd(loginDept.getDeptCode());
			inpatientOrder.setRecUsercd(curEmployee.getJobNo());
			inpatientOrder.setRecUsernm(user1.getName());
		} catch (Exception e) {
			e.printStackTrace();logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
		return "list";
	}
	/**  
	 *  
	 * @Description：  医嘱管理-打开草药详情
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-20 上午11:34:22  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "getChinMediModle", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManage/chinMediInfo.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String getChinMediModle() {	
		try {
			request.setAttribute("staDate", new Date());
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
		return "list";
	}
	/**  
	 *  
	 * @Description：  医嘱管理-打开草药详情（新）
	 * @Author：donghe
	 * @CreateDate：2016-4-20 上午11:34:22  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "getChinMediModleNew", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManageNew/chinMediInfo.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String getChinMediModleNew() {	
		try {
			request.setAttribute("staDate", new Date());
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
		return "list";
	}
	/**
	 * 查询频次时间点
	 * 
	 */
	@Action(value="queryFrePeriod")
	public void queryFrePeriod(){
		try {
			if(inpatientOrder==null){
				inpatientOrder = new InpatientOrderNow();
			}
			List<BusinessFrequency> freList = docAdvManageService.queryBusinessFrequency(inpatientOrder.getFrequencyCode());
			String[] ary = null;
			int a = 0;
			if(freList!=null && freList.size()>0){
				ary = freList.get(0).getPeriod().split(",");//调用API方法按照逗号分隔字符串
				a = ary.length;
			}				
			String json = JSONUtils.toJson(a);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description： 保存特殊频次
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-20 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "saveSpeFreInfo", results = { @Result(name = "json", type = "json") })
	public void saveSpeFreInfo() throws Exception {		
		try{					
			docAdvManageService.updateSpeFreInfo(inpatientOrder);
			WebUtils.webSendString("success");
		}catch(Exception e){			
			WebUtils.webSendString("error");
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 获得渲染数据-检查部位List
	 * 
	 */
	@Action(value="queryCheckpoint")
	public void queryCheckpoint(){		
		try {
			List<BusinessDictionary> checkpointList = innerCodeService.getDictionary("checkpoint");
			String json = JSONUtils.toJson(checkpointList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 获得渲染数据-检查部位Map
	 * 
	 */
	@Action(value="queryCheckpointMap")
	public void queryCheckpointMap(){		
		try {
			Map<String, String> checkpointMap = docAdvManageService.queryCheckpointMap();
			String json = JSONUtils.toJson(checkpointMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 获得渲染数据-样本类型List
	 * 
	 */
	@Action(value="querySampleTept")
	public void querySampleTept(){		
		try {
			List<BusinessDictionary> sampleTeptList = innerCodeService.getDictionary("laboratorysample");
			String json = JSONUtils.toJson(sampleTeptList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 获得渲染数据-样本类型Map
	 * 
	 */
	@Action(value="querySampleTeptMap")
	public void querySampleTeptMap(){		
		try {
			Map<String, String> sampleTeptMap = docAdvManageService.querySampleTeptMap();
			String json = JSONUtils.toJson(sampleTeptMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 获得医嘱类型数据
	 * 
	 */
	@Action(value="queryDocAdvType")
	public void queryDocAdvType(){		
		try {
			if(inpatientKind==null){
				inpatientKind = new InpatientKind();
			}
			List<InpatientKind> kind = docAdvManageService.queryDocAdvType(inpatientKind);
			String json = JSONUtils.toJson(kind);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 查询合同单位List
	 * 
	 */
	@Action(value = "queryReglist",results={@Result(name="json",type="json")})
	public void queryReglist()throws Exception {
		try {
			List<BusinessContractunit> regconLists = docAdvManageService.queryReglist();
			String json = JSONUtils.toJson(regconLists);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 获得渲染数据-医嘱常用类型Map
	 * 
	 */
	@Action(value="queryOpendocadvmarkMap")
	public void queryOpendocadvmarkMap(){		
		try {
			Map<String, String> opendocadvmarkMap = innerCodeService.getBusDictionaryMap("opendocadvmark");
			String json = JSONUtils.toJson(opendocadvmarkMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * 获取药品查询界面
	 * 
	 */
	@Action(value = "queryDrugInfo", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManage/drugs.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryDrugInfo() {	
		return "list";
	}
	/**
	 * 获取非药品查询界面
	 * 
	 */
	@Action(value = "queryUndrugInfo", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManage/nonDrugs.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryUndrugInfo() {	
		return "list";
	}
	/**  
	 *  
	 * @Description：医嘱调用组套树
	 * @Author：yeguanqun
	 * @CreateDate：2016-7-26  
	 * @version 1.0
	 *
	 */
	@Action(value = "stackAndStackInfoForTree")
	public void stackAndStackInfoForTree() {
		try {
			String deptId=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
			String userId=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			List<TreeJson> list = stackInInterService.stackTree(id,deptId,userId,drugType,type);
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  保存药房信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午07:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午07:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "savePharmacyInfo")
	public void savePharmacyInfo(){
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			SysDepartment dept = deptInInterService.getByCode(pharmacyId);
			SecurityUtils.getSubject().getSession().removeAttribute(SessionUtils.SESSIONPHARMACY);
			SecurityUtils.getSubject().getSession().setAttribute(SessionUtils.SESSIONPHARMACY,dept);
			map.put("resMsg", "success");
			map.put("resCode", "药房选择成功!");
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "未知错误请联系管理员!");
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  获得渲染数据-单位
	 * @Author：yeguanqun
	 * @CreateDate：2016-08-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryUnitsList")
	public void queryUnitsList(){
		try {
			List<BusinessDictionary> list = innerCodeService.getDictionary("packunit");
			for (BusinessDictionary businessDictionary : list) {
				businessDictionary.setEncode("B"+businessDictionary.getEncode());
			}
			List<BusinessDictionary> list1 = innerCodeService.getDictionary("minunit");
			for (BusinessDictionary businessDictionary : list1) {
				businessDictionary.setEncode("Z"+businessDictionary.getEncode());
			}
			list.addAll(list1);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/** 电子病历查询
	* @Title: outpatientEmrList 电子病历查询
	* @author dtl 
	* @date 2017年4月5日
	*/
	@Action(value = "emrList")
	public void emrList(){
		try {
			List<EmrMain> list = new ArrayList<EmrMain>();
			Map<String, Object> map = new HashMap<>();
			int total = emrMainInnerService.emrCount(medicalrecordId,emrName);
			map.put("total", total);
			if (total != 0) {
				list = emrMainInnerService.emrList(medicalrecordId,emrName,name,rows,page);
			}
			map.put("rows", list);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	
	/** 电子病历查询
	 * @Title: outpatientEmrList 电子病历查询
	 * @author dtl 
	 * @date 2017年4月5日
	 */
	@Action(value = "getEmr")
	public void getEmr(){
		try {
			EmrMain emrMain = emrMainInnerService.get(emrId);
			if (StringUtils.isNotBlank(emrMain.getStrContent())) {
				WebUtils.webSendString(emrMain.getStrContent());
			}
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}

	/**
	 * @Description:获取医嘱管理(新)页面
	 * @Author：  donghe
	 * @CreateDate： 2016-12-15
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"YZGLX:function:view"})
	@Action(value="docAdvManageInfoNew",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/docAdvManageNew/index.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String docAdvManageInfoNew()throws Exception{
		try {
			user = (User)SessionUtils.getCurrentUserFromShiroSession();
			departmentSerch = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室
			if(departmentSerch==null){
				currentLoginDept="noSelectDept";
			}
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
		return "list";
	}
	/**
	 * @Description:获取药品信息
	 * @Author：  donghe
	 * @CreateDate： 2016-12-15
	 * @return String  
	 * @version 1.0
	**/
	@Action(value="docAdvManageDrugInfo")
	public void docAdvManageDrugInfo() throws Exception{
		try {
			Map<String, Object> map=new HashMap<String, Object>();
			List<DrugInfo> infos = drugInfoInInerService.getPage(page, rows, null);
			int total = drugInfoInInerService.getTotal(null);
			map.put("total", total);
			map.put("rows", infos);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * @Description:获取拆分属性
	 * @Author：  donghe
	 * @CreateDate： 2017-1-15
	 * @return String  
	 * @version 1.0
	**/
	@Action(value="querysplitDrug")
	public void querysplitDrug() throws Exception{
		try {
			Map<String, Object> map=new HashMap<String, Object>();
			/**业务变更 拆分属性-可拆分 **/
			map.put("splitDrug",HisParameters.SPLITDRUG);
			/**业务变更 拆分属性维护-可拆分 **/
			map.put("splitDrugArr",HisParameters.SPLITDRUGARR);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * @Description:根据住院流水号查询住院信息
	 * @Author：  donghe
	 * @CreateDate： 2016-12-15
	 * @return String  
	 * @version 1.0
	**/
	@Action(value="queryInfo")
	public void queryInfo(){
		try {
			InpatientInfoNow inpatientInfo = inpatientInfoInInterService.queryByInpatientNot(inpatientNo);
			String json = JSONUtils.toJson(inpatientInfo);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * @Description:重整医嘱
	 * @Author：  donghe
	 * @CreateDate： 2017-2-9
	 * @return String  
	 * @version 1.0
	**/
	@Action(value="reformAdvice")
	public void reformAdvice() throws Exception{
		String result = "";
		try {
			result = docAdvManageService.reformAdvice(adviceJson);
		} catch (Exception e) {
			result = "error";
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
		WebUtils.webSendString(result);
	}
	/**  
	 *  
	 * 跳转医嘱页面
	 * @Author：donghe
	 * @CreateDate：2016-1-10
	 * @Modifier：donghe
	 * @ModifyDate：2016-1-10
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	
	@Action(value = "advice", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManageNew/docAdvDetailNew.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String advice() {
		try {
			user = (User)SessionUtils.getCurrentUserFromShiroSession();
			departmentSerch = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
		return "list";
	}
	/**  
	 *  
	 * 跳转历史医嘱页面
	 * @Author：donghe
	 * @CreateDate：2016-1-10
	 * @Modifier：donghe
	 * @ModifyDate：2016-1-10
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "hisAdvice", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManageNew/hisDocAdvDetail.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String hisAdvice() {
		return "list";
	}
	/**  
	 *  
	 * 跳转药品页面
	 * @Author：donghe
	 * @CreateDate：2016-1-10
	 * @Modifier：donghe
	 * @ModifyDate：2016-1-10
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "drug", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManageNew/drug.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String drug() {
		return "list";
	}
	/**  
	 *  
	 * 跳转药品页面
	 * @Author：donghe
	 * @CreateDate：2016-1-10
	 * @Modifier：donghe
	 * @ModifyDate：2016-1-10
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "unDrug", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManageNew/unDrug.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String unDrug() {
		return "list";
	}
	/**  
	 *  
	 * 跳转lis页面
	 * @Author：donghe
	 * @CreateDate：2016-1-10
	 * @Modifier：donghe
	 * @ModifyDate：2016-1-10
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "lisView", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManageNew/lis.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String lisView() {
		return "list";
	}
	/**  
	 *  
	 * 跳转pacs页面
	 * @Author：donghe
	 * @CreateDate：2016-1-10
	 * @Modifier：donghe
	 * @ModifyDate：2016-1-10
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "pacsView", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManageNew/pacs.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String pacsView() {
		return "list";
	}
	/**  
	 *  
	 * 跳转pacs页面
	 * @Author：donghe
	 * @CreateDate：2016-1-10
	 * @Modifier：donghe
	 * @ModifyDate：2016-1-10
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "outpatientDetail", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManageNew/outpatientDetail.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String outpatientDetail() {
		try {
			Date now1=DateUtils.getCurrentTime();
			endTime=DateUtils.formatDateY_M_D_H_M_S(now1);
			
			Calendar calendar = Calendar.getInstance();  
			calendar.setTime(now1);  
			calendar.add(Calendar.DAY_OF_MONTH, -2);  
			now1 = calendar.getTime();
			startTime=DateUtils.formatDateY_M_D_H_M_S(now1);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
		return "list";
	}
	/**  
	 *  
	 * 跳转电子病历页面
	 * @Author：donghe
	 * @CreateDate：2016-1-10
	 * @Modifier：donghe
	 * @ModifyDate：2016-1-10
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "viewSelectEme", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManageNew/emr.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewSelectEme() {
		return "list";
	}
	/**  
	 *  
	 * @Description： 跳转到草药查询页面
	 * @Author：donghe
	 * @CreateDate：2016-1-10
	 * @Modifier：donghe
	 * @ModifyDate：2016-1-10  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "viewChinMediModle", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/docAdvManageNew/herbal.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewChinMediModle() {
		return "list";
	}
	/**  
	 *  
	 * @Description：长期医嘱单
	 * @Author：donghe
	 * @CreateDate：2016-3-16  下午11:00
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportToChangqiOrder")
	public void iReportToChangqiOrder() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String inpid= request.getParameter("inpatientNo");//jasper文件所用到的参数 mid
			String flag=request.getParameter("flag");//区别长期医嘱和临时医嘱 如果为1则为长期医嘱 为0位临时医嘱
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			List<AdviceLong> list= docAdvManageService.printAdviceLong(inpid,flag);
			String root_path = request.getSession().getServletContext().getRealPath("/");
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("HOSPITALNAME",HisParameters.PREFIXFILENAME);
			 String reportFilePath=null;
			if("1".equals(flag)){
				parameterMap.put("serviceHopital", "长期医嘱单");
				reportFilePath= root_path + "WEB-INF/reportFormat/jasper/changqiyizhudan.jasper";
			}else{
				parameterMap.put("serviceHopital", "临时医嘱单");
				reportFilePath = root_path + "WEB-INF/reportFormat/jasper/linshiyizhudan.jasper";
			}
			 parameterMap.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
			 iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameterMap, new JRBeanCollectionDataSource(list));
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：临时医嘱单
	 * @Author：donghe
	 * @CreateDate：2016-3-16  下午11:00
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportToLinshiOrder")
	public void iReportToLinshiOrder() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String inpid= request.getParameter("inpatientNo");//jasper文件所用到的参数 mid
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("tid", inpid);
			parameterMap.put("HOSPITALNAME",HisParameters.PREFIXFILENAME);
			
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：历史长期医嘱单
	 * @Author：donghe
	 * @CreateDate：2016-3-16  下午11:00
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportToChangqiOrderHis")
	public void iReportToChangqiOrderHis() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String inpid= request.getParameter("inpatientNo");//jasper文件所用到的参数 mid
			String flag= request.getParameter("flag");//1表示长期0表示临时
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			List<AdviceLong> list=docAdvManageService.printAdvicehis(inpid,flag);
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			String root_path = request.getSession().getServletContext().getRealPath("/");
			String reportFilePath=null;
			if("1".equals(flag)){
				parameterMap.put("serviceHopital", "长期医嘱单");
				reportFilePath= root_path + "WEB-INF/reportFormat/jasper/historychangqiyizhudan.jasper";
			}else{
				parameterMap.put("serviceHopital", "临时医嘱单");
				reportFilePath = root_path + "WEB-INF/reportFormat/jasper/historylinshiyizhudan.jasper";
			}
			parameterMap.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
			parameterMap.put("HOSPITALNAME",HisParameters.PREFIXFILENAME);
			iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameterMap, new JRBeanCollectionDataSource(list));
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：历史临时医嘱单
	 * @Author：donghe
	 * @CreateDate：2016-3-16  下午11:00
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportToLinshiOrderhis")
	public void iReportToLinshiOrderhis() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String inpid= request.getParameter("inpatientNo");//jasper文件所用到的参数 mid
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("tid", inpid);
			parameterMap.put("HOSPITALNAME", hospitalInInterService.getPresentHospital().getName());
			StringBuffer path=new StringBuffer();
			path.append(request.getSession().getServletContext().getRealPath("/"));
			path.append(webPath);
			parameterMap.put("SUBREPORT_DIR", path+"historylinshiyizhudanzibiao.jasper");
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * @Description:查询门诊挂号信息
	 * @Author：  donghe
	 * @CreateDate： 2017-4-15
	 * @return String  
	 * @version 1.0
	**/
	@Action(value="queryoutPatient")
	public void queryoutPatient(){
		try {
			Map<String, Object> map = docAdvManageService.getoutPatient( startTime,  endTime,
					 condition,  type, page, rows);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	/**
	 * @Description:查询门诊挂号信息
	 * @Author：  donghe
	 * @CreateDate： 2017-4-15
	 * @return String  
	 * @version 1.0
	 **/
	@Action(value="queryOutpatientRecipedetail")
	public void queryOutpatientRecipedetail(){
		try {
			String json = null;
			List<OutpatientRecipedetailNow> list= docAdvManageService.queryOutpatientRecipedetail(clinicCode, startTime, endTime);
			if(list != null && list.size() > 0){
				json = JSONUtils.toJson(list);
			}else {
				json = "[]";
			}
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
	@Action(value="queryAllType")
	public void queryAllType(){
		try {
			List<Map<String,String>> list=new ArrayList<>();
			Map<String,String> map1=new HashMap<String,String>();
			Map<String,String> map2=new HashMap<String,String>();
			Map<String,String> map3=new HashMap<String,String>();
			Map<String,String> map4=new HashMap<String,String>();
			map1.put("value", "0");
			map1.put("text", "全部");
			map2.put("value", "1");
			map2.put("text", "病历号");
			map3.put("value","2");
			map3.put("text", "门诊号");
			map4.put("value","3");
			map4.put("text", "姓名");
			list.add(map1);
			list.add(map2);
			list.add(map3);
			list.add(map4);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYYSZ_YZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);
		}
	}
}
