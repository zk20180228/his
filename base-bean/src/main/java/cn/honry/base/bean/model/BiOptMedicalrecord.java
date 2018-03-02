package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiOptMedicalrecord entity. @author MyEclipse Persistence Tools
 */

public class BiOptMedicalrecord implements java.io.Serializable {

	// Fields

	private String id;
	private String clinicCode;
	private String cardNo;
	private String pasource;
	private String name;
	private String sexCode;
	private String sex;
	private String age;
	private String ageUnitCode;
	private String ageUnit;
	private Date birthday;
	private String telephone;
	private String reglvlCode;
	private String reglvlName;
	private String paykindCode;
	private String paykindName;
	private String pactCode;
	private String pactName;
	private String checkpartCode;
	private String checkpart;
	private Date seeDate;
	private String seeDoct;
	private String seeDoctnm;
	private String seeDeptcode;
	private String hostTell;
	private String diagnoseTypeCode;
	private String diagnoseType;
	private Date diagnoseDate;
	private String historyspecil;
	private String allergichistory;
	private String heredityhis;
	private String currentIllness;
	private String anamnesis;
	private Double weight;
	private Double temperature;
	private Double pulse;
	private Double breathing;
	private String bloodPressure;
	private String physicalExamination;
	private String checkresult;
	private String diagnose1Code;
	private String diagnose1;
	private String diagnose2Code;
	private String diagnose2;
	private String diagnose3Code;
	private String diagnose3;
	private String diagnose4Code;
	private String diagnose4;
	private String remark;
	private String hepatitisFlag;
	private String allergenFlag;
	private String purpose;
	private String address;
	private String advice;
	private String status;
	private String nurseCode;
	private Date nurseOprDate;
	private String doctCode;
	private Date doctOprDate;
	private String oprCode;
	private Date oprDate;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;

	// Constructors

	/** default constructor */
	public BiOptMedicalrecord() {
	}

