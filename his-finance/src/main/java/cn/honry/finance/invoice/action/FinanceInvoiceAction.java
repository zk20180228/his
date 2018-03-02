package cn.honry.finance.invoice.action;

import java.io.PrintWriter;
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

import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.FinanceInvoiceStorage;
import cn.honry.base.bean.model.FinanceUsergroup;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.finance.invoice.service.FinanceInvoiceService;
import cn.honry.finance.invoiceStorage.service.InvoiceStorageService;
import cn.honry.finance.userGroup.service.UserGroupService;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })

@Namespace(value = "/finance/financeInvoice")
public class FinanceInvoiceAction extends ActionSupport implements ModelDriven<FinanceInvoice>{

	/** 
	* @Fields serialVersionUID
	*/ 
	private static final long serialVersionUID = 7660894628888276417L;
	/**
	 * 发票终止号
	 */
	private String endNum;
	
	public String getEndNum() {
		return endNum;
	}
	public void setEndNum(String endNum) {
		this.endNum = endNum;
	}
	@Override
	public FinanceInvoice getModel() {
		return financeInvoice;	
	}
	@Autowired
	@Qualifier(value = "invoiceStorageService")
	private InvoiceStorageService invoiceStorageService;
	public void setInvoiceStorageService(InvoiceStorageService invoiceStorageService) {
		this.invoiceStorageService = invoiceStorageService;
	}
	@Autowired
	@Qualifier(value = "financeInvoiceService")
	private FinanceInvoiceService financeInvoiceService;
	public void setFinanceInvoiceService(FinanceInvoiceService financeInvoiceService) {
		this.financeInvoiceService = financeInvoiceService;
	}
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	@Autowired
	@Qualifier(value = "userGroupService")
	private UserGroupService userGroupService;
	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}
	private FinanceInvoice financeInvoice = new FinanceInvoice();
	
	private FinanceUsergroup financeUsergroup;
	
	public FinanceUsergroup getFinanceUsergroup() {
		return financeUsergroup;
	}

	public void setFinanceUsergroup(FinanceUsergroup financeUsergroup) {
		this.financeUsergroup = financeUsergroup;
	}
	/**
	 * 员工实体类
	 */
	private SysEmployee sysEmployee;
	
	public SysEmployee getSysEmployee() {
		return sysEmployee;
	}
	public void setSysEmployee(SysEmployee sysEmployee) {
		this.sysEmployee = sysEmployee;
	}
	

	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private Logger logger=Logger.getLogger(FinanceInvoiceAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	
	/**
	 *  查询所有符合条件的数据
	 * @author sgt
	 * @date 2015-06-15
	 * @version 1.0
	 * @param Manufacturer
	 * @return
	 */
	@RequiresPermissions(value={"FPLQ:function:view"})
	@Action(value = "queryFinanceInvoiceUrl", results = { @Result(name = "queryFinanceInvoiceUrl", location = "/WEB-INF/pages/finance/invoice/invoiceList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryFinanceInvoiceUrl(){
		return "queryFinanceInvoiceUrl";
	}
	@Action(value = "queryFinanceInvoice")
	public void queryFinanceInvoice(){
		try {
			String flag=request.getParameter("flag");
			if(("1").equals(request.getParameter("flag"))){
				String invoiceType=request.getParameter("invoiceType");
				if(invoiceType!=null &&invoiceType!=""){
					financeInvoice.setInvoiceType(invoiceType);
				}
			}
			List<FinanceInvoice> list = financeInvoiceService.queryFinanceInvoice(financeInvoice);
			int total = financeInvoiceService.getFinanceInvoiceCount(financeInvoice);
			Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
			Map map = new HashMap();
			map.put("total", total);
			map.put("rows", list);
			String json = gson.toJson(map);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("FPLQ_CX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FPLQ_CX", "发票领取_查询", "2", "0"), e);
		}
	}
	/**
	 *  增加数据
	 * @author sgt
	 * @date 2015-06-15
	 * @version 1.0
	 * @param Manufacturer
	 * @return
	 */
	@Action(value = "saveFinanceInvoice")
	@RequiresPermissions(value={"FPLQ:function:save"})
	public void saveFinanceInvoice(){
		int flag = 0;//正常1；并发或其他异常0
		String num = request.getParameter("num");
		Map<String, Object> map = new HashMap<>();
		String resCode="error";
		String resMes="领用失败,请重新领取!";
		try {
			int maxStartNo=financeInvoiceService.sumfinace(num,financeInvoice);
			if(maxStartNo>0){
				flag = 1;
				resCode="error";
				resMes="领用失败,您多领了"+maxStartNo+"张发票";
			}else{
				financeInvoiceService.saveFinanceInvoice(financeInvoice, num);
				flag = 1;
				resCode="success";
				resMes="领用成功!";	
			}
		} catch (Exception e) {
			resCode="error";
			resMes="领用失败,请重新领取!";
			logger.error("FPLQ_ZJSJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FPLQ_ZJSJ", "发票领取_增加数据", "2", "0"), e);
		}finally{
			if (flag == 0) {
				resCode="error";
				resMes="领用失败,请重新领取!";
			}
			map.put("resCode", resCode);
			map.put("resMes", resMes); 
			String joString = JSONUtils.toJson(map);
			WebUtils.webSendJSON(joString);
		}
	}
	/**
	 *  人员数据表格
	 * @author sgt
	 * @date 2015-06-16
	 * @version 1.0
	 * @param Manufacturer
	 * @return
	 */
	@Action(value = "employeeTable")
	public void employeeTable(){
		try {
			String name = "";
			if(sysEmployee!=null){
				name = java.net.URLDecoder.decode(sysEmployee.getName(), "UTF-8");
				sysEmployee.setName(name);
			}				
			List<SysEmployee> employeeList = financeInvoiceService.getAllEmp(sysEmployee);
			String json = JSONUtils.toExposeJson(employeeList, true, null, "jobNo", "name");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("FPLQ_RYBG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FPLQ_RYBG", "发票领取_人员数据表格", "2", "0"), e);
		}
	}
	/**  
	 * @Description：  根据领取人和发票种类，查出领取的所有发票号
	 * @Author：kjh
	 * @CreateDate：2015-6-24
	 * @param invoiceGetperson:领取人  invoiceType：发票种类
	 * @version 1.0
	 */
	@Action(value = "findByGetPerson")
	public void findByGetPerson(){
		try {
			String invoiceGetperson=request.getParameter("invoiceGetperson");
			String invoiceType=request.getParameter("invoiceType");
			List<FinanceInvoice> list=financeInvoiceService.findByGetPerson(invoiceGetperson, invoiceType);
			Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
			String json = gson.toJson(list);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("FPLQ_CXFPH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FPLQ_CXFPH", "发票领取_查询发票号", "2", "0"), e);
		}
	}	
	/**  
	 * @Description：  查询数据库中同类型的发票的最大号+1
	 * @Author：kjh
	 * @CreateDate：2015-6-24
	 * @param   invoiceType：发票种类
	 * @version 1.0
	 */
	@Action(value = "findMaxStartNo")
	public void findMaxStartNo(){
		try {
			String invoiceType=request.getParameter("invoiceType");
			List<FinanceInvoiceStorage> maxStartNo=financeInvoiceService.findMaxStartNo(invoiceType);
			if(maxStartNo!=null&&maxStartNo.size()>0){
				String json = JSONUtils.toJson(maxStartNo.get(0));
				WebUtils.webSendJSON(json);
			}else{
			String json = JSONUtils.toJson(maxStartNo);
			WebUtils.webSendJSON(json);
			}
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("FPLQ_CXZDH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FPLQ_CXZDH", "发票领取_查询最大发票号", "2", "0"), e);
		}
	}
	/**  
	 * @Description：  查询数据库中同类型的发票的最大号+1
	 * @Author：kjh
	 * @CreateDate：2015-6-24
	 * @param   invoiceType：发票种类
	 * @version 1.0
	 */
	@Action(value = "sumfinance")
	public void sumfinance(){
		try {
			String num=request.getParameter("num");
			if(StringUtils.isBlank(num)){
				num="0";
			}
			String invoiceType=request.getParameter("invoiceType"); 
			String maxStartNo=financeInvoiceService.sumfinance(num,invoiceType);
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("stateNo", maxStartNo);
			String menuJson = JSONUtils.toJson(map);
			WebUtils.webSendJSON(menuJson);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("FPLQ_CXZDH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FPLQ_CXZDH", "发票领取_查询最大发票号", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：  查询数据
	 * @Author：kjh
	 * @CreateDate：2015-6-12  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryUserGroup")
	public void queryUserGroup(){
		try {
			String groupName = "";
			if(financeUsergroup!=null){
				groupName = java.net.URLDecoder.decode(financeUsergroup.getGroupName(), "UTF-8");
				financeUsergroup.setGroupName(groupName);
			}	
			List<FinanceUsergroup> list = userGroupService.queryUsergroup(financeUsergroup);
			int total = userGroupService.getUsergroupCount(financeUsergroup);
			Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
			Map map = new HashMap();
			map.put("total", total);
			map.put("rows", list);
			String json = gson.toJson(map);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("FPLQ_CXSJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FPLQ_CXSJ", "发票领取_查询数据", "2", "0"), e);
		}

	}
	/** 根据发票号获取发票数量
	 * @Description 
	 * @author  marongbin
	 * @createDate： 2017年4月6日 下午3:55:01 
	 * @modifier 
	 * @modifyDate：: void
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getNum")
	public void getNum(){
		String invoiceType=request.getParameter("invoiceType"); 
		String num = financeInvoiceService.getNum(invoiceType, endNum);
		String menuJson = JSONUtils.toJson(num);
		WebUtils.webSendJSON(menuJson);
	}
}
