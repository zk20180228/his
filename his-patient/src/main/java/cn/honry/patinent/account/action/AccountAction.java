package cn.honry.patinent.account.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.PatientAccount;
import cn.honry.base.bean.model.PatientAccountdetail;
import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.patinent.account.service.AccountDetailService;
import cn.honry.patinent.account.service.AccountService;
import cn.honry.patinent.account.service.AccountrepayDetailService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**  
 *  
 * @className：AccountAction 
 * @Description：账户管理  action
 * @Author：lt
 * @CreateDate：2015-6-5 上午09:13:49  
 * @Modifier：lt
 * @ModifyDate：2015-6-5 上午09:13:49  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/patient/account")
public class AccountAction extends ActionSupport{
	private static final long serialVersionUID = 1L;

	private AccountService accountService;
	
	public AccountService getAccountService() {
		return accountService;
	}
	@Autowired
	@Qualifier(value = "accountService")
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	
	private AccountDetailService detailService;
	public AccountDetailService getDetailService() {
		return detailService;
	}
	@Autowired
	@Qualifier(value = "detailService")
	public void setDetailService(AccountDetailService detailService) {
		this.detailService = detailService;
	}

	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService codeInInterService;
	public void setCodeInInterService(CodeInInterService codeInInterService) {
		this.codeInInterService = codeInInterService;
	}
	
	private AccountrepayDetailService repayDetailService;
	
	public AccountrepayDetailService getRepayDetailService() {
		return repayDetailService;
	}
	@Autowired
	@Qualifier(value = "repayDetailService")
	public void setRepayDetailService(AccountrepayDetailService repayDetailService) {
		this.repayDetailService = repayDetailService;
	}


	// 分页
	private String page;//起始页数
	private String rows;//数据列数
	// 账户
	private PatientAccount account;
	private PatientAccountdetail detail;
	private PatientAccountrepaydetail repaydetail;

	public PatientAccountrepaydetail getRepaydetail() {
		return repaydetail;
	}
	public void setRepaydetail(PatientAccountrepaydetail repaydetail) {
		this.repaydetail = repaydetail;
	}


	// 账户List
	private List<PatientAccount> accountList;
	// 账户明细List
	private List<PatientAccountdetail> detailList;
	// 账户明细List
	private List<PatientAccountrepaydetail> repayDetailList;
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	//账户分类
	private int detailAccounttype;
	
	public int getDetailAccounttype() {
		return detailAccounttype;
	}
	public void setDetailAccounttype(int detailAccounttype) {
		this.detailAccounttype = detailAccounttype;
	}


