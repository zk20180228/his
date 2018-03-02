package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 设备管理
 * @author zxl 20170420
 */
public class MMachineManage extends Entity{
	private String machine_code;//设备码
	private String machine_mobile;//手机号
	private Integer is_lost;//是否挂失
	private String user_account;//用户账号
	private Integer is_white;//是否白名单
	private Integer is_black;//是否黑名单
	
	private String[] ids;//删除用到id
	
	private String mobiles;//单个SIM，用于校验
	
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public String getMachine_code() {
		return machine_code;
	}
	public void setMachine_code(String machine_code) {
		this.machine_code = machine_code;
	}
	public String getMachine_mobile() {
		return machine_mobile;
	}
	public void setMachine_mobile(String machine_mobile) {
		this.machine_mobile = machine_mobile;
	}
	public Integer getIs_lost() {
		return is_lost;
	}
	public void setIs_lost(Integer is_lost) {
		this.is_lost = is_lost;
	}
	public String getUser_account() {
		return user_account;
	}
	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}
	public Integer getIs_white() {
		return is_white;
	}
	public void setIs_white(Integer is_white) {
		this.is_white = is_white;
	}
	public Integer getIs_black() {
		return is_black;
	}
	public void setIs_black(Integer is_black) {
		this.is_black = is_black;
	}
	public String getMobiles() {
		return mobiles;
	}
	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;
	}
	
	
}
