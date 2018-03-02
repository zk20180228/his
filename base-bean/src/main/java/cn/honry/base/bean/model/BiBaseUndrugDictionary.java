package cn.honry.base.bean.model;

/**
 * BiBaseUndrugDictionary entity. @author MyEclipse Persistence Tools
 */

public class BiBaseUndrugDictionary implements java.io.Serializable {

	// Fields

	private String undrugCode;
	private String undrugName;
	private String undrugGbcode;
	private String undrugGjcode;
	private String undrugSystypeCode;
	private String undrugSystype;
	private String undrugDecd;
	private String undrugDept;
	private String undrugMinifeeCode;
	private String undrugMinifee;
	private Double undrugDefaultprice;
	private Double undrugChildrenprice;
	private Double undrugSpecialprice;
	private Double undrugEmergencycaserate;
	private Double undrugOtherpricei;
	private Double undrugOtherpriceii;
	private String undrugUnitCode;
	private String undrugUnit;
	private String undrugSpec;
	private String undrugDefaultSample;
	private String undrugDefaultSampleCode;
	private String undrugLabsampleCode;
	private String undrugLabsample;
	private String undrugRequirements;
	private String undrugMedicalhistory;
	private String undrugDiseaseClassCode;
	private String undrugDiseaseClass;
	private String undrugApplication;
	private String undrugOperateCode;
	private String undrugOperateKind;
	private String undrugSpecialDecd;
	private String undrugSpecialDept;
	private String undrugItemlimit;
	private String undrugNotes;
	private String undrugScope;
	private String undrugEquipmentno;
	private String undrugOperationscale;
	private String undrugValidityrange;
	private String undrugIsprovincelimit;
	private String undrugIscitylimit;
	private String undrugIsownexpense;
	private String undrugIsspecificitems;
	private String undrugConfirmFlag;
	private String undrugIspreorder;
	private String undrugIsbirthcontrol;
	private String undrugIsinformedconsent;
	private String undrugCrontrast;
	private String undrugIsa;
	private String undrugIsb;
	private String undrugIsc;
	private String undrugIsstack;
	private String undrugValidState;
	private String undrugCollateFlag;
	private String undrugArAreaCode;
	private String undrugArArea;
	private String undrugRemark;
	private String specialFlag1;
	private String specialFlag2;
	private String specialFlag3;
	private String specialFlag4;
	private String specialFlag5;
	private String ext1;
	private String ext2;
	private String ext5;
	private String ext4;
	private String ext3;

	// Constructors

	/** default constructor */
	public BiBaseUndrugDictionary() {
	}

