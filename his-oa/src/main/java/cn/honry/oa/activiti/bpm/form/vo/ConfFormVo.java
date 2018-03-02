package cn.honry.oa.activiti.bpm.form.vo;

import java.util.ArrayList;
import java.util.List;

public class ConfFormVo {

	private String id;
    private String code;
    private String name;
    private String content;
    private boolean redirect;
    private String url;
    private String processDefinitionId;
    private String taskId;
    private List<String> buttons = new ArrayList<String>();
    private String activityId;
    private String properties;
    private boolean autoCompleteFirstTask;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isRedirect() {
		return redirect;
	}
	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public List<String> getButtons() {
		return buttons;
	}
	public void setButtons(List<String> buttons) {
		this.buttons = buttons;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public boolean isAutoCompleteFirstTask() {
		return autoCompleteFirstTask;
	}
	public void setAutoCompleteFirstTask(boolean autoCompleteFirstTask) {
		this.autoCompleteFirstTask = autoCompleteFirstTask;
	}
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
    
}
