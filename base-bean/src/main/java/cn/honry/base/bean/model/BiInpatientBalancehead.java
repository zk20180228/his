package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiInpatientBalancehead entity. @author MyEclipse Persistence Tools
 */

public class BiInpatientBalancehead implements java.io.Serializable {

	// Fields

	private String invoiceNo;
	private Boolean transType;
	private String inpatientNo;
	private Short balanceNo;
	private Boolean paykindCode;
	private String pactCode;
	private Double prepayCost;
	private Double changePrepaycost;
	private Double totCost;
	private Double ownCost;
	private Double payCost;
	private Double pubCost;
	private Double ecoCost;
	private Double derCost;
	private Double changeTotcost;
	private Double supplyCost;
	private Double returnCost;
	private Double foregiftCost;
	private Date beginDate;
	private Date endDate;
	private Boolean balanceType;
	private String balanceOpercode;
	private Date balanceDate;
	private Double accountPay;
	private Double officePay;
	private Double largePay;
	private Double miltaryPay;
	private Double cashPay;
	private String name;
	private String balanceoperDeptcode;
	private Double bursaryAdjustovertop;
	private String checkOpcd;
	private Date checkDate;
	private Boolean daybalanceFlag;
	private String daybalanceNo;
	private String daybalanceOpcd;
	private Date daybalanceDate;
	private Date createTime;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public BiInpatientBalancehead() {
	}

	/** full constructor */
	public BiInpatientBalancehead(Boolean transType, String inpatientNo,
			Short balanceNo, Boolean paykindCode, String pactCode,
			Double prepayCost, Double changePrepaycost, Double totCost,
			Double ownCost, Double payCost, Double pubCost, Double ecoCost,
			Double derCost, Double changeTotcost, Double supplyCost,
			Double returnCost, Double foregiftCost, Date beginDate,
			Date endDate, Boolean balanceType, String balanceOpercode,
			Date balanceDate, Double accountPay, Double officePay,
			Double largePay, Double miltaryPay, Double cashPay, String name,
			String balanceoperDeptcode, Double bursaryAdjustovertop,
			String checkOpcd, Date checkDate, Boolean daybalanceFlag,
			String daybalanceNo, String daybalanceOpcd, Date daybalanceDate,
			Date createTime, Date updateTime) {
		this.transType = transType;
		this.inpatientNo = inpatientNo;
		this.balanceNo = balanceNo;
		this.paykindCode = paykindCode;
		this.pactCode = pactCode;
		this.prepayCost = prepayCost;
		this.changePrepaycost = changePrepaycost;
		this.totCost = totCost;
		this.ownCost = ownCost;
		this.payCost = payCost;
		this.pubCost = pubCost;
		this.ecoCost = ecoCost;
		this.derCost = derCost;
		this.changeTotcost = changeTotcost;
		this.supplyCost = supplyCost;
		this.returnCost = returnCost;
		this.foregiftCost = foregiftCost;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.balanceType = balanceType;
		this.balanceOpercode = balanceOpercode;
		this.balanceDate = balanceDate;
		this.accountPay = accountPay;
		this.officePay = officePay;
		this.largePay = largePay;
		this.miltaryPay = miltaryPay;
		this.cashPay = cashPay;
		this.name = name;
		this.balanceoperDeptcode = balanceoperDeptcode;
		this.bursaryAdjustovertop = bursaryAdjustovertop;
		this.checkOpcd = checkOpcd;
		this.checkDate = checkDate;
		this.daybalanceFlag = daybalanceFlag;
		this.daybalanceNo = daybalanceNo;
		this.daybalanceOpcd = daybalanceOpcd;
		this.daybalanceDate = daybalanceDate;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	// Property accessors

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Boolean getTransType() {
		return this.transType;
	}

	public void setTransType(Boolean transType) {
		this.transType = transType;
	}

	public String getInpatientNo() {
		return this.inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public Short getBalanceNo() {
		return this.balanceNo;
	}

	public void setBalanceNo(Short balanceNo) {
		this.balanceNo = balanceNo;
	}

	public Boolean getPaykindCode() {
		return this.paykindCode;
	}

	public void setPaykindCode(Boolean paykindCode) {
		this.paykindCode = paykindCode;
	}

	public String getPactCode() {
		return this.pactCode;
	}

	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}

	public Double getPrepayCost() {
		return this.prepayCost;
	}

	public void setPrepayCost(Double prepayCost) {
		this.prepayCost = prepayCost;
	}

	public Double getChangePrepaycost() {
		return this.changePrepaycost;
	}

	public void setChangePrepaycost(Double changePrepaycost) {
		this.changePrepaycost = changePrepaycost;
	}

	public Double getTotCost() {
		return this.totCost;
	}

	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}

