package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class BiIndexSet extends Entity implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//指标名称
	private String indexName;
	//指标字段
	private String indexField;
	//指标字段中文名
	private String indexFieldChinese;
	//聚合函数
	private String polymerization;
	//排序
	private Integer sort;
	//维度编号
	private String dimensionNumber;
	//分组编号
	private String setGroupid;
	
	public String getSetGroupid() {
		return setGroupid;
	}
	public void setSetGroupid(String setGroupid) {
		this.setGroupid = setGroupid;
	}
	public String getDimensionNumber() {
		return dimensionNumber;
	}
	public void setDimensionNumber(String dimensionNumber) {
		this.dimensionNumber = dimensionNumber;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getIndexField() {
		return indexField;
	}
	public void setIndexField(String indexField) {
		this.indexField = indexField;
	}
	public String getIndexFieldChinese() {
		return indexFieldChinese;
	}
	public void setIndexFieldChinese(String indexFieldChinese) {
		this.indexFieldChinese = indexFieldChinese;
	}
	public String getPolymerization() {
		return polymerization;
	}
	public void setPolymerization(String polymerization) {
		this.polymerization = polymerization;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
