package cn.honry.statistics.bi.outpatient.outpatientWorkload.vo;

public class OutpatientWorkloadVo {
	/** 科室维度 **/
	private String deptDimensionality;
	/** 医生维度 **/
	private String doctorDimensionality;
	/** 医生职级维度 **/
	private String doctorlevelDimensionality;
	/**时间维度 **/
	private String timeChose;
	/** 门诊总量 **/
	private Integer outpatientGross;
	/** 门诊量 **/
	private Integer outpatientNum;
	/** 急诊量 **/
	private Integer emergencyNum;
	/** 日均门诊量 **/
	private Integer outpatientAvernum;
	
	private String code;
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	

	public String getTimeChose() {
		return timeChose;
	}
	public void setTimeChose(String timeChose) {
		this.timeChose = timeChose;
	}
	public String getDeptDimensionality() {
		return deptDimensionality;
	}
	public void setDeptDimensionality(String deptDimensionality) {
		this.deptDimensionality = deptDimensionality;
	}
	public String getDoctorDimensionality() {
		return doctorDimensionality;
	}
	public void setDoctorDimensionality(String doctorDimensionality) {
		this.doctorDimensionality = doctorDimensionality;
	}
	public String getDoctorlevelDimensionality() {
		return doctorlevelDimensionality;
	}
	public void setDoctorlevelDimensionality(String doctorlevelDimensionality) {
		this.doctorlevelDimensionality = doctorlevelDimensionality;
	}
	public Integer getOutpatientGross() {
		return outpatientGross;
	}
	public void setOutpatientGross(Integer outpatientGross) {
		this.outpatientGross = outpatientGross;
	}
	public Integer getOutpatientNum() {
		return outpatientNum;
	}
	public void setOutpatientNum(Integer outpatientNum) {
		this.outpatientNum = outpatientNum;
	}
	public Integer getEmergencyNum() {
		return emergencyNum;
	}
	public void setEmergencyNum(Integer emergencyNum) {
		this.emergencyNum = emergencyNum;
	}
	public Integer getOutpatientAvernum() {
		return outpatientAvernum;
	}
	public void setOutpatientAvernum(Integer outpatientAvernum) {
		this.outpatientAvernum = outpatientAvernum;
	}
	
	
	
}
