package cn.honry.base.bean.model;

import java.util.Date;


/**
 * 用户科室关联表
 * UserLoginDept  
 * @author wujiao
 */
public class UserLoginDept implements java.io.Serializable{
	
	
	/**id**/
	private String id;
	/**栏目编号**/
	private User userId;
	/**角色编号**/
	private SysDepartment deptId;
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
	public User getUserId() {
		return userId;
	}
	public void setUserId(User userId) {
		this.userId = userId;
	}
	public SysDepartment getDeptId() {
		return deptId;
	}
	public void setDeptId(SysDepartment deptId) {
		this.deptId = deptId;
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
