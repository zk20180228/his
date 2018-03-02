package cn.honry.base.bean.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * BiBaseDrugDictionary entity. @author MyEclipse Persistence Tools
 */

public class BiBaseDrugDictionary implements java.io.Serializable {

	// Fields

	private String drugCode;
	private String drugTradeName;
	private String drugCommName;
	private String drugFormalName;
	private String drugOtherName;
	private String drugEnname;
	private String drugEnalias;
	private String drugEncommonname;
	private String drugGbcode;
	private String drugClassCode;
	private String drugMiniFeeCode;
	private String drugTypeCode;
	private String drugNatureCode;
	private String drugKindCode;
	private String drugGrade;
	private String drugSpec;
	private Double drugWeight;
	private String drugWeightUnitCode;
	private Double drugVolum;
	private String drugVolUnitCode;
	private String drugPackUnitCode;
	private String drugMinUnitCode;
	private String drugDoseModelCode;
	private String drugDoseunitCode;
	private String drugFrequencyCode;
	private String drugUsageCode;
	private BigDecimal drugPackQty;
	private Double drugBasicdose;
	private Double drugOncedosage;
	private Short drugEffMonth;
	private Double drugRetailprice;
	private Double drugMaxretailprice;
	private Double drugWholesaleprice;
	private Double drugPurchaseprice;
	private String drugPricetype;
	private String drugSplitType;
	private String drugInfusionFlg;
	private String drugOwncostFlg;
	private String drugSupriceFlg;
	private String drugIsnew;
	private String drugAppendFlg;
	private String drugIsself;
	private String drugIstest;
	private String drugIsgmp;
	private String drugIsotc;
	private String drugIslack;
	private String drugIsterminalconfirm;
	private String drugIsagreementprescription;
	private String drugIscooperativemedical;
	private String drugIsprovincelimit;
	private String drugIscitylimit;
	private String drugRemark;
	private String drugValidState;
	private String drugStorage;
	private String drugNotes;
	private String drugActiveingredient;
	private String drugPhyFunction1;
	private String drugPhyFunction2;
	private String drugPhyFunction3;
	private Date drugEntertime;
	private String drugManufacturer;
	private String drugSupplycompany;
	private String drugDocument;
	private String drugTrademark;
	private String drugPlaceoforigin;
	private String drugOperativenorm;
	private String drugLibid;
	private String drugLibno;
	private String drugBarcode;
	private String drugBrev;
	private String drugInstruction;
	private Boolean drugIstender;
	private String drugBiddingcode;
	private Double drugBiddingprice;
	private String drugTenderCompanyCode;
	private String drugContractcode;
	private Date drugStartdate;
	private Date drugEnddate;
	private Date drugCreateTime;
	private Date operDate;
	private String operCode;
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
	public BiBaseDrugDictionary() {
	}

