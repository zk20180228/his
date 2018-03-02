package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

@SuppressWarnings("serial")
public class DrugUndrug extends Entity implements java.io.Serializable {

	// Fields
	/**项目名称  **/
	private String name;
	/** 名称拼音码 **/
	private String undrugPinyin;
	/**名称五笔码  **/
	private String undrugWb;
	/**名称自定义码  **/
	private String undrugInputcode;
	/**国家编码  **/
	private String undrugGbcode;
	/** 国际编码 **/
	private String undrugGjcode;
	/**系统类别  **/
	private String undrugSystype;
	/**状态  **/
	private Integer undrugState;
	/**执行科室  **/
	private String undrugDept;
	/**项目约束  **/
	private String undrugItemlimit;
	/** 注意事项 **/
	private String undrugNotes;
	/**项目范围  **/
	private String undrugScope;
	/**检查要求  **/
	private String undrugRequirements;
	/** 最小费用代码 **/
	private String undrugMinimumcost;
	/**检查部位或标本  **/
	private String undrugInspectionsite;
	/** 病史检查 **/
	private String undrugMedicalhistory;
	/** 默认价 **/
	private Double defaultprice=0.0;
	/** 儿童价 **/
	private Double childrenprice=0.0;
	/** 特诊价 **/
	private Double specialprice=0.0;
	/** 急诊比例 **/
	private Double undrugEmergencycaserate;
	/**其他价1  **/
	private Double undrugOtherpricei=0.0;
	/**其他价2  **/
	private Double undrugOtherpriceii=0.0;
	/**单位  **/
	private String unit;
	/** 规格 **/
	private String spec;
	/** 申请单名称 **/
	private String undrugApplication;
	/**疾病分类  **/
	private String undrugDiseaseclassification;
	/** 手术编码 **/
	private String undrugOperationcode;
	/** 手术分类 **/
	private String undrugOperationtype;
	/**专科名称  **/
	private String undrugSpecialtyname;
	/** 设备编号 **/
	private String undrugEquipmentno;
	/** 手术规模 **/
	private String undrugOperationscale;
	/**有效范围	  **/
	private Integer undrugValidityrange=0;
	/**  是否省限制**/
	private Integer undrugIsprovincelimit;
	/**是否市限制  **/
	private Integer undrugIscitylimit;
	/**是否自费  **/
	private Integer undrugIsownexpense;
	/**是否确认  **/
	private Integer undrugIssubmit;
	/**是否需要预约  **/
	private Integer undrugIspreorder;
	/**是否计划生育  **/
	private Integer undrugIsbirthcontrol;
	/**是否特定项目  **/
	private Integer undrugIsspecificitems;
	/**是否知情同意书  **/
	private Integer undrugIsinformedconsent=0;
	/**是否对照  **/
	private Integer undrugCrontrast;
	/**是否甲类  **/
	private Integer undrugIsa=0;
	/**是否乙类 **/
	private Integer undrugIsb=0;
	/**是否丙类  **/
	private Integer undrugIsc=0;
	/**是否组套  **/
	private Integer undrugIsstack=0;
	/**备注  **/
	private String undrugRemark;
	/**新加字段   样本类型**/
	private String undrugLabsample;
	
	/**当前页数  **/
	private String page;
	/**每页行数  **/
	private String rows;
	// Property accessors
	//数据库无关的字段
	/**包装数量**/
	private Integer packagingnum;
	//数据库无关字段
	//统计大类代码
	private String feeTypeName;
	//新加字段  非药品code lyy
	private String code;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getUndrugPinyin() {
		return this.undrugPinyin;
	}

	public void setUndrugPinyin(String undrugPinyin) {
		this.undrugPinyin = undrugPinyin;
	}

	public String getUndrugWb() {
		return this.undrugWb;
	}

	public void setUndrugWb(String undrugWb) {
		this.undrugWb = undrugWb;
	}

	public String getUndrugInputcode() {
		return this.undrugInputcode;
	}

	public void setUndrugInputcode(String undrugInputcode) {
		this.undrugInputcode = undrugInputcode;
	}

	public String getUndrugGbcode() {
		return this.undrugGbcode;
	}

	public void setUndrugGbcode(String undrugGbcode) {
		this.undrugGbcode = undrugGbcode;
	}

	public String getUndrugGjcode() {
		return this.undrugGjcode;
	}

