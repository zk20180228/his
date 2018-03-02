package cn.honry.outpatient.observation.vo;

import java.io.Serializable;

/**
 * 
 * <p>根据就诊卡号，查询患者部分信息 </p>
 * @Author: zhangkui
 * @CreateDate: 2017年11月16日 下午5:03:49 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年11月16日 下午5:03:49 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class PatientVo implements Serializable{

	private static final long serialVersionUID = 8977812750370176739L;
	
	private String patientName;//姓名PATIENT_NAME
	private String sex;//性别PATIENT_SEX
	private Integer age;//年龄PATIENT_AGE
	private String birthday;//出生日期PATIENT_BIRTHDAY
	private String identityType;//证件类型PATIENT_CERTIFICATESTYPE
	private String identityCode;//证件号码PATIENT_CERTIFICATESNO
	private String address;//地址 PATIENT_ADDRESS
	private String unionUnit;//合作单位UNIT_ID
	//private String outpatientCode;//门诊号
	private String patientNo;//病历号MEDICALRECORD_ID
	
	
	
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
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getIdentityType() {
		return identityType;
	}
	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}
	public String getIdentityCode() {
		return identityCode;
	}
	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUnionUnit() {
		return unionUnit;
	}
	public void setUnionUnit(String unionUnit) {
		this.unionUnit = unionUnit;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	
	
	
	
	
	

}
