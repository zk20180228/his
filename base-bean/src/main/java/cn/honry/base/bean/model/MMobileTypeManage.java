package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 手机号黑名单
 * @author zxl 20170420
 */
public class MMobileTypeManage extends Entity{
	private String mobileCategory;//手机类别
	private String type;//类型  1、白名单，2、黑名单
	private String mobileRemark;//备注
	
	public String getMobileRemark() {
		return mobileRemark;
	}
	public void setMobileRemark(String mobileRemark) {
		this.mobileRemark = mobileRemark;
	}
	public String getMobileCategory() {
		return mobileCategory;
	}
	public void setMobileCategory(String mobileCategory) {
		this.mobileCategory = mobileCategory;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
