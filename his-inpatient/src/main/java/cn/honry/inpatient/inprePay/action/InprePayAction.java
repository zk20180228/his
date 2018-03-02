package cn.honry.inpatient.inprePay.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.hospital.service.HospitalInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inpatient.info.service.InpatientInfoService;
import cn.honry.inpatient.inprePay.service.InprePayService;
import cn.honry.inpatient.inprePay.vo.AcceptingGold;
import cn.honry.inpatient.inprePay.vo.PatientVo;
import cn.honry.report.service.IReportService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;


/**  
 *  
 * @className：InprePayAction 
 * @Description：  住院预交金Action
 * @Author：aizhonghua
 * @CreateDate：2016-3-09 下午18:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-3-09 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/inprePay")
public class InprePayAction extends ActionSupport {
	private Logger logger=Logger.getLogger(InprePayAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private static final long serialVersionUID = 1L;
	
	/**
	 * 注入住院预交金Service
	 */
	@Autowired 
	@Qualifier(value = "inprePayService")
	private InprePayService inprePayService;
	/**
	 * 注入住院预交金Service
	 */
	@Autowired 
	@Qualifier(value = "inpatientInfoService")
	private InpatientInfoService inpatientInfoService;
	
	private CodeInInterService innerCodeService;
	@Autowired
	@Qualifier(value = "innerCodeService")
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	@Autowired
	@Qualifier(value = "hospitalInInterService")
	private HospitalInInterService hospitalInInterService;
	public void setHospitalInInterService(
			HospitalInInterService hospitalInInterService) {
		this.hospitalInInterService = hospitalInInterService;
	}
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	
	private PatinentInnerService patinentInnerService;
	@Autowired 
	@Qualifier(value = "patinentInnerService")
	public void setPatinentInnerService(PatinentInnerService patinentInnerService) {
		this.patinentInnerService = patinentInnerService;
	}
	/**
	 * 住院预交金实体类
	 */
	private InpatientInPrepayNow inPrepay;
	
	private HttpServletRequest request = ServletActionContext.getRequest();


	

	/**
	 * 就诊卡号，用于查询患者信息
	 */
	private String idcardNo;
	
	/**
	 * 病历号，用于查询患者信息
	 */
	private String medicale;
	
	/**
	 * 患者名称，用于保存
	 */
	private String name;
	
	/**
	 * 患者住院号，用于保存及查询预交金信息
	 */
	private String inpatientNo;
	
	/**
	 * 当前页数，用于分页查询
	 */
	private String page;
	
	/**
	 * 分页条数，用于分页查询
	 */
	private String rows;
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	
	/**
	 * id集合
	 */
	private String[] ids;
	
	/**
	 * 发票号
	 */
	private String invoiceUserNo;
	
	/**
	 * 判断是否领取发票号
	 */
	private String InvoiceNoflag;
	
	
	public String getInvoiceNoflag() {
		return InvoiceNoflag;
	}
	public void setInvoiceNoflag(String invoiceNoflag) {
		InvoiceNoflag = invoiceNoflag;
	}
	public String getInvoiceUserNo() {
		return invoiceUserNo;
	}
	public void setInvoiceUserNo(String invoiceUserNo) {
		this.invoiceUserNo = invoiceUserNo;
	}
	/**
	 * setters and getters
	 */
	public void setInprePayService(InprePayService inprePayService) {
		this.inprePayService = inprePayService;
	}
	public InpatientInPrepayNow getInPrepay() {
		return inPrepay;
	}
	public void setInPrepay(InpatientInPrepayNow inPrepay) {
		this.inPrepay = inPrepay;
	}
	
	public String getMedicale() {
		return medicale;
	}
	public void setMedicale(String medicale) {
		this.medicale = medicale;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getIdcardNo() {
		return idcardNo;
	}
	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
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
	
	/**  
	 *  
	 * @Description：  跳转到住院预交金list页面
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-09 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-09 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZYYJJGL:function:view"})
	@Action(value = "listInprePay", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/inprePay/inprePayList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listInprePay() {
		 Map<String,String> map=new HashMap<String,String>();
		//查询发票（根据发票类型，和领取人（获得的员工Id））
		map = inprePayService.queryFinanceInvoiceNo(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(),"04");
		invoiceUserNo=map.get("resCode");
		InvoiceNoflag=map.get("resMsg");
		return "list";
	}
	
	/**
	 *
	 * @Description：通过就诊卡号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午3:53:37 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "findPatientByIdcardNo")
	public void findPatientByIdcardNo() {
		try{
			Map<String, Object> retMap = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(idcardNo)){
				idcardNo=idcardNo.trim();
			}
			//查询就诊卡是否存在
			boolean isValid = inprePayService.checkIdcardNo(idcardNo);
			if(isValid){
				//通过接口查询就诊卡号对应的病历号
				medicale = patinentInnerService.getMedicalrecordId(idcardNo);
				//查询患者信息
				List<PatientVo> voList = inprePayService.findPatientByInpatientNo(medicale);
				retMap.put("rows",voList);
			}else{
				retMap.put("resMsg", "error");
				retMap.put("resCode","就诊卡不存在，请重新输入！");
			}
			String json = JSONUtils.toJson(retMap,false,"yyyy-MM-dd",false);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_ZYYJJGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_ZYYJJGL", "住院收费_住院预交金管理", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  跳转到添加预交金页面
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-09 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-09 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "payInpre", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/inprePay/payInpre.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String payInpre() {
		return "list";
	}
	
	/**
	 *
	 * @Description：保存预交金
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "savaInprePay")
	public void savaInprePay() {
		Map<String,Object> retMap = new HashMap<String,Object>();
		try {
			//添加预交金信息
			inPrepay.setName(name);
			inPrepay.setInpatientNo(inpatientNo);
			String msg=inprePayService.save(inPrepay);
			//更新对应的住院主表中的预交金余额
			if("success".equals(msg)){
				inprePayService.updatePatientByInpNo(inpatientNo,inPrepay.getPrepayCost());
				retMap.put("resMsg", "success");
				retMap.put("id", inPrepay.getId());
				retMap.put("resCode","缴纳预交金成功！");
			}else if("error".equals(msg)){
				retMap.put("resMsg", "error");
				retMap.put("resCode","缴纳预交金失败！");
			}else{
				retMap.put("resMsg", "error");
				retMap.put("resCode",msg);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			retMap.put("resMsg", "error");
			retMap.put("resCode","缴纳预交金失败！");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("ZYSF_ZYYJJGL", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_ZYYJJGL", "住院收费_住院预交金管理", "2", "0"), e);
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 *
	 * @Description：查询预交金信息 - 分页查询
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "queryInprePay")
	public void queryInprePay() {
		try{
			Map<String,Object> retMap = new HashMap<String,Object>();
			if(StringUtils.isBlank(inpatientNo)){
				retMap.put("total", 0);
				retMap.put("rows", new ArrayList<InpatientInPrepayNow>());
			}else{
				int total = inprePayService.getInprePayTotal(inpatientNo);
				List<InpatientInPrepayNow> list = inprePayService.getInprePayPage(inpatientNo,page,rows);
				retMap.put("total", total);
				retMap.put("rows", list);
			}
			String json = JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_ZYYJJGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_ZYYJJGL", "住院收费_住院预交金管理", "2", "0"), e);
		}
	}
	/**
	 *
	 * @Description：返回预交金 
	 * @Author：tangfeishuai
	 * @CreateDate：2016年5月26日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "removeInprePay")
	public void removeInprePay() {
		try{
			inprePayService.removeInprePayByids(ids);
			String json = JSONUtils.toJson("success");
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_ZYYJJGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_ZYYJJGL", "住院收费_住院预交金管理", "2", "0"), e);
		}
	}
	
	/**
	 *
	 * @Description：根据患者住院流水号查询封账状态
	 * @Author：tangfeishuai
	 * @CreateDate：2016年5月26日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "isStopAcount")
	public void isStopAcount() {
		try{
			InpatientInfoNow inp=inprePayService.findPatientByInpNo(inpatientNo);
			String json = JSONUtils.toJson(inp);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_ZYYJJGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_ZYYJJGL", "住院收费_住院预交金管理", "2", "0"), e);
		}
	}
	@Action(value = "isStopAcountNow")
	public void isStopAcountNow() {
		try{
			InpatientInfoNow inp=inprePayService.isStopAcountNow(inpatientNo);
			String json = JSONUtils.toJson(inp);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_ZYYJJGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_ZYYJJGL", "住院收费_住院预交金管理", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  跳转到添加担保金页面
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-09 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-09 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "surety", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/inprePay/surety.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String surety() {
		return "list";
	}
	
	/**
	 *
	 * @Description：获得银行Map
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 * @return:银行Map
	 *
	 */
	@Action(value = "getCodeBankMap")
	public void getCodeBankMap() {
		try{
			Map<String,String> retMap = inprePayService.getCodeBankMap();
			String json = JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_ZYYJJGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_ZYYJJGL", "住院收费_住院预交金管理", "2", "0"), e);
		}
	}
	
	/**
	 *
	 * @Description：通过住院号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午3:53:37 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "findPatientByInpatientNo")
	public void findPatientByInpatientNo() {
		try{
			Map<String, Object> retMap = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(medicale)){
				medicale=medicale.trim();
			}
			//查询患者信息
			List<PatientVo> voList = inprePayService.findPatientByInpatientNo(medicale);
			retMap.put("rows",voList);
			String json = JSONUtils.toJson(retMap,false,"yyyy-MM-dd",false);
			WebUtils.webSendJSON(json);
		}catch(Exception e ){
			logger.error("ZYSF_ZYYJJGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_ZYYJJGL", "住院收费_住院预交金管理", "2", "0"), e);
		}
	}
	
	
	/**  
	 * @Description： 住院预交金支付
	 * @Author：donghe
	 * @CreateDate：2016-10-19
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryPayType")
	public void queryPayType(){
		try{
			List<BusinessDictionary> list =innerCodeService.getDictionary("payway");
			List<BusinessDictionary> newList=new ArrayList<BusinessDictionary>();
			for(int i=0;i<list.size();i++){
				if(!"YS".equals(list.get(i).getEncode())){
					newList.add(list.get(i));
				}
			}
			String mapJosn = JSONUtils.toExposeJson(newList, false, null, "encode","name");
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("ZYSF_ZYYJJGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_ZYYJJGL", "住院收费_住院预交金管理", "2", "0"), e);
		}
	}
	/**
	 * @Description：住院预交金报表打印
	 * @Author：donghe
	 * @CreateDate：2017-2-21
	 * @throws Exception
	 */
	@Action(value = "iReportzyyjj")
	public void iReportzyyjj() throws Exception {
			String id= request.getParameter("id");//jasper文件所用到的参数 shouldPay
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			List<AcceptingGold> list=inprePayService.iReportzyyjj(id);
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("id", id);
			parameterMap.put("hospitalName", HisParameters.PREFIXFILENAME);
			String root_path = request.getSession().getServletContext().getRealPath("/");
			String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/zhuyuanyujiaojin.jasper";
			 try {
				iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameterMap, new JRBeanCollectionDataSource(list));
			} catch (Exception e) {
				logger.error("ZYSF_ZYYJJGL_DY", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_ZYYJJGL_DY", "住院收费_住院预交金管理_打印", "2", "0"), e);
				e.printStackTrace();
			}

	}
}
