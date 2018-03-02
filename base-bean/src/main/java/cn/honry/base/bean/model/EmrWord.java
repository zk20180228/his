/**
 * 
 */
package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * @author abc
 *电子病历常用词表
 */
public class EmrWord extends Entity{
	/**
	 * 单词编码
	 */
	private String wordCode;
	/**
	 * 单词类型
	 */
	private String wordType;
	/**
	 * 单词名称
	 */
	private String wordName;
	/**
	 * 拼音码
	 */
	private String wordPinYin;
	/**
	 * 五笔码
	 */
	private String wordWb;
	/**
	 * 自定义码
	 */
	private String wordInputCode;
	
	/**  分页  **/
	private String page;
	private String rows;
	
	public String getWordCode() {
		return wordCode;
	}
	public void setWordCode(String wordCode) {
		this.wordCode = wordCode;
	}
	public String getWordType() {
		return wordType;
	}
	public void setWordType(String wordType) {
		this.wordType = wordType;
	}
	public String getWordName() {
		return wordName;
	}
	public void setWordName(String wordName) {
		this.wordName = wordName;
	}
	public String getWordPinYin() {
		return wordPinYin;
	}
	public void setWordPinYin(String wordPinYin) {
		this.wordPinYin = wordPinYin;
	}
	public String getWordWb() {
		return wordWb;
	}
	public void setWordWb(String wordWb) {
		this.wordWb = wordWb;
	}
	public String getWordInputCode() {
		return wordInputCode;
	}
	public void setWordInputCode(String wordInputCode) {
		this.wordInputCode = wordInputCode;
	}
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
	
}
