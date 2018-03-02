package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class InpatientNuiLeave extends Entity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	//住院流水号
	private String inpatientNo;
	//请假天数
	private int leaveDays; 
	//给假医生
	private String doctCode;
	//请假操作人
	private String operCode;
	//0请假 1销假 2作废
	private int leaveFlag; 
	//备注
	private String remark;
	//销假（作废）操作人
	private String cancelOpcd;
	//销假时间（作废时间）
	private Date cancelDate;
	//请假时间
	private Date leaveDate;
	
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	
	public int getLeaveDays() {
		return leaveDays;
	}
	public void setLeaveDays(int leaveDays) {
		this.leaveDays = leaveDays;
	}
	public String getDoctCode() {
		return doctCode;
	}
	public void setDoctCode(String doctCode) {
		this.doctCode = doctCode;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public int getLeaveFlag() {
		return leaveFlag;
	}
	public void setLeaveFlag(int leaveFlag) {
		this.leaveFlag = leaveFlag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCancelOpcd() {
		return cancelOpcd;
	}
	public void setCancelOpcd(String cancelOpcd) {
		this.cancelOpcd = cancelOpcd;
	}
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	
	public Date getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}
	
}
