package cn.honry.oa.activiti.queryFlow.vo;

import java.sql.Date;

public class remindersVo {
		private String id;
		private String procedureid;
		private String porcedurename;
		private Integer remindernum;
		private Date remindtime;
		private String reminderedname;
		private String remindrecontent;
		private String createuser;
		private Date createtime;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getProcedureid() {
			return procedureid;
		}
		public void setProcedureid(String procedureid) {
			this.procedureid = procedureid;
		}
		public String getPorcedurename() {
			return porcedurename;
		}
		public void setPorcedurename(String porcedurename) {
			this.porcedurename = porcedurename;
		}
		public Integer getRemindernum() {
			return remindernum;
		}
		public void setRemindernum(Integer remindernum) {
			this.remindernum = remindernum;
		}
		public Date getRemindtime() {
			return remindtime;
		}
		public void setRemindtime(Date remindtime) {
			this.remindtime = remindtime;
		}
		public String getReminderedname() {
			return reminderedname;
		}
		public void setReminderedname(String reminderedname) {
			this.reminderedname = reminderedname;
		}
		public String getRemindrecontent() {
			return remindrecontent;
		}
		public void setRemindrecontent(String remindrecontent) {
			this.remindrecontent = remindrecontent;
		}
		public String getCreateuser() {
			return createuser;
		}
		public void setCreateuser(String createuser) {
			this.createuser = createuser;
		}
		public Date getCreatetime() {
			return createtime;
		}
		public void setCreatetime(Date createtime) {
			this.createtime = createtime;
		}
}
