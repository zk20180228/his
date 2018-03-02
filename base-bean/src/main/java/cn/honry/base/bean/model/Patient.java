package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

@SuppressWarnings("serial")
public class Patient extends Entity implements java.io.Serializable {

	/**  合同单位CODE **/
	private String unit;
	/**  血型 **/
	private String patientBloodcode;
	/**  患者年龄 **/
	private Double patientAge;
	/**  年 月  天 **/
	private String patientAgeunit;
	/**  户口或家庭邮编 **/
	private String patientHomezip;
	/**  单位邮编 **/
	private String patientWorkzip;
	/**  是否药物过敏  0:不过敏 1：过敏**/
	private Integer patientIsanaphy=0;
	/** 是否重大疾病  0：没有 1：有**/
	private Integer patientIshepatitis=0;
	/** 患者姓名  **/
	private String patientName;
	/** 拼音码  **/
	private String patientPinyin;
	/** 五笔码  **/
	private String patientWb;
	/** 自定义码	  **/
	private String patientInputcode;
	/** 性别  **/
	private Integer patientSex;
	/** 出生日期  **/
	private Date patientBirthday;
	/** 家庭地址  **/
	private String patientAddress;
	/**家庭地址具体省市县**/
	private String patientCity;	
	/** 门牌号  **/
	private String patientDoorno;
	/** 电话  **/
	private String patientPhone;
	/**证件类型   **/
	private String patientCertificatestype;
	/** 证件号码  **/
	private String patientCertificatesno;
	/** 出生地  **/
	private String patientBirthplace;
	/** 籍贯  **/
	private String patientNativeplace;
	/**国籍   **/
	private String patientNationality;
	/** 民族  **/
	private String patientNation;
	/** 工作单位  **/
	private String patientWorkunit;
	/** 单位电话  **/
	private String patientWorkphone;
	/** 婚姻状况  **/
	private String patientWarriage;
	/** 职业  **/
	private String patientOccupation;
	/** 医保手册号  **/
	private String patientHandbook;
	/** 电子邮箱  **/
	private String patientEmail;
	/** 母亲姓名  **/
	private String patientMother;
	/** 联系人  **/
	private String patientLinkman;
	/** 联系人关系  **/
	private String patientLinkrelation;
	/** 联系人地址  **/
	private String patientLinkaddress;
	/** 联系人门牌号  **/
	private String patientLinkdoorno;
	/** 联系电话  **/
	private String patientLinkphone;	
	/** 新添加字段 病历号  **/
	private String medicalrecordId;
	/**
	 * 结算类别
	 */
	private String patientPaykind;
	
	/**
	 * 2016年9月27日15:16:57 GH
	 */
	private String cardNo;
	/**
	 * 备注
	 */
	private String reportRemark;
	
	/**
	 * 2016年11月8日zpty新增字段
	 * 病案号
	 */
	private String caseNo;
	
	/**  2016年11月14日zpty新增字段
	 * 住院流水号 **/
	private String inpatientNo;
	/**  2016年11月14日zpty新增字段
	 * 住院次数**/
	private Integer inpatientSum=0;

	/** 出生日期 显示字段 **/
	private String patientBirthdayView;
	
	/**  2017年9月23日zpty对比新数据与历史数据,新增冗余字段**/
	/**  合同单位CODE 与unit相同**/
	private String unitCode;
	/**  所属医院 **/
	private String hospitalId;
	/**  所属院区 **/
	private String areaCode;
	
