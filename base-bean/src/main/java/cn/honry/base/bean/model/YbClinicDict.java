package cn.honry.base.bean.model;

import java.sql.Timestamp;

import cn.honry.base.bean.business.Entity;

/**
 * TYbClinicDict entity. @author MyEclipse Persistence Tools
 */

public class YbClinicDict extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer serialNo;
	private String itemCode;
	private Timestamp startTime;
	private String itemName;
	private String costType;
	private String costItemGrade;
	private String itemUnit;
	private String pinyinCode;
	private String wbCode;
	private String maxPrice;
	private String cityMaxPrice;
	private String countyMaxPrice;
	private String otherPrice;
	private String lxPrice;
	private String outSelfProportion;
	private String inSelfProportion;
	private String lxSelfProportion;
	private String gsSelfProportion;
	private String sySelfProportion;
	private String eySelfProportion;
	private String insuranceNo;
	private String approveFlag;
	private Timestamp stopTime;
	private String operator;
	private Timestamp operatorTiem;
	private String memo;
	private String eyPrice;
	private String insuranceItemCode;
	
	/** 分页用的page和rows*/
	private String page;
	private String rows;

	// Constructors

	/** default constructor */
	public YbClinicDict() {
	}

	/** full constructor */
	public YbClinicDict(Integer serialNo, String itemCode, Timestamp startTime,
			String itemName, String costType, String costItemGrade,
			String itemUnit, String pinyinCode, String wbCode, String maxPrice,
			String cityMaxPrice, String countyMaxPrice, String otherPrice,
			String lxPrice, String outSelfProportion, String inSelfProportion,
			String lxSelfProportion, String gsSelfProportion,
			String sySelfProportion, String eySelfProportion,
			String insuranceNo, String approveFlag, Timestamp stopTime,
			String operator, Timestamp operatorTiem, String memo,
			String eyPrice, String insuranceItemCode) {
		this.serialNo = serialNo;
		this.itemCode = itemCode;
		this.startTime = startTime;
		this.itemName = itemName;
		this.costType = costType;
		this.costItemGrade = costItemGrade;
		this.itemUnit = itemUnit;
		this.pinyinCode = pinyinCode;
		this.wbCode = wbCode;
		this.maxPrice = maxPrice;
		this.cityMaxPrice = cityMaxPrice;
		this.countyMaxPrice = countyMaxPrice;
		this.otherPrice = otherPrice;
		this.lxPrice = lxPrice;
		this.outSelfProportion = outSelfProportion;
		this.inSelfProportion = inSelfProportion;
		this.lxSelfProportion = lxSelfProportion;
		this.gsSelfProportion = gsSelfProportion;
		this.sySelfProportion = sySelfProportion;
		this.eySelfProportion = eySelfProportion;
		this.insuranceNo = insuranceNo;
		this.approveFlag = approveFlag;
		this.stopTime = stopTime;
		this.operator = operator;
		this.operatorTiem = operatorTiem;
		this.memo = memo;
		this.eyPrice = eyPrice;
		this.insuranceItemCode = insuranceItemCode;
	}
	public Integer getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getCostType() {
		return this.costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getCostItemGrade() {
		return this.costItemGrade;
	}

	public void setCostItemGrade(String costItemGrade) {
		this.costItemGrade = costItemGrade;
	}

	public String getItemUnit() {
		return this.itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public String getPinyinCode() {
		return this.pinyinCode;
	}

	public void setPinyinCode(String pinyinCode) {
		this.pinyinCode = pinyinCode;
	}

	public String getWbCode() {
		return this.wbCode;
	}

	public void setWbCode(String wbCode) {
		this.wbCode = wbCode;
	}

	public String getMaxPrice() {
		return this.maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getCityMaxPrice() {
		return this.cityMaxPrice;
	}

	public void setCityMaxPrice(String cityMaxPrice) {
		this.cityMaxPrice = cityMaxPrice;
	}

	public String getCountyMaxPrice() {
		return this.countyMaxPrice;
	}

	public void setCountyMaxPrice(String countyMaxPrice) {
		this.countyMaxPrice = countyMaxPrice;
	}

	public String getOtherPrice() {
		return this.otherPrice;
	}

	public void setOtherPrice(String otherPrice) {
		this.otherPrice = otherPrice;
	}

	public String getLxPrice() {
		return this.lxPrice;
	}

	public void setLxPrice(String lxPrice) {
		this.lxPrice = lxPrice;
	}

	public String getOutSelfProportion() {
		return this.outSelfProportion;
	}

	public void setOutSelfProportion(String outSelfProportion) {
		this.outSelfProportion = outSelfProportion;
	}

	public String getInSelfProportion() {
		return this.inSelfProportion;
	}

	public void setInSelfProportion(String inSelfProportion) {
		this.inSelfProportion = inSelfProportion;
	}

	public String getLxSelfProportion() {
		return this.lxSelfProportion;
	}

	public void setLxSelfProportion(String lxSelfProportion) {
		this.lxSelfProportion = lxSelfProportion;
	}

	public String getGsSelfProportion() {
		return this.gsSelfProportion;
	}

	public void setGsSelfProportion(String gsSelfProportion) {
		this.gsSelfProportion = gsSelfProportion;
	}

	public String getSySelfProportion() {
		return this.sySelfProportion;
	}

	public void setSySelfProportion(String sySelfProportion) {
		this.sySelfProportion = sySelfProportion;
	}

	public String getEySelfProportion() {
		return this.eySelfProportion;
	}

	public void setEySelfProportion(String eySelfProportion) {
		this.eySelfProportion = eySelfProportion;
	}

	public String getInsuranceNo() {
		return this.insuranceNo;
	}

	public void setInsuranceNo(String insuranceNo) {
		this.insuranceNo = insuranceNo;
	}

	public String getApproveFlag() {
		return this.approveFlag;
	}

	public void setApproveFlag(String approveFlag) {
		this.approveFlag = approveFlag;
	}

	public Timestamp getStopTime() {
		return this.stopTime;
	}

	public void setStopTime(Timestamp stopTime) {
		this.stopTime = stopTime;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Timestamp getOperatorTiem() {
		return this.operatorTiem;
	}

	public void setOperatorTiem(Timestamp operatorTiem) {
		this.operatorTiem = operatorTiem;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getEyPrice() {
		return this.eyPrice;
	}

	public void setEyPrice(String eyPrice) {
		this.eyPrice = eyPrice;
	}

	public String getInsuranceItemCode() {
		return this.insuranceItemCode;
	}

	public void setInsuranceItemCode(String insuranceItemCode) {
		this.insuranceItemCode = insuranceItemCode;
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