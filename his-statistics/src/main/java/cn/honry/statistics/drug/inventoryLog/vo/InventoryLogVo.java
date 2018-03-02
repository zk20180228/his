package cn.honry.statistics.drug.inventoryLog.vo;

import java.util.Date;


public class InventoryLogVo{

	/**科室代码0-全部部门*/
	private String drugDeptCode;
	/**药品编码*/
	private String drugCode;
	/**批号(如果等于'全部',则表示所有批号的药品)*/
	private String batchNo;
	/**商品名称*/
	private String tradeName;
	/**规格*/
	private String specs;
	/**零售价*/
	private Double retailPrice;
	/**包装单位*/
	private String packUnit;
	/**包装数*/
	private Double packQty;
	/**实际盘存数量*/
	private Double adjustNum;
	/**盘存单位*/
	private String adjustUnit;
	/**货位号*/
	private String placeNo;
	/**备注*/
	private String remark;

	/**盘点单号*/
	private String checkCode;
	/**科室名称*/
	private String deptName;
	/**创建人*/
	private String createuser;
	/**创建时间*/
	private Date createtime;
	/**人员姓名*/
	private String userName;

	public String getDrugDeptCode() {
		return this.drugDeptCode;
	}

	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}

	public String getDrugCode() {
		return this.drugCode;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getTradeName() {
		return this.tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getSpecs() {
		return this.specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public Double getRetailPrice() {
		return this.retailPrice;
	}

	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public String getPackUnit() {
		return this.packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public Double getPackQty() {
		return this.packQty;
	}

	public void setPackQty(Double packQty) {
		this.packQty = packQty;
	}

	public Double getAdjustNum() {
		return this.adjustNum;
	}

	public void setAdjustNum(Double adjustNum) {
		this.adjustNum = adjustNum;
	}

	public String getAdjustUnit() {
		return this.adjustUnit;
	}

	public void setAdjustUnit(String adjustUnit) {
		this.adjustUnit = adjustUnit;
	}

	public String getPlaceNo() {
		return this.placeNo;
	}

	public void setPlaceNo(String placeNo) {
		this.placeNo = placeNo;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	

}
