package cn.honry.inner.inpatient.consultation.vo;

import java.util.Date;

public class FiveSearch {
	/** 病房号 **/
	private String bedwardName;
	/**姓名*/
	private String patientName;
	/**性别(从编码表里读取)*/
	private String reportSex;
	/**年龄*/
	private Integer reportAge;
	/** 部门地点 **/
	private String deptAddress;
	/** 住院流水号  **/
	private String inpatientNo;
	/**患者床号*/
	private String bedName;
	/**床号id*/
	private String bedNo;
	/** 患者所在病区 **/
	private String nurdept;
	
	public String getNurdept() {
		return nurdept;
	}
	public void setNurdept(String nurdept) {
		this.nurdept = nurdept;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getReportSex() {
		return reportSex;
	}
	public void setReportSex(String reportSex) {
		this.reportSex = reportSex;
	}
	public Integer getReportAge() {
		return reportAge;
	}
	public void setReportAge(Integer reportAge) {
		this.reportAge = reportAge;
	}
	public String getDeptAddress() {
		return deptAddress;
	}
	public void setDeptAddress(String deptAddress) {
		this.deptAddress = deptAddress;
	}
	public String getBedwardName() {
		return bedwardName;
	}
	public void setBedwardName(String bedwardName) {
		this.bedwardName = bedwardName;
	}
	
	
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCnslDeptcd() {
		return cnslDeptcd;
	}
	public void setCnslDeptcd(String cnslDeptcd) {
		this.cnslDeptcd = cnslDeptcd;
	}
	public String getCnslDoccd() {
		return cnslDoccd;
	}
	public void setCnslDoccd(String cnslDoccd) {
		this.cnslDoccd = cnslDoccd;
	}
	public Date getCnslDate() {
		return cnslDate;
	}
	public void setCnslDate(Date cnslDate) {
		this.cnslDate = cnslDate;
	}
	public Date getMoStdt() {
		return moStdt;
	}
	public void setMoStdt(Date moStdt) {
		this.moStdt = moStdt;
	}
	public Date getMoEddt() {
		return moEddt;
	}
	public void setMoEddt(Date moEddt) {
		this.moEddt = moEddt;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	/**会诊地点*/
	private String location;
	/**会诊科室*/
	private String cnslDeptcd;
	/**会诊医师*/
	private String cnslDoccd;
	/**预约会诊日期*/
	private Date cnslDate;
	/**处方起始日、授权日期*/
	private Date moStdt;
	/**处方结束日、截止日期*/
	private Date moEddt;
	/**住院科室代码、申请科室*/
	private String deptCode;
	/**医嘱医师姓名、申请医师*/
	private String docName;
	/**填写申请日期*/
	private Date applyDate;
}
