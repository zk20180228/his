package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 任务定义用户
 * @author luyanshou
 *
 */
public class OaTaskDefUser extends Entity {

	private static final long serialVersionUID = 1L;

	 /** 值. */
    private String value;

    /** 类型. */
    private String type;

    /** 分类. */
    private String catalog;

    /** 状态. */
    private String status;
    
    /**任务定义(外键)*/
    private String baseId;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
    
}
