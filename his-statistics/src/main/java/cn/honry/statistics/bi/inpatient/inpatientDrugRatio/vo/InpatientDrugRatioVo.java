package cn.honry.statistics.bi.inpatient.inpatientDrugRatio.vo;

public class InpatientDrugRatioVo {
	/**时间*/
	private String timeChose;
	/**科室*/
	private String deptCode;
	/**门诊人数*/
	private Double inpatientSum;
	/**门诊费用*/
	private Double inpatientFee;
	/**药品费用*/
	private Double drugFee;
	/**人均*/
	private Double perFee;
	/**日均*/
	private Double dayFee;
	
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
	public Double getInpatientSum() {
		return inpatientSum;
	}
	public void setInpatientSum(Double inpatientSum) {
		this.inpatientSum = inpatientSum;
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
	public Double getPerFee() {
		return perFee;
	}
	public void setPerFee(Double perFee) {
		this.perFee = perFee;
	}
	public Double getDayFee() {
		return dayFee;
	}
	public void setDayFee(Double dayFee) {
		this.dayFee = dayFee;
	}
	
}
