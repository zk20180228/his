package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class PathApply extends Entity implements Serializable {

	/**  
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月15日 上午10:45:58 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月15日 上午10:45:58 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String applyNo	        ;//		申请流水号
	private String inpatientNo	    ;//		住院流水号
	private String medicalrecordId	;//		病历号
	private String cpId	    ;//			临床路径ID
	private String versionNo	;//		临床路径版本号
	private String applyType	;//		申请类别
	private String applyStatus	;//		申请状态
	private String applyCode	;//			申请科室
	private String applyDoctCode	;//		申请医生
	private Date   applyDate	;//申请时间
	private String applyMemo	    ;//		申请说明
	private String approvalUser	;//	审批人
	private Date   approvalDate	;//	审批日期
	private Date   executeDate	;//	执行日期
	private String hospitalId	;//	所属医院
	private String areaCode	;//	所属院区
	
	/**数据库没有字段**/
	private String patientName;
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	public String getApplyType() {
		return applyType;
	}
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	public String getApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}
	public String getApplyCode() {
		return applyCode;
	}
	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}
	public String getApplyDoctCode() {
		return applyDoctCode;
	}
	public void setApplyDoctCode(String applyDoctCode) {
		this.applyDoctCode = applyDoctCode;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public String getApplyMemo() {
		return applyMemo;
	}
	public void setApplyMemo(String applyMemo) {
		this.applyMemo = applyMemo;
	}
	public String getApprovalUser() {
		return approvalUser;
	}
	public void setApprovalUser(String approvalUser) {
		this.approvalUser = approvalUser;
	}
	public Date getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	public Date getExecuteDate() {
		return executeDate;
	}
	public void setExecuteDate(Date executeDate) {
		this.executeDate = executeDate;
	}
	public String getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
}
