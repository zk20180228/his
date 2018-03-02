package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;


/**
 * 意见箱管理员维护表
 * @author zxl
 * Date:2015/05/20 15:30
 */

public class MAdviceManage extends Entity implements java.io.Serializable  {	

	/**员工编号**/
	private String userAccount;
	/**姓名**/
	private String empName;
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	 
	
	
	
}