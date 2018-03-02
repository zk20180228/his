package cn.honry.statistics.bi.bistac.treatmentEffect.vo;

import java.util.Date;


public class TreatmentEffectVo {
	private Date sTime;//开始时间
	private Date eTime;//结束时间
	private String out_Date;//出院时间
	
	/**统计科室 */
	private String deptName;
	/**好转人次*/
    private Integer improvedNum;
	/**好转人次百分比 */
	private Double improvedPer;
	/**其他人次*/
    private Integer otherNum;
	/**其他人次百分比 */
	private Double otherPer;
	/**死亡人次*/
    private Integer deathNum;
	/**死亡人次百分比 */
	private Double deathPer;
	/**未治疗人次*/
    private Integer untreatedNum;
	/**未治疗人次百分比 */
	private Double untreatedPer;
	/**无效人次*/
    private Integer invalidNum;
	/**无效人次百分比 */
	private Double invalidPer;
	/**治愈人次*/
    private Integer cureNum;
	/**治愈人次百分比 */
	private Double curePer;
	/**总计人次*/
    private Integer totalNum;
    /**出院人数*/
    private Integer outStateTotal;
    /**出院状态*/
    private Integer outState;

	public Integer getOutStateTotal() {
		return outStateTotal;
	}
	public void setOutStateTotal(Integer outStateTotal) {
		this.outStateTotal = outStateTotal;
	}
	public Integer getOutState() {
		return outState;
	}
	public void setOutState(Integer outState) {
		this.outState = outState;
	}
	public Date getsTime() {
		return sTime;
	}
	public void setsTime(Date sTime) {
		this.sTime = sTime;
	}
	public Date geteTime() {
		return eTime;
	}
	public void seteTime(Date eTime) {
		this.eTime = eTime;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Integer getImprovedNum() {
		return improvedNum;
	}
	public void setImprovedNum(Integer improvedNum) {
		this.improvedNum = improvedNum;
	}
	public Double getImprovedPer() {
		return improvedPer;
	}
	public void setImprovedPer(Double improvedPer) {
		this.improvedPer = improvedPer;
	}
	public Integer getOtherNum() {
		return otherNum;
	}
	public void setOtherNum(Integer otherNum) {
		this.otherNum = otherNum;
	}
	public Double getOtherPer() {
		return otherPer;
	}
	public void setOtherPer(Double otherPer) {
		this.otherPer = otherPer;
	}
	public Integer getDeathNum() {
		return deathNum;
	}
	public void setDeathNum(Integer deathNum) {
		this.deathNum = deathNum;
	}
	public Double getDeathPer() {
		return deathPer;
	}
	public void setDeathPer(Double deathPer) {
		this.deathPer = deathPer;
	}
	public Integer getUntreatedNum() {
		return untreatedNum;
	}
	public void setUntreatedNum(Integer untreatedNum) {
		this.untreatedNum = untreatedNum;
	}
	public Double getUntreatedPer() {
		return untreatedPer;
	}
	public void setUntreatedPer(Double untreatedPer) {
		this.untreatedPer = untreatedPer;
	}
	public Integer getInvalidNum() {
		return invalidNum;
	}
	public void setInvalidNum(Integer invalidNum) {
		this.invalidNum = invalidNum;
	}
	public Double getInvalidPer() {
		return invalidPer;
	}
	public void setInvalidPer(Double invalidPer) {
		this.invalidPer = invalidPer;
	}
	public Integer getCureNum() {
		return cureNum;
	}
	public void setCureNum(Integer cureNum) {
		this.cureNum = cureNum;
	}
	public Double getCurePer() {
		return curePer;
	}
	public void setCurePer(Double curePer) {
		this.curePer = curePer;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	
	public String getOut_Date() {
		return out_Date;
	}
	public void setOut_Date(String out_Date) {
		this.out_Date = out_Date;
	}
}
