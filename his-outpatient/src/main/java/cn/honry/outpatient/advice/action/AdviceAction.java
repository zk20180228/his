package cn.honry.outpatient.advice.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

import cn.honry.base.bean.model.BusinessAdvicetoSystemclass;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.DrugSpedrug;
import cn.honry.base.bean.model.OutpatientMedicalrecord;
import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.adviceToSystemclass.service.AdviceTScInInterService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.inpatient.kind.service.InpatientKindInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inner.vo.ComboGroupVo;
import cn.honry.outpatient.advice.service.AdviceService;
import cn.honry.outpatient.advice.vo.AdviceVo;
import cn.honry.outpatient.advice.vo.InpatientInfoVo;
import cn.honry.outpatient.advice.vo.InspectionReportList;
import cn.honry.outpatient.advice.vo.IreportPatientVo;
import cn.honry.outpatient.advice.vo.LisVo;
import cn.honry.outpatient.advice.vo.OutpatientVo;
import cn.honry.outpatient.advice.vo.PatientVo;
import cn.honry.outpatient.advice.vo.RecipelInfoVo;
import cn.honry.outpatient.advice.vo.RegisterMainVo;
import cn.honry.outpatient.advice.vo.ViewInfoVo;
import cn.honry.outpatient.newInfo.service.RegistrationService;
import cn.honry.report.service.IReportService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;


