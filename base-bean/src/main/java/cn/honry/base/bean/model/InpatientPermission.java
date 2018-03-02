package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class InpatientPermission extends Entity implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 流水号(随机的一个20位数字)*/
	private String permissionNo;
	/** 住院流水号*/
	private String inpatientNo;
	/** 授权科室代码*/
	private String deptCode;
	/**授权医师代号 */
	private String docCode;
	/** 医嘱权限*/
	private String moPermission;
	/** 备注*/
	private String remark;
	/** 用户代码*/
	private String operCode;
	/** 授权时间*/
	private Date operDate;
	/** 处方结束日*/
	private Date moStdt;
	/** 处方起始日*/
	private Date moEddt;
	//新增字段 2017-06-12
   /**医院编码**/
	private Integer hospitalId;
   /**院区编码吗**/
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
	public String getPermissionNo() {
		return permissionNo;
	}
	public void setPermissionNo(String permissionNo) {
		this.permissionNo = permissionNo;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDocCode() {
		return docCode;
	}
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}
	public String getMoPermission() {
		return moPermission;
	}
	public void setMoPermission(String moPermission) {
		this.moPermission = moPermission;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public Date getMoStdt() {
		return moStdt;
	}
	public void setMoStdt(Date moStdt) {
		this.moStdt = moStdt;
	}
	public Date getMoEddt() {
		return moEddt;
	}
	public void setMoEddt(Date moEddt) {
		this.moEddt = moEddt;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
