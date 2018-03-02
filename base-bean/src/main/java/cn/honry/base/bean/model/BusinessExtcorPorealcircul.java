package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;
/**
 * 体外循环表
 * @author liudelin
 * Date:2015/6/30 8:56
 */
public class BusinessExtcorPorealcircul  extends Entity{

	/**住院号/门诊号 **/
	private String inpatientNo;
	/**病历号**/
	private String patientNo;
	/**姓名**/
	private String name;
	/**性别**/
	private String sex;
	/**年龄**/
	private String age;
	/**体重**/
	private Double weight;
	/**体表面积**/
	private Double bodyarea;
	/**手术名称**/
	private String operationName;
	/**手术者**/
	private String operater;
	/**灌注医师**/
	private String affuseDoctor;
	/**转流时间**/
	private String turntime;
	/**阻断时间**/
	private String  blocktime;
	/**辅助时间**/
	private String  assisttime;
	/**停循环时间**/
	private String  dhctime;
	/**温度范围**/
	private String  temperange;
	/**血压（转前）**/
	private String  piesisFront;
	/**血压（转中）**/
	private String  piesisMiddle;
	/**血压（转后）**/
	private String piesisEnd;
	/**超滤**/
	private String filtRate;
	/**预冲量**/
	private String impulsevoLume;
	/**尿量**/
	private String urinevoLume;
	/**尿色**/
	private String urineColour;
	/**通气量**/
	private String aeratevoLume;
	/**输血量**/
	private String transFusionvolLume;
	/**肺型**/
	private String lungsType;
	/**痊愈后情况**/
	private String recoverycase;
	/**患者来源  C：门诊；I：住院**/
	private String patientFlg;
	
	
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getBodyarea() {
		return bodyarea;
	}
	public void setBodyarea(Double bodyarea) {
		this.bodyarea = bodyarea;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getOperater() {
		return operater;
	}
	public void setOperater(String operater) {
		this.operater = operater;
	}
	public String getAffuseDoctor() {
		return affuseDoctor;
	}
	public void setAffuseDoctor(String affuseDoctor) {
		this.affuseDoctor = affuseDoctor;
	}
	public String getTurntime() {
		return turntime;
	}
	public void setTurntime(String turntime) {
		this.turntime = turntime;
	}
	public String getBlocktime() {
		return blocktime;
	}
	public void setBlocktime(String blocktime) {
		this.blocktime = blocktime;
	}
	public String getAssisttime() {
		return assisttime;
	}
	public void setAssisttime(String assisttime) {
		this.assisttime = assisttime;
	}
	public String getDhctime() {
		return dhctime;
	}
	public void setDhctime(String dhctime) {
		this.dhctime = dhctime;
	}
	public String getTemperange() {
		return temperange;
	}
	public void setTemperange(String temperange) {
		this.temperange = temperange;
	}
	public String getPiesisFront() {
		return piesisFront;
	}
	public void setPiesisFront(String piesisFront) {
		this.piesisFront = piesisFront;
	}
	public String getPiesisMiddle() {
		return piesisMiddle;
	}
	public void setPiesisMiddle(String piesisMiddle) {
		this.piesisMiddle = piesisMiddle;
	}
	public String getPiesisEnd() {
		return piesisEnd;
	}
	public void setPiesisEnd(String piesisEnd) {
		this.piesisEnd = piesisEnd;
	}
	public String getFiltRate() {
		return filtRate;
	}
	public void setFiltRate(String filtRate) {
		this.filtRate = filtRate;
	}
	public String getImpulsevoLume() {
		return impulsevoLume;
	}
	public void setImpulsevoLume(String impulsevoLume) {
		this.impulsevoLume = impulsevoLume;
	}
	public String getUrinevoLume() {
		return urinevoLume;
	}
	public void setUrinevoLume(String urinevoLume) {
		this.urinevoLume = urinevoLume;
	}
	public String getUrineColour() {
		return urineColour;
	}
	public void setUrineColour(String urineColour) {
		this.urineColour = urineColour;
	}
	public String getAeratevoLume() {
		return aeratevoLume;
	}
	public void setAeratevoLume(String aeratevoLume) {
		this.aeratevoLume = aeratevoLume;
	}
	public String getTransFusionvolLume() {
		return transFusionvolLume;
	}
	public void setTransFusionvolLume(String transFusionvolLume) {
		this.transFusionvolLume = transFusionvolLume;
	}
	public String getLungsType() {
		return lungsType;
	}
	public void setLungsType(String lungsType) {
		this.lungsType = lungsType;
	}
	public String getRecoverycase() {
		return recoverycase;
	}
	public void setRecoverycase(String recoverycase) {
		this.recoverycase = recoverycase;
	}
	public String getPatientFlg() {
		return patientFlg;
	}
	public void setPatientFlg(String patientFlg) {
		this.patientFlg = patientFlg;
	}
	
	
	
}
