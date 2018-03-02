package cn.honry.base.bean.model;

import java.util.Date;

public class VidInfoOrder {

	
	/**ID*/
	private String id;
	/**住院流水号(6位日期+6位流水号)*/
	private String inpatientNoo;
	/**医疗类别(从编码表里读取)*/
	private String medicalType;
	/**病历号*/
	private String medicalrecordId;
	/**就诊卡号*/
	private String idcardNo;
	/**医疗证号*/
	private String mcardNo;
	/**姓名*/
	private String patientName;
	/**证件类型(从编码表里读取)*/
	private String certificatesType;
	/**证件号码*/
	private String certificatesNo;
	/**性别(从编码表里读取)*/
	private String reportSex;
	/**出生日期*/
	private Date reportBirthday;
	/**年龄*/
	private Integer reportAge;
	/**年龄单位(年月天)*/
	private String reportAgeunit;
	/**床号  (从病床维护表里读取)*/
	private String bedId;
	/**职业代码(从编码表里读取)*/
	private String profCode;
	/**工作单位*/
	private String workName;
	/**工作单位电话*/
	private String workTel;
	/**单位邮编*/
	private String workZip;
	/**户口或家庭地址*/
	private String home;
	/**家庭电话*/
	private String homeTel;
	/**户口或家庭邮编*/
	private String homeZip;
	/**籍贯(从城市表里读取)*/
	private String dist;
	/**出生地代码(从城市表里读取)*/
	private String birthArea;
	/**民族(从编码表里读取)*/
	private String nationCode;
	/**联系人姓名*/
	private String linkmanName;
	/**联系人电话*/
	private String linkmanTel;
	/**联系人地址*/
	private String linkmanAddress;
	/**关系(从编码表里读取)*/
	private String relaCode;
	/**婚姻状况(从编码表里读取)*/
	private String mari;
	/**国籍(从国家编码表里读取)*/
	private String counCode;
	/**身高*/
	private Double height;
	/**体重*/
	private Double weight;
	/**血压*/
	private String bloodDress;
	/**血型编码(从编码表里读取)*/
	private String bloodCode;
	/**重大疾病标志1:有  0:无*/
	private Integer hepatitisFlag;
	/**过敏标志1:有  0:无*/
	private Integer anaphyFlag;
	/**入院日期*/
	private Date inDate;
	/**科室代码*/
	private String deptCodeo;
	/**结算类别 1-自费  2-保险 3-公费在职 4-公费退休 5-公费高干 (从编码表里读取)*/
	private String paykindCode;
	/**合同单位代码 (从合同单位编码表里读取)*/
	private String pactCode;
	/**入院情况*/
	private String inCircs;
	/**诊断名称（建议用此保存主诊断）*/
	private String diagName;
	/**入院途径*/
	private String inAvenue;
	/**入院来源 1:门诊，2:急诊，3:转科，4:转院 (从编码表里读取)*/
	private String inSource;
	/**住院次数*/
	private Integer inTimes;
	/**预交金额(未结)*/
	private Double prepayCost;
	/**转入预交金额（未结)*/
	private Double changePrepaycost;
	/**M 金额 D时间段*/
	private String alterType;
	/**警戒线开始时间*/
	private Date alterBegin;
	/**警戒线结束时间*/
	private Date alterEnd;
	/**警戒线*/
	private Double moneyAlert;
	/**费用金额(未结)*/
	private Double totCost;
	/**自费金额(未结)*/
	private Double ownCost;
	/**自付金额(未结)*/
	private Double payCost;
	/**公费金额(未结)*/
	private Double pubCost;
	/**优惠金额(未结)*/
	private Double ecoCost;
	/**余额(未结)*/
	private Double freeCost;
	/**转入费用金额(未结)*/
	private Double changeTotcost;
	/**待遇上限*/
	private Double upperLimit;
	/**固定费用间隔天数*/
	private Integer feeInterval;
	/**结算序号*/
	private Integer balanceNo;
	/**费用金额(已结)*/
	private Double balanceCost;
	/**预交金额(已结)*/
	private Double balancePrepay;
	/**结算日期(上次)*/
	private Date balanceDate;
	/**是否关帐*/
	private Integer stopAcount;
	/**婴儿标志 1:有婴儿；0:无婴儿*/
	private Integer babyFlago;
	/**病案状态: 0 无需病案 1 需要病案 2 医生站形成病案 3 病案室形成病案 4病案封存*/
	private Integer caseFlag;
	/**R-住院登记  I-病房接诊 B-出院登记 O-出院结算 P-预约出院,N-无费退院*/
	private String inState;
	/**是否请假 0 非 1是*/
	private Integer leaveFlag;
	/**出院日期(预约)*/
	private Date prepayOutdate;
	/**出院日期*/
	private Date outDate;
	/**转归代号*/
	private String zg;
	/**开据医师*/
	private String emplCode;
	/**是否在ICU 0 NO 1 YES*/
	private Integer inIcu;
	/**病案送入病案室否0未1送*/
	private Integer casesendFlag;
	/**护理级别(TEND):名称显示护理级别名称(一级护理，二级护理，三级护理) (从编码表里读取)*/
	private String tend;
	/**病危：0 普通 1 病重 2 病危*/
	private Integer criticalFlag;
	/**上次固定费用时间*/
	private Date prefixfeeDate;
	/**血滞纳金*/
	private Double bloodLatefee;
	/**公费患者日限额*/
	private Double dayLimit;
	/**公费患者日限额累计*/
	private Double limitTot;
	/**公费患者日限额超标部分金额*/
	private Double limitOvertop;
	/**生育保险患者电脑号*/
	private String procreatePcno;
	/**公费患者公费药品累计(日限额)*/
	private Double bursaryTotmedfee;
	/**备注*/
	private String remark;
	/**床位上限*/
	private Double bedLimit;
	/**空调上限*/
	private Double airLimit;
	/**床费超标处理 0超标不限 1超标自理 2超标不计*/
	private Integer bedoverdeal;
	/**（公医超日限额是否同意：0不同意，1同意）*/
	private Integer extFlag;
	/**扩展标记1*/
	private String extFlag1;
	/**扩展标记2*/
	private String extFlag2;
	/**膳食花费总额*/
	private Double boardCost;
	/**膳食预交金额*/
	private Double boardPrepay;
	/**膳食结算状态：0在院 1出院*/
	private Integer boardState;
	/**自费比例*/
	private Double ownRate;
	/**自付比例*/
	private Double payRate;
	/**扩展数值（中山一用作－剩余统筹金额）*/
	private Double extNumber;
	/**扩展编码（）*/
	private String extCode;
	/**建立人员**/
	private String createUser;
	/**建立部门**/
	private String createDept;
	/**建立时间**/
	private Date createTime;
	/**修改人员**/
	private String updateUser;
	/**修改时间**/
	private Date updateTime;
	/**删除人员**/
	private String deleteUser;
	/**删除时间**/
	private Date deleteTime;	
	/**停用标志**/
	private Integer stop_flg=0;
	/**删除标志**/
	private Integer del_flg=0;
	
	
	//-------------------------------------------------------------------------------------------------------------------------

