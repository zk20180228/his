package cn.honry.base.bean.model;

import java.util.HashSet;
import java.util.Set;

import cn.honry.base.bean.business.Entity;

/**
 * 流程分类实体类
 * @author luyanshou
 *
 */
public class OaBpmCategory extends Entity {

	
	private static final long serialVersionUID = 1L;
	
	/**编号*/
	private String code;
	
	/**名称*/
	private String name;
	
	/**排序*/
	private Integer priority;
	
	/** 租户. */
    private String tenantId;

    /** 流程集合 */
    private Set<OaBpmProcess> bpmProcesses = new HashSet<>(0);

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

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Set<OaBpmProcess> getBpmProcesses() {
		return bpmProcesses;
	}

	public void setBpmProcesses(Set<OaBpmProcess> bpmProcesses) {
		this.bpmProcesses = bpmProcesses;
	}

	
}
