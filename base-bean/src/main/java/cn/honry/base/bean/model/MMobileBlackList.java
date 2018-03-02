package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 手机号黑名单
 * @author zxl 20170420
 */
public class MMobileBlackList extends Entity{
	private String mobileNum;//手机号
	private String type;//类型  1、业务审批，2、日程安排，3、文章管理
	private String[] ids;//删除用到id
	private String mobileRemark;//备注
	
	public String getMobileRemark() {
		return mobileRemark;
	}
	public void setMobileRemark(String mobileRemark) {
		this.mobileRemark = mobileRemark;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public String getMobileNum() {
		return mobileNum;
	}
	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
