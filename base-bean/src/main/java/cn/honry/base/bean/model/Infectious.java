package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * TOutpatientInfectiousreport entity. @author MyEclipse Persistence Tools
 */

public class Infectious extends Entity {

	private static final long serialVersionUID = 1L;
	// Fields

	private String reportId;
	private String reportNo;
	private Integer reportType;
	private String idcardNo;
	private String medicalrecordId;
	private Integer patientType;
	private String patientName;
	private String patientParents;
	private String certificatesType;
	private String certificatesNo;
	private String reportSex;
	private Date reportBirthday;
	private Integer reportAge;
	private String reportAgeunit;
	private String patientProfession;
	private String otherProfession;
	private String workPlace;
	private String telephone;
	private String homeArea;
	private String homeCouty;
	private String homeTown;
	private String homeAddress;
	private String patientDept;
	private String diseaseType;
	private String otherDisease;
	private Date infectDate;
	private Date diagnosisDate;
	private Date deadDate;
	private String caseClass1;
	private Integer caseClass2;
	private Integer infectotherFlag;
	private Integer state;
	private Integer additionFlag;
	private Integer sexinfectionFlag;
	private String patientMari;
	private String patientEducation;
	private String infectHistory;
	private String otherInfect;
	private String infectSource;
	private String otherSource;
	private String sampleSource;
	private String otherSample;
	private Integer reportHbsag;
	private double alt;
	private Date firsthbDate;
	private Integer againstHbc;
	private Integer liverbiopsyResult;
	private Integer rps;
	private String reportRemark;
	private String reportUnit;
	private String reportUnitphone;
	private String reportDoctor;
	private String doctorDept;
	private Date reportDate;
	private String cancelOper;
	private Date cancelDate;
	private String approveOper;
	private Date approveDate;
	private String reportReason;
	private Integer correctFlag;
	private String correctReportNo;
	private String correctedReportNo;
	private String correctedDisease;
	private String extendInfo1;
	private String extendInfo2;
	private String extendInfo3;
	private String inpatientNo;
	private String extendInfo4;
	private String extendInfo5;
	
    //	疾病名字
 	private String diseaseTypename;
 	//	其他疾病名字
 	private String otherDiseasename;
 	
 	/*--------------显示字段-----begin------------*/
 	
 	/** 
 	* @Fields strSex : 性别 （渲染后的） 
 	*/ 
 	private String strSex;
 	/** 
 	* @Fields strProvince : 省 （渲染后的） 
 	*/ 
 	private String strProvince;
 	/** 
 	 * @Fields strCity : 市 （渲染后的） 
 	 */ 
 	private String strCity;
 	/** 
 	 * @Fields strCounty : 县 （渲染后的） 
 	 */ 
 	private String strCounty;
 	/** 
 	* @Fields strMarry : 婚姻状态（渲染后的） 
 	*/ 
 	private String strMarry;
 	/** 
 	 * @Fields strDegree : 文化程度（渲染后的） 
 	 */ 
 	private String strDegree;
 	
 	/*--------------显示字段-----end------------*/
 	
	public String getDiseaseTypename() {
		return diseaseTypename;
	}

	public void setDiseaseTypename(String diseaseTypename) {
		this.diseaseTypename = diseaseTypename;
	}

	public String getOtherDiseasename() {
		return otherDiseasename;
	}

	public void setOtherDiseasename(String otherDiseasename) {
		this.otherDiseasename = otherDiseasename;
	}

	public String getReportId() {
		return this.reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getReportNo() {
		return this.reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}


	public String getIdcardNo() {
		return this.idcardNo;
	}

	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}

