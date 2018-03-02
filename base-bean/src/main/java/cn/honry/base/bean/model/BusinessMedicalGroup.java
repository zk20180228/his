package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;


public class BusinessMedicalGroup extends Entity {
	/*医院编号*/
	private Hospital hospital;
	/*科室*/
	private String deptId;
	/*名称*/
	private String name;
	/*拼音码*/
	private String pinyin;
	/*五笔码*/
	private String wb;
	/*自定义码*/
	private String inputCode;
	/*备注*/
	private String remark;
	/** 页数   **/
	private String page;
	/**  每页行数  **/
	private String rows;
	
	/**  新加字段 **/
	/*所属院区*/
	private String areaCode;
	
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getWb() {
		return wb;
	}

	public void setWb(String wb) {
		this.wb = wb;
	}

	public String getInputCode() {
		return inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPage() {
		return page;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getRows() {
		return rows;
	}

}