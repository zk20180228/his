package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class BiStatSet extends Entity  implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//统计名称
	private String setStatname;
	//分组id
	private String setGroupid;
	//分组名称
	private String setGroupname;
	//类型
	private Integer setType;
	//表和视图名称
	private String setTvname;
	//sql类型
	private Integer setSqlType;
	//显示字段
	private String setDisplayField;
	//标识字段
	private String setKeyField;
	//横轴字段
	private String setXField;
	//纵轴字段
	private String setYField;
	//横轴字段描述
	private String setXDescription;
	//纵轴字段描述
	private String setYDescription;
	//图形类型 1柱状图；2折线图；3散点图；4K线图；5饼图；6雷达图；7和弦图；8地图；9仪表盘；10沙漏图；11力导布局图；12孤岛
	private Integer setPicType;
	//分页 1是  0否
	private Integer setIsAutoPagesize;
	//排序字段
	private String setOrderField;
	//排序字段描述
	private String setOrderChinese;
	//维度code
	private String dimensionNumber;
	public String getDimensionNumber() {
		return dimensionNumber;
	}
	public void setDimensionNumber(String dimensionNumber) {
		this.dimensionNumber = dimensionNumber;
	}
	public String getSetStatname() {
		return setStatname;
	}
	public void setSetStatname(String setStatname) {
		this.setStatname = setStatname;
	}
	public String getSetGroupid() {
		return setGroupid;
	}
	public void setSetGroupid(String setGroupid) {
		this.setGroupid = setGroupid;
	}
	public String getSetGroupname() {
		return setGroupname;
	}
	public void setSetGroupname(String setGroupname) {
		this.setGroupname = setGroupname;
	}
	public Integer getSetType() {
		return setType;
	}
	public void setSetType(Integer setType) {
		this.setType = setType;
	}
	public String getSetTvname() {
		return setTvname;
	}
	public void setSetTvname(String setTvname) {
		this.setTvname = setTvname;
	}
	public Integer getSetSqlType() {
		return setSqlType;
	}
	public void setSetSqlType(Integer setSqlType) {
		this.setSqlType = setSqlType;
	}
	public String getSetDisplayField() {
		return setDisplayField;
	}
	public void setSetDisplayField(String setDisplayField) {
		this.setDisplayField = setDisplayField;
	}
	public String getSetKeyField() {
		return setKeyField;
	}
	public void setSetKeyField(String setKeyField) {
		this.setKeyField = setKeyField;
	}
	public String getSetXField() {
		return setXField;
	}
	public void setSetXField(String setXField) {
		this.setXField = setXField;
	}
	public String getSetYField() {
		return setYField;
	}
	public void setSetYField(String setYField) {
		this.setYField = setYField;
	}
	public String getSetXDescription() {
		return setXDescription;
	}
	public void setSetXDescription(String setXDescription) {
		this.setXDescription = setXDescription;
	}
	public String getSetYDescription() {
		return setYDescription;
	}
	public void setSetYDescription(String setYDescription) {
		this.setYDescription = setYDescription;
	}
	public Integer getSetPicType() {
		return setPicType;
	}
	public void setSetPicType(Integer setPicType) {
		this.setPicType = setPicType;
	}
	public Integer getSetIsAutoPagesize() {
		return setIsAutoPagesize;
	}
	public void setSetIsAutoPagesize(Integer setIsAutoPagesize) {
		this.setIsAutoPagesize = setIsAutoPagesize;
	}
	public String getSetOrderField() {
		return setOrderField;
	}
	public void setSetOrderField(String setOrderField) {
		this.setOrderField = setOrderField;
	}
	public String getSetOrderChinese() {
		return setOrderChinese;
	}
	public void setSetOrderChinese(String setOrderChinese) {
		this.setOrderChinese = setOrderChinese;
	}
	
}
