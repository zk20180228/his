package cn.honry.statistics.finance.pharmacyRefund.vo;

import java.util.Date;

public class RefundVo {
	//发票流水号
	private String invoiceNo;
	//患者名称
	private String patientName;
	//退费项目
	private String feeStatCode;
	//退费日期
	private Date confirmDate;
	//退费金额
	private Double refundMoney;
	//发药窗口
	private String sendWin;
	//发票科目代码
	private String invocode;
	
	public String getInvocode() {
		return invocode;
	}
	public void setInvocode(String invocode) {
		this.invocode = invocode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getFeeStatCode() {
		return feeStatCode;
	}
	public void setFeeStatCode(String feeStatCode) {
		this.feeStatCode = feeStatCode;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public Double getRefundMoney() {
		return refundMoney;
	}
	public void setRefundMoney(Double refundMoney) {
		this.refundMoney = refundMoney;
	}
	public String getSendWin() {
		return sendWin;
	}
	public void setSendWin(String sendWin) {
		this.sendWin = sendWin;
	}
	
	
}
