package cn.honry.statistics.deptstat.illMedicalRecoder.vo;

import java.util.List;


//危重病历人数比例统计分析VO
public class IllMedicalRecoderVo {
	
	private String deptCode;//部门Code
	private String deptName;//部门名称
	private String num;//总数量
	private String cured;//治愈情况
	private String better;//好转
	private String noCured;//未治愈
	private String death;//死亡
	private String other;//其他
	private String cureRate;//治愈率
	private String deathRate;//死亡率
	private String averInhost;//平均住院日
	private String averFeeCost;//平均住院费用
	private String compliInterface;//并发症感染
	private String allIll;//全院患者
	private String thanFloor;//占全院比
	private List<IllMedicalRecoderVo> list;//报表打印
	
	public List<IllMedicalRecoderVo> getList() {
		return list;
	}
	public void setList(List<IllMedicalRecoderVo> list) {
		this.list = list;
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
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getCured() {
		return cured;
	}
	public void setCured(String cured) {
		this.cured = cured;
	}
	public String getBetter() {
		return better;
	}
	public void setBetter(String better) {
		this.better = better;
	}

	public String getNoCured() {
		return noCured;
	}
	public void setNoCured(String noCured) {
		this.noCured = noCured;
	}
	public String getDeath() {
		return death;
	}
	public void setDeath(String death) {
		this.death = death;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getCureRate() {
		return cureRate;
	}
	public void setCureRate(String cureRate) {
		this.cureRate = cureRate;
	}
	public String getDeathRate() {
		return deathRate;
	}
	public void setDeathRate(String deathRate) {
		this.deathRate = deathRate;
	}
	public String getAverInhost() {
		return averInhost;
	}
	public void setAverInhost(String averInhost) {
		this.averInhost = averInhost;
	}
	public String getAverFeeCost() {
		return averFeeCost;
	}
	public void setAverFeeCost(String averFeeCost) {
		this.averFeeCost = averFeeCost;
	}
	public String getCompliInterface() {
		return compliInterface;
	}
	public void setCompliInterface(String compliInterface) {
		this.compliInterface = compliInterface;
	}
	public String getAllIll() {
		return allIll;
	}
	public void setAllIll(String allIll) {
		this.allIll = allIll;
	}
	public String getThanFloor() {
		return thanFloor;
	}
	public void setThanFloor(String thanFloor) {
		this.thanFloor = thanFloor;
	}
	
}
