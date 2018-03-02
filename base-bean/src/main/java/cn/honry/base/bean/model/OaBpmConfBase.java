package cn.honry.base.bean.model;

import java.util.HashSet;
import java.util.Set;

import cn.honry.base.bean.business.Entity;

/**
 * 流程配置实体
 * @author luyanshou
 *
 */
public class OaBpmConfBase extends Entity {

	
	private static final long serialVersionUID = -207590644303297324L;


	/**编号*/
	private String code;
	
    /** 流程定义ID. */
    private String processDefinitionId;

    /** 流程定义KEY. */
    private String processDefinitionKey;

    /** 流程定义版本. */
    private Integer processDefinitionVersion;

    /**节点集合*/
    private Set<OaBpmConfNode> bpmConfNodes=new HashSet<>(0);
    
    /**流程定义集合 */
    private Set<OaBpmProcess> bpmProcesses = new HashSet<OaBpmProcess>(0);

    
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	public Integer getProcessDefinitionVersion() {
		return processDefinitionVersion;
	}

	public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
		this.processDefinitionVersion = processDefinitionVersion;
	}

	public Set<OaBpmConfNode> getBpmConfNodes() {
		return bpmConfNodes;
	}

	public void setBpmConfNodes(Set<OaBpmConfNode> bpmConfNodes) {
		this.bpmConfNodes = bpmConfNodes;
	}

	public Set<OaBpmProcess> getBpmProcesses() {
		return bpmProcesses;
	}

	public void setBpmProcesses(Set<OaBpmProcess> bpmProcesses) {
		this.bpmProcesses = bpmProcesses;
	}
    
}
