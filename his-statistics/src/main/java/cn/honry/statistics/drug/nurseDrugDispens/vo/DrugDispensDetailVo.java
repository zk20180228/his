package cn.honry.statistics.drug.nurseDrugDispens.vo;

import java.util.List;

public class DrugDispensDetailVo {
	/**床号名称**/
	private String bedName;
	/**姓名*/
	private String patientName;
	/**通用名称**/
	private String drugCommonname;
	/**规格**/
	private String spec;
	/**单位**/
	private String unit;
	/**剂型**/
	private String drugDosageform;
	/**数量**/
	private Integer qtys;
	/**零售价**/
	private Double drugRetailprice;
	/**金额**/
	private Double totCosts;
	/**摆药单分类名称*/
	private String billclassName;
	/**取药药房*/
	private String deptDrugName;
	/**药品编码*/
	private String drugId;
	/**名称拼音码**/
	private String drugNamepinyin;
	/**名称五笔码**/
	private String drugNamewb;
	/**名称自定义码**/
	private String drugNameinputcode;
	/**申请科室*/
	private String deptName;
	/**报表打印**/
	private List<DrugDispensDetailVo> drugDispensDetailList;
	
	public List<DrugDispensDetailVo> getDrugDispensDetailList() {
		return drugDispensDetailList;
	}
	public void setDrugDispensDetailList(
			List<DrugDispensDetailVo> drugDispensDetailList) {
		this.drugDispensDetailList = drugDispensDetailList;
	}
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
	public String getDrugCommonname() {
		return drugCommonname;
	}
	public void setDrugCommonname(String drugCommonname) {
		this.drugCommonname = drugCommonname;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDrugDosageform() {
		return drugDosageform;
	}
	public void setDrugDosageform(String drugDosageform) {
		this.drugDosageform = drugDosageform;
	}
	public Integer getQtys() {
		return qtys;
	}
	public void setQtys(Integer qtys) {
		this.qtys = qtys;
	}
	public Double getDrugRetailprice() {
		return drugRetailprice;
	}
	public void setDrugRetailprice(Double drugRetailprice) {
		this.drugRetailprice = drugRetailprice;
	}
	public Double getTotCosts() {
		return totCosts;
	}
	public void setTotCosts(Double totCosts) {
		this.totCosts = totCosts;
	}
	public String getBillclassName() {
		return billclassName;
	}
	public void setBillclassName(String billclassName) {
		this.billclassName = billclassName;
	}
	public String getDeptDrugName() {
		return deptDrugName;
	}
	public void setDeptDrugName(String deptDrugName) {
		this.deptDrugName = deptDrugName;
	}
	public String getDrugId() {
		return drugId;
	}
	public void setDrugId(String drugId) {
		this.drugId = drugId;
	}
	public String getDrugNamepinyin() {
		return drugNamepinyin;
	}
	public void setDrugNamepinyin(String drugNamepinyin) {
		this.drugNamepinyin = drugNamepinyin;
	}
	public String getDrugNamewb() {
		return drugNamewb;
	}
	public void setDrugNamewb(String drugNamewb) {
		this.drugNamewb = drugNamewb;
	}
	public String getDrugNameinputcode() {
		return drugNameinputcode;
	}
	public void setDrugNameinputcode(String drugNameinputcode) {
		this.drugNameinputcode = drugNameinputcode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}
