package cn.honry.inner.nursestation.nurseCharge.vo;

import java.util.Date;

/**
 * 护士站收费
 *
 */
public class NurseChargeVo {
	
	
	/**
	 * 渲染
	 */
	private String id;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 统计费用代码
	 */
	private String feeStatCode;
	/**
	 * 统计费用名称
	 */
	private String feeStatName;
	/**
	 * 最小费用代码（编码表中取）
	 */
	private String minfeeCode; 
	/**
	 * 最小费用名称
	 */
	private String feename;
	/**
	 * 组套Id(组套信息)
	 */
	private String stackId; 
	/**
	 * 组套名
	 */
	private String stackName;
	/**
	 * 组套子项的ID
	 */
	private String undrugId;
	/**
	 * 项目名称
	 */
	private String undrugName;
	/**
	 * 处方号
	 */
	private String recipeNo;
	/**
	 * 处方内流水号
	 */
	private Integer sequenceNo;
	/**
	 * 单价
	 */
	private Double money;
	/**
	 * 儿童价
	 */
	private Double undrugChildrenprice;
	/**
	 * 特诊价
	 */
	private Double undrugSpecialprice;
	/**
	 * 执行科室
	 */
	private String dept;
	/**
	 * 单位
	 */
	private String unit;
	/**
	 * 类型（非药品1，和中草药）
	 */
	private String category;
	/**
	 * 病历号
	 */
	private String medicalrecordId;
	/**
	 * 住院流水号   *
	 */
	private String inpatientNo ; 
	/**
	 * 项目名
	 */
	private String patientName;
	/**
	 * 数量
	 */
	private Double amount;
	/**
	 * 付数
	 */
	private Integer unumber;
	/**
	 * 总额
	 */
	private Double moneyMount;
	/**
	 * 收费日期
	 */
	private Date chargeOrder;
	/**
	 * 结算类别
	 */
	private String paykindCode;
	/**
	 * 合同单位
	 */
	private String pactCode;
	/**
	 * 住院科室
	 */
	private String deptCode;
	/**
	 * 开方医生
	 */
	private String emplCode;
	/**
	 * 余额
	 */
	private Double freeCost;
	/**
	 * 结算状态
	 */
	private Integer blanceState;
	/**
	 * 区分药品明细0，非药品明细1，和新添加""
	 */
	private String zsd;
	/**
	 * 价格类型
	 */
	private Integer priceForm;
	/**
	 * 公费比例
	 */
	private Double pubRati;
	/**
	 * 是否绑定母亲身上
	 */
	private Integer yefyis;
	/**
	 * 是否婴儿用药 0 不是 1 是
	 */
	private Integer babyFlag; 
	/**
	 * 住院金额（预交金额）
	 */
	private Double inpatBalance;
	/**
	 * 费用总额
	 */
	private Double totCost;
	/**
	 * 自费
	 */
	private Double enseCost;
	/**
	 * 自付
	 */
	private Double selfCost;
	/**
	 * 优惠
	 */
	private Double privilegeCost;
	/**
	 * 公费
	 */
	private Double pubCost;
	/**
	 * 药品类别
	 */
	private String drugType;
	/**
	 * 项目名称（id）
	 */
	private String nid;
	/**
	 * 取药科室
	 */
	private String getdrugDept;
	/**
	 * 床号
	 */
	private String bedName;
	/**
	 * 1药品2非药品
	 */
	private String ty;
	/**
	 * 药品非药品项目编码
	 */
	private String  itemCode;
	/**
	 * 计费人
	 */
	private String feeOpercode;
	/**
	 * 计费时间
	 */
	private Date feeDate;
	/**
	 * 最小费用代码
	 */
	private String undrugMinimumcost;
	/**
	 * 规格
	 */
	private String spec;
	/**
	 *申请单名称 
	 */
	private String undrugApplication;
	
