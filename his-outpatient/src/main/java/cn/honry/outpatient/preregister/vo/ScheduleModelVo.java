package cn.honry.outpatient.preregister.vo;

import java.util.Date;

/**  
 *  
 * @className：EmpScheduleVo
 * @Description：  
 * @Author：wujiao
 * @CreateDate：2015-11-19 下午06:22:16  
 * @Modifier：wujiao
 * @ModifyDate：2015-11-19 下午06:22:16
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class ScheduleModelVo {
	private String id;
	private String deptId;
	private String deptName;
	private String doctId;
	private String grade;
	private String scheduleId;//排班编号
	private Date sdate;
	private Integer midd;
	private Integer prelimit;
	private Integer phonelimit;
	private Integer netlimit;
	private String starttime;
	private String enttime;
	
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEnttime() {
		return enttime;
	}
	public void setEnttime(String enttime) {
		this.enttime = enttime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDoctId() {
		return doctId;
	}
	public void setDoctId(String doctId) {
		this.doctId = doctId;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public Date getSdate() {
		return sdate;
	}
	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}
	public Integer getMidd() {
		return midd;
	}
	public void setMidd(Integer midd) {
		this.midd = midd;
	}
	public Integer getPrelimit() {
		return prelimit;
	}
	public void setPrelimit(Integer prelimit) {
		this.prelimit = prelimit;
	}
	public Integer getPhonelimit() {
		return phonelimit;
	}
	public void setPhonelimit(Integer phonelimit) {
		this.phonelimit = phonelimit;
	}
	public Integer getNetlimit() {
		return netlimit;
	}
	public void setNetlimit(Integer netlimit) {
		this.netlimit = netlimit;
	}
	
	
}
