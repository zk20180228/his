package cn.honry.statistics.drug.billsearch.vo;

public class BillClassHzVo {
	/**
	 * 药品名称
	 */
	private String drugName;
	/**
	 * 规格
	 */
	private String specs;
	/**
	 * 总量
	 */
	private Double drugedNum;
	/**
	 * 申请科室名称
	 */
	private String deptCode;
	/**
	 * 取药药房名称
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
	
	

}
