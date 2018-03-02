package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;
/**
 * 临床科室正台安排信息表
 * @author liuyuanyuan
 * @date 2015-08-17
 *
 */
public class BusinessArrangeconsole extends Entity{
	/** 手术科室代码  */
	private String opsDpcd;
	/** 临床科室代码  */
	private String deptCode;
	/** 星期 (1,2,3,4,5,6,7)  */
	private Integer week;
	/** 正台数量  */
	private Integer limitedqty;
	/** 使用数量  */
	private Integer usedQty;
	
	
	public String getOpsDpcd() {
		return opsDpcd;
	}
	public void setOpsDpcd(String opsDpcd) {
		this.opsDpcd = opsDpcd;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Integer getWeek() {
		return week;
	}
	public void setWeek(Integer week) {
		this.week = week;
	}
	public Integer getLimitedqty() {
		return limitedqty;
	}
	public void setLimitedqty(Integer limitedqty) {
		this.limitedqty = limitedqty;
	}
	public Integer getUsedQty() {
		return usedQty;
	}
	public void setUsedQty(Integer usedQty) {
		this.usedQty = usedQty;
	}
	
	   
}
