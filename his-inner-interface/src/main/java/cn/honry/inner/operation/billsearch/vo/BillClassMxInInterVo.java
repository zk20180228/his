package cn.honry.inner.operation.billsearch.vo;

import java.util.Date;

public class BillClassMxInInterVo {
	
	/**
	 * 床号
	 */
	private String bedName;
	
	/**
	 * 患者姓名
	 */
	private String patientName;
	
	/**
	 * 住院号
	 */
	private String inpatientNo;
	
	/**
	 * 药品名称
	 */
	private String tradeName;
	
	/**
	 * 规格
	 */
	private String specs; 
	/**
	 * 每次量
	 */
	private Double doseOnce;
	/**
	 * 剂量单位
	 */
	private String doseUnit;
	/**
	 * 频次
	 */
	private String dfqFerq;
	/**
	 * 用法
	 */
	private String useName;
	/**
	 * 总量
	 */
	private Double drugedNum;
	/**
	 * 申请科室名称
	 */
	private String deptCode;
	
	/**
	 * 取药药房
	 */
	private String drugDeptCode;
	
	/**
	 * 摆药单名称
	 */
	private String billClassName;
	
	/**
	 * 有效性
	 */
	private String validState;
	/**
	 * 拼音码
	 */
	private String drugPinYin;
	
	/**
	 * 五笔码
	 */
	private String drugWb;
	
	/**
	 * 状态
	 */
	private String state;
	/**
	 * 摆药单号
	 */
	private String drugedBill;
	/**
	 * 发药时间
	 */
	private Date printDate;
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public Double getDoseOnce() {
		return doseOnce;
	}
	public void setDoseOnce(Double doseOnce) {
		this.doseOnce = doseOnce;
	}
	
	public String getDoseUnit() {
		return doseUnit;
	}
	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}
	public String getDfqFerq() {
		return dfqFerq;
	}
	public void setDfqFerq(String dfqFerq) {
		this.dfqFerq = dfqFerq;
	}
	public String getUseName() {
		return useName;
	}
	public void setUseName(String useName) {
		this.useName = useName;
	}
	public Double getDrugedNum() {
		return drugedNum;
	}
	public void setDrugedNum(Double drugedNum) {
		this.drugedNum = drugedNum;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDrugDeptCode() {
		return drugDeptCode;
	}
	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}
	public String getBillClassName() {
		return billClassName;
	}
	public void setBillClassName(String billClassName) {
		this.billClassName = billClassName;
	}
	public String getValidState() {
		return validState;
	}
	public void setValidState(String validState) {
		this.validState = validState;
	}
	public String getDrugPinYin() {
		return drugPinYin;
	}
	public void setDrugPinYin(String drugPinYin) {
		this.drugPinYin = drugPinYin;
	}
	public String getDrugWb() {
		return drugWb;
	}
	public void setDrugWb(String drugWb) {
		this.drugWb = drugWb;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDrugedBill() {
		return drugedBill;
	}
	public void setDrugedBill(String drugedBill) {
		this.drugedBill = drugedBill;
	}
	public Date getPrintDate() {
		return printDate;
	}
	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}
	
	
	
	

}
