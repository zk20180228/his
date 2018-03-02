package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**  
 *  
 * @className：InpatientInfo.java 
 * @Author：lt
 * @CreateDate：2015-6-24  
 * @version 1.0
 *
 */
public class InpatientInfo extends Entity implements java.io.Serializable {
	// Fields
	private static final long serialVersionUID = 1L;
	
	
	/**账户卡号*/
	private String accountId;
	/**总费用(账户金额明细表)*/
	private Integer creditAmount;
	/**住院流水号(6位日期+6位流水号)*/
	private String inpatientNo;
	/**医疗类别(从编码表里读取)*/
	private String medicalType;
	/**病历号*/
	private String medicalrecordId;
	/**就诊卡编号*/
	private String idcardNo;
	/**医疗证号*/
	private String mcardNo;
	/**姓名*/
	private String patientName;
	/**证件类型(从编码表里读取)*/
	private String certificatesType;
	/**证件号码*/
	private String certificatesNo;
	/**性别(从编码表里读取)*/
	private String reportSex;
	/**出生日期*/
	private Date reportBirthday;
	/**年龄*/
	private Integer reportAge;
	/**年龄单位(年月天)*/
	private String reportAgeunit;
	/**职业代码(从编码表里读取)*/
	private String profCode;
	/**工作单位*/
	private String workName;
	/**工作单位电话*/
	private String workTel;
	/**单位邮编*/
	private String workZip;
	/**户口或家庭地址*/
	private String home;
	/**家庭电话*/
	private String homeTel;
	/**户口或家庭邮编*/
	private String homeZip;
	/**籍贯(从城市表里读取)*/
	private String dist;
	/**出生地代码(从城市表里读取)*/
	private String birthArea;
	/**民族(从编码表里读取)*/
	private String nationCode;
	/**联系人姓名*/
	private String linkmanName;
	/**联系人电话*/
	private String linkmanTel;
	/**联系人地址*/
	private String linkmanAddress;
	/**关系(从编码表里读取)*/
	private String relaCode;
	/**婚姻状况(从编码表里读取)*/
	private String mari;
	/**国籍(从国家编码表里读取)*/
	private String counCode;
	/**身高*/
	private Double height;
	/**体重*/
	private Double weight;
	/**血压*/
	private String bloodDress;
	/**血型编码(从编码表里读取)*/
	private String bloodCode;
	/**重大疾病标志1:有  0:无*/
	private Integer hepatitisFlag;
	/**过敏标志1:有  0:无*/
	private Integer anaphyFlag;
	/**入院日期*/
	private Date inDate;
	/**科室代码*/
	private String deptCode;
	/**结算类别 01-自费 02-保险 03-公费在职 04-公费退休 05-公费高干 (从编码表里读取)*/
	private String paykindCode;
	/**合同单位代码 (从合同单位编码表里读取)*/
	private String pactCode;
	/**床号  (从病床维护表里读取)*/
	private String bedId;
	/**护理单元代码 (部门表里的护士站)*/
	private String nurseCellCode;
	/**医师代码(住院)*/
	private String houseDocCode;
	/**医师代码(主治)*/
	private String chargeDocCode;
	/**医师代码(主任)*/
	private String chiefDocCode;
	/**护士代码(责任)*/
	private String dutyNurseCode;
	/**护理单元名称 (部门表里的护士站)*/
	private String nurseCellName;
	/**医师名称(住院)*/
	private String houseDocName;
	/**医师名称(主治)*/
	private String chargeDocName;
	/**医师名称(主任)*/
	private String chiefDocName;
	/**护士名称(责任)*/
	private String dutyNurseName;
	/**入院情况*/
	private String inCircs;
	/**诊断名称（建议用此保存主诊断）*/
	private String diagName;
	/**入院途径*/
	private String inAvenue;
	/**入院来源 1:门诊，2:急诊，3:转科，4:转院 (从编码表里读取)*/
	private String inSource;
	/**住院次数*/
	private Integer inTimes;
	/**预交金额(未结)*/
	private Double prepayCost;
	/**转入预交金额（未结)*/
	private Double changePrepaycost;
	/**M 金额 D时间段*/
	private String alterType;
	/**警戒线开始时间*/
	private Date alterBegin;
	/**警戒线结束时间*/
	private Date alterEnd;
	/**警戒线*/
	private Double moneyAlert;
	/**费用金额(未结)*/
	private Double totCost;
	/**自费金额(未结)*/
	private Double ownCost;
	/**自付金额(未结)*/
	private Double payCost;
	/**公费金额(未结)*/
	private Double pubCost;
	/**优惠金额(未结)*/
	private Double ecoCost;
	/**余额(未结)*/
	private Double freeCost;
	/**转入费用金额(未结)*/
	private Double changeTotcost;
	/**待遇上限*/
	private Double upperLimit;
	/**固定费用间隔天数*/
	private Integer feeInterval;
	/**结算序号*/
	private Integer balanceNo;
	/**费用金额(已结)*/
	private Double balanceCost;
	/**预交金额(已结)*/
	private Double balancePrepay;
	/**结算日期(上次)*/
	private Date balanceDate;
	/**是否关帐 1是 0否*/
	private Integer stopAcount;
	/**婴儿标志 1:婴儿；0:非婴儿*/
	private Integer babyFlag;
	/**病案状态: 0 无需病案 1 需要病案 2 医生站形成病案 3 病案室形成病案 4病案封存*/
	private Integer caseFlag;
	/**R-住院登记  I-病房接诊 B-出院登记 O-出院结算 P-预约出院,N-无费退院,C-婴儿封账*/
	private String inState;
	/**是否请假 0 非 1是*/
	private Integer leaveFlag;
	/**出院日期(预约)*/
	private Date prepayOutdate;
	/**出院日期*/
	private Date outDate;
	/**转归代号*/
	private String zg;
	/**开据医师*/
	private String emplCode;
	/**是否在ICU 0 NO 1 YES*/
	private Integer inIcu;
	/**病案送入病案室否0未1送*/
	private Integer casesendFlag;
	/**护理级别(TEND):名称显示护理级别名称(一级护理，二级护理，三级护理) (从编码表里读取)*/
	private String tend;
	/**病危：0 普通 1 病重 2 病危*/
	private Integer criticalFlag;
	/**上次固定费用时间*/
	private Date prefixfeeDate;
	/**血滞纳金*/
	private Double bloodLatefee;
	/**公费患者日限额*/
	private Double dayLimit;
	/**公费患者日限额累计*/
	private Double limitTot;
	/**公费患者日限额超标部分金额*/
	private Double limitOvertop;
	/**生育保险患者电脑号*/
	private String procreatePcno;
	/**公费患者公费药品累计(日限额)*/
	private Double bursaryTotmedfee;
	/**备注*/
	private String remark;
	/**床位上限*/
	private Double bedLimit;
	/**空调上限*/
	private Double airLimit;
	/**床费超标处理 0超标不限 1超标自理 2超标不计*/
	private Integer bedoverdeal;
	/**（公医超日限额是否同意：0不同意，1同意）*/
	private String extFlag;
	/**扩展标记1*/
	private String extFlag1;
	/**扩展标记2*/
	private String extFlag2;
	/**膳食花费总额*/
	private Double boardCost;
	/**膳食预交金额*/
	private Double boardPrepay;
	/**膳食结算状态：0在院 1出院*/
	private Integer boardState;
	/**自费比例*/
	private Double ownRate;
	/**自付比例*/
	private Double payRate;
	/**扩展数值（中山一用作－剩余统筹金额）*/
	private Double extNumber;
	/**扩展编码（）*/
	private String extCode;
	/**患者状态（从编码表中取值）*/
	private String patientStatus;
	/**是否有婴儿（0-无，1-有）*/
	private Integer haveBabyFlag;
	/**年龄*/
	private String age;
	//新加字段
	/**性别名称**/
	private String reportSexName;
	/**科室名称**/
	private String deptName;
	/**开据医师名称**/
	private String emplName;
	/** 就诊卡表Id **/
	private String idcardId;
	/** 病房Id **/
	private String bedwardId;
	/** 病房name **/
	private String bedwardName;
	/** 病床表Id **/
	private String bedNo;
	/** 病床号name **/
	private String bedName;
	/**出院状态**/
	private Integer outState;
	
