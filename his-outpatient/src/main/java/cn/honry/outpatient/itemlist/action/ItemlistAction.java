package cn.honry.outpatient.itemlist.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessStackinfo;
import cn.honry.base.bean.model.OutpatientItemlist;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientAccount;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.patient.account.service.AccountInInterService;
import cn.honry.outpatient.itemlist.service.ItemlistService;
import cn.honry.outpatient.itemlist.vo.UndrugVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


/**  
 *  
 * @className：IcdAction 
 * @Description：  icdAction
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/outpatient/itemlist")
public class ItemlistAction extends ActionSupport implements ModelDriven<OutpatientItemlist> {
	@Override
	public OutpatientItemlist getModel() {
		return itemlist;
	}
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier(value = "itemlistService")
	private ItemlistService itemlistService;
	public void setItemlistService(ItemlistService itemlistService) {
		this.itemlistService = itemlistService;
	}
	@Autowired
	@Qualifier(value = "accountService")
	private AccountInInterService accountInInterService;

	public void setAccountInInterService(AccountInInterService accountInInterService) {
		this.accountInInterService = accountInInterService;
	}
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	private OutpatientItemlist itemlist = new OutpatientItemlist();
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	   
	// 记录异常日志
	private Logger logger = Logger.getLogger(ItemlistAction.class);
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	
	/**  
	 *  
	 * @Description：  跳转到门诊划价页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-18 下午03:38:21  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-18 下午03:38:21  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"MZHJ:function:view"})
	@Action(value = "listItemlist", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/itemlist/itemlistList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listItemlist() {
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  查询最小费用下的非药品tree
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-18 下午05:37:12  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-18 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryUndrugByMinimum", results = { @Result(name = "json", type = "json") })
	public void queryUndrugByMinimum() {
		try {
			String treeJsonList = itemlistService.queryUndrugByMinimum(itemlist.getId());
			WebUtils.webSendString(treeJsonList);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("MZHJ_FZTSF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHJ_FZTSF", "门诊划价_分诊台收费", "2","0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  查询非药品组套
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-3 上午09:34:35  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-3 上午09:34:35  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryUndrugStackTree", results = { @Result(name = "json", type = "json") })
	public void queryUndrugStackTree() {
		try {
			String treeJsonList = itemlistService.queryUndrugStackTree(itemlist.getId());
			WebUtils.webSendString(treeJsonList);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("MZHJ_FZTSF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHJ_FZTSF", "门诊划价_分诊台收费", "2","0"), e);
		}
	}
	
	/**  
	 * @Description： 根据病历号 查询患者信息 门诊收费
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-19 下午01:09:49  
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryRegisterInfoByCaseNo", results = { @Result(name = "json", type = "json") })
	public void queryRegisterInfoByCaseNo() {
		try {
			String midicalrecordId = ServletActionContext.getRequest().getParameter("midicalrecordId");
			Patient patient = itemlistService.queryRegisterInfoByCaseNo(midicalrecordId);
			String json = JSONUtils.toJson(patient);
			WebUtils.webSendString(json);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("MZHJ_FZTSF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHJ_FZTSF", "门诊划价_分诊台收费", "2","0"), e);
		}
	}
	
	/**  
	 * @Description： 根据病历号 查询挂号信息 护士站收费
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-19 下午01:09:49  
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryRegisterByCaseNo", results = { @Result(name = "json", type = "json") })
	public void queryRegisterByCaseNo() {
		try {
			RegisterInfo info = itemlistService.queryRegisterByCaseNo(itemlist.getId());
			String json = JSONUtils.toJson(info);
			WebUtils.webSendString(json);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("MZHJ_FZTSF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHJ_FZTSF", "门诊划价_分诊台收费", "2","0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description： 根据病历号 查询挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-19 下午01:09:49  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-19 下午01:09:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryUndrugStackInfo", results = { @Result(name = "json", type = "json") })
	public void queryUndrugStackInfo() {
		try {
			List<BusinessStackinfo> infoList = itemlistService.queryUndrugStackInfo(itemlist.getId());
			String json = JSONUtils.toJson(infoList);
	
			WebUtils.webSendString(json);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("MZHJ_FZTSF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHJ_FZTSF", "门诊划价_分诊台收费", "2","0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  保存门诊收费信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-3 下午06:04:25  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-3 下午06:04:25  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "saveItemlist", results = { @Result(name = "json", type = "json") })
	public void saveItemlist() {
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			//查询账户余额
			PatientAccount account = accountInInterService.getAccountByMedicalrecord(StringUtils.isBlank(itemlist.getCaseNo())?"":itemlist.getCaseNo());
			String json = request.getParameter("jsonData");
			List<UndrugVo> infoList = JSONUtils.fromJson(json, new TypeToken<List<UndrugVo>>(){});
			//计算总金额
			Double sumMoney = new Double(0.00);
			for(UndrugVo vo : infoList){
				sumMoney += vo.getPayCost();
			}
			if(account==null){
				map.put("resMsg", "error");
				map.put("resCode", "该病历号无账户信息,请联系管理员!");
			}else if(account.getStop_flg()==1){//停用
				map.put("resMsg", "error");
				map.put("resCode", "账户["+account.getAccountName()+"]已停用,请联系管理员!");
			}else if(account.getAccountState()==2){//注销3结清4冻结
				map.put("resMsg", "error");
				map.put("resCode", "账户["+account.getAccountName()+"]已注销!");
			}else if(account.getAccountState()==3){//注销3结清4冻结
				map.put("resMsg", "error");
				map.put("resCode", "账户["+account.getAccountName()+"]已结清款项,无法收费!");
			}else if(account.getAccountState()==4){//注销3结清4冻结
				map.put("resMsg", "error");
				map.put("resCode", "账户["+account.getAccountName()+"]已结冻结,请联系管理员!");
			}
			else if(account.getAccountState()==1&&sumMoney>account.getClinicBalance()){//总金额大于剩余的门诊金额无法结账
				map.put("resMsg", "error");
				map.put("resCode", "账户["+account.getAccountName()+"]剩余预存门诊金额["+account.getClinicBalance()+"],请充值缴费后结算!");
			}else{
				try {
					itemlistService.saveItemlist(infoList,account,itemlist,sumMoney);
					map.put("resMsg", "success");
					map.put("resCode", "缴费成功,本次扣费["+sumMoney+"]元,账户余额["+account.getClinicBalance()+"]元!");
				} catch (Exception e) {
					map.put("resMsg", "error");
					map.put("resCode", "保存失败!");
					e.printStackTrace();
					logger.error("MZHJ_FZTSF", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHJ_FZTSF", "门诊划价_分诊台收费", "2","0"), e);
				}
			}
		WebUtils.webSendString(JSONUtils.toJson(map));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZHJ_FZTSF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHJ_FZTSF", "门诊划价_分诊台收费", "2","0"), e);
		}
	}
	
	/**  
	 * @Description： 开立科室下拉框
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "quertComboboxDept", results = { @Result(name = "json", type = "json") })
	public void quertComboboxDept() {
		try {
			List<SysDepartment> departmentList = itemlistService.quertComboboxDept();
			String json = JSONUtils.toJson(departmentList);
		
			WebUtils.webSendString(json);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("MZHJ_FZTSF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHJ_FZTSF", "门诊划价_分诊台收费", "2","0"), e);
		}
	}
	
	/**  
	 * @Description： 开立医生下拉框
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "quertComboboxEmp", results = { @Result(name = "json", type = "json") })
	public void quertComboboxEmp() {
		try {
			String dept = ServletActionContext.getRequest().getParameter("dept");
			List<SysEmployee> employeeList = itemlistService.quertComboboxEmp(dept);
			String json = JSONUtils.toJson(employeeList);
		
			WebUtils.webSendString(json);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("MZHJ_FZTSF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHJ_FZTSF", "门诊划价_分诊台收费", "2","0"), e);
		}
	}
	
	
	/**  
	 * @Description： 合同单位下拉
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "quertComboboxCont", results = { @Result(name = "json", type = "json") })
	public void quertComboboxCont() {
		try {
			List<BusinessContractunit> contractunitList = itemlistService.quertComboboxCont();
			String json = JSONUtils.toJson(contractunitList);
	
			WebUtils.webSendString(json);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("MZHJ_FZTSF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHJ_FZTSF", "门诊划价_分诊台收费", "2","0"), e);
		}
	}
}
