package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class MetOpsArrange extends Entity implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**手术序号**/
	private String operationno;
	/**角色编码**/
	private String roleCode;
	/**员工代码**/
	private String emplCode;
	/**员工姓名**/
	private String emplName;
	/**0术前安排/1术后记录**/
	private String foreFlag;
	/**状态：正常/加班/直落等**/
	private String operKind;

	public String getOperationno() {
		return operationno;
	}
	public void setOperationno(String operationno) {
		this.operationno = operationno;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getEmplCode() {
		return emplCode;
	}
	public void setEmplCode(String emplCode) {
		this.emplCode = emplCode;
	}
	public String getEmplName() {
		return emplName;
	}
	public void setEmplName(String emplName) {
		this.emplName = emplName;
	}
	public String getForeFlag() {
		return foreFlag;
	}
	public void setForeFlag(String foreFlag) {
		this.foreFlag = foreFlag;
	}
	public String getOperKind() {
		return operKind;
	}
	public void setOperKind(String operKind) {
		this.operKind = operKind;
	}
	
}
