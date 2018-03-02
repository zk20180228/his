package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class InpatientHangbedinfo extends Entity {
	 //住院号 
	 private String inpatientNo;
	 //发生序号
	 private Integer  happenNo;
	 //床号
	 private String  bedNo;
	 //状态 0 挂床 1 解挂 
	 private Integer  status;
	 //1 挂床 2 包床  
	 private Integer  bedKind;
	//新增字段 2017-06-12
	   /**医院编码**/
	   private Integer hospitalId;
	   /**院区编码吗**/
	   private String areaCode;
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public Integer getHappenNo() {
		return happenNo;
	}
	public void setHappenNo(Integer happenNo) {
		this.happenNo = happenNo;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getBedKind() {
		return bedKind;
	}
	public void setBedKind(Integer bedKind) {
		this.bedKind = bedKind;
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