	public String getMedicalrecordId() {
		return this.medicalrecordId;
	}

	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}


	public String getPatientName() {
		return this.patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientParents() {
		return this.patientParents;
	}

	public void setPatientParents(String patientParents) {
		this.patientParents = patientParents;
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

	public Integer getReportAge() {
		return this.reportAge;
	}

	public void setReportAge(Integer reportAge) {
		this.reportAge = reportAge;
	}

	public String getReportAgeunit() {
		return this.reportAgeunit;
	}

	public void setReportAgeunit(String reportAgeunit) {
		this.reportAgeunit = reportAgeunit;
	}

	public String getPatientProfession() {
		return this.patientProfession;
	}

	public void setPatientProfession(String patientProfession) {
		this.patientProfession = patientProfession;
	}

	public String getOtherProfession() {
		return this.otherProfession;
	}

	public void setOtherProfession(String otherProfession) {
		this.otherProfession = otherProfession;
	}

	public String getWorkPlace() {
		return this.workPlace;
	}

	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getHomeArea() {
		return this.homeArea;
	}

	public void setHomeArea(String homeArea) {
		this.homeArea = homeArea;
	}

	public String getHomeCouty() {
		return this.homeCouty;
	}

	public void setHomeCouty(String homeCouty) {
		this.homeCouty = homeCouty;
	}

	public String getHomeTown() {
		return this.homeTown;
	}

	public void setHomeTown(String homeTown) {
		this.homeTown = homeTown;
	}

	public String getHomeAddress() {
		return this.homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getPatientDept() {
		return this.patientDept;
	}

	public void setPatientDept(String patientDept) {
		this.patientDept = patientDept;
	}

	public String getDiseaseType() {
		return this.diseaseType;
	}

	public void setDiseaseType(String diseaseType) {
		this.diseaseType = diseaseType;
	}

	public String getOtherDisease() {
		return this.otherDisease;
	}

	public void setOtherDisease(String otherDisease) {
		this.otherDisease = otherDisease;
	}

	public Date getInfectDate() {
		return this.infectDate;
	}

	public void setInfectDate(Date infectDate) {
		this.infectDate = infectDate;
	}

	public Date getDiagnosisDate() {
		return this.diagnosisDate;
	}

	public void setDiagnosisDate(Date diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}

	public Date getDeadDate() {
		return this.deadDate;
	}

	public void setDeadDate(Date deadDate) {
		this.deadDate = deadDate;
	}

	public String getCaseClass1() {
		return this.caseClass1;
	}

	public void setCaseClass1(String caseClass1) {
		this.caseClass1 = caseClass1;
	}


	public String getPatientMari() {
		return this.patientMari;
	}

	public void setPatientMari(String patientMari) {
		this.patientMari = patientMari;
	}

	public String getPatientEducation() {
		return this.patientEducation;
	}

	public void setPatientEducation(String patientEducation) {
		this.patientEducation = patientEducation;
	}

	public String getInfectHistory() {
		return this.infectHistory;
	}

	public void setInfectHistory(String infectHistory) {
		this.infectHistory = infectHistory;
	}

	public String getOtherInfect() {
		return this.otherInfect;
	}

	public void setOtherInfect(String otherInfect) {
		this.otherInfect = otherInfect;
	}

	public String getInfectSource() {
		return this.infectSource;
	}

	public void setInfectSource(String infectSource) {
		this.infectSource = infectSource;
	}

	public String getOtherSource() {
		return this.otherSource;
	}

	public void setOtherSource(String otherSource) {
		this.otherSource = otherSource;
	}

	public String getSampleSource() {
		return this.sampleSource;
	}

	public void setSampleSource(String sampleSource) {
		this.sampleSource = sampleSource;
	}

	public String getOtherSample() {
		return this.otherSample;
	}

	public void setOtherSample(String otherSample) {
		this.otherSample = otherSample;
	}


	public double getAlt() {
		return this.alt;
	}

	public void setAlt(double alt) {
		this.alt = alt;
	}

	public Date getFirsthbDate() {
		return this.firsthbDate;
	}

	public void setFirsthbDate(Date firsthbDate) {
		this.firsthbDate = firsthbDate;
	}
	public String getReportRemark() {
		return this.reportRemark;
	}

	public void setReportRemark(String reportRemark) {
		this.reportRemark = reportRemark;
	}

	public String getReportUnit() {
		return this.reportUnit;
	}

	public void setReportUnit(String reportUnit) {
		this.reportUnit = reportUnit;
	}

	public String getReportUnitphone() {
		return this.reportUnitphone;
	}

	public void setReportUnitphone(String reportUnitphone) {
		this.reportUnitphone = reportUnitphone;
	}

	public String getReportDoctor() {
		return this.reportDoctor;
	}

	public void setReportDoctor(String reportDoctor) {
		this.reportDoctor = reportDoctor;
	}

	public String getDoctorDept() {
		return this.doctorDept;
	}

	public void setDoctorDept(String doctorDept) {
		this.doctorDept = doctorDept;
	}

	public Date getReportDate() {
		return this.reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getCancelOper() {
		return this.cancelOper;
	}

	public void setCancelOper(String cancelOper) {
		this.cancelOper = cancelOper;
	}

	public Date getCancelDate() {
		return this.cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getApproveOper() {
		return this.approveOper;
	}

	public void setApproveOper(String approveOper) {
		this.approveOper = approveOper;
	}

	public Date getApproveDate() {
		return this.approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	public String getReportReason() {
		return this.reportReason;
	}

	public void setReportReason(String reportReason) {
		this.reportReason = reportReason;
	}
	public String getCorrectReportNo() {
		return this.correctReportNo;
	}

	public Integer getReportType() {
		return reportType;
	}

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}

	public Integer getPatientType() {
		return patientType;
	}

	public void setPatientType(Integer patientType) {
		this.patientType = patientType;
	}

	public Integer getCaseClass2() {
		return caseClass2;
	}

	public void setCaseClass2(Integer caseClass2) {
		this.caseClass2 = caseClass2;
	}

	public Integer getInfectotherFlag() {
		return infectotherFlag;
	}

	public void setInfectotherFlag(Integer infectotherFlag) {
		this.infectotherFlag = infectotherFlag;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getAdditionFlag() {
		return additionFlag;
	}

	public void setAdditionFlag(Integer additionFlag) {
		this.additionFlag = additionFlag;
	}

	public Integer getSexinfectionFlag() {
		return sexinfectionFlag;
	}

	public void setSexinfectionFlag(Integer sexinfectionFlag) {
		this.sexinfectionFlag = sexinfectionFlag;
	}

	public Integer getReportHbsag() {
		return reportHbsag;
	}

	public void setReportHbsag(Integer reportHbsag) {
		this.reportHbsag = reportHbsag;
	}

	public Integer getAgainstHbc() {
		return againstHbc;
	}

	public void setAgainstHbc(Integer againstHbc) {
		this.againstHbc = againstHbc;
	}

	public Integer getLiverbiopsyResult() {
		return liverbiopsyResult;
	}

	public void setLiverbiopsyResult(Integer liverbiopsyResult) {
		this.liverbiopsyResult = liverbiopsyResult;
	}

	public Integer getRps() {
		return rps;
	}

	public void setRps(Integer rps) {
		this.rps = rps;
	}

	public Integer getCorrectFlag() {
		return correctFlag;
	}

	public void setCorrectFlag(Integer correctFlag) {
		this.correctFlag = correctFlag;
	}

	public void setCorrectReportNo(String correctReportNo) {
		this.correctReportNo = correctReportNo;
	}

	public String getCorrectedReportNo() {
		return this.correctedReportNo;
	}

	public void setCorrectedReportNo(String correctedReportNo) {
		this.correctedReportNo = correctedReportNo;
	}

	public String getCorrectedDisease() {
		return this.correctedDisease;
	}

	public void setCorrectedDisease(String correctedDisease) {
		this.correctedDisease = correctedDisease;
	}

	public String getExtendInfo1() {
		return this.extendInfo1;
	}

	public void setExtendInfo1(String extendInfo1) {
		this.extendInfo1 = extendInfo1;
	}

	public String getExtendInfo2() {
		return this.extendInfo2;
	}

	public void setExtendInfo2(String extendInfo2) {
		this.extendInfo2 = extendInfo2;
	}

	public String getExtendInfo3() {
		return this.extendInfo3;
	}

	public void setExtendInfo3(String extendInfo3) {
		this.extendInfo3 = extendInfo3;
	}

	public String getInpatientNo() {
		return this.inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getExtendInfo4() {
		return this.extendInfo4;
	}

	public void setExtendInfo4(String extendInfo4) {
		this.extendInfo4 = extendInfo4;
	}

	public String getExtendInfo5() {
		return this.extendInfo5;
	}

	public void setExtendInfo5(String extendInfo5) {
		this.extendInfo5 = extendInfo5;
	}

	/*--------------显示字段-----begin------------*/
	
	public String getStrProvince() {
		return strProvince;
	}

	public String getStrSex() {
		return strSex;
	}

	public void setStrSex(String strSex) {
		this.strSex = strSex;
	}

	public void setStrProvince(String strProvince) {
		this.strProvince = strProvince;
	}

	public String getStrCity() {
		return strCity;
	}

	public void setStrCity(String strCity) {
		this.strCity = strCity;
	}

	public String getStrCounty() {
		return strCounty;
	}

	public void setStrCounty(String strCounty) {
		this.strCounty = strCounty;
	}

	public String getStrMarry() {
		return strMarry;
	}

	public void setStrMarry(String strMarry) {
		this.strMarry = strMarry;
	}

	public String getStrDegree() {
		return strDegree;
	}

	public void setStrDegree(String strDegree) {
		this.strDegree = strDegree;
	}

	/*--------------显示字段----end-------------*/
}