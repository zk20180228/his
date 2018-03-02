package cn.honry.base.bean.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * BiInpatientInfo entity. @author MyEclipse Persistence Tools
 */

public class BiInpatientInfo implements java.io.Serializable {

	// Fields

	private String inpatientNo;
	private String outpatientNo;
	private String cardNo;
	private String medicalNo;
	private String patientId;
	private String PName;
	private String contractCode;
	private String column8;
	private String clinicDiag;
	private String dischargeDiag;
	private Date inpatientTime;
	private String medicalType;
	private String mcardNo;
	private String patientName;
	private String certificatesType;
	private String certificatesNo;
	private String reportSex;
	private Date reportBirthday;
	private String profCode;
	private String workName;
	private String workTel;
	private String workZip;
	private String home;
	private String homeTel;
	private String homeZip;
	private String dist;
	private String birthArea;
	private String nationCode;
	private String mari;
	private String counCode;
	private Double height;
	private Double weight;
	private String bloodDress;
	private String bloodCode;
	private Boolean hepatitisFlag;
	private Boolean anaphyFlag;
	private String deptCode;
	private String pactCode;
	private String diagName;
	private String inAvenue;
	private String inSource;
	private BigDecimal inTimes;
	private Double prepayCost;
	private Double changePrepaycost;
	private String alterType;
	private Date alterBegin;
	private Date alterEnd;
	private Double moneyAlert;
	private Double totCost;
	private Double ownCost;
	private Double payCost;
	private Double pubCost;
	private Double ecoCost;
	private Double freeCost;
	private Double changeTotcost;
	private Double upperLimit;
	private Short balanceNo;
	private Double balanceCost;
	private Double balancePrepay;
	private Date balanceDate;
	private String inState;
	private Boolean diagOutstate;
	private Date prepayOutdate;
	private Date outDate;
	private String zgCode;
	private String zgName;
	private String emplCode;
	private Boolean inIcu;
	private String tend;
	private String remark;
	private Double bedLimit;
	private Double airLimit;
	private Double boardCost;
	private Double boardPrepay;
	private Boolean boardState;
	private Double ownRate;
	private Double payRate;
	private String patientStatus;
	private Date createTime;
	private Date updateTime;
	//2016年8月24日10:54:35  患者年龄 
	private String age;
	
	// Constructors

	/** default constructor */
	public BiInpatientInfo() {
	}

