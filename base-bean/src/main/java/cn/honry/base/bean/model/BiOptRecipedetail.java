package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiOptRecipedetail entity. @author MyEclipse Persistence Tools
 */

public class BiOptRecipedetail implements java.io.Serializable {

	// Fields

	private String sequenceNo;
	private String recipeNo;
	private String seeNo;
	private String clinicCode;
	private String cardNo;
	private Date regDate;
	private String regDeptCode;
	private String regDept;
	private String itemCode;
	private String itemName;
	private String specs;
	private String drugFlag;
	private String classCode;
	private String className;
	private String miniFeeCode;
	private String miniFeeName;
	private Double unitPrice;
	private Double qty;
	private Short days;
	private Short packQty;
	private String priceUnitCode;
	private String priceUnit;
	private Double ownCost;
	private Double payCost;
	private Double pubCost;
	private Double totCost;
	private Double baseDose;
	private String selfMade;
	private String drugQuanlityCode;
	private String drugQuanlity;
	private Double onceDose;
	private String onceUnitCode;
	private String onceUnit;
	private String doseModelCode;
	private String doseModelName;
	private String frequencyCode;
	private String frequencyName;
	private String usageCode;
	private String usageName;
	private String execDpcd;
	private String execDept;
	private String mainDrug;
	private String combNo;
	private String hypotest;
	private Byte injectNumber;
	private String remark;
	private String doctCode;
	private String doctName;
	private String doctDpcd;
	private String doctDept;
	private Date operDate;
	private String status;
	private String cancelUserid;
	private Date cancelDate;
	private String emcFlag;
	private String labTypeCode;
	private String labType;
	private String checkBodyCode;
	private String checkBody;
	private String applyNo;
	private String subtblFlag;
	private String needConfirm;
	private String confirmCode;
	private String confirmDecd;
	private String confirmDept;
	private Date confirmDate;
	private String chargeFlag;
	private String chargeCode;
	private Date chargeDate;
	private Short recipeSeq;
	private String recipeFeeseq;
	private String phamarcyCode;
	private String phamarcyDept;
	private String minunitFlag;
	private Long sortorder;
	private String printFlag;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;

	// Constructors

	/** default constructor */
	public BiOptRecipedetail() {
	}

