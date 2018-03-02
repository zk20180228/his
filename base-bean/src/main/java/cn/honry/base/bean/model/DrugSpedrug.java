package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * TDrugSpedrug entity. @author MyEclipse Persistence Tools
 */

public class DrugSpedrug  extends Entity  {

	// Fields

	private String applyNo;
	private String clinicCode;
	private String bedNo;
	private String name;
	private String sex;
	private int age;
	private String patientDept;
	private String comDoctor;
	private String comTitle;
	private String diagnose;
	private String drugBased;
	private String drugCode;
	private String drugName;
	private String usage;
	private String dosage;
	private String purpose;
	private String infectiondiagnosis;
	private Integer isexam;
	private String sampleType;
	private Integer isbacterial;
	private String bacterial;
	private Integer issensitive;
	private String useddrug;
	private String applyReason;
	private String groupOpinion;
	private String applicDoctor;
	private Integer applicState;
	private String examOpinion;
	private String examDoctor;
	private Date examDate;
	private String examOpinion2;
	private String examDoctor2;
	private Date examDate2;
	private Date  inDate;
	private String spec;
	/**
	 * 性别渲染字段
	 */
	private String sexName;
	/****/
	private Date brithday;
	public Date getBrithday() {
		return brithday;
	}

	public void setBrithday(Date brithday) {
		this.brithday = brithday;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getApplyNo() {
		return this.applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public String getClinicCode() {
		return this.clinicCode;
	}

	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}

	public String getBedNo() {
		return this.bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	
	public String getPatientDept() {
		return this.patientDept;
	}

	public void setPatientDept(String patientDept) {
		this.patientDept = patientDept;
	}

	public String getComDoctor() {
		return this.comDoctor;
	}

	public void setComDoctor(String comDoctor) {
		this.comDoctor = comDoctor;
	}

	public String getComTitle() {
		return this.comTitle;
	}

	public void setComTitle(String comTitle) {
		this.comTitle = comTitle;
	}

	public String getDiagnose() {
		return this.diagnose;
	}

	public void setDiagnose(String diagnose) {
		this.diagnose = diagnose;
	}

	public String getDrugBased() {
		return this.drugBased;
	}

	public void setDrugBased(String drugBased) {
		this.drugBased = drugBased;
	}

	public String getDrugCode() {
		return this.drugCode;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}

	public String getDrugName() {
		return this.drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getUsage() {
		return this.usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getDosage() {
		return this.dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getPurpose() {
		return this.purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getInfectiondiagnosis() {
		return this.infectiondiagnosis;
	}

	public void setInfectiondiagnosis(String infectiondiagnosis) {
		this.infectiondiagnosis = infectiondiagnosis;
	}


	public String getSampleType() {
		return this.sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}


	public String getBacterial() {
		return this.bacterial;
	}

	public void setBacterial(String bacterial) {
		this.bacterial = bacterial;
	}


	public String getUseddrug() {
		return this.useddrug;
	}

	public void setUseddrug(String useddrug) {
		this.useddrug = useddrug;
	}

	public String getApplyReason() {
		return this.applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}

	public String getGroupOpinion() {
		return this.groupOpinion;
	}

	public void setGroupOpinion(String groupOpinion) {
		this.groupOpinion = groupOpinion;
	}

	public String getApplicDoctor() {
		return this.applicDoctor;
	}

	public void setApplicDoctor(String applicDoctor) {
		this.applicDoctor = applicDoctor;
	}


	public Integer getIsexam() {
		return isexam;
	}

	public void setIsexam(Integer isexam) {
		this.isexam = isexam;
	}

	public Integer getIsbacterial() {
		return isbacterial;
	}

	public void setIsbacterial(Integer isbacterial) {
		this.isbacterial = isbacterial;
	}

	public Integer getIssensitive() {
		return issensitive;
	}

	public void setIssensitive(Integer issensitive) {
		this.issensitive = issensitive;
	}

	public Integer getApplicState() {
		return applicState;
	}

	public void setApplicState(Integer applicState) {
		this.applicState = applicState;
	}

	public String getExamOpinion() {
		return this.examOpinion;
	}

	public void setExamOpinion(String examOpinion) {
		this.examOpinion = examOpinion;
	}

	public String getExamDoctor() {
		return this.examDoctor;
	}

	public void setExamDoctor(String examDoctor) {
		this.examDoctor = examDoctor;
	}

	public Date getExamDate() {
		return this.examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public String getExamOpinion2() {
		return this.examOpinion2;
	}

	public void setExamOpinion2(String examOpinion2) {
		this.examOpinion2 = examOpinion2;
	}

	public String getExamDoctor2() {
		return this.examDoctor2;
	}

	public void setExamDoctor2(String examDoctor2) {
		this.examDoctor2 = examDoctor2;
	}

	public Date getExamDate2() {
		return this.examDate2;
	}

	public void setExamDate2(Date examDate2) {
		this.examDate2 = examDate2;
	}

	public String getSexName() {
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}





}