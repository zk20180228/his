package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 合同单位维护表实体类
 * @author liudelin
 * Date:2015/5/29 15:56
 */
public class BusinessContractunit extends Entity{

	/**单位编号**/
	private String hospitalId;
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
	/**结算类别  01 自费  02 保险 03 公费在职  04公费退休  05 公费干部**/
	private String paykindCode;
	/**价格形势   0三甲价  1特诊价  2儿童价**/
	private Integer priceForm;
	/**公费比例**/
	private Double pubRatio = 0.0;
	/**自负比例**/
	private Double payRatio = 0.0;
	/**自费比例**/
	private Double ownRatio = 0.0;
	/**优惠比例**/
	private Double ecoRatio = 0.0;
	/**欠费比例,郑大按比例欠费(居民用)**/
	private Double arrRatio = 0.0;
	/**婴儿标志   0无关    1有关**/
	private Integer babyFlag;
	/**是否要求必须有医疗证号  0否    1是**/
	private Integer mcardFlag;
	/**是否监控(1受监控  0不受监控)可作为是否自动上传标志**/
	private Integer controlFlag;
	/**flag   0全部   1药品   2非药品**/
	private Integer flag;
	/**日限额**/
	private Double dayLimit;
	/**月限额**/
	private Double monthLimit;
	/**年限额**/
	private Double yearLimit;
	/**一次限额,郑大按比例欠费分子**/
	private Double onceLimit;
	/**床位上限**/
	private Double bedLimit;
	/**空调上限,郑大按比例欠费分母**/
	private Double airLimit;	
	/**说明**/
	private String description;
	/**排序**/
	private Integer order; 
	public String getEncode() {
		return encode;
	}
	public String getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
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
	public String getPaykindCode() {
		return paykindCode;
	}
	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	}
	public Integer getPriceForm() {
		return priceForm;
	}
	public void setPriceForm(Integer priceForm) {
		this.priceForm = priceForm;
	}
	public Double getPubRatio() {
		return pubRatio;
	}
	public void setPubRatio(Double pubRatio) {
		this.pubRatio = pubRatio;
	}
	public Double getPayRatio() {
		return payRatio;
	}
	public void setPayRatio(Double payRatio) {
		this.payRatio = payRatio;
	}
	public Double getOwnRatio() {
		return ownRatio;
	}
	public void setOwnRatio(Double ownRatio) {
		this.ownRatio = ownRatio;
	}
	public Double getEcoRatio() {
		return ecoRatio;
	}
	public void setEcoRatio(Double ecoRatio) {
		this.ecoRatio = ecoRatio;
	}
	public Double getArrRatio() {
		return arrRatio;
	}
	public void setArrRatio(Double arrRatio) {
		this.arrRatio = arrRatio;
	}
	public Integer getBabyFlag() {
		return babyFlag;
	}
	public void setBabyFlag(Integer babyFlag) {
		this.babyFlag = babyFlag;
	}
	public Integer getMcardFlag() {
		return mcardFlag;
	}
	public void setMcardFlag(Integer mcardFlag) {
		this.mcardFlag = mcardFlag;
	}
	public Integer getControlFlag() {
		return controlFlag;
	}
	public void setControlFlag(Integer controlFlag) {
		this.controlFlag = controlFlag;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Double getDayLimit() {
		return dayLimit;
	}
	public void setDayLimit(Double dayLimit) {
		this.dayLimit = dayLimit;
	}
	public Double getMonthLimit() {
		return monthLimit;
	}
	public void setMonthLimit(Double monthLimit) {
		this.monthLimit = monthLimit;
	}
	public Double getYearLimit() {
		return yearLimit;
	}
	public void setYearLimit(Double yearLimit) {
		this.yearLimit = yearLimit;
	}
	public Double getOnceLimit() {
		return onceLimit;
	}
	public void setOnceLimit(Double onceLimit) {
		this.onceLimit = onceLimit;
	}
	public Double getBedLimit() {
		return bedLimit;
	}
	public void setBedLimit(Double bedLimit) {
		this.bedLimit = bedLimit;
	}
	public Double getAirLimit() {
		return airLimit;
	}
	public void setAirLimit(Double airLimit) {
		this.airLimit = airLimit;
	}
	
	
	
	
}
