package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;


/**
 * 挂号信息表历史表
 * @author liudelin
 * Date:2015/6/5 15:30
 */
public class RegisterInfoHis extends Entity{

	/**排班编号（外键）**/
	private RegisterSchedule scheduleId;
	/**患者编号**/
	private Patient patientId;
	/**预约号**/
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
	private String order;
	/**病例号**/
	private String midicalrecordId;
	/**挂号费用**/
	private Double fee;
	/**合同单位**/
	private String contractunit;
	/**午别**/
	private Integer midday;
	/**就诊卡号**/
	private String idcardId;
	/**添加字段   挂号时间**/
	private Date registerTime;
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
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
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
	public Date getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	public Patient getPatientId() {
		return patientId;
	}
	public void setPatientId(Patient patientId) {
		this.patientId = patientId;
	}
	public Integer getMidday() {
		return midday;
	}
	public void setMidday(Integer midday) {
		this.midday = midday;
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
	
}
