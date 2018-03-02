package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;
/**
 * 发票入库实体类
 * @author tuchuanjiang
 *
 */
public class FinanceInvoiceStorage extends Entity  implements java.io.Serializable {

	/** 版本号    **/
	private int version;
	/** 发票类型  **/
	private String invoiceType;
	/** 发票类型编码  **/
	private String invoiceCode;
	/** 发票开始号  **/
	private String invoiceStartno;
	/** 发票终止号  **/
	private String invoiceEndno;
	/** 发票已用号  **/
	private String invoiceUsedno;
	/** 使用状态(0-在用,1-停用)  **/
	private Integer invoiceUseState;
	/** 拼音码  **/
	private String invoicePinyin;
	/** 五笔码  **/
	private String invoiceWb;
	/** 自定义码  **/
	private String invoiceInputcode;
	/** 排序  **/
	private Integer invoiceOrder;
	/** 备注  **/
	private String invoiceRemark;
	/** 分页用的page和rows*/
	private String page;
	private String rows;
	
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
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getInvoiceCode() {
		return invoiceCode;
	}
	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
	public String getInvoiceStartno() {
		return invoiceStartno;
	}
	public void setInvoiceStartno(String invoiceStartno) {
		this.invoiceStartno = invoiceStartno;
	}
	public String getInvoiceEndno() {
		return invoiceEndno;
	}
	public void setInvoiceEndno(String invoiceEndno) {
		this.invoiceEndno = invoiceEndno;
	}
	public String getInvoiceUsedno() {
		return invoiceUsedno;
	}
	public void setInvoiceUsedno(String invoiceUsedno) {
		this.invoiceUsedno = invoiceUsedno;
	}
	
	public Integer getInvoiceUseState() {
		return invoiceUseState;
	}
	public void setInvoiceUseState(Integer invoiceUseState) {
		this.invoiceUseState = invoiceUseState;
	}
	public String getInvoicePinyin() {
		return invoicePinyin;
	}
	public void setInvoicePinyin(String invoicePinyin) {
		this.invoicePinyin = invoicePinyin;
	}
	public String getInvoiceWb() {
		return invoiceWb;
	}
	public void setInvoiceWb(String invoiceWb) {
		this.invoiceWb = invoiceWb;
	}
	public String getInvoiceInputcode() {
		return invoiceInputcode;
	}
	public void setInvoiceInputcode(String invoiceInputcode) {
		this.invoiceInputcode = invoiceInputcode;
	}
	
	public Integer getInvoiceOrder() {
		return invoiceOrder;
	}
	public void setInvoiceOrder(Integer invoiceOrder) {
		this.invoiceOrder = invoiceOrder;
	}
	public String getInvoiceRemark() {
		return invoiceRemark;
	}
	public void setInvoiceRemark(String invoiceRemark) {
		this.invoiceRemark = invoiceRemark;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	
}
