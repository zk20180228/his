package cn.honry.inner.inpatient.consultation.vo;

import java.util.Date;


public class PatientTreeVo {
	/** 病历号 **/
	private String id;
	/** 患者姓名 **/
	private String name;
	/** 出生年月日 **/
	private Date birthday;
	/** 性别 **/
	private Integer sex;
	/** 所在科室Address **/
	private String deptAddress;
	
	/** 
	* @Fields clinicCode : 门诊号 
	*/ 
	private String clinicCode;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getDeptAddress() {
		return deptAddress;
	}
	public void setDeptAddress(String deptAddress) {
		this.deptAddress = deptAddress;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	
	
}
