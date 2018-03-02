package cn.honry.inpatient.auditDrug.vo;

public class DrugVo {
	/**药品名称代码**/
	private String itemCode;
	/**药品申请数量**/
	private Double itemSum;
	/**申请药房**/
	private String deptCode;
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public Double getItemSum() {
		return itemSum;
	}
	public void setItemSum(Double itemSum) {
		this.itemSum = itemSum;
	}
	
	
}
