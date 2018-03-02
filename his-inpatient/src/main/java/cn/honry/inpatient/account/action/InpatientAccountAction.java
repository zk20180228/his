package cn.honry.inpatient.account.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
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

import cn.honry.base.bean.model.InpatientAccount;
import cn.honry.base.bean.model.InpatientAccountdetail;
import cn.honry.base.bean.model.InpatientAccountrepaydetail;
import cn.honry.inpatient.account.service.InpatientAccountDetailService;
import cn.honry.inpatient.account.service.InpatientAccountRepayDetailService;
import cn.honry.inpatient.account.service.InpatientAccountService;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;

/**
 * ClassName: InpatientAccountAction 
 * @Description: 住院账号管理action
 * @author lt
 * @date 2015-6-30
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/inpatient/account")
@SuppressWarnings({"all"})
public class InpatientAccountAction extends ActionSupport{
	// 账户
	private InpatientAccount inpatientAccount;
	private InpatientAccountdetail detail;
	private InpatientAccountrepaydetail repaydetail;

	// 账户List
	private List<InpatientAccount> accountList;
	// 账户明细List
	private List<InpatientAccountdetail> detailList;
	// 账户明细List
	private List<InpatientAccountrepaydetail> repaydetailList;
	
	private String id;
	private HttpServletRequest request = ServletActionContext.getRequest();
	// 分页
	private String page;//起始页数
	private String rows;//数据列数
	
	@Autowired
	@Qualifier(value="inpatientAccountService")
	private InpatientAccountService inpatientAccountService;
	public void setInpatientAccountService(
			InpatientAccountService inpatientAccountService) {
		this.inpatientAccountService = inpatientAccountService;
	}

	@Autowired
	@Qualifier(value="inpatientAccountdetailService")
	private InpatientAccountDetailService inpatientAccountdetailService;
	
	public void setInpatientAccountdetailService(
			InpatientAccountDetailService inpatientAccountdetailService) {
		this.inpatientAccountdetailService = inpatientAccountdetailService;
	}

	@Autowired
	@Qualifier(value="inpatientAccountRepaydetailService")
	private InpatientAccountRepayDetailService inpatientAccountRepaydetailService;
	public void setInpatientAccountRepaydetailService(
			InpatientAccountRepayDetailService inpatientAccountRepaydetailService) {
		this.inpatientAccountRepaydetailService = inpatientAccountRepaydetailService;
	}

	/**
	 * @Description:住院账号管理主界面
	 * @Author：  lt
	 * @CreateDate： 2015-6-30
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"ZYZHGL:function:add"})
	@Action(value = "addInpatientAccount", results = { @Result(name = "add", location = "/WEB-INF/pages/inpatient/account/accountEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addInpatientAccount() {
		ServletActionContext.getRequest().setAttribute("account", new InpatientAccount());
		ServletActionContext.getRequest().setAttribute("detail", new InpatientAccountdetail());
		ServletActionContext.getRequest().setAttribute("repaydetail", new InpatientAccountrepaydetail());
		return "add";
	}
	
	/**
	 * @Description:添加账户
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @return
	 * @param @throws Exception   
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"ZYZHGL:function:save"})
	@Action(value = "editInpatientAccount", results = { @Result(name = "toAddInpatientAccount", type = "redirect", location = "addInpatientAccount.action") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editInpatientAccount() throws Exception {		
		inpatientAccount.setAccountState(1);
		inpatientAccount.setAccountBalance(0.0);
		inpatientAccountService.saveOrUpdate(inpatientAccount);
		return "toAddInpatientAccount";
	}

	/**
	 * @Description:收款、返还、补打操作
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @return
	 * @param @throws Exception   
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"ZYZHGL:function:receivables","ZYZHGL:function:return","ZYZHGL:function:make"},logical=Logical.OR)
	@Action(value = "editInpatientAccountDetail", results = { @Result(name = "toAddInpatientAccount", type = "redirect", location = "addInpatientAccount.action") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editInpatientAccountDetail() throws Exception {
		String repaydetailId = ServletActionContext.getRequest().getParameter("repaydetailId");
		inpatientAccount = inpatientAccountService.get(inpatientAccount.getId());
		InpatientAccountrepaydetail pRepaydetail = inpatientAccountRepaydetailService.get(repaydetailId);
		//创建操作记录实体 ,用来记录操作记录数据 更新PatientAccountdetail 表
		detail = new InpatientAccountdetail();
		
		repaydetail.setDetailIshis(0);
		//判断是返还操作还是 收款操作。  ==0是收款  !=0是收款 
		if(!"0".equals(repaydetailId)){
			if(repaydetail.getDetailOptype()==2){
				detail.setDetailAmount(-pRepaydetail.getDetailDebitamount());
				detail.setDetailOptype("返还");
				detail.setDetailVancancy(inpatientAccount.getAccountBalance()-pRepaydetail.getDetailDebitamount());
				
				repaydetail.setDetailRefid(repaydetailId);
				repaydetail.setDetailOptype(2);
				repaydetail.setDetailCreditamount(pRepaydetail.getDetailDebitamount());
				repaydetail.setDetailDebitamount(null);
				inpatientAccount.setAccountBalance(inpatientAccount.getAccountBalance()-repaydetail.getDetailCreditamount());
			}else if(repaydetail.getDetailOptype()==3){
				detail.setDetailAmount(pRepaydetail.getDetailDebitamount());
				detail.setDetailOptype("补打");
				detail.setDetailVancancy(0.0);
				
				repaydetail.setDetailBankunit(pRepaydetail.getDetailBankunit());
				repaydetail.setDetailBankbillno(pRepaydetail.getDetailBankbillno());
				repaydetail.setDetailPaytype(pRepaydetail.getDetailPaytype());
				repaydetail.setDetailBank(pRepaydetail.getDetailBank());
				repaydetail.setDetailBankaccount(pRepaydetail.getDetailBankaccount());
				repaydetail.setDetailRefid(repaydetailId);
				repaydetail.setDetailOptype(3);
				repaydetail.setDetailDebitamount(pRepaydetail.getDetailDebitamount());
			}
			
		}else{
			repaydetail.setDetailOptype(1);
			detail.setDetailAmount(repaydetail.getDetailDebitamount());
			detail.setDetailOptype("收款");
			detail.setDetailVancancy(inpatientAccount.getAccountBalance()+repaydetail.getDetailDebitamount());
			inpatientAccount.setAccountBalance(inpatientAccount.getAccountBalance()+repaydetail.getDetailDebitamount());
		}
		repaydetail.setInpatientAccount(inpatientAccount);
		detail.setInpatientAccount(inpatientAccount);
		inpatientAccountRepaydetailService.saveOrUpdate(repaydetail);
		inpatientAccountdetailService.saveOrUpdate(detail);//保存记录信息
		inpatientAccountService.saveOrUpdate(inpatientAccount);
		return "toAddInpatientAccount";
	}
	/**
	 * @Description:获取预存金或历史预存金列表
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryInpatientRepayDetail", results = { @Result(name = "json", type = "json") })
	public void queryInpatientRepayDetail() throws Exception {
		String accountId = ServletActionContext.getRequest().getParameter("accountId");
		Integer ishis = Integer.parseInt(ServletActionContext.getRequest().getParameter("ishis"));
		InpatientAccountrepaydetail repayDetailSearch = new InpatientAccountrepaydetail();
		repaydetailList = inpatientAccountRepaydetailService.getPage(page,rows,repayDetailSearch,accountId,ishis);
		int total = inpatientAccountRepaydetailService.getTotal(repayDetailSearch,accountId,ishis);
		//返还过的条目加背景色
		List<String> listStr = new ArrayList<String>(); 
		for (InpatientAccountrepaydetail repayDetail : repaydetailList) {
			if(repayDetail.getDetailRefid() != null && repayDetail.getDetailOptype() == 2){
				listStr.add(repayDetail.getDetailRefid());
			}
		}
		Gson gson = new GsonBuilder()
		.registerTypeAdapterFactory(HibernateCascade.FACTORY)
		.create();
		String json = gson.toJson(repaydetailList);
		String str = gson.toJson(listStr);
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write("{\"total\":" + total + ",\"rows\":" + json + ",\"str\":" + str + "}");
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	/**
	 * @Description:结清账户
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"ZYZHGL:function:clean"})
	@Action(value = "totalInpatientAccount", results = { @Result(name = "json", type = "json") })
	public void totalInpatientAccount() throws Exception {
		String medicalId = ServletActionContext.getRequest().getParameter("medicalId");
		if(StringUtils.isNotEmpty(medicalId)){
			inpatientAccount = inpatientAccountService.queryByMedical(medicalId);
		}else{
			String accountId = ServletActionContext.getRequest().getParameter("accountId");
			inpatientAccount = inpatientAccountService.get(accountId);
		}
		
		if(inpatientAccount!=null){
			repaydetail = new InpatientAccountrepaydetail();
			if(inpatientAccount.getAccountBalance()>0){
				repaydetail.setDetailOptype(4);
				repaydetail.setDetailCreditamount(inpatientAccount.getAccountBalance());
			}else{
				repaydetail.setDetailDebitamount(inpatientAccount.getAccountBalance());
				repaydetail.setDetailOptype(4);
			}
			repaydetail.setInpatientAccount(inpatientAccount);
			inpatientAccountRepaydetailService.saveOrUpdate(repaydetail);
			
			//创建操作记录实体 ,用来记录操作记录数据 更新PatientAccountdetail 表
			detail = new InpatientAccountdetail();
			detail.setDetailAmount(inpatientAccount.getAccountBalance());
			detail.setDetailOptype("结清账户");
			detail.setDetailVancancy(0.0);
			detail.setInpatientAccount(inpatientAccount);
			inpatientAccountdetailService.saveOrUpdate(detail);//保存记录信息
			
			inpatientAccount.setAccountBalance(0.0);
			inpatientAccountService.saveOrUpdate(inpatientAccount);
			//执行预存金变为历史预存金操作
			inpatientAccountRepaydetailService.updateIshis(inpatientAccount.getId());
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(inpatientAccount.getAccountBalance());
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	/**
	 * @Description: 验证账户是否已结清
	 * @Author：  lt
	 * @CreateDate： 2015-6-16
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	@Action(value = "checkInpatientAccount", results = { @Result(name = "json", type = "json") })
	public void checkInpatientAccount() throws Exception {
		String retVal = "no";
		String accountId = ServletActionContext.getRequest().getParameter("accountId");
		inpatientAccount = inpatientAccountService.get(accountId);
		if(inpatientAccount.getAccountBalance() != 0.0){
			retVal = "no";
		}else{
			retVal = "yes";
		}
		Gson gson = new Gson();
		String json = gson.toJson(retVal);
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	/**
	 * @Description:注销账户
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"ZYZHGL:function:cancellation"})
	@Action(value = "zhuxiaoInpatientAccount", results = { @Result(name = "json", type = "json") })
	public void zhuxiaoInpatientAccount() throws Exception {
		String retVal = "no";
		String accountId = ServletActionContext.getRequest().getParameter("accountId");
		inpatientAccount = inpatientAccountService.get(accountId);
		inpatientAccount.setDel_flg(1);
		inpatientAccountService.saveOrUpdate(inpatientAccount);
		retVal = "yes";
		
		//创建操作记录实体 ,用来记录操作记录数据 更新PatientAccountdetail 表
		detail = new InpatientAccountdetail();
		detail.setDetailAmount(0.0);
		detail.setDetailOptype("注销账户");
		detail.setDetailVancancy(inpatientAccount.getAccountBalance());
		detail.setInpatientAccount(inpatientAccount);
		inpatientAccountdetailService.saveOrUpdate(detail);//保存记录信息
		
		Gson gson = new Gson();
		String json = gson.toJson(retVal);
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	/**
	 * @Description: 执行账户停用,启用
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"ZYZHGL:function:disable","ZYZHGL:function:disable"},logical=Logical.OR)
	@Action(value = "stopFlgInpatientAccount", results = { @Result(name = "json", type = "json") })
	public void stopFlgInpatientAccount() throws Exception {
		String retVal = "no";
		String accountId = ServletActionContext.getRequest().getParameter("accountId");
		int stopFlg = Integer.parseInt(ServletActionContext.getRequest().getParameter("stopFlg"));
		inpatientAccount = inpatientAccountService.get(accountId);
		inpatientAccount.setStop_flg(stopFlg);
		inpatientAccountService.saveOrUpdate(inpatientAccount);
		retVal = "yes";
		
		//创建操作记录实体 ,用来记录操作记录数据 更新PatientAccountdetail 表
		detail = new InpatientAccountdetail();
		detail.setDetailAmount(0.0);
		String str = "";
		if(stopFlg == 1){
			str = "停用账户";
		}else if(stopFlg == 0){
			str = "启用账户";
		}
		detail.setDetailOptype(str);
		detail.setDetailVancancy(inpatientAccount.getAccountBalance());
		detail.setInpatientAccount(inpatientAccount);
		inpatientAccountdetailService.saveOrUpdate(detail);//保存记录信息
		
		Gson gson = new Gson();
		String json = gson.toJson(retVal);
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public InpatientAccount getInpatientAccount() {
		return inpatientAccount;
	}

	public void setInpatientAccount(InpatientAccount inpatientAccount) {
		this.inpatientAccount = inpatientAccount;
	}

	public InpatientAccountdetail getDetail() {
		return detail;
	}

	public void setDetail(InpatientAccountdetail detail) {
		this.detail = detail;
	}

	public InpatientAccountrepaydetail getRepaydetail() {
		return repaydetail;
	}

	public void setRepaydetail(InpatientAccountrepaydetail repaydetail) {
		this.repaydetail = repaydetail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<InpatientAccount> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<InpatientAccount> accountList) {
		this.accountList = accountList;
	}

	public List<InpatientAccountdetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<InpatientAccountdetail> detailList) {
		this.detailList = detailList;
	}

	public List<InpatientAccountrepaydetail> getRepaydetailList() {
		return repaydetailList;
	}

	public void setRepaydetailList(List<InpatientAccountrepaydetail> repaydetailList) {
		this.repaydetailList = repaydetailList;
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
	
}
