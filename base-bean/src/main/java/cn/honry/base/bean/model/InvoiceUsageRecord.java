package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class InvoiceUsageRecord  extends Entity  implements java.io.Serializable {
	/** 账务组或员工ID  **/
	private String userId;
	/** 账务组或员工编码  **/
	private String userCode;
	/** 1-组2-员工  **/
	private Integer userType;
	/** 发票号  **/
	private String invoiceNo;
	/** 召回操作员  **/
	private String recoveryOpcd;
	/** 召回发票号  **/
	private String recoveryInvoiceNo;
	/** 召回时间  **/
	private Date recoveryDate;
	/** 使用状态(0-未使用 1-已使用 2-召回)  **/
	private Integer invoiceUsestate;
	/** 组名/员工名**/
	private String userName;
	private String page;
	private String rows;
	//新增字段  2016-11-15 11:39:33
	/**召回操作员姓名*/
	private String recoveryOpceName;
	
	public String getRecoveryOpceName() {
		return recoveryOpceName;
	}
	public void setRecoveryOpceName(String recoveryOpceName) {
		this.recoveryOpceName = recoveryOpceName;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getRecoveryOpcd() {
		return recoveryOpcd;
	}
	public void setRecoveryOpcd(String recoveryOpcd) {
		this.recoveryOpcd = recoveryOpcd;
	}
	public String getRecoveryInvoiceNo() {
		return recoveryInvoiceNo;
	}
	public void setRecoveryInvoiceNo(String recoveryInvoiceNo) {
		this.recoveryInvoiceNo = recoveryInvoiceNo;
	}
	public Date getRecoveryDate() {
		return recoveryDate;
	}
	public void setRecoveryDate(Date recoveryDate) {
		this.recoveryDate = recoveryDate;
	}
	public Integer getInvoiceUsestate() {
		return invoiceUsestate;
	}
	public void setInvoiceUsestate(Integer invoiceUsestate) {
		this.invoiceUsestate = invoiceUsestate;
	}
}
