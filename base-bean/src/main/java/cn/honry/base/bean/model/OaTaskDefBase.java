package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 任务定义实体类
 * @author luyanshou
 *
 */
public class OaTaskDefBase extends Entity {

	
	private static final long serialVersionUID = 1L;

	/** 编号. */
    private String code;

    /** 名称. */
    private String name;

    /** 流程定义ID. */
    private String processDefinitionId;

    /** 表单编号. */
    private String formKey;

    /** 表单类型. */
    private String formType;

    /** 会签类型. */
    private String countersignType;

    /** 会签用户. */
    private String countersignUser;

    /** 会签策略. */
    private String countersignStrategy;

    /** 会签通过率. */
    private Integer countersignRate;

    /** 分配策略. */
    private String assignStrategy;

    /** 流程标识. */
    private String processDefinitionKey;

    /** 流程版本. */
    private Integer processDefinitionVersion;

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

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getCountersignType() {
		return countersignType;
	}

	public void setCountersignType(String countersignType) {
		this.countersignType = countersignType;
	}

	public String getCountersignUser() {
		return countersignUser;
	}

	public void setCountersignUser(String countersignUser) {
		this.countersignUser = countersignUser;
	}

	public String getCountersignStrategy() {
		return countersignStrategy;
	}

	public void setCountersignStrategy(String countersignStrategy) {
		this.countersignStrategy = countersignStrategy;
	}

	public Integer getCountersignRate() {
		return countersignRate;
	}

	public void setCountersignRate(Integer countersignRate) {
		this.countersignRate = countersignRate;
	}

	public String getAssignStrategy() {
		return assignStrategy;
	}

	public void setAssignStrategy(String assignStrategy) {
		this.assignStrategy = assignStrategy;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	public Integer getProcessDefinitionVersion() {
		return processDefinitionVersion;
	}

	public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
		this.processDefinitionVersion = processDefinitionVersion;
	}
    
}
