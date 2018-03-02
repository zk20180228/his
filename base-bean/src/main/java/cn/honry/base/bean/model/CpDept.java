package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class CpDept extends Entity {
	/**
	 * 科室代码
	 */
	private String deptCode;
	/**
	 * 科室名称
	 */
	private String deptName;
	/**输入码
	 * 
	 */
	private String inputCode;
	/**
	 * 五笔码
	 */
	private String inputCodeWB;
	/**
	 * 自定义码
	 */
	private String customCode;
	/**
	 * 临床路径ID
	 */
	private String cpID;
	/**
	 * 临床路径版本号
	 */
	private String versionNo;
	private String hospitalid;
	private String areaCode;
	/**
	 * 渲染字段
	 */
	private String hospitalidName;
	private String areaCodeName;
	private String cpWayName;
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getInputCode() {
		return inputCode;
	}
	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	public String getInputCodeWB() {
		return inputCodeWB;
	}
	public void setInputCodeWB(String inputCodeWB) {
		this.inputCodeWB = inputCodeWB;
	}
	public String getCustomCode() {
		return customCode;
	}
	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}
	public String getCpID() {
		return cpID;
	}
	public void setCpID(String cpID) {
		this.cpID = cpID;
	}
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	public String getHospitalid() {
		return hospitalid;
	}
	public void setHospitalid(String hospitalid) {
		this.hospitalid = hospitalid;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaCodeName() {
		return areaCodeName;
	}
	public void setAreaCodeName(String areaCodeName) {
		this.areaCodeName = areaCodeName;
	}
	public String getHospitalidName() {
		return hospitalidName;
	}
	public void setHospitalidName(String hospitalidName) {
		this.hospitalidName = hospitalidName;
	}
	public String getCpWayName() {
		return cpWayName;
	}
	public void setCpWayName(String cpWayName) {
		this.cpWayName = cpWayName;
	}
	
}
