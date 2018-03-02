package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;


/**
 * 栏目功能表
 * SysMenufunction entity. @author aizhonghua
 */

public class SysMenufunction extends Entity {
	
	
	/**分类**/
	private Integer mfClass;
	/**名称**/
	private String mfName;
	/**别名**/
	private String mfAlias;
	/**图标**/
	private String mfIcon;
	/**访问页面请求**/
	private String mfAction;
	/**页面**/
	private String mfFile;
	/**说明**/
	private String mfDescription;
	/**排序**/
	private Integer mfOrder;
	/** 
	* @Fields mfBelong : 栏目功能归属 1平台，2移动，3移动与平台
	*/ 
	private Integer mfBelong;
	/** 
	* @Fields mfInterface : 移动接口路径
	*/ 
	private String mfInterface;
	
	public Integer getMfClass() {
		return mfClass;
	}
	public void setMfClass(Integer mfClass) {
		this.mfClass = mfClass;
	}
	public String getMfName() {
		return mfName;
	}
	public void setMfName(String mfName) {
		this.mfName = mfName;
	}
	public String getMfAlias() {
		return mfAlias;
	}
	public void setMfAlias(String mfAlias) {
		this.mfAlias = mfAlias;
	}
	public String getMfIcon() {
		return mfIcon;
	}
	public void setMfIcon(String mfIcon) {
		this.mfIcon = mfIcon;
	}
	public String getMfFile() {
		return mfFile;
	}
	public void setMfFile(String mfFile) {
		this.mfFile = mfFile;
	}
	public String getMfDescription() {
		return mfDescription;
	}
	public void setMfDescription(String mfDescription) {
		this.mfDescription = mfDescription;
	}
	public Integer getMfOrder() {
		return mfOrder;
	}
	public void setMfOrder(Integer mfOrder) {
		this.mfOrder = mfOrder;
	}
	public String getMfAction() {
		return mfAction;
	}
	public void setMfAction(String mfAction) {
		this.mfAction = mfAction;
	}
	public Integer getMfBelong() {
		return mfBelong;
	}
	public void setMfBelong(Integer mfBelong) {
		this.mfBelong = mfBelong;
	}
	public String getMfInterface() {
		return mfInterface;
	}
	public void setMfInterface(String mfInterface) {
		this.mfInterface = mfInterface;
	}
	
	
}