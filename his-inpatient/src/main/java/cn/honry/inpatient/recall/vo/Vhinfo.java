package cn.honry.inpatient.recall.vo;

import java.util.Date;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.Hospital;

public class Vhinfo {
	/**床位使用情况编号**/
	private String bedinfoId;
	/**医疗流水号*/
	private String clinicNo;
	/**入院时间*/
	private Date inDate;
	/**出院时间*/
	private Date outDate;
	/**床号  (从病床维护表里读取)*/
	private String bedId;
	/**护理单元代码 (部门表里的护士站)*/
	private String nurseCellCode;
	/**医师代码(住院)*/
	private String houseDocCode;
	/**医师代码(主治)*/
	private String chargeDocCode;
	/**医师代码(主任)*/
	private String chiefDocCode;
	/**护士代码(责任)*/
	private String dutyNurseCode;
	
	/**  医院编号 **/
	private Hospital hospitalId;
	/**病房编号   **/
	private BusinessBedward businessBedward;
	/** 床号  **/
	private String bedName;
	/** 床位等级  **/
	private String bedLevel;
	/** 床位状态  **/
	private String bedState;
	/**床位编制   **/
	private Integer bedOrgan;
	/** 床位电话  **/
	private String bedPhone;
	/**归属   **/
	private String bedBelong;
	/**费用   **/
	private Double bedFee;
	/** 排序  **/
	private Integer bedOrder;
	/** 当前病人编号 **/
	private String patientId;
	public String getBedinfoId() {
		return bedinfoId;
	}
	public void setBedinfoId(String bedinfoId) {
		this.bedinfoId = bedinfoId;
	}
	public String getClinicNo() {
		return clinicNo;
	}
	public void setClinicNo(String clinicNo) {
		this.clinicNo = clinicNo;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
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
	public String getHouseDocCode() {
		return houseDocCode;
	}
	public void setHouseDocCode(String houseDocCode) {
		this.houseDocCode = houseDocCode;
	}
	public String getChargeDocCode() {
		return chargeDocCode;
	}
	public void setChargeDocCode(String chargeDocCode) {
		this.chargeDocCode = chargeDocCode;
	}
	public String getChiefDocCode() {
		return chiefDocCode;
	}
	public void setChiefDocCode(String chiefDocCode) {
		this.chiefDocCode = chiefDocCode;
	}
	public String getDutyNurseCode() {
		return dutyNurseCode;
	}
	public void setDutyNurseCode(String dutyNurseCode) {
		this.dutyNurseCode = dutyNurseCode;
	}
	public Hospital getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Hospital hospitalId) {
		this.hospitalId = hospitalId;
	}
	public BusinessBedward getBusinessBedward() {
		return businessBedward;
	}
	public void setBusinessBedward(BusinessBedward businessBedward) {
		this.businessBedward = businessBedward;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public String getBedLevel() {
		return bedLevel;
	}
	public void setBedLevel(String bedLevel) {
		this.bedLevel = bedLevel;
	}
	public String getBedState() {
		return bedState;
	}
	public void setBedState(String bedState) {
		this.bedState = bedState;
	}
	public Integer getBedOrgan() {
		return bedOrgan;
	}
	public void setBedOrgan(Integer bedOrgan) {
		this.bedOrgan = bedOrgan;
	}
	public String getBedPhone() {
		return bedPhone;
	}
	public void setBedPhone(String bedPhone) {
		this.bedPhone = bedPhone;
	}
	public String getBedBelong() {
		return bedBelong;
	}
	public void setBedBelong(String bedBelong) {
		this.bedBelong = bedBelong;
	}
	public Double getBedFee() {
		return bedFee;
	}
	public void setBedFee(Double bedFee) {
		this.bedFee = bedFee;
	}
	public Integer getBedOrder() {
		return bedOrder;
	}
	public void setBedOrder(Integer bedOrder) {
		this.bedOrder = bedOrder;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	
	
}
