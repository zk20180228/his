package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 
 * <p>
 * 员工扩展信息
 * </p>
 * 
 * @Author: dutianliang
 * @CreateDate: 2017年7月28日 下午4:36:31
 * @Modifier: dutianliang
 * @ModifyDate: 2017年7月28日 下午4:36:31
 * @ModifyRmk:
 * @version: V1.0:
 *
 */
public class EmployeeExtend extends Entity {

	/** 员工编号 **/
	private String employeeJobNo;
	/** 所在科室 **/
	private String deptCode;
	/** 职务代码 **/
	private String dutiesCode;
	/** 职务分类 **/
	private String dutiesType;
	/** 职务级别 **/
	private String dutiesLevel;
	/** 职称代码 **/
	private String titleCode;
	/** 职称分类 **/
	private String titleType;
	/** 职称级别 **/
	private String titleLevel;
	/** 员工类型 **/
	private String employeeType;
	/** 人事关系 **/
	private String personnelRelations;
	/** 学部 **/
	private String division;
	/** 学部编号 **/
	private String divisionCode;
	/** 部门 **/
	private String department;
	/** 部门编号 **/
	private String departmentCode;
	/** 总支 **/
	private String generalbranch;
	/** 总支编号 **/
	private String generalbranchCode;
	/** 姓名 **/
	private String employeeName;
	/** 性别 **/
	private String employeeSexName;
	/** 性别编号 **/
	private String employeeSexCode;
	/** 证件号码 **/
	private String employeeIdentityCard;
	/** 出生日期 **/
	private Date employeeBirthday;
	/** 年龄 **/
	private Integer employeeAge;
	/** 人员职称 **/
	private String titleName;
	/** 职务 **/
	private String dutiesName;
	/** 民族 **/
	private String nationalName;
	/** 民族编号 **/
	private String nationalCode;
	/** 编制类别 **/
	private String organizationName;
	/** 编制类别编号 **/
	private String organizationCode;
	/** 政治面貌 **/
	private String politicalstatusName;
	/** 政治面貌编号 **/
	private String politicalstatusCode;
	/** 手机号 **/
	private String employeeMobile;
	/**
	 * 
	 * @Fields titleTypeName : 职称分类名称
	 *
	 */
	private String titleTypeName;
	/**
	 * 
	 * @Fields dutiesTypeName : 职务分类名称
	 *
	 */
	private String dutiesTypeName;
	/**
	 * 
	 * @Fields employeeTypeName : 员工类型名称
	 *
	 */
	private String employeeTypeName;
	/**  
	 * 
	 * @Fields strBirthday : 生日字符串 
	 *
	 */
	private String strBirthday;
	
	/**决策组**/
	private String manageGroup;
	/**  
	 * 
	 * @Fields areaCode : 所属院区
	 *
	 */
	private String areaCode;
	/**  
	 * 
	 * @Fields areaCodeName : 所属院区名称
	 *
	 */
	private String areaCodeName;
	/**  
	 * 
	 * @Fields empFlg : 人员类别 1医生 2护士.......
	 *
	 */
	private Integer empFlg;

	public String getEmployeeJobNo() {
		return employeeJobNo;
	}

	public void setEmployeeJobNo(String employeeJobNo) {
		this.employeeJobNo = employeeJobNo;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDutiesCode() {
		return dutiesCode;
	}

	public void setDutiesCode(String dutiesCode) {
		this.dutiesCode = dutiesCode;
	}

	public String getDutiesType() {
		return dutiesType;
	}

	public void setDutiesType(String dutiesType) {
		this.dutiesType = dutiesType;
	}

	public String getDutiesLevel() {
		return dutiesLevel;
	}

	public void setDutiesLevel(String dutiesLevel) {
		this.dutiesLevel = dutiesLevel;
	}

	public String getTitleCode() {
		return titleCode;
	}

	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}

	public String getTitleType() {
		return titleType;
	}

	public void setTitleType(String titleType) {
		this.titleType = titleType;
	}

