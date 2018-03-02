package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiOperationDiagnose entity. @author MyEclipse Persistence Tools
 */

public class BiOperationDiagnose implements java.io.Serializable {

	// Fields

	private BiOperationDiagnoseId id;
	private String inpatientNo;
	private String cardNo;
	private String diagKind;
	private String icdCode;
	private String diagName;
	private Date createTime;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public BiOperationDiagnose() {
	}

	/** minimal constructor */
	public BiOperationDiagnose(BiOperationDiagnoseId id, String inpatientNo,
			String cardNo, String diagKind, String icdCode, String diagName) {
		this.id = id;
		this.inpatientNo = inpatientNo;
		this.cardNo = cardNo;
		this.diagKind = diagKind;
		this.icdCode = icdCode;
		this.diagName = diagName;
	}

	/** full constructor */
	public BiOperationDiagnose(BiOperationDiagnoseId id, String inpatientNo,
			String cardNo, String diagKind, String icdCode, String diagName,
			Date createTime, Date updateTime) {
		this.id = id;
		this.inpatientNo = inpatientNo;
		this.cardNo = cardNo;
		this.diagKind = diagKind;
		this.icdCode = icdCode;
		this.diagName = diagName;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	// Property accessors

	public BiOperationDiagnoseId getId() {
		return this.id;
	}

	public void setId(BiOperationDiagnoseId id) {
		this.id = id;
	}

	public String getInpatientNo() {
		return this.inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getDiagKind() {
		return this.diagKind;
	}

	public void setDiagKind(String diagKind) {
		this.diagKind = diagKind;
	}

	public String getIcdCode() {
		return this.icdCode;
	}

	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}

	public String getDiagName() {
		return this.diagName;
	}

	public void setDiagName(String diagName) {
		this.diagName = diagName;
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