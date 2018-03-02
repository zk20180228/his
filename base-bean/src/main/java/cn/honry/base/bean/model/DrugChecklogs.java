package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;


/**
 * ClassName: DrugChecklogs 
 * @Description: 盘点日志表
 * @author lt
 * @date 2015-8-17
 */
@SuppressWarnings("serial")
public class DrugChecklogs extends Entity implements java.io.Serializable {

	/**多出了一个ID字段,原ID(checkCode)加入*/
	private String checkCode;
	
	/**科室代码0-全部部门*/
	private String drugDeptCode;
	/**药品编码*/
	private String drugCode;
	/**批号(如果等于'全部',则表示所有批号的药品)*/
	private String batchNo;
	/**商品名称*/
	private String tradeName;
	/**规格*/
	private String specs;
	/**零售价*/
	private Double retailPrice;
	/**包装单位*/
	private String packUnit;
	/**包装数*/
	private Double packQty;
	/**实际盘存数量*/
	private Double adjustNum;
	/**盘存单位*/
	private String adjustUnit;
	/**货位号*/
	private String placeNo;
	/**备注*/
	private String remark;
	
	/** 
	* @Fields hospitalId : 所属医院 
	*/ 
	private Integer hospitalId;
	/** 
	* @Fields areaCode : 所属院区
	*/ 
	private String areaCode;

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getDrugDeptCode() {
		return this.drugDeptCode;
	}

	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}

	public String getDrugCode() {
		return this.drugCode;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getTradeName() {
		return this.tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getSpecs() {
		return this.specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public Double getRetailPrice() {
		return this.retailPrice;
	}

	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public String getPackUnit() {
		return this.packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public Double getPackQty() {
		return this.packQty;
	}

	public void setPackQty(Double packQty) {
		this.packQty = packQty;
	}

	public Double getAdjustNum() {
		return this.adjustNum;
	}

	public void setAdjustNum(Double adjustNum) {
		this.adjustNum = adjustNum;
	}

	public String getAdjustUnit() {
		return this.adjustUnit;
	}

	public void setAdjustUnit(String adjustUnit) {
		this.adjustUnit = adjustUnit;
	}

	public String getPlaceNo() {
		return this.placeNo;
	}

	public void setPlaceNo(String placeNo) {
		this.placeNo = placeNo;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
}