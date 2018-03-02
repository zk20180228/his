package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 权限-用户表
 * Date:2013/9/25 15:03
 * Name:sun
 */
public class User extends Entity{
	/**所属医院编号**/
	private Hospital hospitalId;
	/**账号**/
	private String account;
	/**密码**/
	private String password;
	/**加密码**/
	private String encrypt;
	/**系统编码**/
	private String code;
	/**姓名**/
	private String name;
	/**昵称**/
	private String nickName;
	/**类型**/
	private Integer type;
	/**性别**/
	private Integer sex;
	/**生日**/
	private Date birthday;
	/**电话**/
	private String phone;
	/**电子邮箱**/
	private String email;
	/**备注**/
	private String remark;
	/**排序**/
	private Integer order;
	/**失败次数**/
	private Integer failedTimes;
	/**最后一次登录时间**/
	private Date lastLoginTime;
	/**能否授权**/
	private Integer canAuthorize=0;
	/**  
	 * 
	 * @Fields gesturePsw : 手势密码(md5加密后)  20170517
	 *
	 */
	private String gesturePsw;
	/**建立人员**/
	/**医院名称**/
	private String hName;
	
	/**移动端设备码**/
	private String deviceCode;
	/**移动端使用状态**/
	private Integer userAppUsageStatus;
	/**app使用状态(0:使用,1:停用)**/
	private Integer useStatus;
	
	public Integer getUseStatus() {
		return useStatus;
	}
	public void setUseStatus(Integer useStatus) {
		this.useStatus = useStatus;
	}
	public String getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
	public Integer getUserAppUsageStatus() {
		return userAppUsageStatus;
	}
	public void setUserAppUsageStatus(Integer userAppUsageStatus) {
		this.userAppUsageStatus = userAppUsageStatus;
	}
	public Hospital getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Hospital hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEncrypt() {
		return encrypt;
	}
	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public Integer getFailedTimes() {
		return failedTimes;
	}
	public void setFailedTimes(Integer failedTimes) {
		this.failedTimes = failedTimes;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public Integer getCanAuthorize() {
		return canAuthorize;
	}
	public void setCanAuthorize(Integer canAuthorize) {
		this.canAuthorize = canAuthorize;
	}
	public String gethName() {
		return hName;
	}
	public void sethName(String hName) {
		this.hName = hName;
	}
	public String getGesturePsw() {
		return gesturePsw;
	}
	public void setGesturePsw(String gesturePsw) {
		this.gesturePsw = gesturePsw;
	}
}
