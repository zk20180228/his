package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiRegister entity. @author MyEclipse Persistence Tools
 */

public class BiRegister implements java.io.Serializable {

	// Fields

	private String serialNo;
	private String clinicCode;
	private String transType;
	private String cardNo;
	private Date regDate;
	private String noonCode;
	private String patinentId;
	private String patientName;
	private String patientSex;
	private Date patientBirthday;
	private String patientAgeunit;
	private String patientAge;
	private String patientAgeunitCode;
	private String paykindCode;
	private String paykindName;
	private String pactCode;
	private String pactName;
	private String medicalTypeCode;
	private String medicalType;
	private String mcardNo;
	private String reglevlCode;
	private String reglevlName;
	private String deptCode;
	private String deptName;
	private String schemaNo;
	private String orderNo;
	private String seeno;
	private Date beginTime;
	private Date endTime;
	private String doctCode;
	private String doctName;
	private String ynregchrg;
	private String invoiceNo;
	private String invoicePrintFlag;
	private String ynbook;
	private String ynfr;
	private String appendFlag;
	private String regFeeCode;
	private Double regFee;
	private String chckFeeCode;
	private Double chckFee;
	private String diagFeeCode;
	private Double diagFee;
	private String othFeeCode;
	private Double othFee;
	private String bookFeeCode;
	private Double bookFee;
	private String bookFlag;
	private Double ecoCost;
	private Double ownCost;
	private Double pubCost;
	private Double payCost;
	private Double sumCost;
	private String operCode;
	private Date operDate;
	private String cancelOpcd;
	private Date cancelDate;
	private String balanceFlag;
	private String balanceNo;
	private String balanceOpcd;
	private Date balanceDate;
	private String ynsee;
	private Date seeDate;
	private String triageFlag;
	private String triageOpcd;
	private Date triageDate;
	private Byte printInvoicecnt;
	private String seeDpcd;
	private String seeDocd;
	private String inSource;
	private String inState;
	private String isAccount;
	private String operseq;
	private Date inDate;
	private Date outDate;
	private String zgCode;
	private String zgName;
	private String siCard;
	private String siNo;
	private String backnumberReason;
	private String accountNo;
	private String upFlag;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String emergencyFlag;
	// Constructors

	/** default constructor */
	public BiRegister() {
	}

	/** minimal constructor */
	public BiRegister(String clinicCode, String transType, String cardNo,
			Date regDate, String patinentId) {
		this.clinicCode = clinicCode;
		this.transType = transType;
		this.cardNo = cardNo;
		this.regDate = regDate;
		this.patinentId = patinentId;
	}

