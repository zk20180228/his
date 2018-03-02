package cn.honry.oa.humantask.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.honry.oa.humantask.queue.QueueUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.EmployeeExtend;
import cn.honry.base.bean.model.OaBpmConfForm;
import cn.honry.base.bean.model.OaBpmConfUser;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaFormInfo;
import cn.honry.base.bean.model.OaKVProp;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.base.bean.model.OaTaskConfUser;
import cn.honry.base.bean.model.OaTaskDefBase;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.bean.model.OaTaskParticipant;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.mq.MessageSend;
import cn.honry.oa.activiti.bpm.form.service.OaBpmConfFormService;
import cn.honry.oa.activiti.bpm.form.vo.ConfFormVo;
import cn.honry.oa.activiti.bpm.form.vo.FormParameterVo;
import cn.honry.oa.activiti.bpm.node.service.OaBpmConfNodeService;
import cn.honry.oa.activiti.bpm.process.service.OaBpmProcessService;
import cn.honry.oa.activiti.bpm.process.vo.OaProcessVo;
import cn.honry.oa.activiti.bpm.user.service.OaBpmConfUserService;
import cn.honry.oa.activiti.bpm.utils.BeanMapper;
import cn.honry.oa.activiti.bpm.utils.ExtendVo;
import cn.honry.oa.activiti.listener.AssigneeAliasHumanTaskListener;
import cn.honry.oa.activiti.listener.HumanTaskListener;
import cn.honry.oa.activiti.queryFlow.service.QueryFlowService;
import cn.honry.oa.activiti.task.service.TaskDefaultService;
import cn.honry.oa.activiti.task.service.TaskInternalService;
import cn.honry.oa.humantask.dao.HumanTaskDao;
import cn.honry.oa.humantask.service.HumanTaskService;
import cn.honry.oa.humantask.vo.HumanTaskVo;
import cn.honry.oa.humantask.vo.ParticipantVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

import javax.annotation.Resource;

@Service("humanTaskService")
@Transactional
@SuppressWarnings({ "all" })
public class HumanTaskServiceImpl implements HumanTaskService {

	private List<HumanTaskListener> humanTaskListeners;
	
	private BeanMapper beanMapper = new BeanMapper();
	
	public void setHumanTaskListeners(List<HumanTaskListener> humanTaskListeners) {
		this.humanTaskListeners = humanTaskListeners;
	}
	
	@Autowired
	@Qualifier(value = "humanTaskDao")
	private HumanTaskDao humanTaskDao;
	
	@Autowired
	@Qualifier(value = "taskDefaultService")
	private TaskDefaultService taskDefaultService;

	@Autowired
	@Qualifier(value = "queryFlowService")
	private QueryFlowService queryFlowService;
	
	public void setTaskDefaultService(TaskDefaultService taskDefaultService) {
		this.taskDefaultService = taskDefaultService;
	}
	@Autowired
	@Qualifier(value = "messageSend")
	private MessageSend messageSend;
	@Autowired
	@Qualifier(value = "taskInternalService")
	private TaskInternalService taskInternalService;
	
	public void setTaskInternalService(TaskInternalService taskInternalService) {
		this.taskInternalService = taskInternalService;
	}

	@Autowired
	@Qualifier(value = "oaBpmConfFormService")
	private OaBpmConfFormService oaBpmConfFormService;

	public void setOaBpmConfFormService(OaBpmConfFormService oaBpmConfFormService) {
		this.oaBpmConfFormService = oaBpmConfFormService;
	}
	
	@Autowired
	@Qualifier(value = "oaBpmConfNodeService")
	private OaBpmConfNodeService oaBpmConfNodeService;
	
	public void setOaBpmConfNodeService(OaBpmConfNodeService oaBpmConfNodeService) {
		this.oaBpmConfNodeService = oaBpmConfNodeService;
	}

	@Autowired
	@Qualifier(value = "oaBpmConfUserService")
	private OaBpmConfUserService oaBpmConfUserService;
	
	public void setOaBpmConfUserService(OaBpmConfUserService oaBpmConfUserService) {
		this.oaBpmConfUserService = oaBpmConfUserService;
	}

	@Autowired
	@Qualifier(value = "oaBpmProcessService")
	private OaBpmProcessService oaBpmProcessService;
	
