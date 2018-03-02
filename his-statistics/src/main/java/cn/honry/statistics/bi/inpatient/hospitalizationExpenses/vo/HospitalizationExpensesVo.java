package cn.honry.statistics.bi.inpatient.hospitalizationExpenses.vo;


public class HospitalizationExpensesVo {
	/**科室**/
	private String deptname;
	/** 计费日期**/
	private String feeDate;
	/** 总金额**/
	private Double totCost;
	/** 药品金额**/
	private Double drugs;
	/** 非药品金额 **/
	private Double noDrugs;
	/** 缴费次数**/
	private Integer passengers;
	
	/** 药品金额 /占比**/
	private String drugsPro;
	/** 非药品金额/占比 **/
	private String noDrugsPro;
	/** 缴费次数/平均每次缴费**/
	private String passengersAvg;
	
	
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getFeeDate() {
		return feeDate;
	}
	public void setFeeDate(String feeDate) {
		this.feeDate = feeDate;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public Double getDrugs() {
		return drugs;
	}
	public void setDrugs(Double drugs) {
		this.drugs = drugs;
	}
	public Double getNoDrugs() {
		return noDrugs;
	}
	public void setNoDrugs(Double noDrugs) {
		this.noDrugs = noDrugs;
	}
	public Integer getPassengers() {
		return passengers;
	}
	public void setPassengers(Integer passengers) {
		this.passengers = passengers;
	}
	public String getDrugsPro() {
		return drugsPro;
	}
	public void setDrugsPro(String drugsPro) {
		this.drugsPro = drugsPro;
	}
	public String getNoDrugsPro() {
		return noDrugsPro;
	}
	public void setNoDrugsPro(String noDrugsPro) {
		this.noDrugsPro = noDrugsPro;
	}
	public String getPassengersAvg() {
		return passengersAvg;
	}
	public void setPassengersAvg(String passengersAvg) {
		this.passengersAvg = passengersAvg;
	}
	
}