	public void setUndrugGjcode(String undrugGjcode) {
		this.undrugGjcode = undrugGjcode;
	}

	public String getUndrugSystype() {
		return this.undrugSystype;
	}

	public void setUndrugSystype(String undrugSystype) {
		this.undrugSystype = undrugSystype;
	}

	public Integer getUndrugState() {
		return this.undrugState;
	}

	public void setUndrugState(Integer undrugState) {
		this.undrugState = undrugState;
	}

	public String getUndrugDept() {
		return this.undrugDept;
	}

	public void setUndrugDept(String undrugDept) {
		this.undrugDept = undrugDept;
	}

	public String getUndrugItemlimit() {
		return this.undrugItemlimit;
	}

	public void setUndrugItemlimit(String undrugItemlimit) {
		this.undrugItemlimit = undrugItemlimit;
	}

	public String getUndrugNotes() {
		return this.undrugNotes;
	}

	public void setUndrugNotes(String undrugNotes) {
		this.undrugNotes = undrugNotes;
	}

	public String getUndrugScope() {
		return this.undrugScope;
	}

	public void setUndrugScope(String undrugScope) {
		this.undrugScope = undrugScope;
	}

	public String getUndrugRequirements() {
		return this.undrugRequirements;
	}

	public void setUndrugRequirements(String undrugRequirements) {
		this.undrugRequirements = undrugRequirements;
	}

	public String getUndrugMinimumcost() {
		return this.undrugMinimumcost;
	}

	public void setUndrugMinimumcost(String undrugMinimumcost) {
		this.undrugMinimumcost = undrugMinimumcost;
	}

	public String getUndrugInspectionsite() {
		return this.undrugInspectionsite;
	}

	public void setUndrugInspectionsite(String undrugInspectionsite) {
		this.undrugInspectionsite = undrugInspectionsite;
	}

	public String getUndrugMedicalhistory() {
		return this.undrugMedicalhistory;
	}

	public void setUndrugMedicalhistory(String undrugMedicalhistory) {
		this.undrugMedicalhistory = undrugMedicalhistory;
	}

	public Double getUndrugEmergencycaserate() {
		return this.undrugEmergencycaserate;
	}

	public void setUndrugEmergencycaserate(Double undrugEmergencycaserate) {
		this.undrugEmergencycaserate = undrugEmergencycaserate;
	}

	public Double getUndrugOtherpricei() {
		return this.undrugOtherpricei;
	}

	public void setUndrugOtherpricei(Double undrugOtherpricei) {
		this.undrugOtherpricei = undrugOtherpricei;
	}

	public Double getUndrugOtherpriceii() {
		return this.undrugOtherpriceii;
	}

	public void setUndrugOtherpriceii(Double undrugOtherpriceii) {
		this.undrugOtherpriceii = undrugOtherpriceii;
	}


	public String getUndrugApplication() {
		return this.undrugApplication;
	}

	public void setUndrugApplication(String undrugApplication) {
		this.undrugApplication = undrugApplication;
	}

	public String getUndrugDiseaseclassification() {
		return this.undrugDiseaseclassification;
	}

	public void setUndrugDiseaseclassification(
			String undrugDiseaseclassification) {
		this.undrugDiseaseclassification = undrugDiseaseclassification;
	}

	public String getUndrugOperationcode() {
		return this.undrugOperationcode;
	}

	public void setUndrugOperationcode(String undrugOperationcode) {
		this.undrugOperationcode = undrugOperationcode;
	}

	public String getUndrugOperationtype() {
		return this.undrugOperationtype;
	}

	public void setUndrugOperationtype(String undrugOperationtype) {
		this.undrugOperationtype = undrugOperationtype;
	}

	public String getUndrugSpecialtyname() {
		return this.undrugSpecialtyname;
	}

	public void setUndrugSpecialtyname(String undrugSpecialtyname) {
		this.undrugSpecialtyname = undrugSpecialtyname;
	}

	public String getUndrugEquipmentno() {
		return this.undrugEquipmentno;
	}

	public void setUndrugEquipmentno(String undrugEquipmentno) {
		this.undrugEquipmentno = undrugEquipmentno;
	}

	public String getUndrugOperationscale() {
		return this.undrugOperationscale;
	}

	public void setUndrugOperationscale(String undrugOperationscale) {
		this.undrugOperationscale = undrugOperationscale;
	}

