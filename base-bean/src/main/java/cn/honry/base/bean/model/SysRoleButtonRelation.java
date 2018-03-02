package cn.honry.base.bean.model;

import java.util.Date;

/**
 * 角色-按钮关联表
 * SysRoleButtonRelation entity. @author aizhonghua
 */

public class SysRoleButtonRelation implements java.io.Serializable {
	
	/**id**/
	private String id;
	/**角色编号**/
	private SysRole sysRole;
	/**按钮编号**/
	private SysMenuButton sysMenuButton;
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
	public SysRole getSysRole() {
		return sysRole;
	}
	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}
	public SysMenuButton getSysMenuButton() {
		return sysMenuButton;
	}
	public void setSysMenuButton(SysMenuButton sysMenuButton) {
		this.sysMenuButton = sysMenuButton;
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