package cn.honry.statistics.doctor.regisdocscheinfo.vo;

import java.util.Date;

public class RegisDocScheInfoVo {
	
	/**
	 * 科室姓名
	 */
	private String deptName;
	
	/**
	 * 医生姓名
	 */
	private String doctorName;
	
	/**
	 * 职称(根据挂号级别得到职称)
	 */
	private String reglevlName;
	
	/**
	 * 星期
	 */
	private Integer weekday;
	
	/**
	 * 午别
	 */
	private Integer noonName;
	
	/**
	 * 专长
	 */
	private String empRemark;
	
	/**
	 * 日期
	 */
	private Date seeDate;
	
	/**
	 * 拼音码
	 */
	private String empPinyin;

	/**
	 * 星期
	 */
	private String weekdayStr;
	/**
	 * 午别
	 */
	private String noonNameStr;
	
	public String getNoonNameStr() {
		return noonNameStr;
	}

	public void setNoonNameStr(String noonNameStr) {
		this.noonNameStr = noonNameStr;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getReglevlName() {
		return reglevlName;
	}

	public void setReglevlName(String reglevlName) {
		this.reglevlName = reglevlName;
	}

	
	public Integer getWeekday() {
		return weekday;
	}

	public void setWeekday(Integer weekday) {
		this.weekday = weekday;
	}

	public Integer getNoonName() {
		return noonName;
	}

	public void setNoonName(Integer noonName) {
		this.noonName = noonName;
	}

	public String getEmpRemark() {
		return empRemark;
	}

	public void setEmpRemark(String empRemark) {
		this.empRemark = empRemark;
	}

	public Date getSeeDate() {
		return seeDate;
	}

	public void setSeeDate(Date seeDate) {
		this.seeDate = seeDate;
	}

	public String getEmpPinyin() {
		return empPinyin;
	}

	public void setEmpPinyin(String empPinyin) {
		this.empPinyin = empPinyin;
	}

	public String getWeekdayStr() {
		return weekdayStr;
	}

	public void setWeekdayStr(String weekdayStr) {
		this.weekdayStr = weekdayStr;
	}
	
}
