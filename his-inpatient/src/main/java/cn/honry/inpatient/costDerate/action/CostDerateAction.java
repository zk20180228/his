package cn.honry.inpatient.costDerate.action;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inpatient.costDerate.service.CostDerateService;
import cn.honry.inpatient.costDerate.vo.DerateVo;
import cn.honry.inpatient.costDerate.vo.ThreeSearchVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**   
*  
* @className： CostDerateAction
* @description：费用减免action
* @author：tcj
* @createDate：2015-12-9   
* @modifyRmk：  
* @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/costDerate")
public class CostDerateAction extends ActionSupport implements ModelDriven<InpatientDerate>{

	private Logger logger=Logger.getLogger(CostDerateAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	/**
	 * 栏目别名
	 */
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CostDerateService costDerateService;
	@Autowired
	@Qualifier(value = "costDerateService")
	public void setCostDerateService(CostDerateService costDerateService) {
		this.costDerateService = costDerateService;
	}
	private InpatientDerate inpatientDerate = new InpatientDerate();
	
	public InpatientDerate getInpatientDerate() {
		return inpatientDerate;
	}
	public void setInpatientDerate(InpatientDerate inpatientDerate) {
		this.inpatientDerate = inpatientDerate;
	}
	@Override
	public InpatientDerate getModel() {
		return inpatientDerate;
	}
	/**
	 * 科室Code
	 */
	private String deptCode;

	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
	 * @Description：  获取费用减免页面
	 * @Author：TCJ
	 * @CreateDate：2015-1-9 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"FYJM:function:view"})
	@Action(value = "listCostDerate", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/costDerate/costDerateList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listConsultation(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		ServletActionContext.getRequest().setAttribute("now", df.format(new Date()));
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		ServletActionContext.getRequest().setAttribute("userId", user.getAccount());
		if(dept!=null){
			ServletActionContext.getRequest().setAttribute("deptCode", dept.getDeptCode());
		}
		return "list";
	}
	/**  
	 * @Description：  根据病历号查询最新的接诊记录
	 * @Author：TCJ
	 * @CreateDate：2015-1-11 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryInpatientInfoObj")
	public void queryInpatientInfoObj(){
		try{
			String medicalrecordId=ServletActionContext.getRequest().getParameter("medicalrecordId");
			String deptCode=ServletActionContext.getRequest().getParameter("deptCode");
			DerateVo obj=costDerateService.queryInpatientInfoObj(medicalrecordId,deptCode);
			if(obj.getFreeCost()!=null){
				BigDecimal bg = new BigDecimal(obj.getFreeCost()).setScale(2, RoundingMode.UP);
				obj.setFreeCost(bg.doubleValue());
			}
			if(obj.getOwnCost()!=null){
				BigDecimal bg1 = new BigDecimal(obj.getOwnCost()).setScale(2, RoundingMode.UP);
				obj.setOwnCost(bg1.doubleValue());
			}
			if(obj.getPayCost()!=null){
				BigDecimal bg2 = new BigDecimal(obj.getPayCost()).setScale(2, RoundingMode.UP);
				obj.setPayCost(bg2.doubleValue());
			}
			if(obj.getPrepayCost()!=null){
				BigDecimal bg3 = new BigDecimal(obj.getPrepayCost()).setScale(2, RoundingMode.UP);
				obj.setPrepayCost(bg3.doubleValue());
			}
			if(obj.getPubCost()!=null){
				BigDecimal bg4 = new BigDecimal(obj.getPubCost()).setScale(2, RoundingMode.UP);
				obj.setPubCost(bg4.doubleValue());
			}
			if(obj.getTotCost()!=null){
				BigDecimal bg5 = new BigDecimal(obj.getTotCost()).setScale(2, RoundingMode.UP);
				obj.setTotCost(bg5.doubleValue());
			}
			Map<String,Object> map=new HashMap<String,Object>();
			if(obj.getPatientName()!=null){
				map.put("resMsg","success");
				map.put("resCode", obj);
			}else{
				map.put("resMsg", "error");
				map.put("resCode", "该患者不在当前病区");
			}
			String json=JSONUtils.toJson(map,DateUtils.DATE_FORMAT);
			WebUtils.webSendJSON(json);
		}catch(Exception ex){
			logger.error("ZYSF_FYJM", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_FYJM", "住院收费_费用减免", "2", "0"), ex);
		}
	}
	/**  
	 * @Description：  根据住院流水号查询ThreeSarchVo的信息
	 * @Author：TCJ
	 * @CreateDate：2015-1-11 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryThreeSearch", results = { @Result(name = "json", type = "json") })
	public void queryThreeSearch(){
		try {
			String inpatientNo=ServletActionContext.getRequest().getParameter("inpatientNo");
			List<ThreeSearchVo> tsv=costDerateService.queryThreeSearch(inpatientNo);
			for(ThreeSearchVo vo:tsv){
				if(vo.getOwnCost()!=null){
					BigDecimal bg1 = new BigDecimal(vo.getOwnCost()).setScale(2, RoundingMode.UP);
					vo.setOwnCost(bg1.doubleValue());		
				}
				if(vo.getPayCost()!=null){
					 BigDecimal bg2 = new BigDecimal(vo.getPayCost()).setScale(2, RoundingMode.UP);
					 vo.setPayCost(bg2.doubleValue());
				}
				if(vo.getTotCost()!=null){
					 BigDecimal bg3 = new BigDecimal(vo.getTotCost()).setScale(2, RoundingMode.UP);
					 vo.setTotCost(bg3.doubleValue());
				}
				if(vo.getPubCost()!=null){
					 BigDecimal bg4 = new BigDecimal(vo.getPubCost()).setScale(2, RoundingMode.UP);
					 vo.setPubCost(bg4.doubleValue());
				}
			}
				PrintWriter out = WebUtils.getResponse().getWriter();
				String json = JSONUtils.toJson(tsv);
				out.write(json);
		}
		catch (Exception ex) {
			logger.error("ZYSF_FYJM", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_FYJM", "住院收费_费用减免", "2", "0"), ex);
			System.out.println(ex.getMessage());
		}
	}
	/**  
	 * @Description：  根据住院流水号查询费用减免表中的记录
	 * @Author：TCJ
	 * @CreateDate：2015-1-11 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryDerate", results = { @Result(name = "json", type = "json") })
	public void queryDerate(){
		try {
			String inpatientNo=ServletActionContext.getRequest().getParameter("inpatientNo");
			List<InpatientDerate> lst=costDerateService.queryDerate(inpatientNo);
				PrintWriter out = WebUtils.getResponse().getWriter();
				String json = JSONUtils.toJson(lst,"yyyy/MM/dd HH:mm:ss");
				out.write(json);
			}
		catch (Exception ex) {
			logger.error("ZYSF_FYJM", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_FYJM", "住院收费_费用减免", "2", "0"), ex);
			System.out.println(ex.getMessage());
		}
	}
	/**  
	 * @Description：  保存费用减免信息
	 * @Author：TCJ
	 * @CreateDate：2015-1-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "saveDerateDategrid", results = { @Result(name = "json", type = "json") })
	public void saveDerateDategrid(){
		String derateJson=ServletActionContext.getRequest().getParameter("date");
		String no=ServletActionContext.getRequest().getParameter("no");
		try{
			costDerateService.saveDerateDategrid(derateJson,no);
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("ZYSF_FYJM", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_FYJM", "住院收费_费用减免", "2", "0"), e);
		}
	}
	/**  
	 * @Description：  查询最小费用名称Map
	 * @Author：TCJ
	 * @CreateDate：2015-1-18
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "quertFreeCodeMap", results = { @Result(name = "json", type = "json") })
	public void quertFreeCodeMap(){
		try{
			List<BusinessDictionary> freeCodeList = innerCodeService.getDictionary("drugMinimumcost");
			Map<String,String> feeCodeMap = new HashMap<String,String>();
			for(BusinessDictionary freeCode:freeCodeList){
				feeCodeMap.put(freeCode.getEncode(), freeCode.getName());
			}
			String json=JSONUtils.toJson(feeCodeMap);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_FYJM", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_FYJM", "住院收费_费用减免", "2", "0"), e);	
		}
	}
	/**  
	 * @Description： 通过患者Id从住院主表中查询病历号
	 * @Author：TCJ
	 * @CreateDate：2015-1-18
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryInformationById", results = { @Result(name = "json", type = "json") })
	public void queryInformationById(){
		String inpatientId=ServletActionContext.getRequest().getParameter("inpatientId");
		InpatientInfoNow ipInfo=costDerateService.querNurseCharge(inpatientId);
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			String json = JSONUtils.toJson(ipInfo.getMedicalrecordId());
			out.write(json);
		}
		catch (Exception ex) {
			logger.error("ZYSF_FYJM", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_FYJM", "住院收费_费用减免", "2", "0"), ex);
			System.out.println(ex.getMessage());
		}
	}
	/**  
	 * @Description： 根据最小费用名称分组查询减免金额的和
	 * @Author：TCJ
	 * @CreateDate：2016-2-18
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryDerateMoneySum", results = { @Result(name = "json", type = "json") })
	public void queryDerateMoneySum(){
		try{
			String inpatientNo=ServletActionContext.getRequest().getParameter("inpatientNo");
			List<InpatientDerate> ipd=costDerateService.queryDerateMoneySum(inpatientNo);
			for(InpatientDerate pd:ipd){
				if(pd.getDerateCost()!=null){
					BigDecimal bg= new BigDecimal(pd.getDerateCost()).setScale(2, RoundingMode.UP);
					pd.setDerateCost(bg.doubleValue());			
				}
			}
			String json=JSONUtils.toJson(ipd);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_FYJM", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_FYJM", "住院收费_费用减免", "2", "0"), e);
		}
	}

	/**  
	 *  
	 * @Description：本区患者树
	 * @Author：tuchuanjiang
	 * @CreateDate：2016-3-21
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "patientNarTree")
	public void patientNarTree() {
	    List<TreeJson> treeDepat = null;
	    String deptCode = ServletActionContext.getRequest().getParameter("deptCode");
		try {
			treeDepat = costDerateService.patientNarTree(deptCode,inpatientDerate.getId());
		} catch (Exception e) {
			logger.error("ZYSF_FYJM", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_FYJM", "住院收费_费用减免", "2", "0"), e);
			e.printStackTrace();
		}
		String json=JSONUtils.toJson(treeDepat);
		WebUtils.webSendJSON(json);
	} 

	/**  
	 * @Description： 根据病历号或住院号查询信息
	 * @Author：donghe
	 * @CreateDate：2016-3-21
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryInpatientInfoLi", results = { @Result(name = "json", type = "json") })
	public void queryInpatientInfoLi(){
		try {
		//前台传来的是就诊卡号，因牵扯较多，未另建变量
		String medicalrecordId=ServletActionContext.getRequest().getParameter("medicalrecordId");
		if(StringUtils.isNotBlank(medicalrecordId)){
			medicalrecordId=medicalrecordId.trim();
		}
		//通过接口查询就诊卡号对应的病历号
		List<InpatientInfoNow> ipd=costDerateService.queryInpatientInfoList(medicalrecordId);
			PrintWriter out = WebUtils.getResponse().getWriter();
			String json = JSONUtils.toJson(ipd);
			out.write(json);
		}
		catch (Exception ex) {
			logger.error("ZYSF_FYJM", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_FYJM", "住院收费_费用减免", "2", "0"), ex);
			System.out.println(ex.getMessage());
		}
	}
	/**
	 * 医生下拉框渲染
	 * @Author：tuchuanjiang	
	 * @version 1.0
	 */
	@Action(value = "empComboboxDerate")
	public void empComboboxDerate(){
		try{
			List<SysEmployee> empl=costDerateService.empComboboxDerate();
			Map<String,String> map=new HashMap<String,String>();
			for(SysEmployee emp:empl){
				map.put(emp.getId(), emp.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_FYJM", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_FYJM", "住院收费_费用减免", "2", "0"), e);
		}
	}
	
	/**
	 * userMap查询
	 * @Author：tuchuanjiang	
	 * @version 1.0
	 */
	@Action(value = "queryUserDerate")
	public void queryUserDerate(){
		try{
			Map<String, String> userlist=employeeInInterService.queryEmpCodeAndNameMap();
			String json=JSONUtils.toJson(userlist);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_FYJM", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_FYJM", "住院收费_费用减免", "2", "0"), e);
		}
	}
	
	/**
	 * deptMap查询
	 * @Author：tuchuanjiang	
	 * @version 1.0
	 */
	@Action(value = "queryDeptMapPublic")
	public void queryDeptMapPublic(){
		try{
			Map<String, String> userlist=deptInInterService.querydeptCodeAndNameMap();
			String json=JSONUtils.toJson(userlist);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_FYJM", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_FYJM", "住院收费_费用减免", "2", "0"), e);
		}
	}
}
