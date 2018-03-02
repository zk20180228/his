package cn.honry.oa.activiti.listener;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cn.honry.base.bean.model.OaTaskDefBase;
import cn.honry.base.bean.model.OaTaskDefUser;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.bean.model.OaTaskParticipant;
import cn.honry.oa.activiti.bpm.utils.ApplicationContextHelper;
import cn.honry.oa.activiti.task.service.TaskDefaultService;
import cn.honry.oa.activiti.task.service.impl.TaskDefaultServiceImpl;
import cn.honry.oa.humantask.vo.HumanTaskConstants;

/**
 * 监听器-->在新增用户任务时设置相应的任务参与者
 * 即:在T_OA_TASK_INFO表中插入一条数据时,同时在T_OA_TASK_PARTICIPANT中插入数据
 * @author luyanshou
 *
 */
public class TaskDefUserHumanTaskListener implements HumanTaskListener {

	@Override
	public void onCreate(OaTaskInfo taskInfo) throws Exception {

		if (HumanTaskConstants.CATALOG_COPY.equals(taskInfo.getCatalog())) {
            return;
        }
		String taskDefinitionKey = taskInfo.getCode();
        String processDefinitionId = taskInfo.getProcessDefinitionId();
        TaskDefaultService taskDefaultService = ApplicationContextHelper.getBean(TaskDefaultServiceImpl.class);
        String hql1=" from OaTaskDefBase b where b.code=? and b.processDefinitionId=? and b.stop_flg=0 and b.del_flg=0";
        List<OaTaskDefBase> bases = taskDefaultService.getList(hql1, new OaTaskDefBase(), taskDefinitionKey,processDefinitionId);
        String baseId="";
        if(bases!=null && bases.size()>0){
        	OaTaskDefBase taskDefBase = bases.get(0);
        	baseId=taskDefBase.getId();
        }
        StringBuffer hql=new StringBuffer("from OaTaskDefUser u where u.baseId=? and u.stop_flg=0 and u.del_flg=0 ");
        List<OaTaskDefUser> list = taskDefaultService.getList(
        		hql.toString(),new OaTaskDefUser(),baseId);
        if(list!=null && list.size()>0){
        	for (OaTaskDefUser taskUser : list) {
        		if("disable".equals(taskUser.getStatus())){
        			continue;
        		}
        		String catalog = taskUser.getCatalog();
                String type = taskUser.getType();
                String value = taskUser.getValue();
                if ("assignee".equals(catalog) && StringUtils.isNotBlank(value)) {//负责人
                    taskInfo.setAssignee(value);
                } else if ("candidate".equals(catalog)) {//候选人
                    OaTaskParticipant taskParticipant = new OaTaskParticipant();
                    taskParticipant.setCategory(catalog);
                    taskParticipant.setRef(value);
                    taskParticipant.setType(type);
                    taskParticipant.setTaskId(taskInfo.getId());
                    taskDefaultService.saveOrUpdate(taskParticipant);;
                }
			}
        }
	}

	@Override
	public void onComplete(OaTaskInfo taskInfo) throws Exception {

	}

}
