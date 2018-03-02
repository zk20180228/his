package cn.honry.statistics.bi.inpatient.clinicalPathway.vo;

public class analysisClinicalVo {
	/**科室code**/
	private String deptCode;
	/**总人数**/
	private Integer totalNum;
	/**入径数**/
	private Double inNum;
	/**入径率**/
	private String inRare;
	/**出径数**/
	private Double outNum;
	/**出径率**/
	private String outRare;
	/**完成数**/
	private Double overNum;
	/**完成率**/
	private String overRare;
	/**变异数**/
	private Double vaiviationNum;
	/**变异率**/
	private String vaiviationRare;
	/**好转数**/
	private Double betterNum;
	/**好转率**/
	private String betterRare;
	/**治愈数**/
	private Double cureNum;
	/**治愈率**/
	private String cureRare;
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Double getInNum() {
		return inNum;
	}
	public void setInNum(Double inNum) {
		this.inNum = inNum;
	}
	public String getInRare() {
		return inRare;
	}
	public void setInRare(String inRare) {
		this.inRare = inRare;
	}
	public Double getOutNum() {
		return outNum;
	}
	public void setOutNum(Double outNum) {
		this.outNum = outNum;
	}
	public String getOutRare() {
		return outRare;
	}
	public void setOutRare(String outRare) {
		this.outRare = outRare;
	}
	public Double getOverNum() {
		return overNum;
	}
	public void setOverNum(Double overNum) {
		this.overNum = overNum;
	}
	public String getOverRare() {
		return overRare;
	}
	public void setOverRare(String overRare) {
		this.overRare = overRare;
	}
	public Double getVaiviationNum() {
		return vaiviationNum;
	}
	public void setVaiviationNum(Double vaiviationNum) {
		this.vaiviationNum = vaiviationNum;
	}
	public String getVaiviationRare() {
		return vaiviationRare;
	}
	public void setVaiviationRare(String vaiviationRare) {
		this.vaiviationRare = vaiviationRare;
	}
	public Double getBetterNum() {
		return betterNum;
	}
	public void setBetterNum(Double betterNum) {
		this.betterNum = betterNum;
	}
	public String getBetterRare() {
		return betterRare;
	}
	public void setBetterRare(String betterRare) {
		this.betterRare = betterRare;
	}
	public Double getCureNum() {
		return cureNum;
	}
	public void setCureNum(Double cureNum) {
		this.cureNum = cureNum;
	}
	public String getCureRare() {
		return cureRare;
	}
	public void setCureRare(String cureRare) {
		this.cureRare = cureRare;
	}
	
}
