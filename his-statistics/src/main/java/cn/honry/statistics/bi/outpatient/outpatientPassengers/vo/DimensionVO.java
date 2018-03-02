package cn.honry.statistics.bi.outpatient.outpatientPassengers.vo;
//门诊人次统计 VO
public class DimensionVO {
	/**总人次**/
	private Integer sumPeople;
	/**急诊人次**/
	private Integer emerGencyPeople;
	/**普诊人次**/
	private Integer ordinaryPeople;
	/**年度**/
	private String years;
	/**科室**/
	private String dept;
	/**地域**/
	private String region;
	/**开始时间**/
	private Integer start;
	/**结束时间**/
	private Integer end;
	/**性别**/
	private String sex;
	/**统计大类**/
	private String codeName;
	/**比例**/
	private String sumtotcosts;
	/**年龄**/
	private String age;
	/**年龄单位**/
	private String ageUnit;
	public String getSumtotcosts() {
		return sumtotcosts;
	}
	public void setSumtotcosts(String sumtotcosts) {
		this.sumtotcosts = sumtotcosts;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getSumPeople() {
		return sumPeople;
	}
	public void setSumPeople(Integer sumPeople) {
		this.sumPeople = sumPeople;
	}
	public Integer getEmerGencyPeople() {
		return emerGencyPeople;
	}
	public void setEmerGencyPeople(Integer emerGencyPeople) {
		this.emerGencyPeople = emerGencyPeople;
	}
	public Integer getOrdinaryPeople() {
		return ordinaryPeople;
	}
	public void setOrdinaryPeople(Integer ordinaryPeople) {
		this.ordinaryPeople = ordinaryPeople;
	}
	public String getYears() {
		return years;
	}
	public void setYears(String years) {
		this.years = years;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getEnd() {
		return end;
	}
	public void setEnd(Integer end) {
		this.end = end;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getAgeUnit() {
		return ageUnit;
	}
	public void setAgeUnit(String ageUnit) {
		this.ageUnit = ageUnit;
	}
	
	
	
	
}
