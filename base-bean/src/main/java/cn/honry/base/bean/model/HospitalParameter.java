package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class HospitalParameter extends Entity implements java.io.Serializable {

	// Fields
	/** 参数名称*/
	private String parameterName;
	/** 参数代码*/
	private String parameterCode;
	/** 参数类型*/
	private String parameterType;
	/** 参数值*/
	private String parameterValue;
	/** 参数单位*/
	private String parameterUnit;
	/** 参数上限*/
	private String parameterDownlimit;
	/** 参数下限*/
	private String parameterUplimit;
	/** 备注*/
	private String parameterRemark;
	/** 预留字段1*/
	private String parameterKey1;
	/** 预留字段2*/
	private String parameterKey2;
	/** 预留字段3*/
	private String parameterKey3;

	public String getParameterName() {
		return this.parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterCode() {
		return this.parameterCode;
	}

	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
	}

	public String getParameterType() {
		return this.parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public String getParameterValue() {
		return this.parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getParameterUnit() {
		return this.parameterUnit;
	}

	public void setParameterUnit(String parameterUnit) {
		this.parameterUnit = parameterUnit;
	}

	public String getParameterDownlimit() {
		return this.parameterDownlimit;
	}

	public void setParameterDownlimit(String parameterDownlimit) {
		this.parameterDownlimit = parameterDownlimit;
	}

	public String getParameterUplimit() {
		return this.parameterUplimit;
	}

	public void setParameterUplimit(String parameterUplimit) {
		this.parameterUplimit = parameterUplimit;
	}

	public String getParameterRemark() {
		return this.parameterRemark;
	}

	public void setParameterRemark(String parameterRemark) {
		this.parameterRemark = parameterRemark;
	}

	public String getParameterKey1() {
		return this.parameterKey1;
	}

	public void setParameterKey1(String parameterKey1) {
		this.parameterKey1 = parameterKey1;
	}

	public String getParameterKey2() {
		return this.parameterKey2;
	}

	public void setParameterKey2(String parameterKey2) {
		this.parameterKey2 = parameterKey2;
	}

	public String getParameterKey3() {
		return this.parameterKey3;
	}

	public void setParameterKey3(String parameterKey3) {
		this.parameterKey3 = parameterKey3;
	}

}