	//虚拟字段   病区
	private String bingqu;
	private InpatientAccount account;//做查询显示用
	private Integer remarkalert=0;
	private String outCircs;//出院情况
	private String infoBedId;//
	private Double accountAmount;//出院登记总费用
	
	private Double balance;//余额
	
	/**医院编码**/
	private Integer hospitalId;
	/**院区编码**/
	private String areaCode;
	
	
	
	
	// Property accessors
	
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
	public String getAccountId() {
		return accountId;
	}
	public String getNurseCellName() {
		return nurseCellName;
	}
	public void setNurseCellName(String nurseCellName) {
		this.nurseCellName = nurseCellName;
	}
	public String getHouseDocName() {
		return houseDocName;
	}
	public void setHouseDocName(String houseDocName) {
		this.houseDocName = houseDocName;
	}
	public String getChargeDocName() {
		return chargeDocName;
	}
	public void setChargeDocName(String chargeDocName) {
		this.chargeDocName = chargeDocName;
	}
	public String getChiefDocName() {
		return chiefDocName;
	}
	public void setChiefDocName(String chiefDocName) {
		this.chiefDocName = chiefDocName;
	}
	public String getDutyNurseName() {
		return dutyNurseName;
	}
	public void setDutyNurseName(String dutyNurseName) {
		this.dutyNurseName = dutyNurseName;
	}
	public String getReportSexName() {
		return reportSexName;
	}
	public void setReportSexName(String reportSexName) {
		this.reportSexName = reportSexName;
	}
	public String getEmplName() {
		return emplName;
	}
	public void setEmplName(String emplName) {
		this.emplName = emplName;
	}
	public String getPatientStatus() {
		return patientStatus;
	}
	public void setPatientStatus(String patientStatus) {
		this.patientStatus = patientStatus;
	}
	public String getBingqu() {
		return bingqu;
	}
	public void setBingqu(String bingqu) {
		this.bingqu = bingqu;
	}
	public Integer getOutState() {
		return outState;
	}
	public void setOutState(Integer outState) {
		this.outState = outState;
	}
	public Double getAccountAmount() {
		return accountAmount;
	}
	public void setAccountAmount(Double accountAmount) {
		this.accountAmount = accountAmount;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public Integer getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(Integer creditAmount) {
		this.creditAmount = creditAmount;
	}
	
	
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getOutCircs() {
		return outCircs;
	}
	public void setOutCircs(String outCircs) {
		this.outCircs = outCircs;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public Integer getRemarkalert() {
		return remarkalert;
	}
	public void setRemarkalert(Integer remarkalert) {
		this.remarkalert = remarkalert;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getMedicalType() {
		return medicalType;
	}
	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getIdcardNo() {
		return idcardNo;
	}
	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	public String getMcardNo() {
		return mcardNo;
	}
	public void setMcardNo(String mcardNo) {
		this.mcardNo = mcardNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getCertificatesType() {
		return certificatesType;
	}
	public void setCertificatesType(String certificatesType) {
		this.certificatesType = certificatesType;
	}
	public String getCertificatesNo() {
		return certificatesNo;
	}
	public void setCertificatesNo(String certificatesNo) {
		this.certificatesNo = certificatesNo;
	}
	public String getReportSex() {
		return reportSex;
	}
	public void setReportSex(String reportSex) {
		this.reportSex = reportSex;
	}
	public Date getReportBirthday() {
		return reportBirthday;
	}
	public void setReportBirthday(Date reportBirthday) {
		this.reportBirthday = reportBirthday;
	}
	public Integer getReportAge() {
		return reportAge;
	}
	public void setReportAge(Integer reportAge) {
		this.reportAge = reportAge;
	}
	public String getReportAgeunit() {
		return reportAgeunit;
	}
	public void setReportAgeunit(String reportAgeunit) {
		this.reportAgeunit = reportAgeunit;
	}
	public String getProfCode() {
		return profCode;
	}
	public void setProfCode(String profCode) {
		this.profCode = profCode;
	}
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	public String getWorkTel() {
		return workTel;
	}
	public void setWorkTel(String workTel) {
		this.workTel = workTel;
	}
	public String getWorkZip() {
		return workZip;
	}
	public void setWorkZip(String workZip) {
		this.workZip = workZip;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public String getHomeTel() {
		return homeTel;
	}
	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}
	public String getHomeZip() {
		return homeZip;
	}
	public void setHomeZip(String homeZip) {
		this.homeZip = homeZip;
	}
	public String getDist() {
		return dist;
	}
	public void setDist(String dist) {
		this.dist = dist;
	}
	public String getBirthArea() {
		return birthArea;
	}
	public void setBirthArea(String birthArea) {
		this.birthArea = birthArea;
	}
	public String getNationCode() {
		return nationCode;
	}
	public void setNationCode(String nationCode) {
		this.nationCode = nationCode;
	}
	public String getLinkmanName() {
		return linkmanName;
	}
	public void setLinkmanName(String linkmanName) {
		this.linkmanName = linkmanName;
	}
	public String getLinkmanTel() {
		return linkmanTel;
	}
	public void setLinkmanTel(String linkmanTel) {
		this.linkmanTel = linkmanTel;
	}
	public String getLinkmanAddress() {
		return linkmanAddress;
	}
	public void setLinkmanAddress(String linkmanAddress) {
		this.linkmanAddress = linkmanAddress;
	}
	public String getRelaCode() {
		return relaCode;
	}
	public void setRelaCode(String relaCode) {
		this.relaCode = relaCode;
	}
	public String getMari() {
		return mari;
	}
	public void setMari(String mari) {
		this.mari = mari;
	}
	public String getCounCode() {
		return counCode;
	}
	public void setCounCode(String counCode) {
		this.counCode = counCode;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String getBloodDress() {
		return bloodDress;
	}
	public void setBloodDress(String bloodDress) {
		this.bloodDress = bloodDress;
	}
	public String getBloodCode() {
		return bloodCode;
	}
	public void setBloodCode(String bloodCode) {
		this.bloodCode = bloodCode;
	}
	public Integer getHepatitisFlag() {
		return hepatitisFlag;
	}
	public void setHepatitisFlag(Integer hepatitisFlag) {
		this.hepatitisFlag = hepatitisFlag;
	}
	public Integer getAnaphyFlag() {
		return anaphyFlag;
	}
	public void setAnaphyFlag(Integer anaphyFlag) {
		this.anaphyFlag = anaphyFlag;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
	public String getBedId() {
		return bedId;
	}
	public void setBedId(String bedId) {
		this.bedId = bedId;
	}
	public String getInCircs() {
		return inCircs;
	}
	public void setInCircs(String inCircs) {
		this.inCircs = inCircs;
	}
	public String getDiagName() {
		return diagName;
	}
	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}
	public String getInAvenue() {
		return inAvenue;
	}
	public void setInAvenue(String inAvenue) {
		this.inAvenue = inAvenue;
	}
	public String getInSource() {
		return inSource;
	}
	public void setInSource(String inSource) {
		this.inSource = inSource;
	}
	public Integer getInTimes() {
		return inTimes;
	}
	public void setInTimes(Integer inTimes) {
		this.inTimes = inTimes;
	}
	public Double getPrepayCost() {
		return prepayCost;
	}
	public void setPrepayCost(Double prepayCost) {
		this.prepayCost = prepayCost;
	}
	public Double getChangePrepaycost() {
		return changePrepaycost;
	}
	public void setChangePrepaycost(Double changePrepaycost) {
		this.changePrepaycost = changePrepaycost;
	}
	public String getAlterType() {
		return alterType;
	}
	public void setAlterType(String alterType) {
		this.alterType = alterType;
	}
	public Date getAlterBegin() {
		return alterBegin;
	}
	public void setAlterBegin(Date alterBegin) {
		this.alterBegin = alterBegin;
	}
	public Date getAlterEnd() {
		return alterEnd;
	}
	public void setAlterEnd(Date alterEnd) {
		this.alterEnd = alterEnd;
	}
	public Double getMoneyAlert() {
		return moneyAlert;
	}
	public void setMoneyAlert(Double moneyAlert) {
		this.moneyAlert = moneyAlert;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
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
	public Double getPubCost() {
		return pubCost;
	}
	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}
	public Double getEcoCost() {
		return ecoCost;
	}
	public void setEcoCost(Double ecoCost) {
		this.ecoCost = ecoCost;
	}
	public Double getFreeCost() {
		return freeCost;
	}
	public void setFreeCost(Double freeCost) {
		this.freeCost = freeCost;
	}
	public Double getChangeTotcost() {
		return changeTotcost;
	}
	public void setChangeTotcost(Double changeTotcost) {
		this.changeTotcost = changeTotcost;
	}
	public Double getUpperLimit() {
		return upperLimit;
	}
	public void setUpperLimit(Double upperLimit) {
		this.upperLimit = upperLimit;
	}
	public Integer getFeeInterval() {
		return feeInterval;
	}
	public void setFeeInterval(Integer feeInterval) {
		this.feeInterval = feeInterval;
	}
	public Integer getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(Integer balanceNo) {
		this.balanceNo = balanceNo;
	}
	public Double getBalanceCost() {
		return balanceCost;
	}
	public void setBalanceCost(Double balanceCost) {
		this.balanceCost = balanceCost;
	}
	public Double getBalancePrepay() {
		return balancePrepay;
	}
	public void setBalancePrepay(Double balancePrepay) {
		this.balancePrepay = balancePrepay;
	}
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public Integer getStopAcount() {
		return stopAcount;
	}
	public void setStopAcount(Integer stopAcount) {
		this.stopAcount = stopAcount;
	}
	public Integer getBabyFlag() {
		return babyFlag;
	}
	public void setBabyFlag(Integer babyFlag) {
		this.babyFlag = babyFlag;
	}
	public Integer getCaseFlag() {
		return caseFlag;
	}
	public void setCaseFlag(Integer caseFlag) {
		this.caseFlag = caseFlag;
	}
	public String getInState() {
		return inState;
	}
	public void setInState(String inState) {
		this.inState = inState;
	}
	public Integer getLeaveFlag() {
		return leaveFlag;
	}
	public void setLeaveFlag(Integer leaveFlag) {
		this.leaveFlag = leaveFlag;
	}
	public Date getPrepayOutdate() {
		return prepayOutdate;
	}
	public void setPrepayOutdate(Date prepayOutdate) {
		this.prepayOutdate = prepayOutdate;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public String getZg() {
		return zg;
	}
	public void setZg(String zg) {
		this.zg = zg;
	}
	public String getEmplCode() {
		return emplCode;
	}
	public void setEmplCode(String emplCode) {
		this.emplCode = emplCode;
	}
	public Integer getInIcu() {
		return inIcu;
	}
	public void setInIcu(Integer inIcu) {
		this.inIcu = inIcu;
	}
	public Integer getCasesendFlag() {
		return casesendFlag;
	}
	public void setCasesendFlag(Integer casesendFlag) {
		this.casesendFlag = casesendFlag;
	}
	public String getTend() {
		return tend;
	}
	public void setTend(String tend) {
		this.tend = tend;
	}
	public Integer getCriticalFlag() {
		return criticalFlag;
	}
	public void setCriticalFlag(Integer criticalFlag) {
		this.criticalFlag = criticalFlag;
	}
	public Date getPrefixfeeDate() {
		return prefixfeeDate;
	}
	public void setPrefixfeeDate(Date prefixfeeDate) {
		this.prefixfeeDate = prefixfeeDate;
	}
	public Double getBloodLatefee() {
		return bloodLatefee;
	}
	public void setBloodLatefee(Double bloodLatefee) {
		this.bloodLatefee = bloodLatefee;
	}
	public Double getDayLimit() {
		return dayLimit;
	}
	public void setDayLimit(Double dayLimit) {
		this.dayLimit = dayLimit;
	}
	public Double getLimitTot() {
		return limitTot;
	}
	public void setLimitTot(Double limitTot) {
		this.limitTot = limitTot;
	}
	public Double getLimitOvertop() {
		return limitOvertop;
	}
	public void setLimitOvertop(Double limitOvertop) {
		this.limitOvertop = limitOvertop;
	}
	public String getProcreatePcno() {
		return procreatePcno;
	}
	public void setProcreatePcno(String procreatePcno) {
		this.procreatePcno = procreatePcno;
	}
	public Double getBursaryTotmedfee() {
		return bursaryTotmedfee;
	}
	public void setBursaryTotmedfee(Double bursaryTotmedfee) {
		this.bursaryTotmedfee = bursaryTotmedfee;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getBedLimit() {
		return bedLimit;
	}
	public void setBedLimit(Double bedLimit) {
		this.bedLimit = bedLimit;
	}
	public Double getAirLimit() {
		return airLimit;
	}
	public void setAirLimit(Double airLimit) {
		this.airLimit = airLimit;
	}
	public Integer getBedoverdeal() {
		return bedoverdeal;
	}
	public void setBedoverdeal(Integer bedoverdeal) {
		this.bedoverdeal = bedoverdeal;
	}
	public String getExtFlag() {
		return extFlag;
	}
	public void setExtFlag(String extFlag) {
		this.extFlag = extFlag;
	}
	public String getExtFlag1() {
		return extFlag1;
	}
	public void setExtFlag1(String extFlag1) {
		this.extFlag1 = extFlag1;
	}
	public String getExtFlag2() {
		return extFlag2;
	}
	public void setExtFlag2(String extFlag2) {
		this.extFlag2 = extFlag2;
	}
	public Double getBoardCost() {
		return boardCost;
	}
	public void setBoardCost(Double boardCost) {
		this.boardCost = boardCost;
	}
	public Double getBoardPrepay() {
		return boardPrepay;
	}
	public void setBoardPrepay(Double boardPrepay) {
		this.boardPrepay = boardPrepay;
	}
	public Integer getBoardState() {
		return boardState;
	}
	public void setBoardState(Integer boardState) {
		this.boardState = boardState;
	}
	public Double getOwnRate() {
		return ownRate;
	}
	public void setOwnRate(Double ownRate) {
		this.ownRate = ownRate;
	}
	public Double getPayRate() {
		return payRate;
	}
	public void setPayRate(Double payRate) {
		this.payRate = payRate;
	}
	public Double getExtNumber() {
		return extNumber;
	}
	public void setExtNumber(Double extNumber) {
		this.extNumber = extNumber;
	}
	public String getExtCode() {
		return extCode;
	}
	public void setExtCode(String extCode) {
		this.extCode = extCode;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public String getHouseDocCode() {
		return houseDocCode;
	}
	public void setHouseDocCode(String houseDocCode) {
		this.houseDocCode = houseDocCode;
	}
	public String getChargeDocCode() {
		return chargeDocCode;
	}
	public void setChargeDocCode(String chargeDocCode) {
		this.chargeDocCode = chargeDocCode;
	}
	public String getChiefDocCode() {
		return chiefDocCode;
	}
	public void setChiefDocCode(String chiefDocCode) {
		this.chiefDocCode = chiefDocCode;
	}
	public String getDutyNurseCode() {
		return dutyNurseCode;
	}
	public void setDutyNurseCode(String dutyNurseCode) {
		this.dutyNurseCode = dutyNurseCode;
	}
	public InpatientAccount getAccount() {
		return account;
	}
	public void setAccount(InpatientAccount account) {
		this.account = account;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getInfoBedId() {
		return infoBedId;
	}
	public void setInfoBedId(String infoBedId) {
		this.infoBedId = infoBedId;
	}
	public Integer getHaveBabyFlag() {
		return haveBabyFlag;
	}
	public void setHaveBabyFlag(Integer haveBabyFlag) {
		this.haveBabyFlag = haveBabyFlag;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getIdcardId() {
		return idcardId;
	}
	public void setIdcardId(String idcardId) {
		this.idcardId = idcardId;
	}
	public String getBedwardId() {
		return bedwardId;
	}
	public void setBedwardId(String bedwardId) {
		this.bedwardId = bedwardId;
	}
	public String getBedwardName() {
		return bedwardName;
	}
	public void setBedwardName(String bedwardName) {
		this.bedwardName = bedwardName;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	
}