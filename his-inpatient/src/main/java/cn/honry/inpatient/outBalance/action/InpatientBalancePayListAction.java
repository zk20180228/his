package cn.honry.inpatient.outBalance.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.InpatientBalancePay;
import cn.honry.inpatient.outBalance.service.InpatientBalancePayListService;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("all")
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef("manageInterceptor")})
@Namespace(value="/inpatient/outbalancebalance")
public class InpatientBalancePayListAction extends ActionSupport{
	/**
	 * 住院收费结算实体
	 */
	private InpatientBalancePay inpatientBalancePay;
	public InpatientBalancePay getInpatientBalancePay() {
		return inpatientBalancePay;
	}
	public void setInpatientBalancePay(InpatientBalancePay inpatientBalancePay) {
		this.inpatientBalancePay = inpatientBalancePay;
	}

	@Autowired
	@Qualifier(value = "inpatientBalancePayListService")
	private InpatientBalancePayListService inpatientBalancePayListService;
	public void setInpatientBalancePayListService(
			InpatientBalancePayListService inpatientBalancePayListService) {
		this.inpatientBalancePayListService = inpatientBalancePayListService;
	}

	/**  
	 *  
	 * @Description：保存住院收费结算
	 * @Author：dh
	 * @CreateDate：2016-4-1
	 * @return void 
	 * @version 1.0
	 */
	@Action(value = "saveInpatientBalancePay")
	public void saveInpatientBalancePay(){
		String retVal="no";
		try {
			inpatientBalancePayListService.saveInpatientBalancePay(inpatientBalancePay);
			retVal="yes";
		} catch (Exception e) {
			retVal="no";
		}
		WebUtils.webSendString(retVal);
	}
}
