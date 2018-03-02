package cn.honry.statistics.bi.outpatient.outpatientFeeType.vo;


public class OutpatientFeeTypeVo {
	//科室
	private String doctDept;
	//年龄区间
	private String age;
	//年龄单位
	private String ageUnit;
	//文化程度
	private String degree;
	//地域
	private String address;
	//支付方式
	private String payway;
	/**时间维度 **/
	private String timeChose;
	//金额                        
	private Double totCost;
	//比例
	private Double scale;
	

	
	

	public String getAgeUnit() {
		return ageUnit;
	}

	public void setAgeUnit(String ageUnit) {
		this.ageUnit = ageUnit;
	}

	public String getDoctDept() {
		return doctDept;
	}

	public void setDoctDept(String doctDept) {
		this.doctDept = doctDept;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}

	public Double getTotCost() {
		return totCost;
	}

	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}



	public Double getScale() {
		return scale;
	}

	public void setScale(Double scale) {
		this.scale = scale;
	}

	public String getTimeChose() {
		return timeChose;
	}

	public void setTimeChose(String timeChose) {
		this.timeChose = timeChose;
	}
	
}
