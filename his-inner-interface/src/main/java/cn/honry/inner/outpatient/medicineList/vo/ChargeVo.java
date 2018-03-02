package cn.honry.inner.outpatient.medicineList.vo;

import java.util.Map;

/**  
 * @Description：  收费接口 传值 VO
 * @Author：ldl
 * @CreateDate：2016-04-26
 * @ModifyRmk：  
 * @version 1.0
 * 不带*的 是必填项 剩下的按照注释存
 */
/**
 * @author win7
 *
 */
public class ChargeVo {
	/**门诊号*/
	private String clinicCode; 
	/**现金收费金额*/
	private Double cash; //*支付方式至少选择一种
	/**银行卡收费金额*/
	private Double bankCard; //*支付方式至少选择一种
	/**支票收费金额*/
	private Double check; //*支付方式至少选择一种
	/**患者账户收费金额*/
	private Double hospitalAccount; //*支付方式至少选择一种
	/**医嘱ID集合*/
	private String doctorId; // 拼接格式"','" 例如 ： "'4564654564564','564565315313'"
	/**发票号*/
	private String invoiceNo;
	/**总金额*/
	private Double totCost;
	/**开户银行*/
	private String bankUniti; //*支付方式不选择支票的时候 不用选择
	/**开户账户*/
	private String banki; //*支付方式不选择支票的时候 不用选择
	/**开具单位*/ 
	private String bankAccounti; //*支付方式不选择支票的时候 不用选择
	/**支票号/交易号*/
	private String bankNo; //*支付方式不选择支票的时候 不用选择
	/**发票科目代码*/
	private String invoiceTypeCode;// 拼接格式"','" 例如 ： "'4564654564564','564565315313'"
	/**发票科目名称*/
	private String invoiceTypeName;// 拼接格式"','" 例如 ： "'4564654564564','564565315313'"
	/**每科目金额*/
	private String invoiceTypeMoney;// 拼接格式"','" 例如 ： "'4564654564564','564565315313'"
	/**处方号集合*/
	private String recipeNo;// 拼接格式"','" 例如 ： "'4564654564564','564565315313'"
	//处方号去重集合
	private String rowsListNo;
	//结算方式
	private String pink;
	//发票号Map<门诊号_分发票类型code,发票号_该发票号金额_该发票号_领取该发票号所在组的ID>
	private Map<String,String> invoiceMap;
	private String newconts;//更改后的合同单位
	
	
	public String getNewconts() {
		return newconts;
	}
	public void setNewconts(String newconts) {
		this.newconts = newconts;
	}
	public Map<String, String> getInvoiceMap() {
		return invoiceMap;
	}
	public void setInvoiceMap(Map<String, String> invoiceMap) {
		this.invoiceMap = invoiceMap;
	}
	public String getPink() {
		return pink;
	}
	public void setPink(String pink) {
		this.pink = pink;
	}
	public String getRowsListNo() {
		return rowsListNo;
	}
	public void setRowsListNo(String rowsListNo) {
		this.rowsListNo = rowsListNo;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public Double getCash() {
		return cash;
	}
	public void setCash(Double cash) {
		this.cash = cash;
	}
	public Double getBankCard() {
		return bankCard;
	}
	public void setBankCard(Double bankCard) {
		this.bankCard = bankCard;
	}
	public Double getCheck() {
		return check;
	}
	public void setCheck(Double check) {
		this.check = check;
	}
	public Double getHospitalAccount() {
		return hospitalAccount;
	}
	public void setHospitalAccount(Double hospitalAccount) {
		this.hospitalAccount = hospitalAccount;
	}
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
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
	public String getInvoiceTypeCode() {
		return invoiceTypeCode;
	}
	public void setInvoiceTypeCode(String invoiceTypeCode) {
		this.invoiceTypeCode = invoiceTypeCode;
	}
	public String getInvoiceTypeName() {
		return invoiceTypeName;
	}
	public void setInvoiceTypeName(String invoiceTypeName) {
		this.invoiceTypeName = invoiceTypeName;
	}
	public String getInvoiceTypeMoney() {
		return invoiceTypeMoney;
	}
	public void setInvoiceTypeMoney(String invoiceTypeMoney) {
		this.invoiceTypeMoney = invoiceTypeMoney;
	}
	
	
}
