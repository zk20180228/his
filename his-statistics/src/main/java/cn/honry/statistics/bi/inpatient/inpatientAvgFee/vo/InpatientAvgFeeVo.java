package cn.honry.statistics.bi.inpatient.inpatientAvgFee.vo;

public class InpatientAvgFeeVo {
	/**时间*/
	private String timeChose;
	/**科室*/
	private String deptCode;
	/**住院人次*/
	private Double inSum;
	/**均次费用*/
	private Double perFee;
	
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
	public Double getInSum() {
		return inSum;
	}
	public void setInSum(Double inSum) {
		this.inSum = inSum;
	}
	public Double getPerFee() {
		return perFee;
	}
	public void setPerFee(Double perFee) {
		this.perFee = perFee;
	}
	
}
