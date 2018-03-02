package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 *  
 * @Description：药品 非药品变更表 
 * @Author：donghe
 * @CreateDate：2016-6-21
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class BusinessChangeRecord extends Entity implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**项目编码**/
	private String itemCode;
	/**发生序号**/
	private Integer happenNo;
	/**项目类别 0 药品 1 非药品 2 保留**/
	private String itemType;
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
	
	//用于显示字段
	private String itemName;//项目名称
	private String spec;//规格
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
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
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
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
