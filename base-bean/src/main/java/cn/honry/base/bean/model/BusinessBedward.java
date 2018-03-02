package cn.honry.base.bean.model;

import java.math.BigDecimal;

import cn.honry.base.bean.business.Entity;

/**
 * AbstractTBusinessBedward entity provides the base persistence definition of
 * the TBusinessBedward entity. @author MyEclipse Persistence Tools
 * 病房维护
 */

public class BusinessBedward extends Entity  implements java.io.Serializable {

	// Fields
	/** 医院编号  **/
	private Hospital hospitalId;
	/** 病房编码*/
	private String bedwardCode;
	/** 病房编号 **/
	private String bedwardName;
	/** 拼音码  **/
	private String codePinyin;
	/** 五笔码  **/
	private String codeWb;
	/** 自定义码  **/
	private String codeInputcode;
	/** 护士站 **/
	private String nursestation;
	/** 护士站路径,读取部门表的path  **/
	private String path;
	/**额定床位数*/
	private Integer planbednum;	
	/**	开放床位数*/
	private Integer openbednum;
	/**排序**/
	private BigDecimal bedwardOrder;
	/** 默认选中  **/
	private boolean selected;
	
	public Hospital getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Hospital hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getBedwardCode() {
		return bedwardCode;
	}
	public void setBedwardCode(String bedwardCode) {
		this.bedwardCode = bedwardCode;
	}
	public String getBedwardName() {
		return bedwardName;
	}
	public void setBedwardName(String bedwardName) {
		this.bedwardName = bedwardName;
	}
	public String getCodePinyin() {
		return codePinyin;
	}
	public void setCodePinyin(String codePinyin) {
		this.codePinyin = codePinyin;
	}
	public String getCodeWb() {
		return codeWb;
	}
	public void setCodeWb(String codeWb) {
		this.codeWb = codeWb;
	}
	public String getCodeInputcode() {
		return codeInputcode;
	}
	public void setCodeInputcode(String codeInputcode) {
		this.codeInputcode = codeInputcode;
	}
	public String getNursestation() {
		return nursestation;
	}
	public void setNursestation(String nursestation) {
		this.nursestation = nursestation;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getPlanbednum() {
		return planbednum;
	}
	public void setPlanbednum(Integer planbednum) {
		this.planbednum = planbednum;
	}
	public Integer getOpenbednum() {
		return openbednum;
	}
	public void setOpenbednum(Integer openbednum) {
		this.openbednum = openbednum;
	}
	public BigDecimal getBedwardOrder() {
		return bedwardOrder;
	}
	public void setBedwardOrder(BigDecimal bedwardOrder) {
		this.bedwardOrder = bedwardOrder;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}