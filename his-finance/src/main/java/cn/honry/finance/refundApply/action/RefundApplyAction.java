package cn.honry.finance.refundApply.action;

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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.finance.refundApply.service.RefundApplyServcie;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/finance/refundApply")
//@Namespace(value = "/outpatient/refundApply")
@SuppressWarnings("all")
public class RefundApplyAction extends ActionSupport{
	Logger logger = Logger.getLogger(RefundApplyAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	/**
     * 栏目别名,在主界面中点击栏目时传到action的参数
     */
	private String menuAlias;

	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	/**
     * 发票号
     */
	private String invoiceNo;
	
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	/**
     * 药品收费记录
     */
	private String drugList;
	
	public String getDrugList() {
		return drugList;
	}
	public void setDrugList(String drugList) {
		this.drugList = drugList;
	}
	
	/**
     * 非药品
     */
	
	private String unDrugList;

	public String getUnDrugList() {
		return unDrugList;
	}
	public void setUnDrugList(String unDrugList) {
		this.unDrugList = unDrugList;
	}

	/**
     * 门诊号
     */
	private String clinicCode;
	
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	
	

	/**
     * 退费service
     */
	@Autowired
	@Qualifier(value = "refundApplyService")
	private RefundApplyServcie refundApplyService;
	public void setRefundApplyService(RefundApplyServcie refundApplyService) {
		this.refundApplyService = refundApplyService;
	}
	
	/**  
	 * @Description：访问门诊退费申请
	 * @Author：ldl
	 * @CreateDate：2016-06-24
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "toView", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/refundApply/refundApply.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toView() {
		return "list";
	}
	
	/**  
	 * @Description：门诊退费申请
	 * @Author：ldl
	 * @CreateDate：2016-06-27
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "saveRefund")
	public void saveRefund(){
		try{
			Map<String,String> map =  refundApplyService.saveRefund(invoiceNo,drugList,unDrugList,clinicCode);
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZTF_MZTFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZTF_MZTFSQ", "门诊退费_门诊退费申请", "1", "0"), e);
		}
	}
	

}
