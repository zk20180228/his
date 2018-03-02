package cn.honry.oa.workProcessCount.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.oa.workProcessCount.constant.WorkProcessCountConstant;
import cn.honry.oa.workProcessCount.dao.WorkProcessCountDao;
import cn.honry.oa.workProcessCount.service.WorkProcessCountService;
import cn.honry.oa.workProcessCount.vo.WorkProcessCountVo;
import cn.honry.utils.DateUtils;

@Transactional
@Service("workProcessCountService")
public class WorkProcessCountServiceImpl implements WorkProcessCountService {

	@Autowired
	@Qualifier("workProcessCountDao")
	private WorkProcessCountDao workProcessCountDao;
	public void setWorkProcessCountDao(WorkProcessCountDao workProcessCountDao) {
		this.workProcessCountDao = workProcessCountDao;
	}
	
	@Resource
	private ProcessEngine processEngine;
	public void setProcessEngine(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}
	
	
	
	
	@Override
	public Map<String,Object> workList(String userId,String workFlag,String serialNumber, String workName, String page, String rows) throws Exception {
		
		//一,待办任务----运行中的任务表所有符合条件的记录，但是排除挂起状态的任务
		if(WorkProcessCountConstant.WORK_FLAG_TODO.equals(workFlag)){//待办任务，查询运行中的任务表
			
			//查询运行中的任务-->act_ru_task(运行时任务数据表)
			//1.创建任务查询对象
			TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
			
			//2.添加过滤条件，分页
			taskQuery.taskAssignee(userId);//任务的执行人
			if(StringUtils.isNotBlank(serialNumber)){
				taskQuery.executionId(serialNumber);//任务的流水号，这里我认为是运行中流程实例的执行id
			}
			ProcessDefinition pd=null;
			if(StringUtils.isNotBlank(workName)){
				//查询根据流程定义的key查询流程定义的id
				//1.获得流程定义查询对象
				ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService().createProcessDefinitionQuery();
				//2.根据流程定义的名字模糊查询流程定义
				processDefinitionQuery.processDefinitionNameLike("%"+workName+"%");//（在这里根据实际数据为工程名/文号）
				List<ProcessDefinition> list = processDefinitionQuery.list();
				if(list!=null&&list.size()>0){
					pd=list.get(0);//多个匹配只取其中的一个
					
					//这个需要严格的测试，我也不知道能用不=================================================
					taskQuery.processDefinitionId(pd.getId());//根据流程定义的id,查询任务列表
				}else{
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("total", 0);
					map.put("rows", new ArrayList());
					return map;
				}
				
			}
			
			taskQuery.active();//任务是激活状态，可处理状态
			
			//可以在这里添加排序条件：======================================
			taskQuery.orderByTaskCreateTime().desc();//按照流程的创建时间倒序
			long totalPages = taskQuery.count();//总记录数
			
			if(StringUtils.isNotBlank(page)&&StringUtils.isNotBlank(rows)){//有分页条件时
				//分页
				int pages=Integer.parseInt(rows);//每页显示多少条
				int start=(Integer.parseInt(page)-1)*pages;//从第几行开始查
				taskQuery.listPage(start, pages);//分页
			}
		
			//3.查询列表
			List<Task> list = taskQuery.list();
			//将task中的数据转换为我们需要的Vo：WorkProcessCountVo
			String processDefinitionName=null;
			if(pd!=null){
				processDefinitionName=pd.getName();
			}
			
			List<WorkProcessCountVo> rsList =runTaskConvertWPCV(list,processDefinitionName);
			Map<String,Object> map= new HashMap<String, Object>();
			map.put("total", totalPages);
			map.put("rows", rsList);
		
			return map;
		}
		
		//办结任务
		if(WorkProcessCountConstant.WORK_FLAG_YEDO.equals(workFlag)){//办结任务,查询历史表
				
			//查询历史任务表 act_hi_taskinst（历史任务 流程实例信息）核心表
			//1.创建历史任务查询对象
			HistoricTaskInstanceQuery historicTaskInstanceQuery = processEngine.getHistoryService().createHistoricTaskInstanceQuery();
			
			//2.添加过滤条件
			historicTaskInstanceQuery.finished();//查询已办结的任务
			historicTaskInstanceQuery.taskAssignee(userId);//受理人
			
			if(StringUtils.isNotBlank(serialNumber)){
				historicTaskInstanceQuery.executionId(serialNumber);//任务的流水号，这里我认为是运行中流程实例的执行id 
			}
			ProcessDefinition pd=null;
			if(StringUtils.isNotBlank(workName)){
				//查询根据流程定义的key查询流程定义的id
				//1.获得流程定义查询对象
				ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService().createProcessDefinitionQuery();
				//2.根据流程定义的名字模糊查询流程定义
				processDefinitionQuery.processDefinitionNameLike("%"+workName+"%");//（在这里根据实际数据为工程名/文号）
				List<ProcessDefinition> list = processDefinitionQuery.list();
				if(list!=null&&list.size()>0){
					pd=list.get(0);//多个匹配只取其中的一个
					//这个需要严格的测试，我也不知道能用不
					historicTaskInstanceQuery.processDefinitionId(pd.getId());//根据流程定义的id,查询任务列表
				}else{
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("total", 0);
					map.put("rows", new ArrayList());
					return map;
				}
				
			}
			
			//可以在这里添加排序条件：======================================
			historicTaskInstanceQuery.orderByHistoricTaskInstanceEndTime().desc();//根据任务的结束时间倒序
			long totalPages = historicTaskInstanceQuery.count();//总记录数
			
			if(StringUtils.isNotBlank(page)&&StringUtils.isNotBlank(rows)){//当都不为空时才分页，否则模拟分页
				//分页
				int pages=Integer.parseInt(rows);//每页显示多少条
				int start=(Integer.parseInt(page)-1)*pages;//从第几行开始查
				historicTaskInstanceQuery.listPage(start, pages);//分页
			}
			
			//3.查询办结任务的列表
			List<HistoricTaskInstance> list = historicTaskInstanceQuery.list();
			//将task中的数据转换为我们需要的Vo：WorkProcessCountVo
			String processDefinitionName=null;
			if(pd!=null){
				processDefinitionName=pd.getName();
			}
			
			//4.转换为前台需要的vo 
			List<WorkProcessCountVo> rsList =historyTaskConvertWPCV(list, processDefinitionName);
			Map<String,Object> map= new HashMap<String, Object>();
			map.put("total", totalPages);
			map.put("rows", rsList);

			return map;
			
		}
		
		//关注工作---->我个人现在这么做的：当前人点击关注是时，我会把当前登录人的id,任务执行的id,存入一个关注表（自定义的表），
		//当查询关注列表时，得到当前登录人的id,到关注表查询执行任务的id,再到运行中的任务表,历史任务表中查询任务实例	关注表名为：T_OA_EXPANDACTIVITI_ATTENTION
		//注意：现在我仅查了运行中的任务表，因为我觉得我的关注，历史数据没意义
		if(WorkProcessCountConstant.WORK_FLAG_ANDO.equals(workFlag)){//关注工作列表
			
			//根据当前登录的用户查询查询任务的执行id集合
			List<String> executionIds = getTaskExecutionId(userId);
			List<WorkProcessCountVo> rsList = new ArrayList<WorkProcessCountVo>();
			
			List<Task> runTasks = new ArrayList<Task>();
			
			//如果输入的运行中的流程实的执行id-->serialNumber不为空，判断该用户的执行id集合是否包含该id，如果包含，通过用户自己的执行id集合进行查询，否则，直接返回值
			boolean contains=false;
			if(StringUtils.isNotBlank(serialNumber)){
				contains = executionIds.contains(serialNumber);
			}
			
			if(StringUtils.isBlank(serialNumber)||contains){//如果流水号为空，那么查询该用户下的所有执行中的id
				//运行中的任务表查询任务
				for(String executionId:executionIds){
					//获得任务查询对象
					TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
					taskQuery.executionId(executionId);;//任务的流水号，这里我认为是运行中流程实例的执行id
					taskQuery.active();//任务是激活状态===========
					//先进行查询，如果运行中的任务无此流程实例执行id,那么可以认为是该流程实例已经完毕，因此，要删除自定义表中的对应的记录
					Task task = taskQuery.singleResult();
					if(task==null){
						//删除关注表中对应的记录
						deleteAttention(new String[]{executionId});
					}
					
					if(StringUtils.isNotBlank(workName)){
						//查询根据流程定义的key查询流程定义的id
						//1.获得流程定义查询对象
						ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService().createProcessDefinitionQuery();
						//2.根据流程定义的名字模糊查询流程定义
						processDefinitionQuery.processDefinitionNameLike("%"+workName+"%");//（在这里根据实际数据为工程名/文号）
						List<ProcessDefinition> list = processDefinitionQuery.list();
						ProcessDefinition pd=null;
						if(list!=null&&list.size()>0){
							pd=list.get(0);//多个匹配只取其中的一个
							//这个需要严格的测试，我也不知道能用不=================
							taskQuery.processDefinitionId(pd.getId());//根据流程定义的id,查询任务列表
						}else{
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("total", 0);
							map.put("rows", new ArrayList());
							return map;
						}
						
					}
					
					List<Task> list = taskQuery.list();//不一定是一条记录，比如当遇到并行网关时======================这个真不知道怎么处理？？
					
					runTasks.addAll(list);
				}
				
			}else{//如果不包含且不为空，直接返回
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("total", 0);
				map.put("rows", new ArrayList());
				return map;
			}
			
			//转换为前台需要的vo
			List<WorkProcessCountVo> wpcv = runTaskConvertWPCV(runTasks,null);
			
			//分页返回
			return analogPage(wpcv, page, rows);
		}
		
		
		
		//挂起工作---->就是查询挂起运行中流程实例对应的任务(流程实例和任务是1对多的关系)
		if(WorkProcessCountConstant.WORK_FLAG_HUDO.equals(workFlag)){//查询挂起任务列表---->查询对应的任务表---查询对应的挂起任务
			//1.获得任务查询对象
			TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
			//2.添加条件
			taskQuery.taskAssignee(userId);
			if(StringUtils.isNotBlank(serialNumber)){
				taskQuery.executionId(serialNumber);//任务的流水号，这里我认为是运行中流程实例的执行id 
			}
			ProcessDefinition pd=null;
			if(StringUtils.isNotBlank(workName)){
				//查询根据流程定义的key查询流程定义的id
				//1.获得流程定义查询对象
				ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService().createProcessDefinitionQuery();
				//2.根据流程定义的名字模糊查询流程定义
				processDefinitionQuery.processDefinitionNameLike("%"+workName+"%");//（在这里根据实际数据为工程名/文号）
				List<ProcessDefinition> list = processDefinitionQuery.list();
				if(list!=null&&list.size()>0){
					pd=list.get(0);//多个匹配只取其中的一个
					//这个需要严格的测试，我也不知道能用不=================
					taskQuery.processDefinitionId(pd.getId());//根据流程定义的id,查询任务列表
				}else{
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("total", 0);
					map.put("rows", new ArrayList());
					return map;
				}
				
			}
			
			taskQuery.suspended();//任务是挂起的============这个需要测试，我只是猜测
			taskQuery.orderByTaskCreateTime().desc();//按照流程的创建时间倒序
			long totalPages = taskQuery.count();//总记录数
			
			if(StringUtils.isNotBlank(page)&&StringUtils.isNotBlank(rows)){//有分页条件时
				//分页
				int pages=Integer.parseInt(rows);//每页显示多少条
				int start=(Integer.parseInt(page)-1)*pages;//从第几行开始查
				taskQuery.listPage(start, pages);//分页
			}
		
			//3.查询列表
			List<Task> list = taskQuery.list();
			//将task中的数据转换为我们需要的Vo：WorkProcessCountVo
			String processDefinitionName=null;
			if(pd!=null){
				processDefinitionName=pd.getName();
			}
			
			List<WorkProcessCountVo> rsList = runTaskConvertWPCV(list, processDefinitionName);
			
			Map<String, Object> map= new HashMap<String, Object>();
			map.put("total", totalPages);
			map.put("rows", rsList);
		
			return map;
		}
		
		//委托工作----和挂起任务代码一样，为了区别我就不抽取了
		if(WorkProcessCountConstant.WORK_FLAG_ETDO.equals(workFlag)){//查询委托任务列表---->查询对应运行时的任务表，委托人是当前人
			//1.获得任务查询对象
			TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
			//2.添加条件
			taskQuery.taskOwner(userId);//委托人是当前登录人====
			if(StringUtils.isNotBlank(serialNumber)){
				taskQuery.executionId(serialNumber);//任务的流水号，这里我认为是运行中流程实例的执行id 
			}
			ProcessDefinition pd=null;
			if(StringUtils.isNotBlank(workName)){
				//查询根据流程定义的key查询流程定义的id
				//1.获得流程定义查询对象
				ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService().createProcessDefinitionQuery();
				//2.根据流程定义的名字模糊查询流程定义
				processDefinitionQuery.processDefinitionNameLike("%"+workName+"%");//（在这里根据实际数据为工程名/文号）
				List<ProcessDefinition> list = processDefinitionQuery.list();
				if(list!=null&&list.size()>0){
					pd=list.get(0);//多个匹配只取其中的一个
					//这个需要严格的测试，我也不知道能用不=================
					taskQuery.processDefinitionId(pd.getId());//根据流程定义的id,查询任务列表
				}else{
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("total", 0);
					map.put("rows", new ArrayList());
					return map;
				}
				
			}
			
			taskQuery.active();//任务是激活状态
			
			taskQuery.orderByTaskCreateTime().desc();//按照流程的创建时间倒序
			long totalPages = taskQuery.count();//总记录数
			
			if(StringUtils.isNotBlank(page)&&StringUtils.isNotBlank(rows)){//有分页条件时
				//分页
				int pages=Integer.parseInt(rows);//每页显示多少条
				int start=(Integer.parseInt(page)-1)*pages;//从第几行开始查
				taskQuery.listPage(start, pages);//分页
			}
		
			//3.查询列表
			List<Task> list = taskQuery.list();
			//将task中的数据转换为我们需要的Vo：WorkProcessCountVo
			String processDefinitionName=null;
			if(pd!=null){
				processDefinitionName=pd.getName();
			}
			
			List<WorkProcessCountVo> rsList = runTaskConvertWPCV(list, processDefinitionName);
			
			Map<String, Object> map= new HashMap<String, Object>();
			map.put("total", totalPages);
			map.put("rows", rsList);
		
			return map;
		}
		
		//全部工作
		if(WorkProcessCountConstant.WORK_FLAG_ALDO.equals(workFlag)){//查询全部任务，正在执行的激活状态的任务+历史任务+挂起状态=========todo
			
			ArrayList<WorkProcessCountVo> list = new ArrayList<WorkProcessCountVo>();
			List<WorkProcessCountVo> toDoList = (List<WorkProcessCountVo>) workList(userId, WorkProcessCountConstant.WORK_FLAG_TODO, serialNumber, workName, null, null).get("rows");//待办任务列表
			List<WorkProcessCountVo> huList= (List<WorkProcessCountVo>) workList(userId, WorkProcessCountConstant.WORK_FLAG_HUDO, serialNumber, workName, null, null).get("rows");//挂起任务列表
			List<WorkProcessCountVo> etList=(List<WorkProcessCountVo>) workList(userId, WorkProcessCountConstant.WORK_FLAG_ETDO, serialNumber, workName, null, null).get("rows");//委托任务列表
			List<WorkProcessCountVo> historyList = (List<WorkProcessCountVo>) workList(userId, WorkProcessCountConstant.WORK_FLAG_YEDO, serialNumber, workName, null, null).get("rows");//办结任务列表
			
			list.addAll(toDoList);
			list.addAll(huList);
			list.addAll(etList);
			list.addAll(historyList);
			
			return analogPage(list,page,rows);//返回分页后的map
		}
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("total", 0);
		map.put("rows", new ArrayList<WorkProcessCountVo>());
		
		return map;
	}
	

