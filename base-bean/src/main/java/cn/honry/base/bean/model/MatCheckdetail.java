package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class MatCheckdetail extends Entity implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String checkdetailCode;//盘点明细流水号
	private String checkCode;//盘点主表流水号
	private String storageCode;//仓库编码
	private String itemCode;//物品编码
	private String itemName;//物品名称
	private String stockCode;//库存流水号
	private String stockNo;//库存序号
	private Integer stockCollecttype;//库存汇总方式
	private Double inPrice;//购入价格(大包装)
	private Double salePrice;//零售价格
	private String specs;//规格
	private String minUnit;//最小单位
	private String packUnit;//大包装单位
	private Integer packQty;//大包装数量
	private String placeCode;//库位编号
	private String factoryCode;//生产厂家
	private String companyCode;//供货公司
	private Double fstoreNum;//封帐库存数量
	private Double adjustNum;//实际盘存数量
	private Double cstoreNum;//结存库存数量
	private Double inNum;//原始购入数量
	private Integer profitFlag;//盈亏标记(0-盘亏,1-盘盈,2-无盈亏)
	private Double profitLossNum;//盈亏数量
	private Integer checkState;//盘点状态(0-封帐,1-结存,2-取消)
	private String memo;//备注
	private String operCode;//操作员
	private Date operDate;//操作日期
	private Double inpriceLoss;//盘亏金额(入库价)
	private Double inpriceProfit;//盘盈金额(入库价)
	private Double salepriceLoss;//盘亏金额(零售价)
	private Double salepriceProfit;//盘盈金额(零售价)
	//传参用
	private String fstoreNum1;
	private String highvalueBarcode;
	private Double pdje;
	private Double pdlsje;
	
	public Double getInpriceProfit() {
		return inpriceProfit;
	}
	public void setInpriceProfit(Double inpriceProfit) {
		this.inpriceProfit = inpriceProfit;
	}
	public Double getSalepriceProfit() {
		return salepriceProfit;
	}
	public void setSalepriceProfit(Double salepriceProfit) {
		this.salepriceProfit = salepriceProfit;
	}
	public Double getPdje() {
		return pdje;
	}
	public void setPdje(Double pdje) {
		this.pdje = pdje;
	}
	public Double getPdlsje() {
		return pdlsje;
	}
	public void setPdlsje(Double pdlsje) {
		this.pdlsje = pdlsje;
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
	public String getCheckdetailCode() {
		return checkdetailCode;
	}
	public String getFstoreNum1() {
		return fstoreNum1;
	}
	public void setFstoreNum1(String fstoreNum1) {
		this.fstoreNum1 = fstoreNum1;
	}
	public String getHighvalueBarcode() {
		return highvalueBarcode;
	}
	public void setHighvalueBarcode(String highvalueBarcode) {
		this.highvalueBarcode = highvalueBarcode;
	}
	public void setCheckdetailCode(String checkdetailCode) {
		this.checkdetailCode = checkdetailCode;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
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
	public Integer getStockCollecttype() {
		return stockCollecttype;
	}
	public void setStockCollecttype(Integer stockCollecttype) {
		this.stockCollecttype = stockCollecttype;
	}
	public Double getInPrice() {
		return inPrice;
	}
	public void setInPrice(Double inPrice) {
		this.inPrice = inPrice;
	}
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public String getMinUnit() {
		return minUnit;
	}
	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
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
	public String getPlaceCode() {
		return placeCode;
	}
	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
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
	public Double getFstoreNum() {
		return fstoreNum;
	}
	public void setFstoreNum(Double fstoreNum) {
		this.fstoreNum = fstoreNum;
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
	public Double getInNum() {
		return inNum;
	}
	public void setInNum(Double inNum) {
		this.inNum = inNum;
	}
	public Integer getProfitFlag() {
		return profitFlag;
	}
	public void setProfitFlag(Integer profitFlag) {
		this.profitFlag = profitFlag;
	}
	public Double getProfitLossNum() {
		return profitLossNum;
	}
	public void setProfitLossNum(Double profitLossNum) {
		this.profitLossNum = profitLossNum;
	}
	public Integer getCheckState() {
		return checkState;
	}
	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
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
	

}
