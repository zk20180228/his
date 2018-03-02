package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiBaseContractunit entity. @author MyEclipse Persistence Tools
 */

public class OaReminders implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String procedureId;
	private String procedureName;
	private Integer reminderNum;
	private String reminder;
	private String reminderName;
	private Date remindTime;
	private String reminderd;
	private String reminderdName;
	private String remindNode;
	private String remindenodeName;
	private String remindreContent;
	private Date remidereTime;
	private Integer remindreStatus;
	private Integer type;
	private String back1;
	private String createUser;
	private Date createTime;
	private String remindcontent;
	private Date remidetime;
	private Date applyTime;
	/**删除标志**/
	private Integer del_flg=0;
	
	
	private String flag;//处理标记
	private String taskInfoId;//task_info的id
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getTaskInfoId() {
		return taskInfoId;
	}

	public void setTaskInfoId(String taskInfoId) {
		this.taskInfoId = taskInfoId;
	}

	public Integer getDel_flg() {
		return del_flg;
	}

	public void setDel_flg(Integer del_flg) {
		this.del_flg = del_flg;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRemindcontent() {
		return remindcontent;
	}

	public void setRemindcontent(String remindcontent) {
		this.remindcontent = remindcontent;
	}

	public Date getRemidetime() {
		return remidetime;
	}

	public void setRemidetime(Date remidetime) {
		this.remidetime = remidetime;
	}

	public String getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(String procedureId) {
		this.procedureId = procedureId;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public Integer getReminderNum() {
		return reminderNum;
	}

	public void setReminderNum(Integer reminderNum) {
		this.reminderNum = reminderNum;
	}

	public String getReminder() {
		return reminder;
	}

	public void setReminder(String reminder) {
		this.reminder = reminder;
	}

	public String getReminderName() {
		return reminderName;
	}

	public void setReminderName(String reminderName) {
		this.reminderName = reminderName;
	}

	public Date getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
	}

	public String getReminderd() {
		return reminderd;
	}

	public void setReminderd(String reminderd) {
		this.reminderd = reminderd;
	}

	public String getReminderdName() {
		return reminderdName;
	}

	public void setReminderdName(String reminderdName) {
		this.reminderdName = reminderdName;
	}

	public String getRemindNode() {
		return remindNode;
	}

	public void setRemindNode(String remindNode) {
		this.remindNode = remindNode;
	}

	public String getRemindenodeName() {
		return remindenodeName;
	}

	public void setRemindenodeName(String remindenodeName) {
		this.remindenodeName = remindenodeName;
	}

	public String getRemindreContent() {
		return remindreContent;
	}

	public void setRemindreContent(String remindreContent) {
		this.remindreContent = remindreContent;
	}

	public Date getRemidereTime() {
		return remidereTime;
	}

	public void setRemidereTime(Date remidereTime) {
		this.remidereTime = remidereTime;
	}

	public Integer getRemindreStatus() {
		return remindreStatus;
	}

	public void setRemindreStatus(Integer remindreStatus) {
		this.remindreStatus = remindreStatus;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBack1() {
		return back1;
	}

	public void setBack1(String back1) {
		this.back1 = back1;
	}

	
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	// Constructors

	/** default constructor */
	public OaReminders() {
	}

	

	
}