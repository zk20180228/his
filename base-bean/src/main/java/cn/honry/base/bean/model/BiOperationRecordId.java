package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiOperationRecordId entity. @author MyEclipse Persistence Tools
 */

public class BiOperationRecordId implements java.io.Serializable {

	// Fields

	private String operationNo;
	private String inpatientNo;
	private String medicalNo;
	private String patientNo;
	private String patientName;
	private String wardNo;
	private String wardName;
	private String deptCode;
	private String deptName;
	private String bedNo;
	private String consoleCode;
	private String consoleName;
	private Date preTime;
	private Date arrange;
	private Date opTime;
	private Double hourLong;
	private String beforeDiag;
	private String afterDiag;
	private String beforeName;
	private String afterName;
	private String anesthesiaMethod;
	private String opPerson;
	private String anesthesiaPerson;
	private String position;
	private String incisionType;
	private String lesion;
	private String opStep;
	private Date createTime;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public BiOperationRecordId() {
	}

	/** full constructor */
	public BiOperationRecordId(String operationNo, String inpatientNo,
			String medicalNo, String patientNo, String patientName,
			String wardNo, String wardName, String deptCode, String deptName,
			String bedNo, String consoleCode, String consoleName, Date preTime,
			Date arrange, Date opTime, Double hourLong, String beforeDiag,
			String afterDiag, String beforeName, String afterName,
			String anesthesiaMethod, String opPerson, String anesthesiaPerson,
			String position, String incisionType, String lesion, String opStep,
			Date createTime, Date updateTime) {
		this.operationNo = operationNo;
		this.inpatientNo = inpatientNo;
		this.medicalNo = medicalNo;
		this.patientNo = patientNo;
		this.patientName = patientName;
		this.wardNo = wardNo;
		this.wardName = wardName;
		this.deptCode = deptCode;
		this.deptName = deptName;
		this.bedNo = bedNo;
		this.consoleCode = consoleCode;
		this.consoleName = consoleName;
		this.preTime = preTime;
		this.arrange = arrange;
		this.opTime = opTime;
		this.hourLong = hourLong;
		this.beforeDiag = beforeDiag;
		this.afterDiag = afterDiag;
		this.beforeName = beforeName;
		this.afterName = afterName;
		this.anesthesiaMethod = anesthesiaMethod;
		this.opPerson = opPerson;
		this.anesthesiaPerson = anesthesiaPerson;
		this.position = position;
		this.incisionType = incisionType;
		this.lesion = lesion;
		this.opStep = opStep;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	// Property accessors

	public String getOperationNo() {
		return this.operationNo;
	}

	public void setOperationNo(String operationNo) {
		this.operationNo = operationNo;
	}

	public String getInpatientNo() {
		return this.inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getMedicalNo() {
		return this.medicalNo;
	}

	public void setMedicalNo(String medicalNo) {
		this.medicalNo = medicalNo;
	}

	public String getPatientNo() {
		return this.patientNo;
	}

	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}

	public String getPatientName() {
		return this.patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getWardNo() {
		return this.wardNo;
	}

	public void setWardNo(String wardNo) {
		this.wardNo = wardNo;
	}

	public String getWardName() {
		return this.wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
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

	public String getBedNo() {
		return this.bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getConsoleCode() {
		return this.consoleCode;
	}

	public void setConsoleCode(String consoleCode) {
		this.consoleCode = consoleCode;
	}

	public String getConsoleName() {
		return this.consoleName;
	}

	public void setConsoleName(String consoleName) {
		this.consoleName = consoleName;
	}

	public Date getPreTime() {
		return this.preTime;
	}

	public void setPreTime(Date preTime) {
		this.preTime = preTime;
	}

	public Date getArrange() {
		return this.arrange;
	}

	public void setArrange(Date arrange) {
		this.arrange = arrange;
	}

	public Date getOpTime() {
		return this.opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public Double getHourLong() {
		return this.hourLong;
	}

	public void setHourLong(Double hourLong) {
		this.hourLong = hourLong;
	}

	public String getBeforeDiag() {
		return this.beforeDiag;
	}

	public void setBeforeDiag(String beforeDiag) {
		this.beforeDiag = beforeDiag;
	}

	public String getAfterDiag() {
		return this.afterDiag;
	}

	public void setAfterDiag(String afterDiag) {
		this.afterDiag = afterDiag;
	}

	public String getBeforeName() {
		return this.beforeName;
	}

	public void setBeforeName(String beforeName) {
		this.beforeName = beforeName;
	}

	public String getAfterName() {
		return this.afterName;
	}

	public void setAfterName(String afterName) {
		this.afterName = afterName;
	}

	public String getAnesthesiaMethod() {
		return this.anesthesiaMethod;
	}

	public void setAnesthesiaMethod(String anesthesiaMethod) {
		this.anesthesiaMethod = anesthesiaMethod;
	}

	public String getOpPerson() {
		return this.opPerson;
	}

	public void setOpPerson(String opPerson) {
		this.opPerson = opPerson;
	}

	public String getAnesthesiaPerson() {
		return this.anesthesiaPerson;
	}

	public void setAnesthesiaPerson(String anesthesiaPerson) {
		this.anesthesiaPerson = anesthesiaPerson;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getIncisionType() {
		return this.incisionType;
	}

	public void setIncisionType(String incisionType) {
		this.incisionType = incisionType;
	}

	public String getLesion() {
		return this.lesion;
	}

	public void setLesion(String lesion) {
		this.lesion = lesion;
	}

	public String getOpStep() {
		return this.opStep;
	}

	public void setOpStep(String opStep) {
		this.opStep = opStep;
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
		if (!(other instanceof BiOperationRecordId))
			return false;
		BiOperationRecordId castOther = (BiOperationRecordId) other;

		return ((this.getOperationNo() == castOther.getOperationNo()) || (this
				.getOperationNo() != null && castOther.getOperationNo() != null && this
				.getOperationNo().equals(castOther.getOperationNo())))
				&& ((this.getInpatientNo() == castOther.getInpatientNo()) || (this
						.getInpatientNo() != null
						&& castOther.getInpatientNo() != null && this
						.getInpatientNo().equals(castOther.getInpatientNo())))
				&& ((this.getMedicalNo() == castOther.getMedicalNo()) || (this
						.getMedicalNo() != null
						&& castOther.getMedicalNo() != null && this
						.getMedicalNo().equals(castOther.getMedicalNo())))
				&& ((this.getPatientNo() == castOther.getPatientNo()) || (this
						.getPatientNo() != null
						&& castOther.getPatientNo() != null && this
						.getPatientNo().equals(castOther.getPatientNo())))
				&& ((this.getPatientName() == castOther.getPatientName()) || (this
						.getPatientName() != null
						&& castOther.getPatientName() != null && this
						.getPatientName().equals(castOther.getPatientName())))
				&& ((this.getWardNo() == castOther.getWardNo()) || (this
						.getWardNo() != null && castOther.getWardNo() != null && this
						.getWardNo().equals(castOther.getWardNo())))
				&& ((this.getWardName() == castOther.getWardName()) || (this
						.getWardName() != null
						&& castOther.getWardName() != null && this
						.getWardName().equals(castOther.getWardName())))
				&& ((this.getDeptCode() == castOther.getDeptCode()) || (this
						.getDeptCode() != null
						&& castOther.getDeptCode() != null && this
						.getDeptCode().equals(castOther.getDeptCode())))
				&& ((this.getDeptName() == castOther.getDeptName()) || (this
						.getDeptName() != null
						&& castOther.getDeptName() != null && this
						.getDeptName().equals(castOther.getDeptName())))
				&& ((this.getBedNo() == castOther.getBedNo()) || (this
						.getBedNo() != null && castOther.getBedNo() != null && this
						.getBedNo().equals(castOther.getBedNo())))
				&& ((this.getConsoleCode() == castOther.getConsoleCode()) || (this
						.getConsoleCode() != null
						&& castOther.getConsoleCode() != null && this
						.getConsoleCode().equals(castOther.getConsoleCode())))
				&& ((this.getConsoleName() == castOther.getConsoleName()) || (this
						.getConsoleName() != null
						&& castOther.getConsoleName() != null && this
						.getConsoleName().equals(castOther.getConsoleName())))
				&& ((this.getPreTime() == castOther.getPreTime()) || (this
						.getPreTime() != null && castOther.getPreTime() != null && this
						.getPreTime().equals(castOther.getPreTime())))
				&& ((this.getArrange() == castOther.getArrange()) || (this
						.getArrange() != null && castOther.getArrange() != null && this
						.getArrange().equals(castOther.getArrange())))
				&& ((this.getOpTime() == castOther.getOpTime()) || (this
						.getOpTime() != null && castOther.getOpTime() != null && this
						.getOpTime().equals(castOther.getOpTime())))
				&& ((this.getHourLong() == castOther.getHourLong()) || (this
						.getHourLong() != null
						&& castOther.getHourLong() != null && this
						.getHourLong().equals(castOther.getHourLong())))
				&& ((this.getBeforeDiag() == castOther.getBeforeDiag()) || (this
						.getBeforeDiag() != null
						&& castOther.getBeforeDiag() != null && this
						.getBeforeDiag().equals(castOther.getBeforeDiag())))
				&& ((this.getAfterDiag() == castOther.getAfterDiag()) || (this
						.getAfterDiag() != null
						&& castOther.getAfterDiag() != null && this
						.getAfterDiag().equals(castOther.getAfterDiag())))
				&& ((this.getBeforeName() == castOther.getBeforeName()) || (this
						.getBeforeName() != null
						&& castOther.getBeforeName() != null && this
						.getBeforeName().equals(castOther.getBeforeName())))
				&& ((this.getAfterName() == castOther.getAfterName()) || (this
						.getAfterName() != null
						&& castOther.getAfterName() != null && this
						.getAfterName().equals(castOther.getAfterName())))
				&& ((this.getAnesthesiaMethod() == castOther
						.getAnesthesiaMethod()) || (this.getAnesthesiaMethod() != null
						&& castOther.getAnesthesiaMethod() != null && this
						.getAnesthesiaMethod().equals(
								castOther.getAnesthesiaMethod())))
				&& ((this.getOpPerson() == castOther.getOpPerson()) || (this
						.getOpPerson() != null
						&& castOther.getOpPerson() != null && this
						.getOpPerson().equals(castOther.getOpPerson())))
				&& ((this.getAnesthesiaPerson() == castOther
						.getAnesthesiaPerson()) || (this.getAnesthesiaPerson() != null
						&& castOther.getAnesthesiaPerson() != null && this
						.getAnesthesiaPerson().equals(
								castOther.getAnesthesiaPerson())))
				&& ((this.getPosition() == castOther.getPosition()) || (this
						.getPosition() != null
						&& castOther.getPosition() != null && this
						.getPosition().equals(castOther.getPosition())))
				&& ((this.getIncisionType() == castOther.getIncisionType()) || (this
						.getIncisionType() != null
						&& castOther.getIncisionType() != null && this
						.getIncisionType().equals(castOther.getIncisionType())))
				&& ((this.getLesion() == castOther.getLesion()) || (this
						.getLesion() != null && castOther.getLesion() != null && this
						.getLesion().equals(castOther.getLesion())))
				&& ((this.getOpStep() == castOther.getOpStep()) || (this
						.getOpStep() != null && castOther.getOpStep() != null && this
						.getOpStep().equals(castOther.getOpStep())))
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
				+ (getOperationNo() == null ? 0 : this.getOperationNo()
						.hashCode());
		result = 37
				* result
				+ (getInpatientNo() == null ? 0 : this.getInpatientNo()
						.hashCode());
		result = 37 * result
				+ (getMedicalNo() == null ? 0 : this.getMedicalNo().hashCode());
		result = 37 * result
				+ (getPatientNo() == null ? 0 : this.getPatientNo().hashCode());
		result = 37
				* result
				+ (getPatientName() == null ? 0 : this.getPatientName()
						.hashCode());
		result = 37 * result
				+ (getWardNo() == null ? 0 : this.getWardNo().hashCode());
		result = 37 * result
				+ (getWardName() == null ? 0 : this.getWardName().hashCode());
		result = 37 * result
				+ (getDeptCode() == null ? 0 : this.getDeptCode().hashCode());
		result = 37 * result
				+ (getDeptName() == null ? 0 : this.getDeptName().hashCode());
		result = 37 * result
				+ (getBedNo() == null ? 0 : this.getBedNo().hashCode());
		result = 37
				* result
				+ (getConsoleCode() == null ? 0 : this.getConsoleCode()
						.hashCode());
		result = 37
				* result
				+ (getConsoleName() == null ? 0 : this.getConsoleName()
						.hashCode());
		result = 37 * result
				+ (getPreTime() == null ? 0 : this.getPreTime().hashCode());
		result = 37 * result
				+ (getArrange() == null ? 0 : this.getArrange().hashCode());
		result = 37 * result
				+ (getOpTime() == null ? 0 : this.getOpTime().hashCode());
		result = 37 * result
				+ (getHourLong() == null ? 0 : this.getHourLong().hashCode());
		result = 37
				* result
				+ (getBeforeDiag() == null ? 0 : this.getBeforeDiag()
						.hashCode());
		result = 37 * result
				+ (getAfterDiag() == null ? 0 : this.getAfterDiag().hashCode());
		result = 37
				* result
				+ (getBeforeName() == null ? 0 : this.getBeforeName()
						.hashCode());
		result = 37 * result
				+ (getAfterName() == null ? 0 : this.getAfterName().hashCode());
		result = 37
				* result
				+ (getAnesthesiaMethod() == null ? 0 : this
						.getAnesthesiaMethod().hashCode());
		result = 37 * result
				+ (getOpPerson() == null ? 0 : this.getOpPerson().hashCode());
		result = 37
				* result
				+ (getAnesthesiaPerson() == null ? 0 : this
						.getAnesthesiaPerson().hashCode());
		result = 37 * result
				+ (getPosition() == null ? 0 : this.getPosition().hashCode());
		result = 37
				* result
				+ (getIncisionType() == null ? 0 : this.getIncisionType()
						.hashCode());
		result = 37 * result
				+ (getLesion() == null ? 0 : this.getLesion().hashCode());
		result = 37 * result
				+ (getOpStep() == null ? 0 : this.getOpStep().hashCode());
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