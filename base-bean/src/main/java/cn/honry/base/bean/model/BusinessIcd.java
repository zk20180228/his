package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class BusinessIcd extends Entity {
	/**医院**/
	private Hospital hospital;
	/**诊断码**/
	private String code;
	/**诊断名称**/
	private String name;
	/**拼音码**/
	private String pinyin;
	/**五笔码**/
	private String wb;
	/**自定义码**/
	private String inputcode;
	/**分类 1:ICD10 2:ICD9 3:ICDOperation**/
	private Integer type;
	/**疾病分类:从编码表读取**/
	private String diseasetype;
	/**适用性别:从编码表读取**/
	private String sex;
	/**是否30中疾病**/
	private Integer isThirty=0;
	/**是否传染病**/
	private Integer isCom=0;
	/**是否肿瘤**/
	private Integer isTumor=0;
	/**是否中医诊断**/
	private Integer isTcm=0;
	
	public Hospital getHospital() {
		return hospital;
	}
	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getWb() {
		return wb;
	}
	public void setWb(String wb) {
		this.wb = wb;
	}
	public String getInputcode() {
		return inputcode;
	}
	public void setInputcode(String inputcode) {
		this.inputcode = inputcode;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getDiseasetype() {
		return diseasetype;
	}
	public void setDiseasetype(String diseasetype) {
		this.diseasetype = diseasetype;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getIsThirty() {
		return isThirty;
	}
	public void setIsThirty(Integer isThirty) {
		this.isThirty = isThirty;
	}
	public Integer getIsCom() {
		return isCom;
	}
	public void setIsCom(Integer isCom) {
		this.isCom = isCom;
	}
	public Integer getIsTumor() {
		return isTumor;
	}
	public void setIsTumor(Integer isTumor) {
		this.isTumor = isTumor;
	}
	public Integer getIsTcm() {
		return isTcm;
	}
	public void setIsTcm(Integer isTcm) {
		this.isTcm = isTcm;
	}

}