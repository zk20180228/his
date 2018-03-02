package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 
 * @author conglin
 * 虚拟科室关系表
 */
public class FictitiousDept extends Entity{
	private Integer hospitalId;
	private String deptId;
	/**名称**/
	private String deptName;
	/**虚拟科室code**/
	private String deptCode;
	/**简称**/
	private String deptBrev;
	/**英文名**/
	private String deptEname;
	/**所属院区,1:河西院区,2:郑东院区,3:惠济院区**/
	private Integer deptDistrict;
	/**所属院区,1:河西院区,2:郑东院区,3:惠济院区**/
	private String deptDistrictName;
	/**拼音码**/
	private String deptPinYin;
	/**五笔码**/
	private String deptWb;
	/**自定义码**/
	private String deptInputCode;
	/**上级**/
	private String deptParent;
	/**是否有下级**/
	private int deptHasson;
	/**层级**/
	private Integer deptLevel;
	/**排序**/
	private Integer deptOrder;
	/**他的所有的上级，用逗号隔开，包括他自身**/
	private String deptUppath;
	/**如果父级=0，则为排序号左边补充0，扩展为8位，再在后面—加一个“,”；
父级<>0，则为父级的Dept_PATH+排序号左边补充0，扩展为8位，再在后面—加一个“,”；
**/
	private String deptPath;
	/**C-门诊, I-住院, F-财务，L-后勤，PI-药库，T-医技(终端)，0-其它，D-机关(部门)，P-药房，N-护士站 ，S-科研,O-其他,OP-手术,U-自定义**/
	private String deptType;
	/**从编码表获取**/
	private String deptProperty;
	/**1:是0否**/
	private int deptIsforregister;
	/**挂号顺序号，如果是挂号部门，可以填这个字段，非挂号不用填 **/
	private String deptRegisterNo;
	/**是否核算部门   1:是0否**/
	private int deptIsforaccounting;
	/**备注**/
	private String deptRemark;

	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptBrev() {
		return deptBrev;
	}
	public void setDeptBrev(String deptBrev) {
		this.deptBrev = deptBrev;
	}
	public String getDeptEname() {
		return deptEname;
	}
	public void setDeptEname(String deptEname) {
		this.deptEname = deptEname;
	}
	public Integer getDeptDistrict() {
		return deptDistrict;
	}
	public void setDeptDistrict(Integer deptDistrict) {
		this.deptDistrict = deptDistrict;
	}
	public String getDeptPinYin() {
		return deptPinYin;
	}
	public void setDeptPinYin(String deptPinYin) {
		this.deptPinYin = deptPinYin;
	}
	public String getDeptWb() {
		return deptWb;
	}
	public void setDeptWb(String deptWb) {
		this.deptWb = deptWb;
	}
	public String getDeptInputCode() {
		return deptInputCode;
	}
	public void setDeptInputCode(String deptInputCode) {
		this.deptInputCode = deptInputCode;
	}
	public String getDeptParent() {
		return deptParent;
	}
	public void setDeptParent(String deptParent) {
		this.deptParent = deptParent;
	}
	public int getDeptHasson() {
		return deptHasson;
	}
	public void setDeptHasson(int deptHasson) {
		this.deptHasson = deptHasson;
	}
	public Integer getDeptLevel() {
		return deptLevel;
	}
	public void setDeptLevel(Integer deptLevel) {
		this.deptLevel = deptLevel;
	}
	public Integer getDeptOrder() {
		return deptOrder;
	}
	public void setDeptOrder(Integer deptOrder) {
		this.deptOrder = deptOrder;
	}
	public String getDeptUppath() {
		return deptUppath;
	}
	public void setDeptUppath(String deptUppath) {
		this.deptUppath = deptUppath;
	}
	public String getDeptPath() {
		return deptPath;
	}
	public void setDeptPath(String deptPath) {
		this.deptPath = deptPath;
	}
	public String getDeptType() {
		return deptType;
	}
	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}
	public String getDeptProperty() {
		return deptProperty;
	}
	public void setDeptProperty(String deptProperty) {
		this.deptProperty = deptProperty;
	}
	public int getDeptIsforregister() {
		return deptIsforregister;
	}
	public void setDeptIsforregister(int deptIsforregister) {
		this.deptIsforregister = deptIsforregister;
	}
	public String getDeptRegisterNo() {
		return deptRegisterNo;
	}
	public void setDeptRegisterNo(String deptRegisterNo) {
		this.deptRegisterNo = deptRegisterNo;
	}
	public int getDeptIsforaccounting() {
		return deptIsforaccounting;
	}
	public void setDeptIsforaccounting(int deptIsforaccounting) {
		this.deptIsforaccounting = deptIsforaccounting;
	}
	public String getDeptRemark() {
		return deptRemark;
	}
	public void setDeptRemark(String deptRemark) {
		this.deptRemark = deptRemark;
	}
	
	public String getDeptDistrictName() {
		return deptDistrictName;
	}
	public void setDeptDistrictName(String deptDistrictName) {
		this.deptDistrictName = deptDistrictName;
	}
	@Override
	public String toString() {
		return "FictitiousDept [hospitalId=" + hospitalId + ", deptId="
				+ deptId + ", deptName=" + deptName + ", deptCode=" + deptCode
				+ ", deptBrev=" + deptBrev + ", deptEname=" + deptEname
				+ ", deptDistrict=" + deptDistrict + ", deptPinYin="
				+ deptPinYin + ", deptWb=" + deptWb + ", deptInputCode="
				+ deptInputCode + ", deptParent=" + deptParent
				+ ", deptHasson=" + deptHasson + ", deptLevel=" + deptLevel
				+ ", deptOrder=" + deptOrder + ", deptUppath=" + deptUppath
				+ ", deptPath=" + deptPath + ", deptType=" + deptType
				+ ", deptProperty=" + deptProperty + ", deptIsforregister="
				+ deptIsforregister + ", deptRegisterNo=" + deptRegisterNo
				+ ", deptIsforaccounting=" + deptIsforaccounting
				+ ", deptRemark=" + deptRemark + "]";
	}
	
}
