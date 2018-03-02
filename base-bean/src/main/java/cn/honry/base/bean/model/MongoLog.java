package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class MongoLog extends Entity {
	/**栏目别名**/
	private String menuType;
	/**开始时间**/
	private Date startTime;
	/**结束时间**/
	private Date endTime;
	/**总条数**/
	private Integer totalNum;
	/**状态：1-成功；0-失败**/
	private Integer state;
	/**计算开始时间**/
	private Date countStartTime;
	/**计算结束时间**/
	private Date countEndTime;
	
	
	
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
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
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getCountStartTime() {
		return countStartTime;
	}
	public void setCountStartTime(Date countStartTime) {
		this.countStartTime = countStartTime;
	}
	public Date getCountEndTime() {
		return countEndTime;
	}
	public void setCountEndTime(Date countEndTime) {
		this.countEndTime = countEndTime;
	}
	
}
