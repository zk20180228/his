package cn.honry.statistics.sys.outpatientInvoice.vo;

import java.util.Date;

public class InvoiceInfoVo {
	/**姓名**/
	private String name;
	/**年龄**/
	private Date age;
	/**门诊号**/
	private String no;
	/**科室**/
	private String dept;
	/**挂号日期**/
	private Date dates;
	/**总金额**/
	private Double sumMoney;
	/**医师**/
	private String emp;
	/**挂号员**/
	private String user;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getAge() {
		return age;
	}
	public void setAge(Date age) {
		this.age = age;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public Date getDates() {
		return dates;
	}
	public void setDates(Date dates) {
		this.dates = dates;
	}
	public Double getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(Double sumMoney) {
		this.sumMoney = sumMoney;
	}
	public String getEmp() {
		return emp;
	}
	public void setEmp(String emp) {
		this.emp = emp;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	
}
