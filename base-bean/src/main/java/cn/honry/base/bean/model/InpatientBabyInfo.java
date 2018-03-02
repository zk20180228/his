package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：  婴儿信息实体
 * @Author：huangbiao
 * @CreateDate：2016-3-11 17:11 
 * @version 1.0
 *
 */
public class InpatientBabyInfo  extends Entity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String inpatientNo;//住院流水号
	private int happenNo;//发生序号
	private String name;//姓名
	private String sexCode;//性别
	private Date birthDay;//生日
	private double height;//身高
	private double weight;//体重
	private double headSize;//头围
	private String bloodCode;//血型编码
	private Date inDate;//入院日期
	private Date prepayOutDate;//出院日期
	private String operCode;//操作员
	private Date operDate;//操作日期
	private int cancelFlag;//取消标志
	private String motherInpatientNo;//母亲住院流水号
	private String birthCertificateNo;//出生证编号
	private int healthStatus;//健康状况
	private int placeType;//出生地点分类
	private Date issueDate;//签发日期
	private String facility;//接生机构名称
	private String home;//家庭住址
	private String fatherName;//父亲姓名
	private String fatherAge;//父亲年龄
	private String fatherNation;//父亲民族
	private String fatherNationality;//父亲国籍
	private String fatherCardnNo;//父亲身份证号
	private String motherName;//母亲姓名
	private String motherAge;//母亲年龄
	private String motherNation;//母亲民族
	private String motherNationality;//母亲国籍
	private String motherCardNo;//母亲身份证号
	private String gestation;//出生孕周
	private String birtPlace;//出生地点
	private int status;//出生状态
	private String receiver;//接生人
	private String birthAddress;//出生地
	private String home1;
	private String home2;
	private String home3;
	private String home4;
	private String home5;
	//新加字段
	/**性别名称**/
	private String sexName;
	/**操作员名称**/
	private String operName;
	/**医院编码**/
	private Integer hospitalId;
	/**院区编码**/
	private String areaCode;
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
	public String getSexName() {
		return sexName;
	}
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	public String getOperName() {
		return operName;
	}
	public void setOperName(String operName) {
		this.operName = operName;
	}
	public String getHome1() {
		return home1;
	}
	public void setHome1(String home1) {
		this.home1 = home1;
	}
	public String getHome2() {
		return home2;
	}
	public void setHome2(String home2) {
		this.home2 = home2;
	}
	public String getHome3() {
		return home3;
	}
	public void setHome3(String home3) {
		this.home3 = home3;
	}
	public String getHome4() {
		return home4;
	}
	public void setHome4(String home4) {
		this.home4 = home4;
	}
	public String getHome5() {
		return home5;
	}
	public void setHome5(String home5) {
		this.home5 = home5;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public int getHappenNo() {
		return happenNo;
	}
	public void setHappenNo(int happenNo) {
		this.happenNo = happenNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSexCode() {
		return sexCode;
	}
	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}
	public Date getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getHeadSize() {
		return headSize;
	}
	public void setHeadSize(double headSize) {
		this.headSize = headSize;
	}
	public String getBloodCode() {
		return bloodCode;
	}
	public void setBloodCode(String bloodCode) {
		this.bloodCode = bloodCode;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public Date getPrepayOutDate() {
		return prepayOutDate;
	}
	public void setPrepayOutDate(Date prepayOutDate) {
		this.prepayOutDate = prepayOutDate;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public int getCancelFlag() {
		return cancelFlag;
	}
	public void setCancelFlag(int cancelFlag) {
		this.cancelFlag = cancelFlag;
	}
	public String getMotherInpatientNo() {
		return motherInpatientNo;
	}
	public void setMotherInpatientNo(String motherInpatientNo) {
		this.motherInpatientNo = motherInpatientNo;
	}
	public String getBirthCertificateNo() {
		return birthCertificateNo;
	}
	public void setBirthCertificateNo(String birthCertificateNo) {
		this.birthCertificateNo = birthCertificateNo;
	}
	public int getHealthStatus() {
		return healthStatus;
	}
	public void setHealthStatus(int healthStatus) {
		this.healthStatus = healthStatus;
	}
	public int getPlaceType() {
		return placeType;
	}
	public void setPlaceType(int placeType) {
		this.placeType = placeType;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getFatherAge() {
		return fatherAge;
	}
	public void setFatherAge(String fatherAge) {
		this.fatherAge = fatherAge;
	}
	public String getFatherNation() {
		return fatherNation;
	}
	public void setFatherNation(String fatherNation) {
		this.fatherNation = fatherNation;
	}
	public String getFatherNationality() {
		return fatherNationality;
	}
	public void setFatherNationality(String fatherNationality) {
		this.fatherNationality = fatherNationality;
	}
	public String getFatherCardnNo() {
		return fatherCardnNo;
	}
	public void setFatherCardnNo(String fatherCardnNo) {
		this.fatherCardnNo = fatherCardnNo;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public String getMotherAge() {
		return motherAge;
	}
	public void setMotherAge(String motherAge) {
		this.motherAge = motherAge;
	}
	public String getMotherNation() {
		return motherNation;
	}
	public void setMotherNation(String motherNation) {
		this.motherNation = motherNation;
	}
	public String getMotherNationality() {
		return motherNationality;
	}
	public void setMotherNationality(String motherNationality) {
		this.motherNationality = motherNationality;
	}
	public String getMotherCardNo() {
		return motherCardNo;
	}
	public void setMotherCardNo(String motherCardNo) {
		this.motherCardNo = motherCardNo;
	}
	public String getGestation() {
		return gestation;
	}
	public void setGestation(String gestation) {
		this.gestation = gestation;
	}
	public String getBirtPlace() {
		return birtPlace;
	}
	public void setBirtPlace(String birtPlace) {
		this.birtPlace = birtPlace;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getBirthAddress() {
		return birthAddress;
	}
	public void setBirthAddress(String birthAddress) {
		this.birthAddress = birthAddress;
	}
	
	
}
