package cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.vo;

public class OutpatientIndicatorsVO {
	/*
	 * 科室号
	 */
	private String deptCode;
	/*
	 * 科室名字
	 */
	private String deptName;
	/*
	 * 日平均门诊人次  -- 表示：总门诊诊疗人次（挂号数）
	 * 人均门诊费用 -- 表示：总门诊收入
	 * 门诊人均诊察人次 -- 表示：就诊数
	 * 每百人   门急诊的入院人数 -- 表示：入院人数
	 */
	private Double numerator;
	/*
	 * 日平均门诊人次  -- 表示：门诊工作时数（排班数）
	 * 人均门诊费用 -- 表示：总挂号人数
	 * 门诊人均诊察人次 -- 表示：挂号数
	 * 每  百人  门急诊的入院人数 -- 表示：门诊人次+急诊人次
	 */
	private Double denominator;
	
	
	public Double getNumerator() {
		return numerator;
	}
	public void setNumerator(Double numerator) {
		this.numerator = numerator;
	}
	public Double getDenominator() {
		return denominator;
	}
	public void setDenominator(Double denominator) {
		this.denominator = denominator;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}
