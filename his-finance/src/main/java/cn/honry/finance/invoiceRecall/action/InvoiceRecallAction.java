package cn.honry.finance.invoiceRecall.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.finance.invoiceRecall.service.InvoiceReacllService;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value="/finance/InvoiceRecall")
//@Namespace(value = "/finance")
@SuppressWarnings({"all"})
public class InvoiceRecallAction extends ActionSupport{
	
	private Logger logger=Logger.getLogger(InvoiceRecallAction.class);
	/**错误日志存储**/
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value = "invoiceReacllService" )
	private InvoiceReacllService invoiceReacllService;
	public void setInvoiceReacllService(InvoiceReacllService invoiceReacllService) {
		this.invoiceReacllService = invoiceReacllService;
	}
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
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	private String page;
	private String rows;
	private String date;
	private String num;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
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

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**  
	 *   发票召回界面
	 * @Author：tcj
	 * @CreateDate：2016-06-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"FPZH:function:view"})
	@Action(value="invoiceRecallList",results={@Result(name="list",location = "/WEB-INF/pages/finance/invoiceRecall/invoiceRecallList.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String invoiceRecallList()throws Exception{
		return "list";
	}
	/**  
	 *   查询发票领取记录
	 * @Author：tcj
	 * @CreateDate：2016-06-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value ="queryInvoiceRecall" )
	public void queryInvoiceRecall(){
		String name = ServletActionContext.getRequest().getParameter("name");
		List<FinanceInvoice> fil;
		try {
			fil = invoiceReacllService.queryInvoiceRecall(page,rows,name);
			int total =invoiceReacllService.getTotal(name);
			java.util.Map<String, Object> map=new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", fil);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("CWGL_FPZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_FPZH", "财务管理_发票召回", "2", "0"), e);
			}
	}
	/**  
	 *   查询员工Map
	 * @Author：tcj
	 * @CreateDate：2016-06-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value="queryEmpMapRecall")
	public void queryEmpMap(){
		List<SysEmployee> empl=inpatientInfoInInterService.queryEmpMapPublic();
		Map<String,String> map=new HashMap<String,String>();
		for(SysEmployee emp : empl){
			map.put(emp.getJobNo(), emp.getName());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**  
	 *   发票召回
	 * @Author：tcj
	 * @CreateDate：2016-06-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value="recallInvoice")
	public void recallInvoice(){
		String dates="["+date+"]";
		String result=invoiceReacllService.recallInvoice(dates,num);
		WebUtils.webSendString(result);
	}
}
