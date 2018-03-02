package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

@SuppressWarnings("serial")
public class DrugApplyin extends Entity implements java.io.Serializable {
	// Fields
	/**申请流水号*/
	private Long applyNumber;
	/**库存科室 0-全部部门*/
	private String drugDeptCode;
	/**申请单号*/
	private String applyBillcode;
	/**药品编码*/
	private String drugCode;
	/**药品商品名*/
	private String tradeName;
	/**药品类别*/
	private String drugType;
	/**药品性质*/
	private String drugQuality;
	/**规格*/
	private String specs;
	/**包装单位*/
	private String packUnit;
	/**包装数*/
	private Integer packQty;
	/**最小单位*/
	private String minUnit;
	/**显示的单位标记*/
	private Integer showFlag;
	/**显示的单位*/
	private String showUnit;
	/**批号*/
	private String batchNo;
	/**有效期*/
	private Date validDate;
	/**生产厂家*/
	private String producerCode;
	/**申供货单位代码*/
	private String companyCode;
	/**申零售价*/
	private Double retailPrice;
	/**批发价*/
	private Double wholesalePrice;
	/**购入价*/
	private Double purchasePrice;
	/**零售金额*/
	private Double retailCost;
	/**批发金额*/
	private Double wholesaleCost;
	/**购入金额*/
	private Double purchaseCost;
	/**申请人编码*/
	private String applyOpercode;
	/**申请日期*/
	private Date applyDate;
	/**申请状态 0申请，1（配药）打印，2核准（出库），3作废，4暂不摆药*/
	private String applyState;
	/**申请出库量(每付的总数量)*/
	private Double applyNum;
	/**入库数量*/
	private Double inputNum;
	/**入库人*/
	private String inputOpercode;
	/**入库日期*/
	private Date inputDate;
	/**货位码*/
	private String placeCode;
	/**制剂序号－生产序号或检验序号*/
	private String medId;
	/**发票号*/
	private String invoiceNo;
	/**送药单流水号*/
	private String deliveryNo;
	/**招标序号－招标单的序号*/
	private String tenderNo;
	/**实际扣率*/
	private Double actualRate;
	/**操作员*/
	private String operCode;
	/**操作日期*/
	private Date operDate;
	/**备注*/
	private String mark;
	/**扩展字段*/
	private String extCode;
	/**扩展字段1*/
	private String extCode1;
	/**扩展字段2*/
	private String extCode2;
	
	//一下字段做展示用
	private String vValidDate;
	private String vInputDate;

	/** 
	* @Fields hospitalId : 所属医院 
	*/ 
	private Integer hospitalId;
	/** 
	* @Fields areaCode : 所属院区
	*/ 
	private String areaCode;

	
	public Long getApplyNumber() {
		return this.applyNumber;
	}

	public void setApplyNumber(Long applyNumber) {
		this.applyNumber = applyNumber;
	}

	public String getDrugDeptCode() {
		return this.drugDeptCode;
	}

	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}

	public String getApplyBillcode() {
		return this.applyBillcode;
	}

	public void setApplyBillcode(String applyBillcode) {
		this.applyBillcode = applyBillcode;
	}

	public String getDrugCode() {
		return this.drugCode;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}

	public String getTradeName() {
		return this.tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getDrugType() {
		return this.drugType;
	}

	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}

	public String getDrugQuality() {
		return this.drugQuality;
	}

	public void setDrugQuality(String drugQuality) {
		this.drugQuality = drugQuality;
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

	public Integer getPackQty() {
		return this.packQty;
	}

	public void setPackQty(Integer packQty) {
		this.packQty = packQty;
	}

	public String getMinUnit() {
		return this.minUnit;
	}

	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}

	public Integer getShowFlag() {
		return this.showFlag;
	}

	public void setShowFlag(Integer showFlag) {
		this.showFlag = showFlag;
	}

	public String getShowUnit() {
		return this.showUnit;
	}

	public void setShowUnit(String showUnit) {
		this.showUnit = showUnit;
	}

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getValidDate() {
		return this.validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public String getProducerCode() {
		return this.producerCode;
	}

	public void setProducerCode(String producerCode) {
		this.producerCode = producerCode;
	}

	public String getCompanyCode() {
		return this.companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public Double getRetailPrice() {
		return this.retailPrice;
	}

	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public Double getWholesalePrice() {
		return this.wholesalePrice;
	}

	public void setWholesalePrice(Double wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}

	public Double getPurchasePrice() {
		return this.purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Double getRetailCost() {
		return this.retailCost;
	}

	public void setRetailCost(Double retailCost) {
		this.retailCost = retailCost;
	}

	public Double getWholesaleCost() {
		return this.wholesaleCost;
	}

	public void setWholesaleCost(Double wholesaleCost) {
		this.wholesaleCost = wholesaleCost;
	}

	public Double getPurchaseCost() {
		return this.purchaseCost;
	}

	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public String getApplyOpercode() {
		return this.applyOpercode;
	}

	public void setApplyOpercode(String applyOpercode) {
		this.applyOpercode = applyOpercode;
	}

	public Date getApplyDate() {
		return this.applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getApplyState() {
		return applyState;
	}

	public void setApplyState(String applyState) {
		this.applyState = applyState;
	}

	public Double getApplyNum() {
		return this.applyNum;
	}

	public void setApplyNum(Double applyNum) {
		this.applyNum = applyNum;
	}

	public Double getInputNum() {
		return this.inputNum;
	}

	public void setInputNum(Double inputNum) {
		this.inputNum = inputNum;
	}

	public String getInputOpercode() {
		return this.inputOpercode;
	}

	public void setInputOpercode(String inputOpercode) {
		this.inputOpercode = inputOpercode;
	}

	public Date getInputDate() {
		return this.inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}

	public String getPlaceCode() {
		return this.placeCode;
	}

	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}

	public String getMedId() {
		return this.medId;
	}

	public void setMedId(String medId) {
		this.medId = medId;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getDeliveryNo() {
		return this.deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public String getTenderNo() {
		return this.tenderNo;
	}

	public void setTenderNo(String tenderNo) {
		this.tenderNo = tenderNo;
	}

	public Double getActualRate() {
		return this.actualRate;
	}

	public void setActualRate(Double actualRate) {
		this.actualRate = actualRate;
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

	public String getMark() {
		return this.mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getExtCode() {
		return this.extCode;
	}

	public void setExtCode(String extCode) {
		this.extCode = extCode;
	}

	public String getExtCode1() {
		return this.extCode1;
	}

	public void setExtCode1(String extCode1) {
		this.extCode1 = extCode1;
	}

	public String getExtCode2() {
		return this.extCode2;
	}

	public void setExtCode2(String extCode2) {
		this.extCode2 = extCode2;
	}

	public String getvValidDate() {
		return vValidDate;
	}

	public void setvValidDate(String vValidDate) {
		this.vValidDate = vValidDate;
	}

	public String getvInputDate() {
		return vInputDate;
	}

	public void setvInputDate(String vInputDate) {
		this.vInputDate = vInputDate;
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