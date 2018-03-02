package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**
 * 住院：结算员日结明细表  实体
 * @author  tcj
 * @date 创建时间：2016年4月12日
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
public class InpatientScDreportdetail extends Entity implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	/** 统计序号  **/
	private Integer staticNo;
	/** 种类 **/
	private String kind;
	/** 开始时间  **/
	private Date beginDate;
	/** 结束日期  **/
	private Date endDate;
	/** 操作员代码  **/
	private String operCode;
	/** 统计大类  **/
	private String statCode;
	/** 费用金额  **/
	private double totCost;
	/** 自费医疗 **/
	private double ownCost;
	/** 自付医疗  **/
	private double payCost;
	/** 公费医疗  **/
	private double pubCost;
	/** 备注  **/
	private String mark;
//	/** 新添加字段 医院编码  **/
//	private Integer hospitalId;
//	/** 新添加字段 院区编码  **/
//	private String areaCode;
//	
//	public Integer getHospitalId() {
//		return hospitalId;
//	}
//	public void setHospitalId(Integer hospitalId) {
//		this.hospitalId = hospitalId;
//	}
//	public String getAreaCode() {
//		return areaCode;
//	}
//	public void setAreaCode(String areaCode) {
//		this.areaCode = areaCode;
//	}
	public Integer getStaticNo() {
		return staticNo;
	}
	public void setStaticNo(Integer staticNo) {
		this.staticNo = staticNo;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public String getStatCode() {
		return statCode;
	}
	public void setStatCode(String statCode) {
		this.statCode = statCode;
	}
	public double getTotCost() {
		return totCost;
	}
	public void setTotCost(double totCost) {
		this.totCost = totCost;
	}
	public double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(double ownCost) {
		this.ownCost = ownCost;
	}
	public double getPayCost() {
		return payCost;
	}
	public void setPayCost(double payCost) {
		this.payCost = payCost;
	}
	public double getPubCost() {
		return pubCost;
	}
	public void setPubCost(double pubCost) {
		this.pubCost = pubCost;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	
}