	/** full constructor */
	public BiInpatientInfo(String outpatientNo, String cardNo,
			String medicalNo, String patientId, String PName,
			String contractCode, String column8, String clinicDiag,
			String dischargeDiag, Date inpatientTime, String medicalType,
			String mcardNo, String patientName, String certificatesType,
			String certificatesNo, String reportSex, Date reportBirthday,
			String profCode, String workName, String workTel, String workZip,
			String home, String homeTel, String homeZip, String dist,
			String birthArea, String nationCode, String mari, String counCode,
			Double height, Double weight, String bloodDress, String bloodCode,
			Boolean hepatitisFlag, Boolean anaphyFlag, String deptCode,
			String pactCode, String diagName, String inAvenue, String inSource,
			BigDecimal inTimes, Double prepayCost, Double changePrepaycost,
			String alterType, Date alterBegin, Date alterEnd,
			Double moneyAlert, Double totCost, Double ownCost, Double payCost,
			Double pubCost, Double ecoCost, Double freeCost,
			Double changeTotcost, Double upperLimit, Short balanceNo,
			Double balanceCost, Double balancePrepay, Date balanceDate,
			String inState, Boolean diagOutstate, Date prepayOutdate,
			Date outDate, String zgCode, String zgName, String emplCode,
			Boolean inIcu, String tend, String remark, Double bedLimit,
			Double airLimit, Double boardCost, Double boardPrepay,
			Boolean boardState, Double ownRate, Double payRate,
			String patientStatus, Date createTime, Date updateTime, String age) {
		this.outpatientNo = outpatientNo;
		this.cardNo = cardNo;
		this.medicalNo = medicalNo;
		this.patientId = patientId;
		this.PName = PName;
		this.contractCode = contractCode;
		this.column8 = column8;
		this.clinicDiag = clinicDiag;
		this.dischargeDiag = dischargeDiag;
		this.inpatientTime = inpatientTime;
		this.medicalType = medicalType;
		this.mcardNo = mcardNo;
		this.patientName = patientName;
		this.certificatesType = certificatesType;
		this.certificatesNo = certificatesNo;
		this.reportSex = reportSex;
		this.reportBirthday = reportBirthday;
		this.profCode = profCode;
		this.workName = workName;
		this.workTel = workTel;
		this.workZip = workZip;
		this.home = home;
		this.homeTel = homeTel;
		this.homeZip = homeZip;
		this.dist = dist;
		this.birthArea = birthArea;
		this.nationCode = nationCode;
		this.mari = mari;
		this.counCode = counCode;
		this.height = height;
		this.weight = weight;
		this.bloodDress = bloodDress;
		this.bloodCode = bloodCode;
		this.hepatitisFlag = hepatitisFlag;
		this.anaphyFlag = anaphyFlag;
		this.deptCode = deptCode;
		this.pactCode = pactCode;
		this.diagName = diagName;
		this.inAvenue = inAvenue;
		this.inSource = inSource;
		this.inTimes = inTimes;
		this.prepayCost = prepayCost;
		this.changePrepaycost = changePrepaycost;
		this.alterType = alterType;
		this.alterBegin = alterBegin;
		this.alterEnd = alterEnd;
		this.moneyAlert = moneyAlert;
		this.totCost = totCost;
		this.ownCost = ownCost;
		this.payCost = payCost;
		this.pubCost = pubCost;
		this.ecoCost = ecoCost;
		this.freeCost = freeCost;
		this.changeTotcost = changeTotcost;
		this.upperLimit = upperLimit;
		this.balanceNo = balanceNo;
		this.balanceCost = balanceCost;
		this.balancePrepay = balancePrepay;
		this.balanceDate = balanceDate;
		this.inState = inState;
		this.diagOutstate = diagOutstate;
		this.prepayOutdate = prepayOutdate;
		this.outDate = outDate;
		this.zgCode = zgCode;
		this.zgName = zgName;
		this.emplCode = emplCode;
		this.inIcu = inIcu;
		this.tend = tend;
		this.remark = remark;
		this.bedLimit = bedLimit;
		this.airLimit = airLimit;
		this.boardCost = boardCost;
		this.boardPrepay = boardPrepay;
		this.boardState = boardState;
		this.ownRate = ownRate;
		this.payRate = payRate;
		this.patientStatus = patientStatus;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.age = age;
	}

	// Property accessors

