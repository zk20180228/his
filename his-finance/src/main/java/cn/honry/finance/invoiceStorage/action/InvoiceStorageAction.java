package cn.honry.finance.invoiceStorage.action;

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
import cn.honry.base.bean.model.FinanceInvoiceStorage;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.finance.invoiceStorage.service.InvoiceStorageService;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 
 * <p>发票入库管理 </p>
 * @Author: yuke
 * @CreateDate: 2017年7月4日 上午9:57:37 
 * @Modifier: yuke
 * @ModifyDate: 2017年7月4日 上午9:57:37 
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
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/finance/invoiceStorage")
//@Namespace(value = "/finance")
@SuppressWarnings({"all"})
public class InvoiceStorageAction extends ActionSupport{
	private Logger logger=Logger.getLogger(InvoiceStorageAction.class);
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	@Autowired
	@Qualifier(value = "invoiceStorageService")
	private InvoiceStorageService invoiceStorageService;
	
	private FinanceInvoiceStorage financeInvoiceStorage;
	private HttpServletRequest request = ServletActionContext.getRequest();
	/**
	 * 注入inpatientInfoInInterService公共接口
	 */
	private InpatientInfoInInterService inpatientInfoInInterService;
	@Autowired
	@Qualifier(value = "inpatientInfoInInterService")
	public void setInpatientInfoInInterService(
			InpatientInfoInInterService inpatientInfoInInterService) {
		this.inpatientInfoInInterService = inpatientInfoInInterService;
	}
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	
	public FinanceInvoiceStorage getFinanceInvoiceStorage() {
		return financeInvoiceStorage;
	}
	public void setFinanceInvoiceStorage(FinanceInvoiceStorage financeInvoiceStorage) {
		this.financeInvoiceStorage = financeInvoiceStorage;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public void setInvoiceStorageService(InvoiceStorageService invoiceStorageService) {
		this.invoiceStorageService = invoiceStorageService;
	}
	/**
	 * 分页查询所用到的page和rows
	 */
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
	/**  
	 *   发票入库界面
	 * @Author：tcj
	 * @CreateDate：2016-06-28
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"FPRKGL:function:view"})
	@Action(value="invoiceStorageList",results={@Result(name="list",location = "/WEB-INF/pages/finance/invoiceStorage/invoiceStorageList.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String invoiceStorageList()throws Exception{
		return "list";
	}
	/**  
	 *   保存（修改）发票入库记录
	 * @Author：tcj
	 * @CreateDate：2016-6-28
	 * @version 1.0
	 */
	@Action(value = "saveform")
	public void saveform(){
		try {
			String invoiceStartnos = request.getParameter("invoiceStartnos");
			financeInvoiceStorage.setInvoiceStartno(invoiceStartnos+financeInvoiceStorage.getInvoiceStartno());
			financeInvoiceStorage.setInvoiceEndno(invoiceStartnos+financeInvoiceStorage.getInvoiceEndno());
			String result;
			result = invoiceStorageService.saveform(financeInvoiceStorage);
			WebUtils.webSendString(result);
		} catch (Exception e) {
			logger.error("CWGL_FPRKGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_FPRKGL", "发票管理_发票入库管理", "2", "0"), e);
		}
	}
	/**  
	 *   查询发票入库记录
	 * @Author：tcj
	 * @CreateDate：2016-6-28
	 * @version 1.0
	 */
	@Action(value = "queryInvoiceStorage")
	public void queryInvoiceStorage(){
		FinanceInvoiceStorage fis=new FinanceInvoiceStorage();
		String invoiceType = request.getParameter("invoiceType");
		String invoiceEndno = request.getParameter("invoiceEndno");
		String invoiceStartno = request.getParameter("invoiceStartno");
		String invoiceUseState = request.getParameter("invoiceUseState");
		String invoiceStartnos = request.getParameter("invoiceStartnos");
		if (StringUtils.isNotBlank(invoiceType)) {
			fis.setInvoiceType(invoiceType);
		}
		if(StringUtils.isBlank(invoiceType)){
			if (StringUtils.isNotBlank(invoiceEndno)) {
				fis.setInvoiceEndno(invoiceEndno);
			}
			if (StringUtils.isNotBlank(invoiceStartno)) {
				fis.setInvoiceStartno(invoiceStartno);
			}
		}else{
			if (StringUtils.isNotBlank(invoiceEndno)) {
				fis.setInvoiceEndno(invoiceType+invoiceEndno);
			}
			if (StringUtils.isNotBlank(invoiceStartno)) {
				fis.setInvoiceStartno(invoiceType+invoiceStartno);
			}
		}
		if (StringUtils.isNotBlank(invoiceUseState)) {
			fis.setInvoiceUseState(Integer.parseInt(invoiceUseState));
		}
		List<FinanceInvoiceStorage> fisl;
		try {
			fisl = invoiceStorageService.queryInvoiceStorage(page,rows,fis);
			int total=invoiceStorageService.getTotal(fis);
			java.util.Map<String, Object> map=new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", fisl);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("CWGL_FPRKGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_FPRKGL", "发票管理_发票入库管理", "2", "0"), e);
		}
	}
	/**
	 * 查询发票类型List（code，name）
	 * @author tcj
	 * @version 1.0
	 */
	@Action(value="queryInvoiceType")
	public void queryInvoiceType(){
		try {
			String type = "invoiceType";
			String encode=null;
			List<BusinessDictionary> bcl;
			bcl = invoiceStorageService.queryInvoiceType(type, encode);
			String json=JSONUtils.toExposeJson(bcl, false, null, "encode","name","pinyin","wb","inputCode");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("CWGL_FPRKGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_FPRKGL", "发票管理_发票入库管理", "2", "0"), e);
		}
	}
	/**  
	 *   查询发票类型Map
	 * @Author：tcj
	 * @CreateDate：2016-6-28
	 * @version 1.0
	 */
	@Action(value = "queryInvoiceTypeAJAX")
	public void queryInvoiceTypeAJAX(){
		String type="invoiceType";
		String encode=null;
		List<BusinessDictionary> invoiceTypel =inpatientInfoInInterService.queryDictionaryListForcomboboxPublic(type, encode);
		Map<String,String> map=new HashMap<String,String>();
		for(BusinessDictionary bd:invoiceTypel){
			map.put(bd.getEncode(), bd.getName());
		}
		String json= JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
}
