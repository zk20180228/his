package cn.honry.oa.activiti.bpm.form.vo;

import java.util.Collections;
import java.util.List;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class FormParameterVo {

	public static final String OPERATION_BUSINESS_KEY = "businessKey";
    public static final String OPERATION_BPM_PROCESS_ID = "bpmProcessId";
    public static final String OPERATION_HUMAN_TASK_ID = "humanTaskId";
    public static final String OPERATION_COMMENT = "_humantask_comment_";
    public static final String OPERATION_ACTION = "_humantask_action_";
    private String businessKey = null;
    private String bpmProcessId = null;
    private String humanTaskId = null;
    private String action;
    private String comment = null;
    private MultiValueMap<String, String> multiValueMap=new LinkedMultiValueMap<String, String>();
    private ConfFormVo formVo;
    private String nextStep;
    private String processDefinitionId;
    private String assignee;//任务负责人
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public String getBpmProcessId() {
		return bpmProcessId;
	}
	public void setBpmProcessId(String bpmProcessId) {
		this.bpmProcessId = bpmProcessId;
	}
	public String getHumanTaskId() {
		return humanTaskId;
	}
	public void setHumanTaskId(String humanTaskId) {
		this.humanTaskId = humanTaskId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public MultiValueMap<String, String> getMultiValueMap() {
		return multiValueMap;
	}
	public void setMultiValueMap(MultiValueMap<String, String> multiValueMap) {
		this.multiValueMap = multiValueMap;
	}
	public ConfFormVo getFormVo() {
		return formVo;
	}
	public void setFormVo(ConfFormVo formVo) {
		this.formVo = formVo;
	}
	public String getNextStep() {
		return nextStep;
	}
	public void setNextStep(String nextStep) {
		this.nextStep = nextStep;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
    
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public List<String> getList(String key) {
        if (multiValueMap == null) {
            return Collections.emptyList();
        }

        return multiValueMap.get(key);
    }
}
