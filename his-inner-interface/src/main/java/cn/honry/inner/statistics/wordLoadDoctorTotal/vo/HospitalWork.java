package cn.honry.inner.statistics.wordLoadDoctorTotal.vo;

/**
 * 
 * 
 * <p>住院医生工作量统计VO </p>
 * @Author: XCL
 * @CreateDate: 2017年7月13日 上午11:35:59 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月13日 上午11:35:59 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public class HospitalWork {
	
	/**科室**/
	private String deptCode;
	
	/**医生**/
	private String doctor;
	
	/**住院人次**/
	private Integer hosVisitors;
	
	/**出院人次**/
	private Integer outVisitors;
	
	/**并发人次**/
	private Integer concurVisitors;
	
	/**治愈人次**/
	private Integer cureVisitors;
	
	/**未治愈人次**/
	private Integer unCureVisitors;
	
	/**好转人次**/
	private Integer betterVisitors;
	
	/**死亡人次**/
	private Integer deathVisitors;
	
	/**平均住院天数**/
	private Integer averageInhost;
	/**统计时间**/
	private String inDate;
	
	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public Integer getAverageInhost() {
		return averageInhost;
	}

	public void setAverageInhost(Integer averageInhost) {
		this.averageInhost = averageInhost;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public Integer getHosVisitors() {
		return hosVisitors;
	}

	public void setHosVisitors(Integer hosVisitors) {
		this.hosVisitors = hosVisitors;
	}

	public Integer getOutVisitors() {
		return outVisitors;
	}

	public void setOutVisitors(Integer outVisitors) {
		this.outVisitors = outVisitors;
	}

	public Integer getConcurVisitors() {
		return concurVisitors;
	}

	public void setConcurVisitors(Integer concurVisitors) {
		this.concurVisitors = concurVisitors;
	}

	public Integer getCureVisitors() {
		return cureVisitors;
	}

	public void setCureVisitors(Integer cureVisitors) {
		this.cureVisitors = cureVisitors;
	}

	public Integer getUnCureVisitors() {
		return unCureVisitors;
	}

	public void setUnCureVisitors(Integer unCureVisitors) {
		this.unCureVisitors = unCureVisitors;
	}

	public Integer getBetterVisitors() {
		return betterVisitors;
	}

	public void setBetterVisitors(Integer betterVisitors) {
		this.betterVisitors = betterVisitors;
	}

	public Integer getDeathVisitors() {
		return deathVisitors;
	}

	public void setDeathVisitors(Integer deathVisitors) {
		this.deathVisitors = deathVisitors;
	}

	@Override
	public String toString() {
		return "HospitalWork [deptCode=" + deptCode + ", doctor=" + doctor
				+ ", hosVisitors=" + hosVisitors + ", outVisitors="
				+ outVisitors + ", concurVisitors=" + concurVisitors
				+ ", cureVisitors=" + cureVisitors + ", unCureVisitors="
				+ unCureVisitors + ", betterVisitors=" + betterVisitors
				+ ", deathVisitors=" + deathVisitors + ", averageInhost="
				+ averageInhost + ", inDate=" + inDate + "]";
	}
	
	
}
