package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;


@SuppressWarnings("serial")
public class DrugBillclass extends Entity implements java.io.Serializable {
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
	/**摆药台ID*/
	private String controlId;
	
	
	public String getControlId() {
		return controlId;
	}

	public void setControlId(String controlId) {
		this.controlId = controlId;
	}

	public String getBillclassCode() {
		return this.billclassCode;
	}

	public void setBillclassCode(String billclassCode) {
		this.billclassCode = billclassCode;
	}

	public String getBillclassName() {
		return this.billclassName;
	}

	public void setBillclassName(String billclassName) {
		this.billclassName = billclassName;
	}

	public String getBillclassAttr() {
		return this.billclassAttr;
	}

	public void setBillclassAttr(String billclassAttr) {
		this.billclassAttr = billclassAttr;
	}

	public String getPrintType() {
		return this.printType;
	}

	public void setPrintType(String printType) {
		this.printType = printType;
	}

	public Integer getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}

	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getMark() {
		return this.mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
}