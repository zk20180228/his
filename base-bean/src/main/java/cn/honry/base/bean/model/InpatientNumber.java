package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class InpatientNumber extends Entity implements java.io.Serializable {

	/**项目名称  **/
	private String name;
	/** 就诊卡号**/
	private String idcardNo;
	/**病历号  **/
	private String medicalrecordId;
	/**  病案号**/
	private String caseNo;
	/**住院流水号 **/
	private String inpatientNo;
	/** 住院号**/
	private String inpatientId;
	/**入院时间 **/
	private Date inDate;
	/** 出院时间**/
	private Date outDate;
	/** 是否召回 **/
	private Integer recall;
	/** 科室代码 **/
	private String deptCode;
	/** 床位 **/
	private String bedinfoId;
	/** 结算类别 1-自费  2-保险 3-公费在职 4-公费退休 5-公费高干 (从编码表里读取) **/
	private String  paykindCode;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdcardNo() {
		return idcardNo;
	}
	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getInpatientId() {
		return inpatientId;
	}
	public void setInpatientId(String inpatientId) {
		this.inpatientId = inpatientId;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public Integer getRecall() {
		return recall;
	}
	public void setRecall(Integer recall) {
		this.recall = recall;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getBedinfoId() {
		return bedinfoId;
	}
	public void setBedinfoId(String bedinfoId) {
		this.bedinfoId = bedinfoId;
	}
	public String getPaykindCode() {
		return paykindCode;
	}
	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	}
	
	
}
