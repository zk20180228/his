package cn.honry.base.bean.model;

import java.util.Date;

/**
 * 
 * 
 * <p>同步数据管理Vo</p>
 * @Author: XCL
 * @CreateDate: 2017年9月19日 下午8:00:40 
 * @Modifier: XCL
 * @ModifyDate: 2017年9月19日 下午8:00:40 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public class IDataSynch {
	/**主键**/
	private String id; 
	/**编码**/
	private String code;
	/**相关表**/
	private String tableName;
	/**表名**/
	private String tableZhName;
	/**所属视图**/
	private String viewName;
	/**所属视图名称**/
	private String viewZhName;
	/**是否业务关联0是1否**/
	private Integer workSign;
	/**同步方式0增量1全量**/
	private Integer synchSign;
	/**增量字段**/
	private String synchCond;
	/**线程数**/
	private Integer threadNum;
	/**时间间隔（值）**/
	private Integer timeSpace;
	/**时间间隔（单位）S秒M分H时D天W周**/
	private String timeUnit;
	/**同步时长**/
	private Integer synchLength;
	/**同步时长（单位）S秒M分H时D天W周**/
	private String synchUnit;
	/**schema(账户)**/
	private String schema;
	/**查询字段**/
	private String queryField;
	/**查询条件**/
	private String queryCond;
	/**分组字段**/
	private String groupFiled;
	/**排序字段**/
	private String orderFiled;
	/**排序条件**/
	private String orderCond;
	/**表排序**/
	private Integer tableOrder;
	/**视图排序**/
	private Integer viewOrder;
	/**执行服务分类(I_SERVE_MANAGE.CODE)（主）**/
	private String serveCode;
	/**执行服务分类(I_SERVE_MANAGE.CODE)（备）**/
	private String serveCodeprepare;
	/**默认同步时间**/
	private Date defaTime;
	/**默认显示同步时间**/
	private String defaTimeStr;
	/**最新同步时间**/
	private Date newestTime;
	/**最新同步显示**/
	private String newesTimeStr;
	/**状态0启用1停用**/
	private Integer state;
	/**备注**/
	private String remarks;
	/**同步数据主键**/
	private String primaryColum;
	/**表所属用户**/
	private String tableFromUser;
	/**分区字段**/
	private String tablePartition;
	
	public String getTablePartition() {
		return tablePartition;
	}
	public void setTablePartition(String tablePartition) {
		this.tablePartition = tablePartition;
	}
	public String getTableFromUser() {
		return tableFromUser;
	}
	public void setTableFromUser(String tableFromUser) {
		this.tableFromUser = tableFromUser;
	}
	public String getServeCodeprepare() {
		return serveCodeprepare;
	}
	public void setServeCodeprepare(String serveCodeprepare) {
		this.serveCodeprepare = serveCodeprepare;
	}
	public String getPrimaryColum() {
		return primaryColum;
	}
	public void setPrimaryColum(String primaryColum) {
		this.primaryColum = primaryColum;
	}
	public String getDefaTimeStr() {
		return defaTimeStr;
	}
	public void setDefaTimeStr(String defaTimeStr) {
		this.defaTimeStr = defaTimeStr;
	}
	public String getNewesTimeStr() {
		return newesTimeStr;
	}
	public void setNewesTimeStr(String newesTimeStr) {
		this.newesTimeStr = newesTimeStr;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableZhName() {
		return tableZhName;
	}
	public void setTableZhName(String tableZhName) {
		this.tableZhName = tableZhName;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public String getViewZhName() {
		return viewZhName;
	}
	public void setViewZhName(String viewZhName) {
		this.viewZhName = viewZhName;
	}
	public Integer getWorkSign() {
		return workSign;
	}
	public void setWorkSign(Integer workSign) {
		this.workSign = workSign;
	}
	public Integer getSynchSign() {
		return synchSign;
	}
	public void setSynchSign(Integer synchSign) {
		this.synchSign = synchSign;
	}
	public String getSynchCond() {
		return synchCond;
	}
	public void setSynchCond(String synchCond) {
		this.synchCond = synchCond;
	}
	public Integer getThreadNum() {
		return threadNum;
	}
	public void setThreadNum(Integer threadNum) {
		this.threadNum = threadNum;
	}
	public Integer getTimeSpace() {
		return timeSpace;
	}
	public void setTimeSpace(Integer timeSpace) {
		this.timeSpace = timeSpace;
	}
	public String getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	public Integer getSynchLength() {
		return synchLength;
	}
	public void setSynchLength(Integer synchLength) {
		this.synchLength = synchLength;
	}
	public String getSynchUnit() {
		return synchUnit;
	}
	public void setSynchUnit(String synchUnit) {
		this.synchUnit = synchUnit;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getQueryField() {
		return queryField;
	}
	public void setQueryField(String queryField) {
		this.queryField = queryField;
	}
	public String getQueryCond() {
		return queryCond;
	}
	public void setQueryCond(String queryCond) {
		this.queryCond = queryCond;
	}
	public String getGroupFiled() {
		return groupFiled;
	}
	public void setGroupFiled(String groupFiled) {
		this.groupFiled = groupFiled;
	}
	public String getOrderFiled() {
		return orderFiled;
	}
	public void setOrderFiled(String orderFiled) {
		this.orderFiled = orderFiled;
	}
	public String getOrderCond() {
		return orderCond;
	}
	public void setOrderCond(String orderCond) {
		this.orderCond = orderCond;
	}
	public Integer getTableOrder() {
		return tableOrder;
	}
	public void setTableOrder(Integer tableOrder) {
		this.tableOrder = tableOrder;
	}
	public Integer getViewOrder() {
		return viewOrder;
	}
	public void setViewOrder(Integer viewOrder) {
		this.viewOrder = viewOrder;
	}
	public String getServeCode() {
		return serveCode;
	}
	public void setServeCode(String serveCode) {
		this.serveCode = serveCode;
	}
	public Date getDefaTime() {
		return defaTime;
	}
	public void setDefaTime(Date defaTime) {
		this.defaTime = defaTime;
	}
	public Date getNewestTime() {
		return newestTime;
	}
	public void setNewestTime(Date newestTime) {
		this.newestTime = newestTime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		return "IDataSynch [id=" + id + ", code=" + code + ", tableName="
				+ tableName + ", tableZhName=" + tableZhName + ", viewName="
				+ viewName + ", viewZhName=" + viewZhName + ", workSign="
				+ workSign + ", synchSign=" + synchSign + ", synchCond="
				+ synchCond + ", threadNum=" + threadNum + ", timeSpace="
				+ timeSpace + ", timeUnit=" + timeUnit + ", synchLength="
				+ synchLength + ", synchUnit=" + synchUnit + ", schema="
				+ schema + ", queryField=" + queryField + ", queryCond="
				+ queryCond + ", groupFiled=" + groupFiled + ", orderFiled="
				+ orderFiled + ", orderCond=" + orderCond + ", tableOrder="
				+ tableOrder + ", viewOrder=" + viewOrder + ", serveCode="
				+ serveCode + ", defaTime=" + defaTime + ", newestTime="
				+ newestTime + ", state=" + state + ", remarks=" + remarks
				+ "]";
	}
	
}
