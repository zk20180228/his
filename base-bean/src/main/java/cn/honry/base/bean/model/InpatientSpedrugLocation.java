package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;


/**
 * DrugDruglimit
 * TInpatientSpedrugLocation entity. @author MyEclipse Persistence Tools
 */

public class InpatientSpedrugLocation  extends Entity{
	
	private static final long serialVersionUID = 1L;

	private String id;
	private String deptCode;
	private String drugCode;
	private String specs;
	private String roomCode;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
	public String getRoomCode() {
		return roomCode;
	}
	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}
	
}