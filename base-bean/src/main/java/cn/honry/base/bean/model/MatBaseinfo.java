package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 物资字典表
 */
public class MatBaseinfo  extends Entity{

	/**物品编码**/
	private String itemCode;
	/**物品科目编码**/
	private String kindCode;
	/**仓库代码**/
	private String storageCode;
	/**物品名称**/
	private String itemName;
	/**拼音编码**/
	private String spellCode;
	/**五笔码**/
	private String wbCode;
	/**自定义码**/
	private String customCode;
	/**国家编码**/
	private String gbCode;
	/**别名**/
	private String otherName;
	/**别名拼音码**/
	private String otherSpell;
	/**别名五笔码**/
	private String otherWb;
	/**别名自定义码**/
	private String otherCustom;
	/**有效范围(0-本科室,1-本科室及下级科室,2-全院,3-指定科室)**/
	private Integer effectArea;
	/**有效科室(EFFECT_AREA=3时有效)**/
	private String effectDept;
	/**规格**/
	private String specs;
	/**最小单位**/
	private String minUnit;
	/**最新入库单价(包装单位)**/
	private Double inPrice;
	/**零售价格**/
	private Double salePrice;
	/**大包装单位**/
	private String packUnit;
	/**大包装数量**/
	private Long packQty;
	/**大包装价格**/
	private Double packPrice;
	/**加价规则、用于入库自动加价 0：不加价，1,按固定加价率2,按价格加价3,按规格加价**/
	private String addRate;
	/**最小费用代码**/
	private String feeCode;
	/**财务收费标志(0－否,1－是)**/
	private Integer financeFlag;
	/**停用标记(0－停用,1－使用)**/
	private Integer validFlag;
	/**特殊材料标志(0－否,1－是)**/
	private Integer specialFlag;
	/**高值耗材标志(0－否,1－是)**/
	private Integer highvalueFlag;
	/**生产厂家**/
	private String factoryCode;
	/**供货公司**/
	private String companyCode;
	/**来源**/
	private String inSource;
	/**用途**/
	private String usage;
	/**批文信息**/
	private String approveInfo;
	/**生产者**/
	private String mader;
	/**注册号**/
	private String registerCode;
	/**特殊类别**/
	private String specialType;
	/**注册时间**/
	private Date registerDate;
	/**到期时间**/
	private Date overDate;
	/**是否打包-供应室用(1是0否)**/
	private Integer packFlag;
	/**财务审核标记**/
	private Integer examineFlag;
	/**是否一次性耗材-供应室用(1是0否)**/
	private Integer norecycleFlag;
	/**备注**/
	private String memo;
	/**操作员**/
	private String operCode;
	/**操作日期**/
	private Date operDate;
	/**是否按批次管理 0否 1是**/
	private Integer batchFlag;
	/**按周计划入库还是月计划入库  (0－否,1－是)**/
	private String plan;
	/**是否允许收费预扣 0否 1是**/
	private Integer prestockFlag;
	/**上限数量**/
	private Double topNum;
	/**下限数量**/
	private Double lowNum;
	//数据库无关字段
	/**供货公司名称**/
	private String companyName;
	
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getKindCode() {
		return this.kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	public String getStorageCode() {
		return this.storageCode;
	}

	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSpellCode() {
		return this.spellCode;
	}

	public void setSpellCode(String spellCode) {
		this.spellCode = spellCode;
	}

	public String getWbCode() {
		return this.wbCode;
	}

	public void setWbCode(String wbCode) {
		this.wbCode = wbCode;
	}

	public String getCustomCode() {
		return this.customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getGbCode() {
		return this.gbCode;
	}

	public void setGbCode(String gbCode) {
		this.gbCode = gbCode;
	}

	public String getOtherName() {
		return this.otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public String getOtherSpell() {
		return this.otherSpell;
	}

	public void setOtherSpell(String otherSpell) {
		this.otherSpell = otherSpell;
	}

	public String getOtherWb() {
		return this.otherWb;
	}

	public void setOtherWb(String otherWb) {
		this.otherWb = otherWb;
	}

	public String getOtherCustom() {
		return this.otherCustom;
	}

	public void setOtherCustom(String otherCustom) {
		this.otherCustom = otherCustom;
	}

	public String getEffectDept() {
		return this.effectDept;
	}

	public void setEffectDept(String effectDept) {
		this.effectDept = effectDept;
	}

	public String getSpecs() {
		return this.specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public String getMinUnit() {
		return this.minUnit;
	}

	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}

	public Double getInPrice() {
		return this.inPrice;
	}

	public void setInPrice(Double inPrice) {
		this.inPrice = inPrice;
	}

	public Double getSalePrice() {
		return this.salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public String getPackUnit() {
		return this.packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public Long getPackQty() {
		return this.packQty;
	}

	public void setPackQty(Long packQty) {
		this.packQty = packQty;
	}

	public Double getPackPrice() {
		return this.packPrice;
	}

	public void setPackPrice(Double packPrice) {
		this.packPrice = packPrice;
	}

	public String getAddRate() {
		return this.addRate;
	}

	public void setAddRate(String addRate) {
		this.addRate = addRate;
	}

	public String getFeeCode() {
		return this.feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getFactoryCode() {
		return this.factoryCode;
	}

	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}

	public String getCompanyCode() {
		return this.companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getInSource() {
		return this.inSource;
	}

	public void setInSource(String inSource) {
		this.inSource = inSource;
	}

	public String getUsage() {
		return this.usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getApproveInfo() {
		return this.approveInfo;
	}

	public void setApproveInfo(String approveInfo) {
		this.approveInfo = approveInfo;
	}

	public String getMader() {
		return this.mader;
	}

	public void setMader(String mader) {
		this.mader = mader;
	}

	public String getRegisterCode() {
		return this.registerCode;
	}

	public void setRegisterCode(String registerCode) {
		this.registerCode = registerCode;
	}

	public String getSpecialType() {
		return this.specialType;
	}

	public void setSpecialType(String specialType) {
		this.specialType = specialType;
	}

	public Date getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Date getOverDate() {
		return this.overDate;
	}

	public void setOverDate(Date overDate) {
		this.overDate = overDate;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOperCode() {
		return this.operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}

	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public Integer getEffectArea() {
		return effectArea;
	}

	public void setEffectArea(Integer effectArea) {
		this.effectArea = effectArea;
	}

	public Integer getFinanceFlag() {
		return financeFlag;
	}

	public void setFinanceFlag(Integer financeFlag) {
		this.financeFlag = financeFlag;
	}

	public Integer getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}

	public Integer getSpecialFlag() {
		return specialFlag;
	}

	public void setSpecialFlag(Integer specialFlag) {
		this.specialFlag = specialFlag;
	}

	public Integer getHighvalueFlag() {
		return highvalueFlag;
	}

	public void setHighvalueFlag(Integer highvalueFlag) {
		this.highvalueFlag = highvalueFlag;
	}

	public Integer getPackFlag() {
		return packFlag;
	}

	public void setPackFlag(Integer packFlag) {
		this.packFlag = packFlag;
	}

	public Integer getExamineFlag() {
		return examineFlag;
	}

	public void setExamineFlag(Integer examineFlag) {
		this.examineFlag = examineFlag;
	}

	public Integer getNorecycleFlag() {
		return norecycleFlag;
	}

	public void setNorecycleFlag(Integer norecycleFlag) {
		this.norecycleFlag = norecycleFlag;
	}

	public Integer getBatchFlag() {
		return batchFlag;
	}

	public void setBatchFlag(Integer batchFlag) {
		this.batchFlag = batchFlag;
	}

	public Integer getPrestockFlag() {
		return prestockFlag;
	}

	public void setPrestockFlag(Integer prestockFlag) {
		this.prestockFlag = prestockFlag;
	}

	public String getPlan() {
		return this.plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public Double getTopNum() {
		return this.topNum;
	}

	public void setTopNum(Double topNum) {
		this.topNum = topNum;
	}

	public Double getLowNum() {
		return this.lowNum;
	}

	public void setLowNum(Double lowNum) {
		this.lowNum = lowNum;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyName() {
		return companyName;
	}

}