package cn.honry.base.bean.model;

import java.sql.Timestamp;

import cn.honry.base.bean.business.Entity;

/**
 * TYbHisvsyb entity. @author MyEclipse Persistence Tools
 */

public class YbHisvsyb extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer serialNo;
	private String insuranceNo;
	private String itemCode;
	private String itemName;
	private String pinyinCode;
	private String wbCode;
	private String itemType;
	private String ybItemCode;
	private String ybItemName;
	private String ybItemLevel;
	private String ybCostType;
	private String ybDrugDosageform;
	private String ybItemSpec;
	private String ybItemUnit;
	private String ybItemPrice;
	private String inSelfProportion;
	private String costType;
	private String itemDosageform;
	private String itemSpec;
	private String itemUnit;
	private Double itemPrice;
	private String approveFlag;
	private Timestamp applyTime;
	private String memo;
	private String operator;
	private Timestamp operatorTime;
	/** 分页用的page和rows*/
	private String page;
	private String rows;
	// Constructors

	/** default constructor */
	public YbHisvsyb() {
	}

	/** full constructor */
	public YbHisvsyb(Integer serialNo, String insuranceNo, String itemCode,
			String itemName,String pinyinCode,String wbCode, String itemType, String ybItemCode,
			String ybItemName, String ybItemLevel, String ybCostType,
			String ybDrugDosageform, String ybItemSpec, String ybItemUnit,
			String ybItemPrice, String inSelfProportion, String costType,
			String itemDosageform, String itemSpec, String itemUnit,
			Double itemPrice, String approveFlag, Timestamp applyTime,
			String memo, String operator, Timestamp operatorTime) {
		this.serialNo = serialNo;
		this.insuranceNo = insuranceNo;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.pinyinCode=pinyinCode;
		this.wbCode=wbCode;
		this.itemType = itemType;
		this.ybItemCode = ybItemCode;
		this.ybItemName = ybItemName;
		this.ybItemLevel = ybItemLevel;
		this.ybCostType = ybCostType;
		this.ybDrugDosageform = ybDrugDosageform;
		this.ybItemSpec = ybItemSpec;
		this.ybItemUnit = ybItemUnit;
		this.ybItemPrice = ybItemPrice;
		this.inSelfProportion = inSelfProportion;
		this.costType = costType;
		this.itemDosageform = itemDosageform;
		this.itemSpec = itemSpec;
		this.itemUnit = itemUnit;
		this.itemPrice = itemPrice;
		this.approveFlag = approveFlag;
		this.applyTime = applyTime;
		this.memo = memo;
		this.operator = operator;
		this.operatorTime = operatorTime;
	}
	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getInsuranceNo() {
		return this.insuranceNo;
	}

	public void setInsuranceNo(String insuranceNo) {
		this.insuranceNo = insuranceNo;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getPinyinCode() {
		return pinyinCode;
	}

	public void setPinyinCode(String pinyinCode) {
		this.pinyinCode = pinyinCode;
	}

	public String getWbCode() {
		return wbCode;
	}

	public void setWbCode(String wbCode) {
		this.wbCode = wbCode;
	}

	public String getItemType() {
		return this.itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getYbItemCode() {
		return this.ybItemCode;
	}

	public void setYbItemCode(String ybItemCode) {
		this.ybItemCode = ybItemCode;
	}

	public String getYbItemName() {
		return this.ybItemName;
	}

	public void setYbItemName(String ybItemName) {
		this.ybItemName = ybItemName;
	}

	public String getYbItemLevel() {
		return this.ybItemLevel;
	}

	public void setYbItemLevel(String ybItemLevel) {
		this.ybItemLevel = ybItemLevel;
	}

	public String getYbCostType() {
		return this.ybCostType;
	}

	public void setYbCostType(String ybCostType) {
		this.ybCostType = ybCostType;
	}

	public String getYbDrugDosageform() {
		return this.ybDrugDosageform;
	}

	public void setYbDrugDosageform(String ybDrugDosageform) {
		this.ybDrugDosageform = ybDrugDosageform;
	}

	public String getYbItemSpec() {
		return this.ybItemSpec;
	}

	public void setYbItemSpec(String ybItemSpec) {
		this.ybItemSpec = ybItemSpec;
	}

	public String getYbItemUnit() {
		return this.ybItemUnit;
	}

	public void setYbItemUnit(String ybItemUnit) {
		this.ybItemUnit = ybItemUnit;
	}

	public String getYbItemPrice() {
		return this.ybItemPrice;
	}

	public void setYbItemPrice(String ybItemPrice) {
		this.ybItemPrice = ybItemPrice;
	}

	public String getInSelfProportion() {
		return this.inSelfProportion;
	}

	public void setInSelfProportion(String inSelfProportion) {
		this.inSelfProportion = inSelfProportion;
	}

	public String getCostType() {
		return this.costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getItemDosageform() {
		return this.itemDosageform;
	}

	public void setItemDosageform(String itemDosageform) {
		this.itemDosageform = itemDosageform;
	}

	public String getItemSpec() {
		return this.itemSpec;
	}

	public void setItemSpec(String itemSpec) {
		this.itemSpec = itemSpec;
	}

	public String getItemUnit() {
		return this.itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public Double getItemPrice() {
		return this.itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getApproveFlag() {
		return this.approveFlag;
	}

	public void setApproveFlag(String approveFlag) {
		this.approveFlag = approveFlag;
	}

	public Timestamp getApplyTime() {
		return this.applyTime;
	}

	public void setApplyTime(Timestamp applyTime) {
		this.applyTime = applyTime;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Timestamp getOperatorTime() {
		return this.operatorTime;
	}

	public void setOperatorTime(Timestamp operatorTime) {
		this.operatorTime = operatorTime;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}