package cn.honry.inner.statistics.wordLoadDoctorTotal.vo;

public class Dashboard {
	private String value;
	private String name;
	private String dept;
	private String stat;
	private String inhost;
	private String coun;
	private Double douValue;
	private String doctor;
	private String classType;
	private Integer num;
	private String code;
	private String date;
	private String date1;
	private String deptCode;
	private String docCode;
	
	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDocCode() {
		return docCode;
	}

	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	public String getValue() {
		return value;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setValue(String value) {
		this.value = value;
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
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public String getInhost() {
		return inhost;
	}
	public void setInhost(String inhost) {
		this.inhost = inhost;
	}
	public String getCoun() {
		return coun;
	}
	public void setCoun(String coun) {
		this.coun = coun;
	}
	public Double getDouValue() {
		return douValue;
	}
	public void setDouValue(Double douValue) {
		this.douValue = douValue;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDate1() {
		return date1;
	}
	public void setDate1(String date1) {
		this.date1 = date1;
	}
	@Override
	public String toString() {
		return "Dashboard [value=" + value + ", name=" + name + ", dept="
				+ dept + ", stat=" + stat + ", inhost=" + inhost + ", coun="
				+ coun + ", douValue=" + douValue + ", doctor=" + doctor
				+ ", classType=" + classType + ", num=" + num + ", code="
				+ code + ", date1=" + date1 + "]";
	}
	
}
