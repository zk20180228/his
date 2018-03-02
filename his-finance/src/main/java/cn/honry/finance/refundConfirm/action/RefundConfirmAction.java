package cn.honry.finance.refundConfirm.action;

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

import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.finance.medicinelist.service.MedicinelistService;
import cn.honry.finance.refund.service.RefundService;
import cn.honry.finance.refundConfirm.service.RefundConfirmService;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/finance/refundConfirm")
//@Namespace(value = "/outpatient/refundConfirm")
@SuppressWarnings("all")
public class RefundConfirmAction extends ActionSupport{
	Logger logger = Logger.getLogger(RefundConfirmAction.class);
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
     * 退费申请实体
     */
	private InpatientCancelitem cancelitem;
	
	public InpatientCancelitem getCancelitem() {
		return cancelitem;
	}
	public void setCancelitem(InpatientCancelitem cancelitem) {
		this.cancelitem = cancelitem;
	}
	
	/**
     * 退费药品ID集合
     */
	private String drugIds;
	
	public String getDrugIds() {
		return drugIds;
	}
	public void setDrugIds(String drugIds) {
		this.drugIds = drugIds;
	}

	/**
     * 病历号
     */
	private String medicalRecord;
	
	public String getMedicalRecord() {
		return medicalRecord;
	}
	public void setMedicalRecord(String medicalRecord) {
		this.medicalRecord = medicalRecord;
	}

	/**
     * 退费路径
     */
	private String payType;
	
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	/**
     * 原发票号
     */
	private String invoiceNo;
	
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	
	/**
     * 退费金额
     */
	private String applyCost;
	
	public String getApplyCost() {
		return applyCost;
	}
	public void setApplyCost(String applyCost) {
		this.applyCost = applyCost;
	}

	/**
     * 退费service
     */
	@Autowired
	@Qualifier(value = "refundConfirmService")
	private RefundConfirmService refundConfirmService;
	public void setRefundConfirmService(RefundConfirmService refundConfirmService) {
		this.refundConfirmService = refundConfirmService;
	}
	
	/**
     * 退费service
     */
	@Autowired
	@Qualifier(value = "refundService")
	private RefundService refundService;
	
	public void setRefundService(RefundService refundService) {
		this.refundService = refundService;
	}
	
	/**
     * 门诊收费service
     */
    @Autowired
    @Qualifier(value = "medicinelistService")
    private MedicinelistService medicinelistService;
	public void setMedicinelistService(MedicinelistService medicinelistService) {
		this.medicinelistService = medicinelistService;
	}
	
	/**  
	 * @Description：访问门诊退费申请
	 * @Author：ldl
	 * @CreateDate：2016-06-24
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "toView", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/refundConfirm/refundConfirm.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toView() {
		return "list";
	}
	
	/**  
	 * @Description：查询患者收费信息，和退费申请记录
	 * @Author：ldl
	 * @CreateDate：2016-06-28
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "query")
	public void query(){
		try{
			Map<String,Object> map = new HashMap<String, Object>();
			//系统参数退费支付方式
			HospitalParameter parameterPayType = refundService.queryParameterByPayType();
			payType = parameterPayType.getParameterValue();
			Double applyCost = 0.0;//初始化申请总金额
			String name = null; //初始化患者姓名
			String medicalRecord = null;//初始化病历号
			List<FinanceInvoiceInfoNow> invoiceseqs = refundService.findInvoiceInfoByInvoiceNo(cancelitem.getBillNo());//通过发票号获取发票序号
			String invoiceNos = "";
			String invoiceSeqs = "";
			if(invoiceseqs.size()>0){
				for(FinanceInvoiceInfoNow info : invoiceseqs){//遍历得到所有的发票序号
					if(invoiceSeqs!=""){
						invoiceSeqs = invoiceSeqs + "','";
					}
					invoiceSeqs = invoiceSeqs + info.getInvoiceSeq();
				}
				List<FinanceInvoiceInfoNow> invoiceInfoLists = refundService.findInvoiceInfoByInvoiceSeqs(invoiceSeqs);//通过发票序号获取发票号
				for (FinanceInvoiceInfoNow f : invoiceInfoLists) {
					if(invoiceNos!=""){
						invoiceNos += "','";
					}
					invoiceNos += f.getInvoiceNo();
				}
			}
			List<InpatientCancelitemNow> cancelitemList =  refundConfirmService.query(invoiceNos);//通过发票号获取所有退费信息
			if(cancelitemList.size()>0){
				String cancelInvoice = "";
				for(InpatientCancelitemNow modls:cancelitemList){
					applyCost = applyCost + (modls.getSalePrice()*modls.getQuantity());
					if(cancelInvoice!=""){
						cancelInvoice += "','";
					}
					cancelInvoice += modls.getBillNo();
				}
				map.put("resMsg", "success");
				map.put("name", cancelitemList.get(0).getName());
				map.put("medicalRecord", cancelitemList.get(0).getCardNo());
				map.put("applyCost", applyCost);
				map.put("payType", payType);
				map.put("invoice", cancelInvoice);
				map.put("resCode", cancelitemList);
			}else{
				map.put("resMsg", "error");
				map.put("resCode", "对不起，发票号不存在");
			}
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZTF_MATFQR", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZTF_MATFQR", "门诊退费_门诊退费确认", "1", "0"), e);
		}
	}
	
	
	/**  
	 * @Description：退费确认
	 * @Author：ldl
	 * @CreateDate：2016-06-28
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "refundSave")
	public void refundSave(){
		Map<String,String> map = new HashMap<String, String>();
		Map<String,String> maps = new HashMap<String, String>();
		boolean flag = true;
		//初始化发票号
		try{
			map = refundConfirmService.refundSaveNow(drugIds,null,invoiceNo,payType,applyCost,medicalRecord);
		}catch(Exception e){
			map.put("resMsg", "error");
			String message = e.getLocalizedMessage();
			message = message.substring(message.indexOf(":")+1,message.length());
			if(" INVOICE IS NOT ENOUGTH".equals(message)){
				map.put("resCode", "发票不足、请领取发票！");
			}else{
				map.put("resCode", "系统繁忙，请稍后重试...");
				logger.error("MZTF_MATFQR", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZTF_MATFQR", "门诊退费_门诊退费确认", "1", "0"), e);
			}
			flag = true;
			throw new RuntimeException(e);
		}finally{
			if(!flag){
				map.put("resMsg", "error");
				map.put("resCode", "系统繁忙，请稍后重试...");
			}
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		}
	}
}
