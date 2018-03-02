package cn.honry.base.bean.model;

import java.util.Date;

/**
 * TSysUserMenuDatajuris entity. @author MyEclipse Persistence Tools
 */

public class MsysUserMenuFunjuris{
	
	private String id;
	private String userAcc;
	private String menuAlias;
	private Integer menuHaveson;
	private Integer menuLevel;
	private Integer menuOrder;
	private String menuPath;
	private String menuParent;
	private Integer rmIsVisible;
	private String jurisCode;
	/**建立人员**/
	private String createUser;
	/**建立部门**/
	private String createDept;
	/**建立时间**/
	private Date createTime;
	private Integer maxOrder;
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
	public Integer getMenuHaveson() {
		return menuHaveson;
	}
	public void setMenuHaveson(Integer menuHaveson) {
		this.menuHaveson = menuHaveson;
	}
	public Integer getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}
	public Integer getMenuOrder() {
		return menuOrder;
	}
	public void setMenuOrder(Integer menuOrder) {
		this.menuOrder = menuOrder;
	}
	public String getMenuPath() {
		return menuPath;
	}
	public void setMenuPath(String menuPath) {
		this.menuPath = menuPath;
	}
	public String getMenuParent() {
		return menuParent;
	}
	public void setMenuParent(String menuParent) {
		this.menuParent = menuParent;
	}
	public Integer getRmIsVisible() {
		return rmIsVisible;
	}
	public void setRmIsVisible(Integer rmIsVisible) {
		this.rmIsVisible = rmIsVisible;
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
	public Integer getMaxOrder() {
		return maxOrder;
	}
	public void setMaxOrder(Integer maxOrder) {
		this.maxOrder = maxOrder;
	}
	
}