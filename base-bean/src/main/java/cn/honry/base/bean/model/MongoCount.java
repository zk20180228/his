package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class MongoCount extends Entity {
	/**栏目别名**/
	private String munyType;
	/**开始时间**/
	private Date startTime;
	/**结束时间**/
	private Date endTime;
	/**执行方式  1 - SQL；2 -- 大数据；3-间接**/
	private Integer executeWayY;
	/**状态（年）   1开启 0 关闭**/
	private Integer stateY = 1;
	/**执行SQL**/
	private String executeSQLY;
	/**按月执行时的执行方式**/
	private Integer executeWayM;
	/**按天执行时的执行方式**/
	private Integer executeWayD;
	/**执行时间为按月执行,执行方式为SQL时有值**/
	private String executeSQLM;
	/**执行时间为按天执行，执行方式为SQL**/
	private String executeSQLD;
	/**默认执行方式 1 日；2 月； 3年；（备用字段）**/
	private String defaultTime;
	/**按月执行-状态 1 开启0关闭**/
	private Integer stateM = 1;
	/**按日执行-状态 1 开启0关闭**/
	private Integer stateD = 1;
	/**栏目开启或关闭  1开启 0 关闭**/
	private Integer state = 1;
	/**栏目名称  虚拟字段***/
	private String menuTypeName;
	public String getMunyType() {
		return munyType;
	}
	public void setMunyType(String munyType) {
		this.munyType = munyType;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getExecuteWayY() {
		return executeWayY;
	}
	public void setExecuteWayY(Integer executeWayY) {
		this.executeWayY = executeWayY;
	}
	public String getExecuteSQLY() {
		return executeSQLY;
	}
	public void setExecuteSQLY(String executeSQLY) {
		this.executeSQLY = executeSQLY;
	}
	public Integer getExecuteWayM() {
		return executeWayM;
	}
	public void setExecuteWayM(Integer executeWayM) {
		this.executeWayM = executeWayM;
	}
	public Integer getExecuteWayD() {
		return executeWayD;
	}
	public void setExecuteWayD(Integer executeWayD) {
		this.executeWayD = executeWayD;
	}
	public String getExecuteSQLM() {
		return executeSQLM;
	}
	public void setExecuteSQLM(String executeSQLM) {
		this.executeSQLM = executeSQLM;
	}
	public String getExecuteSQLD() {
		return executeSQLD;
	}
	public void setExecuteSQLD(String executeSQLD) {
		this.executeSQLD = executeSQLD;
	}
	public Integer getStateY() {
		return stateY;
	}
	public void setStateY(Integer stateY) {
		this.stateY = stateY;
	}
	public String getDefaultTime() {
		return defaultTime;
	}
	public void setDefaultTime(String defaultTime) {
		this.defaultTime = defaultTime;
	}
	public Integer getStateM() {
		return stateM;
	}
	public void setStateM(Integer stateM) {
		this.stateM = stateM;
	}
	public Integer getStateD() {
		return stateD;
	}
	public void setStateD(Integer stateD) {
		this.stateD = stateD;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getMenuTypeName() {
		return menuTypeName;
	}
	public void setMenuTypeName(String menuTypeName) {
		this.menuTypeName = menuTypeName;
	}
}
