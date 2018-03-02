package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiOptFeedetail entity. @author MyEclipse Persistence Tools
 */

public class BiOptFeedetail implements java.io.Serializable {

	// Fields

	private String orderSd;
	private String recipeSeq;
	private String recipeNo;
	private Short sequenceNo;
	private String transType;
	private String clinicCode;
	private String cardNo;
	private Date regDate;
	private String regDpcd;
	private String regDept;
	private String doctCode;
	private String doctName;
	private String doctDpcd;
	private String doctDept;
	private String itemCode;
	private String itemName;
	private String drugFlag;
	private String specs;
	private String selfMade;
	private String drugQuanlityCode;
	private String drugQuality;
	private String doseModelCode;
	private String doseModel;
	private String classCode;
	private String className;
	private Double unitPrice;
	private Double qty;
	private Short days;
	private String frequencyCode;
	private String frequencyName;
	private String usageCode;
	private String useName;
	private Byte injectNumber;
	private String emcFlag;
	private String labTypeCode;
	private String labType;
	private String checkBodyCode;
	private String checkBody;
	private Double doseOnce;
	private String onceUnitCode;
	private String doseUnit;
	private String miniFeeCode;
	private String miniFeeName;
	private Double baseDose;
	private Short packQty;
	private String priceUnitCode;
	private String priceUnit;
	private Double pubCost;
	private Double payCost;
	private Double ownCost;
	private Double ecoCost;
	private Double totCost;
	private Double overCost;
	private Double excessCost;
	private Double drugOwncost;
	private Boolean costSource;
	private String execDpcd;
	private String execDpnm;
	private String centerCode;
	private String itemGrade;
	private String mainDrug;
	private String combNo;
	private String operCode;
	private Date operDate;
	private String payFlag;
	private String cancelFlag;
	private String feeCpcd;
	private Date feeDate;
	private String invoiceSeq;
	private String invoiceNo;
	private String invoCode;
	private String invoSequence;
	private String confirmFlag;
	private String confirmCode;
	private String confirmDept;
	private Date confirmDate;
	private String confirmDpcd;
	private Double newItemrate;
	private Double oldItemrate;
	private String minunitFlag;
	private String checkupFlag;
	private String specialitemFlag;
	private String dailyBalanceFlag;
	private String packageCode;
	private String packageName;
	private Double nobackNum;
	private Double confirmNum;
	private Byte confirmInject;
	private String subjobFlag;
	private String accountFlag;
	private String updateSequenceno;
	private String accountNo;
	private String useFlag;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private Double idKey;
	private Double dateKey;
	private Double employeeKey;
	private Double orgKey;
	private Double patientKey;
	private String id;
	private Date openAdviceDate;
	private String age;
	private String ageUnit;
	// Constructors

	/** default constructor */
	public BiOptFeedetail() {
	}

