package cn.honry.oa.activiti.bpm.node.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.OaBpmConfBase;
import cn.honry.base.bean.model.OaBpmConfNode;
import cn.honry.base.service.BaseService;
import cn.honry.oa.activiti.bpm.utils.ExtendVo;

/**
 * 流程节点配置Service接口
 * @author user
 *
 */
public interface OaBpmConfNodeService extends BaseService<OaBpmConfNode> {

	/**
	 * 根据流程节点code和流程配置id获取节点信息
	 * @param code 节点code
	 * @param bpmConfBase 流程配置
	 * @return
	 */
	 List<OaBpmConfNode> getList(String code,OaBpmConfBase bpmConfBase);
	 
	 /**
	  * 根据流程节点code和流程配置id获取节点信息
	  * @param code 节点code
	  * @param bpmConfBase 流程配置
	  * @return
	  */
	 OaBpmConfNode findUinque(String code,OaBpmConfBase bpmConfBase);
	 
	 /**
	  * 根据流程定义ID和节点code获取节点扩展信息
	  * @param processDefinitionId 流程定义ID
	  * @param code 节点code
	  * @return
	  */
	String getExtend(String processDefinitionId,String code);

	 /**
	  * 节点code获取节点扩展信息
	  * @param processDefinitionId 流程定义ID
	  * @param code 节点code
	  * @return
	  */
	List<OaBpmConfNode> getConfNodeListByConfBaseCode(String code);

	/**
	  * 保存节点扩展信息
	  * @param processDefinitionId 流程定义ID
	  * @param code 节点code
	  * @return
	  */
	void saveNode(Map<String, ExtendVo> dataMap);
}
