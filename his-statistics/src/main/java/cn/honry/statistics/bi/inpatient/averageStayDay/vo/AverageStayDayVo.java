package cn.honry.statistics.bi.inpatient.averageStayDay.vo;

public class AverageStayDayVo {
	private String deptName;//科室名称
	private Integer aveStayDay;//平均住院日
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Integer getAveStayDay() {
		return aveStayDay;
	}
	public void setAveStayDay(Integer aveStayDay) {
		this.aveStayDay = aveStayDay;
	}
	
	
}
