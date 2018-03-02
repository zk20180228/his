package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @className：OutpatientItemlist 
 * @Description：  门诊非药品明细表
 * @Author：aizhonghua
 * @CreateDate：2015-12-2 上午10:33:41  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-12-2 上午10:33:41  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class OutpatientItemlist extends Entity {
	
	private static final long serialVersionUID = 1L;
	
	/** 处方号 **/
	private String recipeNo;
	/** 处方内项目流水号 **/
	private Integer sequenceNo;
	/** 交易类型,1正交易，2反交易 **/
	private Integer transType;
	/** 住院流水号 **/
	private String outpatientNo;
	/** 姓名  **/
	private String name;
	/** 结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干  **/
	private Integer paykindCode;
	/** 合同单位  **/
	private String pactCode;
	/** 更新库存的流水号(物资) **/
	private Integer updateSequenceno;
	/** 护士站代码  **/
	private String nurseCellCode;
	/** 开立科室代码  **/
	private String recipeDeptcode;
	/** 执行科室代码  **/
	private String executeDeptcode;
	/** 扣库科室代码  **/
	private String stockDeptcode;
	/** 开立医师代码  **/
	private String recipeDoccode;
	/** 项目代码  **/
	private String itemCode;
	/** 最小费用代码  **/
	private String feeCode;
	/** 中心代码  **/
	private String centerCode;
	/** 项目名称  **/
	private String itemName;
	/** 单价  **/
	private Double unitPrice;
	/** 数量  **/
	private Integer qty;
	/** 当前单位  **/
	private String currentUnit;
	/** 组套代码  **/
	private String packageCode;
	/** 组套名称  **/
	private String packageName;
	/** 费用金额  **/
	private Double totCost;
	/** 自费金额  **/
	private Double ownCost;
	/** 自付金额  **/
	private Double payCost;
	/** 公费金额  **/
	private Double pubCost;
	/** 优惠金额  **/
	private Double ecoCost;
	/** 出库单序列号  **/
	private Integer sendmatSequence;
	/** 发放状态（0 划价 2发放（执行） 1 批费）  **/
	private Integer sendFlag;
	/** 是否婴儿用 0 不是 1 是  **/
	private Integer babyFlag;
	/** 急诊抢救标志  **/
	private Integer jzqjFlag;
	/** 扩展标志(公费患者是否使用了自费的项目0否,1是) **/
	private Integer extFlag;
	/** 结算发票号  **/
	private String invoiceNo;
	/** 结算序号  **/
	private Integer balanceNo;
	/** 结算状态  **/
	private Integer balanceState;
	/** 可退数量  **/
	private Integer nobackNum;
	/** 扩展代码(中山一：保存退费原记录的处方号) **/
	private String extCode;
	/** 扩展操作员  **/
	private String extOpercode;
	/** 扩展日期  **/
	private Date extDate;
	/** 审批号(中山一：退费时保存退费申请单号) **/
	private String apprno;
	/** 划价人 **/
	private String chargeOpercode;
	/** 划价日期 **/
	private Date chargeDate;
	/** 已确认数  **/
	private Integer confirmNum;
	/** 设备号 **/
	private String machineNo;
	/** 执行人代码  **/
	private String execOpercode;
	/** 执行日期  **/
	private Date execDate;
	/** 发放人  **/
	private String sendOpercode;
	/** 计费人  **/
	private String feeOpercode;
	/** 计费日期  **/
	private Date feeDate;
	/** 发放日期  **/
	private Date sendDate;
	/** 审核人 **/
	private String checkOpercode;
	/** 审核序号 **/
	private String checkNo;
	/** 医嘱流水号  **/
	private String moOrder;
	/** 医嘱执行单流水号  **/
	private String moExecSqn;
	/** 收费比率  **/
	private Double feeRate;
	/** 收费员科室  **/
	private String feeoperDeptcode;
	/** 上传标志  **/
	private Integer uploadFlag;
	/** 扩展标志1  **/
	private Integer extFlag1;
	/** 扩展标志2(收费方式0门诊直接收费,1护士站医嘱收费,2确认收费,3身份变更,4比例调整) 5 终端确认收费 6终端取消  **/
	private Integer extFlag2;
	/** 聊城市医保新增(记录凭单号) **/
	private String extFlag3;
	/** 0非药品 2物资  **/
	private Integer itemFlag;
	/** 医疗组  **/
	private String medicalteamCode;
	/** 手术编码  **/
	private String operationno;
	/** 医保交易流水号  **/
	private String transactionSequenceNumber;
	/** 医保交易时间  **/
	private Date siTransactionDatetime;
	/** HIS处方号  **/
	private String hisRecipeNo;
	/** 医保处方号  **/
	private String siRecipeNo;
	/** HIS原处方号  **/
	private String hisCancelRecipeNo;
	/** 医保原处方号  **/
	private String siCancelRecipeNo;
	
	//添加字段
	private String caseNo;//病历号
	
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public String getOutpatientNo() {
		return outpatientNo;
	}
	public void setOutpatientNo(String outpatientNo) {
		this.outpatientNo = outpatientNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPaykindCode() {
		return paykindCode;
	}
	public void setPaykindCode(Integer paykindCode) {
		this.paykindCode = paykindCode;
	}
	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	public Integer getUpdateSequenceno() {
		return updateSequenceno;
	}
	public void setUpdateSequenceno(Integer updateSequenceno) {
		this.updateSequenceno = updateSequenceno;
	}
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public String getRecipeDeptcode() {
		return recipeDeptcode;
	}
	public void setRecipeDeptcode(String recipeDeptcode) {
		this.recipeDeptcode = recipeDeptcode;
	}
	public String getExecuteDeptcode() {
		return executeDeptcode;
	}
	public void setExecuteDeptcode(String executeDeptcode) {
		this.executeDeptcode = executeDeptcode;
	}
	public String getStockDeptcode() {
		return stockDeptcode;
	}
	public void setStockDeptcode(String stockDeptcode) {
		this.stockDeptcode = stockDeptcode;
	}
	public String getRecipeDoccode() {
		return recipeDoccode;
	}
	public void setRecipeDoccode(String recipeDoccode) {
		this.recipeDoccode = recipeDoccode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public String getCenterCode() {
		return centerCode;
	}
	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public String getCurrentUnit() {
		return currentUnit;
	}
	public void setCurrentUnit(String currentUnit) {
		this.currentUnit = currentUnit;
	}
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public Double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}
	public Double getPubCost() {
		return pubCost;
	}
	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}
	public Double getEcoCost() {
		return ecoCost;
	}
	public void setEcoCost(Double ecoCost) {
		this.ecoCost = ecoCost;
	}
	public Integer getSendmatSequence() {
		return sendmatSequence;
	}
	public void setSendmatSequence(Integer sendmatSequence) {
		this.sendmatSequence = sendmatSequence;
	}
	public Integer getSendFlag() {
		return sendFlag;
	}
	public void setSendFlag(Integer sendFlag) {
		this.sendFlag = sendFlag;
	}
	public Integer getBabyFlag() {
		return babyFlag;
	}
	public void setBabyFlag(Integer babyFlag) {
		this.babyFlag = babyFlag;
	}
	public Integer getJzqjFlag() {
		return jzqjFlag;
	}
	public void setJzqjFlag(Integer jzqjFlag) {
		this.jzqjFlag = jzqjFlag;
	}
	public Integer getExtFlag() {
		return extFlag;
	}
	public void setExtFlag(Integer extFlag) {
		this.extFlag = extFlag;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Integer getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(Integer balanceNo) {
		this.balanceNo = balanceNo;
	}
	public Integer getBalanceState() {
		return balanceState;
	}
	public void setBalanceState(Integer balanceState) {
		this.balanceState = balanceState;
	}
	public Integer getNobackNum() {
		return nobackNum;
	}
	public void setNobackNum(Integer nobackNum) {
		this.nobackNum = nobackNum;
	}
	public String getExtCode() {
		return extCode;
	}
	public void setExtCode(String extCode) {
		this.extCode = extCode;
	}
	public String getExtOpercode() {
		return extOpercode;
	}
	public void setExtOpercode(String extOpercode) {
		this.extOpercode = extOpercode;
	}
	public Date getExtDate() {
		return extDate;
	}
	public void setExtDate(Date extDate) {
		this.extDate = extDate;
	}
	public String getApprno() {
		return apprno;
	}
	public void setApprno(String apprno) {
		this.apprno = apprno;
	}
	public String getChargeOpercode() {
		return chargeOpercode;
	}
	public void setChargeOpercode(String chargeOpercode) {
		this.chargeOpercode = chargeOpercode;
	}
	public Date getChargeDate() {
		return chargeDate;
	}
	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}
	public Integer getConfirmNum() {
		return confirmNum;
	}
	public void setConfirmNum(Integer confirmNum) {
		this.confirmNum = confirmNum;
	}
	public String getMachineNo() {
		return machineNo;
	}
	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}
	public String getExecOpercode() {
		return execOpercode;
	}
	public void setExecOpercode(String execOpercode) {
		this.execOpercode = execOpercode;
	}
	public Date getExecDate() {
		return execDate;
	}
	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}
	public String getSendOpercode() {
		return sendOpercode;
	}
	public void setSendOpercode(String sendOpercode) {
		this.sendOpercode = sendOpercode;
	}
	public String getFeeOpercode() {
		return feeOpercode;
	}
	public void setFeeOpercode(String feeOpercode) {
		this.feeOpercode = feeOpercode;
	}
	public Date getFeeDate() {
		return feeDate;
	}
	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public String getCheckOpercode() {
		return checkOpercode;
	}
	public void setCheckOpercode(String checkOpercode) {
		this.checkOpercode = checkOpercode;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	public String getMoOrder() {
		return moOrder;
	}
	public void setMoOrder(String moOrder) {
		this.moOrder = moOrder;
	}
	public String getMoExecSqn() {
		return moExecSqn;
	}
	public void setMoExecSqn(String moExecSqn) {
		this.moExecSqn = moExecSqn;
	}
	public Double getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(Double feeRate) {
		this.feeRate = feeRate;
	}
	public String getFeeoperDeptcode() {
		return feeoperDeptcode;
	}
	public void setFeeoperDeptcode(String feeoperDeptcode) {
		this.feeoperDeptcode = feeoperDeptcode;
	}
	public Integer getUploadFlag() {
		return uploadFlag;
	}
	public void setUploadFlag(Integer uploadFlag) {
		this.uploadFlag = uploadFlag;
	}
	public Integer getExtFlag1() {
		return extFlag1;
	}
	public void setExtFlag1(Integer extFlag1) {
		this.extFlag1 = extFlag1;
	}
	public Integer getExtFlag2() {
		return extFlag2;
	}
	public void setExtFlag2(Integer extFlag2) {
		this.extFlag2 = extFlag2;
	}
	public String getExtFlag3() {
		return extFlag3;
	}
	public void setExtFlag3(String extFlag3) {
		this.extFlag3 = extFlag3;
	}
	public Integer getItemFlag() {
		return itemFlag;
	}
	public void setItemFlag(Integer itemFlag) {
		this.itemFlag = itemFlag;
	}
	public String getMedicalteamCode() {
		return medicalteamCode;
	}
	public void setMedicalteamCode(String medicalteamCode) {
		this.medicalteamCode = medicalteamCode;
	}
	public String getOperationno() {
		return operationno;
	}
	public void setOperationno(String operationno) {
		this.operationno = operationno;
	}
	public String getTransactionSequenceNumber() {
		return transactionSequenceNumber;
	}
	public void setTransactionSequenceNumber(String transactionSequenceNumber) {
		this.transactionSequenceNumber = transactionSequenceNumber;
	}
	public Date getSiTransactionDatetime() {
		return siTransactionDatetime;
	}
	public void setSiTransactionDatetime(Date siTransactionDatetime) {
		this.siTransactionDatetime = siTransactionDatetime;
	}
	public String getHisRecipeNo() {
		return hisRecipeNo;
	}
	public void setHisRecipeNo(String hisRecipeNo) {
		this.hisRecipeNo = hisRecipeNo;
	}
	public String getSiRecipeNo() {
		return siRecipeNo;
	}
	public void setSiRecipeNo(String siRecipeNo) {
		this.siRecipeNo = siRecipeNo;
	}
	public String getHisCancelRecipeNo() {
		return hisCancelRecipeNo;
	}
	public void setHisCancelRecipeNo(String hisCancelRecipeNo) {
		this.hisCancelRecipeNo = hisCancelRecipeNo;
	}
	public String getSiCancelRecipeNo() {
		return siCancelRecipeNo;
	}
	public void setSiCancelRecipeNo(String siCancelRecipeNo) {
		this.siCancelRecipeNo = siCancelRecipeNo;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

}