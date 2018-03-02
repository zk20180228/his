package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;
/**
 * keyValue属性实体
 * @author luyanshou
 *
 */
public class OaKVProp extends Entity {

	private static final long serialVersionUID = 1L;

	/**编号*/
	private String code;
	
	/**类型*/
	private Integer type;
	
	/**值*/
	private String value;
	
	/**租户id*/
    private String tenantId;
    
    /**所属记录的id(外键)*/
    private String recordId;
    
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
    
}