	/** full constructor */
	public BiBaseUndrugDictionary(String undrugName, String undrugGbcode,
			String undrugGjcode, String undrugSystypeCode,
			String undrugSystype, String undrugDecd, String undrugDept,
			String undrugMinifeeCode, String undrugMinifee,
			Double undrugDefaultprice, Double undrugChildrenprice,
			Double undrugSpecialprice, Double undrugEmergencycaserate,
			Double undrugOtherpricei, Double undrugOtherpriceii,
			String undrugUnitCode, String undrugUnit, String undrugSpec,
			String undrugDefaultSample, String undrugDefaultSampleCode,
			String undrugLabsampleCode, String undrugLabsample,
			String undrugRequirements, String undrugMedicalhistory,
			String undrugDiseaseClassCode, String undrugDiseaseClass,
			String undrugApplication, String undrugOperateCode,
			String undrugOperateKind, String undrugSpecialDecd,
			String undrugSpecialDept, String undrugItemlimit,
			String undrugNotes, String undrugScope, String undrugEquipmentno,
			String undrugOperationscale, String undrugValidityrange,
			String undrugIsprovincelimit, String undrugIscitylimit,
			String undrugIsownexpense, String undrugIsspecificitems,
			String undrugConfirmFlag, String undrugIspreorder,
			String undrugIsbirthcontrol, String undrugIsinformedconsent,
			String undrugCrontrast, String undrugIsa, String undrugIsb,
			String undrugIsc, String undrugIsstack, String undrugValidState,
			String undrugCollateFlag, String undrugArAreaCode,
			String undrugArArea, String undrugRemark, String specialFlag1,
			String specialFlag2, String specialFlag3, String specialFlag4,
			String specialFlag5, String ext1, String ext2, String ext5,
			String ext4, String ext3) {
		this.undrugName = undrugName;
		this.undrugGbcode = undrugGbcode;
		this.undrugGjcode = undrugGjcode;
		this.undrugSystypeCode = undrugSystypeCode;
		this.undrugSystype = undrugSystype;
		this.undrugDecd = undrugDecd;
		this.undrugDept = undrugDept;
		this.undrugMinifeeCode = undrugMinifeeCode;
		this.undrugMinifee = undrugMinifee;
		this.undrugDefaultprice = undrugDefaultprice;
		this.undrugChildrenprice = undrugChildrenprice;
		this.undrugSpecialprice = undrugSpecialprice;
		this.undrugEmergencycaserate = undrugEmergencycaserate;
		this.undrugOtherpricei = undrugOtherpricei;
		this.undrugOtherpriceii = undrugOtherpriceii;
		this.undrugUnitCode = undrugUnitCode;
		this.undrugUnit = undrugUnit;
		this.undrugSpec = undrugSpec;
		this.undrugDefaultSample = undrugDefaultSample;
		this.undrugDefaultSampleCode = undrugDefaultSampleCode;
		this.undrugLabsampleCode = undrugLabsampleCode;
		this.undrugLabsample = undrugLabsample;
		this.undrugRequirements = undrugRequirements;
		this.undrugMedicalhistory = undrugMedicalhistory;
		this.undrugDiseaseClassCode = undrugDiseaseClassCode;
		this.undrugDiseaseClass = undrugDiseaseClass;
		this.undrugApplication = undrugApplication;
		this.undrugOperateCode = undrugOperateCode;
		this.undrugOperateKind = undrugOperateKind;
		this.undrugSpecialDecd = undrugSpecialDecd;
		this.undrugSpecialDept = undrugSpecialDept;
		this.undrugItemlimit = undrugItemlimit;
		this.undrugNotes = undrugNotes;
		this.undrugScope = undrugScope;
		this.undrugEquipmentno = undrugEquipmentno;
		this.undrugOperationscale = undrugOperationscale;
		this.undrugValidityrange = undrugValidityrange;
		this.undrugIsprovincelimit = undrugIsprovincelimit;
		this.undrugIscitylimit = undrugIscitylimit;
		this.undrugIsownexpense = undrugIsownexpense;
		this.undrugIsspecificitems = undrugIsspecificitems;
		this.undrugConfirmFlag = undrugConfirmFlag;
		this.undrugIspreorder = undrugIspreorder;
		this.undrugIsbirthcontrol = undrugIsbirthcontrol;
		this.undrugIsinformedconsent = undrugIsinformedconsent;
		this.undrugCrontrast = undrugCrontrast;
		this.undrugIsa = undrugIsa;
		this.undrugIsb = undrugIsb;
		this.undrugIsc = undrugIsc;
		this.undrugIsstack = undrugIsstack;
		this.undrugValidState = undrugValidState;
		this.undrugCollateFlag = undrugCollateFlag;
		this.undrugArAreaCode = undrugArAreaCode;
		this.undrugArArea = undrugArArea;
		this.undrugRemark = undrugRemark;
		this.specialFlag1 = specialFlag1;
		this.specialFlag2 = specialFlag2;
		this.specialFlag3 = specialFlag3;
		this.specialFlag4 = specialFlag4;
		this.specialFlag5 = specialFlag5;
		this.ext1 = ext1;
		this.ext2 = ext2;
		this.ext5 = ext5;
		this.ext4 = ext4;
		this.ext3 = ext3;
	}

	// Property accessors

	public String getUndrugCode() {
		return this.undrugCode;
	}

	public void setUndrugCode(String undrugCode) {
		this.undrugCode = undrugCode;
	}

	public String getUndrugName() {
		return this.undrugName;
	}

