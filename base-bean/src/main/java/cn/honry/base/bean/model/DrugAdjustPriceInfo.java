package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 药品调价表
 */

public class DrugAdjustPriceInfo extends Entity {

	/*库房编码*/
	private String drugDept;
	/*药品编码*/
	private String drugCode;
	/*药品类别*/
	private String drugType;
	/*药品性质*/
	private String drugQuality;
	/*调价前零售价格*/
	private Double preRetailPrice;
	/*调价前批发价格*/
	private Double preWholesalePrice;
	/*调价后零售价格*/
	private Double retailPrice;
	/*调价后批发价格*/
	private Double wholesalePrice;
	/*调价单据号*/
	private String adjustBillCode;
	/*调价前购入价*/
	private Double prePurchasePrice=0.0;
	/*调价后购入价*/
	private Double purchasePrice=0.0;
	/*调价时药库库存量*/
	private Double storeSum=0.0;
	/*盈亏标记*/
	private Integer profitFlag=0;
	/*调价执行时间*/
	private Date inureTime;
	/*药库调价标记*/
	private Integer ddAdjustMark=0;
	/*药房调价标记*/
	private Integer dsAdjustMark=0;
	/*药品商品名*/
	private String tradeName;
	/*规格*/
	private String specs;
	/*生产厂家*/
	private String producer;
	/*包装单位*/
	private String packUnit;
	/*包装数*/
	private Short packQty;
	/*最小单位*/
	private String minUnit;
	/*调价单状态：0、未调价；1、已调价；2、无效*/
	private Integer currentState;
	/*作废人编码*/
	private String abolishOper;
	/*作废时间*/
	private Date abolishDate;
	/*招标文件号*/
	private String fileNo;
	/*最近一次的供货单位*/
	private String lastCompanycode;
	/*最近一次供货单位的入库数量*/
	private Double lastInsum;
	/*最近一次供货单位的购入价*/
	private Double lastPurchase;
	/*最近一次供货单位的批发价*/
	private Double lastWholesale;
	/*最近一次供货单位的零售价*/
	private Double lastRetail;
	/*最近一次的供货入库单号*/
	private String lastInbillcode;
	/*备注*/
	private String remark;
	/*调价依据*/
	private String adjustReason;
	/*调价方式，1零售价2批发价 */
	private Integer adjustMode;
	/*审核人*/
	private String checkUser;
	/*审核部门*/
	private String checkDept;
	/*审核意见*/
	private String checkOpinion;
	/*审核时间*/
	private Date checkTime;
	/** 
	* @Fields hospitalId : 所属医院 
	*/ 
	private Integer hospitalId;
	/** 
	* @Fields areaCode : 所属院区
	*/ 
	private String areaCode;
	
	//与数据库无关字段 厂家名称
	private String prdoctName;
	//与数据库无关字段 起始日期
	private Date drugStartdate;
	//与数据库无关字段 按照执行调价终止日期查询表记录
	private Date inureEnd;

	public String getDrugDept() {
		return this.drugDept;
	}

	public void setDrugDept(String drugDept) {
		this.drugDept = drugDept;
	}

