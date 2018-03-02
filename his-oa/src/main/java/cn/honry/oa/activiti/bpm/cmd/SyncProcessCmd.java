package cn.honry.oa.activiti.bpm.cmd;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.cmd.GetBpmnModelCmd;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.apache.commons.lang3.StringUtils;

import cn.honry.base.bean.model.OaBpmConfBase;
import cn.honry.base.bean.model.OaBpmConfCountersign;
import cn.honry.base.bean.model.OaBpmConfForm;
import cn.honry.base.bean.model.OaBpmConfListener;
import cn.honry.base.bean.model.OaBpmConfNode;
import cn.honry.base.bean.model.OaBpmConfUser;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.oa.activiti.bpm.base.service.OaBpmConfBaseService;
import cn.honry.oa.activiti.bpm.base.service.impl.OaBpmConfBaseServiceImpl;
import cn.honry.oa.activiti.bpm.form.service.OaBpmConfFormService;
import cn.honry.oa.activiti.bpm.form.service.impl.OaBpmConfFormServiceImpl;
import cn.honry.oa.activiti.bpm.form.vo.XformField;
import cn.honry.oa.activiti.bpm.graph.Graph;
import cn.honry.oa.activiti.bpm.graph.Node;
import cn.honry.oa.activiti.bpm.listener.service.OaBpmConfListenerService;
import cn.honry.oa.activiti.bpm.listener.service.impl.OaBpmConfListenerServiceImpl;
import cn.honry.oa.activiti.bpm.node.service.OaBpmConfNodeService;
import cn.honry.oa.activiti.bpm.node.service.impl.OaBpmConfNodeServiceImpl;
import cn.honry.oa.activiti.bpm.utils.ApplicationContextHelper;
import cn.honry.oa.activiti.task.service.TaskDefaultService;
import cn.honry.oa.activiti.task.service.impl.TaskDefaultServiceImpl;
import cn.honry.oa.activiti.task.service.impl.TaskDefinitionServiceImpl;
import cn.honry.oa.activiti.task.vo.TaskDefinitionVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

/**
 * 执行命令
 * 把xml解析的内存模型保存到数据库中
 * @author luyanshou
 *
 */
public class SyncProcessCmd implements Command<Void> {


	/** 流程定义id. */
	private String processDefinitionId;
	/** 扩展属性 */
	private Map<String,String> extendMap;

