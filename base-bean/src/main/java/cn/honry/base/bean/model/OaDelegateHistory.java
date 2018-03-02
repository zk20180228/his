package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 代理记录表
 * @author luyanshou
 *
 */
public class OaDelegateHistory extends Entity {

	
	private static final long serialVersionUID = 1L;

	/** 负责人. */
    private String assignee;

    /** 代理人. */
    private String attorney;

    /** 代理时间. */
    private Date delegateTime;

    /** 关联的人工任务id. */
    private String taskInfoId;

    /** 流程定义id. */
    private String processDefinitionId;

    /** 任务定义key(节点id). */
    private String taskDefinitionKey;

    /** 状态. */
    private Integer status;

    /** 租户id. */
    private String tenantId;
    
    /** 流程名称. */
    private String processName;
    
    /** 节点名称. */
    private String activityName;
    
    /** 负责人名称. */
    private String assingeName;
    
    /** 委托人名称. */
    private String attorneyName;
	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getAttorney() {
		return attorney;
	}

	public void setAttorney(String attorney) {
		this.attorney = attorney;
	}

	public Date getDelegateTime() {
		return delegateTime;
	}

	public void setDelegateTime(Date delegateTime) {
		this.delegateTime = delegateTime;
	}

	public String getTaskInfoId() {
		return taskInfoId;
	}

	public void setTaskInfoId(String taskInfoId) {
		this.taskInfoId = taskInfoId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getAssingeName() {
		return assingeName;
	}

	public void setAssingeName(String assingeName) {
		this.assingeName = assingeName;
	}

	public String getAttorneyName() {
		return attorneyName;
	}

	public void setAttorneyName(String attorneyName) {
		this.attorneyName = attorneyName;
	}
    
}
