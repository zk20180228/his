package cn.honry.statistics.bi.outpatientDrugRatio.vo;

public class DrugRatioVo {
	

	/** 科室维度 **/
	private String deptDimensionality;
	/**门诊人数*/
	private Double outpatientSum;
	/**门诊费用*/
	private Double outpatientFee;
	/**药品费用*/
	private Double drugFee;
	/**构成比例*/
	private Double conRatio;

	/**环比 **/
	private Double chain;
	
	/**同比 **/
	private Double rose;
	
	
	
	/**人均费用 **/
	private Double perCapitaCost;
	
	/**时间维度 **/
	private String timeChose;
	
	
	public String getTimeChose() {
		return timeChose;
	}
	public void setTimeChose(String timeChose) {
		this.timeChose = timeChose;
	}
	
	public String getDeptDimensionality() {
		return deptDimensionality;
	}
	public void setDeptDimensionality(String deptDimensionality) {
		this.deptDimensionality = deptDimensionality;
	}
	public Double getOutpatientSum() {
		return outpatientSum;
	}
	public void setOutpatientSum(Double outpatientSum) {
		this.outpatientSum = outpatientSum;
	}
	public Double getOutpatientFee() {
		return outpatientFee;
	}
	public void setOutpatientFee(Double outpatientFee) {
		this.outpatientFee = outpatientFee;
	}
	public Double getDrugFee() {
		return drugFee;
	}
	public void setDrugFee(Double drugFee) {
		this.drugFee = drugFee;
	}
	
	public Double getConRatio() {
		return conRatio;
	}
	public void setConRatio(Double conRatio) {
		this.conRatio = conRatio;
	}
	public Double getChain() {
		return chain;
	}
	public void setChain(Double chain) {
		this.chain = chain;
	}
	public Double getPerCapitaCost() {
		return perCapitaCost;
	}
	public void setPerCapitaCost(Double perCapitaCost) {
		this.perCapitaCost = perCapitaCost;
	}
	public Double getRose() {
		return rose;
	}
	public void setRose(Double rose) {
		this.rose = rose;
	}
	
}
