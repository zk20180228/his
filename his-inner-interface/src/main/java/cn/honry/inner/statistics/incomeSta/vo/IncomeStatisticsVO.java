package cn.honry.inner.statistics.incomeSta.vo;

public class IncomeStatisticsVO {
	private String areaname;//院区
	private String feename;//费别
	private Double fee;//费用
	
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	public String getFeename() {
		return feename;
	}
	public void setFeename(String feename) {
		this.feename = feename;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
}
