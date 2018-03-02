package cn.honry.oa.humantask.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.EmployeeExtend;
import cn.honry.base.bean.model.OaBpmConfForm;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaFormInfo;
import cn.honry.base.bean.model.OaKVProp;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.oa.activiti.bpm.form.vo.ConfFormVo;
import cn.honry.oa.activiti.bpm.form.vo.FormParameterVo;
import cn.honry.oa.activiti.bpm.utils.ExtendVo;
import cn.honry.oa.humantask.vo.HumanTaskVo;
import cn.honry.oa.humantask.vo.ParticipantVo;

public interface HumanTaskService {

	/**
	 * 流程发起之前，配置每个任务的负责人.
	 * @param businessKey 业务标识
	 * @param taskDefinitionKeys 任务定义key
	 * @param taskAssignees
	 */
	void configTaskDef(String businessKey,List<String> taskDefinitionKeys, List<String> taskAssignees);
	/**
	 * 保存任务
	 * @param humanTaskVo
	 * @return
	 */
	HumanTaskVo saveHumanTask(HumanTaskVo humanTaskVo);
	
	/**
	 * 保存任务参与者
	 * @param vo
	 */
	void saveParticipant(ParticipantVo vo);
	
	/**
	 * 根据任务id获取vo对象
	 * @param taskId
	 * @return
	 */
	HumanTaskVo findHumanTaskByTaskId(String taskId);
	
	/**
	 * 保存/更新任务
	 * @param humanTaskVo
	 * @return
	 */
	HumanTaskVo saveHumanTask(HumanTaskVo humanTaskVo,boolean triggerListener);
	
	/**
	 * 根据id获取任务vo对象
	 * @param id
	 * @return
	 */
	HumanTaskVo findHumanTask(String id);
	HumanTaskVo findLastHumanTaskByTaskId(String taskId);
	List<HumanTaskVo> findHumanTaskListByTaskId(String taskId);
	/**
	 * 获取任务表单
	 * @param humanTaskVo 
	 * @return
	 */
	ConfFormVo getFormVo(HumanTaskVo humanTaskVo);
	
	/**
	 * 根据流程实例id获取任务vo对象
	 * @param processInstanceId
	 * @return
	 */
	 List<HumanTaskVo> findHumanTasksByProcessInstanceId(String processInstanceId);
	 /**
	  * 查找撤回的最新的两条记录
	  * @param processInstanceId
	  * @return
	  */
	 List<HumanTaskVo> findHumanTasksByProcessInstanceIdRecall(String processInstanceId);
	 
	 /**
	  * 根据ref获取记录
	  * @param ref
	  * @return
	  */
	 OaKVRecord getRecord(String ref);
	 
	 /**
	  * 根据父级id获取任务vo对象
	  * @param parentId
	  * @return
	  */
	 List<HumanTaskVo> getHumanTask(String parentId);
	 
	 /**
	  * 保存草稿
	  * @param userId 登录用户
	  * @param deptCode 登录科室
	  * @param tenantId 租户id
	  * @param formParameter 表单参数
	  * @return
	  */
	  String saveDraft(String userId,String deptCode,String tenantId,FormParameterVo formParameter);
	  
	 /**
	  * 获取负责人列表
	  * @param processDefinitionId
	  * @param code
	  * @param account
	  * @param processInstanceId
	  * @param businessKey
	  * @return
	  */
	  List<EmployeeExtend> selectAssignee(String processDefinitionId,String code,String account,String processInstanceId,String businessKey);
	  
	  /**
	   * 完成任务
	   * @param humanTaskId
	   * @param userId
	   * @param formParameter
	   * @param taskParameters
	   * @param record
	   * @param processInstanceId
	   */
	  void completeTask(String humanTaskId, String userId,FormParameterVo formParameter,
			  Map<String, Object> taskParameters,OaKVRecord record, String processInstanceId);
	  
   /**
	* 根据id查询表单
	*/
	OaKVRecord queryOaKVRecordById(String id);
	/**
	 * 根据id查询流程定义实体
	 */
	OaBpmProcess queryOaBpmProcessById(String id);
	/**
	 * 根据id查询流程定义实体
	 */
	OaFormInfo queryOaFormInfoById(String formCode)  throws SQLException, IOException;
	/**
	 * 根据formCode查询OaKVProp
	 */
	List<OaKVProp> queryOaKVPropById(String recordId);
	/**
	 * 查询表单配置实体
	 */
	List<OaBpmConfForm> queryOaBpmConfFormBybaseId(String baseId);
	
	/**
	 * 查询上一个节点
	 * @param processInstanceId
	 * @param taskId
	 * @return
	 */
	String findPreviousActivities(String processInstanceId,String taskId);
	
	/**
	 * 获取所有已通过的节点
	 * @param processInstanceId 流程实例id
	 * @param taskId 任务id
	 * @param activityId 节点id
	 * @return
	 */
	Map<String,String> getAllPrevious(String processInstanceId, String taskId,String activityId);
	
	OaTaskInfo findPrevious(String processInstanceId, String taskId);
	
	/**
	 * 回退到任务发起人
	 * @param humanTaskId
	 */
	void rollbackInitiator(String humanTaskId);
	
	/**
	 * 回退到指定节点
	 * @param processInstanceId 流程实例id
	 * @param activityId 要退回的节点id 
	 * @param taskId 当前活动节点的任务id
	 */
	void rollback(String processInstanceId,String activityId,String taskId);
	
	/**
	 * 根据实例id和任务id获取该任务之前已完成的任务
	 * @param processInstanceId 流程实例id
	 * @param taskId 任务id
	 * @return
	 */
	List<OaTaskInfo> findAllPreviousActivities(String processInstanceId, String taskId);
	
	 /**
	  * 获取扩展的规则
	  * @param processDefinitionId
	  * @param code
	  * @return
	  */
	 ExtendVo getConfRule(String processDefinitionId,String code);
	 
	 /**
	  * 根据流程实例id删除当前活动的节点
	  * @param processInstanceId 流程实例id
	  */
	void removeHumanTaskByProcessInstanceId(String processInstanceId);
	
	/**
	 * 根据流程任务id删除当前活动的节点
	 * @param taskId 任务id
	 */
	void removeHumanTaskByTaskId(String taskId);
	
	/**
	 * 撤销任务
	 * @param taskId 任务id
	 */
	String withdrawTask(String taskId);
	/**
	 * 完成任务/回退
	 * @param userId
	 * @param deptCode
	 * @param tenantId
	 * @param formParameter
	 * @param taskParameters
	 */
	void complete(String userId,String deptCode,String tenantId,FormParameterVo formParameter,Map<String, Object> taskParameters);
}
