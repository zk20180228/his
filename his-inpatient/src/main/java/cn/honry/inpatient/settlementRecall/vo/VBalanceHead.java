package cn.honry.inpatient.settlementRecall.vo;

public class VBalanceHead {
    private String id;
    private String invoiceNo;//  发票号码 结转时为流水号
    private Double returnCost;//  返还金额
    private Double supplyCost;//  补收金额
    private Integer  balanceType;// 结算类型 1:在院结算，2:出院结算，3:直接结算，4:重结算 5:结转，6欠费结算
    private String inpatientNo;//  住院流水号
    private Integer balanceNo;//   结算序号
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public Double getReturnCost() {
		return returnCost;
	}

	public void setReturnCost(Double returnCost) {
		this.returnCost = returnCost;
	}

	public Double getSupplyCost() {
		return supplyCost;
	}

	public void setSupplyCost(Double supplyCost) {
		this.supplyCost = supplyCost;
	}

	public void setBalanceType(Integer balanceType) {
		this.balanceType = balanceType;
	}

	public Integer getBalanceType() {
		return balanceType;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getInpatientNo() {
		return inpatientNo;
	}

	public void setBalanceNo(Integer balanceNo) {
		this.balanceNo = balanceNo;
	}

	public Integer getBalanceNo() {
		return balanceNo;
	}
	
   
}
