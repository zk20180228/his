package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 终端确认申请表
 */

public class TecTerminalApply  extends Entity{
	
	/**申请流水号 **/
	private String applyNumber;
	/**住院流水号/门诊号/体检号 **/
	private String clinicNo;
	/**姓名 **/
	private String name;
	/**合同单位 **/
	private String pactCode;
	/**申请部门编码（科室或者病区） **/
	private String deptCode;
	/**终端科室编码 **/
	private String executeDeptcode;
	/**门诊是挂号科室、住院是在院科室 **/
	private String inhosDeptcode;
	/**发药部门编码 **/
	private String drugDeptCode;
	/**更新库存的流水号(物资) **/
	private Long updateSequenceno;
	/**开立医师代码 **/
	private String recipeDoccode;
	/**处方号 **/
	private String recipeNo;
	/**处方内项目流水号 **/
	private Integer sequenceNo;
	/**项目代码 **/
	private String itemCode;
	/**项目名称 **/
	private String itemName;
	/**单价 **/
	private Double unitPrice;
	/**数量 **/
	private Double qty;
	/**当前单位 **/
	private String currentUnit;
	/**组套代码 **/
	private String packageCode;
	/**组套名称 **/
	private String packageName;
	/**费用金额 **/
	private Double totCost;
	/**项目状态（0 划价  1 批费 2 执行（药品发放）） **/
	private String sendFlag;
	/**已确认数 **/
	private Integer confirmNum;
	/**设备号 **/
	private String machineNo;
	/**0 未收费 1门诊收费 2 扣门诊账户 3预收费团体体检 4 药品预审核 **/
	private String payFlag;
	/**新旧项目标识： 0 旧 1 新 **/
	private String extFlag;
	/**1 有效  ,0无效 **/
	private String extFlag1;
	/**扩展标志2(收费方式0住院处直接收费,1护士站医嘱收费,2确认收费,3身份变更,4比例调整) **/
	private String extFlag2;
	/**备注 **/
	private String mark;
	/**医嘱流水号 **/
	private String moOrder;
	/**医嘱执行单流水号 **/
	private String moExecSqn;
	/**操作员（插入申请单） **/
	private String operCode;
	/**操作时间（插入申请单） **/
	private Date operDate;
	/**患者类别：‘1’ 门诊|‘2’ 住院|‘3’ 急诊|‘4’ 体检  5 集体体检 **/
	private String patienttype;
	/**性别 **/
	private String patientsex;
	/**药品发放时间 **/
	private Date senddatetime;
	/**终端执行人编号 **/
	private String validateoperatorcode;
	/**终端执行时间 **/
	private Date validetedtime;
	/**是否是药品 1：是0：否 **/
	private String isphamacy;
	/**就诊卡编码 **/
	private String cardNo;
	//**数据库无关字段开始**/
	/**部门名称 **/
	private String deptName;
	/**处方明细id **/
	private String outpatientFeedetailId;
	/**执行数量 **/
	private Integer executeNum;
	/**数据库无关字段结束**/
	

	public String getClinicNo() {
		return this.clinicNo;
	}

	public String getApplyNumber() {
		return applyNumber;
	}

	public void setApplyNumber(String applyNumber) {
		this.applyNumber = applyNumber;
	}

	public void setClinicNo(String clinicNo) {
		this.clinicNo = clinicNo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPactCode() {
		return this.pactCode;
	}

	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}

	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getExecuteDeptcode() {
		return this.executeDeptcode;
	}

	public void setExecuteDeptcode(String executeDeptcode) {
		this.executeDeptcode = executeDeptcode;
	}

	public String getInhosDeptcode() {
		return this.inhosDeptcode;
	}

	public void setInhosDeptcode(String inhosDeptcode) {
		this.inhosDeptcode = inhosDeptcode;
	}

	public String getDrugDeptCode() {
		return this.drugDeptCode;
	}

	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}

	public Long getUpdateSequenceno() {
		return this.updateSequenceno;
	}

	public void setUpdateSequenceno(Long updateSequenceno) {
		this.updateSequenceno = updateSequenceno;
	}

	public String getRecipeDoccode() {
		return this.recipeDoccode;
	}

	public void setRecipeDoccode(String recipeDoccode) {
		this.recipeDoccode = recipeDoccode;
	}

	public String getRecipeNo() {
		return this.recipeNo;
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

	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public String getCurrentUnit() {
		return this.currentUnit;
	}

	public void setCurrentUnit(String currentUnit) {
		this.currentUnit = currentUnit;
	}

	public String getPackageCode() {
		return this.packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Double getTotCost() {
		return this.totCost;
	}

	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}

	public String getSendFlag() {
		return this.sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}

	public String getMachineNo() {
		return this.machineNo;
	}

	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}

	public String getPayFlag() {
		return this.payFlag;
	}

	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}

	public String getExtFlag() {
		return this.extFlag;
	}

	public void setExtFlag(String extFlag) {
		this.extFlag = extFlag;
	}

	public String getExtFlag1() {
		return this.extFlag1;
	}

	public void setExtFlag1(String extFlag1) {
		this.extFlag1 = extFlag1;
	}

	public String getExtFlag2() {
		return this.extFlag2;
	}

	public void setExtFlag2(String extFlag2) {
		this.extFlag2 = extFlag2;
	}

	public String getMark() {
		return this.mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getMoOrder() {
		return this.moOrder;
	}

	public void setMoOrder(String moOrder) {
		this.moOrder = moOrder;
	}

	public String getMoExecSqn() {
		return this.moExecSqn;
	}

	public void setMoExecSqn(String moExecSqn) {
		this.moExecSqn = moExecSqn;
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

	public String getPatienttype() {
		return this.patienttype;
	}

	public void setPatienttype(String patienttype) {
		this.patienttype = patienttype;
	}

	public String getPatientsex() {
		return this.patientsex;
	}

	public void setPatientsex(String patientsex) {
		this.patientsex = patientsex;
	}
	public Date getSenddatetime() {
		return senddatetime;
	}

	public void setSenddatetime(Date senddatetime) {
		this.senddatetime = senddatetime;
	}

	public String getValidateoperatorcode() {
		return validateoperatorcode;
	}

	public void setValidateoperatorcode(String validateoperatorcode) {
		this.validateoperatorcode = validateoperatorcode;
	}

	public Date getValidetedtime() {
		return validetedtime;
	}

	public void setValidetedtime(Date validetedtime) {
		this.validetedtime = validetedtime;
	}

	public String getIsphamacy() {
		return isphamacy;
	}

	public void setIsphamacy(String isphamacy) {
		this.isphamacy = isphamacy;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setOutpatientFeedetailId(String outpatientFeedetailId) {
		this.outpatientFeedetailId = outpatientFeedetailId;
	}

	public String getOutpatientFeedetailId() {
		return outpatientFeedetailId;
	}

	public Integer getExecuteNum() {
		return executeNum;
	}

	public void setExecuteNum(Integer executeNum) {
		this.executeNum = executeNum;
	}

	public Integer getConfirmNum() {
		return confirmNum;
	}

	public void setConfirmNum(Integer confirmNum) {
		this.confirmNum = confirmNum;
	}

}