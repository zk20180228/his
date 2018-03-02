package cn.honry.oa.activiti.queryFlow.dao;

import java.util.List;

import cn.honry.base.bean.model.OaActivitiDept;
import cn.honry.base.bean.model.OaBpmCategory;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.base.bean.model.OaReminders;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.dao.EntityDao;
import cn.honry.oa.activiti.queryFlow.vo.OaTaskInfoVAct;
/**  
 *  
 * @Description：  流程查询
 * @Author：donghe
 * @CreateDate：2017-7-17 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
import cn.honry.oa.activiti.queryFlow.vo.remindersVo;

@SuppressWarnings({ "all" })
public interface QueryFlowDao extends EntityDao<OaTaskInfo> {
	/**
	 * 查询人工任务 根据流程
	 */
	OaTaskInfo querybyInstanceId(String instanceId);

	/**
	 * 查询人工任务 根据流程
	 */
	List<OaTaskInfo> queryOaTaskInfo();

	/**
	 * 查询草稿列表
	 */
	List<OaKVRecord> queryOaKVRecord(String id, int page, int rows,
			String category, String startTime, String endTime);

	/**
	 * 查询草稿列表条数
	 */
	int queryOaKVRecordtotal(String id, String category, String startTime,
			String endTime);

	/**
	 * 未结流程
	 */
	List<OaTaskInfo> queryOaTaskInfoVAct(String param, int page, int rows,
			String category, String startTime, String endTime);

	int queryOaTaskInfoVActTotal(String param, String category,
			String startTime, String endTime);

	/**
	 * 退件箱
	 */
	List<OaTaskInfo> tuijianqueryOaTaskInfoVAct(String param, int page,
			int rows, String category, String startTime, String endTime);

	int tuijianqueryOaTaskInfoVActToal(String param, String category,
			String startTime, String endTime);

	int queryListSize(String userAccount, String startTime, String endTime,
			String title);

	List<OaReminders> queryListpan(String taskInfoId);

	List<OaReminders> queryListId(String id);

	/**
	 * 查询待办数据
	 * 
	 * @Author: zpty
	 * @CreateDate: 2017年8月12日 下午7:35:26
	 * @param account
	 *            当前登录人
	 * @param tenantId
	 *            租用人
	 * @version: V1.0
	 * @throws:
	 * @return: List<OaTaskInfo> 返回值类型
	 */
	List<OaTaskInfo> getListForTask(String account, String tenantId);

	/**
	 * 我收到的催办
	 * 
	 * @param userAccount
	 * @return
	 */
	List<OaReminders> queryListcui(String userAccount);

	/**
	 * 历史记录
	 */
	List<OaTaskInfo> querylishijili(String param);

	/**
	 * 已结流程
	 */
	List<OaTaskInfo> querylistyijie(String param, int page, int rows,
			String category, String startTime, String endTime);

	int querylistyijieTotal(String param, String category, String startTime,
			String endTime);

	/**
	 * 查询流程分类
	 */
	List<OaBpmCategory> quert();

	/**
	 * 根据id删除催办
	 * 
	 * @param id
	 */
	void deleteMyCuiBan(String id);

	/**
	 * 根据实例ID删除催办
	 * 
	 * @param processId
	 */
	void deleteMyCuiBanByProcess(String processId);

	/**
	 * 根据实例id获取催办
	 * 
	 * @param processid
	 * @return
	 */
	List<OaReminders> getMyCuiBanByProecssID(String processid);

	/**
	 * 查询待办数据
	 * 
	 * @Author: zpty
	 * @CreateDate: 2017年8月12日 下午7:35:26
	 * @param account
	 *            当前登录人
	 * @param tenantId
	 *            租用人
	 * @version: V1.0
	 * @throws:
	 * @return: List<OaTaskInfo> 返回值类型
	 */
	List<OaTaskInfo> getListForTask(String account, String tenantId,
			String startTime, String endTime, String title, int page, int rows);

	int getNumberForTask(String account, String tenantId, String startTime,
			String endTime, String title);

	/**
	 * 历史记录
	 */
	List<OaTaskInfo> querylishijili(String startTime, String endTime,
			String title, int page, int rows);

	int querylishijiliNum(String startTime, String endTime, String title);

	List<OaReminders> queryList1(String userAccount, String startTime,
			String endTime, String param, int page, int rows);

	List<OaReminders> queryList(String userAccount, String startTime,
			String endTime, String param, int page, int rows);

	int queryNum(String startTime, String endTime, String title);

	/**
	 * 删除草稿箱记录
	 * 
	 * @param id
	 */
	void deleteMyGao(String id);

	/**
	 * 根据流程实例ID查询流程定义ID
	 * 
	 * @param processInstanceId
	 * @return
	 */
	List<String> queryProcessDefinitionIdByProcessInstanceId(
			String processInstanceId);

	/**
	 * 获取所有的流程分类
	 * 
	 * @return
	 */
	List<OaBpmCategory> getAllOaBpmCategory();

	/**
	 * 获取所有工作流科室
	 * 
	 * @return
	 */
	List<OaActivitiDept> getAllOaActivitiDept();

	/**
	 * 根据流程实例和code查询info表数据
	 */
	OaTaskInfo queryOaTaskInfo(String processInstanceId, String code);

	/**
	 * 根据实例id查询催办次数（已结）
	 */
	int queryRemindernum(String processInstanceId);

}
