package cn.honry.statistics.bi.bistac.outpatientUseMedic.vo;

import java.util.Date;

public class OutpatientUseMedicVo{
	private String selectTime;//选择时间
	private Double drugFee;//月药品收入
	private Double totCost;//月总收入
	private Integer total;//月患者人次
	private Double avgDays;//月人均用要天数
	private Double days;//天人总用要天数
	private Double num;//用药数量
	private String doctCodeName;//开立医生
	private String regDpcdName;//开立科室
	private String type;//药品类别
	private Double cost1= 0.0d;//月住院药品金额
	private Double cost2= 0.0d;//月住院非药品金额
	
	private Double totCostTotal;//年用药金额
	private Date sTime;//开始时间
	private Date eTime;//结束时间
	
	public Double getNum() {
		return num;
	}

	public void setNum(Double num) {
		this.num = num;
	}

	public String getDoctCodeName() {
		return doctCodeName;
	}

	public void setDoctCodeName(String doctCodeName) {
		this.doctCodeName = doctCodeName;
	}

	public String getRegDpcdName() {
		return regDpcdName;
	}

	public void setRegDpcdName(String regDpcdName) {
		this.regDpcdName = regDpcdName;
	}

	public Double getDays() {
		return days;
	}

	public void setDays(Double days) {
		this.days = days;
	}

	public Double getCost1() {
		return cost1;
	}

	public void setCost1(Double cost1) {
		this.cost1 = cost1;
	}

	public Double getCost2() {
		return cost2;
	}

	public void setCost2(Double cost2) {
		this.cost2 = cost2;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getAvgDays() {
		return avgDays;
	}

	public void setAvgDays(Double avgDays) {
		this.avgDays = avgDays;
	}

	public String getSelectTime() {
		return selectTime;
	}

	public void setSelectTime(String selectTime) {
		this.selectTime = selectTime;
	}

	public Double getDrugFee() {
		return drugFee;
	}

	public void setDrugFee(Double drugFee) {
		this.drugFee = drugFee;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

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

	public Double getTotCost() {
		return totCost;
	}

	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}

	public Double getTotCostTotal() {
		return totCostTotal;
	}

	public void setTotCostTotal(Double totCostTotal) {
		this.totCostTotal = totCostTotal;
	}
}
