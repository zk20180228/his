package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * @author lt
 * 药品拆分属性
 */
@SuppressWarnings("serial")
public class DrugSplit extends Entity {
	/**部门编码*/
	private String deptCode;
	/**药品编码*/
	private String drugCode;
	/**拆分状态:  0-不可拆分  1-可拆分,配药时不取整  2-可拆分配药时上取整 3不可拆分，当日取整*/
	private String propertyCode;
	/**分类：0药品，1剂型*/
	private Integer type;
	
	//以下字段做显示用
	private String drugName;
	private String drugSpec;
	private String drugPackUnit;
	private String drugMinUnit;
	private int drugPackQty;
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public String getPropertyCode() {
		return propertyCode;
	}
	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getDrugSpec() {
		return drugSpec;
	}
	public void setDrugSpec(String drugSpec) {
		this.drugSpec = drugSpec;
	}
	public String getDrugPackUnit() {
		return drugPackUnit;
	}
	public void setDrugPackUnit(String drugPackUnit) {
		this.drugPackUnit = drugPackUnit;
	}
	public String getDrugMinUnit() {
		return drugMinUnit;
	}
	public void setDrugMinUnit(String drugMinUnit) {
		this.drugMinUnit = drugMinUnit;
	}
	public int getDrugPackQty() {
		return drugPackQty;
	}
	public void setDrugPackQty(int drugPackQty) {
		this.drugPackQty = drugPackQty;
	}
}
