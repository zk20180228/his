package cn.honry.base.bean.model;


import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 床位等级编码实体类
 * 
 *
 */
public class CodeBedgrade extends Entity {
	//代码
	private String encode;
	//名称
	private String name;
	//拼音码
	private String pinyin;
	//五笔码
	private String wb;
	//自定义码
	private String inputCode;
	//说明
	private String description;
	//排序
	private Integer order; 
	//可选标志
	private Integer canselect;
	//默认标志
	private Integer isdefault;
	//适用医院
	private String hospital;
	//不适用医院
	private String nonhospital;
	
	/** 分页用的page和rows*/
	private String page;
	private String rows;
	private String chargeBedlevel;//床位等级
	private String undrugName;//项目名称
	private Double chargeAmount;//数量
	private Double chargeUnitprice;//单价
	private Date starttime;//开始时间
	private Date endtIme;//结束时间
	private Integer isabOutChildeen;//是否和婴儿有关
	private Integer isabOutTime;//是否与时间有关
	private Integer chargeState;//状态
	private Integer chargeOrder;//顺序
	
	
	
	public String getChargeBedlevel() {
		return chargeBedlevel;
	}
	public void setChargeBedlevel(String chargeBedlevel) {
		this.chargeBedlevel = chargeBedlevel;
	}
	public Double getChargeAmount() {
		return chargeAmount;
	}
	public void setChargeAmount(Double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	public Double getChargeUnitprice() {
		return chargeUnitprice;
	}
	public void setChargeUnitprice(Double chargeUnitprice) {
		this.chargeUnitprice = chargeUnitprice;
	}
	public Date getStarttime() {
		return starttime;
	}
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	public Date getEndtIme() {
		return endtIme;
	}
	public void setEndtIme(Date endtIme) {
		this.endtIme = endtIme;
	}
	public Integer getIsabOutChildeen() {
		return isabOutChildeen;
	}
	public void setIsabOutChildeen(Integer isabOutChildeen) {
		this.isabOutChildeen = isabOutChildeen;
	}
	public Integer getIsabOutTime() {
		return isabOutTime;
	}
	public void setIsabOutTime(Integer isabOutTime) {
		this.isabOutTime = isabOutTime;
	}
	public Integer getChargeState() {
		return chargeState;
	}
	public void setChargeState(Integer chargeState) {
		this.chargeState = chargeState;
	}
	public Integer getChargeOrder() {
		return chargeOrder;
	}
	public void setChargeOrder(Integer chargeOrder) {
		this.chargeOrder = chargeOrder;
	}
	public String getUndrugName() {
		return undrugName;
	}
	public void setUndrugName(String undrugName) {
		this.undrugName = undrugName;
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
	public String getNonhospital() {
		return nonhospital;
	}
	public void setNonhospital(String nonhospital) {
		this.nonhospital = nonhospital;
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
