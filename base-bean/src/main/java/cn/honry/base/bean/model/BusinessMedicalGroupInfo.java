package cn.honry.base.bean.model;

import java.math.BigDecimal;

import cn.honry.base.bean.business.Entity;


public class BusinessMedicalGroupInfo extends Entity{

	// Fields
	
	/**医疗组编号**/
	private BusinessMedicalGroup businessMedicalgroup;
	/**医生编号**/
	private String doctorId;
	/**医生姓名**/
	private String doctor;
	/**备注**/
	private String remark;
	/**排序**/
	private BigDecimal Order;
	/**判断显示医生是住院部下，还是住院部下的某一科室**/
	private String isOwnDept;
	
	public BusinessMedicalGroup getBusinessMedicalgroup() {
		return businessMedicalgroup;
	}

	public void setBusinessMedicalgroup(BusinessMedicalGroup businessMedicalgroup) {
		this.businessMedicalgroup = businessMedicalgroup;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getOrder() {
		return Order;
	}

	public void setOrder(BigDecimal order) {
		Order = order;
	}

	public void setIsOwnDept(String isOwnDept) {
		this.isOwnDept = isOwnDept;
	}

	public String getIsOwnDept() {
		return isOwnDept;
	}

}