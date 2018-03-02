package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;


public class DrugManufacturer extends Entity implements java.io.Serializable {

	/**  
	 *  
	 * @Description：  
	 * @Author：lyy
	 * @CreateDate：2015-10-21 上午09:35:05  
	 * @Modifier：lyy
	 * @ModifyDate：2015-10-21 上午09:35:05  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	/** 名称   **/
	private String manufacturerName;
	/** 拼音码   **/
	private String manufacturerPinyin;
	/** 五笔码   **/
	private String manufacturerWb;
	/** 自定义码   **/
	private String manufacturerInputcode;
	/** 联系方式   **/
	private String manufacturerLink;
	/** 开户银行   **/
	private String manufacturerBank;
	/** 开户账号   **/
	private String manufacturerAccount;
	/** 地址   **/
	private String manufacturerAddress;
	/** GMP   **/
	private String manufacturerGmp;
	/** 备注   **/
	private String manufacturerRemark;
	/** 页数   **/
	private String page;
	/**  每页行数  **/
	private String rows;
	// Property accessors


	public String getManufacturerName() {
		return this.manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getManufacturerPinyin() {
		return this.manufacturerPinyin;
	}

	public void setManufacturerPinyin(String manufacturerPinyin) {
		this.manufacturerPinyin = manufacturerPinyin;
	}

	public String getManufacturerWb() {
		return this.manufacturerWb;
	}

	public void setManufacturerWb(String manufacturerWb) {
		this.manufacturerWb = manufacturerWb;
	}

	public String getManufacturerInputcode() {
		return this.manufacturerInputcode;
	}

	public void setManufacturerInputcode(String manufacturerInputcode) {
		this.manufacturerInputcode = manufacturerInputcode;
	}

	public String getManufacturerLink() {
		return this.manufacturerLink;
	}

	public void setManufacturerLink(String manufacturerLink) {
		this.manufacturerLink = manufacturerLink;
	}

	public String getManufacturerBank() {
		return this.manufacturerBank;
	}

	public void setManufacturerBank(String manufacturerBank) {
		this.manufacturerBank = manufacturerBank;
	}

	public String getManufacturerAccount() {
		return this.manufacturerAccount;
	}

	public void setManufacturerAccount(String manufacturerAccount) {
		this.manufacturerAccount = manufacturerAccount;
	}

	public String getManufacturerAddress() {
		return this.manufacturerAddress;
	}

	public void setManufacturerAddress(String manufacturerAddress) {
		this.manufacturerAddress = manufacturerAddress;
	}

	public String getManufacturerGmp() {
		return this.manufacturerGmp;
	}

	public void setManufacturerGmp(String manufacturerGmp) {
		this.manufacturerGmp = manufacturerGmp;
	}

	public String getManufacturerRemark() {
		return this.manufacturerRemark;
	}

	public void setManufacturerRemark(String manufacturerRemark) {
		this.manufacturerRemark = manufacturerRemark;
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