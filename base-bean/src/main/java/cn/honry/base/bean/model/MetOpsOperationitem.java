package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class MetOpsOperationitem extends Entity implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**手术序号**/
	private String operationno;
	/**门诊号/住院流水号**/
	private String clinicCode;
	/**住院科室**/
	private String deptCode;
	/**项目编码**/
	private String itemCode;
	/**项目名称**/
	private String itemName;
	/**单价**/
	private Double unitPrice;
	/**收费比例**/
	private Double feeRate;
	/**数量**/
	private Integer qty;
	/**单位**/
	private String stockUnit;
	/**手术规模**/
	private String degree;
	/**切口类型**/
	private String icniType;
	/**幕上幕下**/
	private String screenup;
	/**1有菌/0无菌**/
	private String yngerm;
	/**手术部位**/
	private String opePos;
	/**1加急/0否**/
	private String ynurgent;
	/**1病危/0否**/
	private String ynchange;
	/**1重症/0否**/
	private String ynheavy;
	/**1特殊手术/0否**/
	private String ynspecial;
	/**1主手术/0否**/
	private String mainFlag;
	/**备注**/
	private String remark;
	/**1有效/0无效**/
	private String ynvalid;
	
	
	public String getOperationno() {
		return operationno;
	}
	public void setOperationno(String operationno) {
		this.operationno = operationno;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(Double feeRate) {
		this.feeRate = feeRate;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public String getStockUnit() {
		return stockUnit;
	}
	public void setStockUnit(String stockUnit) {
		this.stockUnit = stockUnit;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getIcniType() {
		return icniType;
	}
	public void setIcniType(String icniType) {
		this.icniType = icniType;
	}
	public String getScreenup() {
		return screenup;
	}
	public void setScreenup(String screenup) {
		this.screenup = screenup;
	}
	public String getYngerm() {
		return yngerm;
	}
	public void setYngerm(String yngerm) {
		this.yngerm = yngerm;
	}
	public String getOpePos() {
		return opePos;
	}
	public void setOpePos(String opePos) {
		this.opePos = opePos;
	}
	public String getYnurgent() {
		return ynurgent;
	}
	public void setYnurgent(String ynurgent) {
		this.ynurgent = ynurgent;
	}
	public String getYnchange() {
		return ynchange;
	}
	public void setYnchange(String ynchange) {
		this.ynchange = ynchange;
	}
	public String getYnheavy() {
		return ynheavy;
	}
	public void setYnheavy(String ynheavy) {
		this.ynheavy = ynheavy;
	}
	public String getYnspecial() {
		return ynspecial;
	}
	public void setYnspecial(String ynspecial) {
		this.ynspecial = ynspecial;
	}
	public String getMainFlag() {
		return mainFlag;
	}
	public void setMainFlag(String mainFlag) {
		this.mainFlag = mainFlag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getYnvalid() {
		return ynvalid;
	}
	public void setYnvalid(String ynvalid) {
		this.ynvalid = ynvalid;
	}

	
	
}
