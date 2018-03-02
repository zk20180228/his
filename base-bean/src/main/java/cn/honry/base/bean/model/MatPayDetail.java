package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

@SuppressWarnings("serial")
public class MatPayDetail extends Entity {

	//付款明细编号
	private String payDetailNo;
	//付款头表编号
	private String payHeadNo;
	//付款单内序号
	private Integer sequenceNo;
	//公司编码
	private String companyCode;
	//发票号码
	private String invoiceNo;
	//仓库编码
	private String storageCode;
	//付款类型（现金，发票）
	private String payType;
	//本次付款金额
	private Double payCost;
	//未付金额
	private Double unPayCost;
	//付款凭证
	private String payCredence;
	//未付款凭证
	private String unPayCredence;
	//未付款凭证日期
	private Date credenceDate;
	//运费
	private Double carriageCost;
	//开户银行
	private String openBank;
	//银行账号
	private String openCount;
	//付款人代码
	private String payOperCode;
	//付款日期
	private Date payDate;
	//备注
	private String memo;
	//操作员
	private String operCode;
	//操作日期
	private Date operDate;
	//付款结存单据号
	private String payListNum;
	
	public String getPayDetailNo() {
		return payDetailNo;
	}
	public void setPayDetailNo(String payDetailNo) {
		this.payDetailNo = payDetailNo;
	}
	public String getPayHeadNo() {
		return payHeadNo;
	}
	public void setPayHeadNo(String payHeadNo) {
		this.payHeadNo = payHeadNo;
	}
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}
	public Double getUnPayCost() {
		return unPayCost;
	}
	public void setUnPayCost(Double unPayCost) {
		this.unPayCost = unPayCost;
	}
	public String getPayCredence() {
		return payCredence;
	}
	public void setPayCredence(String payCredence) {
		this.payCredence = payCredence;
	}
	public String getUnPayCredence() {
		return unPayCredence;
	}
	public void setUnPayCredence(String unPayCredence) {
		this.unPayCredence = unPayCredence;
	}
	public Date getCredenceDate() {
		return credenceDate;
	}
	public void setCredenceDate(Date credenceDate) {
		this.credenceDate = credenceDate;
	}
	public Double getCarriageCost() {
		return carriageCost;
	}
	public void setCarriageCost(Double carriageCost) {
		this.carriageCost = carriageCost;
	}
	public String getOpenBank() {
		return openBank;
	}
	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}
	public String getOpenCount() {
		return openCount;
	}
	public void setOpenCount(String openCount) {
		this.openCount = openCount;
	}
	public String getPayOperCode() {
		return payOperCode;
	}
	public void setPayOperCode(String payOperCode) {
		this.payOperCode = payOperCode;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
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
	public String getPayListNum() {
		return payListNum;
	}
	public void setPayListNum(String payListNum) {
		this.payListNum = payListNum;
	}
	
	
}
