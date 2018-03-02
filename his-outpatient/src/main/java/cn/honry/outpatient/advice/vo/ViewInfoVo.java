package cn.honry.outpatient.advice.vo;


public class ViewInfoVo {
	private String id;//id
	private String name;//名称
	private String code;//编码
	private String type;//类型
	private String sysType;//系统类型
	private Integer delFlg;//删除标志
	private Integer stopFlg;//停用标志
	private Double price;//价格
	private Integer ty;//药品1非药品0
	private Integer surSum;//剩余数量
	private String spec;//规格
	private String unit;//单位
	private Integer insured;//医保标记
	private String inputcode;//自定义码
	private String commonName;//通用名
	private String inspectionsite;//检查检体
	private String diseaseclassification;//疾病分类
	private String specialtyName;//专科名称
	private String medicalhistory;//病史及检查
	private String requirements;//检查要求
	private String notes;//注意事项
	private String minimumUnit;//最小单位
	private Double basicdose;//基本剂量
	private String doseunit;//剂量单位
	private String frequency;//频次
	private String usemode;//用法
	private String remark;//备注
	private Integer istestsensitivity;//0不需要皮试1青霉素皮试2原药皮试
	private String dept;//执行科室:从部门表获取
	private String gbcode;//国家编码
	private String grade;//药品等级
	private String instruction;//说明书
	private Integer stop_flg;//停用
	private String minimumcost;//最小费用代码
	private Integer packagingnum;//包装数量
	private String nature;//药品性质
	private Integer ismanufacture;//自制药标志
	private String dosageform;//剂型
	private Integer oncedosage;//每次用量
	private String labsample;//样本类型
	private Integer isProvincelimit;//是否省限制
	private Integer isCitylimit;//是否市限制
	private String drugGrade;//药品等级
	private Integer isInformedconsent;//是否知情同意书
	private Integer restrictionofantibiotic;//抗菌药限制1非抗菌药2无限制3职级限制4特殊管理
	/**业务变更 药品拆分属性 2017-03-02 09:45 aizhonghua**/
	private Integer splitattr;//药品表拆分属性
	private Integer property;//药品拆分维护表属性0-不可拆分  1-可拆分,配药时不取整  2-可拆分配药时上取整 3不可拆分，当日取整
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getDelFlg() {
		return delFlg;
	}
	public void setDelFlg(Integer delFlg) {
		this.delFlg = delFlg;
	}
	public Integer getStopFlg() {
		return stopFlg;
	}
	public void setStopFlg(Integer stopFlg) {
		this.stopFlg = stopFlg;
	}
	public Integer getTy() {
		return ty;
	}
	public void setTy(Integer ty) {
		this.ty = ty;
	}
	public Integer getSurSum() {
		return surSum;
	}
	public void setSurSum(Integer surSum) {
		this.surSum = surSum;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSysType() {
		return sysType;
	}
	public void setSysType(String sysType) {
		this.sysType = sysType;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getInsured() {
		return insured;
	}
	public void setInsured(Integer insured) {
		this.insured = insured;
	}
	public String getInputcode() {
		return inputcode;
	}
	public void setInputcode(String inputcode) {
		this.inputcode = inputcode;
	}
	public String getCommonName() {
		return commonName;
	}
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	public String getInspectionsite() {
		return inspectionsite;
	}
	public void setInspectionsite(String inspectionsite) {
		this.inspectionsite = inspectionsite;
	}
	public String getDiseaseclassification() {
		return diseaseclassification;
	}
	public void setDiseaseclassification(String diseaseclassification) {
		this.diseaseclassification = diseaseclassification;
	}
	public String getSpecialtyName() {
		return specialtyName;
	}
	public void setSpecialtyName(String specialtyName) {
		this.specialtyName = specialtyName;
	}
	public String getMedicalhistory() {
		return medicalhistory;
	}
	public void setMedicalhistory(String medicalhistory) {
		this.medicalhistory = medicalhistory;
	}
	public String getRequirements() {
		return requirements;
	}
	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getMinimumUnit() {
		return minimumUnit;
	}
	public void setMinimumUnit(String minimumUnit) {
		this.minimumUnit = minimumUnit;
	}
	public Double getBasicdose() {
		return basicdose;
	}
	public void setBasicdose(Double basicdose) {
		this.basicdose = basicdose;
	}
	public String getDoseunit() {
		return doseunit;
	}
	public void setDoseunit(String doseunit) {
		this.doseunit = doseunit;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getUsemode() {
		return usemode;
	}
	public void setUsemode(String usemode) {
		this.usemode = usemode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getIstestsensitivity() {
		return istestsensitivity;
	}
	public void setIstestsensitivity(Integer istestsensitivity) {
		this.istestsensitivity = istestsensitivity;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getGbcode() {
		return gbcode;
	}
	public void setGbcode(String gbcode) {
		this.gbcode = gbcode;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public Integer getStop_flg() {
		return stop_flg;
	}
	public void setStop_flg(Integer stop_flg) {
		this.stop_flg = stop_flg;
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
	public Integer getOncedosage() {
		return oncedosage;
	}
	public void setOncedosage(Integer oncedosage) {
		this.oncedosage = oncedosage;
	}
	public String getLabsample() {
		return labsample;
	}
	public void setLabsample(String labsample) {
		this.labsample = labsample;
	}
	public Integer getIsProvincelimit() {
		return isProvincelimit;
	}
	public void setIsProvincelimit(Integer isProvincelimit) {
		this.isProvincelimit = isProvincelimit;
	}
	public Integer getIsCitylimit() {
		return isCitylimit;
	}
	public void setIsCitylimit(Integer isCitylimit) {
		this.isCitylimit = isCitylimit;
	}
	public String getDrugGrade() {
		return drugGrade;
	}
	public void setDrugGrade(String drugGrade) {
		this.drugGrade = drugGrade;
	}
	public Integer getIsInformedconsent() {
		return isInformedconsent;
	}
	public void setIsInformedconsent(Integer isInformedconsent) {
		this.isInformedconsent = isInformedconsent;
	}
	public Integer getRestrictionofantibiotic() {
		return restrictionofantibiotic;
	}
	public void setRestrictionofantibiotic(Integer restrictionofantibiotic) {
		this.restrictionofantibiotic = restrictionofantibiotic;
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
