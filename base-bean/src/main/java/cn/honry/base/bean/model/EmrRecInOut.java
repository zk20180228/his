/**
 * 
 */
package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * @author abc
 *电子病历常用词表
 */
public class EmrRecInOut extends Entity{
	/** 
	* @Fields inoutRecid : 借阅档案号
	*/ 
	private String inoutRecid;
	/** 
	* @Fields inoutAppdate : 借阅申请时间
	*/ 
	private Date inoutAppdate;
	/** 
	* @Fields inoutAppperson : 借阅申请人
	*/ 
	private String inoutAppperson;
	/** 
	* @Fields inoutDeadlinr : 借阅期限,单位：小时
	*/ 
	private Double inoutDeadlinr;
	/** 
	* @Fields inoutIndate : 归还时间
	*/ 
	private Date inoutIndate;
	/** 
	* @Fields inoutState : 借阅状态,0申请1同意2归还3不同意
	*/ 
	private Integer inoutState;
	/** 
	* @Fields inoutCheckUser : 审核人
	*/ 
	private String inoutCheckUser;
	/** 
	* @Fields inoutCheckDate : 审核时间 
	*/ 
	private Date inoutCheckDate;
	
	
	
	/*----------------------数据库无关字段-----------------------------*/
	
	/** 
	* @Fields cardId : 住院流水号
	*/ 
	String cardId;
	/** 
	* @Fields patientName : 患者姓名
	*/ 
	String patientName;
	/** 
	* @Fields patientsex : 患者性别
	*/ 
	String patientsex;
	/** 
	* @Fields appperson : 借阅申请人姓名 
	*/ 
	String appperson;
	/** 
	* @Fields checkUser : 审核人姓名
	*/ 
	String checkUser;
	
	public String getInoutRecid() {
		return inoutRecid;
	}
	public void setInoutRecid(String inoutRecid) {
		this.inoutRecid = inoutRecid;
	}
	public Date getInoutAppdate() {
		return inoutAppdate;
	}
	public void setInoutAppdate(Date inoutAppdate) {
		this.inoutAppdate = inoutAppdate;
	}
	public String getInoutAppperson() {
		return inoutAppperson;
	}
	public void setInoutAppperson(String inoutAppperson) {
		this.inoutAppperson = inoutAppperson;
	}
	public Double getInoutDeadlinr() {
		return inoutDeadlinr;
	}
	public void setInoutDeadlinr(Double inoutDeadlinr) {
		this.inoutDeadlinr = inoutDeadlinr;
	}
	public Date getInoutIndate() {
		return inoutIndate;
	}
	public void setInoutIndate(Date inoutIndate) {
		this.inoutIndate = inoutIndate;
	}
	public Integer getInoutState() {
		return inoutState;
	}
	public void setInoutState(Integer inoutState) {
		this.inoutState = inoutState;
	}
	public String getInoutCheckUser() {
		return inoutCheckUser;
	}
	public void setInoutCheckUser(String inoutCheckUser) {
		this.inoutCheckUser = inoutCheckUser;
	}
	public Date getInoutCheckDate() {
		return inoutCheckDate;
	}
	public void setInoutCheckDate(Date inoutCheckDate) {
		this.inoutCheckDate = inoutCheckDate;
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
	public String getPatientsex() {
		return patientsex;
	}
	public void setPatientsex(String patientsex) {
		this.patientsex = patientsex;
	}
	public String getAppperson() {
		return appperson;
	}
	public void setAppperson(String appperson) {
		this.appperson = appperson;
	}
	public String getCheckUser() {
		return checkUser;
	}
	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}
	
}
