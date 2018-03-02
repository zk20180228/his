package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 流程定义实体
 * @author luyanshou
 *
 */
public class OaBpmProcess extends Entity {
	
	private static final long serialVersionUID = 950644267884644807L;

	/**编号*/
	private String code;
	
	/** 名称. */
    private String name;

    /** 排序. */
    private Integer priority;
    
    /**流程分类编号*/
    private String categoryCode;

    /** 备注. */
    private String descn;

    /** 是否配置任务. */
    private Integer useTaskConf;
    
    /** 租户. */
    private String tenantId;
    
    /**流程配置编号 */
    private String confBaseCode;
    
    /**表单配置编号 */
    private String formCode;
    
    /**工作流科室编号 */
    private String deptCode;
    
    /**前置流程**/
    private String topFlow;
    
    /**后置流程**/
    private String downFlow;
    
    /**移动端访问路径**/
    private String action;
    
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

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

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getDescn() {
		return descn;
	}

	public void setDescn(String descn) {
		this.descn = descn;
	}

	public Integer getUseTaskConf() {
		return useTaskConf;
	}

	public void setUseTaskConf(Integer useTaskConf) {
		this.useTaskConf = useTaskConf;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getConfBaseCode() {
		return confBaseCode;
	}

	public void setConfBaseCode(String confBaseCode) {
		this.confBaseCode = confBaseCode;
	}

	public String getFormCode() {
		return formCode;
	}

	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getTopFlow() {
		return topFlow;
	}

	public void setTopFlow(String topFlow) {
		this.topFlow = topFlow;
	}

	public String getDownFlow() {
		return downFlow;
	}

	public void setDownFlow(String downFlow) {
		this.downFlow = downFlow;
	}

}
