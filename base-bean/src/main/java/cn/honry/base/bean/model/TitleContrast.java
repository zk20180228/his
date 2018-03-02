package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class TitleContrast extends Entity{
	/**
	 * 职称code
	 */
	private String titleCode;
	/**
	 * 职称name
	 */
	private String titleName;
	/**
	 * 职称等级
	 */
	private String titleLevel;
	/**
	 * 职称拼音
	 */
	private String titlePinyin;
	
	/**
	 * 职称五笔
	 */
	private String titleWb;
	
	/**
	 * 职称自定义码 
	 */
	private String titleInputCode;
	
	/**
	 * 所属级别code
	 */
	private String belongTypeCode;
	
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
	private String belongTypeName;
	
	public String getTitleCode() {
		return titleCode;
	}
	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}
	public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	public String getTitleLevel() {
		return titleLevel;
	}
	public void setTitleLevel(String titleLevel) {
		this.titleLevel = titleLevel;
	}
	public String getTitlePinyin() {
		return titlePinyin;
	}
	public void setTitlePinyin(String titlePinyin) {
		this.titlePinyin = titlePinyin;
	}
	public String getTitleWb() {
		return titleWb;
	}
	public void setTitleWb(String titleWb) {
		this.titleWb = titleWb;
	}
	public String getTitleInputCode() {
		return titleInputCode;
	}
	public void setTitleInputCode(String titleInputCode) {
		this.titleInputCode = titleInputCode;
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
	public String getBelongTypeCode() {
		return belongTypeCode;
	}
	public void setBelongTypeCode(String belongTypeCode) {
		this.belongTypeCode = belongTypeCode;
	}
	public String getBelongTypeName() {
		return belongTypeName;
	}
	public void setBelongTypeName(String belongTypeName) {
		this.belongTypeName = belongTypeName;
	}
	
}
