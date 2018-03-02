package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;


@SuppressWarnings("serial")
public class SysDbAdmin extends Entity  {

	
	/** 表名  **/
	private String tableName;
	/** 分区名 **/
	private String partitionName ;
	/** 表空间名  **/
	private String tablespaceName;
	/** 数据库名  **/
	private String database;
	/** 分区字段  **/
	private String columnName;
	/** 分区类型1.时间(年)2.时间(月)3.时间(日)2.其他 **/
	private Integer zoneType;
	/** 分区值 **/
	private String highValue;
	
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getPartitionName() {
		return partitionName;
	}
	public void setPartitionName(String partitionName) {
		this.partitionName = partitionName;
	}
	public String getTablespaceName() {
		return tablespaceName;
	}
	public void setTablespaceName(String tablespaceName) {
		this.tablespaceName = tablespaceName;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getHighValue() {
		return highValue;
	}
	public void setHighValue(String highValue) {
		this.highValue = highValue;
	}
	public String getPage() {
		return null;
	}
	public String getRows() {
		return null;
	}
	public Integer getZoneType() {
		return zoneType;
	}
	public void setZoneType(Integer zoneType) {
		this.zoneType = zoneType;
	}
	
}