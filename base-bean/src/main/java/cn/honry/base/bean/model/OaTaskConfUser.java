package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 任务实例用户配置实体类
 * @author luyanshou
 *
 */
public class OaTaskConfUser extends Entity {

	private static final long serialVersionUID = 1L;

	 /** 业务标识. */
    private String businessKey;

    /** 编码. */
    private String code;

    /** 值. */
    private String value;

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
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

    
}
