package cn.honry.base.bean.model;

import java.util.Date;

import org.omg.PortableInterceptor.INACTIVE;

import cn.honry.base.bean.business.Entity;

@SuppressWarnings("all")
public class DrugApplyout extends Entity implements java.io.Serializable {
	/**申请流水号*/
	private Long applyNumber;
	/**申请部门编码（科室或者病区）*/
	private String deptCode;
	/**发药部门编码*/
	private String drugDeptCode;
	/**操作类型分类  1 门诊发药 ， 2 内部入库,3 门诊退药，4 住院摆药 ,5住院退药*/
	private Integer opType;
	/**批次号*/
	private String groupCode;
	/**药品编码*/
	private String drugCode;
	/**药品商品名*/
	private String tradeName;
	/**批号*/
	private String batchNo;
	/**药品类别*/
	private String drugType;
	/**药品性质*/
	private String drugQuality;
	/**规格*/
	private String specs;
	/**包装单位*/
	private String packUnit;
	/**包装数*/
	private Integer packQty;
	/**最小单位*/
	private String minUnit;
	/**显示的单位标记(0最小单位,1包装单位)*/
	private Integer showFlag;
	/**显示的单位*/
	private String showUnit;
	/**零售价*/
	private Double retailPrice;
	/**批发价*/
	private Double wholesalePrice;
	/**购入价*/
	private Double purchasePrice;
	/**申请单号*/
	private String applyBillcode;
	/**申请人编码*/
	private String applyOpercode;
	/**申请日期*/
	private Date applyDate;
	/**申请状态 0申请，1（配药）打印，2核准（出库），3作废，4暂不摆药*/
	private Integer applyState;
	/**申请出库量(每付的总数量)*/
	private Double applyNum;
	/**付数（草药）*/
	private Integer days;
	/**是否预扣库存1是0否*/
	private Integer preoutFlag=0;
	/**收费状态：0未收费，1已收费*/
	private Integer chargeFlag;
	/**患者编号*/
	private String patientId;
	/**患者科室*/
	private String patientDept;
	/**摆药单号*/
	private String drugedBill;
	/**摆药科室*/
	private String drugedDept;
	/**摆药人*/
	private String drugedEmpl;
	/**摆药日期*/
	private Date drugedDate;
	/**摆药数量*/
	private Double drugedNum;
	/**每次剂量*/
	private Double doseOnce;
	/**剂量单位*/
	private String doseUnit;
	/**用法代码*/
	private String usageCode;
	/**用法名称*/
	private String useName;
	/**频次代码*/
	private String dfqFreq;
	/**频次名称*/
	private String dfqCexp;
	/**剂型代码*/
	private String doseModelCode;
	/**医嘱类别*/
	private String orderType;
	/**医嘱流水号*/
	private String moOrder;
	/**组合序号*/
	private String combNo;
	/**执行单流水号*/
	private String execSqn;
	/**处方号*/
	private String recipeNo;
	/**处方内项目流水号*/
	private Integer sequenceNo;
	/**医嘱发送类型2临时，1集中，0未发送*/
	private Integer sendType;
	/**摆药单分类代码 */
	private String billclassCode;
	/**打印状态（0未打印，1已打印）*/
	private Integer printState;
	/**门诊调剂标记1是0否*/
	private Integer relieveFlag;
	/**调剂单流水号*/
	private String relieveCode;
	/**操作员（打印人）*/
	private String printEmpl;
	/**操作日期（打印时间）*/
	private Date printDate;
	/**出库单流水号（退库申请时，保存申请退库记录的出库单流水号号）*/
	private Long outBillCode;
	/**有效标记（1有效，0无效，2不摆药）*/
	private Integer validState;
	/**备注*/
	private String mark;
	/**取消操作员*/
	private String cancelEmpl;
	/**取消日期*/
	private Date cancelDate;
	/**货位号*/
	private String placeCode;
	/**开方科室*/
	private String recipeDept;
	/**开方医生*/
	private String recipeOper;
	/**是否婴儿 1 是 0 否*/
	private Integer babyFlag;
	/**扩展字段--1代表护士打印过*/
	private String extFlag;
	/**扩展字段1*/
	private String extFlag1;
	/**批次流水号,根据医嘱执行时间及组合号赋值*/
	private String compoundGroup;
	/**是否需配液 ‘1’ 是 0 否*/
	private Integer compoundFlag;
	/**是否配液已执行 1 是 0 否*/
	private Integer compoundExec;
	/**配液执行人*/
	private String compoundOper;
	/**申配液时间*/
	private Date compoundDate;
	//数据库没有 用来显示操作字段
	private double storeSum;
	private double totalretailPrice;
	private String queshibili;
	/**-Start与数据库无关字段，用于显示药品信息表中的，部分字段-**/
	/**名称**/
	private String drugName;
	/**名称自定义码**/
	private String drugNameinputcode;
	/**名称拼音码**/
	private String drugNamepinyin;
	/**名称五笔码**/
	private String drugNamewb;
	/**通用名称**/
	private String drugCommonname;
	/**通用名称拼音码**/
	private String drugCnamepinyin;
	/**通用名称五笔码**/
	private String drugCnamewb;
	/**通用名称自定义码**/
	private String drugCnameinputcode;
	/**规格**/
	private String drugSpec;
	/**剂型**/
	private String drugDosageform;
	/**零售价**/
	private Double drugRetailprice;
	/**最小单位**/
	private String drugMinimumunit;
	/**包装数量**/
	private Integer drugPackagingnum;
	/**包装单位**/
	private String drugPackagingunit;
	/**厂商id**/
	private String producerCode;
	/**有效期*/
	private Date drugEnddate;
	/**供货单位代码**/
	private String companyCode;
	/**厂商名称**/
	private String manufacturerName;
	/**最低库存量*/
	private Double lowSum;
	/**最高库存量*/
	private Double topSum;
	//只用来显示  申请时间
	private Date applyEnd;  
	/**患者名称*/
	private String patientName;
	/**科室名称*/
	private String deptName;
	/**分类代码名称*/
	private String billclassName;
	/**患者姓名*/
	private String name;
	/**病床号*/
	private String bedno;
	/**病床号*/
	private String bednoId;
	/**药品[规格]*/
	private String specsName;
	/**总钱数*/
	private Double sumCost;
	//新增字段 2016-11-15 10:51:58
	/**申请部门名称*/
	private String  deptCodeName;
	/**发药部门名称*/
	private String drugDeptName;
	/**药品类别名称*/
	private String drugTypeName;
	/**药品性质名称*/
	private String drugQulityName;
	/**申请人姓名*/
	private String applyOpername;
	/**患者科室名称*/
	private String patientDeptName;
	/**摆药科室名称*/
	private String drugedDeptName;
	/**摆药人姓名*/
	private String drugedEmplName;
	/**剂型名称*/
	private String doseModelName;
	/**操作员（打印人）姓名*/
	private String printEmplName;
	/**取消操作员姓名*/
	private String cancelEmplName;
	/**开方科室名称*/
	private String recipeDeptName;
	/**开方医生姓名*/
	private String recipeOperName;
	/**配液执行人姓名**/
	private String compountOperName;
	
	
	public String getSpecsName() {
		return specsName;
	}

