package cn.honry.base.bean.model;

import java.util.Date;

/**
 * TDepartment entity. @author MyEclipse Persistence Tools
 */

public class SysDeptRelativity implements java.io.Serializable {

	/** 编号 **/
	private String id;
	/** 部门编号 **/
	private String dept;
	/** 部门分类 **/
	private String deptType;
	/** 相关部门 **/
	private String refdept;
	/** 相关部门分类 **/
	private String refdeptType;
	/** 备注 **/
	private String remark;
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
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getDeptType() {
		return deptType;
	}
	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}
	public String getRefdept() {
		return refdept;
	}
	public void setRefdept(String refdept) {
		this.refdept = refdept;
	}
	public String getRefdeptType() {
		return refdeptType;
	}
	public void setRefdeptType(String refdeptType) {
		this.refdeptType = refdeptType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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