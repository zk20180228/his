package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class CpWay extends Entity {

	/**  
	 * 临床路径表
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月20日 上午11:24:34 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月20日 上午11:24:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;

	
	private String cpId	;//			临床路径ID
	private String cpName	;//			临床路径名称
	private String inputCode	;//	输入码
	private String inputCodeWb;//			五笔码
	private String customCode	;//			自定义码
	private String typeId	    ;//		分类ID
	private String memo	        ;//			简要说明
	private String caseType;//病理分类
	private String hospitalId	;//		所属医院
	private String areaCode	;//		所属院区
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public String getCpName() {
		return cpName;
	}
	public void setCpName(String cpName) {
		this.cpName = cpName;
	}
	public String getInputCode() {
		return inputCode;
	}
	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	public String getInputCodeWb() {
		return inputCodeWb;
	}
	public void setInputCodeWb(String inputCodeWb) {
		this.inputCodeWb = inputCodeWb;
	}
	public String getCustomCode() {
		return customCode;
	}
	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
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
