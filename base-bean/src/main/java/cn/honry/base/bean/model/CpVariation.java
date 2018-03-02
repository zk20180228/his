package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class CpVariation extends Entity {

	/**  
	 * 变异信息记录
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月15日 下午1:45:30 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月15日 下午1:45:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	private String inpatientNo	    ;//		住院流水号
	private String medicalrecordId	;//		病历号
	private String stageId	        ;//	阶段ID
	private Date variationDate	    ;//变异时间
	private String variationCode	;//	变异代码
	private String variationDirection;//	变异方向
	private String hospitalId	     ;//		所属医院
	private String areaCode	         ;//		所属院区
	private Date ececuteDate;//执行时间
	private String variationReason; //变异原因
	private String variationFactorCode;//变异因素代码
	
	public String getVariationReason() {
		return variationReason;
	}
	public void setVariationReason(String variationReason) {
		this.variationReason = variationReason;
	}
	public String getVariationFactorCode() {
		return variationFactorCode;
	}
	public void setVariationFactorCode(String variationFactorCode) {
		this.variationFactorCode = variationFactorCode;
	}
	public Date getEcecuteDate() {
		return ececuteDate;
	}
	public void setEcecuteDate(Date ececuteDate) {
		this.ececuteDate = ececuteDate;
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
	public String getStageId() {
		return stageId;
	}
	public void setStageId(String stageId) {
		this.stageId = stageId;
	}
	public Date getVariationDate() {
		return variationDate;
	}
	public void setVariationDate(Date variationDate) {
		this.variationDate = variationDate;
	}
	public String getVariationCode() {
		return variationCode;
	}
	public void setVariationCode(String variationCode) {
		this.variationCode = variationCode;
	}
	public String getVariationDirection() {
		return variationDirection;
	}
	public void setVariationDirection(String variationDirection) {
		this.variationDirection = variationDirection;
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
