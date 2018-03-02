package cn.honry.base.bean.model;

import java.sql.Clob;

import cn.honry.base.bean.business.Entity;

public class OaFormInfo extends Entity{

	private static final long serialVersionUID = 1L;
	
	private String formCode;
	private String formName;
	private String formTname;
	private String formDesc;
	private Clob formInfo;
	private Integer formState;
	private String formMula;
	
	private String formInfoStr;
	
	public String getFormCode() {
		return formCode;
	}
	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getFormTname() {
		return formTname;
	}
	public void setFormTname(String formTname) {
		this.formTname = formTname;
	}
	public String getFormDesc() {
		return formDesc;
	}
	public void setFormDesc(String formDesc) {
		this.formDesc = formDesc;
	}
	public Clob getFormInfo() {
		return formInfo;
	}
	public void setFormInfo(Clob formInfo) {
		this.formInfo = formInfo;
	}
	public Integer getFormState() {
		return formState;
	}
	public void setFormState(Integer formState) {
		this.formState = formState;
	}
	public String getFormInfoStr() {
		return formInfoStr;
	}
	public void setFormInfoStr(String formInfoStr) {
		this.formInfoStr = formInfoStr;
	}
	public String getFormMula() {
		return formMula;
	}
	public void setFormMula(String formMula) {
		this.formMula = formMula;
	}
	
}
