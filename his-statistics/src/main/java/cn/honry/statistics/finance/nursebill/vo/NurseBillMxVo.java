package cn.honry.statistics.finance.nursebill.vo;

import java.util.Date;
import java.util.List;

public class NurseBillMxVo {
	/**
	 * 床号
	 */
	private String bedNo;
	
	/**
	 * 姓名
	 */
	private String patientName;
	
	/**
	 * 住院号
	 */
	private String medicalRecordID;
	
	/**
	 * 药品名称
	 */
	private String drugName;
	
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
	private String dfqFreq;
	
	/**
	 * 用法
	 */
	private String useName;
	
	/**
	 * 总量
	 */
	private Double applyNum;
	
	/**
	 * 单位
	 */
	private String minUnit;
	
	/**
	 * 申请科室
	 */
	private String applyDept;
	
	/**
	 * 发药药房
	 */
	private String drugDept;
	
	/**
	 * 摆药单
	 */
	private String billClassName;
	
	/**
	 * 有效性
	 */
	private String validState;
	
	/**
	 * 拼音码
	 */
	private String drugBasicPinYin;
	
	/**
	 * 五笔码
	 */
	private String drugBasicWb;
	
	/**
	 * 状态
	 */
	private String states;
	
	/**
	 * 摆药单号
	 */
	private String drugedBill;
	
	/**
	 * 发药时间
	 */
	private Date printDate;
	/**报表打印**/
	private List<NurseBillMxVo> nurseBillMxList;
	

	public List<NurseBillMxVo> getNurseBillMxList() {
		return nurseBillMxList;
	}

	public void setNurseBillMxList(List<NurseBillMxVo> nurseBillMxList) {
		this.nurseBillMxList = nurseBillMxList;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getMedicalRecordID() {
		return medicalRecordID;
	}

	public void setMedicalRecordID(String medicalRecordID) {
		this.medicalRecordID = medicalRecordID;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getSpecs() {
		return specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public String getDoseUnit() {
		return doseUnit;
	}

	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}

	public String getDfqFreq() {
		return dfqFreq;
	}

	public void setDfqFreq(String dfqFreq) {
		this.dfqFreq = dfqFreq;
	}

	public String getUseName() {
		return useName;
	}

	public void setUseName(String useName) {
		this.useName = useName;
	}

	public String getMinUnit() {
		return minUnit;
	}

	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}

	public String getApplyDept() {
		return applyDept;
	}

	public void setApplyDept(String applyDept) {
		this.applyDept = applyDept;
	}

	public String getDrugDept() {
		return drugDept;
	}

	public void setDrugDept(String drugDept) {
		this.drugDept = drugDept;
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

	public String getDrugBasicPinYin() {
		return drugBasicPinYin;
	}

	public void setDrugBasicPinYin(String drugBasicPinYin) {
		this.drugBasicPinYin = drugBasicPinYin;
	}

	public String getDrugBasicWb() {
		return drugBasicWb;
	}

	public void setDrugBasicWb(String drugBasicWb) {
		this.drugBasicWb = drugBasicWb;
	}

	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
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

	public Double getDoseOnce() {
		return doseOnce;
	}

	public void setDoseOnce(Double doseOnce) {
		this.doseOnce = doseOnce;
	}

	public Double getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(Double applyNum) {
		this.applyNum = applyNum;
	}
	
	

}
