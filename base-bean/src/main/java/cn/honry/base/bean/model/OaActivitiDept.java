package cn.honry.base.bean.model;

import java.util.Date;

/**
 * 工作流科室
 * @author zpty
 * Date:2017/8/13 15:00
 */
public class OaActivitiDept{
	/**唯一编号(主键)**/
	private String id;
	/** 科室编号 **/
	private String deptCode;
	/** 名称 **/
	private String deptName;
	/**父级code*/
	private String parentCode;
	/**父级路径*/
	private String parentUppath;
	/**科室状态0：启用 1：停用**/
	private Integer deptType=0;
	/**排序**/
	private Integer deptOrder;
	/**建立人员**/
	private String createUser;
	/**建立部门**/
	private String createDept;
	/**建立时间**/
	private Date createTime;
	/**修改人员**/
	private String updateUser;
	/**修改时间**/
	private Date updateTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getParentUppath() {
		return parentUppath;
	}
	public void setParentUppath(String parentUppath) {
		this.parentUppath = parentUppath;
	}
	public Integer getDeptType() {
		return deptType;
	}
	public void setDeptType(Integer deptType) {
		this.deptType = deptType;
	}
	public Integer getDeptOrder() {
		return deptOrder;
	}
	public void setDeptOrder(Integer deptOrder) {
		this.deptOrder = deptOrder;
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
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	

}