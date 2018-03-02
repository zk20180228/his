package cn.honry.statistics.deptstat.patientDiseaseType.vo;

public class PatientDiseaseType {
	private String deptName;//部门
	private Double total;//患者总人数
	private Double cure;//治愈
	private Double curePer;//治愈%
	private Double better;//好转
	private Double betterPer;//好转%
	private Double healed;//未愈
	private Double healedPer;//未愈%
	private Double death;//死亡
	private Double deathPer;//死亡%
	private Double normal;//正常产
	private Double normalPer;//正常产%
	private Double planning;//计生
	private Double planningPer;//计生%
	private Double other;//其他
	private Double otherPer;//其他%
	/*icd*/
	private String icdName;//icd疾病
	private Integer icdNum;//例数（人）
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getCure() {
		return cure;
	}
	public void setCure(Double cure) {
		this.cure = cure;
	}
	public Double getCurePer() {
		return curePer;
	}
	public void setCurePer(Double curePer) {
		this.curePer = curePer;
	}
	public Double getBetter() {
		return better;
	}
	public void setBetter(Double better) {
		this.better = better;
	}
	public Double getBetterPer() {
		return betterPer;
	}
	public void setBetterPer(Double betterPer) {
		this.betterPer = betterPer;
	}
	public Double getHealed() {
		return healed;
	}
	public void setHealed(Double healed) {
		this.healed = healed;
	}
	public Double getHealedPer() {
		return healedPer;
	}
	public void setHealedPer(Double healedPer) {
		this.healedPer = healedPer;
	}
	public Double getDeath() {
		return death;
	}
	public void setDeath(Double death) {
		this.death = death;
	}
	public Double getDeathPer() {
		return deathPer;
	}
	public void setDeathPer(Double deathPer) {
		this.deathPer = deathPer;
	}
	public Double getNormal() {
		return normal;
	}
	public void setNormal(Double normal) {
		this.normal = normal;
	}
	public Double getNormalPer() {
		return normalPer;
	}
	public void setNormalPer(Double normalPer) {
		this.normalPer = normalPer;
	}
	public Double getPlanning() {
		return planning;
	}
	public void setPlanning(Double planning) {
		this.planning = planning;
	}
	public Double getPlanningPer() {
		return planningPer;
	}
	public void setPlanningPer(Double planningPer) {
		this.planningPer = planningPer;
	}
	public Double getOther() {
		return other;
	}
	public void setOther(Double other) {
		this.other = other;
	}
	public Double getOtherPer() {
		return otherPer;
	}
	public void setOtherPer(Double otherPer) {
		this.otherPer = otherPer;
	}
	public String getIcdName() {
		return icdName;
	}
	public void setIcdName(String icdName) {
		this.icdName = icdName;
	}
	public Integer getIcdNum() {
		return icdNum;
	}
	public void setIcdNum(Integer icdNum) {
		this.icdNum = icdNum;
	}
	
}
