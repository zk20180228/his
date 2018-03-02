package cn.honry.patinent.register.patientInfoCollection.vo;

import java.util.Date;

public class Collection {
	private String id;
	private String name;
	private Integer sex;
	private Date birthday;
	private String address;
	private String phone;
	private String dept;
	private String grade;
	private String emp;
	private String certificatestype;
	private String certificatesno;
	private String occupation;
	private String birthplace;
	private String nationality;
	private String nation;
	private String workunit;
	private String workphone;
	private String warriage;
	private String linkman;
	private String linkphone;
	private Integer status;
	private Integer state;
	/**家庭地址具体省市县**/
	private String patientCity;
	/**
	 * 省市县三级联动
	 */
	/**
	 * 第一层ID
	 */
	private String oneId;
	/**
	 * 第一层名字(省名)
	 */
	private String oneName;
	/**
	 * 第二层ID
	 */
	private String twoId;
	/**
	 * 第二层名字(市名)
	 */
	private String twoName;
	/**
	 * 第三层ID
	 */
	private String threeId;
	/**
	 * 第三层名字(县名)
	 */
	private String threeName;
	/**
	 * 第四层ID
	 */
	private String fourId;
	/**
	 * 第四层名字(区名)
	 */
	private String fourName;
	
	
	/**
	 * 第一层ID
	 */
	private String oneCode;
	/**
	 * 第二层ID
	 */
	private String twoCode;
	/**
	 * 第三层ID
	 */
	private String threeCode;
	/**
	 * 第四层ID
	 */
	private String fourCode;
	
	
	/**
	 * 页面回显的时候用到
	 */
	
	
	public String getId() {
		return id;
	}
	public String getOneCode() {
		return oneCode;
	}
	public void setOneCode(String oneCode) {
		this.oneCode = oneCode;
	}
	public String getTwoCode() {
		return twoCode;
	}
	public void setTwoCode(String twoCode) {
		this.twoCode = twoCode;
	}
	public String getThreeCode() {
		return threeCode;
	}
	public void setThreeCode(String threeCode) {
		this.threeCode = threeCode;
	}
	public String getFourCode() {
		return fourCode;
	}
	public void setFourCode(String fourCode) {
		this.fourCode = fourCode;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCertificatestype() {
		return certificatestype;
	}
	public void setCertificatestype(String certificatestype) {
		this.certificatestype = certificatestype;
	}
	public String getCertificatesno() {
		return certificatesno;
	}
	public void setCertificatesno(String certificatesno) {
		this.certificatesno = certificatesno;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getBirthplace() {
		return birthplace;
	}
	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getWorkunit() {
		return workunit;
	}
	public void setWorkunit(String workunit) {
		this.workunit = workunit;
	}
	public String getWorkphone() {
		return workphone;
	}
	public void setWorkphone(String workphone) {
		this.workphone = workphone;
	}
	public String getWarriage() {
		return warriage;
	}
	public void setWarriage(String warriage) {
		this.warriage = warriage;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	
	public String getLinkphone() {
		return linkphone;
	}
	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getEmp() {
		return emp;
	}
	public void setEmp(String emp) {
		this.emp = emp;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPatientCity() {
		return patientCity;
	}
	public void setPatientCity(String patientCity) {
		this.patientCity = patientCity;
	}
	public String getOneId() {
		return oneId;
	}
	public void setOneId(String oneId) {
		this.oneId = oneId;
	}
	public String getOneName() {
		return oneName;
	}
	public void setOneName(String oneName) {
		this.oneName = oneName;
	}
	public String getTwoId() {
		return twoId;
	}
	public void setTwoId(String twoId) {
		this.twoId = twoId;
	}
	public String getTwoName() {
		return twoName;
	}
	public void setTwoName(String twoName) {
		this.twoName = twoName;
	}
	public String getThreeId() {
		return threeId;
	}
	public void setThreeId(String threeId) {
		this.threeId = threeId;
	}
	public String getThreeName() {
		return threeName;
	}
	public void setThreeName(String threeName) {
		this.threeName = threeName;
	}
	public String getFourId() {
		return fourId;
	}
	public void setFourId(String fourId) {
		this.fourId = fourId;
	}
	public String getFourName() {
		return fourName;
	}
	public void setFourName(String fourName) {
		this.fourName = fourName;
	}
	
	

}
