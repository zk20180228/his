package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class BusinessDictionary extends Entity implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	/**父级编码ID**/
	private String parentId;
	/**子编码标志(0-子,1-父)**/
	private Integer haveson;
	/**编码类型**/
	private String type;
	/**代码**/
	private String encode;
	/**名称**/
	private String name;
	/**拼音码**/
	private String pinyin;
	/**五笔码**/
	private String wb;
	/**自定义码**/
	private String inputCode;
	/**备注**/
	private String mark;
	/**顺序号**/
	private Integer sortId;
	/**排序号(用于生成路径)**/
	private Integer order;
	/**路径**/
	private String path;
	/**层级**/
	private Integer level;
	/**所有父级**/
	private String uppath;
	/**0-在用,1-停用**/
	private Integer validState;
	/**扩展字段1**/
	private String extC1;
	/**扩展字段2**/
	private String extC2;
	/**扩展字段3**/
	private String extC3;
	/**操作员**/
	private String operCode;
	/**操作时间**/
	private Date operDate;
	
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public Integer getHaveson() {
		return haveson;
	}
	public void setHaveson(Integer haveson) {
		this.haveson = haveson;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
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
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public Integer getSortId() {
		return sortId;
	}
	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getUppath() {
		return uppath;
	}
	public void setUppath(String uppath) {
		this.uppath = uppath;
	}
	public Integer getValidState() {
		return validState;
	}
	public void setValidState(Integer validState) {
		this.validState = validState;
	}
	public String getExtC1() {
		return extC1;
	}
	public void setExtC1(String extC1) {
		this.extC1 = extC1;
	}
	public String getExtC2() {
		return extC2;
	}
	public void setExtC2(String extC2) {
		this.extC2 = extC2;
	}
	public String getExtC3() {
		return extC3;
	}
	public void setExtC3(String extC3) {
		this.extC3 = extC3;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
}
