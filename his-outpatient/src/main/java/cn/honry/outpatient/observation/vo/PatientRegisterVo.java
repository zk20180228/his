package cn.honry.outpatient.observation.vo;

import java.io.Serializable;

/**
 * 
 * <p>根据就诊卡号查询患者挂号信息</p>
 * @Author: zhangkui
 * @CreateDate: 2017年11月16日 下午5:23:19 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年11月16日 下午5:23:19 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class PatientRegisterVo implements Serializable {


	private static final long serialVersionUID = 7630954140253013480L;
	
	private String patientName;//姓名PATIENT_NAME
	private String patientSex;//性别PATIENT_SEX
	private String clinicCode;//门诊号CLINIC_CODE
	private String dept;//挂号科室DEPT_NAME
	private String expxrt;//挂号专家DOCT_NAME
	private String type;// 挂号类型EMERGENCY_FLAG
	private String regDate;//挂号日期REG_DATE
	
	
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPatientSex() {
		return patientSex;
	}
	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getExpxrt() {
		return expxrt;
	}
	public void setExpxrt(String expxrt) {
		this.expxrt = expxrt;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	
	
	
	
}
