package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**
 * 物资出库表
 * @author  lyy
 * @createDate： 2016年2月19日 下午5:59:33 
 * @modifier lyy
 * @modifyDate：2016年2月19日 下午5:59:33  
 * @modifyRmk：  
 * @version 1.0
 */
public class MatOutput extends Entity implements java.io.Serializable{
	private String outNo;       //出库流水号
	private String outListCode;   //出库单号
	private Integer outSerialNo;   //出库单内序号
	private String storageCode;   //仓库编码
	private String targetDept;  //领用科室编码
	private String stockCode;   //库存流水号
	private String regCode;  //生产厂家认证记录编号
	private String outClass3;   //出库类型  用户定义
	private String outClass3mean;  //出库分类  系统定义
	private Integer transType;  //交易类型(1-正交易,2-反交易)
	private Integer outState;   //状态(0-申请出库,1-审批出库,2-核准出库,3-备货出库,4-特殊出库核准)
	private String itemCode;   //物品编码
	private String itemName;   //物品名称
	private String kindCode;   //物品科目编码
	private String specs;   //规格
	private String batchNo;   //批号
	private Double outNum;   //出库数量
	private String minUnit;   //最小单位
	private String packUnit;   //大包装单位
	private Integer packQty;  //大包装包装数量
	private Double outPrice;   //出库价(入库时的价格)
	private Double outCost;  //出库金额
	private Double salePrice;  //零售单价
	private Double saleCost;  //零售金额
	private Double addRate;   //加价规则、用于入库自动加价
	private Double otherFee;  //附加费
	private Date validDate;  //有效期
	private Double privStoreNum;  //出库前库存量
	private String placeCode;  //库位编号
	private Date outDate;  //出库日期
	private String getType;   //领用类型(0-科室领用,1-患者领用)
	private String getPersonid;  //物资消耗人员(住院流水号或门诊卡号)
	private String recipeNo;   //处方号
	private Integer sequenceNo;  //处方内项目流水号
	private String applyOper;  //申请人
	private Date applyDate;  //申请时间
	private String examOper;  //审批人
	private Date examDate;  //审批人时间
	private String approveOper;  //核准人
	private Date approveDate;  //核准时间
	private String targetInNo;  //对应的入库记录流水号
	private Date produceDate;  //生产日期
	private String useName;   //用途
	private String barCode;  //条码
	private Integer financeFlag;   //是否财务收费项目(1-是,0-否)
	private String applyNo;   //申请流水号
	private Double returnNum;   //已退数量
	private String returnOutNo;  //退掉的出库流水号
	private Double returnApplyNum;  //申请退库数量
	private String memo;   //备注
	private String operCode;  //操作员
	private Date operDate;   //操作日期
	private Integer highvalueFlag;   //高值耗材标志
	private String highvalueBarcode;   //高值耗材条形码
	private String receivePerson; //科室领用人
	public String getOutNo() {
		return outNo;
	}
	public void setOutNo(String outNo) {
		this.outNo = outNo;
	}
	public String getOutListCode() {
		return outListCode;
	}
	public void setOutListCode(String outListCode) {
		this.outListCode = outListCode;
	}
	public Integer getOutSerialNo() {
		return outSerialNo;
	}
	public void setOutSerialNo(Integer outSerialNo) {
		this.outSerialNo = outSerialNo;
	}
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}
	public String getTargetDept() {
		return targetDept;
	}
	public void setTargetDept(String targetDept) {
		this.targetDept = targetDept;
	}
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public String getRegCode() {
		return regCode;
	}
	public void setRegCode(String regCode) {
		this.regCode = regCode;
	}
	public String getOutClass3() {
		return outClass3;
	}
	public void setOutClass3(String outClass3) {
		this.outClass3 = outClass3;
	}
	public String getOutClass3mean() {
		return outClass3mean;
	}
	public void setOutClass3mean(String outClass3mean) {
		this.outClass3mean = outClass3mean;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public Integer getOutState() {
		return outState;
	}
	public void setOutState(Integer outState) {
		this.outState = outState;
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
	public String getKindCode() {
		return kindCode;
	}
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Double getOutNum() {
		return outNum;
	}
	public void setOutNum(Double outNum) {
		this.outNum = outNum;
	}
	public String getMinUnit() {
		return minUnit;
	}
	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}
	public String getPackUnit() {
		return packUnit;
	}
	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}
	public Integer getPackQty() {
		return packQty;
	}
	public void setPackQty(Integer packQty) {
		this.packQty = packQty;
	}
	public Double getOutPrice() {
		return outPrice;
	}
	public void setOutPrice(Double outPrice) {
		this.outPrice = outPrice;
	}
	public Double getOutCost() {
		return outCost;
	}
	public void setOutCost(Double outCost) {
		this.outCost = outCost;
	}
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	public Double getSaleCost() {
		return saleCost;
	}
	public void setSaleCost(Double saleCost) {
		this.saleCost = saleCost;
	}
	public Double getAddRate() {
		return addRate;
	}
	public void setAddRate(Double addRate) {
		this.addRate = addRate;
	}
	public Double getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	public Double getPrivStoreNum() {
		return privStoreNum;
	}
	public void setPrivStoreNum(Double privStoreNum) {
		this.privStoreNum = privStoreNum;
	}
	public String getPlaceCode() {
		return placeCode;
	}
	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public String getGetType() {
		return getType;
	}
	public void setGetType(String getType) {
		this.getType = getType;
	}
	public String getGetPersonid() {
		return getPersonid;
	}
	public void setGetPersonid(String getPersonid) {
		this.getPersonid = getPersonid;
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
	public String getApplyOper() {
		return applyOper;
	}
	public void setApplyOper(String applyOper) {
		this.applyOper = applyOper;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public String getExamOper() {
		return examOper;
	}
	public void setExamOper(String examOper) {
		this.examOper = examOper;
	}
	public Date getExamDate() {
		return examDate;
	}
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
	public String getApproveOper() {
		return approveOper;
	}
	public void setApproveOper(String approveOper) {
		this.approveOper = approveOper;
	}
	public Date getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}
	public String getTargetInNo() {
		return targetInNo;
	}
	public void setTargetInNo(String targetInNo) {
		this.targetInNo = targetInNo;
	}
	public Date getProduceDate() {
		return produceDate;
	}
	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}
	public String getUseName() {
		return useName;
	}
	public void setUseName(String useName) {
		this.useName = useName;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public Integer getFinanceFlag() {
		return financeFlag;
	}
	public void setFinanceFlag(Integer financeFlag) {
		this.financeFlag = financeFlag;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public Double getReturnNum() {
		return returnNum;
	}
	public void setReturnNum(Double returnNum) {
		this.returnNum = returnNum;
	}
	public String getReturnOutNo() {
		return returnOutNo;
	}
	public void setReturnOutNo(String returnOutNo) {
		this.returnOutNo = returnOutNo;
	}
	public Double getReturnApplyNum() {
		return returnApplyNum;
	}
	public void setReturnApplyNum(Double returnApplyNum) {
		this.returnApplyNum = returnApplyNum;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	public Integer getHighvalueFlag() {
		return highvalueFlag;
	}
	public void setHighvalueFlag(Integer highvalueFlag) {
		this.highvalueFlag = highvalueFlag;
	}
	public String getHighvalueBarcode() {
		return highvalueBarcode;
	}
	public void setHighvalueBarcode(String highvalueBarcode) {
		this.highvalueBarcode = highvalueBarcode;
	}
	public String getReceivePerson() {
		return receivePerson;
	}
	public void setReceivePerson(String receivePerson) {
		this.receivePerson = receivePerson;
	}
	
}
