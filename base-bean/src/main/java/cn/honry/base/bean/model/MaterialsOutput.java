package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**
 * 物资出库表
 * @author  lyy
 * @createDate： 2016年1月26日 下午8:22:45 
 * @modifier lyy
 * @modifyDate：2016年1月26日 下午8:22:45  
 * @modifyRmk：  
 * @version 1.0
 */
public class MaterialsOutput extends Entity implements java.io.Serializable{
	private String outNo;   //出库单流水号
	private String outListCode;   //自定义出库单号 默认年月日+流水号
	private Integer outSerialNo;   //出库单内序号
	private String storageCode;  //仓库部门编码
	private String outType;  //出库类型-用户定义
	private String outClass;  //出库分类 系统定义
	private String state;  //0(申请出库)暂出库  1(审批出库)正式出库   2  核准
	private String stockNo;   //库存序号
	private String itemCode;  //物品编码
	private String itemName;  //物品名称
	private String kindCode;   //物品科目编码
	private String specs;   //规格
	private String batchNo;  //批号
	private Double outNum;  //出库数量
	private String statUnit;  //计量单位
	private Double price;    //出库单价
	private Double outCost;   //出库金额
	private Double salePrice;  //零售单价
	private Double saleCost;   //零售金额
	private Double addRate;    //加价比例
	private Double otherFee;   //附加费
	private Date validDate;   //有效期
	private Double privStoreNum;   //出库前库存量
	private String placeCode;   //库位编号
	private String isPrivate;   //是否个人领用 0 否 1 是
	private String deptCode;   //领用科室编码
	private String isNurse;   //是否病区 0 科室 1 病区
	private String drawOper;   //领取人(病区二级库存消耗时，保存患者住院号)
	private String getPerson;  //物资消耗人员(住院流水号或门诊卡号)
	private String applyOper;   //申请出库人
	private Date applyDate;    //申请时间
	private String examOper;   //审批人
	private Date examDate;   //审批人时间
	private Date outDate;   //出库日期(物品出库日期，不一定是正式出库日期)
	private String approveOper;  //核准人
	private Date approveDate;   //核准时间
	private String billState;  //0  有发票     1  无发票    2  发票补录
	private Integer debit;  //借方科目
	private String inNo;   //入库记录流水号
	private String outSequence;  //卫生材料流水号
	private Date produceDate;  //生产日期
	private String useName;  //用途
	private String barCode;   //条码
	private String financeFlag;  //是否财务收费项目1 是 0 否
	private String memo;  //备注
	private String extend;  //扩展字段
	private String applyListCode;   //申请单号
	private Integer applySerialNo;  //申请单内序号
	private String recipeNo;    //处方号
	private Integer sequenceNo;   //处方内项目流水号
	private Double returns;   //已退数量
	private Double returnApplyNum;  //申请退库数量
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
	public String getOutType() {
		return outType;
	}
	public void setOutType(String outType) {
		this.outType = outType;
	}
	public String getOutClass() {
		return outClass;
	}
	public void setOutClass(String outClass) {
		this.outClass = outClass;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStockNo() {
		return stockNo;
	}
	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
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
	public String getStatUnit() {
		return statUnit;
	}
	public void setStatUnit(String statUnit) {
		this.statUnit = statUnit;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
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
	public String getIsPrivate() {
		return isPrivate;
	}
	public void setIsPrivate(String isPrivate) {
		this.isPrivate = isPrivate;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getIsNurse() {
		return isNurse;
	}
	public void setIsNurse(String isNurse) {
		this.isNurse = isNurse;
	}
	public String getDrawOper() {
		return drawOper;
	}
	public void setDrawOper(String drawOper) {
		this.drawOper = drawOper;
	}
	public String getGetPerson() {
		return getPerson;
	}
	public void setGetPerson(String getPerson) {
		this.getPerson = getPerson;
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
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
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
	public String getBillState() {
		return billState;
	}
	public void setBillState(String billState) {
		this.billState = billState;
	}
	public Integer getDebit() {
		return debit;
	}
	public void setDebit(Integer debit) {
		this.debit = debit;
	}
	public String getInNo() {
		return inNo;
	}
	public void setInNo(String inNo) {
		this.inNo = inNo;
	}
	public String getOutSequence() {
		return outSequence;
	}
	public void setOutSequence(String outSequence) {
		this.outSequence = outSequence;
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
	public String getFinanceFlag() {
		return financeFlag;
	}
	public void setFinanceFlag(String financeFlag) {
		this.financeFlag = financeFlag;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
	public String getApplyListCode() {
		return applyListCode;
	}
	public void setApplyListCode(String applyListCode) {
		this.applyListCode = applyListCode;
	}
	public Integer getApplySerialNo() {
		return applySerialNo;
	}
	public void setApplySerialNo(Integer applySerialNo) {
		this.applySerialNo = applySerialNo;
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
	public Double getReturns() {
		return returns;
	}
	public void setReturns(Double returns) {
		this.returns = returns;
	}
	public Double getReturnApplyNum() {
		return returnApplyNum;
	}
	public void setReturnApplyNum(Double returnApplyNum) {
		this.returnApplyNum = returnApplyNum;
	}
	
}
