package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * @Description  长期医嘱限制性药品性质编码实体类
 * @author    tangfeishuai
 * @CreateDate 2016-04-12
 * @version   1.0
 */
public class BusinessAdvdrugnature extends Entity {
	/**
	 * 代码
	 */
	private String encode;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 拼音码
	 */
	private String pinyin;
	
	/**
	 * 五笔码
	 */
	private String wb;
	
	/**
	 * 自定义码
	 */
	private String inputCode;
	
	/**
	 * 说明
	 */
	private String description;
	
	/**
	 * 排序
	 */
	private Integer order; 
	
	/**
	 * 可选标志
	 */
	private Integer canselect;
	
	/**
	 * 默认标志
	 */
	private Integer isdefault;
	
	/**
	 * 适用医院
	 */
	private String hospital;
	
	/** 
	 * 分页用的page
	 */
	private String page;
	
    /**
     * 分页用的rows
     */
	private String rows;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public Integer getCanselect() {
		return canselect;
	}
	public void setCanselect(Integer canselect) {
		this.canselect = canselect;
	}
	public Integer getIsdefault() {
		return isdefault;
	}
	public void setIsdefault(Integer isdefault) {
		this.isdefault = isdefault;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
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
