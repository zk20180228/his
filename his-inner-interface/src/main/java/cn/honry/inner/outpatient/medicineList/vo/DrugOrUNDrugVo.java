package cn.honry.inner.outpatient.medicineList.vo;

public class DrugOrUNDrugVo {
	/**
	 * 药品非药品编号
	 */
	private String drugOrUndrugCode;
	/**
	 * 是否需要预约 0不需要确认   1 需要确认
	 */
	private Integer issubmit;
	/**
	 * 非药品是否需要预约
	 */
	private Integer ispreorder;
	/**
	 * '1'表示药品、‘0’表示非药品
	 */
	private Integer isdrug;
	/**
	 * 设备编号 由于药品没有设备编号，这里使用药品的code
	 */
	private String  equipmentNO;
	
	
	public Integer getIssubmit() {
		return issubmit;
	}
	public void setIssubmit(Integer issubmit) {
		this.issubmit = issubmit;
	}
	public String getDrugOrUndrugCode() {
		return drugOrUndrugCode;
	}
	public void setDrugOrUndrugCode(String drugOrUndrugCode) {
		this.drugOrUndrugCode = drugOrUndrugCode;
	}
	public Integer getIsdrug() {
		return isdrug;
	}
	public void setIsdrug(Integer isdrug) {
		this.isdrug = isdrug;
	}
	public Integer getIspreorder() {
		return ispreorder;
	}
	public void setIspreorder(Integer ispreorder) {
		this.ispreorder = ispreorder;
	}
	public String getEquipmentNO() {
		return equipmentNO;
	}
	public void setEquipmentNO(String equipmentNO) {
		this.equipmentNO = equipmentNO;
	}
	
	
	
}
