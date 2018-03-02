package cn.honry.oa.activiti.task.service;

import java.util.List;
import java.util.Map;

import cn.honry.oa.activiti.bpm.process.vo.ProcessTaskDefinition;

/**
 * 处理流程的Service接口
 * @author luyanshou
 *
 */
public interface TaskInternalService {

	/**
     * 获得任务定义.
     */
    List<ProcessTaskDefinition> findTaskDefinitions(String processDefinitionId);
    
    /**
     * 触发execution继续执行.
     */
    void signalExecution(String executionId);
    
    /**
     * 完成协办.
     */
    void resolveTask(String taskId);
    
    /**
     * 完成任务.
     */
    void completeTask(String taskId, String userId,
            Map<String, Object> variables);
    
    /**
     * 解析表达式.
     */
    Object executeExpression(String taskId, String expressionText);
    
    /**
     * 获取第一个userTask节点id
     * @param processDefinitionId 流程定义id
     * @param userId
     * @return
     */
    String findFirstUserTaskActivityId(String processDefinitionId,String userId);
    
    /**
     * 回退指定节点，指定负责人. 如果activityId为null，就自动查询上一个节点.
     */
    void rollback(String taskId, String activityId, String userId);
    
    /**
     * 撤销任务.
     * @param taskId 任务id
     * @return 0-撤销成功 1-流程结束 2-下一结点已经通过,不能撤销
     */
    Integer withdrawTask(String taskId);
}
