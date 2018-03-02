package cn.honry.base.bean.model;

import java.math.BigDecimal;

/**
 * BiBaseContractunit entity. @author MyEclipse Persistence Tools
 */

public class BiBaseContractunit implements java.io.Serializable {

	// Fields

	private String unitCode;
	private String unitName;
	private String paykindCode;
	private String paykindName;
	private String priceForm;
	private Double pubRatio;
	private Double payRatio;
	private Double ownRatio;
	private Double ecoRatio;
	private Double arrRatio;
	private String babyFlag;
	private String mcardFlag;
	private String controlFlag;
	private String flag;
	private Double dayLimit;
	private Double monthLimit;
	private Double yearLimit;
	private Double onceLimit;
	private Double bedLimit;
	private Double airLimit;
	private String unitDescription;
	private BigDecimal unitOrder;

	// Constructors

	/** default constructor */
	public BiBaseContractunit() {
	}

	/** full constructor */
	public BiBaseContractunit(String unitName, String paykindCode,
			String paykindName, String priceForm, Double pubRatio,
			Double payRatio, Double ownRatio, Double ecoRatio, Double arrRatio,
			String babyFlag, String mcardFlag, String controlFlag, String flag,
			Double dayLimit, Double monthLimit, Double yearLimit,
			Double onceLimit, Double bedLimit, Double airLimit,
			String unitDescription, BigDecimal unitOrder) {
		this.unitName = unitName;
		this.paykindCode = paykindCode;
		this.paykindName = paykindName;
		this.priceForm = priceForm;
		this.pubRatio = pubRatio;
		this.payRatio = payRatio;
		this.ownRatio = ownRatio;
		this.ecoRatio = ecoRatio;
		this.arrRatio = arrRatio;
		this.babyFlag = babyFlag;
		this.mcardFlag = mcardFlag;
		this.controlFlag = controlFlag;
		this.flag = flag;
		this.dayLimit = dayLimit;
		this.monthLimit = monthLimit;
		this.yearLimit = yearLimit;
		this.onceLimit = onceLimit;
		this.bedLimit = bedLimit;
		this.airLimit = airLimit;
		this.unitDescription = unitDescription;
		this.unitOrder = unitOrder;
	}

	// Property accessors

	public String getUnitCode() {
		return this.unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getPaykindCode() {
		return this.paykindCode;
	}

	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	}

	public String getPaykindName() {
		return this.paykindName;
	}

	public void setPaykindName(String paykindName) {
		this.paykindName = paykindName;
	}

	public String getPriceForm() {
		return this.priceForm;
	}

	public void setPriceForm(String priceForm) {
		this.priceForm = priceForm;
	}

	public Double getPubRatio() {
		return this.pubRatio;
	}

	public void setPubRatio(Double pubRatio) {
		this.pubRatio = pubRatio;
	}

	public Double getPayRatio() {
		return this.payRatio;
	}

	public void setPayRatio(Double payRatio) {
		this.payRatio = payRatio;
	}

	public Double getOwnRatio() {
		return this.ownRatio;
	}

	public void setOwnRatio(Double ownRatio) {
		this.ownRatio = ownRatio;
	}

	public Double getEcoRatio() {
		return this.ecoRatio;
	}

	public void setEcoRatio(Double ecoRatio) {
		this.ecoRatio = ecoRatio;
	}

	public Double getArrRatio() {
		return this.arrRatio;
	}

	public void setArrRatio(Double arrRatio) {
		this.arrRatio = arrRatio;
	}

	public String getBabyFlag() {
		return this.babyFlag;
	}

	public void setBabyFlag(String babyFlag) {
		this.babyFlag = babyFlag;
	}

	public String getMcardFlag() {
		return this.mcardFlag;
	}

	public void setMcardFlag(String mcardFlag) {
		this.mcardFlag = mcardFlag;
	}

	public String getControlFlag() {
		return this.controlFlag;
	}

	public void setControlFlag(String controlFlag) {
		this.controlFlag = controlFlag;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Double getDayLimit() {
		return this.dayLimit;
	}

	public void setDayLimit(Double dayLimit) {
		this.dayLimit = dayLimit;
	}

	public Double getMonthLimit() {
		return this.monthLimit;
	}

	public void setMonthLimit(Double monthLimit) {
		this.monthLimit = monthLimit;
	}

	public Double getYearLimit() {
		return this.yearLimit;
	}

	public void setYearLimit(Double yearLimit) {
		this.yearLimit = yearLimit;
	}

	public Double getOnceLimit() {
		return this.onceLimit;
	}

	public void setOnceLimit(Double onceLimit) {
		this.onceLimit = onceLimit;
	}

	public Double getBedLimit() {
		return this.bedLimit;
	}

	public void setBedLimit(Double bedLimit) {
		this.bedLimit = bedLimit;
	}

	public Double getAirLimit() {
		return this.airLimit;
	}

	public void setAirLimit(Double airLimit) {
		this.airLimit = airLimit;
	}

	public String getUnitDescription() {
		return this.unitDescription;
	}

	public void setUnitDescription(String unitDescription) {
		this.unitDescription = unitDescription;
	}

	public BigDecimal getUnitOrder() {
		return this.unitOrder;
	}

	public void setUnitOrder(BigDecimal unitOrder) {
		this.unitOrder = unitOrder;
	}

}