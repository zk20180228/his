package cn.honry.base.bean.model;

import java.io.Serializable;

import cn.honry.base.bean.business.Entity;
/**
 * 病区退费申请物资信息表
 * @author  lyy
 * @createDate： 2016年1月26日 下午8:35:57 
 * @modifier lyy
 * @modifyDate：2016年1月26日 下午8:35:57  
 * @modifyRmk：  
 * @version 1.0
 */
public class MaterialsCancelmetlist extends Entity implements Serializable {
	private String applyNo;   //申请流水号
	private String outNo;   //出库单流水号
	private String stockNo;  //库存序号
	private String recipeNo;  //处方号
	private Integer sequenceNo;  //处方内项目流水号
	private String itemCode;   //物品编码
	private String itemName;   //物品名称
	private String specs;  //规格
	private String statUnit;   //计量单位
	private Double salePrice;   //零售单价
	private Integer outNum;   //出库数量
	private String cancelflag;   //确认标识（0申请，1退费，2作废）
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public String getOutNo() {
		return outNo;
	}
	public void setOutNo(String outNo) {
		this.outNo = outNo;
	}
	public String getStockNo() {
		return stockNo;
	}
	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
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
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public String getStatUnit() {
		return statUnit;
	}
	public void setStatUnit(String statUnit) {
		this.statUnit = statUnit;
	}
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	public Integer getOutNum() {
		return outNum;
	}
	public void setOutNum(Integer outNum) {
		this.outNum = outNum;
	}
	public String getCancelflag() {
		return cancelflag;
	}
	public void setCancelflag(String cancelflag) {
		this.cancelflag = cancelflag;
	}
	
}
