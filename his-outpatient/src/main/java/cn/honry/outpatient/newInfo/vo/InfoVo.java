package cn.honry.outpatient.newInfo.vo;

import java.util.Date;

public class InfoVo {
	//ID
	private String id;
	//科室
	private String deptId;
	//级别
	private String grade;
	//医生
	private String  empId;
	//午别
	private Integer midday;
	//专科
	private String clinic;
	//挂号额限
	private Integer limit;
	//已挂号人数
	private Integer infoAlready;
	//剩余号数
	private Integer infoSurplus;
	//停诊原因
	private String stoprEason;
	//科室name
	private String deptName;
	//级别name
	private String titleName;
	//医生name
	private String empName;
	//是否加号
	private Integer appFlag;
	//是否停诊
	private Integer isStop;
	//排版时间
	private Date dates;
	//特诊限额
	private Integer speciallimit;
	//特诊已挂
	private Integer special;
	//排版ID
	private String workdeptId;
	//排版开始时间
	private String scheduleStarttime;
	//排版结束时间
	private String scheduleEndtime;
	//排版日期
	private Date scheduleDate;
	public Date getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	public String getScheduleStarttime() {
		return scheduleStarttime;
	}
	public void setScheduleStarttime(String scheduleStarttime) {
		this.scheduleStarttime = scheduleStarttime;
	}
	public String getScheduleEndtime() {
		return scheduleEndtime;
	}
	public void setScheduleEndtime(String scheduleEndtime) {
		this.scheduleEndtime = scheduleEndtime;
	}
	public String getWorkdeptId() {
		return workdeptId;
	}
	public void setWorkdeptId(String workdeptId) {
		this.workdeptId = workdeptId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getIsStop() {
		return isStop;
	}
	public void setIsStop(Integer isStop) {
		this.isStop = isStop;
	}
	public Integer getAppFlag() {
		return appFlag;
	}
	public void setAppFlag(Integer appFlag) {
		this.appFlag = appFlag;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public Integer getMidday() {
		return midday;
	}
	public void setMidday(Integer midday) {
		this.midday = midday;
	}
	
	public String getClinic() {
		return clinic;
	}
	public void setClinic(String clinic) {
		this.clinic = clinic;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getInfoAlready() {
		return infoAlready;
	}
	public void setInfoAlready(Integer infoAlready) {
		this.infoAlready = infoAlready;
	}
	public Integer getInfoSurplus() {
		return infoSurplus;
	}
	public void setInfoSurplus(Integer infoSurplus) {
		this.infoSurplus = infoSurplus;
	}
	public String getStoprEason() {
		return stoprEason;
	}
	public void setStoprEason(String stoprEason) {
		this.stoprEason = stoprEason;
	}
	public Date getDates() {
		return dates;
	}
	public void setDates(Date dates) {
		this.dates = dates;
	}
	public Integer getSpeciallimit() {
		return speciallimit;
	}
	public void setSpeciallimit(Integer speciallimit) {
		this.speciallimit = speciallimit;
	}
	public Integer getSpecial() {
		return special;
	}
	public void setSpecial(Integer special) {
		this.special = special;
	}
	
	
}
