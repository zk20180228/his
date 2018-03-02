package cn.honry.statistics.finance.detailedDaily.vo;

import java.util.List;

public class VdetailedDaily {
	/**费用总金额**/
	private  Double totCost;
	/**统计费用名称**/
	private String statName;
	/**患者科室**/
	private String deptName;
	/**住院号**/
	private String inpatientNo;
	/**患者姓名**/
	private String name;
	/**计费人姓名**/
	private String operName;
	/**计费人所在科室**/
	private String balanceOpername;
	/**报表所用**/
	private List<VdetailedDaily> vdetailedDaily;
	public List<VdetailedDaily> getVdetailedDaily() {
		return vdetailedDaily;
	}
	public void setVdetailedDaily(List<VdetailedDaily> vdetailedDaily) {
		this.vdetailedDaily = vdetailedDaily;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public String getStatName() {
		return statName;
	}
	public void setStatName(String statName) {
		this.statName = statName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOperName() {
		return operName;
	}
	public void setOperName(String operName) {
		this.operName = operName;
	}
	public String getBalanceOpername() {
		return balanceOpername;
	}
	public void setBalanceOpername(String balanceOpername) {
		this.balanceOpername = balanceOpername;
	}
}
