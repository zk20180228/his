package cn.honry.base.bean.model;

import java.io.Serializable;

import cn.honry.base.bean.business.Entity;

public class CpMasterIndex extends Entity implements Serializable {
	/**  
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月15日 上午11:50:19 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月15日 上午11:50:19 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	private String inpatientNo	    ;//	住院流水号
	private String medicalrecordId	;//	病历号
	private String cpId	        ;//		临床路径ID
	private String versionCode	    ;//	临床路径版本号
	private String deptCode	    ;//	入径科室
	private String inpathFlag	    ;//入径标志
	private String outpathFlag	    ;//出径标志
	private String outpTypeCode	;//出径情况代码
	private String hospitalId	    ;//	所属医院
	private String areaCode	    ;//	所属院区
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
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getInpathFlag() {
		return inpathFlag;
	}
	public void setInpathFlag(String inpathFlag) {
		this.inpathFlag = inpathFlag;
	}
	public String getOutpathFlag() {
		return outpathFlag;
	}
	public void setOutpathFlag(String outpathFlag) {
		this.outpathFlag = outpathFlag;
	}
	public String getOutpTypeCode() {
		return outpTypeCode;
	}
	public void setOutpTypeCode(String outpTypeCode) {
		this.outpTypeCode = outpTypeCode;
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
