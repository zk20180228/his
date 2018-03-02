package cn.honry.inpatient.outBalance.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalancePay;
import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.inpatient.outBalance.service.InpatientAccountListService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/inpatient/outbalanceAccount")
public class InpatientAccountListAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 住院收费结算实付表
	 */
	private InpatientBalancePay inpatientBalancePay=new InpatientBalancePay();
	
	public InpatientBalancePay getInpatientBalancePay() {
		return inpatientBalancePay;
	}
	public void setInpatientBalancePay(InpatientBalancePay inpatientBalancePay) {
		this.inpatientBalancePay = inpatientBalancePay;
	}
	private InpatientBalanceHead inpatientBalanceHead=new InpatientBalanceHead();
	
	public InpatientBalanceHead getInpatientBalanceHead() {
		return inpatientBalanceHead;
	}
	public void setInpatientBalanceHead(InpatientBalanceHead inpatientBalanceHead) {
		this.inpatientBalanceHead = inpatientBalanceHead;
	}
	private InpatientFeeInfo inpatientFeeInfo=new InpatientFeeInfo();
	private List<InpatientFeeInfo> inpatientFeeInfoList;
	/**
	 * 费用汇总
	 */
	public InpatientFeeInfo getInpatientFeeInfo() {
		return inpatientFeeInfo;
	}
	public void setInpatientFeeInfo(InpatientFeeInfo inpatientFeeInfo) {
		this.inpatientFeeInfo = inpatientFeeInfo;
	}
	/**
	 * 费用减免
	 */
	private InpatientDerate inpatientDerate=new InpatientDerate();
	
	public InpatientDerate getInpatientDerate() {
		return inpatientDerate;
	}
	public void setInpatientDerate(InpatientDerate inpatientDerate) {
		this.inpatientDerate = inpatientDerate;
	}
	/**
	 * 摆药单
	 */
	private List<OutpatientDrugcontrol>  Drugcontrol;
	public List<OutpatientDrugcontrol> getDrugcontrol() {
		return Drugcontrol;
	}
	public void setDrugcontrol(List<OutpatientDrugcontrol> drugcontrol) {
		Drugcontrol = drugcontrol;
	}
	/**
	 * 住院主表
	 */
	private InpatientInfo inpatientInfo=new InpatientInfo();
	
	public InpatientInfo getInpatientInfo() {
		return inpatientInfo;
	}
	public void setInpatientInfo(InpatientInfo inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}
	/**
	 * 住院收费结算实付表
	 */
	private InpatientCancelitem inpatientCancelitem=new InpatientCancelitem();
	
	public InpatientCancelitem getInpatientCancelitem() {
		return inpatientCancelitem;
	}
	public void setInpatientCancelitem(InpatientCancelitem inpatientCancelitem) {
		this.inpatientCancelitem = inpatientCancelitem;
	}
	/**
	 * 住院账户实体
	 */
	private  InpatientAccount inpatientAccount=new InpatientAccount();
	private  PatientAccountrepaydetail patientAccountrepaydetail=new PatientAccountrepaydetail();
	public PatientAccountrepaydetail getPatientAccountrepaydetail() {
		return patientAccountrepaydetail;
	}
	public void setPatientAccountrepaydetail(
			PatientAccountrepaydetail patientAccountrepaydetail) {
		this.patientAccountrepaydetail = patientAccountrepaydetail;
	}
	private List<PatientAccountrepaydetail> patientAccountrepaydetailList;
	private List<InpatientAccount> InpatientAccountList;

	public InpatientAccount getInpatientAccount() {
		return inpatientAccount;
	}
	public void setInpatientAccount(InpatientAccount inpatientAccount) {
		this.inpatientAccount = inpatientAccount;
	}
	
	private InpatientAccountListService inpatientAccountListService;

	public InpatientAccountListService getInpatientAccountListService() {
		return inpatientAccountListService;
	}
	@Autowired
	@Qualifier(value = "inpatientAccountListService")
	public void setInpatientAccountListService(
			InpatientAccountListService inpatientAccountListService) {
		this.inpatientAccountListService = inpatientAccountListService;
	}
	/**
	 * 减免金额
	 */
	private String jianmian;
	
	public String getJianmian() {
		return jianmian;
	}
	public void setJianmian(String jianmian) {
		this.jianmian = jianmian;
	}
	/**
	 * 病历号
	 */
	private String medicalrecordId;
	/**
	 * 住院流水号
	 */
	private String inpatientNo;
	/**
	 * 就诊卡或住院流水号
	 */
	private String clinicCode;
	/**
	 * 发票号
	 */
	private String invoiceNo;
	/**
	 * 结算序号
	 */
	private String balanceNo;
	/**
	 * 入院时间
	 */
	private String inDate1;
	/**
	 * 当前时间
	 */
	private String outDate1;
	/**
	 * 入院时间
	 */
	private String inDate2;
	/**
	 * 当前时间
	 */
	private String outDate2;
	/**
	 * 入院时间
	 */
	private String inDate3;
	/**
	 * 当前时间
	 */
	private String outDate3;
	/**
	 * 住院实付Json
	 */
	private String zfJson;
	/**
	 * 预交金
	 */
	private String yj;
	/**
	 * 自费金额合计
	 */
	private String zfMoney;
	
	public String getZfMoney() {
		return zfMoney;
	}
	public void setZfMoney(String zfMoney) {
		this.zfMoney = zfMoney;
	}
	public String getYj() {
		return yj;
	}
	public void setYj(String yj) {
		this.yj = yj;
	}
	public String getZfJson() {
		return zfJson;
	}
	public void setZfJson(String zfJson) {
		this.zfJson = zfJson;
	}
	public String getInDate3() {
		return inDate3;
	}
	public void setInDate3(String inDate3) {
		this.inDate3 = inDate3;
	}
	public String getOutDate3() {
		return outDate3;
	}
	public void setOutDate3(String outDate3) {
		this.outDate3 = outDate3;
	}
	public String getInDate2() {
		return inDate2;
	}
	public void setInDate2(String inDate2) {
		this.inDate2 = inDate2;
	}
	public String getOutDate2() {
		return outDate2;
	}
	public void setOutDate2(String outDate2) {
		this.outDate2 = outDate2;
	}
	public String getInDate1() {
		return inDate1;
	}
	public void setInDate1(String inDate1) {
		this.inDate1 = inDate1;
	}
	public String getOutDate1() {
		return outDate1;
	}
	public void setOutDate1(String outDate1) {
		this.outDate1 = outDate1;
	}
	public String getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	/**
	 * @Description:查询
	 * @Author： dh
	 * @CreateDate： 2016-1-4
	 * @return String  
	 * @version 1.0
	**/
	@Action(value = "queryInpatientInfoList")
	public void queryInpatientInfoList() throws Exception{
		InpatientAccountList=inpatientAccountListService.getID(inpatientNo);
		String json=JSONUtils.toJson(InpatientAccountList);
		WebUtils.webSendJSON(json);
	}
	/**
	 * @Description:根据病历号查询担保金
	 * @Author： dh
	 * @CreateDate： 2016-1-4
	 * @return String  
	 * @version 1.0
	**/
	@Action(value = "queryPatientAccountList")
	public void queryPatientAccountList() throws Exception{
		patientAccountrepaydetailList = inpatientAccountListService.getmedicalrecordId(medicalrecordId);
		String json=JSONUtils.toJson(patientAccountrepaydetailList);
		WebUtils.webSendJSON(json);
	}
	/**
	 * @Description：  根据病历号查询预交金列表
	 * @Author：dh
	 * @CreateDate：2016-1-11 上午10:20:21  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */				
	@Action(value="QueryPatientAccountrepaydetailID")
	public void QueryPatientAccountrepaydetailID() throws Exception{
		patientAccountrepaydetailList = inpatientAccountListService.getpayment(medicalrecordId);
		String json=JSONUtils.toJson(patientAccountrepaydetailList);
		WebUtils.webSendJSON(json);
	}
	/**
	 * @Description：  根据病历号查询预交金总额
	 * @Author：dh
	 * @CreateDate：2016-1-11 上午10:20:21  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */				
	@Action(value="getpaymentzonge", interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void getpaymentzonge() throws Exception{
		patientAccountrepaydetailList = inpatientAccountListService.getpaymentzonge(inpatientNo);
		String json=JSONUtils.toJson(patientAccountrepaydetailList);
		WebUtils.webSendJSON(json);
	}
	/**
	 * @Description:查询转押金总金额
	 * @Author： dh
	 * @CreateDate： 2016-1-10
	 * @version 1.0
	**/
	@Action(value = "queryzhuanya",interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void queryzhuanya() throws Exception{
		patientAccountrepaydetailList=inpatientAccountListService.Queryzhuanya(medicalrecordId);
		String json=JSONUtils.toJson(patientAccountrepaydetailList);
		WebUtils.webSendJSON(json);
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
		parameterMap.put("medicalrecordId", balanceMedicalId);//病历号
		parameterMap.put("inpatientNo", BalanpatientNo);//住院流水号
		parameterMap.put("balanceNo", balanceNo);//结算序号
		parameterMap.put("jmMoney", jmMoney);//减免金额
		parameterMap.put("sh", sh);//应收
		parameterMap.put("sh1", sh1);//补收
		parameterMap.put("outDate", outDate);//结算时间
		parameterMap.put("inDate", inDate);//住院时间
		String retVal="no";
		if("发票已用完请重新领取!".equals(invoiceNo)){
			retVal = "发票已用完请重新领取!";
			WebUtils.webSendString(retVal);
		}else{
			try {
				retVal = inpatientAccountListService.saveBalance(parameterMap,zfJson);
			} catch (Exception e) {
				retVal="no";
			}
			WebUtils.webSendString(retVal);
		}
	}
	/**
	 * 结算时间
	 */
	private String outDate;
	/**
	 * 住院时间
	 */
	private String inDate;
	
	public String getOutDate() {
		return outDate;
	}
	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	/**
	 * 住院流水号
	 */
	private String BalanpatientNo;
	public String getBalanpatientNo() {
		return BalanpatientNo;
	}
	public void setBalanpatientNo(String balanpatientNo) {
		BalanpatientNo = balanpatientNo;
	}
	/**
	 * 实收
	 */
	private String sh;
	/**
	 * 补收
	 */
	private String sh1;
	
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
	/**
	 * 减免金额
	 */
	private String jmMoney;
	public String getJmMoney() {
		return jmMoney;
	}
	public void setJmMoney(String jmMoney) {
		this.jmMoney = jmMoney;
	}
	/**
	 * 结算保存病历号
	 */
	private String balanceMedicalId;
	public String getBalanceMedicalId() {
		return balanceMedicalId;
	}
	public void setBalanceMedicalId(String balanceMedicalId) {
		this.balanceMedicalId = balanceMedicalId;
	}
	/**
	 * 住院流水号
	 */
	private String inpatientNof;
	public String getInpatientNof() {
		return inpatientNof;
	}
	public void setInpatientNof(String inpatientNof) {
		this.inpatientNof = inpatientNof;
	}
	
	
}
