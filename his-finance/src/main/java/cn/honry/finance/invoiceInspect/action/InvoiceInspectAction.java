package cn.honry.finance.invoiceInspect.action;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.finance.invoiceInspect.service.InvoiceInspectService;
import cn.honry.finance.invoiceInspect.vo.InvoiceInspectVo;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * <p>发票核销</p>
 * @Author: yuke
 * @CreateDate: 2017年7月4日 上午10:50:23 
 * @Modifier: yuke
 * @ModifyDate: 2017年7月4日 上午10:50:23 
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
@Namespace(value = "/finance/invoiceInspect")
//@Namespace(value = "/finance")
@SuppressWarnings({"all"})
public class InvoiceInspectAction extends ActionSupport implements ModelDriven<FinanceInvoiceInfoNow>{
	private Logger logger=Logger.getLogger(InvoiceInspectAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	@Override
	public FinanceInvoiceInfoNow getModel() {
		return businessInvoiceInfo;
	}
	private FinanceInvoiceInfoNow businessInvoiceInfo;
	public FinanceInvoiceInfoNow getBusinessInvoiceInfo() {
		return businessInvoiceInfo;
	}
	public void setBusinessInvoiceInfo(FinanceInvoiceInfoNow businessInvoiceInfo) {
		this.businessInvoiceInfo = businessInvoiceInfo;
	}
	@Autowired
	@Qualifier(value = "invoiceInspectService")
	private InvoiceInspectService invoiceInspectService ;
	public void setInvoiceInspectService(InvoiceInspectService invoiceInspectService) {
		this.invoiceInspectService = invoiceInspectService;
	}
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	private Date beginTime;//开始时间
	private Date endTime;//结束时间
	private String balanceOpcd;//收费员
	private String encode;//节点ID
	private String page;
	private String rows;
	private String q;
	
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
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getBalanceOpcd() {
		return balanceOpcd;
	}
	public void setBalanceOpcd(String balanceOpcd) {
		this.balanceOpcd = balanceOpcd;
	}
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
	
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	/**  
	 * @Description：  发票核查页面
	 * @Author：tcj
	 * @CreateDate：2016-2-1  上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"FPHC:function:view"})
	@Action(value="invoiceInspectList",results={@Result(name="list",location = "/WEB-INF/pages/finance/invoiceInspect/invoiceInspectList.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String invoiceInspectList()throws Exception{
		return "list";
	}
	/**  
	 * @Description：  查询发票类型树
	 * @Author：tcj
	 * @CreateDate：2016-2-1  上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryTreeList", results = { @Result(name = "json", type = "json") })
	public void queryTreeList(){
		try {
		List<TreeJson> invoiceTypeTree =  invoiceInspectService.queryInvoiceType(null);
		String json = JSONUtils.toJson(invoiceTypeTree);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception ex) {
			logger.error("CWGL_FPHX", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_FPHX", "发票管理_发票核销", "2", "0"), ex);
		}
	}
	/**  
	 * @Description：  查询待审核列表
	 * @Author：tcj
	 * @CreateDate：2016-2-1  上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"FPHC:function:query"})
	@Action(value = "queryInvoiceInfoList", results = { @Result(name = "json", type = "json") })
	public void queryInvoiceInfoList(){
		List<InvoiceInspectVo> bii;
		try {
			bii = invoiceInspectService.queryInvoiceInfoList(beginTime, endTime, balanceOpcd, encode,page,rows);
			int total =invoiceInspectService.getTotal(beginTime, endTime, balanceOpcd, encode);
			java.util.Map<String, Object> map=new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", bii);
			String json=JSONUtils.toJson(map,"yyy-MM-dd");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("CWGL_FPHX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_FPHX", "发票管理_发票核销", "2", "0"), e);
		}
	}
	/**  
	 * @Description：  保存发票核查列表
	 * @Author：tcj
	 * @CreateDate：2016-2-1  上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"FPHC:function:save"})
	@Action(value = "saveDatagridInfo", results = { @Result(name = "json", type = "json") })
	public void saveDatagrid(){
		try {
			String rowdate=ServletActionContext.getRequest().getParameter("rowdate");
			String intype=ServletActionContext.getRequest().getParameter("intype");
			invoiceInspectService.saveDatagrid(rowdate,intype);
		} catch (Exception e) {
			logger.error("CWGL_FPHX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_FPHX", "发票管理_发票核销", "2", "0"), e);
		}
	}
	/**  
	 * @Description：  查询收费员下拉框
	 * @Author：tcj
	 * @CreateDate：2016-2-1  上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryBalanceOpcd", results = { @Result(name = "json", type = "json") })
	public void queryBalanceOpcd(){
		List<SysEmployee> empList;
		try {
			empList = invoiceInspectService.queryBalanceOpcd(q);
			String json=JSONUtils.toExposeJson(empList, false, null, "jobNo","name");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("CWGL_FPHX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_FPHX", "发票管理_发票核销", "2", "0"), e);
		}
	}
}
