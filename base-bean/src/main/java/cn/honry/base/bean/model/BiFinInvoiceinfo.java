package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiFinInvoiceinfo entity. @author MyEclipse Persistence Tools
 */

public class BiFinInvoiceinfo implements java.io.Serializable {

	// Fields

	private String id;
	private String invoiceNo;
	private String transType;
	private String cardNo;
	private String clinicCode;
	private Date regDate;
	private String patientName;
	private String paykindCode;
	private String paykindName;
	private String pactCode;
	private String pactName;
	private String medicalTypeCode;
	private String medicalType;
	private Double totCost;
	private Double pubCost;
	private Double ownCost;
	private Double payCost;
	private Double realCost;
	private Double back1;
	private Double back2;
	private Double back3;
	private String operCode;
	private Date operDate;
	private String examineFlag;
	private String cancelFlag;
	private String cancelInvoice;
	private String cancelCode;
	private Date cancelDate;
	private String checkFlag;
	private String checkOpcd;
	private Date checkDate;
	private String balanceFlag;
	private String balanceNo;
	private String balanceOpcd;
	private Date balanceDate;
	private String invoiceSeq;
	private String extFlag;
	private String printInvoiceno;
	private String drugWindow;
	private String accountFlag;
	private String invoiceComb;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;

	// Constructors

	/** default constructor */
	public BiFinInvoiceinfo() {
	}

	/** minimal constructor */
	public BiFinInvoiceinfo(String invoiceNo, String transType, String cardNo,
			String clinicCode, String paykindCode, String invoiceSeq) {
		this.invoiceNo = invoiceNo;
		this.transType = transType;
		this.cardNo = cardNo;
		this.clinicCode = clinicCode;
		this.paykindCode = paykindCode;
		this.invoiceSeq = invoiceSeq;
	}

	/** full constructor */
	public BiFinInvoiceinfo(String invoiceNo, String transType, String cardNo,
			String clinicCode, Date regDate, String patientName,
			String paykindCode, String paykindName, String pactCode,
			String pactName, String medicalTypeCode, String medicalType,
			Double totCost, Double pubCost, Double ownCost, Double payCost,
			Double realCost, Double back1, Double back2, Double back3,
			String operCode, Date operDate, String examineFlag,
			String cancelFlag, String cancelInvoice, String cancelCode,
			Date cancelDate, String checkFlag, String checkOpcd,
			Date checkDate, String balanceFlag, String balanceNo,
			String balanceOpcd, Date balanceDate, String invoiceSeq,
			String extFlag, String printInvoiceno, String drugWindow,
			String accountFlag, String invoiceComb, String ext1, String ext2,
			String ext3, String ext4) {
		this.invoiceNo = invoiceNo;
		this.transType = transType;
		this.cardNo = cardNo;
		this.clinicCode = clinicCode;
		this.regDate = regDate;
		this.patientName = patientName;
		this.paykindCode = paykindCode;
		this.paykindName = paykindName;
		this.pactCode = pactCode;
		this.pactName = pactName;
		this.medicalTypeCode = medicalTypeCode;
		this.medicalType = medicalType;
		this.totCost = totCost;
		this.pubCost = pubCost;
		this.ownCost = ownCost;
		this.payCost = payCost;
		this.realCost = realCost;
		this.back1 = back1;
		this.back2 = back2;
		this.back3 = back3;
		this.operCode = operCode;
		this.operDate = operDate;
		this.examineFlag = examineFlag;
		this.cancelFlag = cancelFlag;
		this.cancelInvoice = cancelInvoice;
		this.cancelCode = cancelCode;
		this.cancelDate = cancelDate;
		this.checkFlag = checkFlag;
		this.checkOpcd = checkOpcd;
		this.checkDate = checkDate;
		this.balanceFlag = balanceFlag;
		this.balanceNo = balanceNo;
		this.balanceOpcd = balanceOpcd;
		this.balanceDate = balanceDate;
		this.invoiceSeq = invoiceSeq;
		this.extFlag = extFlag;
		this.printInvoiceno = printInvoiceno;
		this.drugWindow = drugWindow;
		this.accountFlag = accountFlag;
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

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getClinicCode() {
		return this.clinicCode;
	}

	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}

	public Date getRegDate() {
		return this.regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getPatientName() {
		return this.patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPaykindCode() {
		return this.paykindCode;
	}

	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	}

	public String getPaykindName() {
		return this.paykindName;
	}

	public void setPaykindName(String paykindName) {
		this.paykindName = paykindName;
	}

	public String getPactCode() {
		return this.pactCode;
	}

	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}

	public String getPactName() {
		return this.pactName;
	}

	public void setPactName(String pactName) {
		this.pactName = pactName;
	}

	public String getMedicalTypeCode() {
		return this.medicalTypeCode;
	}

	public void setMedicalTypeCode(String medicalTypeCode) {
		this.medicalTypeCode = medicalTypeCode;
	}

	public String getMedicalType() {
		return this.medicalType;
	}

	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}

	public Double getTotCost() {
		return this.totCost;
	}

	public void setTotCost(Double totCost) {
		this.totCost = totCost;
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

	public Double getRealCost() {
		return this.realCost;
	}

	public void setRealCost(Double realCost) {
		this.realCost = realCost;
	}

	public Double getBack1() {
		return this.back1;
	}

	public void setBack1(Double back1) {
		this.back1 = back1;
	}

	public Double getBack2() {
		return this.back2;
	}

	public void setBack2(Double back2) {
		this.back2 = back2;
	}

	public Double getBack3() {
		return this.back3;
	}

	public void setBack3(Double back3) {
		this.back3 = back3;
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

	public String getExamineFlag() {
		return this.examineFlag;
	}

	public void setExamineFlag(String examineFlag) {
		this.examineFlag = examineFlag;
	}

	public String getCancelFlag() {
		return this.cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

	public String getCancelInvoice() {
		return this.cancelInvoice;
	}

	public void setCancelInvoice(String cancelInvoice) {
		this.cancelInvoice = cancelInvoice;
	}

	public String getCancelCode() {
		return this.cancelCode;
	}

	public void setCancelCode(String cancelCode) {
		this.cancelCode = cancelCode;
	}

	public Date getCancelDate() {
		return this.cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
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

	public Date getBalanceDate() {
		return this.balanceDate;
	}

	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}

	public String getInvoiceSeq() {
		return this.invoiceSeq;
	}

	public void setInvoiceSeq(String invoiceSeq) {
		this.invoiceSeq = invoiceSeq;
	}

	public String getExtFlag() {
		return this.extFlag;
	}

	public void setExtFlag(String extFlag) {
		this.extFlag = extFlag;
	}

	public String getPrintInvoiceno() {
		return this.printInvoiceno;
	}

	public void setPrintInvoiceno(String printInvoiceno) {
		this.printInvoiceno = printInvoiceno;
	}

	public String getDrugWindow() {
		return this.drugWindow;
	}

	public void setDrugWindow(String drugWindow) {
		this.drugWindow = drugWindow;
	}

	public String getAccountFlag() {
		return this.accountFlag;
	}

	public void setAccountFlag(String accountFlag) {
		this.accountFlag = accountFlag;
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