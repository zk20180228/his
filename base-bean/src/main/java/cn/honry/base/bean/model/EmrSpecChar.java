/**
 * 
 */
package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * @author abc
 *电子病历特殊字符表
 */
public class EmrSpecChar extends Entity{
	/**
	 * 编码
	 */
	private String specCharCode;
	/**
	 * 名称
	 */
	private String specCharName;
	/**
	 * 拼音码
	 */
	private String specCharPinYin;
	/**
	 * 五笔码
	 */
	private String specCharWb;
	/**
	 * 自定义码
	 */
	private String specCharInputCode;
	
	/**  分页  **/
	private String page;
	private String rows;
	
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	
	public String getSpecCharCode() {
		return specCharCode;
	}
	public void setSpecCharCode(String specCharCode) {
		this.specCharCode = specCharCode;
	}
	public String getSpecCharName() {
		return specCharName;
	}
	public void setSpecCharName(String specCharName) {
		this.specCharName = specCharName;
	}
	public String getSpecCharPinYin() {
		return specCharPinYin;
	}
	public void setSpecCharPinYin(String specCharPinYin) {
		this.specCharPinYin = specCharPinYin;
	}
	public String getSpecCharWb() {
		return specCharWb;
	}
	public void setSpecCharWb(String specCharWb) {
		this.specCharWb = specCharWb;
	}
	public String getSpecCharInputCode() {
		return specCharInputCode;
	}
	public void setSpecCharInputCode(String specCharInputCode) {
		this.specCharInputCode = specCharInputCode;
	}
	
}
