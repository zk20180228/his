package cn.honry.outpatient.itemlist.vo;

public class InfoVo {
	private String name;//患者姓名
	private Integer sex;//性别
	private Double age;//年龄
	private String dept;//看诊科室
	private String doc;//看诊医生
	private String contractunit;//合同单位
	private String grade;//等级编码
	private String idcardId;//就诊卡号
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Double getAge() {
		return age;
	}
	public void setAge(Double age) {
		this.age = age;
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
	public String getContractunit() {
		return contractunit;
	}
	public void setContractunit(String contractunit) {
		this.contractunit = contractunit;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getIdcardId() {
		return idcardId;
	}
	public void setIdcardId(String idcardId) {
		this.idcardId = idcardId;
	}
	
}
