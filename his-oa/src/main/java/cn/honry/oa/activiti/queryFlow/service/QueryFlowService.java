package cn.honry.oa.activiti.queryFlow.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.OaBpmCategory;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.base.bean.model.OaReminders;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.service.BaseService;
import cn.honry.utils.TreeJson;

public interface QueryFlowService extends BaseService<OaTaskInfo>{
	/**
	 * 查询人工任务   根据流程
	 */
	OaTaskInfo querybyInstanceId(String instanceId);
	/**
	 * 查询人工任务   根据流程
	 */
	List<OaTaskInfo> queryOaTaskInfo();
	/**
	 * 查询草稿列表
	 */
	List<OaKVRecord> queryOaKVRecord(String id,int page,int rows,String category,String startTime,String endTime);
	/**
	 * 查询草稿列表条数
	 */
	int queryOaKVRecordtotal(String id,String category,String startTime,String endTime);
	/**
	 * 未结流程
	 */
	List<OaTaskInfo> queryOaTaskInfoVAct(String param,int page,int rows,String category,String startTime,String endTime);
	/**未结流程
	 * @param param
	 * @param category
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int queryOaTaskInfoVActTotal(String param,String category,String startTime,String endTime);
	List<OaReminders>  queryList(String userAccount,String startTime, String endTime, String title,int page,int rows);
	List<OaReminders>  queryList1(String userAccount,String startTime, String endTime, String title,int page,int rows);
	/**
	 * 退件箱
	 */
	List<OaTaskInfo> tuijianqueryOaTaskInfoVAct(String param,int page,int rows,String category,String startTime,String endTime);
	/**退件箱总数
	 * @param param
	 * @param category
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int tuijianqueryOaTaskInfoVActTotal(String param,String category,String startTime,String endTime);
	Map<String, Object> savecuiban(String processInstanceId,String name,String attr2,String code,String assignee,String remindcontent,String taskInfoId);
	Map<String, Object> updatecuiban(String processInstanceId,String code);
	Map<String, Object> updateHuifu(String processInstanceId,String code,String neiron);
	/**
	 * 查询待办数据
	 * @Author: zpty
	 * @CreateDate: 2017年8月12日 下午7:35:26 
	 * @param account 当前登录人
	 * @param tenantId 租用人
	 * @version: V1.0
	 * @throws:
	 * @return: List<OaTaskInfo> 返回值类型
	 */
	List<OaTaskInfo> getListForTask(String account, String tenantId);
	/**
	 * 我收到的催办
	 * @param userAccount
	 * @return
	 */
	List<OaReminders>  queryListcui(String userAccount);
	/**
	 * 历史记录
	 */
	List<OaTaskInfo> querylishijili(String param);
	/**
	 * 已结流程
	 */
	List<OaTaskInfo> querylistyijie(String param,int page,int rows,String category,String startTime,String endTime);
	int querylistyijieTotal(String param,String category,String startTime,String endTime);
	/**
	 * 查询流程分类
	 */
	List<OaBpmCategory> quert();
	void deleteMyCuiBan(String rowsid);
	/**根据实例id删除催办
	 * @param processid
	 */
	void deleteMyCuiBanByProcess(String processid);
	/**根据实例id获取催办
	 * @param processid
	 * @return
	 */
	List<OaReminders> getMyCuiBanByProecssId(String processid);
	
	/**
	 * 查询待办数据
	 * @Author: zpty
	 * @CreateDate: 2017年8月12日 下午7:35:26 
	 * @param account 当前登录人
	 * @param tenantId 租用人
	 * @version: V1.0
	 * @throws:
	 * @return: List<OaTaskInfo> 返回值类型
	 */
	List<OaTaskInfo> getListForTask(String account, String tenantId, String startTime, String endTime, String title, int page, int rows);

	int getNumberForTask(String account, String tenantId, String startTime, String endTime, String title);

	/**
	 * 历史记录
	 */
	List<OaTaskInfo> querylishijili(String startTime, String endTime, String title, int page, int rows);
	int querylishijiliNum(String startTime, String endTime, String title);

	Integer  queryListSize(String userAccount,String startTime, String endTime, String title);

	int queryNum(String startTime, String endTime, String title);
	/**
	 * 删除草稿箱记录
	 * @param rowsid
	 */
	void deleteMyGao(String rowsid);
	/**新建业务左侧树
	 * @return
	 */
	List<TreeJson> getXinJianTree();
	/**
	 * 根据流程实例和code查询info表数据
	 */
	OaTaskInfo queryOaTaskInfo(String processInstanceId,String code);
}
