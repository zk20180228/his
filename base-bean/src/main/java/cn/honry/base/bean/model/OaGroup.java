package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class OaGroup extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**员工编号*/
	private String empCode;
	
	/**员工所属组编号*/
	private String groupCode;
	
	/**备注*/
	private String backUp;

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getBackUp() {
		return backUp;
	}

	public void setBackUp(String backUp) {
		this.backUp = backUp;
	}
	
}
