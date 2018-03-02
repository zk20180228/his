package cn.honry.oa.humantask.dao;

import java.util.List;

import cn.honry.base.bean.model.OaBpmConfForm;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaFormInfo;
import cn.honry.base.bean.model.OaKVProp;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface HumanTaskDao extends EntityDao<OaKVRecord>{
	/**
	 * 根据id查询流程定义实体    草稿箱
	 */
	OaKVRecord queryOaKVRecordById(String id);
	/**
	 * 根据id查询流程定义实体
	 */
	OaBpmProcess queryOaBpmProcessById(String id);
	/**
	 * 根据formCode查询OaFormInfo
	 */
	OaFormInfo queryOaFormInfoById(String formCode);
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
	 * 获取所有已处理节点
	 * @param processInstanceId
	 * @param taskId
	 * @return
	 */
	List<OaTaskInfo> findAllPreviousActivities(String processInstanceId,String taskId);
	
	/**
	 * 获取员工名(扩展表)
	 * @param jobNo
	 * @return
	 */
	String getEmpExtendName(String jobNo);
	
	/**
	 * 获取负责人名称
	 * @param jobNo
	 * @return
	 */
	String getAssigneeName(String jobNo);
	
	/**
	 * 获取已处理节点code和name(去重)
	 * @param processInstanceId 流程实例id
	 * @return
	 */
	List<OaTaskInfo> findAllPreviousActivitieCode(String processInstanceId);
	
	/**
	 * 根据节点id获取任务节点信息(有可能有多个,因为有驳回)
	 * @param processInstanceId
	 * @param activityId
	 * @return
	 */
	List<OaTaskInfo> findActivitiesByActId(String processInstanceId,String activityId);
	
	/**
	 * 根据流程实例获取已完成的任务
	 * 根据创建时间和任务id正序排列
	 * @param processInstanceId
	 * @return
	 */
	List<OaTaskInfo> findCompleteActs(String processInstanceId);
	
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
	 * 撤回流程只能由撤回人进行提交
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2018年1月22日 下午8:28:54 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2018年1月22日 下午8:28:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id:
	 * @throws:
	 * @return: void
	 *
	 */
	void changeAssignee(String id,String assignee,String assigneeName);
}
