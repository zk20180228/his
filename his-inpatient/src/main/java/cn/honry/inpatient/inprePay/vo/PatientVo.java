package cn.honry.inpatient.inprePay.vo;

import java.util.Date;

/**
 *
 * @Title: PatientVo.java
 * @Description：用于前后交互的患者信息对象
 * @Author：aizhonghua
 * @CreateDate：2016年4月11日 下午5:24:30 
 * @Modifier：
 * @ModifyDate：
 * @ModifyRmk：  
 * @version： 1.0：
 *
 */
public class PatientVo {
	/**
	 * 病历号
	 */
	private String medicale;
	/**
	 * 住院流水号
	 */
	private String inpatientNo;
	/**
	 * 就诊卡号
	 */
	private String idcard;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别
	 */
	private Integer sex;
	/**
	 * 证件类型
	 */
	private String certificatesType;
	/**
	 * 证件号
	 */
	private String certificatesNo;
	/**
	 * 出生日期
	 */
	private Date birthDay;
	/**
	 * 民族
	 */
	private String nation;
	/**
	 * 联系方式
	 */
	private String phone;
	/**
	 * 医保号
	 */
	private String handBook;
	/**
	 * 单日消费额度
	 */
	private Double dayLimit;
	/**
	 * 籍贯
	 */
	private String nationAlity;
	
	/**
	 * 性别
	 */
	private String sexName;
	/**
	 * 住院状态
	 */
	private String  inState;
	
	
	public String getInState() {
		return inState;
	}
	public void setInState(String inState) {
		this.inState = inState;
	}
	public String getSexName() {
		return sexName;
	}
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	public String getMedicale() {
		return medicale;
	}
	public void setMedicale(String medicale) {
		this.medicale = medicale;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
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
	public Date getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getHandBook() {
		return handBook;
	}
	public void setHandBook(String handBook) {
		this.handBook = handBook;
	}
	public Double getDayLimit() {
		return dayLimit;
	}
	public void setDayLimit(Double dayLimit) {
		this.dayLimit = dayLimit;
	}
	public String getNationAlity() {
		return nationAlity;
	}
	public void setNationAlity(String nationAlity) {
		this.nationAlity = nationAlity;
	}
	
}
