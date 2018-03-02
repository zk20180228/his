package cn.honry.base.bean.model;

import java.util.Date;

/**
 * TSysUserMenuDatajuris entity. @author MyEclipse Persistence Tools
 */

public class SysUserMenuFunjuris{
	
	private String id;
	private String userAcc;
	private String menuAlias;
	private String jurisCode;
	private String createUser;
	private String createDept;
	private Date createTime;
	private Integer rmMenuOrder;
	private Integer rmIsVisible = 0;
	/***********与数据表无关字段***********/
	/**  
	 * @Fields menuOrder : 栏目排序
	 */
	private Integer menuOrder;
	/***********与数据表无关字段***********/
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserAcc() {
		return userAcc;
	}
	public void setUserAcc(String userAcc) {
		this.userAcc = userAcc;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getJurisCode() {
		return jurisCode;
	}
	public void setJurisCode(String jurisCode) {
		this.jurisCode = jurisCode;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateDept() {
		return createDept;
	}
	public void setCreateDept(String createDept) {
		this.createDept = createDept;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getMenuOrder() {
		return menuOrder;
	}
	public void setMenuOrder(Integer menuOrder) {
		this.menuOrder = menuOrder;
	}
	public Integer getRmMenuOrder() {
		return rmMenuOrder;
	}
	public void setRmMenuOrder(Integer rmMenuOrder) {
		this.rmMenuOrder = rmMenuOrder;
	}
	public Integer getRmIsVisible() {
		return rmIsVisible;
	}
	public void setRmIsVisible(Integer rmIsVisible) {
		this.rmIsVisible = rmIsVisible;
	}
	
}