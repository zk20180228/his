package cn.honry.oa.activiti.queryFlow.vo;

import java.util.Date;

public class OaTaskInfoVAct {
	private String id;
	/** 业务标识. */
    private String businessKey;

    /** 代码. */
    private String code;

    /** 任务名称. */
    private String name;

    /** 任务描述. */
    private String description;

    /** 优先级. */
    private Integer priority;

    /** 分类. */
    private String category;

    /** 表单. */
    private String form;

    /** 租户. */
    private String tenantId;

    /** 状态. */
    private String status;

    /** 暂停状态. */
    private String suspendStatus;

    /** 代理状态. */
    private String delegateStatus;

    /** 完成状态. */
    private String completeStatus;

    /** 跳过状态. */
    private String skipStatus;

    /** 升级状态. */
    private String escalateStatus;

    /** 抄送状态. */
    private String copyStatus;

    /** 抄送任务ID. */
    private String copyTaskId;

    /** 展示名称. */
    private String presentationName;

    /** 展示标题. */
    private String presentationSubject;


    /** 激活时间. */
    private Date activationTime;

    /** 领取时间. */
    private Date claimTime;

    /** 完成时间. */
    private Date completeTime;

    /** 过期时间. */
    private Date expirationTime;

    /** 最后修改时间. */
    private Date lastModifiedTime;

    /** 持续时间. */
    private String duration;

    

    /** 任务发起人. */
    private String initiator;

    /** 负责人. */
    private String assignee;

    /** 委托人（被代理）. */
    private String owner;

    /** 最后处理人. */
    private String lastModifier;

    /** 泳道. */
    private String swimlane;
    
    /**父级id*/
    private String parentId;

    /** 关联的任务ID. */
    private String taskId;

    /** 关联的分支ID. */
    private String executionId;

    /** 关联的流程实例ID. */
    private String processInstanceId;

    /** 关联的流程定义ID. */
    private String processDefinitionId;

    /** 扩展字段1. */
    private String attr1;

    /** 扩展字段2. */
    private String attr2;

    /** 扩展字段3. */
    private String attr3;

    /** 扩展字段4. */
    private String attr4;

    /** 扩展字段5. */
    private String attr5;

    /** 流程业务标识. */
    private String processBusinessKey;

    /** 关联的流程发起人. */
    private String processStarter;

    /** 分类. */
    private String catalog;

    /** 操作. */
    private String action;

    /** 意见. */
    private String comments;

    /** 消息. */
    private String message;
    
    private String actid;
    
    private String actprocinstid;
    private String actbusinesskey;
    private String actprocdefid;
    private Date actstarttime;
    private String actendtime;
    private String actduration;
    private String actstartuserid;
    private String actstartactid;
    private String actendactid;
    
    private String actsuperprocessinstanceid;
    private String actdeletereason;
    private String acttenantid;
    private String actname;
    
	public String getActid() {
		return actid;
	}

	public void setActid(String actid) {
		this.actid = actid;
	}

	public String getActprocinstid() {
		return actprocinstid;
	}

	public void setActprocinstid(String actprocinstid) {
		this.actprocinstid = actprocinstid;
	}

	public String getActbusinesskey() {
		return actbusinesskey;
	}

	public void setActbusinesskey(String actbusinesskey) {
		this.actbusinesskey = actbusinesskey;
	}

	public String getActprocdefid() {
		return actprocdefid;
	}

	public void setActprocdefid(String actprocdefid) {
		this.actprocdefid = actprocdefid;
	}


	public Date getActstarttime() {
		return actstarttime;
	}

	public void setActstarttime(Date actstarttime) {
		this.actstarttime = actstarttime;
	}

	public String getActendtime() {
		return actendtime;
	}

	public void setActendtime(String actendtime) {
		this.actendtime = actendtime;
	}

	public String getActduration() {
		return actduration;
	}

