package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiBasePatient entity. @author MyEclipse Persistence Tools
 */

public class BiBasePatient implements java.io.Serializable {

	// Fields

	private String patinentId;
	private String cardNo;
	private String unitCode;
	private String unitName;
	private String patientPaykindCode;
	private String patientPaykind;
	private String mcardNo;
	private String siCard;
	private String patientName;
	private Date patientBirthday;
	private String patientSexCode;
	private String patientSex;
	private String patientBloodcode;
	private String patientBloodname;
	private String patientAddress;
	private String provinceCode;
	private String patientAddressProvince;
	private String cityCode;
	private String patientAddressCity;
	private String districtCode;
	private String patientAddressDistrict;
	private String patientNationCode;
	private String patientNation;
	private String patientNativeplace;
	private String patientNationality;
	private String patientWarriageCode;
	private String patientWarriage;
	private String employeeEducationCode;
	private String employeeEducation;
	private String patientOccupationCode;
	private String patientOccupation;
	private String patientIsanaphy;
	private String patientIshepatitis;
	private String validFlg;

	// Constructors

	/** default constructor */
	public BiBasePatient() {
	}

	/** minimal constructor */
	public BiBasePatient(String patientName) {
		this.patientName = patientName;
	}

	/** full constructor */
	public BiBasePatient(String cardNo, String unitCode, String unitName,
			String patientPaykindCode, String patientPaykind, String mcardNo,
			String siCard, String patientName, Date patientBirthday,
			String patientSexCode, String patientSex, String patientBloodcode,
			String patientBloodname, String patientAddress,
			String provinceCode, String patientAddressProvince,
			String cityCode, String patientAddressCity, String districtCode,
			String patientAddressDistrict, String patientNationCode,
			String patientNation, String patientNativeplace,
			String patientNationality, String patientWarriageCode,
			String patientWarriage, String employeeEducationCode,
			String employeeEducation, String patientOccupationCode,
			String patientOccupation, String patientIsanaphy,
			String patientIshepatitis, String validFlg) {
		this.cardNo = cardNo;
		this.unitCode = unitCode;
		this.unitName = unitName;
		this.patientPaykindCode = patientPaykindCode;
		this.patientPaykind = patientPaykind;
		this.mcardNo = mcardNo;
		this.siCard = siCard;
		this.patientName = patientName;
		this.patientBirthday = patientBirthday;
		this.patientSexCode = patientSexCode;
		this.patientSex = patientSex;
		this.patientBloodcode = patientBloodcode;
		this.patientBloodname = patientBloodname;
		this.patientAddress = patientAddress;
		this.provinceCode = provinceCode;
		this.patientAddressProvince = patientAddressProvince;
		this.cityCode = cityCode;
		this.patientAddressCity = patientAddressCity;
		this.districtCode = districtCode;
		this.patientAddressDistrict = patientAddressDistrict;
		this.patientNationCode = patientNationCode;
		this.patientNation = patientNation;
		this.patientNativeplace = patientNativeplace;
		this.patientNationality = patientNationality;
		this.patientWarriageCode = patientWarriageCode;
		this.patientWarriage = patientWarriage;
		this.employeeEducationCode = employeeEducationCode;
		this.employeeEducation = employeeEducation;
		this.patientOccupationCode = patientOccupationCode;
		this.patientOccupation = patientOccupation;
		this.patientIsanaphy = patientIsanaphy;
		this.patientIshepatitis = patientIshepatitis;
		this.validFlg = validFlg;
	}

	// Property accessors

	public String getPatinentId() {
		return this.patinentId;
	}

