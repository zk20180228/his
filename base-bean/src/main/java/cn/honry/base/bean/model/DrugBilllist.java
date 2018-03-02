package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

@SuppressWarnings("serial")
public class DrugBilllist extends Entity implements java.io.Serializable {
	/**摆药单分类编码*/
	private DrugBillclass drugBillclass;
	/**医嘱类别*/
	private String typeCode;
	/**用法代码*/
	private String usageCode;
	/**药品类别:1西药、2中成药、3中草药*/
	private String drugType;
	/**药品性质*/
	private String drugQuality;
	/**剂型代码*/
	private String doseModelCode;
	/**医嘱状态1长期/2临时/3全部*/
	private Integer ipmState;
	
	public DrugBillclass getDrugBillclass() {
		return drugBillclass;
	}
	public void setDrugBillclass(DrugBillclass drugBillclass) {
		this.drugBillclass = drugBillclass;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getUsageCode() {
		return usageCode;
	}
	public void setUsageCode(String usageCode) {
		this.usageCode = usageCode;
	}
	public String getDrugType() {
		return drugType;
	}
	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}
	public String getDrugQuality() {
		return drugQuality;
	}
	public void setDrugQuality(String drugQuality) {
		this.drugQuality = drugQuality;
	}
	public String getDoseModelCode() {
		return doseModelCode;
	}
	public void setDoseModelCode(String doseModelCode) {
		this.doseModelCode = doseModelCode;
	}
	public Integer getIpmState() {
		return ipmState;
	}
	public void setIpmState(Integer ipmState) {
		this.ipmState = ipmState;
	}

}