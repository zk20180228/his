package cn.honry.oa.activiti.listener;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.oa.activiti.task.service.TaskDefaultService;
import cn.honry.oa.humantask.vo.HumanTaskConstants;


public class TaskConfUserHumanTaskListener implements HumanTaskListener {

	@Autowired
	@Qualifier(value = "taskDefaultService")
	private TaskDefaultService taskDefaultService;

	public void setTaskDefaultService(TaskDefaultService taskDefaultService) {
		this.taskDefaultService = taskDefaultService;
	}
	
	@Override
	public void onCreate(OaTaskInfo taskInfo) throws Exception {

		if (HumanTaskConstants.CATALOG_COPY.equals(taskInfo.getCatalog())) {//抄送
            return;
        }

        String taskDefinitionKey = taskInfo.getCode();
        String businessKey = taskInfo.getBusinessKey();
        String hql= "select t.value from OaTaskConfUser t where code=? and businessKey=? and t.stop_flg=0 and del_flg=0 ";
        String value = taskDefaultService.findUnique(hql, "", taskDefinitionKey,businessKey);
        if(StringUtils.isNotBlank(value)){
        	taskInfo.setAssignee(value);//设置负责人
        }
	}

	@Override
	public void onComplete(OaTaskInfo taskInfo) throws Exception {

	}

}