	@Override
	public void addWork(String workFlag, WorkProcessCountVo workProcessCountVo) {
		
	}


	
	@Override
	public void hangUpRecoveryWork(String[] id) {
		
		for(String i: id){
			
			processEngine.getRuntimeService().activateProcessInstanceById(i);//激活一个流程实例
		}
	}


	@Override
	public void takeBackEntrustWork(String workFlag, String[] id) {
		
		
	}


	@Override
	public void urgeDoWork(String workFlag, String[] id) {
		
		
	}


	@Override
	public void delWork(String workFlag, String[] id) {
		
		
	}


	@Override
	public void exportWork(String workFlag, String[] id) {
		
		
	}


	@Override
	public void hostWork(String workFlag, String[] id) {
		
		
	}


	@Override
	public void postilWork(String workFlag, String[] id) {
		
		
	}


	@Override
	public void hangUpwork(String[] id) {
		
		RuntimeService runtimeService = processEngine.getRuntimeService();
		for(String processInstanceId:id){
			runtimeService.suspendProcessInstanceById(processInstanceId);//挂起任务
		}
		
	}


	@Override
	public void entrustWork(String workFlag, String[] id) {
		
		
	}
	
	
	
	/**
	 * 
	 * <p>因为前台要的是已经定义好的vo，因此需要将运行中的Task转换为我们需要的Vo：注意是运行中的奥，是为了区别历史任务表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月24日 下午2:48:21 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月24日 下午2:48:21 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param taskList 任务列表
	 * @param 	ProcessDefinitionName 流程定义的名字（ NAME_ ）字段对应的值
	 * @return  List<WorkProcessCountVo>
	 * @throws:
	 *
	 */
	public List<WorkProcessCountVo> runTaskConvertWPCV(List<Task> taskList,String processDefinitionName){
		List<WorkProcessCountVo> list = new ArrayList<WorkProcessCountVo>();
		for(Task task :taskList){
			WorkProcessCountVo vo = new WorkProcessCountVo();
			vo.setId(task.getExecutionId());
			vo.setSerialNumber(task.getExecutionId());
			
			if(StringUtils.isNotBlank(processDefinitionName)){
				vo.setWorkName(processDefinitionName);
				vo.setProcessType(processDefinitionName);
			}else{
				//根据流程定义的id,查询流程定义的名字
				ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService().createProcessDefinitionQuery();//获取流程定义的查询对象
				processDefinitionQuery.processDefinitionId(task.getProcessDefinitionId());
				ProcessDefinition processDefinition = processDefinitionQuery.singleResult();//流程定义实体
				vo.setWorkName(processDefinition.getName());
				vo.setProcessType(processDefinition.getName());
			}
			
			String executionId= task.getExecutionId();//流程实例的执行id
			String step=(String) processEngine.getRuntimeService().getVariable(executionId, WorkProcessCountConstant.STEP);
			if(null!=step){
			}else{
				vo.setProcessMap(task.getName());//流程定义的节点名称,如第2步：xx
			}	
			//获取流程变量------>这个地方需在开启流程时存一个发起人的流程变量
			String createPerson=(String) processEngine.getRuntimeService().getVariable(executionId, WorkProcessCountConstant.CREATE_USER);//根据流程实例的执行id，查询指定的流程变量
			if(createPerson!=null){
				vo.setCreatePerson(createPerson);//流程的发起人
			}
			//查询任务所在的流程实例的状态
			 ExecutionQuery executionQuery = processEngine.getRuntimeService().createExecutionQuery();
			 Execution execution = executionQuery.executionId(executionId).singleResult();
			 if(execution.isSuspended()){//状态为已挂起
				 vo.setState(WorkProcessCountConstant.STATE_HUP);//状态自定义,我认为就是挂起状态
			 }else{
				 vo.setState(WorkProcessCountConstant.STATE_SERVEN);//状态自定义,我认为就是处理中
			 }
			Date createTime = task.getCreateTime();
			vo.setArriveTime(DateUtils.formatDateY_M_D_H_M_S(task.getCreateTime()));//创建时间，就是到达时间
			
			Date date = task.getDueDate();
			if(date==null){
				date=new Date();//正在执行时，持续时间可能为null
			}
			Long s =date.getTime()-createTime.getTime();
			vo.setRemainTime(s.toString());//暂时写为毫秒值
			vo.setOption(null);//to do这个暂时没做===========================================
			
			list.add(vo);
		}
		
		return list;
	} 
	
