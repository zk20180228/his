package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class HospInfections extends Entity{
	private static final long serialVersionUID = 1L;
	/*卡片编号*/
	private String report_no;
	/*报卡类别*/
	private Integer report_type;
	/*病历号*/
	private String medicalrecord_id;
	/*病人类型*/
	private Integer patient_type;
	/*姓名*/
	private String patient_name;
	/*患者家长姓名*/
	private String patient_parents;
	/*证件类型*/
	private String certificates_type;
	/*身份证号*/
	private String certificates_no;
	/*性别*/
	private String report_sex;
	/*出生日期*/
	private String report_birthday;
	/*年龄*/
	private Integer report_age;
	/*年龄单位*/
	private String report_ageunit;
	/*患者职业*/
	private String patient_profession;
	/*其他职业*/
	private String other_profession;
	/*工作单位*/
	private String work_place;
	/*联系电话*/
	private String telephone;
	/*病人来源*/
	private String home_area;
	/*现住址*/
	private String home_couty;
	/*镇,乡*/
	private String home_town;
	/*村（门牌号）*/
	private String home_address;
	/*就诊卡号*/
	private String idcard_no;
	/*床号*/
	private String ded_no;
	/*入院日期*/
	private String into_day;
	/*感染日期*/
	private String infection_day;
	/*入院诊断*/
	private String intodiagnosis;
	/*感染诊断*/
	private String infectiondiagnosis;
	/*感染部位*/
	private String infectionsite;
	/*感染预后*/
	private String afterinfection;
	/*病原学检查*/
	private String etiological;
	/*标本名称*/
	private String specimen;
	/*病原体*/
	private String pathogen;
	/*易感因素*/
	private String dactor;
	/*手术名称*/
	private String operation_name;
	/*手术日期*/
	private String operation_day;
	/*切口类型*/
	private String incisiontype;
	/*药物名称*/
	private String medicine_name;
	/*剂量*/
	private String medicine_dose;
	/*给药方式*/
	private String medicine_mode;
	/*用药频数*/
	private String medicine_frequency;
	/*开始用药时间*/
	private String medicine_begin_day;
	/*结束用药时间*/
	private String medicine_end_day;
	/*备注*/
	private String remarks;
	public String getReport_no() {
		return report_no;
	}
	public void setReport_no(String report_no) {
		this.report_no = report_no;
	}
	public Integer getReport_type() {
		return report_type;
	}
	public void setReport_type(Integer report_type) {
		this.report_type = report_type;
	}
	public String getMedicalrecord_id() {
		return medicalrecord_id;
	}
	public void setMedicalrecord_id(String medicalrecord_id) {
		this.medicalrecord_id = medicalrecord_id;
	}
	public Integer getPatient_type() {
		return patient_type;
	}
	public void setPatient_type(Integer patient_type) {
		this.patient_type = patient_type;
	}
	public String getPatient_name() {
		return patient_name;
	}
	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}
	public String getPatient_parents() {
		return patient_parents;
	}
	public void setPatient_parents(String patient_parents) {
		this.patient_parents = patient_parents;
	}
	public String getCertificates_type() {
		return certificates_type;
	}
	public void setCertificates_type(String certificates_type) {
		this.certificates_type = certificates_type;
	}
	public String getCertificates_no() {
		return certificates_no;
	}
	public void setCertificates_no(String certificates_no) {
		this.certificates_no = certificates_no;
	}
	public String getReport_sex() {
		return report_sex;
	}
	public void setReport_sex(String report_sex) {
		this.report_sex = report_sex;
	}
	public String getReport_birthday() {
		return report_birthday;
	}
	public void setReport_birthday(String report_birthday) {
		this.report_birthday = report_birthday;
	}
	public Integer getReport_age() {
		return report_age;
	}
	public void setReport_age(Integer report_age) {
		this.report_age = report_age;
	}
	public String getReport_ageunit() {
		return report_ageunit;
	}
	public void setReport_ageunit(String report_ageunit) {
		this.report_ageunit = report_ageunit;
	}
	public String getPatient_profession() {
		return patient_profession;
	}
	public void setPatient_profession(String patient_profession) {
		this.patient_profession = patient_profession;
	}
	public String getOther_profession() {
		return other_profession;
	}
	public void setOther_profession(String other_profession) {
		this.other_profession = other_profession;
	}
	public String getWork_place() {
		return work_place;
	}
	public void setWork_place(String work_place) {
		this.work_place = work_place;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getHome_area() {
		return home_area;
	}
	public void setHome_area(String home_area) {
		this.home_area = home_area;
	}
	public String getHome_couty() {
		return home_couty;
	}
	public void setHome_couty(String home_couty) {
		this.home_couty = home_couty;
	}
	public String getHome_town() {
		return home_town;
	}
	public void setHome_town(String home_town) {
		this.home_town = home_town;
	}
	public String getHome_address() {
		return home_address;
	}
	public void setHome_address(String home_address) {
		this.home_address = home_address;
	}
	public String getIdcard_no() {
		return idcard_no;
	}
	public void setIdcard_no(String idcard_no) {
		this.idcard_no = idcard_no;
	}
	public String getDed_no() {
		return ded_no;
	}
	public void setDed_no(String ded_no) {
		this.ded_no = ded_no;
	}
	public String getInto_day() {
		return into_day;
	}
	public void setInto_day(String into_day) {
		this.into_day = into_day;
	}
	public String getInfection_day() {
		return infection_day;
	}
	public void setInfection_day(String infection_day) {
		this.infection_day = infection_day;
	}
	public String getIntodiagnosis() {
		return intodiagnosis;
	}
	public void setIntodiagnosis(String intodiagnosis) {
		this.intodiagnosis = intodiagnosis;
	}
	public String getInfectiondiagnosis() {
		return infectiondiagnosis;
	}
	public void setInfectiondiagnosis(String infectiondiagnosis) {
		this.infectiondiagnosis = infectiondiagnosis;
	}
	public String getInfectionsite() {
		return infectionsite;
	}
	public void setInfectionsite(String infectionsite) {
		this.infectionsite = infectionsite;
	}
	public String getAfterinfection() {
		return afterinfection;
	}
	public void setAfterinfection(String afterinfection) {
		this.afterinfection = afterinfection;
	}
	public String getEtiological() {
		return etiological;
	}
	public void setEtiological(String etiological) {
		this.etiological = etiological;
	}
	public String getSpecimen() {
		return specimen;
	}
	public void setSpecimen(String specimen) {
		this.specimen = specimen;
	}
	public String getPathogen() {
		return pathogen;
	}
	public void setPathogen(String pathogen) {
		this.pathogen = pathogen;
	}
	public String getDactor() {
		return dactor;
	}
	public void setDactor(String dactor) {
		this.dactor = dactor;
	}
	public String getOperation_name() {
		return operation_name;
	}
	public void setOperation_name(String operation_name) {
		this.operation_name = operation_name;
	}
	public String getOperation_day() {
		return operation_day;
	}
	public void setOperation_day(String operation_day) {
		this.operation_day = operation_day;
	}
	public String getIncisiontype() {
		return incisiontype;
	}
	public void setIncisiontype(String incisiontype) {
		this.incisiontype = incisiontype;
	}
	public String getMedicine_name() {
		return medicine_name;
	}
	public void setMedicine_name(String medicine_name) {
		this.medicine_name = medicine_name;
	}
	public String getMedicine_dose() {
		return medicine_dose;
	}
	public void setMedicine_dose(String medicine_dose) {
		this.medicine_dose = medicine_dose;
	}
	public String getMedicine_mode() {
		return medicine_mode;
	}
	public void setMedicine_mode(String medicine_mode) {
		this.medicine_mode = medicine_mode;
	}
	public String getMedicine_frequency() {
		return medicine_frequency;
	}
	public void setMedicine_frequency(String medicine_frequency) {
		this.medicine_frequency = medicine_frequency;
	}
	public String getMedicine_begin_day() {
		return medicine_begin_day;
	}
	public void setMedicine_begin_day(String medicine_begin_day) {
		this.medicine_begin_day = medicine_begin_day;
	}
	public String getMedicine_end_day() {
		return medicine_end_day;
	}
	public void setMedicine_end_day(String medicine_end_day) {
		this.medicine_end_day = medicine_end_day;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
