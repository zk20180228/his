package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.honry.base.bean.business.Entity;

/**
 *入库记录表
 */

@SuppressWarnings("serial")
public class DrugInStore extends Entity implements Serializable{

	/**库存科室 0-全部部门**/
	private String drugDeptCode;
	/**入库单流水号**/
	private String inBillCode;
	/**序号**/
	private String serialCode;
	/**批次号**/
	private String groupCode;
	/**入库单据号(6位时间+6位流水号)**/
	private String inListCode;
	/**入库类型 1一般 2发票3核准4内部入库申请5特殊入库6内部入库审核7外部入库退库8内部入库退库申请9内部入库退库审核10其他入库**/
	private String inType;
	/**入库分类**/
	private String inKind;
	/**出库单号**/
	private String outBillCode;
	/**出库单序号**/
	private String outSerialCode;
	/**出库单据号**/
	private String outListCode;
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
	/**供货单位代码：供货商或是科室**/
	private String companyCode;
	/**零售价**/
	private Double retailPrice;
	/**批发价**/
	private Double wholesalePrice;
	/**购入价**/
	private Double purchasePrice;
	/**入库数**/
	private Double inNum;
	/**零售金额**/
	private Double retailCost;
	/**批发金额**/
	private Double wholesaleCost;
	/**购入金额**/
	private Double purchaseCost;
	/**入库后库存数量**/
	private Double storeNum;
	/**入库后库存总金额**/
	private Double storeCost=0.0;
	/**特殊标记，1是，0否**/
	private Integer specialFlag;
	/**入库状态，0-申请，1-审批（发票入库），2-核准**/
	private Integer inState;
	/**申请入库量**/
	private Double applyNum;
	/**申请入库操作员**/
	private String applyOpercode;
	/**申请入库日期**/
	private Date applyDate;
	/**审批数量**/
	private Double examNum;
	/**审批人（药库发票入库人）**/
	private String examOpercode;
	/**审批日期（药库发票入库日期）**/
	private Date examDate;
	/**核准人**/
	private String approveOpercode;
	/**核准日期**/
	private Date approveDate;
	/**货位码**/
	private String placeCode;
	/**退库数量**/
	private Double returnNum;
	/**申请序号**/
	private String applyNumber;
	/**制剂序号－生产序号或检验序号**/
	private String medId;
	/**发票号**/
	private String invoiceNo;
	/**送药单流水号**/
	private String deliveryNo;
	/**招标序号－招标单的序号**/
	private String tenderNo;
	/**实际扣率**/
	private Double actualRate;
	/**现金标志**/
	private Integer cashFlag;
	/**供货商结存付款状态 0 未付 1 未全付 2 付清**/
	private Integer payState;
	/**备注**/
	private String remark;
	/**一般入库时的购入价**/
	private Double purcharsePriceFirsttime;
	/**招标标记，1是，0否f**/
	private Integer isTenderOffer=0;
	/**发票上的发票日期**/
	private Date invoiceDate;
	/**入库时间，即实际入库发生时间**/
	private Date inDate;
	/**源科室（供货单位）类别  1 院内科室 2 供货单位 3 扩**/
	private Integer sourceCompanyType;
	/**操作类型：1 门诊发药, 2 内部入库,3 门诊退药,4 住院摆药,5住院退药,6入库退货,7核准入库,8赠送入库,9一般入库,10手工退药入库,11特殊入库,12
	 * 20161229
	 * **/
	private String opType;
	/** 
	* @Fields hospitalId : 所属医院 
	*/ 
	private Integer hospitalId;
	/** 
	* @Fields areaCode : 所属院区
	*/ 
	private String areaCode;
	//数据库没有的字段，做json串日期格式处理
	/** 
	 * @Fields drugId :药品code 
	 */ 
	private String drugId;
	private String vValidDate;
	private String vIndate;
	/**-Start与数据库无关字段，用于显示药品信息表中的，部分字段-**/
	/**前台传来的退回数量**/
	private Double retNum;
	/**名称**/
	private String drugName;
	/**名称自定义码**/
	private String drugNameinputcode;
	/**名称拼音码**/
	private String drugNamepinyin;
	/**名称五笔码**/
	private String drugNamewb;
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
	/**通用名称**/
	private String drugCommonname;
	/**通用名称拼音码**/
	private String drugCnamepinyin;
	/**通用名称五笔码**/
	private String drugCnamewb;
	/**通用名称自定义码**/
	private String drugCnameinputcode;
	/**所有的库存量
	 * wujiao
	 * **/
	private Double allinNum;
	/**供货公司id
	 * wujiao
	 * **/
	private String companyId;
	/**
	 * 剩余数量
	 */
	private Double storeSum;
	
