package cn.honry.inner.baseinfo.stack.vo;

import java.util.Date;

public class StackAndStockInfoInInterVo {
	private Integer ty;//类别
	private String stackId;//组套id
	private String stackInfoId;//组套详情id
	private Integer stackInfoIsDrug;//是否是药品
	private Integer stackInfoNum;//开立数量
	private String stackInfoUnit;//单位
	private String stackInfoDeptId;//科室
	private String stackInfoRemark;//组套备注			
	private Date dateBgn;//医嘱开始时间
	private Date dateEnd;//医嘱结束时间
	private Integer days;//草药服数
	private String intervaldays;//间隔天数
	private String id;//药品id
	private String name;//名称
	private String gbCode;//国家编码
	private String spec;//规格
	private String drugType;//药品类别
	private String drugSystype;//系统类别
	private String drugMinimumcost;//最小费用
	private String drugNature;//药品性质
	private String drugDosageform;//剂型
	private String drugGrade;//药品等级
	private Integer drugSplitattr;//拆分属性
	private String drugManufacturer;//生产厂家
	private String drugPackagingunit;//包装单位
	private Integer packagingnum;//包装数量
	private String unit;//最小单位
	private Double drugBasicdose;//基本剂量
	private String drugDoseunit;//剂量单位
	private Double drugRetailprice;//零售价
	private Double drugMaxretailprice;//最高零售价
	private Double drugWholesaleprice;//批发价
	private Double drugPurchaseprice;//购入价
	private String drugPricetype;//价格形式
	private Integer drugIsnew;//是否新药
	private Integer drugIsmanufacture;//是否自制
	private Integer drugIstestsensitivity;//是否试敏
	private Integer drugIsgmp;//是否GMP
	private Integer drugIsotc;//是否OTC
	private Integer drugIslack;//是否缺药
	private Integer drugIsagreementprescription;//是否协定处方
	private Integer drugIscooperativemedical;//是否合作医疗
	private Integer drugRestrictionofantibiotic;//抗菌药限制特性
	private String drugRemark;//备注
	private String drugUsemode;//使用方法
	private Double drugOncedosage;//一次用量
	private String drugFrequency;//频次
	private String drugNotes;//注意事项
	private String drugOperativenorm;//执行标准
	private String drugInstruction;////说明书
	private Integer stop_flg;//停用标志
	private Integer del_flg;//删除标志
	private Integer unDrugState;//状态1在用2停用3废弃
	private String unDrugDept;//执行科室:从部门表获取
	private String unDrugItemlimit;//项目约束
	private String unDrugScope;//项目范围
	private String unDrugRequirements;//检查要求
	private String unDrugInspectionsite;//检查部位或标本
	private String unDrugMedicalhistory;//病史检查
	private Double unDrugChildrenPrice;//儿童价
	private Double unDrugSpecialPrice;//特诊价
	private Double unDrugEmergencyaserate;//急诊比例
	private String unDrugDiseaseclassificattion;//疾病分类:从编码表获取
	private String unDrugSpecialtyName;//专科名称
	private Integer unDrugIsprovincelimit;//是否省限制
	private Integer unDrugIscitylimit;//是否市限制
	private Integer unDrugIsownexpense;//是否自费
	private Integer unDrugIssubmit;//是否确认
	private Integer unDrugIspreorder;//是否需要预约
	private Integer unDrugIsbirthcontrol;//是否计划生育
	private Integer unDrugIsspecificitems;//是否特定项目
	private Integer unDrugIsinformedconsent;//是否知情同意书
	private Integer unDrugCrontrast;//是否对照
	private Integer unDrugIsA;//是否甲类
	private Integer unDrugIsB;//是否乙类
	private Integer unDrugIsC;//是否丙类
	private String infoId;//库存id
	private String storageDeptid;//科室
	private Double storeSum;//总数量
	private Double preoutSum;//预扣库存数量
	private Integer unitFlag;//默认发药单位标记 '0'－最小单位，'1'－包装单位
	private Integer changeFlag;//是否可以拆零1是0否，指默认发药单位为最小单位时，是否可以
	private Integer validFlag;//有效性标志1  在用 0 停用 2 废弃 
	private String labsample;//样本类型
	private String combNo;   //组合流水号
	
