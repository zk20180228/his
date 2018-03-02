package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class MatBaseRegInfo extends Entity implements java.io.Serializable {

	/**物品编码**/
	private String itemCode;
	/**生产厂家**/
	private String factoryCode;
	/**规格**/
	private String specs;
	/**大包装单位**/
	private String packUnit;
	/**大包装数量**/
	private Long packQty;
	/**大包装价格**/
	private Double packPrice;
	/**注册号**/
	private String registerCode;
	/**特殊类别**/
	private String specialType;
	/**注册时间**/
	private Date registerDate;
	/**到期时间**/
	private Date overDate;
	/**是否为当前字典默认(1-是,0-否)**/
	private Integer defaultFlag;
	/**有效标记(0－停用,1－有效)**/
	private Integer validFlag;
	/**生产者**/
	private String mader;
	/**备注**/
	private String memo;
	/**操作员**/
	private String operCode;
	/**操作日期**/
	private Date operDate;
	//与数据库无关的字段
	/**是否为当前字典默认(1-是,0-否)**/
	private String defaultFlagShow;
	/**有效标记(0－停用,1－有效)**/
	private String validFlagShow;
	/**生产厂家**/
	private String factoryCodeShow;
	
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getFactoryCode() {
		return this.factoryCode;
	}

	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}

	public String getSpecs() {
		return this.specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public String getPackUnit() {
		return this.packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public Long getPackQty() {
		return this.packQty;
	}

	public void setPackQty(Long packQty) {
		this.packQty = packQty;
	}

	public Double getPackPrice() {
		return this.packPrice;
	}

	public void setPackPrice(Double packPrice) {
		this.packPrice = packPrice;
	}

	public String getRegisterCode() {
		return this.registerCode;
	}

	public void setRegisterCode(String registerCode) {
		this.registerCode = registerCode;
	}

	public String getSpecialType() {
		return this.specialType;
	}

	public void setSpecialType(String specialType) {
		this.specialType = specialType;
	}

	public Date getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Date getOverDate() {
		return this.overDate;
	}

	public void setOverDate(Date overDate) {
		this.overDate = overDate;
	}

	public String getMader() {
		return this.mader;
	}

	public void setMader(String mader) {
		this.mader = mader;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOperCode() {
		return this.operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}

	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public void setDefaultFlag(Integer defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	public Integer getDefaultFlag() {
		return defaultFlag;
	}

	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}

	public Integer getValidFlag() {
		return validFlag;
	}

	public void setDefaultFlagShow(String defaultFlagShow) {
		this.defaultFlagShow = defaultFlagShow;
	}

	public String getDefaultFlagShow() {
		return defaultFlagShow;
	}

	public void setValidFlagShow(String validFlagShow) {
		this.validFlagShow = validFlagShow;
	}

	public String getValidFlagShow() {
		return validFlagShow;
	}

	public void setFactoryCodeShow(String factoryCodeShow) {
		this.factoryCodeShow = factoryCodeShow;
	}

	public String getFactoryCodeShow() {
		return factoryCodeShow;
	}

}