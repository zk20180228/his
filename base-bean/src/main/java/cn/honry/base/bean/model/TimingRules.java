package cn.honry.base.bean.model;

import java.math.BigDecimal;

import cn.honry.base.bean.business.Entity;

/**
 * BiBaseContractunit entity. @author MyEclipse Persistence Tools
 */

public class TimingRules extends Entity{
	/**栏目别名*/
	private String menuAlias;
	/**类型 3年2月1日**/
	private Integer type;
	/**规则**/
	private String rules;
	/**状态 1开启 2关闭**/
	private Integer state;
	/**备注*/
	private String remark;
	/**渲染字段使用*/
	private String aliasName;
	
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getRules() {
		return rules;
	}
	public void setRules(String rules) {
		this.rules = rules;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}