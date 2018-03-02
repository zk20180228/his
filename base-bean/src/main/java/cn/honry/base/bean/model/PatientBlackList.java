package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**
 * 
 * 患者黑名单 
 *
 */
public class PatientBlackList extends Entity implements java.io.Serializable {

	/**患者编号**/
	private Patient patient;
	/**病历号**/
	private String medicalrecordId;
	/**从编码表获取，爽约、欠费、医闹**/
	private String blacklistType;
	/**进入黑名单原因**/
	private String blacklistIntoreason;
	/**退出黑名单原因**/
	private String blacklistOutreason;
	/**有效开始时间**/
	private Date blacklistStarttime;
	/**有效结束时间**/
	private Date blacklistEndtime;
	/**患者id**/
	private String patientId;
	/**患者name**/
	private String patientName;
	
	/**有效开始时间显示**/
	private String blacklistStarttimeView;
	/**有效结束时间显示**/
	private String blacklistEndtimeView;
	
	public Patient getPatient() {
		return this.patient;
	}

	public void setPatient(Patient Patient) {
		this.patient = Patient;
	}

	public String getMedicalrecordId() {
		return this.medicalrecordId;
	}

	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}

	public String getBlacklistType() {
		return this.blacklistType;
	}

	public void setBlacklistType(String blacklistType) {
		this.blacklistType = blacklistType;
	}

	public String getBlacklistIntoreason() {
		return this.blacklistIntoreason;
	}

	public void setBlacklistIntoreason(String blacklistIntoreason) {
		this.blacklistIntoreason = blacklistIntoreason;
	}

	public String getBlacklistOutreason() {
		return this.blacklistOutreason;
	}

	public void setBlacklistOutreason(String blacklistOutreason) {
		this.blacklistOutreason = blacklistOutreason;
	}

	public Date getBlacklistStarttime() {
		return this.blacklistStarttime;
	}

	public void setBlacklistStarttime(Date blacklistStarttime) {
		this.blacklistStarttime = blacklistStarttime;
	}

	public Date getBlacklistEndtime() {
		return this.blacklistEndtime;
	}

	public void setBlacklistEndtime(Date blacklistEndtime) {
		this.blacklistEndtime = blacklistEndtime;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientName() {
		return patientName;
	}

	public String getBlacklistStarttimeView() {
		return blacklistStarttimeView;
	}

	public void setBlacklistStarttimeView(String blacklistStarttimeView) {
		this.blacklistStarttimeView = blacklistStarttimeView;
	}

	public String getBlacklistEndtimeView() {
		return blacklistEndtimeView;
	}

	public void setBlacklistEndtimeView(String blacklistEndtimeView) {
		this.blacklistEndtimeView = blacklistEndtimeView;
	}

	
}