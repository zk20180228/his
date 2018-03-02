package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * AbstractTBusinessHospitalbed entity provides the base persistence definition
 * of the TBusinessHospitalbed entity. @author MyEclipse Persistence Tools
 */

public class BusinessHospitalbed extends Entity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
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
	private String bedOrgan;
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
	
	private String BedwardID;
	/**患者姓名**/
	private String patientName;
	/**护理组**/
	private String nursetendGroup;
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
	/**护理单元名称 (部门表里的护士站)*/
	private String nurseCellName;
	/**医师名称(住院)*/
	private String houseDocName;
	/**医师名称(主治)*/
	private String chargeDocName;
	/**医师名称(主任)*/
	private String chiefDocName;
	/**护士名称(责任)*/
	private String dutyNurseName;
	/** 虚拟字段*/
	private String bedLevelEncode;
	/** 虚拟字段   房间号*/
	private String roomNum;
	
	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public String getNursetendGroup() {
		return nursetendGroup;
	}

	public void setNursetendGroup(String nursetendGroup) {
		this.nursetendGroup = nursetendGroup;
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

	public String getNurseCellName() {
		return nurseCellName;
	}

	public void setNurseCellName(String nurseCellName) {
		this.nurseCellName = nurseCellName;
	}

	public String getHouseDocName() {
		return houseDocName;
	}

	public void setHouseDocName(String houseDocName) {
		this.houseDocName = houseDocName;
	}

	public String getChargeDocName() {
		return chargeDocName;
	}

	public void setChargeDocName(String chargeDocName) {
		this.chargeDocName = chargeDocName;
	}

	public String getChiefDocName() {
		return chiefDocName;
	}

	public void setChiefDocName(String chiefDocName) {
		this.chiefDocName = chiefDocName;
	}

	public String getDutyNurseName() {
		return dutyNurseName;
	}

	public void setDutyNurseName(String dutyNurseName) {
		this.dutyNurseName = dutyNurseName;
	}

	public String getBedLevelEncode() {
		return bedLevelEncode;
	}

	public void setBedLevelEncode(String bedLevelEncode) {
		this.bedLevelEncode = bedLevelEncode;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getBedwardID() {
		return BedwardID;
	}

	public void setBedwardID(String bedwardID) {
		BedwardID = bedwardID;
	}

	public BusinessBedward getBusinessBedward() {
		return businessBedward;
	}

	public void setBusinessBedward(BusinessBedward businessBedward) {
		this.businessBedward = businessBedward;
	}

	public String getBedName() {
		return this.bedName;
	}

	public void setBedName(String bedName) {
		this.bedName = bedName;
	}

	public String getBedLevel() {
		return this.bedLevel;
	}

	public void setBedLevel(String bedLevel) {
		this.bedLevel = bedLevel;
	}

	public String getBedState() {
		return this.bedState;
	}

	public void setBedState(String bedState) {
		this.bedState = bedState;
	}

	public String getBedOrgan() {
		return bedOrgan;
	}

	public void setBedOrgan(String bedOrgan) {
		this.bedOrgan = bedOrgan;
	}

	public String getBedPhone() {
		return this.bedPhone;
	}

	public void setBedPhone(String bedPhone) {
		this.bedPhone = bedPhone;
	}

	public String getBedBelong() {
		return this.bedBelong;
	}

	public void setBedBelong(String bedBelong) {
		this.bedBelong = bedBelong;
	}

	public Double getBedFee() {
		return this.bedFee;
	}

	public void setBedFee(Double bedFee) {
		this.bedFee = bedFee;
	}

	public Integer getBedOrder() {
		return this.bedOrder;
	}

	public void setBedOrder(Integer bedOrder) {
		this.bedOrder = bedOrder;
	}

	public String getPatientId() {
		return this.patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public Hospital getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Hospital hospitalId) {
		this.hospitalId = hospitalId;
	}
	
}