package cn.honry.outpatient.blacklist.vo;

public class EmployeeBlackListVo {
	private String reason;
	private String employeeid;
	private String employeeName;
	private String employeeOldName;
	private String blackListid;

	
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getEmployeeid() {
		return employeeid;
	}
	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeOldName() {
		return employeeOldName;
	}
	public void setEmployeeOldName(String employeeOldName) {
		this.employeeOldName = employeeOldName;
	}
	public String getBlackListid() {
		return blackListid;
	}
	public void setBlackListid(String blackListid) {
		this.blackListid = blackListid;
	}
	
}
