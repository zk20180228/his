package cn.honry.base.bean.model;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：  设备采购管理
 * @Author：zpty
 * @CreateDate：2017-11-14 上午09:35:05  
 *
 */
public class AssetsPurch extends Entity{

	
	private static final long serialVersionUID = 1L;

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
	/** 采购数量   **/
	private Integer purchNum;
	/**运费(元)*/
	private Double tranCost = 0.0;
	/**安装费(元)*/
	private Double instCost = 0.0;
	/**采购总价(元)*/
	private Double purchTotal = 0.0;
	/** 申报人工号 **/
	private String applAcc;
	/** 申报人姓名 **/
	private String applName;
	/** 申报时间 **/
	private Date applDate;
	/** 申报状态(0草稿1申请,待审核2未批准3已申报) **/
	private Integer applState;
	/** 审核未通过的原因 **/
	private String reason;
	
	/** 页数   **/
	private String page;
	/**  每页行数  **/
	private String rows;
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
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
	public Integer getPurchNum() {
		return purchNum;
	}
	public void setPurchNum(Integer purchNum) {
		this.purchNum = purchNum;
	}
	public Double getInstCost() {
		return instCost;
	}
	public void setInstCost(Double instCost) {
		this.instCost = instCost;
	}
	public Double getPurchTotal() {
		return purchTotal;
	}
	public void setPurchTotal(Double purchTotal) {
		this.purchTotal = purchTotal;
	}
	public String getApplAcc() {
		return applAcc;
	}
	public void setApplAcc(String applAcc) {
		this.applAcc = applAcc;
	}
	public String getApplName() {
		return applName;
	}
	public void setApplName(String applName) {
		this.applName = applName;
	}
	public Date getApplDate() {
		return applDate;
	}
	public void setApplDate(Date applDate) {
		this.applDate = applDate;
	}
	public Integer getApplState() {
		return applState;
	}
	public void setApplState(Integer applState) {
		this.applState = applState;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Double getTranCost() {
		return tranCost;
	}
	public void setTranCost(Double tranCost) {
		this.tranCost = tranCost;
	}
	
}