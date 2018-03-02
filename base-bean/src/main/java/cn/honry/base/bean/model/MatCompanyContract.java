package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class MatCompanyContract extends Entity implements java.io.Serializable {
	 private String icompanyId;//公司主键编码
	 private String companyCode;//公司编码
	 private Integer contractType;//合同类别 1-正式合同，0-临时合同
	 private String  contractFilename;//证件文件名称
	 private String contractFilepath;//合同文件保存路径【相对路径】
	 private String contractCode;//合同编号
	 private Date contractDate;//签订时间
	 private Date overDate;//到期时间
	 private Integer validFlag;//有效标记(0－停用,1－有效)
	 private String mark;//备注
	 private String operCode;//操作员
	 private Date operDate;//操作日期
	 
	 
	public String getIcompanyId() {
		return icompanyId;
	}
	public void setIcompanyId(String icompanyId) {
		this.icompanyId = icompanyId;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public Integer getContractType() {
		return contractType;
	}
	public void setContractType(Integer contractType) {
		this.contractType = contractType;
	}
	public String getContractFilename() {
		return contractFilename;
	}
	public void setContractFilename(String contractFilename) {
		this.contractFilename = contractFilename;
	}
	public String getContractFilepath() {
		return contractFilepath;
	}
	public void setContractFilepath(String contractFilepath) {
		this.contractFilepath = contractFilepath;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public Date getContractDate() {
		return contractDate;
	}
	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}
	public Date getOverDate() {
		return overDate;
	}
	public void setOverDate(Date overDate) {
		this.overDate = overDate;
	}
	public Integer getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	 
	 
	 

}
