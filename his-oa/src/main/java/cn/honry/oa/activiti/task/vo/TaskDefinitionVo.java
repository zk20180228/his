package cn.honry.oa.activiti.task.vo;

import java.util.ArrayList;
import java.util.List;

import cn.honry.base.bean.model.OaBpmConfCountersign;
import cn.honry.base.bean.model.OaTaskDefUser;

public class TaskDefinitionVo {

	public static final String CATALOG_ASSIGNEE = "assignee";
    public static final String CATALOG_CANDIDATE = "candidate";
    public static final String CATALOG_NOTIFICATION = "notification";
    public static final String TYPE_USER = "user";
    public static final String TYPE_GROUP = "group";
    private String id;
    private String code;
    private String name;
    private String assignStrategy;
    private String formType;
    private String formKey;
    private OaBpmConfCountersign countersign;
    private List<String> operations = new ArrayList<>();
    private String processDefinitionId;
    private List<OaTaskDefUser> taskUsers= new ArrayList<>();
    
	public List<OaTaskDefUser> getTaskUsers() {
		return taskUsers;
	}
	public void setTaskUsers(List<OaTaskDefUser> taskUsers) {
		this.taskUsers = taskUsers;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getAssignStrategy() {
		return assignStrategy;
	}
	public void setAssignStrategy(String assignStrategy) {
		this.assignStrategy = assignStrategy;
	}
	public String getFormType() {
		return formType;
	}
	public void setFormType(String formType) {
		this.formType = formType;
	}
	public String getFormKey() {
		return formKey;
	}
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}
	public OaBpmConfCountersign getCountersign() {
		return countersign;
	}
	public void setCountersign(OaBpmConfCountersign countersign) {
		this.countersign = countersign;
	}
	public List<String> getOperations() {
		return operations;
	}
	public void setOperations(List<String> operations) {
		this.operations = operations;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
    
	public void setAssignee(String assignee){
		for(OaTaskDefUser taskUser:taskUsers){
			if (CATALOG_ASSIGNEE.equals(taskUser.getCatalog())) {
                taskUser.setValue(assignee);
                return;
            }
		}
		OaTaskDefUser user= new OaTaskDefUser();
		user.setCatalog(CATALOG_ASSIGNEE);
        user.setType(TYPE_USER);
        user.setValue(assignee);
        taskUsers.add(user);
	}
	
	public void addCandidateUser(String candidateUser) {
        OaTaskDefUser taskUser = new OaTaskDefUser();
        taskUser.setCatalog(CATALOG_CANDIDATE);
        taskUser.setType(TYPE_USER);
        taskUser.setValue(candidateUser);
        taskUsers.add(taskUser);
    }

    public void addCandidateGroup(String candidateGroup) {
        OaTaskDefUser taskUser = new OaTaskDefUser();
        taskUser.setCatalog(CATALOG_CANDIDATE);
        taskUser.setType(TYPE_GROUP);
        taskUser.setValue(candidateGroup);
        taskUsers.add(taskUser);
    }
	
}
