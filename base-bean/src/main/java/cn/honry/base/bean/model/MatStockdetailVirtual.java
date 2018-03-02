package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * TMatStockdetailVirtual entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class MatStockdetailVirtual extends Entity {

	// Fields
	private String stockCode;
	private String stockNo;
	private String storageCode;
	private String kindCode;
	private String itemCode;
	private String itemName;
	private String regCode;
	private String specs;
	private String batchNo;
	private String placeCode;
	private Double storeNum;
	private Double storeCost;
	private String minUnit;
	private String inNo;
	private Double inNum;
	private Double inPrice;
	private Date inputDate;
	private Double salePrice;
	private Double saleCost;
	private Long packQty;
	private String packUnit;
	private Double packPrice;
	private Date produceDate;
	private Date validDate;
	private String factoryCode;
	private String companyCode;
	private Date outputDate;
	private Integer validState;
	private Integer addrateFlag;
	private Date addrateDate;
	private String addrateOper;
	private String barCode;
	private String class2Priv;
	private String class3Priv;
	private Double topNum;
	private Double lowNum;
	private String lackFlag;
	private String memo;
	private String operCode;
	private Date operDate;
	private Integer highvalueFlag;
	private String highvalueBarcode;
	private Date disinfectionDate;
	
	private Double returnedNum;
	private Integer detectFlag;
	private Double inCost;
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
	public Double getInNum() {
		return inNum;
	}
	public void setInNum(Double inNum) {
		this.inNum = inNum;
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
	public Long getPackQty() {
		return packQty;
	}
	public void setPackQty(Long packQty) {
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
	public String getClass2Priv() {
		return class2Priv;
	}
	public void setClass2Priv(String class2Priv) {
		this.class2Priv = class2Priv;
	}
	public String getClass3Priv() {
		return class3Priv;
	}
	public void setClass3Priv(String class3Priv) {
		this.class3Priv = class3Priv;
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
	public String getLackFlag() {
		return lackFlag;
	}
	public void setLackFlag(String lackFlag) {
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
	public Integer getDetectFlag() {
		return detectFlag;
	}
	public void setDetectFlag(Integer detectFlag) {
		this.detectFlag = detectFlag;
	}
	public Double getInCost() {
		return inCost;
	}
	public void setInCost(Double inCost) {
		this.inCost = inCost;
	}
}