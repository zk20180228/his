package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 * 
 * 个人通讯录实体
 * @Author:zxl
 * @CreateDate: 2017年7月15日 下午4:24:32 
 * @Modifier: zxl
 * @ModifyDate: 2017年7月15日 下午4:24:32 
 * @ModifyRmk:  
 * @version: V1.0  PersonalAddressList
 */
public class PersonalAddressList  extends Entity{
	/**
	 * 姓名
	 */
	private String perName;
	/**
	 * 性别
	 */
	private String perSex;
	/**
	 * 生日
	 */
	private Date perBirthday;
	/**
	 * 拼音
	 */
	private String perPinyin;
	/**
	 * 五笔
	 */
	private String perWb;
	/**
	 * 自定义码
	 */
	private String perInputCode;
	
	/**
	 * 移动电话
	 */
	private String  mobilePhone;
	
	/**
	 * 办公电话
	 */
	private String  workPhone;
	
	/**
	 * 家庭住址
	 */
	private String  perAddress;
	
	/**
	 * 电子邮箱
	 */
	private String  perEmail;
	
	/**
	 * 备注
	 */
	private String  perRemark;
	
	/**
	 * 是否分组 
	 */
	private Integer  ifGroup;
	
	/**
	 * 所属分组name
	 */
	private String  belongGroupName;
	
	/**
	 * 所属分组Name 
	 */
	private String  groupName;
	
	/**
	 * 父级code
	 */
	private String parentCode;
	
	/**
	 * 父级路径
	 */
	private String parentUppath;
	
	/**
	 * 所属用户
	 */
	private String belongUser;
	
	/**
	 * 排序
	 */
	private Integer perOrder;
	

	public String getPerName() {
		return perName;
	}

	public void setPerName(String perName) {
		this.perName = perName;
	}

	public String getPerSex() {
		return perSex;
	}

	public void setPerSex(String perSex) {
		this.perSex = perSex;
	}

	public Date getPerBirthday() {
		return perBirthday;
	}

	public void setPerBirthday(Date perBirthday) {
		this.perBirthday = perBirthday;
	}

	public String getPerPinyin() {
		return perPinyin;
	}

	public void setPerPinyin(String perPinyin) {
		this.perPinyin = perPinyin;
	}

	public String getPerWb() {
		return perWb;
	}

	public void setPerWb(String perWb) {
		this.perWb = perWb;
	}

	public String getPerInputCode() {
		return perInputCode;
	}

	public void setPerInputCode(String perInputCode) {
		this.perInputCode = perInputCode;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getPerAddress() {
		return perAddress;
	}

	public void setPerAddress(String perAddress) {
		this.perAddress = perAddress;
	}

	public String getPerEmail() {
		return perEmail;
	}

	public void setPerEmail(String perEmail) {
		this.perEmail = perEmail;
	}

	public String getPerRemark() {
		return perRemark;
	}

	public void setPerRemark(String perRemark) {
		this.perRemark = perRemark;
	}

	public Integer getIfGroup() {
		return ifGroup;
	}

	public void setIfGroup(Integer ifGroup) {
		this.ifGroup = ifGroup;
	}

	public String getBelongGroupName() {
		return belongGroupName;
	}

	public void setBelongGroupName(String belongGroupName) {
		this.belongGroupName = belongGroupName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getParentUppath() {
		return parentUppath;
	}

	public void setParentUppath(String parentUppath) {
		this.parentUppath = parentUppath;
	}

	public String getBelongUser() {
		return belongUser;
	}

	public void setBelongUser(String belongUser) {
		this.belongUser = belongUser;
	}

	public Integer getPerOrder() {
		return perOrder;
	}

	public void setPerOrder(Integer perOrder) {
		this.perOrder = perOrder;
	}
	
	
}
