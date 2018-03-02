package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class RegisterFee extends Entity{

	
	/**医院编号**/
	private Hospital hospitalId;
	/**单位编号**/
	private String unitId;
	/**挂号级别**/
	private String registerGrade;
	/**挂号费**/
	private Double registerFee = 0.0;
	/**检查费**/
	private Double checkFee = 0.0;
	/**治疗费**/
	private Double treatmentFee = 0.0;
	/**其他费用**/
	private Double otherFee = 0.0;
	/**说明**/
	private String description;
	/**排序**/
	private Integer order; 
	//总费用
	private Double sumCost;
	
	public Double getSumCost() {
		return sumCost;
	}
	public void setSumCost(Double sumCost) {
		this.sumCost = sumCost;
	}
	//BusinessContractunit表的关联内容
	private String Uid;
	
	public String getUid() {
		return Uid;
	}
	public void setUid(String uid) {
		Uid = uid;
	}
	
	public Hospital getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Hospital hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getRegisterGrade() {
		return registerGrade;
	}
	public void setRegisterGrade(String registerGrade) {
		this.registerGrade = registerGrade;
	}
	public Double getRegisterFee() {
		return registerFee;
	}
	public void setRegisterFee(Double registerFee) {
		this.registerFee = registerFee;
	}
	public Double getCheckFee() {
		return checkFee;
	}
	public void setCheckFee(Double checkFee) {
		this.checkFee = checkFee;
	}
	public Double getTreatmentFee() {
		return treatmentFee;
	}
	public void setTreatmentFee(Double treatmentFee) {
		this.treatmentFee = treatmentFee;
	}
	public Double getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	
}
