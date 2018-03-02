package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/***
 * 物资供领科室关系表
 * @Description:
 * @author  wfj
 * @date 创建时间：2016年2月20日
 * @version 1.0
 * @parameter 
 * @since
 */
public class MatInoutDept extends Entity implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//备用字段
	private String class2Code;
	//  1-入库   2-出库
	private Integer stockClass;
	//科室编码
	private String deptCode;
	//供物资或领物资单位码
	private String objectDeptCode;
	//供物资或领物资单位名称
	private String objectDeptName;
	//操作员代码
	private String operCode;
	//操作时间
	private Date operDate;
	//备注
	private String mark;
	//排序
	private Integer sortid;
	
	
	public String getClass2Code() {
		return class2Code;
	}
	public void setClass2Code(String class2Code) {
		this.class2Code = class2Code;
	}
	public Integer getStockClass() {
		return stockClass;
	}
	public void setStockClass(Integer stockClass) {
		this.stockClass = stockClass;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getObjectDeptCode() {
		return objectDeptCode;
	}
	public void setObjectDeptCode(String objectDeptCode) {
		this.objectDeptCode = objectDeptCode;
	}
	public String getObjectDeptName() {
		return objectDeptName;
	}
	public void setObjectDeptName(String objectDeptName) {
		this.objectDeptName = objectDeptName;
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
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public Integer getSortid() {
		return sortid;
	}
	public void setSortid(Integer sortid) {
		this.sortid = sortid;
	}
	
}
