package cn.honry.statistics.bi.inpatient.peopleInHospital.vo;

import java.util.List;

public class PeopleInHospitalVo {
	
	/** 科室**/
	private String deptName;
	/**性别 **/
	private String sex;
	/** 入院来源**/
	private String sourceName;
	/** 病危**/
	private String criticalName;
	/**时间维度 **/
	private String timeChose;
	/** 在院人次**/
	private Integer hospitalPerson;
	/** 比例**/
	private Double percentage;
	
	List<PeopleInHospital2Vo> type;
	
	public String getTimeChose() {
		return timeChose;
	}
	public void setTimeChose(String timeChose) {
		this.timeChose = timeChose;
	}
	public List<PeopleInHospital2Vo> getType() {
		return type;
	}
	public void setType(List<PeopleInHospital2Vo> type) {
		this.type = type;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getCriticalName() {
		return criticalName;
	}
	public void setCriticalName(String criticalName) {
		this.criticalName = criticalName;
	}
	public Integer getHospitalPerson() {
		return hospitalPerson;
	}
	public void setHospitalPerson(Integer hospitalPerson) {
		this.hospitalPerson = hospitalPerson;
	}
	public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	
}
