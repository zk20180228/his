package cn.honry.base.bean.model;

import java.math.BigDecimal;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * BiBaseContractunit entity. @author MyEclipse Persistence Tools
 */

public class HandingStatus {
	private String id;
	/**栏目别名*/
	private String menuAlias;
	/**类型 3年2月1日**/
	private String type;
	/**开始时间**/
	private String begainTime;
	/**结束时间**/
	private String endTime;
	/**状态 ，0执行失败，1执行成功，2执行中**/
	private Integer state;
	/**处理方式      1手动，0自动*/
	private Integer handWay;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getBegainTime() {
		return begainTime;
	}
	public void setBegainTime(String begainTime) {
		this.begainTime = begainTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getHandWay() {
		return handWay;
	}
	public void setHandWay(Integer handWay) {
		this.handWay = handWay;
	}
}