	public String getDrugCode() {
		return this.drugCode;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
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

	public Double getPreRetailPrice() {
		return this.preRetailPrice;
	}

	public void setPreRetailPrice(Double preRetailPrice) {
		this.preRetailPrice = preRetailPrice;
	}

	public Double getPreWholesalePrice() {
		return this.preWholesalePrice;
	}

	public void setPreWholesalePrice(Double preWholesalePrice) {
		this.preWholesalePrice = preWholesalePrice;
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

	public String getAdjustBillCode() {
		return this.adjustBillCode;
	}

	public void setAdjustBillCode(String adjustBillCode) {
		this.adjustBillCode = adjustBillCode;
	}

	public Double getPrePurchasePrice() {
		return this.prePurchasePrice;
	}

	public void setPrePurchasePrice(Double prePurchasePrice) {
		this.prePurchasePrice = prePurchasePrice;
	}

	public Double getPurchasePrice() {
		return this.purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Double getStoreSum() {
		return this.storeSum;
	}

	public void setStoreSum(Double storeSum) {
		this.storeSum = storeSum;
	}

	public Integer getProfitFlag() {
		return this.profitFlag;
	}

	public void setProfitFlag(Integer profitFlag) {
		this.profitFlag = profitFlag;
	}

	public Date getInureTime() {
		return this.inureTime;
	}

	public void setInureTime(Date inureTime) {
		this.inureTime = inureTime;
	}

	public Integer getDdAdjustMark() {
		return this.ddAdjustMark;
	}

	public void setDdAdjustMark(Integer ddAdjustMark) {
		this.ddAdjustMark = ddAdjustMark;
	}

	public Integer getDsAdjustMark() {
		return this.dsAdjustMark;
	}

	public void setDsAdjustMark(Integer dsAdjustMark) {
		this.dsAdjustMark = dsAdjustMark;
	}

	public String getTradeName() {
		return this.tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getSpecs() {
		return this.specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public String getProducer() {
		return this.producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getPackUnit() {
		return this.packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public Short getPackQty() {
		return this.packQty;
	}

	public void setPackQty(Short packQty) {
		this.packQty = packQty;
	}

	
	public String getMinUnit() {
		return minUnit;
	}

	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}

	public Integer getCurrentState() {
		return this.currentState;
	}

	public void setCurrentState(Integer currentState) {
		this.currentState = currentState;
	}

	public String getAbolishOper() {
		return this.abolishOper;
	}

	public void setAbolishOper(String abolishOper) {
		this.abolishOper = abolishOper;
	}

	public Date getAbolishDate() {
		return this.abolishDate;
	}

	public void setAbolishDate(Date abolishDate) {
		this.abolishDate = abolishDate;
	}

	public String getFileNo() {
		return this.fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getLastCompanycode() {
		return this.lastCompanycode;
	}

	public void setLastCompanycode(String lastCompanycode) {
		this.lastCompanycode = lastCompanycode;
	}

	public Double getLastInsum() {
		return this.lastInsum;
	}

	public void setLastInsum(Double lastInsum) {
		this.lastInsum = lastInsum;
	}

	public Double getLastPurchase() {
		return this.lastPurchase;
	}

	public void setLastPurchase(Double lastPurchase) {
		this.lastPurchase = lastPurchase;
	}

	public Double getLastWholesale() {
		return this.lastWholesale;
	}

	public void setLastWholesale(Double lastWholesale) {
		this.lastWholesale = lastWholesale;
	}

	public Double getLastRetail() {
		return this.lastRetail;
	}

	public void setLastRetail(Double lastRetail) {
		this.lastRetail = lastRetail;
	}

	public String getLastInbillcode() {
		return this.lastInbillcode;
	}

	public void setLastInbillcode(String lastInbillcode) {
		this.lastInbillcode = lastInbillcode;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setAdjustReason(String adjustReason) {
		this.adjustReason = adjustReason;
	}

	public String getAdjustReason() {
		return adjustReason;
	}

	public void setAdjustMode(Integer adjustMode) {
		this.adjustMode = adjustMode;
	}

	public Integer getAdjustMode() {
		return adjustMode;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public String getCheckUser() {
		return checkUser;
	}

	public void setCheckDept(String checkDept) {
		this.checkDept = checkDept;
	}

	public String getCheckDept() {
		return checkDept;
	}

	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}

	public String getCheckOpinion() {
		return checkOpinion;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setPrdoctName(String prdoctName) {
		this.prdoctName = prdoctName;
	}

	public String getPrdoctName() {
		return prdoctName;
	}

	public void setDrugStartdate(Date drugStartdate) {
		this.drugStartdate = drugStartdate;
	}

	public Date getDrugStartdate() {
		return drugStartdate;
	}

	public void setInureEnd(Date inureEnd) {
		this.inureEnd = inureEnd;
	}

	public Date getInureEnd() {
		return inureEnd;
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