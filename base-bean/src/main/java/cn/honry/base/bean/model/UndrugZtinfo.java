package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;


/**
 * 2016年11月4日16:38:54
 * 复合关系维护 实体
 * @author GH
 */
@SuppressWarnings("serial")
public class UndrugZtinfo extends Entity implements java.io.Serializable {
	
	//组合编码   非药品组套数据的编码
	private String packageCode;
	//组合名称  非药品组套数据的名称
	private String packageName;
	//项目编号  非药品明细数据的编码
	private String itemCode;
	//项目名称  非药品明细数据的名称
	private String itemName;
	//排序号 
	private Integer sortId=0;
	//操作人
	private String operCode;
	//操作时间
	private Date operDate;
	//拼音码  非药品明细数据
	private String spellCode;
	//五笔码  非药品明细数据
	private String wbCode;
	//自定义码  非药品明细数据
	private String inputCode;
	//是否有效  默认‘0’  ‘0’无效 ‘1’有效 
	private Integer validState;
	//数量 
	private Double qty;
	
	/** 
	* @Fields areaCode : 所在医院 
	*/ 
	private String hospitalId;
	/** 
	* @Fields areaCode : 所属院区 
	*/ 
	private String areaCode;
	
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Integer getSortId() {
		return sortId;
	}
	public void setSortId(Integer sortId) {
		this.sortId = sortId;
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
	public String getSpellCode() {
		return spellCode;
	}
	public void setSpellCode(String spellCode) {
		this.spellCode = spellCode;
	}
	public String getWbCode() {
		return wbCode;
	}
	public void setWbCode(String wbCode) {
		this.wbCode = wbCode;
	}
	public String getInputCode() {
		return inputCode;
	}
	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	public Integer getValidState() {
		return validState;
	}
	public void setValidState(Integer validState) {
		this.validState = validState;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public String getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	
	
	
}
