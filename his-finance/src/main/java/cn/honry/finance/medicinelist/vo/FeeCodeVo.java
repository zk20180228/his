package cn.honry.finance.medicinelist.vo;

public class FeeCodeVo {

	//费用同意类别代码
	private String feeTypeCode;
	//金额
	private Double fees;
	//费用统计类别名称
	private String feeTypeName;
	
	
	public String getFeeTypeCode() {
		return feeTypeCode;
	}
	public void setFeeTypeCode(String feeTypeCode) {
		this.feeTypeCode = feeTypeCode;
	}
	public String getFeeTypeName() {
		return feeTypeName;
	}
	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
	}
	public Double getFees() {
		return fees;
	}
	public void setFees(Double fees) {
		this.fees = fees;
	}
	
	
}
