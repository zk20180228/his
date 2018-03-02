package cn.honry.finance.medicinelist.vo;

import java.util.Date;

public class RecipedetailVo {
	//处方号
	private String recipeNo;
	//科室
	private String dept;
	//科室名称
	private String deptName;
	//医生
	private String emp;
	//医生姓名
	private String empName;
	//总金额
	private Double sumMoney;
	//收费状态
	private Integer recipedStatus;
	/**挂号日期**/
	private Date regDate;
	/**划价时间**/
	private Date operDate;
	
	
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getEmp() {
		return emp;
	}
	public void setEmp(String emp) {
		this.emp = emp;
	}
	public Double getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(Double sumMoney) {
		this.sumMoney = sumMoney;
	}
	public Integer getRecipedStatus() {
		return recipedStatus;
	}
	public void setRecipedStatus(Integer recipedStatus) {
		this.recipedStatus = recipedStatus;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	
}
