package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 处方明细表
 * TOutpatientFeedetail entity. @author MyEclipse Persistence Tools
 */

public class OutpatientFeedetailNow extends Entity {

	private static final long serialVersionUID = 1L;
	
	/**版本号**/
	private int version;
	/**处方号**/
	private String recipeNo;
	/**处方内项目流水号[4]**/
	private Integer sequenceNo;
	/**交易类型,1正交易，2反交易[5]**/
	private Integer transType;
	/**就诊卡号**/
	private String cardNo;
	/**挂号日期**/
	private Date regDate;
	/**开单科室[9]**/
	private String regDpcd;
	/**开方医师[10]**/
	private String doctCode;
	/**开方医师所在科室[11]**/
	private String doctDept;
	/**项目代码[12]**/
	private String itemCode;
	/**项目名称[13]**/
	private String itemName;
	/**1药品/0非药[14]**/
	private String drugFlag;
	/**规格[15]**/
	private String specs;
	/**自制药标志**/
	private Integer selfMade;
	/**药品性质**/
	private String drugQuality;
	/**剂型[18]**/
	private String doseModelCode;
	/**最小费用代码[19]**/
	private String feeCode;
	/**系统类别[20]**/
	private String classCode;
	/**单价[21]**/
	private Double unitPrice;
	/**数量[22]**/
	private Double qty;
	/**草药的付数，其他药品为1[23]**/
	private Integer days;
	/**频次代码[24]**/
	private String frequencyCode;
	/**用法代码[25]**/
	private String usageCode;
	/**用法名称[26]**/
	private String useName;
	/**院内注射次数[27]**/
	private Integer injectNumber;
	/**加急标记:1普通/2加急[28]**/
	private Integer emcFlag;
	/**样本类型[29]**/
	private String labType;
	/**检体[30]**/
	private String checkBody;
	/**每次用量[31]**/
	private Double doseOnce;
	/**每次用量单位[32]**/
	private String doseUnit;
	/**基本剂量[33]**/
	private Double baseDose;
	/**包装数量[34]**/
	private Integer packQty;
	/**计价单位[35]**/
	private String priceUnit;
	/**可报效金额[36]**/
	private Double pubCost;
	/**自付金额[37]**/
	private Double payCost;
	/**现金金额[38]**/
	private Double ownCost;
	/**执行科室代码[39]**/
	private String execDpcd;
	/**执行科室名称[40]**/
	private String execDpnm;
	/**医保中心项目代码[41]**/
	private String centerCode;
	/**项目等级，1甲类，2乙类，3丙类[42]**/
	private Integer itemGrade;
	/**主药标志[43]**/
	private Integer mainDrug;
	/**组合号[44]**/
	private String combNo;
	/**划价人[45]**/
	private String operCode;
	/**划价时间[46]**/
	private Date operDate;
	/**0划价 1收费 3预收费团体体检 4 药品预审核**/
	private Integer payFlag;
	/**0退费，1正常，2重打，3注销[48]**/
	private Integer cancelFlag;
	/**收费员代码[49]**/
	private String feeCpcd;
	/**收费日期[50]**/
	private Date feeDate;
	/**票据号[51]**/
	private String invoiceNo;
	/**发票科目代码[52]**/
	private String invoCode;
	/**发票内流水号[53]**/
	private String invoSequence;
	/**0未确认/1确认[54]**/
	private Integer confirmFlag;
	/**确认人[55]**/
	private String confirmCode;
	/**确认科室[56]**/
	private String confirmDept;
	/**确认时间[57]**/
	private Date confirmDate;
	/**优惠金额[58]**/
	private Double ecoCost;
	/**发票序号，一次结算产生多张发票的combNo**/
	private String invoiceSeq;
	/**新项目比例**/
	private Double newItemrate;
	/**原项目比例**/
	private Double oldItemrate;
	/**扩展标志 特殊项目标志 1 0 非**/
	private Integer extFlag;
	/**0 正常/1个人体检/2 集体体检**/
	private Integer extFlag1;
	/**日结标志：0：未日结/1：已日结**/
	private Integer extFlag2;
	/**1 包装 单位 0, 最小单位**/
	private Integer extFlag3;
	/**复合项目代码**/
	private String packageCode;
	/**复合项目名称**/
	private String packageName;
	/**可退数量**/
	private Double nobackNum;
	/**确认数量**/
	private Double confirmNum;
	/**已确认院注次数**/
	private Integer confirmInject;
	/**医嘱项目流水号或者体检项目流水号**/
	private String moOrder;
	/**条码号**/
	private String sampleId;
	/**收费序列**/
	private String recipeSeq;
	/**超标金额**/
	private Double overCost;
	/**药品超标金额**/
	private Double excessCost;
	/**自费药金额**/
	private Double drugOwncost;
	/**费用来源 0 操作员 1 医嘱 2 终端 3 体检**/
	private Integer costSource;
	/**附材标志**/
	private Integer subjobFlag;
	/**0没有扣账户 1 已经扣账户**/
	private Integer accountFlag;
	/**更新库存的流水号(物资)**/
	private Long updateSequenceno;
	/**账户患者消费账号**/
	private String accountNo;
	/**0 未出结果 1 已出结果**/
	private Integer useFlag;
	/**门诊号**/
	private String clinicCode;
	/**病历号**/
	private String patientNo;
	/**扩展标记一(如果该项目为附材存主药的医嘱流水号,否则为空)**/
	private String extendOne;
	/**扩展标记二**/
	private String extendTwo;
	/**数据库无关字段  科室名称**/
	private String deptName;
	/**数据库无关字段 发票领取状态0未领取或已用完1领取未用完**/
	private String invoiceNoflay;
	/**费用金额**/
	private Double totCost;
	/**数据库无关字段 是否发药**/
	private String flay;
	/**数据库无关字段 虚拟可退数量**/
	private Integer nobackNums;
	
