/**
 * 
 */
package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * @author abc
 *电子病历医技症状表
 */
public class EmrSymptom extends Entity{
	/**
	 * 编码
	 */
	private String symptomCode;
	/**
	 * 类型
	 */
	private String symptomType;
	/**
	 * 名称
	 */
	private String symptomName;
	/**
	 * 拼音码
	 */
	private String symptomPinYin;
	/**
	 * 五笔码
	 */
	private String symptomWb;
	/**
	 * 自定义码
	 */
	private String symptomInputCode;
	
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
	public String getSymptomCode() {
		return symptomCode;
	}
	public void setSymptomCode(String symptomCode) {
		this.symptomCode = symptomCode;
	}
	public String getSymptomType() {
		return symptomType;
	}
	public void setSymptomType(String symptomType) {
		this.symptomType = symptomType;
	}
	public String getSymptomName() {
		return symptomName;
	}
	public void setSymptomName(String symptomName) {
		this.symptomName = symptomName;
	}
	public String getSymptomPinYin() {
		return symptomPinYin;
	}
	public void setSymptomPinYin(String symptomPinYin) {
		this.symptomPinYin = symptomPinYin;
	}
	public String getSymptomWb() {
		return symptomWb;
	}
	public void setSymptomWb(String symptomWb) {
		this.symptomWb = symptomWb;
	}
	public String getSymptomInputCode() {
		return symptomInputCode;
	}
	public void setSymptomInputCode(String symptomInputCode) {
		this.symptomInputCode = symptomInputCode;
	}
	
}
