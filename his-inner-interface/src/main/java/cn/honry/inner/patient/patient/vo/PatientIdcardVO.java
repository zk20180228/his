package cn.honry.inner.patient.patient.vo;

import java.util.Date;

public class PatientIdcardVO {
	/** GH 2016年10月13日 生日 查询条件**/
	private String brithDayCondition;
	
	
	/**唯一编号(主键)**/
	private String id;
	/**  编号 **/
	private String businessContractunit;
	/** 患者姓名  **/
	private String patientName;
	/** 性别  **/
	private Integer patientSex;
	/** 电话  **/
	private String patientPhone;
	/** 医保手册号  **/
	private String patientHandbook;
	/** 电子邮箱  **/
	private String patientEmail;
	/** 联系人  **/
	private String patientLinkman;
	/** 联系人关系  **/
	private String patientLinkrelation;
	/** 新添加字段 病历号  **/
	private String medicalrecordId;
	
	//zpty20151223添加
	/** 出生日期  **/
	private Date patientBirthday;
	/** 家庭地址  **/
	private String patientAddress;
	/**家庭地址具体省市县**/
	private String patientCity;	
	/**证件类型   **/
	private String patientCertificatestype;
	/** 证件号码  **/
	private String patientCertificatesno;
	
	//就诊卡id
	private String idcardId;
	/**卡号*/
	private String idcardNo;
	/**卡类型1:磁卡2;IC卡3保障卡，从编码表里读取*/
	private String idcardType;
	/**建卡时间*/
	private Date idcardCreatetime;
	/**操作人员*/
	private String idcardOperator;
	/**备注**/
	private String idcardRemark;
	
	//存放string类型的日期
	private String createTimeStr;
	private String birthdayStr;
	//wujiao2016-3-23
	private String name;
	private Integer idcardStatus;//状态
	
	
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getBirthdayStr() {
		return birthdayStr;
	}
	public void setBirthdayStr(String birthdayStr) {
		this.birthdayStr = birthdayStr;
	}
	public Integer getIdcardStatus() {
		return idcardStatus;
	}
	public void setIdcardStatus(Integer idcardStatus) {
		this.idcardStatus = idcardStatus;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBrithDayCondition() {
		return brithDayCondition;
	}
	public void setBrithDayCondition(String brithDayCondition) {
		this.brithDayCondition = brithDayCondition;
	}
	public String getBusinessContractunit() {
		return businessContractunit;
	}
	public void setBusinessContractunit(String businessContractunit) {
		this.businessContractunit = businessContractunit;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	
	public Integer getPatientSex() {
		return patientSex;
	}
	public void setPatientSex(Integer patientSex) {
		this.patientSex = patientSex;
	}
	public String getPatientPhone() {
		return patientPhone;
	}
	public void setPatientPhone(String patientPhone) {
		this.patientPhone = patientPhone;
	}
	public String getPatientHandbook() {
		return patientHandbook;
	}
	public void setPatientHandbook(String patientHandbook) {
		this.patientHandbook = patientHandbook;
	}
	public String getPatientEmail() {
		return patientEmail;
	}
	public void setPatientEmail(String patientEmail) {
		this.patientEmail = patientEmail;
	}
	public String getPatientLinkman() {
		return patientLinkman;
	}
	public void setPatientLinkman(String patientLinkman) {
		this.patientLinkman = patientLinkman;
	}
	public String getPatientLinkrelation() {
		return patientLinkrelation;
	}
	public void setPatientLinkrelation(String patientLinkrelation) {
		this.patientLinkrelation = patientLinkrelation;
	}
	public String getIdcardId() {
		return idcardId;
	}
	public void setIdcardId(String idcardId) {
		this.idcardId = idcardId;
	}
	public String getIdcardNo() {
		return idcardNo;
	}
	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	public String getIdcardType() {
		return idcardType;
	}
	public void setIdcardType(String idcardType) {
		this.idcardType = idcardType;
	}
	public String getIdcardOperator() {
		return idcardOperator;
	}
	public void setIdcardOperator(String idcardOperator) {
		this.idcardOperator = idcardOperator;
	}
	public String getIdcardRemark() {
		return idcardRemark;
	}
	public void setIdcardRemark(String idcardRemark) {
		this.idcardRemark = idcardRemark;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	
	
	public Date getPatientBirthday() {
		return patientBirthday;
	}
	public void setPatientBirthday(Date patientBirthday) {
		this.patientBirthday = patientBirthday;
	}
	public Date getIdcardCreatetime() {
		return idcardCreatetime;
	}
	public void setIdcardCreatetime(Date idcardCreatetime) {
		this.idcardCreatetime = idcardCreatetime;
	}
	public String getPatientAddress() {
		return patientAddress;
	}
	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}
	public String getPatientCity() {
		return patientCity;
	}
	public void setPatientCity(String patientCity) {
		this.patientCity = patientCity;
	}
	public String getPatientCertificatestype() {
		return patientCertificatestype;
	}
	public void setPatientCertificatestype(String patientCertificatestype) {
		this.patientCertificatestype = patientCertificatestype;
	}
	public String getPatientCertificatesno() {
		return patientCertificatesno;
	}
	public void setPatientCertificatesno(String patientCertificatesno) {
		this.patientCertificatesno = patientCertificatesno;
	}
	
	
}
