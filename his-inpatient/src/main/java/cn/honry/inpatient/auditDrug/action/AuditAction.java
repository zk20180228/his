package cn.honry.inpatient.auditDrug.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inpatient.auditDrug.service.AuditService;
import cn.honry.inpatient.auditDrug.vo.Parameter;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/***
 * 
 * @ClassName: AuditAction 
 * @Description: 摆药单核审Action
 * @author ldl
 * @date 2016年4月15日 下午6:24:35 
 *
 *    在住院药房管理下的的摆药单核审
 *    可以对摆药单进行核准操作，
 *    对于非退药的情况同时进行扣库存，
 *    需要在此处录入核准人的工号，以记录发药员。
 *    只有药师、药剂师可进行核准操作。
 *  此功能在护士站药品集中发送后进行。
 *  
 */
/**
 * @author win7
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/auditDrug")
public class AuditAction extends ActionSupport{
	private Logger logger=Logger.getLogger(AuditAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private static final long serialVersionUID = 1L;
	
	//栏目别名,在主界面中点击栏目时传到action的参数
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	 /**
	  * 注入本类service
	  */
	@Autowired
	@Qualifier(value = "auditService")
	private AuditService auditService;
	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}
	
	 /**
	  * 参数列表（前后台传值）
	  */
	private Parameter parameter;
	
	public Parameter getParameter() {
		return parameter;
	}
	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
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
	 * 注入系统参数的service
	 * 调用接口
	 */
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	/**  
	 * @Description：  访问住院摆药单审核
	 * @Author：ldl
	 * @CreateDate：2016-04-29 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "auditList",results={ @Result(name="view",location="/WEB-INF/pages/inpatient/auditDrug/auditDrugList.jsp")},interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String auditList(){
		try {
			parameter = new Parameter();
			//根据参数查询 摆药是否需要核准
			String parameterHz = parameterInnerService.getparameter("isNeedApproval");
			if("".equals(parameterHz)){
				parameterHz = "1";
			}
			parameter.setParameterHz(parameterHz);
			//根据参数查询 退费时是申请 还是直接退费
			String parameterTf = parameterInnerService.getparameter("tytssfxyljtf");
			if("".equals(parameterTf)){
				parameterTf = "0";
			}
			parameter.setParameterTf(parameterTf); 
			//默认一个摆药台
			String deptCode =null;
			if(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession() != null){
				deptCode=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
			}
			OutpatientDrugcontrol drugcontril = auditService.queryDrugcontril(deptCode);
			//参数 发送类型
			parameter.setSendType(drugcontril.getSendType());
			//参数 摆药台Id
			parameter.setControlId(drugcontril.getId());
			//摆药台数据显示类型
			parameter.setShowLevel(drugcontril.getShowLevel());
		} catch (Exception e) {
			logger.error("ZYYFGL_BYDHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYFGL_BYDHZ", "住院药房管理_摆药单核准", "2", "0"), e);
		}
		return "view";
	}

	/**  
	 * @Description：核准跳转
	 * @Author：ldl
	 * @CreateDate：2016-04-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "examineDrugList",results={ @Result(name="view",location="/WEB-INF/pages/inpatient/auditDrug/examineDrugList.jsp")},interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String examineDrugList(){
		return "view";
	}
	
	/**  
	 * @Description：跳转退药台
	 * @Author：ldl
	 * @CreateDate：2016-04-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "withdrawalDrugList",results={ @Result(name="view",location="/WEB-INF/pages/inpatient/auditDrug/withdrawalDrugList.jsp")},interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String withdrawalDrugList(){
		return "view";
	}
	
	
	/**  
	 * @Description：  进入页面时首先选择当前登陆药房下的摆药台
	 * @Author：ldl
	 * @CreateDate：2016-04-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryDrugHouse")
	public void queryDrugHouse(){
		try {
			String deptCode =null;
			if(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession() != null){
				deptCode=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
			}
			List<OutpatientDrugcontrol> drugcontrolList = auditService.queryDrugHouse(deptCode);
			String mapJosn = JSONUtils.toJson(drugcontrolList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("ZYYFGL_BYDHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYFGL_BYDHZ", "住院药房管理_摆药单核准", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description：摆药树
	 * @Author：ldl
	 * @CreateDate：2016-04-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "approveNoDrugTree")
	public void approveNoDrugTree() {
		try {
			List<TreeJson> treeDepar =  auditService.approveNoDrugTree(parameter.getControlId(),parameter.getSendType(),parameter.getOpType(),parameter.getApplyState(),parameter.getSendFlag(),parameter.getMedicalrecordId());
			String mapJosn = JSONUtils.toJson(treeDepar);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("ZYYFGL_BYDHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYFGL_BYDHZ", "住院药房管理_摆药单核准", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description：摆药明细记录
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "findDeptSummary")
	public void findDeptSummary() {
		try {
			List<DrugApplyoutNow> applyoutList =  auditService.findDeptSummary(parameter.getDrugedBill(),parameter.getOpType(),parameter.getSendType(),parameter.getSendFlag(),parameter.getApplyState());
			String mapJosn = JSONUtils.toJson(applyoutList,DateUtils.DATE_FORMAT);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("ZYYFGL_BYDHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYFGL_BYDHZ", "住院药房管理_摆药单核准", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description：科室汇总记录
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "detailed")
	public void detailed() {
		try {
			List<DrugApplyoutNow> applyoutList =  auditService.findDetailed(parameter.getDrugedBill(),parameter.getOpType(),parameter.getSendType(),parameter.getSendFlag(),parameter.getApplyState(),parameter.getDeptId());
			String mapJosn = JSONUtils.toJson(applyoutList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("ZYYFGL_BYDHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYFGL_BYDHZ", "住院药房管理_摆药单核准", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description：摆药过程
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "approvalDrugSave")
	public void approvalDrugSave() {
		try{
			Map<String,String> map = auditService.approvalDrugSave(parameter.getApplyNumberCode(),parameter.getParameterHz());
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);			
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("ZYYFGL_BYDHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYFGL_BYDHZ", "住院药房管理_摆药单核准", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：根据住院号查询住院流水号
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryMedicalrecordId")
	public void queryMedicalrecordId() {
		try {
			Map<String,String> map = auditService.queryMedicalrecordId(parameter.getIdCard());
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("ZYYFGL_BYDHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYFGL_BYDHZ", "住院药房管理_摆药单核准", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Description：药品出库核准
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "examineDrugSaveAndUpdate")
	public void examineDrugSaveAndUpdate() {
		try{
			Map<String,String> map = auditService.examineDrugSaveAndUpdate(parameter.getApplyNumberCode(),parameter.getIds());
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);			
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("ZYYFGL_BYDHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYFGL_BYDHZ", "住院药房管理_摆药单核准", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：退药过程
	 * @Author：ldl
	 * @CreateDate：2016-05-06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "withdrawalDrug")
	public void withdrawalDrug() {
		try{
			Map<String,String> map = auditService.withdrawalDrug(parameter.getApplyNumberCode(),parameter.getParameterTf());
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);		
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("ZYYFGL_BYDHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYFGL_BYDHZ", "住院药房管理_摆药单核准", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：渲染单位
	 * @Author：ldl
	 * @CreateDate：2016-05-06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "packFunction")
	public void packFunction() {
		try {
			//药品编码
			List<BusinessDictionary> drugpackagingunitList = innerCodeService.getDictionary("minunit");
			Map<String,String> packMap = new HashMap<String, String>();
			for(BusinessDictionary pack : drugpackagingunitList){
				packMap.put(pack.getEncode(), pack.getName());
			}
			String mapJosn = JSONUtils.toJson(packMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("ZYYFGL_BYDHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYFGL_BYDHZ", "住院药房管理_摆药单核准", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：渲染医嘱类型
	 * @Author：ldl
	 * @CreateDate：2016-05-06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "submissiveFunction")
	public void submissiveFunction() {
		try {
			List<InpatientKind> kindList =  auditService.queryKindFunction();
			Map<String,String> kindMap = new HashMap<String, String>();
			for(InpatientKind kind : kindList){
				kindMap.put(kind.getTypeCode(), kind.getTypeName());
			}
			String mapJosn = JSONUtils.toJson(kindMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			logger.error("ZYYFGL_BYDHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYFGL_BYDHZ", "住院药房管理_摆药单核准", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	
}
