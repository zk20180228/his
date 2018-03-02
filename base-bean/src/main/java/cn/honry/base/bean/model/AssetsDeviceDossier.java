package cn.honry.base.bean.model;
import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：  设备档案管理
 * @Author：zpty
 * @CreateDate：2017-11-14 上午09:35:05  
 *
 */
public class AssetsDeviceDossier extends Entity{

	
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
	/** 领用部门编码 **/
	private String useDeptCode;
	/** 领用部门名称 **/
	private String useDeptName;
	/** 领用人工号 **/
	private String useAcc;
	/** 领用人姓名 **/
	private String useName;
	/** 维修次数   **/
	private Integer maintainNum=0;
	
	/** 页数   **/
	private String page;
	/**  每页行数  **/
	private String rows;
	
	//新增字段
	/**  领用数量 --只用来做中间变量,不存入数据库**/
	private Integer useNum;
	/**  状态(0正常1维修2废弃)  **/
	private Integer state=0;
	
	
	public Integer getMaintainNum() {
		return maintainNum;
	}
	public void setMaintainNum(Integer maintainNum) {
		this.maintainNum = maintainNum;
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
	public String getUseDeptCode() {
		return useDeptCode;
	}
	public void setUseDeptCode(String useDeptCode) {
		this.useDeptCode = useDeptCode;
	}
	public String getUseDeptName() {
		return useDeptName;
	}
	public void setUseDeptName(String useDeptName) {
		this.useDeptName = useDeptName;
	}
	public String getUseAcc() {
		return useAcc;
	}
	public void setUseAcc(String useAcc) {
		this.useAcc = useAcc;
	}
	public String getUseName() {
		return useName;
	}
	public void setUseName(String useName) {
		this.useName = useName;
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
	public Integer getUseNum() {
		return useNum;
	}
	public void setUseNum(Integer useNum) {
		this.useNum = useNum;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
}