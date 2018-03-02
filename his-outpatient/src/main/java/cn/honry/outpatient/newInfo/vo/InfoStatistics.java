package cn.honry.outpatient.newInfo.vo;

public class InfoStatistics {

	//挂号总额
	private Integer limitSum;
	//挂号总限额
	private Integer limit;
	//加号额
	private Integer limitAdd;
	//时段挂号数
	private Integer limitDate;
	//预约限额
	private Integer limitPrere;
	//预约已挂
	private Integer limitPrereInfo;
	//网络额限
	private Integer limitNet;
	//特诊额限
	private Integer speciallimit;
	
	public Integer getLimitNet() {
		return limitNet;
	}
	public void setLimitNet(Integer limitNet) {
		this.limitNet = limitNet;
	}
	public Integer getSpeciallimit() {
		return speciallimit;
	}
	public void setSpeciallimit(Integer speciallimit) {
		this.speciallimit = speciallimit;
	}
	public Integer getLimitSum() {
		return limitSum;
	}
	public void setLimitSum(Integer limitSum) {
		this.limitSum = limitSum;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getLimitAdd() {
		return limitAdd;
	}
	public void setLimitAdd(Integer limitAdd) {
		this.limitAdd = limitAdd;
	}
	public Integer getLimitDate() {
		return limitDate;
	}
	public void setLimitDate(Integer limitDate) {
		this.limitDate = limitDate;
	}
	public Integer getLimitPrere() {
		return limitPrere;
	}
	public void setLimitPrere(Integer limitPrere) {
		this.limitPrere = limitPrere;
	}
	public Integer getLimitPrereInfo() {
		return limitPrereInfo;
	}
	public void setLimitPrereInfo(Integer limitPrereInfo) {
		this.limitPrereInfo = limitPrereInfo;
	}
	
	
	
}
