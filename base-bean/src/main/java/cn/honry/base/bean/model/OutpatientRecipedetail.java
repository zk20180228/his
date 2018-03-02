package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * TOutpatientRecipedetail entity. @author MyEclipse Persistence Tools
 */

public class OutpatientRecipedetail extends Entity{

	private static final long serialVersionUID = 515664436087947175L;
	
	/**看诊序号**/
	private String seeNo;
	/**父节点id**/
	private String _parentId;
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
	/**1药品，2非药品**/
	private Integer drugFlag;
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
	/**作废人**/
	private String cancelUserid;
	/**作废时间**/
	private Date cancelDate;
	/**加急标记**/
	private Integer emcFlag;
	/**样本类型**/
	private String labType;
	/**检体**/
	private String checkBody;
	/**申请单号**/
	private String applyNo;
	/**附材**/
	private Integer subtblFlag;
	/**是否需要确认**/
	private Integer needConfirm;
	/**确认人**/
	private String confirmCode;
	/**确认科室**/
	private String confirmDept;
	/**确认时间**/
	private Date confirmDate;
	/**0未收费/1收费**/
	private Integer chargeFlag;
	/**收费员**/
	private String chargeCode;
	/**收费时间**/
	private Date chargeDate;
	/**处方号**/
	private String recipeNo;
	/**发药药房**/
	private String phamarcyCode;
	/**开立单位 1 包装单位0是最小单位**/
	private Integer minunitFlag;
	/**排序**/
	private Integer dataorder;
	/**处方打印标志 **/
	private Integer printFlag;
	
	
	/**项目流水号 **/
	private String sequencenNo;
	/**收费序列 **/
	private String recipeFeeseq;
	/**处方内流水号 **/
	private Integer recipeSeq;
	/**审核标记：0无需审核1待审核2通过3未通过**/
	private Integer auditFlg;
	/**审核意见 **/
	private String auditRemark;
	/**挂号科室名称**/
	private String regDeptName;
	/**系统类别名称**/
	private String className;
	/**最小费用名称**/
	private String feeName;
	/**每次用量单位名称**/
	private String onceUnitName;
	/**剂型名称**/
	private String doseModelName;
	/**频次名称**/
	private String frequencyName;
	/**用法名称**/
	private String usageName;
	/**执行科室名称**/
	private String execDpcdName;
	/**开立医生姓名**/
	private String doctName;
	/**医生科室名称**/
	private String doctDpcdName;
	/**医院编码*/
	private Integer hospitalId;
	/**院区编码*/
	private String areaCode;
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
	public Integer getDrugFlag() {
		return drugFlag;
	}
	public void setDrugFlag(Integer drugFlag) {
		this.drugFlag = drugFlag;
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
	public String getCancelUserid() {
		return cancelUserid;
	}
	public void setCancelUserid(String cancelUserid) {
		this.cancelUserid = cancelUserid;
	}
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	public Integer getEmcFlag() {
		return emcFlag;
	}
	public void setEmcFlag(Integer emcFlag) {
		this.emcFlag = emcFlag;
	}
	public String getLabType() {
		return labType;
	}
	public void setLabType(String labType) {
		this.labType = labType;
	}
	public String getCheckBody() {
		return checkBody;
	}
	public void setCheckBody(String checkBody) {
		this.checkBody = checkBody;
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
	public void add(OutpatientRecipedetail node) {
		
	}
	public void set_parentId(String _parentId) {
		this._parentId = _parentId;
	}
	public String get_parentId() {
		return _parentId;
	}
	public String getSequencenNo() {
		return sequencenNo;
	}
	public void setSequencenNo(String sequencenNo) {
		this.sequencenNo = sequencenNo;
	}
	public String getRecipeFeeseq() {
		return recipeFeeseq;
	}
	public void setRecipeFeeseq(String recipeFeeseq) {
		this.recipeFeeseq = recipeFeeseq;
	}
	public Integer getRecipeSeq() {
		return recipeSeq;
	}
	public void setRecipeSeq(Integer recipeSeq) {
		this.recipeSeq = recipeSeq;
	}
	public Integer getAuditFlg() {
		return auditFlg;
	}
	public void setAuditFlg(Integer auditFlg) {
		this.auditFlg = auditFlg;
	}
	public String getAuditRemark() {
		return auditRemark;
	}
	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}
	public Double getOnceDose() {
		return onceDose;
	}
	public void setOnceDose(Double onceDose) {
		this.onceDose = onceDose;
	}
	public String getRegDeptName() {
		return regDeptName;
	}
	public void setRegDeptName(String regDeptName) {
		this.regDeptName = regDeptName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getFeeName() {
		return feeName;
	}
	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}
	public String getOnceUnitName() {
		return onceUnitName;
	}
	public void setOnceUnitName(String onceUnitName) {
		this.onceUnitName = onceUnitName;
	}
	public String getDoseModelName() {
		return doseModelName;
	}
	public void setDoseModelName(String doseModelName) {
		this.doseModelName = doseModelName;
	}
	public String getFrequencyName() {
		return frequencyName;
	}
	public void setFrequencyName(String frequencyName) {
		this.frequencyName = frequencyName;
	}
	public String getUsageName() {
		return usageName;
	}
	public void setUsageName(String usageName) {
		this.usageName = usageName;
	}
	public String getExecDpcdName() {
		return execDpcdName;
	}
	public void setExecDpcdName(String execDpcdName) {
		this.execDpcdName = execDpcdName;
	}
	public String getDoctName() {
		return doctName;
	}
	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}
	public String getDoctDpcdName() {
		return doctDpcdName;
	}
	public void setDoctDpcdName(String doctDpcdName) {
		this.doctDpcdName = doctDpcdName;
	}
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
	
}