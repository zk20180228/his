package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 财务：门诊预交金表实体
 * @author  wangfujun
 * @date 创建时间：2016年3月28日 下午2:43:41
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
public class OutpatientOutprepay extends Entity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
//	/**  **/
//	private String ;
	/** 患者病历号	 **/
	private String medicalrecordId;
	/** 门诊账户编号  **/
	private String accountId;
	/** 发生序号 **/
	private Integer happenNo;
	/** 患者姓名 **/
	private String patientName;
	/** 预交金额 **/
	private Double prepayCost;
	/** 预交金收据号码 **/
	private String receiptNo;
	/** 支付方式：CA现金CH支票CD信用卡DB借记卡AJ转押金PO汇票PS保险帐户YS院内账户  **/
	private String prepayType;
	/** 开户银行 **/
	private String openBank;
	/** 开户帐户 **/
	private String openAccounts;
	/** POS交易流水号或支票号或汇票号 **/
	private String postransNo;
	/** 日结标记：0未日结/1已日结 **/
	private Integer balanceFlag;
	/** 日结标识号  **/
	private String daybalanceNo;
	/** 日结人 **/
	private String daybalanceOpcd;
	/** 日结时间 **/
	private Date daybalanceDate;
	/** 预交金状态：0反还，1收取，2重打 **/
	private Integer prepayState;
	/** 重打次数 **/
	private Integer printTimes;
	/** 原预交金收据号码 **/
	private String oldRecipeno;
	/** 是否历史数据(结清帐户以前的数据)1是 0否 **/
	private Integer ishistory;
	/** 开户单位 **/
	private String workName;
	/** 优惠金额 **/
	private Double ecoCost;
	/** 清仓标志 **/
	private Integer offFlag;
	/**清仓人  **/
	private String offOpercode;
	/** 窗口类型：1窗口 2自助机 **/
	private Integer sourceType;
	/** 自助机器号 **/
	private String machineNo;
	/** 对账流水号 **/
	private String machineTransno;
	/** 银行交易流水号 **/
	private String bankTransno;
	
	
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
	public Integer getHappenNo() {
		return happenNo;
	}
	public void setHappenNo(Integer happenNo) {
		this.happenNo = happenNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public Double getPrepayCost() {
		return prepayCost;
	}
	public void setPrepayCost(Double prepayCost) {
		this.prepayCost = prepayCost;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getPrepayType() {
		return prepayType;
	}
	public void setPrepayType(String prepayType) {
		this.prepayType = prepayType;
	}
	public String getOpenBank() {
		return openBank;
	}
	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}
	public String getOpenAccounts() {
		return openAccounts;
	}
	public void setOpenAccounts(String openAccounts) {
		this.openAccounts = openAccounts;
	}
	public String getPostransNo() {
		return postransNo;
	}
	public void setPostransNo(String postransNo) {
		this.postransNo = postransNo;
	}
	public Integer getBalanceFlag() {
		return balanceFlag;
	}
	public void setBalanceFlag(Integer balanceFlag) {
		this.balanceFlag = balanceFlag;
	}
	public String getDaybalanceNo() {
		return daybalanceNo;
	}
	public void setDaybalanceNo(String daybalanceNo) {
		this.daybalanceNo = daybalanceNo;
	}
	public String getDaybalanceOpcd() {
		return daybalanceOpcd;
	}
	public void setDaybalanceOpcd(String daybalanceOpcd) {
		this.daybalanceOpcd = daybalanceOpcd;
	}
	public Date getDaybalanceDate() {
		return daybalanceDate;
	}
	public void setDaybalanceDate(Date daybalanceDate) {
		this.daybalanceDate = daybalanceDate;
	}
	public Integer getPrepayState() {
		return prepayState;
	}
	public void setPrepayState(Integer prepayState) {
		this.prepayState = prepayState;
	}
	public Integer getPrintTimes() {
		return printTimes;
	}
	public void setPrintTimes(Integer printTimes) {
		this.printTimes = printTimes;
	}
	public String getOldRecipeno() {
		return oldRecipeno;
	}
	public void setOldRecipeno(String oldRecipeno) {
		this.oldRecipeno = oldRecipeno;
	}
	public Integer getIshistory() {
		return ishistory;
	}
	public void setIshistory(Integer ishistory) {
		this.ishistory = ishistory;
	}
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	public Double getEcoCost() {
		return ecoCost;
	}
	public void setEcoCost(Double ecoCost) {
		this.ecoCost = ecoCost;
	}
	public Integer getOffFlag() {
		return offFlag;
	}
	public void setOffFlag(Integer offFlag) {
		this.offFlag = offFlag;
	}
	public String getOffOpercode() {
		return offOpercode;
	}
	public void setOffOpercode(String offOpercode) {
		this.offOpercode = offOpercode;
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
	public String getBankTransno() {
		return bankTransno;
	}
	public void setBankTransno(String bankTransno) {
		this.bankTransno = bankTransno;
	}
	
}
