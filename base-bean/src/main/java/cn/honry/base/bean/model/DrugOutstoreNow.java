package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

@SuppressWarnings("serial")
public class DrugOutstoreNow extends Entity implements java.io.Serializable {
	
	/**出库科室编码**/
	private String drugDeptCode;
	/**出库单号**/
	private String outBillCode;
	/**序号**/
	private String serialCode;
	/**批次号**/
	private String groupCode;
	/**出库单据号**/
	private String outListCode;
	/**出库类型 1一般 2出库退库申请3出库退库审核4出库审批5报损6价让出库7特殊8各科室领药9特殊出库**/
	private String outType;
	/**入库单流水号**/
	private String inBillCode;
	/**出库单序号**/
	private String outSerialCode;
	/**入库单据号(6位时间+6位流水号)**/
	private String inListCode;
	/**药品编码**/
	private String drugCode;
	/**药品商品名**/
	private String tradeName;
	/**药品类别**/
	private String drugType;
	/**药品性质**/
	private String drugQuality;
	/**规格**/
	private String specs;
	/**包装单位**/
	private String packUnit;
	/**包装数**/
	private Double packQty = 1.0d;
	/**最小单位**/
	private String minUnit;
	/**显示的单位标记 1 包装单位2最小单位**/
	private Integer showFlag;
	/**显示的单位**/
	private String showUnit;
	/**批号**/
	private String batchNo;
	/**有效期**/
	private Date validDate;
	/**生产厂家**/
	private String producerCode;
	/**供货单位代码**/
	private String companyCode;
	/**零售价**/
	private Double retailPrice;
	/**批发价**/
	private Double wholesalePrice = 0.0d;
	/**购入价**/
	private Double purchasePrice=0.0d;
	/**出库数量**/
	private Double outNum;
	/**零售金额**/
	private Double retailCost;
	/**批发金额**/
	private Double wholesaleCost;
	/**购入金额**/
	private Double purchaseCost;
	/**出库后库存数量**/
	private Double storeNum;
	/**出库后库存总金额**/
	private Double storeCost;
	/**特殊标记，1是，0否**/
	private Integer specialFlag;
	/**出库状态 0申请、1审批、2核准**/
	private Integer outState;
	/**申请出库量**/
	private Double applyNum;
	/**申请出库人**/
	private String applyOpercode;
	/**申请出库日期**/
	private String applyDate;
	/**审批数量**/
	private Double examNum;
	/**审批人**/
	private String examOpercode;
	/**审批日期**/
	private Date examDate;
	/**核准人**/
	private String approveOpercode;
	/**核准日期**/
	private Date approveDate;
	/**货位码**/
	private String placeCode;
	/**退库数量**/
	private Double returnNum;
	/**摆药单号**/
	private String drugedBill;
	/**制剂序号－生产序号或检验序号**/
	private String medId;
	/**领药单位编码**/
	private String drugStorageCode;
	/**处方号**/
	private String recipeNo;
	/**处方流水号**/
	private String sequenceNo;
	/**签字人**/
	private String signPerson;
	/**领药人**/
	private String getPerson;
	/**冲账标志0表示没有冲账1表示被冲账**/
	private Integer strikeFlag;
	/**是否药房向药柜出库记录 0 非药房向药柜出库 1 药房向药柜出库**/
	private Integer arkFlag;
	/**药柜发药 出库单流水号**/
	private String arkBillCode;
	/**出库记录发生时间**/
	private Date outDate;
	/**备注**/
	private String remark;
	/** 
	* @Fields drugedCode : 发药人
	*/ 
	private String drugedCode;
	/** 
	* @Fields drugedDate : 发药时间 
	*/ 
	private Date drugedDate;
	/** 
	* @Fields hospitalId : 所属医院 
	*/ 
	private Integer hospitalId;
	/** 
	* @Fields areaCode : 所属院区
	*/ 
	private String areaCode;
	
	//-Start与数据库无关字段，用于显示药品信息表中的，部分字段
	/**名称**/
	private String drugName;
	/**名称拼音码**/
	private String drugNamepinyin;
	/**名称五笔码**/
	private String drugNamewb;
	/**名称自定义码**/
	private String drugNameinputcode;
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
	private Double drugRetailprice=0.0;
	/**最小单位**/
	private String drugMinimumunit;
	/**包装数量**/
	private Integer drugPackagingnum;
	/**包装单位**/
	private String drugPackagingunit;
	/**库存数量**/
	private Double storeSum; 
	/**出库金额**/
	private Double outlCost;
	/**页面移除使用**/
	private String flagId;
	/**出库之前的库存量**/
	private Double beforeOutSum;
	/**退库金额**/
	private Double returnOutCost;
	/**出库申请id*/
	private String applyoutId;
	/**原零售价**/
	private Double oldRetailPrice;
	/**厂商名称**/
	private String manufacturerName;
	/** 
	* @Fields outDrugNum : 出库药品种数 
	*/ 
	private Integer outDrugNum;
	//页面显示用
	private Double num;
	/**各科室领药（区分是出库记录Id还是出库申请Id）1是出库记录id 2是出库申请单id**/
	private int differentId;
	//传值用。wujiao2016-04-12
	/**批发价**/
	private Double drugWholesaleprice=0.0;
	/**购入价**/
	private Double drugPurchaseprice=0.0;
	
	
	/**领药单位名称**/
	private String drugStorageName;
	/**出库科室名称**/
	private String drugDeptName;
	/**审批人姓名**/
	private String examOpername;
	/**核准人姓名**/
	private String approveOpername;
	/**签字人姓名**/
	private String signPersonName;
	/**领药人姓名**/
	private String getPersonName;
	/**发药人姓名**/
	private String drugedName;
	/**生产厂家名称**/
	private String producerName;
	/**供货单位名称**/
	private String companyName;
	
