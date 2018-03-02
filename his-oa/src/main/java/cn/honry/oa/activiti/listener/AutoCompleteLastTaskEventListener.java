package cn.honry.oa.activiti.listener;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.mq.MessageSend;
import cn.honry.oa.extend.service.ExtendService;
import cn.honry.oa.humantask.service.HumanTaskService;
import cn.honry.oa.humantask.vo.HumanTaskVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;

/**
 * 任务结束
 *
 */
/**
 * @author Administrator
 *
 */
public class AutoCompleteLastTaskEventListener implements ActivitiEventListener {

	@Autowired
	@Qualifier(value = "extendService")
	private ExtendService extendService;
	@Autowired
	@Qualifier(value = "humanTaskService")
	private HumanTaskService humanTaskService;
	@Autowired
	@Qualifier(value = "messageSend")
	private MessageSend messageSend;

	public void setExtendService(ExtendService extendService) {
		this.extendService = extendService;
	}

	public void setHumanTaskService(HumanTaskService humanTaskService) {
		this.humanTaskService = humanTaskService;
	}

	public void setMessageSend(MessageSend messageSend) {
		this.messageSend = messageSend;
	}

	@Override
	public boolean isFailOnException() {
		return false;
	}

	@Override
	public void onEvent(ActivitiEvent event) {

		if (!(event instanceof ActivitiEntityEventImpl)) {
			return;
		}

		ActivitiEntityEventImpl activitiEntityEventImpl = (ActivitiEntityEventImpl) event;
		Object entity = activitiEntityEventImpl.getEntity();

		if (!(entity instanceof ExecutionEntity)) {
			return;
		}
		ExecutionEntity executionEntity = (ExecutionEntity) entity;
		switch (event.getType()) {
		case PROCESS_COMPLETED:
			this.onCreate(executionEntity);

			break;
		default:
			break;
		}
	}

	public void onCreate(ExecutionEntity executionEntity) {
		String businessKey = executionEntity.getBusinessKey();
		// 1.查询流程配置
		OaBpmProcess process = extendService
				.getProcessbyBusinessKeyOrId(businessKey);
		// 2.判断是否有前置任务
		if (process != null) {
			if (StringUtils.isNotBlank(process.getTopFlow())) {
				Map<String, VariableInstanceEntity> varMap = executionEntity
						.getVariableInstances();
				if (varMap != null && varMap.size() > 0) {
					// 获得关联编码
					VariableInstanceEntity leaveCodeEntity = varMap
							.get("leaveCode");
					String leaveCode = leaveCodeEntity.getTextValue();
					// 获得需要操作的流程
					OaKVRecord topRecord = extendService
							.getRecordByBusinessKeyAndFlowCode(
									process.getTopFlow(), leaveCode);
					if (topRecord != null) {
						topRecord.setFlowState(2);
						extendService.saveRecord(topRecord);
					}
				}
			}
		}
		OaKVRecord downRecord = extendService
				.getRecordByBusinessKey(businessKey);
		downRecord.setFlowState(0);
		extendService.saveRecord(downRecord);
		/**
		 * hzq 2018-01-12 业务流转完毕后，给业务发起人发送通知
		 */
		String processInstanceId = executionEntity.getProcessInstanceId();
		if (StringUtils.isNotBlank(processInstanceId)) {
			List<HumanTaskVo> humanTaskVos = humanTaskService
					.findHumanTasksByProcessInstanceId(processInstanceId);
			if (humanTaskVos != null && humanTaskVos.size() > 0) {
				HumanTaskVo humanTaskVo = humanTaskVos.get(0);
				String createUserAccount = humanTaskVo.getCreateUser();
				if (StringUtils.isNotBlank(createUserAccount)) {
					String attr2 = humanTaskVo.getAttr2();
					String processName = "";
					if (StringUtils.isNotBlank(attr2)) {
						String[] split = attr2.split("-");
						processName = split[split.length - 1];
					}
					Map<String, Object> map = new LinkedHashMap<>();
					map.put("jid", createUserAccount);
					map.put("msg_type", "msg_type_activiti_end_message");
					map.put("content", "流程:" + processName + "已完成审批");
					map.put("msg_time", DateUtils
							.formatDateY_M_D_H_M_S(DateUtils.getCurrentTime()));
					try {
						messageSend.sendMessage(JSONUtils.toJson(map));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
