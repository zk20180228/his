package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class BiDimensionSet extends Entity implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//维度名称
	private String dimensionName;
	//维度类型
	private Integer dimensionType;
	//维度编号
	private String dimensionNumber;
	//字典表名
	private String tableName;
	//字段名称
	private String fieldName;
	//字段的中文名
	private String fieldChinese;
	//排序
	private String sort;
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getDimensionName() {
		return dimensionName;
	}
	public void setDimensionName(String dimensionName) {
		this.dimensionName = dimensionName;
	}
	public Integer getDimensionType() {
		return dimensionType;
	}
	public void setDimensionType(Integer dimensionType) {
		this.dimensionType = dimensionType;
	}
	public String getDimensionNumber() {
		return dimensionNumber;
	}
	public void setDimensionNumber(String dimensionNumber) {
		this.dimensionNumber = dimensionNumber;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldChinese() {
		return fieldChinese;
	}
	public void setFieldChinese(String fieldChinese) {
		this.fieldChinese = fieldChinese;
	}
	
}
