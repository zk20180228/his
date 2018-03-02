package cn.honry.outpatient.advice.vo;

public class IreportPatientVo {
	private String name;//患者姓名
	private String sex;//性别:从性别编码表读取
	private String age;//年龄
	private String dept;//科室名称
	private String medicalrecordid;//病历号
	private String historyspecil;//病史和特征
	private String diagnse;//诊断1
	private String advice;//医嘱建议
	private String address;//就诊地址
	private String dd;//执行科室名称
	private String pay;//总金额
	private String itemName;//处方
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getMedicalrecordid() {
		return medicalrecordid;
	}
	public void setMedicalrecordid(String medicalrecordid) {
		this.medicalrecordid = medicalrecordid;
	}
	public String getHistoryspecil() {
		return historyspecil;
	}
	public void setHistoryspecil(String historyspecil) {
		this.historyspecil = historyspecil;
	}
	public String getDiagnse() {
		return diagnse;
	}
	public void setDiagnse(String diagnse) {
		this.diagnse = diagnse;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDd() {
		return dd;
	}
	public void setDd(String dd) {
		this.dd = dd;
	}
	public String getPay() {
		return pay;
	}
	public void setPay(String pay) {
		this.pay = pay;
	}
	
}
