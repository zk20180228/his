package cn.honry.base.bean.model;

import java.util.Date;

public class MContactDBVersion{
	/** 主键 **/
	private Integer id;
	/** db文件地址 **/
	private String dbAdress;
	/** 插入时间 **/
	private Date createTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDbAdress() {
		return dbAdress;
	}
	public void setDbAdress(String dbAdress) {
		this.dbAdress = dbAdress;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
