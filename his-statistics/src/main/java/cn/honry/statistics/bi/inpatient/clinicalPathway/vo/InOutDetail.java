package cn.honry.statistics.bi.inpatient.clinicalPathway.vo;

import java.util.Date;

/**
 * 出入径明细查询实体
 * 
 * <p> </p>
 * @Author: zouxianhao
 * @CreateDate: 2017年11月29日 下午4:48:14 
 * @Modifier: zouxianhao
 * @ModifyDate: 2017年11月29日 下午4:48:14 
 * @ModifyRmk:  
 * @version: V1.0:
 * @throws:
 * @return: 
 *
 */
public class InOutDetail {
	private String deptCode;
	private String inpatientNo;
	private String inpatientName;
	private String sexCode;
	private String age;
	private String ageUnit;
	private String cpId;
	private Date inPathDate;
	private Date outPathDate;
	private String inPathTime;
	private String outPathTime;
	private String outpTypeCode;
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getInpatientName() {
		return inpatientName;
	}
	public void setInpatientName(String inpatientName) {
		this.inpatientName = inpatientName;
	}
	public String getSexCode() {
		return sexCode;
	}
	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getAgeUnit() {
		return ageUnit;
	}
	public void setAgeUnit(String ageUnit) {
		this.ageUnit = ageUnit;
	}
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public Date getInPathDate() {
		return inPathDate;
	}
	public void setInPathDate(Date inPathDate) {
		this.inPathDate = inPathDate;
	}
	public Date getOutPathDate() {
		return outPathDate;
	}
	public void setOutPathDate(Date outPathDate) {
		this.outPathDate = outPathDate;
	}
	public String getInPathTime() {
		return inPathTime;
	}
	public void setInPathTime(String inPathTime) {
		this.inPathTime = inPathTime;
	}
	public String getOutPathTime() {
		return outPathTime;
	}
	public void setOutPathTime(String outPathTime) {
		this.outPathTime = outPathTime;
	}
	public String getOutpTypeCode() {
		return outpTypeCode;
	}
	public void setOutpTypeCode(String outpTypeCode) {
		this.outpTypeCode = outpTypeCode;
	}
}