	/**
	 * 
	 * <p>因为前台要的是已经定义好的vo，因此需要将历史表中的Task转换为我们需要的Vo：注意是运行中的奥，是为了区别运行时的任务表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月24日 下午2:48:21 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月24日 下午2:48:21 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param taskList 历史任务列表
	 * @param 	ProcessDefinitionName 流程定义的名字（ NAME_ ）字段对应的值
	 * @return  List<WorkProcessCountVo>
	 * @throws:
	 *
	 */
	public List<WorkProcessCountVo> historyTaskConvertWPCV(List<HistoricTaskInstance> taskList,String processDefinitionName){
		List<WorkProcessCountVo> rslList = new ArrayList<WorkProcessCountVo>();
		for(HistoricTaskInstance task :taskList){
			WorkProcessCountVo vo = new WorkProcessCountVo();
			vo.setId(task.getExecutionId());
			vo.setSerialNumber(task.getExecutionId());
			if(StringUtils.isNotBlank(processDefinitionName)){
				vo.setWorkName(processDefinitionName);
			}else{
				//根据流程定义的id,查询流程定义的名字
				ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService().createProcessDefinitionQuery();//获取流程定义的查询对象
				processDefinitionQuery.processDefinitionId(task.getProcessDefinitionId());
				ProcessDefinition processDefinition = processDefinitionQuery.singleResult();//流程定义实体
				vo.setWorkName(processDefinition.getName());
			}
			
			String executionId= task.getExecutionId();//流程实例的执行id
			
			//这里要根据执行的id查询自定义的存储流程变量的表====================================================
			String step="";
			
			vo.setProcessMap(step+task.getName());//流程定义的节点名称,如第2步：xx
			//这里要根据执行的id查询自定义的存储流程变量的表====================================================
			String createPerson="发起人，需要查询自定义的表";
			vo.setCreatePerson(createPerson);//流程的发起人
			vo.setEndTime(DateUtils.formatDateY_M_D_H_M_S(task.getEndTime()));//办结时间
			
			//根据流程的执行id去act_ru_execution（运行时流程执行实例）查询正在运行的流程实例，如果查不到，或者报异常，我认为是该任务已经执行完毕=======================================
			//获取正在执行的流程实例的查询对象
			ExecutionQuery executionQuery = processEngine.getRuntimeService().createExecutionQuery();
			executionQuery.executionId(executionId);
			Execution execution = executionQuery.singleResult();
			if(execution.isEnded()){//表示已结束
				//我认为是流程实例已经执行完毕
				vo.setState(WorkProcessCountConstant.STATE_NINE);//9-->已结束
			}else if(execution.isSuspended()){
				vo.setState(WorkProcessCountConstant.STATE_NINE);//9-->已结束,任务的状态,历史表中，任务的状态都是已结束
				vo.setProcessSate(WorkProcessCountConstant.STATE_HUP);//流程状态8-->进行中
			}else{//说明运行中的流程实例表中存在改流程实例
				vo.setState(WorkProcessCountConstant.STATE_NINE);//9-->已结束,任务的状态,历史表中，任务的状态都是已结束
				vo.setProcessSate(WorkProcessCountConstant.STATE_EIGHT);//流程状态8-->进行中
			}
			
			vo.setArriveTime(DateUtils.formatDateY_M_D_H_M_S(task.getCreateTime()));//到达时间
			vo.setOption(null);//to do这个暂时没做====================
			
			rslList.add(vo);
		}
		
		return rslList;
	}
	
