package cn.honry.base.bean.model;

import java.math.BigDecimal;

/**
 * BiBaseFrequency entity. @author MyEclipse Persistence Tools
 */

public class BiBaseFrequency implements java.io.Serializable {

	// Fields

	private String frequencyEncode;
	private String frequencyName;
	private Short frequencyTime;
	private Short frequencyNum;
	private String frequencyUnit;
	private String frequencyAlwaysFlag;
	private String frequencyUsemode;
	private String frequencyPeriod;
	private String frequencyDescription;
	private BigDecimal frequencyOrder;
	private String frequencyCanselect;
	private String frequencyIsdefault;

	// Constructors

	/** default constructor */
	public BiBaseFrequency() {
	}

	/** full constructor */
	public BiBaseFrequency(String frequencyName, Short frequencyTime,
			Short frequencyNum, String frequencyUnit,
			String frequencyAlwaysFlag, String frequencyUsemode,
			String frequencyPeriod, String frequencyDescription,
			BigDecimal frequencyOrder, String frequencyCanselect,
			String frequencyIsdefault) {
		this.frequencyName = frequencyName;
		this.frequencyTime = frequencyTime;
		this.frequencyNum = frequencyNum;
		this.frequencyUnit = frequencyUnit;
		this.frequencyAlwaysFlag = frequencyAlwaysFlag;
		this.frequencyUsemode = frequencyUsemode;
		this.frequencyPeriod = frequencyPeriod;
		this.frequencyDescription = frequencyDescription;
		this.frequencyOrder = frequencyOrder;
		this.frequencyCanselect = frequencyCanselect;
		this.frequencyIsdefault = frequencyIsdefault;
	}

	// Property accessors

	public String getFrequencyEncode() {
		return this.frequencyEncode;
	}

	public void setFrequencyEncode(String frequencyEncode) {
		this.frequencyEncode = frequencyEncode;
	}

	public String getFrequencyName() {
		return this.frequencyName;
	}

	public void setFrequencyName(String frequencyName) {
		this.frequencyName = frequencyName;
	}

	public Short getFrequencyTime() {
		return this.frequencyTime;
	}

	public void setFrequencyTime(Short frequencyTime) {
		this.frequencyTime = frequencyTime;
	}

	public Short getFrequencyNum() {
		return this.frequencyNum;
	}

	public void setFrequencyNum(Short frequencyNum) {
		this.frequencyNum = frequencyNum;
	}

	public String getFrequencyUnit() {
		return this.frequencyUnit;
	}

	public void setFrequencyUnit(String frequencyUnit) {
		this.frequencyUnit = frequencyUnit;
	}

	public String getFrequencyAlwaysFlag() {
		return this.frequencyAlwaysFlag;
	}

	public void setFrequencyAlwaysFlag(String frequencyAlwaysFlag) {
		this.frequencyAlwaysFlag = frequencyAlwaysFlag;
	}

	public String getFrequencyUsemode() {
		return this.frequencyUsemode;
	}

	public void setFrequencyUsemode(String frequencyUsemode) {
		this.frequencyUsemode = frequencyUsemode;
	}

	public String getFrequencyPeriod() {
		return this.frequencyPeriod;
	}

	public void setFrequencyPeriod(String frequencyPeriod) {
		this.frequencyPeriod = frequencyPeriod;
	}

	public String getFrequencyDescription() {
		return this.frequencyDescription;
	}

	public void setFrequencyDescription(String frequencyDescription) {
		this.frequencyDescription = frequencyDescription;
	}

	public BigDecimal getFrequencyOrder() {
		return this.frequencyOrder;
	}

	public void setFrequencyOrder(BigDecimal frequencyOrder) {
		this.frequencyOrder = frequencyOrder;
	}

	public String getFrequencyCanselect() {
		return this.frequencyCanselect;
	}

	public void setFrequencyCanselect(String frequencyCanselect) {
		this.frequencyCanselect = frequencyCanselect;
	}

	public String getFrequencyIsdefault() {
		return this.frequencyIsdefault;
	}

	public void setFrequencyIsdefault(String frequencyIsdefault) {
		this.frequencyIsdefault = frequencyIsdefault;
	}

}