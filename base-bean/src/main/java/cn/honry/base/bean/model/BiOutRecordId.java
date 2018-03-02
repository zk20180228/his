package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiOutRecordId entity. @author MyEclipse Persistence Tools
 */

public class BiOutRecordId implements java.io.Serializable {

	// Fields

	private String inpatientNo;
	private String patientNo;
	private Date inTime;
	private Date outTime;
	private String inDiag;
	private String outDiag;
	private String inStatus;
	private String treament;
	private String outStatus;
	private String judge;
	private Date createTime;
	private String updateTime;

	// Constructors

	/** default constructor */
	public BiOutRecordId() {
	}

	/** full constructor */
	public BiOutRecordId(String inpatientNo, String patientNo, Date inTime,
			Date outTime, String inDiag, String outDiag, String inStatus,
			String treament, String outStatus, String judge, Date createTime,
			String updateTime) {
		this.inpatientNo = inpatientNo;
		this.patientNo = patientNo;
		this.inTime = inTime;
		this.outTime = outTime;
		this.inDiag = inDiag;
		this.outDiag = outDiag;
		this.inStatus = inStatus;
		this.treament = treament;
		this.outStatus = outStatus;
		this.judge = judge;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	// Property accessors

	public String getInpatientNo() {
		return this.inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getPatientNo() {
		return this.patientNo;
	}

	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}

	public Date getInTime() {
		return this.inTime;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public Date getOutTime() {
		return this.outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	public String getInDiag() {
		return this.inDiag;
	}

	public void setInDiag(String inDiag) {
		this.inDiag = inDiag;
	}

	public String getOutDiag() {
		return this.outDiag;
	}

	public void setOutDiag(String outDiag) {
		this.outDiag = outDiag;
	}

	public String getInStatus() {
		return this.inStatus;
	}

	public void setInStatus(String inStatus) {
		this.inStatus = inStatus;
	}

	public String getTreament() {
		return this.treament;
	}

	public void setTreament(String treament) {
		this.treament = treament;
	}

	public String getOutStatus() {
		return this.outStatus;
	}

	public void setOutStatus(String outStatus) {
		this.outStatus = outStatus;
	}

	public String getJudge() {
		return this.judge;
	}

	public void setJudge(String judge) {
		this.judge = judge;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BiOutRecordId))
			return false;
		BiOutRecordId castOther = (BiOutRecordId) other;

		return ((this.getInpatientNo() == castOther.getInpatientNo()) || (this
				.getInpatientNo() != null && castOther.getInpatientNo() != null && this
				.getInpatientNo().equals(castOther.getInpatientNo())))
				&& ((this.getPatientNo() == castOther.getPatientNo()) || (this
						.getPatientNo() != null
						&& castOther.getPatientNo() != null && this
						.getPatientNo().equals(castOther.getPatientNo())))
				&& ((this.getInTime() == castOther.getInTime()) || (this
						.getInTime() != null && castOther.getInTime() != null && this
						.getInTime().equals(castOther.getInTime())))
				&& ((this.getOutTime() == castOther.getOutTime()) || (this
						.getOutTime() != null && castOther.getOutTime() != null && this
						.getOutTime().equals(castOther.getOutTime())))
				&& ((this.getInDiag() == castOther.getInDiag()) || (this
						.getInDiag() != null && castOther.getInDiag() != null && this
						.getInDiag().equals(castOther.getInDiag())))
				&& ((this.getOutDiag() == castOther.getOutDiag()) || (this
						.getOutDiag() != null && castOther.getOutDiag() != null && this
						.getOutDiag().equals(castOther.getOutDiag())))
				&& ((this.getInStatus() == castOther.getInStatus()) || (this
						.getInStatus() != null
						&& castOther.getInStatus() != null && this
						.getInStatus().equals(castOther.getInStatus())))
				&& ((this.getTreament() == castOther.getTreament()) || (this
						.getTreament() != null
						&& castOther.getTreament() != null && this
						.getTreament().equals(castOther.getTreament())))
				&& ((this.getOutStatus() == castOther.getOutStatus()) || (this
						.getOutStatus() != null
						&& castOther.getOutStatus() != null && this
						.getOutStatus().equals(castOther.getOutStatus())))
				&& ((this.getJudge() == castOther.getJudge()) || (this
						.getJudge() != null && castOther.getJudge() != null && this
						.getJudge().equals(castOther.getJudge())))
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
				+ (getInpatientNo() == null ? 0 : this.getInpatientNo()
						.hashCode());
		result = 37 * result
				+ (getPatientNo() == null ? 0 : this.getPatientNo().hashCode());
		result = 37 * result
				+ (getInTime() == null ? 0 : this.getInTime().hashCode());
		result = 37 * result
				+ (getOutTime() == null ? 0 : this.getOutTime().hashCode());
		result = 37 * result
				+ (getInDiag() == null ? 0 : this.getInDiag().hashCode());
		result = 37 * result
				+ (getOutDiag() == null ? 0 : this.getOutDiag().hashCode());
		result = 37 * result
				+ (getInStatus() == null ? 0 : this.getInStatus().hashCode());
		result = 37 * result
				+ (getTreament() == null ? 0 : this.getTreament().hashCode());
		result = 37 * result
				+ (getOutStatus() == null ? 0 : this.getOutStatus().hashCode());
		result = 37 * result
				+ (getJudge() == null ? 0 : this.getJudge().hashCode());
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