	/** full constructor */
	public BiOptFeedetail(String recipeSeq, String recipeNo, Short sequenceNo,
			String transType, String clinicCode, String cardNo, Date regDate,
			String regDpcd, String regDept, String doctCode, String doctName,
			String doctDpcd, String doctDept, String itemCode, String itemName,
			String drugFlag, String specs, String selfMade,
			String drugQuanlityCode, String drugQuality, String doseModelCode,
			String doseModel, String classCode, String className,
			Double unitPrice, Double qty, Short days, String frequencyCode,
			String frequencyName, String usageCode, String useName,
			Byte injectNumber, String emcFlag, String labTypeCode,
			String labType, String checkBodyCode, String checkBody,
			Double doseOnce, String onceUnitCode, String doseUnit,
			String miniFeeCode, String miniFeeName, Double baseDose,
			Short packQty, String priceUnitCode, String priceUnit,
			Double pubCost, Double payCost, Double ownCost, Double ecoCost,
			Double totCost, Double overCost, Double excessCost,
			Double drugOwncost, Boolean costSource, String execDpcd,
			String execDpnm, String centerCode, String itemGrade,
			String mainDrug, String combNo, String operCode, Date operDate,
			String payFlag, String cancelFlag, String feeCpcd, Date feeDate,
			String invoiceSeq, String invoiceNo, String invoCode,
			String invoSequence, String confirmFlag, String confirmCode,
			String confirmDept, Date confirmDate, String confirmDpcd,
			Double newItemrate, Double oldItemrate, String minunitFlag,
			String checkupFlag, String specialitemFlag,
			String dailyBalanceFlag, String packageCode, String packageName,
			Double nobackNum, Double confirmNum, Byte confirmInject,
			String subjobFlag, String accountFlag, String updateSequenceno,
			String accountNo, String useFlag, String ext1, String ext2,
			String ext3, String ext4,Double idKey,Double dateKey,
			Double employeeKey,Double orgKey,Double patientKey,String id,Date openAdviceDate,String age,String ageUnit) {
		this.recipeSeq = recipeSeq;
		this.recipeNo = recipeNo;
		this.sequenceNo = sequenceNo;
		this.transType = transType;
		this.clinicCode = clinicCode;
		this.cardNo = cardNo;
		this.regDate = regDate;
		this.regDpcd = regDpcd;
		this.regDept = regDept;
		this.doctCode = doctCode;
		this.doctName = doctName;
		this.doctDpcd = doctDpcd;
		this.doctDept = doctDept;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.drugFlag = drugFlag;
		this.specs = specs;
		this.selfMade = selfMade;
		this.drugQuanlityCode = drugQuanlityCode;
		this.drugQuality = drugQuality;
		this.doseModelCode = doseModelCode;
		this.doseModel = doseModel;
		this.classCode = classCode;
		this.className = className;
		this.unitPrice = unitPrice;
		this.qty = qty;
		this.days = days;
		this.frequencyCode = frequencyCode;
		this.frequencyName = frequencyName;
		this.usageCode = usageCode;
		this.useName = useName;
		this.injectNumber = injectNumber;
		this.emcFlag = emcFlag;
		this.labTypeCode = labTypeCode;
		this.labType = labType;
		this.checkBodyCode = checkBodyCode;
		this.checkBody = checkBody;
		this.doseOnce = doseOnce;
		this.onceUnitCode = onceUnitCode;
		this.doseUnit = doseUnit;
		this.miniFeeCode = miniFeeCode;
		this.miniFeeName = miniFeeName;
		this.baseDose = baseDose;
		this.packQty = packQty;
		this.priceUnitCode = priceUnitCode;
		this.priceUnit = priceUnit;
		this.pubCost = pubCost;
		this.payCost = payCost;
		this.ownCost = ownCost;
		this.ecoCost = ecoCost;
		this.totCost = totCost;
		this.overCost = overCost;
		this.excessCost = excessCost;
		this.drugOwncost = drugOwncost;
		this.costSource = costSource;
		this.execDpcd = execDpcd;
		this.execDpnm = execDpnm;
		this.centerCode = centerCode;
		this.itemGrade = itemGrade;
		this.mainDrug = mainDrug;
		this.combNo = combNo;
		this.operCode = operCode;
		this.operDate = operDate;
		this.payFlag = payFlag;
		this.cancelFlag = cancelFlag;
		this.feeCpcd = feeCpcd;
		this.feeDate = feeDate;
		this.invoiceSeq = invoiceSeq;
		this.invoiceNo = invoiceNo;
		this.invoCode = invoCode;
		this.invoSequence = invoSequence;
		this.confirmFlag = confirmFlag;
		this.confirmCode = confirmCode;
		this.confirmDept = confirmDept;
		this.confirmDate = confirmDate;
		this.confirmDpcd = confirmDpcd;
		this.newItemrate = newItemrate;
		this.oldItemrate = oldItemrate;
		this.minunitFlag = minunitFlag;
		this.checkupFlag = checkupFlag;
		this.specialitemFlag = specialitemFlag;
		this.dailyBalanceFlag = dailyBalanceFlag;
		this.packageCode = packageCode;
		this.packageName = packageName;
		this.nobackNum = nobackNum;
		this.confirmNum = confirmNum;
		this.confirmInject = confirmInject;
		this.subjobFlag = subjobFlag;
		this.accountFlag = accountFlag;
		this.updateSequenceno = updateSequenceno;
		this.accountNo = accountNo;
		this.useFlag = useFlag;
		this.ext1 = ext1;
		this.ext2 = ext2;
		this.ext3 = ext3;
		this.ext4 = ext4;
		this.idKey = idKey;
		this.dateKey = dateKey;
		this.employeeKey = employeeKey;
		this.orgKey = orgKey;
		this.patientKey = patientKey;
		this.id = id;
		this.openAdviceDate = openAdviceDate;
		this.age = age;
		this.ageUnit = ageUnit;
	}

