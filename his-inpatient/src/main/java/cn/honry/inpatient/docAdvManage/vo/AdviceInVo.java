package cn.honry.inpatient.docAdvManage.vo;

import java.util.Date;


/**  
 *  
 * @className：AdviceVo 
 * @Description：  门诊医嘱Vo
 * @Author：aizhonghua
 * @CreateDate：2015-12-11 下午04:13:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-12-11 下午04:13:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class AdviceInVo {
	private String id;
	private String adviceNo;
	private String type;//类型
	private String adviceType;//医嘱类型
	private String adviceId;//医嘱名称Id
	private String adviceName;//医嘱名称
	private String adviceNameView;//医嘱名称
	private Double adPrice;//价格
	private String adPackUnitHid;//包装单位
	private String adMinUnitHid;//单位
	private String adDosaUnitHid;//剂量
	private String adDosaUnitHidJudge;//剂量
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
	private Double totalNum;//总量
	private String totalUnitHid;//总单位Id
	private String totalUnitHidJudge;//总单位Id
	private String totalUnit;//总单位
	private Double dosageHid;//每次用量
	private Double dosageMin;//每次剂量
	private String dosage;//每次用量
	private String unit;//单位
	private Integer setNum;//服数
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
	private Integer auditing;//是否需要审核
	private Integer colour;//医嘱状态
	private String limit;//省市限制
	private Integer ty;//是否为药品
	/**业务变更 药品拆分属性 2017-03-02 09:45 aizhonghua**/
	private Integer splitattr;//药品表拆分属性
	private Integer property;//药品拆分维护表属性0-不可拆分  1-可拆分,配药时不取整  2-可拆分配药时上取整 3不可拆分，当日取整
	
	private Integer dataOrder;
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
	public Integer getAuditing() {
		return auditing;
	}
	public void setAuditing(Integer auditing) {
		this.auditing = auditing;
	}
	public Integer getColour() {
		return colour;
	}
	public void setColour(Integer colour) {
		this.colour = colour;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public Integer getTy() {
		return ty;
	}
	public void setTy(Integer ty) {
		this.ty = ty;
	}
	public String getAdviceNo() {
		return adviceNo;
	}
	public void setAdviceNo(String adviceNo) {
		this.adviceNo = adviceNo;
	}
	public Double getDosageMin() {
		return dosageMin;
	}
	public void setDosageMin(Double dosageMin) {
		this.dosageMin = dosageMin;
	}
	public Integer getDataOrder() {
		return dataOrder;
	}
	public void setDataOrder(Integer dataOrder) {
		this.dataOrder = dataOrder;
	}
	public String getAdDosaUnitHidJudge() {
		return adDosaUnitHidJudge;
	}
	public void setAdDosaUnitHidJudge(String adDosaUnitHidJudge) {
		this.adDosaUnitHidJudge = adDosaUnitHidJudge;
	}
	public String getTotalUnitHidJudge() {
		return totalUnitHidJudge;
	}
	public void setTotalUnitHidJudge(String totalUnitHidJudge) {
		this.totalUnitHidJudge = totalUnitHidJudge;
	}
	public Integer getSplitattr() {
		return splitattr;
	}
	public void setSplitattr(Integer splitattr) {
		this.splitattr = splitattr;
	}
	public Integer getProperty() {
		return property;
	}
	public void setProperty(Integer property) {
		this.property = property;
	}
}
