package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 特限药品对照表
 * @author Administrator
 *
 */
public class BusinessSpedrug extends Entity{
	/**特殊类别 ：0科室 1医生  **/
	private	Integer speType;
	/**医生/科室编码**/
	private	String	speCode;
	/**医生/科室名称 **/
	private	String	speName;
	/**药品主键编号**/
	private	String	drugCode;
	/**商品名称 **/
	private	String	tradeName;
	/** 规格**/
	private	String	specs;
	/** 扩展字段 **/
	private	String	extend;
	/** 备注 **/
	private	String	memo;
	/** 操作员 **/
	private	String	operCode;
	/**  操作时间**/
	private	Date	operTime;
	public Integer getSpeType() {
		return speType;
	}
	public void setSpeType(Integer speType) {
		this.speType = speType;
	}
	public String getSpeCode() {
		return speCode;
	}
	public void setSpeCode(String speCode) {
		this.speCode = speCode;
	}
	public String getSpeName() {
		return speName;
	}
	public void setSpeName(String speName) {
		this.speName = speName;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Date getOperTime() {
		return operTime;
	}
	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}
}
