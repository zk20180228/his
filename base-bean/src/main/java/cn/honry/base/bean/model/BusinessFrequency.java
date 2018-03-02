package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 频次实体类
 * @author liujinliang
 * Date:2015/5/20 13:00
 */
public class BusinessFrequency extends Entity {
	private static final long serialVersionUID = 1L;
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
	/**用法**/
	private String useMode;
	/**时间点**/
	private String period;
	/**说明**/
	private String description;
	/**排序**/
	private Integer order;
	/**可选标志**/
	private Integer canSelect;
	/**默认标志**/
	private Integer isDefault;
	/**适用医院**/
	private String hospital;
	/**不适用医院**/
	private String nonHospital;
	/**  分页  **/
	private String page;
	private String rows;
	
	/** 新加字段  lyy 20160507 **/ 
	//频次次数
	private Integer frequencyTime;
	//频次数目
	private Integer frequencyNum;
	//频次单位
	private String frequencyUnit;
	//频次持续标志 0-不持续  1-持续
	private Integer alwaysFlag;
	
	public Integer getFrequencyTime() {
		return frequencyTime;
	}
	public void setFrequencyTime(Integer frequencyTime) {
		this.frequencyTime = frequencyTime;
	}
	public Integer getFrequencyNum() {
		return frequencyNum;
	}
	public void setFrequencyNum(Integer frequencyNum) {
		this.frequencyNum = frequencyNum;
	}
	public String getFrequencyUnit() {
		return frequencyUnit;
	}
	public void setFrequencyUnit(String frequencyUnit) {
		this.frequencyUnit = frequencyUnit;
	}
	public Integer getAlwaysFlag() {
		return alwaysFlag;
	}
	public void setAlwaysFlag(Integer alwaysFlag) {
		this.alwaysFlag = alwaysFlag;
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
	public String getUseMode() {
		return useMode;
	}
	public void setUseMode(String useMode) {
		this.useMode = useMode;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
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
	public Integer getCanSelect() {
		return canSelect;
	}
	public void setCanSelect(Integer canSelect) {
		this.canSelect = canSelect;
	}
	public Integer getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getNonHospital() {
		return nonHospital;
	}
	public void setNonHospital(String nonHospital) {
		this.nonHospital = nonHospital;
	}
}
