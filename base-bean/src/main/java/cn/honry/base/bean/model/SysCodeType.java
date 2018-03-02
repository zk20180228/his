package cn.honry.base.bean.model;


import cn.honry.base.bean.business.Entity;
/**
 * 编码类别实体类
 * @author hedong 
 * Date:2015/5/21 13:00
 */
public class SysCodeType extends Entity {
	/**代码**/
	private String code;
	/**名称**/
	private String name;
	/**类型**/
	private Integer type;
	/**描述**/
	private String description;
	/**等级**/
	private Integer level;
	/**排序**/
	private Integer order;
	/**适用医院**/
	private String hospital;
	
	/**  分页  **/
	private String page;
	private String rows;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
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
