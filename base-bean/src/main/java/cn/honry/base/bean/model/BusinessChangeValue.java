package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**  
 *  业务变更记录表
 * @Author:luyanshou
 * @version 1.0
 */
public class BusinessChangeValue extends Entity {

	private static final long serialVersionUID = 1L;

	/**变更序号**/
	private String changeNo;
	/**表名称**/
	private String tableName;
	/**记录id**/
	private String tableId;
	/**字段名**/
	private String columns;
	/**原值**/
	private String oldValue;
	/**新值**/
	private String newValue;
	
	
	public String getChangeNo() {
		return changeNo;
	}
	public void setChangeNo(String changeNo) {
		this.changeNo = changeNo;
	}
	public String getTableName() {
		return tableName;
	}
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
