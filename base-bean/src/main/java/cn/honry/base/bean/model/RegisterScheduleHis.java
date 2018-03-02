package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * TRegisterScheduleHis entity. @author MyEclipse Persistence Tools
 */

public class RegisterScheduleHis extends Entity {

	private SysDepartment department;
	private Clinic clinic;
	private SysEmployee doctor;
	private Integer scheduleWeek;
	private Date scheduleDate;
	private Integer scheduleMidday;
	private Integer scheduleLimit;
	private Integer schedulePrelimit;
	private Integer schedulePhonelimit;
	private Integer scheduleNetlimit;
	private Date scheduleStarttime;
	private Date scheduleEndtime;
	private String scheduleStopreason;
	private String scheduleRemark;
	private Date scheduleCreatetime;
	
	public SysDepartment getDepartment() {
		return department;
	}
	public void setDepartment(SysDepartment department) {
		this.department = department;
	}
	public Clinic getClinic() {
		return clinic;
	}
	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}
	public SysEmployee getDoctor() {
		return doctor;
	}
	public void setDoctor(SysEmployee doctor) {
		this.doctor = doctor;
	}
	public Integer getScheduleWeek() {
		return scheduleWeek;
	}
	public void setScheduleWeek(Integer scheduleWeek) {
		this.scheduleWeek = scheduleWeek;
	}
	public Date getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	public Integer getScheduleMidday() {
		return scheduleMidday;
	}
	public void setScheduleMidday(Integer scheduleMidday) {
		this.scheduleMidday = scheduleMidday;
	}
	public Integer getScheduleLimit() {
		return scheduleLimit;
	}
	public void setScheduleLimit(Integer scheduleLimit) {
		this.scheduleLimit = scheduleLimit;
	}
	public Integer getSchedulePrelimit() {
		return schedulePrelimit;
	}
	public void setSchedulePrelimit(Integer schedulePrelimit) {
		this.schedulePrelimit = schedulePrelimit;
	}
	public Integer getSchedulePhonelimit() {
		return schedulePhonelimit;
	}
	public void setSchedulePhonelimit(Integer schedulePhonelimit) {
		this.schedulePhonelimit = schedulePhonelimit;
	}
	public Integer getScheduleNetlimit() {
		return scheduleNetlimit;
	}
	public void setScheduleNetlimit(Integer scheduleNetlimit) {
		this.scheduleNetlimit = scheduleNetlimit;
	}
	public Date getScheduleStarttime() {
		return scheduleStarttime;
	}
	public void setScheduleStarttime(Date scheduleStarttime) {
		this.scheduleStarttime = scheduleStarttime;
	}
	public Date getScheduleEndtime() {
		return scheduleEndtime;
	}
	public void setScheduleEndtime(Date scheduleEndtime) {
		this.scheduleEndtime = scheduleEndtime;
	}
	public String getScheduleStopreason() {
		return scheduleStopreason;
	}
	public void setScheduleStopreason(String scheduleStopreason) {
		this.scheduleStopreason = scheduleStopreason;
	}
	public String getScheduleRemark() {
		return scheduleRemark;
	}
	public void setScheduleRemark(String scheduleRemark) {
		this.scheduleRemark = scheduleRemark;
	}
	public Date getScheduleCreatetime() {
		return scheduleCreatetime;
	}
	public void setScheduleCreatetime(Date scheduleCreatetime) {
		this.scheduleCreatetime = scheduleCreatetime;
	}

}