package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;



public class DrugInfo extends Entity {

	// Fields
	/**医院编号**/
	private Hospital hospital;
	/**名称**/
	private String name;
	/**名称拼音码**/
	private String drugNamepinyin;
	/**名称五笔码**/
	private String drugNamewb;
	/**名称自定义码**/
	private String drugNameinputcode;
	/**通用名称**/
	private String drugCommonname;
	/**通用名称拼音码**/
	private String drugCnamepinyin;
	/**通用名称五笔码**/
	private String drugCnamewb;
	/**通用名称自定义码**/
	private String drugCnameinputcode;
	/**基本药物1**/
	private String duugBasici;
	/**基本药物2**/
	private String duugBasicii;
	/**基本药物拼音码**/
	private String drugBasicpinyin;
	/**基本药物五笔码**/
	private String drugBasicwb;
	/**基本药物自定义码**/
	private String drugBasicinputcode;
	/**招标识别码**/
	private String drugBiddingcode;
	/**英文品名**/
	private String drugEnname;
	/**英文别名**/
	private String drugEnalias;
	/**英文通用名**/
	private String drugEncommonname;
	/**国家编码**/
	private String drugGbcode;
	/**库位编码**/
	private String drugLibid;
	/**规格**/
	private String spec;
	/**药品类别**/
	private String drugType;
	/**系统类别**/
	private String drugSystype;
	/**最小费用**/
	private String drugMinimumcost;
	/**药品性质**/
	private String drugNature;
	/**剂型**/
	private String drugDosageform;
	/**药品等级**/
	private String drugGrade;
	/**拆分属性**/
	private Integer drugSplitattr;
	/**生产厂家**/
	private String drugManufacturer;
	/**包装单位**/
	private String drugPackagingunit;
	/**包装数量**/
	private Integer packagingnum;
	/**最小单位**/
	private String unit;
	/**基本剂量**/
	private Double drugBasicdose;
	/**剂量单位**/
	private String drugDoseunit;
	/**零售价**/
	private Double drugRetailprice=0.0;
	/**最高零售价**/
	private Double drugMaxretailprice=0.0;
	/**批发价**/
	private Double drugWholesaleprice=0.0;
	/**购入价**/
	private Double drugPurchaseprice=0.0;
	/**价格形式**/
	private String drugPricetype;
	/**是否新药**/
	private Integer drugIsnew=0;
	/**1国产2进口3自制4合资**/
	private Integer drugIsmanufacture=0;
	/**0不需要皮试1青霉素皮试2原药皮试**/
	private Integer drugIstestsensitivity=0;
	/**是否GMP**/
	private Integer drugIsgmp=0;
	/**是否OTC**/
	private Integer drugIsotc=0;
	/**是否缺药**/
	private Integer drugIslack=0;
	/**  是否省限制**/
	private Integer drugIsprovincelimit;
	/**是否市限制  **/
	private Integer drugIscitylimit;
	/**终端确认**/
	private Integer drugIsterminalsubmit=0;
	/**是否大屏幕显示**/
	private Integer drugIsscreen=0;
	/**是否协定处方**/
	private Integer drugIsagreementprescription=0;
	/**是否合作医疗**/
	private Integer drugIscooperativemedical=0;
	/**1非抗菌药2无限制3职级限制4特殊管理**/
	private Integer drugRestrictionofantibiotic;
	/**备注**/
	private String drugRemark;
	/**使用方法**/
	private String drugUsemode;
	/**一次用量**/
	private Double drugOncedosage=0.0;
	/**频次**/
	private String drugFrequency;
	/**存储条件**/
	private String drugStorage;
	/**注意事项**/
	private String drugNotes;
	/**有效成分**/
	private String drugActiveingredient;
	/**一级药理**/
	private String drugPrimarypharmacology;
	/**二级药理**/
	private String drugTwogradepharmacology;
	/**三级药理**/
	private String drugThreegradepharmacology;
	/**入院时间**/
	private Date drugEntertime;
	/**供货公司**/
	private String drugSupplycompany;
	/**批文信息**/
	private String drugDocument;
	/**注册商标**/
	private String drugTrademark;
	/**产地**/
	private String drugPlaceoforigin;
	/**执行标准**/
	private String drugOperativenorm;
	/**库位号**/
	private String drugLibno;
	/**条形码**/
	private String drugBarcode;
	/**药品简介**/
	private String drugBrev;
	/**说明书**/
	private String drugInstruction;
	/**是否是招标药**/
	private Integer drugIstender=0;
	/**中标公司**/
	private String drugWinningcompany;
	/**合同代码**/
	private String drugContractcode;
	/**中标价**/
	private Double drugBiddingprice=0.0;
	/**起始日期**/
	private Date drugStartdate;
	/**终止日期**/
	private Date drugEnddate;
	/**停用原因**/
	private String stopReason;
	/** 页数   **/
	private String page;
	/**  每页行数  **/
	private String rows;
	/** 
	* @Fields showFlag : 显示的单位标记(0最小单位,1包装单位)
	*/ 
	private Integer showFlag = 0;
	
