package cn.honry.inner.statistics.peopleNumOfOperation.vo;

public class PeopleNumOfOperationVo {
	private String dept_name;//科室名称
	private String deptCode;//科室
	private Double optnums;//手术人数
	private Double optcounts;//手术例数
	private Double cyzs;//出院总数
	private String sszb;//手术占比
	private Double totalPatient;//全院患者
	private String ssrszqyb;//手术人数占全院比
	private Double ssrszqybb;//手术人数占全院比
	private String finalDate;//时间
	private String transOut;//转出
	private String outPatTrans;//转出
	private String operaPro;//手术占比
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public Double getOptnums() {
		return optnums;
	}
	public void setOptnums(Double optnums) {
		this.optnums = optnums;
	}
	public Double getOptcounts() {
		return optcounts;
	}
	public void setOptcounts(Double optcounts) {
		this.optcounts = optcounts;
	}
	public Double getCyzs() {
		return cyzs;
	}
	public void setCyzs(Double cyzs) {
		this.cyzs = cyzs;
	}
	public String getSszb() {
		return sszb;
	}
	public void setSszb(String sszb) {
		this.sszb = sszb;
	}
	public Double getTotalPatient() {
		return totalPatient;
	}
	public void setTotalPatient(Double totalPatient) {
		this.totalPatient = totalPatient;
	}
	public String getSsrszqyb() {
		return ssrszqyb;
	}
	public void setSsrszqyb(String ssrszqyb) {
		this.ssrszqyb = ssrszqyb;
	}
	public Double getSsrszqybb() {
		return ssrszqybb;
	}
	public void setSsrszqybb(Double ssrszqybb) {
		this.ssrszqybb = ssrszqybb;
	}
	public String getFinalDate() {
		return finalDate;
	}
	public void setFinalDate(String finalDate) {
		this.finalDate = finalDate;
	}
	public String getTransOut() {
		return transOut;
	}
	public void setTransOut(String transOut) {
		this.transOut = transOut;
	}
	public String getOutPatTrans() {
		return outPatTrans;
	}
	public void setOutPatTrans(String outPatTrans) {
		this.outPatTrans = outPatTrans;
	}
	public String getOperaPro() {
		return operaPro;
	}
	public void setOperaPro(String operaPro) {
		this.operaPro = operaPro;
	}
	
}
