package cn.honry.oa.activiti.bpm.cmd;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import cn.honry.oa.activiti.bpm.graph.ActivitiHistoryGraphBuilder;
import cn.honry.oa.activiti.bpm.graph.Graph;

/**
 * 查询流程图(包含历史)命令
 * @author luyanshou
 *
 */
public class FindHistoryGraphCmd implements Command<Graph> {
    private String processInstanceId;

    public FindHistoryGraphCmd(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Graph execute(CommandContext commandContext) {
        return new ActivitiHistoryGraphBuilder(processInstanceId).build();
    }
}
