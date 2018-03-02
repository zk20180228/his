package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiVitalSignsId entity. @author MyEclipse Persistence Tools
 */

public class BiVitalSignsId implements java.io.Serializable {

	// Fields

	private Date measureTime;
	private Date operTime;
	private String operCode;
	private String operName;
	private String inpatinetNo;
	private String patientNo;
	private String name;
	private String bedNo;
	private String wardNo;
	private String wardName;
	private String deptCode;
	private String deptName;
	private Short breath;
	private Short pulse;
	private Double temperature;
	private Short bldh;
	private Short bldl;
	private String measuerFlg;
	private Boolean dropHeat;
	private Double heatDown;
	private String typeCode;
	private String typeName;
	private String rmk;
	private Date createTime;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public BiVitalSignsId() {
	}

	/** full constructor */
	public BiVitalSignsId(Date measureTime, Date operTime, String operCode,
			String operName, String inpatinetNo, String patientNo, String name,
			String bedNo, String wardNo, String wardName, String deptCode,
			String deptName, Short breath, Short pulse, Double temperature,
			Short bldh, Short bldl, String measuerFlg, Boolean dropHeat,
			Double heatDown, String typeCode, String typeName, String rmk,
			Date createTime, Date updateTime) {
		this.measureTime = measureTime;
		this.operTime = operTime;
		this.operCode = operCode;
		this.operName = operName;
		this.inpatinetNo = inpatinetNo;
		this.patientNo = patientNo;
		this.name = name;
		this.bedNo = bedNo;
		this.wardNo = wardNo;
		this.wardName = wardName;
		this.deptCode = deptCode;
		this.deptName = deptName;
		this.breath = breath;
		this.pulse = pulse;
		this.temperature = temperature;
		this.bldh = bldh;
		this.bldl = bldl;
		this.measuerFlg = measuerFlg;
		this.dropHeat = dropHeat;
		this.heatDown = heatDown;
		this.typeCode = typeCode;
		this.typeName = typeName;
		this.rmk = rmk;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	// Property accessors

	public Date getMeasureTime() {
		return this.measureTime;
	}

	public void setMeasureTime(Date measureTime) {
		this.measureTime = measureTime;
	}

	public Date getOperTime() {
		return this.operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public String getOperCode() {
		return this.operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}

	public String getOperName() {
		return this.operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getInpatinetNo() {
		return this.inpatinetNo;
	}

	public void setInpatinetNo(String inpatinetNo) {
		this.inpatinetNo = inpatinetNo;
	}

	public String getPatientNo() {
		return this.patientNo;
	}

	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBedNo() {
		return this.bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getWardNo() {
		return this.wardNo;
	}

	public void setWardNo(String wardNo) {
		this.wardNo = wardNo;
	}

	public String getWardName() {
		return this.wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Short getBreath() {
		return this.breath;
	}

	public void setBreath(Short breath) {
		this.breath = breath;
	}

	public Short getPulse() {
		return this.pulse;
	}

	public void setPulse(Short pulse) {
		this.pulse = pulse;
	}

	public Double getTemperature() {
		return this.temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Short getBldh() {
		return this.bldh;
	}

	public void setBldh(Short bldh) {
		this.bldh = bldh;
	}

	public Short getBldl() {
		return this.bldl;
	}

	public void setBldl(Short bldl) {
		this.bldl = bldl;
	}

	public String getMeasuerFlg() {
		return this.measuerFlg;
	}

	public void setMeasuerFlg(String measuerFlg) {
		this.measuerFlg = measuerFlg;
	}

	public Boolean getDropHeat() {
		return this.dropHeat;
	}

	public void setDropHeat(Boolean dropHeat) {
		this.dropHeat = dropHeat;
	}

	public Double getHeatDown() {
		return this.heatDown;
	}

	public void setHeatDown(Double heatDown) {
		this.heatDown = heatDown;
	}

	public String getTypeCode() {
		return this.typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getRmk() {
		return this.rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BiVitalSignsId))
			return false;
		BiVitalSignsId castOther = (BiVitalSignsId) other;

		return ((this.getMeasureTime() == castOther.getMeasureTime()) || (this
				.getMeasureTime() != null && castOther.getMeasureTime() != null && this
				.getMeasureTime().equals(castOther.getMeasureTime())))
				&& ((this.getOperTime() == castOther.getOperTime()) || (this
						.getOperTime() != null
						&& castOther.getOperTime() != null && this
						.getOperTime().equals(castOther.getOperTime())))
				&& ((this.getOperCode() == castOther.getOperCode()) || (this
						.getOperCode() != null
						&& castOther.getOperCode() != null && this
						.getOperCode().equals(castOther.getOperCode())))
				&& ((this.getOperName() == castOther.getOperName()) || (this
						.getOperName() != null
						&& castOther.getOperName() != null && this
						.getOperName().equals(castOther.getOperName())))
				&& ((this.getInpatinetNo() == castOther.getInpatinetNo()) || (this
						.getInpatinetNo() != null
						&& castOther.getInpatinetNo() != null && this
						.getInpatinetNo().equals(castOther.getInpatinetNo())))
				&& ((this.getPatientNo() == castOther.getPatientNo()) || (this
						.getPatientNo() != null
						&& castOther.getPatientNo() != null && this
						.getPatientNo().equals(castOther.getPatientNo())))
				&& ((this.getName() == castOther.getName()) || (this.getName() != null
						&& castOther.getName() != null && this.getName()
						.equals(castOther.getName())))
				&& ((this.getBedNo() == castOther.getBedNo()) || (this
						.getBedNo() != null && castOther.getBedNo() != null && this
						.getBedNo().equals(castOther.getBedNo())))
				&& ((this.getWardNo() == castOther.getWardNo()) || (this
						.getWardNo() != null && castOther.getWardNo() != null && this
						.getWardNo().equals(castOther.getWardNo())))
				&& ((this.getWardName() == castOther.getWardName()) || (this
						.getWardName() != null
						&& castOther.getWardName() != null && this
						.getWardName().equals(castOther.getWardName())))
				&& ((this.getDeptCode() == castOther.getDeptCode()) || (this
						.getDeptCode() != null
						&& castOther.getDeptCode() != null && this
						.getDeptCode().equals(castOther.getDeptCode())))
				&& ((this.getDeptName() == castOther.getDeptName()) || (this
						.getDeptName() != null
						&& castOther.getDeptName() != null && this
						.getDeptName().equals(castOther.getDeptName())))
				&& ((this.getBreath() == castOther.getBreath()) || (this
						.getBreath() != null && castOther.getBreath() != null && this
						.getBreath().equals(castOther.getBreath())))
				&& ((this.getPulse() == castOther.getPulse()) || (this
						.getPulse() != null && castOther.getPulse() != null && this
						.getPulse().equals(castOther.getPulse())))
				&& ((this.getTemperature() == castOther.getTemperature()) || (this
						.getTemperature() != null
						&& castOther.getTemperature() != null && this
						.getTemperature().equals(castOther.getTemperature())))
				&& ((this.getBldh() == castOther.getBldh()) || (this.getBldh() != null
						&& castOther.getBldh() != null && this.getBldh()
						.equals(castOther.getBldh())))
				&& ((this.getBldl() == castOther.getBldl()) || (this.getBldl() != null
						&& castOther.getBldl() != null && this.getBldl()
						.equals(castOther.getBldl())))
				&& ((this.getMeasuerFlg() == castOther.getMeasuerFlg()) || (this
						.getMeasuerFlg() != null
						&& castOther.getMeasuerFlg() != null && this
						.getMeasuerFlg().equals(castOther.getMeasuerFlg())))
				&& ((this.getDropHeat() == castOther.getDropHeat()) || (this
						.getDropHeat() != null
						&& castOther.getDropHeat() != null && this
						.getDropHeat().equals(castOther.getDropHeat())))
				&& ((this.getHeatDown() == castOther.getHeatDown()) || (this
						.getHeatDown() != null
						&& castOther.getHeatDown() != null && this
						.getHeatDown().equals(castOther.getHeatDown())))
				&& ((this.getTypeCode() == castOther.getTypeCode()) || (this
						.getTypeCode() != null
						&& castOther.getTypeCode() != null && this
						.getTypeCode().equals(castOther.getTypeCode())))
				&& ((this.getTypeName() == castOther.getTypeName()) || (this
						.getTypeName() != null
						&& castOther.getTypeName() != null && this
						.getTypeName().equals(castOther.getTypeName())))
				&& ((this.getRmk() == castOther.getRmk()) || (this.getRmk() != null
						&& castOther.getRmk() != null && this.getRmk().equals(
						castOther.getRmk())))
				&& ((this.getCreateTime() == castOther.getCreateTime()) || (this
						.getCreateTime() != null
						&& castOther.getCreateTime() != null && this
						.getCreateTime().equals(castOther.getCreateTime())))
				&& ((this.getUpdateTime() == castOther.getUpdateTime()) || (this
						.getUpdateTime() != null
						&& castOther.getUpdateTime() != null && this
						.getUpdateTime().equals(castOther.getUpdateTime())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getMeasureTime() == null ? 0 : this.getMeasureTime()
						.hashCode());
		result = 37 * result
				+ (getOperTime() == null ? 0 : this.getOperTime().hashCode());
		result = 37 * result
				+ (getOperCode() == null ? 0 : this.getOperCode().hashCode());
		result = 37 * result
				+ (getOperName() == null ? 0 : this.getOperName().hashCode());
		result = 37
				* result
				+ (getInpatinetNo() == null ? 0 : this.getInpatinetNo()
						.hashCode());
		result = 37 * result
				+ (getPatientNo() == null ? 0 : this.getPatientNo().hashCode());
		result = 37 * result
				+ (getName() == null ? 0 : this.getName().hashCode());
		result = 37 * result
				+ (getBedNo() == null ? 0 : this.getBedNo().hashCode());
		result = 37 * result
				+ (getWardNo() == null ? 0 : this.getWardNo().hashCode());
		result = 37 * result
				+ (getWardName() == null ? 0 : this.getWardName().hashCode());
		result = 37 * result
				+ (getDeptCode() == null ? 0 : this.getDeptCode().hashCode());
		result = 37 * result
				+ (getDeptName() == null ? 0 : this.getDeptName().hashCode());
		result = 37 * result
				+ (getBreath() == null ? 0 : this.getBreath().hashCode());
		result = 37 * result
				+ (getPulse() == null ? 0 : this.getPulse().hashCode());
		result = 37
				* result
				+ (getTemperature() == null ? 0 : this.getTemperature()
						.hashCode());
		result = 37 * result
				+ (getBldh() == null ? 0 : this.getBldh().hashCode());
		result = 37 * result
				+ (getBldl() == null ? 0 : this.getBldl().hashCode());
		result = 37
				* result
				+ (getMeasuerFlg() == null ? 0 : this.getMeasuerFlg()
						.hashCode());
		result = 37 * result
				+ (getDropHeat() == null ? 0 : this.getDropHeat().hashCode());
		result = 37 * result
				+ (getHeatDown() == null ? 0 : this.getHeatDown().hashCode());
		result = 37 * result
				+ (getTypeCode() == null ? 0 : this.getTypeCode().hashCode());
		result = 37 * result
				+ (getTypeName() == null ? 0 : this.getTypeName().hashCode());
		result = 37 * result
				+ (getRmk() == null ? 0 : this.getRmk().hashCode());
		result = 37
				* result
				+ (getCreateTime() == null ? 0 : this.getCreateTime()
						.hashCode());
		result = 37
				* result
				+ (getUpdateTime() == null ? 0 : this.getUpdateTime()
						.hashCode());
		return result;
	}

}