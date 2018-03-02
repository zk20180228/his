package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * MatInput entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class MatInput extends Entity{

	// Fields
	/**入库流水号**/
	private String inNo;
	/**入库单号**/
	private String inListCode;
	/**入库单内序号**/
	private Integer inSerialNo;
	/**仓库编码**/
	private String storageCode;
	/**库存流水号**/
	private String stockCode;
	/**生产厂家认证记录编号**/
	private String regCode;
	/**入库类型  用户定义**/
	private String inClass3;
	/**入库分类  系统定义**/
	private String inClass3mean;
	/**交易类型(1-正交易,2-反交易)**/
	private Integer transType;
	/**状态(0-申请入库,1-正式入库,2-核准入库)**/
	private Integer inState;
	/**物品编码**/
	private String itemCode;
	/**物品名称**/
	private String itemName;
	/**物品科目编码**/
	private String kindCode;
	/**规格**/
	private String specs;
	/**批号**/
	private String batchNo;
	/**入库数量**/
	private Double inNum;
	/**最小单位**/
	private String minUnit;
	/**大包装入库数量**/
	private Integer packInNum;
	/**大包装单位**/
	private String packUnit;
	/**大包装包装数量**/
	private Long packQty;
	/**入库价(大包装)**/
	private Double inPrice;
	/**入库金额**/
	private Double inCost;
	/**加价规则、用于入库自动加价**/
	private String addRate;
	/**零售价格**/
	private Double salePrice=0.0;
	/**零售金额**/
	private Double saleCost=0.0;
	/**入库前库存量**/
	private Double privStoreNum;
	/**库位编号**/
	private String placeCode;
	/**入库日期**/
	private Date inDate;
	/**申请入库操作员**/
	private String applyOper;
	/**申请入库日期**/
	private Date applyDate;
	/**审核入库操作员**/
	private String examOper;
	/**审核入库日期**/
	private Date examDate;
	/**核准操作员**/
	private String approveOper;
	/**核准日期**/
	private Date approveDate;
	/**发票号码**/
	private String invoiceNo;
	/**发票日期(发票上写的日期)**/
	private Date invoiceDate;
	/**来源科室**/
	private String sourceDept;
	/**来源科室名称**/
	private String sourceDeptname;
	/**供货公司编码**/
	private String companyCode;
	/**供货公司名称**/
	private String companyName;
	/**生产厂家**/
	private String factoryCode;
	/**生产日期**/
	private Date produceDate;
	/**有效期**/
	private Date validDate;
	/**是否是生产入库(1-是,0-否)**/
	private Integer produceFlag;
	/**采购员**/
	private String buyOper;
	/**条码号**/
	private String barCode;
	/**计划单流水号**/
	private String planNo;
	/**出库记录流水号**/
	private String outNo;
	/**财务标志**/
	private Integer financeFlag;
	/**已退数量**/
	private Double returnNum;
	/**退掉的入库流水号**/
	private String returnInNo;
	/**招标类型**/
	private String bidType;
	/**招标编号**/
	private String bidCode;
	/**是否有检测报告(1-是,0-否)**/
	private Integer detectFlag;
	/**入库时是否使用大包装入库**/
	private Integer packinFlag;
	/**申请流水号**/
	private String applyNo;
	/**备注**/
	private String memo;
	/**操作员**/
	private String operCode;
	/**操作日期**/
	private Date operDate;
	/**高值耗材标志**/
	private Integer highvalueFlag;
	/**高值耗材条形码**/
	private String highvalueBarcode;
	/**灭菌日期**/
	private Date disinfectionDate;
	/**备货入库操作员**/
	private String virtualOper;
	/**备货入库操作日期**/
	private Date virtualDate;
	/**大包装价格**/
	private Double packPrice;
	//数据库无关字段
	private String matBaseinfoId;
	
	public String getInNo() {
		return inNo;
	}
	public void setInNo(String inNo) {
		this.inNo = inNo;
	}
	public String getInListCode() {
		return inListCode;
	}
	public void setInListCode(String inListCode) {
		this.inListCode = inListCode;
	}
	public Integer getInSerialNo() {
		return inSerialNo;
	}
	public void setInSerialNo(Integer inSerialNo) {
		this.inSerialNo = inSerialNo;
	}
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
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
	public String getInClass3() {
		return inClass3;
	}
	public void setInClass3(String inClass3) {
		this.inClass3 = inClass3;
	}
	public String getInClass3mean() {
		return inClass3mean;
	}
	public void setInClass3mean(String inClass3mean) {
		this.inClass3mean = inClass3mean;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public Integer getInState() {
		return inState;
	}
	public void setInState(Integer inState) {
		this.inState = inState;
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
	public Double getInNum() {
		return inNum;
	}
	public void setInNum(Double inNum) {
		this.inNum = inNum;
	}
	public String getMinUnit() {
		return minUnit;
	}
	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}
	public Integer getPackInNum() {
		return packInNum;
	}
	public void setPackInNum(Integer packInNum) {
		this.packInNum = packInNum;
	}
	public String getPackUnit() {
		return packUnit;
	}
	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}
	public Long getPackQty() {
		return packQty;
	}
	public void setPackQty(Long packQty) {
		this.packQty = packQty;
	}
	public Double getInPrice() {
		return inPrice;
	}
	public void setInPrice(Double inPrice) {
		this.inPrice = inPrice;
	}
	public Double getInCost() {
		return inCost;
	}
	public void setInCost(Double inCost) {
		this.inCost = inCost;
	}
	public String getAddRate() {
		return addRate;
	}
	public void setAddRate(String addRate) {
		this.addRate = addRate;
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
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
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
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getSourceDept() {
		return sourceDept;
	}
	public void setSourceDept(String sourceDept) {
		this.sourceDept = sourceDept;
	}
	public String getSourceDeptname() {
		return sourceDeptname;
	}
	public void setSourceDeptname(String sourceDeptname) {
		this.sourceDeptname = sourceDeptname;
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
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	public Date getProduceDate() {
		return produceDate;
	}
	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	public Integer getProduceFlag() {
		return produceFlag;
	}
	public void setProduceFlag(Integer produceFlag) {
		this.produceFlag = produceFlag;
	}
	public String getBuyOper() {
		return buyOper;
	}
	public void setBuyOper(String buyOper) {
		this.buyOper = buyOper;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getPlanNo() {
		return planNo;
	}
	public void setPlanNo(String planNo) {
		this.planNo = planNo;
	}
	public String getOutNo() {
		return outNo;
	}
	public void setOutNo(String outNo) {
		this.outNo = outNo;
	}
	public Integer getFinanceFlag() {
		return financeFlag;
	}
	public void setFinanceFlag(Integer financeFlag) {
		this.financeFlag = financeFlag;
	}
	public Double getReturnNum() {
		return returnNum;
	}
	public void setReturnNum(Double returnNum) {
		this.returnNum = returnNum;
	}
	public String getReturnInNo() {
		return returnInNo;
	}
	public void setReturnInNo(String returnInNo) {
		this.returnInNo = returnInNo;
	}
	public String getBidType() {
		return bidType;
	}
	public void setBidType(String bidType) {
		this.bidType = bidType;
	}
	public String getBidCode() {
		return bidCode;
	}
	public void setBidCode(String bidCode) {
		this.bidCode = bidCode;
	}
	public Integer getDetectFlag() {
		return detectFlag;
	}
	public void setDetectFlag(Integer detectFlag) {
		this.detectFlag = detectFlag;
	}
	public Integer getPackinFlag() {
		return packinFlag;
	}
	public void setPackinFlag(Integer packinFlag) {
		this.packinFlag = packinFlag;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
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
	public Date getDisinfectionDate() {
		return disinfectionDate;
	}
	public void setDisinfectionDate(Date disinfectionDate) {
		this.disinfectionDate = disinfectionDate;
	}
	public String getVirtualOper() {
		return virtualOper;
	}
	public void setVirtualOper(String virtualOper) {
		this.virtualOper = virtualOper;
	}
	public Date getVirtualDate() {
		return virtualDate;
	}
	public void setVirtualDate(Date virtualDate) {
		this.virtualDate = virtualDate;
	}
	public Double getPackPrice() {
		return packPrice;
	}
	public void setPackPrice(Double packPrice) {
		this.packPrice = packPrice;
	}
	public void setMatBaseinfoId(String matBaseinfoId) {
		this.matBaseinfoId = matBaseinfoId;
	}
	public String getMatBaseinfoId() {
		return matBaseinfoId;
	}

}