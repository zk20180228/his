package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class BusinessAdvicetoSystemclass extends Entity implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	/**医嘱类型*/
	private String typeId;
	/**系统类别*/
	private String classId;
	
	/** 分页用的page和rows*/
	private String page;
	private String rows;
	
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	
}
