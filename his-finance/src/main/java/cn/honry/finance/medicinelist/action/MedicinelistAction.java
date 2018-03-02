package cn.honry.finance.medicinelist.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
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
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.finance.medicinelist.service.MedicinelistService;
import cn.honry.finance.medicinelist.vo.FeeCodeVo;
import cn.honry.finance.medicinelist.vo.RecipedetailVo;
import cn.honry.finance.medicinelist.vo.UndrugAndWare;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.drug.stockInfo.service.BusinessStockInfoInInterService;
import cn.honry.inner.outpatient.medicineList.service.MedicineListInInterService;
import cn.honry.inner.outpatient.medicineList.vo.ChargeVo;
import cn.honry.inner.technical.mat.service.MatOutPutInInterService;
import cn.honry.inner.technical.mat.vo.OutputInInterVO;
import cn.honry.inner.vo.MedicalVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;

/**  
 * @className：门诊收费
 * @Description：  
 * @Author：ldl
 * @CreateDate：2016-01-22
 * @ModifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/finance/medicinelist")
//@Namespace(value = "/outpatient/medicinelist")
public class MedicinelistAction extends ActionSupport{
	Logger logger = Logger.getLogger(MedicinelistAction.class);
    private static final long serialVersionUID = 1L;
    /**
     * 收费明细表实体
     */
    private OutpatientFeedetailNow feedetail;
    
    public OutpatientFeedetailNow getFeedetail() {
		return feedetail;
	}
	public void setFeedetail(OutpatientFeedetailNow feedetail) {
		this.feedetail = feedetail;
	}
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	/**
     * 收费接口
     */
    @Autowired
    @Qualifier(value = "medicineListInInterService")
    private MedicineListInInterService medicineListInInterService;
    public void setMedicineListInInterService(
			MedicineListInInterService medicineListInInterService) {
		this.medicineListInInterService = medicineListInInterService;
	}
	
	public MedicineListInInterService getMedicineListInInterService() {
		return medicineListInInterService;
	}
	
	/**
     * 门诊收费service
     */
    @Autowired
    @Qualifier(value = "medicinelistService")
    private MedicinelistService medicinelistService;
	public void setMedicinelistService(MedicinelistService medicinelistService) {
		this.medicinelistService = medicinelistService;
	}
	/***
	 * inner-interface包下的科室相关的service
	 */
	@Autowired
    @Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	@Autowired
	@Qualifier(value = "matOutPutInInterService")
	private MatOutPutInInterService matOutPutInInterService;
	@Autowired
	@Qualifier(value = "businessStockInfoInInterService")
	private BusinessStockInfoInInterService businessStockInfoInInterService;
	public void setBusinessStockInfoInInterService(
			BusinessStockInfoInInterService businessStockInfoInInterService) {
		this.businessStockInfoInInterService = businessStockInfoInInterService;
	}
	/**
     * 合同单位ID
     */
	private String count;
	
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	
	/**
     * 结算类别
     */
	private String pink;
	
	public String getPink() {
		return pink;
	}
	public void setPink(String pink) {
		this.pink = pink;
	}
	/**
     * 中间部分记录json格式
     */
	private String jsonRowsList;
	
	public String getJsonRowsList() {
		return jsonRowsList;
	}
	public void setJsonRowsList(String jsonRowsList) {
		this.jsonRowsList = jsonRowsList;
	}
	
	/**
     * 非药品表模糊查询字段
     */
	private String undrugCodes;
	
	public String getUndrugCodes() {
		return undrugCodes;
	}
	public void setUndrugCodes(String undrugCodes) {
		this.undrugCodes = undrugCodes;
	}
	
	/**
     * 分页属性
     */
    private String doctorId;
	
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	
	/**
     * 医嘱ID
     */
    private String page;
    
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	
	/**
     * 分页属性
     */
	private String rows;
	
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	/**
     * 最小费用代码
     */
	private String encode;
	
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
	/**
     * 非药品ID
     */
	private String undrugId;
	
	public String getUndrugId() {
		return undrugId;
	}
	public void setUndrugId(String undrugId) {
		this.undrugId = undrugId;
	}
	
	/**
     * 费用集合
     */
	private String rowFee;
	
	/**
     * 统计类别名称集合
     */
	private String rowTypeName;
	
	/**
     * 统计类别代码集合
     */
	private String rowTypeCode;
	
	/**
     * 发票号
     */
	private String invoiceNo;
	
	/**
     * 现金支付金额
     */
	private String cash;
	
	/**
     * 银行卡支付金额
     */
	private String bankCard;
	
	/**
     * 院内账户支付金额
     */
	private String hospitalAccount;
	
	/**
     * 支票支付金额
     */
	private String check;
	
	/**
     * 开户银行
     */
	private String bankUniti;
	
	/**
     * 开户账户
     */
	private String banki;
	
	/**
     * 具体单位
     */
	private String bankAccounti;
	
	/**
     * 支票号
     */
	private String bankNo;
	
	/**
     * 收费信息表中药品
     */
	private String feedetailIds;
	
	/**
     * 收费信息表中非药品
     */
	private String feedetaiNotIds;
	
	/**
     * 门诊号
     */
	private String clinicCode;
	
	/**
     * 处方号
     */
	private String recipedetailIds;
	
	/**
     * 总金额
     */
	private String totCost;
	
	/**
     * 密码
     */
	private String passwords;
	
	private String rowsListNo;
	/**
	 * combogrid参数
	 */
	private String q;
	/**门诊发票类型*/
	String invoiceType = HisParameters.OUTPATIENTINVOICETYPE;
	private String invoiceNum;
	private String feeID;//处方明细表id
	private String invoiceMap;
	private String newconts ;//更改后的合同单位
	private String feeWhenUnenougth;//库存不足的时候是否收费；0否/1是
	
	
	public String getFeeWhenUnenougth() {
		return feeWhenUnenougth;
	}
	public void setFeeWhenUnenougth(String feeWhenUnenougth) {
		this.feeWhenUnenougth = feeWhenUnenougth;
	}
	public String getNewconts() {
		return newconts;
	}
	public void setNewconts(String newconts) {
		this.newconts = newconts;
	}
	public String getInvoiceMap() {
		return invoiceMap;
	}
	public void setInvoiceMap(String invoiceMap) {
		this.invoiceMap = invoiceMap;
	}
	public String getFeeID() {
		return feeID;
	}
	public void setFeeID(String feeID) {
		this.feeID = feeID;
	}
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getRowsListNo() {
		return rowsListNo;
	}
	public void setRowsListNo(String rowsListNo) {
		this.rowsListNo = rowsListNo;
	}
	public String getPasswords() {
		return passwords;
	}
	public void setPasswords(String passwords) {
		this.passwords = passwords;
	}
	public String getRowFee() {
		return rowFee;
	}
	public void setRowFee(String rowFee) {
		this.rowFee = rowFee;
	}
	public String getRowTypeName() {
		return rowTypeName;
	}
	public void setRowTypeName(String rowTypeName) {
		this.rowTypeName = rowTypeName;
	}
	public String getRowTypeCode() {
		return rowTypeCode;
	}
	public void setRowTypeCode(String rowTypeCode) {
		this.rowTypeCode = rowTypeCode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getCash() {
		return cash;
	}
	public void setCash(String cash) {
		this.cash = cash;
	}
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getHospitalAccount() {
		return hospitalAccount;
	}
	public void setHospitalAccount(String hospitalAccount) {
		this.hospitalAccount = hospitalAccount;
	}
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	public String getBankUniti() {
		return bankUniti;
	}
	public void setBankUniti(String bankUniti) {
		this.bankUniti = bankUniti;
	}
	public String getBanki() {
		return banki;
	}
	public void setBanki(String banki) {
		this.banki = banki;
	}
	public String getBankAccounti() {
		return bankAccounti;
	}
	public void setBankAccounti(String bankAccounti) {
		this.bankAccounti = bankAccounti;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getFeedetailIds() {
		return feedetailIds;
	}
	public void setFeedetailIds(String feedetailIds) {
		this.feedetailIds = feedetailIds;
	}
	public String getFeedetaiNotIds() {
		return feedetaiNotIds;
	}
	public void setFeedetaiNotIds(String feedetaiNotIds) {
		this.feedetaiNotIds = feedetaiNotIds;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public String getRecipedetailIds() {
		return recipedetailIds;
	}
	public void setRecipedetailIds(String recipedetailIds) {
		this.recipedetailIds = recipedetailIds;
	}
	public String getTotCost() {
		return totCost;
	}
	public void setTotCost(String totCost) {
		this.totCost = totCost;
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
	 * @Description：  跳转到门诊收费(领取发票号)
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:view"})
	@Action(value = "listMedicinelist", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/medicinelist/medicinelistList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listMedicinelist() {
		try{
			//创建一个map
			Map<String,String> map=new HashMap<String,String>();
			
			//查询发票（根据发票类型，和领取人（获得的员工Id））
//			map = medicinelistService.queryFinanceInvoiceNo(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(),invoiceType);
			map = medicineListInInterService.getInvoiceNoByCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(), invoiceType,1);
			//new一个feedetail（收费明细表实体）
			feedetail = new OutpatientFeedetailNow();
			feedetail.setInvoiceNo(map.get("resCode"));
			feedetail.setInvoiceNoflay(map.get("resMsg"));
			return "list";
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
			return "list";
		}
	}
	
	/**  
	 * @Description：  根据病历号或就诊卡号后6位，弹框显示患者信息，
	 * @Author：wfj
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "dialogQueryBlhOrCardNo")
	public void dialogQueryBlhOrCardNo(){
		try{
			List<Patient> patientList=new ArrayList<Patient>();
			//根据病历号模糊查询患者
			patientList = medicinelistService.vagueFindPatientById(feedetail.getPatientNo());
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("resSize", patientList.size());
			map.put("resData", patientList);
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description：  根据病历号或就诊卡号模糊查询出的信息
	 * @Author：wfj
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:view"})
	@Action(value = "selectCardNoDialogURL", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/medicinelist/selectCardNoDialog.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String selectDialogURL(){
		return "list";
	}
	
	/**  
	 * @Description：  根据病历号或就诊卡号查询出患者信息和挂号记录
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:query"})
	@Action(value = "queryBlhOrCardNo")
	public void queryBlhOrCardNo() {
		try{
			//根据病历号或就诊卡号查询出患者信息和挂号记录
			Map<String,Object> outMap = medicinelistService.queryBlhOrCardNo(feedetail);
			String mapJosn = JSONUtils.toJson(outMap, DateUtils.DATE_FORMAT);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description：  渲染科室
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:query"})
	@Action(value = "querydeptfunction")
	public void querydeptfunction(){
		try{
			//查询科室
			List<SysDepartment> departmentList = deptInInterService.getDept();
			Map<String,String> deptMap = new HashMap<String, String>();
			for(SysDepartment dept : departmentList){//把ID和name放到一个map中
				deptMap.put(dept.getDeptCode(), dept.getDeptName());
			}
			String mapJosn = JSONUtils.toJson(deptMap);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description：  渲染合同单位
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:query"})
	@Action(value = "querycontfunction")
	public void querycontfunction(){
		try{
			//查询合同单位
			List<BusinessContractunit> contractunitList = medicinelistService.findContractunit();
			Map<String,String> contMap = new HashMap<String, String>();
			for(BusinessContractunit cont : contractunitList){//把ID和name放到一个map中
				contMap.put(cont.getEncode(), cont.getName());
			}
			String mapJosn = JSONUtils.toJson(contMap);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description：  渲染员工
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:query"})
	@Action(value = "queryempfunction")
	public void queryempfunction(){
		try{
			//查询员工
			List<SysEmployee> employeeList = medicinelistService.findEmployee();
			Map<String,String> empMap = new HashMap<String, String>();
			for(SysEmployee emp : employeeList){//把ID和name放到一个map中
				empMap.put(emp.getJobNo(), emp.getName());
			}
			String mapJosn = JSONUtils.toJson(empMap);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description：  多条查询挂号信息(弹窗显示)
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:view"})
	@Action(value = "findInfoList")
	public void findInfoList(){
		try{
			//根据病历号查询该患者挂号信息
			List<RegistrationNow> infoList = medicinelistService.findRegisterInfo(feedetail.getPatientNo());
			String mapJosn = JSONUtils.toJson(infoList,DateUtils.DATE_FORMAT);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description： 科室下拉框
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:query"})
	@Action(value = "quertComboboxDept")
	public void quertComboboxDept(){
		try{
			List<SysDepartment> departmentList = medicinelistService.quertComboboxDept();
			String mapJosn = JSONUtils.toJson(departmentList);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description：合同单位下拉框
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:query"})
	@Action(value = "quertComboboxCont")
	public void quertComboboxCont(){
		try{
			List<BusinessContractunit> contractunitList = medicinelistService.findContractunit();
			String mapJosn = JSONUtils.toJson(contractunitList);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description： 员工下拉（根据科室可联动）
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:query"})
	@Action(value = "quertComboboxEmp")
	public void quertComboboxEmp(){
		try{
			List<SysEmployee> employeeList = medicinelistService.findEmployeeList(feedetail.getRegDpcd());
			String mapJosn = JSONUtils.toJson(employeeList);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description： 右上角处方条数列表
	 * @Author：ldl
	 * @CreateDate：2016-03-31
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:query"})
	@Action(value = "findFeedetailStatistics")
	public void findFeedetailStatistics() {
		try{
			//查询右上角的处方列表（处方号相同的处方会在一条数据中，并且将他们的金额合并算出总和）参数为门诊号，开立科室，开单医生
			List<RecipedetailVo> recVoList = new ArrayList<RecipedetailVo>();
			if(StringUtils.isNotBlank(feedetail.getClinicCode())){
				recVoList = medicinelistService.findFeedetailStatistics(feedetail.getClinicCode(),feedetail.getRegDpcdname(),feedetail.getDoctCodename());
			}
			String mapJosn = JSONUtils.toJson(recVoList);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description： 得到合同单位的结算方式
	 * @Author：ldl
	 * @CreateDate：2016-03-31
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:query"})
	@Action(value = "queryCountByPaykindCode")
	public void queryCountByPaykindCode(){
		try{
			BusinessContractunit contractunit =  medicinelistService.queryCountByPaykindCode(count);
			String mapJosn = JSONUtils.toJson(contractunit);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description：  中间收费明细列表
	 * @Author：ldl
	 * @CreateDate：2016-03-31
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:query"})
	@Action(value = "findFeedetailDetails")
	public void findFeedetailDetails(){
		try{
			if(feedetail==null){
				feedetail=new OutpatientFeedetailNow();
			}
			List<MedicalVo> detailsList = new ArrayList<MedicalVo>();
			if(StringUtils.isNotBlank(feedetail.getRecipeNo())){
				detailsList =  medicinelistService.findFeedetailDetails(feedetail.getRecipeNo());
			}
			String mapJosn = JSONUtils.toJson(detailsList);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	
	/**  
	 * @Description：  渲染单位
	 * @Author：ldl
	 * @CreateDate：2016-03-31
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:query"})
	@Action(value = "queryPackFunction")
	public void queryPackFunction(){
		try{
			//药品编码
			List<BusinessDictionary> drugpackagingunitList = innerCodeService.getDictionary(HisParameters.DRUGPACKUNIT);
			//非药品编码
			List<BusinessDictionary> nonmedicineencodingList = innerCodeService.getDictionary("nonmedicineencoding");
			Map<String,String> packMap = new HashMap<String, String>();
			for(BusinessDictionary pack : drugpackagingunitList){
				packMap.put(pack.getId(), pack.getName());
			}
			for(BusinessDictionary packNot : nonmedicineencodingList){
				packMap.put(packNot.getId(), packNot.getName());
			}
			String mapJosn = JSONUtils.toJson(packMap);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	
	/**  
	 * @Description：统计大类对照（左下角）
	 * @Author：ldl
	 * @CreateDate：2016-03-31
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:query"})
	@Action(value = "findMinfeeStatCode")
	public void findMinfeeStatCode(){
		try{
			//根据传过来的json查询统计大类的项目，赋值到新建的页面显示VO
			List<FeeCodeVo> feeCodeVoList = medicinelistService.findMinfeeStatCodeByMinfeeCodes(jsonRowsList);
			String mapJosn = JSONUtils.toJson(feeCodeVoList);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	
	/**  
	 * @Description：  查询非药品
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZSF:function:query"})
	@Action(value = "findNotDrugInfoViewList")
	public void findNotDrugInfoViewList() {
		try{
			int total = medicinelistService.getTotalUndrug(q);
			List<UndrugAndWare> undrugAndWareList = medicinelistService.findUndrugAndWareList(q,page,rows);
			Map<String,Object> undrugMap = new HashMap<String,Object>();
			undrugMap.put("total", total);
			undrugMap.put("rows", undrugAndWareList);
			String mapJosn = JSONUtils.toJson(undrugMap);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description：得到最小费用代码（单独）
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryMinState")
	public void queryMinState(){
		try{
			BusinessDictionary drugminimumcost =  innerCodeService.getDictionaryByCode("drugMinimumcost",encode);
			MinfeeStatCode minfeeStatCode = medicinelistService.minfeeStatCodeByEncode(drugminimumcost.getEncode());
			String mapJosn = JSONUtils.toJson(minfeeStatCode);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description：添加非药品时带出的辅材
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "findOdditionalitemByTypeCode")
	public void findOdditionalitemByTypeCode(){
		try{
			List<MedicalVo> odditionalitemList = medicinelistService.findOdditionalitemByTypeCode(undrugId);
			String mapJosn = JSONUtils.toJson(odditionalitemList);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description：  渲染频次
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @version 1.0,
	 */
	@Action(value = "queryfrequenyFunction")
	public void queryfrequenyFunction(){
		try{
			List<BusinessFrequency> frequencyList = medicinelistService.findFrequency();
			Map<String,String> frequencyMap = new HashMap<String, String>();
			for(BusinessFrequency frequency : frequencyList){
				frequencyMap.put(frequency.getEncode(), frequency.getName());
			}
			String mapJosn = JSONUtils.toJson(frequencyMap);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description：  划价保存
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws IOException 
	 */
	@Action(value = "savePrice")
	public void savePrice() throws IOException{
		try{
			medicinelistService.savePrice(jsonRowsList,feedetail.getClinicCode());
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description：  执行科室下拉（在可编辑表格中）
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryEdComboboxDept")
	public void queryEdComboboxDept(){
		try{
			List<SysDepartment> departmentList = deptInInterService.queryAllDept();
			String mapJosn = JSONUtils.toJson(departmentList);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	
	/**  
	 * @Description： 执行收费
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "chargeImplement")
	public void chargeImplement() {
		Map<String,String> map = new HashMap<String,String>();
		Boolean flag = false;
		try{
			ChargeVo chargeVo = new ChargeVo();
			chargeVo.setBankAccounti(bankAccounti);
			if(StringUtils.isNotBlank(bankCard)){
				chargeVo.setBankCard(Double.parseDouble(bankCard));
			}
			if(StringUtils.isNotBlank(cash)){
				chargeVo.setCash(Double.parseDouble(cash));
			}
			if(StringUtils.isNotBlank(check)){
				chargeVo.setCheck(Double.parseDouble(check));
			}
			if(StringUtils.isNotBlank(hospitalAccount)){
				chargeVo.setHospitalAccount(Double.parseDouble(hospitalAccount));
			}
			if(StringUtils.isNotBlank(totCost)){
				chargeVo.setTotCost(Double.parseDouble(totCost));
			}
			chargeVo.setBanki(banki);
			chargeVo.setBankNo(bankNo);
			chargeVo.setBankUniti(bankUniti);
			chargeVo.setClinicCode(clinicCode);
			chargeVo.setDoctorId(doctorId);//doctorId 药品是id、非药品是code
			chargeVo.setInvoiceNo(invoiceNo);
			chargeVo.setInvoiceTypeCode(rowTypeCode);
			chargeVo.setInvoiceTypeName(rowTypeName);
			chargeVo.setInvoiceTypeMoney(rowFee);
			chargeVo.setRecipeNo(recipedetailIds);
			chargeVo.setRowsListNo(rowsListNo);//处方号
			chargeVo.setPink(pink);
			chargeVo.setNewconts(newconts);
//			Map<String, String> map2 = JSONUtils.fromJson(invoiceMap, (TypeToken<Map<String,String>>) new TypeToken<Map<String,String>>(){}.getType());
			Map<String, String> map2 = new HashMap<String,String>();
			Gson gson = new Gson();
			map2 = gson.fromJson(invoiceMap, Map.class);
			chargeVo.setInvoiceMap(map2);
			map = medicineListInInterService.chargeImplement(chargeVo,feeWhenUnenougth);
			flag = true;
		}catch(Exception e){
			map.put("resMsg", "error");
			String message = e.getLocalizedMessage();
			message = message.substring(message.indexOf(":")+1,message.length());
			if(" INVOICE IS NOT ENOUGTH".equals(message)){
				map.put("resCode", "发票不足、请领取发票！");
			}else{
				map.put("resCode", message);
				logger.error("MZSF_MASF", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
			}
			flag = true;
			throw new RuntimeException(e);
		}finally{
			if(!flag){
				map.put("resMsg", "error");
				map.put("resCode", "系统繁忙，请稍后重试...");
			}
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		}
		
	}
	
	/**  
	 * @Description：  预扣库存
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "findDrugStorage")
	public void findDrugStorage() {
		try{
			Map<String,Object> map=new HashMap<String,Object>();
			List<OutpatientFeedetailNow> feedetailList = medicinelistService.findDrugStorage(feedetailIds);
			OutputInInterVO out = new OutputInInterVO();
			List<String> drugCode = new ArrayList<String>();
			List<Double> applyNums = new ArrayList<Double>();
			List<Integer> showFlags = new ArrayList<Integer>();
			Boolean flag = true;
			String resCode = "";
			if(feedetailList.size()>0){
				for(OutpatientFeedetailNow feedetail:feedetailList){
					if("1".equals(feedetail.getDrugFlag())){//药品
						drugCode.add(feedetail.getItemCode());
						applyNums.add(feedetail.getQty());
						showFlags.add(feedetail.getExtFlag3());
						Map<String, Object> valiuDrug = businessStockInfoInInterService.ynValiuDrug(feedetail.getExecDpcd(), drugCode, applyNums, showFlags, true, true, false);
						if(0==(Integer)valiuDrug.get("valiuFlag")){
							flag = false;
							if(StringUtils.isNotBlank(resCode)){
								resCode += ",";
							}
							resCode += valiuDrug.get("failMesgs");
						}
						drugCode.clear();
						applyNums.clear();
						showFlags.clear();
					}else{//非药品
						out.setApplyNum(feedetail.getQty());
						out.setFlag("I");
						out.setInpatientNo(feedetail.getClinicCode());
						out.setRecipeNo(feedetail.getRecipeNo());
						out.setSequenceNo(feedetail.getSequenceNo());
						out.setStorageCode(feedetail.getExecDpcd());
						out.setTransType(feedetail.getTransType());
						out.setUndrugItemCode(feedetail.getItemCode());
						out.setUseNum(feedetail.getQty());
						Map<String, Object> judgeMat = matOutPutInInterService.judgeMat(out);
						Integer object = (Integer) judgeMat.get("resCode");//0物资充足；1物资不足；3非物资
						if(1==object){//物资不足
							flag = false;
							map.put("resMsg", "error");
							map.put("resCode","【"+feedetail.getItemName()+"】"+"物资不足，请联系物资管理员");
							break;
						}
					}
				}
				map.put("resMsg", "error");
				map.put("resCode",resCode);
			}
			if(flag){
				map.put("resMsg", "success");
				map.put("infoList","feelsing");
			}
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description：验证住院账户密码是否正确
	 * @Author：ldl
	 * @CreateDate：2016-04-05
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "veriPassWord")
	public void veriPassWord(){
		try{
			String state = "";//是否密码正确1正确2不正确
			OutpatientAccount account = medicinelistService.veriPassWord(DigestUtils.md5Hex(passwords),feedetail.getPatientNo());
			if(StringUtils.isNotBlank(account.getId())){
				state = "OK";
			}else{
				state = "NO";
			}
			String mapJosn = JSONUtils.toJson(state);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description：检验医技是否确认
	 * @Author：ldl
	 * @CreateDate：2016-04-13
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "unDrugWarehouse")
	public void unDrugWarehouse(){
		try{
			Map<String,String> map = medicinelistService.unDrugWarehouse(feedetaiNotIds);
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	/**  
	 * 
	 * <p> 根据收费员code和类型获取单张发票号</p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月5日 下午4:49:46 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月5日 下午4:49:46 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "getInvoiceNoByCode")
	public void getInvoiceNoByCode(){
		try{
			int num = StringUtils.isNotBlank(invoiceNum)?Integer.parseInt(invoiceNum):1;
			Map<String, String> map = medicineListInInterService.getInvoiceNoByCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(), invoiceType,num);
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	/**  
	 * 
	 * <p>系统类别渲染 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月5日 下午4:50:31 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月5日 下午4:50:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "getSystemTypeMap")
	public void getSystemTypeMap(){
		try{
			Map<String, String> map = medicinelistService.getSystemTypeMap(HisParameters.SYSTEMTYPE);
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){}
	}
	/**
	 * @Description 根据处方明细ID获取处方明细
	 * @author  marongbin
	 * @createDate： 2017年2月21日 下午8:17:45 
	 * @modifier 
	 * @modifyDate：
	 * @param feeID
	 * @return: Map<String,String>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getMoney")
	public void getMoney(){
		try{
			Map<String, String> map = medicinelistService.getMoney(feeID);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
	/**
	 * @Description  修改发票号
	 * @author  marongbin
	 * @createDate： 2017年3月13日 上午10:46:24 
	 * @modifier 
	 * @modifyDate：: void
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "changeInvoiceNO")
	public void changeInvoiceNO(){
		try{
			String id = ServletActionContext.getRequest().getParameter("id");
			Map<String, String> map = medicineListInInterService.changeInvoiceNO(id, invoiceNo);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("MZSF_MASF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MASF", "门诊收费_门诊收费", "1", "0"), e);
		}
	}
}