	public String getTitleLevel() {
		return titleLevel;
	}

	public void setTitleLevel(String titleLevel) {
		this.titleLevel = titleLevel;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getPersonnelRelations() {
		return personnelRelations;
	}

	public void setPersonnelRelations(String personnelRelations) {
		this.personnelRelations = personnelRelations;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getDivisionCode() {
		return divisionCode;
	}

	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getGeneralbranch() {
		return generalbranch;
	}

	public void setGeneralbranch(String generalbranch) {
		this.generalbranch = generalbranch;
	}

	public String getGeneralbranchCode() {
		return generalbranchCode;
	}

	public void setGeneralbranchCode(String generalbranchCode) {
		this.generalbranchCode = generalbranchCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeSexName() {
		return employeeSexName;
	}

	public void setEmployeeSexName(String employeeSexName) {
		this.employeeSexName = employeeSexName;
	}

	public String getEmployeeSexCode() {
		return employeeSexCode;
	}

	public void setEmployeeSexCode(String employeeSexCode) {
		this.employeeSexCode = employeeSexCode;
	}

	public String getEmployeeIdentityCard() {
		return employeeIdentityCard;
	}

	public void setEmployeeIdentityCard(String employeeIdentityCard) {
		this.employeeIdentityCard = employeeIdentityCard;
	}

	public Date getEmployeeBirthday() {
		return employeeBirthday;
	}

	public void setEmployeeBirthday(Date employeeBirthday) {
		this.employeeBirthday = employeeBirthday;
	}

	public Integer getEmployeeAge() {
		return employeeAge;
	}

	public void setEmployeeAge(Integer employeeAge) {
		this.employeeAge = employeeAge;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getDutiesName() {
		return dutiesName;
	}

	public void setDutiesName(String dutiesName) {
		this.dutiesName = dutiesName;
	}

	public String getNationalName() {
		return nationalName;
	}

	public void setNationalName(String nationalName) {
		this.nationalName = nationalName;
	}

	public String getNationalCode() {
		return nationalCode;
	}

	public void setNationalCode(String nationalCode) {
		this.nationalCode = nationalCode;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getPoliticalstatusName() {
		return politicalstatusName;
	}

	public void setPoliticalstatusName(String politicalstatusName) {
		this.politicalstatusName = politicalstatusName;
	}

	public String getPoliticalstatusCode() {
		return politicalstatusCode;
	}

	public void setPoliticalstatusCode(String politicalstatusCode) {
		this.politicalstatusCode = politicalstatusCode;
	}

	public String getEmployeeMobile() {
		return employeeMobile;
	}

	public void setEmployeeMobile(String employeeMobile) {
		this.employeeMobile = employeeMobile;
	}

	public String getTitleTypeName() {
		return titleTypeName;
	}

	public void setTitleTypeName(String titleTypeName) {
		this.titleTypeName = titleTypeName;
	}

	public String getDutiesTypeName() {
		return dutiesTypeName;
	}

	public void setDutiesTypeName(String dutiesTypeName) {
		this.dutiesTypeName = dutiesTypeName;
	}

	public String getEmployeeTypeName() {
		return employeeTypeName;
	}

	public void setEmployeeTypeName(String employeeTypeName) {
		this.employeeTypeName = employeeTypeName;
	}

	public String getStrBirthday() {
		return strBirthday;
	}

	public void setStrBirthday(String strBirthday) {
		this.strBirthday = strBirthday;
	}

	public String getManageGroup() {
		return manageGroup;
	}

	public void setManageGroup(String manageGroup) {
		this.manageGroup = manageGroup;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaCodeName() {
		return areaCodeName;
	}

	public void setAreaCodeName(String areaCodeName) {
		this.areaCodeName = areaCodeName;
	}

	public Integer getEmpFlg() {
		return empFlg;
	}

	public void setEmpFlg(Integer empFlg) {
		this.empFlg = empFlg;
	}


}