package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 表单配置实体
 * @author luyanshou
 *
 */
public class OaBpmConfForm extends Entity {

	
	private static final long serialVersionUID = 1L;

	/**编号*/
	private String code;
	
	/** 值. */
    private String value;

    /** 分类. */
    private Integer type;

    /** 原始值. */
    private String originValue;

    /** 原始类型. */
    private Integer originType;

    /** 状态. */
    private Integer status;
    
    /**所属节点*/
    private String confNodeCode;

    /**绑定属性集合*/
    private String properties;
    
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

	public String getOriginValue() {
		return originValue;
	}

	public void setOriginValue(String originValue) {
		this.originValue = originValue;
	}

	public Integer getOriginType() {
		return originType;
	}

	public void setOriginType(Integer originType) {
		this.originType = originType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getConfNodeCode() {
		return confNodeCode;
	}

	public void setConfNodeCode(String confNodeCode) {
		this.confNodeCode = confNodeCode;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}
    
}
