package cn.honry.oa.activiti.bpm.process.vo;

public class OaTaskForm {

	private boolean exists;//是否存在
    private boolean taskForm;//任务表单
    private String processDefinitionId;//流程定义id
    private String activityId;//活动节点id
    private String assignee;
    private String formKey;//表单code
    private String initiatorName;
	public boolean isExists() {
		return exists;
	}
	public void setExists(boolean exists) {
		this.exists = exists;
	}
	public boolean isTaskForm() {
		return taskForm;
	}
	public void setTaskForm(boolean taskForm) {
		this.taskForm = taskForm;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getFormKey() {
		return formKey;
	}
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}
	public String getInitiatorName() {
		return initiatorName;
	}
	public void setInitiatorName(String initiatorName) {
		this.initiatorName = initiatorName;
	}
    
}
