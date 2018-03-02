package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * ClassName: DrugPayhead 
 * @Description: 药库供应商往来帐目头表
 * @author lt
 * @date 2015-8-8
 */
@SuppressWarnings("serial")
public class DrugPayhead extends Entity implements java.io.Serializable {

	private String inListCode;
	private String invoiceNo;
	private Date invoiceDate;
	private Double payCost;
	private Double unpayCost;
	private Integer payFlag;
	private Date payDate;
	private Double deliveryCost;
	private Double retailCost;
	private Double wholesaleCost;
	private Double purchaseCost;
	private Double discountCost;
	private String drugDeptCode;
	private String companyCode;
	private String companyName;
	private String merk;
	private String extCode;
	private String extCode1;
	private String extCode2;
	private Date extDate;
	private double extNumber;
	
	/** 
	* @Fields hospitalId : 所属医院 
	*/ 
	private Integer hospitalId;
	/** 
	* @Fields areaCode : 所属院区
	*/ 
	private String areaCode;
	
	public String getInListCode() {
		return inListCode;
	}
	public void setInListCode(String inListCode) {
		this.inListCode = inListCode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}
	public Double getUnpayCost() {
		return unpayCost;
	}
	public void setUnpayCost(Double unpayCost) {
		this.unpayCost = unpayCost;
	}
	public Integer getPayFlag() {
		return payFlag;
	}
	public void setPayFlag(Integer payFlag) {
		this.payFlag = payFlag;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public Double getDeliveryCost() {
		return deliveryCost;
	}
	public void setDeliveryCost(Double deliveryCost) {
		this.deliveryCost = deliveryCost;
	}
	public Double getRetailCost() {
		return retailCost;
	}
	public void setRetailCost(Double retailCost) {
		this.retailCost = retailCost;
	}
	public Double getWholesaleCost() {
		return wholesaleCost;
	}
	public void setWholesaleCost(Double wholesaleCost) {
		this.wholesaleCost = wholesaleCost;
	}
	public Double getPurchaseCost() {
		return purchaseCost;
	}
	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}
	public Double getDiscountCost() {
		return discountCost;
	}
	public void setDiscountCost(Double discountCost) {
		this.discountCost = discountCost;
	}
	public String getDrugDeptCode() {
		return drugDeptCode;
	}
	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getMerk() {
		return merk;
	}
	public void setMerk(String merk) {
		this.merk = merk;
	}
	public String getExtCode() {
		return extCode;
	}
	public void setExtCode(String extCode) {
		this.extCode = extCode;
	}
	public String getExtCode1() {
		return extCode1;
	}
	public void setExtCode1(String extCode1) {
		this.extCode1 = extCode1;
	}
	public String getExtCode2() {
		return extCode2;
	}
	public void setExtCode2(String extCode2) {
		this.extCode2 = extCode2;
	}
	public Date getExtDate() {
		return extDate;
	}
	public void setExtDate(Date extDate) {
		this.extDate = extDate;
	}
	public double getExtNumber() {
		return extNumber;
	}
	public void setExtNumber(double extNumber) {
		this.extNumber = extNumber;
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