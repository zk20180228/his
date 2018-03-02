package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiBaseEmployee entity. @author MyEclipse Persistence Tools
 */

public class BiBaseEmployee implements java.io.Serializable {

	// Fields

	private String employeeNo;
	private String employeeName;
	private String deptCode;
	private String deptName;
	private String employeeTypeCode;
	private String employeeType;
	private String mcardNo;
	private String employeeWorkstate;
	private String employeeSexCode;
	private String employeeSex;
	private Date employeeBirthday;
	private String employeePostCode;
	private String employeePost;
	private String employeeTitleCode;
	private String employeeEducationCode;
	private String employeeNationCode;
	private String employeeNation;
	private String employeeEducation;
	private String employeeTitle;
	private String employeeIsexpert;
	private String employeeRemark;

	// Constructors

	/** default constructor */
	public BiBaseEmployee() {
	}

	/** full constructor */
	public BiBaseEmployee(String employeeName, String deptCode,
			String deptName, String employeeTypeCode, String employeeType,
			String mcardNo, String employeeWorkstate, String employeeSexCode,
			String employeeSex, Date employeeBirthday, String employeePostCode,
			String employeePost, String employeeTitleCode,
			String employeeEducationCode, String employeeNationCode,
			String employeeNation, String employeeEducation,
			String employeeTitle, String employeeIsexpert, String employeeRemark) {
		this.employeeName = employeeName;
		this.deptCode = deptCode;
		this.deptName = deptName;
		this.employeeTypeCode = employeeTypeCode;
		this.employeeType = employeeType;
		this.mcardNo = mcardNo;
		this.employeeWorkstate = employeeWorkstate;
		this.employeeSexCode = employeeSexCode;
		this.employeeSex = employeeSex;
		this.employeeBirthday = employeeBirthday;
		this.employeePostCode = employeePostCode;
		this.employeePost = employeePost;
		this.employeeTitleCode = employeeTitleCode;
		this.employeeEducationCode = employeeEducationCode;
		this.employeeNationCode = employeeNationCode;
		this.employeeNation = employeeNation;
		this.employeeEducation = employeeEducation;
		this.employeeTitle = employeeTitle;
		this.employeeIsexpert = employeeIsexpert;
		this.employeeRemark = employeeRemark;
	}

	// Property accessors

	public String getEmployeeNo() {
		return this.employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getEmployeeName() {
		return this.employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getEmployeeTypeCode() {
		return this.employeeTypeCode;
	}

	public void setEmployeeTypeCode(String employeeTypeCode) {
		this.employeeTypeCode = employeeTypeCode;
	}

	public String getEmployeeType() {
		return this.employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getMcardNo() {
		return this.mcardNo;
	}

	public void setMcardNo(String mcardNo) {
		this.mcardNo = mcardNo;
	}

	public String getEmployeeWorkstate() {
		return this.employeeWorkstate;
	}

	public void setEmployeeWorkstate(String employeeWorkstate) {
		this.employeeWorkstate = employeeWorkstate;
	}

	public String getEmployeeSexCode() {
		return this.employeeSexCode;
	}

	public void setEmployeeSexCode(String employeeSexCode) {
		this.employeeSexCode = employeeSexCode;
	}

	public String getEmployeeSex() {
		return this.employeeSex;
	}

	public void setEmployeeSex(String employeeSex) {
		this.employeeSex = employeeSex;
	}

	public Date getEmployeeBirthday() {
		return this.employeeBirthday;
	}

	public void setEmployeeBirthday(Date employeeBirthday) {
		this.employeeBirthday = employeeBirthday;
	}

	public String getEmployeePostCode() {
		return this.employeePostCode;
	}

	public void setEmployeePostCode(String employeePostCode) {
		this.employeePostCode = employeePostCode;
	}

	public String getEmployeePost() {
		return this.employeePost;
	}

	public void setEmployeePost(String employeePost) {
		this.employeePost = employeePost;
	}

	public String getEmployeeTitleCode() {
		return this.employeeTitleCode;
	}

	public void setEmployeeTitleCode(String employeeTitleCode) {
		this.employeeTitleCode = employeeTitleCode;
	}

	public String getEmployeeEducationCode() {
		return this.employeeEducationCode;
	}

	public void setEmployeeEducationCode(String employeeEducationCode) {
		this.employeeEducationCode = employeeEducationCode;
	}

	public String getEmployeeNationCode() {
		return this.employeeNationCode;
	}

	public void setEmployeeNationCode(String employeeNationCode) {
		this.employeeNationCode = employeeNationCode;
	}

	public String getEmployeeNation() {
		return this.employeeNation;
	}

	public void setEmployeeNation(String employeeNation) {
		this.employeeNation = employeeNation;
	}

	public String getEmployeeEducation() {
		return this.employeeEducation;
	}

	public void setEmployeeEducation(String employeeEducation) {
		this.employeeEducation = employeeEducation;
	}

	public String getEmployeeTitle() {
		return this.employeeTitle;
	}

	public void setEmployeeTitle(String employeeTitle) {
		this.employeeTitle = employeeTitle;
	}

	public String getEmployeeIsexpert() {
		return this.employeeIsexpert;
	}

	public void setEmployeeIsexpert(String employeeIsexpert) {
		this.employeeIsexpert = employeeIsexpert;
	}

	public String getEmployeeRemark() {
		return this.employeeRemark;
	}

	public void setEmployeeRemark(String employeeRemark) {
		this.employeeRemark = employeeRemark;
	}

}