	public String getInpatientNo() {
		return this.inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getOutpatientNo() {
		return this.outpatientNo;
	}

	public void setOutpatientNo(String outpatientNo) {
		this.outpatientNo = outpatientNo;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getMedicalNo() {
		return this.medicalNo;
	}

	public void setMedicalNo(String medicalNo) {
		this.medicalNo = medicalNo;
	}

	public String getPatientId() {
		return this.patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPName() {
		return this.PName;
	}

	public void setPName(String PName) {
		this.PName = PName;
	}

	public String getContractCode() {
		return this.contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getColumn8() {
		return this.column8;
	}

	public void setColumn8(String column8) {
		this.column8 = column8;
	}

	public String getClinicDiag() {
		return this.clinicDiag;
	}

	public void setClinicDiag(String clinicDiag) {
		this.clinicDiag = clinicDiag;
	}

	public String getDischargeDiag() {
		return this.dischargeDiag;
	}

	public void setDischargeDiag(String dischargeDiag) {
		this.dischargeDiag = dischargeDiag;
	}

	public Date getInpatientTime() {
		return this.inpatientTime;
	}

	public void setInpatientTime(Date inpatientTime) {
		this.inpatientTime = inpatientTime;
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

	public String getPatientName() {
		return this.patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getCertificatesType() {
		return this.certificatesType;
	}

	public void setCertificatesType(String certificatesType) {
		this.certificatesType = certificatesType;
	}

	public String getCertificatesNo() {
		return this.certificatesNo;
	}

	public void setCertificatesNo(String certificatesNo) {
		this.certificatesNo = certificatesNo;
	}

	public String getReportSex() {
		return this.reportSex;
	}

	public void setReportSex(String reportSex) {
		this.reportSex = reportSex;
	}

	public Date getReportBirthday() {
		return this.reportBirthday;
	}

	public void setReportBirthday(Date reportBirthday) {
		this.reportBirthday = reportBirthday;
	}

	public String getProfCode() {
		return this.profCode;
	}

	public void setProfCode(String profCode) {
		this.profCode = profCode;
	}

	public String getWorkName() {
		return this.workName;
	}

	public void setWorkName(String workName) {
		this.workName = workName;
	}

	public String getWorkTel() {
		return this.workTel;
	}

	public void setWorkTel(String workTel) {
		this.workTel = workTel;
	}

	public String getWorkZip() {
		return this.workZip;
	}

	public void setWorkZip(String workZip) {
		this.workZip = workZip;
	}

	public String getHome() {
		return this.home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getHomeTel() {
		return this.homeTel;
	}

	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}

	public String getHomeZip() {
		return this.homeZip;
	}

	public void setHomeZip(String homeZip) {
		this.homeZip = homeZip;
	}

	public String getDist() {
		return this.dist;
	}

	public void setDist(String dist) {
		this.dist = dist;
	}

	public String getBirthArea() {
		return this.birthArea;
	}

	public void setBirthArea(String birthArea) {
		this.birthArea = birthArea;
	}

	public String getNationCode() {
		return this.nationCode;
	}

	public void setNationCode(String nationCode) {
		this.nationCode = nationCode;
	}

	public String getMari() {
		return this.mari;
	}

	public void setMari(String mari) {
		this.mari = mari;
	}

	public String getCounCode() {
		return this.counCode;
	}

	public void setCounCode(String counCode) {
		this.counCode = counCode;
	}

	public Double getHeight() {
		return this.height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getWeight() {
		return this.weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getBloodDress() {
		return this.bloodDress;
	}

	public void setBloodDress(String bloodDress) {
		this.bloodDress = bloodDress;
	}

	public String getBloodCode() {
		return this.bloodCode;
	}

	public void setBloodCode(String bloodCode) {
		this.bloodCode = bloodCode;
	}

	public Boolean getHepatitisFlag() {
		return this.hepatitisFlag;
	}

	public void setHepatitisFlag(Boolean hepatitisFlag) {
		this.hepatitisFlag = hepatitisFlag;
	}

	public Boolean getAnaphyFlag() {
		return this.anaphyFlag;
	}

	public void setAnaphyFlag(Boolean anaphyFlag) {
		this.anaphyFlag = anaphyFlag;
	}

	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getPactCode() {
		return this.pactCode;
	}

	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}

	public String getDiagName() {
		return this.diagName;
	}

	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}

	public String getInAvenue() {
		return this.inAvenue;
	}

	public void setInAvenue(String inAvenue) {
		this.inAvenue = inAvenue;
	}

	public String getInSource() {
		return this.inSource;
	}

	public void setInSource(String inSource) {
		this.inSource = inSource;
	}

	public BigDecimal getInTimes() {
		return this.inTimes;
	}

	public void setInTimes(BigDecimal inTimes) {
		this.inTimes = inTimes;
	}

	public Double getPrepayCost() {
		return this.prepayCost;
	}

	public void setPrepayCost(Double prepayCost) {
		this.prepayCost = prepayCost;
	}

	public Double getChangePrepaycost() {
		return this.changePrepaycost;
	}

	public void setChangePrepaycost(Double changePrepaycost) {
		this.changePrepaycost = changePrepaycost;
	}

	public String getAlterType() {
		return this.alterType;
	}

	public void setAlterType(String alterType) {
		this.alterType = alterType;
	}

	public Date getAlterBegin() {
		return this.alterBegin;
	}

	public void setAlterBegin(Date alterBegin) {
		this.alterBegin = alterBegin;
	}

	public Date getAlterEnd() {
		return this.alterEnd;
	}

	public void setAlterEnd(Date alterEnd) {
		this.alterEnd = alterEnd;
	}

	public Double getMoneyAlert() {
		return this.moneyAlert;
	}

	public void setMoneyAlert(Double moneyAlert) {
		this.moneyAlert = moneyAlert;
	}

	public Double getTotCost() {
		return this.totCost;
	}

	public void setTotCost(Double totCost) {
		this.totCost = totCost;
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

	public Double getPubCost() {
		return this.pubCost;
	}

	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}

	public Double getEcoCost() {
		return this.ecoCost;
	}

	public void setEcoCost(Double ecoCost) {
		this.ecoCost = ecoCost;
	}

	public Double getFreeCost() {
		return this.freeCost;
	}

	public void setFreeCost(Double freeCost) {
		this.freeCost = freeCost;
	}

	public Double getChangeTotcost() {
		return this.changeTotcost;
	}

	public void setChangeTotcost(Double changeTotcost) {
		this.changeTotcost = changeTotcost;
	}

	public Double getUpperLimit() {
		return this.upperLimit;
	}

	public void setUpperLimit(Double upperLimit) {
		this.upperLimit = upperLimit;
	}

	public Short getBalanceNo() {
		return this.balanceNo;
	}

	public void setBalanceNo(Short balanceNo) {
		this.balanceNo = balanceNo;
	}

	public Double getBalanceCost() {
		return this.balanceCost;
	}

	public void setBalanceCost(Double balanceCost) {
		this.balanceCost = balanceCost;
	}

	public Double getBalancePrepay() {
		return this.balancePrepay;
	}

	public void setBalancePrepay(Double balancePrepay) {
		this.balancePrepay = balancePrepay;
	}

	public Date getBalanceDate() {
		return this.balanceDate;
	}

	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}

	public String getInState() {
		return this.inState;
	}

	public void setInState(String inState) {
		this.inState = inState;
	}

	public Boolean getDiagOutstate() {
		return this.diagOutstate;
	}

	public void setDiagOutstate(Boolean diagOutstate) {
		this.diagOutstate = diagOutstate;
	}

	public Date getPrepayOutdate() {
		return this.prepayOutdate;
	}

	public void setPrepayOutdate(Date prepayOutdate) {
		this.prepayOutdate = prepayOutdate;
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

	public String getEmplCode() {
		return this.emplCode;
	}

	public void setEmplCode(String emplCode) {
		this.emplCode = emplCode;
	}

	public Boolean getInIcu() {
		return this.inIcu;
	}

	public void setInIcu(Boolean inIcu) {
		this.inIcu = inIcu;
	}

	public String getTend() {
		return this.tend;
	}

	public void setTend(String tend) {
		this.tend = tend;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getBedLimit() {
		return this.bedLimit;
	}

	public void setBedLimit(Double bedLimit) {
		this.bedLimit = bedLimit;
	}

	public Double getAirLimit() {
		return this.airLimit;
	}

	public void setAirLimit(Double airLimit) {
		this.airLimit = airLimit;
	}

	public Double getBoardCost() {
		return this.boardCost;
	}

	public void setBoardCost(Double boardCost) {
		this.boardCost = boardCost;
	}

	public Double getBoardPrepay() {
		return this.boardPrepay;
	}

	public void setBoardPrepay(Double boardPrepay) {
		this.boardPrepay = boardPrepay;
	}

	public Boolean getBoardState() {
		return this.boardState;
	}

	public void setBoardState(Boolean boardState) {
		this.boardState = boardState;
	}

	public Double getOwnRate() {
		return this.ownRate;
	}

	public void setOwnRate(Double ownRate) {
		this.ownRate = ownRate;
	}

	public Double getPayRate() {
		return this.payRate;
	}

	public void setPayRate(Double payRate) {
		this.payRate = payRate;
	}

	public String getPatientStatus() {
		return this.patientStatus;
	}

	public void setPatientStatus(String patientStatus) {
		this.patientStatus = patientStatus;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

}