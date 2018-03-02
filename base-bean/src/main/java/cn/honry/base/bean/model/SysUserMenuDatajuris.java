package cn.honry.base.bean.model;

import java.util.Date;

/**
 * TSysUserMenuDatajuris entity. @author MyEclipse Persistence Tools
 */

public class SysUserMenuDatajuris{
	
	private String id;
	private String userAcc;
	private String menuAlias;
	private Integer jurisType;
	private String jurisCode;
	private String createUser;
	private String createDept;
	private Date createTime;
	
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
	public Integer getJurisType() {
		return jurisType;
	}
	public void setJurisType(Integer jurisType) {
		this.jurisType = jurisType;
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
	
}