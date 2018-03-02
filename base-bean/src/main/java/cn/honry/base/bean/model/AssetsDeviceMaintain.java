package cn.honry.base.bean.model;
import java.util.Date;
import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：  设备维修管理
 * @Author：zpty
 * @CreateDate：2017-11-14 上午09:35:05  
 *
 */
public class AssetsDeviceMaintain extends Entity{

	
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
	/** 维修次数   **/
	private Integer maintainNum;
	/** 申请科室编码 **/
	private String applDeptCode;
	/** 申请科室名称 **/
	private String applDeptName;
	/** 申报人工号 **/
	private String applAcc;
	/** 申报人姓名 **/
	private String applName;
	/** 申报时间 **/
	private Date applDate;
	/**维修原因**/
	private String repairReson;
	
	/** 页数   **/
	private String page;
	/**  每页行数  **/
	private String rows;
	/** 数据库没有字段  0正常 1已维修 **/
	private String state;
	
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRepairReson() {
		return repairReson;
	}
	public void setRepairReson(String repairReson) {
		this.repairReson = repairReson;
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
	public Integer getMaintainNum() {
		return maintainNum;
	}
	public void setMaintainNum(Integer maintainNum) {
		this.maintainNum = maintainNum;
	}
	public String getApplDeptCode() {
		return applDeptCode;
	}
	public void setApplDeptCode(String applDeptCode) {
		this.applDeptCode = applDeptCode;
	}
	public String getApplDeptName() {
		return applDeptName;
	}
	public void setApplDeptName(String applDeptName) {
		this.applDeptName = applDeptName;
	}
	
}