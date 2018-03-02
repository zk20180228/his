package cn.honry.statistics.deptstat.deathPatient.vo;

import java.sql.Date;


/**  
 * 
 * 患者死亡信息统计
 * @Author: wangshujuan
 * @CreateDate: 2017年11月14日 下午4:09:43 
 * @Modifier: wangshujuan
 * @ModifyDate: 2017年11月14日 下午4:09:43 
 * @ModifyRmk:  
 * @version: V1.0
 * @param deptCode 
 *
 */
public class DeathPatientVo {
	private String deptCode;//部门Code
	private String deptName;//部门名称
	private String patientNo;//病历号
	private String cardNo;//就诊卡号
	private String patientName;//姓名
	private String sex;//性别
	private Date birthday;//出生日期
	private int age;//年龄
	private String ageunit;//年龄单位
	private Date inDate;//入院时间
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
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAgeunit() {
		return ageunit;
	}
	public void setAgeunit(String ageunit) {
		this.ageunit = ageunit;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
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
