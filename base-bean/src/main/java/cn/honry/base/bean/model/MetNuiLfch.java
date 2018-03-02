package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class MetNuiLfch extends Entity  implements java.io.Serializable{

	//科室代码
	private String deptCode;
	//科室名称
	private String deptName;
	//护理站代码
	private String nurseCellCode;
	//护理站名称
	private String nurseCellName;
	//病床号
	private String bedNo;
	//住院流水号
	private String inpatientNo;
	//住院号
	private String patientNo;
	//患者姓名
	private String name;
	//入院日期
	private Date inDate;
	//测量日期
	private Date lfchDate;
	//呼吸
	private Double lfchBrth;
	//脉搏
	private Double lfchPuls;
	//温度
	private Double lfchHeat;
	//血压（高）
	private Double lfchBldh;
	//血压（低）
	private Double lfchBldl;
	//时间点标记
	private String lfchFlag;
	//强行降温标志，1降温 0否
	private Integer dropHeat;
	//目标温度
	private Double lfchHeatDown;
	//体温类型
	private String lfchStyle;
	//备注
	private String lfchNote;
	//操作员代码
	private String operCode;
	//神智
	private String mind;
	//瞳孔
	private String pupil;
	
	public String getMind() {
		return mind;
	}
	public void setMind(String mind) {
		this.mind = mind;
	}
	public String getPupil() {
		return pupil;
	}
	public void setPupil(String pupil) {
		this.pupil = pupil;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public String getNurseCellName() {
		return nurseCellName;
	}
	public void setNurseCellName(String nurseCellName) {
		this.nurseCellName = nurseCellName;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public Date getLfchDate() {
		return lfchDate;
	}
	public void setLfchDate(Date lfchDate) {
		this.lfchDate = lfchDate;
	}
	public Double getLfchBrth() {
		return lfchBrth;
	}
	public void setLfchBrth(Double lfchBrth) {
		this.lfchBrth = lfchBrth;
	}
	public Double getLfchPuls() {
		return lfchPuls;
	}
	public void setLfchPuls(Double lfchPuls) {
		this.lfchPuls = lfchPuls;
	}
	public Double getLfchHeat() {
		return lfchHeat;
	}
	public void setLfchHeat(Double lfchHeat) {
		this.lfchHeat = lfchHeat;
	}
	public Double getLfchBldh() {
		return lfchBldh;
	}
	public void setLfchBldh(Double lfchBldh) {
		this.lfchBldh = lfchBldh;
	}
	public Double getLfchBldl() {
		return lfchBldl;
	}
	public void setLfchBldl(Double lfchBldl) {
		this.lfchBldl = lfchBldl;
	}
	public String getLfchFlag() {
		return lfchFlag;
	}
	public void setLfchFlag(String lfchFlag) {
		this.lfchFlag = lfchFlag;
	}
	public Integer getDropHeat() {
		return dropHeat;
	}
	public void setDropHeat(Integer dropHeat) {
		this.dropHeat = dropHeat;
	}
	public Double getLfchHeatDown() {
		return lfchHeatDown;
	}
	public void setLfchHeatDown(Double lfchHeatDown) {
		this.lfchHeatDown = lfchHeatDown;
	}
	public String getLfchStyle() {
		return lfchStyle;
	}
	public void setLfchStyle(String lfchStyle) {
		this.lfchStyle = lfchStyle;
	}
	public String getLfchNote() {
		return lfchNote;
	}
	public void setLfchNote(String lfchNote) {
		this.lfchNote = lfchNote;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	  
}
