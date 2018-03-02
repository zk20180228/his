package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 移动端登录令牌表
 * @author zpty
 * Date:2016/01/11 15:30
 */

public class SysMobileLoginToken extends Entity {	
	/**用户编号**/
	private User userId;
	/**用户账户**/
	private String userAccount;
	/**手机编码**/
	private String mobileCode;
	/**令牌**/
	private String loginToken;
	/**员工主键**/
	private SysEmployee employeeId;
	/**最近一次登录时间**/
	private Date latestLoginDate;
	/**是否有效:0-有效，1-无效**/
	private Integer valid;
	/**手机型号:1-adroid,2-ios,3-wphone**/
	private Integer flag;
	/**备注**/
	private String remark;
	/**有效天数**/
	private Long days;
	
	public User getUserId() {
		return userId;
	}
	public void setUserId(User userId) {
		this.userId = userId;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getMobileCode() {
		return mobileCode;
	}
	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}
	public String getLoginToken() {
		return loginToken;
	}
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}
	public SysEmployee getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(SysEmployee employeeId) {
		this.employeeId = employeeId;
	}
	public Date getLatestLoginDate() {
		return latestLoginDate;
	}
	public void setLatestLoginDate(Date latestLoginDate) {
		this.latestLoginDate = latestLoginDate;
	}
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getDays() {
		return days;
	}
	public void setDays(Long days) {
		this.days = days;
	}

}