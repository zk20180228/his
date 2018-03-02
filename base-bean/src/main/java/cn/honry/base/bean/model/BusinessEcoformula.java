package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class BusinessEcoformula extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//就诊记录或就诊卡
	private  String clinicCode;
	//优惠准则标志 0 对人 1 对就诊记录
	private  Integer ecoFlag;
	//合同单位   1 享受 0 不享受
	private  Integer pactcodeFlag;
	//单病种标志 1 享受 0 不享受
	private  Integer icdcodeFlag;
	//时段标志    1 享受 0 不享受
	private  Integer dateFlag;
	//优惠原则关系 0 取最大优惠 1 取并优惠
	private  Integer ecorealFlag;
	//特殊规则公式
	private  String specilFormula;
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public Integer getEcoFlag() {
		return ecoFlag;
	}
	public void setEcoFlag(Integer ecoFlag) {
		this.ecoFlag = ecoFlag;
	}
	public Integer getPactcodeFlag() {
		return pactcodeFlag;
	}
	public void setPactcodeFlag(Integer pactcodeFlag) {
		this.pactcodeFlag = pactcodeFlag;
	}
	public Integer getIcdcodeFlag() {
		return icdcodeFlag;
	}
	public void setIcdcodeFlag(Integer icdcodeFlag) {
		this.icdcodeFlag = icdcodeFlag;
	}
	public Integer getDateFlag() {
		return dateFlag;
	}
	public void setDateFlag(Integer dateFlag) {
		this.dateFlag = dateFlag;
	}
	public Integer getEcorealFlag() {
		return ecorealFlag;
	}
	public void setEcorealFlag(Integer ecorealFlag) {
		this.ecorealFlag = ecorealFlag;
	}
	public String getSpecilFormula() {
		return specilFormula;
	}
	public void setSpecilFormula(String specilFormula) {
		this.specilFormula = specilFormula;
	}
	
}
