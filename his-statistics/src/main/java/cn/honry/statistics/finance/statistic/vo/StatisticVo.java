package cn.honry.statistics.finance.statistic.vo;

/**  
 *  收入统计汇总VO
 * @Author:luyanshou
 * @version 1.0
 */
public class StatisticVo {

	private String inhosDeptcode;//在院科室代码
	private String deptName;//科室名称
	private Double totCost ;// 费用金额
	private String reportCode;//报表代码
	private String reportName;//报表名称
	private String minfeeCode;//最小费用代码（编码表中取）
	private String minfeeName;//最小费用名称
	private String feeStatCode;//统计费用代码（编码表中取）
	private String feeStatName;//统计费用名称
	
	public String getInhosDeptcode() {
		return inhosDeptcode;
	}
	public void setInhosDeptcode(String inhosDeptcode) {
		this.inhosDeptcode = inhosDeptcode;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public String getMinfeeCode() {
		return minfeeCode;
	}
	public void setMinfeeCode(String minfeeCode) {
		this.minfeeCode = minfeeCode;
	}
	public String getMinfeeName() {
		return minfeeName;
	}
	public void setMinfeeName(String minfeeName) {
		this.minfeeName = minfeeName;
	}
	public String getFeeStatCode() {
		return feeStatCode;
	}
	public void setFeeStatCode(String feeStatCode) {
		this.feeStatCode = feeStatCode;
	}
	public String getFeeStatName() {
		return feeStatName;
	}
	public void setFeeStatName(String feeStatName) {
		this.feeStatName = feeStatName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getReportCode() {
		return reportCode;
	}
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	
}
