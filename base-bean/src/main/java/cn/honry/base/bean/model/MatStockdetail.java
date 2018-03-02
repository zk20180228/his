package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * MatStockdetail entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class MatStockdetail extends Entity {

	// Fields
	private String stockCode;//STOCK_CODE
	private int version;//版本号
	private String stockNo;//库存序号
	private String storageCode;//仓库代码
	private String kindCode;//科目编码
	private String itemCode;//物品编码
	private String itemName;//物资名称
	private String regCode;//生产厂家认证记录编号
	private String specs;//规格
	private String batchNo;//批号
	private String placeCode;//库位编号
	private Double storeNum;//库存数量
	private Double storeCost;//库存金额
	private String minUnit;//最小单位
	private String inNo;//入库记录流水号 流水记录
	private Double inNum;//购入数量
	private Double inPrice;//购入价格(大包装)
	private Date inputDate;//购入日期
	private Double salePrice;//零售价格
	private Double saleCost;//零售金额
	private Integer packQty;//大包装数量
	private String packUnit;//大包装单位
	private Double packPrice;//大包装价格(购入价)
	private Date produceDate;//生产日期
	private Date validDate;//有效期
	private String factoryCode;//生产厂家
	private String companyCode;//供货公司
	private Date outputDate;//出库日期
	private Integer validState;//有效性状态(0-停用,1-在用,2-废弃)
	private Integer addrateFlag;//加价标志(0-未加价,1-已加价)
	private Date addrateDate;//加价时间
	private String addrateOper;//加价人
	private String barCode;//条形码
	private Double topNum;//上限数量
	private Double lowNum;//下限数量
	private Integer lackFlag;//缺货标志(1-缺货,0-否)
	private String memo;//备注
	private String operCode;//操作员
	private Date operDate;//操作日期
	private Integer highvalueFlag;//高值耗材标志
	private String highvalueBarcode;//高值耗材条形码
	private Date disinfectionDate;//灭菌日期
	//显示作用
	private Double returnedNum;
	private Double inCost;
	private String invoiceNO;
	private Date invoiceDate;
	private String code;
	private Integer no;
	private Double profitLossNum;//盈亏数量
	private Double fstoreNum;//封帐库存数量
	private Double adjustNum;//实际盘存数量
	private Double cstoreNum;//结存库存数量
	private Double inpriceLoss;//盘亏金额(入库价) 
	private Double salepriceLoss;//盘亏金额(零售价)
	private String checkCode;//盘点流水号
	//虚拟字段
	/**拼音编码**/
	private String spellCode;
	/**五笔码**/
	private String wbCode;
	/**自定义码**/
	private String customCode;
	
	public String getSpellCode() {
		return spellCode;
	}
	public void setSpellCode(String spellCode) {
		this.spellCode = spellCode;
	}
	public String getWbCode() {
		return wbCode;
	}
	public void setWbCode(String wbCode) {
		this.wbCode = wbCode;
	}
	public String getCustomCode() {
		return customCode;
	}
	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	public Double getFstoreNum() {
		return fstoreNum;
	}
	public void setFstoreNum(Double fstoreNum) {
		this.fstoreNum = fstoreNum;
	}
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public String getStockNo() {
		return stockNo;
	}
	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}
	public String getKindCode() {
		return kindCode;
	}
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getRegCode() {
		return regCode;
	}
	public void setRegCode(String regCode) {
		this.regCode = regCode;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getPlaceCode() {
		return placeCode;
	}
	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
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
	public String getMinUnit() {
		return minUnit;
	}
	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}
	public String getInNo() {
		return inNo;
	}
	public void setInNo(String inNo) {
		this.inNo = inNo;
	}
	public Double getInPrice() {
		return inPrice;
	}
	public void setInPrice(Double inPrice) {
		this.inPrice = inPrice;
	}
	public Date getInputDate() {
		return inputDate;
	}
	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	public Double getSaleCost() {
		return saleCost;
	}
	public void setSaleCost(Double saleCost) {
		this.saleCost = saleCost;
	}
	
	public Integer getPackQty() {
		return packQty;
	}
	public void setPackQty(Integer packQty) {
		this.packQty = packQty;
	}
	public String getPackUnit() {
		return packUnit;
	}
	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}
	public Double getPackPrice() {
		return packPrice;
	}
	public void setPackPrice(Double packPrice) {
		this.packPrice = packPrice;
	}
	public Date getProduceDate() {
		return produceDate;
	}
	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public Date getOutputDate() {
		return outputDate;
	}
	public void setOutputDate(Date outputDate) {
		this.outputDate = outputDate;
	}
	public Integer getValidState() {
		return validState;
	}
	public void setValidState(Integer validState) {
		this.validState = validState;
	}
	public Integer getAddrateFlag() {
		return addrateFlag;
	}
	public void setAddrateFlag(Integer addrateFlag) {
		this.addrateFlag = addrateFlag;
	}
	public Date getAddrateDate() {
		return addrateDate;
	}
	public void setAddrateDate(Date addrateDate) {
		this.addrateDate = addrateDate;
	}
	public String getAddrateOper() {
		return addrateOper;
	}
	public void setAddrateOper(String addrateOper) {
		this.addrateOper = addrateOper;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public Double getTopNum() {
		return topNum;
	}
	public void setTopNum(Double topNum) {
		this.topNum = topNum;
	}
	public Double getLowNum() {
		return lowNum;
	}
	public void setLowNum(Double lowNum) {
		this.lowNum = lowNum;
	}
	public Integer getLackFlag() {
		return lackFlag;
	}
	public void setLackFlag(Integer lackFlag) {
		this.lackFlag = lackFlag;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public Integer getHighvalueFlag() {
		return highvalueFlag;
	}
	public void setHighvalueFlag(Integer highvalueFlag) {
		this.highvalueFlag = highvalueFlag;
	}
	public String getHighvalueBarcode() {
		return highvalueBarcode;
	}
	public void setHighvalueBarcode(String highvalueBarcode) {
		this.highvalueBarcode = highvalueBarcode;
	}
	public Date getDisinfectionDate() {
		return disinfectionDate;
	}
	public void setDisinfectionDate(Date disinfectionDate) {
		this.disinfectionDate = disinfectionDate;
	}
	public Double getReturnedNum() {
		return returnedNum;
	}
	public void setReturnedNum(Double returnedNum) {
		this.returnedNum = returnedNum;
	}
	public Double getInCost() {
		return inCost;
	}
	public void setInCost(Double inCost) {
		this.inCost = inCost;
	}
	public String getInvoiceNO() {
		return invoiceNO;
	}
	public void setInvoiceNO(String invoiceNO) {
		this.invoiceNO = invoiceNO;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public Double getInNum() {
		return inNum;
	}
	public void setInNum(Double inNum) {
		this.inNum = inNum;
	}
	public Double getProfitLossNum() {
		return profitLossNum;
	}
	public void setProfitLossNum(Double profitLossNum) {
		this.profitLossNum = profitLossNum;
	}
	public Double getAdjustNum() {
		return adjustNum;
	}
	public void setAdjustNum(Double adjustNum) {
		this.adjustNum = adjustNum;
	}
	public Double getCstoreNum() {
		return cstoreNum;
	}
	public void setCstoreNum(Double cstoreNum) {
		this.cstoreNum = cstoreNum;
	}
	public Double getInpriceLoss() {
		return inpriceLoss;
	}
	public void setInpriceLoss(Double inpriceLoss) {
		this.inpriceLoss = inpriceLoss;
	}
	public Double getSalepriceLoss() {
		return salepriceLoss;
	}
	public void setSalepriceLoss(Double salepriceLoss) {
		this.salepriceLoss = salepriceLoss;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
}