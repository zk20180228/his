package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 门诊患者账户操作流水表实体
 * @author  wangfujun
 * @date 创建时间：2016年3月28日 下午2:24:17
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
public class OutpatientAccountrecord extends Entity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
//	sequence_no	VARCHAR2(50)	N			主键（交易流水号）
	/** 患者病历号 **/
	private String medicalrecordId;
	/** 门诊账户编号 **/
	private String accountId;
	/** 操作类型：0存预交金 1新建帐户 2停帐户 3重启帐户 4支付 5退费入户 6注销帐户 7授权支付 8退预交金 9修改密码 10结清余额11授权12补打   **/
	private Integer opertype;
	/** 交易金额  **/
	private Double money;
	/** 相关科室  **/
	private String deptCode;
	/** 操作员 **/
	private String operCode;
	/** 操作时间 	 **/
	private Date operDate;
	/** 备注	 **/
	private String remark;
	/** 是否有效：0有效1无效	 **/
	private Integer valid;
	/** 交易后余额	 **/
	private Double accountBalance;
	/** 发票类型	 **/
	private String invoiceType;
	/** 1窗口 2自助机	 **/
	private Integer sourceType;
	/** 自助机器号	 **/
	private String machineNo;
	/** 对账流水号	 **/
	private String machineTransno;
	/** 被授权门诊卡号	 **/
	private String empowerCardno;
	/** 被授权人姓名 	**/
	private String empowerName;
	/** 授权人姓名 	**/
	private String licensorName;
	/** 授权金额	 **/
	private Double empowerCost;
	
	
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public Integer getOpertype() {
		return opertype;
	}
	public void setOpertype(Integer opertype) {
		this.opertype = opertype;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date date) {
		this.operDate = date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	public Double getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public Integer getSourceType() {
		return sourceType;
	}
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}
	public String getMachineNo() {
		return machineNo;
	}
	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}
	public String getMachineTransno() {
		return machineTransno;
	}
	public void setMachineTransno(String machineTransno) {
		this.machineTransno = machineTransno;
	}
	public String getEmpowerCardno() {
		return empowerCardno;
	}
	public void setEmpowerCardno(String empowerCardno) {
		this.empowerCardno = empowerCardno;
	}
	public String getEmpowerName() {
		return empowerName;
	}
	public void setEmpowerName(String empowerName) {
		this.empowerName = empowerName;
	}
	public String getLicensorName() {
		return licensorName;
	}
	public void setLicensorName(String licensorName) {
		this.licensorName = licensorName;
	}
	public Double getEmpowerCost() {
		return empowerCost;
	}
	public void setEmpowerCost(Double empowerCost) {
		this.empowerCost = empowerCost;
	}
	
	
}
