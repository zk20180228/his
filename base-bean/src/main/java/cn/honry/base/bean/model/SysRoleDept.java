package cn.honry.base.bean.model;

import java.util.Date;

/**
 * 角色-科室关联表
 * SysRoleMenuRelation entity. @author MyEclipse Persistence Tools
 */
public class SysRoleDept implements java.io.Serializable{
	
	/**id**/
	private String id;
	/**栏目编号**/
	private SysRole roleId;
	/**角色编号**/
	private SysDepartment deptId;
	/**备注**/
	private String  rdRemark;
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
	public SysRole getRoleId() {
		return roleId;
	}
	public void setRoleId(SysRole roleId) {
		this.roleId = roleId;
	}
	public SysDepartment getDeptId() {
		return deptId;
	}
	public void setDeptId(SysDepartment deptId) {
		this.deptId = deptId;
	}
	public String getRdRemark() {
		return rdRemark;
	}
	public void setRdRemark(String rdRemark) {
		this.rdRemark = rdRemark;
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
