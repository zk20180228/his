package cn.honry.statistics.deptstat.dischargeStatistics.vo;

import java.util.List;

public class DischargeStatisticsVo {
private String illCode;
private String illName;
private String deptCode;//科室名称
private String diseaseKind;//疾病种类
private String icd_code;
private String outPatient;//出院病人数
private String subTotal;//小计
private String cure;//治愈
private String better;//好转
private String notCure;//未治愈
private String death;//死亡
private String other;//其他
private String checkTreat;//检治
private String cureRate;//治愈率
private String betterRate;//好转率
private String deathRate;//死亡率
private String avgInpatDay;//平均住院日
private String avgBeforeOperDay;//平均术前住院日数
private String cost;//总费用
private String avgBedFee;//人均床位费
private String avgDrugFee;//人均药费
private String avgOperFee;//人均手术费
private String avgCheckTreat;//人均检治
private String drugProp;//药品比
private String num;//合计
//报表list
private List<DischargeStatisticsVo> list;


public List<DischargeStatisticsVo> getList() {
	return list;
}
public void setList(List<DischargeStatisticsVo> list) {
	this.list = list;
}
public String getNum() {
	return num;
}
public void setNum(String num) {
	this.num = num;
}
public String getCheckTreat() {
	return checkTreat;
}
public void setCheckTreat(String checkTreat) {
	this.checkTreat = checkTreat;
}
public String getIllCode() {
	return illCode;
}
public void setIllCode(String illCode) {
	this.illCode = illCode;
}
public String getIllName() {
	return illName;
}
public void setIllName(String illName) {
	this.illName = illName;
}
public String getDeptCode() {
	return deptCode;
}
public void setDeptCode(String deptCode) {
	this.deptCode = deptCode;
}
public String getDiseaseKind() {
	return diseaseKind;
}
public void setDiseaseKind(String diseaseKind) {
	this.diseaseKind = diseaseKind;
}
public String getIcd_code() {
	return icd_code;
}
public void setIcd_code(String icd_code) {
	this.icd_code = icd_code;
}
public String getOutPatient() {
	return outPatient;
}
public void setOutPatient(String outPatient) {
	this.outPatient = outPatient;
}
public String getSubTotal() {
	return subTotal;
}
public void setSubTotal(String subTotal) {
	this.subTotal = subTotal;
}
public String getCure() {
	return cure;
}
public void setCure(String cure) {
	this.cure = cure;
}
public String getBetter() {
	return better;
}
public void setBetter(String better) {
	this.better = better;
}
public String getNotCure() {
	return notCure;
}
public void setNotCure(String notCure) {
	this.notCure = notCure;
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
public String getBetterRate() {
	return betterRate;
}
public void setBetterRate(String betterRate) {
	this.betterRate = betterRate;
}
public String getDeathRate() {
	return deathRate;
}
public void setDeathRate(String deathRate) {
	this.deathRate = deathRate;
}
public String getAvgInpatDay() {
	return avgInpatDay;
}
public void setAvgInpatDay(String avgInpatDay) {
	this.avgInpatDay = avgInpatDay;
}
public String getAvgBeforeOperDay() {
	return avgBeforeOperDay;
}
public void setAvgBeforeOperDay(String avgBeforeOperDay) {
	this.avgBeforeOperDay = avgBeforeOperDay;
}
public String getCost() {
	return cost;
}
public void setCost(String cost) {
	this.cost = cost;
}
public String getAvgBedFee() {
	return avgBedFee;
}
public void setAvgBedFee(String avgBedFee) {
	this.avgBedFee = avgBedFee;
}
public String getAvgDrugFee() {
	return avgDrugFee;
}
public void setAvgDrugFee(String avgDrugFee) {
	this.avgDrugFee = avgDrugFee;
}
public String getAvgOperFee() {
	return avgOperFee;
}
public void setAvgOperFee(String avgOperFee) {
	this.avgOperFee = avgOperFee;
}
public String getAvgCheckTreat() {
	return avgCheckTreat;
}
public void setAvgCheckTreat(String avgCheckTreat) {
	this.avgCheckTreat = avgCheckTreat;
}
public String getDrugProp() {
	return drugProp;
}
public void setDrugProp(String drugProp) {
	this.drugProp = drugProp;
}

}
