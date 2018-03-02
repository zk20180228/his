package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * ClassName: InpatientAccount 
 * @Description: 住院账户实体
 * @author lt
 * @date 2015-6-30
 */
@SuppressWarnings("serial")
public class InpatientAccount extends Entity implements java.io.Serializable {

	/**新增字段  就诊卡编号**/
	private PatientIdcard idcard;
	private String inpatientNo;
	private String medicalrecordId;
	private String accountRefid;
	private String accountName;
	private String accountType;
	private Integer accountState;
	private Double accountBalance;
	private Double accountDaylimit;
	private Double accountFrozencapital;
	private Date accountFrozentime;
	private Date accountUnfrozentime;
	private String accountRemark;
	
	public String getInpatientNo() {
		return this.inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getMedicalrecordId() {
		return this.medicalrecordId;
	}

	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}

	public String getAccountRefid() {
		return this.accountRefid;
	}

	public void setAccountRefid(String accountRefid) {
		this.accountRefid = accountRefid;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Integer getAccountState() {
		return accountState;
	}

	public void setAccountState(Integer accountState) {
		this.accountState = accountState;
	}

	public Double getAccountBalance() {
		return this.accountBalance;
	}

	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public Double getAccountDaylimit() {
		return this.accountDaylimit;
	}

	public void setAccountDaylimit(Double accountDaylimit) {
		this.accountDaylimit = accountDaylimit;
	}

	public Double getAccountFrozencapital() {
		return this.accountFrozencapital;
	}

	public void setAccountFrozencapital(Double accountFrozencapital) {
		this.accountFrozencapital = accountFrozencapital;
	}

	public Date getAccountFrozentime() {
		return this.accountFrozentime;
	}

	public void setAccountFrozentime(Date accountFrozentime) {
		this.accountFrozentime = accountFrozentime;
	}

	public Date getAccountUnfrozentime() {
		return this.accountUnfrozentime;
	}

	public void setAccountUnfrozentime(Date accountUnfrozentime) {
		this.accountUnfrozentime = accountUnfrozentime;
	}

	public String getAccountRemark() {
		return this.accountRemark;
	}

	public void setAccountRemark(String accountRemark) {
		this.accountRemark = accountRemark;
	}

	public PatientIdcard getIdcard() {
		return idcard;
	}

	public void setIdcard(PatientIdcard idcard) {
		this.idcard = idcard;
	}
	
}