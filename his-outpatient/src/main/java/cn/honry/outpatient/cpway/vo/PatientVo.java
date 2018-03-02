package cn.honry.outpatient.cpway.vo;

import java.io.Serializable;

/**
 * 
 * <p>患者Vo</p>
 * @Author: zhangkui
 * @CreateDate: 2017年12月25日 下午3:38:54 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年12月25日 下午3:38:54 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class PatientVo implements Serializable{

	private static final long serialVersionUID = -765013759865133076L;

	private String medicalrecord_id;//病历号
	private String patientName;//患者姓名
	
	public String getMedicalrecord_id() {
		return medicalrecord_id;
	}
	public void setMedicalrecord_id(String medicalrecord_id) {
		this.medicalrecord_id = medicalrecord_id;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	
	
}