	/** full constructor */
	public BiBaseDrugDictionary(String drugTradeName, String drugCommName,
			String drugFormalName, String drugOtherName, String drugEnname,
			String drugEnalias, String drugEncommonname, String drugGbcode,
			String drugClassCode, String drugMiniFeeCode, String drugTypeCode,
			String drugNatureCode, String drugKindCode, String drugGrade,
			String drugSpec, Double drugWeight, String drugWeightUnitCode,
			Double drugVolum, String drugVolUnitCode, String drugPackUnitCode,
			String drugMinUnitCode, String drugDoseModelCode,
			String drugDoseunitCode, String drugFrequencyCode,
			String drugUsageCode, BigDecimal drugPackQty, Double drugBasicdose,
			Double drugOncedosage, Short drugEffMonth, Double drugRetailprice,
			Double drugMaxretailprice, Double drugWholesaleprice,
			Double drugPurchaseprice, String drugPricetype,
			String drugSplitType, String drugInfusionFlg,
			String drugOwncostFlg, String drugSupriceFlg, String drugIsnew,
			String drugAppendFlg, String drugIsself, String drugIstest,
			String drugIsgmp, String drugIsotc, String drugIslack,
			String drugIsterminalconfirm, String drugIsagreementprescription,
			String drugIscooperativemedical, String drugIsprovincelimit,
			String drugIscitylimit, String drugRemark, String drugValidState,
			String drugStorage, String drugNotes, String drugActiveingredient,
			String drugPhyFunction1, String drugPhyFunction2,
			String drugPhyFunction3, Date drugEntertime,
			String drugManufacturer, String drugSupplycompany,
			String drugDocument, String drugTrademark,
			String drugPlaceoforigin, String drugOperativenorm,
			String drugLibid, String drugLibno, String drugBarcode,
			String drugBrev, String drugInstruction, Boolean drugIstender,
			String drugBiddingcode, Double drugBiddingprice,
			String drugTenderCompanyCode, String drugContractcode,
			Date drugStartdate, Date drugEnddate, Date drugCreateTime,
			Date operDate, String operCode, String specialFlag1,
			String specialFlag2, String specialFlag3, String specialFlag4,
			String specialFlag5, String ext1, String ext2, String ext5,
			String ext4, String ext3) {
		this.drugTradeName = drugTradeName;
		this.drugCommName = drugCommName;
		this.drugFormalName = drugFormalName;
		this.drugOtherName = drugOtherName;
		this.drugEnname = drugEnname;
		this.drugEnalias = drugEnalias;
		this.drugEncommonname = drugEncommonname;
		this.drugGbcode = drugGbcode;
		this.drugClassCode = drugClassCode;
		this.drugMiniFeeCode = drugMiniFeeCode;
		this.drugTypeCode = drugTypeCode;
		this.drugNatureCode = drugNatureCode;
		this.drugKindCode = drugKindCode;
		this.drugGrade = drugGrade;
		this.drugSpec = drugSpec;
		this.drugWeight = drugWeight;
		this.drugWeightUnitCode = drugWeightUnitCode;
		this.drugVolum = drugVolum;
		this.drugVolUnitCode = drugVolUnitCode;
		this.drugPackUnitCode = drugPackUnitCode;
		this.drugMinUnitCode = drugMinUnitCode;
		this.drugDoseModelCode = drugDoseModelCode;
		this.drugDoseunitCode = drugDoseunitCode;
		this.drugFrequencyCode = drugFrequencyCode;
		this.drugUsageCode = drugUsageCode;
		this.drugPackQty = drugPackQty;
		this.drugBasicdose = drugBasicdose;
		this.drugOncedosage = drugOncedosage;
		this.drugEffMonth = drugEffMonth;
		this.drugRetailprice = drugRetailprice;
		this.drugMaxretailprice = drugMaxretailprice;
		this.drugWholesaleprice = drugWholesaleprice;
		this.drugPurchaseprice = drugPurchaseprice;
		this.drugPricetype = drugPricetype;
		this.drugSplitType = drugSplitType;
		this.drugInfusionFlg = drugInfusionFlg;
		this.drugOwncostFlg = drugOwncostFlg;
		this.drugSupriceFlg = drugSupriceFlg;
		this.drugIsnew = drugIsnew;
		this.drugAppendFlg = drugAppendFlg;
		this.drugIsself = drugIsself;
		this.drugIstest = drugIstest;
		this.drugIsgmp = drugIsgmp;
		this.drugIsotc = drugIsotc;
		this.drugIslack = drugIslack;
		this.drugIsterminalconfirm = drugIsterminalconfirm;
		this.drugIsagreementprescription = drugIsagreementprescription;
		this.drugIscooperativemedical = drugIscooperativemedical;
		this.drugIsprovincelimit = drugIsprovincelimit;
		this.drugIscitylimit = drugIscitylimit;
		this.drugRemark = drugRemark;
		this.drugValidState = drugValidState;
		this.drugStorage = drugStorage;
		this.drugNotes = drugNotes;
		this.drugActiveingredient = drugActiveingredient;
		this.drugPhyFunction1 = drugPhyFunction1;
		this.drugPhyFunction2 = drugPhyFunction2;
		this.drugPhyFunction3 = drugPhyFunction3;
		this.drugEntertime = drugEntertime;
		this.drugManufacturer = drugManufacturer;
		this.drugSupplycompany = drugSupplycompany;
		this.drugDocument = drugDocument;
		this.drugTrademark = drugTrademark;
		this.drugPlaceoforigin = drugPlaceoforigin;
		this.drugOperativenorm = drugOperativenorm;
		this.drugLibid = drugLibid;
		this.drugLibno = drugLibno;
		this.drugBarcode = drugBarcode;
		this.drugBrev = drugBrev;
		this.drugInstruction = drugInstruction;
		this.drugIstender = drugIstender;
		this.drugBiddingcode = drugBiddingcode;
		this.drugBiddingprice = drugBiddingprice;
		this.drugTenderCompanyCode = drugTenderCompanyCode;
		this.drugContractcode = drugContractcode;
		this.drugStartdate = drugStartdate;
		this.drugEnddate = drugEnddate;
		this.drugCreateTime = drugCreateTime;
		this.operDate = operDate;
		this.operCode = operCode;
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

	public String getDrugCode() {
		return this.drugCode;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}

	public String getDrugTradeName() {
		return this.drugTradeName;
	}

	public void setDrugTradeName(String drugTradeName) {
		this.drugTradeName = drugTradeName;
	}

	public String getDrugCommName() {
		return this.drugCommName;
	}

	public void setDrugCommName(String drugCommName) {
		this.drugCommName = drugCommName;
	}

	public String getDrugFormalName() {
		return this.drugFormalName;
	}

	public void setDrugFormalName(String drugFormalName) {
		this.drugFormalName = drugFormalName;
	}

	public String getDrugOtherName() {
		return this.drugOtherName;
	}

	public void setDrugOtherName(String drugOtherName) {
		this.drugOtherName = drugOtherName;
	}

	public String getDrugEnname() {
		return this.drugEnname;
	}

	public void setDrugEnname(String drugEnname) {
		this.drugEnname = drugEnname;
	}

	public String getDrugEnalias() {
		return this.drugEnalias;
	}

	public void setDrugEnalias(String drugEnalias) {
		this.drugEnalias = drugEnalias;
	}

	public String getDrugEncommonname() {
		return this.drugEncommonname;
	}

	public void setDrugEncommonname(String drugEncommonname) {
		this.drugEncommonname = drugEncommonname;
	}

	public String getDrugGbcode() {
		return this.drugGbcode;
	}

	public void setDrugGbcode(String drugGbcode) {
		this.drugGbcode = drugGbcode;
	}

	public String getDrugClassCode() {
		return this.drugClassCode;
	}

	public void setDrugClassCode(String drugClassCode) {
		this.drugClassCode = drugClassCode;
	}

	public String getDrugMiniFeeCode() {
		return this.drugMiniFeeCode;
	}

	public void setDrugMiniFeeCode(String drugMiniFeeCode) {
		this.drugMiniFeeCode = drugMiniFeeCode;
	}

	public String getDrugTypeCode() {
		return this.drugTypeCode;
	}

	public void setDrugTypeCode(String drugTypeCode) {
		this.drugTypeCode = drugTypeCode;
	}

	public String getDrugNatureCode() {
		return this.drugNatureCode;
	}

	public void setDrugNatureCode(String drugNatureCode) {
		this.drugNatureCode = drugNatureCode;
	}

	public String getDrugKindCode() {
		return this.drugKindCode;
	}

	public void setDrugKindCode(String drugKindCode) {
		this.drugKindCode = drugKindCode;
	}

	public String getDrugGrade() {
		return this.drugGrade;
	}

	public void setDrugGrade(String drugGrade) {
		this.drugGrade = drugGrade;
	}

	public String getDrugSpec() {
		return this.drugSpec;
	}

	public void setDrugSpec(String drugSpec) {
		this.drugSpec = drugSpec;
	}

	public Double getDrugWeight() {
		return this.drugWeight;
	}

	public void setDrugWeight(Double drugWeight) {
		this.drugWeight = drugWeight;
	}

	public String getDrugWeightUnitCode() {
		return this.drugWeightUnitCode;
	}

	public void setDrugWeightUnitCode(String drugWeightUnitCode) {
		this.drugWeightUnitCode = drugWeightUnitCode;
	}

	public Double getDrugVolum() {
		return this.drugVolum;
	}

	public void setDrugVolum(Double drugVolum) {
		this.drugVolum = drugVolum;
	}

	public String getDrugVolUnitCode() {
		return this.drugVolUnitCode;
	}

	public void setDrugVolUnitCode(String drugVolUnitCode) {
		this.drugVolUnitCode = drugVolUnitCode;
	}

	public String getDrugPackUnitCode() {
		return this.drugPackUnitCode;
	}

	public void setDrugPackUnitCode(String drugPackUnitCode) {
		this.drugPackUnitCode = drugPackUnitCode;
	}

	public String getDrugMinUnitCode() {
		return this.drugMinUnitCode;
	}

	public void setDrugMinUnitCode(String drugMinUnitCode) {
		this.drugMinUnitCode = drugMinUnitCode;
	}

	public String getDrugDoseModelCode() {
		return this.drugDoseModelCode;
	}

	public void setDrugDoseModelCode(String drugDoseModelCode) {
		this.drugDoseModelCode = drugDoseModelCode;
	}

	public String getDrugDoseunitCode() {
		return this.drugDoseunitCode;
	}

	public void setDrugDoseunitCode(String drugDoseunitCode) {
		this.drugDoseunitCode = drugDoseunitCode;
	}

	public String getDrugFrequencyCode() {
		return this.drugFrequencyCode;
	}

	public void setDrugFrequencyCode(String drugFrequencyCode) {
		this.drugFrequencyCode = drugFrequencyCode;
	}

	public String getDrugUsageCode() {
		return this.drugUsageCode;
	}

	public void setDrugUsageCode(String drugUsageCode) {
		this.drugUsageCode = drugUsageCode;
	}

	public BigDecimal getDrugPackQty() {
		return this.drugPackQty;
	}

	public void setDrugPackQty(BigDecimal drugPackQty) {
		this.drugPackQty = drugPackQty;
	}

	public Double getDrugBasicdose() {
		return this.drugBasicdose;
	}

	public void setDrugBasicdose(Double drugBasicdose) {
		this.drugBasicdose = drugBasicdose;
	}

	public Double getDrugOncedosage() {
		return this.drugOncedosage;
	}

	public void setDrugOncedosage(Double drugOncedosage) {
		this.drugOncedosage = drugOncedosage;
	}

	public Short getDrugEffMonth() {
		return this.drugEffMonth;
	}

	public void setDrugEffMonth(Short drugEffMonth) {
		this.drugEffMonth = drugEffMonth;
	}

	public Double getDrugRetailprice() {
		return this.drugRetailprice;
	}

	public void setDrugRetailprice(Double drugRetailprice) {
		this.drugRetailprice = drugRetailprice;
	}

	public Double getDrugMaxretailprice() {
		return this.drugMaxretailprice;
	}

	public void setDrugMaxretailprice(Double drugMaxretailprice) {
		this.drugMaxretailprice = drugMaxretailprice;
	}

	public Double getDrugWholesaleprice() {
		return this.drugWholesaleprice;
	}

	public void setDrugWholesaleprice(Double drugWholesaleprice) {
		this.drugWholesaleprice = drugWholesaleprice;
	}

	public Double getDrugPurchaseprice() {
		return this.drugPurchaseprice;
	}

	public void setDrugPurchaseprice(Double drugPurchaseprice) {
		this.drugPurchaseprice = drugPurchaseprice;
	}

	public String getDrugPricetype() {
		return this.drugPricetype;
	}

	public void setDrugPricetype(String drugPricetype) {
		this.drugPricetype = drugPricetype;
	}

	public String getDrugSplitType() {
		return this.drugSplitType;
	}

	public void setDrugSplitType(String drugSplitType) {
		this.drugSplitType = drugSplitType;
	}

	public String getDrugInfusionFlg() {
		return this.drugInfusionFlg;
	}

	public void setDrugInfusionFlg(String drugInfusionFlg) {
		this.drugInfusionFlg = drugInfusionFlg;
	}

	public String getDrugOwncostFlg() {
		return this.drugOwncostFlg;
	}

	public void setDrugOwncostFlg(String drugOwncostFlg) {
		this.drugOwncostFlg = drugOwncostFlg;
	}

	public String getDrugSupriceFlg() {
		return this.drugSupriceFlg;
	}

	public void setDrugSupriceFlg(String drugSupriceFlg) {
		this.drugSupriceFlg = drugSupriceFlg;
	}

	public String getDrugIsnew() {
		return this.drugIsnew;
	}

	public void setDrugIsnew(String drugIsnew) {
		this.drugIsnew = drugIsnew;
	}

	public String getDrugAppendFlg() {
		return this.drugAppendFlg;
	}

	public void setDrugAppendFlg(String drugAppendFlg) {
		this.drugAppendFlg = drugAppendFlg;
	}

	public String getDrugIsself() {
		return this.drugIsself;
	}

	public void setDrugIsself(String drugIsself) {
		this.drugIsself = drugIsself;
	}

	public String getDrugIstest() {
		return this.drugIstest;
	}

	public void setDrugIstest(String drugIstest) {
		this.drugIstest = drugIstest;
	}

	public String getDrugIsgmp() {
		return this.drugIsgmp;
	}

	public void setDrugIsgmp(String drugIsgmp) {
		this.drugIsgmp = drugIsgmp;
	}

	public String getDrugIsotc() {
		return this.drugIsotc;
	}

	public void setDrugIsotc(String drugIsotc) {
		this.drugIsotc = drugIsotc;
	}

	public String getDrugIslack() {
		return this.drugIslack;
	}

	public void setDrugIslack(String drugIslack) {
		this.drugIslack = drugIslack;
	}

	public String getDrugIsterminalconfirm() {
		return this.drugIsterminalconfirm;
	}

	public void setDrugIsterminalconfirm(String drugIsterminalconfirm) {
		this.drugIsterminalconfirm = drugIsterminalconfirm;
	}

	public String getDrugIsagreementprescription() {
		return this.drugIsagreementprescription;
	}

	public void setDrugIsagreementprescription(
			String drugIsagreementprescription) {
		this.drugIsagreementprescription = drugIsagreementprescription;
	}

	public String getDrugIscooperativemedical() {
		return this.drugIscooperativemedical;
	}

	public void setDrugIscooperativemedical(String drugIscooperativemedical) {
		this.drugIscooperativemedical = drugIscooperativemedical;
	}

	public String getDrugIsprovincelimit() {
		return this.drugIsprovincelimit;
	}

	public void setDrugIsprovincelimit(String drugIsprovincelimit) {
		this.drugIsprovincelimit = drugIsprovincelimit;
	}

	public String getDrugIscitylimit() {
		return this.drugIscitylimit;
	}

	public void setDrugIscitylimit(String drugIscitylimit) {
		this.drugIscitylimit = drugIscitylimit;
	}

	public String getDrugRemark() {
		return this.drugRemark;
	}

	public void setDrugRemark(String drugRemark) {
		this.drugRemark = drugRemark;
	}

	public String getDrugValidState() {
		return this.drugValidState;
	}

	public void setDrugValidState(String drugValidState) {
		this.drugValidState = drugValidState;
	}

	public String getDrugStorage() {
		return this.drugStorage;
	}

	public void setDrugStorage(String drugStorage) {
		this.drugStorage = drugStorage;
	}

	public String getDrugNotes() {
		return this.drugNotes;
	}

	public void setDrugNotes(String drugNotes) {
		this.drugNotes = drugNotes;
	}

	public String getDrugActiveingredient() {
		return this.drugActiveingredient;
	}

	public void setDrugActiveingredient(String drugActiveingredient) {
		this.drugActiveingredient = drugActiveingredient;
	}

	public String getDrugPhyFunction1() {
		return this.drugPhyFunction1;
	}

	public void setDrugPhyFunction1(String drugPhyFunction1) {
		this.drugPhyFunction1 = drugPhyFunction1;
	}

	public String getDrugPhyFunction2() {
		return this.drugPhyFunction2;
	}

	public void setDrugPhyFunction2(String drugPhyFunction2) {
		this.drugPhyFunction2 = drugPhyFunction2;
	}

	public String getDrugPhyFunction3() {
		return this.drugPhyFunction3;
	}

	public void setDrugPhyFunction3(String drugPhyFunction3) {
		this.drugPhyFunction3 = drugPhyFunction3;
	}

	public Date getDrugEntertime() {
		return this.drugEntertime;
	}

	public void setDrugEntertime(Date drugEntertime) {
		this.drugEntertime = drugEntertime;
	}

	public String getDrugManufacturer() {
		return this.drugManufacturer;
	}

	public void setDrugManufacturer(String drugManufacturer) {
		this.drugManufacturer = drugManufacturer;
	}

	public String getDrugSupplycompany() {
		return this.drugSupplycompany;
	}

	public void setDrugSupplycompany(String drugSupplycompany) {
		this.drugSupplycompany = drugSupplycompany;
	}

	public String getDrugDocument() {
		return this.drugDocument;
	}

	public void setDrugDocument(String drugDocument) {
		this.drugDocument = drugDocument;
	}

	public String getDrugTrademark() {
		return this.drugTrademark;
	}

	public void setDrugTrademark(String drugTrademark) {
		this.drugTrademark = drugTrademark;
	}

	public String getDrugPlaceoforigin() {
		return this.drugPlaceoforigin;
	}

	public void setDrugPlaceoforigin(String drugPlaceoforigin) {
		this.drugPlaceoforigin = drugPlaceoforigin;
	}

	public String getDrugOperativenorm() {
		return this.drugOperativenorm;
	}

	public void setDrugOperativenorm(String drugOperativenorm) {
		this.drugOperativenorm = drugOperativenorm;
	}

	public String getDrugLibid() {
		return this.drugLibid;
	}

	public void setDrugLibid(String drugLibid) {
		this.drugLibid = drugLibid;
	}

	public String getDrugLibno() {
		return this.drugLibno;
	}

	public void setDrugLibno(String drugLibno) {
		this.drugLibno = drugLibno;
	}

	public String getDrugBarcode() {
		return this.drugBarcode;
	}

	public void setDrugBarcode(String drugBarcode) {
		this.drugBarcode = drugBarcode;
	}

	public String getDrugBrev() {
		return this.drugBrev;
	}

	public void setDrugBrev(String drugBrev) {
		this.drugBrev = drugBrev;
	}

	public String getDrugInstruction() {
		return this.drugInstruction;
	}

	public void setDrugInstruction(String drugInstruction) {
		this.drugInstruction = drugInstruction;
	}

	public Boolean getDrugIstender() {
		return this.drugIstender;
	}

	public void setDrugIstender(Boolean drugIstender) {
		this.drugIstender = drugIstender;
	}

	public String getDrugBiddingcode() {
		return this.drugBiddingcode;
	}

	public void setDrugBiddingcode(String drugBiddingcode) {
		this.drugBiddingcode = drugBiddingcode;
	}

	public Double getDrugBiddingprice() {
		return this.drugBiddingprice;
	}

	public void setDrugBiddingprice(Double drugBiddingprice) {
		this.drugBiddingprice = drugBiddingprice;
	}

	public String getDrugTenderCompanyCode() {
		return this.drugTenderCompanyCode;
	}

	public void setDrugTenderCompanyCode(String drugTenderCompanyCode) {
		this.drugTenderCompanyCode = drugTenderCompanyCode;
	}

	public String getDrugContractcode() {
		return this.drugContractcode;
	}

	public void setDrugContractcode(String drugContractcode) {
		this.drugContractcode = drugContractcode;
	}

	public Date getDrugStartdate() {
		return this.drugStartdate;
	}

	public void setDrugStartdate(Date drugStartdate) {
		this.drugStartdate = drugStartdate;
	}

	public Date getDrugEnddate() {
		return this.drugEnddate;
	}

	public void setDrugEnddate(Date drugEnddate) {
		this.drugEnddate = drugEnddate;
	}

	public Date getDrugCreateTime() {
		return this.drugCreateTime;
	}

	public void setDrugCreateTime(Date drugCreateTime) {
		this.drugCreateTime = drugCreateTime;
	}

	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public String getOperCode() {
		return this.operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
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