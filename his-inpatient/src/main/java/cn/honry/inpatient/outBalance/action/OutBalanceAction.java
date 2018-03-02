package cn.honry.inpatient.outBalance.action;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
import cn.honry.base.bean.model.BusinessEcoformula;
import cn.honry.base.bean.model.BusinessEcoicdfee;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inpatient.outBalance.service.OutBalanceService;
import cn.honry.inpatient.outBalance.vo.InvoicePrintVo;
import cn.honry.report.service.IReportService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**  
 * @className：OutBalanceAction
 * @Description：  出院结算Action 
 * @Author：hedong
 * @CreateDate：2015-12-1 下午16:50:08 
 * @Modifier：
 * @ModifyDate：
 * @ModifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/outbalanceout")
public class OutBalanceAction extends ActionSupport{
	private Logger logger=Logger.getLogger(OutBalanceAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private static final long serialVersionUID = 1L;
    /**
     * 出院结算service
     * @param outBalanceService
     */
	@Autowired
	@Qualifier(value = "outBalanceService")
	private OutBalanceService outBalanceService;
	public void setOutBalanceService(OutBalanceService outBalanceService) {
		this.outBalanceService = outBalanceService;
	}
	/**
	 * 公共编码资料service实现层
	 * @param innerCodeService
	 */
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	/**
	 * 注入patinentInnerService公共接口
	 */
	private PatinentInnerService patinentInnerService;
	@Autowired
	@Qualifier(value = "patinentInnerService")
	public PatinentInnerService getPatinentInnerService() {
		return patinentInnerService;
	}
	public void setPatinentInnerService(PatinentInnerService patinentInnerService) {
		this.patinentInnerService = patinentInnerService;
	}
	/**报表打印接口**/
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
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
	 * 发票号
	 */
	private String invoiceNo;
	private HttpServletRequest request;
	/**
	 * 病历号
	 */
	private String medicalrecordIdSearch;
	/**
	 * 现在时间
	 */
	private String now;
	/**
	 * 住院流水号
	 */
	private String inpatientNo; 
	/**
	 * 入院时间
	 */
	private String inDate;
	/**
	 * 结算时间
	 */
	private String outDate;
	/**
	 * 预交金
	 */
	private String yj;
	/**
	 * 汇总信息的自费金额合计 
	 */
	private String zfMoney;
	/**
	 * 结算序号
	 */
	private String balanceNo;
	/**
	 * 减免金额
	 */
	private String jmMoney;
	/**
	 * 应收
	 */
	private String sh;
	/**
	 * 补收
	 */
	private String sh1;
	/**
	 * 住院实付Json
	 */
	private String zfJson;
	/**
	 * 预交金列表ids
	 */
	private String prepayIds;
	/**
	 * 费用列表Json
	 */
	private String costJson;
	public String getCostJson() {
		return costJson;
	}
	public void setCostJson(String costJson) {
		this.costJson = costJson;
	}
	public String getPrepayIds() {
		return prepayIds;
	}
	public void setPrepayIds(String prepayIds) {
		this.prepayIds = prepayIds;
	}
	public String getZfJson() {
		return zfJson;
	}
	public void setZfJson(String zfJson) {
		this.zfJson = zfJson;
	}
	public String getYj() {
		return yj;
	}
	public void setYj(String yj) {
		this.yj = yj;
	}
	public String getZfMoney() {
		return zfMoney;
	}
	public void setZfMoney(String zfMoney) {
		this.zfMoney = zfMoney;
	}
	public String getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}
	public String getJmMoney() {
		return jmMoney;
	}
	public void setJmMoney(String jmMoney) {
		this.jmMoney = jmMoney;
	}
	public String getSh() {
		return sh;
	}
	public void setSh(String sh) {
		this.sh = sh;
	}
	public String getSh1() {
		return sh1;
	}
	public void setSh1(String sh1) {
		this.sh1 = sh1;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getOutDate() {
		return outDate;
	}
	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	public String getMedicalrecordIdSearch() {
		return medicalrecordIdSearch;
	}
	public void setMedicalrecordIdSearch(String medicalrecordIdSearch) {
		this.medicalrecordIdSearch = medicalrecordIdSearch;
	}
	public String getNow() {
		return now;
	}
	public void setNow(String now) {
		this.now = now;
	}
	/**  
	 * @Description：  跳转至出院结算列表页面
	 * @Author：hedong
	 * @CreateDate：2015-12-1 下午16:56:21  
	 * @Modifier：  
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"CYJS:function:view"})
	@Action(value = "listOutBalance", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/outBalance/outBalanceList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listOutBalance() {
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
	 * @Description：  跳转至计算器
	 * @Author：dh
	 * @CreateDate：2016-03-30
	 * @Modifier：  
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "counter", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/outBalance/counter.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String counter() {
		return "list";
	}
	/**  
	 * @Description：  跳转至添加支付方式界面
	 * @Author：dh
	 * @CreateDate：2016-03-30
	 * @Modifier：  
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "PayWayList", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/outBalance/PayWayList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String PayWayList() {
		return "list";
	}
	/**
	 * 根据就诊卡号号获取住院信息
	 * @throws Exception
	 */
	@Action(value = "queryByInpatientNo")
	public void queryByInpatientNo() throws Exception{
		try{
			//通过接口查询就诊卡号对应的病历号
			String medicalrecordId = medicalrecordIdSearch;
			//1：根据住院号获取患者住院信息   (需显示到页面)
			List<InpatientInfoNow> patientInfo = outBalanceService.queryInfoByPatientNo(medicalrecordId);
			for (InpatientInfoNow inpat : patientInfo) {
				String deptCode = inpat.getDeptCode();
				inpat.setDeptCode(outBalanceService.getDeptName(deptCode));
			}
			String json = JSONUtils.toJson(patientInfo);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
		}
	}
	/**
	 * 根据住院号获取住院信息
	 * @throws Exception
	 */
	@Action(value = "queryBymedicalrecordId")
	public void queryBymedicalrecordId() throws Exception{
		try{
			//1：根据住院号获取患者住院信息   (需显示到页面)
			List<InpatientInfoNow> patientInfo = outBalanceService.queryInfoByPatientNo(medicalrecordIdSearch);
			for (InpatientInfoNow inpat : patientInfo) {
				String deptCode = inpat.getDeptCode();
				inpat.setDeptCode(outBalanceService.getDeptName(deptCode));
			}
			String json = JSONUtils.toJson(patientInfo);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
		}
	}
	/**
	 * @Description:查询患者最大的结算序号
	 * @Author： dh
	 * @CreateDate： 2016-1-15
	 * @version 1.0
	**/
	@Action(value = "querybalanceNo")
	public void querybalanceNo(){
		try{
			List<InpatientInfoNow> list = outBalanceService.getbalanceNo(inpatientNo);
			Integer balanceNo = list.get(0).getBalanceNo();
			WebUtils.webSendString(balanceNo.toString());
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
		}
	}
	/**
	 * @Description：  根据住院号查询担保金总额
	 * @Author：dh
	 * @CreateDate：2015-1-7
	 * @ModifyRmk：  
	 * @version 1.0
	 */				
	@Action(value="queryInpatientSurety")
	public void queryInpatientSurety(){
		try{
			List<InpatientSurety> inpatientSuretyList=outBalanceService.queryInpatientSurety(inpatientNo, outDate, inDate);
			String suretyCost = "";
			if(inpatientSuretyList==null){
				suretyCost = "0";
			}else{
				suretyCost = String.valueOf(inpatientSuretyList.get(0).getSuretyCost());
			}
			WebUtils.webSendString(suretyCost);
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
		}
	}
	/**
	 * @Description:查询退费申请详情
	 * @Author： dh
	 * @CreateDate： 2016-1-5
	 * @version 1.0
	**/
	@Action(value = "queryInpatientCancelitemList")
	public void queryInpatientCancelitemList(){
		try{
			List<InpatientCancelitemNow> list = outBalanceService.getInpatientCancelitem(inpatientNo);
			Map<String, Object> retMap = new HashMap<String, Object>();
			if(list.size()>0){
				retMap.put("resMsg", "存在未确认的退费申请");
				retMap.put("resCode", "2");
			}else{
				retMap.put("resCode", "success");
			}
			String json=JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
		}
	}
	/**
	 * @Description:根据病住院流水号查询是否存在有效的退药单
	 * @Author： dh
	 * @CreateDate： 2016-1-5
	 * @version 1.0
	 **/
	@Action(value = "queryDrugcontrolList")
	public void queryDrugcontrolList(){
		try{
			List<OutpatientDrugcontrol> list=outBalanceService.queryDrugcontrol(inpatientNo);
			String result = "";
			if(list.size()>0){
				result="存在";
			}else{
				result="不存在";
			}
			WebUtils.webSendString(result);
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
		}
	}
	/**
	 * @Description:根据住院流水号查询   查询住院预交金表的转押金是否打印
	 * @Author： dh
	 * @CreateDate： 2016-1-5
	 * @version 1.0
	 **/
	@Action(value = "queryInPrepayList")
	public void queryInPrepayList(){
		try{
			List<InpatientInPrepayNow> list=outBalanceService.queryInPrepay(inpatientNo);
			String result = "";
			if(list.size()==0){
				result="不存在";
			}else{
				result="存在";
			}
			WebUtils.webSendString(result);
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);			
		}
	}
	/**
	 * @Description:根据住院流水号    对患者进行封账
	 * @Author： dh
	 * @CreateDate： 2016-1-5
	 * @version 1.0
	**/
	@Action(value = "updateInpatientInfoList")
	public void updateInpatientInfoList(){
		String retVal="";
		try {
			retVal = outBalanceService.UpdateInpatientInfoList(inpatientNo);
		} catch (Exception e) {
			retVal = "封账失败";
		}
		WebUtils.webSendString(retVal);
	}
	/**
	 * @Description：  根据住院号查询预交金总额
	 * @Author：dh
	 * @CreateDate：2015-1-7
	 * @ModifyRmk：  
	 * @version 1.0
	 */				
	@Action(value="queryPrepayCost")
	public void queryPrepayCost(){
		try{
			List<InpatientInPrepayNow> List = outBalanceService.queryInpatientInPrepay(inpatientNo,inDate,outDate);
			String json=JSONUtils.toJson(List);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
		}
	}
	/**
	 * @Description:获取费用汇总
	 * @Author： dh
	 * @CreateDate： 2016-1-5
	 * @version 1.0
	 **/
	@Action(value = "getInpatientFee")
	public void getInpatientFee(){
		try{
			List<InpatientFeeInfoNow> list = outBalanceService.InpatientFeeList(inpatientNo,inDate,outDate);
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
		}
	}
	/**
	 * @Description:渲染支付方式
	 * @Author： dh
	 * @CreateDate： 2016-1-5
	 * @version 1.0
	 **/
	@Action(value = "queryPayway")
	public void queryPayway(){
		try{
			Map<String,String> map = innerCodeService.getBusDictionaryMap("payway");
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
		}
	}
	/**
	 * @Description:支付方式下拉
	 * @Author： dh
	 * @CreateDate： 2016-1-5
	 * @version 1.0
	 **/
	@Action(value = "queryPaywayCom")
	public void queryPaywayCom(){
		try{
			List<BusinessDictionary> list = innerCodeService.getDictionary("payway");
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
		}
	}
	/**
	 * @Description:查询银行
	 * @Author： dh
	 * @CreateDate： 2016-1-5
	 * @version 1.0
	 **/
	@Action(value = "queryBank")
	public void queryBank(){
		try{
			List<BusinessDictionary> list = innerCodeService.getDictionary("bank");
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
		}
	}
	/**
	 * @Description:查询优惠套餐表  是否有单病种优惠
	 * @Author： dh
	 * @CreateDate： 2016-1-5
	 * @version 1.0
	**/
	@Action(value = "queryBusinessEcoformula")
	public void queryBusinessEcoformula(){
		try{
			List<BusinessEcoformula> list = outBalanceService.getclinicCode(inpatientNo);
			String result = "";
			if(list.size()==0){
				result = "0";//不存在单病种优惠
			}else{
				for (BusinessEcoformula businessEcoformula : list) {
					if(businessEcoformula.getIcdcodeFlag() != null && businessEcoformula.getIcdcodeFlag() == 1){
						result = "1";//存在单病种优惠
					}else{
						result = "0";//不存在单病种优惠
					}
				}
			}
			WebUtils.webSendString(result);
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
		}
	}
	/**
	 * @Description:根据住院流水号，单病种编码和当前时间获取单病种优惠信息
	 * @Author： dh
	 * @CreateDate： 2016-1-5
	 * @version 1.0
	**/
	@Action(value = "queryBusinessEcoicdfee")
	public void queryBusinessEcoicdfee(){
		try{
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sysdate = simpledateformat.format(calendar.getTime());
			List<BusinessEcoicdfee> list = outBalanceService.getcost(sysdate, inpatientNo);
			int result = 0;
			if(list.size()==0){
				result = 0;
			}else{
				for (BusinessEcoicdfee businessEcoicdfee : list) {
					result+=businessEcoicdfee.getCost();
				}
			}
			WebUtils.webSendString(String.valueOf(result));
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
		}
	}
	/**
	 * @Description:根据住院流水号 获取减免金额
	 * @Author： dh
	 * @CreateDate： 2016-1-5
	 * @version 1.0
	**/
	@Action(value = "queryInpatientDerateList")
	public void queryInpatientDerateList() {
		try{
			List<InpatientDerate> list=outBalanceService.getclinicNo(inpatientNo,inDate,outDate);
			Double result = 0.00;
			for (InpatientDerate inpatientDerate : list) {
				if(inpatientDerate.getDerateCost() == null){
					result = 0.00;
				}else{
					result = inpatientDerate.getDerateCost();
				}
			}
			WebUtils.webSendString(String.valueOf(result));
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
		}
	}
	/**
	 * @Description:根据住院流水号，住院主表的费用信息
	 * @Author： dh
	 * @CreateDate： 2016-1-5
	 * @version 1.0
	 **/
	@Action(value = "inpatientInfoqueryFee", interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void inpatientInfoqueryFee() throws Exception{
		try{
			List<InpatientInfoNow> list = outBalanceService.InpatientInfoqueryFee(inpatientNo);
			int result = 0;
			if(list.size()==0){
				result = 0;
			}else{
				for (InpatientInfoNow inpatientInfo : list) {
					result+=inpatientInfo.getPubCost();
				}
			}
			WebUtils.webSendString(String.valueOf(result));
		}catch(Exception e){
			logger.error("ZYJS_ZT_QF_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：结算保存
	 * @Author：dh
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "saveBalance", results = { @Result(name = "json", type = "json")})
	public void saveBalance() throws Exception {
		Map<String,String> parameterMap=new HashMap<String,String>();
		parameterMap.put("yj", yj);//预交金
		parameterMap.put("zfMoney", zfMoney);//汇总信息的自费金额合计 
		parameterMap.put("invoiceNo", invoiceNo);//发票号
		parameterMap.put("medicalrecordId", medicalrecordIdSearch);//病历号
		parameterMap.put("inpatientNo", inpatientNo);//住院流水号
		parameterMap.put("balanceNo", balanceNo);//结算序号
		parameterMap.put("jmMoney", jmMoney);//减免金额
		parameterMap.put("sh", sh);//应收
		parameterMap.put("sh1", sh1);//补收
		parameterMap.put("outDate", outDate);//结算时间
		parameterMap.put("inDate", inDate);//住院时间
		parameterMap.put("prepayIds", prepayIds);//预交金列表ids
		String retVal="no";
		if("发票已用完请重新领取!".equals(invoiceNo)){
			retVal = "发票已用完请重新领取!";
			WebUtils.webSendString(retVal);
		}else{
			try {
				retVal = outBalanceService.saveBalance(parameterMap,zfJson,costJson);
			} catch (Exception e) {
				retVal = e.getLocalizedMessage();
				WebUtils.webSendJSON("error");
				//hedong 20170407 异常信息输出至日志文件
				logger.error("ZYJS_ZT_QF_CYJS", e);
				//hedong 20170407 异常信息保存至mongodb
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_中途_欠费_出院结算", "2", "0"), e);
			}
			WebUtils.webSendString(retVal);
		}
	}
	/**  
	 *  
	 * @Description：中途和欠费结算保存
	 * @Author：dh
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "saveBalanceZhongtu", results = { @Result(name = "json", type = "json")})
	public void saveBalanceZhongtu() throws Exception {
		Map<String,String> parameterMap=new HashMap<String,String>();
		parameterMap.put("yj", yj);//预交金
		parameterMap.put("zfMoney", zfMoney);//汇总信息的自费金额合计 
		parameterMap.put("invoiceNo", invoiceNo);//发票号
		parameterMap.put("medicalrecordId", medicalrecordIdSearch);//病历号
		parameterMap.put("inpatientNo", inpatientNo);//住院流水号
		parameterMap.put("balanceNo", balanceNo);//结算序号
		parameterMap.put("jmMoney", jmMoney);//减免金额
		parameterMap.put("sh", sh);//应收
		parameterMap.put("sh1", sh1);//补收
		parameterMap.put("outDate", outDate);//结算时间
		parameterMap.put("inDate", inDate);//住院时间
		parameterMap.put("prepayIds", prepayIds);//预交金列表ids
		String retVal="no";
		if("发票已用完请重新领取!".equals(invoiceNo)){
			retVal = "发票已用完请重新领取!";
			WebUtils.webSendString(retVal);
		}else{
			try {
				retVal = outBalanceService.saveBalanceZhongtu(parameterMap,zfJson,costJson);
			} catch (Exception e) {
				logger.error("ZYJS_CYJS", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_CYJS", "住院结算_出院结算", "2", "0"), e);
				retVal = e.getLocalizedMessage();
			}
			WebUtils.webSendString(retVal);
		}
	}
	/**
	 * @see 住院收费打印
	 */
	@Action(value = "printBalace")
	public void printBalace(){
		request=ServletActionContext.getRequest();
		String root_path = request.getSession().getServletContext().getRealPath("/");
		 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/SFJSQD.jasper";
		 HashMap<String, Object> parameters = new HashMap<String, Object>();
		 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
		 parameters.put("HOSPITALNAME", HisParameters.PREFIXFILENAME);
		 try {
			 List<InvoicePrintVo> list=outBalanceService.printBalance(medicalrecordIdSearch);
			iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(list));
		} catch (Exception e) {
			logger.error("ZYJS_ZTJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYJS_ZTJS", "住院结算_报表打印", "2", "0"), e);
		}
	}
}
