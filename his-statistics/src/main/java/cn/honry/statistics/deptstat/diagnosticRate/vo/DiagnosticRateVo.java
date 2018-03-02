package cn.honry.statistics.deptstat.diagnosticRate.vo;

public class DiagnosticRateVo {
	
	/**经门诊介绍住院的出院病人总人数**/
	private Integer outClicTotal;
	
	/**门诊签收住院时的诊断与出院诊断相符合数**/
	private Integer outCliConsistent;
	
	/**入院与出院诊断符合人数**/
	private Integer inhosConsistent;
	
	/**入院与出院诊断总人数**/
	private Integer inhosTotal;
	
	/**住院手术前后诊断符合人次数**/
	private Integer operaConsis;
	
	/**住院手术前后总人次数**/
	private Integer operaTotal;
	
	/**科室**/
	private String dept;
	
	/**院区**/
	private String campus;
	
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public Integer getOutClicTotal() {
		return outClicTotal;
	}
	public void setOutClicTotal(Integer outClicTotal) {
		this.outClicTotal = outClicTotal;
	}
	public Integer getOutCliConsistent() {
		return outCliConsistent;
	}
	public void setOutCliConsistent(Integer outCliConsistent) {
		this.outCliConsistent = outCliConsistent;
	}
	public Integer getInhosConsistent() {
		return inhosConsistent;
	}
	public void setInhosConsistent(Integer inhosConsistent) {
		this.inhosConsistent = inhosConsistent;
	}
	public Integer getInhosTotal() {
		return inhosTotal;
	}
	public void setInhosTotal(Integer inhosTotal) {
		this.inhosTotal = inhosTotal;
	}
	public Integer getOperaConsis() {
		return operaConsis;
	}
	public void setOperaConsis(Integer operaConsis) {
		this.operaConsis = operaConsis;
	}
	public Integer getOperaTotal() {
		return operaTotal;
	}
	public void setOperaTotal(Integer operaTotal) {
		this.operaTotal = operaTotal;
	}
	@Override
	public String toString() {
		return "DiagnosticRateVo [outClicTotal=" + outClicTotal
				+ ", outCliConsistent=" + outCliConsistent
				+ ", inhosConsistent=" + inhosConsistent + ", inhosTotal="
				+ inhosTotal + ", operaConsis=" + operaConsis + ", operaTotal="
				+ operaTotal + ", dept=" + dept + ", campus=" + campus + "]";
	}
	
}
