package cn.honry.oa.activiti.operation.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.honry.oa.activiti.operation.dao.OaOperationDao;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cmd.GetBpmnModelCmd;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaKVProp;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.oa.activiti.operation.service.OaOperationService;
import cn.honry.oa.activiti.operation.vo.EdgeVo;
import cn.honry.oa.activiti.operation.vo.NodeVo;
import cn.honry.oa.extend.service.ExtendService;
import cn.honry.oa.humantask.service.HumanTaskService;
import cn.honry.oa.humantask.vo.HumanTaskVo;

/**
 * 流程操作Service实现类
 * @author luyanshou
 *
 */
@Service("oaOperationService")
@Transactional
@SuppressWarnings({ "all" })
public class OaOperationServiceImpl implements OaOperationService {

	@Resource
	private ProcessEngine processEngine;//工作流引擎
	@Resource
	private OaOperationDao oaOperationDao;
	
	@Autowired
	@Qualifier(value = "extendService")
	private ExtendService extendService;
	public void setExtendService(ExtendService extendService) {
		this.extendService = extendService;
	}
	
	@Autowired
	@Qualifier(value = "humanTaskService")
	private HumanTaskService humanTaskService;
	public void setHumanTaskService(HumanTaskService humanTaskService) {
		this.humanTaskService = humanTaskService;
	}

	/**
	 * 发起流程
	 * @param userId 用户
	 * @param businessKey 业务标识
	 * @param processDefinitionId 流程定义id
	 * @param processParameters 参数
	 * @return
	 */
	public String startProcess(String userId, String businessKey,
            String processDefinitionId, Map<String, Object> processParameters,OaBpmProcess bpmProcess){
		
		// 先设置登录用户
        IdentityService identityService = processEngine.getIdentityService();
        identityService.setAuthenticatedUserId(userId);

        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceById(processDefinitionId, businessKey,
                		processParameters);
        
		//查询流程配置
		if(bpmProcess!=null){
			//判读是否有前置流程
			if(StringUtils.isNotBlank(bpmProcess.getTopFlow())){
				String leaveCode = processParameters.get("leaveCode").toString();
				OaKVRecord record = extendService.getRecordByBusinessKeyAndFlowCode(bpmProcess.getTopFlow(),leaveCode);
				if(record!=null){
					record.setFlowState(1);//修改为处理中
					extendService.saveRecord(record);
				}
			}
		}
		return processInstance.getId();
	}
	
	/**
	 * 根据流程实例ID获取流程图节点数据
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	public List<NodeVo> traceProcessInstance(String processInstanceId){
		HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService()
				.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		GetBpmnModelCmd getBpmnModelCmd = new GetBpmnModelCmd(
                historicProcessInstance.getProcessDefinitionId());
        BpmnModel bpmnModel = processEngine.getManagementService()
                .executeCommand(getBpmnModelCmd);

        Map<String, GraphicInfo> graphicInfoMap = bpmnModel.getLocationMap();
        List<NodeVo> list = new ArrayList<>();
        for (Map.Entry<String, GraphicInfo> entry : graphicInfoMap.entrySet()){
        	String key = entry.getKey();
            GraphicInfo graphicInfo = entry.getValue();
            NodeVo nodeVo = convertNodeVo(graphicInfo,bpmnModel.getFlowElement(key), key, bpmnModel);
            list.add(nodeVo);
        }
        
		return list;
	}
	
	/**
	 * 转换nodeVo方法
	 * @param graphicInfo
	 * @param flowElement
	 * @param id
	 * @param bpmnModel
	 * @return
	 */
	public NodeVo convertNodeVo(GraphicInfo graphicInfo,FlowElement flowElement, String id, BpmnModel bpmnModel){
		NodeVo vo = new NodeVo();
		vo.setX((int) graphicInfo.getX());
        vo.setY((int) graphicInfo.getY());
        vo.setWidth((int) graphicInfo.getWidth());
        vo.setHeight((int) graphicInfo.getHeight());
        //
        vo.setId(id);
        vo.setName(flowElement.getName());

        if (flowElement instanceof UserTask) {
            vo.setType("用户任务");

            UserTask userTask = (UserTask) flowElement;
            vo.setAssignee(userTask.getAssignee());
        } else if (flowElement instanceof StartEvent) {
            vo.setType("开始事件");
        } else if (flowElement instanceof EndEvent) {
            vo.setType("结束事件");
        } else if (flowElement instanceof ExclusiveGateway) {
            vo.setType("选择网关");
        }

        if (flowElement instanceof FlowNode) {
            FlowNode flowNode = (FlowNode) flowElement;

            for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
                EdgeVo edgeDto = new EdgeVo();
                edgeDto.setId(sequenceFlow.getTargetRef());

                for (GraphicInfo flowGraphicInfo : bpmnModel
                        .getFlowLocationGraphicInfo(sequenceFlow.getId())) {
                    List<Integer> position = new ArrayList<Integer>();
                    position.add((int) flowGraphicInfo.getX()
                            - ((int) graphicInfo.getWidth() / 2));
                    position.add((int) flowGraphicInfo.getY()
                            - ((int) graphicInfo.getHeight() / 2));
                    edgeDto.getG().add(position);
                }

                edgeDto.getG().remove(0);
                edgeDto.getG().remove(edgeDto.getG().size() - 1);
                vo.getOutgoings().add(edgeDto);
            }
        }
		return vo;
	}

	/**
	 * 终止运行中的流程实例
	 */
	@Override
	public void endProcessInstance(String id, String account) {
		Authentication.setAuthenticatedUserId(account);
		processEngine.getRuntimeService().deleteProcessInstance(id, "人工终止");
		
		List<HumanTaskVo> humanTaskVoList = humanTaskService.findHumanTasksByProcessInstanceId(id);
		if(humanTaskVoList!=null&&humanTaskVoList.size()>0){
			String key = humanTaskVoList.get(0).getBusinessKey();
			OaBpmProcess process = extendService.getProcessbyBusinessKeyOrId(key);
	        if(process!=null){
				//判读是否有前置流程
				if(StringUtils.isNotBlank(process.getTopFlow())){
					OaKVRecord recode =  humanTaskService.getRecord(key);
					if(recode!=null){
						Map<String, OaKVProp> OaKVProp = recode.getProps();
						if(OaKVProp!=null){
							OaKVProp prop = OaKVProp.get("leaveCode");
							if(prop!=null){
								String leaveCode = prop.getValue();
								if(StringUtils.isNotBlank(leaveCode)){
									OaKVRecord record = extendService.getRecordByBusinessKeyAndFlowCode(process.getTopFlow(),leaveCode);
									if(record!=null){
										record.setFlowState(0);//修改为完成为处理后置流程
										extendService.saveRecord(record);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public Map<String, String> findEdcuations() {
		return oaOperationDao.findEdcuations();
	}
}
