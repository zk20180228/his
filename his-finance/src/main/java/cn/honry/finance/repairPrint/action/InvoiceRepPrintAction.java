package cn.honry.finance.repairPrint.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

import cn.honry.base.bean.model.Hospital;
import cn.honry.base.bean.model.InpatientBalanceList;
import cn.honry.base.bean.model.InpatientBalanceListNow;
import cn.honry.finance.repairPrint.service.InvoiceRepPrintService;
import cn.honry.finance.repairPrint.vo.InvoiceRepPrintVo;
import cn.honry.report.service.IReportService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**  
 *  
 * @className：InvoiceRepPrintAction
 * @Description：  发票补打
 * @Author：yeguanqun
 * @CreateDate：2016-4-18 上午11:56:31  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/finance/invoiceRepPrint")
//@Namespace(value = "/finance")
public class InvoiceRepPrintAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier(value = "invoiceRepPrintService")
	private InvoiceRepPrintService invoiceRepPrintService;	
    public void setInvoiceRepPrintService(InvoiceRepPrintService invoiceRepPrintService) {
		this.invoiceRepPrintService = invoiceRepPrintService;
	}
    /**报表打印**/
    @Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
    private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
    
    private InvoiceRepPrintVo invoiceRepPrintVo;
    private InpatientBalanceList inpatientBalanceList;
    private String invoiceNo;
	
	public InvoiceRepPrintVo getInvoiceRepPrintVo() {
		return invoiceRepPrintVo;
	}
	public void setInvoiceRepPrintVo(InvoiceRepPrintVo invoiceRepPrintVo) {
		this.invoiceRepPrintVo = invoiceRepPrintVo;
	}
	public InpatientBalanceList getInpatientBalanceList() {
		return inpatientBalanceList;
	}
	public void setInpatientBalanceList(InpatientBalanceList inpatientBalanceList) {
		this.inpatientBalanceList = inpatientBalanceList;
	}
	
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	/**  
	 *  
	 * @Description：  跳转发票补打页面
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-18  上午11:56:59  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"FPBD:function:view"})
	@Action(value = "getInvoiceRepPrint", results = { @Result(name = "list", location = "/WEB-INF/pages/finance/repairPrint/invoiceRepPrint.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String getInvoiceRepPrint() {
		return "list";
	}
	
	/**
	 * @Description:获取结算信息
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-4-22
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"FPBD:function:view"})
	@Action(value="queryInvoiceInfo")
	public void queryInvoiceInfo(){
		if(invoiceRepPrintVo==null){
			invoiceRepPrintVo = new InvoiceRepPrintVo();
		}
		List<InvoiceRepPrintVo> invoiceInfoList = invoiceRepPrintService.queryInvoiceInfo(invoiceRepPrintVo);
		String json = JSONUtils.toJson(invoiceInfoList);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * @Description:返查询住院结算明细表的数据
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-4-22
	 * @return String  
	 * @version 1.0
	**/
	@Action(value="queryBalanceList")
	public void queryBalanceList(){
		if(inpatientBalanceList==null){
			inpatientBalanceList = new InpatientBalanceList();
		}
		List<InpatientBalanceListNow> balanceList = invoiceRepPrintService.queryBalanceList(inpatientBalanceList);
		String json = JSONUtils.toJson(balanceList);
		WebUtils.webSendJSON(json);
	}
	/**
	 * @author conglin
	 * @Description: 发票补打
	 */
	@Action(value="printInvoiceInfo")
	public void printInvoiceInfo(){
		if(invoiceRepPrintVo==null){
			invoiceRepPrintVo = new InvoiceRepPrintVo();
		}
		List<InvoiceRepPrintVo> invoiceInfoList = invoiceRepPrintService.queryInvoiceInfo(invoiceRepPrintVo);
		if(inpatientBalanceList==null){
			inpatientBalanceList = new InpatientBalanceList();
		}
		inpatientBalanceList.setInvoiceNo(invoiceInfoList.get(0).getInvoiceNo());
		inpatientBalanceList.setBalanceNo(invoiceInfoList.get(0).getBalanceNo());
		List<InpatientBalanceListNow> balanceList = invoiceRepPrintService.queryBalanceList(inpatientBalanceList);
		invoiceInfoList.get(0).setInpatientBalanceListNow(balanceList);
		
		HttpServletRequest request=ServletActionContext.getRequest();
		String root_path = request.getSession().getServletContext().getRealPath("/");
		 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/VdetailedDaily.jasper";
		 HashMap<String, Object> parameters = new HashMap<String, Object>();
		 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
		 parameters.put("hospitalName",HisParameters.PREFIXFILENAME);
		 try {
			iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(invoiceInfoList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
