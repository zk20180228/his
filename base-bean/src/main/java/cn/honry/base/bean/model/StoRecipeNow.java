package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 处方调剂头表
 * @author zpty
 * Date:2016/01/16 15:30
 */

public class StoRecipeNow extends Entity {	
	/**发药药房编码**/
	private String drugDeptCode;
	/**处方号**/
	private String recipeNo;
	/**出库申请分类  1-门诊摆药，3-门诊退药**/
	private String classMeaningCode;
	/**交易类型 1 正交易  2 反交易**/
	private Integer transType;
	/**处方状态: 0申请,1打印,2配药,3发药,4还药(当天未发的药品返回货架)**/
	private Integer recipeState;
	/**门诊号**/
	private String clinicCode;
	/**病历号**/
	private String cardNo;
	/**患者姓名**/
	private String patientName;
	/**性别**/
	private Integer sexCode;
	/**年龄 (出生日期)**/
	private Date birthday;
	/**结算类别**/
	private String paykindCode;
	/**患者科室编码**/
	private String deptCode;
	/**挂号日期**/
	private Date regDate;
	/**开方医生**/
	private String doctCode;
	/**开方医生所在科室**/
	private String doctDept;
	/**配药终端(配药标签打印台)**/
	private String drugedTerminal;
	/**配药终端Code**/
	private String drugedTerminalCode;
	/**配药终端Name**/
	private String drugedTerminalName;
	/**发药终端(发药窗口)**/
	private String sendTerminal;
	/**发药终端Code**/
	private String sendTerminalCode;
	/**发药终端Name**/
	private String sendTerminalName;
	/**收费人编码**/
	private String feeOper;
	/**收费时间**/
	private Date feeDate;
	/**票据号**/
	private String invoiceNo;
	/**处方金额(零售金额)**/
	private Double recipeCost;
	/**处方中药品数量**/
	private Double recipeQty;
	/**已配药的药品数量**/
	private Double drugedQty;
	/**配药人**/
	private String drugedOper;
	/**配药科室**/
	private String drugedDept;
	/**配药日期**/
	private Date drugedDate;
	/**发药人**/
	private String sendOper;
	/**发药科室**/
	private String sendDept;
	/**发药时间**/
	private Date sendDate;
	/**有效状态：1有效，0无效 2 发药后退费**/
	private Integer validState;
	/**退药改药0否1是**/
	private Integer ModifyFlag;
	/**还药人**/
	private String backOper;
	/**还药日期**/
	private Date backDate;
	/**取消操作员**/
	private String cancelOper;
	/**取消日期**/
	private Date cancelDate;
	/**备注 **/
	private String mark;
	/**处方内药品剂数合计**/
	private Double sumDays;
	/**记录原调剂台(发生过其他台配药)**/
	private String extFlag;
	/**记录原发药窗(发生过其他窗发药)**/
	private String extFlag1;
	//新增字段 2016-11-15 10:14:00
	/**发药药房名称*/
	private String drugDeptName;
	/**结算名称*/
	private String payKindName;
	/**患者科室名称*/
	private String deptName;
	/**开方医生姓名*/
	private String doctName;
	/**开方医生所在科室名称*/
	private String doctDeptName;
	/**收费人姓名*/
	private String feeOperName;
	/**配药人姓名*/
	private String drugedOperName;
	/**配药科室名称**/
	private String drugedDeptName;
	/**发药人姓名*/
	private String sendOperName;
	/**发药人科室名称*/
	private String sendDeptName;
	/**还药人姓名*/
	private String backOperName;
	/**取消操作员姓名*/
	private String cancelOperName;
	/**性别*/
	private String sexName;
	/**所属医院**/
	private Integer hospitalID;
	/**所属院区**/
	private String areaCode;
	//与数据库无关字段
	public String getDrugDeptCode() {
		return drugDeptCode;
	}
	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public String getClassMeaningCode() {
		return classMeaningCode;
	}
	public void setClassMeaningCode(String classMeaningCode) {
		this.classMeaningCode = classMeaningCode;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public Integer getRecipeState() {
		return recipeState;
	}
	public void setRecipeState(Integer recipeState) {
		this.recipeState = recipeState;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public Integer getSexCode() {
		return sexCode;
	}
	public void setSexCode(Integer sexCode) {
		this.sexCode = sexCode;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getPaykindCode() {
		return paykindCode;
	}
	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public String getDoctCode() {
		return doctCode;
	}
	public void setDoctCode(String doctCode) {
		this.doctCode = doctCode;
	}
	public String getDoctDept() {
		return doctDept;
	}
	public void setDoctDept(String doctDept) {
		this.doctDept = doctDept;
	}
	public String getDrugedTerminal() {
		return drugedTerminal;
	}
	public void setDrugedTerminal(String drugedTerminal) {
		this.drugedTerminal = drugedTerminal;
	}
	public String getSendTerminal() {
		return sendTerminal;
	}
	public void setSendTerminal(String sendTerminal) {
		this.sendTerminal = sendTerminal;
	}
	public String getFeeOper() {
		return feeOper;
	}
	public void setFeeOper(String feeOper) {
		this.feeOper = feeOper;
	}
	public Date getFeeDate() {
		return feeDate;
	}
	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Double getRecipeCost() {
		return recipeCost;
	}
	public void setRecipeCost(Double recipeCost) {
		this.recipeCost = recipeCost;
	}
	public Double getRecipeQty() {
		return recipeQty;
	}
	public void setRecipeQty(Double recipeQty) {
		this.recipeQty = recipeQty;
	}
	public Double getDrugedQty() {
		return drugedQty;
	}
	public void setDrugedQty(Double drugedQty) {
		this.drugedQty = drugedQty;
	}
	public String getDrugedOper() {
		return drugedOper;
	}
	public void setDrugedOper(String drugedOper) {
		this.drugedOper = drugedOper;
	}
	public String getDrugedDept() {
		return drugedDept;
	}
	public void setDrugedDept(String drugedDept) {
		this.drugedDept = drugedDept;
	}
	public Date getDrugedDate() {
		return drugedDate;
	}
	public void setDrugedDate(Date drugedDate) {
		this.drugedDate = drugedDate;
	}
	public String getSendOper() {
		return sendOper;
	}
	public void setSendOper(String sendOper) {
		this.sendOper = sendOper;
	}
	public String getSendDept() {
		return sendDept;
	}
	public void setSendDept(String sendDept) {
		this.sendDept = sendDept;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public Integer getValidState() {
		return validState;
	}
	public void setValidState(Integer validState) {
		this.validState = validState;
	}
	public Integer getModifyFlag() {
		return ModifyFlag;
	}
	public void setModifyFlag(Integer modifyFlag) {
		ModifyFlag = modifyFlag;
	}
	public String getBackOper() {
		return backOper;
	}
	public void setBackOper(String backOper) {
		this.backOper = backOper;
	}
	public Date getBackDate() {
		return backDate;
	}
	public void setBackDate(Date backDate) {
		this.backDate = backDate;
	}
	public String getCancelOper() {
		return cancelOper;
	}
	public void setCancelOper(String cancelOper) {
		this.cancelOper = cancelOper;
	}
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public Double getSumDays() {
		return sumDays;
	}
	public void setSumDays(Double sumDays) {
		this.sumDays = sumDays;
	}
	public String getExtFlag() {
		return extFlag;
	}
	public void setExtFlag(String extFlag) {
		this.extFlag = extFlag;
	}
	public String getExtFlag1() {
		return extFlag1;
	}
	public void setExtFlag1(String extFlag1) {
		this.extFlag1 = extFlag1;
	}
	public String getDrugedTerminalCode() {
		return drugedTerminalCode;
	}
	public void setDrugedTerminalCode(String drugedTerminalCode) {
		this.drugedTerminalCode = drugedTerminalCode;
	}
	public String getDrugedTerminalName() {
		return drugedTerminalName;
	}
	public void setDrugedTerminalName(String drugedTerminalName) {
		this.drugedTerminalName = drugedTerminalName;
	}
	public String getSendTerminalCode() {
		return sendTerminalCode;
	}
	public void setSendTerminalCode(String sendTerminalCode) {
		this.sendTerminalCode = sendTerminalCode;
	}
	public String getSendTerminalName() {
		return sendTerminalName;
	}
	public void setSendTerminalName(String sendTerminalName) {
		this.sendTerminalName = sendTerminalName;
	}
	public String getDrugDeptName() {
		return drugDeptName;
	}
	public void setDrugDeptName(String drugDeptName) {
		this.drugDeptName = drugDeptName;
	}
	public String getPayKindName() {
		return payKindName;
	}
	public void setPayKindName(String payKindName) {
		this.payKindName = payKindName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDoctName() {
		return doctName;
	}
	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}
	public String getDoctDeptName() {
		return doctDeptName;
	}
	public void setDoctDeptName(String doctDeptName) {
		this.doctDeptName = doctDeptName;
	}
	public String getFeeOperName() {
		return feeOperName;
	}
	public void setFeeOperName(String feeOperName) {
		this.feeOperName = feeOperName;
	}
	public String getDrugedOperName() {
		return drugedOperName;
	}
	public void setDrugedOperName(String drugedOperName) {
		this.drugedOperName = drugedOperName;
	}
	public String getDrugedDeptName() {
		return drugedDeptName;
	}
	public void setDrugedDeptName(String drugedDeptName) {
		this.drugedDeptName = drugedDeptName;
	}
	public String getSendOperName() {
		return sendOperName;
	}
	public void setSendOperName(String sendOperName) {
		this.sendOperName = sendOperName;
	}
	public String getSendDeptName() {
		return sendDeptName;
	}
	public void setSendDeptName(String sendDeptName) {
		this.sendDeptName = sendDeptName;
	}
	public String getBackOperName() {
		return backOperName;
	}
	public void setBackOperName(String backOperName) {
		this.backOperName = backOperName;
	}
	public String getCancelOperName() {
		return cancelOperName;
	}
	public void setCancelOperName(String cancelOperName) {
		this.cancelOperName = cancelOperName;
	}
	public String getSexName() {
		return sexName;
	}
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	public Integer getHospitalID() {
		return hospitalID;
	}
	public void setHospitalID(Integer hospitalID) {
		this.hospitalID = hospitalID;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	

}