package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class TypeContrast extends Entity{
	/**
	 * 人员类型type
	 */
	private String employeeType;
	/**
	 * 人员类型name
	 */
	private String employeeTypeName;
	/**
	 * 职务拼音
	 */
	private String employeePinyin;
	
	/**
	 * 职务五笔
	 */
	private String employeeWb;
	
	/**
	 * 职务自定义码 
	 */
	private String employeeInputCode;
	
	/**
	 * 扩展字段1
	 */
	private String extC1;
	/**
	 * 扩展字段2
	 */
	private String extC2;
	/**
	 * 扩展字段3
	 */
	private String extC3;
	
	public String getEmployeeType() {
		return employeeType;
	}
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}
	public String getEmployeeTypeName() {
		return employeeTypeName;
	}
	public void setEmployeeTypeName(String employeeTypeName) {
		this.employeeTypeName = employeeTypeName;
	}
	public String getExtC1() {
		return extC1;
	}
	public void setExtC1(String extC1) {
		this.extC1 = extC1;
	}
	public String getExtC2() {
		return extC2;
	}
	public void setExtC2(String extC2) {
		this.extC2 = extC2;
	}
	public String getExtC3() {
		return extC3;
	}
	public void setExtC3(String extC3) {
		this.extC3 = extC3;
	}
	public String getEmployeePinyin() {
		return employeePinyin;
	}
	public void setEmployeePinyin(String employeePinyin) {
		this.employeePinyin = employeePinyin;
	}
	public String getEmployeeWb() {
		return employeeWb;
	}
	public void setEmployeeWb(String employeeWb) {
		this.employeeWb = employeeWb;
	}
	public String getEmployeeInputCode() {
		return employeeInputCode;
	}
	public void setEmployeeInputCode(String employeeInputCode) {
		this.employeeInputCode = employeeInputCode;
	}

}
