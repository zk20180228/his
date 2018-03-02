package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;


/**
 * DrugDruglimit
 * TDrugDruglimit entity. @author MyEclipse Persistence Tools
 */

public class DrugDruglimit  extends Entity{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String doctCode;
	private String drugCode;
	private String isLeadercheck;
	private String isNeedrecipe;
	private String validFlag;
	private String remark;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDoctCode() {
		return doctCode;
	}
	public void setDoctCode(String doctCode) {
		this.doctCode = doctCode;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public String getIsLeadercheck() {
		return isLeadercheck;
	}
	public void setIsLeadercheck(String isLeadercheck) {
		this.isLeadercheck = isLeadercheck;
	}
	public String getIsNeedrecipe() {
		return isNeedrecipe;
	}
	public void setIsNeedrecipe(String isNeedrecipe) {
		this.isNeedrecipe = isNeedrecipe;
	}
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}