	/** full constructor */
	public BiRegister(String clinicCode, String transType, String cardNo,
			Date regDate, String noonCode, String patinentId,
			String patientName, String patientSex, Date patientBirthday,
			String patientAgeunit, String patientAge,
			String patientAgeunitCode, String paykindCode, String paykindName,
			String pactCode, String pactName, String medicalTypeCode,
			String medicalType, String mcardNo, String reglevlCode,
			String reglevlName, String deptCode, String deptName,
			String schemaNo, String orderNo, String seeno, Date beginTime,
			Date endTime, String doctCode, String doctName, String ynregchrg,
			String invoiceNo, String invoicePrintFlag, String ynbook,
			String ynfr, String appendFlag, String regFeeCode, Double regFee,
			String chckFeeCode, Double chckFee, String diagFeeCode,
			Double diagFee, String othFeeCode, Double othFee,
			String bookFeeCode, Double bookFee, String bookFlag,
			Double ecoCost, Double ownCost, Double pubCost, Double payCost,
			Double sumCost, String operCode, Date operDate, String cancelOpcd,
			Date cancelDate, String balanceFlag, String balanceNo,
			String balanceOpcd, Date balanceDate, String ynsee, Date seeDate,
			String triageFlag, String triageOpcd, Date triageDate,
			Byte printInvoicecnt, String seeDpcd, String seeDocd,
			String inSource, String inState, String isAccount, String operseq,
			Date inDate, Date outDate, String zgCode, String zgName,
			String siCard, String siNo, String backnumberReason,
			String accountNo, String upFlag, String ext1, String ext2,
			String ext3, String ext4) {
		this.clinicCode = clinicCode;
		this.transType = transType;
		this.cardNo = cardNo;
		this.regDate = regDate;
		this.noonCode = noonCode;
		this.patinentId = patinentId;
		this.patientName = patientName;
		this.patientSex = patientSex;
		this.patientBirthday = patientBirthday;
		this.patientAgeunit = patientAgeunit;
		this.patientAge = patientAge;
		this.patientAgeunitCode = patientAgeunitCode;
		this.paykindCode = paykindCode;
		this.paykindName = paykindName;
		this.pactCode = pactCode;
		this.pactName = pactName;
		this.medicalTypeCode = medicalTypeCode;
		this.medicalType = medicalType;
		this.mcardNo = mcardNo;
		this.reglevlCode = reglevlCode;
		this.reglevlName = reglevlName;
		this.deptCode = deptCode;
		this.deptName = deptName;
		this.schemaNo = schemaNo;
		this.orderNo = orderNo;
		this.seeno = seeno;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.doctCode = doctCode;
		this.doctName = doctName;
		this.ynregchrg = ynregchrg;
		this.invoiceNo = invoiceNo;
		this.invoicePrintFlag = invoicePrintFlag;
		this.ynbook = ynbook;
		this.ynfr = ynfr;
		this.appendFlag = appendFlag;
		this.regFeeCode = regFeeCode;
		this.regFee = regFee;
		this.chckFeeCode = chckFeeCode;
		this.chckFee = chckFee;
		this.diagFeeCode = diagFeeCode;
		this.diagFee = diagFee;
		this.othFeeCode = othFeeCode;
		this.othFee = othFee;
		this.bookFeeCode = bookFeeCode;
		this.bookFee = bookFee;
		this.bookFlag = bookFlag;
		this.ecoCost = ecoCost;
		this.ownCost = ownCost;
		this.pubCost = pubCost;
		this.payCost = payCost;
		this.sumCost = sumCost;
		this.operCode = operCode;
		this.operDate = operDate;
		this.cancelOpcd = cancelOpcd;
		this.cancelDate = cancelDate;
		this.balanceFlag = balanceFlag;
		this.balanceNo = balanceNo;
		this.balanceOpcd = balanceOpcd;
		this.balanceDate = balanceDate;
		this.ynsee = ynsee;
		this.seeDate = seeDate;
		this.triageFlag = triageFlag;
		this.triageOpcd = triageOpcd;
		this.triageDate = triageDate;
		this.printInvoicecnt = printInvoicecnt;
		this.seeDpcd = seeDpcd;
		this.seeDocd = seeDocd;
		this.inSource = inSource;
		this.inState = inState;
		this.isAccount = isAccount;
		this.operseq = operseq;
		this.inDate = inDate;
		this.outDate = outDate;
		this.zgCode = zgCode;
		this.zgName = zgName;
		this.siCard = siCard;
		this.siNo = siNo;
		this.backnumberReason = backnumberReason;
		this.accountNo = accountNo;
		this.upFlag = upFlag;
		this.ext1 = ext1;
		this.ext2 = ext2;
		this.ext3 = ext3;
		this.ext4 = ext4;
	}

	// Property accessors
	

	public String getSerialNo() {
		return this.serialNo;
	}


	public String getEmergencyFlag() {
		return emergencyFlag;
	}

