package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 *  物资库管表
 */
@SuppressWarnings("serial")
public class MatStockInfo extends Entity {

	// Fields
	/**库房编码**/
	private String itemDeptCode;
	/**版本号**/
	private int version;
	/**物资编码**/
	private String itemCode;
	/**物资名称**/
	private String itemName;
	/**物资分类**/
	private String itemKind;
	/**包装单位**/
	private String packUnit;
	/**包装数量**/
	private Long packQty;
	/**小包装单位**/
	private String minUnit;
	/**小包装数**/
	private Long miniQty;
	/**显示的单位标记(0最小单位,1包装单位)**/
	private Integer showFlag;
	/**参考零售价**/
	private Double retailPrice;
	/**总数量**/
	private Double storeSum;
	/**总金额**/
	private Double storeCost;
	/**预扣库存数量**/
	private Long preoutSum;
	/**预扣库存金额**/
	private Double preoutCost;
	/**生产日期**/
	private Date produceDate;
	/**有效期**/
	private Date validDate;
	/**货位号**/
	private String placeCode;
	/**生产厂家**/
	private String factoryCode;
	/**供货公司**/
	private String companyCode;
	/**上限数量**/
	private Double topNum;
	/**下限数量**/
	private Double lowNum;
	/**缺货标志(1-缺货,0-否)**/
	private Integer lackFlag;
	/**备注**/
	private String mark;
	/**日盘点标记**/
	private String dailtycheckFlag;
	/**默认单位标记(0－最小单位,1－包装单位)**/
	private Integer unitFlag;
	/**有效性状态 (1-在用,0-停用,2-废弃)**/
	private Integer validState;
	/**操作员**/
	private String operCode;
	/**操作日期**/
	private Date operDate;
	/**高值耗材标志**/
	private Integer highvalueFlag;
	/**高值耗材条形码**/
	private String highvalueBarcode;
	/**灭菌日期**/
	private Date disinfectionDate;
	public String getItemDeptCode() {
		return itemDeptCode;
	}
	public void setItemDeptCode(String itemDeptCode) {
		this.itemDeptCode = itemDeptCode;
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
	public String getItemKind() {
		return itemKind;
	}
	public void setItemKind(String itemKind) {
		this.itemKind = itemKind;
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
	public String getMinUnit() {
		return minUnit;
	}
	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}
	public Long getMiniQty() {
		return miniQty;
	}
	public void setMiniQty(Long miniQty) {
		this.miniQty = miniQty;
	}
	public Integer getShowFlag() {
		return showFlag;
	}
	public void setShowFlag(Integer showFlag) {
		this.showFlag = showFlag;
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
	public Long getPreoutSum() {
		return preoutSum;
	}
	public void setPreoutSum(Long preoutSum) {
		this.preoutSum = preoutSum;
	}
	public Double getPreoutCost() {
		return preoutCost;
	}
	public void setPreoutCost(Double preoutCost) {
		this.preoutCost = preoutCost;
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
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getDailtycheckFlag() {
		return dailtycheckFlag;
	}
	public void setDailtycheckFlag(String dailtycheckFlag) {
		this.dailtycheckFlag = dailtycheckFlag;
	}
	public Integer getUnitFlag() {
		return unitFlag;
	}
	public void setUnitFlag(Integer unitFlag) {
		this.unitFlag = unitFlag;
	}
	public Integer getValidState() {
		return validState;
	}
	public void setValidState(Integer validState) {
		this.validState = validState;
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
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	
	
	
}
