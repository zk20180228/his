package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/** 数据迁移实体类
* @ClassName: MoveDate 数据迁移实体类
* @Description: 数据迁移实体类
* @author dtl
* @date 2016年12月6日
*  
*/
public class MoveDataLog extends Entity{
	/**
	 * 
	 */
	private static final Long serialVersionUID = 1L;
	/** 
	* @Fields optType : 1-迁移，2-删除 
	*/ 
	private Integer optType;
	/** 
	* @Fields dateType : 1-门诊，2-住院 
	*/ 
	private Integer dateType;
	/** 
	* @Fields tableName : 表名 
	*/ 
	private String tableName;
	/** 
	* @Fields tableZhName : 表注释 
	*/ 
	private String tableZhName;
	/** 
	* @Fields dataDate : 数据所在日期 
	*/ 
	private String dataDate;
	/** 
	* @Fields total : 总条数
	*/ 
	private Long total;
	/** 
	* @Fields pageCount : 总页数 
	*/ 
	private Long pageCount;
	/** 
	* @Fields pageSize : 每页条数
	*/ 
	private Long pageSize;
	/** 
	* @Fields isSuccess : 1-success,2-failure 
	*/ 
	private Integer isSuccess;
	/** 
	* @Fields failPage : 失败页码 
	*/ 
	private Long failPage;
	/** 
	* @Fields moveDate : 迁移/删除日期 
	*/ 
	private Date moveDate;
	/** 
	* @Fields startDate : 迁移/删除开始时间 
	*/ 
	private Date startDate;
	/** 
	* @Fields endDate : 迁移/删除结束时间 
	*/ 
	private Date endDate;
	
	
	public Integer getOptType() {
		return optType;
	}
	public void setOptType(Integer optType) {
		this.optType = optType;
	}
	public Integer getDateType() {
		return dateType;
	}
	public void setDateType(Integer dateType) {
		this.dateType = dateType;
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
	public String getDataDate() {
		return dataDate;
	}
	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Long getPageCount() {
		return pageCount;
	}
	public void setPageCount(Long pageCount) {
		this.pageCount = pageCount;
	}
	public Long getPageSize() {
		return pageSize;
	}
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}
	public Long getFailPage() {
		return failPage;
	}
	public void setFailPage(Long failPage) {
		this.failPage = failPage;
	}
	public Date getMoveDate() {
		return moveDate;
	}
	public void setMoveDate(Date moveDate) {
		this.moveDate = moveDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
