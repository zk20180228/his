package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/***
 * 门诊药房：特殊项目与配药台关系
 * @Description:
 * @author  wfj
 * @date 创建时间：2016年3月23日
 * @version 1.0
 * @param:
 * @parameter 
 * @since 
 */
public class StoTerminalSpe extends Entity implements java.io.Serializable{
	/** 配药台编码（对应的特殊配药台编码） **/
	private String code;
	/** 类别:1药品，2专科，3结算类别，4特定收费窗口，5挂号级别**/
	private Integer itemType;
	/** 特殊配药项目编码(药品：药品编码，专科：科室编码，结算类别：结算类别编码，特定收费窗口CODE) **/
	private String itemCode;
	/** 特殊配药项目名称(药品：药品名称，专科：科室名称，结算类别：结算类别名称，特定收费窗口名称) **/
	private String itemName;
	/** 备注 **/
	private String mark;
	/** 
	* @Fields operCode : 操作人
	*/ 
	private String operCode;
	/** 
	* @Fields operDate : 操作时间 
	*/ 
	private Date operDate;
	/**配药台所属科室id**/
	private String deptid;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getItemType() {
		return itemType;
	}
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	
}