	public void setOaBpmProcessService(OaBpmProcessService oaBpmProcessService) {
		this.oaBpmProcessService = oaBpmProcessService;
	}

	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}

	@Resource
	private QueueUtils queueUtils;
	public void setQueueUtils(QueueUtils queueUtils) {
		this.queueUtils = queueUtils;
	}

	/**
	 * 流程发起之前，配置每个任务的负责人.
	 * @param businessKey 业务标识
	 * @param taskDefinitionKeys 任务定义key
	 * @param taskAssignees
	 */
	public void configTaskDef(String businessKey,List<String> taskDefinitionKeys, List<String> taskAssignees){
		if (taskDefinitionKeys == null) {
            return;
        }

        int index = 0;

        for (String taskDefinitionKey : taskDefinitionKeys){
        	String taskAssignee = taskAssignees.get(index++);
        	String hql = "from OaTaskConfUser where businessKey=? and code=?";
        	OaTaskConfUser taskConfUser = taskDefaultService.findUnique(hql,
        			new OaTaskConfUser(), businessKey,taskDefinitionKey);
        	if (taskConfUser == null) {
                taskConfUser = new OaTaskConfUser();
            }

            taskConfUser.setBusinessKey(businessKey);
            taskConfUser.setCode(taskDefinitionKey);
            taskConfUser.setValue(taskAssignee);
            taskDefaultService.saveOrUpdate(taskConfUser);
        }
	}
	
	/**
	 * 保存/更新任务
	 * @param HumanTaskVo
	 * @return
	 */
	public HumanTaskVo saveHumanTask(HumanTaskVo humanTaskVo){
		
		return saveHumanTask(humanTaskVo, true);
	}
	
	/**
	 * 保存/更新任务
	 * @param HumanTaskVo
	 * @return
	 */
	public HumanTaskVo saveHumanTask(HumanTaskVo humanTaskVo,boolean triggerListener){
		if(humanTaskVo==null){
			return null;
		}
		String id = humanTaskVo.getId();
		OaTaskInfo taskInfo= new OaTaskInfo();
		
		if(StringUtils.isNotBlank(id)){
			 taskInfo = taskDefaultService.findUnique("from OaTaskInfo where id=? and del_flg=0 and stop_flg=0 ",new OaTaskInfo(),id);
			
		}
		try {
			beanMapper.copy(humanTaskVo, taskInfo);
		} catch (Exception e1) {
		}
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		
		String processDefinitionId = taskInfo.getProcessDefinitionId();
		String attr2 = taskInfo.getAttr2();//流程名称
		if(StringUtils.isBlank(attr2)){
			String businessKey = taskInfo.getBusinessKey();
			List<OaTaskInfo> list = taskDefaultService.getList(
					"from OaTaskInfo where businessKey=? and attr2 is not null and del_flg=0 and stop_flg=0",
					new OaTaskInfo(), businessKey);
			if(list==null||list.size()==0){
				OaBpmProcess oaBpmProcess = oaBpmProcessService.getProcessInfo(processDefinitionId);
				taskInfo.setCreateUser(account);
				taskInfo.setCreateTime(new Date());
				if(oaBpmProcess!=null){
					String name = "";
					String processName = oaBpmProcess.getName();
					EmployeeExtend employeeExtend = taskDefaultService.findUnique(
							"from EmployeeExtend where employeeJobNo=? and del_flg=0 and stop_flg=0",new EmployeeExtend(),account);
					if(employeeExtend!=null){
						String department = employeeExtend.getDepartment();
						taskInfo.setCreateDept(employeeExtend.getDepartmentCode());
						if(StringUtils.isNotBlank(department)){
							name+=department;
						}
						String employeeName = employeeExtend.getEmployeeName();
						if(StringUtils.isNotBlank(employeeName)){
							name+="-"+employeeName;
						}
					}
					if(StringUtils.isNotBlank(processName)){
						name+="-"+processName;
					}
					taskInfo.setAttr2(name);
				}
			}else{
				OaTaskInfo info = list.get(0);
				taskInfo.setAttr2(info.getAttr2());
				taskInfo.setCreateUser(info.getCreateUser());//流程发起人
				taskInfo.setCreateDept(info.getCreateDept());//流程发起人所在科室
			}
		}
		if(StringUtils.isBlank(taskInfo.getCreateuserName())){//如果创建人名称为空,设置创建人名称
			String createUser = taskInfo.getCreateUser();
			if(StringUtils.isNotBlank(createUser)){
				String empExtendName = humanTaskDao.getEmpExtendName(taskInfo.getCreateUser());
				taskInfo.setCreateuserName(empExtendName);
			}
		}
		
		
		if(StringUtils.isBlank(taskInfo.getLastModifierName())){//如果最后处理人名字为空,设置处理人名称
			String lastModifier = taskInfo.getLastModifier();
			if(StringUtils.isNotBlank(lastModifier)){
				String lastModifierName = humanTaskDao.getEmpExtendName(lastModifier);
				taskInfo.setLastModifierName(lastModifierName);
			}
		}
		taskDefaultService.saveOrUpdate(taskInfo);
		
		ExtendVo extendVo = getConfRule(processDefinitionId,taskInfo.getCode());
		if(extendVo!=null){
			if(Boolean.parseBoolean(extendVo.getWithdept())){//与上一节点同部门
				String attr1 = taskInfo.getAttr1();
				if(StringUtils.isNotBlank(attr1)&& attr1.indexOf("dept:{dept}")==-1){
					String string = attr1.replace("]", "&&dept:{dept}]");
					taskInfo.setAttr1(string);
				}
			}
		}
		if(triggerListener){
			String assig = taskInfo.getAssignee();
			if(StringUtils.isBlank(assig)){
				List<OaTaskInfo> list = humanTaskDao.findAllPreviousActivities(taskInfo.getProcessInstanceId(), taskInfo.getTaskId());
				if(list!=null && list.size()>0){//如果指定了负责人,先设置负责人
					String attr3 = list.get(0).getAttr3();
					if(StringUtils.isNotBlank(attr3)){
						taskInfo.setAssignee(attr3);
					}
				}
			}
			if ((id == null) && (humanTaskListeners != null)){
				for (HumanTaskListener humanTaskListener : humanTaskListeners) {
					try {
						humanTaskListener.onCreate(taskInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(StringUtils.isBlank(taskInfo.getAssigneeName())){//如果负责人名称为空,设置负责人名称
				String assignee = taskInfo.getAssignee();
				if(StringUtils.isNotBlank(assignee)){
					String assigneeName = humanTaskDao.getAssigneeName(assignee);
					taskInfo.setAssigneeName(assigneeName);
				}
			}
			
			if(StringUtils.isBlank(taskInfo.getProcessStarterName())){//如果负责人名称为空,设置负责人名称
				String processStarter = taskInfo.getProcessStarter();
				if(StringUtils.isNotBlank(processStarter)){
					String processStarterName = humanTaskDao.getEmpExtendName(processStarter);
					taskInfo.setProcessStarterName(processStarterName);
				}
			}
			
			humanTaskVo.setAssignee(taskInfo.getAssignee());
			humanTaskVo.setOwner(taskInfo.getOwner());
			humanTaskVo.setId(taskInfo.getId());
			
			if(extendVo!=null && StringUtils.isNotBlank(extendVo.getMessage())){
				if("true".equals(extendVo.getMessage())){
					OaBpmProcess oaBpmProcess = oaBpmProcessService.getProcessInfo(taskInfo.getProcessDefinitionId());
					String action = "";
					String actss = oaBpmProcess.getAction();
					if(StringUtils.isNotBlank(actss)){
						String [] act = actss.split(";");
						if(act.length>1){
							action = act[1];
						}else{
							action = act[0];
						}
						String assignee = taskInfo.getAssignee();
						if(StringUtils.isNotBlank(assignee)){
							String[] ids = assignee.split(",");
							if(null!=ids&&ids.length>0){
								Map<String,Object> map = new LinkedHashMap<String, Object>();
								for (String jid : ids) {
									if(!account.equals(jid)){
										map.put("id", taskInfo.getId());
										map.put("action", action);
										String[] dept_name_title = taskInfo.getAttr2().split("-");
										map.put("title",StringUtils.isNotBlank(taskInfo.getAttr2())?dept_name_title[dept_name_title.length-1]:"");
										if(dept_name_title.length==3){
											map.put("username",StringUtils.isNotBlank(taskInfo.getAttr2())?dept_name_title[0]+"--"+dept_name_title[1]:"" );
										} else if(dept_name_title.length==2){
											map.put("username",StringUtils.isNotBlank(taskInfo.getAttr2())?dept_name_title[0]:"" );
										} else{
											map.put("username","" );
										}
										map.put("createTime", new Date());
										map.put("jid", jid);
										map.put("msg_type", "msg_type_activiti_message");
										try {
											messageSend.sendMessage(JSONUtils.toJson(map));
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return humanTaskVo;
	}
	
	/**
	 * 保存任务参与者
	 * @param vo
	 */
	public void saveParticipant(ParticipantVo vo){
		if(vo==null){
			return ;
		}
		OaTaskParticipant taskParticipant = new OaTaskParticipant();
		 taskParticipant.setRef(vo.getCode());
	     taskParticipant.setType(vo.getType());
	     taskParticipant.setTaskId(vo.getHumanTaskId());
	     taskDefaultService.saveOrUpdate(taskParticipant);
	}
	
	/**
	 * 根据任务id获取vo对象
	 * @param taskId
	 * @return
	 */
	public HumanTaskVo findHumanTaskByTaskId(String taskId){
		HumanTaskVo humanTaskVo = new HumanTaskVo();
		OaTaskInfo taskInfo = taskDefaultService.findUnique(
				"from OaTaskInfo where taskId=? and stop_flg=0 and del_flg=0", new OaTaskInfo(), taskId);
		try {
			if(taskInfo!=null){
				beanMapper.copy(taskInfo, humanTaskVo);
			}
		} catch (Exception e) {
		}
		return humanTaskVo;
	}
	/**
	 * 最近的一条记录
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月2日 下午5:14:39 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月2日 下午5:14:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param taskId
	 * @return:
	 * @throws:
	 * @return: HumanTaskVo
	 *
	 */
	public HumanTaskVo findLastHumanTaskByTaskId(String taskId){
		HumanTaskVo humanTaskVo = new HumanTaskVo();
		OaTaskInfo taskInfo = taskDefaultService.findUnique(
				"from OaTaskInfo where taskId=? and stop_flg=0 and del_flg=0 order by createtime desc", new OaTaskInfo(), taskId);
		try {
			if(taskInfo!=null){
				beanMapper.copy(taskInfo, humanTaskVo);
			}
		} catch (Exception e) {
		}
		return humanTaskVo;
	}
	/**
	 * 根据taskId按创建时间倒叙查找
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2018年1月23日 上午9:18:39 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2018年1月23日 上午9:18:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param taskId
	 * @return:
	 * @throws:
	 * @return: 
	 *
	 */
	public List<HumanTaskVo> findHumanTaskListByTaskId(String taskId){
		String hql="from OaTaskInfo where taskId=? and stop_flg=0 and del_flg=0 order by createtime desc";
		 List<OaTaskInfo> list = taskDefaultService.getList(hql,new OaTaskInfo(), taskId);
		 List<HumanTaskVo> voList= new ArrayList<>();
		 if(list!=null && list.size()>0){
			 for (OaTaskInfo oaTaskInfo : list) {
				 HumanTaskVo vo = new HumanTaskVo();
				 try {
					beanMapper.copy(oaTaskInfo, vo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				 voList.add(vo);
			}
		 }
		 return voList;
	}
	/**
	 * 根据id获取任务vo对象
	 * @param id
	 * @return
	 */
	public HumanTaskVo findHumanTask(String id){
		HumanTaskVo humanTaskVo = new HumanTaskVo();
		OaTaskInfo taskInfo = taskDefaultService.findUnique(
				"from OaTaskInfo where id=? and stop_flg=0 and del_flg=0", new OaTaskInfo(), id);
		try {
			if(taskInfo!=null){
				beanMapper.copy(taskInfo, humanTaskVo);
			}
		} catch (Exception e) {
		}
		return humanTaskVo;
	}
	
	/**
	 * 获取任务表单
	 * @param humanTaskVo 
	 * @return
	 */
	public ConfFormVo getFormVo(HumanTaskVo humanTaskVo){
		if(humanTaskVo==null){
			return null;
		}
		ConfFormVo vo =null;
		String code = humanTaskVo.getCode();
        String processDefinitionId = humanTaskVo.getProcessDefinitionId();
		if(StringUtils.isNotBlank(humanTaskVo.getTaskId())){
			OaTaskDefBase taskDefBase = taskDefaultService.findUnique(
					"from OaTaskDefBase where code=? and processDefinitionId=? and stop_flg=0 and del_flg=0 "
					,new OaTaskDefBase(), code,processDefinitionId);
			if(taskDefBase!=null){
				String formKey = taskDefBase.getFormKey();
				if(StringUtils.isNotBlank(formKey)){
					vo = oaBpmConfFormService.getForm(formKey, humanTaskVo.getTenantId());
					if(vo==null){
						vo=new ConfFormVo();
					}
				}
				vo.setCode(formKey);
				vo.setActivityId(code);
				vo.setProcessDefinitionId(processDefinitionId);
				
				StringBuffer hql=new StringBuffer("select p.value from OaTaskDefOperation p ")
				.append(" where p.baseId=? and p.stop_flg=0 and p.del_flg=0");
				List<String> list = taskDefaultService.getList(hql.toString(),"",taskDefBase.getId());
				if(list!=null && list.size()>0){
					vo.getButtons().addAll(list);
				}
				ExtendVo extendVo = getConfRule(processDefinitionId, code);
				if(extendVo!=null){
					String reject = extendVo.getReject();//是否可驳回
					String stepreject = extendVo.getStepreject();//是否可逐级驳回
					String isAssigner = extendVo.getIsAssigner();//是否可指定下一节点任务负责人
					List<String> buttonsList = vo.getButtons();
					if("true".equals(isAssigner)){
						buttonsList.add("selectAssignee");
					}
					buttonsList.add("completeTask");
					if("true".equals(stepreject)){//暂时注掉逐级驳回功能
						buttonsList.add("rollbackActivity");
					}else if("true".equals(reject)){
						buttonsList.add("rollback");
					}
				}
				
			}
			
		}else{//taskId为空
			vo = new ConfFormVo();
			vo.setCode(humanTaskVo.getForm());
			vo.setActivityId(code);
			vo.setProcessDefinitionId(processDefinitionId);
		}
		
		if(vo==null){
			return new ConfFormVo();
		}
		
		vo.setTaskId(humanTaskVo.getTaskId());
		ConfFormVo form = oaBpmConfFormService.getForm(code, humanTaskVo.getTenantId());
		String properties = oaBpmConfFormService.getFormProperties(humanTaskVo.getForm(), code,humanTaskVo.getProcessDefinitionId());
		vo.setProperties(properties);
		if(form==null){
			return vo;
		}
		vo.setRedirect(false);//此处应该判断是否为外部表单(功能未实现)
		vo.setUrl(form.getUrl());
		vo.setContent(form.getContent());
		return vo;
	}
	
	/**
	 * 根据流程实例id获取任务vo对象
	 * @param processInstanceId
	 * @return
	 */
	 public List<HumanTaskVo> findHumanTasksByProcessInstanceId(String processInstanceId){
		 String hql="from OaTaskInfo where processInstanceId=? and stop_flg=0 and del_flg=0 order by completeTime desc ";
		 List<OaTaskInfo> list = taskDefaultService.getList(hql,new OaTaskInfo(), processInstanceId);
		 List<HumanTaskVo> voList= new ArrayList<>();
		 if(list!=null && list.size()>0){
			 for (OaTaskInfo oaTaskInfo : list) {
				 HumanTaskVo vo = new HumanTaskVo();
				 try {
					beanMapper.copy(oaTaskInfo, vo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				 voList.add(vo);
			}
		 }
		 return voList;
	 }
	 /**
	  * 查找撤回的最新的两条记录
	  * @param processInstanceId
	  * @return
	  */
	 public List<HumanTaskVo> findHumanTasksByProcessInstanceIdRecall(String processInstanceId){
		 String hql="from OaTaskInfo where processInstanceId=? and stop_flg=0 and del_flg=0 order by createtime desc,to_number(taskId) desc";
		 List<OaTaskInfo> list = taskDefaultService.getList(hql,new OaTaskInfo(), processInstanceId);
		 List<HumanTaskVo> voList= new ArrayList<>();
		 if(list!=null && list.size()>0){
			 for (OaTaskInfo oaTaskInfo : list) {
				 HumanTaskVo vo = new HumanTaskVo();
				 try {
					 beanMapper.copy(oaTaskInfo, vo);
				 } catch (Exception e) {
					 e.printStackTrace();
				 }
				 voList.add(vo);
			 }
		 }
		 return voList;
	 }
	 
	 public List<HumanTaskVo> getHumanTask(String parentId){
		 String hql=" from OaTaskInfo where parentId=? and stop_flg=0 and del_flg=0 ";
		 List<OaTaskInfo> list = taskDefaultService.getList(hql,new OaTaskInfo(), parentId);
		 List<HumanTaskVo> voList= new ArrayList<>();
		 if(list!=null && list.size()>0){
			 for (OaTaskInfo oaTaskInfo : list) {
				 HumanTaskVo vo = new HumanTaskVo();
				 try {
					beanMapper.copy(oaTaskInfo, vo);
				} catch (Exception e) {
				}
				 voList.add(vo);
			}
		 }
		 return voList;
	 }
	 
	 /**
	  * 根据ref获取记录
	  * @param ref
	  * @return
	  */
	 public OaKVRecord getRecord(String ref){
		 if(StringUtils.isBlank(ref)){
			 return null;
		 }
		 
		 OaKVRecord record = taskDefaultService.findUnique(
					"from OaKVRecord t where t.id=? and t.stop_flg=0 and t.del_flg=0 ", new OaKVRecord(), ref);
			if(record!=null){
				List<OaKVProp> list = taskDefaultService.getList(
						"from OaKVProp t where t.recordId=? and t.stop_flg=0 and t.del_flg=0", new OaKVProp(), record.getId());
				if(list!=null && list.size()>0){
					for (OaKVProp oaKVProp : list) {
						record.getProps().put(oaKVProp.getCode(), oaKVProp);
					}
				}
			}
			return record;
	 }
	
	 /**
	  * 保存草稿
	  * @param userId 登录用户
	  * @param deptCode 登录科室
	  * @param tenantId 租户id
	  * @param formParameter 表单参数
	  * @return
	  */
	  public String saveDraft(String userId,String deptCode,String tenantId,FormParameterVo formParameter){
		  String humanTaskId = formParameter.getHumanTaskId();
		  if(StringUtils.isBlank(humanTaskId)){
			  return null;
		  }
		  HumanTaskVo humanTaskVo = findHumanTask(humanTaskId);
		  if(humanTaskVo==null){
			  return null;
		  }
		  OaKVRecord record = getRecord(humanTaskVo.getBusinessKey());
		  String businessKey=null;
		  Date date = new Date();
		  if(record==null){
			  record= new OaKVRecord();
			  record.setFlowCode(oaBpmConfFormService.getRecordSeq());
			  record.setCreateTime(date);
			  record.setCreateUser(userId);
			  taskDefaultService.saveOrUpdate(record);
		  }
		  businessKey=record.getId();
		  record.setUpdateTime(date);
		  record.setUpdateUser(userId);
		  if(StringUtils.isBlank(record.getBusinessKey())){
			  record.setBusinessKey(businessKey);
		  }
		  List<OaKVProp> list = taskDefaultService.getList(
				  "from OaKVProp where recordId=? and stop_flg=0 and del_flg=0 ", new OaKVProp(),businessKey);
		  Map<String,OaKVProp> map =new HashMap<>();
		  for (OaKVProp oaKVProp : list) {
			map.put(oaKVProp.getCode(), oaKVProp);
		}
		  String except="humanTaskId,zkhonryState,account,nextAssignee,";
		  for (Entry<String, List<String>> entry : formParameter
				  .getMultiValueMap().entrySet()) {
			  String key = entry.getKey();
			  if(except.contains(key)){
				  continue;
			  }
			  List<String> value = entry.getValue();
			  
			  if ((value == null) || (value.isEmpty())) {
				  continue;
			  }
			  OaKVProp prop = map.get(key);
			  if(prop==null){
				  prop =new OaKVProp();
			  }
			  prop.setCode(key);
			  prop.setType(0);
			  prop.setValue(value.get(0));
			  prop.setRecordId(record.getId());
			  prop.setCreateDept(deptCode);
			  prop.setCreateTime(date);
			  prop.setCreateUser(userId);
			  taskDefaultService.saveOrUpdate(prop);
		  }
		 return businessKey;
	 }
	 

	  /**
	   * 完成任务
	   * @param humanTaskId
	   * @param userId
	   * @param formParameter
	   * @param taskParameters
	   * @param record
	   * @param processInstanceId
	   */
	  public void completeTask(String humanTaskId, String userId,FormParameterVo formParameter,
			  Map<String, Object> taskParameters,OaKVRecord record, String processInstanceId){
		  if(StringUtils.isBlank(humanTaskId)){
			  return;
		  }
		  HumanTaskVo humanTaskVo = findHumanTask(humanTaskId);
		  if(humanTaskVo==null){
			  throw new IllegalStateException("任务不存在");
		  }
		  String action = formParameter.getAction();
		  String comment = formParameter.getComment();
		  String assignee = formParameter.getAssignee();
		  String string = taskParameters.get("zkhonryState").toString();
		  if(StringUtils.isBlank(string)||"0".equals(string)){
			  humanTaskVo.setCompleteStatus("0");
		  }else{
			  humanTaskVo.setCompleteStatus("1");
		  }
		  Date date = new Date();
		  humanTaskVo.setStatus("complete");
		  humanTaskVo.setCompleteTime(date);
		  humanTaskVo.setAction("完成");
		  humanTaskVo.setLastModifier(userId);
		  humanTaskVo.setLastModifiedTime(date);
		  if(StringUtils.isNotBlank(action)){
			  humanTaskVo.setAction(action);
		  }
		  if(StringUtils.isNotBlank(comment)){
			  humanTaskVo.setComment(comment);
		  }
		  if(StringUtils.isNotBlank(assignee)){
			  humanTaskVo.setAttr3(assignee);
		  }
		  record.setStatus(0);//修改草稿状态为0
		  taskDefaultService.saveOrUpdate(record);
		  //删除对应的截止时间--
		  
		  // 处理抄送任务
		  if ("copy".equals(humanTaskVo.getCategory())) {
			  humanTaskVo.setStatus("complete");
			  humanTaskVo.setAction("完成");
			  this.saveHumanTask(humanTaskVo, false);
			  return;
		  }
		  //处理开始任务
		  if ("startEvent".equals(humanTaskVo.getCategory())) {
			  humanTaskVo.setStatus("complete");
			  humanTaskVo.setAction("提交");
			  this.saveHumanTask(humanTaskVo, false);
			  taskInternalService.signalExecution(humanTaskVo
					  .getExecutionId());
			  
			  return;
		  }
		  // 处理协办任务
		  if ("pending".equals(humanTaskVo.getDelegateStatus())) {
			  humanTaskVo.setStatus("active");
			  humanTaskVo.setDelegateStatus("resolved");
			  humanTaskVo.setAssignee(humanTaskVo.getOwner());
			  humanTaskVo.setAction("完成");
			  this.saveHumanTask(humanTaskVo, false);
			  taskInternalService.resolveTask(humanTaskVo.getTaskId());
			  
			  return;
		  } 

		  // 处理协办链式任务
		  if ("pendingCreate".equals(humanTaskVo.getDelegateStatus())) {
			  humanTaskVo.setDelegateStatus("resolved");
			  humanTaskVo.setStatus("complete");
			  humanTaskVo.setAction("完成");
			  this.saveHumanTask(humanTaskVo, false);
			  if(humanTaskVo.getParentId() != null){
				  HumanTaskVo targetHumanTaskVo = findHumanTask(humanTaskVo.getParentId());
				  targetHumanTaskVo.setStatus("active");
				  if(targetHumanTaskVo.getParentId() == null){
					  targetHumanTaskVo.setDelegateStatus("resolved");
				  }
				  this.saveHumanTask(targetHumanTaskVo, false);
			  }
			  return;
		  }
		  
		  this.saveHumanTask(humanTaskVo, false);

		  if("reimbursement".equals(humanTaskVo.getForm()) && record!=null){//日常费用报销中的金额验证
			  Map<String, OaKVProp> props = record.getProps();
			  if(props!=null){
				  for(Entry<String, OaKVProp> entry:props.entrySet()){
					  String key = entry.getKey();
					  OaKVProp prop = entry.getValue();
					  String value = prop.getValue();
					  if(StringUtils.isBlank(value)){
						  continue;
					  }
					  if("lowercase".equals(key)){
						  taskParameters.put(key, value);
					  }
				  }
			  }
		  }
		  
		  // 判断加签
		  if ("vote".equals(humanTaskVo.getCatalog())
				  && (humanTaskVo.getParentId() != null)) {
			  HumanTaskVo parentTask = this.findHumanTask(humanTaskVo
					  .getParentId());
			  List<HumanTaskVo> list = this.getHumanTask(parentTask.getId());
			  boolean completed = true;
			  for (HumanTaskVo childTask : list) {
				  if (!"complete".equals(childTask.getStatus())) {
					  completed = false;
					  break;
				  }
			  }
			  
			  if (completed) {
				  parentTask.setAssignee(parentTask.getOwner());
				  parentTask.setOwner("");
				  parentTask.setStatus("complete");
				  parentTask.setAction("完成");
				  this.saveHumanTask(parentTask, false);
				  taskInternalService.completeTask(humanTaskVo.getTaskId(),userId, taskParameters);
			  }
		  } else {
			  taskInternalService.completeTask(humanTaskVo.getTaskId(),userId, taskParameters);
		  }
		  
		  if (humanTaskListeners != null) {
			  String id = humanTaskVo.getId();
			  OaTaskInfo taskInfo = taskDefaultService.findUnique(
					  "from OaTaskInfo where id=? and stop_flg=0 and del_flg=0", new OaTaskInfo(), id);
			  for(HumanTaskListener humanTaskListener : humanTaskListeners){
				  try {
					  humanTaskListener.onComplete(taskInfo);
				  } catch (Exception ex) {
				  }
			  }
		  }
	  }

	  /**
	   * 回退任务发起人
	   * @param humanTaskId
	   */
	  public void rollbackInitiator(String humanTaskId){
		  HumanTaskVo humanTaskVo = findHumanTask(humanTaskId);
		  if(humanTaskVo.getId()==null){//任务不存在
			  throw new IllegalStateException("任务不存在");
		  }
		  String user = humanTaskVo.getCreateUser();//流程发起人
		  String taskId = humanTaskVo.getTaskId();
		  String processDefinitionId = humanTaskVo.getProcessDefinitionId();//流程定义id
		  String activityId = taskInternalService.findFirstUserTaskActivityId(processDefinitionId, user);
		  taskInternalService.rollback(taskId, activityId, user);

		  String str=humanTaskVo.getAttr2();
		  //推送提醒
		  String title="";
		  if(StringUtils.isNotBlank(str)){
			  String[] titles=str.split("-");
			  if(titles!=null&&titles.length>0){
				  title=titles[titles.length-1]+"被驳回";
			  }
		  }
		  queueUtils.push_msg_type_reject(user,title);

	  }
	  
	  /**
	   * 回退指定节点
	   * @param activityId 要退回的节点id
	   * @param taskId 当前活动节点的任务id
	   */
	  public void rollback(String processInstanceId,String activityId,String taskId){
		  List<OaTaskInfo> list = humanTaskDao.findActivitiesByActId(processInstanceId,activityId);
		  if(list==null || list.size()==0){
			  throw new IllegalStateException("任务不存在");
		  }
		  OaTaskInfo oaTaskInfo = list.get(0);
		  String user = oaTaskInfo.getLastModifier();//负责人
		  taskInternalService.rollback(taskId, activityId, user);

		  String str=oaTaskInfo.getAttr2();
		  String title="";
		  if(StringUtils.isNotBlank(str)){
			  //驳回推送提醒
			  String[] titles=str.split("-");
			  if(titles!=null&&titles.length>0){
				  title=titles[titles.length-1]+"被驳回";
			  }
		  }
		  queueUtils.push_msg_type_reject(user,title);
	  }
	  
	  /**
	   * 撤销任务
	   * @param taskId 任务id
	   * @return 
	   */
	  public String withdrawTask(String taskId){
		  if(StringUtils.isBlank(taskId)){
			  return "参数为空,撤回失败!";
		  }
		  Integer task = taskInternalService.withdrawTask(taskId);
		  if(task==0){
			  //撤回流程只能由撤回人进行提交
			  List<HumanTaskVo> listByTaskId = findHumanTaskListByTaskId(taskId);
			  if(listByTaskId!=null&&listByTaskId.size()>1){
				  humanTaskDao.changeAssignee(listByTaskId.get(0).getId(),listByTaskId.get(1).getLastModifier(),listByTaskId.get(1).getLastModifierName());
			  }
			  return "撤回成功!";
		  }else if(task==1){
			  return "流程已结束,不能撤回!";
		  }else if(task==2){
			  return "下一结点已经通过,不能撤回!";
		  }else if(task==4){
			  return "退回节点,不能撤回!";
		  }{
			  return "该节点已撤回，不能再撤回!";
		  }
	  }
	  
	 /**
	  * 获取扩展的规则
	  * @param processDefinitionId
	  * @param code
	  * @return
	  */
	 public ExtendVo getConfRule(String processDefinitionId,String code){
		 String extend = oaBpmConfNodeService.getExtend(processDefinitionId, code);
		 try {
			ExtendVo extendVo = JSONUtils.fromJson(extend, ExtendVo.class);
			return extendVo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return null;
	 } 
	 
	 /**
	  * 获取负责人列表
	  * @param processDefinitionId
	  * @param code
	  * @param account
	  * @return
	  */
	 public List<EmployeeExtend> selectAssignee(String processDefinitionId,String code,String account,
			 String processInstanceId,String businessKey){
		 List<EmployeeExtend> empLists= new ArrayList<>();
		 List<OaBpmConfUser> userList = oaBpmConfUserService.getUserList(processDefinitionId, code);
		 String value="";
		 if(userList!=null && userList.size()>0){
			 OaBpmConfUser user = userList.get(0);
			 value = user.getValue();//负责人规则列表
		 }
		 ExtendVo extendVo = getConfRule(processDefinitionId, code);//获取规则
		 if(extendVo!=null){
				if(Boolean.parseBoolean(extendVo.getWithdept())){//与上一节点同部门
					if(StringUtils.isNotBlank(value)&& value.indexOf("dept:{dept}")==-1){
						value = value.replace("]", "&&dept:{dept}]");
					}else{
						value="[dept:{dept}]";
					}
				}
		 }
		 if(StringUtils.isNotBlank(value)){
			 //解析负责人规则
			 String hql="from EmployeeExtend where stop_flg=0 and del_flg=0 and employeeJobNo=? ";
			 EmployeeExtend emp = taskDefaultService.findUnique(hql, new EmployeeExtend(),account);
			 AssigneeAliasHumanTaskListener listener = null;
				if(humanTaskListeners!=null && humanTaskListeners.size()>0){
					for (HumanTaskListener humanTaskListener : humanTaskListeners) {
						if(humanTaskListener instanceof AssigneeAliasHumanTaskListener){
							listener=(AssigneeAliasHumanTaskListener) humanTaskListener;
						}
					}
				}
				if(listener==null){
					listener = new AssigneeAliasHumanTaskListener();
				}
			 OaTaskInfo taskInfo = new OaTaskInfo();
			 taskInfo.setProcessInstanceId(processInstanceId);
			 taskInfo.setBusinessKey(businessKey);
			 String expression = listener.expression(value, emp,taskInfo)
					 .replace("select employeeJobNo", "select employeeName,employeeJobNo");//查询hql语句
			 if(StringUtils.isBlank(expression)){//负责人表达式为空时,将任务负责人设置为系统管理员
				 String admin = parameterInnerService.getparameter("adminOA");
				 if(StringUtils.isBlank(admin)){
					 admin="000000";
				 }
				 expression= "select employeeName,employeeJobNo from EmployeeExtend where employeeJobNo= '"+admin+"'";
			 }
			 List<String[]> list = taskDefaultService.getList(expression, new String[2]);
			 if(list!=null && list.size()>0){
				 for (Object[] strings : list) {
					 EmployeeExtend extend = new EmployeeExtend();
					 extend.setEmployeeName(strings[0].toString());
					 extend.setEmployeeJobNo(strings[1].toString());
					 empLists.add(extend);
				 }
			 }
		 }
		 return empLists;
	 }

	@Override
	public OaKVRecord queryOaKVRecordById(String id) {
		return humanTaskDao.queryOaKVRecordById(id);
	}

	@Override
	public OaBpmProcess queryOaBpmProcessById(String id) {
		return humanTaskDao.queryOaBpmProcessById(id);
	}

	@Override
	public OaFormInfo queryOaFormInfoById(String formCode) throws SQLException, IOException {
		OaFormInfo info = humanTaskDao.queryOaFormInfoById(formCode);
		String str = "";
		if(info.getFormInfo()!=null){
			str = ClobToString(info.getFormInfo());
		}
		info.setFormInfoStr(str);
		return humanTaskDao.queryOaFormInfoById(formCode);
	}

	private String ClobToString(Clob clob) throws SQLException, IOException {   
        String reString = "";   
        java.io.Reader is = clob.getCharacterStream();// 得到流   
        BufferedReader br = new BufferedReader(is);   
        String s = br.readLine();   
        StringBuffer sb = new StringBuffer();   
        while (s != null) {  
            sb.append(s);   
            s = br.readLine();   
        }   
        reString = sb.toString();   
        return reString;   
    }
	
	@Override
	public List<OaKVProp> queryOaKVPropById(String recordId) {
		return humanTaskDao.queryOaKVPropById(recordId);
	}

	@Override
	public List<OaBpmConfForm> queryOaBpmConfFormBybaseId(String baseId) {
		return humanTaskDao.queryOaBpmConfFormBybaseId(baseId);
	}

	@Override
	public String findPreviousActivities(String processInstanceId, String taskId) {
		return humanTaskDao.findPreviousActivities(processInstanceId, taskId);
		
	}
	
	public Map<String,String> getAllPrevious(String processInstanceId, String taskId,String activityId){
		Map<String,String> map = new HashMap<>();
		List<OaTaskInfo> list = humanTaskDao.findCompleteActs(processInstanceId);
		if(list==null|| list.size()==0){
			return map;
		}
		for (OaTaskInfo info : list) {
			map.put(info.getCode(), info.getName());
			if(activityId.equals(info.getCode())){//对于有驳回的流程,取第一次出现的节点
				return map;
			}
		}
		return map;
	}
	
	public OaTaskInfo findPrevious(String processInstanceId, String taskId){
		List<OaTaskInfo> list = humanTaskDao.findAllPreviousActivities(processInstanceId, taskId);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return new OaTaskInfo();
	}
	
	
	public List<OaTaskInfo> findAllPreviousActivities(String processInstanceId, String taskId){
		List<OaTaskInfo> list = humanTaskDao.findAllPreviousActivitieCode(processInstanceId);
		return list;
	}

	@Override
	public void removeHumanTaskByProcessInstanceId(String processInstanceId) {
		humanTaskDao.removeHumanTaskByProcessInstanceId(processInstanceId);
	}

	@Override
	public void removeHumanTaskByTaskId(String taskId) {
		humanTaskDao.removeHumanTaskByTaskId(taskId);
	}
	
	/**
	 * 完成任务/回退
	 * @param userId
	 * @param deptCode
	 * @param tenantId
	 * @param formParameter
	 * @param taskParameters
	 */
	public void complete(String userId,String deptCode,String tenantId,FormParameterVo formParameter,Map<String, Object> taskParameters){
		saveDraft(userId, deptCode, tenantId, formParameter);//保存草稿
		String humanTaskId = formParameter.getHumanTaskId();
		HumanTaskVo humanTaskVo = findHumanTask(humanTaskId);
		String processInstanceId = humanTaskVo.getProcessInstanceId();
		String businessKey = humanTaskVo.getBusinessKey();
		OaKVRecord record = getRecord(businessKey);
		String zkhonryState= (String)taskParameters.get("zkhonryState");
		if("0".equals(zkhonryState)){
			completeTask(humanTaskId, userId, formParameter, taskParameters, record, processInstanceId);
		}else if("1".equals(zkhonryState)){
			rollbackInitiator(humanTaskId);
		}else{
			String rollbackActivityId= (String)taskParameters.get("rollbackActivityId");
			rollback(processInstanceId,rollbackActivityId,humanTaskVo.getTaskId());
		}
	}
}