	private String id;
	/**
	 * @Description: 账户信息列表
	 * @Author：  lt
	 * @CreateDate：2015-6-5
	 * @Modifier：lt
	 * @ModifyDate：2015-6-5
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	@Action(value = "listAccount", results = { @Result(name = "list", location = "/WEB-INF/pages/patient/account/accountList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listAccount() {
		return "list";
	}
	
	/**
	 * @Description: 查询账户信息
	 * @Author：  lt
	 * @CreateDate：2015-6-5
	 * @Modifier：lt
	 * @ModifyDate：2015-6-5
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	@Action(value = "queryAccount", results = { @Result(name = "json", type = "json") })
	public void queryAccount() {
		//String id = ServletActionContext.getRequest().getParameter("id");
		String accountName = ServletActionContext.getRequest().getParameter("accountName");
		String accountType = ServletActionContext.getRequest().getParameter("accountType");
		PatientAccount accountSearch = new PatientAccount();
		if(StringUtils.isNotBlank(accountName)){
			accountSearch.setAccountName(accountName);
		}
		if(StringUtils.isNotBlank(accountType)){
			accountSearch.setAccountType(accountType);
		}
		accountList = accountService.getPage(page,rows,accountSearch);
		int total = accountService.getTotal(accountSearch);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", accountList);
		String menuJson = JSONUtils.toJson(map);
		WebUtils.webSendJSON(menuJson);
	}
	/**
	 * @Description: 账户信息添加
	 * @Author：  lt
	 * @CreateDate： 2015-6-5
	 * @Modifier：lt
	 * @ModifyDate：2015-6-5
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"HZZH:function:view"})
	@Action(value = "addAccount", results = { @Result(name = "add", location = "/WEB-INF/pages/patient/account/accountEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addAccount() {
		ServletActionContext.getRequest().setAttribute("account", new PatientAccount());
		ServletActionContext.getRequest().setAttribute("detail", new PatientAccountdetail());
		ServletActionContext.getRequest().setAttribute("repaydetail", new PatientAccountrepaydetail());
		return "add";
	}
	
	/**
	 * @Description: 账户信息修改
	 * @Author：  lt
	 * @CreateDate： 2015-6-5
	 * @Modifier：lt
	 * @ModifyDate：2015-6-5
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	@Action(value = "editInfoAccount", results = { @Result(name = "edit", location = "/WEB-INF/pages/patient/account/accountEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editInfoAccount() {
		account = accountService.get(id);
		ServletActionContext.getRequest().setAttribute("account", account);
		return "edit";
	}
	/**
	 * @Description: 账户信息浏览
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	@Action(value = "viewAccount", results = { @Result(name = "view", location = "/WEB-INF/pages/patient/account/accountView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewAccount() {
		account = accountService.get(id);
		ServletActionContext.getRequest().setAttribute("account", account);
		return "view";
	}
	
	/**
	 * @Description: 账户信息添加&修改
	 * @Author：  lt
	 * @CreateDate： 2015-6-5
	 * @Modifier：lt
	 * @ModifyDate：2015-6-5
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"HZZH:function:save"})
	@Action(value = "editAccount", results = { @Result(name = "toAddAccount", type = "redirect", location = "addAccount.action") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editAccount() throws Exception {
		//TODO 迁移遗留问题 ICd
		/*PatientIdcard idcard = idcardService.queryByIdcardNo(account.getIdcard().getIdcardNo());
		account.setIdcard(idcard);
		account.setAccountPassword(DigestUtils.md5Hex(account.getAccountPassword()));
		account.setAccountState(1);
		account.setAccountBalance(0.0);
		account.setClinicBalance(0d);
		account.setInpatientBalance(0d);
		if(account.getAccountDaylimit()==null){
			account.setAccountDaylimit(0d);
		}
		accountService.saveOrUpdate(account)*/;
		return "toAddAccount";
	}
	/**
	 * @Description: 账户信息删除
	 * @Author：  lt
	 * @CreateDate： 2015-6-5
	 * @Modifier：lt
	 * @ModifyDate：2015-6-5
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	@Action(value = "delAccount", results = { @Result(name = "list", location = "/WEB-INF/pages/patient/account/accountList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delAccount() throws Exception {
		accountService.del(id);
		return "list";
	}
	/**
	 * @Description: 账户密码验证
	 * @Author：  lt
	 * @CreateDate： 2015-6-6
	 * @Modifier：lt
	 * @ModifyDate：2015-6-6
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	@Action(value = "updatePwdAccount", results = { @Result(name = "json", type = "json") })
	public void updatePwdAccount(){
		String retVal = "no";
		String accountId = ServletActionContext.getRequest().getParameter("accountId");
		String oldPwd = ServletActionContext.getRequest().getParameter("oldPwd");
		account = accountService.get(accountId);
		if(account.getAccountPassword()!=null && account.getAccountPassword().equals(DigestUtils.md5Hex(oldPwd))){
			retVal="yes";
		}else{
			retVal="no";
		}
		String menuJson = JSONUtils.toJson(retVal);
		WebUtils.webSendJSON(menuJson);
	}
	
	/**
	 * @Description: 账户密码修改
	 * @Author：  lt
	 * @CreateDate： 2015-6-6
	 * @Modifier：lt
	 * @ModifyDate：2015-6-6
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"HZZH:function:edit"})
	@Action(value = "savePwdAccount", results = { @Result(name = "toAddAccount", type = "redirect", location = "addAccount.action?menuAlias=HZZH&detailAccounttype=1") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String savePwdAccount() throws Exception {
		String pwd = DigestUtils.md5Hex(account.getAccountPassword());
		account = accountService.get(account.getId());
		account.setAccountPassword(pwd);
		accountService.saveOrUpdate(account);
		
		//创建操作记录实体 ,用来记录操作记录数据 更新PatientAccountdetail 表
		detail = new PatientAccountdetail();
		detail.setDetailAmount(0.0);
		detail.setDetailOptype("修改密码");
		detail.setDetailVancancy(account.getAccountBalance());
		detail.setAccount(account);
		detailService.saveOrUpdate(detail);//保存记录信息
		return "toAddAccount";
	}
	/**
	 * @Description:账户及明细信息添加&修改
	 * @Author：  lt
	 * @CreateDate： 2015-6-6
	 * @Modifier：lt
	 * @ModifyDate：2015-6-6
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"HZZH:function:receivables","HZZH:function:return","HZZH:function:make" },logical=Logical.OR)
	@Action(value = "editAccountDetail", results = { @Result(name = "json", type = "json")})
	public void editAccountDetail() throws Exception {
		String result = "no";
		String repaydetailId = ServletActionContext.getRequest().getParameter("repaydetailId");
		String type = ServletActionContext.getRequest().getParameter("type");
		account = accountService.queryById(account.getId());
		PatientAccountrepaydetail pRepaydetail = repayDetailService.get(repaydetailId);
		//创建操作记录实体 ,用来记录操作记录数据 更新PatientAccountdetail 表
		detail = new PatientAccountdetail();
		
		repaydetail.setDetailIshis(0);
		//判断是返还操作还是 收款操作。  ==0是收款  !=0是返还
		if(!"0".equals(repaydetailId)){
			if(repaydetail.getDetailOptype()==2){
				detail.setDetailAmount(-pRepaydetail.getDetailDebitamount());
				detail.setDetailOptype("返还");
				if(detailAccounttype==1){
					detail.setDetailVancancy(account.getClinicBalance()-pRepaydetail.getDetailDebitamount());
				}else{
					detail.setDetailVancancy(account.getInpatientBalance()-pRepaydetail.getDetailDebitamount());
				}
				repaydetail.setDetailAccounttype(detailAccounttype);
				repaydetail.setDetailRefid(repaydetailId);
				repaydetail.setDetailOptype(2);
				repaydetail.setDetailCreditamount(pRepaydetail.getDetailDebitamount());
				repaydetail.setDetailDebitamount(null);
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
			if(detailAccounttype==1){
				detail.setDetailVancancy(account.getClinicBalance()+repaydetail.getDetailDebitamount());
			}else{
				detail.setDetailVancancy(account.getInpatientBalance()+repaydetail.getDetailDebitamount());
			}
			//account.setAccountBalance(account.getAccountBalance()+repaydetail.getDetailDebitamount());
		}
		repaydetail.setAccount(account);
		detail.setAccount(account);
		repaydetail.setDetailAccounttype(detailAccounttype);
		detail.setDetailAccounttype(detailAccounttype);
		boolean flag = repayDetailService.saveOrUpdate(repaydetail,detail,type);
		if(flag){
			result = "yes";
		}
		PrintWriter out = WebUtils.getResponse().getWriter();
		out.write(result);
	}
	
	/**
	 * @Description: 账户及明细信息列表
	 * @Author：  lt
	 * @CreateDate： 2015-6-6
	 * @Modifier：lt
	 * @ModifyDate：2015-6-6
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	@Action(value = "queryRepayDetail", results = { @Result(name = "json", type = "json") })
	public void queryRepayDetail() throws Exception {
		String accountId = ServletActionContext.getRequest().getParameter("accountId");
		Integer ishis = Integer.parseInt(ServletActionContext.getRequest().getParameter("ishis"));
		PatientAccountrepaydetail repayDetailSearch = new PatientAccountrepaydetail();
		repayDetailList = repayDetailService.getPage(page,rows,repayDetailSearch,accountId,ishis,detailAccounttype);
		int total = repayDetailService.getTotal(repayDetailSearch,accountId,ishis,detailAccounttype);
		
		List<String> listStr = new ArrayList<String>(); 
		for (PatientAccountrepaydetail repayDetail : repayDetailList) {
			if(StringUtils.isNotBlank(repayDetail.getDetailRefid())&&repayDetail.getDetailOptype()==2){
				listStr.add(repayDetail.getDetailRefid());
			}
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", repayDetailList);
		String menuJson = JSONUtils.toJson(map);
		WebUtils.webSendJSON(menuJson);
	}
	/**
	 * @desc 结清账户
	 * @author  lt
	 * @param 参数
	 * @date 2015-6-18
	 * @version 1.0
	 * @return void
	 * @throws Exception 
	 */
	@RequiresPermissions(value={"HZZH:function:clean"})
	@Action(value = "totalAccount", results = { @Result(name = "json", type = "json") })
	public void totalAccount() throws Exception {
		String idcardId = ServletActionContext.getRequest().getParameter("idcardId");
		if(StringUtils.isNotEmpty(idcardId)){
			account = accountService.queryByIdcardId(idcardId);
		}else{
			String accountId = ServletActionContext.getRequest().getParameter("accountId");
			account = accountService.queryById(accountId);
		}
		if(account!=null){
			//double total = repayDetailService.totalAccount(accountId);
			repaydetail = new PatientAccountrepaydetail();
			if(account.getClinicBalance()!=0d&&account.getClinicBalance()!=null){
				if(account.getClinicBalance()>0){
					//repaydetail.setDetailRefid(repaydetailId);
					repaydetail.setDetailOptype(4);
					repaydetail.setDetailCreditamount(account.getClinicBalance());
				}else{
					repaydetail.setDetailDebitamount(account.getClinicBalance());
					repaydetail.setDetailOptype(4);
				}
				repaydetail.setAccount(account);
				repaydetail.setDetailAccounttype(1);
				repayDetailService.saveOrUpdate(repaydetail);
				
				//创建操作记录实体 ,用来记录操作记录数据 更新PatientAccountdetail 表
				detail = new PatientAccountdetail();
				detail.setDetailAmount(account.getClinicBalance());
				detail.setDetailOptype("结清账户");
				detail.setDetailVancancy(0.0);
				detail.setAccount(account);
				detail.setDetailAccounttype(1);
				detailService.saveOrUpdate(detail);//保存记录信息
				
				//执行预存金变为历史预存金操作
				repayDetailService.updateIshis(account.getId());
				account.setClinicBalance(0d);
			}
			if(account.getInpatientBalance()!=0d && account.getInpatientBalance()!=null){
				if(account.getInpatientBalance()>0){
					//repaydetail.setDetailRefid(repaydetailId);
					repaydetail.setDetailOptype(4);
					repaydetail.setDetailCreditamount(account.getInpatientBalance());
				}else{
					repaydetail.setDetailDebitamount(account.getInpatientBalance());
					repaydetail.setDetailOptype(4);
				}
				repaydetail.setAccount(account);
				repaydetail.setDetailAccounttype(2);
				repayDetailService.saveOrUpdate(repaydetail);
				
				//创建操作记录实体 ,用来记录操作记录数据 更新PatientAccountdetail 表
				detail = new PatientAccountdetail();
				detail.setDetailAmount(account.getInpatientBalance());
				detail.setDetailOptype("结清账户");
				detail.setDetailVancancy(0.0);
				detail.setAccount(account);
				detail.setDetailAccounttype(2);
				detailService.saveOrUpdate(detail);//保存记录信息
				
				//执行预存金变为历史预存金操作
				repayDetailService.updateIshis(account.getId());
				account.setInpatientBalance(0d);
			}
			account.setAccountBalance(0d);
			accountService.saveOrUpdate(account);
		}
		String menuJson = JSONUtils.toJson(account.getAccountBalance());
		WebUtils.webSendJSON(menuJson);
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
	@Action(value = "checkAccount", results = { @Result(name = "json", type = "json") })
	public void checkAccount() throws Exception {
		String retVal = "no";
		String idcardId = ServletActionContext.getRequest().getParameter("idcardId");
		if(StringUtils.isNotEmpty(idcardId)){
			account = accountService.queryByIdcardId(idcardId);
		}else{
			String accountId = ServletActionContext.getRequest().getParameter("accountId");
			account = accountService.queryById(accountId);
		}
		
		String json = "";
		if(account==null){
			retVal = "yes";
			json = JSONUtils.toJson(retVal);
		}else{
			json = JSONUtils.toJson(account);
		}
		WebUtils.webSendJSON(json);
	}
	/**
	 * @Description: 执行账户注销
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"HZZH:function:cancellation"})
	@Action(value = "zhuxiaoAccount", results = { @Result(name = "json", type = "json") })
	public void zhuxiaoAccount() throws Exception {
		String retVal = "no";
		String accountId = ServletActionContext.getRequest().getParameter("accountId");
		account = accountService.queryById(accountId);
		account.setDel_flg(1);
		accountService.saveOrUpdate(account);
		retVal = "yes";
		
		//创建操作记录实体 ,用来记录操作记录数据 更新PatientAccountdetail 表
		detail = new PatientAccountdetail();
		detail.setDetailAmount(0.0);
		detail.setDetailOptype("注销账户");
		detail.setDetailVancancy(account.getAccountBalance());
		detail.setAccount(account);
		detailService.saveOrUpdate(detail);//保存记录信息
		
		String menuJson = JSONUtils.toJson(retVal);
		WebUtils.webSendJSON(menuJson);
	}
	
	/**
	 * @Description: 执行账户冻结
	 * @Author：  lt
	 * @CreateDate： 2015-11-26
	 * @version 1.0
	**/
	@RequiresPermissions(value={"HZZH:function:freeze"})
	@Action(value = "freezeAccount", results = { @Result(name = "json", type = "json") })
	public void freezeAccount() throws Exception {
		String retVal = "no";
		String accountId = ServletActionContext.getRequest().getParameter("accountId");
		account = accountService.queryById(accountId);
		account.setAccountFrozencapital(account.getAccountBalance());
		account.setAccountFrozentime(new Date());
		account.setAccountState(4);//表状态冻结
		accountService.saveOrUpdate(account);
		retVal = "yes";
		
		//创建操作记录实体 ,用来记录操作记录数据 更新PatientAccountdetail 表
		detail = new PatientAccountdetail();
		detail.setDetailAmount(0d);
		detail.setDetailOptype("冻结账户");
		detail.setDetailVancancy(account.getAccountBalance());
		detail.setAccount(account);
		detailService.saveOrUpdate(detail);//保存记录信息
		
		String menuJson = JSONUtils.toJson(retVal);
		WebUtils.webSendJSON(menuJson);
	}
	/**
	 * @Description: 执行账户解冻
	 * @Author：  lt
	 * @CreateDate： 2015-11-26
	 * @version 1.0
	**/
	@RequiresPermissions(value={"HZZH:function:unfreeze"})
	@Action(value = "unfreezeAccount", results = { @Result(name = "json", type = "json") })
	public void unfreezeAccount() throws Exception {
		String retVal = "no";
		String accountId = ServletActionContext.getRequest().getParameter("accountId");
		account = accountService.queryById(accountId);
		account.setAccountFrozencapital(0d);
		account.setAccountUnfrozentime(new Date());
		account.setAccountState(1);//表状态正常
		accountService.saveOrUpdate(account);
		retVal = "yes";
		
		//创建操作记录实体 ,用来记录操作记录数据 更新PatientAccountdetail 表
		detail = new PatientAccountdetail();
		detail.setDetailAmount(0d);
		detail.setDetailOptype("解冻账户");
		detail.setDetailVancancy(account.getAccountBalance());
		detail.setAccount(account);
		detailService.saveOrUpdate(detail);//保存记录信息
		
		String menuJson = JSONUtils.toJson(retVal);
		WebUtils.webSendJSON(menuJson);
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
	@RequiresPermissions(value={"HZZH:function:disable","HZZH:function:enable"},logical=Logical.OR)
	@Action(value = "stopFlgAccount", results = { @Result(name = "json", type = "json") })
	public void stopFlgAccount() throws Exception {
		String retVal = "no";
		String accountId = ServletActionContext.getRequest().getParameter("accountId");
		int stopFlg = Integer.parseInt(ServletActionContext.getRequest().getParameter("stopFlg"));
		account = accountService.queryById(accountId);
		account.setStop_flg(stopFlg);
		accountService.saveOrUpdate(account);
		retVal = "yes";
		
		//创建操作记录实体 ,用来记录操作记录数据 更新PatientAccountdetail 表
		detail = new PatientAccountdetail();
		detail.setDetailAmount(0.0);
		String str = "";
		if(stopFlg == 1){
			str = "停用账户";
		}else if(stopFlg == 0){
			str = "启用账户";
		}
		detail.setDetailOptype(str);
		detail.setDetailVancancy(account.getAccountBalance());
		detail.setAccount(account);
		detailService.saveOrUpdate(detail);//保存记录信息
		
		String menuJson = JSONUtils.toJson(retVal);
		WebUtils.webSendJSON(menuJson);
	}
	
	/**  
	 * @Description： 支票开户银行渲染
	 * @param:
	 * @Author：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryBankMapComboboxs", results = { @Result(name = "json", type = "json") })
	public void queryBankMapComboboxs() {
		List<BusinessDictionary> codeBank = codeInInterService.getDictionary("bank");
		Map<String,String> payMap = new HashMap<String, String>();
		for(BusinessDictionary modl : codeBank){
			payMap.put(modl.getId(), modl.getName());
		}
		String menuJson = JSONUtils.toJson(payMap);
		WebUtils.webSendJSON(menuJson);
	}
	
	
	
	
	
	public PatientAccountdetail getDetail() {
		return detail;
	}

	public void setDetail(PatientAccountdetail detail) {
		this.detail = detail;
	}

	public List<PatientAccountdetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<PatientAccountdetail> detailList) {
		this.detailList = detailList;
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

	public PatientAccount getAccount() {
		return account;
	}

	public void setAccount(PatientAccount account) {
		this.account = account;
	}

	public List<PatientAccount> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<PatientAccount> accountList) {
		this.accountList = accountList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
