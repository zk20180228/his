package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * OA系统首页组件实体类
 * @author zpty
 * Date:2017/7/15 15:00
 */
public class OaUserPortal extends Entity {
	private static final long serialVersionUID = 1L;
	/**用户账户**/
	private String account;
	/**组件编号**/
	private String widget;
	/**组件参数**/
	private String params;
	/**组件所在列**/
	private String colum;
	/**组件所在行**/
	private String row;
	/**组件顺序**/
	private Integer order;
	/**组件昵称**/
	private String name;
	/**组件状态(个人),默认0:正常,1为停用**/
	private Integer local=0;
	/**全局状态(全局),默认0:正常,1为停用**/
	private Integer global=0;
	/**所属医院**/
	private Integer hospitalCode;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getWidget() {
		return widget;
	}
	public void setWidget(String widget) {
		this.widget = widget;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getColum() {
		return colum;
	}
	public void setColum(String colum) {
		this.colum = colum;
	}
	public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getLocal() {
		return local;
	}
	public void setLocal(Integer local) {
		this.local = local;
	}
	public Integer getGlobal() {
		return global;
	}
	public void setGlobal(Integer global) {
		this.global = global;
	}
	public Integer getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(Integer hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	
}
