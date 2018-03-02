package cn.honry.assets.assetsPurch.vo;
import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：  设备采购计划管理
 * @Author：zpty
 * @CreateDate：2017-11-14 上午09:35:05  
 *
 */
public class AssetsPurchVo extends Entity{

	
	private static final long serialVersionUID = 1L;
	/** 办公用途名称编码   **/
	private String officeCode;
	/** 办公用途名称   **/
	private String officeName;
	/** 设备分类编码   **/
	private String classCode;
	/** 设备分类名称   **/
	private String className;
	/** 设备名称编码   **/
	private String deviceCode;
	/** 设备名称   **/
	private String deviceName;
	/** 计量单位   **/
	private String meterUnit;
	/**计划单价(元)*/
	private Double planPrice = 0.0;
	/** 计划数量   **/
	private Integer planNum;
	/** 采购数量   **/
	private Integer purchNum;
	//新加
	/** 申报人姓名 **/
	private String applName;
	/**计划总价(元)*/
	private Double planPriceTotal = 0.0;
	/**完成情况*/
	private String condition;
	
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getApplName() {
		return applName;
	}
	public void setApplName(String applName) {
		this.applName = applName;
	}
	public Double getPlanPriceTotal() {
		return planPriceTotal;
	}
	public void setPlanPriceTotal(Double planPriceTotal) {
		this.planPriceTotal = planPriceTotal;
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
	public Double getPlanPrice() {
		return planPrice;
	}
	public void setPlanPrice(Double planPrice) {
		this.planPrice = planPrice;
	}
	public Integer getPlanNum() {
		return planNum;
	}
	public void setPlanNum(Integer planNum) {
		this.planNum = planNum;
	}
	public Integer getPurchNum() {
		return purchNum;
	}
	public void setPurchNum(Integer purchNum) {
		this.purchNum = purchNum;
	}
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public String getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
}