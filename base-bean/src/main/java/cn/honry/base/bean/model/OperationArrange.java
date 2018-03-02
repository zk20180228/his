package cn.honry.base.bean.model;


import cn.honry.base.bean.business.Entity;
/**
 * 手术麻醉人员安排
 * @author zhangjin
 * @CreateDate：2016-04-11 
 *
 */
public class OperationArrange extends Entity{

	private static final long serialVersionUID = 1L;
	/**
	 * 手术序号
	 */
	private String operationId;
	/**
	 * 手术角色
	 * 洗手护士：wash 1,2,3……
	 * 巡回：tour 1,2,3……
	 * 进修：engage 1,2,3……
	 * 学生：student 1,2,3……
	 * 临时助手：thelper 1,2,3……
	 * 主麻：zanesthesia 1,2,3……
	 * 助麻：fanesthesia 1,2,3……
	 */
	private String roleCode;
	/**
	 * 员工代码
	 */
	private String emplCode;
	/**
	 * 员工姓名
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
	
	public String getOperationId() {
		return this.operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getRoleCode() {
		return this.roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getEmplCode() {
		return this.emplCode;
	}

	public void setEmplCode(String emplCode) {
		this.emplCode = emplCode;
	}

	public String getEmplName() {
		return this.emplName;
	}

	public void setEmplName(String emplName) {
		this.emplName = emplName;
	}

	public String getForeFlag() {
		return this.foreFlag;
	}

	public void setForeFlag(String foreFlag) {
		this.foreFlag = foreFlag;
	}

	public String getOperKind() {
		return this.operKind;
	}

	public void setOperKind(String operKind) {
		this.operKind = operKind;
	}
}
