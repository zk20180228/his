package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * AbstractTEmrAttroption entity provides the base persistence definition of the
 * TEmrAttroption entity. @author MyEclipse Persistence Tools
 */

public class EmrAttroption extends Entity{

	// Fields

	private String optionId;
	private String attrId;
	private String optionCode;
	private String optionName;
	private String optionValue;
	private Integer optionDefaultflg;
	private String optionBak;

	// Constructors

	/** default constructor */
	public EmrAttroption() {
	}

	/** minimal constructor */
	public EmrAttroption(String optionId) {
		this.optionId = optionId;
	}


	// Property accessors

	public String getOptionId() {
		return this.optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}


	public String getAttrId() {
		return attrId;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}


	public String getOptionCode() {
		return this.optionCode;
	}

	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}

	public String getOptionName() {
		return this.optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public String getOptionValue() {
		return this.optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public Integer getOptionDefaultflg() {
		return this.optionDefaultflg;
	}

	public void setOptionDefaultflg(Integer optionDefaultflg) {
		this.optionDefaultflg = optionDefaultflg;
	}

	public String getOptionBak() {
		return this.optionBak;
	}

	public void setOptionBak(String optionBak) {
		this.optionBak = optionBak;
	}



}