package cn.honry.base.bean.model;

import java.sql.Timestamp;

import cn.honry.base.bean.business.Entity;

/**
 * TYbServiceDict entity. @author MyEclipse Persistence Tools
 */

public class YbServiceDict extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer serialNo;
	private String serviceCode;
	private String serviceName;
	private Timestamp startTiem;
	private String costType;
	private String bedLevel;
	private String payStandard;
	private String pinyinCode;
	private String wbCode;
	private String maxPrice;
	private String cityMaxPrice;
	private String countyMaxPrice;
	private String otherPrice;
	private String lxPrice;
	private Timestamp stopTime;
	private String operator;
	private Timestamp operatorTime;
	private String eyPrice;

	/** 分页用的page和rows*/
	private String page;
	private String rows;
	// Constructors

	/** default constructor */
	public YbServiceDict() {
	}

	/** full constructor */
	public YbServiceDict(Integer serialNo, String serviceCode,
			String serviceName, Timestamp startTiem, String costType,
			String bedLevel, String payStandard, String pinyinCode,
			String wbCode, String maxPrice, String cityMaxPrice,
			String countyMaxPrice, String otherPrice, String lxPrice,
			Timestamp stopTime, String operator, Timestamp operatorTime,
			String eyPrice) {
		this.serialNo = serialNo;
		this.serviceCode = serviceCode;
		this.serviceName = serviceName;
		this.startTiem = startTiem;
		this.costType = costType;
		this.bedLevel = bedLevel;
		this.payStandard = payStandard;
		this.pinyinCode = pinyinCode;
		this.wbCode = wbCode;
		this.maxPrice = maxPrice;
		this.cityMaxPrice = cityMaxPrice;
		this.countyMaxPrice = countyMaxPrice;
		this.otherPrice = otherPrice;
		this.lxPrice = lxPrice;
		this.stopTime = stopTime;
		this.operator = operator;
		this.operatorTime = operatorTime;
		this.eyPrice = eyPrice;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getServiceCode() {
		return this.serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Timestamp getStartTiem() {
		return this.startTiem;
	}

	public void setStartTiem(Timestamp startTiem) {
		this.startTiem = startTiem;
	}

	public String getCostType() {
		return this.costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getBedLevel() {
		return this.bedLevel;
	}

	public void setBedLevel(String bedLevel) {
		this.bedLevel = bedLevel;
	}

	public String getPayStandard() {
		return this.payStandard;
	}

	public void setPayStandard(String payStandard) {
		this.payStandard = payStandard;
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

	public Timestamp getOperatorTime() {
		return this.operatorTime;
	}

	public void setOperatorTime(Timestamp operatorTime) {
		this.operatorTime = operatorTime;
	}

	public String getEyPrice() {
		return this.eyPrice;
	}

	public void setEyPrice(String eyPrice) {
		this.eyPrice = eyPrice;
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