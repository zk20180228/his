package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiInpatientItemlist entity. @author MyEclipse Persistence Tools
 */

public class BiInpatientItemlist implements java.io.Serializable {

	// Fields

	private BiInpatientItemlistId id;
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
	private String itemCode;
	private String feeCode;
	private String centerCode;
	private String itemName;
	private Double unitPrice;
	private Double qty;
	private String currentUnit;
	private Double totCost;
	private Double ownCost;
	private Double payCost;
	private Double pubCost;
	private Double ecoCost;
	private Boolean sendFlag;
	private Boolean babyFlag;
	private Boolean jzqjFlag;
	private Boolean broughtFlag;
	private String invoiceNo;
	private Short balanceNo;
	private Boolean balanceState;
	private Double nobackNum;
	private String chargeOpercode;
	private Date chargeDate;
	private Double confirmNum;
	private String execOpercode;
	private Date execDate;
	private String sendOpercode;
	private String feeOpercode;
	private Date feeDate;
	private Date sendDate;
	private String moOrder;
	private String moExecSqn;
	private Double feeRate;
	private String feeoperDeptcode;
	private Boolean uploadFlag;
	private Boolean itemFlag;
	private String operationno;
	private String transactionSequenceNumber;
	private Date siTransactionDatetime;
	private String hisRecipeNo;
	private String siRecipeNo;
	private String hisCancelRecipeNo;
	private String siCancelRecipeNo;
	private Date createTime;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public BiInpatientItemlist() {
	}

	/** minimal constructor */
	public BiInpatientItemlist(BiInpatientItemlistId id) {
		this.id = id;
	}

