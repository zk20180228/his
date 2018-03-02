package cn.honry.base.bean.model;

import java.util.Date;

/**  
 * 类说明   病危通知单实体
 *  
 * @author qh 
 * @date 2017年4月28日  
 */
public class CriticalNotice {
	/**医院名称**/
	private String hospitalName;
	/**病人姓名**/
	private String patient_name;
	/**当前时间**/
	private Date datesj;
	/**病床信息**/
	private String bedward_name;
	/**医生姓名**/
	private String house_doc_name;
	/**护士**/
	private String duty_nurse_name;
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getPatient_name() {
		return patient_name;
	}
	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}
	public Date getDatesj() {
		return datesj;
	}
	public void setDatesj(Date datesj) {
		this.datesj = datesj;
	}
	public String getBedward_name() {
		return bedward_name;
	}
	public void setBedward_name(String bedward_name) {
		this.bedward_name = bedward_name;
	}
	public String getHouse_doc_name() {
		return house_doc_name;
	}
	public void setHouse_doc_name(String house_doc_name) {
		this.house_doc_name = house_doc_name;
	}
	public String getDuty_nurse_name() {
		return duty_nurse_name;
	}
	public void setDuty_nurse_name(String duty_nurse_name) {
		this.duty_nurse_name = duty_nurse_name;
	}
	
}
