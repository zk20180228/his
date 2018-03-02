package cn.honry.inpatient.bill.vo;

public class OutpatientDrugcontrolDrugbillclass {
	/**摆药单分类代码*/
	private String billclassCode;
	/**摆药单分类名称*/
	private String billclassName;
	/**摆药单属性O一般摆药，T特殊药品摆药，R出院带药摆药*/
	private String billclassAttr;
	/**打印类型T汇总D明细H草药R出院带药*/
	private String printType;
	/**是否有效－1有效，0无效*/
	private Integer validFlag;
	/**摆药科室编码，AAAA表示全院*/
	private String deptCode;
	/**备注*/
	private String mark;
	/**摆药单名称*/
	private String ControlName;
	public String getBillclassCode() {
		return billclassCode;
	}
	public void setBillclassCode(String billclassCode) {
		this.billclassCode = billclassCode;
	}
	public String getBillclassName() {
		return billclassName;
	}
	public void setBillclassName(String billclassName) {
		this.billclassName = billclassName;
	}
	public String getBillclassAttr() {
		return billclassAttr;
	}
	public void setBillclassAttr(String billclassAttr) {
		this.billclassAttr = billclassAttr;
	}
	public String getPrintType() {
		return printType;
	}
	public void setPrintType(String printType) {
		this.printType = printType;
	}
	public Integer getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getControlName() {
		return ControlName;
	}
	public void setControlName(String controlName) {
		ControlName = controlName;
	}
	
	
}
