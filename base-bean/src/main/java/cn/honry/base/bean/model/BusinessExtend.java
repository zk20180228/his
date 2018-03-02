package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 业务属性扩展表
 * @author zpty
 * Date:2016/01/16 15:30
 */

public class BusinessExtend extends Entity {	
	/**扩展分类**/
	private String extendClass;
	/**项目编码**/
	private String itemCode;
	/**属性代码**/
	private String propertyCode;
	/**属性名称**/
	private String propertyName;
	/**字符属性**/
	private String stringProperty;
	/**数值属性**/
	private Integer numberProperty;
	/**日期属性**/
	private Date dateProperty;
	/**备注**/
	private String mark;
	/**操作员**/
	private String operCode;
	/**操作时间**/
	private Date operDate;
	
	public String getExtendClass() {
		return extendClass;
	}
	public void setExtendClass(String extendClass) {
		this.extendClass = extendClass;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getPropertyCode() {
		return propertyCode;
	}
	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getStringProperty() {
		return stringProperty;
	}
	public void setStringProperty(String stringProperty) {
		this.stringProperty = stringProperty;
	}
	public Integer getNumberProperty() {
		return numberProperty;
	}
	public void setNumberProperty(Integer numberProperty) {
		this.numberProperty = numberProperty;
	}
	public Date getDateProperty() {
		return dateProperty;
	}
	public void setDateProperty(Date dateProperty) {
		this.dateProperty = dateProperty;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
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