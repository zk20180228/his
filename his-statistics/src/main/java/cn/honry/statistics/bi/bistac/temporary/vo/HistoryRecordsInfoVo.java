package cn.honry.statistics.bi.bistac.temporary.vo;

public class HistoryRecordsInfoVo {
	/**门诊号或住院号**/
	private String clinicNo;
	/**主诉**/
	private String mainDesc;
	/**过敏史**/
	private String allergicHistory;
	/**家族遗传史**/
	private String heredityHis;
	/**现病史**/
	private String presentIllness;
	/**体温**/
	private Double temperature;
	/**脉搏**/
	private Double pulse;
	/**呼吸**/
	private Double breathing;
	/**血压**/
	private Double bloodPressure;
	/**体格检查**/
	private String physicalExamination;
	/**校验检查**/
	private String checkResult;
	/**诊断检查**/
	private String diagnose1;
	/**医嘱建议**/
	private String advice;
	/**病史和特征**/
	private String historySpecil;
	
	/**getters and setters**/
	public String getClinicNo() {
		return clinicNo;
	}
	public void setClinicNo(String clinicNo) {
		this.clinicNo = clinicNo;
	}
	public String getMainDesc() {
		return mainDesc;
	}
	public void setMainDesc(String mainDesc) {
		this.mainDesc = mainDesc;
	}
	public String getAllergicHistory() {
		return allergicHistory;
	}
	public void setAllergicHistory(String allergicHistory) {
		this.allergicHistory = allergicHistory;
	}
	public String getHeredityHis() {
		return heredityHis;
	}
	public void setHeredityHis(String heredityHis) {
		this.heredityHis = heredityHis;
	}
	public String getPresentIllness() {
		return presentIllness;
	}
	public void setPresentIllness(String presentIllness) {
		this.presentIllness = presentIllness;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getPulse() {
		return pulse;
	}
	public void setPulse(Double pulse) {
		this.pulse = pulse;
	}
	public Double getBreathing() {
		return breathing;
	}
	public void setBreathing(Double breathing) {
		this.breathing = breathing;
	}
	public Double getBloodPressure() {
		return bloodPressure;
	}
	public void setBloodPressure(Double bloodPressure) {
		this.bloodPressure = bloodPressure;
	}
	public String getPhysicalExamination() {
		return physicalExamination;
	}
	public void setPhysicalExamination(String physicalExamination) {
		this.physicalExamination = physicalExamination;
	}
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	public String getDiagnose1() {
		return diagnose1;
	}
	public void setDiagnose1(String diagnose1) {
		this.diagnose1 = diagnose1;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	public String getHistorySpecil() {
		return historySpecil;
	}
	public void setHistorySpecil(String historySpecil) {
		this.historySpecil = historySpecil;
	}
	
}

