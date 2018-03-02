package cn.honry.statistics.sys.medicalFeeDetail.vo;

public class FeeDetailsVo {
	/**
	 * 住院号
	 */
	private String inpatientNo;
	/**
	 * 最小单位
	 */
	private String feeCode;
	/**
	 * 费用
	 */
	private double totalCost;
	
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}
		
}