	/**
	 * 类型op_type
	 * zpty20170103
	 * **/
	private String opType;
	
	public Double getDrugWholesaleprice() {
		return drugWholesaleprice;
	}
	public void setDrugWholesaleprice(Double drugWholesaleprice) {
		this.drugWholesaleprice = drugWholesaleprice;
	}
	public Double getDrugPurchaseprice() {
		return drugPurchaseprice;
	}
	public void setDrugPurchaseprice(Double drugPurchaseprice) {
		this.drugPurchaseprice = drugPurchaseprice;
	}
	public Double getNum() {
		return num;
	}
	public void setNum(Double num) {
		this.num = num;
	}
	public String getDrugDeptCode() {
		return drugDeptCode;
	}
	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}
	public String getOutBillCode() {
		return outBillCode;
	}
	public void setOutBillCode(String outBillCode) {
		this.outBillCode = outBillCode;
	}
	public String getSerialCode() {
		return serialCode;
	}
	public void setSerialCode(String serialCode) {
		this.serialCode = serialCode;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getOutListCode() {
		return outListCode;
	}
	public void setOutListCode(String outListCode) {
		this.outListCode = outListCode;
	}

	public String getOutType() {
		return outType;
	}
	public void setOutType(String outType) {
		this.outType = outType;
	}
	public String getInBillCode() {
		return inBillCode;
	}
	public void setInBillCode(String inBillCode) {
		this.inBillCode = inBillCode;
	}
	public String getOutSerialCode() {
		return outSerialCode;
	}
	public void setOutSerialCode(String outSerialCode) {
		this.outSerialCode = outSerialCode;
	}
	public String getInListCode() {
		return inListCode;
	}
	public void setInListCode(String inListCode) {
		this.inListCode = inListCode;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getDrugType() {
		return drugType;
	}
	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}
	public String getDrugQuality() {
		return drugQuality;
	}
	public void setDrugQuality(String drugQuality) {
		this.drugQuality = drugQuality;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public String getPackUnit() {
		return packUnit;
	}
	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}
	public Double getPackQty() {
		return packQty;
	}
	public void setPackQty(Double packQty) {
		this.packQty = packQty;
	}
	public String getMinUnit() {
		return minUnit;
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
		return showUnit;
	}
	public void setShowUnit(String showUnit) {
		this.showUnit = showUnit;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	public String getProducerCode() {
		return producerCode;
	}
	public void setProducerCode(String producerCode) {
		this.producerCode = producerCode;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public Double getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}
	public Double getWholesalePrice() {
		return wholesalePrice;
	}
	public void setWholesalePrice(Double wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}
	public Double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public Double getOutNum() {
		return outNum;
	}
	public void setOutNum(Double outNum) {
		this.outNum = outNum;
	}
	public Double getRetailCost() {
		return retailCost;
	}
	public void setRetailCost(Double retailCost) {
		this.retailCost = retailCost;
	}
	public Double getWholesaleCost() {
		return wholesaleCost;
	}
	public void setWholesaleCost(Double wholesaleCost) {
		this.wholesaleCost = wholesaleCost;
	}
	public Double getPurchaseCost() {
		return purchaseCost;
	}
	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}
	public Double getStoreNum() {
		return storeNum;
	}
	public void setStoreNum(Double storeNum) {
		this.storeNum = storeNum;
	}
	public Double getStoreCost() {
		return storeCost;
	}
	public void setStoreCost(Double storeCost) {
		this.storeCost = storeCost;
	}
	public Integer getSpecialFlag() {
		return specialFlag;
	}
	public void setSpecialFlag(Integer specialFlag) {
		this.specialFlag = specialFlag;
	}
	public Integer getOutState() {
		return outState;
	}
	public void setOutState(Integer outState) {
		this.outState = outState;
	}
	public Double getApplyNum() {
		return applyNum;
	}
	public void setApplyNum(Double applyNum) {
		this.applyNum = applyNum;
	}
	public String getApplyOpercode() {
		return applyOpercode;
	}
	public void setApplyOpercode(String applyOpercode) {
		this.applyOpercode = applyOpercode;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public Double getExamNum() {
		return examNum;
	}
	public void setExamNum(Double examNum) {
		this.examNum = examNum;
	}
	public String getExamOpercode() {
		return examOpercode;
	}
	public void setExamOpercode(String examOpercode) {
		this.examOpercode = examOpercode;
	}
	public Date getExamDate() {
		return examDate;
	}
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
	public String getApproveOpercode() {
		return approveOpercode;
	}
	public void setApproveOpercode(String approveOpercode) {
		this.approveOpercode = approveOpercode;
	}
	public Date getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}
	public String getPlaceCode() {
		return placeCode;
	}
	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}
	public Double getReturnNum() {
		return returnNum;
	}
	public void setReturnNum(Double returnNum) {
		this.returnNum = returnNum;
	}
	public String getDrugedBill() {
		return drugedBill;
	}
	public void setDrugedBill(String drugedBill) {
		this.drugedBill = drugedBill;
	}
	public String getMedId() {
		return medId;
	}
	public void setMedId(String medId) {
		this.medId = medId;
	}
	public String getDrugStorageCode() {
		return drugStorageCode;
	}
	public void setDrugStorageCode(String drugStorageCode) {
		this.drugStorageCode = drugStorageCode;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getSignPerson() {
		return signPerson;
	}
	public void setSignPerson(String signPerson) {
		this.signPerson = signPerson;
	}
	public String getGetPerson() {
		return getPerson;
	}
	public void setGetPerson(String getPerson) {
		this.getPerson = getPerson;
	}
	public Integer getStrikeFlag() {
		return strikeFlag;
	}
	public void setStrikeFlag(Integer strikeFlag) {
		this.strikeFlag = strikeFlag;
	}
	public Integer getArkFlag() {
		return arkFlag;
	}
	public void setArkFlag(Integer arkFlag) {
		this.arkFlag = arkFlag;
	}
	public String getArkBillCode() {
		return arkBillCode;
	}
	public void setArkBillCode(String arkBillCode) {
		this.arkBillCode = arkBillCode;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getDrugCommonname() {
		return drugCommonname;
	}
	public void setDrugCommonname(String drugCommonname) {
		this.drugCommonname = drugCommonname;
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
	public void setStoreSum(Double storeSum) {
		this.storeSum = storeSum;
	}
	public Double getStoreSum() {
		return storeSum;
	}
	public void setOutlCost(Double outlCost) {
		this.outlCost = outlCost;
	}
	public Double getOutlCost() {
		return outlCost;
	}
	public void setFlagId(String flagId) {
		this.flagId = flagId;
	}
	public String getFlagId() {
		return flagId;
	}
	public void setBeforeOutSum(Double beforeOutSum) {
		this.beforeOutSum = beforeOutSum;
	}
	public Double getBeforeOutSum() {
		return beforeOutSum;
	}
	public void setReturnOutCost(Double returnOutCost) {
		this.returnOutCost = returnOutCost;
	}
	public Double getReturnOutCost() {
		return returnOutCost;
	}
	public void setApplyoutId(String applyoutId) {
		this.applyoutId = applyoutId;
	}
	public String getApplyoutId() {
		return applyoutId;
	}
	public void setOldRetailPrice(Double oldRetailPrice) {
		this.oldRetailPrice = oldRetailPrice;
	}
	public Double getOldRetailPrice() {
		return oldRetailPrice;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getManufacturerName() {
		return manufacturerName;
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
	public void setDifferentId(int differentId) {
		this.differentId = differentId;
	}
	public int getDifferentId() {
		return differentId;
	}
	public String getDrugStorageName() {
		return drugStorageName;
	}
	public void setDrugStorageName(String drugStorageName) {
		this.drugStorageName = drugStorageName;
	}
	public String getDrugDeptName() {
		return drugDeptName;
	}
	public void setDrugDeptName(String drugDeptName) {
		this.drugDeptName = drugDeptName;
	}
	public String getExamOpername() {
		return examOpername;
	}
	public void setExamOpername(String examOpername) {
		this.examOpername = examOpername;
	}
	public String getApproveOpername() {
		return approveOpername;
	}
	public void setApproveOpername(String approveOpername) {
		this.approveOpername = approveOpername;
	}
	public String getSignPersonName() {
		return signPersonName;
	}
	public void setSignPersonName(String signPersonName) {
		this.signPersonName = signPersonName;
	}
	public String getGetPersonName() {
		return getPersonName;
	}
	public void setGetPersonName(String getPersonName) {
		this.getPersonName = getPersonName;
	}
	public String getDrugedName() {
		return drugedName;
	}
	public void setDrugedName(String drugedName) {
		this.drugedName = drugedName;
	}
	public String getProducerName() {
		return producerName;
	}
	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public Integer getOutDrugNum() {
		return outDrugNum;
	}
	public void setOutDrugNum(Integer outDrugNum) {
		this.outDrugNum = outDrugNum;
	}
	public String getDrugedCode() {
		return drugedCode;
	}
	public void setDrugedCode(String drugedCode) {
		this.drugedCode = drugedCode;
	}
	public Date getDrugedDate() {
		return drugedDate;
	}
	public void setDrugedDate(Date drugedDate) {
		this.drugedDate = drugedDate;
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
	
}