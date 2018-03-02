package cn.honry.statistics.finance.inpatientFeeBalSum.vo;

public class FeeBalSumVo {
	//统计费用名称
	private String feeStatCode;
	//费用金额    
	private Double cost; 
	//合同单位 
	private String pactCode;
	//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干   
	private String paykindCode;
	
	public String getFeeStatCode() {
		return feeStatCode;
	}
	public void setFeeStatCode(String feeStatCode) {
		this.feeStatCode = feeStatCode;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	public String getPaykindCode() {
		return paykindCode;
	}
	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	} 
	
}
