package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @className：InpatientDerate.java 
 * @Description：费用减免表
 * @Author：hedong
 * @CreateDate：2015-08-17  
 * @version 1.0
 * 结算召回模块
 */
public class InpatientDerate extends Entity implements java.io.Serializable {
	  private static final long serialVersionUID = 1L;
	  private String clinicNo;// 医疗流水号
	  private Integer transType;//交易类型 1正交易 2反交易
	  private Integer happenNo;//发生序号  
	  private String derateKind;// 减免种类 0 总额 1 最小费用 2 项目减免 
	  private String recipeNo;//处方号
	  private Integer sequenceNo;//处方内项目流水号 
	  private String derateType;// 减免类型
	  private String feeCode;//  最小费用
	  private Double derateCost;// 减免金额  
	  private String derateCause;// 减免原因
	  private String confirmOpercode;// 批准人员工代码 
	  private String confirmName;//批准人姓名
	  private String deptCode;//科室代码 
	  private Integer balanceNo;//结算序号
	  private String balanceState;//结算状态 0:未结算；1:已结算 
	  private String invoiceNo;//发票号
	  private String cancelCode;//作废人代码  
	  private Date cancelDate;//作废时间
	  private String itemCode;//项目编码 
	  private String itemName;//项目名称
	  private String feeName;//最小费用名称 
	  private String validFlag;//是否有效 
	  
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public String getClinicNo() {
		return clinicNo;
	}
	public void setClinicNo(String clinicNo) {
		this.clinicNo = clinicNo;
	}
	public Integer getHappenNo() {
		return happenNo;
	}
	public void setHappenNo(Integer happenNo) {
		this.happenNo = happenNo;
	}
	public String getDerateKind() {
		return derateKind;
	}
	public void setDerateKind(String derateKind) {
		this.derateKind = derateKind;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getDerateType() {
		return derateType;
	}
	public void setDerateType(String derateType) {
		this.derateType = derateType;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public Double getDerateCost() {
		return derateCost;
	}
	public void setDerateCost(Double derateCost) {
		this.derateCost = derateCost;
	}
	public String getDerateCause() {
		return derateCause;
	}
	public void setDerateCause(String derateCause) {
		this.derateCause = derateCause;
	}
	public String getConfirmOpercode() {
		return confirmOpercode;
	}
	public void setConfirmOpercode(String confirmOpercode) {
		this.confirmOpercode = confirmOpercode;
	}
	public String getConfirmName() {
		return confirmName;
	}
	public void setConfirmName(String confirmName) {
		this.confirmName = confirmName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Integer getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(Integer balanceNo) {
		this.balanceNo = balanceNo;
	}
	public String getBalanceState() {
		return balanceState;
	}
	public void setBalanceState(String balanceState) {
		this.balanceState = balanceState;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getCancelCode() {
		return cancelCode;
	}
	public void setCancelCode(String cancelCode) {
		this.cancelCode = cancelCode;
	}
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
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
	public String getFeeName() {
		return feeName;
	}
	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	  
	  
}
