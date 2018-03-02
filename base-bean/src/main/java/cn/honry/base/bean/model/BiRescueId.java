package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiRescueId entity. @author MyEclipse Persistence Tools
 */

public class BiRescueId implements java.io.Serializable {

	// Fields

	private String outpatientNo;
	private String medicalNo;
	private String cardNo;
	private String PName;
	private String PId;
	private String sex;
	private Double age;
	private String ageUnit;
	private String bedNo;
	private Date rescueTime;
	private String docCode;
	private String docName;
	private String PSituation;
	private String record;
	private String PWent;
	private Date createTime;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public BiRescueId() {
	}

	/** full constructor */
	public BiRescueId(String outpatientNo, String medicalNo, String cardNo,
			String PName, String PId, String sex, Double age, String ageUnit,
			String bedNo, Date rescueTime, String docCode, String docName,
			String PSituation, String record, String PWent, Date createTime,
			Date updateTime) {
		this.outpatientNo = outpatientNo;
		this.medicalNo = medicalNo;
		this.cardNo = cardNo;
		this.PName = PName;
		this.PId = PId;
		this.sex = sex;
		this.age = age;
		this.ageUnit = ageUnit;
		this.bedNo = bedNo;
		this.rescueTime = rescueTime;
		this.docCode = docCode;
		this.docName = docName;
		this.PSituation = PSituation;
		this.record = record;
		this.PWent = PWent;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	// Property accessors

	public String getOutpatientNo() {
		return this.outpatientNo;
	}

	public void setOutpatientNo(String outpatientNo) {
		this.outpatientNo = outpatientNo;
	}

	public String getMedicalNo() {
		return this.medicalNo;
	}

	public void setMedicalNo(String medicalNo) {
		this.medicalNo = medicalNo;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPName() {
		return this.PName;
	}

	public void setPName(String PName) {
		this.PName = PName;
	}

	public String getPId() {
		return this.PId;
	}

	public void setPId(String PId) {
		this.PId = PId;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Double getAge() {
		return this.age;
	}

	public void setAge(Double age) {
		this.age = age;
	}

	public String getAgeUnit() {
		return this.ageUnit;
	}

	public void setAgeUnit(String ageUnit) {
		this.ageUnit = ageUnit;
	}

	public String getBedNo() {
		return this.bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public Date getRescueTime() {
		return this.rescueTime;
	}

	public void setRescueTime(Date rescueTime) {
		this.rescueTime = rescueTime;
	}

	public String getDocCode() {
		return this.docCode;
	}

	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	public String getDocName() {
		return this.docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getPSituation() {
		return this.PSituation;
	}

	public void setPSituation(String PSituation) {
		this.PSituation = PSituation;
	}

	public String getRecord() {
		return this.record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	public String getPWent() {
		return this.PWent;
	}

	public void setPWent(String PWent) {
		this.PWent = PWent;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BiRescueId))
			return false;
		BiRescueId castOther = (BiRescueId) other;

		return ((this.getOutpatientNo() == castOther.getOutpatientNo()) || (this
				.getOutpatientNo() != null
				&& castOther.getOutpatientNo() != null && this
				.getOutpatientNo().equals(castOther.getOutpatientNo())))
				&& ((this.getMedicalNo() == castOther.getMedicalNo()) || (this
						.getMedicalNo() != null
						&& castOther.getMedicalNo() != null && this
						.getMedicalNo().equals(castOther.getMedicalNo())))
				&& ((this.getCardNo() == castOther.getCardNo()) || (this
						.getCardNo() != null && castOther.getCardNo() != null && this
						.getCardNo().equals(castOther.getCardNo())))
				&& ((this.getPName() == castOther.getPName()) || (this
						.getPName() != null && castOther.getPName() != null && this
						.getPName().equals(castOther.getPName())))
				&& ((this.getPId() == castOther.getPId()) || (this.getPId() != null
						&& castOther.getPId() != null && this.getPId().equals(
						castOther.getPId())))
				&& ((this.getSex() == castOther.getSex()) || (this.getSex() != null
						&& castOther.getSex() != null && this.getSex().equals(
						castOther.getSex())))
				&& ((this.getAge() == castOther.getAge()) || (this.getAge() != null
						&& castOther.getAge() != null && this.getAge().equals(
						castOther.getAge())))
				&& ((this.getAgeUnit() == castOther.getAgeUnit()) || (this
						.getAgeUnit() != null && castOther.getAgeUnit() != null && this
						.getAgeUnit().equals(castOther.getAgeUnit())))
				&& ((this.getBedNo() == castOther.getBedNo()) || (this
						.getBedNo() != null && castOther.getBedNo() != null && this
						.getBedNo().equals(castOther.getBedNo())))
				&& ((this.getRescueTime() == castOther.getRescueTime()) || (this
						.getRescueTime() != null
						&& castOther.getRescueTime() != null && this
						.getRescueTime().equals(castOther.getRescueTime())))
				&& ((this.getDocCode() == castOther.getDocCode()) || (this
						.getDocCode() != null && castOther.getDocCode() != null && this
						.getDocCode().equals(castOther.getDocCode())))
				&& ((this.getDocName() == castOther.getDocName()) || (this
						.getDocName() != null && castOther.getDocName() != null && this
						.getDocName().equals(castOther.getDocName())))
				&& ((this.getPSituation() == castOther.getPSituation()) || (this
						.getPSituation() != null
						&& castOther.getPSituation() != null && this
						.getPSituation().equals(castOther.getPSituation())))
				&& ((this.getRecord() == castOther.getRecord()) || (this
						.getRecord() != null && castOther.getRecord() != null && this
						.getRecord().equals(castOther.getRecord())))
				&& ((this.getPWent() == castOther.getPWent()) || (this
						.getPWent() != null && castOther.getPWent() != null && this
						.getPWent().equals(castOther.getPWent())))
				&& ((this.getCreateTime() == castOther.getCreateTime()) || (this
						.getCreateTime() != null
						&& castOther.getCreateTime() != null && this
						.getCreateTime().equals(castOther.getCreateTime())))
				&& ((this.getUpdateTime() == castOther.getUpdateTime()) || (this
						.getUpdateTime() != null
						&& castOther.getUpdateTime() != null && this
						.getUpdateTime().equals(castOther.getUpdateTime())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getOutpatientNo() == null ? 0 : this.getOutpatientNo()
						.hashCode());
		result = 37 * result
				+ (getMedicalNo() == null ? 0 : this.getMedicalNo().hashCode());
		result = 37 * result
				+ (getCardNo() == null ? 0 : this.getCardNo().hashCode());
		result = 37 * result
				+ (getPName() == null ? 0 : this.getPName().hashCode());
		result = 37 * result
				+ (getPId() == null ? 0 : this.getPId().hashCode());
		result = 37 * result
				+ (getSex() == null ? 0 : this.getSex().hashCode());
		result = 37 * result
				+ (getAge() == null ? 0 : this.getAge().hashCode());
		result = 37 * result
				+ (getAgeUnit() == null ? 0 : this.getAgeUnit().hashCode());
		result = 37 * result
				+ (getBedNo() == null ? 0 : this.getBedNo().hashCode());
		result = 37
				* result
				+ (getRescueTime() == null ? 0 : this.getRescueTime()
						.hashCode());
		result = 37 * result
				+ (getDocCode() == null ? 0 : this.getDocCode().hashCode());
		result = 37 * result
				+ (getDocName() == null ? 0 : this.getDocName().hashCode());
		result = 37
				* result
				+ (getPSituation() == null ? 0 : this.getPSituation()
						.hashCode());
		result = 37 * result
				+ (getRecord() == null ? 0 : this.getRecord().hashCode());
		result = 37 * result
				+ (getPWent() == null ? 0 : this.getPWent().hashCode());
		result = 37
				* result
				+ (getCreateTime() == null ? 0 : this.getCreateTime()
						.hashCode());
		result = 37
				* result
				+ (getUpdateTime() == null ? 0 : this.getUpdateTime()
						.hashCode());
		return result;
	}

}