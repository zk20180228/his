package cn.honry.base.bean.model;


import cn.honry.base.bean.business.Entity;
/**
 * 手术项目
 * @author zhangjin
 * @CreateDate：2016-04-11 
 *
 */
public class OperationItem extends Entity{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 手术序号
	 */
	private String operationId;
	/**
	 * 住院流水号
	 */
	private String inpatientNo;
	/**
	 * 住院科室
	 */
	private String deptCode; 
	/**
	 * 发生序号
	 */
	private Integer happenNo;
	/**
	 * 就诊卡号/病历号
	 */
	private String cardNo;
	/**
	 * 手术名称
	 */
	private String itemName;
	/**
	 * 手术Id
	 */
	private String itemId;
	/**
	 * 是否无效 1有效 0无效
	 */
	private String itemFlag;
	/**
	 *单价 
	 */
	private Double unitPrice; 
	/**
	 * 收费比例
	 */
	private Integer feeRate; 
	/**
	 * 数量
	 */
	private Integer qty; 
	/**
	 * 单位
	 */
	private String stockUnit; 
	/**
	 * 手术规模
	 */
	private String degree; 
	/**
	 * 切口类型
	 */
	private String icniType; 
	/**
	 * 幕上幕下
	 */
	private String screenup; 
	/**
	 * 1有菌/0无菌
	 */
	private String yngerm; 
	/**
	 * 手术部位
	 */
	private String opePos; 
	/**
	 * 1加急/0否
	 */
	private String ynurgent; 
	/**
	 * 1病危/0否
	 */
	private String ynchange; 
	/**
	 * 1重症/0否
	 */
	private String yuheavy; 
	/**
	 * 1特殊手术/0否
	 */
	private String ynspecial; 
	/**
	 * 1主手术/0否
	 */
	private String mainFlag; 
	/**
	 * 备注
	 */
	private String remark; 
	
	
	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(Integer feeRate) {
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

	public String getYuheavy() {
		return yuheavy;
	}

	public void setYuheavy(String yuheavy) {
		this.yuheavy = yuheavy;
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

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getOperationId() {
		return this.operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getInpatientNo() {
		return this.inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public Integer getHappenNo() {
		return happenNo;
	}

	public void setHappenNo(Integer happenNo) {
		this.happenNo = happenNo;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemFlag() {
		return this.itemFlag;
	}

	public void setItemFlag(String itemFlag) {
		this.itemFlag = itemFlag;
	}


}
