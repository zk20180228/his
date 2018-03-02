package cn.honry.statistics.operation.syntheticalStat.vo;



public class InvoiceInfoVo{
	
	/**发票号码**/
	private String invoiceNo;
	/**划价收费**/
	private String priceCost;
	/**状态**/
	private String status;
	/**录入来源**/
	private String source;
	/**项目编码**/
	private String itemCode;
	/**项目名称**/
	private String itemName;
	/**组**/
	private String groupNo;
	/**组名**/
	private String groupName;
	/**内部序号**/
	private Integer inOrderNo;
	/**序列号**/
	private String seqNo;
	/**规格**/
	private String spec;
	/**数量**/
	private Integer qty;
	/**单价**/
	private Double unitPrice;
	/**总金额**/
	private Double totalAmount;
	/**首付金额**/
	private Double shoufuAmount;
	/**记账金额**/
	private Double tallyAmount;
	/**开单科室**/
	private String billDept;
	/**开单医生**/
	private String billdoc;
	/**录入人**/
	private String inputPerson;
	/**录入时间**/
	private String inputTime;
	/**执行科室**/
	private String exeDept;
	/**收款员**/
	private String receiver;
	/**收费日期**/
	private String chargeDate;
	/**确认执行时间**/
	private String conExeTime;
	/**确认执行科室**/
	private String conExeDept;
	/**确认执行人**/
	private String conExePerson;
	
	/**getters and setters**/
	
	public String getPriceCost() {
		return priceCost;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public void setPriceCost(String priceCost) {
		this.priceCost = priceCost;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Integer getInOrderNo() {
		return inOrderNo;
	}
	public void setInOrderNo(Integer inOrderNo) {
		this.inOrderNo = inOrderNo;
	}
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getShoufuAmount() {
		return shoufuAmount;
	}
	public void setShoufuAmount(Double shoufuAmount) {
		this.shoufuAmount = shoufuAmount;
	}
	public Double getTallyAmount() {
		return tallyAmount;
	}
	public void setTallyAmount(Double tallyAmount) {
		this.tallyAmount = tallyAmount;
	}
	public String getBillDept() {
		return billDept;
	}
	public void setBillDept(String billDept) {
		this.billDept = billDept;
	}
	public String getBilldoc() {
		return billdoc;
	}
	public void setBilldoc(String billdoc) {
		this.billdoc = billdoc;
	}
	public String getInputPerson() {
		return inputPerson;
	}
	public void setInputPerson(String inputPerson) {
		this.inputPerson = inputPerson;
	}
	public String getInputTime() {
		return inputTime;
	}
	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}
	public String getExeDept() {
		return exeDept;
	}
	public void setExeDept(String exeDept) {
		this.exeDept = exeDept;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getChargeDate() {
		return chargeDate;
	}
	public void setChargeDate(String chargeDate) {
		this.chargeDate = chargeDate;
	}
	public String getConExeTime() {
		return conExeTime;
	}
	public void setConExeTime(String conExeTime) {
		this.conExeTime = conExeTime;
	}
	public String getConExeDept() {
		return conExeDept;
	}
	public void setConExeDept(String conExeDept) {
		this.conExeDept = conExeDept;
	}
	public String getConExePerson() {
		return conExePerson;
	}
	public void setConExePerson(String conExePerson) {
		this.conExePerson = conExePerson;
	}
	
}