	/**执行数量-医技**/
	private Integer exeQty;
	/**执行设备-医技**/
	private String exeEqui;
	/**执行人-医技**/
	private String exeUser;
	/**终端申请id-医技**/
	private String appId;
	/**开单科室名称**/
	private String regDpcdname;
	/**开方医师姓名**/
	private String doctCodename;
	/**开方医师所在科室名称**/
	private String doctDeptname;
	/**频次名称**/
	private String frequencyName;
	/**划价人姓名**/
	private String operName;
	/**收费员姓名**/
	private String feeCpcdname;
	/**确认人姓名**/
	private String confirmName;
	/**确认科室名称**/
	private String confirmDeptname;
	/**医院编码*/
	private Integer hospitalId;
	/**院区编码*/
	private String areaCode;
	public String getFlay() {
		return flay;
	}
	public void setFlay(String flay) {
		this.flay = flay;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
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
	public String getRegDpcd() {
		return regDpcd;
	}
	public void setRegDpcd(String regDpcd) {
		this.regDpcd = regDpcd;
	}
	public String getDoctCode() {
		return doctCode;
	}
	public void setDoctCode(String doctCode) {
		this.doctCode = doctCode;
	}
	public String getDoctDept() {
		return doctDept;
	}
	public void setDoctDept(String doctDept) {
		this.doctDept = doctDept;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getDrugFlag() {
		return drugFlag;
	}
	public void setDrugFlag(String drugFlag) {
		this.drugFlag = drugFlag;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public Integer getSelfMade() {
		return selfMade;
	}
	public void setSelfMade(Integer selfMade) {
		this.selfMade = selfMade;
	}
	public String getDrugQuality() {
		return drugQuality;
	}
	public void setDrugQuality(String drugQuality) {
		this.drugQuality = drugQuality;
	}
	public String getDoseModelCode() {
		return doseModelCode;
	}
	public void setDoseModelCode(String doseModelCode) {
		this.doseModelCode = doseModelCode;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public String getFrequencyCode() {
		return frequencyCode;
	}
	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}
	public String getUsageCode() {
		return usageCode;
	}
	public void setUsageCode(String usageCode) {
		this.usageCode = usageCode;
	}
	public String getUseName() {
		return useName;
	}
	public void setUseName(String useName) {
		this.useName = useName;
	}
	public Integer getInjectNumber() {
		return injectNumber;
	}
	public void setInjectNumber(Integer injectNumber) {
		this.injectNumber = injectNumber;
	}
	public Integer getEmcFlag() {
		return emcFlag;
	}
	public void setEmcFlag(Integer emcFlag) {
		this.emcFlag = emcFlag;
	}
	public String getLabType() {
		return labType;
	}
	public void setLabType(String labType) {
		this.labType = labType;
	}
	public String getCheckBody() {
		return checkBody;
	}
	public void setCheckBody(String checkBody) {
		this.checkBody = checkBody;
	}
	public Double getDoseOnce() {
		return doseOnce;
	}
	public void setDoseOnce(Double doseOnce) {
		this.doseOnce = doseOnce;
	}
	public String getDoseUnit() {
		return doseUnit;
	}
	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}
	public Double getBaseDose() {
		return baseDose;
	}
	public void setBaseDose(Double baseDose) {
		this.baseDose = baseDose;
	}
	public Integer getPackQty() {
		return packQty;
	}
	public void setPackQty(Integer packQty) {
		this.packQty = packQty;
	}
	public String getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
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
	public Double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}
	public String getExecDpcd() {
		return execDpcd;
	}
	public void setExecDpcd(String execDpcd) {
		this.execDpcd = execDpcd;
	}
	public String getExecDpnm() {
		return execDpnm;
	}
	public void setExecDpnm(String execDpnm) {
		this.execDpnm = execDpnm;
	}
	public String getCenterCode() {
		return centerCode;
	}
	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}
	public Integer getItemGrade() {
		return itemGrade;
	}
	public void setItemGrade(Integer itemGrade) {
		this.itemGrade = itemGrade;
	}
	public Integer getMainDrug() {
		return mainDrug;
	}
	public void setMainDrug(Integer mainDrug) {
		this.mainDrug = mainDrug;
	}
	public String getCombNo() {
		return combNo;
	}
	public void setCombNo(String combNo) {
		this.combNo = combNo;
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
	public Integer getPayFlag() {
		return payFlag;
	}
	public void setPayFlag(Integer payFlag) {
		this.payFlag = payFlag;
	}
	public Integer getCancelFlag() {
		return cancelFlag;
	}
	public void setCancelFlag(Integer cancelFlag) {
		this.cancelFlag = cancelFlag;
	}
	public String getFeeCpcd() {
		return feeCpcd;
	}
	public void setFeeCpcd(String feeCpcd) {
		this.feeCpcd = feeCpcd;
	}
	public Date getFeeDate() {
		return feeDate;
	}
	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getInvoCode() {
		return invoCode;
	}
	public void setInvoCode(String invoCode) {
		this.invoCode = invoCode;
	}
	public String getInvoSequence() {
		return invoSequence;
	}
	public void setInvoSequence(String invoSequence) {
		this.invoSequence = invoSequence;
	}
	public Integer getConfirmFlag() {
		return confirmFlag;
	}
	public void setConfirmFlag(Integer confirmFlag) {
		this.confirmFlag = confirmFlag;
	}
	public String getConfirmCode() {
		return confirmCode;
	}
	public void setConfirmCode(String confirmCode) {
		this.confirmCode = confirmCode;
	}
	public String getConfirmDept() {
		return confirmDept;
	}
	public void setConfirmDept(String confirmDept) {
		this.confirmDept = confirmDept;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public Double getEcoCost() {
		return ecoCost;
	}
	public void setEcoCost(Double ecoCost) {
		this.ecoCost = ecoCost;
	}
	public String getInvoiceSeq() {
		return invoiceSeq;
	}
	public void setInvoiceSeq(String invoiceSeq) {
		this.invoiceSeq = invoiceSeq;
	}
	public Double getNewItemrate() {
		return newItemrate;
	}
	public void setNewItemrate(Double newItemrate) {
		this.newItemrate = newItemrate;
	}
	public Double getOldItemrate() {
		return oldItemrate;
	}
	public void setOldItemrate(Double oldItemrate) {
		this.oldItemrate = oldItemrate;
	}
	public Integer getExtFlag() {
		return extFlag;
	}
	public void setExtFlag(Integer extFlag) {
		this.extFlag = extFlag;
	}
	public Integer getExtFlag1() {
		return extFlag1;
	}
	public void setExtFlag1(Integer extFlag1) {
		this.extFlag1 = extFlag1;
	}
	public Integer getExtFlag2() {
		return extFlag2;
	}
	public void setExtFlag2(Integer extFlag2) {
		this.extFlag2 = extFlag2;
	}
	public Integer getExtFlag3() {
		return extFlag3;
	}
	public void setExtFlag3(Integer extFlag3) {
		this.extFlag3 = extFlag3;
	}
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Double getNobackNum() {
		return nobackNum;
	}
	public void setNobackNum(Double nobackNum) {
		this.nobackNum = nobackNum;
	}
	public Double getConfirmNum() {
		return confirmNum;
	}
	public void setConfirmNum(Double confirmNum) {
		this.confirmNum = confirmNum;
	}
	public Integer getConfirmInject() {
		return confirmInject;
	}
	public void setConfirmInject(Integer confirmInject) {
		this.confirmInject = confirmInject;
	}
	public String getMoOrder() {
		return moOrder;
	}
	public void setMoOrder(String moOrder) {
		this.moOrder = moOrder;
	}
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	public String getRecipeSeq() {
		return recipeSeq;
	}
	public void setRecipeSeq(String recipeSeq) {
		this.recipeSeq = recipeSeq;
	}
	public Double getOverCost() {
		return overCost;
	}
	public void setOverCost(Double overCost) {
		this.overCost = overCost;
	}
	public Double getExcessCost() {
		return excessCost;
	}
	public void setExcessCost(Double excessCost) {
		this.excessCost = excessCost;
	}
	public Double getDrugOwncost() {
		return drugOwncost;
	}
	public void setDrugOwncost(Double drugOwncost) {
		this.drugOwncost = drugOwncost;
	}
	public Integer getCostSource() {
		return costSource;
	}
	public void setCostSource(Integer costSource) {
		this.costSource = costSource;
	}
	public Integer getSubjobFlag() {
		return subjobFlag;
	}
	public void setSubjobFlag(Integer subjobFlag) {
		this.subjobFlag = subjobFlag;
	}
	public Integer getAccountFlag() {
		return accountFlag;
	}
	public void setAccountFlag(Integer accountFlag) {
		this.accountFlag = accountFlag;
	}
	public Long getUpdateSequenceno() {
		return updateSequenceno;
	}
	public void setUpdateSequenceno(Long updateSequenceno) {
		this.updateSequenceno = updateSequenceno;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public Integer getUseFlag() {
		return useFlag;
	}
	public void setUseFlag(Integer useFlag) {
		this.useFlag = useFlag;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public String getExtendOne() {
		return extendOne;
	}
	public void setExtendOne(String extendOne) {
		this.extendOne = extendOne;
	}
	public String getExtendTwo() {
		return extendTwo;
	}
	public void setExtendTwo(String extendTwo) {
		this.extendTwo = extendTwo;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptName() {
		return deptName;
	}
	public String getInvoiceNoflay() {
		return invoiceNoflay;
	}
	public void setInvoiceNoflay(String invoiceNoflay) {
		this.invoiceNoflay = invoiceNoflay;
	}
	public Integer getExeQty() {
		return exeQty;
	}
	public void setExeQty(Integer exeQty) {
		this.exeQty = exeQty;
	}
	public String getExeEqui() {
		return exeEqui;
	}
	public void setExeEqui(String exeEqui) {
		this.exeEqui = exeEqui;
	}
	public String getExeUser() {
		return exeUser;
	}
	public void setExeUser(String exeUser) {
		this.exeUser = exeUser;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public Integer getNobackNums() {
		return nobackNums;
	}
	public void setNobackNums(Integer nobackNums) {
		this.nobackNums = nobackNums;
	}
	public String getRegDpcdname() {
		return regDpcdname;
	}
	public void setRegDpcdname(String regDpcdname) {
		this.regDpcdname = regDpcdname;
	}
	public String getDoctCodename() {
		return doctCodename;
	}
	public void setDoctCodename(String doctCodename) {
		this.doctCodename = doctCodename;
	}
	public String getDoctDeptname() {
		return doctDeptname;
	}
	public void setDoctDeptname(String doctDeptname) {
		this.doctDeptname = doctDeptname;
	}
	public String getFrequencyName() {
		return frequencyName;
	}
	public void setFrequencyName(String frequencyName) {
		this.frequencyName = frequencyName;
	}
	public String getOperName() {
		return operName;
	}
	public void setOperName(String operName) {
		this.operName = operName;
	}
	public String getFeeCpcdname() {
		return feeCpcdname;
	}
	public void setFeeCpcdname(String feeCpcdname) {
		this.feeCpcdname = feeCpcdname;
	}
	public String getConfirmName() {
		return confirmName;
	}
	public void setConfirmName(String confirmName) {
		this.confirmName = confirmName;
	}
	public String getConfirmDeptname() {
		return confirmDeptname;
	}
	public void setConfirmDeptname(String confirmDeptname) {
		this.confirmDeptname = confirmDeptname;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
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