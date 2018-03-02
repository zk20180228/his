package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class OutpatientMixLiquid extends Entity  implements java.io.Serializable {
	/**项目流水号**/
	private String sequenceNo;
	/**看诊序号**/
	private String seeNo;
	/**门诊号**/
	private String clinicCode;
	/**病历号**/
	private String patientNo;
	/**挂号日期**/
	private Date regDate;
	/**挂号科室**/
	private String regDept;
	/**项目代码**/
	private String itemCode;
	/**项目名称**/
	private String itemName;
	/**规格**/
	private String specs;
	/**系统类别**/
	private String classCode;
	/**最小费用代码**/
	private String feeCode;
	/**单价**/
	private Double unitPrice;
	/**开立数量**/
	private Double qty;
	/**付数**/
	private Integer days;
	/**包装数量**/
	private Integer packQty;
	/**计价单位**/
	private String itemUnit;
	/**自费金额**/
	private Double ownCost;
	/**自负金额**/
	private Double payCost;
	/**报销金额**/
	private Double pubCost;
	/**基本剂量**/
	private Double baseDose;
	/**自制药**/
	private Integer selfMade;
	/**药品性质**/
	private String drugQuanlity;
	/**每次用量（剂量）**/
	private Double onceDose;
	/**每次用量单位**/
	private String onceUnit;
	/**剂型代码**/
	private String doseModelCode;
	/**频次**/
	private String frequencyCode;
	/**用法**/
	private String usageCode;
	/**执行科室代码**/
	private String execDpcd;
	/**主药标志**/
	private Integer mainDrug;
	/**组合号**/
	private String combNo;
	/**皮试**/
	private Integer hypotest;
	/**院内注射次数**/
	private Integer injectNumber;
	/**备注**/
	private String remark;
	/**开立医生**/
	private String doctCode;
	/**医生科室**/
	private String doctDpcd;
	/**开立时间**/
	private Date operDate;
	/**处方状态**/
	private Integer status;
	/**加急标记**/
	private Integer emcFlag;
	/**申请单号**/
	private String applyNo;
	/**附材**/
	private Integer subtblFlag;
	/**是否需要确认**/
	private Integer needConfirm;
	/**0未收费/1收费**/
	private Integer chargeFlag;
	/**收费员**/
	private String chargeCode;
	/**收费时间**/
	private Date chargeDate;
	/**处方号**/
	private String recipeNo;
	/**处方内流水号 **/
	private Integer recipeSeq;
	/**收费序列 **/
	private String recipeFeeseq;
	/**发药药房**/
	private String phamarcyCode;
	/**开立单位 1 包装单位0是最小单位**/
	private Integer minunitFlag;
	/**排列序号，按排列序号由大到小顺序显示医嘱**/
	private Integer dataorder;
	/**处方打印标志 **/
	private Integer printFlag;
	/**配液人**/
	private String confirmCode;
	/**配液科室**/
	private String confirmDept;
	/**配液时间**/
	private Date confirmDate;
	/**医嘱号**/
	private String moOrder;
	/**有无不良反应 0-无  1-有**/
	private Integer ifReact;
	/**局部皮肤情况 0：完好 1：渗透**/
	private Integer skinStatus;
	/**配液备注**/
	private String describe;
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getSeeNo() {
		return seeNo;
	}
	public void setSeeNo(String seeNo) {
		this.seeNo = seeNo;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public String getRegDept() {
		return regDept;
	}
	public void setRegDept(String regDept) {
		this.regDept = regDept;
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
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public Integer getPackQty() {
		return packQty;
	}
	public void setPackQty(Integer packQty) {
		this.packQty = packQty;
	}
	public String getItemUnit() {
		return itemUnit;
	}
	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}
	public Double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}
	public Double getPubCost() {
		return pubCost;
	}
	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}
	public Double getBaseDose() {
		return baseDose;
	}
	public void setBaseDose(Double baseDose) {
		this.baseDose = baseDose;
	}
	public Integer getSelfMade() {
		return selfMade;
	}
	public void setSelfMade(Integer selfMade) {
		this.selfMade = selfMade;
	}
	public String getDrugQuanlity() {
		return drugQuanlity;
	}
	public void setDrugQuanlity(String drugQuanlity) {
		this.drugQuanlity = drugQuanlity;
	}
	public Double getOnceDose() {
		return onceDose;
	}
	public void setOnceDose(Double onceDose) {
		this.onceDose = onceDose;
	}
	public String getOnceUnit() {
		return onceUnit;
	}
	public void setOnceUnit(String onceUnit) {
		this.onceUnit = onceUnit;
	}
	public String getDoseModelCode() {
		return doseModelCode;
	}
	public void setDoseModelCode(String doseModelCode) {
		this.doseModelCode = doseModelCode;
	}
	public String getFrequencyCode() {
		return frequencyCode;
	}
	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}
	public String getUsageCode() {
		return usageCode;
	}
	public void setUsageCode(String usageCode) {
		this.usageCode = usageCode;
	}
	public String getExecDpcd() {
		return execDpcd;
	}
	public void setExecDpcd(String execDpcd) {
		this.execDpcd = execDpcd;
	}
	public Integer getMainDrug() {
		return mainDrug;
	}
	public void setMainDrug(Integer mainDrug) {
		this.mainDrug = mainDrug;
	}
	public String getCombNo() {
		return combNo;
	}
	public void setCombNo(String combNo) {
		this.combNo = combNo;
	}
	public Integer getHypotest() {
		return hypotest;
	}
	public void setHypotest(Integer hypotest) {
		this.hypotest = hypotest;
	}
	public Integer getInjectNumber() {
		return injectNumber;
	}
	public void setInjectNumber(Integer injectNumber) {
		this.injectNumber = injectNumber;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDoctCode() {
		return doctCode;
	}
	public void setDoctCode(String doctCode) {
		this.doctCode = doctCode;
	}
	public String getDoctDpcd() {
		return doctDpcd;
	}
	public void setDoctDpcd(String doctDpcd) {
		this.doctDpcd = doctDpcd;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getEmcFlag() {
		return emcFlag;
	}
	public void setEmcFlag(Integer emcFlag) {
		this.emcFlag = emcFlag;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public Integer getSubtblFlag() {
		return subtblFlag;
	}
	public void setSubtblFlag(Integer subtblFlag) {
		this.subtblFlag = subtblFlag;
	}
	public Integer getNeedConfirm() {
		return needConfirm;
	}
	public void setNeedConfirm(Integer needConfirm) {
		this.needConfirm = needConfirm;
	}
	public Integer getChargeFlag() {
		return chargeFlag;
	}
	public void setChargeFlag(Integer chargeFlag) {
		this.chargeFlag = chargeFlag;
	}
	public String getChargeCode() {
		return chargeCode;
	}
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}
	public Date getChargeDate() {
		return chargeDate;
	}
	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public Integer getRecipeSeq() {
		return recipeSeq;
	}
	public void setRecipeSeq(Integer recipeSeq) {
		this.recipeSeq = recipeSeq;
	}
	public String getRecipeFeeseq() {
		return recipeFeeseq;
	}
	public void setRecipeFeeseq(String recipeFeeseq) {
		this.recipeFeeseq = recipeFeeseq;
	}
	public String getPhamarcyCode() {
		return phamarcyCode;
	}
	public void setPhamarcyCode(String phamarcyCode) {
		this.phamarcyCode = phamarcyCode;
	}
	public Integer getMinunitFlag() {
		return minunitFlag;
	}
	public void setMinunitFlag(Integer minunitFlag) {
		this.minunitFlag = minunitFlag;
	}
	public Integer getDataorder() {
		return dataorder;
	}
	public void setDataorder(Integer dataorder) {
		this.dataorder = dataorder;
	}
	public Integer getPrintFlag() {
		return printFlag;
	}
	public void setPrintFlag(Integer printFlag) {
		this.printFlag = printFlag;
	}
	public String getConfirmCode() {
		return confirmCode;
	}
	public void setConfirmCode(String confirmCode) {
		this.confirmCode = confirmCode;
	}
	public String getConfirmDept() {
		return confirmDept;
	}
	public void setConfirmDept(String confirmDept) {
		this.confirmDept = confirmDept;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getMoOrder() {
		return moOrder;
	}
	public void setMoOrder(String moOrder) {
		this.moOrder = moOrder;
	}
	public Integer getIfReact() {
		return ifReact;
	}
	public void setIfReact(Integer ifReact) {
		this.ifReact = ifReact;
	}
	public Integer getSkinStatus() {
		return skinStatus;
	}
	public void setSkinStatus(Integer skinStatus) {
		this.skinStatus = skinStatus;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
}
