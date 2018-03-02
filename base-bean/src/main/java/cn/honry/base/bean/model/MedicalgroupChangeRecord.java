package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 * 
 * 医疗组变更
 * @Author: huzhenguo
 * @CreateDate: 2017年7月6日 下午3:33:39 
 * @Modifier: huzhenguo
 * @ModifyDate: 2017年7月6日 下午3:33:39 
 * @ModifyRmk:  
 * @version: V1.0
 * @param:
 * @throws:
 * @return: 
 *
 */
public class MedicalgroupChangeRecord extends Entity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/**项目编码**/
	private String itemCode;
	/**项目名称**/
	private String itemName;
	/**发生序号**/
	private Integer happenNo;
	/**原资料代号**/
	private String oldDataCode;
	/**原资料名称**/
	private String oldDataName;
	/**新资料代号**/
	private String newDataCode;
	/**新资料名称**/
	private String newDataName;
	/**变更原因**/
	private String changeCause;
	/**备注**/
	private String mark;
	/**操作员**/
	private String operCode;
	/**操作时间**/
	private Date operDate;
	/**科室编码**/
	private String deptCode;
	/**科室名称**/
	private String deptName;
	/**院区编码**/
	private String areaCode;
	/**院区名称**/
	private String areaName;
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public Integer getHappenNo() {
		return happenNo;
	}
	public void setHappenNo(Integer happenNo) {
		this.happenNo = happenNo;
	}
	public String getOldDataCode() {
		return oldDataCode;
	}
	public void setOldDataCode(String oldDataCode) {
		this.oldDataCode = oldDataCode;
	}
	public String getOldDataName() {
		return oldDataName;
	}
	public void setOldDataName(String oldDataName) {
		this.oldDataName = oldDataName;
	}
	public String getNewDataCode() {
		return newDataCode;
	}
	public void setNewDataCode(String newDataCode) {
		this.newDataCode = newDataCode;
	}
	public String getNewDataName() {
		return newDataName;
	}
	public void setNewDataName(String newDataName) {
		this.newDataName = newDataName;
	}
	public String getChangeCause() {
		return changeCause;
	}
	public void setChangeCause(String changeCause) {
		this.changeCause = changeCause;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	
}
