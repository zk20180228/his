package cn.honry.finance.medicinelist.vo;

public class InfoVo {
	private String itemCode;//项目编号
	private String itemName;//项目名称
	private Integer qty;//数量
	private String priceUnit;//单位
	private String stackId;//组套编码
	private String stackName;//组套名称
	private Double unitPrice;//价格
	private Double payCost;//金额
	private String isDrug;//是否为药品
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
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public String getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}
	public String getStackId() {
		return stackId;
	}
	public void setStackId(String stackId) {
		this.stackId = stackId;
	}
	public String getStackName() {
		return stackName;
	}
	public void setStackName(String stackName) {
		this.stackName = stackName;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}
	public String getIsDrug() {
		return isDrug;
	}
	public void setIsDrug(String isDrug) {
		this.isDrug = isDrug;
	}
	
}
