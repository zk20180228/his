package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;
/**
 * 
 * @author conglin
 *  虚拟科室关系维护表
 *
 */
public class FictitiousContact extends Entity {
	/**部门表中的部门代码**/
	private String deptCode;
	/**部门名称**/
	private String deptName;
	/**C-门诊, I-住院, F-财务，L-后勤，PI-药库，T-医技(终端)，0-其它，D-机关(部门)，P-药房，N-护士站 ，S-科研,O-其他,OP-手术,U-自定义**/
	private String type;
	/**虚拟部门表部门代码**/
	private String fictCode;
	/**虚拟部门名称**/
	private String fictName;
	/**排序**/
	private Integer fictOrder;
	/**备注**/
	private String mark;
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFictCode() {
		return fictCode;
	}
	public void setFictCode(String fictCode) {
		this.fictCode = fictCode;
	}
	public String getFictName() {
		return fictName;
	}
	public void setFictName(String fictName) {
		this.fictName = fictName;
	}
	public Integer getFictOrder() {
		return fictOrder;
	}
	public void setFictOrder(Integer fictOrder) {
		this.fictOrder = fictOrder;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	@Override
	public String toString() {
		return "FictitiousContact [deptCode=" + deptCode + ", deptName="
				+ deptName + ", type=" + type + ", fictCode=" + fictCode
				+ ", fictName=" + fictName + ", fictOrder=" + fictOrder
				+ ", mark=" + mark + "]";
	}
	
}
