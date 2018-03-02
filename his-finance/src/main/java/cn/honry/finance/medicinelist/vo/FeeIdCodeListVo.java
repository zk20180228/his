package cn.honry.finance.medicinelist.vo;

public class FeeIdCodeListVo {
	//id
	private String id;
	// code
	private String code;
	//feeStatCode
	private String feeStatCode;
	//钱
	private Double payCost;
	
	
	public String getFeeStatCode() {
		return feeStatCode;
	}

	public void setFeeStatCode(String feeStatCode) {
		this.feeStatCode = feeStatCode;
	}

	public Double getPayCost() {
		return payCost;
	}

	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
