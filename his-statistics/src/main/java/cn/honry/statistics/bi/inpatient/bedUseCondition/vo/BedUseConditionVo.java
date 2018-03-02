package cn.honry.statistics.bi.inpatient.bedUseCondition.vo;

/**
 * 病床使用情况vo
 * @author zhuxiaolu
 * @createDate：2016/7/27
 * @version 1.0
 */
public class BedUseConditionVo {
	
	private String deptName;
	private Integer actualBed;//实有床位数
	private Integer actOpenBedDay;//实际开放总床日数
	private Integer aveOpenBed;//平均开放病床
	private Integer actOccBedDay;//实际占用总床日数
	private Integer disPatOccBedDay;//出院者占用总床日数
	private Integer bedTurnTimes;//病床周转次数
	private Integer bedWorkDay;//病床工作日
	private double disPatAveDay;//出院者平均住院日
	private double beddoorEmergency;//每床与每日门急诊诊次之比
	private Integer outHospital;//出院人数
	private double sum;//门诊人次
	
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Integer getActualBed() {
		return actualBed;
	}
	public void setActualBed(Integer actualBed) {
		this.actualBed = actualBed;
	}
	public Integer getActOpenBedDay() {
		return actOpenBedDay;
	}
	public void setActOpenBedDay(Integer actOpenBedDay) {
		this.actOpenBedDay = actOpenBedDay;
	}
	public Integer getAveOpenBed() {
		return aveOpenBed;
	}
	public void setAveOpenBed(Integer aveOpenBed) {
		this.aveOpenBed = aveOpenBed;
	}
	public Integer getActOccBedDay() {
		return actOccBedDay;
	}
	public void setActOccBedDay(Integer actOccBedDay) {
		this.actOccBedDay = actOccBedDay;
	}
	public Integer getDisPatOccBedDay() {
		return disPatOccBedDay;
	}
	public void setDisPatOccBedDay(Integer disPatOccBedDay) {
		this.disPatOccBedDay = disPatOccBedDay;
	}
	public Integer getBedTurnTimes() {
		return bedTurnTimes;
	}
	public void setBedTurnTimes(Integer bedTurnTimes) {
		this.bedTurnTimes = bedTurnTimes;
	}
	public Integer getBedWorkDay() {
		return bedWorkDay;
	}
	public void setBedWorkDay(Integer bedWorkDay) {
		this.bedWorkDay = bedWorkDay;
	}
	public double getDisPatAveDay() {
		return disPatAveDay;
	}
	public void setDisPatAveDay(double disPatAveDay) {
		this.disPatAveDay = disPatAveDay;
	}
	
	public double getBeddoorEmergency() {
		return beddoorEmergency;
	}
	public void setBeddoorEmergency(double beddoorEmergency) {
		this.beddoorEmergency = beddoorEmergency;
	}
	public Integer getOutHospital() {
		return outHospital;
	}
	public void setOutHospital(Integer outHospital) {
		this.outHospital = outHospital;
	}
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	
	
	

	
}
