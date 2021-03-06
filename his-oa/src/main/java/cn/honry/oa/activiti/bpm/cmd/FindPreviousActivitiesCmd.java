package cn.honry.oa.activiti.bpm.cmd;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

/**
 * 获取当前活动节点之前的活动节点命令
 * @author luyanshou
 *
 */
public class FindPreviousActivitiesCmd implements Command<List<PvmActivity>> {
    private String processDefinitionId;
    private String activityId;

    public FindPreviousActivitiesCmd(String processDefinitionId,
            String activityId) {
        this.processDefinitionId = processDefinitionId;
        this.activityId = activityId;
    }

    public List<PvmActivity> execute(CommandContext commandContext) {
        ProcessDefinitionEntity processDefinitionEntity = Context
                .getProcessEngineConfiguration().getDeploymentManager()
                .findDeployedProcessDefinitionById(processDefinitionId);

        if (processDefinitionEntity == null) {
            throw new IllegalArgumentException(
                    "cannot find processDefinition : " + processDefinitionId);
        }

        ActivityImpl activity = processDefinitionEntity
                .findActivity(activityId);

        return this.getPreviousActivities(activity);
    }

    public List<PvmActivity> getPreviousActivities(PvmActivity pvmActivity) {
        List<PvmActivity> pvmActivities = new ArrayList<PvmActivity>();

        for (PvmTransition pvmTransition : pvmActivity.getIncomingTransitions()) {
            PvmActivity targetActivity = pvmTransition.getDestination();

            if ("userTask".equals(targetActivity.getProperty("type"))) {
                pvmActivities.add(targetActivity);
            } else {
                pvmActivities
                        .addAll(this.getPreviousActivities(targetActivity));
            }
        }

        return pvmActivities;
    }
}
