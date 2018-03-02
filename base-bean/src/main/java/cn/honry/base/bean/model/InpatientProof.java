package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 
 * 
 * @author wujiao
 *住院证明
 */

public class InpatientProof extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**门诊卡号**/
	private String idcardNo;
	/**合同单位**/
	private String contractUnit;
	/**姓名**/
	private String patientName;
	/**证件类型**/
	private String certificatesType;
	/**证件号码**/
	private String certificatesNo;
	/**性别**/
	private String reportSex;
	/**出生日期**/
	private Date reportBirthday;
	/**年龄**/
	private Integer reportAge;
	/**年龄单位**/
	private String reportAgeunit;
	/**科室**/
	private String reportDept;
	/**病室(存病区ID)**/
	private String reportBedward;
	/**诊断**/
	private String reportDiagnose;
	/**地址**/
	private String reportAddress;
	/**入院内容**/
	private String reportIntext;
	/**入院情况**/
	private Integer reportSituation;
	/**入院状态**/
	private Integer reportStatus;
	/**半卧还是休克卧**/
	private Integer reportClinostatism;
	/**是否禁食**/
	private Integer reportDiet;
	/**抬价**/
	private Integer reportShillflag;
	/**沐浴**/
	private Integer reportBathflag;
	/**理发**/
	private Integer reportHaircut;
	/**开证日期**/
	private Date reportIssuingdate;
	/**开证医生**/
	private String reportIssuingdoc;
	/**住院约计天数**/
	private Integer inpatientDaycount;
	/**贵重药品：用、不用**/
	private Integer reportDrugflag;
	/**手术类型：大、中、小**/
	private Integer reportOpstype;
	/**输血数量**/
	private Integer reportBloodqty;
	/**x光照相：一般、特别**/
	private Integer reportXflag;
	/**备注**/
	private String reportRemark;
	/** 新添加字段 病历号  **/
	private String medicalrecordId;
	/** 新添加字段 医院编码  **/
	private Integer hospitalId;
	/** 新添加字段 院区编码  **/
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
	public String getContractUnit() {
		return contractUnit;
	}
	public void setContractUnit(String contractUnit) {
		this.contractUnit = contractUnit;
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
	
	public String getReportDiagnose() {
		return reportDiagnose;
	}
	public void setReportDiagnose(String reportDiagnose) {
		this.reportDiagnose = reportDiagnose;
	}
	public String getReportAddress() {
		return reportAddress;
	}
	public void setReportAddress(String reportAddress) {
		this.reportAddress = reportAddress;
	}
	public String getReportIntext() {
		return reportIntext;
	}
	public void setReportIntext(String reportIntext) {
		this.reportIntext = reportIntext;
	}
	public Integer getReportSituation() {
		return reportSituation;
	}
	public void setReportSituation(Integer reportSituation) {
		this.reportSituation = reportSituation;
	}
	public Integer getReportStatus() {
		return reportStatus;
	}
	public void setReportStatus(Integer reportStatus) {
		this.reportStatus = reportStatus;
	}
	public Integer getReportClinostatism() {
		return reportClinostatism;
	}
	public void setReportClinostatism(Integer reportClinostatism) {
		this.reportClinostatism = reportClinostatism;
	}
	public Integer getReportDiet() {
		return reportDiet;
	}
	public void setReportDiet(Integer reportDiet) {
		this.reportDiet = reportDiet;
	}
	public Integer getReportShillflag() {
		return reportShillflag;
	}
	public void setReportShillflag(Integer reportShillflag) {
		this.reportShillflag = reportShillflag;
	}
	public Integer getReportBathflag() {
		return reportBathflag;
	}
	public void setReportBathflag(Integer reportBathflag) {
		this.reportBathflag = reportBathflag;
	}
	public Integer getReportHaircut() {
		return reportHaircut;
	}
	public void setReportHaircut(Integer reportHaircut) {
		this.reportHaircut = reportHaircut;
	}
	public Date getReportIssuingdate() {
		return reportIssuingdate;
	}
	public void setReportIssuingdate(Date reportIssuingdate) {
		this.reportIssuingdate = reportIssuingdate;
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
	public Integer getInpatientDaycount() {
		return inpatientDaycount;
	}
	public void setInpatientDaycount(Integer inpatientDaycount) {
		this.inpatientDaycount = inpatientDaycount;
	}
	public Integer getReportDrugflag() {
		return reportDrugflag;
	}
	public void setReportDrugflag(Integer reportDrugflag) {
		this.reportDrugflag = reportDrugflag;
	}
	public Integer getReportOpstype() {
		return reportOpstype;
	}
	public void setReportOpstype(Integer reportOpstype) {
		this.reportOpstype = reportOpstype;
	}
	public Integer getReportBloodqty() {
		return reportBloodqty;
	}
	public void setReportBloodqty(Integer reportBloodqty) {
		this.reportBloodqty = reportBloodqty;
	}
	public Integer getReportXflag() {
		return reportXflag;
	}
	public void setReportXflag(Integer reportXflag) {
		this.reportXflag = reportXflag;
	}
	public String getReportRemark() {
		return reportRemark;
	}
	public void setReportRemark(String reportRemark) {
		this.reportRemark = reportRemark;
	}
	
	

}
