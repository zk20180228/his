package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

@SuppressWarnings("serial")
public class PatientAccount extends Entity implements java.io.Serializable {

	// Fields
	
	/**就诊卡编号**/
	private PatientIdcard idcard;
	/**账户密码**/
	private String accountPassword;
	/**参考编号**/
	private String accountRefid;
	/**账户名称**/
	private String accountName;
	/**账户类型:从编码表里读取**/
	private String accountType;
	/**金额**/
	private Double accountBalance;
	/**冻结金额**/
	private Double accountFrozencapital;
	/**冻结时间**/
	private Date accountFrozentime;
	/**解冻时间**/
	private Date accountUnfrozentime;
	/**备注**/
	private String accountRemark;
	/**状态：1正常2注销3结清4冻结**/
	private int accountState;
	/**门诊单日消费限额**/
	private Double accountDaylimit;	
	/** 新添加字段 病历号  **/
	private String medicalrecordId;
	/** 新添加字段   门诊金额  **/
	private Double clinicBalance;
	/** 新添加字段  住院金额  **/
	private Double inpatientBalance;
	/**新添加字段  住院单日消费限额**/
	private Double accountInpatientDaylimit;
	
	public PatientIdcard getIdcard() {
		return idcard;
	}

	public void setIdcard(PatientIdcard idcard) {
		this.idcard = idcard;
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

	public Double getAccountBalance() {
		return this.accountBalance;
	}

	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
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
	public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}
	public int getAccountState() {
		return accountState;
	}

	public void setAccountState(int accountState) {
		this.accountState = accountState;
	}

	public Double getAccountDaylimit() {
		return accountDaylimit;
	}

	public void setAccountDaylimit(Double accountDaylimit) {
		this.accountDaylimit = accountDaylimit;
	}

	public String getMedicalrecordId() {
		return medicalrecordId;
	}

	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}

	public Double getClinicBalance() {
		return clinicBalance;
	}

	public void setClinicBalance(Double clinicBalance) {
		this.clinicBalance = clinicBalance;
	}

	public Double getInpatientBalance() {
		return inpatientBalance;
	}

	public void setInpatientBalance(Double inpatientBalance) {
		this.inpatientBalance = inpatientBalance;
	}

	public Double getAccountInpatientDaylimit() {
		return accountInpatientDaylimit;
	}

	public void setAccountInpatientDaylimit(Double accountInpatientDaylimit) {
		this.accountInpatientDaylimit = accountInpatientDaylimit;
	}
	
}