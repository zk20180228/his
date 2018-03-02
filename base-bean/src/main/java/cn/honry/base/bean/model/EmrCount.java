package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class EmrCount extends Entity {
	/** 
	* @Fields patName : 患者姓名 
	*/ 
	private String patName;
	/** 
	* @Fields patSex : 患者性别
	*/ 
	private String patSex;
	/** 
	 * @Fields inpatientNo : 住院流水号
	 */ 
	private String inpatientNo;
	/** 
	* @Fields patAge : 患者年龄
	*/ 
	private String patAge;
	/** 
	* @Fields deptName : 科室名
	*/ 
	private String deptName;
	/** 
	 * @Fields deptCode : 科室code
	 */ 
	private String deptCode;
	/** 
	* @Fields inDate : 入院时间
	*/ 
	private Date inDate;
	/** 
	* @Fields bedName : 床号
	*/ 
	private String bedName;
	/** 
	 * @Fields lostTime : 缺写数 
	 */ 
	private Long lostTime;
	/** 
	 * @Fields overTime : 超时数
	 */ 
	private Long overTime;
	/** 
	* @Fields inDays : 在院天数
	*/ 
	private Integer inDays;
	/** 
	 * @Fields type : 病历类型
	 */ 
	private String type;
	/** 
	* @Fields times : 扣分次数
	*/ 
	private Integer times;
	/** 
	* @Fields score : 扣分数
	*/ 
	private Double score;
	
	
	
	
	/** 
	* @Fields outDate : 出院时间
	*/ 
	private Date outDate;
	/** 
	 * @Fields realTime : 实写次数
	 */ 
	private Long realTime;
	/** 
	 * @Fields requireTime : 要求次数
	 */ 
	private Long requireTime;
	/** 
	* @Fields value : 时间间隔
	*/ 
	private Integer value;
	/** 
	* @Fields unit : 时间单位：1周，2天，3时，4分，5秒
	*/ 
	private Integer unit;
	
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public Long getRealTime() {
		return realTime;
	}
	public void setRealTime(Long realTime) {
		this.realTime = realTime;
	}
	public Long getRequireTime() {
		return requireTime;
	}
	public void setRequireTime(Long requireTime) {
		this.requireTime = requireTime;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Integer getUnit() {
		return unit;
	}
	public void setUnit(Integer unit) {
		this.unit = unit;
	}
	public Long getLostTime() {
		return lostTime == null ? 0L : lostTime;
	}
	public void setLostTime(Long lostTime) {
		if (lostTime == null) {
			this.lostTime = 0L;
		}else {
			this.lostTime = lostTime;
		}
	}
	public Long getOverTime() {
		return overTime == null ? 0L : overTime;
	}
	public void setOverTime(Long overTime) {
		if (overTime == null) {
			this.overTime = 0L;
		}else {
			this.overTime = overTime;
		}
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getPatName() {
		return patName;
	}
	public void setPatName(String patName) {
		this.patName = patName;
	}
	public String getPatSex() {
		return patSex;
	}
	public void setPatSex(String patSex) {
		this.patSex = patSex;
	}
	public String getPatAge() {
		return patAge;
	}
	public void setPatAge(String patAge) {
		this.patAge = patAge;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public Integer getInDays() {
		return inDays;
	}
	public void setInDays(Integer inDays) {
		this.inDays = inDays;
	}
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	
}
