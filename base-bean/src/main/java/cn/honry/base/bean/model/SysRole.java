package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 角色表
 * SysRole entity. @author aizhonghua
 */
 
public class SysRole extends Entity {

	/**角色名称**/
	private String name;
	/**角色描述**/
	private String description;
	/**角色排序**/
	private Integer order;
	/**层级路径**/
	private String path;
	/**父级角色编号**/
	private String parentRoleId;
	/**所有父级**/
	private String uppath;
	/**别名**/
	private String alias;
	/**图标**/
	private String icon;
	/**拼音码**/
	private String pinyin;
	/**五笔码**/
	private String wb;
	/**自定义码**/
	private String inputcode;
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getWb() {
		return wb;
	}
	public void setWb(String wb) {
		this.wb = wb;
	}
	public String getInputcode() {
		return inputcode;
	}
	public void setInputcode(String inputcode) {
		this.inputcode = inputcode;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getParentRoleId() {
		return parentRoleId;
	}
	public void setParentRoleId(String parentRoleId) {
		this.parentRoleId = parentRoleId;
	}
	public String getUppath() {
		return uppath;
	}
	public void setUppath(String uppath) {
		this.uppath = uppath;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	
}