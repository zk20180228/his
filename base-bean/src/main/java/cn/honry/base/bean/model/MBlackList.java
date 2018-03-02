package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 黑名单
 * @author zxl 20170420
 */
public class MBlackList extends Entity{
	
	private String user_account;//用户账户
	private String list_type;//类别  1.设备   2.其他
	private String[] ids;
	private String machine_code;//设备码
	
	//列表显示用
	private String userName;//用户名
	private String deptName;//科室名称
	private String userSex;//性别
	
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public String getUser_account() {
		return user_account;
	}
	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}
	public String getList_type() {
		return list_type;
	}
	public void setList_type(String list_type) {
		this.list_type = list_type;
	}
	public String getMachine_code() {
		return machine_code;
	}
	public void setMachine_code(String machine_code) {
		this.machine_code = machine_code;
	}
	
}
