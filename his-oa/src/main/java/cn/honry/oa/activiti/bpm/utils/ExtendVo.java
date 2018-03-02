package cn.honry.oa.activiti.bpm.utils;

public class ExtendVo {
	
	private String isAssigner;//是否可指定下一节点任务人
	private String message;//是否消息提示
	private String reject;//是否可驳回
	private String stepreject;//当节点可驳回时是否可逐级驳回
	private String urge;//是否可催办
	private String withdept;//是否可与上一节点同部门
	
	public String getIsAssigner() {
		return isAssigner;
	}
	public void setIsAssigner(String isAssigner) {
		this.isAssigner = isAssigner;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getReject() {
		return reject;
	}
	public void setReject(String reject) {
		this.reject = reject;
	}
	public String getStepreject() {
		return stepreject;
	}
	public void setStepreject(String stepreject) {
		this.stepreject = stepreject;
	}
	public String getUrge() {
		return urge;
	}
	public void setUrge(String urge) {
		this.urge = urge;
	}
	public String getWithdept() {
		return withdept;
	}
	public void setWithdept(String withdept) {
		this.withdept = withdept;
	}
	
}
