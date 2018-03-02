package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class EmrRecordMain extends Entity {
	/**归档编码*/
	private String code;
	/**归档日期**/
	private Date date;
	/**纸质病历提交时间**/
	private Date paperDate;
	/**科室送交病历人**/
	private String paperPerson;
	/**签收回退状态，0提交，1签收，2退回*/
	private Integer state;
	/**签收时间**/
	private Date signDate;
	/***签收人**/
	private String signPerson;
	/**患者就诊卡号或者住院号**/
	private String cardId;
	/**患者姓名**/
	private String patientName;
	/**患者性别**/
	private String patientSex;
	/**患者年龄**/
	private Integer patientAge;
	/**出生日期**/
	private Date patientBirth;
	/**入院时间**/
	private Date inDate;
	/**出院时间**/
	private Date outDate;
	/**出院科室**/
	private String outDept;
	/**诊断编码**/
	private String dignose;
	/**住院医生**/
	private String inpatientDoc;
	/**主治医师**/
	private String attendingDoc;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getPaperDate() {
		return paperDate;
	}
	public void setPaperDate(Date paperDate) {
		this.paperDate = paperDate;
	}
	public String getPaperPerson() {
		return paperPerson;
	}
	public void setPaperPerson(String paperPerson) {
		this.paperPerson = paperPerson;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getSignPerson() {
		return signPerson;
	}
	public void setSignPerson(String signPerson) {
		this.signPerson = signPerson;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPatientSex() {
		return patientSex;
	}
	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}
	public Integer getPatientAge() {
		return patientAge;
	}
	public void setPatientAge(Integer patientAge) {
		this.patientAge = patientAge;
	}
	public Date getPatientBirth() {
		return patientBirth;
	}
	public void setPatientBirth(Date patientBirth) {
		this.patientBirth = patientBirth;
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
	public String getOutDept() {
		return outDept;
	}
	public void setOutDept(String outDept) {
		this.outDept = outDept;
	}
	public String getDignose() {
		return dignose;
	}
	public void setDignose(String dignose) {
		this.dignose = dignose;
	}
	public String getInpatientDoc() {
		return inpatientDoc;
	}
	public void setInpatientDoc(String inpatientDoc) {
		this.inpatientDoc = inpatientDoc;
	}
	public String getAttendingDoc() {
		return attendingDoc;
	}
	public void setAttendingDoc(String attendingDoc) {
		this.attendingDoc = attendingDoc;
	}
	
}
