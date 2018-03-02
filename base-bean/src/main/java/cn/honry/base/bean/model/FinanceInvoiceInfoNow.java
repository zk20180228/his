package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 结算信息表
 */

public class FinanceInvoiceInfoNow extends Entity {

	/**发票号 **/
	private String invoiceNo;
	/**交易类型,1正，2反 **/
	private Integer transType;
	/**病历卡号 **/
	private String cardNo;
	/**挂号日期 **/
	private Date regDate;
	/**患者姓名 **/
	private String name;
	/**结算类别代码 **/
	private String paykindCode;
	/**合同单位代码 **/
	private String pactCode;
	/**合同单位名称 **/
	private String pactName;
	/**个人编号 **/
	private String mcardNo;
	/**医疗类别 **/
	private Integer medicalType;
	/**总额 **/
	private Double totCost;
	/**可报效金额 **/
	private Double pubCost;
	/**不可报效金额 **/
	private Double ownCost;
	/**自付金额 **/
	private Double payCost;
	/**预留1 **/
	private Double back1;
	/**预留2 **/
	private Double back2;
	/**预留3 **/
	private Double back3;
	/**实付金额 **/
	private Double realCost;
	/**结算人 **/
	private String operCode;
	/**结算时间 **/
	private Date operDate;
	/**0不是体检/1个人体检/2团体体检 **/
	private Integer examineFlag;
	/**0 退费 "1" 有效 "2" 重打 "3" 注销 **/
	private Integer cancelFlag;
	/**作废票据号 **/
	private String cancelInvoice;
	/**作废操作员 **/
	private String cancelCode;
	/**作废时间 **/
	private Date cancelDate;
	/**0未核查/1已核查 **/
	private Integer checkFlag;
	/**核查人 **/
	private String checkOpcd;
	/**核查时间 **/
	private Date checkDate;
	/**0未日结/1已日结 **/
	private Integer balanceFlag=0;
	/**日结标识号 **/
	private String balanceNo;
	/**日结人 **/
	private String balanceOpcd;
	/**日结时间 **/
	private Date balanceDate;
	/**发票序号，一次结算产生多张发票的combNo **/
	private String invoiceSeq;
	/**扩展标志 1 自费 2 记帐 3 特殊 **/
	private Integer extFlag;
	/**挂号流水号 **/
	private String clinicCode;
	/**实际发票打印号码 **/
	private String printInvoiceno;
	/**本张发票发药窗口信息 **/
	private String drugWindow;
	/**是否账户集中打印发票 **/
	private Integer accountFlag;
	/**一次收费序号 **/
	private String invoiceComb;
	//新增字段  2016-11-15 11:26:15
	/**结算类别名称*/
	private String paykindName;
	/**结算人姓名*/
	private String operName;
	/**作废操作员姓名*/
	private String cancleName;
	/**核查人姓名*/
	private String checkOpcdName;
	/**日结人姓名*/
	private String balanceOpceName;
	/**所属医院**/
	private Integer hospitalId;
	/**所属院区**/
	private String areaCode;
	
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
	public String getPaykindName() {
		return paykindName;
	}
	public void setPaykindName(String paykindName) {
		this.paykindName = paykindName;
	}
	public String getOperName() {
		return operName;
	}
	public void setOperName(String operName) {
		this.operName = operName;
	}
	public String getCancleName() {
		return cancleName;
	}
	public void setCancleName(String cancleName) {
		this.cancleName = cancleName;
	}
	public String getCheckOpcdName() {
		return checkOpcdName;
	}
	public void setCheckOpcdName(String checkOpcdName) {
		this.checkOpcdName = checkOpcdName;
	}
	public String getBalanceOpceName() {
		return balanceOpceName;
	}
	public void setBalanceOpceName(String balanceOpceName) {
		this.balanceOpceName = balanceOpceName;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPaykindCode() {
		return paykindCode;
	}
	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	}
	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	public String getPactName() {
		return pactName;
	}
	public void setPactName(String pactName) {
		this.pactName = pactName;
	}
	public String getMcardNo() {
		return mcardNo;
	}
	public void setMcardNo(String mcardNo) {
		this.mcardNo = mcardNo;
	}
	public Integer getMedicalType() {
		return medicalType;
	}
	public void setMedicalType(Integer medicalType) {
		this.medicalType = medicalType;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public Double getPubCost() {
		return pubCost;
	}
	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}
	public Double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}
	public Double getBack1() {
		return back1;
	}
	public void setBack1(Double back1) {
		this.back1 = back1;
	}
	public Double getBack2() {
		return back2;
	}
	public void setBack2(Double back2) {
		this.back2 = back2;
	}
	public Double getBack3() {
		return back3;
	}
	public void setBack3(Double back3) {
		this.back3 = back3;
	}
	public Double getRealCost() {
		return realCost;
	}
	public void setRealCost(Double realCost) {
		this.realCost = realCost;
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
	public Integer getExamineFlag() {
		return examineFlag;
	}
	public void setExamineFlag(Integer examineFlag) {
		this.examineFlag = examineFlag;
	}
	public Integer getCancelFlag() {
		return cancelFlag;
	}
	public void setCancelFlag(Integer cancelFlag) {
		this.cancelFlag = cancelFlag;
	}
	public String getCancelInvoice() {
		return cancelInvoice;
	}
	public void setCancelInvoice(String cancelInvoice) {
		this.cancelInvoice = cancelInvoice;
	}
	public String getCancelCode() {
		return cancelCode;
	}
	public void setCancelCode(String cancelCode) {
		this.cancelCode = cancelCode;
	}
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	public Integer getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(Integer checkFlag) {
		this.checkFlag = checkFlag;
	}
	public String getCheckOpcd() {
		return checkOpcd;
	}
	public void setCheckOpcd(String checkOpcd) {
		this.checkOpcd = checkOpcd;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public Integer getBalanceFlag() {
		return balanceFlag;
	}
	public void setBalanceFlag(Integer balanceFlag) {
		this.balanceFlag = balanceFlag;
	}
	public String getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}
	public String getBalanceOpcd() {
		return balanceOpcd;
	}
	public void setBalanceOpcd(String balanceOpcd) {
		this.balanceOpcd = balanceOpcd;
	}
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public String getInvoiceSeq() {
		return invoiceSeq;
	}
	public void setInvoiceSeq(String invoiceSeq) {
		this.invoiceSeq = invoiceSeq;
	}
	public Integer getExtFlag() {
		return extFlag;
	}
	public void setExtFlag(Integer extFlag) {
		this.extFlag = extFlag;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public String getPrintInvoiceno() {
		return printInvoiceno;
	}
	public void setPrintInvoiceno(String printInvoiceno) {
		this.printInvoiceno = printInvoiceno;
	}
	public String getDrugWindow() {
		return drugWindow;
	}
	public void setDrugWindow(String drugWindow) {
		this.drugWindow = drugWindow;
	}
	public Integer getAccountFlag() {
		return accountFlag;
	}
	public void setAccountFlag(Integer accountFlag) {
		this.accountFlag = accountFlag;
	}
	public String getInvoiceComb() {
		return invoiceComb;
	}
	public void setInvoiceComb(String invoiceComb) {
		this.invoiceComb = invoiceComb;
	}

	

}