	public void setEmergencyFlag(String emergencyFlag) {
		this.emergencyFlag = emergencyFlag;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getClinicCode() {
		return this.clinicCode;
	}

	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
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

	public Date getRegDate() {
		return this.regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getNoonCode() {
		return this.noonCode;
	}

	public void setNoonCode(String noonCode) {
		this.noonCode = noonCode;
	}

	public String getPatinentId() {
		return this.patinentId;
	}

	public void setPatinentId(String patinentId) {
		this.patinentId = patinentId;
	}

	public String getPatientName() {
		return this.patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientSex() {
		return this.patientSex;
	}

	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	public Date getPatientBirthday() {
		return this.patientBirthday;
	}

	public void setPatientBirthday(Date patientBirthday) {
		this.patientBirthday = patientBirthday;
	}

	public String getPatientAgeunit() {
		return this.patientAgeunit;
	}

	public void setPatientAgeunit(String patientAgeunit) {
		this.patientAgeunit = patientAgeunit;
	}

	public String getPatientAge() {
		return this.patientAge;
	}

	public void setPatientAge(String patientAge) {
		this.patientAge = patientAge;
	}

	public String getPatientAgeunitCode() {
		return this.patientAgeunitCode;
	}

	public void setPatientAgeunitCode(String patientAgeunitCode) {
		this.patientAgeunitCode = patientAgeunitCode;
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

	public String getMcardNo() {
		return this.mcardNo;
	}

	public void setMcardNo(String mcardNo) {
		this.mcardNo = mcardNo;
	}

	public String getReglevlCode() {
		return this.reglevlCode;
	}

	public void setReglevlCode(String reglevlCode) {
		this.reglevlCode = reglevlCode;
	}

	public String getReglevlName() {
		return this.reglevlName;
	}

	public void setReglevlName(String reglevlName) {
		this.reglevlName = reglevlName;
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

	public String getSchemaNo() {
		return this.schemaNo;
	}

	public void setSchemaNo(String schemaNo) {
		this.schemaNo = schemaNo;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSeeno() {
		return this.seeno;
	}

	public void setSeeno(String seeno) {
		this.seeno = seeno;
	}

	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getDoctCode() {
		return this.doctCode;
	}

	public void setDoctCode(String doctCode) {
		this.doctCode = doctCode;
	}

	public String getDoctName() {
		return this.doctName;
	}

	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}

	public String getYnregchrg() {
		return this.ynregchrg;
	}

	public void setYnregchrg(String ynregchrg) {
		this.ynregchrg = ynregchrg;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoicePrintFlag() {
		return this.invoicePrintFlag;
	}

	public void setInvoicePrintFlag(String invoicePrintFlag) {
		this.invoicePrintFlag = invoicePrintFlag;
	}

	public String getYnbook() {
		return this.ynbook;
	}

	public void setYnbook(String ynbook) {
		this.ynbook = ynbook;
	}

	public String getYnfr() {
		return this.ynfr;
	}

	public void setYnfr(String ynfr) {
		this.ynfr = ynfr;
	}

	public String getAppendFlag() {
		return this.appendFlag;
	}

	public void setAppendFlag(String appendFlag) {
		this.appendFlag = appendFlag;
	}

	public String getRegFeeCode() {
		return this.regFeeCode;
	}

	public void setRegFeeCode(String regFeeCode) {
		this.regFeeCode = regFeeCode;
	}

	public Double getRegFee() {
		return this.regFee;
	}

	public void setRegFee(Double regFee) {
		this.regFee = regFee;
	}

	public String getChckFeeCode() {
		return this.chckFeeCode;
	}

	public void setChckFeeCode(String chckFeeCode) {
		this.chckFeeCode = chckFeeCode;
	}

	public Double getChckFee() {
		return this.chckFee;
	}

	public void setChckFee(Double chckFee) {
		this.chckFee = chckFee;
	}

	public String getDiagFeeCode() {
		return this.diagFeeCode;
	}

	public void setDiagFeeCode(String diagFeeCode) {
		this.diagFeeCode = diagFeeCode;
	}

	public Double getDiagFee() {
		return this.diagFee;
	}

	public void setDiagFee(Double diagFee) {
		this.diagFee = diagFee;
	}

	public String getOthFeeCode() {
		return this.othFeeCode;
	}

	public void setOthFeeCode(String othFeeCode) {
		this.othFeeCode = othFeeCode;
	}

	public Double getOthFee() {
		return this.othFee;
	}

	public void setOthFee(Double othFee) {
		this.othFee = othFee;
	}

	public String getBookFeeCode() {
		return this.bookFeeCode;
	}

	public void setBookFeeCode(String bookFeeCode) {
		this.bookFeeCode = bookFeeCode;
	}

	public Double getBookFee() {
		return this.bookFee;
	}

	public void setBookFee(Double bookFee) {
		this.bookFee = bookFee;
	}

	public String getBookFlag() {
		return this.bookFlag;
	}

	public void setBookFlag(String bookFlag) {
		this.bookFlag = bookFlag;
	}

	public Double getEcoCost() {
		return this.ecoCost;
	}

	public void setEcoCost(Double ecoCost) {
		this.ecoCost = ecoCost;
	}

	public Double getOwnCost() {
		return this.ownCost;
	}

	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}

	public Double getPubCost() {
		return this.pubCost;
	}

	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}

	public Double getPayCost() {
		return this.payCost;
	}

	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}

	public Double getSumCost() {
		return this.sumCost;
	}

	public void setSumCost(Double sumCost) {
		this.sumCost = sumCost;
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

	public String getCancelOpcd() {
		return this.cancelOpcd;
	}

	public void setCancelOpcd(String cancelOpcd) {
		this.cancelOpcd = cancelOpcd;
	}

	public Date getCancelDate() {
		return this.cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
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

	public String getYnsee() {
		return this.ynsee;
	}

	public void setYnsee(String ynsee) {
		this.ynsee = ynsee;
	}

	public Date getSeeDate() {
		return this.seeDate;
	}

	public void setSeeDate(Date seeDate) {
		this.seeDate = seeDate;
	}

	public String getTriageFlag() {
		return this.triageFlag;
	}

	public void setTriageFlag(String triageFlag) {
		this.triageFlag = triageFlag;
	}

	public String getTriageOpcd() {
		return this.triageOpcd;
	}

	public void setTriageOpcd(String triageOpcd) {
		this.triageOpcd = triageOpcd;
	}

	public Date getTriageDate() {
		return this.triageDate;
	}

	public void setTriageDate(Date triageDate) {
		this.triageDate = triageDate;
	}

	public Byte getPrintInvoicecnt() {
		return this.printInvoicecnt;
	}

	public void setPrintInvoicecnt(Byte printInvoicecnt) {
		this.printInvoicecnt = printInvoicecnt;
	}

	public String getSeeDpcd() {
		return this.seeDpcd;
	}

	public void setSeeDpcd(String seeDpcd) {
		this.seeDpcd = seeDpcd;
	}

	public String getSeeDocd() {
		return this.seeDocd;
	}

	public void setSeeDocd(String seeDocd) {
		this.seeDocd = seeDocd;
	}

	public String getInSource() {
		return this.inSource;
	}

	public void setInSource(String inSource) {
		this.inSource = inSource;
	}

	public String getInState() {
		return this.inState;
	}

	public void setInState(String inState) {
		this.inState = inState;
	}

	public String getIsAccount() {
		return this.isAccount;
	}

	public void setIsAccount(String isAccount) {
		this.isAccount = isAccount;
	}

	public String getOperseq() {
		return this.operseq;
	}

	public void setOperseq(String operseq) {
		this.operseq = operseq;
	}

	public Date getInDate() {
		return this.inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public Date getOutDate() {
		return this.outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public String getZgCode() {
		return this.zgCode;
	}

	public void setZgCode(String zgCode) {
		this.zgCode = zgCode;
	}

	public String getZgName() {
		return this.zgName;
	}

	public void setZgName(String zgName) {
		this.zgName = zgName;
	}

	public String getSiCard() {
		return this.siCard;
	}

	public void setSiCard(String siCard) {
		this.siCard = siCard;
	}

	public String getSiNo() {
		return this.siNo;
	}

	public void setSiNo(String siNo) {
		this.siNo = siNo;
	}

	public String getBacknumberReason() {
		return this.backnumberReason;
	}

	public void setBacknumberReason(String backnumberReason) {
		this.backnumberReason = backnumberReason;
	}

	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getUpFlag() {
		return this.upFlag;
	}

	public void setUpFlag(String upFlag) {
		this.upFlag = upFlag;
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