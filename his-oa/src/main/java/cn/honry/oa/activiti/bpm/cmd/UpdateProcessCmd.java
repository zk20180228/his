package cn.honry.oa.activiti.bpm.cmd;

import java.io.InputStream;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 更新流程定义命令
 * @author luyanshou
 *
 */
public class UpdateProcessCmd implements Command<Void> {
    private String processDefinitionId;
    private byte[] bytes;

    public UpdateProcessCmd(String processDefinitionId, byte[] bytes) {
        this.processDefinitionId = processDefinitionId;
        this.bytes = bytes;
    }

    public Void execute(CommandContext commandContext) {
        ProcessDefinitionEntity processDefinitionEntity = commandContext
                .getProcessDefinitionEntityManager().findProcessDefinitionById(
                        processDefinitionId);//获取流程定义实体
        String resourceName = processDefinitionEntity.getResourceName();//获取资源名称
        String deploymentId = processDefinitionEntity.getDeploymentId();//获取部署信息
        JdbcTemplate jdbcTemplate = new JdbcTemplate(Context.getProcessEngineConfiguration().getDataSource());
        jdbcTemplate
                .update("update ACT_GE_BYTEARRAY set BYTES_=? where NAME_=? and DEPLOYMENT_ID_=?",
                        bytes, resourceName, deploymentId);

        Context.getProcessEngineConfiguration().getProcessDefinitionCache()
                .remove(processDefinitionId);

        try {
            // 更新流程图图片信息
            ProcessDefinitionDiagramCmd processDefinitionDiagramCmd = new ProcessDefinitionDiagramCmd(
                    processDefinitionEntity.getId());
            InputStream is = processDefinitionDiagramCmd.execute(commandContext);//获取图片信息的输入流
            byte[] pngBytes = IOUtils.toByteArray(is);
            String diagramResourceName = processDefinitionEntity
                    .getDiagramResourceName();
            jdbcTemplate
                    .update("update ACT_GE_BYTEARRAY set BYTES_=? where NAME_=? and DEPLOYMENT_ID_=?",
                            pngBytes, diagramResourceName, deploymentId);
        } catch (Exception ex) {
        }

        return null;
    }
}
