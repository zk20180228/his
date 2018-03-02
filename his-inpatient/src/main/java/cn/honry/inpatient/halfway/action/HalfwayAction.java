package cn.honry.inpatient.halfway.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
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

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inpatient.outBalance.service.OutBalanceService;
import cn.honry.inpatient.settlementRecall.action.SettlementRecallAction;
import cn.honry.utils.ShiroSessionUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * @Description 中途结算
 * @author  lyy
 * @createDate： 2016年7月14日 上午10:23:45 
 * @modifier lyy
 * @modifyDate：2016年7月14日 上午10:23:45
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/halfway")
public class HalfwayAction extends ActionSupport implements ModelDriven<InpatientInfo>{
	private Logger logger=Logger.getLogger(SettlementRecallAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InpatientInfo inpatientInfo=new InpatientInfo();
	@Override
	public InpatientInfo getModel() {
		return inpatientInfo;
	}
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	public OutBalanceService getOutBalanceService() {
		return outBalanceService;
	}

	private OutBalanceService outBalanceService;
	@Autowired
	@Qualifier(value = "outBalanceService")
	public void setOutBalanceService(OutBalanceService outBalanceService) {
		this.outBalanceService = outBalanceService;
	}
	/**
	 * 发票号
	 */
	private String invoiceNo;
	/**
	 * 系统时间
	 */
	private String now;
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getNow() {
		return now;
	}
	public void setNow(String now) {
		this.now = now;
	}
	/**
	 * @Description：  跳转至中途结算列表页面
	 * @Author：dh
	 * @CreateDate：2016-1-8 下午16:20:21  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"ZTJS:function:view"})
	@Action(value = "HalfwayBalance", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/halfway/halfwayList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String HalfwayBalance() {
		//创建一个map
		Map<String,String> map=new HashMap<String,String>();
		//住院发票类型
		String invoiceType = "03";
		try{
			//查询发票（根据发票类型，和领取人（获得的员工Id））
			map = outBalanceService.queryFinanceInvoiceNo(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(),invoiceType);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("ZYJS_ZTJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途结算", "2", "0"), e);
		}
		invoiceNo = map.get("resCode");
		//获取时间
		Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        now = simpledateformat.format(calendar.getTime());
		return "list";
	}
}
