package cn.honry.inpatient.outBalance.vo;

import java.util.List;

public class InvoicePrintVo {
	/**病历号-登记号**/
	private String medicalRecordId;
	/**住院时间**/
	private String inDate;
	/**收款日期-出院日期**/
	private String balaceDate;
	
	private String balanceNo;
	/**姓名**/
	private String pName;
	/**支票号**/
	private String checkNo;
	/**科别**/
	private String deptCode;
	/**费别**/
	private String statName;
	/**金额**/
	private String totCode;
	/**预交金**/
	private Double prepayCost;
	/**补交**/
	private Double supplyCost;
	/**退款**/
	private Double returnCost;
	/**金额大写**/
	private String sumMoney;
	/**小计**/
	private Double sumPay;
	/**收款人**/
	private String operCode;
	private String invoiceNo;
	
	private List<InvoicePrintVo> invoiceList1;
	private List<InvoicePrintVo> invoiceList2;
	private List<InvoicePrintVo> invoiceList3;
	private List<InvoicePrintVo> invoiceList4;
	
	public List<InvoicePrintVo> getInvoiceList1() {
		return invoiceList1;
	}
	public void setInvoiceList1(List<InvoicePrintVo> invoiceList1) {
		this.invoiceList1 = invoiceList1;
	}
	public List<InvoicePrintVo> getInvoiceList2() {
		return invoiceList2;
	}
	public void setInvoiceList2(List<InvoicePrintVo> invoiceList2) {
		this.invoiceList2 = invoiceList2;
	}
	public List<InvoicePrintVo> getInvoiceList3() {
		return invoiceList3;
	}
	public void setInvoiceList3(List<InvoicePrintVo> invoiceList3) {
		this.invoiceList3 = invoiceList3;
	}
	public List<InvoicePrintVo> getInvoiceList4() {
		return invoiceList4;
	}
	public void setInvoiceList4(List<InvoicePrintVo> invoiceList4) {
		this.invoiceList4 = invoiceList4;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getMedicalRecordId() {
		return medicalRecordId;
	}
	public void setMedicalRecordId(String medicalRecordId) {
		this.medicalRecordId = medicalRecordId;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getBalaceDate() {
		return balaceDate;
	}
	public void setBalaceDate(String balaceDate) {
		this.balaceDate = balaceDate;
	}
	public String getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getStatName() {
		return statName;
	}
	public void setStatName(String statName) {
		this.statName = statName;
	}
	public String getTotCode() {
		return totCode;
	}
	public void setTotCode(String totCode) {
		this.totCode = totCode;
	}
	
	public Double getPrepayCost() {
		return prepayCost;
	}
	public void setPrepayCost(Double prepayCost) {
		this.prepayCost = prepayCost;
	}
	public Double getSupplyCost() {
		return supplyCost;
	}
	public void setSupplyCost(Double supplyCost) {
		this.supplyCost = supplyCost;
	}
	public Double getReturnCost() {
		return returnCost;
	}
	public void setReturnCost(Double returnCost) {
		this.returnCost = returnCost;
	}
	public String getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(String sumMoney) {
		this.sumMoney = sumMoney;
	}
	
	public Double getSumPay() {
		return sumPay;
	}
	public void setSumPay(Double sumPay) {
		this.sumPay = sumPay;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	@Override
	public String toString() {
		return "InvoicePrintVo [medicalRecordId=" + medicalRecordId
				+ ", inDate=" + inDate + ", balaceDate=" + balaceDate
				+ ", balanceNo=" + balanceNo + ", pName=" + pName
				+ ", checkNo=" + checkNo + ", deptCode=" + deptCode
				+ ", statName=" + statName + ", totCode=" + totCode
				+ ", prepayCost=" + prepayCost + ", supplyCost=" + supplyCost
				+ ", returnCost=" + returnCost + ", sumMoney=" + sumMoney
				+ ", sumPay=" + sumPay + ", operCode=" + operCode + "]";
	}
	
}
