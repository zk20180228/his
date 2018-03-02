package cn.honry.finance.invoicereprint.action;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
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

import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.finance.invoicereprint.service.InvoiceReprintService;
import cn.honry.finance.invoicereprint.vo.InvoiceReprintVO;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;
/**
 * 
 * <p>发票补打</p>
 * @Author: yuke
 * @CreateDate: 2017年7月4日 上午10:14:45 
 * @Modifier: yuke
 * @ModifyDate: 2017年7月4日 上午10:14:45 
 * @ModifyRmk:  
 * @version: V1.0
 * @param:
 * @throws:
 * @return: 
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/finance/invoiceReprint")
public class InvoiceReprintAction {
	Logger logger = Logger.getLogger(InvoiceReprintAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	/**
	 * 门怎号
	 */
	private String clinicCode;
	/**
	 * 发票号
	 */
	private String invoiceNo;
	@Autowired
	@Qualifier(value="invoiceReprintService")
	private InvoiceReprintService invoiceReprintService;
	/** 
	* @Title: toView 
	* @Description: 转到发票重打/补打页面
	* @return
	* @return String    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年5月9日
	*/
	@Action(value = "toView", results = { @Result(name = "list", location = "/WEB-INF/pages/finance/invoiceReprint/invoicereprint.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toView(){
		return "list";
	}
	/** 
	* @Title: getInvoiceBycode 
	* @Description: 获取
	* @return void    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年5月9日
	*/
	@SuppressWarnings("unchecked")
	@Action(value="getInvoiceBycode")
	public void getInvoiceBycode(){
		List<InvoiceReprintVO> list;
		try {
			list = invoiceReprintService.getInvoiceBycode(clinicCode, invoiceNo);
			ComparatorChain chain = new ComparatorChain();
			chain.addComparator(new BeanComparator("invoiceNo"), false);
			Collections.sort(list,chain);
			WebUtils.webSendJSON(JSONUtils.toJson(list));
		} catch (Exception e) {
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MZFPCBD", "门诊收费_门诊发票重/补打", "3", "0"), e);
		}
	}
	/** 
	* @Title: getByinvoiceNo 
	* @Description: 根据发票号查询发票下的项目
	* @return void    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年5月10日
	*/
	@Action(value="getByinvoiceNo")
	public void getByinvoiceNo(){
		List<OutpatientFeedetailNow> list;
		try {
			list = invoiceReprintService.getfee(invoiceNo);
			WebUtils.webSendJSON(JSONUtils.toJson(list));
		} catch (Exception e) {
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MZFPCBD", "门诊收费_门诊发票重/补打", "3", "0"), e);
		}
	}
	/** 
	* @Title: reprint 
	* @Description: 补打
	* @return void    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年5月10日
	*/
	/***
	 * 因为业务逻辑存在问题，这里补打和重打使用相同的方法；
	 * 该方法是重打和补打都是更改数据库里的发票号，但是对于补打而言，之前的发票号没有使用，会造成发票的浪费
	 */
	@Action(value="reprint")
	public void reprint(){
		Map<String, String> map = new HashMap<String, String>();
		try{
			map = invoiceReprintService.reprint(invoiceNo);
		}catch(Exception e){
			map.put("resCode", "error");
			String message = e.getLocalizedMessage();
			message = message.substring(message.indexOf(":")+1,message.length());
			if(" INVOICE IS NOT ENOUGTH".equals(message)){
				map.put("resMsg", "发票不足、请领取发票！");
			}else{
				map.put("resMsg", "系统繁忙，请稍后重试...");
				logger.error("MZSF_MASF", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MZFPCBD", "门诊收费_门诊发票重/补打", "3", "0"), e);
			}
		}finally{
			WebUtils.webSendJSON(JSONUtils.toJson(map));
		}
	}
	
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
}
