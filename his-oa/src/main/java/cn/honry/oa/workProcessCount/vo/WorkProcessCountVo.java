package cn.honry.oa.workProcessCount.vo;

import java.io.Serializable;

/**
 * 
 * <p>工作流程统计,工作的vo</p>
 * @Author: zhangkui
 * @CreateDate: 2017年7月19日 下午6:14:12 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年7月19日 下午6:14:12 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class WorkProcessCountVo implements Serializable{

	private static final long serialVersionUID = 4127269632023445135L;
	//1？工作和当前登录的人是否有关系，怎么维护关系
	//2?每一项操作都可能不同，是否加操作标识字段
	//3?工作的标识：待办,办结,挂起,委托,这些项是相互独立的吗？
	//4.状态：如处理中，编码，待编。。
	//5?我经办的步骤(流程图)什么意思
	//6?流程的类型,请假审批，固定资产类型等等，是在什么时候确定的？
	//7?公共附件是什么？
	//8?主办什么意思？
	//9？批注什么意思？
	
	private String id;//id
	private String workName;//工作名称/文号
	private String serialNumber;//流水号
	private String processMap;//我经办的步骤(流程图)
	private String createPerson;//发起人
	private String optionPerson;//办理人
	private String state;//任务的状态
	private String processSate;//流程的状态
	private String createTime;//创建时间
	private String arriveTime;//到达时间
	
	private String cancleTime;//取消挂起时间,可能会与办结时间一样
	
	private String endTime;//办结时间
	private String remainTime;//已停留
	private String option;//操作
	private String workSateType;//普通,紧急。。
	private String processType;//流程的类型,请假审批，固定资产类型
	private String workFlag;//工作的标识：待办,办结,挂起,委托,这些项是相互独立的吗？
	private String isAttention;//是否关注
	
	private String executionId;
	
	private String pbAttachment;//公共附件
	
	
	
	
	
	public String getPbAttachment() {
		return pbAttachment;
	}
	public void setPbAttachment(String pbAttachment) {
		this.pbAttachment = pbAttachment;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getProcessMap() {
		return processMap;
	}
	public void setProcessMap(String processMap) {
		this.processMap = processMap;
	}
	public String getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	public String getOptionPerson() {
		return optionPerson;
	}
	public void setOptionPerson(String optionPerson) {
		this.optionPerson = optionPerson;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getArriveTime() {
		return arriveTime;
	}
	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}
	public String getCancleTime() {
		return cancleTime;
	}
	public void setCancleTime(String cancleTime) {
		this.cancleTime = cancleTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getRemainTime() {
		return remainTime;
	}
	public void setRemainTime(String remainTime) {
		this.remainTime = remainTime;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}

	public String getWorkSateType() {
		return workSateType;
	}
	public void setWorkSateType(String workSateType) {
		this.workSateType = workSateType;
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getWorkFlag() {
		return workFlag;
	}
	public void setWorkFlag(String workFlag) {
		this.workFlag = workFlag;
	}
	public String getIsAttention() {
		return isAttention;
	}
	public void setIsAttention(String isAttention) {
		this.isAttention = isAttention;
	}
	public String getExecutionId() {
		return executionId;
	}
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}
	public String getProcessSate() {
		return processSate;
	}
	public void setProcessSate(String processSate) {
		this.processSate = processSate;
	}
	
	
	
	
	
}
