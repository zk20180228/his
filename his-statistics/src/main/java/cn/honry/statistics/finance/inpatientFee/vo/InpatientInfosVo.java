package cn.honry.statistics.finance.inpatientFee.vo;

import java.util.Date;
import java.util.List;

public class InpatientInfosVo {
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
	/**R-住院登记  I-病房接诊 B-出院登记 O-出院结算 P-预约出院,N-无费退院,C-婴儿封账*/
	private String inState;
	/**床号名称*/
	private String bedName;
	/**科室名称*/
	private String deptName;
	/**护理单元名称 (部门表里的护士站)*/
	private String nurseCellName;
	/**性别(从编码表里读取)*/
	private String reportSex;
	/**出生日期*/
	private Date reportBirthday;
	/**合同单位名称*/
	private String pactName;
	/**出院日期*/
	private Date outDate;
	/**费用金额(已结)*/
	private Double balanceCost;
	/**结算类别 01-自费 02-保险 03-公费在职 04-公费退休 05-公费高干 (从编码表里读取)*/
	private String paykindCode;
	/**结算日期(上次)*/
	private Date balanceDate;
	/**预交金额(已结)*/
	private Double balancePrepay;
	/**患者状态（从编码表中取值）*/
	private String patientStatus;
	/**性别名称*/
	private String reportSexName;
	/**年龄名称*/
	private String reportAge;
	/**诊断名称*/
	private String diagName;
	/**子报表**/
	private List<InpatientInfosVo> inpatientInfosVo;
	
	public List<InpatientInfosVo> getInpatientInfosVo() {
		return inpatientInfosVo;
	}
	public void setInpatientInfosVo(List<InpatientInfosVo> inpatientInfosVo) {
		this.inpatientInfosVo = inpatientInfosVo;
	}
	public String getDiagName() {
		return diagName;
	}
	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}
	public String getReportAge() {
		return reportAge;
	}
	public void setReportAge(String reportAge) {
		this.reportAge = reportAge;
	}
	public String getReportSexName() {
		return reportSexName;
	}
	public void setReportSexName(String reportSexName) {
		this.reportSexName = reportSexName;
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
	public String getPactName() {
		return pactName;
	}
	public void setPactName(String pactName) {
		this.pactName = pactName;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public Double getBalanceCost() {
		return balanceCost;
	}
	public void setBalanceCost(Double balanceCost) {
		this.balanceCost = balanceCost;
	}
	public String getPaykindCode() {
		return paykindCode;
	}
	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	}
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public Double getBalancePrepay() {
		return balancePrepay;
	}
	public void setBalancePrepay(Double balancePrepay) {
		this.balancePrepay = balancePrepay;
	}
	public String getPatientStatus() {
		return patientStatus;
	}
	public void setPatientStatus(String patientStatus) {
		this.patientStatus = patientStatus;
	}
	
}
