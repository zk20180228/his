package cn.honry.inpatient.costDerate.vo;

import java.util.Date;

import cn.honry.base.bean.model.BusinessBedward;

public class DerateVo {
	/**姓名*/
	private String patientName;
	/**出生日期*/
	private Date reportBirthday;
	/**入院日期*/
	private Date inDate;
	/**科室代码*/
	private String deptCode;
	/**合同单位代码 (从合同单位编码表里读取)*/
	private String pactCode;
	/**床号  (病床使用记录表主键Id)*/
	private String bedInfoId;
	/**预交金额(未结)*/
	private Double prepayCost;
	/**费用金额(未结)*/
	private Double totCost;
	/**自费金额(未结)*/
	private Double ownCost;
	/**自付金额(未结)*/
	private Double payCost;
	/**公费金额(未结)*/
	private Double pubCost;
	/**余额(未结)*/
	private Double freeCost;
	/**医师代码(住院)*/
	private String houseDocCode;
	/**住院流水号*/
	private String inpatientNo;
	/**科室名称*/
	private String deptName;
	/**护士站名称*/
	private String nurseCellName;
	/**医师代码(名称)*/
	private String houseDocName;
	/**护士站*/
	private String nurseCellCode;
	
	
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public String getHouseDocName() {
		return houseDocName;
	}
	public void setHouseDocName(String houseDocName) {
		this.houseDocName = houseDocName;
	}
	public String getNurseCellName() {
		return nurseCellName;
	}
	public void setNurseCellName(String nurseCellName) {
		this.nurseCellName = nurseCellName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	/**床号  (病床表主键ID)*/
	private String bedId;
	/** 床号  **/
	private String bedName;
	/**病房编号   **/
	private BusinessBedward businessBedward;
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public Date getReportBirthday() {
		return reportBirthday;
	}
	public void setReportBirthday(Date reportBirthday) {
		this.reportBirthday = reportBirthday;
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
	public String getBedInfoId() {
		return bedInfoId;
	}
	public void setBedInfoId(String bedInfoId) {
		this.bedInfoId = bedInfoId;
	}
	public Double getPrepayCost() {
		return prepayCost;
	}
	public void setPrepayCost(Double prepayCost) {
		this.prepayCost = prepayCost;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public Double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
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
	public String getHouseDocCode() {
		return houseDocCode;
	}
	public void setHouseDocCode(String houseDocCode) {
		this.houseDocCode = houseDocCode;
	}
	public String getBedId() {
		return bedId;
	}
	public void setBedId(String bedId) {
		this.bedId = bedId;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public BusinessBedward getBusinessBedward() {
		return businessBedward;
	}
	public void setBusinessBedward(BusinessBedward businessBedward) {
		this.businessBedward = businessBedward;
	}
	
}
