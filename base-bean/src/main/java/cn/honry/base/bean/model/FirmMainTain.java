package cn.honry.base.bean.model;

import java.util.Date;


/**
 * BiCourseRecoure entity. @author MyEclipse Persistence Tools
 */

public class FirmMainTain implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;//主键
	private String firmCode;//厂商编码
	private String firmName;//厂商名称
	private String passWord;//厂商接口密码
	private Date createTime;//创建时间
	private Date updateTime;//修改时间
	private String firmView;//厂商视图
	private String firmUser;//厂商账户
	private String firmPassword;//厂商密码
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirmCode() {
		return firmCode;
	}
	public void setFirmCode(String firmCode) {
		this.firmCode = firmCode;
	}
	public String getFirmName() {
		return firmName;
	}
	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getFirmView() {
		return firmView;
	}
	public void setFirmView(String firmView) {
		this.firmView = firmView;
	}
	public String getFirmUser() {
		return firmUser;
	}
	public void setFirmUser(String firmUser) {
		this.firmUser = firmUser;
	}
	public String getFirmPassword() {
		return firmPassword;
	}
	public void setFirmPassword(String firmPassword) {
		this.firmPassword = firmPassword;
	}
}