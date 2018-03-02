package cn.honry.statistics.doctor.wordLoadDoctorTotal.vo;
/**
 * @see 住院医生工作量统计vo
 * @author conglin
 * 
 *
 */
public class WordLoadVO {
	private String doctorCode;//医生code
	private String doctorName;//医生name
	private String workDate;//工作时间
	private Integer workTotal;//当前工作时间工作总量
	private Double openTotal;//开立数量
	private String deptCode;//科室code
	private Double moneyTotal;//金额
	private String deptName;//科室名称
	private String topName;//获取top名
	
	public String getTopName() {
		return topName;
	}
	public void setTopName(String topName) {
		this.topName = topName;
	}
	public Double getOpenTotal() {
		return openTotal;
	}
	public void setOpenTotal(Double openTotal) {
		this.openTotal = openTotal;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Double getMoneyTotal() {
		return moneyTotal;
	}
	public void setMoneyTotal(Double moneyTotal) {
		this.moneyTotal = moneyTotal;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDoctorCode() {
		return doctorCode;
	}
	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	
	public Integer getWorkTotal() {
		return workTotal;
	}
	public void setWorkTotal(Integer workTotal) {
		this.workTotal = workTotal;
	}
	@Override
	public String toString() {
		return "WordLoadVO [doctorCode=" + doctorCode + ", doctorName="
				+ doctorName + ", workDate=" + workDate + ", workTotal="
				+ workTotal + ", openTotal=" + openTotal + ", deptCode="
				+ deptCode + ", moneyTotal=" + moneyTotal + ", deptName="
				+ deptName + ", topName=" + topName + "]";
	}
	
	
}
