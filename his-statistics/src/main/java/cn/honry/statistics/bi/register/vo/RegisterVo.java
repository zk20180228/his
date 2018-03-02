package cn.honry.statistics.bi.register.vo;

public class RegisterVo {
	
	/** 科室**/
	private String deptName;
	/**挂号级别**/
	private String deptType;
	/**时间维度 **/
	private String timeChose;
	/**挂号人次**/
	private Integer registerPerson;
	/**费用**/
	private Double fee;
	
	
	public String getTimeChose() {
		return timeChose;
	}

	public void setTimeChose(String timeChose) {
		this.timeChose = timeChose;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public String getDeptType() {
		return deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}

	public Integer getRegisterPerson() {
		return registerPerson;
	}

	public void setRegisterPerson(Integer registerPerson) {
		this.registerPerson = registerPerson;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}
