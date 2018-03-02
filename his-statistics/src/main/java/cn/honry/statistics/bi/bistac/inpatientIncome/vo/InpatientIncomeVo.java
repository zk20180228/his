package cn.honry.statistics.bi.bistac.inpatientIncome.vo;

import java.util.Date;

public class InpatientIncomeVo {
	private String deptName;// 下单科室
	private Double totCost1 = 0.0d;// 住院总实收金额1
	private Double supplyCost1 = 0.0d;// 住院总计价金额1
	private String totCostPer1;// 住院总实收金额%1
	private String tongbi1;// 住院总实收金额同比增长%1
	private Double totCost2 = 0.0d;// 住院总实收金额2
	private Double supplyCost2 = 0.0d;// 住院总计价金额2
	private String totCostPer2;// 住院总实收金额%2
	private String tongbi2;// 住院总实收金额同比增长%2
	private Double totCost3 = 0.0d;// 住院总实收金额3
	private Double supplyCost3 = 0.0d;// 住院总计价金额3
	private String totCostPer3;// 住院总实收金额%3
	private String tongbi3;// 住院总实收金额同比增长%3
	private Double lastSupplyCost1 = 0.0d;// 上月住院总实收金额1
	private Double lastSupplyCost2 = 0.0d;// 上月住院总实收金额2
	private Date sTime;// 最小时间
	private Date eTime;// 最大时间

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Double getTotCost1() {
		return totCost1;
	}

	public void setTotCost1(Double totCost1) {
		this.totCost1 = totCost1;
	}

	public Double getSupplyCost1() {
		return supplyCost1;
	}

	public void setSupplyCost1(Double supplyCost1) {
		this.supplyCost1 = supplyCost1;
	}

	public String getTotCostPer1() {
		return totCostPer1;
	}

	public void setTotCostPer1(String totCostPer1) {
		this.totCostPer1 = totCostPer1;
	}

	public String getTongbi1() {
		return tongbi1;
	}

	public void setTongbi1(String tongbi1) {
		this.tongbi1 = tongbi1;
	}

	public Double getTotCost2() {
		return totCost2;
	}

	public void setTotCost2(Double totCost2) {
		this.totCost2 = totCost2;
	}

	public Double getSupplyCost2() {
		return supplyCost2;
	}

	public void setSupplyCost2(Double supplyCost2) {
		this.supplyCost2 = supplyCost2;
	}

	public String getTotCostPer2() {
		return totCostPer2;
	}

	public void setTotCostPer2(String totCostPer2) {
		this.totCostPer2 = totCostPer2;
	}

	public String getTongbi2() {
		return tongbi2;
	}

	public void setTongbi2(String tongbi2) {
		this.tongbi2 = tongbi2;
	}

	public Double getTotCost3() {
		return totCost3;
	}

	public void setTotCost3(Double totCost3) {
		this.totCost3 = totCost3;
	}

	public Double getSupplyCost3() {
		return supplyCost3;
	}

	public void setSupplyCost3(Double supplyCost3) {
		this.supplyCost3 = supplyCost3;
	}

	public String getTotCostPer3() {
		return totCostPer3;
	}

	public void setTotCostPer3(String totCostPer3) {
		this.totCostPer3 = totCostPer3;
	}

	public String getTongbi3() {
		return tongbi3;
	}

	public void setTongbi3(String tongbi3) {
		this.tongbi3 = tongbi3;
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

	public Double getLastSupplyCost1() {
		return lastSupplyCost1;
	}

	public void setLastSupplyCost1(Double lastSupplyCost1) {
		this.lastSupplyCost1 = lastSupplyCost1;
	}

	public Double getLastSupplyCost2() {
		return lastSupplyCost2;
	}

	public void setLastSupplyCost2(Double lastSupplyCost2) {
		this.lastSupplyCost2 = lastSupplyCost2;
	}

}
