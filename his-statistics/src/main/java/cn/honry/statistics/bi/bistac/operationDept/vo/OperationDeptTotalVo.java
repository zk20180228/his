package cn.honry.statistics.bi.bistac.operationDept.vo;

public class OperationDeptTotalVo {
	private Double ssGzlCost;//手术工作量金额
	private Integer ssGzlNum;//手术例数
	private String dept_name;//科室名称
	private String inpatient_date;//手术日期
	public Double getSsGzlCost() {
		return ssGzlCost;
	}
	public void setSsGzlCost(Double ssGzlCost) {
		this.ssGzlCost = ssGzlCost;
	}
	public Integer getSsGzlNum() {
		return ssGzlNum;
	}
	public void setSsGzlNum(Integer ssGzlNum) {
		this.ssGzlNum = ssGzlNum;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getInpatient_date() {
		return inpatient_date;
	}
	public void setInpatient_date(String inpatient_date) {
		this.inpatient_date = inpatient_date;
	}
	@Override
	public String toString() {
		return "OperationDeptTotalVo [ssGzlCost=" + ssGzlCost + ", ssGzlNum="
				+ ssGzlNum + ", dept_name=" + dept_name + ", inpatient_date="
				+ inpatient_date + "]";
	}
	
}
