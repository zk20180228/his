package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class CpVcontrol extends Entity {
	
	private static final long serialVersionUID = 1L;
	
	private String cpId;//临床路径ID
	private String cpName;//临床路径名称
	private String versionNo;//临床路径版号
	private String standCode;//标准代码
	private String standVersionNo;//标准版本号
	private String versionMemo;//版本说明
	private Date versionDate;//版本日期
	private String applyScope;//适用范围
	private String approvalUser;//审批人
	private Date approvalDate;//审批日期
	private String approvalFlag;//审批标志
	private String standardDate;//标准住院日
	private String dateUnit;//日期单位
	private String hospitalId;//所属医院
	private String areaCode;//所属院区
	
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public String getCpName() {
		return cpName;
	}
	public void setCpName(String cpName) {
		this.cpName = cpName;
	}
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	public String getStandCode() {
		return standCode;
	}
	public void setStandCode(String standCode) {
		this.standCode = standCode;
	}
	public String getStandVersionNo() {
		return standVersionNo;
	}
	public void setStandVersionNo(String standVersionNo) {
		this.standVersionNo = standVersionNo;
	}
	public String getVersionMemo() {
		return versionMemo;
	}
	public void setVersionMemo(String versionMemo) {
		this.versionMemo = versionMemo;
	}
	public Date getVersionDate() {
		return versionDate;
	}
	public void setVersionDate(Date versionDate) {
		this.versionDate = versionDate;
	}
	public String getApplyScope() {
		return applyScope;
	}
	public void setApplyScope(String applyScope) {
		this.applyScope = applyScope;
	}
	public String getApprovalUser() {
		return approvalUser;
	}
	public void setApprovalUser(String approvalUser) {
		this.approvalUser = approvalUser;
	}
	public Date getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getApprovalFlag() {
		return approvalFlag;
	}
	public void setApprovalFlag(String approvalFlag) {
		this.approvalFlag = approvalFlag;
	}
	public String getStandardDate() {
		return standardDate;
	}
	public void setStandardDate(String standardDate) {
		this.standardDate = standardDate;
	}
	public String getDateUnit() {
		return dateUnit;
	}
	public void setDateUnit(String dateUnit) {
		this.dateUnit = dateUnit;
	}
	public String getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
}
