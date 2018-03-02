package cn.honry.inpatient.exitNoFee.vo;

import java.util.Date;

public class InpatientInfoVo {
	/**就诊卡号**/
	private String idcardNo;
	/**唯一编号(主键)**/
	private String id;
	/**住院流水号(6位日期+6位流水号)*/
	private String inpatientNo;
	/**病历号*/
	private String medicalrecordId;
	/**姓名*/
	private String patientName;
	/**入院日期*/
	private Date inDate;
	/**合同单位代码 (从合同单位编码表里读取)*/
	private String pactCode;
	/**科室代码*/
	private String deptCode;
	/**床号  (从病床维护表里读取)*/
	private String bedId;
	/**护理单元代码 (部门表里的护士站)*/
	private String nurseCellCode;
	/**预交金额(未结)*/
	private Double prepayCost;
	/**自付金额(未结)*/
	private Double payCost;
	/**自费金额(未结)*/
	private Double ownCost;
	/**费用金额(未结)*/
	private Double totCost;
	/**公费金额(未结)*/
	private Double pubCost;
	/**余额(未结)*/
	private Double freeCost;
	/**R-住院登记  I-病房接诊 B-出院登记 O-出院结算 P-预约出院,N-无费退院*/
	private String inState;
	/**床号名称*/
	private String bedName;
	/**科室名称*/
	private String deptName;
	/**护理单元名称 (部门表里的护士站)*/
	private String nurseCellName;
	/**住院医生*/
	private String houseDocName;
	
	/**性别*/
	private String reportSexName;
	/**年龄*/
	private Integer reportAge;
	/**年龄单位**/
	private String reportAgeUnit;
	/**出院日期*/
	private Date outDate;
	/**出生日期**/
	private Date reportBirthday;
	public Date getReportBirthday() {
		return reportBirthday;
	}
	public void setReportBirthday(Date reportBirthday) {
		this.reportBirthday = reportBirthday;
	}
	public String getIdcardNo() {
		return idcardNo;
	}
	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	public String getReportAgeUnit() {
		return reportAgeUnit;
	}
	public void setReportAgeUnit(String reportAgeUnit) {
		this.reportAgeUnit = reportAgeUnit;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public Integer getReportAge() {
		return reportAge;
	}
	public void setReportAge(Integer reportAge) {
		this.reportAge = reportAge;
	}
	public String getReportSexName() {
		return reportSexName;
	}
	public void setReportSexName(String reportSexName) {
		this.reportSexName = reportSexName;
	}
	public String getHouseDocName() {
		return houseDocName;
	}
	public void setHouseDocName(String houseDocName) {
		this.houseDocName = houseDocName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getBedId() {
		return bedId;
	}
	public void setBedId(String bedId) {
		this.bedId = bedId;
	}
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	public Double getPrepayCost() {
		return prepayCost;
	}
	public void setPrepayCost(Double prepayCost) {
		this.prepayCost = prepayCost;
	}
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}
	public Double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public Double getPubCost() {
		return pubCost;
	}
	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}
	public Double getFreeCost() {
		return freeCost;
	}
	public void setFreeCost(Double freeCost) {
		this.freeCost = freeCost;
	}
	public String getInState() {
		return inState;
	}
	public void setInState(String inState) {
		this.inState = inState;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getNurseCellName() {
		return nurseCellName;
	}
	public void setNurseCellName(String nurseCellName) {
		this.nurseCellName = nurseCellName;
	}
	
}
