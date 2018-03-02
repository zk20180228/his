package cn.honry.oa.activiti.listener;

import java.util.Date;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.IdentityLink;

import cn.honry.base.bean.model.OaBpmConfUser;
import cn.honry.oa.activiti.bpm.user.service.OaBpmConfUserService;
import cn.honry.oa.activiti.bpm.user.service.impl.OaBpmConfUserServiceImpl;
import cn.honry.oa.activiti.bpm.utils.ApplicationContextHelper;
import cn.honry.oa.activiti.bpm.utils.BeanMapper;
import cn.honry.oa.humantask.service.HumanTaskService;
import cn.honry.oa.humantask.service.impl.HumanTaskServiceImpl;
import cn.honry.oa.humantask.vo.DelegateTaskHolder;
import cn.honry.oa.humantask.vo.HumanTaskBuilder;
import cn.honry.oa.humantask.vo.HumanTaskConstants;
import cn.honry.oa.humantask.vo.HumanTaskVo;
import cn.honry.oa.humantask.vo.ParticipantVo;

/**
 * 任务监听器
 * @author luyanshou
 *
 */
public class HumanTaskEventListener implements ActivitiEventListener {

	public static final int TYPE_COPY = 3;
	
	private BeanMapper beanMapper = new BeanMapper();
	
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
        try {
			switch (event.getType()){
			case TASK_CREATED:
				onCreate(taskEntity);
				break;
			case TASK_ASSIGNED:
				onAssign(taskEntity);
				break;
			case TASK_COMPLETED:
				onComplete(taskEntity);
				break;
			case ENTITY_DELETED:
				onDelete(taskEntity);
				break;
			default: 
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isFailOnException() {
		return false;
	}

	public void onCreate(DelegateTask delegateTask){
		HumanTaskVo vo = null;
		
		//根据delegateTask 创建一个 HumanTaskVo对象
		try {
			DelegateTaskHolder.setDelegateTask(delegateTask);
			vo = createHumanTask(delegateTask);
			checkCopyHumanTask(delegateTask, vo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
            DelegateTaskHolder.clear();
        }
		
		if (vo != null) {
            delegateTask.setAssignee(vo.getAssignee());
            delegateTask.setOwner(vo.getOwner());
        }
	}
	
	
	public void onAssign(DelegateTask delegateTask){
		
	}
	
	/**
     * 如果直接完成了activiti的task，要同步完成HumanTask.
     * @param delegateTask
     */
	public void onComplete(DelegateTask delegateTask){
		HumanTaskService humanTaskService = ApplicationContextHelper.getBean(HumanTaskServiceImpl.class);
		HumanTaskVo humanTaskVo = humanTaskService.findHumanTaskByTaskId(delegateTask.getId());
		if ("complete".equals(humanTaskVo.getStatus())) {
            return;
        }
        humanTaskVo.setStatus("complete");
        humanTaskVo.setCompleteTime(new Date());

        if ("start".equals(humanTaskVo.getCatalog())) {
            humanTaskVo.setAction("提交");
        } else {
            humanTaskVo.setAction("完成");
        }
        humanTaskService.saveHumanTask(humanTaskVo, false);
	}
	
	public void onDelete(DelegateTask delegateTask){
		HumanTaskService humanTaskService = ApplicationContextHelper.getBean(HumanTaskServiceImpl.class);
		HumanTaskVo humanTaskVo = humanTaskService.findHumanTaskByTaskId(delegateTask.getId());
		if (humanTaskVo == null) {
            return;
        }

        if ("complete".equals(humanTaskVo.getStatus())) {
            return;
        }

        humanTaskVo.setStatus("delete");
        humanTaskVo.setCompleteTime(new Date());
        humanTaskVo.setAction("人工终止");
        humanTaskVo.setOwner(humanTaskVo.getAssignee());
        //humanTaskVo.setAssignee(Authentication.getAuthenticatedUserId());
        humanTaskService.saveHumanTask(humanTaskVo, false);
        
	}
	
	public HumanTaskVo createHumanTask(DelegateTask delegateTask){
		HumanTaskVo vo = new HumanTaskBuilder().setDelegateTask(delegateTask).setVote(this.isVote(delegateTask)).build();
		HumanTaskService humanTaskService = ApplicationContextHelper.getBean(HumanTaskServiceImpl.class);
		HumanTaskVo taskVo = humanTaskService.saveHumanTask(vo);
		String humanTaskId = taskVo.getId();//保存任务
		
		for (IdentityLink identityLink : delegateTask.getCandidates()){
			String type = identityLink.getType();
			ParticipantVo participantVo= new ParticipantVo();
			participantVo.setType(type);
			participantVo.setHumanTaskId(humanTaskId);
			if ("user".equals(type)){
				 participantVo.setCode(identityLink.getUserId());
			}else{
				participantVo.setCode(identityLink.getGroupId());
			}
			humanTaskService.saveParticipant(participantVo);//保存任务参与者
		}
		
		return taskVo;
	}
	
	/**
     * 是否会签任务.
     */
    public boolean isVote(DelegateTask delegateTask) {
        ExecutionEntity executionEntity = (ExecutionEntity) delegateTask
                .getExecution();
        ActivityImpl activityImpl = executionEntity.getActivity();

        return activityImpl.getProperty("multiInstance") != null;
    }
    
    /**
     * 抄送任务
     * @param delegateTask
     * @param humanTaskVo
     */
    public void checkCopyHumanTask(DelegateTask delegateTask,
            HumanTaskVo humanTaskVo){
    	String activityId = delegateTask.getExecution().getCurrentActivityId();
    	OaBpmConfUserService bpmConfUserService = ApplicationContextHelper.getBean(OaBpmConfUserServiceImpl.class);
    	List<OaBpmConfUser> list = bpmConfUserService.getConfUserList(delegateTask.getProcessDefinitionId(), activityId);
    	
    	ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
    	
    	if(list!=null && list.size()>0){
    		for (OaBpmConfUser bpmConfUser : list) {
    			String value = expressionManager.createExpression(bpmConfUser.getValue())
                        .getValue(delegateTask).toString();
    			Integer status = bpmConfUser.getStatus();
    			Integer type = bpmConfUser.getType();
    			if (status!=null && status == 1){
    				if(type!=null && type==TYPE_COPY){
    					// 创建新任务
    			        HumanTaskVo vo = new HumanTaskVo();
    			        beanMapper.copy(humanTaskVo,vo);
    			        vo.setId(null);
    			        vo.setCategory("copy");
    			        vo.setAssignee(value);
    			        vo.setCatalog(HumanTaskConstants.CATALOG_COPY);
    			        HumanTaskService humanTaskService = ApplicationContextHelper.getBean(HumanTaskServiceImpl.class);
    			        humanTaskService.saveHumanTask(vo);
    				}
    			}
			}
    	}
    }
}
