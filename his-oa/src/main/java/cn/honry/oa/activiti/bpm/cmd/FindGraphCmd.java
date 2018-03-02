package cn.honry.oa.activiti.bpm.cmd;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import cn.honry.oa.activiti.bpm.graph.ActivitiGraphBuilder;
import cn.honry.oa.activiti.bpm.graph.Graph;

public class FindGraphCmd implements Command<Graph> {

    private String processDefinitionId;

    public FindGraphCmd(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public Graph execute(CommandContext commandContext) {
        return new ActivitiGraphBuilder(processDefinitionId).build();
    }
}
