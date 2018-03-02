package cn.honry.statistics.bi.statisticalSetting.vo;

import java.io.Serializable;

public class VtableName implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		/**表名**/
		private String tableName;
		/**字段名称**/
		private String columnName;
		/**字段类型**/
		private String columnType;
		public String getColumnType() {
			return columnType;
		}
		public void setColumnType(String columnType) {
			this.columnType = columnType;
		}
		public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		public String getColumnName() {
			return columnName;
		}
		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}
		
}
