package cn.honry.inner.vo;

import java.util.Date;


/**  
 *  
 * @className：MedicalVo
 * @Description：  
 * @Author：ldl
 * @CreateDate：2016-01-28
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class MedicalVo {
	
	private String type;//类型
	private String adviceType;//医嘱类型
	private String adviceId;//医嘱名称Id
	private String adviceName;//医嘱名称
	private String adviceNameView;//医嘱名称
	private Double adPrice=0.00;//价格
	private String adPackUnitHid;//包装单位
	private String adMinUnitHid;//单位
	private String adDosaUnitHid;//剂量
	private Double adDrugBasiHid;//基本剂量
	private String specs;//规格
	private String sysType;//系统类别
	private String drugType;//药品类别
	private String minimumcost;//最小费用代码
	private Integer packagingnum;//包装数量
	private String nature;//药品性质
	private Integer ismanufacture;//自制药标志
	private String dosageform;//剂型
	private Integer isInformedconsent;//是否知情同意书
	private String group;//组
	private Double totalNum=1d;//总量
	private String totalUnitHid;//总单位Id
	private String totalUnit;//总单位
	private Double dosageHid;//每次用量
	private String dosage;//每次用量
	private String unit;//单位 药品为包装单位/最小单位，根据处方明细中的extFlag3进行判断；非药品为汉字
	private Integer setNum=1;//服数
	private String frequencyHid;//频次编码Id
	private String frequency;//频次编码
	private String usageNameHid;////用法名称Id
	private String usageName;//用法名称
	private Integer injectionNum;//院注次数
	private String openDoctor;//开立医生
	private String executiveDeptHid;//执行科室
	private String executiveDept;//执行科室
	private Integer isUrgentHid;//加急
	private String isUrgent;//加急
	private String inspectPartId;//检查部位Id
	private String inspectPart;//检查部位
	private String sampleTeptHid;//样本类型
	private String sampleTept;//样本类型
	private String minusDeptHid;//扣库科室
	private String minusDept;//扣库科室
	private String remark;//备注
	private String inputPeop;//录入人
	private String openDept;//开立科室
	private Date startTime;//开立时间
	private Date endTime;//停止时间
	private String stopPeop;//停止人
	private Integer isSkinHid;//是否需要皮试
	private String isSkin;//是否需要皮试
	private String recipeNo;
	private String drugFlag;//分类（1药品，2非药品）
	private String FeedetailId;//明细表Id
	private Double adPriceSum;
	private String feeCode;//最小费用代码
	private Integer subjobFlag;//附材标志
	private String extendOne;//拓展标记
	private String moOrder;//医嘱流水号
	private String feeStatCode;//虚拟字段 -- 统计大类代码
	private String feeStatName;//虚拟字段 -- 统计大类名称
	private String follow;//虚拟字段 -- 跟随
	private String stust;//虚拟字段 -- 状态
	private String recipeSeq;//收费序列
	/**确认人**/
	private String confirmCode;
	/**确认科室**/
	private String confirmDept;
	/**确认时间**/
	private Date confirmDate;
	/**确认数量**/
	private Double confirmNum;
	/**是否需要确认**/
	private Integer issubmit;
	/**是否确认**/
	private Integer confirmFlag;
	//新增字段--医保相关的费用  2016-11-23 16:14:53
	/**可报效金额[36]**/
	private Double pubCost;
	/**优惠金额[58]**/
	private Double ecoCost;
	/**超标金额**/
	private Double overCost;
	/**药品超标金额**/
	private Double excessCost;
	/**自费药金额**/
	private Double drugOwncost;
	/**自付金额[37]**/
	private Double payCost;
	/**1 包装 单位 0, 最小单位;渲染单位时使用**/
	private Integer extFlag3;
	
	
	public Integer getExtFlag3() {
		return extFlag3;
	}
	public void setExtFlag3(Integer extFlag3) {
		this.extFlag3 = extFlag3;
	}
	public Double getPubCost() {
		return pubCost;
	}
	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}
	public Double getEcoCost() {
		return ecoCost;
	}
	public void setEcoCost(Double ecoCost) {
		this.ecoCost = ecoCost;
	}
	public Double getOverCost() {
		return overCost;
	}
	public void setOverCost(Double overCost) {
		this.overCost = overCost;
	}
	public Double getExcessCost() {
		return excessCost;
	}
	public void setExcessCost(Double excessCost) {
		this.excessCost = excessCost;
	}
	public Double getDrugOwncost() {
		return drugOwncost;
	}
	public void setDrugOwncost(Double drugOwncost) {
		this.drugOwncost = drugOwncost;
	}
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}
	public String getRecipeSeq() {
		return recipeSeq;
	}
	public void setRecipeSeq(String recipeSeq) {
		this.recipeSeq = recipeSeq;
	}
	public String getFeeStatName() {
		return feeStatName;
	}
	public void setFeeStatName(String feeStatName) {
		this.feeStatName = feeStatName;
	}
	public String getStust() {
		return stust;
	}
	public void setStust(String stust) {
		this.stust = stust;
	}
	public String getFeeStatCode() {
		return feeStatCode;
	}
	public void setFeeStatCode(String feeStatCode) {
		this.feeStatCode = feeStatCode;
	}
	public String getFollow() {
		return follow;
	}
	public void setFollow(String follow) {
		this.follow = follow;
	}
	public String getMoOrder() {
		return moOrder;
	}
	public void setMoOrder(String moOrder) {
		this.moOrder = moOrder;
	}
	public String getExtendOne() {
		return extendOne;
	}
	public void setExtendOne(String extendOne) {
		this.extendOne = extendOne;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public Double getAdPriceSum() {
		return adPriceSum;
	}
	public void setAdPriceSum(Double adPriceSum) {
		this.adPriceSum = adPriceSum;
	}
	public String getFeedetailId() {
		return FeedetailId;
	}
	public void setFeedetailId(String feedetailId) {
		FeedetailId = feedetailId;
	}
	public String getDrugFlag() {
		return drugFlag;
	}
	public void setDrugFlag(String drugFlag) {
		this.drugFlag = drugFlag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAdviceType() {
		return adviceType;
	}
	public void setAdviceType(String adviceType) {
		this.adviceType = adviceType;
	}
	public String getAdviceName() {
		return adviceName;
	}
	public void setAdviceName(String adviceName) {
		this.adviceName = adviceName;
	}
	public String getAdPackUnitHid() {
		return adPackUnitHid;
	}
	public void setAdPackUnitHid(String adPackUnitHid) {
		this.adPackUnitHid = adPackUnitHid;
	}
	public String getAdMinUnitHid() {
		return adMinUnitHid;
	}
	public void setAdMinUnitHid(String adMinUnitHid) {
		this.adMinUnitHid = adMinUnitHid;
	}
	public String getAdDosaUnitHid() {
		return adDosaUnitHid;
	}
	public void setAdDosaUnitHid(String adDosaUnitHid) {
		this.adDosaUnitHid = adDosaUnitHid;
	}
	public Double getAdDrugBasiHid() {
		return adDrugBasiHid;
	}
	public void setAdDrugBasiHid(Double adDrugBasiHid) {
		this.adDrugBasiHid = adDrugBasiHid;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public Double getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Double totalNum) {
		this.totalNum = totalNum;
	}
	public String getTotalUnitHid() {
		return totalUnitHid;
	}
	public void setTotalUnitHid(String totalUnitHid) {
		this.totalUnitHid = totalUnitHid;
	}
	public String getTotalUnit() {
		return totalUnit;
	}
	public void setTotalUnit(String totalUnit) {
		this.totalUnit = totalUnit;
	}
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getSetNum() {
		return setNum;
	}
	public void setSetNum(Integer setNum) {
		this.setNum = setNum;
	}
	public String getFrequencyHid() {
		return frequencyHid;
	}
	public void setFrequencyHid(String frequencyHid) {
		this.frequencyHid = frequencyHid;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getUsageNameHid() {
		return usageNameHid;
	}
	public void setUsageNameHid(String usageNameHid) {
		this.usageNameHid = usageNameHid;
	}
	public String getUsageName() {
		return usageName;
	}
	public void setUsageName(String usageName) {
		this.usageName = usageName;
	}
	public Integer getInjectionNum() {
		return injectionNum;
	}
	public void setInjectionNum(Integer injectionNum) {
		this.injectionNum = injectionNum;
	}
	public String getOpenDoctor() {
		return openDoctor;
	}
	public void setOpenDoctor(String openDoctor) {
		this.openDoctor = openDoctor;
	}
	public String getExecutiveDept() {
		return executiveDept;
	}
	public void setExecutiveDept(String executiveDept) {
		this.executiveDept = executiveDept;
	}
	public Integer getIsUrgentHid() {
		return isUrgentHid;
	}
	public void setIsUrgentHid(Integer isUrgentHid) {
		this.isUrgentHid = isUrgentHid;
	}
	public String getIsUrgent() {
		return isUrgent;
	}
	public void setIsUrgent(String isUrgent) {
		this.isUrgent = isUrgent;
	}
	public String getInspectPart() {
		return inspectPart;
	}
	public void setInspectPart(String inspectPart) {
		this.inspectPart = inspectPart;
	}
	public String getSampleTept() {
		return sampleTept;
	}
	public void setSampleTept(String sampleTept) {
		this.sampleTept = sampleTept;
	}
	public String getMinusDept() {
		return minusDept;
	}
	public void setMinusDept(String minusDept) {
		this.minusDept = minusDept;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getInputPeop() {
		return inputPeop;
	}
	public void setInputPeop(String inputPeop) {
		this.inputPeop = inputPeop;
	}
	public String getOpenDept() {
		return openDept;
	}
	public void setOpenDept(String openDept) {
		this.openDept = openDept;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getStopPeop() {
		return stopPeop;
	}
	public void setStopPeop(String stopPeop) {
		this.stopPeop = stopPeop;
	}
	public String getAdviceId() {
		return adviceId;
	}
	public void setAdviceId(String adviceId) {
		this.adviceId = adviceId;
	}
	public Double getAdPrice() {
		return adPrice;
	}
	public void setAdPrice(Double adPrice) {
		this.adPrice = adPrice;
	}
	public Integer getIsSkinHid() {
		return isSkinHid;
	}
	public void setIsSkinHid(Integer isSkinHid) {
		this.isSkinHid = isSkinHid;
	}
	public String getIsSkin() {
		return isSkin;
	}
	public void setIsSkin(String isSkin) {
		this.isSkin = isSkin;
	}
	public String getMinusDeptHid() {
		return minusDeptHid;
	}
	public void setMinusDeptHid(String minusDeptHid) {
		this.minusDeptHid = minusDeptHid;
	}
	public String getExecutiveDeptHid() {
		return executiveDeptHid;
	}
	public void setExecutiveDeptHid(String executiveDeptHid) {
		this.executiveDeptHid = executiveDeptHid;
	}
	public String getAdviceNameView() {
		return adviceNameView;
	}
	public void setAdviceNameView(String adviceNameView) {
		this.adviceNameView = adviceNameView;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public String getSysType() {
		return sysType;
	}
	public void setSysType(String sysType) {
		this.sysType = sysType;
	}
	public String getMinimumcost() {
		return minimumcost;
	}
	public void setMinimumcost(String minimumcost) {
		this.minimumcost = minimumcost;
	}
	public Integer getPackagingnum() {
		return packagingnum;
	}
	public void setPackagingnum(Integer packagingnum) {
		this.packagingnum = packagingnum;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public Integer getIsmanufacture() {
		return ismanufacture;
	}
	public void setIsmanufacture(Integer ismanufacture) {
		this.ismanufacture = ismanufacture;
	}
	public String getDosageform() {
		return dosageform;
	}
	public void setDosageform(String dosageform) {
		this.dosageform = dosageform;
	}
	public Double getDosageHid() {
		return dosageHid;
	}
	public void setDosageHid(Double dosageHid) {
		this.dosageHid = dosageHid;
	}
	public String getInspectPartId() {
		return inspectPartId;
	}
	public void setInspectPartId(String inspectPartId) {
		this.inspectPartId = inspectPartId;
	}
	public String getSampleTeptHid() {
		return sampleTeptHid;
	}
	public void setSampleTeptHid(String sampleTeptHid) {
		this.sampleTeptHid = sampleTeptHid;
	}
	public String getDrugType() {
		return drugType;
	}
	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}
	public Integer getIsInformedconsent() {
		return isInformedconsent;
	}
	public void setIsInformedconsent(Integer isInformedconsent) {
		this.isInformedconsent = isInformedconsent;
	}
	public Integer getSubjobFlag() {
		return subjobFlag;
	}
	public void setSubjobFlag(Integer subjobFlag) {
		this.subjobFlag = subjobFlag;
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
	public Double getConfirmNum() {
		return confirmNum;
	}
	public void setConfirmNum(Double confirmNum) {
		this.confirmNum = confirmNum;
	}
	public Integer getIssubmit() {
		return issubmit;
	}
	public void setIssubmit(Integer issubmit) {
		this.issubmit = issubmit;
	}
	public Integer getConfirmFlag() {
		return confirmFlag;
	}
	public void setConfirmFlag(Integer confirmFlag) {
		this.confirmFlag = confirmFlag;
	}
	
}
