package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiFinInvoicedetail entity. @author MyEclipse Persistence Tools
 */

public class BiFinInvoicedetail implements java.io.Serializable {

	// Fields

	private String id;
	private String invoiceNo;
	private String transType;
	private Byte invoSequence;
	private String invoCode;
	private String invoName;
	private Double pubCost;
	private Double ownCost;
	private Double payCost;
	private String deptCode;
	private String deptName;
	private Date operDate;
	private String operCode;
	private String balanceFlag;
	private String balanceNo;
	private String balanceOpcd;
	private Date balanceDate;
	private String cancelFlag;
	private String invoiceSeq;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;

	// Constructors

	/** default constructor */
	public BiFinInvoicedetail() {
	}

	/** minimal constructor */
	public BiFinInvoicedetail(String invoiceNo, String transType,
			Byte invoSequence, String invoCode) {
		this.invoiceNo = invoiceNo;
		this.transType = transType;
		this.invoSequence = invoSequence;
		this.invoCode = invoCode;
	}

	/** full constructor */
	public BiFinInvoicedetail(String invoiceNo, String transType,
			Byte invoSequence, String invoCode, String invoName,
			Double pubCost, Double ownCost, Double payCost, String deptCode,
			String deptName, Date operDate, String operCode,
			String balanceFlag, String balanceNo, String balanceOpcd,
			Date balanceDate, String cancelFlag, String invoiceSeq,
			String ext1, String ext2, String ext3, String ext4) {
		this.invoiceNo = invoiceNo;
		this.transType = transType;
		this.invoSequence = invoSequence;
		this.invoCode = invoCode;
		this.invoName = invoName;
		this.pubCost = pubCost;
		this.ownCost = ownCost;
		this.payCost = payCost;
		this.deptCode = deptCode;
		this.deptName = deptName;
		this.operDate = operDate;
		this.operCode = operCode;
		this.balanceFlag = balanceFlag;
		this.balanceNo = balanceNo;
		this.balanceOpcd = balanceOpcd;
		this.balanceDate = balanceDate;
		this.cancelFlag = cancelFlag;
		this.invoiceSeq = invoiceSeq;
		this.ext1 = ext1;
		this.ext2 = ext2;
		this.ext3 = ext3;
		this.ext4 = ext4;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getTransType() {
		return this.transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public Byte getInvoSequence() {
		return this.invoSequence;
	}

	public void setInvoSequence(Byte invoSequence) {
		this.invoSequence = invoSequence;
	}

	public String getInvoCode() {
		return this.invoCode;
	}

	public void setInvoCode(String invoCode) {
		this.invoCode = invoCode;
	}

	public String getInvoName() {
		return this.invoName;
	}

	public void setInvoName(String invoName) {
		this.invoName = invoName;
	}

	public Double getPubCost() {
		return this.pubCost;
	}

	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}

	public Double getOwnCost() {
		return this.ownCost;
	}

	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}

	public Double getPayCost() {
		return this.payCost;
	}

	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}

	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public String getOperCode() {
		return this.operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}

	public String getBalanceFlag() {
		return this.balanceFlag;
	}

	public void setBalanceFlag(String balanceFlag) {
		this.balanceFlag = balanceFlag;
	}

	public String getBalanceNo() {
		return this.balanceNo;
	}

	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}

	public String getBalanceOpcd() {
		return this.balanceOpcd;
	}

	public void setBalanceOpcd(String balanceOpcd) {
		this.balanceOpcd = balanceOpcd;
	}

	public Date getBalanceDate() {
		return this.balanceDate;
	}

	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}

	public String getCancelFlag() {
		return this.cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

	public String getInvoiceSeq() {
		return this.invoiceSeq;
	}

	public void setInvoiceSeq(String invoiceSeq) {
		this.invoiceSeq = invoiceSeq;
	}

	public String getExt1() {
		return this.ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return this.ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return this.ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return this.ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

}