package cn.honry.statistics.bi.inpatient.expensesAnaly.vo;

import java.math.BigDecimal;

public class ExpensesAnalyVO {
	/** 科室维度 **/
	private String deptDimensionality;
	/**时间维度 **/
	private String timeChose;
	/**性别**/
	private String sex;
	/**费用类别**/
	private String codeName;
	/**费用总额**/
	private Double totCost;
	/**费用比例  **/
	private BigDecimal expensesProportion;
	/**环比 **/
	private Double chain;
	/**同比 **/
	private Double rose;
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public String getDeptDimensionality() {
		return deptDimensionality;
	}
	public void setDeptDimensionality(String deptDimensionality) {
		this.deptDimensionality = deptDimensionality;
	}
	public String getTimeChose() {
		return timeChose;
	}
	public void setTimeChose(String timeChose) {
		this.timeChose = timeChose;
	}
	public BigDecimal getExpensesProportion() {
		return expensesProportion;
	}
	public void setExpensesProportion(BigDecimal expensesProportion) {
		this.expensesProportion = expensesProportion;
	}
	public Double getChain() {
		return chain;
	}
	public void setChain(Double chain) {
		this.chain = chain;
	}
	public Double getRose() {
		return rose;
	}
	public void setRose(Double rose) {
		this.rose = rose;
	}
}
