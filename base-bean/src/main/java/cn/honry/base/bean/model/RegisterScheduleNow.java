package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * RegisterSchedule entity. @author Azh
 */

public class RegisterScheduleNow extends Entity {

	/**1挂号排班2工作排班**/
	private Integer scheduleClass;
	/**工作科室**/
	private String scheduleWorkdept;
	/** 编号  **/
	private String modelId;
	/** 类型1科室2医生  **/
	private Integer type;
	/** 部门  **/
	private String department;
	/** 诊室  **/
	private String clinic;
	/** 医生  **/
	private String doctor;
	/** 星期  **/
	private Integer week;
	/** 日期  **/
	private Date date;
	/** 午别1上午2下午3晚上  **/
	private Integer midday;
	/** 挂号限额（挂号总限额包括预约+门诊）  **/
	private Integer limit;
	/** 预约限额（预约总限额包括电话+网络）  **/
	private Integer preLimit;
	/** 电话限额  **/
	private Integer phoneLimit;
	/** 网络限额  **/
	private Integer netLimit;
	/** 特诊限额 **/
	private Integer speciallimit;
	/** 开始时间  **/
	private String startTime;
	/** 结束时间  **/
	private String endTime;
	/** 是否加号 1是 0否  **/
	private Integer appFlag=0;
	/** 是否停诊1停2未停  **/
	private Integer isStop=2;
	/** 停诊人  **/
	private User stopDoctor;
	/** 停诊时间  **/
	private Date stopTime;
	/** 停诊原因  **/
	private String stoprEason;
	/** 挂号级别  **/
	private String reggrade;
	/** 备注  **/
	private String remark;
	/** 所属科室名称  **/
	private String scheduleDeptname;
	/** 工作诊室名称  **/
	private String scheduleClinicname;
	/** 医生姓名  **/
	private String scheduleDoctorname;
	/** 午别  **/
	private String scheduleMiddayname;
	
	/** 添加字段**/
	private String search;//用于查询
	/**添加虚拟字段     已挂人数 **/
	private Integer newpeople;
	/**添加虚拟字段     预约人数 **/
	private Integer wilpeople;
	
	/**添加虚拟字段     由于date是数据库关键字这里加一个date显示 **/
	private Date showDate;
	/**医院编码*/
	private Integer hospitalId;
	/**院区编码*/
	private String areaCode;
	public Integer getNewpeople() {
		return newpeople;
	}
	public void setNewpeople(Integer newpeople) {
		this.newpeople = newpeople;
	}
	public Integer getWilpeople() {
		return wilpeople;
	}
	public void setWillpeople(Integer wilpeople) {
		this.wilpeople = wilpeople;
	}
	public Integer getWeek() {
		return week;
	}
	public void setWeek(Integer week) {
		this.week = week;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getMidday() {
		return midday;
	}
	public void setMidday(Integer midday) {
		this.midday = midday;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getPreLimit() {
		return preLimit;
	}
	public void setPreLimit(Integer preLimit) {
		this.preLimit = preLimit;
	}
	public Integer getPhoneLimit() {
		return phoneLimit;
	}
	public void setPhoneLimit(Integer phoneLimit) {
		this.phoneLimit = phoneLimit;
	}
	public Integer getNetLimit() {
		return netLimit;
	}
	public void setNetLimit(Integer netLimit) {
		this.netLimit = netLimit;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStoprEason() {
		return stoprEason;
	}
	public void setStoprEason(String stoprEason) {
		this.stoprEason = stoprEason;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getAppFlag() {
		return appFlag;
	}
	public void setAppFlag(Integer appFlag) {
		this.appFlag = appFlag;
	}
	public Integer getIsStop() {
		return isStop;
	}
	public void setIsStop(Integer isStop) {
		this.isStop = isStop;
	}
	public User getStopDoctor() {
		return stopDoctor;
	}
	public void setStopDoctor(User stopDoctor) {
		this.stopDoctor = stopDoctor;
	}
	public Date getStopTime() {
		return stopTime;
	}
	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}
	public String getReggrade() {
		return reggrade;
	}
	public void setReggrade(String reggrade) {
		this.reggrade = reggrade;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public Integer getSpeciallimit() {
		return speciallimit;
	}
	public void setSpeciallimit(Integer speciallimit) {
		this.speciallimit = speciallimit;
	}
	public Integer getScheduleClass() {
		return scheduleClass;
	}
	public void setScheduleClass(Integer scheduleClass) {
		this.scheduleClass = scheduleClass;
	}
	public String getScheduleWorkdept() {
		return scheduleWorkdept;
	}
	public void setScheduleWorkdept(String scheduleWorkdept) {
		this.scheduleWorkdept = scheduleWorkdept;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getClinic() {
		return clinic;
	}
	public void setClinic(String clinic) {
		this.clinic = clinic;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getScheduleDeptname() {
		return scheduleDeptname;
	}
	public void setScheduleDeptname(String scheduleDeptname) {
		this.scheduleDeptname = scheduleDeptname;
	}
	public String getScheduleClinicname() {
		return scheduleClinicname;
	}
	public void setScheduleClinicname(String scheduleClinicname) {
		this.scheduleClinicname = scheduleClinicname;
	}
	public String getScheduleDoctorname() {
		return scheduleDoctorname;
	}
	public void setScheduleDoctorname(String scheduleDoctorname) {
		this.scheduleDoctorname = scheduleDoctorname;
	}
	public String getScheduleMiddayname() {
		return scheduleMiddayname;
	}
	public void setScheduleMiddayname(String scheduleMiddayname) {
		this.scheduleMiddayname = scheduleMiddayname;
	}
	public Date getShowDate() {
		return showDate;
	}
	public void setShowDate(Date showDate) {
		this.showDate = showDate;
	}
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

	
}