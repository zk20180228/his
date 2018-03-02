package cn.honry.oa.activiti.task.service;

import cn.honry.oa.activiti.task.vo.TaskDefinitionVo;

public interface TaskDefinitionService {

	/**
	 * 添加任务定义相关数据
	 * @param taskDefinition
	 */
	void create(TaskDefinitionVo taskDefinition);
}
