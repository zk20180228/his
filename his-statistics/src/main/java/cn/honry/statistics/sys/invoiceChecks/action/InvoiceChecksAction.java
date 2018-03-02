package cn.honry.statistics.sys.invoiceChecks.action;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.hospital.service.HospitalInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.sys.invoiceChecks.service.InvoiceChecksService;
import cn.honry.statistics.sys.invoiceChecks.vo.VinpatirntInfoBalance;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/***
 * 结算患者发票对账单
 * @Description:
 * @author: donghe
 * @CreateDate: 2016年6月22日 
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/InvoiceChecks")
//@Namespace(value = "/stat/sys")
public class InvoiceChecksAction extends ActionSupport{
	private Logger logger=Logger.getLogger(InvoiceChecksAction.class);

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
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	private HttpServletRequest request;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 当前页数，用于分页查询
	 */
	private String page;
	/****/
	private String idCard;
	/**
	 * 分页条数，用于分页查询
	 */
	private String rows;

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
	
	@Autowired
    @Qualifier(value = "invoiceChecksService")
 	private InvoiceChecksService invoiceChecksService;
	public void setInvoiceChecksService(InvoiceChecksService invoiceChecksService) {
		this.invoiceChecksService = invoiceChecksService;
	}
	/**
	 * 注入patinentInnerService公共接口
	 */
	private PatinentInnerService patinentInnerService;
	@Autowired
	@Qualifier(value = "patinentInnerService")
	public void setPatinentInnerService(PatinentInnerService patinentInnerService) {
		this.patinentInnerService = patinentInnerService;
	}
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	/**获取医院名称**/
	@Autowired
	@Qualifier(value="hospitalInInterService")
	private HospitalInInterService hospitalInInterService;
	public void setHospitalInInterService(
			HospitalInInterService hospitalInInterService) {
		this.hospitalInInterService = hospitalInInterService;
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
	 * 开始时间
	 */
	private String beginDate;
	/**
	 * 结束时间
	 */
	private String endDate;
	
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * 病历号
	 */
	private String medicalrecordId;
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	/**
	 * 住院流水号
	 */
	private String inpatientNo;
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
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
	
	private  VinpatirntInfoBalance balance;
	
	public VinpatirntInfoBalance getBalance() {
		return balance;
	}
	public void setBalance(VinpatirntInfoBalance balance) {
		this.balance = balance;
	}
	/***
	 * 
	 * @Description:结算患者发票对账单  页面跳转
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@Action(value="listInvoiceChecks",results={ @Result(name = "list",location= "/WEB-INF/pages/stat/invoiceChecks/invoiceChecks.jsp") },interceptorRefs={@InterceptorRef(value= "manageInterceptor") })
	public String listInvoiceChecks(){
		//获取时间
		Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd hh24:mm:ss");
        now = simpledateformat.format(calendar.getTime());
		return "list";
	}
	/***
	 * 
	 * @Description:查询患者信息以及费用信息
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@Action(value = "queryVinpatirntInfoBalancelist")
	public void queryVinpatirntInfoBalancelist() {
		try{
			List<VinpatirntInfoBalance> list = invoiceChecksService.queryVinpatirntInfoBalance(inpatientNo,invoiceNo);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_JSHZFPDZD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_JSHZFPDZD", "住院统计_结算患者发票对账单", "2", "0"), e);
		}
	}
	/***
	 * 
	 * @Description:根据后六位查询患者信息
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@Action(value = "queryInpatientInfolist")
	public void queryInpatientInfolist() {
		try{
			if(StringUtils.isNotBlank(medicalrecordId)){
				medicalrecordId=medicalrecordId.trim();
			}else{
				idCard=idCard.trim();
			}
			List<InpatientInfoNow> list = invoiceChecksService.queryInfolist(medicalrecordId,idCard);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_JSHZFPDZD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_JSHZFPDZD", "住院统计_结算患者发票对账单", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/***
	 * 
	 * @Description:根据后六位查询患者信息
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@Action(value = "queryInpatientBalanceHeadlist")
	public void queryInpatientBalanceHeadlist() {
		try{
			List<InpatientBalanceHeadNow> list = invoiceChecksService.queryBalanceHead(inpatientNo);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("ZYTJ_JSHZFPDZD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_JSHZFPDZD", "住院统计_结算患者发票对账单", "2", "0"), e);
		}
	}
	/***
	 * 
	 * @Description:结账患者 List
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@Action(value = "queryVinpatirntInfoBalan")
	public void queryVinpatirntInfoBalan() {
		try{
			Map<String,Object> retMap = new HashMap<String,Object>();
			if(StringUtils.isBlank(inpatientNo)){
				List<VinpatirntInfoBalance> list = new ArrayList<VinpatirntInfoBalance>();
				int total = 0 ;
				retMap.put("total", total);
				retMap.put("rows", list);
				String json = JSONUtils.toJson(retMap);
				WebUtils.webSendJSON(json);
			}else{
				List<VinpatirntInfoBalance> list = invoiceChecksService.queryVinpatirntInfoBalancepages(inpatientNo, invoiceNo, page, rows);
				int total = invoiceChecksService.queryVinpatirntInfoBalanceTotal(inpatientNo, invoiceNo);
				retMap.put("total", total);
				retMap.put("rows", list);
				String json = JSONUtils.toJson(retMap);
				WebUtils.webSendJSON(json);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("ZYTJ_JSHZFPDZD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_JSHZFPDZD", "住院统计_结算患者发票对账单", "2", "0"), e);
		}
	}
	/**
	 * @author conglin
	 * @See 根据发票号和住流水号提供打印功能
	 * @throws null
	 */
	
	@Action(value = "queryVinpatirntInfoBalanPDF")
	public void queryVinpatirntInfoBalanPDF() {
		try {
			balance.setPatientName(java.net.URLDecoder.decode(balance.getPatientName(),"UTF-8"));
			balance.setPactCode(java.net.URLDecoder.decode(balance.getPactCode(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			ArrayList<VinpatirntInfoBalance> vinpatirntInfoBalanceList=new ArrayList<VinpatirntInfoBalance>();
			
			balance.setVinpatirntInfoBalance(invoiceChecksService.queryVinpatirntInfoBalancepages(inpatientNo, balance.getInvoiceNo(), page, rows));
			
			request=ServletActionContext.getRequest();
			 String root_path = request.getSession().getServletContext().getRealPath("/");
			 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/VinpatirntInfoBalance.jasper";
			 HashMap<String, Object> parameters = new HashMap<String, Object>();
			 parameters.put("hospitalName", hospitalInInterService.getPresentHospital().getName());
			 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
			 parameters.put("serviceHopital", "结账患者发票对账单");
			 vinpatirntInfoBalanceList.add(balance);
				iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(vinpatirntInfoBalanceList));
			} catch (Exception e) {
				logger.error("ZYTJ_JSHZFPDZD", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_JSHZFPDZD", "住院统计_结算患者发票对账单", "2", "0"), e);
				e.printStackTrace();
			}
	}
}



