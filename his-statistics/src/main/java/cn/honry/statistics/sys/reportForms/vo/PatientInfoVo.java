package cn.honry.statistics.sys.reportForms.vo;

public class PatientInfoVo {
	private String medicalrecordId;//病例号
	private String name;//患者姓名
	private String nameId;//患者id
	private Integer sex;//患者性别
	private String age;//年龄
	//private Integer ageunit;//年龄单位
	private String address;//地域
	private String tyepPatient;//患者类型
	private String tyep;//疾病类型
	private String dept;//科室
	private String doctor;//责任医生 
	private Double medicalExpense = 0.0;//医疗费用 门诊
	private Double drugCost = 0.0;//药品费用 门诊
	private Double total = 0.0;//费用总计 门诊
	private Integer days = 0;//天数
	private Double medicalExpensez = 0.0;//医疗费用 住院
	private Double drugCostz = 0.0;//药品费用 住院
	private Double otherExpensesz = 0.0;//其他费用 住院
	private Double totalz = 0.0;//费用总计 住院
	private Double totalall = 0.0 ;//费用总计 全部

	public Double getTotalall() {
		return totalall;
	}
	public void setTotalall(Double totalall) {
		this.totalall = totalall;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameId() {
		return nameId;
	}
	public void setNameId(String nameId) {
		this.nameId = nameId;
	}
	
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTyepPatient() {
		return tyepPatient;
	}
	public void setTyepPatient(String tyepPatient) {
		this.tyepPatient = tyepPatient;
	}
	public String getTyep() {
		return tyep;
	}
	public void setTyep(String tyep) {
		this.tyep = tyep;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public Double getMedicalExpense() {
		return medicalExpense;
	}
	public void setMedicalExpense(Double medicalExpense) {
		this.medicalExpense = medicalExpense;
	}
	public Double getDrugCost() {
		return drugCost;
	}
	public void setDrugCost(Double drugCost) {
		this.drugCost = drugCost;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public Double getMedicalExpensez() {
		return medicalExpensez;
	}
	public void setMedicalExpensez(Double medicalExpensez) {
		this.medicalExpensez = medicalExpensez;
	}
	public Double getDrugCostz() {
		return drugCostz;
	}
	public void setDrugCostz(Double drugCostz) {
		this.drugCostz = drugCostz;
	}
	public Double getOtherExpensesz() {
		return otherExpensesz;
	}
	public void setOtherExpensesz(Double otherExpensesz) {
		this.otherExpensesz = otherExpensesz;
	}
	public Double getTotalz() {
		return totalz;
	}
	public void setTotalz(Double totalz) {
		this.totalz = totalz;
	}
	
	
	
	
	
	
	
	
	

}
