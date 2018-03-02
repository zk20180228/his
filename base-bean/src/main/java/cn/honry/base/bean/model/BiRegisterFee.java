package cn.honry.base.bean.model;

/**
 * BiRegisterFee entity. @author MyEclipse Persistence Tools
 */

public class BiRegisterFee implements java.io.Serializable {

	// Fields

	private String regFeeId;
	private String hospitalCode;
	private String unitCode;
	private String unitName;
	private String regGradeCode;
	private String regGrade;
	private Double regFee;
	private Double checkFee;
	private Double treatmentFee;
	private Double otherFee;
	private String validFlag;

	// Constructors

	/** default constructor */
	public BiRegisterFee() {
	}

	/** full constructor */
	public BiRegisterFee(String hospitalCode, String unitCode, String unitName,
			String regGradeCode, String regGrade, Double regFee,
			Double checkFee, Double treatmentFee, Double otherFee,
			String validFlag) {
		this.hospitalCode = hospitalCode;
		this.unitCode = unitCode;
		this.unitName = unitName;
		this.regGradeCode = regGradeCode;
		this.regGrade = regGrade;
		this.regFee = regFee;
		this.checkFee = checkFee;
		this.treatmentFee = treatmentFee;
		this.otherFee = otherFee;
		this.validFlag = validFlag;
	}

	// Property accessors

	public String getRegFeeId() {
		return this.regFeeId;
	}

	public void setRegFeeId(String regFeeId) {
		this.regFeeId = regFeeId;
	}

	public String getHospitalCode() {
		return this.hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getUnitCode() {
		return this.unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getRegGradeCode() {
		return this.regGradeCode;
	}

	public void setRegGradeCode(String regGradeCode) {
		this.regGradeCode = regGradeCode;
	}

	public String getRegGrade() {
		return this.regGrade;
	}

	public void setRegGrade(String regGrade) {
		this.regGrade = regGrade;
	}

	public Double getRegFee() {
		return this.regFee;
	}

	public void setRegFee(Double regFee) {
		this.regFee = regFee;
	}

	public Double getCheckFee() {
		return this.checkFee;
	}

	public void setCheckFee(Double checkFee) {
		this.checkFee = checkFee;
	}

	public Double getTreatmentFee() {
		return this.treatmentFee;
	}

	public void setTreatmentFee(Double treatmentFee) {
		this.treatmentFee = treatmentFee;
	}

	public Double getOtherFee() {
		return this.otherFee;
	}

	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}

	public String getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}