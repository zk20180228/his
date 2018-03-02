package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 门诊患者账户表实体
 * @author  wangfujun
 * @date 创建时间：2016年3月28日 下午2:06:12
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
public class OutpatientAccount extends Entity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 病历号 **/
	private String medicalrecordId;
	/** 版本号 **/
	private int version;
	/** 就诊卡编号  **/
	private String idcardId;
	/** 账户名称  **/
	private String accountName;
	/** 账户类型：0普通账户，1-记账账户 ;默认：0**/
	private Integer accountType;
	/** 账户状态：0停用 1正常 2注销 ；默认：1 **/
	private Integer accountState;
	/** 账户密码  默认值123456 （MD5加密） **/
	private String accountPassword;
	/** 金额 ;默认：0	**/
	private Double accountBalance;
	/** 门诊单日消费限额 ；默认：0	**/
	private Double accountDaylimit;
	/** 是否授权：1是 0否 ；默认：0**/
	private Integer isEmpower;
	
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getIdcardId() {
		return idcardId;
	}
	public void setIdcardId(String idcardId) {
		this.idcardId = idcardId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public Integer getAccountType() {
		return accountType;
	}
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	public Integer getAccountState() {
		return accountState;
	}
	public void setAccountState(Integer accountState) {
		this.accountState = accountState;
	}
	public String getAccountPassword() {
		return accountPassword;
	}
	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}
	public Double getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}
	public Double getAccountDaylimit() {
		return accountDaylimit;
	}
	public void setAccountDaylimit(Double accountDaylimit) {
		this.accountDaylimit = accountDaylimit;
	}
	public Integer getIsEmpower() {
		return isEmpower;
	}
	public void setIsEmpower(Integer isEmpower) {
		this.isEmpower = isEmpower;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
}