	public void setActduration(String actduration) {
		this.actduration = actduration;
	}

	public String getActstartuserid() {
		return actstartuserid;
	}

	public void setActstartuserid(String actstartuserid) {
		this.actstartuserid = actstartuserid;
	}

	public String getActstartactid() {
		return actstartactid;
	}

	public void setActstartactid(String actstartactid) {
		this.actstartactid = actstartactid;
	}

	public String getActendactid() {
		return actendactid;
	}

	public void setActendactid(String actendactid) {
		this.actendactid = actendactid;
	}

	public String getActsuperprocessinstanceid() {
		return actsuperprocessinstanceid;
	}

	public void setActsuperprocessinstanceid(String actsuperprocessinstanceid) {
		this.actsuperprocessinstanceid = actsuperprocessinstanceid;
	}

	public String getActdeletereason() {
		return actdeletereason;
	}

	public void setActdeletereason(String actdeletereason) {
		this.actdeletereason = actdeletereason;
	}

	public String getActtenantid() {
		return acttenantid;
	}

	public void setActtenantid(String acttenantid) {
		this.acttenantid = acttenantid;
	}

	public String getActname() {
		return actname;
	}

	public void setActname(String actname) {
		this.actname = actname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSuspendStatus() {
		return suspendStatus;
	}

	public void setSuspendStatus(String suspendStatus) {
		this.suspendStatus = suspendStatus;
	}

	public String getDelegateStatus() {
		return delegateStatus;
	}

	public void setDelegateStatus(String delegateStatus) {
		this.delegateStatus = delegateStatus;
	}

	public String getCompleteStatus() {
		return completeStatus;
	}

	public void setCompleteStatus(String completeStatus) {
		this.completeStatus = completeStatus;
	}

	public String getSkipStatus() {
		return skipStatus;
	}

	public void setSkipStatus(String skipStatus) {
		this.skipStatus = skipStatus;
	}

	public String getEscalateStatus() {
		return escalateStatus;
	}

	public void setEscalateStatus(String escalateStatus) {
		this.escalateStatus = escalateStatus;
	}

	public String getCopyStatus() {
		return copyStatus;
	}

	public void setCopyStatus(String copyStatus) {
		this.copyStatus = copyStatus;
	}

	public String getCopyTaskId() {
		return copyTaskId;
	}

	public void setCopyTaskId(String copyTaskId) {
		this.copyTaskId = copyTaskId;
	}

	public String getPresentationName() {
		return presentationName;
	}

	public void setPresentationName(String presentationName) {
		this.presentationName = presentationName;
	}

	public String getPresentationSubject() {
		return presentationSubject;
	}

	public void setPresentationSubject(String presentationSubject) {
		this.presentationSubject = presentationSubject;
	}

	public Date getActivationTime() {
		return activationTime;
	}

	public void setActivationTime(Date activationTime) {
		this.activationTime = activationTime;
	}

	public Date getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(Date claimTime) {
		this.claimTime = claimTime;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public String getSwimlane() {
		return swimlane;
	}

	public void setSwimlane(String swimlane) {
		this.swimlane = swimlane;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getAttr1() {
		return attr1;
	}

	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}

	public String getAttr2() {
		return attr2;
	}

	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}

	public String getAttr3() {
		return attr3;
	}

	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}

	public String getAttr4() {
		return attr4;
	}

	public void setAttr4(String attr4) {
		this.attr4 = attr4;
	}

	public String getAttr5() {
		return attr5;
	}

	public void setAttr5(String attr5) {
		this.attr5 = attr5;
	}

	public String getProcessBusinessKey() {
		return processBusinessKey;
	}

	public void setProcessBusinessKey(String processBusinessKey) {
		this.processBusinessKey = processBusinessKey;
	}

	public String getProcessStarter() {
		return processStarter;
	}

	public void setProcessStarter(String processStarter) {
		this.processStarter = processStarter;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