	public void setPatinentId(String patinentId) {
		this.patinentId = patinentId;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getUnitCode() {
		return this.unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getPatientPaykindCode() {
		return this.patientPaykindCode;
	}

	public void setPatientPaykindCode(String patientPaykindCode) {
		this.patientPaykindCode = patientPaykindCode;
	}

	public String getPatientPaykind() {
		return this.patientPaykind;
	}

	public void setPatientPaykind(String patientPaykind) {
		this.patientPaykind = patientPaykind;
	}

	public String getMcardNo() {
		return this.mcardNo;
	}

	public void setMcardNo(String mcardNo) {
		this.mcardNo = mcardNo;
	}

	public String getSiCard() {
		return this.siCard;
	}

	public void setSiCard(String siCard) {
		this.siCard = siCard;
	}

	public String getPatientName() {
		return this.patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Date getPatientBirthday() {
		return this.patientBirthday;
	}

	public void setPatientBirthday(Date patientBirthday) {
		this.patientBirthday = patientBirthday;
	}

	public String getPatientSexCode() {
		return this.patientSexCode;
	}

	public void setPatientSexCode(String patientSexCode) {
		this.patientSexCode = patientSexCode;
	}

	public String getPatientSex() {
		return this.patientSex;
	}

	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	public String getPatientBloodcode() {
		return this.patientBloodcode;
	}

	public void setPatientBloodcode(String patientBloodcode) {
		this.patientBloodcode = patientBloodcode;
	}

	public String getPatientBloodname() {
		return this.patientBloodname;
	}

	public void setPatientBloodname(String patientBloodname) {
		this.patientBloodname = patientBloodname;
	}

	public String getPatientAddress() {
		return this.patientAddress;
	}

	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}

	public String getProvinceCode() {
		return this.provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getPatientAddressProvince() {
		return this.patientAddressProvince;
	}

	public void setPatientAddressProvince(String patientAddressProvince) {
		this.patientAddressProvince = patientAddressProvince;
	}

	public String getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getPatientAddressCity() {
		return this.patientAddressCity;
	}

	public void setPatientAddressCity(String patientAddressCity) {
		this.patientAddressCity = patientAddressCity;
	}

	public String getDistrictCode() {
		return this.districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getPatientAddressDistrict() {
		return this.patientAddressDistrict;
	}

	public void setPatientAddressDistrict(String patientAddressDistrict) {
		this.patientAddressDistrict = patientAddressDistrict;
	}

	public String getPatientNationCode() {
		return this.patientNationCode;
	}

	public void setPatientNationCode(String patientNationCode) {
		this.patientNationCode = patientNationCode;
	}

	public String getPatientNation() {
		return this.patientNation;
	}

	public void setPatientNation(String patientNation) {
		this.patientNation = patientNation;
	}

	public String getPatientNativeplace() {
		return this.patientNativeplace;
	}

	public void setPatientNativeplace(String patientNativeplace) {
		this.patientNativeplace = patientNativeplace;
	}

	public String getPatientNationality() {
		return this.patientNationality;
	}

	public void setPatientNationality(String patientNationality) {
		this.patientNationality = patientNationality;
	}

	public String getPatientWarriageCode() {
		return this.patientWarriageCode;
	}

	public void setPatientWarriageCode(String patientWarriageCode) {
		this.patientWarriageCode = patientWarriageCode;
	}

	public String getPatientWarriage() {
		return this.patientWarriage;
	}

	public void setPatientWarriage(String patientWarriage) {
		this.patientWarriage = patientWarriage;
	}

	public String getEmployeeEducationCode() {
		return this.employeeEducationCode;
	}

	public void setEmployeeEducationCode(String employeeEducationCode) {
		this.employeeEducationCode = employeeEducationCode;
	}

	public String getEmployeeEducation() {
		return this.employeeEducation;
	}

	public void setEmployeeEducation(String employeeEducation) {
		this.employeeEducation = employeeEducation;
	}

	public String getPatientOccupationCode() {
		return this.patientOccupationCode;
	}

	public void setPatientOccupationCode(String patientOccupationCode) {
		this.patientOccupationCode = patientOccupationCode;
	}

	public String getPatientOccupation() {
		return this.patientOccupation;
	}

	public void setPatientOccupation(String patientOccupation) {
		this.patientOccupation = patientOccupation;
	}

	public String getPatientIsanaphy() {
		return this.patientIsanaphy;
	}

	public void setPatientIsanaphy(String patientIsanaphy) {
		this.patientIsanaphy = patientIsanaphy;
	}

	public String getPatientIshepatitis() {
		return this.patientIshepatitis;
	}

	public void setPatientIshepatitis(String patientIshepatitis) {
		this.patientIshepatitis = patientIshepatitis;
	}

	public String getValidFlg() {
		return this.validFlg;
	}

	public void setValidFlg(String validFlg) {
		this.validFlg = validFlg;
	}

}