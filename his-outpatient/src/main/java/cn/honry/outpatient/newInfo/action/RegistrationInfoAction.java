package cn.honry.outpatient.newInfo.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.RegisterDocSource;
import cn.honry.base.bean.model.RegisterFee;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.baseinfo.hospital.service.HospitalInInterService;
import cn.honry.inner.outpatient.grade.service.GradeInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.outpatient.info.vo.InfoPatient;
import cn.honry.outpatient.newInfo.service.RegistrationService;
import cn.honry.outpatient.newInfo.vo.EmpInfoVo;
import cn.honry.outpatient.newInfo.vo.InfoStatistics;
import cn.honry.outpatient.newInfo.vo.InfoVo;
import cn.honry.outpatient.newInfo.vo.RegPrintVO;
import cn.honry.report.service.IReportService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/register/newInfo")
public class RegistrationInfoAction  extends ActionSupport{

	private Logger logger=Logger.getLogger(RegistrationInfoAction.class);
	/**
	 * 门诊挂号（新）
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	@Autowired
	@Qualifier(value = "ationService")
	private RegistrationService ationService;
	public void setAtionService(RegistrationService ationService) {
		this.ationService = ationService;
	}
	
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	
	@Autowired
	@Qualifier(value = "hospitalInInterService")
	private HospitalInInterService hospitalInInterService;
	public void setHospitalInInterService(HospitalInInterService hospitalInInterService) {
		this.hospitalInInterService = hospitalInInterService;
	}
	
	@Autowired
	@Qualifier(value = "gradeInInterService")
	private GradeInInterService gradeInInterService;
	public void setGradeInInterService(GradeInInterService gradeInInterService) {
		this.gradeInInterService = gradeInInterService;
	}

	
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	private RegisterDocSource registerDocSource;
	
	
	public RegisterDocSource getRegisterDocSource() {
		return registerDocSource;
	}

	public void setRegisterDocSource(RegisterDocSource registerDocSource) {
		this.registerDocSource = registerDocSource;
	}

	private RegistrationNow ationInfo;
	
	public RegistrationNow getAtionInfo() {
		return ationInfo;
	}

	public void setAtionInfo(RegistrationNow ationInfo) {
		this.ationInfo = ationInfo;
	}

	private RegisterPreregisterNow preregister;
	
	public RegisterPreregisterNow getPreregister() {
		return preregister;
	}

	public void setPreregister(RegisterPreregisterNow preregister) {
		this.preregister = preregister;
	}
	

	private String q;
	
	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		q=q==null?"":q;
		this.q = q.trim();
	}
	
	private String passwords;
	
	public String getPasswords() {
		return passwords;
	}

	public void setPasswords(String passwords) {
		this.passwords = passwords;
	}

	private String speciallimitInfo;
	
	public String getSpeciallimitInfo() {
		return speciallimitInfo;
	}

	public void setSpeciallimitInfo(String speciallimitInfo) {
		this.speciallimitInfo = speciallimitInfo;
	}
	private String page;
	private String rows;
	
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
	private String clinicCode;
	
	public String getClinicCode() {
		return clinicCode;
	}

	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}

	@Resource(name="redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}

	/**  
	 * @Description：  挂号信息列表
	 * @Author：liudelin
	 * @CreateDate：2016-07-07 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "toView", results = { @Result(name = "list", location = "/WEB-INF/pages/register/newInfo/registration.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listRegisterInfo() {
		try {
			List<HospitalParameter> middyParameter = parameterInnerService.getMiddyParameter(HisParameters.WORKTIME);
			Map<String,String> mpMap = new HashMap<String,String>();
			for(HospitalParameter hp : middyParameter){
				String middyNum = hp.getParameterValue();
				String parameterTime = hp.getParameterDownlimit()+","+hp.getParameterUplimit();
				
				switch (middyNum) {
				case "1":
					mpMap.put("1", parameterTime);
					break;
				case "2":
					mpMap.put("2", parameterTime);
					break;
				case "3":
					mpMap.put("3", parameterTime);
					break;
				default:
					break;
				}
			}
			HospitalParameter parameter = ationService.changePay();
			ServletActionContext.getRequest().setAttribute("medicalRecordBookPay", parameter.getParameterValue());
			String json = JSONUtils.toJson(mpMap);
			ServletActionContext.getRequest().setAttribute("mpMap", json);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
		return "list";
	}
	
	/**  
	 * @Description：  挂号类别下拉框
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "typeCombobox")
	public void typeCombobox(){
		try {
			List<BusinessDictionary> codeRegistertypeList = innerCodeService.getDictionary("registerType");
			String mapJosn = JSONUtils.toJson(codeRegistertypeList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description：  挂号级别下拉框
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "gradeCombobox")
	public void gradeCombobox(){
		try {
			List<RegisterGrade> gradeList = gradeInInterService.getCombobox(null, q);
			String mapJosn = JSONUtils.toJson(gradeList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description：  挂号科室下拉框
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "deptCombobox")
	public void deptCombobox(){
		try {
			List<SysDepartment> gradeList = ationService.deptCombobox(q);
			String mapJosn = JSONUtils.toJson(gradeList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description：  挂号专家下拉框
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "empCombobox")
	public void empCombobox(){
		try {
			List<EmpInfoVo> employeeList = ationService.empCombobox(ationInfo.getDeptCode(),ationInfo.getReglevlCode(),ationInfo.getNoonCode(),q);
			String mapJosn = JSONUtils.toJson(employeeList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	@Action(value = "empComboboxAllSupport")
	public void empComboboxAllSupport(){
		try {
			List<SysEmployee> empList = employeeInInterService.getEmpInfo();
			String mapJosn = JSONUtils.toJson(empList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	@Action(value = "empComboboxAllSupport1")
	public void empComboboxAllSupport1(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String type = request.getParameter("type");
			List<SysEmployee> empList = employeeInInterService.getEmpByType(type);
			String mapJosn = JSONUtils.toJson(empList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**  
	 * @Description：  合同单位下拉框
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "contCombobox")
	public void contCombobox(){
		try {
			List<BusinessContractunit> contractunitList = ationService.contCombobox(q);
			String mapJosn = JSONUtils.toJson(contractunitList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description：  银行下拉框
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "bankCombobox")
	public void bankCombobox(){
		try {
			List<BusinessDictionary> typeList = innerCodeService.getDictionary("bank");
			String mapJosn = JSONUtils.toJson(typeList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description：  值班列表
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "findScheduleList")
	public void findScheduleList(){
		try {
			List<InfoVo> infoVoList = ationService.findInfoList(ationInfo.getDeptCode(),ationInfo.getDoctCode(),ationInfo.getReglevlCode(),ationInfo.getNoonCode());
			//String deptId, String empId,String gradeId, String midday
			String mapJosn = JSONUtils.toJson(infoVoList,"yyyy-MM-dd");
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description： 统计
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryStatistics")
	public void queryStatistics(){
		try {
			InfoStatistics infoStatistics = ationService.queryStatistics(ationInfo.getDoctCode(),ationInfo.getNoonCode());
			RegistrationNow ationInfos = ationService.queryRegistrationByEmp(ationInfo.getDoctCode());
			infoStatistics.setLimitAdd(ationInfos.getInfoAdd());
			String mapJosn = JSONUtils.toJson(infoStatistics);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description： 特诊挂号费
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "speciallimitInfo")
	public void speciallimitInfo(){
		try {
			HospitalParameter parameter = ationService.speciallimitInfo(speciallimitInfo);
			String mapJosn = JSONUtils.toJson(parameter);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description： 挂号费
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "feeCombobox")
	public void feeCombobox(){
		try {
			RegisterFee registerFee = ationService.feeCombobox(ationInfo.getPactCode(),ationInfo.getReglevlCode());
			registerFee.setSumCost(registerFee.getRegisterFee()+registerFee.getCheckFee()+registerFee.getTreatmentFee()+registerFee.getOtherFee());
			String mapJosn = JSONUtils.toJson(registerFee);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description： 查询患者
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryRegisterInfo")
	public void queryRegisterInfo(){
		try {
			Map<String,Object> map = ationService.queryRegisterInfo(ationInfo.getCardNo());
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description： 渲染科室
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "querydeptComboboxs")
	public void querydeptComboboxs(){
		try {
			List<SysDepartment> deptList = ationService.querydeptComboboxs();
			Map<String,String> map = new HashMap<String, String>();
			for(SysDepartment dept : deptList){
				map.put(dept.getDeptCode(),dept.getDeptName());
			}
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description： 渲染级别
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "querygradeComboboxs")
	public void querygradeComboboxs(){
		try {
			List<RegisterGrade> gradeList = ationService.querygradeComboboxs();
			Map<String,String> map = new HashMap<String, String>();
			for(RegisterGrade grade : gradeList){
				map.put(grade.getCode(),grade.getName());
			}
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description： 渲染专家
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryempComboboxs")
	public void queryempComboboxs(){
		try {
			List<SysEmployee> empList = ationService.queryempComboboxs();
			Map<String,String> map = new HashMap<String, String>();
			for(SysEmployee emp : empList){
				map.put(emp.getJobNo(),emp.getName());
			}
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**  
	 * @Description： 查询历史挂号记录
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "findInfoHisList")
	public void findInfoHisList(){
		try {
			List<RegistrationNow> registrationList = ationService.findInfoHisList(ationInfo.getCardNo());
			String mapJosn = JSONUtils.toJson(registrationList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**
	 * 当前登录人是否在挂号源黑名单
	 */
	@Action(value = "queryBlack")
	public void queryBlack(){
		try {
			int number =ationService.isEmployeeBlack();
			String json = JSONUtils.toJson(number);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**  
	 * @Description： 挂号收费
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZGHX:function:pay"})
	@Action(value = "saveInfo")
	public void saveInfo(){
		boolean falg = false;//保存过程中是否发生并发异常或者是其他异常
		Boolean f = false;
		//医生号源表id  用于更新号源表已挂人数
		String docSource=registerDocSource.getId();
		Map<String,String> map = new HashMap<>();
		String date=DateUtils.formatDateY_M_D(new Date());
		String key ="MZGH";
		String ynbook = ationInfo.getYnbook();
		String field=date+"-"+ationInfo.getDeptCode()+"-"+ationInfo.getDoctCode()+"-"+ationInfo.getNoonCode();
		try {
			f = redisUtil.hexists(key, field);//判断缓存中是否存在该号源信息
			if(f){
				if(ynbook==null||!"02".equals(ynbook)){//非预约
					Long hincr = redisUtil.hincr(key, field, -1L);
					if(hincr<0){//已挂满
						hincr = redisUtil.hincr(key, field, 1L);
						map.put("resMsg", "error");
						map.put("resCode", "该医生号源已挂满！");
						String mapJosn = JSONUtils.toJson(map);
						WebUtils.webSendJSON(mapJosn);
						return;
					}
				}
			}else{//不存在
				map.put("resMsg", "error");
				map.put("resCode", "未找到号源信息,请刷新后重试或联系管理员！");
				String mapJosn = JSONUtils.toJson(map);
				WebUtils.webSendJSON(mapJosn);
				return;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("GHGL_MZGH", e1);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "1"), e1);
		}
		try {
			map = ationService.saveInfo(ationInfo,docSource);
			try {
				if(!"success".equals(map.get("resMsg"))){//没有保存成功
					if(ynbook==null||!"02".equals(ynbook)){//非预约
						redisUtil.hincr(key, field, 1L);//恢复号源
					}
				}
			} catch (Exception e) {
				logger.error("GHGL_MZGH", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "1"), e);
				e.printStackTrace();
			}
			if("02".equals(ationInfo.getYnbook())){
				RegisterPreregisterNow preregister=new RegisterPreregisterNow();
				preregister.setMedicalrecordId(ationInfo.getMidicalrecordId());
				preregister.setPreregisterDate(new Date());
				preregister.setPreregisterDept(ationInfo.getDeptCode());
				preregister.setPreregisterExpert(ationInfo.getDoctCode());
				List<RegisterPreregisterNow> list =ationService.findPreregisterList(preregister,null,null);
				for(int i=0;i<list.size();i++){
					RegisterPreregisterNow registerPreregister=list.get(i);
					registerPreregister.setStatus(3);
					ationService.saveRegisterPreregister(registerPreregister);
				}
			}
			falg = true;
		} catch (Exception e) {
			try {
				if(f){//没有保存成功(在保存过程中发生了异常)
					if(ynbook==null||!"02".equals(ynbook)){//非预约
						redisUtil.hincr(key, field, 1L);//恢复号源
					}
				}
			} catch (Exception e1) {
				logger.error("GHGL_MZGH", e1);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "1", "0"), e1);
				e1.printStackTrace();
			}
		}finally{
			if (!falg) {
				map.put("resMsg", "error");
				map.put("resCode", "网络繁忙，请稍后重试！！");
			}
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		}
	}
	
	/**  
	 * @Description： 判断该患者当日有没有挂过号
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "findInfoVo")
	public void findInfoVo(){
		try {
			Map<String,String> map = ationService.findInfoVo(ationInfo.getDeptCode(),ationInfo.getDoctCode(),ationInfo.getReglevlCode(),ationInfo.getNoonCode(),ationInfo.getMidicalrecordId());
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description： 病历本费
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "changePay")
	public void changePay(){
		try {
			HospitalParameter parameter = ationService.changePay();
			String mapJosn = JSONUtils.toJson(parameter);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description： 验证密码
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "veriPassWord")
	public void veriPassWord(){
		try {
			OutpatientAccount account  = ationService.veriPassWord(passwords,ationInfo.getMidicalrecordId());
			String mapJosn = JSONUtils.toJson(account);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description： 查询预约挂号
	 * @Author：liudelin
	 * @CreateDate：2016-07-13
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "findPreregisterList")
	public void findPreregisterList(){
		try {
			List<RegisterPreregisterNow> preregisterList=null;
			if(preregister==null){
				RegisterPreregisterNow preregister=new RegisterPreregisterNow();
				preregister.setPreregisterDate(new Date());
				preregisterList = ationService.findPreregisterList(preregister,page,rows);
			}else{
				 preregisterList = ationService.findPreregisterList(preregister,page,rows);
			}
			
			String mapJosn = JSONUtils.toJson(preregisterList,"yyyy-MM-dd");
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description： 判断是否存在就诊卡号
	 * @Author：liudelin
	 * @CreateDate：2016-07-13
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "judgeIdcrad")
	public void judgeIdcrad(){
		try {
			InfoPatient preregisters = ationService.judgeIdcrad(preregister.getIdcardId());
			String mapJosn = JSONUtils.toJson(preregisters);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	
	/**  
	 * @Description： 访问退号
	 * @Author：liudelin
	 * @CreateDate：2016-07-15
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "toBackNoView", results = { @Result(name = "list", location = "/WEB-INF/pages/register/newBackNo/newBackNoList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toBackNoView() {
		return "list";
	}
	
	/**  
	 * @Description： 查询挂号信息
	 * @Author：liudelin
	 * @CreateDate：2016-07-15
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryBackNo")
	public void queryBackNo(){
		try {
			List<RegistrationNow> registrationList = ationService.queryBackNo(ationInfo.getCardNo().trim(), ationInfo.getClinicCode().trim());
			String mapJosn = JSONUtils.toJson(registrationList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description： 退号操作
	 * @Author：liudelin
	 * @CreateDate：2016-07-15
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "updateInfo")
	public void updateInfo(){
		boolean flag = false;
		Boolean f = false;
		Map<String,String> map = new HashMap<String,String>();
		String date=DateUtils.formatDateY_M_D(new Date());
		RegistrationNow registrations = ationService.get(ationInfo.getId());
		String key="MZGH";
		String field=date+"-"+registrations.getDeptCode()+"-"+registrations.getDoctCode()+"-"+registrations.getNoonCode();
		try {
			f = redisUtil.hexists(key, field);
			if(f){
				redisUtil.hincr(key, field, 1L);
			}
		} catch (Exception e1) {
			logger.error("GHGL_MZGH", e1);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "1", "0"), e1);
			e1.printStackTrace();
		}
		try{
			map = ationService.saveOrUpdateInfoBack(registrations,ationInfo.getBacknumberReason(),ationInfo.getPayType());
			try {
				if(!"success".equals(map.get("resMsg"))){//退号未成功
					if(f){
						redisUtil.hincr(key, field, -1L);
					}
				}
			} catch (Exception e) {
				logger.error("GHGL_MZGH", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "1"), e);
				e.printStackTrace();
			}
			flag = true;
		}catch(Exception e){
			try {
				if(f){//退号发生异常
					redisUtil.hincr(key, field, -1L);
				}
			} catch (Exception e1) {
				logger.error("GHGL_MZGH", e1);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "1"), e1);
				e1.printStackTrace();
			}
		}finally{
			if(!flag){
				map.put("resMsg", "error");
				map.put("resCode", "网络繁忙，请稍后重试！！");
			}
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		}
		
	}
	/**  
	 * @Description： 打印之后更新挂号表记录
	 * @Author：donghe
	 * @CreateDate：2016-10-19
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "updateRegistration", results = { @Result(name = "list", location = "/WEB-INF/pages/register/newInfo/registration.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String updateRegistration(){
		try {
			ationService.updateRegistration(ationInfo,q);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
		return "list";
	}
	
	/**  
	 * @Description：支付方式下拉框渲染
	 * @Author：donghe
	 * @CreateDate：2016-10-19
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryPayType")
	public void queryPayType(){
		try {
			List<BusinessDictionary> list =innerCodeService.getDictionary("payway");
			List<BusinessDictionary> newList=new ArrayList<BusinessDictionary>();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					if("CA".equals(list.get(i).getEncode())||"DB".equals(list.get(i).getEncode())||"CH".equals(list.get(i).getEncode())||"YS".equals(list.get(i).getEncode())){
						newList.add(list.get(i));
					}
				}
			}
			String mapJosn = JSONUtils.toJson(newList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description： NEW  值班列表
	 * @Author：GH
	 * @CreateDate：2016-07-08 
	 * @ModifyRmk： 变更数据源 从医生号源表中取数据  
	 * @version 1.0
	 */
	@Action(value = "findNewList")
	public void findNewList(){
		List<RegisterDocSource> list =new ArrayList<>();
		String date=DateUtils.formatDateY_M_D(new Date());
		String key="MZGH";
		try {
			list= ationService.findNewInfoList(ationInfo.getDeptCode(),ationInfo.getDoctCode(),ationInfo.getReglevlCode(),ationInfo.getNoonCode());
			for (RegisterDocSource reg : list) {
				String field=date+"-"+reg.getDeptCode()+"-"+reg.getEmployeeCode()+"-"+reg.getMiddayCode();
				Boolean flag = redisUtil.hexists(key, field);
				if(flag){//存在
					Long hincr = redisUtil.hincr(key, field, 0l);
					reg.setClinicSum(reg.getLimitSum()-hincr.intValue());//已挂人数=挂号限额-剩余号数
				}else{
					Integer s=reg.getLimitSum()-reg.getClinicSum();//剩余号源=挂号限额-已挂人数
					long sum = s.longValue();
					long hincr = redisUtil.hincr(key, field, sum);
					if(sum!=hincr){//避免同时操作
						redisUtil.hincr(key, field, -sum);
					}
				}
			}
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
		
		String mapJosn = JSONUtils.toJson(list);
		WebUtils.webSendJSON(mapJosn);
	}
	/**
	 * @Description 
	 * @author  marongbin
	 * @createDate： 2017年1月16日 下午6:45:28 
	 * @modifier 
	 * @modifyDate：
	 * @return: String
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "toRegistrationQuery", results = { @Result(name = "list", location = "/WEB-INF/pages/register/registionquery/registionquery.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toRegistrationQuery() {
		return "list";
	}
	@Action(value = "queryRegistrarion")
	public void queryRegistrarion(){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			List<RegistrationNow> list = ationService.queryRestration(ationInfo,page,rows);
			int total = ationService.queryRestrationTotal(ationInfo, page, rows);
			Map<String,String> cnMap = employeeInInterService.queryEmpCodeAndNameMap();
			for (RegistrationNow re : list) {
				re.setPatientAge(re.getPatientAge()+re.getPatientAgeunit());
				re.setOperCode(cnMap.get(re.getOperCode()));
			}
			map.put("total", total);
			map.put("rows", list);
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**
	 * @Description 判断是否看诊
	 * @author  marongbin
	 * @createDate： 2017年3月1日 下午8:30:39 
	 * @modifier 
	 * @modifyDate：: void
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "checkISsee")
	public void checkISsee(){
		try {
			List<OutpatientRecipedetailNow> list = ationService.checkISsee(clinicCode);
			Map<String,String> map = new HashMap<String,String>();
			if(list!=null&&list.size()>0){
				map.put("resMsg", "error");
				map.put("resCode", "已经开立医嘱，不可以进行退号/换科操作！");
			}else{
				map.put("resMsg", "sucess");
				map.put("resCode", "验证通过！");
			}
			WebUtils.webSendJSON(JSONUtils.toJson(map));
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
	@Action(value = "ireportRegisterJavaBean")
	public void ireportRegisterJavaBean() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String id = request.getParameter("tId");
			String fileName = request.getParameter("fileName");
			String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
			String root_path = request.getSession().getServletContext().getRealPath("/");
			root_path = root_path.replace('\\', '/');
			String reportFilePath = root_path + webPath+fileName+".jasper";
			RegPrintVO now = ationService.getRegByID(id);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", root_path + webPath);
			parameters.put("deptAdress", now.getDeptAdress());
			parameters.put("midicalrecordId", now.getMidicalrecordId());
			parameters.put("createhos", hospitalInInterService.getHospitalByCode(HisParameters.CURRENTHOSPITALCODE).getName());
			parameters.put("patientName", now.getPatientName());
			parameters.put("regFee", now.getRegFee().toString());
			parameters.put("chckFee", now.getChckFee().toString());
			parameters.put("doctName", now.getDoctName());
			parameters.put("regDate", DateUtils.formatDateY_M_D_H_M_S(now.getRegDate()));
			parameters.put("clinicCode", now.getClinicCode());
			parameters.put("schemaNo", now.getSchemaNo());
			parameters.put("orderNo", now.getOrderNo().toString());
			parameters.put("noonCodeNmae", now.getNoonCodeNmae());
			parameters.put("deptName", now.getDeptName());
			ArrayList<RegPrintVO> list = new ArrayList<RegPrintVO>();
			list.add(now);
			JRDataSource source = new JRBeanCollectionDataSource(list);
			iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,source);
		} catch (Exception e) {
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
			e.printStackTrace();
		}
	}
}
