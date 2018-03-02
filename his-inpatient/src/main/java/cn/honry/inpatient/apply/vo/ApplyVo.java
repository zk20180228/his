package cn.honry.inpatient.apply.vo;

import java.util.Date;

/** 
 * @ClassName: ApplyVo 
 * @Description: 
 * @author wfj
 * @date 2016年4月25日 下午4:34:59 
 *  
 */
public class ApplyVo {

	private String id;  //主键id
	private String drugName;  //药品名称
	private String specs;  //规格
	private String costName;  //费用名称 (最小费用)
	private Double price;  //价格(单价*最小费用*数量)
	private Integer nobackNum;  //可退数量
	private String unit;   //当前单位
	private Double moneySum;  //金额(费用金额-自费金额-自付金额-公费金额-优惠金额)
	private String executeDept;  //执行科室
	private String recipeDoc;  //开立医师
	private Date execDate;   //记账日期
	private String drugCode;   //药品编码
	private String moOrder;  //医嘱流水号
	private String moExecSqn;  //医嘱执行单流水号
	private String recipeNo;  //处方号
	private Integer sequenceNo;  //处方流水号
	private Integer packQty;  //包装数
	private String isUnit;    //是否包装单位（当前单位？包装单位：最小单位）
	private String pinyin;  //药品拼音码
	private String recipeDept;  //开立科室
	
	private String isDispensing;   //是否发药
	private String senddrugOpercode;    //发药人
	private Date senddrugDate;      //发药时间
	private String drugPackagingunit; //包装单位  （用来判断是否是和当前单位相同的）
	private String minUnit;  //最小单位
	private String packagingUnit;  //包装单位
	private Double num;   //原退费数量
	//查询条件
	private String name;  //患者名称
	private String inhosDept;   //住院科室
	private String pactCode;  //合同单位
	private String medicalrecordId; //病历号
	private String bedId;  //床号
	private String inpatientNo; //住院流水号
	private String endDate;     //结束时间
	private String firstDate;   //开始时间
	
	private String objName;   //项目名称
	private String objCode;    //项目编号
	private String execPerson;  //记账人
	
	private String execOpercode;  //执行人
	private Date exeDate;   //执行时间
	private String deptcode;  //取药科室
	
	private String isExecute;   //是否执行（根据执行人和执行时间来判断是否是执行人）
	