    /**
     * 构造方法.
     */
    public SyncProcessCmd(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    /**
     * 构造方法.
     */
    public SyncProcessCmd(String processDefinitionId,Map<String,String> extendMap) {
    	this.processDefinitionId = processDefinitionId;
    	this.extendMap = extendMap;
    }
	
    public SyncProcessCmd() {
    	
	}
    
	@Override
	public Void execute(CommandContext commandContext) {
		ProcessDefinitionEntity processDefinitionEntity = new GetDeploymentProcessDefinitionCmd(
                processDefinitionId).execute(commandContext);//获取流程定义实体
        String processDefinitionKey = processDefinitionEntity.getKey();//获取流程定义key
        int processDefinitionVersion = processDefinitionEntity.getVersion();//获取流程定义版本号
        //根据流程定义可以和版本号 获取流程配置
        OaBpmConfBaseService oaBpmConfBaseService=ApplicationContextHelper.getBean(OaBpmConfBaseServiceImpl.class);
        OaBpmConfBase bpmConfBase = oaBpmConfBaseService.findUnique(processDefinitionKey, processDefinitionVersion);
        if (bpmConfBase == null){
        	bpmConfBase = new OaBpmConfBase();
        	
            bpmConfBase.setProcessDefinitionId(processDefinitionId);
            bpmConfBase.setProcessDefinitionKey(processDefinitionKey);
            bpmConfBase.setProcessDefinitionVersion(processDefinitionVersion);
            bpmConfBase.setCreateTime(new Date());
            bpmConfBase.setCreateUser(getAccount());
            bpmConfBase.setCreateDept(getDept());
            oaBpmConfBaseService.saveOrUpdate(bpmConfBase);
        }else if(StringUtils.isBlank(bpmConfBase.getProcessDefinitionId())){
        	bpmConfBase.setProcessDefinitionId(processDefinitionId);
        }
        
        BpmnModel bpmnModel = new GetBpmnModelCmd(processDefinitionId)
        .execute(commandContext);
        Graph graph = new FindGraphCmd(processDefinitionId)
        .execute(commandContext);
        processGlobal(bpmnModel, 1, bpmConfBase);//全局配置
        int priority = 2;//因为全局配置的序号为1,这里从2开始
        for(Node node : graph.getNodes()){
        	if ("exclusiveGateway".equals(node.getType())) {
                continue;
            } else if ("userTask".equals(node.getType())) {//用户任务
                this.processUserTask(node, bpmnModel, priority++, bpmConfBase);
            } else if ("startEvent".equals(node.getType())) {//开始事件
                this.processStartEvent(node, bpmnModel, priority++, bpmConfBase);
            } else if ("endEvent".equals(node.getType())) {//结束事件
                this.processEndEvent(node, bpmnModel, priority++, bpmConfBase);
            }
        }
		return null;
	}


	/**
     * 全局配置.
     */
    public void processGlobal(BpmnModel bpmnModel, int priority,
            OaBpmConfBase bpmConfBase){
    	 Process process = bpmnModel.getMainProcess();
    	 OaBpmConfNodeService oaBpmConfNodeService=ApplicationContextHelper.getBean(OaBpmConfNodeServiceImpl.class);
    	 OaBpmConfNode bpmConfNode = oaBpmConfNodeService.findUinque(process.getId(), bpmConfBase);
    	 if (bpmConfNode == null) {
             bpmConfNode = new OaBpmConfNode();
             bpmConfNode.setCode(process.getId());
             bpmConfNode.setName("全局");
             bpmConfNode.setType("process");
             bpmConfNode.setConfUser("2");
             bpmConfNode.setConfListener("0");
             bpmConfNode.setConfRule("2");
             bpmConfNode.setConfForm("0");
             bpmConfNode.setConfOperation("2");
             bpmConfNode.setConfNotice("2");
             bpmConfNode.setPriority(priority);
             bpmConfNode.setBpmConfBase(bpmConfBase);
             bpmConfNode.setCreateTime(new Date());
             bpmConfNode.setCreateDept(getDept());
             bpmConfNode.setCreateUser(getAccount());
             oaBpmConfNodeService.saveOrUpdate(bpmConfNode);
         }
    	// 配置监听器
    	 processListener(process.getExecutionListeners(), bpmConfNode);
    }
    
    /**
     * 配置用户任务.
     */
    public void processUserTask(Node node, BpmnModel bpmnModel, int priority,
            OaBpmConfBase bpmConfBase){
    	OaBpmConfNodeService oaBpmConfNodeService=ApplicationContextHelper.getBean(OaBpmConfNodeServiceImpl.class);
    	OaBpmConfNode bpmConfNode = oaBpmConfNodeService.findUinque(node.getId(), bpmConfBase);
    	if (bpmConfNode == null) {
            bpmConfNode = new OaBpmConfNode();
            bpmConfNode.setCode(node.getId());
            bpmConfNode.setName(node.getName());
            bpmConfNode.setType(node.getType());
            bpmConfNode.setConfUser("0");
            bpmConfNode.setConfListener("0");
            bpmConfNode.setConfRule("0");
            bpmConfNode.setConfForm("0");
            bpmConfNode.setConfOperation("0");
            bpmConfNode.setConfNotice("0");
            bpmConfNode.setPriority(priority);
            bpmConfNode.setBpmConfBase(bpmConfBase);
            bpmConfNode.setCreateTime(new Date());
            bpmConfNode.setCreateDept(getDept());
            bpmConfNode.setCreateUser(getAccount());
            bpmConfNode.setExtend(extendMap.get(node.getId()));
            oaBpmConfNodeService.saveOrUpdate(bpmConfNode);
        }
    	//配置参与者
    	UserTask userTask = (UserTask) bpmnModel.getFlowElement(node.getId());
        int index = 1;
        index = this.processUserTaskConf(bpmConfNode, userTask.getAssignee(),
                0, index);
        for (String candidateUser : userTask.getCandidateUsers()) {//配置用户
            index = this.processUserTaskConf(bpmConfNode, candidateUser, 1,
                    index);
        }

        for (String candidateGroup : userTask.getCandidateGroups()) {//配置用户组
            this.processUserTaskConf(bpmConfNode, candidateGroup, 2, index);
        }
        // 配置监听器
        this.processListener(userTask.getExecutionListeners(), bpmConfNode);
        this.processListener(userTask.getTaskListeners(), bpmConfNode);
        // 配置表单
        List<FormProperty> formProperties = userTask.getFormProperties();
        Map<String,XformField> map = new HashMap<>();
        for (FormProperty formProperty : formProperties) {
        	XformField xformField = new XformField();
        	xformField.setDisplay(formProperty.isReadable());
        	xformField.setReadonly(!formProperty.isWriteable());
        	xformField.setRequired(formProperty.isRequired());
        	String id = formProperty.getId();
        	map.put(id, xformField);
        }
        String json = JSONUtils.toExposeJson(map, false, null, "required","readonly","display");
        this.processForm(userTask.getFormKey(), bpmConfNode,json);
       
        
        TaskDefinitionVo taskDefinition= new TaskDefinitionVo();
        
        // 会签
        if (userTask.getLoopCharacteristics() != null){
        	OaBpmConfCountersign bpmConfCountersign = new OaBpmConfCountersign();
        	bpmConfCountersign.setType(0);
            bpmConfCountersign.setRate(100);
            boolean sequential = userTask.getLoopCharacteristics().isSequential();
            bpmConfCountersign.setSequential(sequential?1:0);//1-按顺序的;0-平行的(即 无序的)
            bpmConfCountersign.setNodeId(bpmConfNode.getId());
            TaskDefaultService taskDefaultService = ApplicationContextHelper.getBean(TaskDefaultServiceImpl.class);
            taskDefaultService.saveOrUpdate(bpmConfCountersign);
            
            OaBpmConfCountersign counterSign= new OaBpmConfCountersign();
            counterSign.setStrategy("percent");//百分比
            counterSign.setRate(100);
            counterSign.setType(sequential?1:0);
            taskDefinition.setCountersign(counterSign);
        }
        
        //添加运行时任务的相关数据
        taskDefinition.setCode(userTask.getId());
        taskDefinition.setName(userTask.getName());
        taskDefinition.setProcessDefinitionId(bpmConfBase.getProcessDefinitionId());
        taskDefinition.setAssignee(userTask.getAssignee());

        for (String candidateUser : userTask.getCandidateUsers()) {
            taskDefinition.addCandidateUser(candidateUser);
        }
        for (String candidateGroup : userTask.getCandidateGroups()) {
            taskDefinition.addCandidateGroup(candidateGroup);
        }
        String formKey = userTask.getFormKey();
        if(StringUtils.isNotBlank(formKey)){
        	taskDefinition.setFormKey(formKey);
        	if (formKey.startsWith("external:")) {
        		taskDefinition.setFormType("external");
            } else {
            	taskDefinition.setFormType("internal");
            }
        }
        
        TaskDefinitionServiceImpl taskDefinitionService = ApplicationContextHelper.getBean(TaskDefinitionServiceImpl.class);
        taskDefinitionService.create(taskDefinition);
    }

    /**
     * 配置开始事件.
     */
    public void processStartEvent(Node node, BpmnModel bpmnModel, int priority,
            OaBpmConfBase bpmConfBase){
    	OaBpmConfNodeService oaBpmConfNodeService=ApplicationContextHelper.getBean(OaBpmConfNodeServiceImpl.class);
    	OaBpmConfNode bpmConfNode = oaBpmConfNodeService.findUinque(node.getId(), bpmConfBase);
    	if (bpmConfNode == null) {
            bpmConfNode = new OaBpmConfNode();
            bpmConfNode.setCode(node.getId());
            bpmConfNode.setName(node.getName());
            bpmConfNode.setType(node.getType());
            bpmConfNode.setConfUser("2");
            bpmConfNode.setConfListener("0");
            bpmConfNode.setConfRule("2");
            bpmConfNode.setConfForm("0");
            bpmConfNode.setConfOperation("2");
            bpmConfNode.setConfNotice("0");
            bpmConfNode.setPriority(priority);
            bpmConfNode.setBpmConfBase(bpmConfBase);
            bpmConfNode.setCreateTime(new Date());
            bpmConfNode.setCreateDept(getDept());
            bpmConfNode.setCreateUser(getAccount());
            oaBpmConfNodeService.saveOrUpdate(bpmConfNode);
        }
    	FlowElement flowElement = bpmnModel.getFlowElement(node.getId());
    	//配置监听器
    	processListener(flowElement.getExecutionListeners(), bpmConfNode);
    	//配置表单
    	StartEvent startEvent = (StartEvent) flowElement;
    	String formKey = startEvent.getFormKey();
    	 // 配置表单
        this.processForm(formKey, bpmConfNode,null);
    }
    
    /**
     * 配置结束事件.
     */
    public void processEndEvent(Node node, BpmnModel bpmnModel, int priority,
            OaBpmConfBase bpmConfBase){
    	OaBpmConfNodeService oaBpmConfNodeService=ApplicationContextHelper.getBean(OaBpmConfNodeServiceImpl.class);
    	OaBpmConfNode bpmConfNode = oaBpmConfNodeService.findUinque(node.getId(), bpmConfBase);
    	if (bpmConfNode == null) {
            bpmConfNode = new OaBpmConfNode();
            bpmConfNode.setCode(node.getId());
            bpmConfNode.setName(node.getName());
            bpmConfNode.setType(node.getType());
            bpmConfNode.setConfUser("2");
            bpmConfNode.setConfListener("0");
            bpmConfNode.setConfRule("2");
            bpmConfNode.setConfForm("2");
            bpmConfNode.setConfOperation("2");
            bpmConfNode.setConfNotice("0");
            bpmConfNode.setPriority(priority);
            bpmConfNode.setBpmConfBase(bpmConfBase);
            bpmConfNode.setCreateTime(new Date());
            bpmConfNode.setCreateDept(getDept());
            bpmConfNode.setCreateUser(getAccount());
            oaBpmConfNodeService.saveOrUpdate(bpmConfNode);
        }
    	FlowElement flowElement = bpmnModel.getFlowElement(node.getId());
    	//配置监听器
    	processListener(flowElement.getExecutionListeners(), bpmConfNode);
    }
    
    /**
     * 配置监听器
     * @param activitiListeners 监听器集合
     * @param bpmConfNode 节点配置
     */
    public void processListener(List<ActivitiListener> activitiListeners,
            OaBpmConfNode bpmConfNode){
    	Map<String, Integer> eventTypeMap = new HashMap<String, Integer>();
        eventTypeMap.put("start", 0);
        eventTypeMap.put("end", 1);
        eventTypeMap.put("take", 2);
        eventTypeMap.put("create", 3);
        eventTypeMap.put("assignment", 4);
        eventTypeMap.put("complete", 5);
        eventTypeMap.put("delete", 6);
        OaBpmConfListenerService oaBpmConfListenerService = ApplicationContextHelper.getBean(OaBpmConfListenerServiceImpl.class);
        for (ActivitiListener listener : activitiListeners){
        	String value = listener.getImplementation();
        	int type = eventTypeMap.get(listener.getEvent());
        	String nodeCode=bpmConfNode.getId();
        	OaBpmConfListener bpmConfListener = oaBpmConfListenerService.findUnique(value, type, nodeCode);
        	if(bpmConfListener!=null){
        		bpmConfListener = new OaBpmConfListener();
        		bpmConfListener.setCode("");//编号
                bpmConfListener.setValue(value);
                bpmConfListener.setType(type);
                bpmConfListener.setNodeCode(nodeCode);
                bpmConfListener.setCreateTime(new Date());
                bpmConfListener.setCreateDept(getDept());
                bpmConfListener.setCreateUser(getAccount());
                oaBpmConfListenerService.saveOrUpdate(bpmConfListener);
        	}
        }
    }
    
    /**
     * 配置参与者
     * @param bpmConfNode 节点配置
     * @param value 值
     * @param type 类型
     * @param priority 排序
     * @return
     */
    public int processUserTaskConf(OaBpmConfNode bpmConfNode, String value,int type, int priority){
    	if (value == null) {
            return priority;
        }
    	String nodeId = bpmConfNode.getId();
    	TaskDefaultService taskDefaultService = ApplicationContextHelper.getBean(TaskDefaultServiceImpl.class);
    	OaBpmConfUser bpmConfUser = taskDefaultService.findUnique(
    			"from OaBpmConfUser where value=? and type=? and priority=? and nodeId=? and stop_flg=0 and del_flg=0 ",
    			new OaBpmConfUser(), value, type, priority, bpmConfNode.getId());
    	if (bpmConfUser == null) {
            bpmConfUser = new OaBpmConfUser();
            bpmConfUser.setValue(value);
            bpmConfUser.setType(type);
            bpmConfUser.setStatus(0);
            bpmConfUser.setPriority(priority);
            bpmConfUser.setNodeId(nodeId);
            taskDefaultService.saveOrUpdate(bpmConfUser);
        }

        return priority + 1;
    }
    
    /**
     * 配置表单
     * @param formKey 表单编号
     * @param bpmConfNode 节点配置
     * @param properties 绑定的表单属性
     */
    public void processForm(String formKey,OaBpmConfNode bpmConfNode,String properties){
    	if (StringUtils.isBlank(formKey)) {
            return;
        }
    	OaBpmConfFormService oaBpmConfFormService = ApplicationContextHelper.getBean(OaBpmConfFormServiceImpl.class);
    	OaBpmConfForm bpmConfForm = oaBpmConfFormService.getFormByNodeId(bpmConfNode.getId());
    	if(bpmConfForm==null){
    		bpmConfForm = new OaBpmConfForm();
    		bpmConfForm.setCode("");//编号
            bpmConfForm.setValue(formKey);
            bpmConfForm.setType(0);
            bpmConfForm.setOriginValue(formKey);
            bpmConfForm.setOriginType(0);
            bpmConfForm.setStatus(0);
            bpmConfForm.setConfNodeCode(bpmConfNode.getId());
            bpmConfForm.setCreateTime(new Date());
            bpmConfForm.setCreateDept(getDept());
            bpmConfForm.setCreateUser(getAccount());
    	}else{
    		bpmConfForm.setUpdateTime(new Date());
    		bpmConfForm.setUpdateUser(getAccount());
    	}
    	bpmConfForm.setProperties(properties);
    	oaBpmConfFormService.saveOrUpdate(bpmConfForm); 
    }
    
    /**
     * 登录用户
     * @return
     */
    public String getAccount(){
    	String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
    	return account;
    }
    
    /**
     * 登录科室
     * @return
     */
    public String getDept(){
    	SysDepartment department = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//当前科室;
		if(department != null){
			return department.getDeptCode();
		}else{
			return null;
		}
    }
}
