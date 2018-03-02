package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 *
 * @Title: InpatientTecconfirm.java
 * @Description：住院医技终端确认实体类
 * @Author：aizhonghua
 * @CreateDate：2016年4月28日 下午5:04:44 
 * @Modifier：
 * @ModifyDate：
 * @ModifyRmk：  
 * @version： 1.0：
 *
 */
@SuppressWarnings("serial")
public class InpatientTecconfirmNow extends Entity  {

	/**
	 * 医嘱流水号
	 */
	private String moOrder;
	/**
	 * 执行单流水号
	 */
	private String execSqn;
	/**
	 * 流水号
	 */
	private Long currentSequence;
	/**
	 * 项目编码
	 */
	private String itemCode;
	/**
	 * 项目名称
	 */
	private String itemName;
	/**
	 * 本次确认数量
	 */
	private Integer confirmNumber;
	/**
	 * 确认人
	 */
	private String confirmEmployee;
	/**
	 * 确认科室
	 */
	private String confirmDepartment;
	/**
	 * 确认时间
	 */
	private Date confirmDate;
	/**
	 * 状态：0-正常，1-取消，2-退费
	 */
	private Integer status;
	/**
	 * 作废人
	 */
	private String cancleOper;
	/**
	 * 作废日期
	 */
	private Date cancleDate;
	/**
	 * 住院流水号
	 */
	private String inpatientNo;
	/**
	 * 处方号
	 */
	private String recipeNo;
	/**
	 * 处方内项目流水号
	 */
	private Integer sequenceNo;
	/**
	 * 执行设备
	 */
	private String execDevice;
	/**
	 * 执行人
	 */
	private String operator;
	/**
	 * 取消数量
	 */
	private Integer cancelQty;
	
	
	//新加字段
	/**确认科室名称**/
	private String confirmDepartmentName;
	
//	/** 新添加字段 医院编码  **/
//	private Integer hospitalId;
//	/** 新添加字段 院区编码  **/
//	private String areaCode;
//	
//	public Integer getHospitalId() {
//		return hospitalId;
//	}
//	public void setHospitalId(Integer hospitalId) {
//		this.hospitalId = hospitalId;
//	}
//	public String getAreaCode() {
//		return areaCode;
//	}
//	public void setAreaCode(String areaCode) {
//		this.areaCode = areaCode;
//	}
//	
	public String getConfirmDepartmentName() {
		return confirmDepartmentName;
	}
	public void setConfirmDepartmentName(String confirmDepartmentName) {
		this.confirmDepartmentName = confirmDepartmentName;
	}
	public String getMoOrder() {
		return moOrder;
	}
	public void setMoOrder(String moOrder) {
		this.moOrder = moOrder;
	}
	public String getExecSqn() {
		return execSqn;
	}
	public void setExecSqn(String execSqn) {
		this.execSqn = execSqn;
	}
	public Long getCurrentSequence() {
		return currentSequence;
	}
	public void setCurrentSequence(Long currentSequence) {
		this.currentSequence = currentSequence;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Integer getConfirmNumber() {
		return confirmNumber;
	}
	public void setConfirmNumber(Integer confirmNumber) {
		this.confirmNumber = confirmNumber;
	}
	public String getConfirmEmployee() {
		return confirmEmployee;
	}
	public void setConfirmEmployee(String confirmEmployee) {
		this.confirmEmployee = confirmEmployee;
	}
	public String getConfirmDepartment() {
		return confirmDepartment;
	}
	public void setConfirmDepartment(String confirmDepartment) {
		this.confirmDepartment = confirmDepartment;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCancleOper() {
		return cancleOper;
	}
	public void setCancleOper(String cancleOper) {
		this.cancleOper = cancleOper;
	}
	public Date getCancleDate() {
		return cancleDate;
	}
	public void setCancleDate(Date cancleDate) {
		this.cancleDate = cancleDate;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getExecDevice() {
		return execDevice;
	}
	public void setExecDevice(String execDevice) {
		this.execDevice = execDevice;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Integer getCancelQty() {
		return cancelQty;
	}
	public void setCancelQty(Integer cancelQty) {
		this.cancelQty = cancelQty;
	}
	

}