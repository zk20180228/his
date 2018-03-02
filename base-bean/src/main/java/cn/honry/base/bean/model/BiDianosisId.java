package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiDianosisId entity. @author MyEclipse Persistence Tools
 */

public class BiDianosisId implements java.io.Serializable {

	// Fields

	private String outpatientNo;
	private String medicalNo;
	private String cardNo;
	private String inpatientNo;
	private String diagCode;
	private String diagName;
	private Date diagTime;
	private String diagTypeCode;
	private String diagTypeName;
	private String docCode;
	private String docName;
	private String deptCode;
	private String deptName;
	private Boolean isMain;
	private String isSuspected;
	private String icd9Code;
	private String icd9Name;
	private String icd10Code;
	private String icd10Name;
	private String diagGradCode;
	private String diagGradName;
	private Date createDate;
	private Date updateDate;

	// Constructors

	/** default constructor */
	public BiDianosisId() {
	}

	/** full constructor */
	public BiDianosisId(String outpatientNo, String medicalNo, String cardNo,
			String inpatientNo, String diagCode, String diagName,
			Date diagTime, String diagTypeCode, String diagTypeName,
			String docCode, String docName, String deptCode, String deptName,
			Boolean isMain, String isSuspected, String icd9Code,
			String icd9Name, String icd10Code, String icd10Name,
			String diagGradCode, String diagGradName, Date createDate,
			Date updateDate) {
		this.outpatientNo = outpatientNo;
		this.medicalNo = medicalNo;
		this.cardNo = cardNo;
		this.inpatientNo = inpatientNo;
		this.diagCode = diagCode;
		this.diagName = diagName;
		this.diagTime = diagTime;
		this.diagTypeCode = diagTypeCode;
		this.diagTypeName = diagTypeName;
		this.docCode = docCode;
		this.docName = docName;
		this.deptCode = deptCode;
		this.deptName = deptName;
		this.isMain = isMain;
		this.isSuspected = isSuspected;
		this.icd9Code = icd9Code;
		this.icd9Name = icd9Name;
		this.icd10Code = icd10Code;
		this.icd10Name = icd10Name;
		this.diagGradCode = diagGradCode;
		this.diagGradName = diagGradName;
		this.createDate = createDate;
		this.updateDate = updateDate;
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

	public String getInpatientNo() {
		return this.inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getDiagCode() {
		return this.diagCode;
	}

	public void setDiagCode(String diagCode) {
		this.diagCode = diagCode;
	}

	public String getDiagName() {
		return this.diagName;
	}

	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}

	public Date getDiagTime() {
		return this.diagTime;
	}

	public void setDiagTime(Date diagTime) {
		this.diagTime = diagTime;
	}

	public String getDiagTypeCode() {
		return this.diagTypeCode;
	}

	public void setDiagTypeCode(String diagTypeCode) {
		this.diagTypeCode = diagTypeCode;
	}

	public String getDiagTypeName() {
		return this.diagTypeName;
	}

	public void setDiagTypeName(String diagTypeName) {
		this.diagTypeName = diagTypeName;
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

	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Boolean getIsMain() {
		return this.isMain;
	}

	public void setIsMain(Boolean isMain) {
		this.isMain = isMain;
	}

	public String getIsSuspected() {
		return this.isSuspected;
	}

	public void setIsSuspected(String isSuspected) {
		this.isSuspected = isSuspected;
	}

	public String getIcd9Code() {
		return this.icd9Code;
	}

	public void setIcd9Code(String icd9Code) {
		this.icd9Code = icd9Code;
	}

	public String getIcd9Name() {
		return this.icd9Name;
	}

	public void setIcd9Name(String icd9Name) {
		this.icd9Name = icd9Name;
	}

	public String getIcd10Code() {
		return this.icd10Code;
	}

	public void setIcd10Code(String icd10Code) {
		this.icd10Code = icd10Code;
	}

	public String getIcd10Name() {
		return this.icd10Name;
	}

	public void setIcd10Name(String icd10Name) {
		this.icd10Name = icd10Name;
	}

	public String getDiagGradCode() {
		return this.diagGradCode;
	}

	public void setDiagGradCode(String diagGradCode) {
		this.diagGradCode = diagGradCode;
	}

	public String getDiagGradName() {
		return this.diagGradName;
	}

	public void setDiagGradName(String diagGradName) {
		this.diagGradName = diagGradName;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BiDianosisId))
			return false;
		BiDianosisId castOther = (BiDianosisId) other;

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
				&& ((this.getInpatientNo() == castOther.getInpatientNo()) || (this
						.getInpatientNo() != null
						&& castOther.getInpatientNo() != null && this
						.getInpatientNo().equals(castOther.getInpatientNo())))
				&& ((this.getDiagCode() == castOther.getDiagCode()) || (this
						.getDiagCode() != null
						&& castOther.getDiagCode() != null && this
						.getDiagCode().equals(castOther.getDiagCode())))
				&& ((this.getDiagName() == castOther.getDiagName()) || (this
						.getDiagName() != null
						&& castOther.getDiagName() != null && this
						.getDiagName().equals(castOther.getDiagName())))
				&& ((this.getDiagTime() == castOther.getDiagTime()) || (this
						.getDiagTime() != null
						&& castOther.getDiagTime() != null && this
						.getDiagTime().equals(castOther.getDiagTime())))
				&& ((this.getDiagTypeCode() == castOther.getDiagTypeCode()) || (this
						.getDiagTypeCode() != null
						&& castOther.getDiagTypeCode() != null && this
						.getDiagTypeCode().equals(castOther.getDiagTypeCode())))
				&& ((this.getDiagTypeName() == castOther.getDiagTypeName()) || (this
						.getDiagTypeName() != null
						&& castOther.getDiagTypeName() != null && this
						.getDiagTypeName().equals(castOther.getDiagTypeName())))
				&& ((this.getDocCode() == castOther.getDocCode()) || (this
						.getDocCode() != null && castOther.getDocCode() != null && this
						.getDocCode().equals(castOther.getDocCode())))
				&& ((this.getDocName() == castOther.getDocName()) || (this
						.getDocName() != null && castOther.getDocName() != null && this
						.getDocName().equals(castOther.getDocName())))
				&& ((this.getDeptCode() == castOther.getDeptCode()) || (this
						.getDeptCode() != null
						&& castOther.getDeptCode() != null && this
						.getDeptCode().equals(castOther.getDeptCode())))
				&& ((this.getDeptName() == castOther.getDeptName()) || (this
						.getDeptName() != null
						&& castOther.getDeptName() != null && this
						.getDeptName().equals(castOther.getDeptName())))
				&& ((this.getIsMain() == castOther.getIsMain()) || (this
						.getIsMain() != null && castOther.getIsMain() != null && this
						.getIsMain().equals(castOther.getIsMain())))
				&& ((this.getIsSuspected() == castOther.getIsSuspected()) || (this
						.getIsSuspected() != null
						&& castOther.getIsSuspected() != null && this
						.getIsSuspected().equals(castOther.getIsSuspected())))
				&& ((this.getIcd9Code() == castOther.getIcd9Code()) || (this
						.getIcd9Code() != null
						&& castOther.getIcd9Code() != null && this
						.getIcd9Code().equals(castOther.getIcd9Code())))
				&& ((this.getIcd9Name() == castOther.getIcd9Name()) || (this
						.getIcd9Name() != null
						&& castOther.getIcd9Name() != null && this
						.getIcd9Name().equals(castOther.getIcd9Name())))
				&& ((this.getIcd10Code() == castOther.getIcd10Code()) || (this
						.getIcd10Code() != null
						&& castOther.getIcd10Code() != null && this
						.getIcd10Code().equals(castOther.getIcd10Code())))
				&& ((this.getIcd10Name() == castOther.getIcd10Name()) || (this
						.getIcd10Name() != null
						&& castOther.getIcd10Name() != null && this
						.getIcd10Name().equals(castOther.getIcd10Name())))
				&& ((this.getDiagGradCode() == castOther.getDiagGradCode()) || (this
						.getDiagGradCode() != null
						&& castOther.getDiagGradCode() != null && this
						.getDiagGradCode().equals(castOther.getDiagGradCode())))
				&& ((this.getDiagGradName() == castOther.getDiagGradName()) || (this
						.getDiagGradName() != null
						&& castOther.getDiagGradName() != null && this
						.getDiagGradName().equals(castOther.getDiagGradName())))
				&& ((this.getCreateDate() == castOther.getCreateDate()) || (this
						.getCreateDate() != null
						&& castOther.getCreateDate() != null && this
						.getCreateDate().equals(castOther.getCreateDate())))
				&& ((this.getUpdateDate() == castOther.getUpdateDate()) || (this
						.getUpdateDate() != null
						&& castOther.getUpdateDate() != null && this
						.getUpdateDate().equals(castOther.getUpdateDate())));
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
		result = 37
				* result
				+ (getInpatientNo() == null ? 0 : this.getInpatientNo()
						.hashCode());
		result = 37 * result
				+ (getDiagCode() == null ? 0 : this.getDiagCode().hashCode());
		result = 37 * result
				+ (getDiagName() == null ? 0 : this.getDiagName().hashCode());
		result = 37 * result
				+ (getDiagTime() == null ? 0 : this.getDiagTime().hashCode());
		result = 37
				* result
				+ (getDiagTypeCode() == null ? 0 : this.getDiagTypeCode()
						.hashCode());
		result = 37
				* result
				+ (getDiagTypeName() == null ? 0 : this.getDiagTypeName()
						.hashCode());
		result = 37 * result
				+ (getDocCode() == null ? 0 : this.getDocCode().hashCode());
		result = 37 * result
				+ (getDocName() == null ? 0 : this.getDocName().hashCode());
		result = 37 * result
				+ (getDeptCode() == null ? 0 : this.getDeptCode().hashCode());
		result = 37 * result
				+ (getDeptName() == null ? 0 : this.getDeptName().hashCode());
		result = 37 * result
				+ (getIsMain() == null ? 0 : this.getIsMain().hashCode());
		result = 37
				* result
				+ (getIsSuspected() == null ? 0 : this.getIsSuspected()
						.hashCode());
		result = 37 * result
				+ (getIcd9Code() == null ? 0 : this.getIcd9Code().hashCode());
		result = 37 * result
				+ (getIcd9Name() == null ? 0 : this.getIcd9Name().hashCode());
		result = 37 * result
				+ (getIcd10Code() == null ? 0 : this.getIcd10Code().hashCode());
		result = 37 * result
				+ (getIcd10Name() == null ? 0 : this.getIcd10Name().hashCode());
		result = 37
				* result
				+ (getDiagGradCode() == null ? 0 : this.getDiagGradCode()
						.hashCode());
		result = 37
				* result
				+ (getDiagGradName() == null ? 0 : this.getDiagGradName()
						.hashCode());
		result = 37
				* result
				+ (getCreateDate() == null ? 0 : this.getCreateDate()
						.hashCode());
		result = 37
				* result
				+ (getUpdateDate() == null ? 0 : this.getUpdateDate()
						.hashCode());
		return result;
	}

}