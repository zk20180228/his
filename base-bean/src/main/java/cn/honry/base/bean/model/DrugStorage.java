package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**  
 *  
 * @className：DrugStorage.java 
 * @Description：药品库存实体
 * @Author：lt
 * @CreateDate：2015-7-9  
 * @version 1.0
 *
 */
public class DrugStorage extends Entity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	/**批次号，初始化时为1*/
	private String groupCode;
	/**版本号*/
	private int version;
	/**科室,用户选择科室*/
	private String storageDeptid;
	/**药品编码*/
	private String drugId;
	/**批号*/
	private String batchNo;
	/**药品商品名*/
	private String tradeName;
	/**规格*/
	private String specs;
	/**药品类别(编码表)*/
	private String drugType;
	/**药品性质(编码表)*/
	private String drugQuality;
	/**零售价*/
	private Double retailPrice = 0.0d;
	/**批发价*/
	private Double wholesalePrice = 0.0d;
	/**实进价*/
	private Double purchasePrice = 0.0d;
	/**包装单位*/
	private String packUnit;
	/**包装数*/
	private Integer packQty = 1;
	/**最小单位*/
	private String minUnit;
	/**显示的单位标记(0最小单位,1包装单位)*/
	private Integer showFlag;
	/**显示的单位*/
	private String showUnit;
	/**有效期*/
	private Date validDate;
	/**操作类别 1初始化 2入库3出库*/
	private Integer opType;
	/**库存数量*/
	private Double storeSum=0.0;
	/**库存金额*/
	private Double storeCost=0.0;
	/**预扣库存数量*/
	private Double preoutSum=0.0;
	/**预扣库存金额*/
	private Double preoutCost=0.0;
	/**生产厂家*/
	private String producerCode;
	/**最近一次月结的库存量*/
	private Double lastMonthNum;
	/**货位号*/
	private String placeCode;
	/**更新库存的流水号(物资)*/
	private String storeSeq;
	/**在库状态（0-暂入库，1正式入库）*/
	private Integer state;
	/**目标科室(保留最后一次库存数量变化时,产生变化的科室) ,药房或药库*/
	private String targetDept;
	/**单据号(保留最后一次库存数量变化时,产生变化的单据号)*/
	private String billCode;
	/**单据序号(保留最后一次库存数量变化时,产生变化的单内序号)*/
	private Integer serialCode;
	/**备注*/
	private String remark;
	/**库存操作类型1门诊发药,3门诊退药,4住院发药,5住院退药*/
	private String operCode;
	/**发票号*/
	private String invoiceNo;
	/**是否药柜管理药品,1药柜管理,0非药柜管理*/
	private Boolean arkFlag;
	/**药柜管理库存汇总数量*/
	private Double arkQty;
	/**有效性标志,药品的有效性*/
	private Integer validFlag=1;
	//与数据库无关的字段-start
	/**同一个drugid的storeSum(库存数量)的总和*/
	private Double storeSumDrug;
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
	/**包装数量**/
	private Integer drugPackagingnum;
	/**终止日期**/
	private Date drugEnddate;
	/**包装单位**/
	private String drugPackagingunit;
	/**供货单位代码**/
	private String companyCode;
	/**剂量单位**/
	private String drugDoseunit;
	/**频次**/
	private String drugFrequency;
	/**剂型**/
	private String drugDosageform;
	/**库存科室名称**/
	private String storageDeptName;
	/**批发金额**/
	private Double wholesaleCost;
	/**退库数量**/
	private Double returnNum;
	/**入库单据号(6位时间+6位流水号)**/
	private String inListCode;
	/**入库单流水号**/
	private String inBillCode;
	/**用于记录行号**/
	private Integer rowNumber;
	/**最小单位**/
	private String showMinUnit;
	/** 入库表标识*/
	private String flag;
	/** 
	* @Fields hospitalId : 所属医院 
	*/ 
	private Integer hospitalId;
	/** 
	* @Fields areaCode : 所属院区
	*/ 
	private String areaCode;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getShowMinUnit() {
		return showMinUnit;
	}
	public void setShowMinUnit(String showMinUnit) {
		this.showMinUnit = showMinUnit;
	}
	public String getInBillCode() {
		return inBillCode;
	}
	public void setInBillCode(String inBillCode) {
		this.inBillCode = inBillCode;
	}
	public String getInListCode() {
		return inListCode;
	}
	public void setInListCode(String inListCode) {
		this.inListCode = inListCode;
	}
	public Double getReturnNum() {
		return returnNum;
	}
	public void setReturnNum(Double returnNum) {
		this.returnNum = returnNum;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getStorageDeptid() {
		return storageDeptid;
	}
	public void setStorageDeptid(String storageDeptid) {
		this.storageDeptid = storageDeptid;
	}
	public String getDrugId() {
		return drugId;
	}
	public void setDrugId(String drugId) {
		this.drugId = drugId;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
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
	public String getPackUnit() {
		return packUnit;
	}
	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}
	public Integer getPackQty() {
		return packQty;
	}
	public void setPackQty(Integer packQty) {
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
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	public Integer getOpType() {
		return opType;
	}
	public void setOpType(Integer opType) {
		this.opType = opType;
	}
	public Double getStoreSum() {
		return storeSum;
	}
	public void setStoreSum(Double storeSum) {
		this.storeSum = storeSum;
	}
	public Double getStoreCost() {
		return storeCost;
	}
	public void setStoreCost(Double storeCost) {
		this.storeCost = storeCost;
	}
	public Double getPreoutSum() {
		return preoutSum;
	}
	public void setPreoutSum(Double preoutSum) {
		this.preoutSum = preoutSum;
	}
	public Double getPreoutCost() {
		return preoutCost;
	}
	public void setPreoutCost(Double preoutCost) {
		this.preoutCost = preoutCost;
	}
	public String getProducerCode() {
		return producerCode;
	}
	public void setProducerCode(String producerCode) {
		this.producerCode = producerCode;
	}
	public Double getLastMonthNum() {
		return lastMonthNum;
	}
	public void setLastMonthNum(Double lastMonthNum) {
		this.lastMonthNum = lastMonthNum;
	}
	public String getPlaceCode() {
		return placeCode;
	}
	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}
	public String getStoreSeq() {
		return storeSeq;
	}
	public void setStoreSeq(String storeSeq) {
		this.storeSeq = storeSeq;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getTargetDept() {
		return targetDept;
	}
	public void setTargetDept(String targetDept) {
		this.targetDept = targetDept;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public Integer getSerialCode() {
		return serialCode;
	}
	public void setSerialCode(Integer serialCode) {
		this.serialCode = serialCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Boolean getArkFlag() {
		return arkFlag;
	}
	public void setArkFlag(Boolean arkFlag) {
		this.arkFlag = arkFlag;
	}
	public Double getArkQty() {
		return arkQty;
	}
	public void setArkQty(Double arkQty) {
		this.arkQty = arkQty;
	}
	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}
	public Integer getValidFlag() {
		return validFlag;
	}
	public void setStoreSumDrug(Double storeSumDrug) {
		this.storeSumDrug = storeSumDrug;
	}
	public Double getStoreSumDrug() {
		return storeSumDrug;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugSpec(String drugSpec) {
		this.drugSpec = drugSpec;
	}
	public String getDrugSpec() {
		return drugSpec;
	}
	public void setDrugPackagingnum(Integer drugPackagingnum) {
		this.drugPackagingnum = drugPackagingnum;
	}
	public Integer getDrugPackagingnum() {
		return drugPackagingnum;
	}
	public void setDrugEnddate(Date drugEnddate) {
		this.drugEnddate = drugEnddate;
	}
	public Date getDrugEnddate() {
		return drugEnddate;
	}
	public void setDrugPackagingunit(String drugPackagingunit) {
		this.drugPackagingunit = drugPackagingunit;
	}
	public String getDrugPackagingunit() {
		return drugPackagingunit;
	}
	public void setDrugNamepinyin(String drugNamepinyin) {
		this.drugNamepinyin = drugNamepinyin;
	}
	public String getDrugNamepinyin() {
		return drugNamepinyin;
	}
	public void setDrugNamewb(String drugNamewb) {
		this.drugNamewb = drugNamewb;
	}
	public String getDrugNamewb() {
		return drugNamewb;
	}
	public void setDrugNameinputcode(String drugNameinputcode) {
		this.drugNameinputcode = drugNameinputcode;
	}
	public String getDrugNameinputcode() {
		return drugNameinputcode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setDrugDoseunit(String drugDoseunit) {
		this.drugDoseunit = drugDoseunit;
	}
	public String getDrugDoseunit() {
		return drugDoseunit;
	}
	public void setDrugFrequency(String drugFrequency) {
		this.drugFrequency = drugFrequency;
	}
	public String getDrugFrequency() {
		return drugFrequency;
	}
	public void setDrugDosageform(String drugDosageform) {
		this.drugDosageform = drugDosageform;
	}
	public String getDrugDosageform() {
		return drugDosageform;
	}
	public void setStorageDeptName(String storageDeptName) {
		this.storageDeptName = storageDeptName;
	}
	public String getStorageDeptName() {
		return storageDeptName;
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
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public Double getWholesaleCost() {
		return wholesaleCost;
	}
	public void setWholesaleCost(Double wholesaleCost) {
		this.wholesaleCost = wholesaleCost;
	}
	
	public Integer getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
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