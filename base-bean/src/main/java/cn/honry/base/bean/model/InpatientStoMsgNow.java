package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 摆药通知
 * @author  lyy
 * @createDate： 2016年1月15日 下午3:32:03 
 * @modifier lyy
 * @modifyDate：2016年1月15日 下午3:32:03  
 * @modifyRmk：  
 * @version 1.0
 */
public class InpatientStoMsgNow extends Entity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/** 科室编码 **/
	private String  deptCode;
	/** 科室名称 **/
    private String deptName;
    /** 摆药单分类代码 **/
	private String  billclassCode;
	/** 摆药单分类名称 **/
	private String  billclassName; 
	/** 摆药类型  1集中发送，2临时发送，3全部 **/
	private String  sendType; 
	/** 通知或已摆时间由摆药标记决定  **/
	private Date  sendDtime;
	/** 摆药标记0-通知1-已摆  **/
	private String  sendFlag;
	/** 取药科室 **/
	private String   medDeptCode;
	//新加字段
	/**取药科室名称**/
	private String medDeptName;
	/** 新添加字段 医院编码  **/
	private Integer hospitalId;
	/** 新添加字段 院区编码  **/
	private String areaCode;
	
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
	public String getMedDeptName() {
		return medDeptName;
	}
	public void setMedDeptName(String medDeptName) {
		this.medDeptName = medDeptName;
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
	public String getBillclassCode() {
		return billclassCode;
	}
	public void setBillclassCode(String billclassCode) {
		this.billclassCode = billclassCode;
	}
	public String getBillclassName() {
		return billclassName;
	}
	public void setBillclassName(String billclassName) {
		this.billclassName = billclassName;
	}
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	public Date getSendDtime() {
		return sendDtime;
	}
	public void setSendDtime(Date sendDtime) {
		this.sendDtime = sendDtime;
	}
	public String getSendFlag() {
		return sendFlag;
	}
	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}
	public String getMedDeptCode() {
		return medDeptCode;
	}
	public void setMedDeptCode(String medDeptCode) {
		this.medDeptCode = medDeptCode;
	}
}
