package cn.honry.statistics.finance.nursebill.vo;

import java.util.List;

public class NurseBillHzVo {
	
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
	private Double applySum;
	
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
	/**Javabean报表**/
	private List<NurseBillHzVo> NurseBillHzList;
	
	public List<NurseBillHzVo> getNurseBillHzList() {
		return NurseBillHzList;
	}

	public void setNurseBillHzList(List<NurseBillHzVo> nurseBillHzList) {
		NurseBillHzList = nurseBillHzList;
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

	
	public Double getApplySum() {
		return applySum;
	}

	public void setApplySum(Double applySum) {
		this.applySum = applySum;
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

	public String getValidState() {
		return validState;
	}

	public void setValidState(String validState) {
		this.validState = validState;
	}
	
	
}
