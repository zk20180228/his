package cn.honry.base.bean.model;

import java.util.Date;

/**
 * 用户栏目按钮授权表
 * TSysUserMenuButtonsAuthor entity. @author MyEclipse Persistence Tools
 */

public class SysUserMenuButtonsAuthor implements java.io.Serializable {

	/**编号**/
	private String id;
	/**角色编号**/
	private User user;
	/**按钮编号**/
	private SysMenuButton menuButton;
	/**授权启用时间**/
	private Date tartTime;
	/**授权结束时间**/
	private Date endTime;
	/**建立人员**/
	private String createUser;
	/**建立部门**/
	private String createDept;
	/**建立时间**/
	private Date createTime;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public SysMenuButton getMenuButton() {
		return menuButton;
	}
	public void setMenuButton(SysMenuButton menuButton) {
		this.menuButton = menuButton;
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
	public Date getTartTime() {
		return tartTime;
	}
	public void setTartTime(Date tartTime) {
		this.tartTime = tartTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	
}