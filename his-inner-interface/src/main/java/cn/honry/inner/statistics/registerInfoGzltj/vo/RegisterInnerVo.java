package cn.honry.inner.statistics.registerInfoGzltj.vo;

public class RegisterInnerVo {

	private String deptCode;//科室code
	private String deptName;//科室名称
	private Integer num;//挂号数量
	private Double cost;//挂号金额
	private String dateXq;//星期几(1-星期日;2-星期一;...;7-星期六)
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public String getDateXq() {
		return dateXq;
	}
	public void setDateXq(String dateXq) {
		this.dateXq = dateXq;
	}
	
	
}
