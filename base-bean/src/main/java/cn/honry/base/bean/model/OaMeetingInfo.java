package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class OaMeetingInfo extends Entity {
	/**所属院区**/
    private String areaCode;
    /**会议室名称**/
    private String meetName;
    /**会议室地点**/
    private String meetPlace;
    /**容纳人数**/
    private Integer meetNumber;
    /**会议室状态**/
    private String meetState;
    /**会议室类型**/
    private String meetType;
    /**会议室管理员**/
    private String meetAdmin;
    /**会议室设备情况**/
    private String meetEquipment;
    /**是否有投影**/
    private String meetProjector;
    /**是否有音响**/
    private String meetSound;
    /**是否可申请**/
    private String meetIsapply;
    /**联系方式**/
    private String meetPhone;
    /**会议室描述**/
    private String meetDescribe;
    /**所属医院**/
    private String hospitalCode;
    
	/**  分页  **/
	private String page;
	private String rows;
	
    /**会议室管理员Code**/
    private String meetAdminCode;
    /**会议室Code**/
    private String meetCode;
	
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getMeetName() {
		return meetName;
	}
	public void setMeetName(String meetName) {
		this.meetName = meetName;
	}
	public String getMeetPlace() {
		return meetPlace;
	}
	public void setMeetPlace(String meetPlace) {
		this.meetPlace = meetPlace;
	}
	public Integer getMeetNumber() {
		return meetNumber;
	}
	public void setMeetNumber(Integer meetNumber) {
		this.meetNumber = meetNumber;
	}
	public String getMeetState() {
		return meetState;
	}
	public void setMeetState(String meetState) {
		this.meetState = meetState;
	}
	public String getMeetType() {
		return meetType;
	}
	public void setMeetType(String meetType) {
		this.meetType = meetType;
	}
	public String getMeetAdmin() {
		return meetAdmin;
	}
	public void setMeetAdmin(String meetAdmin) {
		this.meetAdmin = meetAdmin;
	}
	public String getMeetEquipment() {
		return meetEquipment;
	}
	public void setMeetEquipment(String meetEquipment) {
		this.meetEquipment = meetEquipment;
	}
	public String getMeetProjector() {
		return meetProjector;
	}
	public void setMeetProjector(String meetProjector) {
		this.meetProjector = meetProjector;
	}
	public String getMeetSound() {
		return meetSound;
	}
	public void setMeetSound(String meetSound) {
		this.meetSound = meetSound;
	}
	public String getMeetIsapply() {
		return meetIsapply;
	}
	public void setMeetIsapply(String meetIsapply) {
		this.meetIsapply = meetIsapply;
	}
	public String getMeetPhone() {
		return meetPhone;
	}
	public void setMeetPhone(String meetPhone) {
		this.meetPhone = meetPhone;
	}
	public String getMeetDescribe() {
		return meetDescribe;
	}
	public void setMeetDescribe(String meetDescribe) {
		this.meetDescribe = meetDescribe;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getMeetAdminCode() {
		return meetAdminCode;
	}
	public void setMeetAdminCode(String meetAdminCode) {
		this.meetAdminCode = meetAdminCode;
	}
	public String getMeetCode() {
		return meetCode;
	}
	public void setMeetCode(String meetCode) {
		this.meetCode = meetCode;
	}
    
}
