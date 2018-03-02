package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 分管科室维护
 * @author Administrator
 *
 */
public class EmployeeDividept extends Entity {
	private String account;//账户
	private String name;//姓名
	private String divisionCode;//学部code
	private String divisionName;//学部name
	private String deptCode;//科室code
	private String deptName;//科室name
	private Integer type;//主管分类0院领导1部门领导
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDivisionCode() {
		return divisionCode;
	}
	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}
	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
