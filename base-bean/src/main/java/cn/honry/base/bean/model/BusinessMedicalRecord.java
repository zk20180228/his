package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 电子病历模版表
 * @author liudelin
 * Date:2015/7/13 15:34
 */
public class BusinessMedicalRecord extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**科室编码**/
	private String  deptCode;
	/**病历分类**/
	private Integer recordType;
	/**医生编码**/
	private String docCode;
	/**主诉**/
	private String maindesc;
	/**过敏史**/
	private String allergichistory;
	/**家族遗传史**/
	private String heredityHis;
	/**病史和特征**/
	private String historyspecil;
	/**现病史**/
	private String presentillness;
	/**体温**/
	private Double temperature;
	/**脉搏**/
	private Double pulse;
	/**呼吸**/
	private Double breathing;
	/**血压**/
	private String bloodPressure;
	/**体格检查**/
	private String physicalExamination;
	/**检查检验结果**/
	private String checkresult;
	/**诊断1**/
	private String diagnose1;
	/**诊断2**/
	private String diagnose2;
	/**诊断3**/
	private String diagnose3;
	/**诊断4**/
	private String diagnose4;
	/**医嘱建议**/
	private String advice;
	/**备注**/
	private String remark;
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Integer getRecordType() {
		return recordType;
	}
	public void setRecordType(Integer recordType) {
		this.recordType = recordType;
	}
	public String getDocCode() {
		return docCode;
	}
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}
	public String getMaindesc() {
		return maindesc;
	}
	public void setMaindesc(String maindesc) {
		this.maindesc = maindesc;
	}
	public String getAllergichistory() {
		return allergichistory;
	}
	public void setAllergichistory(String allergichistory) {
		this.allergichistory = allergichistory;
	}
	public String getHeredityHis() {
		return heredityHis;
	}
	public void setHeredityHis(String heredityHis) {
		this.heredityHis = heredityHis;
	}
	public String getHistoryspecil() {
		return historyspecil;
	}
	public void setHistoryspecil(String historyspecil) {
		this.historyspecil = historyspecil;
	}
	public String getPresentillness() {
		return presentillness;
	}
	public void setPresentillness(String presentillness) {
		this.presentillness = presentillness;
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
	public String getBloodPressure() {
		return bloodPressure;
	}
	public void setBloodPressure(String bloodPressure) {
		this.bloodPressure = bloodPressure;
	}
	public String getPhysicalExamination() {
		return physicalExamination;
	}
	public void setPhysicalExamination(String physicalExamination) {
		this.physicalExamination = physicalExamination;
	}
	public String getCheckresult() {
		return checkresult;
	}
	public void setCheckresult(String checkresult) {
		this.checkresult = checkresult;
	}
	public String getDiagnose1() {
		return diagnose1;
	}
	public void setDiagnose1(String diagnose1) {
		this.diagnose1 = diagnose1;
	}
	public String getDiagnose2() {
		return diagnose2;
	}
	public void setDiagnose2(String diagnose2) {
		this.diagnose2 = diagnose2;
	}
	public String getDiagnose3() {
		return diagnose3;
	}
	public void setDiagnose3(String diagnose3) {
		this.diagnose3 = diagnose3;
	}
	public String getDiagnose4() {
		return diagnose4;
	}
	public void setDiagnose4(String diagnose4) {
		this.diagnose4 = diagnose4;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
