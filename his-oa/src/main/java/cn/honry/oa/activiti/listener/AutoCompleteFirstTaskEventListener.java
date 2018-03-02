package cn.honry.oa.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.task.IdentityLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.honry.oa.humantask.service.HumanTaskService;
import cn.honry.oa.humantask.vo.HumanTaskConstants;
import cn.honry.oa.humantask.vo.HumanTaskVo;
import cn.honry.utils.ShiroSessionUtils;

/**
 * 自动完成第一个节点任务
 * @author luyanshou
 *
 */
public class AutoCompleteFirstTaskEventListener implements
		ActivitiEventListener {

	@Autowired
	@Qualifier(value = "humanTaskService")
	private HumanTaskService humanTaskService;
	
	public void setHumanTaskService(HumanTaskService humanTaskService) {
		this.humanTaskService = humanTaskService;
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

        if (!(entity instanceof TaskEntity)) {
            return;
        }

        TaskEntity taskEntity = (TaskEntity) entity;
        switch (event.getType()) {
        case TASK_CREATED:
            this.onCreate(taskEntity);

            break;
		default:
			break;
        }
	}

	public void onCreate(DelegateTask delegateTask) {
		String initiatorId = Authentication.getAuthenticatedUserId();

        if (initiatorId == null) {
            return;
        }

        String assignee = delegateTask.getAssignee();

        if (assignee == null) {
            return;
        }

        if (!initiatorId.equals(assignee)) {
            return;
        }

        PvmActivity targetActivity = this.findFirstActivity(delegateTask
                .getProcessDefinitionId());
        if (!targetActivity.getId().equals(
                delegateTask.getExecution().getCurrentActivityId())) {
            return;
        }
        
        for (IdentityLink identityLink : delegateTask.getCandidates()) {
            String userId = identityLink.getUserId();
            String groupId = identityLink.getGroupId();

            if (userId != null) {
                delegateTask.deleteCandidateUser(userId);
            }

            if (groupId != null) {
                delegateTask.deleteCandidateGroup(groupId);
            }
        }
        
        HumanTaskVo humanTaskVo = humanTaskService.findHumanTaskByTaskId(delegateTask.getId());
        humanTaskVo.setCatalog(HumanTaskConstants.CATALOG_START);
        humanTaskVo.setLastModifier(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
        humanTaskService.saveHumanTask(humanTaskVo);
        delegateTask.getExecution().setVariableLocal(
                "_ACTIVITI_SKIP_EXPRESSION_ENABLED", true);

        TaskDefinition taskDefinition = ((TaskEntity) delegateTask)
                .getTaskDefinition();
        ExpressionManager expressionManager = Context
                .getProcessEngineConfiguration().getExpressionManager();
        Expression expression = expressionManager
                .createExpression("${_ACTIVITI_SKIP_EXPRESSION_ENABLED}");
        taskDefinition.setSkipExpression(expression);
	}

	
	
	public PvmActivity findFirstActivity(String processDefinitionId) {
		ProcessDefinitionEntity processDefinitionEntity = Context
                .getProcessEngineConfiguration().getProcessDefinitionCache()
                .get(processDefinitionId);

        ActivityImpl startActivity = processDefinitionEntity.getInitial();
        if (startActivity.getOutgoingTransitions().size() != 1) {
            throw new IllegalStateException(
                    "开始节点的出线数量不能大于1,现在的数量是 : "
                            + startActivity.getOutgoingTransitions().size());
        }
        
        PvmTransition pvmTransition = startActivity.getOutgoingTransitions()
                .get(0);
        PvmActivity targetActivity = pvmTransition.getDestination();
        if (!"userTask".equals(targetActivity.getProperty("type"))){
        	return null;
        }
        return targetActivity;
	}

}
