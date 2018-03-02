package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class BusinessIcdO extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**诊断码**/
	private String code;
	/**诊断名称**/
	private String name;
	/**诊断别名1**/
	private String alias;
	/**诊断别名2**/
	private String alias2;
	/**医保诊断编码**/
	private String dsCode;
	/**附加码**/
	private String addcode;
	/**拼音码**/
	private String pinyin;
	/**五笔码**/
	private String wb;
	/**自定义码**/
	private String inputcode;
	/**分类: 0：公共 1 DR特有2FZ特有**/
	private Integer type=0;
	/**疾病死亡原因**/
	private String diereason;
	/**标准住院日**/
	private Integer standarddate;
	/**住院等级**/
	private String inpgrade;
	/**疾病分类:从编码表读取**/
	private String diseasetype;
	/**适用性别:A全部M男F女**/
	private String sex;
	/**是否30中疾病**/
	private Integer isThirty=0;
	/**是否传染病**/
	private Integer isCom=0;
	/**是否肿瘤**/
	private Integer isTumor=0;
	/**是否中医诊断**/
	private Integer isTcm=0;
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
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getAlias2() {
		return alias2;
	}
	public void setAlias2(String alias2) {
		this.alias2 = alias2;
	}
	public String getDsCode() {
		return dsCode;
	}
	public void setDsCode(String dsCode) {
		this.dsCode = dsCode;
	}
	public String getAddcode() {
		return addcode;
	}
	public void setAddcode(String addcode) {
		this.addcode = addcode;
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
	public String getDiereason() {
		return diereason;
	}
	public void setDiereason(String diereason) {
		this.diereason = diereason;
	}
	public Integer getStandarddate() {
		return standarddate;
	}
	public void setStandarddate(Integer standarddate) {
		this.standarddate = standarddate;
	}
	public String getInpgrade() {
		return inpgrade;
	}
	public void setInpgrade(String inpgrade) {
		this.inpgrade = inpgrade;
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