package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * ClassName: DrugCheckdetail 
 * @Description: 盘点明细表
 * @author lt
 * @date 2015-8-17
 */
@SuppressWarnings("serial")
public class DrugCheckdetail extends Entity implements java.io.Serializable {

	/** 
	* @Fields checkCode : 封账单号
	*/ 
	private String	checkCode;
	/**盘点流水号*/
	private Long checkNo;
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
	/**批发价*/
	private Double wholesalePrice;
	/**购入价*/
	private Double purchasePrice;
	/**药品类别*/
	private String drugType;
	/**药品性质*/
	private String drugQuality;
	/**最小单位*/
	private String minUnit;
	/**包装单位*/
	private String packUnit;
	/**包装数*/
	private Double packQty;
	/**货位号*/
	private String placecode;
	/**有效期*/
	private Date validDate;
	/**生产厂家*/
	private String producer;
	/**封帐库存数量*/
	private Double fstoreNum;
	/**实际盘存数量*/
	private Double adjustNum;
	/**结存库存数量*/
	private Double cstoreNum;
	/**对应最小单位的数量*/
	private Double minNum;
	/**对应包装单位的数量*/
	private Double packNum;
	/**盘点盈亏标记（0 、盘亏; 1、盘盈）*/
	private Integer profitFlag;
	/**药品质量情况（0、好；1、不好）*/
	private Integer qualityFlag;
	/**是否是附加药品（附加药品不是封帐的药品）*/
	private Integer addFlag=0;
	/**处理方式*/
	private String disposeWay;
	/**盘点状态标志（0封帐；1结存；2取消）*/
	private Integer checkState;
	/**盈亏数量*/
	private Double profitLossNum;
	
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
	public Long getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(Long checkNo) {
		this.checkNo = checkNo;
	}
	public String getDrugDeptCode() {
		return drugDeptCode;
	}
	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public Double getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}
	public Double getWholesalePrice() {
		return wholesalePrice;
	}
	public void setWholesalePrice(Double wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}
	public Double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getDrugType() {
		return drugType;
	}
	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}
	public String getDrugQuality() {
		return drugQuality;
	}
	public void setDrugQuality(String drugQuality) {
		this.drugQuality = drugQuality;
	}
	public String getMinUnit() {
		return minUnit;
	}
	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}
	public String getPackUnit() {
		return packUnit;
	}
	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}
	public Double getPackQty() {
		return packQty;
	}
	public void setPackQty(Double packQty) {
		this.packQty = packQty;
	}
	public String getPlacecode() {
		return placecode;
	}
	public void setPlacecode(String placecode) {
		this.placecode = placecode;
	}
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public Double getFstoreNum() {
		return fstoreNum;
	}
	public void setFstoreNum(Double fstoreNum) {
		this.fstoreNum = fstoreNum;
	}
	public Double getAdjustNum() {
		return adjustNum;
	}
	public void setAdjustNum(Double adjustNum) {
		this.adjustNum = adjustNum;
	}
	public Double getCstoreNum() {
		return cstoreNum;
	}
	public void setCstoreNum(Double cstoreNum) {
		this.cstoreNum = cstoreNum;
	}
	public Double getMinNum() {
		return minNum;
	}
	public void setMinNum(Double minNum) {
		this.minNum = minNum;
	}
	public Double getPackNum() {
		return packNum;
	}
	public void setPackNum(Double packNum) {
		this.packNum = packNum;
	}
	public Integer getProfitFlag() {
		return profitFlag;
	}
	public void setProfitFlag(Integer profitFlag) {
		this.profitFlag = profitFlag;
	}
	public Integer getQualityFlag() {
		return qualityFlag;
	}
	public void setQualityFlag(Integer qualityFlag) {
		this.qualityFlag = qualityFlag;
	}
	public Integer getAddFlag() {
		return addFlag;
	}
	public void setAddFlag(Integer addFlag) {
		this.addFlag = addFlag;
	}
	public String getDisposeWay() {
		return disposeWay;
	}
	public void setDisposeWay(String disposeWay) {
		this.disposeWay = disposeWay;
	}
	public Integer getCheckState() {
		return checkState;
	}
	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}
	public Double getProfitLossNum() {
		return profitLossNum;
	}
	public void setProfitLossNum(Double profitLossNum) {
		this.profitLossNum = profitLossNum;
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