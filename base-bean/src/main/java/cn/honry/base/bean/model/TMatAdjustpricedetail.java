package cn.honry.base.bean.model;

import java.util.Date;
import cn.honry.base.bean.business.Entity;

/**
 * 物资调价明细表 TMatAdjustpricedetail
 */

public class TMatAdjustpricedetail extends Entity {
	
	/**调价明细流水号 **/
	private String adjustDetailCode;
	/**调价单流水号 **/
	private String adjustCode;
	/**库存流水号 **/
	private String stockCode;
	/**仓库编码 **/
	private String storageCode;
	/**物品编码 **/
	private String itemCode;
	/**物资名称**/
	private String itemName;
	/**科目编码 **/
	private String kindCode;
	/**批号 **/
	private String batchNo;
	/**规格 **/
	private String specs;
	/**最小单位 **/
	private String minUnit;
	/**大包装单位 **/
	private String packUnit;
	/**大包装数量 **/
	private Long packQty;
	/**调价前零售价格 **/
	private Double preSalePrice;
	/**调价后零售价格 **/
	private Double salePrice;
	/**购入价 **/
	private Double inPrice;
	/**调价时库存量 **/
	private Double storeSum;
	/**盈亏标记(1-盈,0-亏) **/
	private Integer profitFlag;
	/**调价执行时间 **/
	private Date inureTime;
	/**备注 **/
	private String memo;
	/**操作员 **/
	private String operCode;
	/**操作日期 **/
	private Date operDate;
	/**是否高值耗材' **/
	private Integer isHighvalue;
	/**高值耗材条形码 **/
	private String highvalueBarcode;
	
	public String getAdjustDetailCode() {
		return adjustDetailCode;
	}
	public void setAdjustDetailCode(String adjustDetailCode) {
		this.adjustDetailCode = adjustDetailCode;
	}
	public String getAdjustCode() {
		return adjustCode;
	}
	public void setAdjustCode(String adjustCode) {
		this.adjustCode = adjustCode;
	}
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
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
	public String getKindCode() {
		return kindCode;
	}
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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
	public Long getPackQty() {
		return packQty;
	}
	public void setPackQty(Long packQty) {
		this.packQty = packQty;
	}
	public Double getPreSalePrice() {
		return preSalePrice;
	}
	public void setPreSalePrice(Double preSalePrice) {
		this.preSalePrice = preSalePrice;
	}
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	public Double getInPrice() {
		return inPrice;
	}
	public void setInPrice(Double inPrice) {
		this.inPrice = inPrice;
	}
	public Double getStoreSum() {
		return storeSum;
	}
	public void setStoreSum(Double storeSum) {
		this.storeSum = storeSum;
	}
	public Integer getProfitFlag() {
		return profitFlag;
	}
	public void setProfitFlag(Integer profitFlag) {
		this.profitFlag = profitFlag;
	}
	public Date getInureTime() {
		return inureTime;
	}
	public void setInureTime(Date inureTime) {
		this.inureTime = inureTime;
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
	public Integer getIsHighvalue() {
		return isHighvalue;
	}
	public void setIsHighvalue(Integer isHighvalue) {
		this.isHighvalue = isHighvalue;
	}
	public String getHighvalueBarcode() {
		return highvalueBarcode;
	}
	public void setHighvalueBarcode(String highvalueBarcode) {
		this.highvalueBarcode = highvalueBarcode;
	}
}