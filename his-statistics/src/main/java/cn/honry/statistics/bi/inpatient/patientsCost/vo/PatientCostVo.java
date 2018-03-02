/**
 * 
 */
package cn.honry.statistics.bi.inpatient.patientsCost.vo;

/**
 * @author luyanshou
 *
 */
public class PatientCostVo {

	private String fee_code;//最小费用代码
	private String inhos_deptcode;//科室编码
	private String deptName;//科室名称
	private Double itemCost;//非药品费用
	private Double medicineCost;//药品费用
	private Double totCost;//总费用
	private String cost;//费用类别(统计费用类型)
	private String timeChose;//时间
	
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getTimeChose() {
		return timeChose;
	}
	public void setTimeChose(String timeChose) {
		this.timeChose = timeChose;
	}
	public String getFee_code() {
		return fee_code;
	}
	public void setFee_code(String fee_code) {
		this.fee_code = fee_code;
	}
	public String getInhos_deptcode() {
		return inhos_deptcode;
	}
	public void setInhos_deptcode(String inhos_deptcode) {
		this.inhos_deptcode = inhos_deptcode;
	}
	public Double getItemCost() {
		return itemCost;
	}
	public void setItemCost(Double itemCost) {
		this.itemCost = itemCost;
	}
	public Double getMedicineCost() {
		return medicineCost;
	}
	public void setMedicineCost(Double medicineCost) {
		this.medicineCost = medicineCost;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	
}
