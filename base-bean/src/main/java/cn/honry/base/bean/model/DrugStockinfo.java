package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;


/**
 * ClassName: DrugStockinfo 
 * @Description: 药品库存维护表实体
 * @author lt
 * @date 2015-7-15
 */
@SuppressWarnings("serial")
public class DrugStockinfo extends Entity implements java.io.Serializable {

	// Fields
	/**科室*/
	private String storageDeptid;
	/**版本号*/
	private int version;
	/**药品编码*/
	private String drugId;
	/**药品类别(编码表)*/
	private String drugType;
	/**药品性质*/
	private String drugQuality;
	/**包装单位*/
	private String packUnit;
	/**包装数*/
	private Integer packQty;
	/**最小单位*/
	private String minUnit;
	/**显示的单位标记*/
	private Integer showFlag;
	/**显示的单位*/
	private String showUnit;
	/**参考零售价*/
	private Double retailPrice=0.0;
	/**总数量*/
	private Double storeSum=0.0;
	/**总金额*/
	private Double storeCost=0.0;
	/**预扣库存数量*/
	private Double preoutSum=0.0;
	/**预扣库存金额*/
	private Double preoutCost=0.0;
	/**有效期*/
	private Date validDate;
	/**货位号*/
	private String placeCode;
	/**生产厂家*/
	private String producerCode;
	/**管理性质(药品库存性质)*/
	private String manageQuality;
	/**最低库存量*/
	private Double lowSum=0.0;
	/**最高库存量*/
	private Double topSum=0.0;
	/**批采购量*/
	private Double needBatch=0.0;
	/**有效期天数*/
	private Integer usefulDays;
	/**缺药标志 0-否，1-是*/
	private Integer lackFlag;
	/**日盘点标志 0 非 1 需要*/
	private Integer dailtycheckFlag;
	/**默认发药单位标记 '0'－最小单位，'1'－包装单位*/
	private Integer unitFlag;
	/**是否可以拆零1是0否，指默认发药单位为最小单位时，是否可以*/
	private Integer changeFlag;
	/**备注*/
	private String remark;
	/**是否药柜管理药品*/
	private Integer arkFlag=0;
	/**药柜管理库存汇总数量*/
	private Double arkQty=0.0;
	/**有效性标志,药品的有效性*/
	private Integer validFlag;
	/** 页数   **/
	private String page;
	/**  每页行数  **/
	private String rows;
	
	/**-Start与数据库无关字段，用于显示药品信息表中的，部分字段-**/
	/**名称**/
	private String drugName;
	/**名称拼音码**/
	private String drugNamepinyin;
	/**名称五笔码**/
	private String drugNamewb;
	/**名称自定义码**/
	private String drugNameinputcode;
	/**规格**/
	private String drugSpec;
	/**剂型**/
	private String drugDosageform;
	/**零售价**/
	private Double drugRetailprice=0.0;
	/**批发价**/
	private Double drugWholesaleprice=0.0;
	/**购入价**/
	private Double drugPurchaseprice=0.0;
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
	/**生产厂家*/
	private String drugManufacturer;
	/**库存科室名*/
	private String deptName;
	/**内部入库总库存*/
	//与数据库无关的字段-start
	/**同一个drugid的storeSum(库存数量)的总和*/
	private Double storeSumDrug=0.0;
	/**终止日期**/
	private Date drugEnddate;
	/**供货单位代码**/
	private String companyCode;
	/**剂量单位**/
	private String drugDoseunit;
	/**频次**/
	private String drugFrequency;
	/**库存科室名称**/
	private String storageDeptName;
	/**-End-**/
	
	/**库存量20170223zpty用在列表页面上显示包装单位的数量*/
	private Double storePackSum=0.0;
	
	//用于存储json传过来的日期类型的validDate
	private String validDateStr;
	
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
	public Double getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
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
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	public String getPlaceCode() {
		return placeCode;
	}
	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}
	public String getProducerCode() {
		return producerCode;
	}
	public void setProducerCode(String producerCode) {
		this.producerCode = producerCode;
	}
	public String getManageQuality() {
		return manageQuality;
	}
	public void setManageQuality(String manageQuality) {
		this.manageQuality = manageQuality;
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
	public Double getNeedBatch() {
		return needBatch;
	}
	public void setNeedBatch(Double needBatch) {
		this.needBatch = needBatch;
	}
	public Integer getUsefulDays() {
		return usefulDays;
	}
	public void setUsefulDays(Integer usefulDays) {
		this.usefulDays = usefulDays;
	}
	public Integer getLackFlag() {
		return lackFlag;
	}
	public void setLackFlag(Integer lackFlag) {
		this.lackFlag = lackFlag;
	}
	public Integer getDailtycheckFlag() {
		return dailtycheckFlag;
	}
	public void setDailtycheckFlag(Integer dailtycheckFlag) {
		this.dailtycheckFlag = dailtycheckFlag;
	}
	public Integer getUnitFlag() {
		return unitFlag;
	}
	public void setUnitFlag(Integer unitFlag) {
		this.unitFlag = unitFlag;
	}
	public Integer getChangeFlag() {
		return changeFlag;
	}
	public void setChangeFlag(Integer changeFlag) {
		this.changeFlag = changeFlag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getArkFlag() {
		return arkFlag;
	}
	public void setArkFlag(Integer arkFlag) {
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
	public void setPage(String page) {
		this.page = page;
	}
	public String getPage() {
		return page;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public String getRows() {
		return rows;
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
	public void setValidDateStr(String validDateStr) {
		this.validDateStr = validDateStr;
	}
	public String getValidDateStr() {
		return validDateStr;
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
	public String getDrugManufacturer() {
		return drugManufacturer;
	}
	public void setDrugManufacturer(String drugManufacturer) {
		this.drugManufacturer = drugManufacturer;
	}
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
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptName() {
		return deptName;
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
	public Double getStoreSumDrug() {
		return storeSumDrug;
	}
	public void setStoreSumDrug(Double storeSumDrug) {
		this.storeSumDrug = storeSumDrug;
	}
	public String getStorageDeptName() {
		return storageDeptName;
	}
	public void setStorageDeptName(String storageDeptName) {
		this.storageDeptName = storageDeptName;
	}
	public Date getDrugEnddate() {
		return drugEnddate;
	}
	public void setDrugEnddate(Date drugEnddate) {
		this.drugEnddate = drugEnddate;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getDrugDoseunit() {
		return drugDoseunit;
	}
	public void setDrugDoseunit(String drugDoseunit) {
		this.drugDoseunit = drugDoseunit;
	}
	public String getDrugFrequency() {
		return drugFrequency;
	}
	public void setDrugFrequency(String drugFrequency) {
		this.drugFrequency = drugFrequency;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public Double getStorePackSum() {
		return storePackSum;
	}
	public void setStorePackSum(Double storePackSum) {
		this.storePackSum = storePackSum;
	}

}