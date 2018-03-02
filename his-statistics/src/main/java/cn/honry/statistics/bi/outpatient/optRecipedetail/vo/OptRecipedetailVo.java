package cn.honry.statistics.bi.outpatient.optRecipedetail.vo;

public class OptRecipedetailVo {
	/** 科室维度 **/
	private String deptDimensionality;
	/** 医生维度 **/
	private String doctorDimensionality;
	/**时间维度 **/
	private String timeChose;
	/** 处方量**/
	private Double qty;
	/** 处方金额 **/
	private Double sprice;
	/** 同比 **/
	private Double an;
	/** 环比 **/
	private Double mom;
	
	private String code;
	public OptRecipedetailVo() {
	}
	public OptRecipedetailVo(String deptDimensionality,
			String doctorDimensionality, String timeChose) {
		super();
		this.deptDimensionality = deptDimensionality;
		this.doctorDimensionality = doctorDimensionality;
		this.timeChose = timeChose;
	}
	public OptRecipedetailVo(String deptDimensionality,
			 String timeChose) {
		super();
		this.deptDimensionality = deptDimensionality;
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

	public String getTimeChose() {
		return timeChose;
	}

	public void setTimeChose(String timeChose) {
		this.timeChose = timeChose;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public Double getSprice() {
		return sprice;
	}

	public void setSprice(Double sprice) {
		this.sprice = sprice;
	}

	public Double getAn() {
		return an;
	}

	public void setAn(Double an) {
		this.an = an;
	}

	public Double getMom() {
		return mom;
	}

	public void setMom(Double mom) {
		this.mom = mom;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "OptRecipedetailVo [deptDimensionality=" + deptDimensionality
				+ ", doctorDimensionality=" + doctorDimensionality
				+ ", timeChose=" + timeChose + ", qty=" + qty + ", sprice="
				+ sprice + ", an=" + an + ", mom=" + mom + ", code=" + code
				+ "]";
	}
	
}
