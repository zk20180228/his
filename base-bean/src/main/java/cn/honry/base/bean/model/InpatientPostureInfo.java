package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**  
 * @className：InpatientPostureInfo.java 
 * @Description：病人体征信息表实体
 * @Author：hedong
 * @CreateDate：2015-08-11
 * @version 1.0
 */
public class InpatientPostureInfo  extends Entity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String patientNo;//住院号
	private String name;      //姓名
	private String temperature;//体温
	private String pulse;//脉搏
	private String breath;//呼吸
	private String pressure;//血压
	private Double weight;//体重
	private Date postureDate;//录入体征时间
	private Integer hospitalId;//医院编码
	private String areaCode;//院区编码
	
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
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
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getPulse() {
		return pulse;
	}
	public void setPulse(String pulse) {
		this.pulse = pulse;
	}
	public String getBreath() {
		return breath;
	}
	public void setBreath(String breath) {
		this.breath = breath;
	}
	public String getPressure() {
		return pressure;
	}
	public void setPressure(String pressure) {
		this.pressure = pressure;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Date getPostureDate() {
		return postureDate;
	}
	public void setPostureDate(Date postureDate) {
		this.postureDate = postureDate;
	}
	
	  
	  
}
