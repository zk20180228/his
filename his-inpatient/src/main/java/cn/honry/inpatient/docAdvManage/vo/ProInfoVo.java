package cn.honry.inpatient.docAdvManage.vo;
/**
 * 项目信息显示Vo
 * @author Administrator
 *
 */
public class ProInfoVo {
	/**项目Id **/
	private String itemId;
	/**项目名称  **/
	private String name;
	/**系统类别  **/
	private String sysType;
	/** 规格 **/
	private String specs;
	/** 默认价 **/
	private Double defaultprice;
	/**包装单位**/
	private String drugPackagingunit;
	/**最小单位  **/
	private String unit;
	/**医保标记  **/
	/**药品等级  **/
	private String drugGrade;
	/**是否省限制**/
	private Integer undrugIsprovincelimit;
	/**是否市限制  **/
	private Integer undrugIscitylimit;
	/**是否自费  **/
	private Integer undrugIsownexpense;
	/**是否特定项目  **/
	private Integer undrugIsspecificitems;
	/**通用名称拼音码  **/
	private String namepinyin;
	/**通用名称五笔码**/
	private String namewb;
	/**通用名称自定义码**/
	private String nameinputcode;
	/**药品通用名称**/
	private String drugCommonname;
	/**药品编码**/
	private String drugId;
	/**执行科室  **/
	private String dept;
	/**检查部位或标本  **/
	private String inspectionSite;
	/**疾病分类  **/
	private String diseaseClassification;
	/**专科名称  **/
	private String specialtyName;
	/**病史检查 **/
	private String medicalHistory;
	/**检查要求  **/
	private String requirements;
	/**注意事项 **/
	private String notes;
	/**国家基本药物编码**/
	private String gbcode;
	/**说明书**/
	private String drugInstruction;
	/**一次用量**/
	private Double drugOncedosage;
	/**剂量单位**/
	private String drugDoseunit;
	/**频次**/
	private String drugFrequency;
	/**最低库存量*/
	private Double lowSum;
	/**剂型代码**/
	private String drugDosageform;
	/**药品类别**/
	private String drugType;
	/**药品性质**/
	private String drugNature;
	/**零售价**/
	private Double drugRetailprice;
	/**备注**/
	private String remark;
	/**使用方法**/
	private String drugUsemode;
	/**基本剂量**/
	private Double drugBasicdose;
	/**是否知情同意书  **/
	private Integer undrugIsinformedconsent;
	/**药品非药品标志**/
	private Integer ty;
	/**1非抗菌药2无限制3职级限制4特殊管理**/
	private Integer drugRestrictionofantibiotic;
	/**库存总数量*/
	private Double storeSum;
	/**停用标志**/
	private Integer stop_flg;
	/**库存停用标志**/
	private Integer stockStop_flg;
	/**0不需要皮试1青霉素皮试2原药皮试**/
	private Integer drugIstestsensitivity;
	/**包装数量**/
	private Integer packagingnum;
	/**项目code 20160825 modified by hedong***/
	private String itemCode;
	/**是否确认  **/
	private Integer undrugIssubmit;
	/**是否需要预约  **/
	private Integer undrugIspreorder;
	private Integer splitattr;//药品表拆分属性
	private Integer property;//药品拆分维护表属性0-不可拆分  1-可拆分,配药时不取整  2-可拆分配药时上取整 3不可拆分，当日取整
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
	public Integer getUndrugIssubmit() {
		return undrugIssubmit;
	}
	public void setUndrugIssubmit(Integer undrugIssubmit) {
		this.undrugIssubmit = undrugIssubmit;
	}
	public Integer getUndrugIspreorder() {
		return undrugIspreorder;
	}
	public void setUndrugIspreorder(Integer undrugIspreorder) {
		this.undrugIspreorder = undrugIspreorder;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSysType() {
		return sysType;
	}
	public void setSysType(String sysType) {
		this.sysType = sysType;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public Double getDefaultprice() {
		return defaultprice;
	}
	public void setDefaultprice(Double defaultprice) {
		this.defaultprice = defaultprice;
	}	
	public String getDrugPackagingunit() {
		return drugPackagingunit;
	}
	public void setDrugPackagingunit(String drugPackagingunit) {
		this.drugPackagingunit = drugPackagingunit;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDrugGrade() {
		return drugGrade;
	}
	public void setDrugGrade(String drugGrade) {
		this.drugGrade = drugGrade;
	}	
	public Integer getUndrugIsprovincelimit() {
		return undrugIsprovincelimit;
	}
	public void setUndrugIsprovincelimit(Integer undrugIsprovincelimit) {
		this.undrugIsprovincelimit = undrugIsprovincelimit;
	}
	public Integer getUndrugIscitylimit() {
		return undrugIscitylimit;
	}
	public void setUndrugIscitylimit(Integer undrugIscitylimit) {
		this.undrugIscitylimit = undrugIscitylimit;
	}
	public Integer getUndrugIsownexpense() {
		return undrugIsownexpense;
	}
	public void setUndrugIsownexpense(Integer undrugIsownexpense) {
		this.undrugIsownexpense = undrugIsownexpense;
	}		
	public Integer getUndrugIsspecificitems() {
		return undrugIsspecificitems;
	}
	public void setUndrugIsspecificitems(Integer undrugIsspecificitems) {
		this.undrugIsspecificitems = undrugIsspecificitems;
	}
	public String getNamepinyin() {
		return namepinyin;
	}
	public void setNamepinyin(String namepinyin) {
		this.namepinyin = namepinyin;
	}
	public String getNamewb() {
		return namewb;
	}
	public void setNamewb(String namewb) {
		this.namewb = namewb;
	}
	public String getNameinputcode() {
		return nameinputcode;
	}
	public void setNameinputcode(String nameinputcode) {
		this.nameinputcode = nameinputcode;
	}
	public String getDrugCommonname() {
		return drugCommonname;
	}
	public void setDrugCommonname(String drugCommonname) {
		this.drugCommonname = drugCommonname;
	}
	public String getDrugId() {
		return drugId;
	}
	public void setDrugId(String drugId) {
		this.drugId = drugId;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getInspectionSite() {
		return inspectionSite;
	}
	public void setInspectionSite(String inspectionSite) {
		this.inspectionSite = inspectionSite;
	}
	public String getDiseaseClassification() {
		return diseaseClassification;
	}
	public void setDiseaseClassification(String diseaseClassification) {
		this.diseaseClassification = diseaseClassification;
	}
	public String getSpecialtyName() {
		return specialtyName;
	}
	public void setSpecialtyName(String specialtyName) {
		this.specialtyName = specialtyName;
	}
	public String getMedicalHistory() {
		return medicalHistory;
	}
	public void setMedicalHistory(String medicalHistory) {
		this.medicalHistory = medicalHistory;
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
	public String getGbcode() {
		return gbcode;
	}
	public void setGbcode(String gbcode) {
		this.gbcode = gbcode;
	}
	public String getDrugInstruction() {
		return drugInstruction;
	}
	public void setDrugInstruction(String drugInstruction) {
		this.drugInstruction = drugInstruction;
	}
	public Double getDrugOncedosage() {
		return drugOncedosage;
	}
	public void setDrugOncedosage(Double drugOncedosage) {
		this.drugOncedosage = drugOncedosage;
	}
	public String getDrugDoseunit() {
		return drugDoseunit;
	}
	public void setDrugDoseunit(String drugDoseunit) {
		this.drugDoseunit = drugDoseunit;
	}
	public String getDrugFrequency() {
		return drugFrequency;
	}
	public void setDrugFrequency(String drugFrequency) {
		this.drugFrequency = drugFrequency;
	}
	public Double getLowSum() {
		return lowSum;
	}
	public void setLowSum(Double lowSum) {
		this.lowSum = lowSum;
	}
	public String getDrugDosageform() {
		return drugDosageform;
	}
	public void setDrugDosageform(String drugDosageform) {
		this.drugDosageform = drugDosageform;
	}
	public String getDrugType() {
		return drugType;
	}
	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}
	public String getDrugNature() {
		return drugNature;
	}
	public void setDrugNature(String drugNature) {
		this.drugNature = drugNature;
	}
	public Double getDrugRetailprice() {
		return drugRetailprice;
	}
	public void setDrugRetailprice(Double drugRetailprice) {
		this.drugRetailprice = drugRetailprice;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDrugUsemode() {
		return drugUsemode;
	}
	public void setDrugUsemode(String drugUsemode) {
		this.drugUsemode = drugUsemode;
	}
	public Double getDrugBasicdose() {
		return drugBasicdose;
	}
	public void setDrugBasicdose(Double drugBasicdose) {
		this.drugBasicdose = drugBasicdose;
	}
	public Integer getUndrugIsinformedconsent() {
		return undrugIsinformedconsent;
	}
	public void setUndrugIsinformedconsent(Integer undrugIsinformedconsent) {
		this.undrugIsinformedconsent = undrugIsinformedconsent;
	}
	public Integer getTy() {
		return ty;
	}
	public void setTy(Integer ty) {
		this.ty = ty;
	}
	public Integer getDrugRestrictionofantibiotic() {
		return drugRestrictionofantibiotic;
	}
	public void setDrugRestrictionofantibiotic(Integer drugRestrictionofantibiotic) {
		this.drugRestrictionofantibiotic = drugRestrictionofantibiotic;
	}
	public Double getStoreSum() {
		return storeSum;
	}
	public void setStoreSum(Double storeSum) {
		this.storeSum = storeSum;
	}
	public Integer getStop_flg() {
		return stop_flg;
	}
	public void setStop_flg(Integer stop_flg) {
		this.stop_flg = stop_flg;
	}
	public Integer getStockStop_flg() {
		return stockStop_flg;
	}
	public void setStockStop_flg(Integer stockStop_flg) {
		this.stockStop_flg = stockStop_flg;
	}
	public Integer getDrugIstestsensitivity() {
		return drugIstestsensitivity;
	}
	public void setDrugIstestsensitivity(Integer drugIstestsensitivity) {
		this.drugIstestsensitivity = drugIstestsensitivity;
	}
	public Integer getPackagingnum() {
		return packagingnum;
	}
	public void setPackagingnum(Integer packagingnum) {
		this.packagingnum = packagingnum;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
}
