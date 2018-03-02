package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiFinPaymode entity. @author MyEclipse Persistence Tools
 */

public class BiFinPaymode implements java.io.Serializable {

	// Fields

	private String id;
	private String invoiceNo;
	private String transType;
	private Byte sequenceNo;
	private String modeCode;
	private String modeName;
	private Double totCost;
	private Double realCost;
	private String bankCode;
	private String bankName;
	private String account;
	private String posNo;
	private String checkNo;
	private String operCode;
	private Date operDate;
	private String checkFlag;
	private String checkOpcd;
	private Date checkDate;
	private String balanceFlag;
	private String balanceNo;
	private String balanceOpcd;
	private String correctFlag;
	private String correctOpcd;
	private Date correctDate;
	private Date balanceDate;
	private String cancelFlag;
	private String invoiceSeq;
	private String invoiceComb;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;

	// Constructors

	/** default constructor */
	public BiFinPaymode() {
	}

	/** minimal constructor */
	public BiFinPaymode(String invoiceNo, String transType, Byte sequenceNo,
			String invoiceSeq) {
		this.invoiceNo = invoiceNo;
		this.transType = transType;
		this.sequenceNo = sequenceNo;
		this.invoiceSeq = invoiceSeq;
	}

	/** full constructor */
	public BiFinPaymode(String invoiceNo, String transType, Byte sequenceNo,
			String modeCode, String modeName, Double totCost, Double realCost,
			String bankCode, String bankName, String account, String posNo,
			String checkNo, String operCode, Date operDate, String checkFlag,
			String checkOpcd, Date checkDate, String balanceFlag,
			String balanceNo, String balanceOpcd, String correctFlag,
			String correctOpcd, Date correctDate, Date balanceDate,
			String cancelFlag, String invoiceSeq, String invoiceComb,
			String ext1, String ext2, String ext3, String ext4) {
		this.invoiceNo = invoiceNo;
		this.transType = transType;
		this.sequenceNo = sequenceNo;
		this.modeCode = modeCode;
		this.modeName = modeName;
		this.totCost = totCost;
		this.realCost = realCost;
		this.bankCode = bankCode;
		this.bankName = bankName;
		this.account = account;
		this.posNo = posNo;
		this.checkNo = checkNo;
		this.operCode = operCode;
		this.operDate = operDate;
		this.checkFlag = checkFlag;
		this.checkOpcd = checkOpcd;
		this.checkDate = checkDate;
		this.balanceFlag = balanceFlag;
		this.balanceNo = balanceNo;
		this.balanceOpcd = balanceOpcd;
		this.correctFlag = correctFlag;
		this.correctOpcd = correctOpcd;
		this.correctDate = correctDate;
		this.balanceDate = balanceDate;
		this.cancelFlag = cancelFlag;
		this.invoiceSeq = invoiceSeq;
		this.invoiceComb = invoiceComb;
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

	public Byte getSequenceNo() {
		return this.sequenceNo;
	}

	public void setSequenceNo(Byte sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getModeCode() {
		return this.modeCode;
	}

	public void setModeCode(String modeCode) {
		this.modeCode = modeCode;
	}

	public String getModeName() {
		return this.modeName;
	}

	public void setModeName(String modeName) {
		this.modeName = modeName;
	}

	public Double getTotCost() {
		return this.totCost;
	}

	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}

	public Double getRealCost() {
		return this.realCost;
	}

	public void setRealCost(Double realCost) {
		this.realCost = realCost;
	}

	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPosNo() {
		return this.posNo;
	}

	public void setPosNo(String posNo) {
		this.posNo = posNo;
	}

	public String getCheckNo() {
		return this.checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
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

	public String getCheckFlag() {
		return this.checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	public String getCheckOpcd() {
		return this.checkOpcd;
	}

	public void setCheckOpcd(String checkOpcd) {
		this.checkOpcd = checkOpcd;
	}

	public Date getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
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

	public String getCorrectFlag() {
		return this.correctFlag;
	}

	public void setCorrectFlag(String correctFlag) {
		this.correctFlag = correctFlag;
	}

	public String getCorrectOpcd() {
		return this.correctOpcd;
	}

	public void setCorrectOpcd(String correctOpcd) {
		this.correctOpcd = correctOpcd;
	}

	public Date getCorrectDate() {
		return this.correctDate;
	}

	public void setCorrectDate(Date correctDate) {
		this.correctDate = correctDate;
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

	public String getInvoiceComb() {
		return this.invoiceComb;
	}

	public void setInvoiceComb(String invoiceComb) {
		this.invoiceComb = invoiceComb;
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