package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 诊室实体类
 * @author kjh
 */
public class Clinic  extends Entity{

	
	private String clinicName;
	private String clinicPiyin;
	private String clinicWb;
	//将诊室自定义码改为Code编码（必填项）
	private String clinicInputcode;
	/**科室id**/
	private String clinicDeptId;
	private String clinicRemark;

	public String getClinicName() {
		return this.clinicName;
	}

	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}

	public String getClinicPiyin() {
		return this.clinicPiyin;
	}

	public void setClinicPiyin(String clinicPiyin) {
		this.clinicPiyin = clinicPiyin;
	}

	public String getClinicWb() {
		return this.clinicWb;
	}

	public void setClinicWb(String clinicWb) {
		this.clinicWb = clinicWb;
	}

	public String getClinicInputcode() {
		return this.clinicInputcode;
	}

	public void setClinicInputcode(String clinicInputcode) {
		this.clinicInputcode = clinicInputcode;
	}

	public String getClinicDeptId() {
		return this.clinicDeptId;
	}

	public void setClinicDeptId(String clinicDeptId) {
		this.clinicDeptId = clinicDeptId;
	}

	public String getClinicRemark() {
		return this.clinicRemark;
	}

	public void setClinicRemark(String clinicRemark) {
		this.clinicRemark = clinicRemark;
	}

}