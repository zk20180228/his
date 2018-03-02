package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;


@SuppressWarnings("serial")
public class PatientIdcard extends Entity implements java.io.Serializable {

	// Fields
	/**患者编号*/
	private Patient patient;
	/**卡号*/
	private String idcardNo;
	/**卡类型1:磁卡2;IC卡3保障卡，从编码表里读取*/
	private String idcardType;
	/**病历号*/
	//private String medicalrecordId;
	/**建卡时间*/
	private Date idcardCreatetime;
	/**金额*//*
	private Double idcardFee;*/
	/**操作人员*/
	private String idcardOperator;
	/**备注**/
	private String idcardRemark;
	
	/**就诊卡状态 zpty20160324增加 **/
	private Integer idcardStatus=1;
	
	/**使用新的门诊账户实体zpty20160408*/
	private OutpatientAccount account;
	
	/**  2017年9月23日zpty对比新数据与历史数据,新增冗余字段**/
	/**  所属医院 **/
	private String hospitalId;
	/**  所属院区 **/
	private String areaCode;
	
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
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

	/*public String getMedicalrecordId() {
		return medicalrecordId;
	}

	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
*/
	public Date getIdcardCreatetime() {
		return idcardCreatetime;
	}

	public void setIdcardCreatetime(Date idcardCreatetime) {
		this.idcardCreatetime = idcardCreatetime;
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


	public OutpatientAccount getAccount() {
		return account;
	}

	public void setAccount(OutpatientAccount account) {
		this.account = account;
	}

	public Integer getIdcardStatus() {
		return idcardStatus;
	}

	public void setIdcardStatus(Integer idcardStatus) {
		this.idcardStatus = idcardStatus;
	}

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


	
	
}
	