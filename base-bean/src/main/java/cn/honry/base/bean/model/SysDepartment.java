package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * TDepartment部门科室 entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class SysDepartment extends Entity {

	/** 医院编号 **/
	private Hospital hospitalId;
	/** 系统编号 **/
	private String deptCode;
	/** 名称 **/
	private String deptName;
	/** 部门简称 **/
	private String deptBrev;
	/** 部门英文 **/
	private String deptEname;
	/** 部门地点 **/
	private String deptAddress;
	/** 拼音码 **/
	private String deptPinyin;
	/** 五笔码 **/
	private String deptWb;
	/** 自定义码 **/
	private String deptInputcode;
	/** 上级 **/
	private String deptParent;
	/** 是否有下级 **/
	private Integer deptHasson;
	/** 层级 **/
	private Integer deptLevel;
	/** 排序 **/
	private Integer deptOrder;
	/** 上级路径 **/
	private String deptUppath;
	/** 层级路径 **/
	private String deptPath;
	/** 部门分类 **/
	private String deptType;
	/** 部门性质 **/
	private String deptProperty;
	/** 是否挂号部门 **/
	private Integer deptIsforregister;
	/** 挂号顺序号，如果是挂号部门，可以填这个字段，非挂号不用填 **/
	private String deptRegisterno;
	/** 是否核算部门 **/
	private Integer deptIsforaccounting;
	/** 备注 **/
	private String deptRemark;
	/** 默认选中  **/
	private boolean selected;
	private String str;
	/** 
	* @Fields fictitiousContentId : 虚拟科室关系id
	*/ 
	private String fictitiousContentId;
	/** 
	* @Fields fictCode : 虚拟科室关系父级科室code 
	*/ 
	private String fictCode;
	
	/** 用来作为挂号科室排序用 **/
	private Integer deptRegisterOrder;
	
	/** 院区编码 **/
	private String areaCode;
	/** 院区名称 **/
	private String areaName;
	
	// Property accessors
	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public Hospital getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Hospital hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptBrev() {
		return this.deptBrev;
	}

	public void setDeptBrev(String deptBrev) {
		this.deptBrev = deptBrev;
	}

	public String getDeptEname() {
		return this.deptEname;
	}

	public void setDeptEname(String deptEname) {
		this.deptEname = deptEname;
	}

	public String getDeptAddress() {
		return this.deptAddress;
	}

	public void setDeptAddress(String deptAddress) {
		this.deptAddress = deptAddress;
	}

	public String getDeptPinyin() {
		return this.deptPinyin;
	}

	public void setDeptPinyin(String deptPinyin) {
		this.deptPinyin = deptPinyin;
	}

	public String getDeptWb() {
		return this.deptWb;
	}

	public void setDeptWb(String deptWb) {
		this.deptWb = deptWb;
	}

	public String getDeptInputcode() {
		return this.deptInputcode;
	}

	public void setDeptInputcode(String deptInputcode) {
		this.deptInputcode = deptInputcode;
	}

	public String getDeptParent() {
		return deptParent;
	}

	public void setDeptParent(String deptParent) {
		this.deptParent = deptParent;
	}

	public Integer getDeptHasson() {
		return deptHasson;
	}

	public void setDeptHasson(Integer deptHasson) {
		this.deptHasson = deptHasson;
	}

	public Integer getDeptLevel() {
		return deptLevel;
	}

	public void setDeptLevel(Integer deptLevel) {
		this.deptLevel = deptLevel;
	}

	public Integer getDeptOrder() {
		return deptOrder;
	}

	public void setDeptOrder(Integer deptOrder) {
		this.deptOrder = deptOrder;
	}

	public String getDeptUppath() {
		return this.deptUppath;
	}

	public void setDeptUppath(String deptUppath) {
		this.deptUppath = deptUppath;
	}

	public String getDeptPath() {
		return this.deptPath;
	}

	public void setDeptPath(String deptPath) {
		this.deptPath = deptPath;
	}

	public String getDeptType() {
		return deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}

	public String getDeptProperty() {
		return deptProperty;
	}

	public void setDeptProperty(String deptProperty) {
		this.deptProperty = deptProperty;
	}

	public Integer getDeptIsforregister() {
		return deptIsforregister;
	}

	public void setDeptIsforregister(Integer deptIsforregister) {
		this.deptIsforregister = deptIsforregister;
	}

	public Integer getDeptIsforaccounting() {
		return deptIsforaccounting;
	}

	public void setDeptIsforaccounting(Integer deptIsforaccounting) {
		this.deptIsforaccounting = deptIsforaccounting;
	}

	public String getDeptRemark() {
		return this.deptRemark;
	}

	public void setDeptRemark(String deptRemark) {
		this.deptRemark = deptRemark;
	}

	public String getDeptRegisterno() {
		return deptRegisterno;
	}

	public void setDeptRegisterno(String deptRegisterno) {
		this.deptRegisterno = deptRegisterno;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Integer getDeptRegisterOrder() {
		return deptRegisterOrder;
	}

	public void setDeptRegisterOrder(Integer deptRegisterOrder) {
		this.deptRegisterOrder = deptRegisterOrder;
	}

	public String getFictitiousContentId() {
		return fictitiousContentId;
	}

	public void setFictitiousContentId(String fictitiousContentId) {
		this.fictitiousContentId = fictitiousContentId;
	}

	public String getFictCode() {
		return fictCode;
	}

	public void setFictCode(String fictCode) {
		this.fictCode = fictCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}


}