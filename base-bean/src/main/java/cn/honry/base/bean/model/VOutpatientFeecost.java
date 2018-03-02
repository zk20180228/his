package cn.honry.base.bean.model;

import java.util.Date;

public class VOutpatientFeecost {
	private String id;
	private String  reglevlCode;//挂号级别
	private String deptCode;//科室
	private  Double sumCost;//挂号费
	private Double totCost;//收费明细总费用
	private Date feeDate; //收费日期
	private Integer drugFlag;//1药品0非药品
	private String feeStatCode;//统计大类
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReglevlCode() {
		return reglevlCode;
	}
	public void setReglevlCode(String reglevlCode) {
		this.reglevlCode = reglevlCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Double getSumCost() {
		return sumCost;
	}
	public void setSumCost(Double sumCost) {
		this.sumCost = sumCost;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public Date getFeeDate() {
		return feeDate;
	}
	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}
	public Integer getDrugFlag() {
		return drugFlag;
	}
	public void setDrugFlag(Integer drugFlag) {
		this.drugFlag = drugFlag;
	}
	public String getFeeStatCode() {
		return feeStatCode;
	}
	public void setFeeStatCode(String feeStatCode) {
		this.feeStatCode = feeStatCode;
	}
	
	

}
