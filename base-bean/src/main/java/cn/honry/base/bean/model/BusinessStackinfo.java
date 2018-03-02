package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;


public class BusinessStackinfo extends Entity {

	/*组套编号（外键）*/
	private BusinessStack stackId;
	/*项目编号*/
	private String stackInfoItemId;
	/*是否药品*/
	private Integer isDrug;
	/*价格*/
	//private Double stackInfoFee;
	/*开立数量*/
	private Integer stackInfoNum;
	/*单位*/
	private String stackInfoUnit;
	/*执行科室*/
	private String stackInfoDeptid;
	/*备注*/
	private String stackInfoRemark;
	/*排序*/
	private Integer stackInfoOrder;
	/*组合流水号*/
	private String combNo;
	/*医嘱类型*/
	private String typeCode;
	/*服药频次*/
	private String frequencyCode;
	/*服药方法*/
	private String usageCode;
	/*每次服用剂量*/
	private Double onceDose;
	/*剂量单位，自备药使用*/
	private String doseUnit;
	/*草药付数(周期)*/
	private Integer days;
	/*主药标记*/
	private Integer mainDrug;
	/*检查部位*/
	private String itemNote;
	/*医嘱开始时间*/
	private Date dateBgn;
	/*医嘱结束时间*/
	private Date dateEnd;
	/*医嘱备注*/
	private String remark;
	/*药品组合医嘱备注*/
	private String remarkComb;
	/*间隔天数*/
	private String intervaldays;
	/*默认价 */
	private Double defaultprice;
	/*儿童价 **/
	private Double childrenprice;
	/*特诊价 */
	private Double specialprice;
	//数据库无关字段
	private String stackInfoItemIdShow;
	private String mainDrugshow;
	private String isDrugShow;
	private String stackInfoItemName;
	private String drugPackagingunit;
	private String unit;
	
	public String getDrugPackagingunit() {
		return drugPackagingunit;
	}

	public void setDrugPackagingunit(String drugPackagingunit) {
		this.drugPackagingunit = drugPackagingunit;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getStackInfoItemId() {
		return stackInfoItemId;
	}

	public String getCombNo() {
		return combNo;
	}

	public void setCombNo(String combNo) {
		this.combNo = combNo;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getFrequencyCode() {
		return frequencyCode;
	}

	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}

	public String getUsageCode() {
		return usageCode;
	}

	public void setUsageCode(String usageCode) {
		this.usageCode = usageCode;
	}

	public String getDoseUnit() {
		return doseUnit;
	}

	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Integer getMainDrug() {
		return mainDrug;
	}

	public void setMainDrug(Integer mainDrug) {
		this.mainDrug = mainDrug;
	}

	public String getItemNote() {
		return itemNote;
	}

	public void setItemNote(String itemNote) {
		this.itemNote = itemNote;
	}

	public Date getDateBgn() {
		return dateBgn;
	}

	public void setDateBgn(Date dateBgn) {
		this.dateBgn = dateBgn;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemarkComb() {
		return remarkComb;
	}

	public void setRemarkComb(String remarkComb) {
		this.remarkComb = remarkComb;
	}

	public String getIntervaldays() {
		return intervaldays;
	}

	public void setIntervaldays(String intervaldays) {
		this.intervaldays = intervaldays;
	}

	public void setStackInfoItemId(String stackInfoItemId) {
		this.stackInfoItemId = stackInfoItemId;
	}

	public Integer getIsDrug() {
		return isDrug;
	}

	public void setIsDrug(Integer isDrug) {
		this.isDrug = isDrug;
	}

	public Integer getStackInfoNum() {
		return stackInfoNum;
	}

	public void setStackInfoNum(Integer stackInfoNum) {
		this.stackInfoNum = stackInfoNum;
	}

	public String getStackInfoUnit() {
		return stackInfoUnit;
	}

	public void setStackInfoUnit(String stackInfoUnit) {
		this.stackInfoUnit = stackInfoUnit;
	}

	public String getStackInfoDeptid() {
		return stackInfoDeptid;
	}

	public void setStackInfoDeptid(String stackInfoDeptid) {
		this.stackInfoDeptid = stackInfoDeptid;
	}

	public String getStackInfoRemark() {
		return stackInfoRemark;
	}

	public void setStackInfoRemark(String stackInfoRemark) {
		this.stackInfoRemark = stackInfoRemark;
	}

	public BusinessStack getStackId() {
		return stackId;
	}

	public void setStackId(BusinessStack stackId) {
		this.stackId = stackId;
	}

	public Integer getStackInfoOrder() {
		return stackInfoOrder;
	}

	public void setStackInfoOrder(Integer stackInfoOrder) {
		this.stackInfoOrder = stackInfoOrder;
	}

	public Double getOnceDose() {
		return onceDose;
	}

	public void setOnceDose(Double onceDose) {
		this.onceDose = onceDose;
	}

	public void setDefaultprice(Double defaultprice) {
		this.defaultprice = defaultprice;
	}

	public Double getDefaultprice() {
		return defaultprice;
	}

	public void setChildrenprice(Double childrenprice) {
		this.childrenprice = childrenprice;
	}

	public Double getChildrenprice() {
		return childrenprice;
	}

	public void setSpecialprice(Double specialprice) {
		this.specialprice = specialprice;
	}

	public Double getSpecialprice() {
		return specialprice;
	}

	public void setStackInfoItemIdShow(String stackInfoItemIdShow) {
		this.stackInfoItemIdShow = stackInfoItemIdShow;
	}

	public String getStackInfoItemIdShow() {
		return stackInfoItemIdShow;
	}

	public void setMainDrugshow(String mainDrugshow) {
		this.mainDrugshow = mainDrugshow;
	}

	public String getMainDrugshow() {
		return mainDrugshow;
	}

	public void setIsDrugShow(String isDrugShow) {
		this.isDrugShow = isDrugShow;
	}

	public String getIsDrugShow() {
		return isDrugShow;
	}

	public String getStackInfoItemName() {
		return stackInfoItemName;
	}

	public void setStackInfoItemName(String stackInfoItemName) {
		this.stackInfoItemName = stackInfoItemName;
	}

}