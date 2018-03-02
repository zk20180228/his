package cn.honry.base.bean.model;
/**
 * 操作日志
 * */
public class MSysOperateLog {
	/**主键id*/ 
	private String log_id;
	/**操作行为*/ 
	private String log_action;
	/**操作账号*/ 
	private String log_account;
	/**操作栏目*/ 
	private String log_menu_id;
	/**操做SQL*/ 
	private String log_sql;
	/**操作表*/ 
	private String log_table;
	/**目标编号*/ 
	private String log_target_id;
	/**操作时间*/ 
	private String log_time;
	/**操作备注*/ 
	private String log_description;
	public String getLog_id() {
		return log_id;
	}
	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}
	public String getLog_action() {
		return log_action;
	}
	public void setLog_action(String log_action) {
		this.log_action = log_action;
	}
	public String getLog_account() {
		return log_account;
	}
	public void setLog_account(String log_account) {
		this.log_account = log_account;
	}
	public String getLog_menu_id() {
		return log_menu_id;
	}
	public void setLog_menu_id(String log_menu_id) {
		this.log_menu_id = log_menu_id;
	}
	public String getLog_sql() {
		return log_sql;
	}
	public void setLog_sql(String log_sql) {
		this.log_sql = log_sql;
	}
	public String getLog_table() {
		return log_table;
	}
	public void setLog_table(String log_table) {
		this.log_table = log_table;
	}
	public String getLog_target_id() {
		return log_target_id;
	}
	public void setLog_target_id(String log_target_id) {
		this.log_target_id = log_target_id;
	}
	public String getLog_time() {
		return log_time;
	}
	public void setLog_time(String log_time) {
		this.log_time = log_time;
	}
	public String getLog_description() {
		return log_description;
	}
	public void setLog_description(String log_description) {
		this.log_description = log_description;
	}
	public MSysOperateLog(String log_action,  String log_menu_id, String log_account,
			String log_sql, String log_table, String log_target_id,
			String log_description) {
		super();
		this.log_action = log_action;
		this.log_account = log_account;
		this.log_menu_id = log_menu_id;
		this.log_sql = log_sql;
		this.log_table = log_table;
		this.log_target_id = log_target_id;
		this.log_description = log_description;
	}
	public MSysOperateLog() {
		super();
	}
	
	
}
