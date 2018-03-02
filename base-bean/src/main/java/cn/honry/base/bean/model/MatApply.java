package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * TMatApply entity. @author MyEclipse Persistence Tools aizhonghua
 * 物资入出库申请表
 */

public class MatApply extends Entity {
	
	/**申请流水号**/
	private String applyNo;
	/**申请单号**/
	private String applyListCode;
	/**单内序号**/
	private Integer applySerialNo;
	/**申请部门(库存部门)**/
	private String storageCode;
	/**目标科室**/
	private String targetDept;
	/**1-入库申请、2-出库申请**/
	private Integer stockClass;
	/**二级权限**/
	private String class2Priv;
	/**系统定义类型**/
	private String sysClass3mean;
	/**申请状态(0-申请,1-审批,2-核准)**/
	private Integer applyState;
	/**物品编码**/
	private String itemCode;
	/**物品名称**/
	private String itemName;
	/**物品科目编码**/
	private String kindCode;
	/**规格**/
	private String specs;
	/**最小单位**/
	private String minUnit;
	/**大包装单位**/
	private String packUnit;
	/**大包装包装数量**/
	private Integer packQty;
	/**申请数量**/
	private Integer applyNum;
	/**申请价格(入库价)**/
	private Double applyPrice;
	/**申请金额**/
	private Double applyCost;
	/**零售价格**/
	private Double salePrice;
	/**零售金额**/
	private Double saleCost;
	/**申请人**/
	private String applyOper;
	/**申请时间**/
	private Date applyDate;
	/**审批人**/
	private String examOper;
	/**审批人时间**/
	private Date examDate;
	/**核准人**/
	private String approveOper;
	/**核准时间**/
	private Date approveDate;
	/**作废人编码**/
	private String abolishOper;
	/**作废时间**/
	private Date abolishDate;
	/**审批数量**/
	private Integer examNum;
	/**公司编码**/
	private String companyCode;
	/**公司名称**/
	private String companyName;
	/**申请科室库存**/
	private Integer storeSum;
	/**全院库存总和**/
	private Integer storeTotsum;
	/**有效性状态(1-有效,0-无效)**/
	private Integer validState;
	/**出库单号**/
	private String outListCode;
	/**入库单号**/
	private String inListCode;
	/**备注**/
	private String memo;
	/**操作员**/
	private String operCode;
	/**操作日期**/
	private Date operDate;
	/**计划单流水号*/
	private String buyListCode;
	/**申请科室审核(1-审核,0-未审核)*/
	private Integer applyCheck;
	/**审核人*/
	private String checkOper;
	/**审核时间*/
	private Date checkDate;
	/**计划状态(0-新申请,1-做过周计划,2-做过月计划)*/
	private Integer applyPlanState;
	
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
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
	public Integer getStockClass() {
		return stockClass;
	}
	public void setStockClass(Integer stockClass) {
		this.stockClass = stockClass;
	}
	public String getClass2Priv() {
		return class2Priv;
	}
	public void setClass2Priv(String class2Priv) {
		this.class2Priv = class2Priv;
	}
	public String getSysClass3mean() {
		return sysClass3mean;
	}
	public void setSysClass3mean(String sysClass3mean) {
		this.sysClass3mean = sysClass3mean;
	}
	public Integer getApplyState() {
		return applyState;
	}
	public void setApplyState(Integer applyState) {
		this.applyState = applyState;
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
	public Integer getApplyNum() {
		return applyNum;
	}
	public void setApplyNum(Integer applyNum) {
		this.applyNum = applyNum;
	}
	public Double getApplyPrice() {
		return applyPrice;
	}
	public void setApplyPrice(Double applyPrice) {
		this.applyPrice = applyPrice;
	}
	public Double getApplyCost() {
		return applyCost;
	}
	public void setApplyCost(Double applyCost) {
		this.applyCost = applyCost;
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
	public String getAbolishOper() {
		return abolishOper;
	}
	public void setAbolishOper(String abolishOper) {
		this.abolishOper = abolishOper;
	}
	public Date getAbolishDate() {
		return abolishDate;
	}
	public void setAbolishDate(Date abolishDate) {
		this.abolishDate = abolishDate;
	}
	public Integer getExamNum() {
		return examNum;
	}
	public void setExamNum(Integer examNum) {
		this.examNum = examNum;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Integer getStoreSum() {
		return storeSum;
	}
	public void setStoreSum(Integer storeSum) {
		this.storeSum = storeSum;
	}
	public Integer getStoreTotsum() {
		return storeTotsum;
	}
	public void setStoreTotsum(Integer storeTotsum) {
		this.storeTotsum = storeTotsum;
	}
	public Integer getValidState() {
		return validState;
	}
	public void setValidState(Integer validState) {
		this.validState = validState;
	}
	public String getOutListCode() {
		return outListCode;
	}
	public void setOutListCode(String outListCode) {
		this.outListCode = outListCode;
	}
	public String getInListCode() {
		return inListCode;
	}
	public void setInListCode(String inListCode) {
		this.inListCode = inListCode;
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
	public String getBuyListCode() {
		return buyListCode;
	}
	public void setBuyListCode(String buyListCode) {
		this.buyListCode = buyListCode;
	}
	public Integer getApplyCheck() {
		return applyCheck;
	}
	public void setApplyCheck(Integer applyCheck) {
		this.applyCheck = applyCheck;
	}
	public String getCheckOper() {
		return checkOper;
	}
	public void setCheckOper(String checkOper) {
		this.checkOper = checkOper;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public Integer getApplyPlanState() {
		return applyPlanState;
	}
	public void setApplyPlanState(Integer applyPlanState) {
		this.applyPlanState = applyPlanState;
	}
	
}