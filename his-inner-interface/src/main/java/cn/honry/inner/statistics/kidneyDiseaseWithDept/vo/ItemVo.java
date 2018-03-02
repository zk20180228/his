package cn.honry.inner.statistics.kidneyDiseaseWithDept.vo;

public class ItemVo {
	private String dept_name;//科室名称
	private String dept_code;//科室编号
	private Integer ruYuNum=0;//入院人次
	private Integer chuYUNum=0;//出院人次
	private Integer bedNum=0;//病床周转次数
	private Integer beds=0;//病床数
	private Integer bedUsed=0;//病床使用
	private String bedPer;//病床使用率（%）
	private Double avgInYuDays=0d;//平均住院天数
	private String saveGoodPer;//住院抢救成功率（%）
	private Integer workNum=0;//门诊工作量
	private Double totalCost=0d;//总收入（万元）
	private Double menCost=0d;//门诊收入（万元）
	private Double zhuCost=0d;//住院收入（万元）
	
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getDept_code() {
		return dept_code;
	}
	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
	}
	public String getBedPer() {
		return bedPer;
	}
	public void setBedPer(String bedPer) {
		this.bedPer = bedPer;
	}
	public Integer getRuYuNum() {
		return ruYuNum;
	}
	public void setRuYuNum(Integer ruYuNum) {
		this.ruYuNum = ruYuNum;
	}
	public Integer getChuYUNum() {
		return chuYUNum;
	}
	public void setChuYUNum(Integer chuYUNum) {
		this.chuYUNum = chuYUNum;
	}
	public Integer getBedNum() {
		return bedNum;
	}
	public void setBedNum(Integer bedNum) {
		this.bedNum = bedNum;
	}

	public Integer getBeds() {
		return beds;
	}
	public void setBeds(Integer beds) {
		this.beds = beds;
	}
	public Integer getBedUsed() {
		return bedUsed;
	}
	public void setBedUsed(Integer bedUsed) {
		this.bedUsed = bedUsed;
	}
	public Double getAvgInYuDays() {
		return avgInYuDays;
	}
	public void setAvgInYuDays(Double avgInYuDays) {
		this.avgInYuDays = avgInYuDays;
	}
	public String getSaveGoodPer() {
		return saveGoodPer;
	}
	public void setSaveGoodPer(String saveGoodPer) {
		this.saveGoodPer = saveGoodPer;
	}
	public Double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
	public Double getMenCost() {
		return menCost;
	}
	public void setMenCost(Double menCost) {
		this.menCost = menCost;
	}
	public Double getZhuCost() {
		return zhuCost;
	}
	public void setZhuCost(Double zhuCost) {
		this.zhuCost = zhuCost;
	}
	public Integer getWorkNum() {
		return workNum;
	}
	public void setWorkNum(Integer workNum) {
		this.workNum = workNum;
	}
	
}
