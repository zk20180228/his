package cn.honry.oa.activiti.task.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.task.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.oa.activiti.bpm.cmd.CompleteTaskWithCommentCmd;
import cn.honry.oa.activiti.bpm.cmd.DeleteTaskWithCommentCmd;
import cn.honry.oa.activiti.bpm.cmd.FindTaskDefinitionsCmd;
import cn.honry.oa.activiti.bpm.cmd.RollbackTaskCmd;
import cn.honry.oa.activiti.bpm.cmd.SignalStartEventCmd;
import cn.honry.oa.activiti.bpm.cmd.WithdrawTaskCmd;
import cn.honry.oa.activiti.bpm.process.vo.ProcessTaskDefinition;
import cn.honry.oa.activiti.task.service.TaskInternalService;

/**
 * 处理流程的Service实现类
 * @author luyanshou
 *
 */
@Service("taskInternalService")
@Transactional
@SuppressWarnings({ "all" })
public class TaskInternalServiceImpl implements TaskInternalService {

	
	@Resource
	private ProcessEngine processEngine;//工作流引擎
	
	/**
     * 获得任务定义.
     */
    public List<ProcessTaskDefinition> findTaskDefinitions(
            String processDefinitionId){
    	List<ProcessTaskDefinition> processTaskDefinitions = new ArrayList<ProcessTaskDefinition>();
    	List<TaskDefinition> taskDefinitions = processEngine.getManagementService().executeCommand(
    			new FindTaskDefinitionsCmd(processDefinitionId));//获取任务定义集合
    	if(taskDefinitions==null || taskDefinitions.size()==0){
    		return processTaskDefinitions;
    	}
    	
    	for (TaskDefinition taskDefinition : taskDefinitions) {
            ProcessTaskDefinition processTaskDefinition = new ProcessTaskDefinition();
            processTaskDefinition.setKey(taskDefinition.getKey());

            if (taskDefinition.getNameExpression() != null) {
                processTaskDefinition.setName(taskDefinition
                        .getNameExpression().getExpressionText());
            }

            if (taskDefinition.getAssigneeExpression() != null) {
                processTaskDefinition.setAssignee(taskDefinition
                        .getAssigneeExpression().getExpressionText());
            }

            processTaskDefinitions.add(processTaskDefinition);
        }

        return processTaskDefinitions;
    }
    
    /**
     * 获得提交节点
     */
    public String findFirstUserTaskActivityId(String processDefinitionId,
            String initiator) {
        GetDeploymentProcessDefinitionCmd getDeploymentProcessDefinitionCmd = new GetDeploymentProcessDefinitionCmd(
                processDefinitionId);
        ProcessDefinitionEntity processDefinitionEntity = processEngine
                .getManagementService().executeCommand(
                        getDeploymentProcessDefinitionCmd);

        ActivityImpl startActivity = processDefinitionEntity.getInitial();

        if (startActivity.getOutgoingTransitions().size() != 1) {
            throw new IllegalStateException(
                    "start activity outgoing transitions cannot more than 1, now is : "
                            + startActivity.getOutgoingTransitions().size());
        }

        PvmTransition pvmTransition = startActivity.getOutgoingTransitions()
                .get(0);
        PvmActivity targetActivity = pvmTransition.getDestination();

        if (!"userTask".equals(targetActivity.getProperty("type"))) {

            return null;
        }

        return targetActivity.getId();
    }
    
    /**
     * 触发execution继续执行.
     */
    public void signalExecution(String executionId){
    	processEngine.getManagementService().executeCommand(new SignalStartEventCmd(executionId));
    }
    
    /**
     * 完成协办.
     */
    public void resolveTask(String taskId) {
        processEngine.getTaskService().resolveTask(taskId);
    }
    
    /**
     * 完成任务.
     */
    public void completeTask(String taskId, String userId,
            Map<String, Object> variables) {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if (task == null) {
            throw new IllegalStateException("任务不存在");
        }

        // 先设置登录用户
        IdentityService identityService = processEngine.getIdentityService();
        identityService.setAuthenticatedUserId(userId);

        // 处理子任务
        if ("subtask".equals(task.getCategory())) {
            processEngine.getManagementService().executeCommand(
                    new DeleteTaskWithCommentCmd(taskId, "完成"));
            JdbcTemplate jdbcTemplate = new JdbcTemplate(Context.getProcessEngineConfiguration().getDataSource());
            int count = jdbcTemplate.queryForObject(
                    "select count(*) from ACT_RU_TASK where PARENT_TASK_ID_=?",
                    Integer.class, task.getParentTaskId());

            if (count > 1) {
                return;
            }

            taskId = task.getParentTaskId();
        }

        processEngine.getManagementService().executeCommand(
                new CompleteTaskWithCommentCmd(taskId, variables, "完成"));
    }
    
    /**
     * 解析表达式.
     */
    public Object executeExpression(String taskId, String expressionText) {
        TaskEntity taskEntity = Context.getCommandContext()
                .getTaskEntityManager().findTaskById(taskId);
        ExpressionManager expressionManager = Context
                .getProcessEngineConfiguration().getExpressionManager();

        return expressionManager.createExpression(expressionText).getValue(
                taskEntity);
    }
    
    /**
     * 回退指定节点，指定负责人. 如果activityId为null，就自动查询上一个节点.
     */
    public void rollback(String taskId, String activityId, String userId) {
        Command<Object> cmd = new RollbackTaskCmd(taskId, activityId, userId);

        processEngine.getManagementService().executeCommand(cmd);
    }
    
    /**
     * 撤销任务.
     * @param taskId 任务id
     * @return 0-撤销成功 1-流程结束 2-下一结点已经通过,不能撤销
     */
    public Integer withdrawTask(String taskId) {
        Command<Integer> cmd = new WithdrawTaskCmd(taskId);

        Integer integer = processEngine.getManagementService().executeCommand(cmd);
        return integer;
    }
}