	/**
	 * 
	 * <p>处理已停留时间的格式=================== </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月24日 下午4:19:09 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月24日 下午4:19:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param date
	 * @throws:
	 *
	 */
	public static String formatRemainTime(Date date){
		String rs="";
		if(date!=null){
			
			String str=DateUtils.formatDateY_M_D_H_M_S(date);//yyyy-MM-dd HH:mm:ss
			Integer y = Integer.parseInt(str.substring(0, 4));
			Integer yc=y-1970;
			if(yc!=0){
				rs+=yc.toString()+"年";
			}
			
			String month= str.substring(5,7);
			if(month.endsWith("0")){
				month=month.substring(1,2);
			}
			
			Integer m= Integer.parseInt(month)-1;
			if(yc!=0||m!=0){
				rs+=m.toString()+"月";
			}
			
			String day= str.substring(8,10);
			if(day.endsWith("0")){
				day=day.substring(1,2);
			}
			
			Integer d= Integer.parseInt(day)-1;
			if(yc!=0||m!=0||d!=0){
				rs+=d.toString()+"天";
			}
			
			String hour= str.substring(11,13);
			if(hour.endsWith("0")){
				hour=hour.substring(1,2);
			}
			Integer h=null;
			if(yc!=0||m!=0||d!=0){
				h= Integer.parseInt(hour);
			}else{
				h= Integer.parseInt(hour)-8;
			}
			
			if(yc!=0||m!=0||d!=0||h!=0){
				rs+=h.toString()+"时";
			}
			
			String minute= str.substring(14,16);
			if(minute.endsWith("0")){
				minute=minute.substring(1,2);
			}
			
			Integer min= Integer.parseInt(minute);
			if(yc!=0||m!=0||d!=0||h!=0||min!=0){
				rs+=min.toString()+"分";
			}
			
			String seconds= str.substring(17,19);
			if(seconds.endsWith("0")){
				seconds=seconds.substring(1,2);
			}
			
			Integer s= Integer.parseInt(seconds);
			if(yc!=0||m!=0||d!=0||h!=0||min!=0||s!=0){
				rs+=s.toString()+"秒";
			}
		}
		return rs;
	}

	
	public static void main(String[] args) {
		Date date = new Date(1000);
		String rs = formatRemainTime(date);
	}



