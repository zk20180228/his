package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;


public class FinanceInvoice extends Entity implements java.io.Serializable {

	/** 领取时间    **/
	private Date invoiceGettime;
	/** 领取人    **/
	private String invoiceGetperson;
	/** 发票种类    **/
	private String invoiceType;
	/** 发票开始号    **/
	private String invoiceStartno;
	/** 发票终止号    **/
	private String invoiceEndno;
	/** 发票已用号    **/
	private String invoiceUsedno;
	/** 使用状态    **/
	private Integer invoiceUsestate;
	/** 公用标志    **/
	private Integer invoiceIspub=0;
	/** 当前页数    **/
	private String page;
	/** 每页行数    **/
	private String rows;


	// Property accessors

	
	public Date getInvoiceGettime() {
		return this.invoiceGettime;
	}

	public void setInvoiceGettime(Date invoiceGettime) {
		this.invoiceGettime = invoiceGettime;
	}

	public String getInvoiceGetperson() {
		return this.invoiceGetperson;
	}

	public void setInvoiceGetperson(String invoiceGetperson) {
		this.invoiceGetperson = invoiceGetperson;
	}

	public String getInvoiceType() {
		return this.invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceStartno() {
		return this.invoiceStartno;
	}

	public void setInvoiceStartno(String invoiceStartno) {
		this.invoiceStartno = invoiceStartno;
	}

	public String getInvoiceEndno() {
		return this.invoiceEndno;
	}

	public void setInvoiceEndno(String invoiceEndno) {
		this.invoiceEndno = invoiceEndno;
	}

	public String getInvoiceUsedno() {
		return this.invoiceUsedno;
	}

	public void setInvoiceUsedno(String invoiceUsedno) {
		this.invoiceUsedno = invoiceUsedno;
	}

	public Integer getInvoiceUsestate() {
		return invoiceUsestate;
	}

	public void setInvoiceUsestate(Integer invoiceUsestate) {
		this.invoiceUsestate = invoiceUsestate;
	}

	public Integer getInvoiceIspub() {
		return this.invoiceIspub;
	}

	public void setInvoiceIspub(Integer invoiceIspub) {
		this.invoiceIspub = invoiceIspub;
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
