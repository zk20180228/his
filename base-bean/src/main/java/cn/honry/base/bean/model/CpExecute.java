package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class CpExecute  extends Entity implements Serializable {
	/**  
	 * 临床路径执行信息
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月15日 下午12:00:35 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月15日 下午12:00:35 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String inpatientNo	   ;//	住院流水号
	private String medicalrecordId;//	病历号
	private String itemNo	       ;//	项目序号
	private String modelCode	 ;//			模板代码
	private String modelVersion;//			模板版本号
	private String stageId	     ;//			阶段ID
	private String planCode	 ;//			计划类别代码
	private String flag	;//		长期/临时标志
	private Date executeDate	;//执行时间
	private String executeUser	;//	执行人
	private String nursSign	;//	护士签名
	private String doctSign	;//	医生签名
	private Date nursSignDate	;//护士签名时间
	private Date doctSignDate	;//医生签名时间
	private String executeFlag	;//	执行标志
	private String hospitalId	;//		所属医院
	private String areaCode	;//		所属院区
	private String cpId	        ;//		临床路径ID
	
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
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
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getModelVersion() {
		return modelVersion;
	}
	public void setModelVersion(String modelVersion) {
		this.modelVersion = modelVersion;
	}
	public String getStageId() {
		return stageId;
	}
	public void setStageId(String stageId) {
		this.stageId = stageId;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Date getExecuteDate() {
		return executeDate;
	}
	public void setExecuteDate(Date executeDate) {
		this.executeDate = executeDate;
	}
	public String getExecuteUser() {
		return executeUser;
	}
	public void setExecuteUser(String executeUser) {
		this.executeUser = executeUser;
	}
	public String getNursSign() {
		return nursSign;
	}
	public void setNursSign(String nursSign) {
		this.nursSign = nursSign;
	}
	public String getDoctSign() {
		return doctSign;
	}
	public void setDoctSign(String doctSign) {
		this.doctSign = doctSign;
	}
	public Date getNursSignDate() {
		return nursSignDate;
	}
	public void setNursSignDate(Date nursSignDate) {
		this.nursSignDate = nursSignDate;
	}
	public Date getDoctSignDate() {
		return doctSignDate;
	}
	public void setDoctSignDate(Date doctSignDate) {
		this.doctSignDate = doctSignDate;
	}
	public String getExecuteFlag() {
		return executeFlag;
	}
	public void setExecuteFlag(String executeFlag) {
		this.executeFlag = executeFlag;
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
