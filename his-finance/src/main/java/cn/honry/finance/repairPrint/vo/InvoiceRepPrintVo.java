package cn.honry.finance.repairPrint.vo;

import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.InpatientBalanceListNow;

public class InvoiceRepPrintVo {
	/**发票号 **/
	private String invoiceNo;
	/**结算时间 **/
	private Date balanceDate;
	/**结算类别代码 **/
	private String paykindName;	
	/**结算人 **/
	private String balanceOperName;
	/**费用总额 **/
	private Double totCost;
	/**自费金额 **/
	private Double ownCost;
	/**自付金额 **/
	private Double payCost;
	/**公费金额 **/
	private Double pubCost;
	/**患者姓名*/
	private String patientName;
	/**合同单位名称 **/
	private String pactName;
	/**住院科室*/
	private String deptName;
	/**所属病区*/
	private String nurseCellName;
	/**住院医生*/
	private String houseDocName;
	/**床号  (从病床维护表里读取)*/
	private String bedName;
	/**出生日期*/
	private Date reportBirthday;
	/**预交金额(未结)*/
	private Double prepayCost;
	/**结算序号*/
	private Integer balanceNo;
	/**发票补打**/
	private List<InpatientBalanceListNow> inpatientBalanceListNow;
	
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getPaykindName() {
		return paykindName;
	}
	public void setPaykindName(String paykindName) {
		this.paykindName = paykindName;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public Double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}
	public Double getPubCost() {
		return pubCost;
	}
	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPactName() {
		return pactName;
	}
	public void setPactName(String pactName) {
		this.pactName = pactName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Date getReportBirthday() {
		return reportBirthday;
	}
	public void setReportBirthday(Date reportBirthday) {
		this.reportBirthday = reportBirthday;
	}
	public Double getPrepayCost() {
		return prepayCost;
	}
	public void setPrepayCost(Double prepayCost) {
		this.prepayCost = prepayCost;
	}
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public String getBalanceOperName() {
		return balanceOperName;
	}
	public void setBalanceOperName(String balanceOperName) {
		this.balanceOperName = balanceOperName;
	}
	public String getNurseCellName() {
		return nurseCellName;
	}
	public void setNurseCellName(String nurseCellName) {
		this.nurseCellName = nurseCellName;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public String getHouseDocName() {
		return houseDocName;
	}
	public void setHouseDocName(String houseDocName) {
		this.houseDocName = houseDocName;
	}
	public Integer getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(Integer balanceNo) {
		this.balanceNo = balanceNo;
	}
	public List<InpatientBalanceListNow> getInpatientBalanceListNow() {
		return inpatientBalanceListNow;
	}
	public void setInpatientBalanceListNow(
			List<InpatientBalanceListNow> inpatientBalanceListNow) {
		this.inpatientBalanceListNow = inpatientBalanceListNow;
	}
	
}
