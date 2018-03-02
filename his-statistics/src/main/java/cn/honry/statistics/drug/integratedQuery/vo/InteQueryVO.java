package cn.honry.statistics.drug.integratedQuery.vo;

import java.util.Date;

/**  
 *  封装查询条件VO
 * @Author:luyanshou
 * @version 1.0
 */
public class InteQueryVO {

	private Date beginTime;//开始时间
	private Date endTime;//结束时间
	private String drugCode;//药品编码
	private String drugDeptCode;//库房编码
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public String getDrugDeptCode() {
		return drugDeptCode;
	}
	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}
	
}
