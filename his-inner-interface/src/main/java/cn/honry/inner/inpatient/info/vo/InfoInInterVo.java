package cn.honry.inner.inpatient.info.vo;

import java.util.Date;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;

public class InfoInInterVo {
	/**姓名**/
	private String patientName;
	/**性别**/
	private String reportSex;
	/**出生日期**/
	private Date reportBirthday;
	/**年龄**/
	private Integer reportAge;
	/**科室**/
	private SysDepartment reportDept;
	/**支付类型**/
	private String payType;
	/** 新添加字段  住院金额  **/
	private Double inpatientBalance;
	/**证件号码**/
	private String certificatesNo;
	/**诊断**/
	private String reportDiagnose;
	/**入院情况**/
	private Integer reportSituation;
	/**病室**/
	private SysDepartment reportBedward;
	/**开证医生**/
	private SysEmployee reportIssuingdoc;
	/**住院主表Id*/
	private String id;
	/**病历号*/
	private String medicalrecordId;
	/**入院日期*/
	private Date inDate;
	/**科室代码*/
	private String deptCode;
	/**合同单位代码 (从合同单位编码表里读取)*/
	private String pactCode;
	/**床号  (从病床维护表里读取)*/
	private String bedId;
	/**余额(未结)*/
	private Double freeCost;
	/**是否关帐*/
	private Integer stopAcount;
	/**R-住院登记  I-病房接诊 B-出院登记 O-出院结算 P-预约出院,N-无费退院*/
	private String inState;
	/**开据医师*/
	private String emplCode;
	/**警戒线*/
	private Double moneyAlert;
	/**结算方式**/
	private String paykindcode;
	/** 合同单位  */
	private String conid;
	//床号
	private String bedName;
	private String inpatientNo;
	/** 门诊号 **/
	private String idno;
	/** 科室Name **/
	private String deptName;
	private String idcardNo;
	/**婴儿标记**/
	private Integer babyFlag;
	
	
	public Integer getBabyFlag() {
		return babyFlag;
	}
	public void setBabyFlag(Integer babyFlag) {
		this.babyFlag = babyFlag;
	}
	public String getIdcardNo() {
		return idcardNo;
	}
	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	public String getConid() {
		return conid;
	}
	public void setConid(String conid) {
		this.conid = conid;
	}
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public String getPaykindcode() {
		return paykindcode;
	}
	public void setPaykindcode(String paykindcode) {
		this.paykindcode = paykindcode;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public SysEmployee getReportIssuingdoc() {
		return reportIssuingdoc;
	}
	public void setReportIssuingdoc(SysEmployee reportIssuingdoc) {
		this.reportIssuingdoc = reportIssuingdoc;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	public String getBedId() {
		return bedId;
	}
	public void setBedId(String bedId) {
		this.bedId = bedId;
	}
	public Double getFreeCost() {
		return freeCost;
	}
	public void setFreeCost(Double freeCost) {
		this.freeCost = freeCost;
	}
	public Integer getStopAcount() {
		return stopAcount;
	}
	public void setStopAcount(Integer stopAcount) {
		this.stopAcount = stopAcount;
	}
	public String getInState() {
		return inState;
	}
	public void setInState(String inState) {
		this.inState = inState;
	}
	public String getEmplCode() {
		return emplCode;
	}
	public void setEmplCode(String emplCode) {
		this.emplCode = emplCode;
	}
	public Double getMoneyAlert() {
		return moneyAlert;
	}
	public void setMoneyAlert(Double moneyAlert) {
		this.moneyAlert = moneyAlert;
	}
	public String getReportDiagnose() {
		return reportDiagnose;
	}
	public void setReportDiagnose(String reportDiagnose) {
		this.reportDiagnose = reportDiagnose;
	}
	public Integer getReportSituation() {
		return reportSituation;
	}
	public void setReportSituation(Integer reportSituation) {
		this.reportSituation = reportSituation;
	}

	public SysDepartment getReportBedward() {
		return reportBedward;
	}
	public void setReportBedward(SysDepartment reportBedward) {
		this.reportBedward = reportBedward;
	}
	public String getCertificatesNo() {
		return certificatesNo;
	}
	public void setCertificatesNo(String certificatesNo) {
		this.certificatesNo = certificatesNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
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
	public SysDepartment getReportDept() {
		return reportDept;
	}
	public void setReportDept(SysDepartment reportDept) {
		this.reportDept = reportDept;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Double getInpatientBalance() {
		return inpatientBalance;
	}
	public void setInpatientBalance(Double inpatientBalance) {
		this.inpatientBalance = inpatientBalance;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}