	public String getReportRemark() {
		return reportRemark;
	}
	public void setReportRemark(String reportRemark) {
		this.reportRemark = reportRemark;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPatientPaykind() {
		return patientPaykind;
	}

	public void setPatientPaykind(String patientPaykind) {
		this.patientPaykind = patientPaykind;
	}

	public String getPatientName() {
		return this.patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientPinyin() {
		return this.patientPinyin;
	}

	public void setPatientPinyin(String patientPinyin) {
		this.patientPinyin = patientPinyin;
	}

	public String getPatientWb() {
		return this.patientWb;
	}

	public void setPatientWb(String patientWb) {
		this.patientWb = patientWb;
	}

	public String getPatientInputcode() {
		return this.patientInputcode;
	}

	public void setPatientInputcode(String patientInputcode) {
		this.patientInputcode = patientInputcode;
	}

	public Integer getPatientSex() {
		return patientSex;
	}

	public void setPatientSex(Integer patientSex) {
		this.patientSex = patientSex;
	}

	public Date getPatientBirthday() {
		return this.patientBirthday;
	}

	public void setPatientBirthday(Date patientBirthday) {
		this.patientBirthday = patientBirthday;
	}

	public String getPatientAddress() {
		return this.patientAddress;
	}

	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}

	public String getPatientDoorno() {
		return this.patientDoorno;
	}

	public void setPatientDoorno(String patientDoorno) {
		this.patientDoorno = patientDoorno;
	}

	public String getPatientPhone() {
		return this.patientPhone;
	}

	public void setPatientPhone(String patientPhone) {
		this.patientPhone = patientPhone;
	}

	public String getPatientCertificatestype() {
		return this.patientCertificatestype;
	}

	public void setPatientCertificatestype(String patientCertificatestype) {
		this.patientCertificatestype = patientCertificatestype;
	}

	public String getPatientCertificatesno() {
		return this.patientCertificatesno;
	}

	public void setPatientCertificatesno(String patientCertificatesno) {
		this.patientCertificatesno = patientCertificatesno;
	}

	public String getPatientBirthplace() {
		return this.patientBirthplace;
	}

	public void setPatientBirthplace(String patientBirthplace) {
		this.patientBirthplace = patientBirthplace;
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

	public String getPatientNation() {
		return this.patientNation;
	}

	public void setPatientNation(String patientNation) {
		this.patientNation = patientNation;
	}

	public String getPatientWorkunit() {
		return this.patientWorkunit;
	}

	public void setPatientWorkunit(String patientWorkunit) {
		this.patientWorkunit = patientWorkunit;
	}

	public String getPatientWorkphone() {
		return this.patientWorkphone;
	}

	public void setPatientWorkphone(String patientWorkphone) {
		this.patientWorkphone = patientWorkphone;
	}

	public String getPatientWarriage() {
		return this.patientWarriage;
	}

	public void setPatientWarriage(String patientWarriage) {
		this.patientWarriage = patientWarriage;
	}

	public String getPatientOccupation() {
		return this.patientOccupation;
	}

	public void setPatientOccupation(String patientOccupation) {
		this.patientOccupation = patientOccupation;
	}

	public String getPatientHandbook() {
		return this.patientHandbook;
	}

	public void setPatientHandbook(String patientHandbook) {
		this.patientHandbook = patientHandbook;
	}

	public String getPatientEmail() {
		return this.patientEmail;
	}

	public void setPatientEmail(String patientEmail) {
		this.patientEmail = patientEmail;
	}

	public String getPatientMother() {
		return this.patientMother;
	}

	public void setPatientMother(String patientMother) {
		this.patientMother = patientMother;
	}

	public String getPatientLinkman() {
		return this.patientLinkman;
	}

	public void setPatientLinkman(String patientLinkman) {
		this.patientLinkman = patientLinkman;
	}

	public String getPatientLinkrelation() {
		return this.patientLinkrelation;
	}

	public void setPatientLinkrelation(String patientLinkrelation) {
		this.patientLinkrelation = patientLinkrelation;
	}

	public String getPatientLinkaddress() {
		return this.patientLinkaddress;
	}

	public void setPatientLinkaddress(String patientLinkaddress) {
		this.patientLinkaddress = patientLinkaddress;
	}

	public String getPatientLinkdoorno() {
		return this.patientLinkdoorno;
	}

	public void setPatientLinkdoorno(String patientLinkdoorno) {
		this.patientLinkdoorno = patientLinkdoorno;
	}

	public String getPatientLinkphone() {
		return this.patientLinkphone;
	}

	public void setPatientLinkphone(String patientLinkphone) {
		this.patientLinkphone = patientLinkphone;
	}

	public String getPatientBloodcode() {
		return patientBloodcode;
	}

	public void setPatientBloodcode(String patientBloodcode) {
		this.patientBloodcode = patientBloodcode;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(Double patientAge) {
		this.patientAge = patientAge;
	}

	public String getPatientAgeunit() {
		return patientAgeunit;
	}

	public void setPatientAgeunit(String patientAgeunit) {
		this.patientAgeunit = patientAgeunit;
	}

	public String getPatientHomezip() {
		return patientHomezip;
	}

	public void setPatientHomezip(String patientHomezip) {
		this.patientHomezip = patientHomezip;
	}

	public String getPatientWorkzip() {
		return patientWorkzip;
	}

	public void setPatientWorkzip(String patientWorkzip) {
		this.patientWorkzip = patientWorkzip;
	}

	public Integer getPatientIsanaphy() {
		return patientIsanaphy;
	}

	public void setPatientIsanaphy(Integer patientIsanaphy) {
		this.patientIsanaphy = patientIsanaphy;
	}

	public Integer getPatientIshepatitis() {
		return patientIshepatitis;
	}

	public void setPatientIshepatitis(Integer patientIshepatitis) {
		this.patientIshepatitis = patientIshepatitis;
	}

	public String getMedicalrecordId() {
		return medicalrecordId;
	}

	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}

	public String getPatientCity() {
		return patientCity;
	}

	public void setPatientCity(String patientCity) {
		this.patientCity = patientCity;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public Integer getInpatientSum() {
		return inpatientSum;
	}
	public void setInpatientSum(Integer inpatientSum) {
		this.inpatientSum = inpatientSum;
	}
	public String getPatientBirthdayView() {
		return patientBirthdayView;
	}
	public void setPatientBirthdayView(String patientBirthdayView) {
		this.patientBirthdayView = patientBirthdayView;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	
}