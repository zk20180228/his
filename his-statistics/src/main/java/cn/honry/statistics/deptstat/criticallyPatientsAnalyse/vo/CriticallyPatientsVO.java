package cn.honry.statistics.deptstat.criticallyPatientsAnalyse.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author yuke
 *
 */
public class CriticallyPatientsVO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**部门编号**/
	private String deptNo;
	/**部门名称**/
	private String deptName;
	
	/**数量**/
	private Integer number;
	
	/**治愈**/
	private Integer cure;
	
	/**好转**/
	private Integer better;
	
	/**未治愈**/
	private Integer nocure;

	/**死亡**/
	private Integer death;
	
	/**其他**/
	private Integer other;
	
	/**治愈率百分比**/
	private String curePercent;
	
	/**死亡率百分比**/
	private String deathPercent;
	
	/**平均住院日**/
	private Double aveInpatient;
	
	/**平均费用**/
	private Double aveCost;
	
	/**并发症感染**/
	private Integer solo;
	
	/**全院患者**/
	private Integer allPatient;
	
	/**占全院比**/
	private String numPercent;
	
	List<CriticallyPatientsVO> list;
	//数量
	private Integer num;
	
	public String getDeptNo() {
		return deptNo;
	}



	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}



	public Integer getNum() {
		return num;
	}



	public void setNum(Integer num) {
		this.num = num;
	}



	public String getDeptName() {
		return deptName;
	}



	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}



	public Integer getNumber() {
		return number;
	}



	public void setNumber(Integer number) {
		this.number = number;
	}



	public Integer getCure() {
		return cure;
	}



	public void setCure(Integer cure) {
		this.cure = cure;
	}



	public Integer getBetter() {
		return better;
	}



	public void setBetter(Integer better) {
		this.better = better;
	}



	public Integer getNocure() {
		return nocure;
	}



	public void setNocure(Integer nocure) {
		this.nocure = nocure;
	}



	public Integer getDeath() {
		return death;
	}



	public void setDeath(Integer death) {
		this.death = death;
	}



	public Integer getOther() {
		return other;
	}



	public void setOther(Integer other) {
		this.other = other;
	}



	public String getCurePercent() {
		return curePercent;
	}



	public void setCurePercent(String curePercent) {
		this.curePercent = curePercent;
	}



	public String getDeathPercent() {
		return deathPercent;
	}



	public void setDeathPercent(String deathPercent) {
		this.deathPercent = deathPercent;
	}



	public Double getAveInpatient() {
		return aveInpatient;
	}



	public void setAveInpatient(Double aveInpatient) {
		this.aveInpatient = aveInpatient;
	}



	public Double getAveCost() {
		return aveCost;
	}



	public void setAveCost(Double aveCost) {
		this.aveCost = aveCost;
	}



	public Integer getSolo() {
		return solo;
	}



	public void setSolo(Integer solo) {
		this.solo = solo;
	}



	public Integer getAllPatient() {
		return allPatient;
	}



	public void setAllPatient(Integer allPatient) {
		this.allPatient = allPatient;
	}



	public String getNumPercent() {
		return numPercent;
	}



	public void setNumPercent(String numPercent) {
		this.numPercent = numPercent;
	}



	public List<CriticallyPatientsVO> getList() {
		return list;
	}



	public void setList(List<CriticallyPatientsVO> list) {
		this.list = list;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	@Override
	public String toString() {
		return "CriticallyPatientsVO [deptName=" + deptName + ", number="
				+ number + ", cure=" + cure + ", better=" + better
				+ ", nocure=" + nocure + ", death=" + death + ", other="
				+ other + ", curePercent=" + curePercent + ", deathPercent="
				+ deathPercent + ", aveInpatient=" + aveInpatient
				+ ", aveCost=" + aveCost + ", solo=" + solo + ", allPatient="
				+ allPatient + ", numPercent=" + numPercent + "]";
	}
	
	
}