	public Double getOwnCost() {
		return this.ownCost;
	}

	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}

	public Double getPayCost() {
		return this.payCost;
	}

	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}

	public Double getPubCost() {
		return this.pubCost;
	}

	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}

	public Double getEcoCost() {
		return this.ecoCost;
	}

	public void setEcoCost(Double ecoCost) {
		this.ecoCost = ecoCost;
	}

	public Double getDerCost() {
		return this.derCost;
	}

	public void setDerCost(Double derCost) {
		this.derCost = derCost;
	}

	public Double getChangeTotcost() {
		return this.changeTotcost;
	}

	public void setChangeTotcost(Double changeTotcost) {
		this.changeTotcost = changeTotcost;
	}

	public Double getSupplyCost() {
		return this.supplyCost;
	}

	public void setSupplyCost(Double supplyCost) {
		this.supplyCost = supplyCost;
	}

	public Double getReturnCost() {
		return this.returnCost;
	}

	public void setReturnCost(Double returnCost) {
		this.returnCost = returnCost;
	}

	public Double getForegiftCost() {
		return this.foregiftCost;
	}

	public void setForegiftCost(Double foregiftCost) {
		this.foregiftCost = foregiftCost;
	}

	public Date getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getBalanceType() {
		return this.balanceType;
	}

	public void setBalanceType(Boolean balanceType) {
		this.balanceType = balanceType;
	}

	public String getBalanceOpercode() {
		return this.balanceOpercode;
	}

	public void setBalanceOpercode(String balanceOpercode) {
		this.balanceOpercode = balanceOpercode;
	}

	public Date getBalanceDate() {
		return this.balanceDate;
	}

	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}

	public Double getAccountPay() {
		return this.accountPay;
	}

	public void setAccountPay(Double accountPay) {
		this.accountPay = accountPay;
	}

	public Double getOfficePay() {
		return this.officePay;
	}

	public void setOfficePay(Double officePay) {
		this.officePay = officePay;
	}

	public Double getLargePay() {
		return this.largePay;
	}

	public void setLargePay(Double largePay) {
		this.largePay = largePay;
	}

	public Double getMiltaryPay() {
		return this.miltaryPay;
	}

	public void setMiltaryPay(Double miltaryPay) {
		this.miltaryPay = miltaryPay;
	}

	public Double getCashPay() {
		return this.cashPay;
	}

	public void setCashPay(Double cashPay) {
		this.cashPay = cashPay;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBalanceoperDeptcode() {
		return this.balanceoperDeptcode;
	}

	public void setBalanceoperDeptcode(String balanceoperDeptcode) {
		this.balanceoperDeptcode = balanceoperDeptcode;
	}

	public Double getBursaryAdjustovertop() {
		return this.bursaryAdjustovertop;
	}

	public void setBursaryAdjustovertop(Double bursaryAdjustovertop) {
		this.bursaryAdjustovertop = bursaryAdjustovertop;
	}

	public String getCheckOpcd() {
		return this.checkOpcd;
	}

	public void setCheckOpcd(String checkOpcd) {
		this.checkOpcd = checkOpcd;
	}

	public Date getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Boolean getDaybalanceFlag() {
		return this.daybalanceFlag;
	}

	public void setDaybalanceFlag(Boolean daybalanceFlag) {
		this.daybalanceFlag = daybalanceFlag;
	}

	public String getDaybalanceNo() {
		return this.daybalanceNo;
	}

	public void setDaybalanceNo(String daybalanceNo) {
		this.daybalanceNo = daybalanceNo;
	}

	public String getDaybalanceOpcd() {
		return this.daybalanceOpcd;
	}

	public void setDaybalanceOpcd(String daybalanceOpcd) {
		this.daybalanceOpcd = daybalanceOpcd;
	}

	public Date getDaybalanceDate() {
		return this.daybalanceDate;
	}

	public void setDaybalanceDate(Date daybalanceDate) {
		this.daybalanceDate = daybalanceDate;
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