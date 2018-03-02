package cn.honry.finance.invoicereprint.vo;

import java.util.Date;

public class InvoiceReprintVO {
	/**发票号**/
	private String invoiceNo;
	/**收费员**/
	private String feeCode;
	/**收费日期**/
	private Date feeDate;
	/**项目名称**/
	private String itemName;
	/**开方医生所在科室名称**/
	private String deptName;
	
	
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public Date getFeeDate() {
		return feeDate;
	}
	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}