/**  
 *  
 * 门诊医嘱  Action
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/outpatient/advice")
public class AdviceAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	private Logger logger=Logger.getLogger(AdviceAction.class);
	/**医嘱Service**/
	@Autowired
	@Qualifier(value = "adviceService")
	private AdviceService adviceService;
	/**项目Service**/
	@Autowired
	@Qualifier(value = "inpatientKindInInterService")
	private InpatientKindInInterService inpatientKindInInterService;
	/**编码Service**/
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	/**挂号Service**/
	@Autowired
	@Qualifier(value = "ationService")
	private RegistrationService ationService;
	/**医嘱项目及系统类别Service**/
	@Autowired
	@Qualifier(value = "adviceTScInInterService")
	private AdviceTScInInterService adviceTScInInterService;
	/**医嘱项目及系统类别Service**/
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	
	/**医嘱实体**/
	private OutpatientRecipedetail recipedetail;
	
	/**栏目别名,在主界面中点击栏目时传到action的参数**/
	private String menuAlias;
	
	/**类型/项目类别**/
	private String type;
	
	/**id**/
	private String id;
	
	/**就诊卡号**/
	private String idCardNo;
	
	/**门诊号**/
	private String clinicNo;
	
	/**药房Id**/
	private String pharmacyId;
	
	/**项目类别**/
	private String advName;
	
	/**项目名称**/
	private String name;
	
	/**项目类别中文**/
	private String typeChi;
	
	/**当前页数**/
	private String page;
	
	/**每页大小**/
	private String rows;
	
	/**查询条件**/
	private String q;
	
	/**当前用户**/
	private String advUserName;
	
	/**当前科室code**/
	private String outMedideptId;
	
	/**当前科室Name**/
	private String outMedideptName;
	
	/**信息**/
	private String jsonData;
	
	/**病历号**/
	private String patientNo;
	
	/**开始时间：草药医嘱**/
	private String staDate;
	
	/**组套Id**/
	private String stackId;
	
	/**组套是否共享**/
	private String stackFlag;
	
	/**组套备注**/
	private String stackRemark;
	
	/**组套自定义码**/
	private String stackInputCode;
	
	/**组套来源**/
	private String stackSource;
	
	/**组套名称**/
	private String stackName;
	
	/**组套类型**/
	private String stackType;
	
	/**审核资格**/
	private boolean auditing;
	
	/**审核状态**/
	private Integer start;

	/**审核意见**/
	private String remarks;
	
	/**门诊医嘱组套  还是住院医嘱组套**/
	private String stackInpmertype;
	
	/**组套父级**/
	private String parent;
	
	/**额外参数**/
	private String para;
	
	/**开始时间**/
	private String startTime;
	
	/**结束时间**/
	private String endTime;
	
	/**表名**/
	private String tab;
	
	/**处方号**/
	private String recipeNo;
	
	/**是否模糊查询**/
	private String vague;
	
	/**住院流水号**/
	private String inpatientNo;
	
	/**setters and getters**/
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public void setAdviceService(AdviceService adviceService) {
		this.adviceService = adviceService;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getClinicNo() {
		return clinicNo;
	}
	public void setClinicNo(String clinicNo) {
		this.clinicNo = clinicNo;
	}
	public OutpatientRecipedetail getRecipedetail() {
		return recipedetail;
	}
	public void setRecipedetail(OutpatientRecipedetail recipedetail) {
		this.recipedetail = recipedetail;
	}
	public String getPharmacyId() {
		return pharmacyId;
	}
	public void setPharmacyId(String pharmacyId) {
		this.pharmacyId = pharmacyId;
	}
	public String getAdvName() {
		return advName;
	}
	public void setAdvName(String advName) {
		this.advName = advName;
	}
	public void setInpatientKindInInterService(InpatientKindInInterService inpatientKindInInterService) {
		this.inpatientKindInInterService = inpatientKindInInterService;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTypeChi() {
		return typeChi;
	}
	public void setTypeChi(String typeChi) {
		this.typeChi = typeChi;
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
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getAdvUserName() {
		return advUserName;
	}
	public void setAdvUserName(String advUserName) {
		this.advUserName = advUserName;
	}
	public String getOutMedideptId() {
		return outMedideptId;
	}
	public void setOutMedideptId(String outMedideptId) {
		this.outMedideptId = outMedideptId;
	}
	public String getOutMedideptName() {
		return outMedideptName;
	}
	public void setOutMedideptName(String outMedideptName) {
		this.outMedideptName = outMedideptName;
	}
	public String getJsonData() {
		return jsonData;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public String getStaDate() {
		return staDate;
	}
	public void setStaDate(String staDate) {
		this.staDate = staDate;
	}
	public String getStackId() {
		return stackId;
	}
	public void setStackId(String stackId) {
		this.stackId = stackId;
	}
	public String getStackFlag() {
		return stackFlag;
	}
	public void setStackFlag(String stackFlag) {
		this.stackFlag = stackFlag;
	}
	public String getStackRemark() {
		return stackRemark;
	}
	public void setStackRemark(String stackRemark) {
		this.stackRemark = stackRemark;
	}
	public String getStackInputCode() {
		return stackInputCode;
	}
	public void setStackInputCode(String stackInputCode) {
		this.stackInputCode = stackInputCode;
	}
	public String getStackSource() {
		return stackSource;
	}
	public void setStackSource(String stackSource) {
		this.stackSource = stackSource;
	}
	public String getStackName() {
		return stackName;
	}
	public void setStackName(String stackName) {
		this.stackName = stackName;
	}
	public String getStackType() {
		return stackType;
	}
	public void setStackType(String stackType) {
		this.stackType = stackType;
	}
	public void setAtionService(RegistrationService ationService) {
		this.ationService = ationService;
	}
	public boolean isAuditing() {
		return auditing;
	}
	public void setAuditing(boolean auditing) {
		this.auditing = auditing;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getStackInpmertype() {
		return stackInpmertype;
	}
	public void setStackInpmertype(String stackInpmertype) {
		this.stackInpmertype = stackInpmertype;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getPara() {
		return para;
	}
	public void setPara(String para) {
		this.para = para;
	}
	public void setAdviceTScInInterService(
			AdviceTScInInterService adviceTScInInterService) {
		this.adviceTScInInterService = adviceTScInInterService;
	}
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
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
	
	public String getTab() {
		return tab;
	}
	public void setTab(String tab) {
		this.tab = tab;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public String getVague() {
		return vague;
	}
	public void setVague(String vague) {
		this.vague = vague;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	
	/**  
	 *  
	 * 跳转医嘱list页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"MZJY:function:view"})
	@Action(value = "listAdvice", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/index.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listAdvice() {
		advName = inpatientKindInInterService.queryKindInfoByName(HisParameters.ADVICETYPEOUTPATIENT);
		advUserName = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		//获得开立科室
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(dept!=null){
			outMedideptId = dept.getDeptCode();
			outMedideptName = dept.getDeptName();
		}
		auditing = adviceService.queryAuditing();
		return "list";
	}
	
	/**  
	 *  
	 * 跳转电子病历页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "record", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/record.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String record() {
		return "list";
	}
	
	/**  
	 *  
	 * 跳转医嘱页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "advice", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/advice.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String advice() {
		auditing = adviceService.queryAuditing();
		return "list";
	}
	
	/**  
	 *  
	 * 跳转历史医嘱页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "hisAdvice", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/hisAdvice.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String hisAdvice() {
		return "list";
	}
	
	/**  
	 *  
	 * 跳转药品页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "drug", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/drug.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String drug() {
		return "list";
	}
	
	/**  
	 *  
	 * 跳转非药品页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "unDrug", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/unDrug.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String unDrug() {
		return "list";
	}
	
	/**  
	 *  
	 * 跳转Lis页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "lis", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/lis.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String lis() {
		return "list";
	}
	
	/**  
	 *  
	 * 跳转Pacs页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "pacs", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/pacs.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String pacs() {
		return "list";
	}
	
	/**  
	 *  
	 * 跳转Patient页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "patient", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/patient.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String patient() {
		startTime = DateUtils.formatDateY_M_D_H_M_S(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));
		endTime = DateUtils.formatDateY_M_D_H_M_S(DateUtils.getCurrentTime());	
		return "list";
	}
	
	/**  
	 *  
	 * 跳转患者分析页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "analysis", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/analysis.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String analysis() {
		return "list";
	}
	
	/**  
	 *  
	 * 获得信息树-异步查询
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:type 1待诊2已诊
	 *
	 */
	@Action(value = "queryAdviceTree")
	public void queryAdviceTree() {
		String json = adviceService.queryAdviceTree(type,id);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * 获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:idCardNo就诊卡号
	 *
	 */
	@Action(value = "queryPatientByidCardNo")
	public void queryPatientByidCardNo() {
		Map<String,Object> retMap = new HashMap<String, Object>();
		if(StringUtils.isBlank(idCardNo)){
			retMap.put("resMsg", "error");
			retMap.put("resCode", "请输入就诊卡号！");
		}else{
			List<PatientVo> voList = adviceService.queryPatientByidCardNo(idCardNo,type);
			if(voList!=null && voList.size()>0){
				retMap.put("resMsg", "success");
				retMap.put("resCode", voList);
			}else{
				retMap.put("resMsg", "error");
				retMap.put("resCode", "暂无待诊信息！");
			}
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * 获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:clinicNo门诊号
	 *
	 */
	@Action(value = "queryPatientByclinicNo")
	public void queryPatientByclinicNo() {
		Map<String,Object> retMap = new HashMap<String, Object>();
		PatientVo vo = adviceService.queryPatientByclinicNo(clinicNo);
		if(vo!=null){
			retMap.put("resMsg", "success");
			retMap.put("resCode", vo);
		}else{
			retMap.put("resMsg", "error");
			retMap.put("resCode", "暂无挂号信息！");
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
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
	@Action(value = "savaPharmacyInfo")
	public void savaPharmacyInfo(){
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			SysDepartment dept = adviceService.getPharmacyInfoById(pharmacyId);
			SecurityUtils.getSubject().getSession().removeAttribute(SessionUtils.SESSIONPHARMACY);
			SecurityUtils.getSubject().getSession().setAttribute(SessionUtils.SESSIONPHARMACY,dept);
			map.put("resMsg", "success");
			map.put("resCode", "药房选择成功！");
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "获取药房失败，请重新选择！");
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description： 门诊医嘱-跳转到项目名称页面
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-05 上午11:34:22  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-05 上午11:34:22  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws UnsupportedEncodingException 
	 *
	 */
	@Action(value = "viewInfo", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/viewInfo.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewInfo(){
		try {
			name = StringUtils.isBlank(name)?name:java.net.URLDecoder.decode(name, "UTF-8");
			typeChi = StringUtils.isBlank(typeChi)?typeChi:java.net.URLDecoder.decode(typeChi, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "list";
	}
	
	/**  
	 *  
	 * 门诊医嘱-查询项目信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-05 上午11:13:12  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-05 上午11:13:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryViewInfo")
	public void queryViewInfo() {	
		Map<String,Object> retMap = new HashMap<String, Object>();
		ViewInfoVo vo = new ViewInfoVo();
		BusinessDictionary drugType = innerCodeService.getDictionaryByName("drugType",typeChi);
		if(drugType!=null){
			vo.setTy(1);
			vo.setType(drugType.getEncode());
			vo.setSysType(type);
			vo.setName(name);
		}else{
			if("非药品".equals(typeChi)){
				vo.setTy(0);
				vo.setSysType(type);
				vo.setName(name);
			}else{
				if(!"全部".equals(typeChi)){
					vo.setSysType(type);
					vo.setName(name);
				}else{
					vo.setName(name);
				}
			}
		}
		int total = adviceService.getViewInfoTotal(vo);
		List<ViewInfoVo> infoList = adviceService.getViewInfoPage(page,rows,vo);
		retMap.put("total", total);
		retMap.put("rows", infoList);
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  获得渲染数据-单位
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryColorInfoJudgeMap")
	public void queryColorInfoJudgeMap(){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("packunit", HisParameters.DRUGPACKUNIT);
		map.put("minunit", HisParameters.DRUGMINUNIT);
		map.put("doseUnit", HisParameters.DOSEUNIT);
		map.put("nonmedicineencoding", HisParameters.UNDRUGUNIT);
		map.put("herbsId",HisParameters.HERBSID);
		/**业务变更 获得毒麻药编码 2017-02-27 10:54 aizhonghua**/
		BusinessDictionary dictionarys = innerCodeService.getDictionaryByName(HisParameters.DRUGPROPERTIES, HisParameters.DRUGPROPERTIESS);
		if(dictionarys!=null){
			map.put("drugPropS",dictionarys.getEncode());
		}
		/**业务变更 获得精神类药编码 2017-02-27 11:01 aizhonghua**/
		BusinessDictionary dictionaryp = innerCodeService.getDictionaryByName(HisParameters.DRUGPROPERTIES, HisParameters.DRUGPROPERTIESP);
		if(dictionarys!=null){
			map.put("drugPropP",dictionaryp.getEncode());
		}
		/**业务变更 是否可开立库存为0的药品  2017-02-27 11:16 aizhonghua**/
		String openZero = parameterInnerService.getparameter(HisParameters.OPENZERO);
		if(StringUtils.isBlank(openZero)){
			openZero = "0";
		}
		map.put("openZero",openZero);
		map.put("isOpenZero",HisParameters.ISOPENZERO);
		/**业务变更 药品默认频次  2017-02-27 11:17 aizhonghua**/
		map.put("drugFre",HisParameters.DRUGFRE);
		/**业务变更 药品默认用法  2017-02-27 11:17 aizhonghua**/
		map.put("drugUse",HisParameters.DRUGUSE);
		/**业务变更 非药品默认频次  2017-02-27 11:17 aizhonghua**/
		map.put("unDrugUse",HisParameters.UNDRUGFRE);
		/**业务变更 抗菌药限制-职级限制  2017-02-28 15:15 aizhonghua**/
		map.put("speDrugRank",HisParameters.SPEDRUGRANK);
		/**业务变更 抗菌药限制-特殊限制  2017-02-28 15:15 aizhonghua**/
		map.put("speDrugSpe",HisParameters.SPEDRUGSPE);
		/**业务变更 拆分属性-可拆分  2017-03-02 11:43 aizhonghua**/
		map.put("splitDrug",HisParameters.SPLITDRUG);
		/**业务变更 拆分属性维护-可拆分  2017-03-02 11:43 aizhonghua**/
		map.put("splitDrugArr",HisParameters.SPLITDRUGARR);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**  
	 *  
	 * @Description：  获得渲染数据-单位
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryColorInfoUnitsList")
	public void queryColorInfoUnitsList(){
		List<ComboGroupVo> unitList = innerCodeService.getUnitAllGroup();
		if(unitList==null || unitList.size()==0){
			unitList = new ArrayList<ComboGroupVo>();
		}else{
			for(ComboGroupVo vo : unitList){
				if(HisParameters.UNDRUGUNIT.equals(vo.getOrganize())){
					vo.setCode(vo.getName());
				}
			}
		}
		String json = JSONUtils.toJson(unitList);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  获得渲染数据-频次
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryColorInfoFrequencyList")
	public void queryColorInfoFrequencyList(){
		List<BusinessFrequency> unitList = adviceService.queryColorInfoFrequencyList();
		String json = JSONUtils.toJson(unitList);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  获得渲染数据-执行科室
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryColorInfoExeDeptList")
	public void queryColorInfoExeDeptList(){
		List<SysDepartment> unitList = adviceService.queryColorInfoExeDeptList();
		String json = JSONUtils.toJson(unitList);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  获得医生职级和药品等级对照关系key药品等级编码value等级名称
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-12 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-12 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryJudgeDocDrugGradeMap")
	public void queryJudgeDocDrugGradeMap(){
		Map<String,String> map = adviceService.queryJudgeDocDrugGradeMap();
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  获得药品等级key药品等级id value等级编码
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-12 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-12 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryJudgeDrugGradeMap")
	public void queryJudgeDrugGradeMap(){
		Map<String,String> map = adviceService.queryJudgeDrugGradeMap();
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  获得全部药品等级key药品等级id value等级名称
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-12 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-12 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryJudgeDrugGradeAllMap")
	public void queryJudgeDrugGradeAllMap(){
		Map<String,String> map = adviceService.queryJudgeDrugGradeAllMap();
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  获得系统类别
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-12 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-12 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryJudgeSysTypeAllMap")
	public void queryJudgeSysTypeAllMap(){
		Map<String,String> map = adviceService.queryJudgeSysTypeAllMap();
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  门诊医嘱-跳转到草药查询页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-9 上午11:34:22  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-9 上午11:34:22  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "viewChinMediModle", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/herbal.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewChinMediModle() {
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  获得草药combogrid
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-12 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-12 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryWestMediInfoModel")
	public void queryWestMediInfoModel() {	
		ViewInfoVo vo = new ViewInfoVo();
		Map<String,Object> retMap = new HashMap<String, Object>();
		String aType = inpatientKindInInterService.queryKindInfoByName(HisParameters.ADVICETYPEOUTPATIENT);
		BusinessDictionary systemType = innerCodeService.getDictionaryByName("systemType","中草药");
		BusinessAdvicetoSystemclass atst = adviceTScInInterService.getAdvtoSysByAtSt(aType,systemType.getEncode());
		if(atst!=null){
			BusinessDictionary drugType = innerCodeService.getDictionaryByName("drugType","中草药");
			vo.setTy(1);
			vo.setType(drugType.getEncode());
			vo.setName(q);
			vo.setSysType(systemType.getEncode());
			int total = adviceService.getViewInfoTotal(vo);
			List<ViewInfoVo> infoList = adviceService.getViewInfoPage(page,rows,vo);
			retMap.put("total", total);
			retMap.put("rows", infoList);
		}else{
			retMap.put("total", 0);
			retMap.put("rows", new ArrayList<ViewInfoVo>());
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  获得煎药方式
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-12 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-12 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "querBoilmedicineway")
	public void querBoilmedicineway() throws IOException {
		List<BusinessDictionary> list = innerCodeService.getDictionary("boilmedicineway");
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  保存医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午08:22:25  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午08:22:25  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "savaAdviceInfo")
	public void savaAdviceInfo() throws IOException {
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			if(StringUtils.isBlank(clinicNo)){
				map.put("resMsg", "error");
				map.put("resCode", "门诊号不能为空,开立医嘱失败!");
			}else if(StringUtils.isBlank(patientNo)){
				map.put("resMsg", "error");
				map.put("resCode", "病历号不能为空,开立医嘱失败!");
			}else if(StringUtils.isBlank(jsonData)){
				map.put("resMsg", "error");
				map.put("resCode", "医嘱信息不能为空,开立医嘱失败!");
			}else{
				Gson gson = new Gson();  
				List<AdviceVo> voList = gson.fromJson(jsonData, new TypeToken<List<AdviceVo>>(){}.getType());
				if(voList==null||voList.size()<=0){
					map.put("resMsg", "error");
					map.put("resCode", "医嘱信息不能为空,开立医嘱失败!");
				}else{
					//通过病历号获得患者信息
					PatientIdcard idcard = adviceService.getIdcardByPatientNo(patientNo);
					if(idcard==null){
						map.put("resMsg", "error");
						map.put("resCode", "就诊卡信息不存在,开立医嘱失败!");
					}else{
						RegistrationNow registration = adviceService.getRegisterInfoByPatientNoAndNo(patientNo,clinicNo);
						if(registration==null){
							map.put("resMsg", "error");
							map.put("resCode", "挂号信息不存在,开立医嘱失败!");
						}else{
							/**开具处方需填写临床诊断（门诊病历） 2017-02-23 18:52 aizhonghua**/
							OutpatientMedicalrecord medicalrecord = adviceService.queryRecord(registration.getClinicCode());
							if(medicalrecord==null){
								map.put("resMsg", "error");
								map.put("resCode", "开立处方前请填写临床诊断（门诊病历）!");
							}else{
								map = adviceService.savaAdviceInfo(voList,patientNo,idcard,registration);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "医嘱信息有误，请重新开立！");
			e.printStackTrace();
			logger.error("医嘱保存失败：", e);
			
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  获得历史医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryMedicalrecordHisList")
	public void queryMedicalrecordHisList(){
		Map<String,Object> retMap = new HashMap<String,Object>();
		try {
			List<AdviceVo> list = adviceService.queryMedicalrecordHisList(clinicNo,para,q);
			retMap.put("resMsg", "success");
			retMap.put("resCode", list);
		} catch (Exception e) {
			retMap.put("resMsg", "error");
			retMap.put("resCode", "医嘱查询失败！");
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  门诊医嘱-打开组套详情
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-9 上午11:34:22  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-9 上午11:34:22  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "viewStackInfoModle", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/stack.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewStackInfoModle() {
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  保存组套信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "savaStackInfo")
	public void savaStackInfo(){
		Map<String,Object> map =new HashMap<String,Object>();
		try {
			adviceService.savaStackInfo(jsonData,stackFlag,stackRemark,stackInputCode,stackSource,stackName,stackType,stackInpmertype,parent);
			map.put("resMsg", "success");
			map.put("resCode", "保存组套成功!");
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "未知错误请联系管理员！");
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：查询历史医嘱
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryHisAdvice")
	public void queryHisAdvice(){
		Map<String,Object> map =new HashMap<String,Object>();
		try {
			String treeJson = adviceService.queryHisAdvice(patientNo);
			map.put("resMsg", "success");
			map.put("resCode", treeJson);
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "历史医嘱查询失败！");
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：查询历史医嘱（加载更多）
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param：id收否分库patientNo就诊卡号para分区时间
	 */
	@Action(value = "queryHisAdviceNext")
	public void queryHisAdviceNext(){
		Map<String,Object> map =new HashMap<String,Object>();
		try {
			List<TreeJson> treeList = adviceService.queryHisAdviceNext(id,patientNo,para);
			map.put("resMsg", "success");
			map.put("resCode", treeList);
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "更多信息加载失败！");
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：诊出
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "passPatient")
	public void passPatient(){
		Map<String,String> map = new HashMap<String, String>();
		try {
			map = ationService.passPatient(clinicNo);
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "诊出失败！");
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：查询病历
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：
	 * @param:clinicNo 看诊号
	 * @version 1.0
	 *
	 */
	@Action(value = "queryRecord")
	public void queryRecord(){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			OutpatientMedicalrecord record = adviceService.queryRecord(clinicNo);
			map.put("resMsg", "success");
			map.put("resCode", record);
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "无病历信息！");
		}
		String json = JSONUtils.toExclusionJson(map, true,DateUtils.DATE_FORMAT,"");
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description： 删除医嘱
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "delAdvice")
	public void delAdvice(){
		Map<String, Object> map = adviceService.delAdvice(id);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description： 获得医生可开立的全部特限药品
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "querySpecialDrugMap")
	public void querySpecialDrugMap() throws IOException {
		Map<String,String> map = adviceService.querySpecialDrugMap();
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description： 获得待审核的患者信息树
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryAuditTree")
	public void queryAuditTree(){
		String json = adviceService.queryAuditTree(id);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description： 审核
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "savaAdviceAuditing")
	public void savaAdviceAuditing(){
		Map<String,String> retMap = new HashMap<String, String>();
		if(StringUtils.isBlank(id)){
			retMap.put("resMsg", "error");
			retMap.put("resCode", "审核信息有误，请重新选择！");
		}else if(start==null){
			retMap.put("resMsg", "error");
			retMap.put("resCode", "请选择审核状态！");
		}else{
			try {
				String clinicNo = adviceService.savaAdviceAuditing(id,start,remarks);
				retMap.put("resMsg", "success");
				retMap.put("resCode", "审核成功！");
				retMap.put("resValue", clinicNo);
			} catch (Exception e) {
				retMap.put("resMsg", "error");
				retMap.put("resCode", "审核失败！");
			}
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description： 查询历史医嘱
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "searchInfoHid")
	public void searchInfoHid(){
		Map<String,Object> retMap = adviceService.searchInfoHid(idCardNo);
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description： 查询特限药申请
	 * @Author：aizhonghua
	 * @CreateDate：2017-03-01 上午11:50:53  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-03-01 上午11:50:53  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "querySpeDrugApply")
	public void querySpeDrugApply(){
 		Map<String,String> retMap = new HashMap<String, String>();
		DrugSpedrug drugSpedrug = adviceService.querySpeDrugApply(clinicNo,para);
		if(drugSpedrug==null){
			retMap.put("resMsg", "error");
			retMap.put("resCode", "此挂号信息下无该特殊限制药申请，请向副高级以上医师提交相关申请！");
		}else if(drugSpedrug.getApplicState()==0){
			retMap.put("resMsg", "error");
			retMap.put("resCode", "您申请的该特限药品暂时未通过审核，请等待上级医师审核！");
		}else if(drugSpedrug.getApplicState()==1){
			retMap.put("resMsg", "success");
		}else if(drugSpedrug.getApplicState()==2){
			retMap.put("resMsg", "error");
			retMap.put("resCode", "您申请的该特限药品未通过审核，无法继续开立！");
		}else{
			retMap.put("resMsg", "error");
			retMap.put("resCode", "查询特限药申请失败！");
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description： 查询特限药申请组套
	 * @Author：aizhonghua
	 * @CreateDate：2017-03-01 上午11:50:53  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-03-01 上午11:50:53  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "querySpeDrugApplyStack")
	public void querySpeDrugApplyStack(){
		Map<String,String> retMap = new HashMap<String, String>();
		List<DrugSpedrug> dsList = adviceService.querySpeDrugApplyStack(clinicNo,para);
		if(dsList!=null && dsList.size()>0){
			StringBuffer buffer = new StringBuffer();
			if(para.split(",").length!=dsList.size()){
				buffer.append("该挂号信息下没有选择的全部特限药申请！其中：");
			}
			int i = 0;
			for(DrugSpedrug spedrug : dsList){
				if(spedrug.getApplicState()==0){
					if(StringUtils.isNotBlank(buffer.toString())){
						buffer.append("，");
					}
					buffer.append("【").append(spedrug.getDrugName()).append("】").append("暂时未通过审核，请等待上级医师审核！");
				}else if(spedrug.getApplicState()==1){
					i ++ ;
				}else if(spedrug.getApplicState()==2){
					if(StringUtils.isNotBlank(buffer.toString())){
						buffer.append("，");
					}
					buffer.append("【").append(spedrug.getDrugName()).append("】").append("未通过审核，无法继续开立！");
				}else{
					if(StringUtils.isNotBlank(buffer.toString())){
						buffer.append("，");
					}
					buffer.append("【").append(spedrug.getDrugName()).append("】").append("审核信息查询失败！");
				}
			}
			if(i==para.split(",").length){
				retMap.put("resMsg", "success");
			}else{
				retMap.put("resMsg", "error");
				retMap.put("resCode", buffer.toString());
			}
		}else{
			retMap.put("resMsg", "error");
			retMap.put("resCode", "此挂号信息下无选择信息的特殊限制药申请，请向副高级以上医师提交相关申请！");
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * 查询处方患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value="queryPatientInfo")
	public void queryPatientInfo(){
		if(StringUtils.isBlank(startTime)){
			startTime=DateUtils.formatDateY_M_D_H_M_S(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));
		}
		if(StringUtils.isBlank(endTime)){
			endTime=DateUtils.formatDateY_M_D_H_M_S(DateUtils.getCurrentTime());	
		}
		Map<String,Object> map = adviceService.queryPatientInfo(page,rows,startTime,endTime,type,para,vague);
		String retJson = JSONUtils.toJson(map);
		WebUtils.webSendJSON(retJson);
	}
	
	/**  
	 *  
	 * 根据处方号查询处方信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value="queryRecipelInfo")
	public void queryRecipelInfo(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<RecipelInfoVo> rows = adviceService.getRecipelInfoRows(recipeNo,tab);
		map.put("rows",rows);
		String retJson = JSONUtils.toJson(rows);
		WebUtils.webSendJSON(retJson);
	}
	
	/** 跳转电子病历界面
	* @Title: emrMain 跳转电子病历界面
	* @Description: 跳转电子病历界面
	* @author dtl 
	* @date 2017年4月7日
	*/
	@Action(value = "emrMain", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/emrMain.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String emrMain() {
		return "list";
	}
	/**  
	 * 
	 * 打印医嘱
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月7日 下午4:50:28 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月7日 下午4:50:28 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value = "iReportToMedicalRecord")
	public void iReportToMedicalRecord() throws Exception {
		try {
			String fileName = request.getParameter("fileName");
			String clinicCode= request.getParameter("clinicCode");
			String patientNo= request.getParameter("patientNo");
			String root_path = request.getSession().getServletContext().getRealPath("/");
			root_path = root_path.replace('\\', '/');
			String reportFilePath = root_path + webPath +fileName+".jasper";
			
			List<String> array=new ArrayList<String>();
			//获取处方信息
			Map<String,List<OutpatientVo>>  map= new HashMap<String, List<OutpatientVo>>();
			List<OutpatientVo> outpatientVos=adviceService.getOutpatientVo(clinicCode,patientNo);
			List<OutpatientVo> voList = null;
			for(OutpatientVo outpatientVo : outpatientVos){
				if(map.get(outpatientVo.getRecipeNo())==null){
					voList = new ArrayList<OutpatientVo>();
					voList.add(outpatientVo);
					map.put(outpatientVo.getRecipeNo(), voList);
					array.add(outpatientVo.getRecipeNo());
				}else{
					map.get(outpatientVo.getRecipeNo()).add(outpatientVo);
				}
			 }
			//获取患者信息
			List<RegisterMainVo> registerMainVos = adviceService.getRegisterMainVo(array);			
			Map<String,RegisterMainVo> recipelStatVoMap = new HashMap<String, RegisterMainVo>();
			for (RegisterMainVo registerMainVo : registerMainVos) {
				recipelStatVoMap.put(registerMainVo.getRecipeNo(), registerMainVo);
			}		
	
			List<HashMap<String,Object>> o=new ArrayList<HashMap<String,Object>>();
			HashMap<String, Object> parameters = null;
			JRDataSource jrd = null;
			List<RegisterMainVo> recipelStatList= null;
			for(String n : array){
				for(int i=0;i<2;i++){
					parameters = new HashMap<String, Object>();
					recipelStatList = new ArrayList<RegisterMainVo>();
					RegisterMainVo vo = recipelStatVoMap.get(n);
					List<OutpatientVo> l = map.get(n);
					vo.setList(l);
					recipelStatList.add(vo);
					jrd = new JRBeanCollectionDataSource(recipelStatList);					
					parameters.put("hName", HisParameters.PREFIXFILENAME);
					parameters.put("PAYDATE",DateUtils.formatDateY_M_D_H_M(new Date()));
					parameters.put("SUBREPORT_DIR", root_path + webPath);
					parameters.put("dataSource", jrd);
					parameters.put("recipeNo", vo.getRecipeNo());
					parameters.put("name", vo.getName());
					parameters.put("sex", vo.getSex());
					parameters.put("age", vo.getAge());
					parameters.put("dept", vo.getDept());
					parameters.put("code", vo.getCode());
					parameters.put("cont", vo.getCont());
					parameters.put("date", vo.getTime());
					parameters.put("mediNo", vo.getMediNo());
					parameters.put("dct", vo.getDct());
					parameters.put("dia", vo.getDia());
					parameters.put("pay", vo.getPay());
					if(i==0){
						parameters.put("isLow", null);
					}else{
						parameters.put("isLow", "底方");
					}
					o.add(parameters);
				}
			}
		   iReportService.doReportToJavaBean2(request,WebUtils.getResponse(),reportFilePath,o);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**  
	 * 
	 * 门诊检查单
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月10日 上午10:03:42 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月10日 上午10:03:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value = "iReportToInspectionCheck")
	public void iReportToInspectionCheck() throws Exception {
		try {
			String fileName = request.getParameter("fileName");
			String clinicCode= request.getParameter("clinicCode");
			String patientNo= request.getParameter("patientNo");
			User user = (User)SessionUtils.getCurrentUserFromShiroSession();
			String applydoc=user.getName();
			Date dt=new Date();
			String newdt=DateUtils.formatDateY_M_D(dt);
			
			String root_path = request.getSession().getServletContext().getRealPath("/");
			root_path = root_path.replace('\\', '/');
			String reportFilePath = root_path + webPath +fileName+".jasper";
	
			List<IreportPatientVo> ireportPatientVo = adviceService.getIreportPatientVo(patientNo, clinicCode);
			List<HashMap<String,Object>> o=new ArrayList<HashMap<String,Object>>();
			HashMap<String, Object> parameters = null;
			JRDataSource jrd = null;
			List<IreportPatientVo> recipelStatList= null;
			for (IreportPatientVo vo : ireportPatientVo) {
				recipelStatList= new ArrayList<IreportPatientVo>();
				recipelStatList.add(vo);
				jrd = new JRBeanCollectionDataSource(recipelStatList);
				parameters = new HashMap<String, Object>();
				parameters.put("hospitalName", HisParameters.PREFIXFILENAME);
				parameters.put("SUBREPORT_DIR", root_path + webPath);
				parameters.put("CLINIC_CODE", clinicCode);
				parameters.put("APPLYDOC", applydoc);
				parameters.put("APPLYDATE", newdt);
				parameters.put("dataSource", jrd);
				o.add(parameters);
			}
		   iReportService.doReportToJavaBean2(request,WebUtils.getResponse(),reportFilePath,o);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**  
	 *  
	 * 跳转list页面(门诊就医中的住院患者信息)
	 * @Author：gaotiantian
	 * @CreateDate：2017-3-31 下午02:05:31  
	 * @Modifier：gaotiantian
	 * @ModifyDate：2017-3-31 下午02:05:31
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "inpatientInforList", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/inpatientInfo.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String InpatientInfoIndex() {
		startTime=DateUtils.formatDateY_M_D_H_M_S(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));
		endTime=DateUtils.formatDateY_M_D_H_M_S(DateUtils.getCurrentTime());
		return "list";
	}
	
	/**  
	 *  
	 * 查询住院患者信息
	 * @Author：gaotiantian
	 * @CreateDate：2017-3-31 下午02:05:31  
	 * @Modifier：gaotiantian
	 * @ModifyDate：2017-3-31 下午02:05:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryInpatientInfoList")
	public void InpatientInfoList() {
		if(StringUtils.isBlank(startTime)){
			startTime=DateUtils.formatDateY_M_D_H_M_S(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));
		}
		if(StringUtils.isBlank(endTime)){
			endTime=DateUtils.formatDateY_M_D_H_M_S(DateUtils.getCurrentTime());	
		}
		Map<String,Object> map = adviceService.getRows(page,rows,startTime,endTime,type,para,vague);
		String retJson = JSONUtils.toJson(map);
		WebUtils.webSendJSON(retJson);
	}
	
	/**  
	 *  
	 * 根据住院号查询医嘱信息
	 * @Author：gaotiantian
	 * @CreateDate：2017-3-31 下午02:05:31  
	 * @Modifier：gaotiantian
	 * @ModifyDate：2017-3-31 下午02:05:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value="queryInpatientAdviceInfo")
	public void InpatientAdviceInfo(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<InpatientInfoVo> rows = adviceService.getInpatientInfoRows(inpatientNo, tab);
		map.put("rows",rows);
		String retJson = JSONUtils.toJson(rows);
		WebUtils.webSendJSON(retJson);
	}
	
	/**
	 * lis查询
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月12日 下午7:13:54 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月12日 下午7:13:54 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="findLis")
	public void findLis(){
		String cardNo = request.getParameter("queryName");
		Map<String,Object> map = new HashMap<String, Object>();
		//根据就诊卡号查询
		List<LisVo> list = new ArrayList<LisVo>();
		Integer total = 0;
		if(StringUtils.isNotBlank(cardNo)){
			list = adviceService.findLis(cardNo,page,rows);
			total = adviceService.findLisNum(cardNo);
		}
		map.put("rows",list);
		map.put("total",total);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**
	 * lis详情
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月13日 上午10:29:37 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月13日 上午10:29:37 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="findLisDetail")
	public void findLisDetail(){
		String inspectionId = request.getParameter("id");
		Map<String,Object> map = new HashMap<String, Object>();
		List<InspectionReportList> list = new ArrayList<InspectionReportList>();
		if(StringUtils.isNotBlank(inspectionId)){
			list = adviceService.findLisDetail(inspectionId);
		}
		map.put("rows",list);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	
}


