package cn.honry.statistics.sys.empWorkStats.vo;

public class EmpWorkStatsVo {

	/**科室编码**/
	private String deptCode;
	/**科室名称**/
	private String deptName;
	/**坐诊医生编码**/
	private String empCode;
	/**坐诊医生名称**/
	private String empName;
	/**挂号人数**/
	private Integer infoSum;
	/**看诊人数**/
	private Integer seeSum;
	
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
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public Integer getInfoSum() {
		return infoSum;
	}
	public void setInfoSum(Integer infoSum) {
		this.infoSum = infoSum;
	}
	public Integer getSeeSum() {
		return seeSum;
	}
	public void setSeeSum(Integer seeSum) {
		this.seeSum = seeSum;
	}
	
	
	
	
}
