package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 预约挂号App注册用户表
 * Date:2017/03/30 15:03
 * Name:zpty
 */
public class MobileRegisterLogin extends Entity{
	/**账号**/
	private String mobile;
	/**密码**/
	private String password;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
