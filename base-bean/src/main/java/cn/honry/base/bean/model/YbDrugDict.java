package cn.honry.base.bean.model;

import java.sql.Timestamp;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

/** 
 * @Description： 医保中心药品目录
 * @Author：zt
 * @CreateDate：2017-12-4 下午14:00:05  
 */
public class YbDrugDict extends Entity{
	
	private static final long serialVersionUID = 1L;
	private Integer serialNo;
	private String drugCode;
	private String drugName;
	private String englishName;
	private String costType;
	private String prescFlag;
	private String costItemGrade;
	private String pinyinCode;
	private String drugDoseunit;
	private String maxPrice;
	private String outSelfProportion;
	private String inSelfProportion;
	private String drugDosageform;
	private String drugOncedosage;
	private String drugFrequency;
	private String drugUsemode;
	private String wbCode;
	private String drugUnit;
	private String drugSpec;
	private String limitTime;
	private String drugGoodsName;
	private String goodsPrice;
	private String goodsPinyinCode;
	private String goodWbCode;
	private String drugManufacturer;
	private String madeSelfFlag;
	private String insuranceNo;
	private String medicine;
	private String lxSelfProportion;
	private String gsSelfProportion;
	private String sySelfProportion;
	private String eySelfProportion;
	private String drugType;
	private String approveFlag;
	private String operator;
	private Date operatorTime;
	private Date startTime;
	private Date stopTime;
	private String memo;
	private String insuranceDrugCode;
	
	/** 分页用的page和rows*/
	private String page;
	private String rows;
	

	
	public Integer getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getDrugCode() {
		return this.drugCode;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}

	public String getDrugName() {
		return this.drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getEnglishName() {
		return this.englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getCostType() {
		return this.costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getPrescFlag() {
		return this.prescFlag;
	}

	public void setPrescFlag(String prescFlag) {
		this.prescFlag = prescFlag;
	}

	public String getCostItemGrade() {
		return this.costItemGrade;
	}

	public void setCostItemGrade(String costItemGrade) {
		this.costItemGrade = costItemGrade;
	}

	public String getPinyinCode() {
		return this.pinyinCode;
	}

	public void setPinyinCode(String pinyinCode) {
		this.pinyinCode = pinyinCode;
	}

	public String getDrugDoseunit() {
		return this.drugDoseunit;
	}

	public void setDrugDoseunit(String drugDoseunit) {
		this.drugDoseunit = drugDoseunit;
	}

	public String getMaxPrice() {
		return this.maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getOutSelfProportion() {
		return this.outSelfProportion;
	}

	public void setOutSelfProportion(String outSelfProportion) {
		this.outSelfProportion = outSelfProportion;
	}

	public String getInSelfProportion() {
		return this.inSelfProportion;
	}

	public void setInSelfProportion(String inSelfProportion) {
		this.inSelfProportion = inSelfProportion;
	}

	public String getDrugDosageform() {
		return this.drugDosageform;
	}

	public void setDrugDosageform(String drugDosageform) {
		this.drugDosageform = drugDosageform;
	}

	public String getDrugOncedosage() {
		return this.drugOncedosage;
	}

	public void setDrugOncedosage(String drugOncedosage) {
		this.drugOncedosage = drugOncedosage;
	}

	public String getDrugFrequency() {
		return this.drugFrequency;
	}

	public void setDrugFrequency(String drugFrequency) {
		this.drugFrequency = drugFrequency;
	}

	public String getDrugUsemode() {
		return this.drugUsemode;
	}

	public void setDrugUsemode(String drugUsemode) {
		this.drugUsemode = drugUsemode;
	}

	public String getWbCode() {
		return this.wbCode;
	}

	public void setWbCode(String wbCode) {
		this.wbCode = wbCode;
	}

	public String getDrugUnit() {
		return this.drugUnit;
	}

	public void setDrugUnit(String drugUnit) {
		this.drugUnit = drugUnit;
	}

	public String getDrugSpec() {
		return this.drugSpec;
	}

	public void setDrugSpec(String drugSpec) {
		this.drugSpec = drugSpec;
	}

	public String getLimitTime() {
		return this.limitTime;
	}

	public void setLimitTime(String limitTime) {
		this.limitTime = limitTime;
	}

	public String getDrugGoodsName() {
		return this.drugGoodsName;
	}

	public void setDrugGoodsName(String drugGoodsName) {
		this.drugGoodsName = drugGoodsName;
	}

	public String getGoodsPrice() {
		return this.goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getGoodsPinyinCode() {
		return this.goodsPinyinCode;
	}

	public void setGoodsPinyinCode(String goodsPinyinCode) {
		this.goodsPinyinCode = goodsPinyinCode;
	}

	public String getGoodWbCode() {
		return this.goodWbCode;
	}

	public void setGoodWbCode(String goodWbCode) {
		this.goodWbCode = goodWbCode;
	}

	public String getDrugManufacturer() {
		return this.drugManufacturer;
	}

	public void setDrugManufacturer(String drugManufacturer) {
		this.drugManufacturer = drugManufacturer;
	}

	public String getMadeSelfFlag() {
		return this.madeSelfFlag;
	}

	public void setMadeSelfFlag(String madeSelfFlag) {
		this.madeSelfFlag = madeSelfFlag;
	}

	public String getInsuranceNo() {
		return this.insuranceNo;
	}

	public void setInsuranceNo(String insuranceNo) {
		this.insuranceNo = insuranceNo;
	}

	public String getMedicine() {
		return this.medicine;
	}

	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}

	public String getLxSelfProportion() {
		return this.lxSelfProportion;
	}

	public void setLxSelfProportion(String lxSelfProportion) {
		this.lxSelfProportion = lxSelfProportion;
	}

	public String getGsSelfProportion() {
		return this.gsSelfProportion;
	}

	public void setGsSelfProportion(String gsSelfProportion) {
		this.gsSelfProportion = gsSelfProportion;
	}

	public String getSySelfProportion() {
		return this.sySelfProportion;
	}

	public void setSySelfProportion(String sySelfProportion) {
		this.sySelfProportion = sySelfProportion;
	}

	public String getEySelfProportion() {
		return this.eySelfProportion;
	}

	public void setEySelfProportion(String eySelfProportion) {
		this.eySelfProportion = eySelfProportion;
	}

	public String getDrugType() {
		return this.drugType;
	}

	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}

	public String getApproveFlag() {
		return this.approveFlag;
	}

	public void setApproveFlag(String approveFlag) {
		this.approveFlag = approveFlag;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperatorTime() {
		return operatorTime;
	}

	public void setOperatorTime(Date operatorTime) {
		this.operatorTime = operatorTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public void setStopTime(Timestamp stopTime) {
		this.stopTime = stopTime;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getInsuranceDrugCode() {
		return this.insuranceDrugCode;
	}

	public void setInsuranceDrugCode(String insuranceDrugCode) {
		this.insuranceDrugCode = insuranceDrugCode;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
}
