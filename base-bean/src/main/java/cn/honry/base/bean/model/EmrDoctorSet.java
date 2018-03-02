package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class EmrDoctorSet extends Entity {
	/**科室编号**/
	private String deptCode;
	/**上级医师**/
	private String higherDoc;
	/***主任医师**/
	private String chiefDoc;
	
	
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getHigherDoc() {
		return higherDoc;
	}
	public void setHigherDoc(String higherDoc) {
		this.higherDoc = higherDoc;
	}
	public String getChiefDoc() {
		return chiefDoc;
	}
	public void setChiefDoc(String chiefDoc) {
		this.chiefDoc = chiefDoc;
	}
	
}
