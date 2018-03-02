package cn.honry.statistics.bi.inpatient.hospitalizationInformation.vo;


public class HospitalizationInformationVo {
	/** 入院时间**/
	private String inpatientTime;
	/** 年龄(根据出生日期处理)**/
	private String reportBirthday;
	/** 地域(根据家庭住址)**/
	private String home;
	/** 患者状态**/
	private String patientstatus;
	/** 峰值 **/
	private Integer peakValue;
//	/** 病种**/
//	private String diseaseType;
	/** 入院来源**/
	private String insource;
	/** 人次**/
	private Integer passengers;
	/** 科室**/
	private String deptName;
	
	
	
	
	public String getInsource() {
		return insource;
	}
	public void setInsource(String insource) {
		this.insource = insource;
	}
	public String getInpatientTime() {
		return inpatientTime;
	}
	public void setInpatientTime(String inpatientTime) {
		this.inpatientTime = inpatientTime;
	}
	public String getReportBirthday() {
		return reportBirthday;
	}
	public void setReportBirthday(String reportBirthday) {
		this.reportBirthday = reportBirthday;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public String getPatientstatus() {
		return patientstatus;
	}
	public void setPatientstatus(String patientstatus) {
		this.patientstatus = patientstatus;
	}
	public Integer getPeakValue() {
		return peakValue;
	}
	public void setPeakValue(Integer peakValue) {
		this.peakValue = peakValue;
	}
	public Integer getPassengers() {
		return passengers;
	}
	public void setPassengers(Integer passengers) {
		this.passengers = passengers;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	
	
	
	
}
