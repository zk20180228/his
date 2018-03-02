package cn.honry.statistics.bi.inpatient.dischargePerson.vo;

public class DischargePersonVo {
	
	/** 住院人次 **/
	private Integer hospitalizationTime;
	
	/** 出院人次 **/
	private Integer dischargePerson;
	
	/** 出院人次所占百分比 **/
	private String percentage;
	
	private String deptName;

	public Integer getHospitalizationTime() {
		return hospitalizationTime;
	}

	public void setHospitalizationTime(Integer hospitalizationTime) {
		this.hospitalizationTime = hospitalizationTime;
	}

	public Integer getDischargePerson() {
		return dischargePerson;
	}

	public void setDischargePerson(Integer dischargePerson) {
		this.dischargePerson = dischargePerson;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	
	
	

}
