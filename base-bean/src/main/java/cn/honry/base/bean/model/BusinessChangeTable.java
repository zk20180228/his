package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 *  业务变更字段表
 * @Author:luyanshou
 * @version 1.0
 */
public class BusinessChangeTable extends Entity{

	private static final long serialVersionUID = 1L;

	/**表名称**/
	private String tableName;
	/**字段名**/
	private String columns;
	/**备注**/
	private String mark;
	/**操作人员**/
	private String operCode;
	/**操作时间**/
	private Date operDate;
	
	public String getTableName() {
		return tableName;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
