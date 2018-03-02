package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：  药品医嘱执行档
 * @Author：liuyuanyuan
 * @CreateDate：2015-8-24 下午05:50:47  
 * @Modifier：liuyuanyuan
 * @ModifyDate：2015-8-24 下午05:50:47  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class InpatientExecdrugNow extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 医嘱执行单流水号*/
	 private String execId;
	 /** 住院科室代码*/
	 private String deptCode;
	 /** 医嘱护理站代码*/
	 private String nurseCellCode;
	 /** 开立科室代码*/
	 private String listDpcd;
	 /** 住院流水号*/
	 private String inpatientNo;
	 /**住院病历号*/
	 private String  patientNo;
	 /**医嘱流水号*/
	 private String moOrder;
	 /**医嘱医师代号*/
	 private String docCode;
	 /**医嘱医师姓名*/
	 private String docName;
	 /**医嘱日期*/
	 private Date moDate;
	 /**是否婴儿医嘱*/
	 private Integer babyFlag;
	 /**婴儿序号*/
	 private Integer happenNo;
	 /**项目属性*/
	 private Integer setItmattr;
	 /**是否包含附材  '1',包含附材 0 不包*/
	 private Integer setSubtbl;
	 /**医嘱类别代码*/
	 private String typeCode;
	 /**医嘱是否分解*/
	 private Integer decmpsState;
	 /**是否计费*/
	 private Integer chargeState;
	 /**药房是否配药*/
	 private Integer needDrug;
	 /**打印执行单*/
	 private Integer prnExelist;
	 /**是否打印医嘱单*/
	 private Integer prnMorlist;
	 /**是否需要确认*/
	 private Integer  needConfirm;
	 /**药品编码*/
	 private String  drugCode;
	 /**药品名称*/
	 private String drugName;
	 /**药品基本剂量*/
	 private Double baseDose;
	 /**剂量单位*/
	 private String doseUnit;
	  /**最小单位*/
	 private String  minUnit;
	  /**计价单位*/
	 private String priceUnit;
	  /**包装数量*/
     private Integer packQty;
	  /**规格*/
	 private String specs;
	  /**剂型代码*/
	 private String doseModelCode;
	  /**1西药/2中成/3草*/ 
     private String drugType;
	  /**药品性质*/ 
	 private String drugQuality;
	  /**零售价*/ 
	 private Double  itemPrice;
	  /**1护士站常备/2扣药房*/ 
	 private Integer stockMin;
	 /**组合序号*/ 
	 private String combNo;
	  /**主药标记*/  
	 private Integer mainDrug;
	  /**用法代码*/
	 private String usageCode;
	  /**用法名称*/ 
	 private String useName;
	  /**用法英文缩写*/  
	 private String englishAb;
	  /**频次代码*/ 
	 private String frequencyCode;
	  /**频次名称*/ 
	 private String  frequencyName;
	  /**每次剂量*/
	 private Double doseOnce;
	  /**付数*/ 
	 private Integer useDays;
	 /**药品用量*/
	 private Double qtyTot;
	 /**要求执行时间*/
	 private Date  useTime;
	  /**取药药房*/
	 private String  pharmacyCode;
	  /**执行科室*/
	 private String  execDpcd;
	  /**1有效/0作废*/
	 private Integer  validFlag;
	  /**作废时间*/
	 private Date   validDate;
	  /**作废人代码*/
	 private String   validUsercd;
	  /**0不需发送/1集中发送/2分散发送/3已配药*/
	 private Integer drugedFlag;
	  /**配药时间*/
	 private Date  drugedDate;
	 /**配药人员代码*/
	 private String  drugedUsercd;
	 /**配药科室代码*/
	 private String   drugedDeptcd;
	 /**0未打印/1已打印*/
	 private Integer prnFlag;
	 /**打印日期*/
	 private Date  prnDate;
	 /**打印人员代码*/
	 private String   prnUsercd;
	 /**打印科室代码*/
	 private String  prnDeptcd;
	 /**配药单组别代码*/
	 private String  setCode;
	 /**配药单号*/
	 private String   setSeqn;
	 /**0待执行/1已*/
	 private Integer  execFlag;
	 /**执行时间*/
	 private Date   execDate;
	 /**执行护士代码*/ 
	 private String   execUsercd;
	 /**执行科室代码*/ 
	 private String   execDeptcd;
	 /**0未打印/1已 - 输液卡*/ 
	 private Integer  execPrnflag;
	 /**执行单打印时间*/
	 private Date execPrndate;
	 /**执行单打印人员*/ 
	 private String  execPrnusercd;
	 /**记账标记*/
	 private Integer  chargeFlag;
	 /**记账时间*/
	 private Date chargeDate;
	 /**记账人代码*/ 
	 private String  chargeUsercd;
	 /**记账科室代码*/
	 private String  chargeDeptcd;
	 /**处方流水号*/ 
	 private String   recipeNo;
	 /**处方内流水号*/
	 private Integer  sequenceNo;
	 /**医嘱说明*/
	 private String  moNote1;
	 /**备注*/
	 private String   moNote2;
	 /**分解时间*/
	 private Date   decoDate;
	 /**收费发送单打印标记*/ 
     private Integer   chargePrnflag;
     /**收费发送单打印日期*/
	 private Date  chargePrndate;
	 /**收费发送单打印人员*/
	 private String   chargePrnusercd;
	 /**巡回卡打印标记*/
	 private Integer  circultPrnflag;
	 /**是否需配液 ‘1’ 是 0 否*/
	 private Integer  compoundFlag;
	 /**是否配液已执行 1 是 0 否*/
	 private Integer   compoundExec;
	 /**配液执行人*/
	 private String    compoundOper;
	 /**配液科室*/
	 private String  compoundDept;
	 /**配液时间*/
	 private Date   compoundDate;
	 
	 
	 private InpatientInfoNow patient;//患者信息
	 private String feeCode;//最小费用代码
	 private Integer homeMadeFlag;//自制标识 
	 private Integer emcFlag;// 急诊抢救标志     
	 
	 //新加字段
	 /**住院科室名称**/
	 private String deptName;
	 /**医嘱护理站名称**/
	 private String nurseCellName;
	 /**开立科室名称**/
	 private String listDpcdName;
	 /**取药药房名称**/
	 private String pharmacyName;
	 /**执行科室名称**/
	 private String execDpcdName;
	 /**作废人名称**/
	 private String validUsercdName;
	 /**配药人员名称**/
	 private String drugedUsercdName;
	 /**配药科室名称**/
	 private String drugedDeptcdName;
	 /**执行护士名称**/
	 private String execUsercdName;
	 /**执行科室名称**/
	 private String execDeptcdName;
	//新增字段 2017-06-12
	   /**医院编码**/
	   private Integer hospitalId;
	   /**院区编码吗**/
	   private String areaCode;
	   
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getExecUsercdName() {
		return execUsercdName;
	}
	public void setExecUsercdName(String execUsercdName) {
		this.execUsercdName = execUsercdName;
	}
	public String getExecDeptcdName() {
		return execDeptcdName;
	}
	public void setExecDeptcdName(String execDeptcdName) {
		this.execDeptcdName = execDeptcdName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getNurseCellName() {
		return nurseCellName;
	}
	public void setNurseCellName(String nurseCellName) {
		this.nurseCellName = nurseCellName;
	}
	public String getListDpcdName() {
		return listDpcdName;
	}
	public void setListDpcdName(String listDpcdName) {
		this.listDpcdName = listDpcdName;
	}
	public String getPharmacyName() {
		return pharmacyName;
	}
	public void setPharmacyName(String pharmacyName) {
		this.pharmacyName = pharmacyName;
	}
	public String getExecDpcdName() {
		return execDpcdName;
	}
	public void setExecDpcdName(String execDpcdName) {
		this.execDpcdName = execDpcdName;
	}
	public String getValidUsercdName() {
		return validUsercdName;
	}
	public void setValidUsercdName(String validUsercdName) {
		this.validUsercdName = validUsercdName;
	}
	public String getDrugedUsercdName() {
		return drugedUsercdName;
	}
	public void setDrugedUsercdName(String drugedUsercdName) {
		this.drugedUsercdName = drugedUsercdName;
	}
	public String getDrugedDeptcdName() {
		return drugedDeptcdName;
	}
	public void setDrugedDeptcdName(String drugedDeptcdName) {
		this.drugedDeptcdName = drugedDeptcdName;
	}
	public String getExecId() {
		return execId;
	}
	public void setExecId(String execId) {
		this.execId = execId;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public String getListDpcd() {
		return listDpcd;
	}
	public void setListDpcd(String listDpcd) {
		this.listDpcd = listDpcd;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public String getMoOrder() {
		return moOrder;
	}
	public void setMoOrder(String moOrder) {
		this.moOrder = moOrder;
	}
	public String getDocCode() {
		return docCode;
	}
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public Date getMoDate() {
		return moDate;
	}
	public void setMoDate(Date moDate) {
		this.moDate = moDate;
	}
	public Integer getBabyFlag() {
		return babyFlag;
	}
	public void setBabyFlag(Integer babyFlag) {
		this.babyFlag = babyFlag;
	}
	public Integer getHappenNo() {
		return happenNo;
	}
	public void setHappenNo(Integer happenNo) {
		this.happenNo = happenNo;
	}
	public Integer getSetItmattr() {
		return setItmattr;
	}
	public void setSetItmattr(Integer setItmattr) {
		this.setItmattr = setItmattr;
	}
	public Integer getSetSubtbl() {
		return setSubtbl;
	}
	public void setSetSubtbl(Integer setSubtbl) {
		this.setSubtbl = setSubtbl;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public Integer getDecmpsState() {
		return decmpsState;
	}
	public void setDecmpsState(Integer decmpsState) {
		this.decmpsState = decmpsState;
	}
	public Integer getChargeState() {
		return chargeState;
	}
	public void setChargeState(Integer chargeState) {
		this.chargeState = chargeState;
	}
	public Integer getNeedDrug() {
		return needDrug;
	}
	public void setNeedDrug(Integer needDrug) {
		this.needDrug = needDrug;
	}
	public Integer getPrnExelist() {
		return prnExelist;
	}
	public void setPrnExelist(Integer prnExelist) {
		this.prnExelist = prnExelist;
	}
	public Integer getPrnMorlist() {
		return prnMorlist;
	}
	public void setPrnMorlist(Integer prnMorlist) {
		this.prnMorlist = prnMorlist;
	}
	public Integer getNeedConfirm() {
		return needConfirm;
	}
	public void setNeedConfirm(Integer needConfirm) {
		this.needConfirm = needConfirm;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public Double getBaseDose() {
		return baseDose;
	}
	public void setBaseDose(Double baseDose) {
		this.baseDose = baseDose;
	}
	public String getDoseUnit() {
		return doseUnit;
	}
	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}
	public String getMinUnit() {
		return minUnit;
	}
	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}
	public String getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}
	public Integer getPackQty() {
		return packQty;
	}
	public void setPackQty(Integer packQty) {
		this.packQty = packQty;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public String getDoseModelCode() {
		return doseModelCode;
	}
	public void setDoseModelCode(String doseModelCode) {
		this.doseModelCode = doseModelCode;
	}
	
	public String getDrugType() {
		return drugType;
	}
	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}
	public String getDrugQuality() {
		return drugQuality;
	}
	public void setDrugQuality(String drugQuality) {
		this.drugQuality = drugQuality;
	}
	public Double getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}
	public Integer getStockMin() {
		return stockMin;
	}
	public void setStockMin(Integer stockMin) {
		this.stockMin = stockMin;
	}
	public String getCombNo() {
		return combNo;
	}
	public void setCombNo(String combNo) {
		this.combNo = combNo;
	}
	public Integer getMainDrug() {
		return mainDrug;
	}
	public void setMainDrug(Integer mainDrug) {
		this.mainDrug = mainDrug;
	}
	public String getUsageCode() {
		return usageCode;
	}
	public void setUsageCode(String usageCode) {
		this.usageCode = usageCode;
	}
	public String getUseName() {
		return useName;
	}
	public void setUseName(String useName) {
		this.useName = useName;
	}
	public String getEnglishAb() {
		return englishAb;
	}
	public void setEnglishAb(String englishAb) {
		this.englishAb = englishAb;
	}
	public String getFrequencyCode() {
		return frequencyCode;
	}
	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}
	public String getFrequencyName() {
		return frequencyName;
	}
	public void setFrequencyName(String frequencyName) {
		this.frequencyName = frequencyName;
	}
	public Double getDoseOnce() {
		return doseOnce;
	}
	public void setDoseOnce(Double doseOnce) {
		this.doseOnce = doseOnce;
	}
	public Integer getUseDays() {
		return useDays;
	}
	public void setUseDays(Integer useDays) {
		this.useDays = useDays;
	}
	public Double getQtyTot() {
		return qtyTot;
	}
	public void setQtyTot(Double qtyTot) {
		this.qtyTot = qtyTot;
	}
	public Date getUseTime() {
		return useTime;
	}
	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}
	public String getPharmacyCode() {
		return pharmacyCode;
	}
	public void setPharmacyCode(String pharmacyCode) {
		this.pharmacyCode = pharmacyCode;
	}
	public String getExecDpcd() {
		return execDpcd;
	}
	public void setExecDpcd(String execDpcd) {
		this.execDpcd = execDpcd;
	}
	public Integer getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	public String getValidUsercd() {
		return validUsercd;
	}
	public void setValidUsercd(String validUsercd) {
		this.validUsercd = validUsercd;
	}
	public Integer getDrugedFlag() {
		return drugedFlag;
	}
	public void setDrugedFlag(Integer drugedFlag) {
		this.drugedFlag = drugedFlag;
	}
	
	public Date getDrugedDate() {
		return drugedDate;
	}
	public void setDrugedDate(Date drugedDate) {
		this.drugedDate = drugedDate;
	}
	public String getDrugedUsercd() {
		return drugedUsercd;
	}
	public void setDrugedUsercd(String drugedUsercd) {
		this.drugedUsercd = drugedUsercd;
	}
	public String getDrugedDeptcd() {
		return drugedDeptcd;
	}
	public void setDrugedDeptcd(String drugedDeptcd) {
		this.drugedDeptcd = drugedDeptcd;
	}
	public Integer getPrnFlag() {
		return prnFlag;
	}
	public void setPrnFlag(Integer prnFlag) {
		this.prnFlag = prnFlag;
	}
	public Date getPrnDate() {
		return prnDate;
	}
	public void setPrnDate(Date prnDate) {
		this.prnDate = prnDate;
	}
	public String getPrnUsercd() {
		return prnUsercd;
	}
	public void setPrnUsercd(String prnUsercd) {
		this.prnUsercd = prnUsercd;
	}
	public String getPrnDeptcd() {
		return prnDeptcd;
	}
	public void setPrnDeptcd(String prnDeptcd) {
		this.prnDeptcd = prnDeptcd;
	}
	public String getSetCode() {
		return setCode;
	}
	public void setSetCode(String setCode) {
		this.setCode = setCode;
	}
	public String getSetSeqn() {
		return setSeqn;
	}
	public void setSetSeqn(String setSeqn) {
		this.setSeqn = setSeqn;
	}
	public Integer getExecFlag() {
		return execFlag;
	}
	public void setExecFlag(Integer execFlag) {
		this.execFlag = execFlag;
	}
	public Date getExecDate() {
		return execDate;
	}
	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}
	public String getExecUsercd() {
		return execUsercd;
	}
	public void setExecUsercd(String execUsercd) {
		this.execUsercd = execUsercd;
	}
	public String getExecDeptcd() {
		return execDeptcd;
	}
	public void setExecDeptcd(String execDeptcd) {
		this.execDeptcd = execDeptcd;
	}
	public Integer getExecPrnflag() {
		return execPrnflag;
	}
	public void setExecPrnflag(Integer execPrnflag) {
		this.execPrnflag = execPrnflag;
	}
	public Date getExecPrndate() {
		return execPrndate;
	}
	public void setExecPrndate(Date execPrndate) {
		this.execPrndate = execPrndate;
	}
	public String getExecPrnusercd() {
		return execPrnusercd;
	}
	public void setExecPrnusercd(String execPrnusercd) {
		this.execPrnusercd = execPrnusercd;
	}
	public Integer getChargeFlag() {
		return chargeFlag;
	}
	public void setChargeFlag(Integer chargeFlag) {
		this.chargeFlag = chargeFlag;
	}
	public Date getChargeDate() {
		return chargeDate;
	}
	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}
	public String getChargeUsercd() {
		return chargeUsercd;
	}
	public void setChargeUsercd(String chargeUsercd) {
		this.chargeUsercd = chargeUsercd;
	}
	public String getChargeDeptcd() {
		return chargeDeptcd;
	}
	public void setChargeDeptcd(String chargeDeptcd) {
		this.chargeDeptcd = chargeDeptcd;
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
	public String getMoNote1() {
		return moNote1;
	}
	public void setMoNote1(String moNote1) {
		this.moNote1 = moNote1;
	}
	public String getMoNote2() {
		return moNote2;
	}
	public void setMoNote2(String moNote2) {
		this.moNote2 = moNote2;
	}
	public Date getDecoDate() {
		return decoDate;
	}
	public void setDecoDate(Date decoDate) {
		this.decoDate = decoDate;
	}
	public Integer getChargePrnflag() {
		return chargePrnflag;
	}
	public void setChargePrnflag(Integer chargePrnflag) {
		this.chargePrnflag = chargePrnflag;
	}
	public Date getChargePrndate() {
		return chargePrndate;
	}
	public void setChargePrndate(Date chargePrndate) {
		this.chargePrndate = chargePrndate;
	}
	public String getChargePrnusercd() {
		return chargePrnusercd;
	}
	public void setChargePrnusercd(String chargePrnusercd) {
		this.chargePrnusercd = chargePrnusercd;
	}
	public Integer getCircultPrnflag() {
		return circultPrnflag;
	}
	public void setCircultPrnflag(Integer circultPrnflag) {
		this.circultPrnflag = circultPrnflag;
	}
	public Integer getCompoundFlag() {
		return compoundFlag;
	}
	public void setCompoundFlag(Integer compoundFlag) {
		this.compoundFlag = compoundFlag;
	}
	public Integer getCompoundExec() {
		return compoundExec;
	}
	public void setCompoundExec(Integer compoundExec) {
		this.compoundExec = compoundExec;
	}
	public String getCompoundOper() {
		return compoundOper;
	}
	public void setCompoundOper(String compoundOper) {
		this.compoundOper = compoundOper;
	}
	public String getCompoundDept() {
		return compoundDept;
	}
	public void setCompoundDept(String compoundDept) {
		this.compoundDept = compoundDept;
	}
	public Date getCompoundDate() {
		return compoundDate;
	}
	public void setCompoundDate(Date compoundDate) {
		this.compoundDate = compoundDate;
	}
	public InpatientInfoNow getPatient() {
		return patient;
	}
	public void setPatient(InpatientInfoNow patient) {
		this.patient = patient;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public Integer getHomeMadeFlag() {
		return homeMadeFlag;
	}
	public void setHomeMadeFlag(Integer homeMadeFlag) {
		this.homeMadeFlag = homeMadeFlag;
	}
	public Integer getEmcFlag() {
		return emcFlag;
	}
	public void setEmcFlag(Integer emcFlag) {
		this.emcFlag = emcFlag;
	}
}
