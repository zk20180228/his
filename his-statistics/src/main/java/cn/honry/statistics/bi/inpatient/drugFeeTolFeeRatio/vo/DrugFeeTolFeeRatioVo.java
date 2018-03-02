package cn.honry.statistics.bi.inpatient.drugFeeTolFeeRatio.vo;

public class DrugFeeTolFeeRatioVo {
	/**时间*/
	private String timeChose;
	/**科室*/
	private String deptCode;
	/**总住院人数*/
	private Double inpatientTolSum;
	/**住院人次费用*/
	private Double inpatientFee;
	/**药品费用*/
	private Double drugFee;
	/**其他费用*/
	private Double otherFee;
	/**药费占比*/
	private String drugFeeRatio;
	
	public String getTimeChose() {
		return timeChose;
	}
	public void setTimeChose(String timeChose) {
		this.timeChose = timeChose;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Double getInpatientTolSum() {
		return inpatientTolSum;
	}
	public void setInpatientTolSum(Double inpatientTolSum) {
		this.inpatientTolSum = inpatientTolSum;
	}
	public Double getInpatientFee() {
		return inpatientFee;
	}
	public void setInpatientFee(Double inpatientFee) {
		this.inpatientFee = inpatientFee;
	}
	public Double getDrugFee() {
		return drugFee;
	}
	public void setDrugFee(Double drugFee) {
		this.drugFee = drugFee;
	}
	public Double getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}
	public String getDrugFeeRatio() {
		return drugFeeRatio;
	}
	public void setDrugFeeRatio(String drugFeeRatio) {
		this.drugFeeRatio = drugFeeRatio;
	}
	
	
}
