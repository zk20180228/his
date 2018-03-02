package cn.honry.statistics.deptstat.outandinpatient.vo;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InPatientVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**住院号**/
	private String patientno;
	/**姓名**/
	private String name;
	/**性别**/
	private String sex;
	/**年龄**/
	private Integer age;
	/**年龄单位**/
	private String ageUnit;
	/**床位号**/
	private String bedNumber;
	/**床位号**/
	private String job;
	/**入院日期**/
	private Date indate;
	/**地址**/
	private String adress;
	/**电话号**/
	private String tel;
	/**费别**/
	private String pact;
	/**诊断**/
	private String clinic;
	
	public String getAgeUnit() {
		return ageUnit;
	}
	public void setAgeUnit(String ageUnit) {
		this.ageUnit = ageUnit;
	}
	public String getPatientno() {
		return patientno;
	}
	public void setPatientno(String patientno) {
		this.patientno = patientno;
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
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getBedNumber() {
		return bedNumber;
	}
	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}
	public Date getIndate() {
		return indate;
	}
	public void setIndate(Date indate) {
		this.indate = indate;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getPact() {
		return pact;
	}
	public void setPact(String pact) {
		this.pact = pact;
	}
	public String getClinic() {
		return clinic;
	}
	public void setClinic(String clinic) {
		this.clinic = clinic;
	}
	@Override
	public String toString() {
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return  (patientno==null?"":patientno) + "," + (name==null?"":name)
				+ "," + (sex==null?"":sex) + "," + (age+ageUnit) + "," + (bedNumber==null?"":bedNumber)
				+ "," + (indate==null?"":df.format(indate)) + "," + adress
				+ "," + (tel==null?"":tel) + "," + (pact==null?"":pact) + "," + (clinic==null?"":clinic)
				+ ",";
	}
	
}
