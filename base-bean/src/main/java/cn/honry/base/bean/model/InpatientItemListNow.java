package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * @className：InpatientItemList.java
 * @Description：住院非药品明细表
 * @Author：hedong
 * @CreateDate：2015-08-12
 * @version 1.0
 */
public class InpatientItemListNow extends Entity implements
		java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String recipeNo;// 处方号
	private Integer sequenceNo;// 处方内项目流水号
	private Integer transtype;// 交易类型,1正交易，2反交易
	private String inpatientNo;// 住院流水号
	private String name;// 姓名 VARCHAR2(20),
	private String paykindCode;// 结算类别 01-自费 02-保险 03-公费在职 04-公费退休 05-公费高干
	private String pactCode;// 合同单位
	private String updateSequenceno;// 更新库存的流水号(物资)
	private String inhosDeptcode;// 在院科室代码
	private String nurseCellCode;// 护士站代码
	private String recipeDeptcode;// 开立科室代码
	private String executeDeptcode;// 执行科室代码
	private String stockDeptcode;// 扣库科室代码
	private String recipeDoccode;// 开立医师代码
	private String itemCode;// 项目代码
	private String feeCode;// 最小费用代码
	private String centerCode;// 中心代码
	private String itemName;// 项目名称
	private Double unitPrice;// 单价
	private Double qty;// 数量
	private String currentUnit;// 当前单位
	private String packageCode;// 组套代码
	private String packageName;// 组套名称
	private Double totCost;// 费用金额
	private Double ownCost;// 自费金额
	private Double payCost;// 自付金额
	private Double pubCost;// 公费金额
	private Double ecoCost;// 优惠金额
	private String sendmatSequence;// 出库单序列号
	private Integer sendFlag;// 发放状态（0 划价 2发放（执行） 1 批费）
	private Integer babyFlag;// 是否婴儿用 0 不是 1 是
	private Integer jzqjFlag;// 急诊抢救标志
	private Integer broughtFlag;// 出院带疗标记 0 否 1 是
	private Integer extFlag;// 扩展标志(公费患者是否使用了自费的项目0否,1是)
	private String invoiceNo;// 结算发票号
	private Integer balanceNo;// 结算序号
	private Integer balanceState;// 结算状态
	private Double nobackNum;// 可退数量
	private String extCode;// 扩展代码(中山一：保存退费原记录的处方号)
	private String extOpercode;// 扩展操作员
	private Date extDate;// 扩展日期
	private String apprno;// 审批号(中山一：退费时保存退费申请单号)
	private String chargeOpercode;// 划价人
	private Date chargeDate;// 划价日期
	private Double confirmNum;// 已确认数
	private String machineNo;// 设备号
	private String execOpercode;// 执行人代码
	private Date execDate;// 执行日期
	private String sendOpercode;// 发放人
	private String feeOpercode;// 计费人
	private Date feeDate;// 计费日期
	private Date sendDate;// 发放日期
	private String checkOpercode;// 审核人
	private String checkNo;// 审核序号
	private String moOrder;// 医嘱流水号
	private String moExecSqn;// 医嘱执行单流水号
	private Double feeRate;// 收费比率
	private String feeoperDeptcode;// 收费员科室
	private Integer uploadFlag;// 上传标志
	private Integer extFlag1;// 扩展标志1
	private Integer extFlag2;// 扩展标志2(收费方式0住院处直接收费,1护士站医嘱收费,2确认收费,3身份变更,4比例调整) 5
								// 终端确认收费 6终端取消
	private String extFlag3;// 聊城市医保新增(记录凭单号)
	private Integer itemFlag;// 0非药品 2物资
	private String medicalteamCode;// 医疗组
	private String operationno;// 手术编码
	private String transactionSequenceNumber;// 医保交易流水号
	private Date siTransactionDatetime;// 医保交易时间
	private String hisRecipeNo;// HIS处方号
	private String siRecipeNo;// 医保处方号
	private String hisCancelRecipeNo;// HIS原处方号
	private String siCancelRecipeNo;// 医保原处方号
	private String operationId;// 手术序号

	/** 医院编码 **/
	private Integer hospitalId;
	/** 院区编码 **/
	private String areaCode;
	// 新加字段
	/** 在院科室名称 **/
	private String inhosDeptname;
	/** 护士站名称 **/
	private String nurseCellName;
	/** 开立科室名称 **/
	private String recipeDeptname;
	/** 执行科室名称 **/
	private String executeDeptname;
	/** 扣库科室名称 **/
	private String stockDeptname;
	/** 开立医师名称 **/
	private String recipeDocname;
	/** 最小费用名称 **/
	private String feeName;
	/** 中心名称 **/
	private String centerName;
	/**
	 * 国家编码
	 */
	private String undrugGbcode;
	// 规格
	private String spec;

	public String getInhosDeptname() {
		return inhosDeptname;
	}

	public void setInhosDeptname(String inhosDeptname) {
		this.inhosDeptname = inhosDeptname;
	}

	public String getNurseCellName() {
		return nurseCellName;
	}
	
	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public void setNurseCellName(String nurseCellName) {
		this.nurseCellName = nurseCellName;
	}

	public String getRecipeDeptname() {
		return recipeDeptname;
	}

	public void setRecipeDeptname(String recipeDeptname) {
		this.recipeDeptname = recipeDeptname;
	}

	public String getExecuteDeptname() {
		return executeDeptname;
	}

	public void setExecuteDeptname(String executeDeptname) {
		this.executeDeptname = executeDeptname;
	}

	public String getStockDeptname() {
		return stockDeptname;
	}

	public void setStockDeptname(String stockDeptname) {
		this.stockDeptname = stockDeptname;
	}

	public String getRecipeDocname() {
		return recipeDocname;
	}

	public void setRecipeDocname(String recipeDocname) {
		this.recipeDocname = recipeDocname;
	}

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

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

	public Integer getTranstype() {
		return transtype;
	}

	public void setTranstype(Integer transtype) {
		this.transtype = transtype;
	}

	public String getInpatientNo() {
		return inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPaykindCode() {
		return paykindCode;
	}

	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	}

	public String getPactCode() {
		return pactCode;
	}

	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}

	public String getUpdateSequenceno() {
		return updateSequenceno;
	}

	public void setUpdateSequenceno(String updateSequenceno) {
		this.updateSequenceno = updateSequenceno;
	}

	public String getInhosDeptcode() {
		return inhosDeptcode;
	}

	public void setInhosDeptcode(String inhosDeptcode) {
		this.inhosDeptcode = inhosDeptcode;
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

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
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

	public String getSendmatSequence() {
		return sendmatSequence;
	}

	public void setSendmatSequence(String sendmatSequence) {
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

	public Integer getBroughtFlag() {
		return broughtFlag;
	}

	public void setBroughtFlag(Integer broughtFlag) {
		this.broughtFlag = broughtFlag;
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

	public Double getNobackNum() {
		return nobackNum;
	}

	public void setNobackNum(Double nobackNum) {
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

	public Double getConfirmNum() {
		return confirmNum;
	}

	public void setConfirmNum(Double confirmNum) {
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

	public String getUndrugGbcode() {
		return undrugGbcode;
	}

	public void setUndrugGbcode(String undrugGbcode) {
		this.undrugGbcode = undrugGbcode;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

}
