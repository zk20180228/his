package cn.honry.base.bean.model;

import java.math.BigDecimal;

import cn.honry.base.bean.business.Entity;

/**
 * 财务人员分组
 */

public class FinanceUsergroup extends Entity {
	
	/** 员工编号  **/
	private SysEmployee employee;
	/** 分组名称**/
	private String groupName;
	/** 组编号**/
	private String no;
	/** 拼音码   **/
	private String groupPinyin;
	/** 五笔码   **/
	private String groupWb;
	/** 自定义码   **/
	private String groupInputcode;
	/** 排序 **/
	private BigDecimal groupOrder;
	/** 备注**/
	private String stackRemark;
	/**页数   **/
	private String page;
	/**  每页行数  **/
	private String rows;
	
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public SysEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(SysEmployee employee) {
		this.employee = employee;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupPinyin() {
		return this.groupPinyin;
	}

	public void setGroupPinyin(String groupPinyin) {
		this.groupPinyin = groupPinyin;
	}

	public String getGroupWb() {
		return this.groupWb;
	}

	public void setGroupWb(String groupWb) {
		this.groupWb = groupWb;
	}

	public String getGroupInputcode() {
		return this.groupInputcode;
	}

	public void setGroupInputcode(String groupInputcode) {
		this.groupInputcode = groupInputcode;
	}

	public BigDecimal getGroupOrder() {
		return this.groupOrder;
	}

	public void setGroupOrder(BigDecimal groupOrder) {
		this.groupOrder = groupOrder;
	}

	public String getStackRemark() {
		return this.stackRemark;
	}

	public void setStackRemark(String stackRemark) {
		this.stackRemark = stackRemark;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
}