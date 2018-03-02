package cn.honry.statistics.deptstat.inpatientStatistics.vo;

public class InpatientStatisticsVo {
	private String code; //科室编码
	private Integer total;//科室人数
	private String  gender;//比例
	private Integer totals;//医院总人数
	
	
	public Integer getTotals() {
		return totals;
	}
	public void setTotals(Integer totals) {
		this.totals = totals;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	@Override
	public String toString() {
		return "InpatientStatisticsVo [code=" + code + ", total=" + total
				+ ", gender=" + gender + ", totals=" + totals + "]";
	}

}
