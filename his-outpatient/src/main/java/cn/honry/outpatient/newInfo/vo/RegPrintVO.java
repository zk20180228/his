package cn.honry.outpatient.newInfo.vo;

import java.util.Date;

public class RegPrintVO {
	private String midicalrecordId;//病历号
	private String  createhos ;//医院名称
	private String patientName;//患者姓名
	private Double regFee = 0.0;//挂号费
	private Double chckFee = 0.0;//检查费
	private String doctName;//医生姓名
	private Date regDate;//挂号日期
	private String clinicCode;//门诊号
	private String schemaNo;//排序号
	private Integer orderNo = 0;//每日序号
	private String noonCodeNmae;//午别
	private String deptAdress;//科室位置
	private String deptName;
	
	
	
	
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getMidicalrecordId() {
		return midicalrecordId;
	}
	public void setMidicalrecordId(String midicalrecordId) {
		this.midicalrecordId = midicalrecordId;
	}
	public String getCreatehos() {
		return createhos;
	}
	public void setCreatehos(String createhos) {
		this.createhos = createhos;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public Double getRegFee() {
		return regFee;
	}
	public void setRegFee(Double regFee) {
		this.regFee = regFee;
	}
	public Double getChckFee() {
		return chckFee;
	}
	public void setChckFee(Double chckFee) {
		this.chckFee = chckFee;
	}
	public String getDoctName() {
		return doctName;
	}
	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public String getSchemaNo() {
		return schemaNo;
	}
	public void setSchemaNo(String schemaNo) {
		this.schemaNo = schemaNo;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public String getNoonCodeNmae() {
		return noonCodeNmae;
	}
	public void setNoonCodeNmae(String noonCodeNmae) {
		this.noonCodeNmae = noonCodeNmae;
	}
	public String getDeptAdress() {
		return deptAdress;
	}
	public void setDeptAdress(String deptAdress) {
		this.deptAdress = deptAdress;
	}
	
}
