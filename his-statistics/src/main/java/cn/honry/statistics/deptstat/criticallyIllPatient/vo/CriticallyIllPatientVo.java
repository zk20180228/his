package cn.honry.statistics.deptstat.criticallyIllPatient.vo;


/**  
 * 
 * 病危患者信息统计
 * @Author: wangshujuan
 * @CreateDate: 2017年11月14日 下午4:09:43 
 * @Modifier: wangshujuan
 * @ModifyDate: 2017年11月14日 下午4:09:43 
 * @ModifyRmk:  
 * @version: V1.0
 * @param deptCode 
 *
 */
public class CriticallyIllPatientVo {
	private String deptCode;//部门Code
	private String deptName;//部门名称
	private String medicalrecordId;//病历号
	private String idcardNo;//就诊卡号
	private String patientName;//姓名
	private String sex;//性别
	private String birthday;//出生日期
	private String age;//年龄
	private String inDate;//入院时间
	private String inCircs;//诊断情况
	private String diagName;//诊断名称
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getIdcardNo() {
		return idcardNo;
	}
	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getInCircs() {
		return inCircs;
	}
	public void setInCircs(String inCircs) {
		this.inCircs = inCircs;
	}
	public String getDiagName() {
		return diagName;
	}
	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}
}
