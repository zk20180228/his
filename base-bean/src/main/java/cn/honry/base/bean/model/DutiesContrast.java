package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class DutiesContrast extends Entity{
	/**
	 * 职务code
	 */
	private String dutiesCode;
	/**
	 * 职务name
	 */
	private String dutiesName;
	/**
	 * 职务等级
	 */
	private String dutiesLevel;
	/**
	 * 职务拼音
	 */
	private String dutiesPinyin;
	
	/**
	 * 职务五笔
	 */
	private String dutiesWb;
	
	/**
	 * 职务自定义码 
	 */
	private String dutiesInputCode;
	
	/**
	 * 所属级别code
	 */
	private String belongLevel;
	
	/**
	 * 扩展字段1
	 */
	private String extC1;
	/**
	 * 扩展字段2
	 */
	private String extC2;
	/**
	 * 扩展字段3
	 */
	private String extC3;
	/**
	 * 所属级别Name
	 */
	private String belongLevelName;

	public String getDutiesCode() {
		return dutiesCode;
	}

	public void setDutiesCode(String dutiesCode) {
		this.dutiesCode = dutiesCode;
	}

	public String getDutiesName() {
		return dutiesName;
	}

	public void setDutiesName(String dutiesName) {
		this.dutiesName = dutiesName;
	}

	public String getDutiesLevel() {
		return dutiesLevel;
	}

	public void setDutiesLevel(String dutiesLevel) {
		this.dutiesLevel = dutiesLevel;
	}

	public String getDutiesPinyin() {
		return dutiesPinyin;
	}

	public void setDutiesPinyin(String dutiesPinyin) {
		this.dutiesPinyin = dutiesPinyin;
	}

	public String getDutiesWb() {
		return dutiesWb;
	}

	public void setDutiesWb(String dutiesWb) {
		this.dutiesWb = dutiesWb;
	}

	public String getDutiesInputCode() {
		return dutiesInputCode;
	}

	public void setDutiesInputCode(String dutiesInputCode) {
		this.dutiesInputCode = dutiesInputCode;
	}

	public String getBelongLevel() {
		return belongLevel;
	}

	public void setBelongLevel(String belongLevel) {
		this.belongLevel = belongLevel;
	}

	public String getExtC1() {
		return extC1;
	}

	public void setExtC1(String extC1) {
		this.extC1 = extC1;
	}

	public String getExtC2() {
		return extC2;
	}

	public void setExtC2(String extC2) {
		this.extC2 = extC2;
	}

	public String getExtC3() {
		return extC3;
	}

	public void setExtC3(String extC3) {
		this.extC3 = extC3;
	}

	public String getBelongLevelName() {
		return belongLevelName;
	}

	public void setBelongLevelName(String belongLevelName) {
		this.belongLevelName = belongLevelName;
	}
	
}
