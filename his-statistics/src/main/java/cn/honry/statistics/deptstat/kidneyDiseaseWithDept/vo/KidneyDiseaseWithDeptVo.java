package cn.honry.statistics.deptstat.kidneyDiseaseWithDept.vo;

public class KidneyDiseaseWithDeptVo {
	private String deptName;//科室
	private String xiangmu;//项目
	private String lastDate;//上年同期
	private String date;//选择时间
	private String differ;//增减数
	private String differPer;//增减率（%）
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getXiangmu() {
		return xiangmu;
	}
	public void setXiangmu(String xiangmu) {
		this.xiangmu = xiangmu;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLastDate() {
		return lastDate;
	}
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	public String getDiffer() {
		return differ;
	}
	public void setDiffer(String differ) {
		this.differ = differ;
	}
	public String getDifferPer() {
		return differPer;
	}
	public void setDifferPer(String differPer) {
		this.differPer = differPer;
	}
	
	
}
