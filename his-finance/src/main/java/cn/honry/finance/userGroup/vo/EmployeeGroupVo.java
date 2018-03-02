package cn.honry.finance.userGroup.vo;
/**
 * 财务分组每组员工信息Vo
 * @author yeguanqun
 *
 */
public class EmployeeGroupVo {
	private String id;//员工Id
	private String jobNo;//工作号
	private String name;//员工姓名
	private String deptName;//部门名称
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	
}
