package cn.honry.base.bean.model;

import java.util.Date;


/**
 * 栏目功能按钮表
 * SysMenuButton entity. @author aizhonghua
 */

public class SysMenuButton implements java.io.Serializable {

	/**id**/
	private String id;
	/**栏目编号**/
	private SysMenu sysMenu;
	/**按钮名称**/
	private String name;
	/**按钮别名**/
	private String alias;
	/**按钮描述**/
	private String description;
	/**功能状态**/
	private Integer status;
	/**按钮权限**/
	private String permission;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
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