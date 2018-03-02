package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 系统用户-角色关联表
 * SysUserRoleRelation entity. @author aizhonghua
 */

public class SysUserRoleRelation extends Entity implements java.io.Serializable {
	
	/** id **/
	private String id;
	/** 角色编号 **/
	private SysRole sysRole;
	/** 用户编号 **/
	private User sysUser;
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
	public User getSysUser() {
		return sysUser;
	}
	public void setSysUser(User sysUser) {
		this.sysUser = sysUser;
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