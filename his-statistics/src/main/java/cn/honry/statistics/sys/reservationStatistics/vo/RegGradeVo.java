package cn.honry.statistics.sys.reservationStatistics.vo;


public class RegGradeVo {
	/**级别代码*/
	private String gradeCode;
	/**是否适用专家号*/
	private Integer gradeExpertNo;
	/**是否适用专科*/
	private Integer gradePecialListNo;
	
	public String getGradeCode() {
		return gradeCode;
	}
	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}
	public Integer getGradeExpertNo() {
		return gradeExpertNo;
	}
	public void setGradeExpertNo(Integer gradeExpertNo) {
		this.gradeExpertNo = gradeExpertNo;
	}
	public Integer getGradePecialListNo() {
		return gradePecialListNo;
	}
	public void setGradePecialListNo(Integer gradePecialListNo) {
		this.gradePecialListNo = gradePecialListNo;
	}
}
