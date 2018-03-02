package cn.honry.inner.operation.record.vo;

public class OperationUserVo {

	/**
	 * @Description:手术人员安排表ID
	 */
	private String id;
	/**
	 * @Description:手术序号
	 */
	private String operationId;
	/**
	 * @Description:角色类型
	 */
	private String roleCode;
	/**
	 * @Description:员工代码
	 */
	private String emplCode;
	/**
	 * @Description:员工姓名
	 */
	private String emplName;
	/**
	 * 0手术申请；1手术安排；2手术登记；4麻醉安排；5麻醉登记
	 */
	private String foreFlag;
	/**
	 * 状态：正常/加班/直落等
	 */
	private String operKind;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
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
