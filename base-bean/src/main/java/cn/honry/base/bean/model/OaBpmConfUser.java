package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 用户配置实体
 * @author luyanshou
 *
 */
public class OaBpmConfUser extends Entity {

	
	private static final long serialVersionUID = 1L;

	/** 值. */
    private String value;

    /** 分类. */
    private Integer type;

    /** 状态. */
    private Integer status;

    /** 排序. */
    private Integer priority;
    
    /**所属节点*/
    private String nodeId;

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

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}
