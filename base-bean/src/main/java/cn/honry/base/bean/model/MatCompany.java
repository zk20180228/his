package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class MatCompany extends Entity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
     private String companyCode;//公司编码
     private String companyName;//公司名称
     private String spellCode;//拼音编码
     private String wbCode;//五笔码
     private String customCode;//自定义码
     private Integer companyType;//公司类别(0:生产厂家,1:供销商)
     private String  coporation;//公司法人
     private String  address;//公司地址
     private String telCode;//公司电话
     private String faxCode;//传真
     private String netAddress;//公司网址
     private String email;//公司邮箱
     private String linkMan;//联系人
     private String linkMail;//联系人邮箱
     private String linkTel;//联系人电话
     private String isoInfo;//ISO信息
     private String openBank;//开户银行
     private String openAccounts;//开户账号
     private Double actualRate;//政策扣率
     private Integer validFlag;//停用标记(0－停用,1－使用)
     private String memo;//备注
     private String operCode;//操作员
     private Date operdate;//操作日期
     private String storage;//所属库房编码
     
     
     
     
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSpellCode() {
		return spellCode;
	}
	public void setSpellCode(String spellCode) {
		this.spellCode = spellCode;
	}
	public String getWbCode() {
		return wbCode;
	}
	public void setWbCode(String wbCode) {
		this.wbCode = wbCode;
	}
	public String getCustomCode() {
		return customCode;
	}
	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}
	public Integer getCompanyType() {
		return companyType;
	}
	public void setCompanyType(Integer companyType) {
		this.companyType = companyType;
	}
	public String getCoporation() {
		return coporation;
	}
	public void setCoporation(String coporation) {
		this.coporation = coporation;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelCode() {
		return telCode;
	}
	public void setTelCode(String telCode) {
		this.telCode = telCode;
	}
	public String getFaxCode() {
		return faxCode;
	}
	public void setFaxCode(String faxCode) {
		this.faxCode = faxCode;
	}
	public String getNetAddress() {
		return netAddress;
	}
	public void setNetAddress(String netAddress) {
		this.netAddress = netAddress;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getLinkMail() {
		return linkMail;
	}
	public void setLinkMail(String linkMail) {
		this.linkMail = linkMail;
	}
	public String getLinkTel() {
		return linkTel;
	}
	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}
	public String getIsoInfo() {
		return isoInfo;
	}
	public void setIsoInfo(String isoInfo) {
		this.isoInfo = isoInfo;
	}
	public String getOpenBank() {
		return openBank;
	}
	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}
	public String getOpenAccounts() {
		return openAccounts;
	}
	public void setOpenAccounts(String openAccounts) {
		this.openAccounts = openAccounts;
	}
	public Double getActualRate() {
		return actualRate;
	}
	public void setActualRate(Double actualRate) {
		this.actualRate = actualRate;
	}
	public Integer getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Date getOperdate() {
		return operdate;
	}
	public void setOperdate(Date operdate) {
		this.operdate = operdate;
	}
	public String getStorage() {
		return storage;
	}
	public void setStorage(String storage) {
		this.storage = storage;
	}

   
}
