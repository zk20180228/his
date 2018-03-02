package cn.honry.assets.deviceDossier.vo;

import java.util.Date;

public class DeviceDossierVo {
	/** 办公用途编码   **/
	private String officeCode;
	/** 办公用途名称   **/
	private String officeName;
	/** 设备分类编码   **/
	private String classCode;
	/** 设备分类名称   **/
	private String className;
	/** 设备条码号   **/
	private String deviceNo;
	/** 设备代码   **/
	private String deviceCode;
	/** 设备名称   **/
	private String deviceName;
	/** 计量单位   **/
	private String meterUnit;
	/**采购单价(元)*/
	private Double purchPrice = 0.0;
	/** 入库数量   **/
	private Integer deviceNum;
	/**采购总价(元)*/
	private Double purchTotal = 0.0;
	/** 折旧年限**/
	private Integer depreciation;
	/** 运费**/
	private Double tranCost= 0.0;
	/** 安装费**/
	private Double instCost= 0.0;
	/** 设备现值**/
	private Double newValue= 0.0;
	/** 入库时间   **/
	private Date deviceDate;
	
	public Date getDeviceDate() {
		return deviceDate;
	}
	public void setDeviceDate(Date deviceDate) {
		this.deviceDate = deviceDate;
	}
	public Double getNewValue() {
		return newValue;
	}
	public void setNewValue(Double newValue) {
		this.newValue = newValue;
	}
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getMeterUnit() {
		return meterUnit;
	}
	public void setMeterUnit(String meterUnit) {
		this.meterUnit = meterUnit;
	}
	public Double getPurchPrice() {
		return purchPrice;
	}
	public void setPurchPrice(Double purchPrice) {
		this.purchPrice = purchPrice;
	}
	public Integer getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(Integer deviceNum) {
		this.deviceNum = deviceNum;
	}
	public Double getPurchTotal() {
		return purchTotal;
	}
	public void setPurchTotal(Double purchTotal) {
		this.purchTotal = purchTotal;
	}
	public Integer getDepreciation() {
		return depreciation;
	}
	public void setDepreciation(Integer depreciation) {
		this.depreciation = depreciation;
	}
	public Double getTranCost() {
		return tranCost;
	}
	public void setTranCost(Double tranCost) {
		this.tranCost = tranCost;
	}
	public Double getInstCost() {
		return instCost;
	}
	public void setInstCost(Double instCost) {
		this.instCost = instCost;
	}
}
