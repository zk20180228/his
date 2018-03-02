package cn.honry.statistics.sys.stop.vo;


public class OutPatientVo  {
	//科室名称
	private String deptName;
	//生病总数
	private Integer sumSick;
	//出差总数
	private Integer sumEvection;
	//开会总数
	private Integer sumMeet;
	//其他总数
	private Integer sumOther;
	//总数
	private Integer sum;
	//科室Id
	private  String deptId;
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Integer getSumSick() {
		return sumSick;
	}
	public void setSumSick(Integer sumSick) {
		this.sumSick = sumSick;
	}
	public Integer getSumEvection() {
		return sumEvection;
	}
	public void setSumEvection(Integer sumEvection) {
		this.sumEvection = sumEvection;
	}
	public Integer getSumMeet() {
		return sumMeet;
	}
	public void setSumMeet(Integer sumMeet) {
		this.sumMeet = sumMeet;
	}
	public Integer getSumOther() {
		return sumOther;
	}
	public void setSumOther(Integer sumOther) {
		this.sumOther = sumOther;
	}
	public Integer getSum() {
		return sum;
	}
	public void setSum(Integer sum) {
		this.sum = sum;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
}
