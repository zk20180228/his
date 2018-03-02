package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

@SuppressWarnings("serial")
public class OutpatientDrugcontrol extends Entity implements java.io.Serializable {
	/**摆药单分类编码*/
	private DrugBillclass drugBillclass;
	/**摆药科室*/
	private String deptCode;
	/**控制台编码*/
	private String controlCode;
	/**控制台名称*/
	private String controlName;
	/**摆药台属性G一般摆药，S特殊药品摆药，O出院带药摆药*/
	private String controlAttr;
	/**发送类型（1集中发送，2临时发送）*/
	private Integer sendType;
	/**备注*/
	private String mark;
	/**显示等级：0显示科室汇总，1显示科室明细，2显示患者明细*/
	private Integer showLevel;
	/**是否自动打印摆药单*/
	private Integer autoPrint;
	/**是否打印门诊标签 该参数只对出院带药摆药有效*/
	private Integer printLabel;
	/**摆药单是否需要预览 打印门诊标签时该字段无效*/
	private Integer billPreview;
	/**扩展字段*/
	private String extendFlag;
	/**扩展字段1*/
	private String extendFlag1;
	/**医院编码*/
	private Integer hospitalId;
	/**院区编码*/
	private String areaCode;
	
	public DrugBillclass getDrugBillclass() {
		return drugBillclass;
	}
	public void setDrugBillclass(DrugBillclass drugBillclass) {
		this.drugBillclass = drugBillclass;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getControlCode() {
		return controlCode;
	}
	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}
	public String getControlName() {
		return controlName;
	}
	public void setControlName(String controlName) {
		this.controlName = controlName;
	}
	public String getControlAttr() {
		return controlAttr;
	}
	public void setControlAttr(String controlAttr) {
		this.controlAttr = controlAttr;
	}
	public Integer getSendType() {
		return sendType;
	}
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public Integer getShowLevel() {
		return showLevel;
	}
	public void setShowLevel(Integer showLevel) {
		this.showLevel = showLevel;
	}
	public Integer getAutoPrint() {
		return autoPrint;
	}
	public void setAutoPrint(Integer autoPrint) {
		this.autoPrint = autoPrint;
	}
	public Integer getPrintLabel() {
		return printLabel;
	}
	public void setPrintLabel(Integer printLabel) {
		this.printLabel = printLabel;
	}
	public Integer getBillPreview() {
		return billPreview;
	}
	public void setBillPreview(Integer billPreview) {
		this.billPreview = billPreview;
	}
	public String getExtendFlag() {
		return extendFlag;
	}
	public void setExtendFlag(String extendFlag) {
		this.extendFlag = extendFlag;
	}
	public String getExtendFlag1() {
		return extendFlag1;
	}
	public void setExtendFlag1(String extendFlag1) {
		this.extendFlag1 = extendFlag1;
	}
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
}