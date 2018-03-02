package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**  
 * @className：电子病历--护理信息维护（实体）
 * @Description：  EmrsMaintenance
 * @Author：ldl
 * @CreateDate：2016-05-17
 * @ModifyRmk：  
 * @version 1.0
 */
public class EmrsMaintenance extends Entity{

	/**
	 * 护理信息维护表实体
	 */
	private static final long serialVersionUID = 1L;

	
	/**患者病历号**/
	private String nurPatid;
	/**患者住院流水号**/
	private String nurInpatientNo;
	/**患者姓名**/
	private String nurName;
	/**手术后天数**/
	private Integer nurOpDay;
	/**住院天数**/
	private Integer nurInDay;
	/**测量时间**/
	private Date nurMeasurEtime; 
	/**测体温方式**/
	private Integer nurMeasurMode; 
	/**体温**/
	private Double nurTemperature;
	/**脉搏**/
	private Integer nurPulse;
	/**呼吸**/
	private Integer nurBreath;
	/**大便次数**/
	private String nurDefacation;
	/**血压**/
	private String nurBloodpressure;
	/**总入量**/
	private Double nurSumin;
	/**总出量**/
	private Double nurSumout;
	/**引流量**/
	private Double nurDrainage;
	/**身高**/
	private Double nurHeight;
	/**体重**/
	private Double nurWeight;
	/**过敏药物**/
	private String nurAllergicdrug;
	/**心率**/
	private Double nurHeartrate;
	/**物理降温**/
	private Double nurPhysicalcooling;
	/**评分**/
	private Double nurScore;
	/**评分等级**/
	private Integer nurLevel;
	
	
	/**与数据库无关字段**/
    /**条数**/
    private Integer total;
	/**患者数Id**/
	private String treeId;
	/**低压**/
	private Integer nurBloodpressured;
	/**高压**/
	private Integer nurBloodpressureg;
	/**患者姓名**/
	private String name;
	/**当前日期**/
	private Date infoDate;
	/**日期**/
	private String dates;
	/**周期**/
	private String weekState;
	
	public String getNurPatid() {
		return nurPatid;
	}
	public void setNurPatid(String nurPatid) {
		this.nurPatid = nurPatid;
	}
	public Integer getNurOpDay() {
		return nurOpDay;
	}
	public void setNurOpDay(Integer nurOpDay) {
		this.nurOpDay = nurOpDay;
	}
	public Integer getNurInDay() {
		return nurInDay;
	}
	public void setNurInDay(Integer nurInDay) {
		this.nurInDay = nurInDay;
	}
	public Date getNurMeasurEtime() {
		return nurMeasurEtime;
	}
	public void setNurMeasurEtime(Date nurMeasurEtime) {
		this.nurMeasurEtime = nurMeasurEtime;
	}
	public Integer getNurMeasurMode() {
		return nurMeasurMode;
	}
	public void setNurMeasurMode(Integer nurMeasurMode) {
		this.nurMeasurMode = nurMeasurMode;
	}
	public Double getNurTemperature() {
		return nurTemperature;
	}
	public void setNurTemperature(Double nurTemperature) {
		this.nurTemperature = nurTemperature;
	}
	public Integer getNurPulse() {
		return nurPulse;
	}
	public void setNurPulse(Integer nurPulse) {
		this.nurPulse = nurPulse;
	}
	public Integer getNurBreath() {
		return nurBreath;
	}
	public void setNurBreath(Integer nurBreath) {
		this.nurBreath = nurBreath;
	}
	public String getNurDefacation() {
		return nurDefacation;
	}
	public void setNurDefacation(String nurDefacation) {
		this.nurDefacation = nurDefacation;
	}
	public String getNurBloodpressure() {
		return nurBloodpressure;
	}
	public void setNurBloodpressure(String nurBloodpressure) {
		this.nurBloodpressure = nurBloodpressure;
	}
	public Double getNurSumin() {
		return nurSumin;
	}
	public void setNurSumin(Double nurSumin) {
		this.nurSumin = nurSumin;
	}
	public Double getNurSumout() {
		return nurSumout;
	}
	public void setNurSumout(Double nurSumout) {
		this.nurSumout = nurSumout;
	}
	public Double getNurDrainage() {
		return nurDrainage;
	}
	public void setNurDrainage(Double nurDrainage) {
		this.nurDrainage = nurDrainage;
	}
	public Double getNurHeight() {
		return nurHeight;
	}
	public void setNurHeight(Double nurHeight) {
		this.nurHeight = nurHeight;
	}
	public Double getNurWeight() {
		return nurWeight;
	}
	public void setNurWeight(Double nurWeight) {
		this.nurWeight = nurWeight;
	}
	public String getNurAllergicdrug() {
		return nurAllergicdrug;
	}
	public void setNurAllergicdrug(String nurAllergicdrug) {
		this.nurAllergicdrug = nurAllergicdrug;
	}
	public Double getNurHeartrate() {
		return nurHeartrate;
	}
	public void setNurHeartrate(Double nurHeartrate) {
		this.nurHeartrate = nurHeartrate;
	}
	public Double getNurPhysicalcooling() {
		return nurPhysicalcooling;
	}
	public void setNurPhysicalcooling(Double nurPhysicalcooling) {
		this.nurPhysicalcooling = nurPhysicalcooling;
	}
	public Double getNurScore() {
		return nurScore;
	}
	public void setNurScore(Double nurScore) {
		this.nurScore = nurScore;
	}
	public Integer getNurLevel() {
		return nurLevel;
	}
	public void setNurLevel(Integer nurLevel) {
		this.nurLevel = nurLevel;
	}
	public String getTreeId() {
		return treeId;
	}
	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}
	public Integer getNurBloodpressured() {
		return nurBloodpressured;
	}
	public void setNurBloodpressured(Integer nurBloodpressured) {
		this.nurBloodpressured = nurBloodpressured;
	}
	public Integer getNurBloodpressureg() {
		return nurBloodpressureg;
	}
	public void setNurBloodpressureg(Integer nurBloodpressureg) {
		this.nurBloodpressureg = nurBloodpressureg;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getInfoDate() {
		return infoDate;
	}
	public void setInfoDate(Date infoDate) {
		this.infoDate = infoDate;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getDates() {
		return dates;
	}
	public void setDates(String dates) {
		this.dates = dates;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getWeekState() {
		return weekState;
	}
	public void setWeekState(String weekState) {
		this.weekState = weekState;
	}
	public String getNurInpatientNo() {
		return nurInpatientNo;
	}
	public void setNurInpatientNo(String nurInpatientNo) {
		this.nurInpatientNo = nurInpatientNo;
	}
	public String getNurName() {
		return nurName;
	}
	public void setNurName(String nurName) {
		this.nurName = nurName;
	}
	
}