	/** full constructor */
	public BiOptRecipedetail(String recipeNo, String seeNo, String clinicCode,
			String cardNo, Date regDate, String regDeptCode, String regDept,
			String itemCode, String itemName, String specs, String drugFlag,
			String classCode, String className, String miniFeeCode,
			String miniFeeName, Double unitPrice, Double qty, Short days,
			Short packQty, String priceUnitCode, String priceUnit,
			Double ownCost, Double payCost, Double pubCost, Double totCost,
			Double baseDose, String selfMade, String drugQuanlityCode,
			String drugQuanlity, Double onceDose, String onceUnitCode,
			String onceUnit, String doseModelCode, String doseModelName,
			String frequencyCode, String frequencyName, String usageCode,
			String usageName, String execDpcd, String execDept,
			String mainDrug, String combNo, String hypotest, Byte injectNumber,
			String remark, String doctCode, String doctName, String doctDpcd,
			String doctDept, Date operDate, String status, String cancelUserid,
			Date cancelDate, String emcFlag, String labTypeCode,
			String labType, String checkBodyCode, String checkBody,
			String applyNo, String subtblFlag, String needConfirm,
			String confirmCode, String confirmDecd, String confirmDept,
			Date confirmDate, String chargeFlag, String chargeCode,
			Date chargeDate, Short recipeSeq, String recipeFeeseq,
			String phamarcyCode, String phamarcyDept, String minunitFlag,
			Long sortorder, String printFlag, String ext1, String ext2,
			String ext3, String ext4) {
		this.recipeNo = recipeNo;
		this.seeNo = seeNo;
		this.clinicCode = clinicCode;
		this.cardNo = cardNo;
		this.regDate = regDate;
		this.regDeptCode = regDeptCode;
		this.regDept = regDept;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.specs = specs;
		this.drugFlag = drugFlag;
		this.classCode = classCode;
		this.className = className;
		this.miniFeeCode = miniFeeCode;
		this.miniFeeName = miniFeeName;
		this.unitPrice = unitPrice;
		this.qty = qty;
		this.days = days;
		this.packQty = packQty;
		this.priceUnitCode = priceUnitCode;
		this.priceUnit = priceUnit;
		this.ownCost = ownCost;
		this.payCost = payCost;
		this.pubCost = pubCost;
		this.totCost = totCost;
		this.baseDose = baseDose;
		this.selfMade = selfMade;
		this.drugQuanlityCode = drugQuanlityCode;
		this.drugQuanlity = drugQuanlity;
		this.onceDose = onceDose;
		this.onceUnitCode = onceUnitCode;
		this.onceUnit = onceUnit;
		this.doseModelCode = doseModelCode;
		this.doseModelName = doseModelName;
		this.frequencyCode = frequencyCode;
		this.frequencyName = frequencyName;
		this.usageCode = usageCode;
		this.usageName = usageName;
		this.execDpcd = execDpcd;
		this.execDept = execDept;
		this.mainDrug = mainDrug;
		this.combNo = combNo;
		this.hypotest = hypotest;
		this.injectNumber = injectNumber;
		this.remark = remark;
		this.doctCode = doctCode;
		this.doctName = doctName;
		this.doctDpcd = doctDpcd;
		this.doctDept = doctDept;
		this.operDate = operDate;
		this.status = status;
		this.cancelUserid = cancelUserid;
		this.cancelDate = cancelDate;
		this.emcFlag = emcFlag;
		this.labTypeCode = labTypeCode;
		this.labType = labType;
		this.checkBodyCode = checkBodyCode;
		this.checkBody = checkBody;
		this.applyNo = applyNo;
		this.subtblFlag = subtblFlag;
		this.needConfirm = needConfirm;
		this.confirmCode = confirmCode;
		this.confirmDecd = confirmDecd;
		this.confirmDept = confirmDept;
		this.confirmDate = confirmDate;
		this.chargeFlag = chargeFlag;
		this.chargeCode = chargeCode;
		this.chargeDate = chargeDate;
		this.recipeSeq = recipeSeq;
		this.recipeFeeseq = recipeFeeseq;
		this.phamarcyCode = phamarcyCode;
		this.phamarcyDept = phamarcyDept;
		this.minunitFlag = minunitFlag;
		this.sortorder = sortorder;
		this.printFlag = printFlag;
		this.ext1 = ext1;
		this.ext2 = ext2;
		this.ext3 = ext3;
		this.ext4 = ext4;
	}

	// Property accessors

	public String getSequenceNo() {
		return this.sequenceNo;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getRecipeNo() {
		return this.recipeNo;
	}

	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}

	public String getSeeNo() {
		return this.seeNo;
	}

	public void setSeeNo(String seeNo) {
		this.seeNo = seeNo;
	}

	public String getClinicCode() {
		return this.clinicCode;
	}

	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Date getRegDate() {
		return this.regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getRegDeptCode() {
		return this.regDeptCode;
	}

	public void setRegDeptCode(String regDeptCode) {
		this.regDeptCode = regDeptCode;
	}

	public String getRegDept() {
		return this.regDept;
	}

	public void setRegDept(String regDept) {
		this.regDept = regDept;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSpecs() {
		return this.specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public String getDrugFlag() {
		return this.drugFlag;
	}

	public void setDrugFlag(String drugFlag) {
		this.drugFlag = drugFlag;
	}

	public String getClassCode() {
		return this.classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMiniFeeCode() {
		return this.miniFeeCode;
	}

	public void setMiniFeeCode(String miniFeeCode) {
		this.miniFeeCode = miniFeeCode;
	}

	public String getMiniFeeName() {
		return this.miniFeeName;
	}

	public void setMiniFeeName(String miniFeeName) {
		this.miniFeeName = miniFeeName;
	}

	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getQty() {
		return this.qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public Short getDays() {
		return this.days;
	}

	public void setDays(Short days) {
		this.days = days;
	}

	public Short getPackQty() {
		return this.packQty;
	}

	public void setPackQty(Short packQty) {
		this.packQty = packQty;
	}

	public String getPriceUnitCode() {
		return this.priceUnitCode;
	}

	public void setPriceUnitCode(String priceUnitCode) {
		this.priceUnitCode = priceUnitCode;
	}

	public String getPriceUnit() {
		return this.priceUnit;
	}

	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}

	public Double getOwnCost() {
		return this.ownCost;
	}

	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}

	public Double getPayCost() {
		return this.payCost;
	}

	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}

	public Double getPubCost() {
		return this.pubCost;
	}

	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}

	public Double getTotCost() {
		return this.totCost;
	}

	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}

