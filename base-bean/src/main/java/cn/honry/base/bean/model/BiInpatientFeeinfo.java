package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiInpatientFeeinfo entity. @author MyEclipse Persistence Tools
 */

public class BiInpatientFeeinfo implements java.io.Serializable {

	// Fields

	private String recipeNo;
	private String feeCode;
	private Boolean transType;
	private String inpatientNo;
	private String name;
	private Boolean paykindCode;
	private String pactCode;
	private String inhosDeptcode;
	private String nurseCellCode;
	private String recipeDeptcode;
	private String executeDeptcode;
	private String stockDeptcode;
	private String recipeDoccode;
	private Double totCost;
	private Double ownCost;
	private Double payCost;
	private Double pubCost;
	private Double ecoCost;
	private String chargeOpercode;
	private Date chargeDate;
	private String feeOpercode;
	private Date feeDate;
	private String balanceOpercode;
	private Date balanceDate;
	private String invoiceNo;
	private Short balanceNo;
	private Boolean balanceState;
	private String checkNo;
	private Boolean babyFlag;
	private String feeoperDeptcode;
	private Date createTime;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public BiInpatientFeeinfo() {
	}

	/** full constructor */
	public BiInpatientFeeinfo(String feeCode, Boolean transType,
			String inpatientNo, String name, Boolean paykindCode,
			String pactCode, String inhosDeptcode, String nurseCellCode,
			String recipeDeptcode, String executeDeptcode,
			String stockDeptcode, String recipeDoccode, Double totCost,
			Double ownCost, Double payCost, Double pubCost, Double ecoCost,
			String chargeOpercode, Date chargeDate, String feeOpercode,
			Date feeDate, String balanceOpercode, Date balanceDate,
			String invoiceNo, Short balanceNo, Boolean balanceState,
			String checkNo, Boolean babyFlag, String feeoperDeptcode,
			Date createTime, Date updateTime) {
		this.feeCode = feeCode;
		this.transType = transType;
		this.inpatientNo = inpatientNo;
		this.name = name;
		this.paykindCode = paykindCode;
		this.pactCode = pactCode;
		this.inhosDeptcode = inhosDeptcode;
		this.nurseCellCode = nurseCellCode;
		this.recipeDeptcode = recipeDeptcode;
		this.executeDeptcode = executeDeptcode;
		this.stockDeptcode = stockDeptcode;
		this.recipeDoccode = recipeDoccode;
		this.totCost = totCost;
		this.ownCost = ownCost;
		this.payCost = payCost;
		this.pubCost = pubCost;
		this.ecoCost = ecoCost;
		this.chargeOpercode = chargeOpercode;
		this.chargeDate = chargeDate;
		this.feeOpercode = feeOpercode;
		this.feeDate = feeDate;
		this.balanceOpercode = balanceOpercode;
		this.balanceDate = balanceDate;
		this.invoiceNo = invoiceNo;
		this.balanceNo = balanceNo;
		this.balanceState = balanceState;
		this.checkNo = checkNo;
		this.babyFlag = babyFlag;
		this.feeoperDeptcode = feeoperDeptcode;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	// Property accessors

	public String getRecipeNo() {
		return this.recipeNo;
	}

	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}

	public String getFeeCode() {
		return this.feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getInhosDeptcode() {
		return this.inhosDeptcode;
	}

	public void setInhosDeptcode(String inhosDeptcode) {
		this.inhosDeptcode = inhosDeptcode;
	}

	public String getNurseCellCode() {
		return this.nurseCellCode;
	}

	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}

	public String getRecipeDeptcode() {
		return this.recipeDeptcode;
	}

	public void setRecipeDeptcode(String recipeDeptcode) {
		this.recipeDeptcode = recipeDeptcode;
	}

	public String getExecuteDeptcode() {
		return this.executeDeptcode;
	}

	public void setExecuteDeptcode(String executeDeptcode) {
		this.executeDeptcode = executeDeptcode;
	}

	public String getStockDeptcode() {
		return this.stockDeptcode;
	}

	public void setStockDeptcode(String stockDeptcode) {
		this.stockDeptcode = stockDeptcode;
	}

	public String getRecipeDoccode() {
		return this.recipeDoccode;
	}

	public void setRecipeDoccode(String recipeDoccode) {
		this.recipeDoccode = recipeDoccode;
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

	public String getChargeOpercode() {
		return this.chargeOpercode;
	}

	public void setChargeOpercode(String chargeOpercode) {
		this.chargeOpercode = chargeOpercode;
	}

	public Date getChargeDate() {
		return this.chargeDate;
	}

	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}

	public String getFeeOpercode() {
		return this.feeOpercode;
	}

	public void setFeeOpercode(String feeOpercode) {
		this.feeOpercode = feeOpercode;
	}

	public Date getFeeDate() {
		return this.feeDate;
	}

	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
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

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Short getBalanceNo() {
		return this.balanceNo;
	}

	public void setBalanceNo(Short balanceNo) {
		this.balanceNo = balanceNo;
	}

	public Boolean getBalanceState() {
		return this.balanceState;
	}

	public void setBalanceState(Boolean balanceState) {
		this.balanceState = balanceState;
	}

	public String getCheckNo() {
		return this.checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public Boolean getBabyFlag() {
		return this.babyFlag;
	}

	public void setBabyFlag(Boolean babyFlag) {
		this.babyFlag = babyFlag;
	}

	public String getFeeoperDeptcode() {
		return this.feeoperDeptcode;
	}

	public void setFeeoperDeptcode(String feeoperDeptcode) {
		this.feeoperDeptcode = feeoperDeptcode;
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