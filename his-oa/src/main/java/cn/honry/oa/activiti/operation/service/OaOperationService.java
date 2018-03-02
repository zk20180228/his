package cn.honry.oa.activiti.operation.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.oa.activiti.operation.vo.NodeVo;

/**
 * 流程操作Service接口
 * @author luyanshou
 *
 */
public interface OaOperationService {

	/**
	 * 发起流程
	 * @param userId 用户
	 * @param businessKey 业务标识
	 * @param processDefinitionId 流程定义id
	 * @param processParameters 参数
	 * @return
	 */
	String startProcess(String userId, String businessKey,String processDefinitionId, Map<String, Object> processParameters,OaBpmProcess bpmProcess);
	
	/**
	 * 根据流程实例ID获取流程图节点数据
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	List<NodeVo> traceProcessInstance(String processInstanceId);

	/**
	 * 终止运行中的流程实例
	 */
	void endProcessInstance(String id, String account);

	public Map<String, String> findEdcuations();
}
