package cn.honry.statistics.bi.bistac.inpatientIncome.vo;

public class InpatientIncome {
	private String deptName;// 下单科室
	private Double cost1 = 0.0d;// 住院药品金额1
	private Double NoCost1 = 0.0d;// 住院非药品金额1
	private Double costs1= 0.0d;;// 住院总金额1
	private String tongbi1="0.00%";// 住院总金额同比增长%1
	private Double cost2 = 0.0d;// 住院药品金额2
	private Double NoCost2 = 0.0d;// 住院非药品金额2
	private Double costs2= 0.0d;;// 住院总金额2
	private String tongbi2="0.00%";// 住院总金额同比增长%2
	private Double cost3 = 0.0d;// 住院药品金额3
	private Double NoCost3 = 0.0d;// 住院非药品金额3
	private Double costs3= 0.0d;;// 住院总金额3
	private String tongbi3="0.00%";// 住院总金额同比增长%3
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Double getCost1() {
		return cost1;
	}
	public void setCost1(Double cost1) {
		this.cost1 = cost1;
	}
	public Double getNoCost1() {
		return NoCost1;
	}
	public void setNoCost1(Double noCost1) {
		NoCost1 = noCost1;
	}
	public Double getCosts1() {
		return costs1;
	}
	public void setCosts1(Double costs1) {
		this.costs1 = costs1;
	}
	public String getTongbi1() {
		return tongbi1;
	}
	public void setTongbi1(String tongbi1) {
		this.tongbi1 = tongbi1;
	}
	public Double getCost2() {
		return cost2;
	}
	public void setCost2(Double cost2) {
		this.cost2 = cost2;
	}
	public Double getNoCost2() {
		return NoCost2;
	}
	public void setNoCost2(Double noCost2) {
		NoCost2 = noCost2;
	}
	public Double getCosts2() {
		return costs2;
	}
	public void setCosts2(Double costs2) {
		this.costs2 = costs2;
	}
	public String getTongbi2() {
		return tongbi2;
	}
	public void setTongbi2(String tongbi2) {
		this.tongbi2 = tongbi2;
	}
	public Double getCost3() {
		return cost3;
	}
	public void setCost3(Double cost3) {
		this.cost3 = cost3;
	}
	public Double getNoCost3() {
		return NoCost3;
	}
	public void setNoCost3(Double noCost3) {
		NoCost3 = noCost3;
	}
	public Double getCosts3() {
		return costs3;
	}
	public void setCosts3(Double costs3) {
		this.costs3 = costs3;
	}
	public String getTongbi3() {
		return tongbi3;
	}
	public void setTongbi3(String tongbi3) {
		this.tongbi3 = tongbi3;
	}
	
}