	public Integer getUndrugValidityrange() {
		return this.undrugValidityrange;
	}

	public void setUndrugValidityrange(Integer undrugValidityrange) {
		this.undrugValidityrange = undrugValidityrange;
	}

	public Integer getUndrugIsprovincelimit() {
		return this.undrugIsprovincelimit;
	}

	public void setUndrugIsprovincelimit(Integer undrugIsprovincelimit) {
		this.undrugIsprovincelimit = undrugIsprovincelimit;
	}

	public Integer getUndrugIscitylimit() {
		return this.undrugIscitylimit;
	}

	public void setUndrugIscitylimit(Integer undrugIscitylimit) {
		this.undrugIscitylimit = undrugIscitylimit;
	}

	public Integer getUndrugIsownexpense() {
		return this.undrugIsownexpense;
	}

	public void setUndrugIsownexpense(Integer undrugIsownexpense) {
		this.undrugIsownexpense = undrugIsownexpense;
	}

	public Integer getUndrugIssubmit() {
		return this.undrugIssubmit;
	}

	public void setUndrugIssubmit(Integer undrugIssubmit) {
		this.undrugIssubmit = undrugIssubmit;
	}

	public Integer getUndrugIspreorder() {
		return this.undrugIspreorder;
	}

	public void setUndrugIspreorder(Integer undrugIspreorder) {
		this.undrugIspreorder = undrugIspreorder;
	}

	public Integer getUndrugIsbirthcontrol() {
		return this.undrugIsbirthcontrol;
	}

	public void setUndrugIsbirthcontrol(Integer undrugIsbirthcontrol) {
		this.undrugIsbirthcontrol = undrugIsbirthcontrol;
	}

	public Integer getUndrugIsspecificitems() {
		return this.undrugIsspecificitems;
	}

	public void setUndrugIsspecificitems(Integer undrugIsspecificitems) {
		this.undrugIsspecificitems = undrugIsspecificitems;
	}

	public Integer getUndrugIsinformedconsent() {
		return this.undrugIsinformedconsent;
	}

	public void setUndrugIsinformedconsent(Integer undrugIsinformedconsent) {
		this.undrugIsinformedconsent = undrugIsinformedconsent;
	}

	public Integer getUndrugCrontrast() {
		return this.undrugCrontrast;
	}

	public void setUndrugCrontrast(Integer undrugCrontrast) {
		this.undrugCrontrast = undrugCrontrast;
	}

	public Integer getUndrugIsa() {
		return this.undrugIsa;
	}

	public void setUndrugIsa(Integer undrugIsa) {
		this.undrugIsa = undrugIsa;
	}

	public Integer getUndrugIsb() {
		return this.undrugIsb;
	}

	public void setUndrugIsb(Integer undrugIsb) {
		this.undrugIsb = undrugIsb;
	}

	public Integer getUndrugIsc() {
		return this.undrugIsc;
	}

	public void setUndrugIsc(Integer undrugIsc) {
		this.undrugIsc = undrugIsc;
	}

	public Integer getUndrugIsstack() {
		return this.undrugIsstack;
	}

	public void setUndrugIsstack(Integer undrugIsstack) {
		this.undrugIsstack = undrugIsstack;
	}

	public String getUndrugRemark() {
		return this.undrugRemark;
	}

	public void setUndrugRemark(String undrugRemark) {
		this.undrugRemark = undrugRemark;
	}
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getSpec() {
		return spec;
	}

	public void setPackagingnum(Integer packagingnum) {
		this.packagingnum = packagingnum;
	}

	public Integer getPackagingnum() {
		return packagingnum;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

	public void setDefaultprice(Double defaultprice) {
		this.defaultprice = defaultprice;
	}

	public Double getDefaultprice() {
		return defaultprice;
	}

	public void setChildrenprice(Double childrenprice) {
		this.childrenprice = childrenprice;
	}

	public Double getChildrenprice() {
		return childrenprice;
	}

	public void setSpecialprice(Double specialprice) {
		this.specialprice = specialprice;
	}

	public Double getSpecialprice() {
		return specialprice;
	}

	public String getUndrugLabsample() {
		return undrugLabsample;
	}

	public void setUndrugLabsample(String undrugLabsample) {
		this.undrugLabsample = undrugLabsample;
	}

	public String getFeeTypeName() {
		return feeTypeName;
	}

	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
	}
	
}