	/** full constructor */
	public BiInpatientItemlist(BiInpatientItemlistId id, Boolean transType,
			String inpatientNo, String name, Boolean paykindCode,
			String pactCode, String inhosDeptcode, String nurseCellCode,
			String recipeDeptcode, String executeDeptcode,
			String stockDeptcode, String recipeDoccode, String itemCode,
			String feeCode, String centerCode, String itemName,
			Double unitPrice, Double qty, String currentUnit, Double totCost,
			Double ownCost, Double payCost, Double pubCost, Double ecoCost,
			Boolean sendFlag, Boolean babyFlag, Boolean jzqjFlag,
			Boolean broughtFlag, String invoiceNo, Short balanceNo,
			Boolean balanceState, Double nobackNum, String chargeOpercode,
			Date chargeDate, Double confirmNum, String execOpercode,
			Date execDate, String sendOpercode, String feeOpercode,
			Date feeDate, Date sendDate, String moOrder, String moExecSqn,
			Double feeRate, String feeoperDeptcode, Boolean uploadFlag,
			Boolean itemFlag, String operationno,
			String transactionSequenceNumber, Date siTransactionDatetime,
			String hisRecipeNo, String siRecipeNo, String hisCancelRecipeNo,
			String siCancelRecipeNo, Date createTime, Date updateTime) {
		this.id = id;
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
		this.itemCode = itemCode;
		this.feeCode = feeCode;
		this.centerCode = centerCode;
		this.itemName = itemName;
		this.unitPrice = unitPrice;
		this.qty = qty;
		this.currentUnit = currentUnit;
		this.totCost = totCost;
		this.ownCost = ownCost;
		this.payCost = payCost;
		this.pubCost = pubCost;
		this.ecoCost = ecoCost;
		this.sendFlag = sendFlag;
		this.babyFlag = babyFlag;
		this.jzqjFlag = jzqjFlag;
		this.broughtFlag = broughtFlag;
		this.invoiceNo = invoiceNo;
		this.balanceNo = balanceNo;
		this.balanceState = balanceState;
		this.nobackNum = nobackNum;
		this.chargeOpercode = chargeOpercode;
		this.chargeDate = chargeDate;
		this.confirmNum = confirmNum;
		this.execOpercode = execOpercode;
		this.execDate = execDate;
		this.sendOpercode = sendOpercode;
		this.feeOpercode = feeOpercode;
		this.feeDate = feeDate;
		this.sendDate = sendDate;
		this.moOrder = moOrder;
		this.moExecSqn = moExecSqn;
		this.feeRate = feeRate;
		this.feeoperDeptcode = feeoperDeptcode;
		this.uploadFlag = uploadFlag;
		this.itemFlag = itemFlag;
		this.operationno = operationno;
		this.transactionSequenceNumber = transactionSequenceNumber;
		this.siTransactionDatetime = siTransactionDatetime;
		this.hisRecipeNo = hisRecipeNo;
		this.siRecipeNo = siRecipeNo;
		this.hisCancelRecipeNo = hisCancelRecipeNo;
		this.siCancelRecipeNo = siCancelRecipeNo;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	// Property accessors

	public BiInpatientItemlistId getId() {
		return this.id;
	}

	public void setId(BiInpatientItemlistId id) {
		this.id = id;
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

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getFeeCode() {
		return this.feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getCenterCode() {
		return this.centerCode;
	}

	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getQty() {
		return this.qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public String getCurrentUnit() {
		return this.currentUnit;
	}

	public void setCurrentUnit(String currentUnit) {
		this.currentUnit = currentUnit;
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

	public Boolean getSendFlag() {
		return this.sendFlag;
	}

	public void setSendFlag(Boolean sendFlag) {
		this.sendFlag = sendFlag;
	}

	public Boolean getBabyFlag() {
		return this.babyFlag;
	}

	public void setBabyFlag(Boolean babyFlag) {
		this.babyFlag = babyFlag;
	}

	public Boolean getJzqjFlag() {
		return this.jzqjFlag;
	}

	public void setJzqjFlag(Boolean jzqjFlag) {
		this.jzqjFlag = jzqjFlag;
	}

	public Boolean getBroughtFlag() {
		return this.broughtFlag;
	}

	public void setBroughtFlag(Boolean broughtFlag) {
		this.broughtFlag = broughtFlag;
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

	public Double getNobackNum() {
		return this.nobackNum;
	}

	public void setNobackNum(Double nobackNum) {
		this.nobackNum = nobackNum;
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

	public Double getConfirmNum() {
		return this.confirmNum;
	}

	public void setConfirmNum(Double confirmNum) {
		this.confirmNum = confirmNum;
	}

	public String getExecOpercode() {
		return this.execOpercode;
	}

	public void setExecOpercode(String execOpercode) {
		this.execOpercode = execOpercode;
	}

	public Date getExecDate() {
		return this.execDate;
	}

	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}

	public String getSendOpercode() {
		return this.sendOpercode;
	}

	public void setSendOpercode(String sendOpercode) {
		this.sendOpercode = sendOpercode;
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

	public Date getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getMoOrder() {
		return this.moOrder;
	}

	public void setMoOrder(String moOrder) {
		this.moOrder = moOrder;
	}

	public String getMoExecSqn() {
		return this.moExecSqn;
	}

	public void setMoExecSqn(String moExecSqn) {
		this.moExecSqn = moExecSqn;
	}

	public Double getFeeRate() {
		return this.feeRate;
	}

	public void setFeeRate(Double feeRate) {
		this.feeRate = feeRate;
	}

	public String getFeeoperDeptcode() {
		return this.feeoperDeptcode;
	}

	public void setFeeoperDeptcode(String feeoperDeptcode) {
		this.feeoperDeptcode = feeoperDeptcode;
	}

	public Boolean getUploadFlag() {
		return this.uploadFlag;
	}

	public void setUploadFlag(Boolean uploadFlag) {
		this.uploadFlag = uploadFlag;
	}

	public Boolean getItemFlag() {
		return this.itemFlag;
	}

	public void setItemFlag(Boolean itemFlag) {
		this.itemFlag = itemFlag;
	}

	public String getOperationno() {
		return this.operationno;
	}

	public void setOperationno(String operationno) {
		this.operationno = operationno;
	}

	public String getTransactionSequenceNumber() {
		return this.transactionSequenceNumber;
	}

	public void setTransactionSequenceNumber(String transactionSequenceNumber) {
		this.transactionSequenceNumber = transactionSequenceNumber;
	}

	public Date getSiTransactionDatetime() {
		return this.siTransactionDatetime;
	}

	public void setSiTransactionDatetime(Date siTransactionDatetime) {
		this.siTransactionDatetime = siTransactionDatetime;
	}

	public String getHisRecipeNo() {
		return this.hisRecipeNo;
	}

	public void setHisRecipeNo(String hisRecipeNo) {
		this.hisRecipeNo = hisRecipeNo;
	}

	public String getSiRecipeNo() {
		return this.siRecipeNo;
	}

	public void setSiRecipeNo(String siRecipeNo) {
		this.siRecipeNo = siRecipeNo;
	}

	public String getHisCancelRecipeNo() {
		return this.hisCancelRecipeNo;
	}

	public void setHisCancelRecipeNo(String hisCancelRecipeNo) {
		this.hisCancelRecipeNo = hisCancelRecipeNo;
	}

	public String getSiCancelRecipeNo() {
		return this.siCancelRecipeNo;
	}

	public void setSiCancelRecipeNo(String siCancelRecipeNo) {
		this.siCancelRecipeNo = siCancelRecipeNo;
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