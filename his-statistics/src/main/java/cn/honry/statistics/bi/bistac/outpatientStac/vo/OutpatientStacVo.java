package cn.honry.statistics.bi.bistac.outpatientStac.vo;

import java.util.Date;

public class OutpatientStacVo {
	private Integer businessHospitalbed;//住院核定床位
	private Integer businessHospitalbedTotals;//住院床位
	private Integer inpatientInfoNow;//当前在院人数
	private Integer inpatientInfoNowGo;//当前出院人数
	private Integer newInpatientInfoNow;//新增住院人数
	private Integer dayOperationApply;//当日手术例数
	private Integer operationApplys;//总手术例数
	private Integer countRegistration;//本日门诊量
	private Integer countRegistrationEme;//本日门诊急诊量
	private Double dayZCost;//查询当日住院实收
	private Double monthZCost;//查询当月住院实收
	private Double yearZCost;//查询当年住院实收
	private Double dayMCost;//当日门诊实收
	private Double monthMCost;//当月门诊实收
	private Double yearMCost;//当年门诊实收
	
	private Date sTime;
	private Date eTime;
	
	public Date getsTime() {
		return sTime;
	}
	public void setsTime(Date sTime) {
		this.sTime = sTime;
	}
	public Date geteTime() {
		return eTime;
	}
	public void seteTime(Date eTime) {
		this.eTime = eTime;
	}
	public Integer getBusinessHospitalbed() {
		return businessHospitalbed;
	}
	public void setBusinessHospitalbed(Integer businessHospitalbed) {
		this.businessHospitalbed = businessHospitalbed;
	}
	public Integer getBusinessHospitalbedTotals() {
		return businessHospitalbedTotals;
	}
	public void setBusinessHospitalbedTotals(Integer businessHospitalbedTotals) {
		this.businessHospitalbedTotals = businessHospitalbedTotals;
	}
	public Integer getInpatientInfoNow() {
		return inpatientInfoNow;
	}
	public void setInpatientInfoNow(Integer inpatientInfoNow) {
		this.inpatientInfoNow = inpatientInfoNow;
	}
	public Integer getInpatientInfoNowGo() {
		return inpatientInfoNowGo;
	}
	public void setInpatientInfoNowGo(Integer inpatientInfoNowGo) {
		this.inpatientInfoNowGo = inpatientInfoNowGo;
	}
	public Integer getNewInpatientInfoNow() {
		return newInpatientInfoNow;
	}
	public void setNewInpatientInfoNow(Integer newInpatientInfoNow) {
		this.newInpatientInfoNow = newInpatientInfoNow;
	}
	public Integer getDayOperationApply() {
		return dayOperationApply;
	}
	public void setDayOperationApply(Integer dayOperationApply) {
		this.dayOperationApply = dayOperationApply;
	}
	public Integer getOperationApplys() {
		return operationApplys;
	}
	public void setOperationApplys(Integer operationApplys) {
		this.operationApplys = operationApplys;
	}
	public Integer getCountRegistration() {
		return countRegistration;
	}
	public void setCountRegistration(Integer countRegistration) {
		this.countRegistration = countRegistration;
	}
	public Integer getCountRegistrationEme() {
		return countRegistrationEme;
	}
	public void setCountRegistrationEme(Integer countRegistrationEme) {
		this.countRegistrationEme = countRegistrationEme;
	}
	public Double getDayZCost() {
		return dayZCost;
	}
	public void setDayZCost(Double dayZCost) {
		this.dayZCost = dayZCost;
	}
	public Double getMonthZCost() {
		return monthZCost;
	}
	public void setMonthZCost(Double monthZCost) {
		this.monthZCost = monthZCost;
	}
	public Double getYearZCost() {
		return yearZCost;
	}
	public void setYearZCost(Double yearZCost) {
		this.yearZCost = yearZCost;
	}
	public Double getDayMCost() {
		return dayMCost;
	}
	public void setDayMCost(Double dayMCost) {
		this.dayMCost = dayMCost;
	}
	public Double getMonthMCost() {
		return monthMCost;
	}
	public void setMonthMCost(Double monthMCost) {
		this.monthMCost = monthMCost;
	}
	public Double getYearMCost() {
		return yearMCost;
	}
	public void setYearMCost(Double yearMCost) {
		this.yearMCost = yearMCost;
	}
	
	
	
}