	public void setUndrugName(String undrugName) {
		this.undrugName = undrugName;
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

	public String getUndrugSystypeCode() {
		return this.undrugSystypeCode;
	}

	public void setUndrugSystypeCode(String undrugSystypeCode) {
		this.undrugSystypeCode = undrugSystypeCode;
	}

	public String getUndrugSystype() {
		return this.undrugSystype;
	}

	public void setUndrugSystype(String undrugSystype) {
		this.undrugSystype = undrugSystype;
	}

	public String getUndrugDecd() {
		return this.undrugDecd;
	}

	public void setUndrugDecd(String undrugDecd) {
		this.undrugDecd = undrugDecd;
	}

	public String getUndrugDept() {
		return this.undrugDept;
	}

	public void setUndrugDept(String undrugDept) {
		this.undrugDept = undrugDept;
	}

	public String getUndrugMinifeeCode() {
		return this.undrugMinifeeCode;
	}

	public void setUndrugMinifeeCode(String undrugMinifeeCode) {
		this.undrugMinifeeCode = undrugMinifeeCode;
	}

	public String getUndrugMinifee() {
		return this.undrugMinifee;
	}

	public void setUndrugMinifee(String undrugMinifee) {
		this.undrugMinifee = undrugMinifee;
	}

	public Double getUndrugDefaultprice() {
		return this.undrugDefaultprice;
	}

	public void setUndrugDefaultprice(Double undrugDefaultprice) {
		this.undrugDefaultprice = undrugDefaultprice;
	}

	public Double getUndrugChildrenprice() {
		return this.undrugChildrenprice;
	}

	public void setUndrugChildrenprice(Double undrugChildrenprice) {
		this.undrugChildrenprice = undrugChildrenprice;
	}

	public Double getUndrugSpecialprice() {
		return this.undrugSpecialprice;
	}

	public void setUndrugSpecialprice(Double undrugSpecialprice) {
		this.undrugSpecialprice = undrugSpecialprice;
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

	public String getUndrugUnitCode() {
		return this.undrugUnitCode;
	}

	public void setUndrugUnitCode(String undrugUnitCode) {
		this.undrugUnitCode = undrugUnitCode;
	}

	public String getUndrugUnit() {
		return this.undrugUnit;
	}

	public void setUndrugUnit(String undrugUnit) {
		this.undrugUnit = undrugUnit;
	}

	public String getUndrugSpec() {
		return this.undrugSpec;
	}

	public void setUndrugSpec(String undrugSpec) {
		this.undrugSpec = undrugSpec;
	}

	public String getUndrugDefaultSample() {
		return this.undrugDefaultSample;
	}

	public void setUndrugDefaultSample(String undrugDefaultSample) {
		this.undrugDefaultSample = undrugDefaultSample;
	}

	public String getUndrugDefaultSampleCode() {
		return this.undrugDefaultSampleCode;
	}

	public void setUndrugDefaultSampleCode(String undrugDefaultSampleCode) {
		this.undrugDefaultSampleCode = undrugDefaultSampleCode;
	}

	public String getUndrugLabsampleCode() {
		return this.undrugLabsampleCode;
	}

	public void setUndrugLabsampleCode(String undrugLabsampleCode) {
		this.undrugLabsampleCode = undrugLabsampleCode;
	}

	public String getUndrugLabsample() {
		return this.undrugLabsample;
	}

	public void setUndrugLabsample(String undrugLabsample) {
		this.undrugLabsample = undrugLabsample;
	}

	public String getUndrugRequirements() {
		return this.undrugRequirements;
	}

	public void setUndrugRequirements(String undrugRequirements) {
		this.undrugRequirements = undrugRequirements;
	}

	public String getUndrugMedicalhistory() {
		return this.undrugMedicalhistory;
	}

	public void setUndrugMedicalhistory(String undrugMedicalhistory) {
		this.undrugMedicalhistory = undrugMedicalhistory;
	}

	public String getUndrugDiseaseClassCode() {
		return this.undrugDiseaseClassCode;
	}

	public void setUndrugDiseaseClassCode(String undrugDiseaseClassCode) {
		this.undrugDiseaseClassCode = undrugDiseaseClassCode;
	}

	public String getUndrugDiseaseClass() {
		return this.undrugDiseaseClass;
	}

	public void setUndrugDiseaseClass(String undrugDiseaseClass) {
		this.undrugDiseaseClass = undrugDiseaseClass;
	}

	public String getUndrugApplication() {
		return this.undrugApplication;
	}

	public void setUndrugApplication(String undrugApplication) {
		this.undrugApplication = undrugApplication;
	}

	public String getUndrugOperateCode() {
		return this.undrugOperateCode;
	}

	public void setUndrugOperateCode(String undrugOperateCode) {
		this.undrugOperateCode = undrugOperateCode;
	}

	public String getUndrugOperateKind() {
		return this.undrugOperateKind;
	}

	public void setUndrugOperateKind(String undrugOperateKind) {
		this.undrugOperateKind = undrugOperateKind;
	}

	public String getUndrugSpecialDecd() {
		return this.undrugSpecialDecd;
	}

	public void setUndrugSpecialDecd(String undrugSpecialDecd) {
		this.undrugSpecialDecd = undrugSpecialDecd;
	}

	public String getUndrugSpecialDept() {
		return this.undrugSpecialDept;
	}

	public void setUndrugSpecialDept(String undrugSpecialDept) {
		this.undrugSpecialDept = undrugSpecialDept;
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

	public String getUndrugValidityrange() {
		return this.undrugValidityrange;
	}

	public void setUndrugValidityrange(String undrugValidityrange) {
		this.undrugValidityrange = undrugValidityrange;
	}

	public String getUndrugIsprovincelimit() {
		return this.undrugIsprovincelimit;
	}

	public void setUndrugIsprovincelimit(String undrugIsprovincelimit) {
		this.undrugIsprovincelimit = undrugIsprovincelimit;
	}

	public String getUndrugIscitylimit() {
		return this.undrugIscitylimit;
	}

	public void setUndrugIscitylimit(String undrugIscitylimit) {
		this.undrugIscitylimit = undrugIscitylimit;
	}

	public String getUndrugIsownexpense() {
		return this.undrugIsownexpense;
	}

	public void setUndrugIsownexpense(String undrugIsownexpense) {
		this.undrugIsownexpense = undrugIsownexpense;
	}

	public String getUndrugIsspecificitems() {
		return this.undrugIsspecificitems;
	}

	public void setUndrugIsspecificitems(String undrugIsspecificitems) {
		this.undrugIsspecificitems = undrugIsspecificitems;
	}

	public String getUndrugConfirmFlag() {
		return this.undrugConfirmFlag;
	}

	public void setUndrugConfirmFlag(String undrugConfirmFlag) {
		this.undrugConfirmFlag = undrugConfirmFlag;
	}

	public String getUndrugIspreorder() {
		return this.undrugIspreorder;
	}

	public void setUndrugIspreorder(String undrugIspreorder) {
		this.undrugIspreorder = undrugIspreorder;
	}

	public String getUndrugIsbirthcontrol() {
		return this.undrugIsbirthcontrol;
	}

	public void setUndrugIsbirthcontrol(String undrugIsbirthcontrol) {
		this.undrugIsbirthcontrol = undrugIsbirthcontrol;
	}

	public String getUndrugIsinformedconsent() {
		return this.undrugIsinformedconsent;
	}

	public void setUndrugIsinformedconsent(String undrugIsinformedconsent) {
		this.undrugIsinformedconsent = undrugIsinformedconsent;
	}

	public String getUndrugCrontrast() {
		return this.undrugCrontrast;
	}

	public void setUndrugCrontrast(String undrugCrontrast) {
		this.undrugCrontrast = undrugCrontrast;
	}

	public String getUndrugIsa() {
		return this.undrugIsa;
	}

	public void setUndrugIsa(String undrugIsa) {
		this.undrugIsa = undrugIsa;
	}

	public String getUndrugIsb() {
		return this.undrugIsb;
	}

	public void setUndrugIsb(String undrugIsb) {
		this.undrugIsb = undrugIsb;
	}

	public String getUndrugIsc() {
		return this.undrugIsc;
	}

	public void setUndrugIsc(String undrugIsc) {
		this.undrugIsc = undrugIsc;
	}

	public String getUndrugIsstack() {
		return this.undrugIsstack;
	}

	public void setUndrugIsstack(String undrugIsstack) {
		this.undrugIsstack = undrugIsstack;
	}

	public String getUndrugValidState() {
		return this.undrugValidState;
	}

	public void setUndrugValidState(String undrugValidState) {
		this.undrugValidState = undrugValidState;
	}

	public String getUndrugCollateFlag() {
		return this.undrugCollateFlag;
	}

	public void setUndrugCollateFlag(String undrugCollateFlag) {
		this.undrugCollateFlag = undrugCollateFlag;
	}

	public String getUndrugArAreaCode() {
		return this.undrugArAreaCode;
	}

	public void setUndrugArAreaCode(String undrugArAreaCode) {
		this.undrugArAreaCode = undrugArAreaCode;
	}

	public String getUndrugArArea() {
		return this.undrugArArea;
	}

	public void setUndrugArArea(String undrugArArea) {
		this.undrugArArea = undrugArArea;
	}

	public String getUndrugRemark() {
		return this.undrugRemark;
	}

	public void setUndrugRemark(String undrugRemark) {
		this.undrugRemark = undrugRemark;
	}

	public String getSpecialFlag1() {
		return this.specialFlag1;
	}

	public void setSpecialFlag1(String specialFlag1) {
		this.specialFlag1 = specialFlag1;
	}

	public String getSpecialFlag2() {
		return this.specialFlag2;
	}

	public void setSpecialFlag2(String specialFlag2) {
		this.specialFlag2 = specialFlag2;
	}

	public String getSpecialFlag3() {
		return this.specialFlag3;
	}

	public void setSpecialFlag3(String specialFlag3) {
		this.specialFlag3 = specialFlag3;
	}

	public String getSpecialFlag4() {
		return this.specialFlag4;
	}

	public void setSpecialFlag4(String specialFlag4) {
		this.specialFlag4 = specialFlag4;
	}

	public String getSpecialFlag5() {
		return this.specialFlag5;
	}

	public void setSpecialFlag5(String specialFlag5) {
		this.specialFlag5 = specialFlag5;
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

	public String getExt5() {
		return this.ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

	public String getExt4() {
		return this.ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt3() {
		return this.ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

}