package cn.honry.oa.humantask.vo;

import java.util.Date;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.lang3.StringUtils;

/**
 * 构建一个HumanTaskVo对象
 * @author luyanshou
 *
 */
public class HumanTaskBuilder {
    private HumanTaskVo humanTaskVo = new HumanTaskVo();

    public HumanTaskBuilder setDelegateTask(DelegateTask delegateTask) {
        humanTaskVo.setBusinessKey(delegateTask.getExecution()
                .getProcessBusinessKey());
        humanTaskVo.setName(delegateTask.getName());
        humanTaskVo.setDescription(delegateTask.getDescription());

        humanTaskVo.setCode(delegateTask.getTaskDefinitionKey());
        humanTaskVo.setAssignee(delegateTask.getAssignee());
        humanTaskVo.setOwner(delegateTask.getOwner());
        humanTaskVo.setDelegateStatus("none");
        humanTaskVo.setPriority(delegateTask.getPriority());
        humanTaskVo.setCreateTime(new Date());
        humanTaskVo.setDuration(delegateTask.getDueDate() + "");
        humanTaskVo.setSuspendStatus("none");
        humanTaskVo.setCategory(delegateTask.getCategory());
        humanTaskVo.setForm(delegateTask.getFormKey());
        humanTaskVo.setTaskId(delegateTask.getId());
        humanTaskVo.setExecutionId(delegateTask.getExecutionId());
        humanTaskVo.setProcessInstanceId(delegateTask.getProcessInstanceId());
        humanTaskVo.setProcessDefinitionId(delegateTask
                .getProcessDefinitionId());
        humanTaskVo.setTenantId(delegateTask.getTenantId());
        humanTaskVo.setStatus("active");
        humanTaskVo.setCatalog(HumanTaskConstants.CATALOG_NORMAL);

        ExecutionEntity executionEntity = (ExecutionEntity) delegateTask
                .getExecution();
        ExecutionEntity processInstance = executionEntity.getProcessInstance();
        humanTaskVo.setPresentationSubject(processInstance.getName());

        String userId = Authentication.getAuthenticatedUserId();
        humanTaskVo.setProcessStarter(userId);

        String rules="";
        for (IdentityLink identityLink : delegateTask.getCandidates()){
        	String type = identityLink.getType();
        	if(rules.length()>0){
        		rules+=",";
        	}
        	if ("candidate".equals(type)){
        		if(StringUtils.isNotBlank(identityLink.getUserId())){
        			rules+=identityLink.getUserId();
        		}else if(StringUtils.isNotBlank(identityLink.getGroupId())){
        			rules+=identityLink.getGroupId();
        		}
        	}
        }
        humanTaskVo.setAttr1(rules);
        return this;
    }

    public HumanTaskBuilder setVote(boolean isVote) {
        if (isVote) {
            humanTaskVo.setCatalog(HumanTaskConstants.CATALOG_VOTE);
        }

        return this;
    }
    
    public HumanTaskVo build() {
        return humanTaskVo;
    }
}