	/**order表中主键*/
	private String orderId;
	/**住院号*/
	private String inpatientNo;
	/**住院病历号 */
	private String patientNo;
	/**住院科室代码 */
	private String deptCode;
	/**住院护理站代码 */
	private String nurseCellCode;
	/**开立科室代码*/
	private String listDpcd;
	/**医嘱流水号*/
	private String   moOrder;
	/**医嘱医师代号*/
	private String  docCode; 
	/**医嘱医师姓名*/
	private String  docName;
	/**医嘱日期**/
	private Date  moDate;
	/**是否婴儿医嘱,1是/0否*/
	private Integer  babyFlag; 
	/**婴儿序号*/
	private Integer  happenNo;
	/**项目属性,1院内项目/2院外项目*/
	private Integer  setItmattr;
	/**是否包含附材,1是/0否*/
	private Integer  setSubtbl; 
	/**医嘱类别代码*/
	private String typeCode;  
	/**医嘱类别名称*/
	private String typeName;
	/**医嘱是否分解:1长期/0临时*/
    private Integer   decmpsState;
    /**是否计费*/
    private Integer   chargeState;
    /**药房是否配药*/
    private Integer   needDrug; 
    /**打印执行单*/
    private Integer   prnExelist;
    /**是否打印医嘱单*/
    private Integer   prmMorlist;
    /**是否需要确认*/
    private Integer   needConfirm; 
    /**项目类别*/
    private String   itemType;         
    /**项目编码*/
    private String  itemCode;           
    /**项目名称*/
    private String  itemName;          
    /**项目类别代码*/
    private String  classCode;
    /**项目类别名称*/
    private String  className; 
    /**取药药房*/
    private String   pharmacyCode; 
    /**执行科室代码*/
    private String  execDpcd;    
    /**执行科室名称*/
    private String   execDpnm;   
    /**药品基本剂量*/
    private Double   baseDose;  
    /**剂量单位*/
    private String   doseUnit;  
    /**最小单位*/
    private String   minUnit;   
    /**计价单位*/
    private String   priceUnit;
    /**包装数量*/
    private Integer  packQty; 
    /**规格*/
    private String  specs;
    /**剂型代码*/
    private String  doseModelCode;
    /**药品类别*/
    private String  drugType;
    /**药品性质*/
    private String  drugQuality;
    /**价格*/
    private Double itemPrice;
    /**组合序号*/
    private String  combNo;
    /**主药标记*/
    private Integer  mainDrug;
    /**医嘱状态,0开立，1审核，2执行，3作废，4重整*/
    private Integer  moStat;
    /**用法代码*/
    private String  usageCode;
    /**用法名称*/
    private String  useName;
    /**用法英文缩写*/
    private String  englishAb;
    /**频次代码*/
    private String  frequencyCode;
    /**频次名称*/
    private String  frequencyName;
    /**每次剂量*/
    private Double doseOnce;
    /**1扣护士站常备药/2扣药房*/
    private Integer stocKin;
    /**项目总量*/
    private Integer qtyTot;
    /**付数*/
    private Integer useDays;
    /**开始时间*/
    private Date dateBgn;
    /**结束时间*/
    private Date dateEnd;
    /**录入人员代码*/
   	private String  recUsercd;
   	/**录入人员姓名*/
   	private String  recUsernm;
   	/**确认标记,0未确认/1已*/
    private Integer confirmFlag;
    /**确认时间*/
    private Date confirmDate;
    /**确认人员代码*/
   	private String  confirmUsercd;
   	/**Dc标记 0未dc/1已dc*/
    private Integer dcFlag;
    /**Dc时间*/
   	private Date dcDate;
   	/**DC原因代码*/
   	private String  dcCode;
   	/**DC原因名称*/
   	private String  dcName;
   	/**DC医师代码*/
   	private String  dcDoccd;
   	/**DC医师姓名*/
   	private String  dcDocnm;
   	/**Dc人员代码*/
   	private String  dcUsercd;
   	/**Dc人员名称*/
   	private String  dcUsernm;
   	/**执行标记 0未执行/1已执行*/
   	private Integer executeFlag;
   	/**执行时间*/
   	private Date executeDate;
   	/**执行人员代码*/
	private String  executeUsercd;
	/**整档标记 0无/1有*/
	private Integer  decoFlag;
	/**本次分解时间*/
	private Date  dateCurmodc;
	/**下次分解时间*/
	private Date  dateNxtmodc;
	/**医嘱说明*/
	private String  moNote1;
	/**备注*/
	private String  moNote2;
	/**1不需要皮试/2需要皮试，未做/3皮试阳/4皮试阴*/
	private Integer  hypotest;
	/**检查部位检体*/
   	private String  itemNote;
   	/**申请单号*/
   	private String  applyNo;
   	/**加急标记: 0普通/1加急*/
   	private Integer  emcFlag;
   	/**医嘱提取标记: 0待提取/1已提取/2DC提取*/
   	private Integer  getFlag;
   	/**是否附材''1''是*/
   	private Integer  subtblFlag;
   	/**排列序号，按排列序号由大到小顺序显示医嘱*/
   	private Integer  sortId;
   	/**DC审核时间*/
   	private Date  dcConfirmDate;
   	/**DC审核人*/
   	private String  dcConfirmOper;
   	/**DC审核标记，0未审核，1已审核*/
   	private Integer  dcConfirmFlag;
   	/**样本类型 名称*/
  	private String  labCode;
  	/**是否需要患者同意 0 不需要  1需要*/
  	private Integer  permission;
  	/**组套编码*/
   	private String  packageCode;
   	/**组套名称*/
   	private String  packageName;
   	/**扩展备注  [执行时间]*/
   	private String  mark1;
   	/**扩展备注1*/
   	private String  mark2;
   	/**扩展备注2*/
   	private String  mark3; 
   	/**执行时间点[特殊频次]*/
   	private String  execTimes;
   	/**执行剂量[特殊频次]*/
   	private String  execDose;
   	
   	
	//-------------------------------------------------------------------------------------------------------------------------

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInpatientNoo() {
		return inpatientNoo;
	}
	public void setInpatientNoo(String inpatientNoo) {
		this.inpatientNoo = inpatientNoo;
	}
	public String getMedicalType() {
		return medicalType;
	}
	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getIdcardNo() {
		return idcardNo;
	}
	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	public String getMcardNo() {
		return mcardNo;
	}
	public void setMcardNo(String mcardNo) {
		this.mcardNo = mcardNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getCertificatesType() {
		return certificatesType;
	}
	public void setCertificatesType(String certificatesType) {
		this.certificatesType = certificatesType;
	}
	public String getCertificatesNo() {
		return certificatesNo;
	}
	public void setCertificatesNo(String certificatesNo) {
		this.certificatesNo = certificatesNo;
	}
	public String getReportSex() {
		return reportSex;
	}
	public void setReportSex(String reportSex) {
		this.reportSex = reportSex;
	}
	public Date getReportBirthday() {
		return reportBirthday;
	}
	public void setReportBirthday(Date reportBirthday) {
		this.reportBirthday = reportBirthday;
	}
	public Integer getReportAge() {
		return reportAge;
	}
	public void setReportAge(Integer reportAge) {
		this.reportAge = reportAge;
	}
	public String getReportAgeunit() {
		return reportAgeunit;
	}
	public void setReportAgeunit(String reportAgeunit) {
		this.reportAgeunit = reportAgeunit;
	}
	public String getBedId() {
		return bedId;
	}
	public void setBedId(String bedId) {
		this.bedId = bedId;
	}
	public String getProfCode() {
		return profCode;
	}
	public void setProfCode(String profCode) {
		this.profCode = profCode;
	}
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	public String getWorkTel() {
		return workTel;
	}
	public void setWorkTel(String workTel) {
		this.workTel = workTel;
	}
	public String getWorkZip() {
		return workZip;
	}
	public void setWorkZip(String workZip) {
		this.workZip = workZip;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public String getHomeTel() {
		return homeTel;
	}
	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}
	public String getHomeZip() {
		return homeZip;
	}
	public void setHomeZip(String homeZip) {
		this.homeZip = homeZip;
	}
	public String getDist() {
		return dist;
	}
	public void setDist(String dist) {
		this.dist = dist;
	}
	public String getBirthArea() {
		return birthArea;
	}
	public void setBirthArea(String birthArea) {
		this.birthArea = birthArea;
	}
	public String getNationCode() {
		return nationCode;
	}
	public void setNationCode(String nationCode) {
		this.nationCode = nationCode;
	}
	public String getLinkmanName() {
		return linkmanName;
	}
	public void setLinkmanName(String linkmanName) {
		this.linkmanName = linkmanName;
	}
	public String getLinkmanTel() {
		return linkmanTel;
	}
	public void setLinkmanTel(String linkmanTel) {
		this.linkmanTel = linkmanTel;
	}
	public String getLinkmanAddress() {
		return linkmanAddress;
	}
	public void setLinkmanAddress(String linkmanAddress) {
		this.linkmanAddress = linkmanAddress;
	}
	public String getRelaCode() {
		return relaCode;
	}
	public void setRelaCode(String relaCode) {
		this.relaCode = relaCode;
	}
	public String getMari() {
		return mari;
	}
	public void setMari(String mari) {
		this.mari = mari;
	}
	public String getCounCode() {
		return counCode;
	}
	public void setCounCode(String counCode) {
		this.counCode = counCode;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String getBloodDress() {
		return bloodDress;
	}
	public void setBloodDress(String bloodDress) {
		this.bloodDress = bloodDress;
	}
	public String getBloodCode() {
		return bloodCode;
	}
	public void setBloodCode(String bloodCode) {
		this.bloodCode = bloodCode;
	}
	public Integer getHepatitisFlag() {
		return hepatitisFlag;
	}
	public void setHepatitisFlag(Integer hepatitisFlag) {
		this.hepatitisFlag = hepatitisFlag;
	}
	public Integer getAnaphyFlag() {
		return anaphyFlag;
	}
	public void setAnaphyFlag(Integer anaphyFlag) {
		this.anaphyFlag = anaphyFlag;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public String getDeptCodeo() {
		return deptCodeo;
	}
	public void setDeptCodeo(String deptCodeo) {
		this.deptCodeo = deptCodeo;
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
	public String getInCircs() {
		return inCircs;
	}
	public void setInCircs(String inCircs) {
		this.inCircs = inCircs;
	}
	public String getDiagName() {
		return diagName;
	}
	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}
	public String getInAvenue() {
		return inAvenue;
	}
	public void setInAvenue(String inAvenue) {
		this.inAvenue = inAvenue;
	}
	public String getInSource() {
		return inSource;
	}
	public void setInSource(String inSource) {
		this.inSource = inSource;
	}
	public Integer getInTimes() {
		return inTimes;
	}
	public void setInTimes(Integer inTimes) {
		this.inTimes = inTimes;
	}
	public Double getPrepayCost() {
		return prepayCost;
	}
	public void setPrepayCost(Double prepayCost) {
		this.prepayCost = prepayCost;
	}
	public Double getChangePrepaycost() {
		return changePrepaycost;
	}
	public void setChangePrepaycost(Double changePrepaycost) {
		this.changePrepaycost = changePrepaycost;
	}
	public String getAlterType() {
		return alterType;
	}
	public void setAlterType(String alterType) {
		this.alterType = alterType;
	}
	public Date getAlterBegin() {
		return alterBegin;
	}
	public void setAlterBegin(Date alterBegin) {
		this.alterBegin = alterBegin;
	}
	public Date getAlterEnd() {
		return alterEnd;
	}
	public void setAlterEnd(Date alterEnd) {
		this.alterEnd = alterEnd;
	}
	public Double getMoneyAlert() {
		return moneyAlert;
	}
	public void setMoneyAlert(Double moneyAlert) {
		this.moneyAlert = moneyAlert;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public Double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
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
	public Double getFreeCost() {
		return freeCost;
	}
	public void setFreeCost(Double freeCost) {
		this.freeCost = freeCost;
	}
	public Double getChangeTotcost() {
		return changeTotcost;
	}
	public void setChangeTotcost(Double changeTotcost) {
		this.changeTotcost = changeTotcost;
	}
	public Double getUpperLimit() {
		return upperLimit;
	}
	public void setUpperLimit(Double upperLimit) {
		this.upperLimit = upperLimit;
	}
	public Integer getFeeInterval() {
		return feeInterval;
	}
	public void setFeeInterval(Integer feeInterval) {
		this.feeInterval = feeInterval;
	}
	public Integer getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(Integer balanceNo) {
		this.balanceNo = balanceNo;
	}
	public Double getBalanceCost() {
		return balanceCost;
	}
	public void setBalanceCost(Double balanceCost) {
		this.balanceCost = balanceCost;
	}
	public Double getBalancePrepay() {
		return balancePrepay;
	}
	public void setBalancePrepay(Double balancePrepay) {
		this.balancePrepay = balancePrepay;
	}
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public Integer getStopAcount() {
		return stopAcount;
	}
	public void setStopAcount(Integer stopAcount) {
		this.stopAcount = stopAcount;
	}
	public Integer getBabyFlago() {
		return babyFlago;
	}
	public void setBabyFlago(Integer babyFlago) {
		this.babyFlago = babyFlago;
	}
	public Integer getCaseFlag() {
		return caseFlag;
	}
	public void setCaseFlag(Integer caseFlag) {
		this.caseFlag = caseFlag;
	}
	public String getInState() {
		return inState;
	}
	public void setInState(String inState) {
		this.inState = inState;
	}
	public Integer getLeaveFlag() {
		return leaveFlag;
	}
	public void setLeaveFlag(Integer leaveFlag) {
		this.leaveFlag = leaveFlag;
	}
	public Date getPrepayOutdate() {
		return prepayOutdate;
	}
	public void setPrepayOutdate(Date prepayOutdate) {
		this.prepayOutdate = prepayOutdate;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public String getZg() {
		return zg;
	}
	public void setZg(String zg) {
		this.zg = zg;
	}
	public String getEmplCode() {
		return emplCode;
	}
	public void setEmplCode(String emplCode) {
		this.emplCode = emplCode;
	}
	public Integer getInIcu() {
		return inIcu;
	}
	public void setInIcu(Integer inIcu) {
		this.inIcu = inIcu;
	}
	public Integer getCasesendFlag() {
		return casesendFlag;
	}
	public void setCasesendFlag(Integer casesendFlag) {
		this.casesendFlag = casesendFlag;
	}
	public String getTend() {
		return tend;
	}
	public void setTend(String tend) {
		this.tend = tend;
	}
	public Integer getCriticalFlag() {
		return criticalFlag;
	}
	public void setCriticalFlag(Integer criticalFlag) {
		this.criticalFlag = criticalFlag;
	}
	public Date getPrefixfeeDate() {
		return prefixfeeDate;
	}
	public void setPrefixfeeDate(Date prefixfeeDate) {
		this.prefixfeeDate = prefixfeeDate;
	}
	public Double getBloodLatefee() {
		return bloodLatefee;
	}
	public void setBloodLatefee(Double bloodLatefee) {
		this.bloodLatefee = bloodLatefee;
	}
	public Double getDayLimit() {
		return dayLimit;
	}
	public void setDayLimit(Double dayLimit) {
		this.dayLimit = dayLimit;
	}
	public Double getLimitTot() {
		return limitTot;
	}
	public void setLimitTot(Double limitTot) {
		this.limitTot = limitTot;
	}
	public Double getLimitOvertop() {
		return limitOvertop;
	}
	public void setLimitOvertop(Double limitOvertop) {
		this.limitOvertop = limitOvertop;
	}
	public String getProcreatePcno() {
		return procreatePcno;
	}
	public void setProcreatePcno(String procreatePcno) {
		this.procreatePcno = procreatePcno;
	}
	public Double getBursaryTotmedfee() {
		return bursaryTotmedfee;
	}
	public void setBursaryTotmedfee(Double bursaryTotmedfee) {
		this.bursaryTotmedfee = bursaryTotmedfee;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getBedLimit() {
		return bedLimit;
	}
	public void setBedLimit(Double bedLimit) {
		this.bedLimit = bedLimit;
	}
	public Double getAirLimit() {
		return airLimit;
	}
	public void setAirLimit(Double airLimit) {
		this.airLimit = airLimit;
	}
	public Integer getBedoverdeal() {
		return bedoverdeal;
	}
	public void setBedoverdeal(Integer bedoverdeal) {
		this.bedoverdeal = bedoverdeal;
	}
	public Integer getExtFlag() {
		return extFlag;
	}
	public void setExtFlag(Integer extFlag) {
		this.extFlag = extFlag;
	}
	public String getExtFlag1() {
		return extFlag1;
	}
	public void setExtFlag1(String extFlag1) {
		this.extFlag1 = extFlag1;
	}
	public String getExtFlag2() {
		return extFlag2;
	}
	public void setExtFlag2(String extFlag2) {
		this.extFlag2 = extFlag2;
	}
	public Double getBoardCost() {
		return boardCost;
	}
	public void setBoardCost(Double boardCost) {
		this.boardCost = boardCost;
	}
	public Double getBoardPrepay() {
		return boardPrepay;
	}
	public void setBoardPrepay(Double boardPrepay) {
		this.boardPrepay = boardPrepay;
	}
	public Integer getBoardState() {
		return boardState;
	}
	public void setBoardState(Integer boardState) {
		this.boardState = boardState;
	}
	public Double getOwnRate() {
		return ownRate;
	}
	public void setOwnRate(Double ownRate) {
		this.ownRate = ownRate;
	}
	public Double getPayRate() {
		return payRate;
	}
	public void setPayRate(Double payRate) {
		this.payRate = payRate;
	}
	public Double getExtNumber() {
		return extNumber;
	}
	public void setExtNumber(Double extNumber) {
		this.extNumber = extNumber;
	}
	public String getExtCode() {
		return extCode;
	}
	public void setExtCode(String extCode) {
		this.extCode = extCode;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateDept() {
		return createDept;
	}
	public void setCreateDept(String createDept) {
		this.createDept = createDept;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getDeleteUser() {
		return deleteUser;
	}
	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}
	public Date getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
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
	
	//-------------------------------------------------------------------------------------------------------------------------
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public String getListDpcd() {
		return listDpcd;
	}
	public void setListDpcd(String listDpcd) {
		this.listDpcd = listDpcd;
	}
	public String getMoOrder() {
		return moOrder;
	}
	public void setMoOrder(String moOrder) {
		this.moOrder = moOrder;
	}
	public String getDocCode() {
		return docCode;
	}
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public Date getMoDate() {
		return moDate;
	}
	public void setMoDate(Date moDate) {
		this.moDate = moDate;
	}
	public Integer getBabyFlag() {
		return babyFlag;
	}
	public void setBabyFlag(Integer babyFlag) {
		this.babyFlag = babyFlag;
	}
	public Integer getHappenNo() {
		return happenNo;
	}
	public void setHappenNo(Integer happenNo) {
		this.happenNo = happenNo;
	}
	public Integer getSetItmattr() {
		return setItmattr;
	}
	public void setSetItmattr(Integer setItmattr) {
		this.setItmattr = setItmattr;
	}
	public Integer getSetSubtbl() {
		return setSubtbl;
	}
	public void setSetSubtbl(Integer setSubtbl) {
		this.setSubtbl = setSubtbl;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getDecmpsState() {
		return decmpsState;
	}
	public void setDecmpsState(Integer decmpsState) {
		this.decmpsState = decmpsState;
	}
	public Integer getChargeState() {
		return chargeState;
	}
	public void setChargeState(Integer chargeState) {
		this.chargeState = chargeState;
	}
	public Integer getNeedDrug() {
		return needDrug;
	}
	public void setNeedDrug(Integer needDrug) {
		this.needDrug = needDrug;
	}
	public Integer getPrnExelist() {
		return prnExelist;
	}
	public void setPrnExelist(Integer prnExelist) {
		this.prnExelist = prnExelist;
	}
	public Integer getPrmMorlist() {
		return prmMorlist;
	}
	public void setPrmMorlist(Integer prmMorlist) {
		this.prmMorlist = prmMorlist;
	}
	public Integer getNeedConfirm() {
		return needConfirm;
	}
	public void setNeedConfirm(Integer needConfirm) {
		this.needConfirm = needConfirm;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getPharmacyCode() {
		return pharmacyCode;
	}
	public void setPharmacyCode(String pharmacyCode) {
		this.pharmacyCode = pharmacyCode;
	}
	public String getExecDpcd() {
		return execDpcd;
	}
	public void setExecDpcd(String execDpcd) {
		this.execDpcd = execDpcd;
	}
	public String getExecDpnm() {
		return execDpnm;
	}
	public void setExecDpnm(String execDpnm) {
		this.execDpnm = execDpnm;
	}
	public Double getBaseDose() {
		return baseDose;
	}
	public void setBaseDose(Double baseDose) {
		this.baseDose = baseDose;
	}
	public String getDoseUnit() {
		return doseUnit;
	}
	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}
	public String getMinUnit() {
		return minUnit;
	}
	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}
	public String getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}
	public Integer getPackQty() {
		return packQty;
	}
	public void setPackQty(Integer packQty) {
		this.packQty = packQty;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public String getDoseModelCode() {
		return doseModelCode;
	}
	public void setDoseModelCode(String doseModelCode) {
		this.doseModelCode = doseModelCode;
	}
	public String getDrugType() {
		return drugType;
	}
	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}
	public String getDrugQuality() {
		return drugQuality;
	}
	public void setDrugQuality(String drugQuality) {
		this.drugQuality = drugQuality;
	}
	public Double getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getCombNo() {
		return combNo;
	}
	public void setCombNo(String combNo) {
		this.combNo = combNo;
	}
	public Integer getMainDrug() {
		return mainDrug;
	}
	public void setMainDrug(Integer mainDrug) {
		this.mainDrug = mainDrug;
	}
	public Integer getMoStat() {
		return moStat;
	}
	public void setMoStat(Integer moStat) {
		this.moStat = moStat;
	}
	public String getUsageCode() {
		return usageCode;
	}
	public void setUsageCode(String usageCode) {
		this.usageCode = usageCode;
	}
	public String getUseName() {
		return useName;
	}
	public void setUseName(String useName) {
		this.useName = useName;
	}
	public String getEnglishAb() {
		return englishAb;
	}
	public void setEnglishAb(String englishAb) {
		this.englishAb = englishAb;
	}
	public String getFrequencyCode() {
		return frequencyCode;
	}
	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}
	public String getFrequencyName() {
		return frequencyName;
	}
	public void setFrequencyName(String frequencyName) {
		this.frequencyName = frequencyName;
	}
	public Double getDoseOnce() {
		return doseOnce;
	}
	public void setDoseOnce(Double doseOnce) {
		this.doseOnce = doseOnce;
	}
	public Integer getStocKin() {
		return stocKin;
	}
	public void setStocKin(Integer stocKin) {
		this.stocKin = stocKin;
	}
	public Integer getQtyTot() {
		return qtyTot;
	}
	public void setQtyTot(Integer qtyTot) {
		this.qtyTot = qtyTot;
	}
	public Integer getUseDays() {
		return useDays;
	}
	public void setUseDays(Integer useDays) {
		this.useDays = useDays;
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
	public String getRecUsercd() {
		return recUsercd;
	}
	public void setRecUsercd(String recUsercd) {
		this.recUsercd = recUsercd;
	}
	public String getRecUsernm() {
		return recUsernm;
	}
	public void setRecUsernm(String recUsernm) {
		this.recUsernm = recUsernm;
	}
	public Integer getConfirmFlag() {
		return confirmFlag;
	}
	public void setConfirmFlag(Integer confirmFlag) {
		this.confirmFlag = confirmFlag;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getConfirmUsercd() {
		return confirmUsercd;
	}
	public void setConfirmUsercd(String confirmUsercd) {
		this.confirmUsercd = confirmUsercd;
	}
	public Integer getDcFlag() {
		return dcFlag;
	}
	public void setDcFlag(Integer dcFlag) {
		this.dcFlag = dcFlag;
	}
	public Date getDcDate() {
		return dcDate;
	}
	public void setDcDate(Date dcDate) {
		this.dcDate = dcDate;
	}
	public String getDcCode() {
		return dcCode;
	}
	public void setDcCode(String dcCode) {
		this.dcCode = dcCode;
	}
	public String getDcName() {
		return dcName;
	}
	public void setDcName(String dcName) {
		this.dcName = dcName;
	}
	public String getDcDoccd() {
		return dcDoccd;
	}
	public void setDcDoccd(String dcDoccd) {
		this.dcDoccd = dcDoccd;
	}
	public String getDcDocnm() {
		return dcDocnm;
	}
	public void setDcDocnm(String dcDocnm) {
		this.dcDocnm = dcDocnm;
	}
	public String getDcUsercd() {
		return dcUsercd;
	}
	public void setDcUsercd(String dcUsercd) {
		this.dcUsercd = dcUsercd;
	}
	public String getDcUsernm() {
		return dcUsernm;
	}
	public void setDcUsernm(String dcUsernm) {
		this.dcUsernm = dcUsernm;
	}
	public Integer getExecuteFlag() {
		return executeFlag;
	}
	public void setExecuteFlag(Integer executeFlag) {
		this.executeFlag = executeFlag;
	}
	public Date getExecuteDate() {
		return executeDate;
	}
	public void setExecuteDate(Date executeDate) {
		this.executeDate = executeDate;
	}
	public String getExecuteUsercd() {
		return executeUsercd;
	}
	public void setExecuteUsercd(String executeUsercd) {
		this.executeUsercd = executeUsercd;
	}
	public Integer getDecoFlag() {
		return decoFlag;
	}
	public void setDecoFlag(Integer decoFlag) {
		this.decoFlag = decoFlag;
	}
	public Date getDateCurmodc() {
		return dateCurmodc;
	}
	public void setDateCurmodc(Date dateCurmodc) {
		this.dateCurmodc = dateCurmodc;
	}
	public Date getDateNxtmodc() {
		return dateNxtmodc;
	}
	public void setDateNxtmodc(Date dateNxtmodc) {
		this.dateNxtmodc = dateNxtmodc;
	}
	public String getMoNote1() {
		return moNote1;
	}
	public void setMoNote1(String moNote1) {
		this.moNote1 = moNote1;
	}
	public String getMoNote2() {
		return moNote2;
	}
	public void setMoNote2(String moNote2) {
		this.moNote2 = moNote2;
	}
	public Integer getHypotest() {
		return hypotest;
	}
	public void setHypotest(Integer hypotest) {
		this.hypotest = hypotest;
	}
	public String getItemNote() {
		return itemNote;
	}
	public void setItemNote(String itemNote) {
		this.itemNote = itemNote;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public Integer getEmcFlag() {
		return emcFlag;
	}
	public void setEmcFlag(Integer emcFlag) {
		this.emcFlag = emcFlag;
	}
	public Integer getGetFlag() {
		return getFlag;
	}
	public void setGetFlag(Integer getFlag) {
		this.getFlag = getFlag;
	}
	public Integer getSubtblFlag() {
		return subtblFlag;
	}
	public void setSubtblFlag(Integer subtblFlag) {
		this.subtblFlag = subtblFlag;
	}
	public Integer getSortId() {
		return sortId;
	}
	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}
	public Date getDcConfirmDate() {
		return dcConfirmDate;
	}
	public void setDcConfirmDate(Date dcConfirmDate) {
		this.dcConfirmDate = dcConfirmDate;
	}
	public String getDcConfirmOper() {
		return dcConfirmOper;
	}
	public void setDcConfirmOper(String dcConfirmOper) {
		this.dcConfirmOper = dcConfirmOper;
	}
	public Integer getDcConfirmFlag() {
		return dcConfirmFlag;
	}
	public void setDcConfirmFlag(Integer dcConfirmFlag) {
		this.dcConfirmFlag = dcConfirmFlag;
	}
	public String getLabCode() {
		return labCode;
	}
	public void setLabCode(String labCode) {
		this.labCode = labCode;
	}
	public Integer getPermission() {
		return permission;
	}
	public void setPermission(Integer permission) {
		this.permission = permission;
	}
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getMark1() {
		return mark1;
	}
	public void setMark1(String mark1) {
		this.mark1 = mark1;
	}
	public String getMark2() {
		return mark2;
	}
	public void setMark2(String mark2) {
		this.mark2 = mark2;
	}
	public String getMark3() {
		return mark3;
	}
	public void setMark3(String mark3) {
		this.mark3 = mark3;
	}
	public String getExecTimes() {
		return execTimes;
	}
	public void setExecTimes(String execTimes) {
		this.execTimes = execTimes;
	}
	public String getExecDose() {
		return execDose;
	}
	public void setExecDose(String execDose) {
		this.execDose = execDose;
	}
   	
   	
   	
}