	public String getCombNo() {
		return combNo;
	}
	public void setCombNo(String combNo) {
		this.combNo = combNo;
	}
	public Integer getTy() {
		return ty;
	}
	public void setTy(Integer ty) {
		this.ty = ty;
	}
	public String getStackId() {
		return stackId;
	}
	public void setStackId(String stackId) {
		this.stackId = stackId;
	}
	public String getStackInfoId() {
		return stackInfoId;
	}
	public void setStackInfoId(String stackInfoId) {
		this.stackInfoId = stackInfoId;
	}
	public Integer getStackInfoIsDrug() {
		return stackInfoIsDrug;
	}
	public void setStackInfoIsDrug(Integer stackInfoIsDrug) {
		this.stackInfoIsDrug = stackInfoIsDrug;
	}
	public Integer getStackInfoNum() {
		return stackInfoNum;
	}
	public void setStackInfoNum(Integer stackInfoNum) {
		this.stackInfoNum = stackInfoNum;
	}
	public String getStackInfoUnit() {
		return stackInfoUnit;
	}
	public void setStackInfoUnit(String stackInfoUnit) {
		this.stackInfoUnit = stackInfoUnit;
	}
	public String getStackInfoDeptId() {
		return stackInfoDeptId;
	}
	public void setStackInfoDeptId(String stackInfoDeptId) {
		this.stackInfoDeptId = stackInfoDeptId;
	}
	public String getStackInfoRemark() {
		return stackInfoRemark;
	}
	public void setStackInfoRemark(String stackInfoRemark) {
		this.stackInfoRemark = stackInfoRemark;
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
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public String getIntervaldays() {
		return intervaldays;
	}
	public void setIntervaldays(String intervaldays) {
		this.intervaldays = intervaldays;
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
	public String getGbCode() {
		return gbCode;
	}
	public void setGbCode(String gbCode) {
		this.gbCode = gbCode;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getDrugType() {
		return drugType;
	}
	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}
	public String getDrugSystype() {
		return drugSystype;
	}
	public void setDrugSystype(String drugSystype) {
		this.drugSystype = drugSystype;
	}
	public String getDrugMinimumcost() {
		return drugMinimumcost;
	}
	public void setDrugMinimumcost(String drugMinimumcost) {
		this.drugMinimumcost = drugMinimumcost;
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
	public Integer getDrugSplitattr() {
		return drugSplitattr;
	}
	public void setDrugSplitattr(Integer drugSplitattr) {
		this.drugSplitattr = drugSplitattr;
	}
	public String getDrugManufacturer() {
		return drugManufacturer;
	}
	public void setDrugManufacturer(String drugManufacturer) {
		this.drugManufacturer = drugManufacturer;
	}
	public String getDrugPackagingunit() {
		return drugPackagingunit;
	}
	public void setDrugPackagingunit(String drugPackagingunit) {
		this.drugPackagingunit = drugPackagingunit;
	}
	public Integer getPackagingnum() {
		return packagingnum;
	}
	public void setPackagingnum(Integer packagingnum) {
		this.packagingnum = packagingnum;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
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
	public Double getDrugRetailprice() {
		return drugRetailprice;
	}
	public void setDrugRetailprice(Double drugRetailprice) {
		this.drugRetailprice = drugRetailprice;
	}
	public Double getDrugMaxretailprice() {
		return drugMaxretailprice;
	}
	public void setDrugMaxretailprice(Double drugMaxretailprice) {
		this.drugMaxretailprice = drugMaxretailprice;
	}
	public Double getDrugWholesaleprice() {
		return drugWholesaleprice;
	}
	public void setDrugWholesaleprice(Double drugWholesaleprice) {
		this.drugWholesaleprice = drugWholesaleprice;
	}
	public Double getDrugPurchaseprice() {
		return drugPurchaseprice;
	}
	public void setDrugPurchaseprice(Double drugPurchaseprice) {
		this.drugPurchaseprice = drugPurchaseprice;
	}
	public String getDrugPricetype() {
		return drugPricetype;
	}
	public void setDrugPricetype(String drugPricetype) {
		this.drugPricetype = drugPricetype;
	}
	public Integer getDrugIsnew() {
		return drugIsnew;
	}
	public void setDrugIsnew(Integer drugIsnew) {
		this.drugIsnew = drugIsnew;
	}
	public Integer getDrugIsmanufacture() {
		return drugIsmanufacture;
	}
	public void setDrugIsmanufacture(Integer drugIsmanufacture) {
		this.drugIsmanufacture = drugIsmanufacture;
	}
	public Integer getDrugIstestsensitivity() {
		return drugIstestsensitivity;
	}
	public void setDrugIstestsensitivity(Integer drugIstestsensitivity) {
		this.drugIstestsensitivity = drugIstestsensitivity;
	}
	public Integer getDrugIsgmp() {
		return drugIsgmp;
	}
	public void setDrugIsgmp(Integer drugIsgmp) {
		this.drugIsgmp = drugIsgmp;
	}
	public Integer getDrugIsotc() {
		return drugIsotc;
	}
	public void setDrugIsotc(Integer drugIsotc) {
		this.drugIsotc = drugIsotc;
	}
	public Integer getDrugIslack() {
		return drugIslack;
	}
	public void setDrugIslack(Integer drugIslack) {
		this.drugIslack = drugIslack;
	}
	public Integer getDrugIsagreementprescription() {
		return drugIsagreementprescription;
	}
	public void setDrugIsagreementprescription(Integer drugIsagreementprescription) {
		this.drugIsagreementprescription = drugIsagreementprescription;
	}
	public Integer getDrugIscooperativemedical() {
		return drugIscooperativemedical;
	}
	public void setDrugIscooperativemedical(Integer drugIscooperativemedical) {
		this.drugIscooperativemedical = drugIscooperativemedical;
	}
	public Integer getDrugRestrictionofantibiotic() {
		return drugRestrictionofantibiotic;
	}
	public void setDrugRestrictionofantibiotic(Integer drugRestrictionofantibiotic) {
		this.drugRestrictionofantibiotic = drugRestrictionofantibiotic;
	}
	public String getDrugRemark() {
		return drugRemark;
	}
	public void setDrugRemark(String drugRemark) {
		this.drugRemark = drugRemark;
	}
	public String getDrugUsemode() {
		return drugUsemode;
	}
	public void setDrugUsemode(String drugUsemode) {
		this.drugUsemode = drugUsemode;
	}
	public Double getDrugOncedosage() {
		return drugOncedosage;
	}
	public void setDrugOncedosage(Double drugOncedosage) {
		this.drugOncedosage = drugOncedosage;
	}
	public String getDrugFrequency() {
		return drugFrequency;
	}
	public void setDrugFrequency(String drugFrequency) {
		this.drugFrequency = drugFrequency;
	}
	public String getDrugNotes() {
		return drugNotes;
	}
	public void setDrugNotes(String drugNotes) {
		this.drugNotes = drugNotes;
	}
	public String getDrugOperativenorm() {
		return drugOperativenorm;
	}
	public void setDrugOperativenorm(String drugOperativenorm) {
		this.drugOperativenorm = drugOperativenorm;
	}
	public String getDrugInstruction() {
		return drugInstruction;
	}
	public void setDrugInstruction(String drugInstruction) {
		this.drugInstruction = drugInstruction;
	}
	public Integer getStop_flg() {
		return stop_flg;
	}
	public void setStop_flg(Integer stop_flg) {
		this.stop_flg = stop_flg;
	}
	public Integer getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(Integer del_flg) {
		this.del_flg = del_flg;
	}
	public Integer getUnDrugState() {
		return unDrugState;
	}
	public void setUnDrugState(Integer unDrugState) {
		this.unDrugState = unDrugState;
	}
	public String getUnDrugDept() {
		return unDrugDept;
	}
	public void setUnDrugDept(String unDrugDept) {
		this.unDrugDept = unDrugDept;
	}
	public String getUnDrugItemlimit() {
		return unDrugItemlimit;
	}
	public void setUnDrugItemlimit(String unDrugItemlimit) {
		this.unDrugItemlimit = unDrugItemlimit;
	}
	public String getUnDrugScope() {
		return unDrugScope;
	}
	public void setUnDrugScope(String unDrugScope) {
		this.unDrugScope = unDrugScope;
	}
	public String getUnDrugRequirements() {
		return unDrugRequirements;
	}
	public void setUnDrugRequirements(String unDrugRequirements) {
		this.unDrugRequirements = unDrugRequirements;
	}
	public String getUnDrugInspectionsite() {
		return unDrugInspectionsite;
	}
	public void setUnDrugInspectionsite(String unDrugInspectionsite) {
		this.unDrugInspectionsite = unDrugInspectionsite;
	}
	public String getUnDrugMedicalhistory() {
		return unDrugMedicalhistory;
	}
	public void setUnDrugMedicalhistory(String unDrugMedicalhistory) {
		this.unDrugMedicalhistory = unDrugMedicalhistory;
	}
	public Double getUnDrugChildrenPrice() {
		return unDrugChildrenPrice;
	}
	public void setUnDrugChildrenPrice(Double unDrugChildrenPrice) {
		this.unDrugChildrenPrice = unDrugChildrenPrice;
	}
	public Double getUnDrugSpecialPrice() {
		return unDrugSpecialPrice;
	}
	public void setUnDrugSpecialPrice(Double unDrugSpecialPrice) {
		this.unDrugSpecialPrice = unDrugSpecialPrice;
	}
	public Double getUnDrugEmergencyaserate() {
		return unDrugEmergencyaserate;
	}
	public void setUnDrugEmergencyaserate(Double unDrugEmergencyaserate) {
		this.unDrugEmergencyaserate = unDrugEmergencyaserate;
	}
	public String getUnDrugDiseaseclassificattion() {
		return unDrugDiseaseclassificattion;
	}
	public void setUnDrugDiseaseclassificattion(String unDrugDiseaseclassificattion) {
		this.unDrugDiseaseclassificattion = unDrugDiseaseclassificattion;
	}
	public String getUnDrugSpecialtyName() {
		return unDrugSpecialtyName;
	}
	public void setUnDrugSpecialtyName(String unDrugSpecialtyName) {
		this.unDrugSpecialtyName = unDrugSpecialtyName;
	}
	public Integer getUnDrugIsprovincelimit() {
		return unDrugIsprovincelimit;
	}
	public void setUnDrugIsprovincelimit(Integer unDrugIsprovincelimit) {
		this.unDrugIsprovincelimit = unDrugIsprovincelimit;
	}
	public Integer getUnDrugIscitylimit() {
		return unDrugIscitylimit;
	}
	public void setUnDrugIscitylimit(Integer unDrugIscitylimit) {
		this.unDrugIscitylimit = unDrugIscitylimit;
	}
	public Integer getUnDrugIsownexpense() {
		return unDrugIsownexpense;
	}
	public void setUnDrugIsownexpense(Integer unDrugIsownexpense) {
		this.unDrugIsownexpense = unDrugIsownexpense;
	}
	public Integer getUnDrugIssubmit() {
		return unDrugIssubmit;
	}
	public void setUnDrugIssubmit(Integer unDrugIssubmit) {
		this.unDrugIssubmit = unDrugIssubmit;
	}
	public Integer getUnDrugIspreorder() {
		return unDrugIspreorder;
	}
	public void setUnDrugIspreorder(Integer unDrugIspreorder) {
		this.unDrugIspreorder = unDrugIspreorder;
	}
	public Integer getUnDrugIsbirthcontrol() {
		return unDrugIsbirthcontrol;
	}
	public void setUnDrugIsbirthcontrol(Integer unDrugIsbirthcontrol) {
		this.unDrugIsbirthcontrol = unDrugIsbirthcontrol;
	}
	public Integer getUnDrugIsspecificitems() {
		return unDrugIsspecificitems;
	}
	public void setUnDrugIsspecificitems(Integer unDrugIsspecificitems) {
		this.unDrugIsspecificitems = unDrugIsspecificitems;
	}
	public Integer getUnDrugIsinformedconsent() {
		return unDrugIsinformedconsent;
	}
	public void setUnDrugIsinformedconsent(Integer unDrugIsinformedconsent) {
		this.unDrugIsinformedconsent = unDrugIsinformedconsent;
	}
	public Integer getUnDrugCrontrast() {
		return unDrugCrontrast;
	}
	public void setUnDrugCrontrast(Integer unDrugCrontrast) {
		this.unDrugCrontrast = unDrugCrontrast;
	}
	public Integer getUnDrugIsA() {
		return unDrugIsA;
	}
	public void setUnDrugIsA(Integer unDrugIsA) {
		this.unDrugIsA = unDrugIsA;
	}
	public Integer getUnDrugIsB() {
		return unDrugIsB;
	}
	public void setUnDrugIsB(Integer unDrugIsB) {
		this.unDrugIsB = unDrugIsB;
	}
	public Integer getUnDrugIsC() {
		return unDrugIsC;
	}
	public void setUnDrugIsC(Integer unDrugIsC) {
		this.unDrugIsC = unDrugIsC;
	}
	public String getInfoId() {
		return infoId;
	}
	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	public String getStorageDeptid() {
		return storageDeptid;
	}
	public void setStorageDeptid(String storageDeptid) {
		this.storageDeptid = storageDeptid;
	}
	public Double getStoreSum() {
		return storeSum;
	}
	public void setStoreSum(Double storeSum) {
		this.storeSum = storeSum;
	}
	public Double getPreoutSum() {
		return preoutSum;
	}
	public void setPreoutSum(Double preoutSum) {
		this.preoutSum = preoutSum;
	}
	public Integer getUnitFlag() {
		return unitFlag;
	}
	public void setUnitFlag(Integer unitFlag) {
		this.unitFlag = unitFlag;
	}
	public Integer getChangeFlag() {
		return changeFlag;
	}
	public void setChangeFlag(Integer changeFlag) {
		this.changeFlag = changeFlag;
	}
	public Integer getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}
	public String getLabsample() {
		return labsample;
	}
	public void setLabsample(String labsample) {
		this.labsample = labsample;
	}
	
}
