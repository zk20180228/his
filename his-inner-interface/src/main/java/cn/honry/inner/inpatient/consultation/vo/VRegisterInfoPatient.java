package cn.honry.inner.inpatient.consultation.vo;

import java.math.BigDecimal;
import java.util.Date;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RegisterSchedule;

public class VRegisterInfoPatient {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**挂号信息表id**/
	private String RegisterInfoId;
	/**排班编号（外键）**/
	private RegisterSchedule scheduleId;
	/**患者编号**/
	private Patient patientId;
	/**门诊号**/
	private String no;
	/**挂号级别**/
	private String grade;
	/**挂号科室**/
	private String dept;
	/**挂号专家**/
	private String expxrt;
	/**挂号日期**/
	private Date date;
	/**挂号类别**/
	private String type;
	/**顺序号**/
	private Integer order;
	/**病例号**/
	private String midicalrecordId;
	/**挂号费用**/
	private Double fee;
	/**午别**/
	private Integer midday;
	/**合同单位**/
	private String contractunit;
	/**就诊卡号**/
	private String idcardId;
	/**挂号收费标志**/
	private Integer paid=1;
	/**是否初诊**/
	private Integer isfiest=0;
	/**是否加号**/
	private Integer appflag=0;
	/**结算类别**/
	private String paykindCode;
	/**分诊类别**/
	private String triageType;
	/**呼叫状态**/
	private Integer callStatus=-1;
	/**分诊标志**/
	private Integer triageflag=0;
	/**分诊护士代码**/
	private String nurse;
	/**分诊时间**/
	private Date triageDate;
	/**是否看诊**/
	private Integer seeFlag=0;
	/**看诊序号**/
	private Integer seeno;
	/**看诊开始时间**/
	private Date seeStartTime;
	/**看诊结束时间**/
	private Date seeEndTime;
	/**看诊日期**/
	private Date seeDate;
	/**发票号**/
	private String invoiceNo;
	/**处方号**/
	private String pecipeNo;
	/**挂号状态**/
	private Integer status=1;
	/**退号原因**/
	private String quitreason;
	/**支付类型**/
	private String payType;
	/**开户单位**/
	private String bankUnit;
	/**开户银行**/
	private String bank;
	/**银行账号**/
	private String bankAccount;
	/**小票号**/
	private String bankBillno;
	/**发票打印标记 0未打印 1打印 2 补打**/
	private Integer invoiceprintflag; 
	/**病历本费**/
	private Double medicalRecordBookPay;
	/**是否购买病历本**/
	private Integer medicalRecordBookFlay;
	
	
	
	//患者信息
	private String Id;
	/**  编号 **/
	private BusinessContractunit businessContractunit;
	/**  血型 **/
	private String patientBloodcode;
	/**  患者年龄 **/
	private Double patientAge;
	/**  1:年 2 月 3 天 **/
	private BigDecimal patientAgeunit;
	/**  户口或家庭邮编 **/
	private String patientHomezip;
	/**  单位邮编 **/
	private String patientWorkzip;
	/**  是否药物过敏 **/
	private Boolean patientIsanaphy;
	/** 是否重大疾病 **/
	private Boolean patientIshepatitis;
	/** 患者姓名  **/
	private String patientName;
	/** 拼音码  **/
	private String patientPinyin;
	/** 五笔码  **/
	private String patientWb;
	/** 自定义码	  **/
	private String patientInputcode;
	/** 性别  **/
	private Integer patientSex;
	/** 出生日期  **/
	private Date patientBirthday;
	/** 家庭地址  **/
	private String patientAddress;
	/**家庭地址具体省市县**/
	private String patientCity;	
	/** 门牌号  **/
	private String patientDoorno;
	/** 电话  **/
	private String patientPhone;
	/**证件类型   **/
	private String patientCertificatestype;
	/** 证件号码  **/
	private String patientCertificatesno;
	/** 出生地  **/
	private String patientBirthplace;
	/** 籍贯  **/
	private String patientNativeplace;
	/**国籍   **/
	private String patientNationality;
	/** 民族  **/
	private String patientNation;
	/** 工作单位  **/
	private String patientWorkunit;
	/** 单位电话  **/
	private String patientWorkphone;
	/** 婚姻状况  **/
	private String patientWarriage;
	/** 职业  **/
	private String patientOccupation;
	/** 医保手册号  **/
	private String patientHandbook;
	/** 电子邮箱  **/
	private String patientEmail;
	/** 母亲姓名  **/
	private String patientMother;
	/** 联系人  **/
	private String patientLinkman;
	/** 联系人关系  **/
	private String patientLinkrelation;
	/** 联系人地址  **/
	private String patientLinkaddress;
	/** 联系人门牌号  **/
	private String patientLinkdoorno;
	/** 联系电话  **/
	private String patientLinkphone;	
	/** 新添加字段 病历号  **/
	private String medicalrecordId;
	
