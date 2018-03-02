package cn.honry.base.bean.model;

import java.util.Date;

/**  
 * 类说明   押金催款单
 *  
 * @author qh 
 * @date 2017年4月28日  
 */
public class DepositReminder {
	/**催费时间**/
	private Date newdate;
	/**病区**/
	private String bedward;
	/**床位**/
	private String bedname;
	/**患者姓名**/
	private String patientname;
	/**押金总额**/
	private Double changecost;
	/**花费总额**/
	private Double totcost;
	/**余额**/
	private Double freecost;
	/**住院号**/
	private String inpatientno;
	/**入院日期**/
	private Date indate;
	/**病历号**/
	private String medicalrecordId;
	
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public Date getNewdate() {
		return newdate;
	}
	public void setNewdate(Date newdate) {
		this.newdate = newdate;
	}
	public String getBedward() {
		return bedward;
	}
	public void setBedward(String bedward) {
		this.bedward = bedward;
	}
	public String getBedname() {
		return bedname;
	}
	public void setBedname(String bedname) {
		this.bedname = bedname;
	}
	public String getPatientname() {
		return patientname;
	}
	public void setPatientname(String patientname) {
		this.patientname = patientname;
	}
	public Double getChangecost() {
		return changecost;
	}
	public void setChangecost(Double changecost) {
		this.changecost = changecost;
	}
	public Double getTotcost() {
		return totcost;
	}
	public void setTotcost(Double totcost) {
		this.totcost = totcost;
	}
	public Double getFreecost() {
		return freecost;
	}
	public void setFreecost(Double freecost) {
		this.freecost = freecost;
	}
	public String getInpatientno() {
		return inpatientno;
	}
	public void setInpatientno(String inpatientno) {
		this.inpatientno = inpatientno;
	}
	public Date getIndate() {
		return indate;
	}
	public void setIndate(Date indate) {
		this.indate = indate;
	}
	
}
