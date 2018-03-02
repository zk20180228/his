package cn.honry.outpatient.advice.vo;

public class InpatientInfoVo{
	/**医嘱类型**/
	private String typeName;
	/**医嘱名称**/
	private String itemName;
	/**组**/
	private String combNo;
	/**总量**/
	private Double qtyTot;
	/**单位（总量单位）**/
	private String priceUnit;
	/**包装单位**/
	private String drugpackagingUnit;
	/**每次剂量**/
	private String doseOnce;
	/**单位（剂量单位）**/
	private String doseUnit;
	/**每次量(每次剂量+剂量单位)**/
	private Double doseOnces;
	/**付数**/
	private Double useDays;
	/**频次**/
	private String frequencyName;
	/**用法名称**/
	private String useName;
	/**开始时间**/
	private String dateBgn;
	/**停止时间**/
	private String dateEnd;
	/**开立时间**/
	private String moDate;
	/**开立医生**/
	private String docName;
	/**执行科室**/
	private String execDpnm;
	/**加急**/
	private Integer isUrgent;
	/**样本类型**/
	private String labCode;
	/**检查部位**/
	private String itemNote;
	/**扣库科室（取药药房）**/
	private String pharmacyCode;
	/**备注**/
	private String moNote2;
	/**录入人**/
	private String recUsernm;
	/**开立科室**/
	private String listDpcdName;
	/**停止人**/
	private String dcUsernm;
	/**皮试**/
	private Integer hypotest;
	/**顺序号**/
	private Integer sortId;
	/**getters and setters**/
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getCombNo() {
		return combNo;
	}
	public void setCombNo(String combNo) {
		this.combNo = combNo;
	}
	public Double getQtyTot() {
		return qtyTot;
	}
	public void setQtyTot(Double qtyTot) {
		this.qtyTot = qtyTot;
	}
	public String getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}
	public String getDrugpackagingUnit() {
		return drugpackagingUnit;
	}
	public void setDrugpackagingUnit(String drugpackagingUnit) {
		this.drugpackagingUnit = drugpackagingUnit;
	}
	public String getDoseOnce() {
		return doseOnce;
	}
	public void setDoseOnce(String doseOnce) {
		this.doseOnce = doseOnce;
	}
	public String getDoseUnit() {
		return doseUnit;
	}
	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}
	public Double getDoseOnces() {
		return doseOnces;
	}
	public void setDoseOnces(Double doseOnces) {
		this.doseOnces = doseOnces;
	}
	public Double getUseDays() {
		return useDays;
	}
	public void setUseDays(Double useDays) {
		this.useDays = useDays;
	}
	public String getFrequencyName() {
		return frequencyName;
	}
	public void setFrequencyName(String frequencyName) {
		this.frequencyName = frequencyName;
	}
	public String getUseName() {
		return useName;
	}
	public void setUseName(String useName) {
		this.useName = useName;
	}
	public String getDateBgn() {
		return dateBgn;
	}
	public void setDateBgn(String dateBgn) {
		this.dateBgn = dateBgn;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getMoDate() {
		return moDate;
	}
	public void setMoDate(String moDate) {
		this.moDate = moDate;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getExecDpnm() {
		return execDpnm;
	}
	public void setExecDpnm(String execDpnm) {
		this.execDpnm = execDpnm;
	}
	public Integer getIsUrgent() {
		return isUrgent;
	}
	public void setIsUrgent(Integer isUrgent) {
		this.isUrgent = isUrgent;
	}
	public String getLabCode() {
		return labCode;
	}
	public void setLabCode(String labCode) {
		this.labCode = labCode;
	}
	public String getItemNote() {
		return itemNote;
	}
	public void setItemNote(String itemNote) {
		this.itemNote = itemNote;
	}
	public String getPharmacyCode() {
		return pharmacyCode;
	}
	public void setPharmacyCode(String pharmacyCode) {
		this.pharmacyCode = pharmacyCode;
	}
	public String getMoNote2() {
		return moNote2;
	}
	public void setMoNote2(String moNote2) {
		this.moNote2 = moNote2;
	}
	public String getRecUsernm() {
		return recUsernm;
	}
	public void setRecUsernm(String recUsernm) {
		this.recUsernm = recUsernm;
	}
	public String getListDpcdName() {
		return listDpcdName;
	}
	public void setListDpcdName(String listDpcdName) {
		this.listDpcdName = listDpcdName;
	}
	public String getDcUsernm() {
		return dcUsernm;
	}
	public void setDcUsernm(String dcUsernm) {
		this.dcUsernm = dcUsernm;
	}
	public Integer getHypotest() {
		return hypotest;
	}
	public void setHypotest(Integer hypotest) {
		this.hypotest = hypotest;
	}
	public Integer getSortId() {
		return sortId;
	}
	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}
	
	
}
