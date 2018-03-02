package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;


public class DrugSupplycompany extends Entity implements java.io.Serializable {

	// Fields
	/** 编号  **/
	private String Id;
	/** 名称  **/
	private String companyName;
	/** 拼音码  **/
	private String companyPinyin;
	/** 五笔码  **/
	private String companyWb;
	/** 自定义码  **/
	private String companyInputcode;
	/** 联系方式  **/
	private String companyLink;
	/** 开户银行	  **/
	private String companyBank;
	/**开户账号   **/
	private String companyAccount;
	/**地址   **/
	private String companyAddress;
	/** GSP  **/
	private String companyGsp;
	/** 备注  **/
	private String companyRemark;
	/** 当前页数  **/
	private String page;
	/** 每页行数  **/
	private String rows;
	
	// Property accessors



	public String getCompanyName() {
		return this.companyName;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyPinyin() {
		return this.companyPinyin;
	}

	public void setCompanyPinyin(String companyPinyin) {
		this.companyPinyin = companyPinyin;
	}

	public String getCompanyWb() {
		return this.companyWb;
	}

	public void setCompanyWb(String companyWb) {
		this.companyWb = companyWb;
	}

	public String getCompanyInputcode() {
		return this.companyInputcode;
	}

	public void setCompanyInputcode(String companyInputcode) {
		this.companyInputcode = companyInputcode;
	}

	public String getCompanyLink() {
		return this.companyLink;
	}

	public void setCompanyLink(String companyLink) {
		this.companyLink = companyLink;
	}

	public String getCompanyBank() {
		return this.companyBank;
	}

	public void setCompanyBank(String companyBank) {
		this.companyBank = companyBank;
	}

	public String getCompanyAccount() {
		return this.companyAccount;
	}

	public void setCompanyAccount(String companyAccount) {
		this.companyAccount = companyAccount;
	}

	public String getCompanyAddress() {
		return this.companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyGsp() {
		return this.companyGsp;
	}

	public void setCompanyGsp(String companyGsp) {
		this.companyGsp = companyGsp;
	}

	public String getCompanyRemark() {
		return this.companyRemark;
	}

	public void setCompanyRemark(String companyRemark) {
		this.companyRemark = companyRemark;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}


}