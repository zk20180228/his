package cn.honry.statistics.bi.outpatient.outpatientTotCost.vo;
//门诊费用维度--指标
public class TotCostDimension {
	/**科室维度**/
	private String dept;
	/**费别维度**/
	private String feeName;
	/**医生维度**/
	private String emp;
	/**时间维度**/
	private String years;
	/**费用**/
	private Double sumCost;
	/**费用比例**/
	private String proportion;
	/**环比**/
	private String monthly;
	/**同比**/
	private String production;
	/**结算统计大类**/
	private String codeName;
	/**平均费用**/
	private Double avgtotcost;
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getFeeName() {
		return feeName;
	}
	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}
	public String getEmp() {
		return emp;
	}
	public void setEmp(String emp) {
		this.emp = emp;
	}
	public String getYears() {
		return years;
	}
	public void setYears(String years) {
		this.years = years;
	}
	public Double getSumCost() {
		return sumCost;
	}
	public void setSumCost(Double sumCost) {
		this.sumCost = sumCost;
	}
	public String getProportion() {
		return proportion;
	}
	public void setProportion(String proportion) {
		this.proportion = proportion;
	}
	public String getMonthly() {
		return monthly;
	}
	public void setMonthly(String monthly) {
		this.monthly = monthly;
	}
	public String getProduction() {
		return production;
	}
	public void setProduction(String production) {
		this.production = production;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public Double getAvgtotcost() {
		return avgtotcost;
	}
	public void setAvgtotcost(Double avgtotcost) {
		this.avgtotcost = avgtotcost;
	}
	
	
}
