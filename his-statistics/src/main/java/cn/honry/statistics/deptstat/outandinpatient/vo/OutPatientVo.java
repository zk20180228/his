package cn.honry.statistics.deptstat.outandinpatient.vo;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import freemarker.template.SimpleDate;

public class OutPatientVo implements Serializable{

	/**  
	 * 
	 * <p> </p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月7日 下午3:06:20 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月7日 下午3:06:20 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
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
	/**主治医生**/
	private String doctor;
	/**主管护士**/
	private String nurse;
	/**入院日期**/
	private Date indate;
	/**出院日期**/
	private Date outdate;
	/**出院情况**/
	private String status;
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
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getNurse() {
		return nurse;
	}
	public void setNurse(String nurse) {
		this.nurse = nurse;
	}
	public Date getIndate() {
		return indate;
	}
	public void setIndate(Date indate) {
		this.indate = indate;
	}
	public Date getOutdate() {
		return outdate;
	}
	public void setOutdate(Date outdate) {
		this.outdate = outdate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
		
		return  patientno + "," + name
				+ "," + (sex==null?"":sex )+ "," + (age+ageUnit )+ "," + bedNumber
				+ "," + doctor + "," + nurse + ","
				+ (indate==null?"":df.format(indate)) + "," + (outdate==null?"": df.format(outdate)) +"," + status
				+ "," + pact + "," + clinic + ",";
	}
	
	
}
