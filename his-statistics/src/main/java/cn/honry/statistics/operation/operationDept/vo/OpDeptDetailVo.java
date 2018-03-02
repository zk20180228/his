package cn.honry.statistics.operation.operationDept.vo;
/***
 * 手术科室明细vo
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
public class OpDeptDetailVo {
	/**
	 * 医生科室
	 */
	private String opDoctorDept;
	/**
	 * 医生名称
	 */
	private String opDoctor;
	/**
	 * 普通台数量
	 */
	private Integer ptsl;
	
	/**
	 * 普通台数金额
	 */
	private Double ptje;
	
	/**
	 * 择期数量
	 */
	private Integer zqsl;
	/**
	 * 择期金额
	 */
	private Double zqje;
	/**
	 * 感染数量
	 */
	private Integer grsl;
	/**
	 * 感染金额
	 */
	private Double grje;
	/**
	 * 急诊台数量
	 */
	private Integer jzsl;
	/**
	 * 急诊台金额
	 */
	private Double jzje;
	/**
	 * 合计数量
	 */
	private Integer hjsl;
	/**
	 * 合计金额
	 */
	private Double hjje;
	/**
	 * 医生连台费
	 */
	private Double ltje;
	
	public String getOpDoctorDept() {
		return opDoctorDept;
	}
	public void setOpDoctorDept(String opDoctorDept) {
		this.opDoctorDept = opDoctorDept;
	}
	public String getOpDoctor() {
		return opDoctor;
	}
	public void setOpDoctor(String opDoctor) {
		this.opDoctor = opDoctor;
	}
	
	public Integer getPtsl() {
		return ptsl;
	}
	public void setPtsl(Integer ptsl) {
		this.ptsl = ptsl;
	}
	public Double getPtje() {
		return ptje;
	}
	public void setPtje(Double ptje) {
		this.ptje = ptje;
	}
	public Integer getJzsl() {
		return jzsl;
	}
	public void setJzsl(Integer jzsl) {
		this.jzsl = jzsl;
	}
	public Double getJzje() {
		return jzje;
	}
	public void setJzje(Double jzje) {
		this.jzje = jzje;
	}
	public Integer getHjsl() {
		return hjsl;
	}
	public void setHjsl(Integer hjsl) {
		this.hjsl = hjsl;
	}
	public Double getHjje() {
		return hjje;
	}
	public void setHjje(Double hjje) {
		this.hjje = hjje;
	}
	public Double getLtje() {
		return ltje;
	}
	public void setLtje(Double ltje) {
		this.ltje = ltje;
	}
	public Integer getZqsl() {
		return zqsl;
	}
	public void setZqsl(Integer zqsl) {
		this.zqsl = zqsl;
	}
	public Double getZqje() {
		return zqje;
	}
	public void setZqje(Double zqje) {
		this.zqje = zqje;
	}
	public Integer getGrsl() {
		return grsl;
	}
	public void setGrsl(Integer grsl) {
		this.grsl = grsl;
	}
	public Double getGrje() {
		return grje;
	}
	public void setGrje(Double grje) {
		this.grje = grje;
	}
	
}
