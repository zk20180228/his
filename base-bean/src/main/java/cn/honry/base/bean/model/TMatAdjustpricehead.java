package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 物资调价主表	TMatAdjustpricehead
 */

public class TMatAdjustpricehead extends Entity{
	
	/**调价单流水号 **/
	private String adjustCode;
	/**调价单据号 **/
	private String adjustListCode;
	/**调价单内序号 **/
	private String serialCode;
	/**仓库编码 **/
	private String storageCode;
	/**物品编码 **/
	private String itemCode;
	/**物资名称 **/
	private String itemName;
	/**科目编码 **/
	private String kindCode;
	/**规格 **/
	private String specs;
	/**批号 **/
	private String batchNo;
	/**最小单位 **/
	private String minUnit;
	/**调价前零售价格 **/
	private Double preSalePrice;
	/**调价后零售价格 **/
	private Double salePrice;
	/**调价时库存量 **/
	private Double storeSum;
	/**'盈亏标记(1-盈,0-亏) **/
	private Integer profitFlag;
	/**调价执行时间 **/
	private Date inureTime;
	/**调价单状态(0-未调价;1-已调价;2-无效) **/
	private Integer currentState;
	/**调价文件编码 **/
	private String adjustFileId;
	/**调价文件号 **/
	private String adjustFileNo;
	/** 调价依据**/
	private String adjustGist;
	/**财务收费物品标志(0－否,1－是 **/
	private Integer financeFlag;
	/**作废人编码 **/
	private String abolishOper;
	/**作废时间 **/
	private Date abolishDate;
	/**库存汇总方式 **/
	private Boolean stockCollecttype;
	/**备注 **/
	private String memo;
	/**操作员编码 **/
	private String operCode;
	/**操作时间 **/
	private Date operDate;
	/**是否高值耗材 **/
	private Integer isHighvalue;
	/**高值耗材条形码 **/
	private String highvalueBarcode;
	
	public String getAdjustCode() {
		return adjustCode;
	}
	public void setAdjustCode(String adjustCode) {
		this.adjustCode = adjustCode;
	}
	public String getAdjustListCode() {
		return adjustListCode;
	}
	public void setAdjustListCode(String adjustListCode) {
		this.adjustListCode = adjustListCode;
	}
	public String getSerialCode() {
		return serialCode;
	}
	public void setSerialCode(String serialCode) {
		this.serialCode = serialCode;
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
	public String getMinUnit() {
		return minUnit;
	}
	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
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
	public Integer getCurrentState() {
		return currentState;
	}
	public void setCurrentState(Integer currentState) {
		this.currentState = currentState;
	}
	public String getAdjustFileId() {
		return adjustFileId;
	}
	public void setAdjustFileId(String adjustFileId) {
		this.adjustFileId = adjustFileId;
	}
	public String getAdjustFileNo() {
		return adjustFileNo;
	}
	public void setAdjustFileNo(String adjustFileNo) {
		this.adjustFileNo = adjustFileNo;
	}
	public String getAdjustGist() {
		return adjustGist;
	}
	public void setAdjustGist(String adjustGist) {
		this.adjustGist = adjustGist;
	}
	public Integer getFinanceFlag() {
		return financeFlag;
	}
	public void setFinanceFlag(Integer financeFlag) {
		this.financeFlag = financeFlag;
	}
	public String getAbolishOper() {
		return abolishOper;
	}
	public void setAbolishOper(String abolishOper) {
		this.abolishOper = abolishOper;
	}
	public Date getAbolishDate() {
		return abolishDate;
	}
	public void setAbolishDate(Date abolishDate) {
		this.abolishDate = abolishDate;
	}
	public Boolean getStockCollecttype() {
		return stockCollecttype;
	}
	public void setStockCollecttype(Boolean stockCollecttype) {
		this.stockCollecttype = stockCollecttype;
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