	//用于显示数据用
	/**货位号*/
	private String placeCode;
	/**生产厂家*/
	private String producerCode;
	/**显示的单位*/
	private String showUnit;
	/** 默认价 **/
	private Double defaultprice;
	/** 儿童价 **/
	private Double childrenprice;
	/** 特诊价 **/
	private Double specialprice;
	/** 科室id，用于查询库存维护表科室下拥有过的药品 **/
	private String deptId;
	/** 入院时间 **/
	private String drugEntertimeStr;
	/** 起始时间 **/
	private String drugStartDateStr;
	/** 终止时间 **/
	private String drugEndDateStr;
	
	//新加字段
	/**每一类药品序号从0开始，一次累加1,药品名相同但规格不同的药视为一类**/
	private String drugSerialno;
	/**按照药品的治疗方向分类，从编码表中过去**/
	private String drugClass;
	/**有效月数,有效期**/
	private Integer effMonth;
	/**大输液标志:编码表**/
	private String infusionFlg;
	/**0公费1自费2-部分负担**/
	private Integer selfFlg;
	/**贵重标志 1贵重0不贵重**/
	private Integer supriceFlg;
	/**辅材标志 1是0否**/
	private Integer appendFlg=0;
	//新加字段  药品code lyy
	private String code;
	/** 
	* @Fields areaCode : 所属院区 
	*/ 
	private String areaCode;
	 
