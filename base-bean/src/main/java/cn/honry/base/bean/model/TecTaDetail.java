package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 终端确认业务子表
 */

public class TecTaDetail extends Entity{

	/**流水号**/
	private String currentSequence;
	/**申请单流水号**/
	private String applyCode;
	/**患者类别**/
	private String patientType;
	/**卡号**/
	private String cardCode;
	/**看诊号**/
	private String clinicCode;
	/**项目编码**/
	private String itemCode;
	/**项目名称**/
	private String itemName;
	/**开立数量**/
	private Double qty;
	/**本次确认数量**/
	private Integer confirmNumber;
	/**剩余数量**/
	private Integer freeNumber;
	/**确认时间**/
	private Date confirmDate;
	/**确认科室**/
	private String confirmDepartment;
	/**确认人**/
	private String confirmEmployee;
	/**状态：0-正常，1-取消，2-退费**/
	private String status;
	/**医嘱流水号**/
	private String extendField1;
	/**门诊扣费**/
	private String extendField2;
	/**取消数量**/
	private String extendField3;
	/**操作员代码**/
	private String operCode;
	/**操作日期**/
	private Date operDate;
	/**作废人**/
	private String cancleOper;
	/**作废日期**/
	private Date cancleDate;
	/**执行设备**/
	private String execDevice;
	/**执行人**/
	private String execOper;
	//**数据库无关字段**/
	/**已确认数量**/
	private Integer haveConfirmNumber;
	/**执行科室名称**/
	private String confirmDeptName;
	
	public String getCurrentSequence() {
		return this.currentSequence;
	}

	public void setCurrentSequence(String currentSequence) {
		this.currentSequence = currentSequence;
	}

	public String getApplyCode() {
		return applyCode;
	}

	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}

	public String getPatientType() {
		return this.patientType;
	}

	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}

	public String getCardCode() {
		return this.cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getClinicCode() {
		return this.clinicCode;
	}

	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public Integer getConfirmNumber() {
		return confirmNumber;
	}

	public void setConfirmNumber(Integer confirmNumber) {
		this.confirmNumber = confirmNumber;
	}

	public Date getConfirmDate() {
		return this.confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getConfirmDepartment() {
		return this.confirmDepartment;
	}

	public void setConfirmDepartment(String confirmDepartment) {
		this.confirmDepartment = confirmDepartment;
	}

	public String getConfirmEmployee() {
		return this.confirmEmployee;
	}

	public void setConfirmEmployee(String confirmEmployee) {
		this.confirmEmployee = confirmEmployee;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExtendField1() {
		return this.extendField1;
	}

	public void setExtendField1(String extendField1) {
		this.extendField1 = extendField1;
	}

	public String getExtendField2() {
		return this.extendField2;
	}

	public void setExtendField2(String extendField2) {
		this.extendField2 = extendField2;
	}

	public String getExtendField3() {
		return this.extendField3;
	}

	public void setExtendField3(String extendField3) {
		this.extendField3 = extendField3;
	}

	public String getOperCode() {
		return this.operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}

	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public String getCancleOper() {
		return this.cancleOper;
	}

	public void setCancleOper(String cancleOper) {
		this.cancleOper = cancleOper;
	}

	public Date getCancleDate() {
		return this.cancleDate;
	}

	public void setCancleDate(Date cancleDate) {
		this.cancleDate = cancleDate;
	}

	public String getExecDevice() {
		return this.execDevice;
	}

	public void setExecDevice(String execDevice) {
		this.execDevice = execDevice;
	}

	public String getExecOper() {
		return this.execOper;
	}

	public void setExecOper(String execOper) {
		this.execOper = execOper;
	}

	public void setConfirmDeptName(String confirmDeptName) {
		this.confirmDeptName = confirmDeptName;
	}

	public String getConfirmDeptName() {
		return confirmDeptName;
	}

	public Integer getFreeNumber() {
		return freeNumber;
	}

	public void setFreeNumber(Integer freeNumber) {
		this.freeNumber = freeNumber;
	}

	public Integer getHaveConfirmNumber() {
		return haveConfirmNumber;
	}

	public void setHaveConfirmNumber(Integer haveConfirmNumber) {
		this.haveConfirmNumber = haveConfirmNumber;
	}

}