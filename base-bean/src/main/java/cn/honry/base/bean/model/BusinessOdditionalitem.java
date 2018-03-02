package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class BusinessOdditionalitem extends Entity{
	
	
	//科室代码，全院统一附材'ROOT'
	private String deptCode;
	//是否药品,1药/2非药
	private Integer drugFlag;
	//分类，药品按用法，非药品按项目代码
	private String typeCode;
	//附属项目的代码
	private String itemCode;
	//数量
	private Double qty;
	//1动态计算/2固定收取
	private Integer changeFlag;
	//使用间隔
	private Integer useInterval;
	//单价
	private Double price;
	
	private String unit;//单位  HD 2015/12/10
	
	private Double totalPrice;//总价
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Integer getDrugFlag() {
		return drugFlag;
	}
	public void setDrugFlag(Integer drugFlag) {
		this.drugFlag = drugFlag;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public Integer getChangeFlag() {
		return changeFlag;
	}
	public void setChangeFlag(Integer changeFlag) {
		this.changeFlag = changeFlag;
	}
	public Integer getUseInterval() {
		return useInterval;
	}
	public void setUseInterval(Integer useInterval) {
		this.useInterval = useInterval;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUnit() {
		return unit;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	
	  
}
