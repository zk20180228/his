package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class BusinessDrugstattime extends Entity  implements java.io.Serializable{
	//代码 PHA_MS月结,PHA_DS日结等
	private String jobCode;
	//名称
	private String jobName;
	//状态N_不统计, D_每日统计, W_每周统计,  M_每月统计，Q_每季度统计,Y_每年统计 ,S_正在统计
	private String jobState;
	//上次执行时间
	private Date lastDtime;
	//下次执行时间
	private Date nextDtime;
	//类型: 0 前台应用程序处理, 1 后台JOB处理
	private String jobType;
	//间隔天数(只有当JOB_STATE为1的时候有用)
	private Integer intervalDays;
	//备注
	private String mark;
	
	public String getJobCode() {
		return jobCode;
	}
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobState() {
		return jobState;
	}
	public void setJobState(String jobState) {
		this.jobState = jobState;
	}
	public Date getLastDtime() {
		return lastDtime;
	}
	public void setLastDtime(Date lastDtime) {
		this.lastDtime = lastDtime;
	}
	public Date getNextDtime() {
		return nextDtime;
	}
	public void setNextDtime(Date nextDtime) {
		this.nextDtime = nextDtime;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public Integer getIntervalDays() {
		return intervalDays;
	}
	public void setIntervalDays(Integer intervalDays) {
		this.intervalDays = intervalDays;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	
}
