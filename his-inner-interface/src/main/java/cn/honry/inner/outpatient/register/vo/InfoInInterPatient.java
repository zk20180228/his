package cn.honry.inner.outpatient.register.vo;

import java.util.Date;

public class InfoInInterPatient {

	
	//患者姓名
	private String name;
	//患者性别
	private Integer sex;
	//患者出生年月日
	private Date dates;
	//就诊卡号
	private String idCardNo;
	//病例号
	private String infoMedicalrecordId;
	//身份证号
	private String patientCertificatesno;
	//患者ID
	private String patientId;
	//合同单位
	private String cout;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Date getDates() {
		return dates;
	}
	public void setDates(Date dates) {
		this.dates = dates;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	
	public String getInfoMedicalrecordId() {
		return infoMedicalrecordId;
	}
	public void setInfoMedicalrecordId(String infoMedicalrecordId) {
		this.infoMedicalrecordId = infoMedicalrecordId;
	}
	public String getPatientCertificatesno() {
		return patientCertificatesno;
	}
	public void setPatientCertificatesno(String patientCertificatesno) {
		this.patientCertificatesno = patientCertificatesno;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getCout() {
		return cout;
	}
	public void setCout(String cout) {
		this.cout = cout;
	}
	
	
}
