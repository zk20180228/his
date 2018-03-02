package cn.honry.inpatient.inpatientProof.vo;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class proReg extends Entity {
	/**挂号类别**/
	private String type;
	/**挂号科室**/
	private String dept;
	/**挂号专家**/
	private String expxrt;
	/**挂号日期**/
	private Date rdate;
	/** 患者姓名  **/
	private String patientName;
	/** 性别  **/
	private Integer patientSex;
	/**预约号**/
	private String no;
	//收费状态  1 表示没有未收费项目  2 表示有未收费项目
	private Integer state;
	/**病历号 **/
	private String medicalrecordId;
//	public proReg() {
//	}
//	
//	public proReg(String type, String dept, String expxrt, Date date,
//			String patientName, Integer patientSex, String no) {
//		this.type = type;
//		this.dept = dept;
//		this.expxrt = expxrt;
//		this.date = date;
//		this.patientName = patientName;
//		this.patientSex = patientSex;
//		this.no = no;
//	}
	
	

	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	

	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getExpxrt() {
		return expxrt;
	}
	public void setExpxrt(String expxrt) {
		this.expxrt = expxrt;
	}

	public Date getRdate() {
		return rdate;
	}

	public void setRdate(Date rdate) {
		this.rdate = rdate;
	}

	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public Integer getPatientSex() {
		return patientSex;
	}
	public void setPatientSex(Integer patientSex) {
		this.patientSex = patientSex;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
}
