package cn.honry.inpatient.arrearage.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inpatient.arrearage.service.ArrearageService;
import cn.honry.inpatient.outBalance.service.OutBalanceService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/arrearage")
public class ArrearageBalanceListAction extends ActionSupport implements ModelDriven<InpatientInfo>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(ArrearageBalanceListAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	//住院患者信息主表model
	private InpatientInfo inpatientInfo = new InpatientInfo();
	@Override
	public InpatientInfo getModel() {
		return inpatientInfo;
	}
	
	private  InpatientInPrepay inpatientInPrepay=new InpatientInPrepay();
	List<InpatientInPrepay> InpatientInPrepayList;
	
	public InpatientInPrepay getInpatientInPrepay() {
		return inpatientInPrepay;
	}
	public void setInpatientInPrepay(InpatientInPrepay inpatientInPrepay) {
		this.inpatientInPrepay = inpatientInPrepay;
	}
	public List<InpatientInPrepay> getInpatientInPrepayList() {
		return InpatientInPrepayList;
	}
	public void setInpatientInPrepayList(
			List<InpatientInPrepay> inpatientInPrepayList) {
		InpatientInPrepayList = inpatientInPrepayList;
	}
	
	private ArrearageService arrearageService;
	public ArrearageService getArrearageService() {
		return arrearageService;
	}
	@Autowired
	@Qualifier(value = "arrearageService")
	public void setArrearageService(ArrearageService arrearageService) {
		this.arrearageService = arrearageService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	//费用汇总表
	private InpatientFeeInfo inpatientFeeInfo=new InpatientFeeInfo();
	private  List<InpatientFeeInfo> InpatientFeeInfoList;
	
	public InpatientFeeInfo getInpatientFeeInfo() {
		return inpatientFeeInfo;
	}
	public void setInpatientFeeInfo(InpatientFeeInfo inpatientFeeInfo) {
		this.inpatientFeeInfo = inpatientFeeInfo;
	}
	public List<InpatientFeeInfo> getInpatientFeeInfoList() {
		return InpatientFeeInfoList;
	}
	public void setInpatientFeeInfoList(List<InpatientFeeInfo> inpatientFeeInfoList) {
		InpatientFeeInfoList = inpatientFeeInfoList;
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
	
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	/**
	 * 现在时间
	 */
	private String now;
	public String getNow() {
		return now;
	}
	public void setNow(String now) {
		this.now = now;
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
	/**
	 * @Description：  跳转至欠费结算列表页面
	 * @Author：dh
	 * @CreateDate：2016-1-7 下午16:20:21  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"QFJS:function:view"})
	@Action(value = "ListarrearageBalance", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/arrearage/arrearageBalanceList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String ListarrearageBalance() {
		//创建一个map
		Map<String,String> map=new HashMap<String,String>();
		//住院发票类型
		String invoiceType = "03";
		//查询发票（根据发票类型，和领取人（获得的员工Id））
		try{
			map = outBalanceService.queryFinanceInvoiceNo(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(),invoiceType);
		}catch(Exception e){
			e.printStackTrace();
		}
		invoiceNo = map.get("resCode");
		//获取时间
		Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        now = simpledateformat.format(calendar.getTime());
		return "list";
	}
	/**
	 * @Description：  根据时间和住院号查询预交金表
	 * @Author：dh
	 * @CreateDate：2016-1-11 上午10:20:21 
	 * @ModifyRmk：  
	 * @version 1.0
	 */				
	@Action(value="queryInpatientInPrepayView",results={@Result(name="json",type="json")})
	public void queryInpatientInPrepayView(){
		String inpatientNo = request.getParameter("inpatientNo");
		String inDate = request.getParameter("inDate");
		String outDate = request.getParameter("outDate");
		try {
			InpatientInPrepayList=arrearageService.QueryInpatientInPrepay(inpatientNo,inDate,outDate);
			String json = JSONUtils.toJson(InpatientInPrepayList,DateUtils.DATETIME_FORMAT);
			WebUtils.webSendString(json);
		}catch (Exception ex) {
			logger.error("ZYJS_QFJS", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_QFJS", "住院结算_欠费结算", "2", "0"), ex);
		}
	}
	/**
	 * @Description：  根据时间和住院号查询预交金总额
	 * @Author：dh
	 * @CreateDate：2016-1-11 上午10:20:21  
	 * @ModifyRmk：  
	 * @version 1.0
	 */				
	@Action(value="QueryprepayCost",results={@Result(name="json",type="json")})
	public void QueryprepayCost(){
		String inpatientNo = request.getParameter("inpatientNo");
		String inDate = request.getParameter("inDate");
		String outDate = request.getParameter("outDate");
		try {
			InpatientInPrepayList=arrearageService.QueryprepayCost(inpatientNo,inDate,outDate);
			String json = JSONUtils.toJson(InpatientInPrepayList);
			WebUtils.webSendString(json);
		}catch (Exception ex) {
			logger.error("ZYJS_QFJS", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_QFJS", "住院结算_欠费结算", "2", "0"), ex);
		}
	}
	/**
	 * @Description：  根据预交金表ID查询预交金
	 * @Author：dh
	 * @CreateDate：2016-1-11 上午10:20:21  
	 * @ModifyRmk：  
	 * @version 1.0
	 */				
	@Action(value="queryInpatientInPrepayID",results={@Result(name="json",type="json")})
	public void queryInpatientInPrepayID(){
		String ids = request.getParameter("id");
		if(StringUtils.isNotBlank(ids)){
		try {
			String[]  id_array=ids.split(",");
			for (String id : id_array) {
				InpatientInPrepayList=arrearageService.QueryInpatientInPrepayID(id);
			}
				String json = JSONUtils.toJson(InpatientInPrepayList);
				WebUtils.webSendString(json);
			}catch (Exception ex) {
				logger.error("ZYJS_QFJS", ex);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_QFJS", "住院结算_欠费结算", "2", "0"), ex);
			}
		}
	}
	/**
	 * @Description：  根据费用汇总表表ID查询
	 * @Author：dh
	 * @CreateDate：2016-1-11 上午10:20:21  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */				
	@Action(value="queryInpatientFeeInfoID",results={@Result(name="json",type="json")})
	public void queryInpatientFeeInfoID(){
		String ids = request.getParameter("id");
		List<InpatientFeeInfo> InpatientFeeInfoLIST = new ArrayList<InpatientFeeInfo>();
		if(StringUtils.isNotBlank(ids)){
			String[]  id_array=ids.split(",");
			try {
				for (String id : id_array) {
					List<InpatientFeeInfo> List=arrearageService.QueryID(id);
					InpatientFeeInfoLIST.addAll(List);
				}
			
				String json = JSONUtils.toJson(InpatientFeeInfoLIST);
				WebUtils.webSendString(json);
			}catch (Exception ex) {
				logger.error("ZYJS_QFJS", ex);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_QFJS", "住院结算_欠费结算", "2", "0"), ex);
			}
		}
	}
	/**
	 * @Description：  根据时间和住院号查询费用汇总表
	 * @Author：dh
	 * @CreateDate：2016-1-11 上午10:20:21 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */				
	@Action(value="queryInpatientFeeInfoView",results={@Result(name="json",type="json")})
	public void queryInpatientFeeInfoView(){
		String inpatientNo = request.getParameter("inpatientNo");
		String inDate = request.getParameter("inDate");
		String outDate = request.getParameter("outDate");
		try {
			InpatientFeeInfoList=arrearageService.inpatientNo(inpatientNo, outDate, inDate);
			String json = JSONUtils.toJson(InpatientFeeInfoList,DateUtils.DATETIME_FORMAT);
			WebUtils.webSendString(json);
		}catch (Exception ex) {
			logger.error("ZYJS_QFJS", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_QFJS", "住院结算_欠费结算", "2", "0"), ex);
		}
	}
	/**
	 * @Description：  根据时间和住院号查询费用总额
	 * @Author：dh
	 * @CreateDate：2016-1-11 上午10:20:21 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */				
	@Action(value="QuerytotCost",results={@Result(name="json",type="json")})
	public void QuerytotCost(){
		String inpatientNo = request.getParameter("inpatientNo");
		String inDate = request.getParameter("inDate");
		String outDate = request.getParameter("outDate");
		try {
			InpatientFeeInfoList=arrearageService.QuerytotCost(inpatientNo, outDate, inDate);
				String json = JSONUtils.toJson(InpatientFeeInfoList);
				WebUtils.webSendString(json);
		}catch (Exception ex) {
			logger.error("ZYJS_QFJS", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_QFJS", "住院结算_欠费结算", "2", "0"), ex);
		}
	}
}