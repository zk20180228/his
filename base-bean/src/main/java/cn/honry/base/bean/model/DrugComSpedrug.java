package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;


/**
 * DrugComSpedrug
 * TOutpatientFeedetail entity. @author MyEclipse Persistence Tools
 */

public class DrugComSpedrug extends Entity{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String speType;
	private String speCode;
	private String drugCode;
	private String specs;
	private String extend;
	private String memo;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSpeType() {
		return speType;
	}
	public void setSpeType(String speType) {
		this.speType = speType;
	}
	public String getSpeCode() {
		return speCode;
	}
	public void setSpeCode(String speCode) {
		this.speCode = speCode;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
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
	
}