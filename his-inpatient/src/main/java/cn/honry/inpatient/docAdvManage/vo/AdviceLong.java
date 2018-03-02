package cn.honry.inpatient.docAdvManage.vo;

import java.util.List;

/**
 * @see  长期医嘱打印
 * @author conglin
 *
 */
public class AdviceLong {
	private String inpatientNo;
	private String patientName;
	private String reportSex;
	private String reportAge;
	private String medicalrecordId;
	private String deptName;
	private String bedName;
	private String nurseCellCode;
	
	private String dateBgn2;
	private String dateBgn1;
	private String dateBgn;
	private String dateEnd2;
	private String dateEnd1;
	private String dateEnd;
	private String itemName;
	private String docCode;
	private String confirmUsercd;
	private String confirmDate;
	private String dcDocnm;
	private String sortId;
	private String executeDate;
	private String executeUsercd;
	private String combNo;
	private String decmpsState;
	private String mainDrug;
	private List<AdviceLong> adviceLongList;
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public List<AdviceLong> getAdviceLongList() {
		return adviceLongList;
	}
	public void setAdviceLongList(List<AdviceLong> adviceLongList) {
		this.adviceLongList = adviceLongList;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
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
	public String getReportAge() {
		return reportAge;
	}
	public void setReportAge(String reportAge) {
		this.reportAge = reportAge;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public String getDateBgn2() {
		return dateBgn2;
	}
	public void setDateBgn2(String dateBgn2) {
		this.dateBgn2 = dateBgn2;
	}
	public String getDateBgn1() {
		return dateBgn1;
	}
	public void setDateBgn1(String dateBgn1) {
		this.dateBgn1 = dateBgn1;
	}
	public String getDateBgn() {
		return dateBgn;
	}
	public void setDateBgn(String dateBgn) {
		this.dateBgn = dateBgn;
	}
	public String getDateEnd2() {
		return dateEnd2;
	}
	public void setDateEnd2(String dateEnd2) {
		this.dateEnd2 = dateEnd2;
	}
	public String getDateEnd1() {
		return dateEnd1;
	}
	public void setDateEnd1(String dateEnd1) {
		this.dateEnd1 = dateEnd1;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getDocCode() {
		return docCode;
	}
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}
	public String getConfirmUsercd() {
		return confirmUsercd;
	}
	public void setConfirmUsercd(String confirmUsercd) {
		this.confirmUsercd = confirmUsercd;
	}
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getDcDocnm() {
		return dcDocnm;
	}
	public void setDcDocnm(String dcDocnm) {
		this.dcDocnm = dcDocnm;
	}
	public String getSortId() {
		return sortId;
	}
	public void setSortId(String sortId) {
		this.sortId = sortId;
	}
	public String getExecuteDate() {
		return executeDate;
	}
	public void setExecuteDate(String executeDate) {
		this.executeDate = executeDate;
	}
	public String getExecuteUsercd() {
		return executeUsercd;
	}
	public void setExecuteUsercd(String executeUsercd) {
		this.executeUsercd = executeUsercd;
	}
	public String getCombNo() {
		return combNo;
	}
	public void setCombNo(String combNo) {
		this.combNo = combNo;
	}
	public String getDecmpsState() {
		return decmpsState;
	}
	public void setDecmpsState(String decmpsState) {
		this.decmpsState = decmpsState;
	}
	public String getMainDrug() {
		return mainDrug;
	}
	public void setMainDrug(String mainDrug) {
		this.mainDrug = mainDrug;
	}
	@Override
	public String toString() {
		return "AdviceLong [inpatientNo=" + inpatientNo + ", patientName="
				+ patientName + ", reportSex=" + reportSex + ", reportAge="
				+ reportAge + ", medicalrecordId=" + medicalrecordId
				+ ", deptName=" + deptName + ", bedName=" + bedName
				+ ", nurseCellCode=" + nurseCellCode + ", dateBgn2=" + dateBgn2
				+ ", dateBgn1=" + dateBgn1 + ", dateBgn=" + dateBgn
				+ ", dateEnd2=" + dateEnd2 + ", dateEnd1=" + dateEnd1
				+ ", dateEnd=" + dateEnd + ", itemName=" + itemName
				+ ", docCode=" + docCode + ", confirmUsercd=" + confirmUsercd
				+ ", confirmDate=" + confirmDate + ", dcDocnm=" + dcDocnm
				+ ", sortId=" + sortId + ", executeDate=" + executeDate
				+ ", executeUsercd=" + executeUsercd + ", combNo=" + combNo
				+ ", decmpsState=" + decmpsState + ", mainDrug=" + mainDrug
				+ ", adviceLongList=" + adviceLongList + "]";
	}
	
}
