package cn.honry.base.bean.model;

import java.util.Date;

/**
 * 操作日志表
 * TSysUseroperation entity. @author MyEclipse Persistence Tools
 */

public class SysUseroperation implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**id**/
	private String id;
	/**操作用户**/
	private User user;
	/**操作行为**/
	private String action;
	/**操作部门**/
	private String deptId;
	/**操作栏目**/
	private String menuId;
	/**操作sql**/
	private String sql;
	/**操作表**/
	private String table;
	/**目标编号**/
	private String targetId;
	/**操作时间**/
	private Date time;
	/**操作备注**/
	private String description;
	/**备用字段1**/
	private String logKey1;
	/**备用字段2**/
	private String logKey2;
	/**备用字段3**/
	private String logKey3;
	/**备用字段4**/
	private String logKey4;
	/**备用字段5**/
	private String logKey5;
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
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLogKey1() {
		return logKey1;
	}
	public void setLogKey1(String logKey1) {
		this.logKey1 = logKey1;
	}
	public String getLogKey2() {
		return logKey2;
	}
	public void setLogKey2(String logKey2) {
		this.logKey2 = logKey2;
	}
	public String getLogKey3() {
		return logKey3;
	}
	public void setLogKey3(String logKey3) {
		this.logKey3 = logKey3;
	}
	public String getLogKey4() {
		return logKey4;
	}
	public void setLogKey4(String logKey4) {
		this.logKey4 = logKey4;
	}
	public String getLogKey5() {
		return logKey5;
	}
	public void setLogKey5(String logKey5) {
		this.logKey5 = logKey5;
	}
	
}