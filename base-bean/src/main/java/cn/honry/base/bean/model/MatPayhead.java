package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * TMatPayhead entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class MatPayhead extends Entity {

	// Fields

	private String payheadNo;
	private String companyCode;
	private String inListCode;
	private String invoiceNo;
	private Date invoiceDate;
	private Date inDate;
	private String storageCode;
	private Double purchaseCost;
	private Double carriageCost;
	private Double discountCost;
	private Double payCost;
	private Double unpayCost;
	private Integer payflag;
	private Date paydate;
	private Double retailCost;
	private Double wholesaleCost;
	private String memo;
	private String operCode;
	private Date operDate;
	public String getPayheadNo() {
		return payheadNo;
	}
	public void setPayheadNo(String payheadNo) {
		this.payheadNo = payheadNo;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
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
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}
	public Double getPurchaseCost() {
		return purchaseCost;
	}
	public void setPurchaseCost(Double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}
	public Double getCarriageCost() {
		return carriageCost;
	}
	public void setCarriageCost(Double carriageCost) {
		this.carriageCost = carriageCost;
	}
	public Double getDiscountCost() {
		return discountCost;
	}
	public void setDiscountCost(Double discountCost) {
		this.discountCost = discountCost;
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
	public Integer getPayflag() {
		return payflag;
	}
	public void setPayflag(Integer payflag) {
		this.payflag = payflag;
	}
	public Date getPaydate() {
		return paydate;
	}
	public void setPaydate(Date paydate) {
		this.paydate = paydate;
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
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
}