package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 预约挂号App用户通讯录
 * Date:2017/03/31 
 * Name:zpty
 */
public class MobileContactsList extends Entity{
	/**登录用户手机号(账户名)**/
	private String mobile;
	/**联系人姓名**/
	private String name;
	/**联系人电话(手机号)**/
	private String phone;
	/**联系人证件号(身份证号)**/
	private String certificate;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	
}