	private Integer quantity;  //退费数量   （退费表）
	private Integer  confirmFlag;  //退药标识
	private Integer chargeFlag;    //退费标识
	private Double chargeMoney;   //退费金额
	private String isReturnsApply;   //是否退费申请
	private Integer senddrugFlag;   //摆药状态(发药)
	private Integer  applyState;   //出库申请状态
	private String sendmatSequence;   //出库单
	private String invoiceNo;  //标识
	private Double qty;  //退费数量
	private String applyNo; //退费主键
	private Integer babyFlag;  //发药状态
	private Integer drugFlag;   //药品标志,1药品/2非药
	private Integer balanceState;  //结算状态
	private String bedName;   //病床名称
	private String reportSex;   //性别
	private String reportSexName;   //性别
	private String certificatesNo; //身份证号
	private String operationId;//手术序号
	private String extFlag3;//单位标示
	private String showUnit;//显示单位
	public String getShowUnit() {
		return showUnit;
	}
	public void setShowUnit(String showUnit) {
		this.showUnit = showUnit;
	}
	public String getExtFlag3() {
		return extFlag3;
	}
	public void setExtFlag3(String extFlag3) {
		this.extFlag3 = extFlag3;
	}
	/*编码   （暂存的是药品和非药品的自定义码）*/
	private String code;
	/*出库流水号*/
	private String updateSequenceno;
	/*0非药品 ,2物资*/
	private Integer itemFlag;
	/*库存流水号*/
	private String stockNo;
	/*执行科室名称*/
	private String executeDeptName;
	/*开立医生名称*/
	private String recipeDocName;
	/*开立科室名称*/
	private String recipeDeptName;
	/*住院科室名称*/
	private String inhosDeptName;
	
	
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	public String getInhosDeptName() {
		return inhosDeptName;
	}
	public void setInhosDeptName(String inhosDeptName) {
		this.inhosDeptName = inhosDeptName;
	}
	public String getExecuteDeptName() {
		return executeDeptName;
	}
	public void setExecuteDeptName(String executeDeptName) {
		this.executeDeptName = executeDeptName;
	}
	public String getRecipeDocName() {
		return recipeDocName;
	}
	public void setRecipeDocName(String recipeDocName) {
		this.recipeDocName = recipeDocName;
	}
	public String getRecipeDeptName() {
		return recipeDeptName;
	}
	public void setRecipeDeptName(String recipeDeptName) {
		this.recipeDeptName = recipeDeptName;
	}
	public String getReportSexName() {
		return reportSexName;
	}
	public void setReportSexName(String reportSexName) {
		this.reportSexName = reportSexName;
	}
	public String getStockNo() {
		return stockNo;
	}
	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}
	public Integer getItemFlag() {
		return itemFlag;
	}
	public void setItemFlag(Integer itemFlag) {
		this.itemFlag = itemFlag;
	}
	public String getUpdateSequenceno() {
		return updateSequenceno;
	}
	public void setUpdateSequenceno(String updateSequenceno) {
		this.updateSequenceno = updateSequenceno;
	}
	public String getReportSex() {
		return reportSex;
	}
	public void setReportSex(String reportSex) {
		this.reportSex = reportSex;
	}
	public String getCertificatesNo() {
		return certificatesNo;
	}
	public void setCertificatesNo(String certificatesNo) {
		this.certificatesNo = certificatesNo;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public Integer getBalanceState() {
		return balanceState;
	}
	public void setBalanceState(Integer balanceState) {
		this.balanceState = balanceState;
	}
	public Double getNum() {
		return num;
	}
	public void setNum(Double num) {
		this.num = num;
	}
	public Integer getDrugFlag() {
		return drugFlag;
	}
	public void setDrugFlag(Integer drugFlag) {
		this.drugFlag = drugFlag;
	}
	public Integer getBabyFlag() {
		return babyFlag;
	}
	public void setBabyFlag(Integer babyFlag) {
		this.babyFlag = babyFlag;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public String getSendmatSequence() {
		return sendmatSequence;
	}
	public void setSendmatSequence(String sendmatSequence) {
		this.sendmatSequence = sendmatSequence;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getDeptcode() {
		return deptcode;
	}
	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}
	public Integer getSenddrugFlag() {
		return senddrugFlag;
	}
	public void setSenddrugFlag(Integer senddrugFlag) {
		this.senddrugFlag = senddrugFlag;
	}
	public Integer getApplyState() {
		return applyState;
	}
	public void setApplyState(Integer applyState) {
		this.applyState = applyState;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getFirstDate() {
		return firstDate;
	}
	public Double getChargeMoney() {
		return chargeMoney;
	}
	public void setFirstDate(String firstDate) {
		this.firstDate = firstDate;
	}
	public void setChargeMoney(Double chargeMoney) {
		this.chargeMoney = chargeMoney;
	}
	public String getSenddrugOpercode() {
		return senddrugOpercode;
	}
	public void setSenddrugOpercode(String senddrugOpercode) {
		this.senddrugOpercode = senddrugOpercode;
	}
	public Date getSenddrugDate() {
		return senddrugDate;
	}
	public void setSenddrugDate(Date senddrugDate) {
		this.senddrugDate = senddrugDate;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public String getCostName() {
		return costName;
	}
	public void setCostName(String costName) {
		this.costName = costName;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getNobackNum() {
		return nobackNum;
	}
	public void setNobackNum(Integer nobackNum) {
		this.nobackNum = nobackNum;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getMoneySum() {
		return moneySum;
	}
	public void setMoneySum(Double moneySum) {
		this.moneySum = moneySum;
	}
	public String getExecuteDept() {
		return executeDept;
	}
	public void setExecuteDept(String executeDept) {
		this.executeDept = executeDept;
	}
	public String getRecipeDoc() {
		return recipeDoc;
	}
	public void setRecipeDoc(String recipeDoc) {
		this.recipeDoc = recipeDoc;
	}
	public Date getExecDate() {
		return execDate;
	}
	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
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
	public Integer getPackQty() {
		return packQty;
	}
	public void setPackQty(Integer packQty) {
		this.packQty = packQty;
	}
	public String getIsUnit() {
		return isUnit;
	}
	public void setIsUnit(String isUnit) {
		this.isUnit = isUnit;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getRecipeDept() {
		return recipeDept;
	}
	public void setRecipeDept(String recipeDept) {
		this.recipeDept = recipeDept;
	}
	public String getDrugPackagingunit() {
		return drugPackagingunit;
	}
	public void setDrugPackagingunit(String drugPackagingunit) {
		this.drugPackagingunit = drugPackagingunit;
	}
	public String getIsDispensing() {
		return isDispensing;
	}
	public void setIsDispensing(String isDispensing) {
		this.isDispensing = isDispensing;
	}
	public String getMinUnit() {
		return minUnit;
	}
	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}
	public String getPackagingUnit() {
		return packagingUnit;
	}
	public void setPackagingUnit(String packagingUnit) {
		this.packagingUnit = packagingUnit;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInhosDept() {
		return inhosDept;
	}
	public void setInhosDept(String inhosDept) {
		this.inhosDept = inhosDept;
	}

	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getBedId() {
		return bedId;
	}
	public void setBedId(String bedId) {
		this.bedId = bedId;
	}
	public String getObjName() {
		return objName;
	}
	public void setObjName(String objName) {
		this.objName = objName;
	}
	public String getObjCode() {
		return objCode;
	}
	public void setObjCode(String objCode) {
		this.objCode = objCode;
	}
	public String getExecPerson() {
		return execPerson;
	}
	public void setExecPerson(String execPerson) {
		this.execPerson = execPerson;
	}
	public String getExecOpercode() {
		return execOpercode;
	}
	public void setExecOpercode(String execOpercode) {
		this.execOpercode = execOpercode;
	}
	public Date getExeDate() {
		return exeDate;
	}
	public void setExeDate(Date exeDate) {
		this.exeDate = exeDate;
	}
	public String getIsExecute() {
		return isExecute;
	}
	public void setIsExecute(String isExecute) {
		this.isExecute = isExecute;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getConfirmFlag() {
		return confirmFlag;
	}
	public void setConfirmFlag(Integer confirmFlag) {
		this.confirmFlag = confirmFlag;
	}
	public Integer getChargeFlag() {
		return chargeFlag;
	}
	public void setChargeFlag(Integer chargeFlag) {
		this.chargeFlag = chargeFlag;
	}
	public String getIsReturnsApply() {
		return isReturnsApply;
	}
	public void setIsReturnsApply(String isReturnsApply) {
		this.isReturnsApply = isReturnsApply;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