	@Override
	public List<String> getTaskExecutionId(String userId) {
		
		return workProcessCountDao.getTaskExecutionId(userId);
	}
	
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
	public void deleteAttention(String[] exectionId)throws Exception{
		
		 workProcessCountDao.deleteAttention(exectionId);
	}
	
	@Override
	public void attention(String userId, String[] workId) throws Exception {
		
		 workProcessCountDao.attention(userId, workId);
	}
	
	
	
	/**
	 * 
	 * <p>由于某种情况下查询出的数据是两张表的集合因此，不能通过api进行分页，现在这个方法模拟分页，在数据量很大事，此方法会慢，因为每次查询的都是满足条件的全部实体的列表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月25日 上午9:31:01 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月25日 上午9:31:01 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param list ：List<WorkProcessCountVo>
	 * @param page：page当前页
	 * @param rows ：rows每页显示多少行
	 * @return Map<String,Object> 分页后的map
	 * @throws:
	 *
	 */
	public Map<String,Object> analogPage(List<WorkProcessCountVo> list,String page,String rows){
		
		Map<String,Object> hashMap = new HashMap<String,Object>();
		if(list.size()<=0){
			hashMap.put("total", 0);
			hashMap.put("rows", new ArrayList<WorkProcessCountVo>());
		
			return hashMap;
		}
		
		Integer p=(page==null?1:Integer.parseInt(page));//页号
		Integer r=(rows==null?10:Integer.parseInt(rows));//每页显示的记录数
		
		//模拟分页
		int total= list.size();
		int totalPages=0;//总页数,向上取整
		if(total%r==0){
			totalPages=total/r;
		}else{
			totalPages=total/r+1;
		}
		int beginIndex=0;//查询的起始行
		int endIndex=0;//查询的结束行	
		if(p<totalPages){
			beginIndex=(p-1)*r;
			endIndex=p*r-1;//角标从0开始
		}
		if(p==totalPages){
			beginIndex=(p-1)*r;
			endIndex=list.size()-1;//查询页等于当前页时，查询的结束是集合的最后角标
		}
		if(p>totalPages||p<0){
			hashMap.put("total", 0);
			hashMap.put("rows", new ArrayList<WorkProcessCountVo>());
			return hashMap;
		}
		
		List<WorkProcessCountVo> subList =new ArrayList<WorkProcessCountVo>();
		for(int i=beginIndex;i<=endIndex;i++){
		
			subList.add(list.get(i));
		}
		
		hashMap.put("total", total);
		hashMap.put("rows",subList);
	
		return hashMap;
	}




	@Override
	public void startProcDef(String processDefId) {
		ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(processDefId);
		System.out.println(processInstance.getName());
		
		
		
	}




	@Override
	public void delTask(String processInstanceId) {
		
		processEngine.getRuntimeService().deleteProcessInstance(processInstanceId, "我愿意");
		
	}




	@Override
	public void complete(String taskId) {
		
		processEngine.getTaskService().complete(taskId);
		
	}




	@Override
	public void suspendProcessInstanceById(String exectionId) {
		
		processEngine.getRuntimeService().suspendProcessInstanceById(exectionId);
	}




	
	
	
	
	

}
