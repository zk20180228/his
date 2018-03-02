package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class Registration extends Entity{

	/**
	 * 门诊挂号（新） 2016-07-07
	 */
	private static final long serialVersionUID = 1L;
	
	/**门诊号**/
	private String clinicCode;
	/**交易类型,1正交易，2反交易**/
	private Integer transType;
	/**就诊卡ID**/
	private String cardId;
	/**就诊卡号**/
	private String cardNo;
	/**挂号日期**/
	private Date regDate;
	/**午别**/
	private Integer noonCode;
	/**姓名**/
	private String patientName;
	/**身份证号**/
	private String patientIdenno;
	/**性别**/
	private Integer patientSex;
	/**出生日**/
	private Date patientBirthday;
	/**患者年龄**/
	private String patientAge;
	/**年龄单位**/
	private String patientAgeunit;
	/**联系电话**/
	private String relaPhone;
	/**地址**/
	private String address;
	/**证件类型**/
	private String cardType;
	/**结算类别号**/
	private String paykindCode;
	/**结算类别名称**/
	private String paykindName;
	/**合同号**/
	private String pactCode;
	/**合同单位名称**/
	private String pactName;
	/**医疗类别**/
	private String medicalType;
	/**医疗证号**/
	private String mcardNo;
	/**挂号级别**/
	private String reglevlCode;
	/**挂号级别名称**/
	private String reglevlName;
	/**科室号**/
	private String deptCode;
	/**科室名称**/
	private String deptName;
	/**排班序号**/
	private String schemaNo;
	/**每日顺序号**/
	private Integer orderNo;
	/**看诊序号**/
	private Integer seeno;
	/**看诊开始时间**/
	private Date beginTime;
	/**看诊结束时间**/
	private Date endTime;
	/**医师代号**/
	private String doctCode;
	/**医师姓名**/
	private String doctName;
	/**挂号收费标志 1是/0否**/
	private Integer ynregchrg=1;
	/**发票号**/
	private String invoiceNo;
	/**发票打印标记  0-未打印，1-打印，2-补打**/
	private Integer invoicePrintFlag;
	/**处方号**/
	private String recipeNo;
	/**1现场挂号/2预约挂号/3特诊挂号**/
	private String ynbook;
	/**1初诊/0复诊**/
	private Integer ynfr=1;
	/**1加号/0正常**/
	private Integer appendFlag=0;
	/**挂号费CODE**/
	private String regFeeCode;
	/**挂号费**/
	private Double regFee;
	/**检查费CODE**/
	private String chckFeeCode;
	/**检查费**/
	private Double chckFee;
	/**诊察费CODE**/
	private String diagFeeCode;
	/**诊察费**/
	private Double diagFee;
	/**附加费CODE**/
	private String othFeeCode;
	/**附加费**/
	private Double othFee;
	/**病历本费CODE**/
	private String bookFeeCode;
	/**病历本费**/
	private Double bookFee;
	/**是否购买病历本1购买/2不购买**/
	private Integer bookFlag;
	/**优惠金额**/
	private Double ecoCost=0.0;
	/**自费金额**/
	private Double ownCost=0.0;
	/**报销金额**/
	private Double pubCost=0.0;
	/**自付金额**/
	private Double payCost;
	/**总金额(未保销未优惠前的总金额)**/
	private Double sumCost;
	/**0退费,1有效,2作废**/
	private Integer validFlag=1;
	/**操作员代码**/
	private String operCode;
	/**操作时间**/
	private Date operDate;
	/**作废人**/
	private String cancelOpcd;
	/**作废时间**/
	private Date cancelDate;
	/**疾病代码**/
	private String icdCode;
	/**疾病名称**/
	private String icdName;
	/**审批人**/
	private String examCode;
	/**审批时间**/
	private Date examDate;
	/**0未核查/1已核查**/
	private Integer checkFlag;
	/**核查人**/
	private String checkOpcd;
	/**核查时间**/
	private Date checkDate;
	/**1已日结/0未日结**/
	private Integer balanceFlag=0;
	/**日结标识号**/
	private String balanceNo;
	/**日结人**/
	private String balanceOpcd;
	/**日结时间**/
	private Date balanceDate;
	/**是否看诊 1是/0否**/
	private Integer ynsee=0;
	/**看诊日期**/
	private Date seeDate;
	/**分诊标志,0未分/1已分**/
	private Integer triageFlag = 0;
	/**分诊护士代码**/
	private String triageOpcd;
	/**分诊时间**/
	private Date triageDate;
	/**打印发票数量**/
	private Integer printInvoicecnt;
	/**看诊科室代码**/
	private String seeDpcd;
	/**看诊医生代码**/
	private String seeDocd;
	/**患者来源**/
	private Integer inSource;
	/**状态**/
	private Integer inState=0;
	/**账户流程标识1 账户挂号 0普通**/
	private Integer isAccount;
	/**收款员每日顺序号**/
	private String operseq;
	/**医保卡号**/
	private String siCard;
	/**医保门诊号**/
	private String siNo;
	/**退号原因**/
	private String backnumberReason;
	/**账户患者消费账号**/
	private String accountNo;
	/**上传标记 0-未上传 1-上传**/
	private String upFlag;
	/**建立医院**/
	private String createhos;
	/**医疗类别code**/
	private String medicalTypeCode;
	/**病历号**/
	private String midicalrecordId;
	/**优先级序号**/
	private Integer seeOptimize;
	/**分诊类别**/
	private String triageType;
	/**午别**/
	private String noonCodeNmae;
	/**性别**/
	private String patientSexName;
	
	//加号人数(虚拟字段)
	private Integer infoAdd;
	//支付方式
	private String payType;
	
	/**
	 * 查询用no
	 */
	private String queryNo;
	
	/**
	 * 级别名称
	 */
	private String gradeName;
	
	/**
	 * 合同单位名称
	 */
	private String contractunitName;
	
	/**
	 * 就诊诊室
	 */
	private String clinicName;
	/**医院编码*/
	private Integer hospitalId;
	/**院区编码*/
	private String areaCode;
	
	public String getTriageType() {
		return triageType;
	}
	public void setTriageType(String triageType) {
		this.triageType = triageType;
	}
	public String getMidicalrecordId() {
		return midicalrecordId;
	}
	public void setMidicalrecordId(String midicalrecordId) {
		this.midicalrecordId = midicalrecordId;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
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
	public Integer getNoonCode() {
		return noonCode;
	}
	public void setNoonCode(Integer noonCode) {
		this.noonCode = noonCode;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPatientIdenno() {
		return patientIdenno;
	}
	public void setPatientIdenno(String patientIdenno) {
		this.patientIdenno = patientIdenno;
	}
	public Integer getPatientSex() {
		return patientSex;
	}
	public void setPatientSex(Integer patientSex) {
		this.patientSex = patientSex;
	}
	public Date getPatientBirthday() {
		return patientBirthday;
	}
	public void setPatientBirthday(Date patientBirthday) {
		this.patientBirthday = patientBirthday;
	}
	public String getPatientAge() {
		return patientAge;
	}
	public void setPatientAge(String patientAge) {
		this.patientAge = patientAge;
	}
	public String getPatientAgeunit() {
		return patientAgeunit;
	}
	public void setPatientAgeunit(String patientAgeunit) {
		this.patientAgeunit = patientAgeunit;
	}
	public String getRelaPhone() {
		return relaPhone;
	}
	public void setRelaPhone(String relaPhone) {
		this.relaPhone = relaPhone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getPaykindCode() {
		return paykindCode;
	}
	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	}
	public String getPaykindName() {
		return paykindName;
	}
	public void setPaykindName(String paykindName) {
		this.paykindName = paykindName;
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
	public String getMedicalType() {
		return medicalType;
	}
	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}
	public String getMcardNo() {
		return mcardNo;
	}
	public void setMcardNo(String mcardNo) {
		this.mcardNo = mcardNo;
	}
	public String getReglevlCode() {
		return reglevlCode;
	}
	public void setReglevlCode(String reglevlCode) {
		this.reglevlCode = reglevlCode;
	}
	public String getReglevlName() {
		return reglevlName;
	}
	public void setReglevlName(String reglevlName) {
		this.reglevlName = reglevlName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getSchemaNo() {
		return schemaNo;
	}
	public void setSchemaNo(String schemaNo) {
		this.schemaNo = schemaNo;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getSeeno() {
		return seeno;
	}
	public void setSeeno(Integer seeno) {
		this.seeno = seeno;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getDoctCode() {
		return doctCode;
	}
	public void setDoctCode(String doctCode) {
		this.doctCode = doctCode;
	}
	public String getDoctName() {
		return doctName;
	}
	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}
	public Integer getYnregchrg() {
		return ynregchrg;
	}
	public void setYnregchrg(Integer ynregchrg) {
		this.ynregchrg = ynregchrg;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Integer getInvoicePrintFlag() {
		return invoicePrintFlag;
	}
	public void setInvoicePrintFlag(Integer invoicePrintFlag) {
		this.invoicePrintFlag = invoicePrintFlag;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	
	public String getYnbook() {
		return ynbook;
	}
	public void setYnbook(String ynbook) {
		this.ynbook = ynbook;
	}
	public Integer getYnfr() {
		return ynfr;
	}
	public void setYnfr(Integer ynfr) {
		this.ynfr = ynfr;
	}
	public Integer getAppendFlag() {
		return appendFlag;
	}
	public void setAppendFlag(Integer appendFlag) {
		this.appendFlag = appendFlag;
	}
	public String getRegFeeCode() {
		return regFeeCode;
	}
	public void setRegFeeCode(String regFeeCode) {
		this.regFeeCode = regFeeCode;
	}
	public Double getRegFee() {
		return regFee;
	}
	public void setRegFee(Double regFee) {
		this.regFee = regFee;
	}
	public String getChckFeeCode() {
		return chckFeeCode;
	}
	public void setChckFeeCode(String chckFeeCode) {
		this.chckFeeCode = chckFeeCode;
	}
	public Double getChckFee() {
		return chckFee;
	}
	public void setChckFee(Double chckFee) {
		this.chckFee = chckFee;
	}
	public String getDiagFeeCode() {
		return diagFeeCode;
	}
	public void setDiagFeeCode(String diagFeeCode) {
		this.diagFeeCode = diagFeeCode;
	}
	public Double getDiagFee() {
		return diagFee;
	}
	public void setDiagFee(Double diagFee) {
		this.diagFee = diagFee;
	}
	public String getOthFeeCode() {
		return othFeeCode;
	}
	public void setOthFeeCode(String othFeeCode) {
		this.othFeeCode = othFeeCode;
	}
	public Double getOthFee() {
		return othFee;
	}
	public void setOthFee(Double othFee) {
		this.othFee = othFee;
	}
	public String getBookFeeCode() {
		return bookFeeCode;
	}
	public void setBookFeeCode(String bookFeeCode) {
		this.bookFeeCode = bookFeeCode;
	}
	public Double getBookFee() {
		return bookFee;
	}
	public void setBookFee(Double bookFee) {
		this.bookFee = bookFee;
	}
	public Integer getBookFlag() {
		return bookFlag;
	}
	public void setBookFlag(Integer bookFlag) {
		this.bookFlag = bookFlag;
	}
	public Double getEcoCost() {
		return ecoCost;
	}
	public void setEcoCost(Double ecoCost) {
		this.ecoCost = ecoCost;
	}
	public Double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}
	public Double getPubCost() {
		return pubCost;
	}
	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}
	public Double getSumCost() {
		return sumCost;
	}
	public void setSumCost(Double sumCost) {
		this.sumCost = sumCost;
	}
	public Integer getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
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
	public String getCancelOpcd() {
		return cancelOpcd;
	}
	public void setCancelOpcd(String cancelOpcd) {
		this.cancelOpcd = cancelOpcd;
	}
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	public String getIcdCode() {
		return icdCode;
	}
	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}
	public String getIcdName() {
		return icdName;
	}
	public void setIcdName(String icdName) {
		this.icdName = icdName;
	}
	public String getExamCode() {
		return examCode;
	}
	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}
	public Date getExamDate() {
		return examDate;
	}
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
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
	public Integer getYnsee() {
		return ynsee;
	}
	public void setYnsee(Integer ynsee) {
		this.ynsee = ynsee;
	}
	public Date getSeeDate() {
		return seeDate;
	}
	public void setSeeDate(Date seeDate) {
		this.seeDate = seeDate;
	}
	public Integer getTriageFlag() {
		return triageFlag;
	}
	public void setTriageFlag(Integer triageFlag) {
		this.triageFlag = triageFlag;
	}
	public String getTriageOpcd() {
		return triageOpcd;
	}
	public void setTriageOpcd(String triageOpcd) {
		this.triageOpcd = triageOpcd;
	}
	public Date getTriageDate() {
		return triageDate;
	}
	public void setTriageDate(Date triageDate) {
		this.triageDate = triageDate;
	}
	public Integer getPrintInvoicecnt() {
		return printInvoicecnt;
	}
	public void setPrintInvoicecnt(Integer printInvoicecnt) {
		this.printInvoicecnt = printInvoicecnt;
	}
	public String getSeeDpcd() {
		return seeDpcd;
	}
	public void setSeeDpcd(String seeDpcd) {
		this.seeDpcd = seeDpcd;
	}
	public String getSeeDocd() {
		return seeDocd;
	}
	public void setSeeDocd(String seeDocd) {
		this.seeDocd = seeDocd;
	}
	public Integer getInSource() {
		return inSource;
	}
	public void setInSource(Integer inSource) {
		this.inSource = inSource;
	}
	public Integer getInState() {
		return inState;
	}
	public void setInState(Integer inState) {
		this.inState = inState;
	}
	public Integer getIsAccount() {
		return isAccount;
	}
	public void setIsAccount(Integer isAccount) {
		this.isAccount = isAccount;
	}
	public String getOperseq() {
		return operseq;
	}
	public void setOperseq(String operseq) {
		this.operseq = operseq;
	}
	public String getSiCard() {
		return siCard;
	}
	public void setSiCard(String siCard) {
		this.siCard = siCard;
	}
	public String getSiNo() {
		return siNo;
	}
	public void setSiNo(String siNo) {
		this.siNo = siNo;
	}
	public String getBacknumberReason() {
		return backnumberReason;
	}
	public void setBacknumberReason(String backnumberReason) {
		this.backnumberReason = backnumberReason;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getUpFlag() {
		return upFlag;
	}
	public void setUpFlag(String upFlag) {
		this.upFlag = upFlag;
	}
	public String getCreatehos() {
		return createhos;
	}
	public void setCreatehos(String createhos) {
		this.createhos = createhos;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getMedicalTypeCode() {
		return medicalTypeCode;
	}
	public void setMedicalTypeCode(String medicalTypeCode) {
		this.medicalTypeCode = medicalTypeCode;
	}
	public Integer getInfoAdd() {
		return infoAdd;
	}
	public void setInfoAdd(Integer infoAdd) {
		this.infoAdd = infoAdd;
	}
	public Integer getSeeOptimize() {
		return seeOptimize;
	}
	public void setSeeOptimize(Integer seeOptimize) {
		this.seeOptimize = seeOptimize;
	}
	public String getQueryNo() {
		return queryNo;
	}
	public void setQueryNo(String queryNo) {
		this.queryNo = queryNo;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getContractunitName() {
		return contractunitName;
	}
	public void setContractunitName(String contractunitName) {
		this.contractunitName = contractunitName;
	}
	public String getClinicName() {
		return clinicName;
	}
	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}
	public String getNoonCodeNmae() {
		return noonCodeNmae;
	}
	public void setNoonCodeNmae(String noonCodeNmae) {
		this.noonCodeNmae = noonCodeNmae;
	}
	public String getPatientSexName() {
		return patientSexName;
	}
	public void setPatientSexName(String patientSexName) {
		this.patientSexName = patientSexName;
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
