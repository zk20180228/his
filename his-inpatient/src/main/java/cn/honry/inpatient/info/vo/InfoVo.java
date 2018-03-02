package cn.honry.inpatient.info.vo;

import java.util.Date;

public class InfoVo {
	/**姓名**/
	private String patientName;
	/**性别**/
	private String reportSex;
	/**出生日期**/
	private Date reportBirthday;
	/**年龄**/
	private Integer reportAge;
	/**科室**/
	private String reportDept;
	/**支付类型**/
	private String payType;
	/** 新添加字段  住院金额  **/
	private Double inpatientBalance;
	/**证件类型**/
	private String certificatesType;
	/**证件号码**/
	private String certificatesNo;
	
	/**诊断**/
	private String reportDiagnose;
	/**入院情况**/
	private Integer reportSituation;
	/**病室**/
	private String reportBedward;
	/**开证医生**/
	private String reportIssuingdoc;
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
	public String getReportDept() {
		return reportDept;
	}
	public void setReportDept(String reportDept) {
		this.reportDept = reportDept;
	}
	public String getReportBedward() {
		return reportBedward;
	}
	public void setReportBedward(String reportBedward) {
		this.reportBedward = reportBedward;
	}
	public String getReportIssuingdoc() {
		return reportIssuingdoc;
	}
	public void setReportIssuingdoc(String reportIssuingdoc) {
		this.reportIssuingdoc = reportIssuingdoc;
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
	
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getCertificatesType() {
		return certificatesType;
	}
	public void setCertificatesType(String certificatesType) {
		this.certificatesType = certificatesType;
	}
	public Double getInpatientBalance() {
		return inpatientBalance;
	}
	public void setInpatientBalance(Double inpatientBalance) {
		this.inpatientBalance = inpatientBalance;
	}
	
}
