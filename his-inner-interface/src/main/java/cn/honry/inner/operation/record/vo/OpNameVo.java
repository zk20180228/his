package cn.honry.inner.operation.record.vo;

/**
 * @className：OpNameVo
 * @Description:专门用来存放手术名称的VO
 * @Author: huangbiao
 * @CreateDate: 2016年4月15日
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
public class OpNameVo {

	/**
	 * @Description:手术名称表里面的ID
	 */
	private String id;
	/**
	 * @Description:手术序号
	 */
	private String operationId;
	/**
	 * @Description:手术名称
	 */
	private String itemName;
	/**
	 * @Description:手术ID
	 */
	private String itemId;
	
	public String getId() {
		return id;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	
}