	/**
	 * 系统类别
	 */
	private String drugSystype;
	/**
	 * 药品性质
	 */
	private String drugNature;
	/**
	 * 剂型
	 */
	private String drugDosageform;
	/**
	 * 药品等级
	 */
	private String drugGrade;
	/**
	 * 最小单位
	 */
	private String minunit;
	/**
	 * 基本剂量
	 */
	private Double drugBasicdose;
	/**
	 * 剂量单位
	 */
	private String drugDoseunit;
	/**
	 * 开立单位;
	 */
	private String extFlag;

	/**
	 * 执行科室名称;
	 */
	private String depth;
	
	private String itemCodeToGroup;//组套时的单个项目编码(用于页面表单数据封装)
	private String itemNameToGroup;//组套时的单个项目名称(用于页面表单数据封装)
	
	
	private Double finalAmount;//最小单位对应数量
	
	/**
	 * 区分是否是划价信息1(是);
	 */
	private String huajia;
	
	
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public String getExtFlag() {
		return extFlag;
	}
	public void setExtFlag(String extFlag) {
		this.extFlag = extFlag;
	}
	public Double getFinalAmount() {
		return finalAmount;
	}
	public void setFinalAmount(Double finalAmount) {
		this.finalAmount = finalAmount;
	}
	public String getItemCodeToGroup() {
		return itemCodeToGroup;
	}
	public void setItemCodeToGroup(String itemCodeToGroup) {
		this.itemCodeToGroup = itemCodeToGroup;
	}
	public String getItemNameToGroup() {
		return itemNameToGroup;
	}
	public void setItemNameToGroup(String itemNameToGroup) {
		this.itemNameToGroup = itemNameToGroup;
	}
	public String getHuajia() {
		return huajia;
	}
	public void setHuajia(String huajia) {
		this.huajia = huajia;
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
	public String getFeeOpercode() {
		return feeOpercode;
	}
	public void setFeeOpercode(String feeOpercode) {
		this.feeOpercode = feeOpercode;
	}
	public Date getFeeDate() {
		return feeDate;
	}
	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}
	public Integer getBabyFlag() {
		return babyFlag;
	}
	public void setBabyFlag(Integer babyFlag) {
		this.babyFlag = babyFlag;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getTy() {
		return ty;
	}
	public void setTy(String ty) {
		this.ty = ty;
	}
	public String getUndrugMinimumcost() {
		return undrugMinimumcost;
	}
	public void setUndrugMinimumcost(String undrugMinimumcost) {
		this.undrugMinimumcost = undrugMinimumcost;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getUndrugApplication() {
		return undrugApplication;
	}
	public void setUndrugApplication(String undrugApplication) {
		this.undrugApplication = undrugApplication;
	}
	public String getDrugSystype() {
		return drugSystype;
	}
	public void setDrugSystype(String drugSystype) {
		this.drugSystype = drugSystype;
	}
	public String getDrugNature() {
		return drugNature;
	}
	public void setDrugNature(String drugNature) {
		this.drugNature = drugNature;
	}
	public String getDrugDosageform() {
		return drugDosageform;
	}
	public void setDrugDosageform(String drugDosageform) {
		this.drugDosageform = drugDosageform;
	}
	public String getDrugGrade() {
		return drugGrade;
	}
	public void setDrugGrade(String drugGrade) {
		this.drugGrade = drugGrade;
	}
	public String getMinunit() {
		return minunit;
	}
	public void setMinunit(String minunit) {
		this.minunit = minunit;
	}
	public Double getDrugBasicdose() {
		return drugBasicdose;
	}
	public void setDrugBasicdose(Double drugBasicdose) {
		this.drugBasicdose = drugBasicdose;
	}
	public String getDrugDoseunit() {
		return drugDoseunit;
	}
	public void setDrugDoseunit(String drugDoseunit) {
		this.drugDoseunit = drugDoseunit;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public String getGetdrugDept() {
		return getdrugDept;
	}
	public void setGetdrugDept(String getdrugDept) {
		this.getdrugDept = getdrugDept;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getDrugType() {
		return drugType;
	}
	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public Double getEnseCost() {
		return enseCost;
	}
	public void setEnseCost(Double enseCost) {
		this.enseCost = enseCost;
	}
	public Double getSelfCost() {
		return selfCost;
	}
	public void setSelfCost(Double selfCost) {
		this.selfCost = selfCost;
	}
	public Double getPrivilegeCost() {
		return privilegeCost;
	}
	public void setPrivilegeCost(Double privilegeCost) {
		this.privilegeCost = privilegeCost;
	}
	public Double getPubCost() {
		return pubCost;
	}
	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}
	public Double getInpatBalance() {
		return inpatBalance;
	}
	public void setInpatBalance(Double inpatBalance) {
		this.inpatBalance = inpatBalance;
	}
	public Integer getYefyis() {
		return yefyis;
	}
	public void setYefyis(Integer yefyis) {
		this.yefyis = yefyis;
	}
	public Integer getPriceForm() {
		return priceForm;
	}
	public void setPriceForm(Integer priceForm) {
		this.priceForm = priceForm;
	}
	public Double getUndrugChildrenprice() {
		return undrugChildrenprice;
	}
	public void setUndrugChildrenprice(Double undrugChildrenprice) {
		this.undrugChildrenprice = undrugChildrenprice;
	}
	public Double getUndrugSpecialprice() {
		return undrugSpecialprice;
	}
	public void setUndrugSpecialprice(Double undrugSpecialprice) {
		this.undrugSpecialprice = undrugSpecialprice;
	}
	public Double getPubRati() {
		return pubRati;
	}
	public void setPubRati(Double pubRati) {
		this.pubRati = pubRati;
	}
	public String getZsd() {
		return zsd;
	}
	public void setZsd(String zsd) {
		this.zsd = zsd;
	}
	public Integer getBlanceState() {
		return blanceState;
	}
	public void setBlanceState(Integer blanceState) {
		this.blanceState = blanceState;
	}
	public Date getChargeOrder() {
		return chargeOrder;
	}
	public void setChargeOrder(Date chargeOrder) {
		this.chargeOrder = chargeOrder;
	}
	public Double getMoneyMount() {
		return moneyMount;
	}
	public void setMoneyMount(Double moneyMount) {
		this.moneyMount = moneyMount;
	}
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
	public String getFeeStatCode() {
		return feeStatCode;
	}
	public void setFeeStatCode(String feeStatCode) {
		this.feeStatCode = feeStatCode;
	}
	public String getFeeStatName() {
		return feeStatName;
	}
	public void setFeeStatName(String feeStatName) {
		this.feeStatName = feeStatName;
	}
	public String getMinfeeCode() {
		return minfeeCode;
	}
	public void setMinfeeCode(String minfeeCode) {
		this.minfeeCode = minfeeCode;
	}
	public String getFeename() {
		return feename;
	}
	public void setFeename(String feename) {
		this.feename = feename;
	}
	public String getStackId() {
		return stackId;
	}
	public void setStackId(String stackId) {
		this.stackId = stackId;
	}
	public String getStackName() {
		return stackName;
	}
	public void setStackName(String stackName) {
		this.stackName = stackName;
	}
	public String getUndrugId() {
		return undrugId;
	}
	public void setUndrugId(String undrugId) {
		this.undrugId = undrugId;
	}
	public String getUndrugName() {
		return undrugName;
	}
	public void setUndrugName(String undrugName) {
		this.undrugName = undrugName;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getUnumber() {
		return unumber;
	}
	public void setUnumber(Integer unumber) {
		this.unumber = unumber;
	}

	
	public String getPaykindCode() {
		return paykindCode;
	}
	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	}
	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getEmplCode() {
		return emplCode;
	}
	public void setEmplCode(String emplCode) {
		this.emplCode = emplCode;
	}
	public Double getFreeCost() {
		return freeCost;
	}
	public void setFreeCost(Double freeCost) {
		this.freeCost = freeCost;
	}
	

	
}