	public Double getBaseDose() {
		return this.baseDose;
	}

	public void setBaseDose(Double baseDose) {
		this.baseDose = baseDose;
	}

	public String getSelfMade() {
		return this.selfMade;
	}

	public void setSelfMade(String selfMade) {
		this.selfMade = selfMade;
	}

	public String getDrugQuanlityCode() {
		return this.drugQuanlityCode;
	}

	public void setDrugQuanlityCode(String drugQuanlityCode) {
		this.drugQuanlityCode = drugQuanlityCode;
	}

	public String getDrugQuanlity() {
		return this.drugQuanlity;
	}

	public void setDrugQuanlity(String drugQuanlity) {
		this.drugQuanlity = drugQuanlity;
	}

	public Double getOnceDose() {
		return this.onceDose;
	}

	public void setOnceDose(Double onceDose) {
		this.onceDose = onceDose;
	}

	public String getOnceUnitCode() {
		return this.onceUnitCode;
	}

	public void setOnceUnitCode(String onceUnitCode) {
		this.onceUnitCode = onceUnitCode;
	}

	public String getOnceUnit() {
		return this.onceUnit;
	}

	public void setOnceUnit(String onceUnit) {
		this.onceUnit = onceUnit;
	}

	public String getDoseModelCode() {
		return this.doseModelCode;
	}

	public void setDoseModelCode(String doseModelCode) {
		this.doseModelCode = doseModelCode;
	}

	public String getDoseModelName() {
		return this.doseModelName;
	}

	public void setDoseModelName(String doseModelName) {
		this.doseModelName = doseModelName;
	}