	/**最小单位**/
	private String showMinUnit;
	/**包装单位**/
	private String showPackUnit;
	/**入库明细标识**/
	private String flag;
	/**供货公司*/
	private String showCompanyCode;
	private List<DrugInStore> list;
	
	public List<DrugInStore> getList() {
		return list;
	}

	public void setList(List<DrugInStore> list) {
		this.list = list;
	}

	public String getShowCompanyCode() {
		return showCompanyCode;
	}

	public void setShowCompanyCode(String showCompanyCode) {
		this.showCompanyCode = showCompanyCode;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getShowPackUnit() {
		return showPackUnit;
	}

	public void setShowPackUnit(String showPackUnit) {
		this.showPackUnit = showPackUnit;
	}

	public String getShowMinUnit() {
		return showMinUnit;
	}

	public void setShowMinUnit(String showMinUnit) {
		this.showMinUnit = showMinUnit;
	}

	public Double getStoreSum() {
		return storeSum;
	}

	public void setStoreSum(Double storeSum) {
		this.storeSum = storeSum;
	}

	/**
	 * 新加字段  入库金额
	 */
	private Double totalretailPrice;

	public Double getTotalretailPrice() {
		return totalretailPrice;
	}

	public void setTotalretailPrice(Double totalretailPrice) {
		this.totalretailPrice = totalretailPrice;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Double getAllinNum() {
		return allinNum;
	}

	public void setAllinNum(Double allinNum) {
		this.allinNum = allinNum;
	}

	public String getvValidDate() {
		return vValidDate;
	}

	public void setvValidDate(String vValidDate) {
		this.vValidDate = vValidDate;
	}

	public String getvIndate() {
		return vIndate;
	}

	public void setvIndate(String vIndate) {
		this.vIndate = vIndate;
	}

	public String getDrugDeptCode() {
		return this.drugDeptCode;
	}

	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}

	public String getInBillCode() {
		return this.inBillCode;
	}

	public void setInBillCode(String inBillCode) {
		this.inBillCode = inBillCode;
	}

	public String getSerialCode() {
		return this.serialCode;
	}

	public void setSerialCode(String serialCode) {
		this.serialCode = serialCode;
	}

	public String getGroupCode() {
		return this.groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getInListCode() {
		return this.inListCode;
	}

	public void setInListCode(String inListCode) {
		this.inListCode = inListCode;
	}

	public String getInType() {
		return inType;
	}

	public void setInType(String inType) {
		this.inType = inType;
	}

	public String getInKind() {
		return this.inKind;
	}

	public void setInKind(String inKind) {
		this.inKind = inKind;
	}

	public String getOutBillCode() {
		return this.outBillCode;
	}

	public void setOutBillCode(String outBillCode) {
		this.outBillCode = outBillCode;
	}

	public String getOutSerialCode() {
		return this.outSerialCode;
	}

	public void setOutSerialCode(String outSerialCode) {
		this.outSerialCode = outSerialCode;
	}

	public String getOutListCode() {
		return this.outListCode;
	}

	public void setOutListCode(String outListCode) {
		this.outListCode = outListCode;
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

	public Double getPackQty() {
		return this.packQty;
	}

	public void setPackQty(Double packQty) {
		this.packQty = packQty;
	}

	public String getMinUnit() {
		return this.minUnit;
	}

	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}

	public Integer getShowFlag() {
		return this.showFlag;
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

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getValidDate() {
		return this.validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public String getProducerCode() {
		return this.producerCode;
	}

	public void setProducerCode(String producerCode) {
		this.producerCode = producerCode;
	}

	public String getCompanyCode() {
		return this.companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
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

	public Double getInNum() {
		return this.inNum;
	}

	public void setInNum(Double inNum) {
		this.inNum = inNum;
	}

	public Double getRetailCost() {
		return this.retailCost;
	}

	public void setRetailCost(Double retailCost) {
		this.retailCost = retailCost;
	}

	public Double getWholesaleCost() {
		return this.wholesaleCost;
	}

	public void setWholesaleCost(Double wholesaleCost) {
		this.wholesaleCost = wholesaleCost;
	}

	public Double getPurchaseCost() {
		return this.purchaseCost;
	}

	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public Double getStoreNum() {
		return this.storeNum;
	}

	public void setStoreNum(Double storeNum) {
		this.storeNum = storeNum;
	}

	public Double getStoreCost() {
		return this.storeCost;
	}

	public void setStoreCost(Double storeCost) {
		this.storeCost = storeCost;
	}

	public Integer getSpecialFlag() {
		return this.specialFlag;
	}

	public void setSpecialFlag(Integer specialFlag) {
		this.specialFlag = specialFlag;
	}

	public Integer getInState() {
		return this.inState;
	}

	public void setInState(Integer inState) {
		this.inState = inState;
	}

	public Double getApplyNum() {
		return this.applyNum;
	}

	public void setApplyNum(Double applyNum) {
		this.applyNum = applyNum;
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

	public Double getExamNum() {
		return this.examNum;
	}

	public void setExamNum(Double examNum) {
		this.examNum = examNum;
	}

	public String getExamOpercode() {
		return this.examOpercode;
	}

	public void setExamOpercode(String examOpercode) {
		this.examOpercode = examOpercode;
	}

	public Date getExamDate() {
		return this.examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public String getApproveOpercode() {
		return this.approveOpercode;
	}

	public void setApproveOpercode(String approveOpercode) {
		this.approveOpercode = approveOpercode;
	}

	public Date getApproveDate() {
		return this.approveDate;
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
		return this.returnNum;
	}

	public void setReturnNum(Double returnNum) {
		this.returnNum = returnNum;
	}

	public String getApplyNumber() {
		return this.applyNumber;
	}

	public void setApplyNumber(String applyNumber) {
		this.applyNumber = applyNumber;
	}

	public String getMedId() {
		return this.medId;
	}

	public void setMedId(String medId) {
		this.medId = medId;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getDeliveryNo() {
		return this.deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public String getTenderNo() {
		return this.tenderNo;
	}

	public void setTenderNo(String tenderNo) {
		this.tenderNo = tenderNo;
	}

	public Double getActualRate() {
		return this.actualRate;
	}

	public void setActualRate(Double actualRate) {
		this.actualRate = actualRate;
	}

	public Integer getCashFlag() {
		return this.cashFlag;
	}

	public void setCashFlag(Integer cashFlag) {
		this.cashFlag = cashFlag;
	}

	public Integer getPayState() {
		return this.payState;
	}

	public void setPayState(Integer payState) {
		this.payState = payState;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getPurcharsePriceFirsttime() {
		return this.purcharsePriceFirsttime;
	}

	public void setPurcharsePriceFirsttime(Double purcharsePriceFirsttime) {
		this.purcharsePriceFirsttime = purcharsePriceFirsttime;
	}

	public Integer getIsTenderOffer() {
		return this.isTenderOffer;
	}

	public void setIsTenderOffer(Integer isTenderOffer) {
		this.isTenderOffer = isTenderOffer;
	}

	public Date getInvoiceDate() {
		return this.invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getInDate() {
		return this.inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public Integer getSourceCompanyType() {
		return this.sourceCompanyType;
	}

	public void setSourceCompanyType(Integer sourceCompanyType) {
		this.sourceCompanyType = sourceCompanyType;
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

	public void setDrugCnamepinyin(String drugCnamepinyin) {
		this.drugCnamepinyin = drugCnamepinyin;
	}

	public String getDrugCnamepinyin() {
		return drugCnamepinyin;
	}

	public void setDrugCnamewb(String drugCnamewb) {
		this.drugCnamewb = drugCnamewb;
	}

	public String getDrugCnamewb() {
		return drugCnamewb;
	}

	public void setDrugCnameinputcode(String drugCnameinputcode) {
		this.drugCnameinputcode = drugCnameinputcode;
	}

	public String getDrugCnameinputcode() {
		return drugCnameinputcode;
	}

	public Double getRetNum() {
		return retNum;
	}

	public void setRetNum(Double retNum) {
		this.retNum = retNum;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getDrugId() {
		return drugId;
	}

	public void setDrugId(String drugId) {
		this.drugId = drugId;
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