	public void setSpecsName(String specsName) {
		this.specsName = specsName;
	}

	public Double getSumCost() {
		return sumCost;
	}

	public void setSumCost(Double sumCost) {
		this.sumCost = sumCost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBedno() {
		return bedno;
	}

	public void setBedno(String bedno) {
		this.bedno = bedno;
	}

	public String getBednoId() {
		return bednoId;
	}

	public void setBednoId(String bednoId) {
		this.bednoId = bednoId;
	}

	public String getBillclassName() {
		return billclassName;
	}

	public void setBillclassName(String billclassName) {
		this.billclassName = billclassName;
	}

	public String getDeptName() {
		return deptName;
	}
	//与数据库无关字段，导出用
	/**药品内部入库申请是的库存数量*/
	private Double storeSumDrug;
	/**药房核准入库导出时的出库数量*/
	private Double outNum;
	/**药房核准入库导出时的出库金额*/
	private Double outlCost;
	/**内部入库退库审核时的入库数量*/
	private Double inNum;
	/**内部入库退库审核时的退回数量*/
	private Double returnNum;
	/**内部入库退库审核时的退回金额*/
	private Double totalReturnCost;
	
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Date getApplyEnd() {
		return applyEnd;
	}

	public void setApplyEnd(Date applyEnd) {
		this.applyEnd = applyEnd;
	}


	
	public Double getLowSum() {
		return lowSum;
	}

	public void setLowSum(Double lowSum) {
		this.lowSum = lowSum;
	}

	public Double getTopSum() {
		return topSum;
	}

	public void setTopSum(Double topSum) {
		this.topSum = topSum;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getProducerCode() {
		return producerCode;
	}

	public void setProducerCode(String producerCode) {
		this.producerCode = producerCode;
	}

	public Date getDrugEnddate() {
		return drugEnddate;
	}

	public void setDrugEnddate(Date drugEnddate) {
		this.drugEnddate = drugEnddate;
	}

	public Double getOutApplyCost() {
		return outApplyCost;
	}

	public void setOutApplyCost(Double outApplyCost) {
		this.outApplyCost = outApplyCost;
	}

	private Double outApplyCost;
	
	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDrugDeptCode() {
		return this.drugDeptCode;
	}

	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}

	public Integer getOpType() {
		return this.opType;
	}

	public void setOpType(Integer opType) {
		this.opType = opType;
	}

	public String getGroupCode() {
		return this.groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getDrugCode() {
		return this.drugCode;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}

	public String getTradeName() {
		return this.tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getDrugType() {
		return this.drugType;
	}

	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}

	public String getDrugQuality() {
		return this.drugQuality;
	}

	public void setDrugQuality(String drugQuality) {
		this.drugQuality = drugQuality;
	}

	public String getSpecs() {
		return this.specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public String getPackUnit() {
		return this.packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public Integer getPackQty() {
		return this.packQty;
	}

	public void setPackQty(Integer packQty) {
		this.packQty = packQty;
	}

	public String getMinUnit() {
		return this.minUnit;
	}

	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}

	public Integer getShowFlag() {
		return showFlag;
	}

	public void setShowFlag(Integer showFlag) {
		this.showFlag = showFlag;
	}

	public String getShowUnit() {
		return this.showUnit;
	}

	public void setShowUnit(String showUnit) {
		this.showUnit = showUnit;
	}

	public Double getRetailPrice() {
		return this.retailPrice;
	}

	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public Double getWholesalePrice() {
		return this.wholesalePrice;
	}

	public void setWholesalePrice(Double wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}

	public Double getPurchasePrice() {
		return this.purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getApplyBillcode() {
		return this.applyBillcode;
	}

	public void setApplyBillcode(String applyBillcode) {
		this.applyBillcode = applyBillcode;
	}

	public String getApplyOpercode() {
		return this.applyOpercode;
	}

	public void setApplyOpercode(String applyOpercode) {
		this.applyOpercode = applyOpercode;
	}

	public Date getApplyDate() {
		return this.applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public long getApplyNumber() {
		return applyNumber;
	}

	public void setApplyNumber(long applyNumber) {
		this.applyNumber = applyNumber;
	}

	public Integer getApplyState() {
		return applyState;
	}

	public void setApplyState(Integer applyState) {
		this.applyState = applyState;
	}

	public Double getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(Double applyNum) {
		this.applyNum = applyNum;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Integer getPreoutFlag() {
		return preoutFlag;
	}

	public void setPreoutFlag(Integer preoutFlag) {
		this.preoutFlag = preoutFlag;
	}

	public Integer getChargeFlag() {
		return chargeFlag;
	}

	public void setChargeFlag(Integer chargeFlag) {
		this.chargeFlag = chargeFlag;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientDept() {
		return patientDept;
	}

	public void setPatientDept(String patientDept) {
		this.patientDept = patientDept;
	}

	public String getDrugedBill() {
		return drugedBill;
	}

	public void setDrugedBill(String drugedBill) {
		this.drugedBill = drugedBill;
	}

	public String getDrugedDept() {
		return drugedDept;
	}

	public void setDrugedDept(String drugedDept) {
		this.drugedDept = drugedDept;
	}

	public String getDrugedEmpl() {
		return drugedEmpl;
	}

	public void setDrugedEmpl(String drugedEmpl) {
		this.drugedEmpl = drugedEmpl;
	}

	public Date getDrugedDate() {
		return drugedDate;
	}

	public void setDrugedDate(Date drugedDate) {
		this.drugedDate = drugedDate;
	}

	public Double getDrugedNum() {
		return drugedNum;
	}

	public void setDrugedNum(Double drugedNum) {
		this.drugedNum = drugedNum;
	}

	public Double getDoseOnce() {
		return doseOnce;
	}

	public void setDoseOnce(Double doseOnce) {
		this.doseOnce = doseOnce;
	}

	public String getDoseUnit() {
		return doseUnit;
	}

	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}

	public String getUsageCode() {
		return usageCode;
	}

	public void setUsageCode(String usageCode) {
		this.usageCode = usageCode;
	}

	public String getUseName() {
		return useName;
	}

	public void setUseName(String useName) {
		this.useName = useName;
	}

	public String getDfqFreq() {
		return dfqFreq;
	}

	public void setDfqFreq(String dfqFreq) {
		this.dfqFreq = dfqFreq;
	}

	public String getDfqCexp() {
		return dfqCexp;
	}

	public void setDfqCexp(String dfqCexp) {
		this.dfqCexp = dfqCexp;
	}

	public String getDoseModelCode() {
		return doseModelCode;
	}

	public void setDoseModelCode(String doseModelCode) {
		this.doseModelCode = doseModelCode;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getMoOrder() {
		return moOrder;
	}

	public void setMoOrder(String moOrder) {
		this.moOrder = moOrder;
	}

	public String getCombNo() {
		return combNo;
	}

	public void setCombNo(String combNo) {
		this.combNo = combNo;
	}

	public String getExecSqn() {
		return execSqn;
	}

	public void setExecSqn(String execSqn) {
		this.execSqn = execSqn;
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

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public String getBillclassCode() {
		return billclassCode;
	}

	public void setBillclassCode(String billclassCode) {
		this.billclassCode = billclassCode;
	}

	public Integer getPrintState() {
		return printState;
	}

	public void setPrintState(Integer printState) {
		this.printState = printState;
	}

	public Integer getRelieveFlag() {
		return relieveFlag;
	}

	public void setRelieveFlag(Integer relieveFlag) {
		this.relieveFlag = relieveFlag;
	}

	public String getRelieveCode() {
		return relieveCode;
	}

	public void setRelieveCode(String relieveCode) {
		this.relieveCode = relieveCode;
	}

	public String getPrintEmpl() {
		return printEmpl;
	}

	public void setPrintEmpl(String printEmpl) {
		this.printEmpl = printEmpl;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public Long getOutBillCode() {
		return outBillCode;
	}

	public void setOutBillCode(Long outBillCode) {
		this.outBillCode = outBillCode;
	}

	public Integer getValidState() {
		return validState;
	}

	public void setValidState(Integer validState) {
		this.validState = validState;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getCancelEmpl() {
		return cancelEmpl;
	}

	public void setCancelEmpl(String cancelEmpl) {
		this.cancelEmpl = cancelEmpl;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getPlaceCode() {
		return placeCode;
	}

	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}

	public String getRecipeDept() {
		return recipeDept;
	}

	public void setRecipeDept(String recipeDept) {
		this.recipeDept = recipeDept;
	}

	public String getRecipeOper() {
		return recipeOper;
	}

	public void setRecipeOper(String recipeOper) {
		this.recipeOper = recipeOper;
	}

	public Integer getBabyFlag() {
		return babyFlag;
	}

	public void setBabyFlag(Integer babyFlag) {
		this.babyFlag = babyFlag;
	}

	public String getExtFlag() {
		return extFlag;
	}

	public void setExtFlag(String extFlag) {
		this.extFlag = extFlag;
	}

	public String getExtFlag1() {
		return extFlag1;
	}

	public void setExtFlag1(String extFlag1) {
		this.extFlag1 = extFlag1;
	}

	public String getCompoundGroup() {
		return compoundGroup;
	}

	public void setCompoundGroup(String compoundGroup) {
		this.compoundGroup = compoundGroup;
	}

	public Integer getCompoundFlag() {
		return compoundFlag;
	}

	public void setCompoundFlag(Integer compoundFlag) {
		this.compoundFlag = compoundFlag;
	}

	public Integer getCompoundExec() {
		return compoundExec;
	}

	public void setCompoundExec(Integer compoundExec) {
		this.compoundExec = compoundExec;
	}

	public String getCompoundOper() {
		return compoundOper;
	}

	public void setCompoundOper(String compoundOper) {
		this.compoundOper = compoundOper;
	}

	public Date getCompoundDate() {
		return compoundDate;
	}

	public void setCompoundDate(Date compoundDate) {
		this.compoundDate = compoundDate;
	}

	public double getStoreSum() {
		return storeSum;
	}

	public void setStoreSum(double storeSum) {
		this.storeSum = storeSum;
	}

	public double getTotalretailPrice() {
		return totalretailPrice;
	}

	public void setTotalretailPrice(double totalretailPrice) {
		this.totalretailPrice = totalretailPrice;
	}

	public String getQueshibili() {
		return queshibili;
	}

	public void setQueshibili(String queshibili) {
		this.queshibili = queshibili;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getDrugNameinputcode() {
		return drugNameinputcode;
	}

	public void setDrugNameinputcode(String drugNameinputcode) {
		this.drugNameinputcode = drugNameinputcode;
	}

	public String getDrugNamepinyin() {
		return drugNamepinyin;
	}

	public void setDrugNamepinyin(String drugNamepinyin) {
		this.drugNamepinyin = drugNamepinyin;
	}

	public String getDrugNamewb() {
		return drugNamewb;
	}

	public void setDrugNamewb(String drugNamewb) {
		this.drugNamewb = drugNamewb;
	}

	public String getDrugCommonname() {
		return drugCommonname;
	}

	public void setDrugCommonname(String drugCommonname) {
		this.drugCommonname = drugCommonname;
	}

	public String getDrugCnamepinyin() {
		return drugCnamepinyin;
	}

	public void setDrugCnamepinyin(String drugCnamepinyin) {
		this.drugCnamepinyin = drugCnamepinyin;
	}

	public String getDrugCnamewb() {
		return drugCnamewb;
	}

	public void setDrugCnamewb(String drugCnamewb) {
		this.drugCnamewb = drugCnamewb;
	}

	public String getDrugCnameinputcode() {
		return drugCnameinputcode;
	}

	public void setDrugCnameinputcode(String drugCnameinputcode) {
		this.drugCnameinputcode = drugCnameinputcode;
	}

	public String getDrugSpec() {
		return drugSpec;
	}

	public void setDrugSpec(String drugSpec) {
		this.drugSpec = drugSpec;
	}

	public String getDrugDosageform() {
		return drugDosageform;
	}

	public void setDrugDosageform(String drugDosageform) {
		this.drugDosageform = drugDosageform;
	}

	public Double getDrugRetailprice() {
		return drugRetailprice;
	}

	public void setDrugRetailprice(Double drugRetailprice) {
		this.drugRetailprice = drugRetailprice;
	}

	public String getDrugMinimumunit() {
		return drugMinimumunit;
	}

	public void setDrugMinimumunit(String drugMinimumunit) {
		this.drugMinimumunit = drugMinimumunit;
	}

	public Integer getDrugPackagingnum() {
		return drugPackagingnum;
	}

	public void setDrugPackagingnum(Integer drugPackagingnum) {
		this.drugPackagingnum = drugPackagingnum;
	}

	public String getDrugPackagingunit() {
		return drugPackagingunit;
	}

	public void setDrugPackagingunit(String drugPackagingunit) {
		this.drugPackagingunit = drugPackagingunit;
	}
	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Double getOutNum() {
		return outNum;
	}

	public void setOutNum(Double outNum) {
		this.outNum = outNum;
	}

	public Double getOutlCost() {
		return outlCost;
	}

	public void setOutlCost(Double outlCost) {
		this.outlCost = outlCost;
	}

	public Double getInNum() {
		return inNum;
	}

	public void setInNum(Double inNum) {
		this.inNum = inNum;
	}

	public Double getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(Double returnNum) {
		this.returnNum = returnNum;
	}

	public Double getTotalReturnCost() {
		return totalReturnCost;
	}

	public void setTotalReturnCost(Double totalReturnCost) {
		this.totalReturnCost = totalReturnCost;
	}

	public Double getStoreSumDrug() {
		return storeSumDrug;
	}

	public void setStoreSumDrug(Double storeSumDrug) {
		this.storeSumDrug = storeSumDrug;
	}

	public String getDeptCodeName() {
		return deptCodeName;
	}

	public void setDeptCodeName(String deptCodeName) {
		this.deptCodeName = deptCodeName;
	}

	public String getDrugDeptName() {
		return drugDeptName;
	}

	public void setDrugDeptName(String drugDeptName) {
		this.drugDeptName = drugDeptName;
	}

	public String getDrugTypeName() {
		return drugTypeName;
	}

	public void setDrugTypeName(String drugTypeName) {
		this.drugTypeName = drugTypeName;
	}

	public String getDrugQulityName() {
		return drugQulityName;
	}

	public void setDrugQulityName(String drugQulityName) {
		this.drugQulityName = drugQulityName;
	}

	public String getApplyOpername() {
		return applyOpername;
	}

	public void setApplyOpername(String applyOpername) {
		this.applyOpername = applyOpername;
	}

	public String getPatientDeptName() {
		return patientDeptName;
	}

	public void setPatientDeptName(String patientDeptName) {
		this.patientDeptName = patientDeptName;
	}

	public String getDrugedDeptName() {
		return drugedDeptName;
	}

	public void setDrugedDeptName(String drugedDeptName) {
		this.drugedDeptName = drugedDeptName;
	}

	public String getDrugedEmplName() {
		return drugedEmplName;
	}

	public void setDrugedEmplName(String drugedEmplName) {
		this.drugedEmplName = drugedEmplName;
	}

	public String getDoseModelName() {
		return doseModelName;
	}

	public void setDoseModelName(String doseModelName) {
		this.doseModelName = doseModelName;
	}

	public String getPrintEmplName() {
		return printEmplName;
	}

	public void setPrintEmplName(String printEmplName) {
		this.printEmplName = printEmplName;
	}

	public String getCancelEmplName() {
		return cancelEmplName;
	}

	public void setCancelEmplName(String cancelEmplName) {
		this.cancelEmplName = cancelEmplName;
	}

	public String getRecipeDeptName() {
		return recipeDeptName;
	}

	public void setRecipeDeptName(String recipeDeptName) {
		this.recipeDeptName = recipeDeptName;
	}

	public String getRecipeOperName() {
		return recipeOperName;
	}

	public void setRecipeOperName(String recipeOperName) {
		this.recipeOperName = recipeOperName;
	}

	public String getCompountOperName() {
		return compountOperName;
	}

	public void setCompountOperName(String compountOperName) {
		this.compountOperName = compountOperName;
	}

	public void setApplyNumber(Long applyNumber) {
		this.applyNumber = applyNumber;
	}

	
}