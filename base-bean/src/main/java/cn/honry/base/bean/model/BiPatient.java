package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiPatient entity. @author MyEclipse Persistence Tools
 */

public class BiPatient implements java.io.Serializable {

	// Fields

	private String patientId;
	private String PName;
	private Date PBirthday;
	private String PSexCode;
	private String PSexName;
	private String PMedicalNo;
	private String PContractCode;
	private String PContractName;
	private String PPaykindCode;
	private String PPaykindName;
	private String PBloodCode;
	private String PBloodName;
	private String education;
	private String degree;
	private String PProvince;
	private String PCity;
	private String PCounty;
	private String PCertificatestype;
	private String PCertificatesno;
	private String PNationality;
	private String PNationCode;
	private String PNationName;
	private String PWarriage;
	private String POccupation;
	private String PHandbook;
	private Boolean PIsanaphy;
	private Boolean PIshepatitis;
	private String createDate;
	private Date updateDate;

	// Constructors

	/** default constructor */
	public BiPatient() {
	}

	/** full constructor */
	public BiPatient(String PName, Date PBirthday, String PSexCode,
			String PSexName, String PMedicalNo, String PContractCode,
			String PContractName, String PPaykindCode, String PPaykindName,
			String PBloodCode, String PBloodName, String education,
			String degree, String PProvince, String PCity, String PCounty,
			String PCertificatestype, String PCertificatesno,
			String PNationality, String PNationCode, String PNationName,
			String PWarriage, String POccupation, String PHandbook,
			Boolean PIsanaphy, Boolean PIshepatitis, String createDate,
			Date updateDate) {
		this.PName = PName;
		this.PBirthday = PBirthday;
		this.PSexCode = PSexCode;
		this.PSexName = PSexName;
		this.PMedicalNo = PMedicalNo;
		this.PContractCode = PContractCode;
		this.PContractName = PContractName;
		this.PPaykindCode = PPaykindCode;
		this.PPaykindName = PPaykindName;
		this.PBloodCode = PBloodCode;
		this.PBloodName = PBloodName;
		this.education = education;
		this.degree = degree;
		this.PProvince = PProvince;
		this.PCity = PCity;
		this.PCounty = PCounty;
		this.PCertificatestype = PCertificatestype;
		this.PCertificatesno = PCertificatesno;
		this.PNationality = PNationality;
		this.PNationCode = PNationCode;
		this.PNationName = PNationName;
		this.PWarriage = PWarriage;
		this.POccupation = POccupation;
		this.PHandbook = PHandbook;
		this.PIsanaphy = PIsanaphy;
		this.PIshepatitis = PIshepatitis;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	// Property accessors

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

	public Date getPBirthday() {
		return this.PBirthday;
	}

	public void setPBirthday(Date PBirthday) {
		this.PBirthday = PBirthday;
	}

	public String getPSexCode() {
		return this.PSexCode;
	}

	public void setPSexCode(String PSexCode) {
		this.PSexCode = PSexCode;
	}

	public String getPSexName() {
		return this.PSexName;
	}

	public void setPSexName(String PSexName) {
		this.PSexName = PSexName;
	}

	public String getPMedicalNo() {
		return this.PMedicalNo;
	}

	public void setPMedicalNo(String PMedicalNo) {
		this.PMedicalNo = PMedicalNo;
	}

	public String getPContractCode() {
		return this.PContractCode;
	}

	public void setPContractCode(String PContractCode) {
		this.PContractCode = PContractCode;
	}

	public String getPContractName() {
		return this.PContractName;
	}

	public void setPContractName(String PContractName) {
		this.PContractName = PContractName;
	}

	public String getPPaykindCode() {
		return this.PPaykindCode;
	}

	public void setPPaykindCode(String PPaykindCode) {
		this.PPaykindCode = PPaykindCode;
	}

	public String getPPaykindName() {
		return this.PPaykindName;
	}

	public void setPPaykindName(String PPaykindName) {
		this.PPaykindName = PPaykindName;
	}

	public String getPBloodCode() {
		return this.PBloodCode;
	}

	public void setPBloodCode(String PBloodCode) {
		this.PBloodCode = PBloodCode;
	}

	public String getPBloodName() {
		return this.PBloodName;
	}

	public void setPBloodName(String PBloodName) {
		this.PBloodName = PBloodName;
	}

	public String getEducation() {
		return this.education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getDegree() {
		return this.degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getPProvince() {
		return this.PProvince;
	}

	public void setPProvince(String PProvince) {
		this.PProvince = PProvince;
	}

	public String getPCity() {
		return this.PCity;
	}

	public void setPCity(String PCity) {
		this.PCity = PCity;
	}

	public String getPCounty() {
		return this.PCounty;
	}

	public void setPCounty(String PCounty) {
		this.PCounty = PCounty;
	}

	public String getPCertificatestype() {
		return this.PCertificatestype;
	}

	public void setPCertificatestype(String PCertificatestype) {
		this.PCertificatestype = PCertificatestype;
	}

	public String getPCertificatesno() {
		return this.PCertificatesno;
	}

	public void setPCertificatesno(String PCertificatesno) {
		this.PCertificatesno = PCertificatesno;
	}

	public String getPNationality() {
		return this.PNationality;
	}

	public void setPNationality(String PNationality) {
		this.PNationality = PNationality;
	}

	public String getPNationCode() {
		return this.PNationCode;
	}

	public void setPNationCode(String PNationCode) {
		this.PNationCode = PNationCode;
	}

	public String getPNationName() {
		return this.PNationName;
	}

	public void setPNationName(String PNationName) {
		this.PNationName = PNationName;
	}

	public String getPWarriage() {
		return this.PWarriage;
	}

	public void setPWarriage(String PWarriage) {
		this.PWarriage = PWarriage;
	}

	public String getPOccupation() {
		return this.POccupation;
	}

	public void setPOccupation(String POccupation) {
		this.POccupation = POccupation;
	}

	public String getPHandbook() {
		return this.PHandbook;
	}

	public void setPHandbook(String PHandbook) {
		this.PHandbook = PHandbook;
	}

	public Boolean getPIsanaphy() {
		return this.PIsanaphy;
	}

	public void setPIsanaphy(Boolean PIsanaphy) {
		this.PIsanaphy = PIsanaphy;
	}

	public Boolean getPIshepatitis() {
		return this.PIshepatitis;
	}

	public void setPIshepatitis(Boolean PIshepatitis) {
		this.PIshepatitis = PIshepatitis;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}