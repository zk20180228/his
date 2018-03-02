package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;


/**
 * 挂号信息表
 * @author liudelin
 * Date:2015/6/5 15:30
 */
/**
 * @author abc
 *
 */
public class RegisterInfo  extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	/**挂号优先级**/
	private Integer seeOptimize;//REGISTER_SEEOPTIMIZE
	//加号人数(虚拟字段)
	private Integer infoAdd;
	/**数据库无关字段  （收费类别：0 未收费 1门诊收费 2 扣门诊账户 3预收费团体体检 4 药品预审核）**/
	private String payFlag;
	/**数据库无关字段 （患者类别：‘1’ 门诊|‘2’ 住院|‘3’ 急诊|‘4’ 体检  5 集体体检）**/
	private String patienttype;

	private String expxrtName;
	
	private String deptName;
	
	private String age;
	
	private String gradeName;
	
	private String contractunitName;
	
	private String preregisterNo;
	
	private String patientIds;

	/**
	 * 就诊诊室
	 */
	private String clinicName;
	/**
	 * 患者姓名
	 */
	private String patientName;
	/**
	 * 患者就诊卡号/病例号/门诊号
	 */
	private String queryNo;
	/**顺序号**/
	private Integer orde;
	
	public Integer getOrde() {
		return orde;
	}
	public void setOrde(Integer orde) {
		this.orde = orde;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getClinicName() {
		return clinicName;
	}
	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}
	public String getContractunitName() {
		return contractunitName;
	}
	public void setContractunitName(String contractunitName) {
		this.contractunitName = contractunitName;
	}
	public RegisterSchedule getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(RegisterSchedule scheduleId) {
		this.scheduleId = scheduleId;
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
	public Integer getAppflag() {
		return appflag;
	}
	public void setAppflag(Integer appflag) {
		this.appflag = appflag;
	}

	/**部门**/
	/**分页**/
	private String page;
	private String rows;

	public String getExpxrtName() {
		return expxrtName;
	}
	public void setExpxrtName(String expxrtName) {
		this.expxrtName = expxrtName;
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
	public Integer getMidday() {
		return midday;
	}
	public void setMidday(Integer midday) {
		this.midday = midday;
	}
	public Patient getPatientId() {
		return patientId;
	}
	public void setPatientId(Patient patientId) {
		this.patientId = patientId;
	}
	public Integer getInfoAdd() {
		return infoAdd;
	}
	public void setInfoAdd(Integer infoAdd) {
		this.infoAdd = infoAdd;
	}
	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}
	public String getPayFlag() {
		return payFlag;
	}
	public void setPatienttype(String patienttype) {
		this.patienttype = patienttype;
	}
	public String getPatienttype() {
		return patienttype;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
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
	public String getPreregisterNo() {
		return preregisterNo;
	}
	public void setPreregisterNo(String preregisterNo) {
		this.preregisterNo = preregisterNo;
	}
	public String getPatientIds() {
		return patientIds;
	}
	public void setPatientIds(String patientIds) {
		this.patientIds = patientIds;
	}
	public Integer getSeeOptimize() {
		return seeOptimize;
	}
	public void setSeeOptimize(Integer seeOptimize) {
		this.seeOptimize = seeOptimize;
	}
	public String getQueryNo() {
		return queryNo;
	}
	public void setQueryNo(String queryNo) {
		this.queryNo = queryNo;
	}
	
	
	
}
