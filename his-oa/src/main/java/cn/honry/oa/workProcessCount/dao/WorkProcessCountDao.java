package cn.honry.oa.workProcessCount.dao;

import java.util.List;

import cn.honry.oa.workProcessCount.vo.WorkProcessCountVo;

/**
 * 
 * <p>工作流程统计的Dao接口 </p>
 * @Author: zhangkui
 * @CreateDate: 2017年7月19日 下午7:02:31 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年7月19日 下午7:02:31 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public interface WorkProcessCountDao {

	//工作列表
	List<WorkProcessCountVo> workList(String workFlag, String serialNumber,String workName, String page, String rows);
	
	//新建工作
	public void addWork(String workFlag, WorkProcessCountVo workProcessCountVo);
	
	/**
	 * 
	 * <p>取消关注：根据执行id删除关注表中对应的记录 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月28日 上午11:09:34 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月28日 上午11:09:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param exectionId 运行中的流程实例的执行id 
	 * @throws:
	 *
	 */
	public void deleteAttention(String[] exectionId)throws Exception;
	
	/**
	 * 
	 * <p> 一键关注,在自定义表中维护当前用户的id，运行中的流程实例的ID</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月28日 下午4:21:43 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月28日 下午4:21:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param userId 当前用户的id
	 * @param workId 运行的流程实例的id
	 * @throws:
	 *
	 */
	public void attention(String userId, String[] workId)throws Exception;
	
	//挂起恢复
	public void hangUpRecoveryWork(String workFlag, String[] id);
    
	//收回委托
	public void takeBackEntrustWork(String workFlag, String[] id);
   
	//催办
	public void urgeDoWork(String workFlag, String[] id);
     
	//删除
	public void delWork(String workFlag, String[] id);
    
	//导出
	public void exportWork(String workFlag, String[] id);
     
	//主办
	public void hostWork(String workFlag, String[] id);
     
	//批注
	public void postilWork(String workFlag, String[] id);
    
	//挂起
	public void hangUpwork(String workFlag, String[] id);
    
	//委托
	public void entrustWork(String workFlag, String[] id);
	
	/**
	 * 
	 * <p>根据当前登录人的id查询关注表中的EXECTIONID </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月24日 下午6:43:52 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月24日 下午6:43:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param userId 当前登录人的id
	 * @return List<String>执行任务的id
	 * @throws:
	 *
	 */
	public List<String> getTaskExecutionId(String userId);

}
