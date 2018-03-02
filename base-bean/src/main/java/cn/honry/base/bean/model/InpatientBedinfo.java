package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @className：InpatientBedinfo.java 
 * @Description：住院床位使用记录表实体
 * @Author：lt
 * @CreateDate：2015-6-26  
 * @version 1.0
 *
 */
public class InpatientBedinfo extends Entity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Fields
	/**医疗流水号*/
	private String clinicNo;
	/**入院时间*/
	private Date inDate;
	/**出院时间*/
	private Date outDate;
	/**床号  (从病床维护表里读取)*/
	private String bedId;
	/**护理单元代码 (部门表里的护士站)*/
	private String nurseCellCode;
	/**医师代码(住院)*/
	private String houseDocCode;
	/**医师代码(主治)*/
	private String chargeDocCode;
	/**医师代码(主任)*/
	private String chiefDocCode;
	/**护士代码(责任)*/
	private String dutyNurseCode;
	/**床号*/
	private String bedNo;
	//新加字段
	/**护理单元名称(部门表里的护士站)**/
	private String nurseCellName;
	/**医师名称(住院)**/
	private String houseDocName;
	/**医师名称(主治)**/
	private String chargeDocName;
	/**医师名称(主任)**/
	private String chiefDocName;
	/**护士名称(责任)**/
	private String dutyNurseName;
	
	public String getNurseCellName() {
		return nurseCellName;
	}
	public void setNurseCellName(String nurseCellName) {
		this.nurseCellName = nurseCellName;
	}
	public String getHouseDocName() {
		return houseDocName;
	}
	public void setHouseDocName(String houseDocName) {
		this.houseDocName = houseDocName;
	}
	public String getChargeDocName() {
		return chargeDocName;
	}
	public void setChargeDocName(String chargeDocName) {
		this.chargeDocName = chargeDocName;
	}
	public String getChiefDocName() {
		return chiefDocName;
	}
	public void setChiefDocName(String chiefDocName) {
		this.chiefDocName = chiefDocName;
	}
	public String getDutyNurseName() {
		return dutyNurseName;
	}
	public void setDutyNurseName(String dutyNurseName) {
		this.dutyNurseName = dutyNurseName;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getBedId() {
		return bedId;
	}
	public void setBedId(String bedId) {
		this.bedId = bedId;
	}
	public String getClinicNo() {
		return clinicNo;
	}
	public void setClinicNo(String clinicNo) {
		this.clinicNo = clinicNo;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public String getHouseDocCode() {
		return houseDocCode;
	}
	public void setHouseDocCode(String houseDocCode) {
		this.houseDocCode = houseDocCode;
	}
	public String getChargeDocCode() {
		return chargeDocCode;
	}
	public void setChargeDocCode(String chargeDocCode) {
		this.chargeDocCode = chargeDocCode;
	}
	public String getChiefDocCode() {
		return chiefDocCode;
	}
	public void setChiefDocCode(String chiefDocCode) {
		this.chiefDocCode = chiefDocCode;
	}
	public String getDutyNurseCode() {
		return dutyNurseCode;
	}
	public void setDutyNurseCode(String dutyNurseCode) {
		this.dutyNurseCode = dutyNurseCode;
	}

	
}