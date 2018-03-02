package cn.honry.outpatient.advice.vo;

public class PatientVo {
	
	/**就诊卡号**/
	private String idCardNo;
	/**姓名**/
	private String name;
	/**科室**/
	private String dept;
	/**医生**/
	private String doc;
	/**门诊号**/
	private String clinicNo;
	/**合同单位**/
	private String assUnit;
	/**病历号**/
	private String caseNo;
	/**性别**/
	private String sex;
	/**年龄**/
	private String age;
	/**顺序号**/
	private Integer orderNo;
	/**挂号日期**/
	private String regDate;
	/**状态**/
	private String ynsee;
	
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
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
	public String getDoc() {
		return doc;
	}
	public void setDoc(String doc) {
		this.doc = doc;
	}
	public String getClinicNo() {
		return clinicNo;
	}
	public void setClinicNo(String clinicNo) {
		this.clinicNo = clinicNo;
	}
	public String getAssUnit() {
		return assUnit;
	}
	public void setAssUnit(String assUnit) {
		this.assUnit = assUnit;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getYnsee() {
		return ynsee;
	}
	public void setYnsee(String ynsee) {
		this.ynsee = ynsee;
	}
	
}
