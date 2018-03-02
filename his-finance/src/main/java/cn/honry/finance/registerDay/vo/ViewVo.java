package cn.honry.finance.registerDay.vo;


public class ViewVo {
	/**发票起始号**/
	private String invoices;
	/**张数**/
	private Integer num;
	/**实收**/
	private Double income;
	/**记账 **/
	private Double accounting;
	/**合计**/
	private Double total;
	/**单数**/
	private Integer extFlag;
	/**备注**/
	private String remarks;
	public String getInvoices() {
		return invoices;
	}
	public void setInvoices(String invoices) {
		this.invoices = invoices;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Double getIncome() {
		return income;
	}
	public void setIncome(Double income) {
		this.income = income;
	}
	public Double getAccounting() {
		return accounting;
	}
	public void setAccounting(Double accounting) {
		this.accounting = accounting;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Integer getExtFlag() {
		return extFlag;
	}
	public void setExtFlag(Integer extFlag) {
		this.extFlag = extFlag;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