	private String ConsultationId;
	/**会诊流水号*/
	private String cnslNO;
	/**住院流水号(6位日期+6位流水号)*/
	private String inpatientNo;
	/**住院病历号*/
	private String patientNo;
	/**住院科室代码*/
	private String deptCode;
	/**护理站代码*/
	private String nueseCellCode;
	/**医嘱医师代号*/
	private String docCode;
	/**医嘱医师姓名*/
	private String docName; 
	/**填写申请日期*/
	private Date applyDate;
	/**预约会诊日期*/
	private Date cnslDate;
	/**会诊类型，1科室/0医生 2 院外会诊*/
	private String cnslKind;
	/**加急会诊,1是/0否*/
	private String urgentFlag;
	/**会诊科室*/
	private String cnslDeptcd;
	/**会诊医师*/
	private String cnslDoccd;
	/**会诊说明*/
	private String cnslNote;
	/**处方起始日*/
	private Date moStdt;
	/**处方结束日*/
	private Date moEddt;
	/**实际会诊日*/
	private Date cnslExdt;
	/**会诊结果*/
	private String cnslRslt;
	/**确认医生代码*/
	private String confirmDoccd;
	/**会诊状态,1申请/2确认*/
	private String cnslStatus;
	/**用户代码*/
	private String operCode;
	/**医院名称*/
	private String hosName;
	/**紧急说明*/
	private String urgentMemo;
	/**简要病历*/
	private String cnslNote1;
	/**检查结果*/
	private String cnslNote2;
	/**初步诊断意见*/
	private String cnslNote3;
	/**会诊地点*/
	private String location;
	/**患者床号*/
	private String bedNo;
	/**会诊纪录*/
	private String cnslRecord;
	/**会诊意见及建议*/
	private String cnslSuggestion;
	/**能开立医嘱,1是/0否*/
	private String createOrderFlag;
	/**目前诊断(申请)*/
	private String zd1;
	/**治疗措施(申请)*/
	private String zl1;
	/**会诊目的(申请)*/
	private String hzmd1;
	/**诊断(会诊者填写)*/
	private String zd2;
	/**处理(会诊者填写)*/
	private String cl2;
	
	
	public String getConsultationId() {
		return ConsultationId;
	}
	public void setConsultationId(String consultationId) {
		ConsultationId = consultationId;
	}
	public String getCnslNO() {
		return cnslNO;
	}
	public void setCnslNO(String cnslNO) {
		this.cnslNO = cnslNO;
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
	public String getNueseCellCode() {
		return nueseCellCode;
	}
	public void setNueseCellCode(String nueseCellCode) {
		this.nueseCellCode = nueseCellCode;
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
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public Date getCnslDate() {
		return cnslDate;
	}
	public void setCnslDate(Date cnslDate) {
		this.cnslDate = cnslDate;
	}
	public String getCnslKind() {
		return cnslKind;
	}
	public void setCnslKind(String cnslKind) {
		this.cnslKind = cnslKind;
	}
	public String getUrgentFlag() {
		return urgentFlag;
	}
	public void setUrgentFlag(String urgentFlag) {
		this.urgentFlag = urgentFlag;
	}
	public String getCnslDeptcd() {
		return cnslDeptcd;
	}
	public void setCnslDeptcd(String cnslDeptcd) {
		this.cnslDeptcd = cnslDeptcd;
	}
	public String getCnslDoccd() {
		return cnslDoccd;
	}
	public void setCnslDoccd(String cnslDoccd) {
		this.cnslDoccd = cnslDoccd;
	}
	public String getCnslNote() {
		return cnslNote;
	}
	public void setCnslNote(String cnslNote) {
		this.cnslNote = cnslNote;
	}
	public Date getMoStdt() {
		return moStdt;
	}
	public void setMoStdt(Date moStdt) {
		this.moStdt = moStdt;
	}
	public Date getMoEddt() {
		return moEddt;
	}
	public void setMoEddt(Date moEddt) {
		this.moEddt = moEddt;
	}
	public Date getCnslExdt() {
		return cnslExdt;
	}
	public void setCnslExdt(Date cnslExdt) {
		this.cnslExdt = cnslExdt;
	}
	public String getCnslRslt() {
		return cnslRslt;
	}
	public void setCnslRslt(String cnslRslt) {
		this.cnslRslt = cnslRslt;
	}
	public String getConfirmDoccd() {
		return confirmDoccd;
	}
	public void setConfirmDoccd(String confirmDoccd) {
		this.confirmDoccd = confirmDoccd;
	}
	public String getCnslStatus() {
		return cnslStatus;
	}
	public void setCnslStatus(String cnslStatus) {
		this.cnslStatus = cnslStatus;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public String getHosName() {
		return hosName;
	}
	public void setHosName(String hosName) {
		this.hosName = hosName;
	}
	public String getUrgentMemo() {
		return urgentMemo;
	}
	public void setUrgentMemo(String urgentMemo) {
		this.urgentMemo = urgentMemo;
	}
	public String getCnslNote1() {
		return cnslNote1;
	}
	public void setCnslNote1(String cnslNote1) {
		this.cnslNote1 = cnslNote1;
	}
	public String getCnslNote2() {
		return cnslNote2;
	}
	public void setCnslNote2(String cnslNote2) {
		this.cnslNote2 = cnslNote2;
	}
	public String getCnslNote3() {
		return cnslNote3;
	}
	public void setCnslNote3(String cnslNote3) {
		this.cnslNote3 = cnslNote3;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getCnslRecord() {
		return cnslRecord;
	}
	public void setCnslRecord(String cnslRecord) {
		this.cnslRecord = cnslRecord;
	}
	public String getCnslSuggestion() {
		return cnslSuggestion;
	}
	public void setCnslSuggestion(String cnslSuggestion) {
		this.cnslSuggestion = cnslSuggestion;
	}
	public String getCreateOrderFlag() {
		return createOrderFlag;
	}
	public void setCreateOrderFlag(String createOrderFlag) {
		this.createOrderFlag = createOrderFlag;
	}
	public String getZd1() {
		return zd1;
	}
	public void setZd1(String zd1) {
		this.zd1 = zd1;
	}
	public String getZl1() {
		return zl1;
	}
	public void setZl1(String zl1) {
		this.zl1 = zl1;
	}
	public String getHzmd1() {
		return hzmd1;
	}
	public void setHzmd1(String hzmd1) {
		this.hzmd1 = hzmd1;
	}
	public String getZd2() {
		return zd2;
	}
	public void setZd2(String zd2) {
		this.zd2 = zd2;
	}
	public String getCl2() {
		return cl2;
	}
	public void setCl2(String cl2) {
		this.cl2 = cl2;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getRegisterInfoId() {
		return RegisterInfoId;
	}
	public void setRegisterInfoId(String registerInfoId) {
		RegisterInfoId = registerInfoId;
	}
	public RegisterSchedule getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(RegisterSchedule scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Patient getPatientId() {
		return patientId;
	}
	public void setPatientId(Patient patientId) {
		this.patientId = patientId;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getExpxrt() {
		return expxrt;
	}
	public void setExpxrt(String expxrt) {
		this.expxrt = expxrt;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getMidicalrecordId() {
		return midicalrecordId;
	}
	public void setMidicalrecordId(String midicalrecordId) {
		this.midicalrecordId = midicalrecordId;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	public Integer getMidday() {
		return midday;
	}
	public void setMidday(Integer midday) {
		this.midday = midday;
	}
	public String getContractunit() {
		return contractunit;
	}
	public void setContractunit(String contractunit) {
		this.contractunit = contractunit;
	}
	public String getIdcardId() {
		return idcardId;
	}
	public void setIdcardId(String idcardId) {
		this.idcardId = idcardId;
	}
	public Integer getPaid() {
		return paid;
	}
	public void setPaid(Integer paid) {
		this.paid = paid;
	}
	public Integer getIsfiest() {
		return isfiest;
	}
	public void setIsfiest(Integer isfiest) {
		this.isfiest = isfiest;
	}
	public Integer getAppflag() {
		return appflag;
	}
	public void setAppflag(Integer appflag) {
		this.appflag = appflag;
	}
	public String getPaykindCode() {
		return paykindCode;
	}
	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	}
	public String getTriageType() {
		return triageType;
	}
	public void setTriageType(String triageType) {
		this.triageType = triageType;
	}
	public Integer getCallStatus() {
		return callStatus;
	}
	public void setCallStatus(Integer callStatus) {
		this.callStatus = callStatus;
	}
	public Integer getTriageflag() {
		return triageflag;
	}
	public void setTriageflag(Integer triageflag) {
		this.triageflag = triageflag;
	}
	public String getNurse() {
		return nurse;
	}
	public void setNurse(String nurse) {
		this.nurse = nurse;
	}
	public Date getTriageDate() {
		return triageDate;
	}
	public void setTriageDate(Date triageDate) {
		this.triageDate = triageDate;
	}
	public Integer getSeeFlag() {
		return seeFlag;
	}
	public void setSeeFlag(Integer seeFlag) {
		this.seeFlag = seeFlag;
	}
	public Integer getSeeno() {
		return seeno;
	}
	public void setSeeno(Integer seeno) {
		this.seeno = seeno;
	}
	public Date getSeeStartTime() {
		return seeStartTime;
	}
	public void setSeeStartTime(Date seeStartTime) {
		this.seeStartTime = seeStartTime;
	}
	public Date getSeeEndTime() {
		return seeEndTime;
	}
	public void setSeeEndTime(Date seeEndTime) {
		this.seeEndTime = seeEndTime;
	}
	public Date getSeeDate() {
		return seeDate;
	}
	public void setSeeDate(Date seeDate) {
		this.seeDate = seeDate;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getPecipeNo() {
		return pecipeNo;
	}
	public void setPecipeNo(String pecipeNo) {
		this.pecipeNo = pecipeNo;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getQuitreason() {
		return quitreason;
	}
	public void setQuitreason(String quitreason) {
		this.quitreason = quitreason;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getBankUnit() {
		return bankUnit;
	}
	public void setBankUnit(String bankUnit) {
		this.bankUnit = bankUnit;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getBankBillno() {
		return bankBillno;
	}
	public void setBankBillno(String bankBillno) {
		this.bankBillno = bankBillno;
	}
	public Integer getInvoiceprintflag() {
		return invoiceprintflag;
	}
	public void setInvoiceprintflag(Integer invoiceprintflag) {
		this.invoiceprintflag = invoiceprintflag;
	}
	public Double getMedicalRecordBookPay() {
		return medicalRecordBookPay;
	}
	public void setMedicalRecordBookPay(Double medicalRecordBookPay) {
		this.medicalRecordBookPay = medicalRecordBookPay;
	}
	public Integer getMedicalRecordBookFlay() {
		return medicalRecordBookFlay;
	}
	public void setMedicalRecordBookFlay(Integer medicalRecordBookFlay) {
		this.medicalRecordBookFlay = medicalRecordBookFlay;
	}
	public BusinessContractunit getBusinessContractunit() {
		return businessContractunit;
	}
	public void setBusinessContractunit(BusinessContractunit businessContractunit) {
		this.businessContractunit = businessContractunit;
	}
	public String getPatientBloodcode() {
		return patientBloodcode;
	}
	public void setPatientBloodcode(String patientBloodcode) {
		this.patientBloodcode = patientBloodcode;
	}
	public Double getPatientAge() {
		return patientAge;
	}
	public void setPatientAge(Double patientAge) {
		this.patientAge = patientAge;
	}
	public BigDecimal getPatientAgeunit() {
		return patientAgeunit;
	}
	public void setPatientAgeunit(BigDecimal patientAgeunit) {
		this.patientAgeunit = patientAgeunit;
	}
	public String getPatientHomezip() {
		return patientHomezip;
	}
	public void setPatientHomezip(String patientHomezip) {
		this.patientHomezip = patientHomezip;
	}
	public String getPatientWorkzip() {
		return patientWorkzip;
	}
	public void setPatientWorkzip(String patientWorkzip) {
		this.patientWorkzip = patientWorkzip;
	}
	public Boolean getPatientIsanaphy() {
		return patientIsanaphy;
	}
	public void setPatientIsanaphy(Boolean patientIsanaphy) {
		this.patientIsanaphy = patientIsanaphy;
	}
	public Boolean getPatientIshepatitis() {
		return patientIshepatitis;
	}
	public void setPatientIshepatitis(Boolean patientIshepatitis) {
		this.patientIshepatitis = patientIshepatitis;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPatientPinyin() {
		return patientPinyin;
	}
	public void setPatientPinyin(String patientPinyin) {
		this.patientPinyin = patientPinyin;
	}
	public String getPatientWb() {
		return patientWb;
	}
	public void setPatientWb(String patientWb) {
		this.patientWb = patientWb;
	}
	public String getPatientInputcode() {
		return patientInputcode;
	}
	public void setPatientInputcode(String patientInputcode) {
		this.patientInputcode = patientInputcode;
	}
	public Integer getPatientSex() {
		return patientSex;
	}
	public void setPatientSex(Integer patientSex) {
		this.patientSex = patientSex;
	}
	public Date getPatientBirthday() {
		return patientBirthday;
	}
	public void setPatientBirthday(Date patientBirthday) {
		this.patientBirthday = patientBirthday;
	}
	public String getPatientAddress() {
		return patientAddress;
	}
	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}
	public String getPatientCity() {
		return patientCity;
	}
	public void setPatientCity(String patientCity) {
		this.patientCity = patientCity;
	}
	public String getPatientDoorno() {
		return patientDoorno;
	}
	public void setPatientDoorno(String patientDoorno) {
		this.patientDoorno = patientDoorno;
	}
	public String getPatientPhone() {
		return patientPhone;
	}
	public void setPatientPhone(String patientPhone) {
		this.patientPhone = patientPhone;
	}
	public String getPatientCertificatestype() {
		return patientCertificatestype;
	}
	public void setPatientCertificatestype(String patientCertificatestype) {
		this.patientCertificatestype = patientCertificatestype;
	}
	public String getPatientCertificatesno() {
		return patientCertificatesno;
	}
	public void setPatientCertificatesno(String patientCertificatesno) {
		this.patientCertificatesno = patientCertificatesno;
	}
	public String getPatientBirthplace() {
		return patientBirthplace;
	}
	public void setPatientBirthplace(String patientBirthplace) {
		this.patientBirthplace = patientBirthplace;
	}
	public String getPatientNativeplace() {
		return patientNativeplace;
	}
	public void setPatientNativeplace(String patientNativeplace) {
		this.patientNativeplace = patientNativeplace;
	}
	public String getPatientNationality() {
		return patientNationality;
	}
	public void setPatientNationality(String patientNationality) {
		this.patientNationality = patientNationality;
	}
	public String getPatientNation() {
		return patientNation;
	}
	public void setPatientNation(String patientNation) {
		this.patientNation = patientNation;
	}
	public String getPatientWorkunit() {
		return patientWorkunit;
	}
	public void setPatientWorkunit(String patientWorkunit) {
		this.patientWorkunit = patientWorkunit;
	}
	public String getPatientWorkphone() {
		return patientWorkphone;
	}
	public void setPatientWorkphone(String patientWorkphone) {
		this.patientWorkphone = patientWorkphone;
	}
	public String getPatientWarriage() {
		return patientWarriage;
	}
	public void setPatientWarriage(String patientWarriage) {
		this.patientWarriage = patientWarriage;
	}
	public String getPatientOccupation() {
		return patientOccupation;
	}
	public void setPatientOccupation(String patientOccupation) {
		this.patientOccupation = patientOccupation;
	}
	public String getPatientHandbook() {
		return patientHandbook;
	}
	public void setPatientHandbook(String patientHandbook) {
		this.patientHandbook = patientHandbook;
	}
	public String getPatientEmail() {
		return patientEmail;
	}
	public void setPatientEmail(String patientEmail) {
		this.patientEmail = patientEmail;
	}
	public String getPatientMother() {
		return patientMother;
	}
	public void setPatientMother(String patientMother) {
		this.patientMother = patientMother;
	}
	public String getPatientLinkman() {
		return patientLinkman;
	}
	public void setPatientLinkman(String patientLinkman) {
		this.patientLinkman = patientLinkman;
	}
	public String getPatientLinkrelation() {
		return patientLinkrelation;
	}
	public void setPatientLinkrelation(String patientLinkrelation) {
		this.patientLinkrelation = patientLinkrelation;
	}
	public String getPatientLinkaddress() {
		return patientLinkaddress;
	}
	public void setPatientLinkaddress(String patientLinkaddress) {
		this.patientLinkaddress = patientLinkaddress;
	}
	public String getPatientLinkdoorno() {
		return patientLinkdoorno;
	}
	public void setPatientLinkdoorno(String patientLinkdoorno) {
		this.patientLinkdoorno = patientLinkdoorno;
	}
	public String getPatientLinkphone() {
		return patientLinkphone;
	}
	public void setPatientLinkphone(String patientLinkphone) {
		this.patientLinkphone = patientLinkphone;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
