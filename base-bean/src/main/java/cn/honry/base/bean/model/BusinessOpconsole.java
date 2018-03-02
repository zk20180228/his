package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;
/**
 * 手术台维护表
 * @author liuyuanyuan
 * @createDate 2015-08-10
 *
 */
public class BusinessOpconsole extends Entity{
	private static final long serialVersionUID = 1L;
	 /** 手术室编码 */
	private BusinessOproom roomCode;
	 /** 手术台代码 */
	 private String  consoleCode;
	 /**手术台名称 */
	 private String consoleName;
	 /**输入码 */
	 private String  inputCode;
	 /**所属科室*/
	 private String  deptCode;
	 /**
	  * 是否有效 
	  *  1有效/0无效
	  */
	 private Integer  validFlag;
	 /**备注 */
	 private String  remark;
	 /**
	  * 当前使用状态
	  * 0 空闲，1正在使用
	  */
	 private Integer usingState;
	 /**手术需要过程总数 */
	 private String  operationNo;
	 /**当前手术过程代码*/
	 private Integer stepNum; 
	 /**当前手术过程序号 */
	 private Integer curstepSeq;
	 /**麻醉机编码*/
	 private String  machineCode;
	 /**监护仪编码 */
	 private String  monitorCode;
	 /**麻醉机名称 */
	 private String  machineName; 
	 /**监护仪名称 */
	 private String  monitorName;
	//BusinessOproom表的关联内容
	private String Uid;
	
	public String getUid() {
		return Uid;
	}
	public void setUid(String uid) {
		Uid = uid;
	}
	public BusinessOproom getRoomCode() {
		return roomCode;
	}
	public void setRoomCode(BusinessOproom roomCode) {
		this.roomCode = roomCode;
	}
	public String getConsoleCode() {
		return consoleCode;
	}
	public void setConsoleCode(String consoleCode) {
		this.consoleCode = consoleCode;
	}
	public String getConsoleName() {
		return consoleName;
	}
	public void setConsoleName(String consoleName) {
		this.consoleName = consoleName;
	}
	public String getInputCode() {
		return inputCode;
	}
	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
	public Integer getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getUsingState() {
		return usingState;
	}
	public void setUsingState(Integer usingState) {
		this.usingState = usingState;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getOperationNo() {
		return operationNo;
	}
	public void setOperationNo(String operationNo) {
		this.operationNo = operationNo;
	}
	public Integer getStepNum() {
		return stepNum;
	}
	public void setStepNum(Integer stepNum) {
		this.stepNum = stepNum;
	}
	public Integer getCurstepSeq() {
		return curstepSeq;
	}
	public void setCurstepSeq(Integer curstepSeq) {
		this.curstepSeq = curstepSeq;
	}
	public String getMachineCode() {
		return machineCode;
	}
	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}
	public String getMonitorCode() {
		return monitorCode;
	}
	public void setMonitorCode(String monitorCode) {
		this.monitorCode = monitorCode;
	}
	public String getMachineName() {
		return machineName;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	public String getMonitorName() {
		return monitorName;
	}
	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}
	
	 
	 	 
}
