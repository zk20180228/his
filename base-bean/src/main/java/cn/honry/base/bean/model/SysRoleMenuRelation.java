package cn.honry.base.bean.model;

import java.util.Date;

/**
 * 角色-栏目关联表
 * SysRoleMenuRelation entity. @author MyEclipse Persistence Tools
 */

public class SysRoleMenuRelation implements java.io.Serializable {

	/**id**/
	private String id;
	/**栏目编号**/
	private SysMenu sysMenu;
	/**角色编号**/
	private SysRole sysRole;
	/**数据权限编号**/
	private SysDataRight sysDataRight;
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
	public SysMenu getSysMenu() {
		return sysMenu;
	}
	public void setSysMenu(SysMenu sysMenu) {
		this.sysMenu = sysMenu;
	}
	public SysRole getSysRole() {
		return sysRole;
	}
	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}
	public SysDataRight getSysDataRight() {
		return sysDataRight;
	}
	public void setSysDataRight(SysDataRight sysDataRight) {
		this.sysDataRight = sysDataRight;
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