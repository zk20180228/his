package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 流程节点配置实体
 * @author luyanshou
 *
 */
public class OaBpmConfNode extends Entity {

	private static final long serialVersionUID = 1L;
	
	/** 外键，流程配置. */
    private OaBpmConfBase bpmConfBase;

    /** 节点编号. */
    private String code;

    /** 节点名称. */
    private String name;

    /** 节点类型. */
    private String type;

    /** 配置用户. */
    private String confUser;

    /** 配置回调. */
    private String confListener;

    /** 配置规则. */
    private String confRule;

    /** 配置表单. */
    private String confForm;

    /** 配置操作. */
    private String confOperation;

    /** 配置提醒. */
    private String confNotice;

    /** 排序. */
    private Integer priority;
    
    private String extend;

	public OaBpmConfBase getBpmConfBase() {
		return bpmConfBase;
	}

	public void setBpmConfBase(OaBpmConfBase bpmConfBase) {
		this.bpmConfBase = bpmConfBase;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getConfUser() {
		return confUser;
	}

	public void setConfUser(String confUser) {
		this.confUser = confUser;
	}

	public String getConfListener() {
		return confListener;
	}

	public void setConfListener(String confListener) {
		this.confListener = confListener;
	}

	public String getConfRule() {
		return confRule;
	}

	public void setConfRule(String confRule) {
		this.confRule = confRule;
	}

	public String getConfForm() {
		return confForm;
	}

	public void setConfForm(String confForm) {
		this.confForm = confForm;
	}

	public String getConfOperation() {
		return confOperation;
	}

	public void setConfOperation(String confOperation) {
		this.confOperation = confOperation;
	}

	public String getConfNotice() {
		return confNotice;
	}

	public void setConfNotice(String confNotice) {
		this.confNotice = confNotice;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}
    
}
