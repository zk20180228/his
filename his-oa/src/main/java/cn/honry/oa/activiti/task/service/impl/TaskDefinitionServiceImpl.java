package cn.honry.oa.activiti.task.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaBpmConfCountersign;
import cn.honry.base.bean.model.OaTaskDefBase;
import cn.honry.base.bean.model.OaTaskDefUser;
import cn.honry.oa.activiti.task.service.TaskDefaultService;
import cn.honry.oa.activiti.task.service.TaskDefinitionService;
import cn.honry.oa.activiti.task.vo.TaskDefinitionVo;

/**
 * 任务相关操作Service
 * @author luyanshou
 *
 */
@Service("taskDefinitionService")
@Transactional
@SuppressWarnings({ "all" })
public class TaskDefinitionServiceImpl implements TaskDefinitionService{

	@Autowired
	@Qualifier(value = "taskDefaultService")
	private TaskDefaultService taskDefaultService;

	public void setTaskDefaultService(TaskDefaultService taskDefaultService) {
		this.taskDefaultService = taskDefaultService;
	} 
	
	/**
	 * 添加任务定义相关数据
	 * @param taskDefinition
	 */
	public void create(TaskDefinitionVo taskDefinition){
        String processDefinitionId = taskDefinition.getProcessDefinitionId();//流程定义id
        String[] strings = processDefinitionId.split("\\:");
        String processDefinitionKey = strings[0];//流程定义key
        Integer processDefinitionVersion= Integer.valueOf(strings[1]);//流程定义版本
        String code = taskDefinition.getCode();
        //1.设置任务配置数据
        StringBuffer hql= new StringBuffer("from OaTaskDefBase where code=? and processDefinitionKey=? and processDefinitionVersion=?")
        .append(" and del_flg=0 and stop_flg=0");
        
        OaTaskDefBase taskDefBase = taskDefaultService.findUnique(hql.toString(),new OaTaskDefBase(),code,
        		processDefinitionKey,processDefinitionVersion);
        
        if(taskDefBase==null){
        	taskDefBase= new OaTaskDefBase();
        	taskDefBase.setCode(code);
        	taskDefBase.setProcessDefinitionId(processDefinitionId);
            taskDefBase.setProcessDefinitionKey(processDefinitionKey);
            taskDefBase.setProcessDefinitionVersion(processDefinitionVersion);
        }
        
        if (StringUtils.isNotBlank(taskDefBase.getProcessDefinitionId())) {
            taskDefBase.setProcessDefinitionId(processDefinitionId);
        }

        taskDefBase.setName(taskDefinition.getName());
        taskDefBase.setAssignStrategy(taskDefinition.getAssignStrategy());
        
        String formKey = taskDefinition.getFormKey();
        if(StringUtils.isNotBlank(formKey)){
        	taskDefBase.setFormKey(formKey);
        	taskDefBase.setFormType(taskDefinition.getFormType());
        }

        OaBpmConfCountersign countersign = taskDefinition.getCountersign();
        if(countersign!=null){
        	taskDefBase.setCountersignRate(countersign.getRate());
        	taskDefBase.setCountersignStrategy(countersign.getStrategy());
        	taskDefBase.setCountersignUser(countersign.getParticipant());
        	Integer type = countersign.getType();
        	if(type!=null && type==1){
        		taskDefBase.setCountersignType("sequential");
        	}else{
        		taskDefBase.setCountersignType("parallel");
        	}
        }
        taskDefaultService.saveOrUpdate(taskDefBase);
        
        //2.任务定义用户数据
        for(OaTaskDefUser taskUser:taskDefinition.getTaskUsers()){
        	String value = taskUser.getValue();
            String type = taskUser.getType();
            String catalog = taskUser.getCatalog();
            if(StringUtils.isBlank(value)){
            	continue;
            }
            String baseId = taskDefBase.getId();
            String userHql="from OaTaskDefUser where baseId=? and value=? and type=? and catalog=? and del_flg=0 and stop_flg=0";
            OaTaskDefUser user = taskDefaultService.findUnique(userHql, new OaTaskDefUser(),baseId,value,type,catalog);
            if(user!=null){
            	continue;
            }
            user = new OaTaskDefUser();
            user.setType(type);
            user.setCatalog(catalog);
            user.setValue(value);
            user.setBaseId(baseId);
            taskDefaultService.saveOrUpdate(user);
        }
        
	}
}
