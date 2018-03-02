package cn.honry.statistics.deptstat.diseaseSurveillance.vo;


public class DiseaseSurveillanceVo {
	
	private String id;//序号
	private String diseasesName;//重点病种名称
	private String count;//总例数
	private String death;//死亡
	private String mortality;//死亡率
	private String twoWeek;//两周内再入院
	private String oneWeek;//一周内再入院
	private String averageDay;//平均住院日
	private String averageFee;//平均住院费用
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDiseasesName() {
		return diseasesName;
	}
	public void setDiseasesName(String diseasesName) {
		this.diseasesName = diseasesName;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getDeath() {
		return death;
	}
	public void setDeath(String death) {
		this.death = death;
	}
	public String getMortality() {
		return mortality;
	}
	public void setMortality(String mortality) {
		this.mortality = mortality;
	}
	public String getTwoWeek() {
		return twoWeek;
	}
	public void setTwoWeek(String twoWeek) {
		this.twoWeek = twoWeek;
	}
	public String getOneWeek() {
		return oneWeek;
	}
	public void setOneWeek(String oneWeek) {
		this.oneWeek = oneWeek;
	}
	public String getAverageDay() {
		return averageDay;
	}
	public void setAverageDay(String averageDay) {
		this.averageDay = averageDay;
	}
	public String getAverageFee() {
		return averageFee;
	}
	public void setAverageFee(String averageFee) {
		this.averageFee = averageFee;
	}
	
	
}
