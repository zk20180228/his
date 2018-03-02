package cn.honry.base.bean.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.honry.base.bean.business.Entity;

/**
 * keyValue记录实体
 * @author luyanshou
 *
 */
public class OaKVRecord extends Entity {

	private static final long serialVersionUID = 1L;

	/**业务key*/
	private String businessKey;
	
	/**名称*/
    private String name;
    
    /**表单模板编号*/
    private String formTemplateCode;
    
    /**分类*/
    private String category;
    
    /**状态*/
    private Integer status;
    
    /**引用*/
    private String ref;
    
    /**操作人*/
    private String userId;
    
    /**租户id*/
    private String tenantId;
    private String processInstanceId;
    /**冗余字段  办理时限*/
    private Date expipationTime;
    
    private Integer flowState;//流程状态
    private String flowCode;//流程编号
    
    public Date getExpipationTime() {
		return expipationTime;
	}
	public void setExpipationTime(Date expipationTime) {
		this.expipationTime = expipationTime;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	/**keyValue属性集合*/
    private Map<String, OaKVProp> props = new LinkedHashMap<String, OaKVProp>();
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFormTemplateCode() {
		return formTemplateCode;
	}
	public void setFormTemplateCode(String formTemplateCode) {
		this.formTemplateCode = formTemplateCode;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public Map<String, OaKVProp> getProps() {
		return props;
	}
	public void setProps(Map<String, OaKVProp> props) {
		this.props = props;
	}
	public Integer getFlowState() {
		return flowState;
	}
	public void setFlowState(Integer flowState) {
		this.flowState = flowState;
	}
	public String getFlowCode() {
		return flowCode;
	}
	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}
    
}