	public String getFrequencyCode() {
		return this.frequencyCode;
	}

	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}

	public String getFrequencyName() {
		return this.frequencyName;
	}

	public void setFrequencyName(String frequencyName) {
		this.frequencyName = frequencyName;
	}

	public String getUsageCode() {
		return this.usageCode;
	}

	public void setUsageCode(String usageCode) {
		this.usageCode = usageCode;
	}

	public String getUsageName() {
		return this.usageName;
	}

	public void setUsageName(String usageName) {
		this.usageName = usageName;
	}

	public String getExecDpcd() {
		return this.execDpcd;
	}

	public void setExecDpcd(String execDpcd) {
		this.execDpcd = execDpcd;
	}

	public String getExecDept() {
		return this.execDept;
	}

	public void setExecDept(String execDept) {
		this.execDept = execDept;
	}

	public String getMainDrug() {
		return this.mainDrug;
	}

	public void setMainDrug(String mainDrug) {
		this.mainDrug = mainDrug;
	}

	public String getCombNo() {
		return this.combNo;
	}

	public void setCombNo(String combNo) {
		this.combNo = combNo;
	}

	public String getHypotest() {
		return this.hypotest;
	}

	public void setHypotest(String hypotest) {
		this.hypotest = hypotest;
	}

	public Byte getInjectNumber() {
		return this.injectNumber;
	}

	public void setInjectNumber(Byte injectNumber) {
		this.injectNumber = injectNumber;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDoctCode() {
		return this.doctCode;
	}

	public void setDoctCode(String doctCode) {
		this.doctCode = doctCode;
	}

	public String getDoctName() {
		return this.doctName;
	}

	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}

	public String getDoctDpcd() {
		return this.doctDpcd;
	}

	public void setDoctDpcd(String doctDpcd) {
		this.doctDpcd = doctDpcd;
	}

	public String getDoctDept() {
		return this.doctDept;
	}

	public void setDoctDept(String doctDept) {
		this.doctDept = doctDept;
	}

	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCancelUserid() {
		return this.cancelUserid;
	}

	public void setCancelUserid(String cancelUserid) {
		this.cancelUserid = cancelUserid;
	}

	public Date getCancelDate() {
		return this.cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getEmcFlag() {
		return this.emcFlag;
	}

	public void setEmcFlag(String emcFlag) {
		this.emcFlag = emcFlag;
	}

	public String getLabTypeCode() {
		return this.labTypeCode;
	}

	public void setLabTypeCode(String labTypeCode) {
		this.labTypeCode = labTypeCode;
	}

	public String getLabType() {
		return this.labType;
	}

	public void setLabType(String labType) {
		this.labType = labType;
	}

	public String getCheckBodyCode() {
		return this.checkBodyCode;
	}

	public void setCheckBodyCode(String checkBodyCode) {
		this.checkBodyCode = checkBodyCode;
	}

	public String getCheckBody() {
		return this.checkBody;
	}

	public void setCheckBody(String checkBody) {
		this.checkBody = checkBody;
	}

	public String getApplyNo() {
		return this.applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public String getSubtblFlag() {
		return this.subtblFlag;
	}

	public void setSubtblFlag(String subtblFlag) {
		this.subtblFlag = subtblFlag;
	}

	public String getNeedConfirm() {
		return this.needConfirm;
	}

	public void setNeedConfirm(String needConfirm) {
		this.needConfirm = needConfirm;
	}

	public String getConfirmCode() {
		return this.confirmCode;
	}

	public void setConfirmCode(String confirmCode) {
		this.confirmCode = confirmCode;
	}

	public String getConfirmDecd() {
		return this.confirmDecd;
	}

	public void setConfirmDecd(String confirmDecd) {
		this.confirmDecd = confirmDecd;
	}

	public String getConfirmDept() {
		return this.confirmDept;
	}

	public void setConfirmDept(String confirmDept) {
		this.confirmDept = confirmDept;
	}

	public Date getConfirmDate() {
		return this.confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getChargeFlag() {
		return this.chargeFlag;
	}

	public void setChargeFlag(String chargeFlag) {
		this.chargeFlag = chargeFlag;
	}

	public String getChargeCode() {
		return this.chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public Date getChargeDate() {
		return this.chargeDate;
	}

	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}

	public Short getRecipeSeq() {
		return this.recipeSeq;
	}

	public void setRecipeSeq(Short recipeSeq) {
		this.recipeSeq = recipeSeq;
	}

	public String getRecipeFeeseq() {
		return this.recipeFeeseq;
	}

	public void setRecipeFeeseq(String recipeFeeseq) {
		this.recipeFeeseq = recipeFeeseq;
	}

	public String getPhamarcyCode() {
		return this.phamarcyCode;
	}

	public void setPhamarcyCode(String phamarcyCode) {
		this.phamarcyCode = phamarcyCode;
	}

	public String getPhamarcyDept() {
		return this.phamarcyDept;
	}

	public void setPhamarcyDept(String phamarcyDept) {
		this.phamarcyDept = phamarcyDept;
	}

	public String getMinunitFlag() {
		return this.minunitFlag;
	}

	public void setMinunitFlag(String minunitFlag) {
		this.minunitFlag = minunitFlag;
	}

	public Long getSortorder() {
		return this.sortorder;
	}

	public void setSortorder(Long sortorder) {
		this.sortorder = sortorder;
	}

	public String getPrintFlag() {
		return this.printFlag;
	}

	public void setPrintFlag(String printFlag) {
		this.printFlag = printFlag;
	}

	public String getExt1() {
		return this.ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return this.ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return this.ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return this.ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

}