package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**  
 *  
 * @Description：医嘱资料档  和床位信息视图
 * @Author：donghe
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class VidOrderBedname extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nurseCellCode1;
	/** 患者床号姓名*/
	private String bedName;
	/** 患者姓名*/
	private String patientName;
	/**住院号*/
	private String inpatientNo;
	/**住院病历号 */
	private String patientNo;
	/**住院科室代码 */
	private String deptCode;
	/**住院护理站代码 */
	private String nurseCellCode;
	/**开立科室代码*/
	private String listDpcd;
	/**医嘱流水号*/
	private String   moOrder;
	/**医嘱医师代号*/
	private String  docCode; 
	/**医嘱医师姓名*/
	private String  docName;
	/**医嘱日期**/
	private Date  moDate;
	/**是否婴儿医嘱,1是/0否*/
	private Integer  babyFlag; 
	/**婴儿序号*/
	private Integer  happenNo;
	/**项目属性,1院内项目/2院外项目*/
	private Integer  setItmattr=1;
	/**是否包含附材,1是/0否*/
	private Integer  setSubtbl; 
	/**医嘱类别代码*/
	private String typeCode;  
	/**医嘱类别名称*/
	private String typeName;
	/**医嘱是否分解:1长期/0临时*/
    private Integer   decmpsState;
    /**是否计费*/
    private Integer   chargeState;
    /**药房是否配药*/
    private Integer   needDrug; 
    /**打印执行单*/
    private Integer   prnExelist;
    /**是否打印医嘱单*/
    private Integer   prmMorlist;
    /**是否需要确认*/
    private Integer   needConfirm; 
    /**项目类别1为药品，2为非药品*/
    private String   itemType;         
    /**项目编码*/
    private String  itemCode;          
    /**项目名称*/
    private String  itemName;          
    /**项目类别代码*/
    private String  classCode;
    /**项目类别名称*/
    private String  className; 
    /**取药药房*/
    private String   pharmacyCode; 
    /**执行科室代码*/
    private String  execDpcd;    
    /**执行科室名称*/
    private String   execDpnm;   
    /**药品基本剂量*/
    private Double   baseDose;  
    /**剂量单位*/
    private String   doseUnit;  
    /**最小单位*/
    private String   minUnit;   
    /**计价单位*/
    private String   priceUnit;
    /**包装数量*/
    private Double  packQty; 
    /**规格*/
    private String  specs;
    /**剂型代码*/
    private String  doseModelCode;
    /**药品类别*/
    private String  drugType;
    /**药品性质*/
    private String  drugQuality;
    /**价格*/
    private Double itemPrice;
    /**组合序号*/
    private String  combNo;
    /**主药标记,1主药，0非主药*/ 
    private Integer  mainDrug;
    /**医嘱状态,0开立，1审核，2执行，3作废，4重整，-1需要上级审核，-3上级审核不通过*/
    private Integer  moStat;
    /**用法代码*/
    private String  usageCode;
    /**用法名称*/
    private String  useName;
    /**用法英文缩写*/
    private String  englishAb;
    /**频次代码*/
    private String  frequencyCode;
    /**频次名称*/
    private String  frequencyName;
    /**每次剂量*/
    private Double doseOnce;
    /**1扣护士站常备药/2扣药房*/
    private Integer stocKin;
    /**项目总量*/
    private Double qtyTot;
    /**付数*/
    private Integer useDays;
    /**开始时间*/
    private Date dateBgn;
    /**结束时间*/
    private Date dateEnd;
    /**录入人员代码*/
   	private String  recUsercd;
   	/**录入人员姓名*/
   	private String  recUsernm; 
   	/**确认标记,0未确认/1已*/
    private Integer confirmFlag=0;
    /**确认时间*/
    private Date confirmDate;
    /**确认人员代码*/
   	private String  confirmUsercd;
   	/**停止标记 0未停止/1已停止*/
    private Integer dcFlag;
    /**停止时间*/
   	private Date dcDate;
   	/**停止原因代码*/
   	private String  dcCode;
   	/**停止原因名称*/
   	private String  dcName;
   	/**停止医师代码*/
   	private String  dcDoccd;
   	/**停止医师姓名*/
   	private String  dcDocnm;
   	/**停止人员代码*/
   	private String  dcUsercd;
   	/**停止人员名称*/
   	private String  dcUsernm;
   	/**执行标记 0未执行/1已执行*/
   	private Integer executeFlag;
   	/**执行时间*/
   	private Date executeDate;
   	/**执行人员代码*/
	private String  executeUsercd;
	/**整档标记 0无/1有*/
	private Integer  decoFlag;
	/**本次分解时间*/
	private Date  dateCurmodc;
	/**下次分解时间*/
	private Date  dateNxtmodc;
	/**医嘱说明*/
	private String  moNote1;
	/**备注*/
	private String  moNote2;
	/**1不需要皮试/2需要皮试，未做/3皮试阳/4皮试阴*/
	private Integer  hypotest;
	/**检查部位检体*/
   	private String  itemNote;
   	/**申请单号*/
   	private String  applyNo;
   	/**加急标记: 0普通/1加急*/
   	private Integer  emcFlag;
   	/**医嘱提取标记: 0待提取/1已提取/2DC提取*/
   	private Integer  getFlag;
   	/**是否附材''1''是*/
   	private Integer  subtblFlag;
   	/**排列序号，按排列序号由大到小顺序显示医嘱*/
   	private Integer  sortId;
   	/**DC审核时间*/
   	private Date  dcConfirmDate;
   	/**DC审核人*/
   	private String  dcConfirmOper;
   	/**DC审核标记，0未审核，1已审核*/
   	private Integer  dcConfirmFlag;
   	/**样本类型 名称*/
  	private String  labCode;
  	/**是否需要患者同意 0 不需要  1需要*/
  	private Integer  permission;
  	/**组套编码*/
   	private String  packageCode;
   	/**组套名称*/
   	private String  packageName;
   	/**扩展备注  [执行时间]*/
   	private String  mark1;
   	/**扩展备注1*/
   	private String  mark2;
   	/**重整医嘱*/
   	private String  mark3; 
   	/**执行时间点[特殊频次]*/
   	private String  execTimes;
   	/**执行剂量[特殊频次]*/
   	private String  execDose;
   	/**包装单位*/
   	private String drugpackagingUnit;
   	/**患者信息*/
   	private InpatientInfo patient;
   	/**执行医嘱流水号*/
   	private String execId;
   	/**是否收费*/
   	private Integer ifFee;
   	/**最小费用*/
   	private String feeCode;
   	/**附材医嘱对应主医嘱类型 1药品；2非药品*/
   	private String mainType;
   	/**临时项目总量*/
	 private Double tempQtyTot; 
	 
	public String getNurseCellCode1() {
		return nurseCellCode1;
	}
	public void setNurseCellCode1(String nurseCellCode1) {
		this.nurseCellCode1 = nurseCellCode1;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
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
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
	public Integer getPrmMorlist() {
		return prmMorlist;
	}
	public void setPrmMorlist(Integer prmMorlist) {
		this.prmMorlist = prmMorlist;
	}
	public Integer getNeedConfirm() {
		return needConfirm;
	}
	public void setNeedConfirm(Integer needConfirm) {
		this.needConfirm = needConfirm;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
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
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
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
	public String getExecDpnm() {
		return execDpnm;
	}
	public void setExecDpnm(String execDpnm) {
		this.execDpnm = execDpnm;
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
	public Double getPackQty() {
		return packQty;
	}
	public void setPackQty(Double packQty) {
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
	public Integer getMoStat() {
		return moStat;
	}
	public void setMoStat(Integer moStat) {
		this.moStat = moStat;
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
	public Integer getStocKin() {
		return stocKin;
	}
	public void setStocKin(Integer stocKin) {
		this.stocKin = stocKin;
	}
	public Double getQtyTot() {
		return qtyTot;
	}
	public void setQtyTot(Double qtyTot) {
		this.qtyTot = qtyTot;
	}
	public Integer getUseDays() {
		return useDays;
	}
	public void setUseDays(Integer useDays) {
		this.useDays = useDays;
	}
	
	public Date getDateBgn() {
		return dateBgn;
	}
	public void setDateBgn(Date dateBgn) {
		this.dateBgn = dateBgn;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getRecUsercd() {
		return recUsercd;
	}
	public void setRecUsercd(String recUsercd) {
		this.recUsercd = recUsercd;
	}
	public String getRecUsernm() {
		return recUsernm;
	}
	public void setRecUsernm(String recUsernm) {
		this.recUsernm = recUsernm;
	}
	public Integer getConfirmFlag() {
		return confirmFlag;
	}
	public void setConfirmFlag(Integer confirmFlag) {
		this.confirmFlag = confirmFlag;
		if(confirmFlag==null){
			this.confirmFlag=0;
		}
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getConfirmUsercd() {
		return confirmUsercd;
	}
	public void setConfirmUsercd(String confirmUsercd) {
		this.confirmUsercd = confirmUsercd;
	}
	public Integer getDcFlag() {
		return dcFlag;
	}
	public void setDcFlag(Integer dcFlag) {
		this.dcFlag = dcFlag;
	}
	public Date getDcDate() {
		return dcDate;
	}
	public void setDcDate(Date dcDate) {
		this.dcDate = dcDate;
	}
	public String getDcCode() {
		return dcCode;
	}
	public void setDcCode(String dcCode) {
		this.dcCode = dcCode;
	}
	public String getDcName() {
		return dcName;
	}
	public void setDcName(String dcName) {
		this.dcName = dcName;
	}
	public String getDcDoccd() {
		return dcDoccd;
	}
	public void setDcDoccd(String dcDoccd) {
		this.dcDoccd = dcDoccd;
	}
	public String getDcDocnm() {
		return dcDocnm;
	}
	public void setDcDocnm(String dcDocnm) {
		this.dcDocnm = dcDocnm;
	}
	public String getDcUsercd() {
		return dcUsercd;
	}
	public void setDcUsercd(String dcUsercd) {
		this.dcUsercd = dcUsercd;
	}
	public String getDcUsernm() {
		return dcUsernm;
	}
	public void setDcUsernm(String dcUsernm) {
		this.dcUsernm = dcUsernm;
	}
	public Integer getExecuteFlag() {
		return executeFlag;
	}
	public void setExecuteFlag(Integer executeFlag) {
		this.executeFlag = executeFlag;
	}
	public Date getExecuteDate() {
		return executeDate;
	}
	public void setExecuteDate(Date executeDate) {
		this.executeDate = executeDate;
	}
	public String getExecuteUsercd() {
		return executeUsercd;
	}
	public void setExecuteUsercd(String executeUsercd) {
		this.executeUsercd = executeUsercd;
	}
	public Integer getDecoFlag() {
		return decoFlag;
	}
	public void setDecoFlag(Integer decoFlag) {
		this.decoFlag = decoFlag;
	}
	public Date getDateCurmodc() {
		return dateCurmodc;
	}
	public void setDateCurmodc(Date dateCurmodc) {
		this.dateCurmodc = dateCurmodc;
	}
	public Date getDateNxtmodc() {
		return dateNxtmodc;
	}
	public void setDateNxtmodc(Date dateNxtmodc) {
		this.dateNxtmodc = dateNxtmodc;
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
	public Integer getHypotest() {
		return hypotest;
	}
	public void setHypotest(Integer hypotest) {
		this.hypotest = hypotest;
	}
	public String getItemNote() {
		return itemNote;
	}
	public void setItemNote(String itemNote) {
		this.itemNote = itemNote;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public Integer getEmcFlag() {
		return emcFlag;
	}
	public void setEmcFlag(Integer emcFlag) {
		this.emcFlag = emcFlag;
	}
	public Integer getGetFlag() {
		return getFlag;
	}
	public void setGetFlag(Integer getFlag) {
		this.getFlag = getFlag;
	}
	public Integer getSubtblFlag() {
		return subtblFlag;
	}
	public void setSubtblFlag(Integer subtblFlag) {
		this.subtblFlag = subtblFlag;
	}
	public Integer getSortId() {
		return sortId;
	}
	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}
	public Date getDcConfirmDate() {
		return dcConfirmDate;
	}
	public void setDcConfirmDate(Date dcConfirmDate) {
		this.dcConfirmDate = dcConfirmDate;
	}
	public String getDcConfirmOper() {
		return dcConfirmOper;
	}
	public void setDcConfirmOper(String dcConfirmOper) {
		this.dcConfirmOper = dcConfirmOper;
	}
	public Integer getDcConfirmFlag() {
		return dcConfirmFlag;
	}
	public void setDcConfirmFlag(Integer dcConfirmFlag) {
		this.dcConfirmFlag = dcConfirmFlag;
		if(dcConfirmFlag==null){
			this.dcConfirmFlag=0;
		}
	}
	public String getLabCode() {
		return labCode;
	}
	public void setLabCode(String labCode) {
		this.labCode = labCode;
	}
	public Integer getPermission() {
		return permission;
	}
	public void setPermission(Integer permission) {
		this.permission = permission;
	}
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getMark1() {
		return mark1;
	}
	public void setMark1(String mark1) {
		this.mark1 = mark1;
	}
	public String getMark2() {
		return mark2;
	}
	public void setMark2(String mark2) {
		this.mark2 = mark2;
	}
	public String getMark3() {
		return mark3;
	}
	public void setMark3(String mark3) {
		this.mark3 = mark3;
	}
	public String getExecTimes() {
		return execTimes;
	}
	public void setExecTimes(String execTimes) {
		this.execTimes = execTimes;
	}
	public String getExecDose() {
		return execDose;
	}
	public void setExecDose(String execDose) {
		this.execDose = execDose;
	}
	public String getDrugpackagingUnit() {
		return drugpackagingUnit;
	}
	public void setDrugpackagingUnit(String drugpackagingUnit) {
		this.drugpackagingUnit = drugpackagingUnit;
	} 	
	public InpatientInfo getPatient() {
		return patient;
	}
	public void setPatient(InpatientInfo patient) {
		this.patient = patient;
	}
	public String getExecId() {
		return execId;
	}
	public void setExecId(String execId) {
		this.execId = execId;
	}
	public Integer getIfFee() {
		return ifFee;
	}
	public void setIfFee(Integer ifFee) {
		this.ifFee = ifFee;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public String getMainType() {
		return mainType;
	}
	public void setMainType(String mainType) {
		this.mainType = mainType;
	}
	public Double getTempQtyTot() {
		return tempQtyTot;
	}
	public void setTempQtyTot(Double tempQtyTot) {
		this.tempQtyTot = tempQtyTot;
	}
}
