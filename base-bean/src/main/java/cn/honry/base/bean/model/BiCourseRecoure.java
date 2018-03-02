package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiCourseRecoure entity. @author MyEclipse Persistence Tools
 */

public class BiCourseRecoure implements java.io.Serializable {

	// Fields

	private String id;
	private Date recordTime;
	private String signName;
	private String signCode;
	private String inpatientNo;
	private String illness;
	private String examination;
	private String clinicDiag;
	private String opinion;
	private String recordCode;
	private String recordName;
	private String recordDept;
	private String recordWard;
	private String other;
	private Date createTime;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public BiCourseRecoure() {
	}

	/** full constructor */
	public BiCourseRecoure(Date recordTime, String signName, String signCode,
			String inpatientNo, String illness, String examination,
			String clinicDiag, String opinion, String recordCode,
			String recordName, String recordDept, String recordWard,
			String other, Date createTime, Date updateTime) {
		this.recordTime = recordTime;
		this.signName = signName;
		this.signCode = signCode;
		this.inpatientNo = inpatientNo;
		this.illness = illness;
		this.examination = examination;
		this.clinicDiag = clinicDiag;
		this.opinion = opinion;
		this.recordCode = recordCode;
		this.recordName = recordName;
		this.recordDept = recordDept;
		this.recordWard = recordWard;
		this.other = other;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getRecordTime() {
		return this.recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public String getSignName() {
		return this.signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getSignCode() {
		return this.signCode;
	}

	public void setSignCode(String signCode) {
		this.signCode = signCode;
	}

	public String getInpatientNo() {
		return this.inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getIllness() {
		return this.illness;
	}

	public void setIllness(String illness) {
		this.illness = illness;
	}

	public String getExamination() {
		return this.examination;
	}

	public void setExamination(String examination) {
		this.examination = examination;
	}

	public String getClinicDiag() {
		return this.clinicDiag;
	}

	public void setClinicDiag(String clinicDiag) {
		this.clinicDiag = clinicDiag;
	}

	public String getOpinion() {
		return this.opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getRecordCode() {
		return this.recordCode;
	}

	public void setRecordCode(String recordCode) {
		this.recordCode = recordCode;
	}

	public String getRecordName() {
		return this.recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public String getRecordDept() {
		return this.recordDept;
	}

	public void setRecordDept(String recordDept) {
		this.recordDept = recordDept;
	}

	public String getRecordWard() {
		return this.recordWard;
	}

	public void setRecordWard(String recordWard) {
		this.recordWard = recordWard;
	}

	public String getOther() {
		return this.other;
	}

	public void setOther(String other) {
		this.other = other;
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

}