	/** full constructor */
	public BiOptMedicalrecord(String clinicCode, String cardNo,
			String pasource, String name, String sexCode, String sex,
			String age, String ageUnitCode, String ageUnit, Date birthday,
			String telephone, String reglvlCode, String reglvlName,
			String paykindCode, String paykindName, String pactCode,
			String pactName, String checkpartCode, String checkpart,
			Date seeDate, String seeDoct, String seeDoctnm, String seeDeptcode,
			String hostTell, String diagnoseTypeCode, String diagnoseType,
			Date diagnoseDate, String historyspecil, String allergichistory,
			String heredityhis, String currentIllness, String anamnesis,
			Double weight, Double temperature, Double pulse, Double breathing,
			String bloodPressure, String physicalExamination,
			String checkresult, String diagnose1Code, String diagnose1,
			String diagnose2Code, String diagnose2, String diagnose3Code,
			String diagnose3, String diagnose4Code, String diagnose4,
			String remark, String hepatitisFlag, String allergenFlag,
			String purpose, String address, String advice, String status,
			String nurseCode, Date nurseOprDate, String doctCode,
			Date doctOprDate, String oprCode, Date oprDate, String ext1,
			String ext2, String ext3, String ext4) {
		this.clinicCode = clinicCode;
		this.cardNo = cardNo;
		this.pasource = pasource;
		this.name = name;
		this.sexCode = sexCode;
		this.sex = sex;
		this.age = age;
		this.ageUnitCode = ageUnitCode;
		this.ageUnit = ageUnit;
		this.birthday = birthday;
		this.telephone = telephone;
		this.reglvlCode = reglvlCode;
		this.reglvlName = reglvlName;
		this.paykindCode = paykindCode;
		this.paykindName = paykindName;
		this.pactCode = pactCode;
		this.pactName = pactName;
		this.checkpartCode = checkpartCode;
		this.checkpart = checkpart;
		this.seeDate = seeDate;
		this.seeDoct = seeDoct;
		this.seeDoctnm = seeDoctnm;
		this.seeDeptcode = seeDeptcode;
		this.hostTell = hostTell;
		this.diagnoseTypeCode = diagnoseTypeCode;
		this.diagnoseType = diagnoseType;
		this.diagnoseDate = diagnoseDate;
		this.historyspecil = historyspecil;
		this.allergichistory = allergichistory;
		this.heredityhis = heredityhis;
		this.currentIllness = currentIllness;
		this.anamnesis = anamnesis;
		this.weight = weight;
		this.temperature = temperature;
		this.pulse = pulse;
		this.breathing = breathing;
		this.bloodPressure = bloodPressure;
		this.physicalExamination = physicalExamination;
		this.checkresult = checkresult;
		this.diagnose1Code = diagnose1Code;
		this.diagnose1 = diagnose1;
		this.diagnose2Code = diagnose2Code;
		this.diagnose2 = diagnose2;
		this.diagnose3Code = diagnose3Code;
		this.diagnose3 = diagnose3;
		this.diagnose4Code = diagnose4Code;
		this.diagnose4 = diagnose4;
		this.remark = remark;
		this.hepatitisFlag = hepatitisFlag;
		this.allergenFlag = allergenFlag;
		this.purpose = purpose;
		this.address = address;
		this.advice = advice;
		this.status = status;
		this.nurseCode = nurseCode;
		this.nurseOprDate = nurseOprDate;
		this.doctCode = doctCode;
		this.doctOprDate = doctOprDate;
		this.oprCode = oprCode;
		this.oprDate = oprDate;
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

	public String getPasource() {
		return this.pasource;
	}

	public void setPasource(String pasource) {
		this.pasource = pasource;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSexCode() {
		return this.sexCode;
	}

	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return this.age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAgeUnitCode() {
		return this.ageUnitCode;
	}

	public void setAgeUnitCode(String ageUnitCode) {
		this.ageUnitCode = ageUnitCode;
	}

	public String getAgeUnit() {
		return this.ageUnit;
	}

	public void setAgeUnit(String ageUnit) {
		this.ageUnit = ageUnit;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getReglvlCode() {
		return this.reglvlCode;
	}

	public void setReglvlCode(String reglvlCode) {
		this.reglvlCode = reglvlCode;
	}

	public String getReglvlName() {
		return this.reglvlName;
	}

	public void setReglvlName(String reglvlName) {
		this.reglvlName = reglvlName;
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

	public String getCheckpartCode() {
		return this.checkpartCode;
	}

	public void setCheckpartCode(String checkpartCode) {
		this.checkpartCode = checkpartCode;
	}

	public String getCheckpart() {
		return this.checkpart;
	}

	public void setCheckpart(String checkpart) {
		this.checkpart = checkpart;
	}

	public Date getSeeDate() {
		return this.seeDate;
	}

	public void setSeeDate(Date seeDate) {
		this.seeDate = seeDate;
	}

	public String getSeeDoct() {
		return this.seeDoct;
	}

	public void setSeeDoct(String seeDoct) {
		this.seeDoct = seeDoct;
	}

	public String getSeeDoctnm() {
		return this.seeDoctnm;
	}

	public void setSeeDoctnm(String seeDoctnm) {
		this.seeDoctnm = seeDoctnm;
	}

	public String getSeeDeptcode() {
		return this.seeDeptcode;
	}

	public void setSeeDeptcode(String seeDeptcode) {
		this.seeDeptcode = seeDeptcode;
	}

	public String getHostTell() {
		return this.hostTell;
	}

	public void setHostTell(String hostTell) {
		this.hostTell = hostTell;
	}

	public String getDiagnoseTypeCode() {
		return this.diagnoseTypeCode;
	}

	public void setDiagnoseTypeCode(String diagnoseTypeCode) {
		this.diagnoseTypeCode = diagnoseTypeCode;
	}

	public String getDiagnoseType() {
		return this.diagnoseType;
	}

	public void setDiagnoseType(String diagnoseType) {
		this.diagnoseType = diagnoseType;
	}

	public Date getDiagnoseDate() {
		return this.diagnoseDate;
	}

	public void setDiagnoseDate(Date diagnoseDate) {
		this.diagnoseDate = diagnoseDate;
	}

	public String getHistoryspecil() {
		return this.historyspecil;
	}

	public void setHistoryspecil(String historyspecil) {
		this.historyspecil = historyspecil;
	}

	public String getAllergichistory() {
		return this.allergichistory;
	}

	public void setAllergichistory(String allergichistory) {
		this.allergichistory = allergichistory;
	}

	public String getHeredityhis() {
		return this.heredityhis;
	}

	public void setHeredityhis(String heredityhis) {
		this.heredityhis = heredityhis;
	}

	public String getCurrentIllness() {
		return this.currentIllness;
	}

	public void setCurrentIllness(String currentIllness) {
		this.currentIllness = currentIllness;
	}

	public String getAnamnesis() {
		return this.anamnesis;
	}

	public void setAnamnesis(String anamnesis) {
		this.anamnesis = anamnesis;
	}

	public Double getWeight() {
		return this.weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getTemperature() {
		return this.temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getPulse() {
		return this.pulse;
	}

	public void setPulse(Double pulse) {
		this.pulse = pulse;
	}

	public Double getBreathing() {
		return this.breathing;
	}

	public void setBreathing(Double breathing) {
		this.breathing = breathing;
	}

	public String getBloodPressure() {
		return this.bloodPressure;
	}

	public void setBloodPressure(String bloodPressure) {
		this.bloodPressure = bloodPressure;
	}

	public String getPhysicalExamination() {
		return this.physicalExamination;
	}

	public void setPhysicalExamination(String physicalExamination) {
		this.physicalExamination = physicalExamination;
	}

	public String getCheckresult() {
		return this.checkresult;
	}

	public void setCheckresult(String checkresult) {
		this.checkresult = checkresult;
	}

	public String getDiagnose1Code() {
		return this.diagnose1Code;
	}

	public void setDiagnose1Code(String diagnose1Code) {
		this.diagnose1Code = diagnose1Code;
	}

	public String getDiagnose1() {
		return this.diagnose1;
	}

	public void setDiagnose1(String diagnose1) {
		this.diagnose1 = diagnose1;
	}

	public String getDiagnose2Code() {
		return this.diagnose2Code;
	}

	public void setDiagnose2Code(String diagnose2Code) {
		this.diagnose2Code = diagnose2Code;
	}

	public String getDiagnose2() {
		return this.diagnose2;
	}

	public void setDiagnose2(String diagnose2) {
		this.diagnose2 = diagnose2;
	}

	public String getDiagnose3Code() {
		return this.diagnose3Code;
	}

	public void setDiagnose3Code(String diagnose3Code) {
		this.diagnose3Code = diagnose3Code;
	}

	public String getDiagnose3() {
		return this.diagnose3;
	}

	public void setDiagnose3(String diagnose3) {
		this.diagnose3 = diagnose3;
	}

	public String getDiagnose4Code() {
		return this.diagnose4Code;
	}

	public void setDiagnose4Code(String diagnose4Code) {
		this.diagnose4Code = diagnose4Code;
	}

	public String getDiagnose4() {
		return this.diagnose4;
	}

	public void setDiagnose4(String diagnose4) {
		this.diagnose4 = diagnose4;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getHepatitisFlag() {
		return this.hepatitisFlag;
	}

	public void setHepatitisFlag(String hepatitisFlag) {
		this.hepatitisFlag = hepatitisFlag;
	}

	public String getAllergenFlag() {
		return this.allergenFlag;
	}

	public void setAllergenFlag(String allergenFlag) {
		this.allergenFlag = allergenFlag;
	}

	public String getPurpose() {
		return this.purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAdvice() {
		return this.advice;
	}

	public void setAdvice(String advice) {
		this.advice = advice;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNurseCode() {
		return this.nurseCode;
	}

	public void setNurseCode(String nurseCode) {
		this.nurseCode = nurseCode;
	}

	public Date getNurseOprDate() {
		return this.nurseOprDate;
	}

	public void setNurseOprDate(Date nurseOprDate) {
		this.nurseOprDate = nurseOprDate;
	}

	public String getDoctCode() {
		return this.doctCode;
	}

	public void setDoctCode(String doctCode) {
		this.doctCode = doctCode;
	}

	public Date getDoctOprDate() {
		return this.doctOprDate;
	}

	public void setDoctOprDate(Date doctOprDate) {
		this.doctOprDate = doctOprDate;
	}

	public String getOprCode() {
		return this.oprCode;
	}

	public void setOprCode(String oprCode) {
		this.oprCode = oprCode;
	}

	public Date getOprDate() {
		return this.oprDate;
	}

	public void setOprDate(Date oprDate) {
		this.oprDate = oprDate;
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