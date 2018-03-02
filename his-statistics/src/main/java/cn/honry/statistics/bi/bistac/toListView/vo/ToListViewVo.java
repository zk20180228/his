package cn.honry.statistics.bi.bistac.toListView.vo;

import java.util.Date;

public class ToListViewVo {
	private Integer outpatientD;//门诊人次
	private Integer outpatientLastM;//上月门诊人次
	private Integer outpatientLastY;//上年门诊人次
	private Integer emergencyD;//急诊人次
	private Integer emergencyLastM;//上月急诊人次
	private Integer emergencyLastY;//上年急诊人次
	
	
	private Date eTime;//最小时间
	private Date sTime;//最大时间
	
	
	public Date geteTime() {
		return eTime;
	}
	public void seteTime(Date eTime) {
		this.eTime = eTime;
	}
	public Date getsTime() {
		return sTime;
	}
	public void setsTime(Date sTime) {
		this.sTime = sTime;
	}
	public Integer getOutpatientD() {
		return outpatientD;
	}
	public void setOutpatientD(Integer outpatientD) {
		this.outpatientD = outpatientD;
	}
	public Integer getOutpatientLastM() {
		return outpatientLastM;
	}
	public void setOutpatientLastM(Integer outpatientLastM) {
		this.outpatientLastM = outpatientLastM;
	}
	public Integer getOutpatientLastY() {
		return outpatientLastY;
	}
	public void setOutpatientLastY(Integer outpatientLastY) {
		this.outpatientLastY = outpatientLastY;
	}
	public Integer getEmergencyD() {
		return emergencyD;
	}
	public void setEmergencyD(Integer emergencyD) {
		this.emergencyD = emergencyD;
	}
	public Integer getEmergencyLastM() {
		return emergencyLastM;
	}
	public void setEmergencyLastM(Integer emergencyLastM) {
		this.emergencyLastM = emergencyLastM;
	}
	public Integer getEmergencyLastY() {
		return emergencyLastY;
	}
	public void setEmergencyLastY(Integer emergencyLastY) {
		this.emergencyLastY = emergencyLastY;
	}
}
