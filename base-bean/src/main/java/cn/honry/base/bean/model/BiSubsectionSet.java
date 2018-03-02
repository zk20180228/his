package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class BiSubsectionSet extends Entity implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//分段名称
	private String subsectionName;
	//分段上限
	private String subsectionUpperLimit;
	//分段下限
	private String subsectionLowerLimit;
	//排序
	private Integer sort;
	//维度编号
	private String dimensionNumber;
	//分段字段
	private String subsectionField;
	//分段字段中文
	private String subsectionFieldChinese;
	//分组编号
	private String setGroupid;
	
	public String getSetGroupid() {
		return setGroupid;
	}
	public void setSetGroupid(String setGroupid) {
		this.setGroupid = setGroupid;
	}
	public String getSubsectionName() {
		return subsectionName;
	}
	public void setSubsectionName(String subsectionName) {
		this.subsectionName = subsectionName;
	}
	public String getSubsectionUpperLimit() {
		return subsectionUpperLimit;
	}
	public void setSubsectionUpperLimit(String subsectionUpperLimit) {
		this.subsectionUpperLimit = subsectionUpperLimit;
	}
	public String getSubsectionLowerLimit() {
		return subsectionLowerLimit;
	}
	public void setSubsectionLowerLimit(String subsectionLowerLimit) {
		this.subsectionLowerLimit = subsectionLowerLimit;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getDimensionNumber() {
		return dimensionNumber;
	}
	public void setDimensionNumber(String dimensionNumber) {
		this.dimensionNumber = dimensionNumber;
	}
	public String getSubsectionField() {
		return subsectionField;
	}
	public void setSubsectionField(String subsectionField) {
		this.subsectionField = subsectionField;
	}
	public String getSubsectionFieldChinese() {
		return subsectionFieldChinese;
	}
	public void setSubsectionFieldChinese(String subsectionFieldChinese) {
		this.subsectionFieldChinese = subsectionFieldChinese;
	}
}
