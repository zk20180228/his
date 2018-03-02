package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class InpatientApplyNow extends Entity{

	private static final long serialVersionUID = 1L;
	//用血申请单号
	private String  applyNum;
	//申请状态
	private String applyState;
	//1 门诊 ，2 住院
	private String patientType;
	//住院号/门诊病历号
	private String patientNo;
	//就诊流水号
	private String clinicNo;
	//患者姓名
	private String name;
	//性别
	private String sexCode;
	//年龄
	private String age;
	//病区
	private String deptCode;
	//所属护士站
	private String nurseCellCode;
	//床号
	private String bedNo;
	//临时诊断
	private String diagnose;
	//诊断名称
	private String diagnoseName;
	//输血目的
	private String bloodAim;
	//输血性质
	private String quality;
	//受血者属地
	private String insource;
	//预定输血日期
	private Date orderTime;
	//申请血型
	private String bloodKind;
	//申请血液成分
	private String bloodTypeCode;
	//申请数量
	private Double quantity;
	//申请数量单位
	private String stockUnit;
	//RH  1 阳性  0 阴性  2 待查
	private String rh;
	//孕产情况
	private String pregnant;
	//受血者血红蛋白
	private Double hematin;
	//受血者HCT
	private Double hct;
	//受血者血小板
	private Double platelet;
	//受血者ALT
	private Double alt;
	//受血者Anti-HCV   1 阳性  0 阴性  2 待查
	private String antiHcv;
	//受血者Anti-HIV1/2  1 阳性  0 阴性  2 待查
	private String antiHiv;
	//受血者梅毒  1 阳性  0 阴性  2 待查
	private String lues;
	//受血者HbsAg  1 阳性  0 阴性  2 待查
	private String hbsag;
	//既往输血史(0无,有n次)
	private String bloodhistory;
	//受血者血型
	private String patientBloodkind;
	//是否收费
	private String isCharge;
	//申请医师
	private String applyDocCode;
	//申请时间
	private Date applyTime;
	//主治医师
	private String chargeDocCode;
	//备注
	private String remarks;
	//配血结果 1 有效 0 无效
	private String matchResult;
	//作废人
	private String cancelCode;
	//作废时间
	private Date cancelDate;
	//受血者Anti-HBs  1 阳性  0 阴性  2 待查
	private String antiHbs;
	//受血者HBeAg  1 阳性  0 阴性  2 待查
	private String hbeag;
	//受血者Anti-HBe  1 阳性  0 阴性  2 待查
	private String antiHbe;
	//受血者Anti-HBc  1 阳性  0 阴性  2 待查
	private String antiHbc;
	//用血申请核准人
	private String approvalOper;
	//用血申请核准时间
	private Date approvalDate;
	//用血申请条码
	private String applyBarcode;
	//是否采血(0:未采血  1:已采血)
	private String getbloodFlag;
	//采血人
	private String getbloodOper;
	//采血时间
	private Date getbloodDate;
	//审批结果 0 无需审 1 未审 2 已审
	private String examResult;
	//审批人
	private String examOperCode;
	//审批时间
	private Date examDate;
	//备血样本登记量
	private String sampleRegQty;
	//备血样本登记人
	private String sampleRegOper;
	//备血样本登记时间
	private Date  sampleDate;
	//申请血液成分2
	private String bloodTypeCode2;
	//申请数量2
	private Double quantity2;
	//血液成分单位2
	private String stockUint2;
	//申请血液成分3
	private String bloodTypeCode3;
	//申请数量3
	private Double quantity3;
	//血液成分单位3
	private String stockUint3;
	//受血者白细胞
	private Double wbc;
	//不核收原因
	private String uncheckReason;
	//输血科处理
	private String backdeal;
	//输血科说明
	private String backdealMeMo;
	//受血者不规则抗体筛选结果(0表示阴性 1表示阳性)
	private String antifilterFlag;
	//输血方式
	private String bloodmethod;
	//是否加急用血(0-否 1-是)
	private String isUrgent;
	//受血者PT
	private  Double pt;
	//受血者APTT
	private  Double aptt;
	//受血者FIB
	private  Double fib;
	//连续输注天数(0无,n天数)
	private String infusionday;
	//费用类别(0-自费用血,1-医保用血)
	private String feetype;
	//新加字段
	/**所属护士站名称**/
	private String nurseCellName;
	/**申请医师名称**/
	private String applyDocName;
	/**主治医师名称**/
	private String chargeDocName;
	/**性别名称**/
	private String sexName;
	/**作废人名称**/
	private String cancelName;
	/**用血申请核准人名称**/
	private String approvalOperName;
	/**科室名称**/
	private String deptName;
	/**医院编码**/
	private Integer hospitalId;
	/**院区编码**/
	private String areaCode;
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getNurseCellName() {
		return nurseCellName;
	}
	public void setNurseCellName(String nurseCellName) {
		this.nurseCellName = nurseCellName;
	}
	public String getApplyDocName() {
		return applyDocName;
	}
	public void setApplyDocName(String applyDocName) {
		this.applyDocName = applyDocName;
	}
	public String getChargeDocName() {
		return chargeDocName;
	}
	public void setChargeDocName(String chargeDocName) {
		this.chargeDocName = chargeDocName;
	}
	public String getSexName() {
		return sexName;
	}
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	public String getCancelName() {
		return cancelName;
	}
	public void setCancelName(String cancelName) {
		this.cancelName = cancelName;
	}
	public String getApprovalOperName() {
		return approvalOperName;
	}
	public void setApprovalOperName(String approvalOperName) {
		this.approvalOperName = approvalOperName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getApplyNum() {
		return applyNum;
	}
	public void setApplyNum(String applyNum) {
		this.applyNum = applyNum;
	}
	public String getApplyState() {
		return applyState;
	}
	public void setApplyState(String applyState) {
		this.applyState = applyState;
	}
	public String getPatientType() {
		return patientType;
	}
	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public String getClinicNo() {
		return clinicNo;
	}
	public void setClinicNo(String clinicNo) {
		this.clinicNo = clinicNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSexCode() {
		return sexCode;
	}
	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
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
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getDiagnose() {
		return diagnose;
	}
	public void setDiagnose(String diagnose) {
		this.diagnose = diagnose;
	}
	public String getDiagnoseName() {
		return diagnoseName;
	}
	public void setDiagnoseName(String diagnoseName) {
		this.diagnoseName = diagnoseName;
	}
	public String getBloodAim() {
		return bloodAim;
	}
	public void setBloodAim(String bloodAim) {
		this.bloodAim = bloodAim;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getInsource() {
		return insource;
	}
	public void setInsource(String insource) {
		this.insource = insource;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public String getBloodKind() {
		return bloodKind;
	}
	public void setBloodKind(String bloodKind) {
		this.bloodKind = bloodKind;
	}
	public String getBloodTypeCode() {
		return bloodTypeCode;
	}
	public void setBloodTypeCode(String bloodTypeCode) {
		this.bloodTypeCode = bloodTypeCode;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public String getStockUnit() {
		return stockUnit;
	}
	public void setStockUnit(String stockUnit) {
		this.stockUnit = stockUnit;
	}
	public String getRh() {
		return rh;
	}
	public void setRh(String rh) {
		this.rh = rh;
	}
	public String getPregnant() {
		return pregnant;
	}
	public void setPregnant(String pregnant) {
		this.pregnant = pregnant;
	}
	public Double getHematin() {
		return hematin;
	}
	public void setHematin(Double hematin) {
		this.hematin = hematin;
	}
	public Double getHct() {
		return hct;
	}
	public void setHct(Double hct) {
		this.hct = hct;
	}
	public Double getPlatelet() {
		return platelet;
	}
	public void setPlatelet(Double platelet) {
		this.platelet = platelet;
	}
	public Double getAlt() {
		return alt;
	}
	public void setAlt(Double alt) {
		this.alt = alt;
	}
	public String getAntiHcv() {
		return antiHcv;
	}
	public void setAntiHcv(String antiHcv) {
		this.antiHcv = antiHcv;
	}
	public String getAntiHiv() {
		return antiHiv;
	}
	public void setAntiHiv(String antiHiv) {
		this.antiHiv = antiHiv;
	}
	public String getLues() {
		return lues;
	}
	public void setLues(String lues) {
		this.lues = lues;
	}
	public String getHbsag() {
		return hbsag;
	}
	public void setHbsag(String hbsag) {
		this.hbsag = hbsag;
	}
	public String getBloodhistory() {
		return bloodhistory;
	}
	public void setBloodhistory(String bloodhistory) {
		this.bloodhistory = bloodhistory;
	}
	public String getPatientBloodkind() {
		return patientBloodkind;
	}
	public void setPatientBloodkind(String patientBloodkind) {
		this.patientBloodkind = patientBloodkind;
	}
	public String getIsCharge() {
		return isCharge;
	}
	public void setIsCharge(String isCharge) {
		this.isCharge = isCharge;
	}
	public String getApplyDocCode() {
		return applyDocCode;
	}
	public void setApplyDocCode(String applyDocCode) {
		this.applyDocCode = applyDocCode;
	}
	public Date getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	public String getChargeDocCode() {
		return chargeDocCode;
	}
	public void setChargeDocCode(String chargeDocCode) {
		this.chargeDocCode = chargeDocCode;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getMatchResult() {
		return matchResult;
	}
	public void setMatchResult(String matchResult) {
		this.matchResult = matchResult;
	}
	public String getCancelCode() {
		return cancelCode;
	}
	public void setCancelCode(String cancelCode) {
		this.cancelCode = cancelCode;
	}
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	public String getAntiHbs() {
		return antiHbs;
	}
	public void setAntiHbs(String antiHbs) {
		this.antiHbs = antiHbs;
	}
	public String getHbeag() {
		return hbeag;
	}
	public void setHbeag(String hbeag) {
		this.hbeag = hbeag;
	}
	public String getAntiHbe() {
		return antiHbe;
	}
	public void setAntiHbe(String antiHbe) {
		this.antiHbe = antiHbe;
	}
	public String getAntiHbc() {
		return antiHbc;
	}
	public void setAntiHbc(String antiHbc) {
		this.antiHbc = antiHbc;
	}
	public String getApprovalOper() {
		return approvalOper;
	}
	public void setApprovalOper(String approvalOper) {
		this.approvalOper = approvalOper;
	}
	public Date getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getApplyBarcode() {
		return applyBarcode;
	}
	public void setApplyBarcode(String applyBarcode) {
		this.applyBarcode = applyBarcode;
	}
	public String getGetbloodFlag() {
		return getbloodFlag;
	}
	public void setGetbloodFlag(String getbloodFlag) {
		this.getbloodFlag = getbloodFlag;
	}
	public String getGetbloodOper() {
		return getbloodOper;
	}
	public void setGetbloodOper(String getbloodOper) {
		this.getbloodOper = getbloodOper;
	}
	public Date getGetbloodDate() {
		return getbloodDate;
	}
	public void setGetbloodDate(Date getbloodDate) {
		this.getbloodDate = getbloodDate;
	}
	public String getExamResult() {
		return examResult;
	}
	public void setExamResult(String examResult) {
		this.examResult = examResult;
	}
	public String getExamOperCode() {
		return examOperCode;
	}
	public void setExamOperCode(String examOperCode) {
		this.examOperCode = examOperCode;
	}
	public Date getExamDate() {
		return examDate;
	}
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
	public String getSampleRegQty() {
		return sampleRegQty;
	}
	public void setSampleRegQty(String sampleRegQty) {
		this.sampleRegQty = sampleRegQty;
	}
	public String getSampleRegOper() {
		return sampleRegOper;
	}
	public void setSampleRegOper(String sampleRegOper) {
		this.sampleRegOper = sampleRegOper;
	}
	public Date getSampleDate() {
		return sampleDate;
	}
	public void setSampleDate(Date sampleDate) {
		this.sampleDate = sampleDate;
	}
	public String getBloodTypeCode2() {
		return bloodTypeCode2;
	}
	public void setBloodTypeCode2(String bloodTypeCode2) {
		this.bloodTypeCode2 = bloodTypeCode2;
	}
	public Double getQuantity2() {
		return quantity2;
	}
	public void setQuantity2(Double quantity2) {
		this.quantity2 = quantity2;
	}
	public String getStockUint2() {
		return stockUint2;
	}
	public void setStockUint2(String stockUint2) {
		this.stockUint2 = stockUint2;
	}
	public String getBloodTypeCode3() {
		return bloodTypeCode3;
	}
	public void setBloodTypeCode3(String bloodTypeCode3) {
		this.bloodTypeCode3 = bloodTypeCode3;
	}
	public Double getQuantity3() {
		return quantity3;
	}
	public void setQuantity3(Double quantity3) {
		this.quantity3 = quantity3;
	}
	public String getStockUint3() {
		return stockUint3;
	}
	public void setStockUint3(String stockUint3) {
		this.stockUint3 = stockUint3;
	}
	public Double getWbc() {
		return wbc;
	}
	public void setWbc(Double wbc) {
		this.wbc = wbc;
	}
	public String getUncheckReason() {
		return uncheckReason;
	}
	public void setUncheckReason(String uncheckReason) {
		this.uncheckReason = uncheckReason;
	}
	public String getBackdeal() {
		return backdeal;
	}
	public void setBackdeal(String backdeal) {
		this.backdeal = backdeal;
	}
	public String getBackdealMeMo() {
		return backdealMeMo;
	}
	public void setBackdealMeMo(String backdealMeMo) {
		this.backdealMeMo = backdealMeMo;
	}
	public String getAntifilterFlag() {
		return antifilterFlag;
	}
	public void setAntifilterFlag(String antifilterFlag) {
		this.antifilterFlag = antifilterFlag;
	}
	public String getBloodmethod() {
		return bloodmethod;
	}
	public void setBloodmethod(String bloodmethod) {
		this.bloodmethod = bloodmethod;
	}
	public String getIsUrgent() {
		return isUrgent;
	}
	public void setIsUrgent(String isUrgent) {
		this.isUrgent = isUrgent;
	}
	public Double getPt() {
		return pt;
	}
	public void setPt(Double pt) {
		this.pt = pt;
	}
	public Double getAptt() {
		return aptt;
	}
	public void setAptt(Double aptt) {
		this.aptt = aptt;
	}
	public Double getFib() {
		return fib;
	}
	public void setFib(Double fib) {
		this.fib = fib;
	}
	public String getInfusionday() {
		return infusionday;
	}
	public void setInfusionday(String infusionday) {
		this.infusionday = infusionday;
	}
	public String getFeetype() {
		return feetype;
	}
	public void setFeetype(String feetype) {
		this.feetype = feetype;
	}
	
}
