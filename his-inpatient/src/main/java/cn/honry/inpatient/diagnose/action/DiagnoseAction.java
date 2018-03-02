package cn.honry.inpatient.diagnose.action;
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

import cn.honry.base.bean.model.BusinessDiagnose;
import cn.honry.base.bean.model.BusinessDiagnoseMedicare;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inpatient.diagnose.service.DiagnoseMedicareService;
import cn.honry.inpatient.diagnose.service.DiagnoseService;
import cn.honry.inpatient.diagnose.vo.DiagnoseVo;
import cn.honry.inpatient.info.service.InpatientInfoService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


/**  
 *  
 * @className：IcdAction 
 * @Description：  icdAction
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/diagnose")
public class DiagnoseAction extends ActionSupport implements ModelDriven<BusinessDiagnose> {
	private Logger logger=Logger.getLogger(DiagnoseAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	@Override
	public BusinessDiagnose getModel() {
		return diagnose;
	}
	private static final long serialVersionUID = 1L;
	private InpatientInfo inpatientInfo=new InpatientInfo();
	private BusinessDiagnoseMedicare  businessMedicare=new BusinessDiagnoseMedicare();
	private BusinessDiagnose diagnose = new BusinessDiagnose();
	private DiagnoseMedicareService diagnoseMedicareService;
	@Autowired
	@Qualifier(value="diagnoseMedicareService")
	public void setDiagnoseMedicareService(DiagnoseMedicareService diagnoseMedicareService) {
		this.diagnoseMedicareService = diagnoseMedicareService;
	}
	private EmployeeInInterService employeeInInterService;
	@Autowired
	@Qualifier(value="employeeInInterService")
	public void setEmployeeInInterService(EmployeeInInterService employeeInInterService) {
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
	private String menuAlias;  //栏目别名，在主界面中点击栏目时传到action中
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	private String page;
	private String rows;
	private  List<DiagnoseVo> codeList;
	

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


	

	public InpatientInfo getInpatientInfo() {
		return inpatientInfo;
	}

	public BusinessDiagnoseMedicare getBusinessMedicare() {
		return businessMedicare;
	}

	public void setInpatientInfo(InpatientInfo inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}

	public void setBusinessMedicare(BusinessDiagnoseMedicare businessMedicare) {
		this.businessMedicare = businessMedicare;
	}
	
	private InpatientInfoService inpatientInfoService;
	@Autowired
	@Qualifier(value = "inpatientInfoService")
	public void setInpatientInfoService(InpatientInfoService inpatientInfoService) {
		this.inpatientInfoService = inpatientInfoService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private DiagnoseService diagnoseService;
	@Autowired
	@Qualifier(value = "diagnoseService")
	public void setDiagnoseService(DiagnoseService diagnoseService) {
		this.diagnoseService = diagnoseService;
	}
	

	/**  
	 *  跳转到住院诊断页面
	 * @Description：  跳转到住院诊断页面
	 * @Author：zhangjin
	 * @CreateDate：2015-12-24上午10:27:00  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZYZD:function:view"}) 
	@Action(value="inpatientDiagnoseList",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/diagnose/diagnoseList.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String inpatientDiagnoseList()throws Exception{
		String userId=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		ServletActionContext.getRequest().setAttribute("userId", userId);//当前登录人
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(dept!=null){
			ServletActionContext.getRequest().setAttribute("deptId", dept.getDeptCode());//当前登录人
		}else{
			ServletActionContext.getRequest().setAttribute("deptFlg", "false");
		}
		return "list";
	}
	/**  
	 *  查询住院诊断(患者信息)
	 * @Description：  住院诊断(患者信息)
	 * @Author：zhangjin
	 * @CreateDate：2015-12-23 下午05:37:12  
	 * @Modifier：
	 * @ModifyDate： 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZYZD:function:view"}) 
	@Action(value = "queryInpinfoDiagnose", results = { @Result(name = "json", type = "json") })
	public void queryInpinfoDiagnose() {
		try {
		String no = request.getParameter("no");
		InpatientInfoNow inpatientInfo=inpatientInfoService.queryInpatientinfo(no);
		String json=JSONUtils.toJson(inpatientInfo, DateUtils.DATE_FORMAT);
		WebUtils.webSendJSON(json);
		}catch (Exception e) {
			logger.error("ZYGL_ZYZD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYZD", "住院管理_住院诊断", "2", "0"), e);
		}
	}
	
	/**  
	 * 根据科室获取住院诊断医生
	 * @Description：  住院诊断
	 * @Author：zhangjin
	 * @CreateDate：2015-12-29 下午05:37:12  
	 * @Modifier：
	 * @ModifyDate： 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZYZD:function:view"}) 
	@Action(value = "queryDiangnoseDoctor", results = { @Result(name = "json", type = "json") })
	public void queryDiangnoseDoctor() {
		 String no = request.getParameter("id");
		 List<SysEmployee> list=employeeInInterService.queryDiangDoctor(no);
		 String json=JSONUtils.toJson(list, DateUtils.DATE_FORMAT);
		 WebUtils.webSendJSON(json);
	}
	
	
	/**  
	 * 根据住院流水号查询住院诊断信息
	 * @Description：  住院诊断(诊断信息)
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-24 下午01:45:32  
	 * @Modifier：
	 * @ModifyDate： 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZYZD:function:view"}) 
	@Action(value = "queryDiagnoseByInpNo", results = { @Result(name = "json", type = "json") })
	public void queryDiagnoseByInpNo(){
		try {
			String inpatientNo=request.getParameter("id");
			codeList = diagnoseService.queryDiagnoseBy(inpatientNo);
			String json=JSONUtils.toJson(codeList, DateUtils.DATE_FORMAT);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYZD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYZD", "住院管理_住院诊断", "2", "0"), e);
		}
	}
	/**  
	 * 保存住院诊断信息
	 * @Description：  保存诊断信息
	 * @Author：zhangjin
	 * @CreateDate：2015-12-25 下午01:45:32  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZYZD:function:save"}) 
	@Action(value = "saveInpinfoOut", results = { @Result(name = "list", type = "json") })
	public String saveInpinfoOut() throws Exception {
		String diagCode=new String(request.getParameter("diagCode").getBytes("ISO-8859-1"),"UTF-8");
		businessMedicare.setId(diagnose.getId());
		businessMedicare.setInpatientNo(diagnose.getInpatientNo());
		businessMedicare.setDiagKind(diagnose.getDiagKind());
		businessMedicare.setDoctName(diagnose.getDoctName());
		businessMedicare.setDiagDate(diagnose.getDiagDate());
		businessMedicare.setMainFlay(diagnose.getMainFlay());
		businessMedicare.setIcdCode(diagnose.getIcdCode());
		businessMedicare.setDiagName(diagnose.getDiagName());
		if("ICD10".equals(diagCode)){
			if(StringUtils.isBlank(diagnose.getId())){
				try{
						diagnoseService.saveOrUpdate(diagnose);
						WebUtils.webSendString("success");
				}catch(Exception e){
					WebUtils.webSendJSON("error");
					logger.error("ZYGL_ZYZD", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYZD", "住院管理_住院诊断", "2", "0"), e);
				}
			}else {
				try{
					
						diagnoseService.saveOrUpdate(diagnose);
						WebUtils.webSendString("success");
				}catch(Exception e){
					WebUtils.webSendJSON("error");
					logger.error("ZYGL_ZYZD", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYZD", "住院管理_住院诊断", "2", "0"), e);
				}
			}
			return "list";
			
			
	
	}else {
	       
			if(StringUtils.isBlank(businessMedicare.getId())){
				try{
					
						diagnoseMedicareService.saveOrUpdate(businessMedicare);
						WebUtils.webSendString("success");
				}catch(Exception e){
					WebUtils.webSendJSON("error");
					logger.error("ZYGL_ZYZD", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYZD", "住院管理_住院诊断", "2", "0"), e);
				}
			}else {
				try{
						diagnoseMedicareService.saveOrUpdate(businessMedicare);
						WebUtils.webSendString("success");
				}catch(Exception e){
					WebUtils.webSendJSON("error");
					logger.error("ZYGL_ZYZD", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYZD", "住院管理_住院诊断", "2", "0"), e);
				}
			}	
	     }
		return "list";
		}
	
	/**  
	 * 跳转到诊断代码选择页面
	 * @Description：  
	 * @Author：zhangjin
	 * @CreateDate：2015-12-24上午10:27:00  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZYZD:function:view"}) 
	@Action(value="diagnoseCode",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/diagnose/diagnoseCode.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String diagnoseCode(){
		ServletActionContext.getRequest().setAttribute("id", "1");
		return "list";
	}
	/**  
	 * 跳转到ICD代码选择页面
	 * @Description：  
	 * @Author：zhangjin
	 * @CreateDate：2015-12-24上午10:27:00  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZYZD:function:view"}) 
	@Action(value="diagnoseIcdCode",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/diagnose/diagnoseCode.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String diagnoseIcdCode(){
		ServletActionContext.getRequest().setAttribute("id", "2");
		return "list";
	}
	/**  
	 * 查询诊断代码
	 * @Description：  诊断代码
	 * @Author：zhangjin
	 * @CreateDate：2015-12-26下午03:37:12  
	 * @Modifier
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZYZD:function:view"}) 
	@Action(value = "queryCode", results = { @Result(name = "json", type = "json")})
	public void queryCode() {
		try {
			int total = diagnoseService.getTotals(diagnose);
			codeList=diagnoseService.getPages(page,rows,diagnose);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", total);
			map.put("rows", codeList);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYZD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYZD", "住院管理_住院诊断", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * 通过就诊卡号查询患者信息
	 * @Description：通过就诊卡号查询患者信息
	 * @Author：dh
	 * @CreateDate：2015-12-21 下午 20：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryFormByIdds",results = { @Result(name = "json", type = "json") })
	public void queryFormByIdds(){
		try {
			//前台传来的是就诊卡号，因牵扯较多，未另建变量
			String medicalrecordId=ServletActionContext.getRequest().getParameter("no");
			if(StringUtils.isNotBlank(medicalrecordId)){
				medicalrecordId=medicalrecordId.trim();
			}
			//通过接口查询就诊卡号对应的病历号
			medicalrecordId = patinentInnerService.getMedicalrecordId(medicalrecordId);
			List<InpatientInfoNow> inpatientInfo = diagnoseService.queryByMedicall(medicalrecordId);
			 String json="";
			if(inpatientInfo != null && inpatientInfo.size() > 0){
				  json = JSONUtils.toJson(inpatientInfo);
			}else{
				json="error";
			}
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYZD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYZD", "住院管理_住院诊断", "2", "0"), e);
			e.printStackTrace();
		}
		 
	}
	/**  
	 * 通过病历号查询患者信息
	 * @Description：通过病历号查询患者信息
	 * @Author：dh
	 * @CreateDate：2015-12-21 下午 20：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryFormBMyIdds",results = { @Result(name = "json", type = "json") })
	public void queryFormBMyIdds(){
		
		try {
			//前台传来的是就诊卡号，因牵扯较多，未另建变量
			String medicalrecordId=ServletActionContext.getRequest().getParameter("no");
			if(StringUtils.isNotBlank(medicalrecordId)){
				medicalrecordId=medicalrecordId.trim();
			}		
			List<InpatientInfoNow> inpatientInfo = diagnoseService.queryByMedicall(medicalrecordId);
			 String json="";
			if(inpatientInfo != null && inpatientInfo.size() > 0){
				  json = JSONUtils.toJson(inpatientInfo,DateUtils.DATE_FORMAT);
			}else{
				json="error";
			}
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYZD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYZD", "住院管理_住院诊断", "2", "0"), e);
		}
	}
}

