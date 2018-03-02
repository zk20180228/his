package cn.honry.oa.activiti.bpm.cmd;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.DefaultFormHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.honry.oa.activiti.bpm.process.vo.OaTaskForm;

/**
 * 获取任务表单命令
 * @author luyanshou
 *
 */
public class FindOaTaskFormCmd implements Command<OaTaskForm> {
    private static Logger logger = LoggerFactory
            .getLogger(FindOaTaskFormCmd.class);
    private String processDefinitionId;

    public FindOaTaskFormCmd(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public OaTaskForm execute(CommandContext commandContext) {
        ProcessDefinitionEntity processDefinitionEntity = Context
                .getProcessEngineConfiguration().getDeploymentManager()
                .findDeployedProcessDefinitionById(processDefinitionId);

        if (processDefinitionEntity == null) {
            throw new IllegalArgumentException(
                    "未找到该流程定义,流程定义id为 : " + processDefinitionId);
        }

        if (processDefinitionEntity.hasStartFormKey()) {
            return this.findStartEventForm(processDefinitionEntity);
        }

        ActivityImpl startActivity = processDefinitionEntity.getInitial();

        if (startActivity.getOutgoingTransitions().size() != 1) {//流程节点的输出线
            throw new IllegalStateException(
                    "start activity outgoing transitions cannot more than 1, now is : "
                            + startActivity.getOutgoingTransitions().size());
        }

        PvmTransition pvmTransition = startActivity.getOutgoingTransitions()
                .get(0);
        PvmActivity targetActivity = pvmTransition.getDestination();

        if (!"userTask".equals(targetActivity.getProperty("type"))) {
            logger.info("第一个活动节点非用户节点,跳过......");

            return new OaTaskForm();
        }

        OaTaskForm firstTaskForm = new OaTaskForm();
        firstTaskForm.setProcessDefinitionId(processDefinitionId);
        firstTaskForm.setExists(true);
        firstTaskForm.setTaskForm(true);

        String taskDefinitionKey = targetActivity.getId();
        logger.debug("activityId : {}", targetActivity.getId());
        firstTaskForm.setActivityId(taskDefinitionKey);

        TaskDefinition taskDefinition = processDefinitionEntity
                .getTaskDefinitions().get(taskDefinitionKey);

        Expression expression = taskDefinition.getAssigneeExpression();

        if (expression != null) {
            String expressionText = expression.getExpressionText();
            firstTaskForm.setAssignee(expressionText);
        } else {
            logger.info("cannot find expression : {}, {}", processDefinitionId,
                    taskDefinitionKey);
        }

        String initiatorVariableName = (String) processDefinitionEntity
                .getProperty(BpmnParse.PROPERTYNAME_INITIATOR_VARIABLE_NAME);
        firstTaskForm.setInitiatorName(initiatorVariableName);

        DefaultFormHandler formHandler = (DefaultFormHandler) taskDefinition
                .getTaskFormHandler();

        if (formHandler.getFormKey() != null) {
            String formKey = formHandler.getFormKey().getExpressionText();
            firstTaskForm.setFormKey(formKey);
        } else {
            logger.info("cannot find formKey from xml : {}, {}",
                    processDefinitionId, taskDefinitionKey);
        }

        return firstTaskForm;
    }

    public OaTaskForm findStartEventForm(
            ProcessDefinitionEntity processDefinitionEntity) {
        OaTaskForm firstTaskForm = new OaTaskForm();
        firstTaskForm.setExists(true);
        firstTaskForm.setProcessDefinitionId(processDefinitionId);
        firstTaskForm.setTaskForm(false);

        DefaultFormHandler formHandler = (DefaultFormHandler) processDefinitionEntity
                .getStartFormHandler();

        if (formHandler.getFormKey() != null) {
            String formKey = formHandler.getFormKey().getExpressionText();
            firstTaskForm.setFormKey(formKey);
            firstTaskForm.setActivityId(processDefinitionEntity.getInitial()
                    .getId());
        }

        return firstTaskForm;
    }
}
