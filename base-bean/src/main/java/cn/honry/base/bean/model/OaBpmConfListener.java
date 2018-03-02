package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 节点监听器实体
 * @author user
 *
 */
public class OaBpmConfListener extends Entity {

	
	private static final long serialVersionUID = 1L;

	/** 主键 */
	private String id;
	
	/** 编号 */
	private String code;
	
	/** 值. */
    private String value;

    /** 分类. */
    private Integer type;

    /** 状态. */
    private Integer status;

    /** 排序. */
    private Integer priority;
    
    /** 所属节点id */
    private String nodeCode;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}
    
}
