package cn.honry.inner.statistics.inneroperationProportion.vo;

public class InnerOperation {
	/**科室代码0-全部部门*/
	private String deptCode;
	/**药品编码*/
	private String deptName;
	/**出院人数*/
	private Integer total;
	/**转出人数*/
	private Integer total1;
	/**手术人数*/
	private Integer total2;
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getTotal1() {
		return total1;
	}
	public void setTotal1(Integer total1) {
		this.total1 = total1;
	}
	public Integer getTotal2() {
		return total2;
	}
	public void setTotal2(Integer total2) {
		this.total2 = total2;
	}
	
	
}
