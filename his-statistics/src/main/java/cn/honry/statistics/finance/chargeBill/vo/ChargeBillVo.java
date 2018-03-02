package cn.honry.statistics.finance.chargeBill.vo;

import java.util.Date;

public class ChargeBillVo {
	/**
	 * 项目名称
	 */
	private String itemName;
	/**
	 * 当前单位
	 */
	private String currentUnit;
	/**
	 * 执行科室
	 */
	private String executeDeptcode;
	/**
	 * 收费人
	 */
	private String chargeOpercode;
	/**
	 * 物品类别（y：药品，f：非药品）
	 */
	private String state;
	/**
	 * 费用金额
	 */
	private Double totCost;
	/**
	 * 自费金额
	 */
	private Double ownCost;
	/**
	 * 自付金额
	 */
	private Double payCost;
	/**
	 * 公费金额
	 */
	private Double pubCost;
	/**
	 * 优惠金额
	 */
	private Double ecoCost;
	/**
	 * 单价
	 */
	private Double unitPrice;
	/**
	 * 数量
	 */
	private Double qty;
	/**
	 * 收费时间
	 */
	private Date chargeDate;
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getCurrentUnit() {
		return currentUnit;
	}
	public void setCurrentUnit(String currentUnit) {
		this.currentUnit = currentUnit;
	}
	public String getExecuteDeptcode() {
		return executeDeptcode;
	}
	public void setExecuteDeptcode(String executeDeptcode) {
		this.executeDeptcode = executeDeptcode;
	}
	public String getChargeOpercode() {
		return chargeOpercode;
	}
	public void setChargeOpercode(String chargeOpercode) {
		this.chargeOpercode = chargeOpercode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public Double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}
	public Double getPubCost() {
		return pubCost;
	}
	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}
	public Double getEcoCost() {
		return ecoCost;
	}
	public void setEcoCost(Double ecoCost) {
		this.ecoCost = ecoCost;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public Date getChargeDate() {
		return chargeDate;
	}
	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}
	
}
