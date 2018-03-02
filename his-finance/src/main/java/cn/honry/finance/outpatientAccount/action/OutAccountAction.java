package cn.honry.finance.outpatientAccount.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientOutprepay;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.finance.outpatientAccount.service.OutAccountService;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * 门诊患者账户管理Action
 * @author  wangfujun
 * @date 创建时间：2016年3月28日 下午1:57:19
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/finance/outAccount")
public class OutAccountAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	//栏目别名,在主界面中点击栏目时传到action的参数
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	private Logger logger=Logger.getLogger(OutAccountAction.class);
	
	/***
	 * 注入本类sevice 
	 */
	@Autowired
	@Qualifier(value = "outAccountService")
	private OutAccountService outAccountService;
	public void setOutAccountService(OutAccountService outAccountService) {
		this.outAccountService = outAccountService;
	}
	
	@Autowired

	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	
/********************************	页面传递参数   Begin	*******************************************************************************/	
	/***
	 * 门诊患者账户表实体
	 */
	private OutpatientAccount account;
	
	/***
	 * 门诊预交金表实体
	 */
	private OutpatientOutprepay outprepay;
	/**  就诊卡号(卡号) **/
	private String idcardNo;
	/**预存金，是否历史（1是，0否）**/
	private String ishistory;
	/**  退款状态，1 部分退款，0 全额退款  **/
	private String resType;
	/**修改密码时，输入的原密码**/
	private String oldpwd;
	/** 修改密码时输入的新密码***/
	private String nowpwd;
	/**起始页数**/
	private String page;
	/**数据条数**/
	private String rows;
	/****
	 * 患者账户信息，是否用就诊卡查询得到   1 是；0  否
	 * 业务需求，
	 * 原：直接根据就站卡查询患者账户信息
	 * 现：若患者发生退卡/补卡情况，就诊卡卡号发生改变，此时需添加病历号查询
	 */
	private String idcardQuery;
	/**患者病历号**/
	private String blhString;
	
	
	/*******************************    页面传递参数   end  *******************************************************************************/
	/***
	 * 主页面初始化
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月30日
	 * @version 1.0
	 * @param
	 * @since
	 */
	@RequiresPermissions(value={"MZZHGL:function:view"}) 
	@Action(value = "accountView",results={ @Result(name="view",location="/WEB-INF/pages/business/outpatientAccount/accountView.jsp")},interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String accountView(){
		return "view";
	}
	
	/***
	 * 跳转:患者创建账户URL
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月30日
	 * @version 1.0
	 * @param
	 * @since
	 */
	@RequiresPermissions(value={"MZZHGL:function:add"}) 
	@Action(value = "accountAdd",results={ @Result(name="add",location="/WEB-INF/pages/business/outpatientAccount/accountAdd.jsp")},interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String accountAdd(){
		return "add";
	}
	
	/***
	 * 跳转：收预交金URL
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月30日
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZZHGL:function:add"}) 
	@Action(value = "checkout",results={ @Result(name="save",location="/WEB-INF/pages/business/outpatientAccount/checkouty.jsp")},interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String checkout(){
		return "save";
	}
	
	/***
	 * 跳转：修改密码URL
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月30日
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZZHGL:function:add"}) 
	@Action(value = "editpwd",results={ @Result(name="edit",location="/WEB-INF/pages/business/outpatientAccount/uppassword.jsp")},interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editpwd(){
		return "edit";
	}
	
	/***
	 * 根据就诊卡号，查询患者信息
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月29日
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZZHGL:function:query"}) 
	@Action(value="queryPatient")
	public void queryPatient(){
		PatientIdcard  model = outAccountService.getForidcardNo(idcardNo,menuAlias);
		Map<String,Object> map = new HashMap<String,Object>();
		Patient patient = null;
		OutpatientAccount account = null;
		if(model != null){
			/** 2017年2月9日14:05:45 GH 判断就诊卡状态  1正常 2停用、挂失 3失效*/
			if(model.getIdcardStatus()!=null&&model.getIdcardStatus()!=1){
				if(model.getIdcardStatus()==2){
					//停用或挂失状态下不可操作账户
					WebUtils.webSendString("stop");
				}else {
					//失效状态下不可操作账户
					WebUtils.webSendString("invalid");
				}
			}else{
				patient = model.getPatient();
				account = outAccountService.getForidcardid(menuAlias, model.getId());
				
				if(account == null){
					String medicalrecordId = blhString;
					account = outAccountService.getAccounForblh(medicalrecordId);
					if(account != null){
						idcardQuery = "0";
					}
				}else{
					idcardQuery = "1";
				}
				map.put("patient", patient);
				map.put("account", account);
				map.put("idcardQuery", idcardQuery);
				String jsonString = JSONUtils.toJson(map, false, DateUtils.DATE_FORMAT, false);
				WebUtils.webSendJSON(jsonString);
			}
		}
	}
	
	/***
	 * 根据就诊卡号，查询患者账户实体
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月1日
	 * @version 1.0
	 * @param
	 * @since
	 */
	@RequiresPermissions(value={"MZZHGL:function:query"}) 
	@Action(value="getAccountForcardNo")
	public void getAccountForcardNo(){
		//根据就诊卡号查就诊卡信息，根据就诊卡信息查账户信息
		PatientIdcard PatientIdcard = outAccountService.getForidcardNo(idcardNo, menuAlias);
		account = outAccountService.getForidcardid(menuAlias,PatientIdcard.getId());
		String jsonString = JSONUtils.toJson(account);
		WebUtils.webSendJSON(jsonString);
	}
	
	/**************************		门诊患者账户表  操作		************************************************************************************************/
	
	/***
	 * 为患者添加患者账户
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月30日
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZZHGL:function:add"}) 
	@Action(value="addAccount")
	public void addAccount(){
		try {
			outAccountService.addAccount(idcardNo,account,menuAlias);
			WebUtils.webSendJSON("success");
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("MZZHGL_TJHZZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZZHGL_TJHZZH", "门诊账户管理_添加患者账户", "2", "0"), e);
		}
	}
		
	/***
	 * 停用账户    重启账户  
	 * 账户状态：0停用 1正常 2注销 ；默认：1
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月31日
	 * @version 1.0
	 * @param
	 * @since
	 */
	@RequiresPermissions(value={"MZZHGL:function:add"}) 
	@Action(value="updateAccount")
	public void updateAccount(){
		try {
			outAccountService.updateAccount(account, idcardNo,blhString,idcardQuery,menuAlias);
			WebUtils.webSendString("success");
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("MZZHGL_CQZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZZHGL_CQZH", "门诊账户管理_重启账户", "2", "0"), e);
		}
	}
	
	/***
	 * 结清账户
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月1日
	 * @version 1.0
	 * @param
	 * @since
	 */
	@RequiresPermissions(value={"MZZHGL:function:add"}) 
	@Action(value="settleAccount")
	public void settleAccount(){
		try {
			outAccountService.settleAccount(idcardNo, menuAlias);
			WebUtils.webSendString("success");
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("MZZHGL_JQZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZZHGL_JQZH", "门诊账户管理_结清账户", "2", "0"), e);
		}
	}
	
	/***
	 * 注销账户
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月1日
	 * @version 1.0
	 * @param
	 * @since
	 */
	@RequiresPermissions(value={"MZZHGL:function:add"}) 
	@Action(value="logoutAccount")
	public void logoutAccount(){
		try {
			outAccountService.settleAccount(idcardNo, menuAlias);
			outAccountService.updateAccount(account, idcardNo,blhString,idcardQuery, menuAlias);
			WebUtils.webSendString("success");
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("MZZHGL_ZXZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZZHGL_ZXZH", "门诊账户管理_注销账户", "2", "0"), e);
		}
	}
	
	/***
	 * 修改密码
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月1日
	 * @version 1.0
	 * @param
	 * @since
	 */
	@RequiresPermissions(value={"MZZHGL:function:add"}) 
	@Action(value="uppwdAccount")
	public void uppwdAccount(){
		try {
			String jsonString = "error";
			Boolean bl = outAccountService.uppwdAccount(idcardNo, oldpwd, nowpwd, menuAlias);
			if(bl){
				jsonString = "success";
			}
			WebUtils.webSendString(jsonString);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("MZZHGL_XGMM", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZZHGL_XGMM", "门诊账户管理_修改密码", "2", "0"), e);
		}
	}
	
	/**************************		门诊预交金表  操作		********************************************************************************************************/
	
	/***
	 * 收预交金
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月31日
	 * @version 1.0
	 * @param
	 * @since
	 */
	@RequiresPermissions(value={"MZZHGL:function:save"}) 
	@Action(value="saveOutprepay")
	public void saveOutprepay(){
		int flag = 0;//抛出并发异常或其他异常：0；正常保存：1；
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			outAccountService.saveOutprepay(idcardNo, outprepay, menuAlias);
			flag = 1; //保存成功，修改标记
			//根据就诊卡号，获取就诊卡信息
			PatientIdcard patientIdcard = outAccountService.getForidcardNo(idcardNo, menuAlias);
			//根据就诊卡编号，获取患者账户信息
			OutpatientAccount patientAccount = outAccountService.getForidcardid(menuAlias, patientIdcard.getId());
			
			map.put("resMsg", "success");
			map.put("accountBalance", patientAccount.getAccountBalance());
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("MZZHGL_SYJJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZZHGL_SYJJ", "门诊账户管理_收预交金", "2", "0"), e);
		}finally{
			if(flag == 0){//保存过程中产生了并发异常或其他异常
				map.put("resMsg", "error");
				map.put("resMes", "系统繁忙，请稍后重试！");
			}
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}
		
	}
	
	/***
	 * 退预交金的金额判断
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月1日
	 * @version 1.0
	 * @param
	 * @since
	 */
	@RequiresPermissions(value={"MZZHGL:function:save"}) 
	@Action(value="judgeMoney")
	public void judgeMoney(){
		OutpatientOutprepay model = outAccountService.get(outprepay.getId());
		OutpatientAccount vo = outAccountService.getAccountForID(model.getAccountId());
		Map<String,Object> map = new HashMap<String,Object>();
		/***
		 * 预存金金额  > 账户余额；进行部分退款，退账户余额
		 * 预存金金额  < 账户余额；进行全额退款，退预交余额
		 * 1 部分退款，0 全额退款
		 */
		if(model.getPrepayCost() > vo.getAccountBalance()){
			map.put("resMsg","success");
			map.put("resType","1");
			map.put("resCode", "金额不足，只可进行部分退款，退款金额为：" + vo.getAccountBalance() + " 元。");
		}else{
			map.put("resMsg","success");
			map.put("resType","0");
			map.put("resCode","账户余额：" + vo.getAccountBalance() + " 元，本次将退款："+model.getPrepayCost() + " 元。");
		}
		String jsonString = JSONUtils.toJson(map);
		WebUtils.webSendJSON(jsonString);
	}
	
	/***
	 * 退预交金
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月1日
	 * @version 1.0
	 * @param
	 * @since
	 */
	@RequiresPermissions(value={"MZZHGL:function:save"}) 
	@Action(value="returnOutprepay")
	public void returnOutprepay(){
		Map<String,Object> map = new HashMap<String,Object>();
		outAccountService.returnOutprepay(outprepay.getAccountId(), outprepay.getId(), resType);
		//账户金额
		OutpatientAccount patientAccount = outAccountService.getAccountForID(outprepay.getAccountId());
		map.put("resMsg", "success");
		map.put("accountBalance", patientAccount.getAccountBalance());
		
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/***
	 * 补打预交金操作
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月1日
	 * @version 1.0
	 * @param
	 * @since
	 */
	@RequiresPermissions(value={"MZZHGL:function:save"}) 
	@Action(value="makeOutprepay")
	public void makeOutprepay(){
		try {
			outAccountService.makeOutprepay(outprepay.getId());
			WebUtils.webSendString("success");
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("MZZHGL_BDYJJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZZHGL_BDYJJ", "门诊账户管理_补打预交金", "2", "0"), e);
		}
	}
	
	/**************************		前台数据展示		********************************************************************************************************/
	
	/***
	 * 查询患者预存金
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月31日
	 * @version 1.0
	 * @param
	 * @since
	 */
	@RequiresPermissions(value={"MZZHGL:function:query"}) 
	@Action(value="queryPrestore")
	public void queryPrestore(){
		try {
			int total = outAccountService.getTotal(account.getId(),ishistory, menuAlias);
			List<OutpatientOutprepay> list = outAccountService.queryPrestore(account.getId(), ishistory, menuAlias,page,rows);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("total", total);
			map.put("rows", list);
			String jsonString=JSONUtils.toJson(map, false, DateUtils.DATETIME_FORMAT_HM, false);
			WebUtils.webSendJSON(jsonString);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("MZZHGL_CXYJJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZZHGL_CXYJJ", "门诊账户管理_查询患者预交金", "2", "0"), e);
		}
	}
	
	/***
	 * 查询患者账户明细
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月31日
	 * @version 1.0
	 * @param
	 * @since
	 */
	@RequiresPermissions(value={"MZZHGL:function:query"}) 
	@Action(value="queryDatailed")
	public void queryDatailed(){
		try {
			int total = outAccountService.getTotal(account.getId(), menuAlias);
			List<OutpatientAccountrecord> list = outAccountService.queryDatailed(account.getId(), menuAlias,page,rows);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("total", total);
			map.put("rows", list);
			String jsonString=JSONUtils.toJson(map, false, DateUtils.DATETIME_FORMAT_HM, false);
			WebUtils.webSendJSON(jsonString);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("MZZHGL_CXZHMX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZZHGL_CXZHMX", "门诊账户管理_查询账户明细", "2", "0"), e);
		}
	}
	
	/***
	 * 根据就诊卡号查询患者信息
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月1日
	 * @version 1.0
	 * @param
	 * @since
	 */
	@RequiresPermissions(value={"MZZHGL:function:query"}) 
	@Action(value="queryidcard")
	public void queryidcard(){
		try {
			PatientIdcard patientIdcard = outAccountService.getForidcardNo(idcardNo, menuAlias);
			List<PatientIdcard> list = new ArrayList<PatientIdcard>();
			list.add(patientIdcard);
			String jsonString=JSONUtils.toJson(list, false, DateUtils.DATETIME_FORMAT_HM,false);
			WebUtils.webSendJSON(jsonString);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("MZZHGL_CXZHXX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZZHGL_CXZHXX", "门诊账户管理_查询账户信息", "2", "0"), e);
		}
	}
	
	/***
	 * 账户状态判定
	 * @Title: accounState 
	 * @author  WFJ
	 * @createDate ：2016年5月9日
	 * @return void
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZZHGL:function:query"}) 
	@Action(value="accounState")
	public void accounState(){
		//账户状态：0停用 1正常 2注销
		int state = outAccountService.findAccounState(idcardNo);
		String json = JSONUtils.toJson(state);
		WebUtils.webSendString(json);
	}
	
	/***
	 * 修改账户单日消费限额
	 * @Title: updayLimit 
	 * @author  WFJ
	 * @createDate ：2016年5月11日
	 * @return void
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZZHGL:function:save"}) 
	@Action(value="updayLimit")
	public void updayLimit(){
		Map<String,Object> map = new HashMap<String,Object>();
		
		outAccountService.updayLimit(idcardNo,account.getAccountDaylimit());
		
		//根据就诊卡号，获取就诊卡信息
		PatientIdcard patientIdcard = outAccountService.getForidcardNo(idcardNo, menuAlias);
		//根据就诊卡编号，获取患者账户信息
		OutpatientAccount patientAccount = outAccountService.getForidcardid(menuAlias, patientIdcard.getId());
		
		map.put("resMsg", "success");
		map.put("accountDaylimit", patientAccount.getAccountDaylimit());
		
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
/*--------------------------------------------------------- get and set ------------------------------------------------------------------------*/
	public String getOldpwd() {
		return oldpwd;
	}
	public void setOldpwd(String oldpwd) {
		this.oldpwd = oldpwd;
	}
	public String getNowpwd() {
		return nowpwd;
	}
	public void setNowpwd(String nowpwd) {
		this.nowpwd = nowpwd;
	}
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public String getIshistory() {
		return ishistory;
	}
	public void setIshistory(String ishistory) {
		this.ishistory = ishistory;
	}
	public String getIdcardNo() {
		return idcardNo;
	}
	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	public OutpatientAccount getAccount() {
		return account;
	}
	public void setAccount(OutpatientAccount account) {
		this.account = account;
	}
	public OutpatientOutprepay getOutprepay() {
		return outprepay;
	}
	public void setOutprepay(OutpatientOutprepay outprepay) {
		this.outprepay = outprepay;
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
	public String getIdcardQuery() {
		return idcardQuery;
	}
	public void setIdcardQuery(String idcardQuery) {
		this.idcardQuery = idcardQuery;
	}
	public String getBlhString() {
		return blhString;
	}
	public void setBlhString(String blhString) {
		this.blhString = blhString;
	}
}