	private Float weight;
	private String weightUnit;
	private Float volum;
	private String volUnit;
	

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Float getVolum() {
		return volum;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public void setVolum(Float volum) {
		this.volum = volum;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public String getVolUnit() {
		return volUnit;
	}

	public void setVolUnit(String volUnit) {
		this.volUnit = volUnit;
	}

	public Integer getEffMonth() {
		return effMonth;
	}

	public void setEffMonth(Integer effMonth) {
		this.effMonth = effMonth;
	}

	public String getDrugSerialno() {
		return drugSerialno;
	}

	public void setDrugSerialno(String drugSerialno) {
		this.drugSerialno = drugSerialno;
	}

	public String getDrugClass() {
		return drugClass;
	}

	public void setDrugClass(String drugClass) {
		this.drugClass = drugClass;
	}

	public String getInfusionFlg() {
		return infusionFlg;
	}

	public void setInfusionFlg(String infusionFlg) {
		this.infusionFlg = infusionFlg;
	}

	public Integer getSelfFlg() {
		return selfFlg;
	}

	public void setSelfFlg(Integer selfFlg) {
		this.selfFlg = selfFlg;
	}

	public Integer getSupriceFlg() {
		return supriceFlg;
	}

	public void setSupriceFlg(Integer supriceFlg) {
		this.supriceFlg = supriceFlg;
	}

	public Integer getAppendFlg() {
		return appendFlg;
	}

	public void setAppendFlg(Integer appendFlg) {
		this.appendFlg = appendFlg;
	}

	public String getPlaceCode() {
		return placeCode;
	}

	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}

	public String getProducerCode() {
		return producerCode;
	}

	public void setProducerCode(String producerCode) {
		this.producerCode = producerCode;
	}

	public String getShowUnit() {
		return showUnit;
	}

	public void setShowUnit(String showUnit) {
		this.showUnit = showUnit;
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
	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public String getDrugNamepinyin() {
		return this.drugNamepinyin;
	}

	public void setDrugNamepinyin(String drugNamepinyin) {
		this.drugNamepinyin = drugNamepinyin;
	}

	public String getDrugNamewb() {
		return this.drugNamewb;
	}

	public void setDrugNamewb(String drugNamewb) {
		this.drugNamewb = drugNamewb;
	}

	public String getDrugNameinputcode() {
		return this.drugNameinputcode;
	}

	public void setDrugNameinputcode(String drugNameinputcode) {
		this.drugNameinputcode = drugNameinputcode;
	}

	public String getDrugCommonname() {
		return this.drugCommonname;
	}

	public void setDrugCommonname(String drugCommonname) {
		this.drugCommonname = drugCommonname;
	}

	public String getDrugCnamepinyin() {
		return this.drugCnamepinyin;
	}

	public void setDrugCnamepinyin(String drugCnamepinyin) {
		this.drugCnamepinyin = drugCnamepinyin;
	}

	public String getDrugCnamewb() {
		return this.drugCnamewb;
	}

	public void setDrugCnamewb(String drugCnamewb) {
		this.drugCnamewb = drugCnamewb;
	}

	public String getDrugCnameinputcode() {
		return this.drugCnameinputcode;
	}

	public void setDrugCnameinputcode(String drugCnameinputcode) {
		this.drugCnameinputcode = drugCnameinputcode;
	}

	public String getDuugBasici() {
		return this.duugBasici;
	}

	public void setDuugBasici(String duugBasici) {
		this.duugBasici = duugBasici;
	}

	public String getDuugBasicii() {
		return this.duugBasicii;
	}

	public void setDuugBasicii(String duugBasicii) {
		this.duugBasicii = duugBasicii;
	}

	public String getDrugBasicpinyin() {
		return this.drugBasicpinyin;
	}

	public void setDrugBasicpinyin(String drugBasicpinyin) {
		this.drugBasicpinyin = drugBasicpinyin;
	}

	public String getDrugBasicwb() {
		return this.drugBasicwb;
	}

	public void setDrugBasicwb(String drugBasicwb) {
		this.drugBasicwb = drugBasicwb;
	}

	public String getDrugBasicinputcode() {
		return this.drugBasicinputcode;
	}

	public void setDrugBasicinputcode(String drugBasicinputcode) {
		this.drugBasicinputcode = drugBasicinputcode;
	}

	public String getDrugBiddingcode() {
		return this.drugBiddingcode;
	}

	public void setDrugBiddingcode(String drugBiddingcode) {
		this.drugBiddingcode = drugBiddingcode;
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

	public String getDrugLibid() {
		return this.drugLibid;
	}

	public void setDrugLibid(String drugLibid) {
		this.drugLibid = drugLibid;
	}


	public String getDrugType() {
		return this.drugType;
	}

	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}

	public String getDrugSystype() {
		return this.drugSystype;
	}

	public void setDrugSystype(String drugSystype) {
		this.drugSystype = drugSystype;
	}

	public String getDrugMinimumcost() {
		return this.drugMinimumcost;
	}

	public void setDrugMinimumcost(String drugMinimumcost) {
		this.drugMinimumcost = drugMinimumcost;
	}

	public String getDrugNature() {
		return this.drugNature;
	}

	public void setDrugNature(String drugNature) {
		this.drugNature = drugNature;
	}

	public String getDrugDosageform() {
		return this.drugDosageform;
	}

	public void setDrugDosageform(String drugDosageform) {
		this.drugDosageform = drugDosageform;
	}

	public String getDrugGrade() {
		return this.drugGrade;
	}

	public void setDrugGrade(String drugGrade) {
		this.drugGrade = drugGrade;
	}

	public Integer getDrugSplitattr() {
		return this.drugSplitattr;
	}

	public void setDrugSplitattr(Integer drugSplitattr) {
		this.drugSplitattr = drugSplitattr;
	}

	public String getDrugManufacturer() {
		return this.drugManufacturer;
	}

	public void setDrugManufacturer(String drugManufacturer) {
		this.drugManufacturer = drugManufacturer;
	}

	public String getDrugPackagingunit() {
		return this.drugPackagingunit;
	}

	public void setDrugPackagingunit(String drugPackagingunit) {
		this.drugPackagingunit = drugPackagingunit;
	}

	public Double getDrugBasicdose() {
		return this.drugBasicdose;
	}

	public void setDrugBasicdose(Double drugBasicdose) {
		this.drugBasicdose = drugBasicdose;
	}

	public String getDrugDoseunit() {
		return this.drugDoseunit;
	}

	public void setDrugDoseunit(String drugDoseunit) {
		this.drugDoseunit = drugDoseunit;
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

	public Integer getDrugIsnew() {
		return this.drugIsnew;
	}

	public void setDrugIsnew(Integer drugIsnew) {
		this.drugIsnew = drugIsnew;
	}

	public Integer getDrugIsmanufacture() {
		return this.drugIsmanufacture;
	}

	public void setDrugIsmanufacture(Integer drugIsmanufacture) {
		this.drugIsmanufacture = drugIsmanufacture;
	}

	public Integer getDrugIstestsensitivity() {
		return this.drugIstestsensitivity;
	}

	public void setDrugIstestsensitivity(Integer drugIstestsensitivity) {
		this.drugIstestsensitivity = drugIstestsensitivity;
	}

	public Integer getDrugIsgmp() {
		return this.drugIsgmp;
	}

	public void setDrugIsgmp(Integer drugIsgmp) {
		this.drugIsgmp = drugIsgmp;
	}

	public Integer getDrugIsotc() {
		return this.drugIsotc;
	}

	public void setDrugIsotc(Integer drugIsotc) {
		this.drugIsotc = drugIsotc;
	}

	public Integer getDrugIslack() {
		return this.drugIslack;
	}

	public void setDrugIslack(Integer drugIslack) {
		this.drugIslack = drugIslack;
	}

	public Integer getDrugIsterminalsubmit() {
		return this.drugIsterminalsubmit;
	}

	public void setDrugIsterminalsubmit(Integer drugIsterminalsubmit) {
		this.drugIsterminalsubmit = drugIsterminalsubmit;
	}

	public Integer getDrugIsscreen() {
		return this.drugIsscreen;
	}

	public void setDrugIsscreen(Integer drugIsscreen) {
		this.drugIsscreen = drugIsscreen;
	}

	public Integer getDrugIsagreementprescription() {
		return this.drugIsagreementprescription;
	}

	public void setDrugIsagreementprescription(
			Integer drugIsagreementprescription) {
		this.drugIsagreementprescription = drugIsagreementprescription;
	}

	public Integer getDrugIscooperativemedical() {
		return this.drugIscooperativemedical;
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
		return this.drugRemark;
	}

	public void setDrugRemark(String drugRemark) {
		this.drugRemark = drugRemark;
	}

	public String getDrugUsemode() {
		return this.drugUsemode;
	}

	public void setDrugUsemode(String drugUsemode) {
		this.drugUsemode = drugUsemode;
	}

	public Double getDrugOncedosage() {
		return this.drugOncedosage;
	}

	public void setDrugOncedosage(Double drugOncedosage) {
		this.drugOncedosage = drugOncedosage;
	}

	public String getDrugFrequency() {
		return this.drugFrequency;
	}

	public void setDrugFrequency(String drugFrequency) {
		this.drugFrequency = drugFrequency;
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

	public String getDrugPrimarypharmacology() {
		return this.drugPrimarypharmacology;
	}

	public void setDrugPrimarypharmacology(String drugPrimarypharmacology) {
		this.drugPrimarypharmacology = drugPrimarypharmacology;
	}

	public String getDrugTwogradepharmacology() {
		return this.drugTwogradepharmacology;
	}

	public void setDrugTwogradepharmacology(String drugTwogradepharmacology) {
		this.drugTwogradepharmacology = drugTwogradepharmacology;
	}

	public String getDrugThreegradepharmacology() {
		return this.drugThreegradepharmacology;
	}

	public void setDrugThreegradepharmacology(String drugThreegradepharmacology) {
		this.drugThreegradepharmacology = drugThreegradepharmacology;
	}

	public Date getDrugEntertime() {
		return this.drugEntertime;
	}

	public void setDrugEntertime(Date drugEntertime) {
		this.drugEntertime = drugEntertime;
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

	public Integer getDrugIstender() {
		return this.drugIstender;
	}

	public void setDrugIstender(Integer drugIstender) {
		this.drugIstender = drugIstender;
	}

	public String getDrugWinningcompany() {
		return this.drugWinningcompany;
	}

	public void setDrugWinningcompany(String drugWinningcompany) {
		this.drugWinningcompany = drugWinningcompany;
	}

	public String getDrugContractcode() {
		return this.drugContractcode;
	}

	public void setDrugContractcode(String drugContractcode) {
		this.drugContractcode = drugContractcode;
	}

	public Double getDrugBiddingprice() {
		return this.drugBiddingprice;
	}

	public void setDrugBiddingprice(Double drugBiddingprice) {
		this.drugBiddingprice = drugBiddingprice;
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

	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}

	public String getStopReason() {
		return stopReason;
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

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptId() {
		return deptId;
	}
	public Integer getDrugIsprovincelimit() {
		return drugIsprovincelimit;
	}

	public void setDrugIsprovincelimit(Integer drugIsprovincelimit) {
		this.drugIsprovincelimit = drugIsprovincelimit;
	}

	public Integer getDrugIscitylimit() {
		return drugIscitylimit;
	}

	public void setDrugIscitylimit(Integer drugIscitylimit) {
		this.drugIscitylimit = drugIscitylimit;
	}

	public Integer getShowFlag() {
		return showFlag;
	}

	public void setShowFlag(Integer showFlag) {
		this.showFlag = showFlag;
	}

	public String getDrugEntertimeStr() {
		return drugEntertimeStr;
	}

	public void setDrugEntertimeStr(String drugEntertimeStr) {
		this.drugEntertimeStr = drugEntertimeStr;
	}

	public String getDrugStartDateStr() {
		return drugStartDateStr;
	}

	public void setDrugStartDateStr(String drugStartDateStr) {
		this.drugStartDateStr = drugStartDateStr;
	}

	public String getDrugEndDateStr() {
		return drugEndDateStr;
	}

	public void setDrugEndDateStr(String drugEndDateStr) {
		this.drugEndDateStr = drugEndDateStr;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

}