	// Property accessors

	public String getOrderSd() {
		return this.orderSd;
	}

	public void setOrderSd(String orderSd) {
		this.orderSd = orderSd;
	}

	public String getRecipeSeq() {
		return this.recipeSeq;
	}

	public void setRecipeSeq(String recipeSeq) {
		this.recipeSeq = recipeSeq;
	}

	public String getRecipeNo() {
		return this.recipeNo;
	}

	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}

	public Short getSequenceNo() {
		return this.sequenceNo;
	}

	public void setSequenceNo(Short sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getTransType() {
		return this.transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getClinicCode() {
		return this.clinicCode;
	}

	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
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

	public String getRegDpcd() {
		return this.regDpcd;
	}

	public void setRegDpcd(String regDpcd) {
		this.regDpcd = regDpcd;
	}

	public String getRegDept() {
		return this.regDept;
	}

	public void setRegDept(String regDept) {
		this.regDept = regDept;
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

	public String getDoctDpcd() {
		return this.doctDpcd;
	}

	public void setDoctDpcd(String doctDpcd) {
		this.doctDpcd = doctDpcd;
	}

	public String getDoctDept() {
		return this.doctDept;
	}

	public void setDoctDept(String doctDept) {
		this.doctDept = doctDept;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getDrugFlag() {
		return this.drugFlag;
	}

	public void setDrugFlag(String drugFlag) {
		this.drugFlag = drugFlag;
	}

	public String getSpecs() {
		return this.specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public String getSelfMade() {
		return this.selfMade;
	}

	public void setSelfMade(String selfMade) {
		this.selfMade = selfMade;
	}

	public String getDrugQuanlityCode() {
		return this.drugQuanlityCode;
	}

	public void setDrugQuanlityCode(String drugQuanlityCode) {
		this.drugQuanlityCode = drugQuanlityCode;
	}

	public String getDrugQuality() {
		return this.drugQuality;
	}

	public void setDrugQuality(String drugQuality) {
		this.drugQuality = drugQuality;
	}

	public String getDoseModelCode() {
		return this.doseModelCode;
	}

	public void setDoseModelCode(String doseModelCode) {
		this.doseModelCode = doseModelCode;
	}

	public String getDoseModel() {
		return this.doseModel;
	}

	public void setDoseModel(String doseModel) {
		this.doseModel = doseModel;
	}

	public String getClassCode() {
		return this.classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getQty() {
		return this.qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public Short getDays() {
		return this.days;
	}

	public void setDays(Short days) {
		this.days = days;
	}

	public String getFrequencyCode() {
		return this.frequencyCode;
	}

	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}

	public String getFrequencyName() {
		return this.frequencyName;
	}

	public void setFrequencyName(String frequencyName) {
		this.frequencyName = frequencyName;
	}

	public String getUsageCode() {
		return this.usageCode;
	}

	public void setUsageCode(String usageCode) {
		this.usageCode = usageCode;
	}

	public String getUseName() {
		return this.useName;
	}

	public void setUseName(String useName) {
		this.useName = useName;
	}

	public Byte getInjectNumber() {
		return this.injectNumber;
	}

	public void setInjectNumber(Byte injectNumber) {
		this.injectNumber = injectNumber;
	}

	public String getEmcFlag() {
		return this.emcFlag;
	}

	public void setEmcFlag(String emcFlag) {
		this.emcFlag = emcFlag;
	}

	public String getLabTypeCode() {
		return this.labTypeCode;
	}

	public void setLabTypeCode(String labTypeCode) {
		this.labTypeCode = labTypeCode;
	}

	public String getLabType() {
		return this.labType;
	}

	public void setLabType(String labType) {
		this.labType = labType;
	}

	public String getCheckBodyCode() {
		return this.checkBodyCode;
	}

	public void setCheckBodyCode(String checkBodyCode) {
		this.checkBodyCode = checkBodyCode;
	}

	public String getCheckBody() {
		return this.checkBody;
	}

	public void setCheckBody(String checkBody) {
		this.checkBody = checkBody;
	}

	public Double getDoseOnce() {
		return this.doseOnce;
	}

	public void setDoseOnce(Double doseOnce) {
		this.doseOnce = doseOnce;
	}

	public String getOnceUnitCode() {
		return this.onceUnitCode;
	}

	public void setOnceUnitCode(String onceUnitCode) {
		this.onceUnitCode = onceUnitCode;
	}

	public String getDoseUnit() {
		return this.doseUnit;
	}

	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}

	public String getMiniFeeCode() {
		return this.miniFeeCode;
	}

	public void setMiniFeeCode(String miniFeeCode) {
		this.miniFeeCode = miniFeeCode;
	}

	public String getMiniFeeName() {
		return this.miniFeeName;
	}

	public void setMiniFeeName(String miniFeeName) {
		this.miniFeeName = miniFeeName;
	}

	public Double getBaseDose() {
		return this.baseDose;
	}

	public void setBaseDose(Double baseDose) {
		this.baseDose = baseDose;
	}

	public Short getPackQty() {
		return this.packQty;
	}

	public void setPackQty(Short packQty) {
		this.packQty = packQty;
	}

	public String getPriceUnitCode() {
		return this.priceUnitCode;
	}

	public void setPriceUnitCode(String priceUnitCode) {
		this.priceUnitCode = priceUnitCode;
	}

	public String getPriceUnit() {
		return this.priceUnit;
	}

	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
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

	public Double getOwnCost() {
		return this.ownCost;
	}

	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}

	public Double getEcoCost() {
		return this.ecoCost;
	}

	public void setEcoCost(Double ecoCost) {
		this.ecoCost = ecoCost;
	}

	public Double getTotCost() {
		return this.totCost;
	}

	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}

	public Double getOverCost() {
		return this.overCost;
	}

	public void setOverCost(Double overCost) {
		this.overCost = overCost;
	}

	public Double getExcessCost() {
		return this.excessCost;
	}

	public void setExcessCost(Double excessCost) {
		this.excessCost = excessCost;
	}

	public Double getDrugOwncost() {
		return this.drugOwncost;
	}

	public void setDrugOwncost(Double drugOwncost) {
		this.drugOwncost = drugOwncost;
	}

	public Boolean getCostSource() {
		return this.costSource;
	}

	public void setCostSource(Boolean costSource) {
		this.costSource = costSource;
	}

	public String getExecDpcd() {
		return this.execDpcd;
	}

	public void setExecDpcd(String execDpcd) {
		this.execDpcd = execDpcd;
	}

	public String getExecDpnm() {
		return this.execDpnm;
	}

	public void setExecDpnm(String execDpnm) {
		this.execDpnm = execDpnm;
	}

	public String getCenterCode() {
		return this.centerCode;
	}

	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}

	public String getItemGrade() {
		return this.itemGrade;
	}

	public void setItemGrade(String itemGrade) {
		this.itemGrade = itemGrade;
	}

	public String getMainDrug() {
		return this.mainDrug;
	}

	public void setMainDrug(String mainDrug) {
		this.mainDrug = mainDrug;
	}

	public String getCombNo() {
		return this.combNo;
	}

	public void setCombNo(String combNo) {
		this.combNo = combNo;
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

	public String getPayFlag() {
		return this.payFlag;
	}

	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}

	public String getCancelFlag() {
		return this.cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

	public String getFeeCpcd() {
		return this.feeCpcd;
	}

	public void setFeeCpcd(String feeCpcd) {
		this.feeCpcd = feeCpcd;
	}

	public Date getFeeDate() {
		return this.feeDate;
	}

	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}

	public String getInvoiceSeq() {
		return this.invoiceSeq;
	}

	public void setInvoiceSeq(String invoiceSeq) {
		this.invoiceSeq = invoiceSeq;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoCode() {
		return this.invoCode;
	}

	public void setInvoCode(String invoCode) {
		this.invoCode = invoCode;
	}

	public String getInvoSequence() {
		return this.invoSequence;
	}

	public void setInvoSequence(String invoSequence) {
		this.invoSequence = invoSequence;
	}

	public String getConfirmFlag() {
		return this.confirmFlag;
	}

	public void setConfirmFlag(String confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

	public String getConfirmCode() {
		return this.confirmCode;
	}

	public void setConfirmCode(String confirmCode) {
		this.confirmCode = confirmCode;
	}

	public String getConfirmDept() {
		return this.confirmDept;
	}

	public void setConfirmDept(String confirmDept) {
		this.confirmDept = confirmDept;
	}

	public Date getConfirmDate() {
		return this.confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getConfirmDpcd() {
		return this.confirmDpcd;
	}

	public void setConfirmDpcd(String confirmDpcd) {
		this.confirmDpcd = confirmDpcd;
	}

	public Double getNewItemrate() {
		return this.newItemrate;
	}

	public void setNewItemrate(Double newItemrate) {
		this.newItemrate = newItemrate;
	}

	public Double getOldItemrate() {
		return this.oldItemrate;
	}

	public void setOldItemrate(Double oldItemrate) {
		this.oldItemrate = oldItemrate;
	}

	public String getMinunitFlag() {
		return this.minunitFlag;
	}

	public void setMinunitFlag(String minunitFlag) {
		this.minunitFlag = minunitFlag;
	}

	public String getCheckupFlag() {
		return this.checkupFlag;
	}

	public void setCheckupFlag(String checkupFlag) {
		this.checkupFlag = checkupFlag;
	}

	public String getSpecialitemFlag() {
		return this.specialitemFlag;
	}

	public void setSpecialitemFlag(String specialitemFlag) {
		this.specialitemFlag = specialitemFlag;
	}

	public String getDailyBalanceFlag() {
		return this.dailyBalanceFlag;
	}

	public void setDailyBalanceFlag(String dailyBalanceFlag) {
		this.dailyBalanceFlag = dailyBalanceFlag;
	}

	public String getPackageCode() {
		return this.packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Double getNobackNum() {
		return this.nobackNum;
	}

	public void setNobackNum(Double nobackNum) {
		this.nobackNum = nobackNum;
	}

	public Double getConfirmNum() {
		return this.confirmNum;
	}

	public void setConfirmNum(Double confirmNum) {
		this.confirmNum = confirmNum;
	}

	public Byte getConfirmInject() {
		return this.confirmInject;
	}

	public void setConfirmInject(Byte confirmInject) {
		this.confirmInject = confirmInject;
	}

	public String getSubjobFlag() {
		return this.subjobFlag;
	}

	public void setSubjobFlag(String subjobFlag) {
		this.subjobFlag = subjobFlag;
	}

	public String getAccountFlag() {
		return this.accountFlag;
	}

	public void setAccountFlag(String accountFlag) {
		this.accountFlag = accountFlag;
	}

	public String getUpdateSequenceno() {
		return this.updateSequenceno;
	}

	public void setUpdateSequenceno(String updateSequenceno) {
		this.updateSequenceno = updateSequenceno;
	}

	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getUseFlag() {
		return this.useFlag;
	}

	public void setUseFlag(String useFlag) {
		this.useFlag = useFlag;
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

	public Double getIdKey() {
		return idKey;
	}

	public void setIdKey(Double idKey) {
		this.idKey = idKey;
	}

	public Double getDateKey() {
		return dateKey;
	}

	public void setDateKey(Double dateKey) {
		this.dateKey = dateKey;
	}

	public Double getEmployeeKey() {
		return employeeKey;
	}

	public void setEmployeeKey(Double employeeKey) {
		this.employeeKey = employeeKey;
	}

	public Double getOrgKey() {
		return orgKey;
	}

	public void setOrgKey(Double orgKey) {
		this.orgKey = orgKey;
	}

	public Double getPatientKey() {
		return patientKey;
	}

	public void setPatientKey(Double patientKey) {
		this.patientKey = patientKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getOpenAdviceDate() {
		return openAdviceDate;
	}

	public void setOpenAdviceDate(Date openAdviceDate) {
		this.openAdviceDate = openAdviceDate;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAgeUnit() {
		return ageUnit;
	}

	public void setAgeUnit(String ageUnit) {
		this.ageUnit = ageUnit;
	}

}