package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * TTecCarrier entity. @author MyEclipse Persistence Tools
 */

public class TecCarrier extends Entity implements java.io.Serializable {

	// Fields

	private String deptCode;//科室编码
	private String carrierCode;//预约载体编码
	private String carrierName;//预约载体名称
	private String carrierType;//预约载体类别
	private String spellCode;//拼音码
	private String wbCode;//五笔码
	private String userCode;//自定义码
	private String model;//型号
	private Integer isDisengaged;//是否空闲：0－否、1－是
	private Date disengagedTime;//预计空闲日期
	private Integer dayQuota;//日限额（总限额＝各个限额之和）
	private Integer doctorQuota;//医生直接预约限额
	private Integer selfQuota;//患者自助预约限额（在医院、触摸屏）
	private Integer webQuota;//患者自助预约限额（Web）
	private String building;//所处建筑物
	private String floor;//所处楼层
	private String room;//所处房间
	private Integer sortId;//排列序号
	private Integer isPrestoptime;//是否有预停用时间：0－否、1－是
	private Date preStoptime;//预停用时间
	private Date preStarttime;//与启动时间
	private Integer avgTurnoverTime;//平均周转时间
	private String createOper;//创建人工号
	private Date createDate;//创建时间
	private Integer isValid;//是否有效：1－是、0－否
	private String invalidOper;//使无效操作员
	private Date invalidTime;//使无效时间
	private String validOper;//使有效操作员
	private Date validTime;//使有效时间
	private String deviceType;//设备类型
	private Integer isLarge;//是否大型设备,0不是,1是
	private String carrierMemo;//备注信息
	/**
	 * 设备编码
	 */
	private String deviceCode;
	/**
	 * 设备名称
	 */
	private String deviceName;

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getCarrierCode() {
		return this.carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getCarrierName() {
		return this.carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getCarrierType() {
		return this.carrierType;
	}

	public void setCarrierType(String carrierType) {
		this.carrierType = carrierType;
	}

	public String getSpellCode() {
		return this.spellCode;
	}

	public void setSpellCode(String spellCode) {
		this.spellCode = spellCode;
	}

	public String getWbCode() {
		return this.wbCode;
	}

	public void setWbCode(String wbCode) {
		this.wbCode = wbCode;
	}

	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getIsDisengaged() {
		return this.isDisengaged;
	}

	public void setIsDisengaged(Integer isDisengaged) {
		this.isDisengaged = isDisengaged;
	}

	public Date getDisengagedTime() {
		return this.disengagedTime;
	}

	public void setDisengagedTime(Date disengagedTime) {
		this.disengagedTime = disengagedTime;
	}

	public Integer getDayQuota() {
		return this.dayQuota;
	}

	public void setDayQuota(Integer dayQuota) {
		this.dayQuota = dayQuota;
	}

	public Integer getDoctorQuota() {
		return this.doctorQuota;
	}

	public void setDoctorQuota(Integer doctorQuota) {
		this.doctorQuota = doctorQuota;
	}

	public Integer getSelfQuota() {
		return this.selfQuota;
	}

	public void setSelfQuota(Integer selfQuota) {
		this.selfQuota = selfQuota;
	}

	public Integer getWebQuota() {
		return this.webQuota;
	}

	public void setWebQuota(Integer webQuota) {
		this.webQuota = webQuota;
	}

	public String getBuilding() {
		return this.building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getFloor() {
		return this.floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getRoom() {
		return this.room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public Integer getSortId() {
		return this.sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}

	public Integer getIsPrestoptime() {
		return this.isPrestoptime;
	}

	public void setIsPrestoptime(Integer isPrestoptime) {
		this.isPrestoptime = isPrestoptime;
	}

	public Date getPreStoptime() {
		return this.preStoptime;
	}

	public void setPreStoptime(Date preStoptime) {
		this.preStoptime = preStoptime;
	}

	public Date getPreStarttime() {
		return this.preStarttime;
	}

	public void setPreStarttime(Date preStarttime) {
		this.preStarttime = preStarttime;
	}

	public Integer getAvgTurnoverTime() {
		return this.avgTurnoverTime;
	}

	public void setAvgTurnoverTime(Integer avgTurnoverTime) {
		this.avgTurnoverTime = avgTurnoverTime;
	}

	public String getCreateOper() {
		return this.createOper;
	}

	public void setCreateOper(String createOper) {
		this.createOper = createOper;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getIsValid() {
		return this.isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public String getInvalidOper() {
		return this.invalidOper;
	}

	public void setInvalidOper(String invalidOper) {
		this.invalidOper = invalidOper;
	}

	public Date getInvalidTime() {
		return this.invalidTime;
	}

	public void setInvalidTime(Date invalidTime) {
		this.invalidTime = invalidTime;
	}

	public String getValidOper() {
		return this.validOper;
	}

	public void setValidOper(String validOper) {
		this.validOper = validOper;
	}

	public Date getValidTime() {
		return this.validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

	public String getDeviceType() {
		return this.deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public Integer getIsLarge() {
		return this.isLarge;
	}

	public void setIsLarge(Integer isLarge) {
		this.isLarge = isLarge;
	}

	public String getCarrierMemo() {
		return this.carrierMemo;
	}

	public void setCarrierMemo(String carrierMemo) {
		this.carrierMemo = carrierMemo;
	}


}