package cn.honry.oa.activiti.operation.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.Command;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.EmployeeExtend;
import cn.honry.base.bean.model.OaBpmConfBase;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaKVProp;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.fastdfs.FastDFSClient;
import cn.honry.inner.system.upload.service.UploadFileService;
import cn.honry.oa.activiti.bpm.cmd.FindHistoryGraphCmd;
import cn.honry.oa.activiti.bpm.cmd.HistoryProcessInstanceDiagramCmd;
import cn.honry.oa.activiti.bpm.form.service.OaBpmConfFormService;
import cn.honry.oa.activiti.bpm.form.vo.ButtonVo;
import cn.honry.oa.activiti.bpm.form.vo.ButtonVoHelper;
import cn.honry.oa.activiti.bpm.form.vo.ConfFormVo;
import cn.honry.oa.activiti.bpm.form.vo.FormParameterVo;
import cn.honry.oa.activiti.bpm.form.vo.Xform;
import cn.honry.oa.activiti.bpm.form.vo.XformBuilder;
import cn.honry.oa.activiti.bpm.graph.Graph;
import cn.honry.oa.activiti.bpm.process.service.OaBpmProcessService;
import cn.honry.oa.activiti.bpm.process.vo.ProcessTaskDefinition;
import cn.honry.oa.activiti.operation.service.OaOperationService;
import cn.honry.oa.activiti.operation.vo.NodeVo;
import cn.honry.oa.activiti.task.service.TaskDefaultService;
import cn.honry.oa.activiti.task.service.TaskInternalService;
import cn.honry.oa.activiti.tenant.service.TenantService;
import cn.honry.oa.extend.service.ExtendService;
import cn.honry.oa.humantask.service.HumanTaskService;
import cn.honry.oa.humantask.vo.HumanTaskVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 流程相关操作Action
 * @author luyanshou
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/activiti/operation")
@SuppressWarnings({ "all" })
public class OaOperationAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource
	private ProcessEngine processEngine;//工作流引擎
	
	@Autowired
	@Qualifier(value = "oaBpmProcessService")
	private OaBpmProcessService oaBpmProcessService;

	public void setOaBpmProcessService(OaBpmProcessService oaBpmProcessService) {
		this.oaBpmProcessService = oaBpmProcessService;
	}
	
	@Autowired
	@Qualifier(value = "oaBpmConfFormService")
	private OaBpmConfFormService oaBpmConfFormService;

	public void setOaBpmConfFormService(OaBpmConfFormService oaBpmConfFormService) {
		this.oaBpmConfFormService = oaBpmConfFormService;
	}

	@Autowired
	@Qualifier(value = "oaOperationService")
	private OaOperationService oaOperationService;
	
	public void setOaOperationService(OaOperationService oaOperationService) {
		this.oaOperationService = oaOperationService;
	}

	@Autowired
	@Qualifier(value = "taskDefaultService")
	private TaskDefaultService taskDefaultService;

	public void setTaskDefaultService(TaskDefaultService taskDefaultService) {
		this.taskDefaultService = taskDefaultService;
	}
	
	@Autowired
	@Qualifier(value = "taskInternalService")
	private TaskInternalService taskInternalService;
	
	public void setTaskInternalService(TaskInternalService taskInternalService) {
		this.taskInternalService = taskInternalService;
	}

	@Autowired
	@Qualifier(value = "humanTaskService")
	private HumanTaskService humanTaskService;
	
	public void setHumanTaskService(HumanTaskService humanTaskService) {
		this.humanTaskService = humanTaskService;
	}

	@Autowired
	@Qualifier(value = "uploadFileService")
	private UploadFileService uploadFileService;
	
	public void setUploadFileService(UploadFileService uploadFileService) {
		this.uploadFileService = uploadFileService;
	}
	
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	
	@Autowired
	@Qualifier(value = "tenantService")
	private TenantService tenantService;
	
	public void setTenantService(TenantService tenantService) {
		this.tenantService = tenantService;
	}
	/**文件名*/
	private List<File> fileReason;
	private List<String> fileReasonFileName;
	/**文件名*/
	private List<File> fileReason1;
	private List<String> fileReason1FileName;
	private ButtonVoHelper voHelper=new ButtonVoHelper();
	private String id;
	private Date completeTime;
	public Date getCompleteTime() {
		return completeTime;
	}


	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	private String businessKey;
	
	private String processInstanceId;
	
	public List<File> getFileReason1() {
		return fileReason1;
	}


	public void setFileReason1(List<File> fileReason1) {
		this.fileReason1 = fileReason1;
	}
	public List<String> getFileReason1FileName() {
		return fileReason1FileName;
	}


	public void setFileReason1FileName(List<String> fileReason1FileName) {
		this.fileReason1FileName = fileReason1FileName;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getBusinessKey() {
		return businessKey;
	}


	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

public List<File> getFileReason() {
		return fileReason;
	}


	public void setFileReason(List<File> fileReason) {
		this.fileReason = fileReason;
	}


	public List<String> getFileReasonFileName() {
		return fileReasonFileName;
	}


	public void setFileReasonFileName(List<String> fileReasonFileName) {
		this.fileReasonFileName = fileReasonFileName;
	}


	//--------------------------------------发起流程---------------------------------------------------------------
	/**
	 * 发起流程界面
	 * @return
	 */
	@Action(value = "viewStartForm", results = { 
			@Result(name = "list",location = "/WEB-INF/pages/oa/activiti/operation/startProcess.jsp"),
			@Result(name = "error",location = "/WEB-INF/pages/oa/activiti/operation/startProcess.jsp"),
			@Result(name = "form",location = "/WEB-INF/pages/oa/activiti/operation/viewStartForm.jsp"),
			@Result(name = "task",location = "/WEB-INF/pages/oa/activiti/operation/taskConf.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewStartForm(){
		String tenantId = tenantService.getTenantId();//获取租户id
		String processDefinitionId= "";
		if(StringUtils.isBlank(id)){
			return "error";
		}
		
		FormParameterVo formParameter = new FormParameterVo();
		formParameter.setBpmProcessId(id);
        formParameter.setBusinessKey(businessKey);
        
		OaBpmProcess bpmProcess = oaBpmProcessService.get(id);
		OaBpmConfBase bpmConfBase = oaBpmProcessService.getConfBase(id);
		
		Integer useTaskConf = bpmProcess.getUseTaskConf();
		if(bpmConfBase!=null){
			processDefinitionId = bpmConfBase.getProcessDefinitionId();
			ConfFormVo vo = oaBpmConfFormService.getForm(processDefinitionId);
			formParameter.setFormVo(vo);
			if(vo==null){//未绑定表单
				HttpServletRequest request = ServletActionContext.getRequest();
				request.setAttribute("bpmProcessId", id);
				request.setAttribute("businessKey", businessKey);
				return "list";
			}
			if(StringUtils.isNotBlank(vo.getCode())){
				if(vo.isRedirect()){//外部表单
					//应该重定向到外部表单的URL
					String url = vo.getUrl()+"?processDefinitionId="+processDefinitionId;
				}
				
				if(useTaskConf!=null && useTaskConf==1){//需配置任务负责人
					formParameter.setNextStep("taskConf");
				}else{
					formParameter.setNextStep("confirmStartProcess");
				}
				//调用显示开始表单的方法.
				return startform(formParameter, tenantId);
			}else if(useTaskConf!=null && useTaskConf==1){//vo.code()为空,同时useTaskConf为1 即:没找到form表单,但需要配置任务负责人
				 formParameter.setProcessDefinitionId(processDefinitionId);
				 //执行配置任务负责人的方法
				 return doTaskConf(formParameter);
			}else{
				// 如果也不需要配置任务负责人，就直接进入确认发起流程
				return startProcess(formParameter);
			}
		}
		
		
		return "list";
	}

	/**
	 * 显示表单
	 * @param formParameter
	 * @param tenantId
	 * @return
	 */
	public String startform(FormParameterVo formParameter,String tenantId){
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String,String> parMap = new HashMap<String, String>();
		if(StringUtils.isNotBlank(processInstanceId)){//关联流程
			String praArr[] = processInstanceId.split(",");
			for(String pra : praArr){
				String[] pArr = pra.split("_");
				parMap.put(pArr[0], pArr[1]);
			}
		}
		SysEmployee empl = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
		if(empl!=null){//员工
			if(StringUtils.isNotBlank(empl.getName())){
				parMap.put("name", empl.getName());
				parMap.put("people", empl.getName());
				parMap.put("applicant", empl.getName());
				parMap.put("selfName", empl.getName());
			}

			if(StringUtils.isNotBlank(empl.getEducation())){
                Map<String, String> map = oaOperationService.findEdcuations();
                parMap.put("education", map.get(empl.getEducation()));
            }
		}


		EmployeeExtend empExtend = employeeInInterService.getEmpExtend(empl.getJobNo());
		if(empExtend!=null){
			if(StringUtils.isNotBlank(empExtend.getDutiesName())){
				parMap.put("post", empExtend.getDutiesName());//职务
			}
			if(StringUtils.isNotBlank(empExtend.getTitleName())){
				parMap.put("title", empExtend.getTitleName());//职称
			}
			if(StringUtils.isNotBlank(empExtend.getDepartment())){//科室
				parMap.put("dept", empExtend.getDepartment());
				parMap.put("selfDept", empExtend.getDepartment());
			}
			if(StringUtils.isNotBlank(empExtend.getEmployeeTypeName())){//员工类型名称
				parMap.put("appType", empExtend.getEmployeeTypeName());
			}
			if(StringUtils.isNotBlank(empExtend.getEmployeeSexName())){//性别
				parMap.put("selfSex", empExtend.getEmployeeSexName());
			}
			if(StringUtils.isNotBlank(empExtend.getEmployeeMobile())){//手机号
				parMap.put("phone", empExtend.getEmployeeMobile());
			}
			if(StringUtils.isNotBlank(empExtend.getEmployeeJobNo())){//工号
				parMap.put("jobNo", empExtend.getEmployeeJobNo());
			}
			if(StringUtils.isNotBlank(empExtend.getEmployeeIdentityCard())){//身份证号
				parMap.put("cardNo", empExtend.getEmployeeIdentityCard());
			}
			if(empExtend.getEmployeeBirthday()!=null){
				DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
				String date = dt.format(empExtend.getEmployeeBirthday());
				parMap.put("selfBirthday", date);
				parMap.put("birthday", date);

				//年龄 age 单位：年  比如  18岁
                Date nowDate = new Date();
                int nowYear = Integer.parseInt(DateUtils.formatDateY(nowDate));
                int bYaer = Integer.parseInt(DateUtils.formatDateY(empExtend.getEmployeeBirthday()));
                parMap.put("age", (nowYear-bYaer)+"岁");

            }



		}
		
		parMap.put("startDate", DateUtils.formatDateY_M_D(DateUtils.addDay(new Date(), 1)));//开始时间
		parMap.put("workDate", DateUtils.formatDateY_M_D(new Date()));//到职时间
		
		request.setAttribute("initMap", JSONUtils.toJson(parMap));
		request.setAttribute("formVo",formParameter.getFormVo());
		request.setAttribute("bpmProcessId", formParameter.getBpmProcessId());
        request.setAttribute("businessKey", formParameter.getBusinessKey());
        request.setAttribute("nextStep", formParameter.getNextStep());
        request.setAttribute("json", new HashMap<String, String>());
        List<ButtonVo> list = new ArrayList<ButtonVo>();
        OaBpmProcess bpmProcess = oaBpmProcessService.get(formParameter.getBpmProcessId());
        if(bpmProcess!=null && StringUtils.isBlank(bpmProcess.getTopFlow())){//有前置流程的流程,不设置保存草稿按钮
        	list.add(voHelper.findButton("saveDraft"));
        }
        list.add(voHelper.findButton(formParameter.getNextStep()));
        request.setAttribute("buttons", list);//页面显示的按钮列表
        
        OaKVRecord record = oaBpmConfFormService.getRecord(formParameter.getBusinessKey());
        String content = formParameter.getFormVo().getContent();
        if(record!=null){
        	Map<String, OaKVProp> props = record.getProps();//获取保存的表单草稿数据
			String json = JSONUtils.toJson(props);
        	if(StringUtils.isNotBlank(json)){
        		request.setAttribute("json", json);
        	}
        	
        	 Xform xform = new XformBuilder().setTaskDefaultService(taskDefaultService)
        			 .setContent(content).setRecord(record).build();
        }else{
        	XformBuilder builder = new XformBuilder();
        	builder.setContent(content);
        	 Xform xform = builder.setContent(content).build();
        	 request.setAttribute("xform", xform);
        }
		return "form";
	}
	
	/**
	 * 配置任务负责人的方法
	 * @param formParameter
	 * @return
	 */
	public String doTaskConf(FormParameterVo formParameter){
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("bpmProcessId", formParameter.getBpmProcessId());
        request.setAttribute("businessKey", formParameter.getBusinessKey());
        request.setAttribute("nextStep", formParameter.getNextStep());
        String processDefinitionId = formParameter.getProcessDefinitionId();
        if(StringUtils.isNotBlank(processDefinitionId)){
        	
        	List<ProcessTaskDefinition> list = taskInternalService.findTaskDefinitions(processDefinitionId);
        	request.setAttribute("taskDefinitions", list);
        }
		return "task";
	}
	
	/**
	 * 跳转到确认发起流程页面
	 * @param formParameter
	 * @return
	 */
	public String startProcess(FormParameterVo formParameter){
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("bpmProcessId", formParameter.getBpmProcessId());
        request.setAttribute("businessKey", formParameter.getBusinessKey());
        request.setAttribute("nextStep", formParameter.getNextStep());
        //配置每个任务的负责人
        humanTaskService.configTaskDef(formParameter.getBusinessKey(),
                formParameter.getList("taskDefinitionKeys"),
                formParameter.getList("taskAssignees"));
        
		return "list";
	}
	
	public void taskConf(){
		
	}
	
	/**
	 * 执行发起流程的方法
	 * @return
	 */
	@Action(value="startProcessInstance")
	public void startProcessInstance(){
		Map<String,Object> map= new HashMap<>();
		try {
		OaBpmProcess bpmProcess = oaBpmProcessService.get(id);
		FormParameterVo formParameter = saveRecode(bpmProcess.getName());//保存表单数据
		startProcess(formParameter);//配置任务负责人
		
		OaBpmConfBase confBase = oaBpmProcessService.getConfBase(id);
		String processDefinitionId = confBase.getProcessDefinitionId();//流程定义id
		ConfFormVo formVo = oaBpmConfFormService.getForm(processDefinitionId);//获取表单数据
		String content="";
		if(formVo!=null){
			content=formVo.getContent();
		}
		String key = formParameter.getBusinessKey();
		OaKVRecord record = oaBpmConfFormService.getRecord(key);
		Xform xform = new XformBuilder().setTaskDefaultService(taskDefaultService)
				.setContent(content).setRecord(record).build();
			Map<String, Object> mapData = xform.getMapData();
			if(mapData==null){
				mapData=new HashMap<>();
			}
			EmployeeExtend empExtend = employeeInInterService.getEmpExtend(getAccount());
			if(empExtend!=null){
				mapData.put("employeeType", empExtend.getEmployeeType());
				mapData.put("dutiesLevel", empExtend.getDutiesLevel());
				mapData.put("deptAreaCode", empExtend.getAreaCode());
				mapData.put("departmentCode", empExtend.getDepartmentCode());
			}else{
				mapData.put("employeeType","HLRY");
				mapData.put("dutiesLevel", "");
			}
			String processInstanceId = oaOperationService.startProcess(getAccount(), key, processDefinitionId, mapData,bpmProcess);//发起流程
			if(record!=null){
				record.setStatus(0);//修改状态为正常
				taskDefaultService.saveOrUpdate(record);
			}
			String humanTaskId = taskDefaultService.findUnique(
					"select id from OaTaskInfo where processInstanceId=? and stop_flg=0 and del_flg=0","",processInstanceId);
			map.put("humanTaskId", humanTaskId);
			map.put("resMsg", "success");
			map.put("resCode", "提交成功！");
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "提交失败！");
			e.printStackTrace();
		}
		
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
	/**
	 * 保存草稿
	 * @return
	 */
	@Action(value="saveDraft")
	public void saveDraft(){
		Map<String,Object> map= new HashMap<>();
		try {
			OaBpmProcess bpmProcess = oaBpmProcessService.get(id);
			FormParameterVo formParameter = saveRecode1(bpmProcess.getName());//保存表单数据
			map.put("resMsg", "success");
			map.put("resCode", "提交成功！");
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "提交失败！");
			e.printStackTrace();
		}
		
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 把数据先保存到记录表(OaKVRecord)中.
	 * @return
	 */
	public FormParameterVo saveRecode1(String bpmProcessName){
		FormParameterVo formParameter=new FormParameterVo();
		formParameter.setBusinessKey(businessKey);
		formParameter.setBpmProcessId(id);
		String tenantId = tenantService.getTenantId();//租户id
		//文件上传
		List<String> urllist = new ArrayList<String>();
		String urlandName = "";
		if(fileReason!=null&&fileReason.size()>0){
			for (int i = 0; i < fileReason.size(); i++) {
				File file = fileReason.get(i);
				if(file!=null){
					String fname = fileReasonFileName.get(i);
					FastDFSClient client = null;
					try {
						client = new FastDFSClient();
					} catch (Exception e) {
						e.printStackTrace();
					}
					String fileurl = client.uploadFile(file, fname);
					
//					String fileurl = uploadFileService.fileUpload(file, fname, "1", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					if(StringUtils.isNoneBlank(urlandName)){
						urlandName +=";";
					}
					urlandName += fname +"#"+fileurl;
				}
			}
			if(StringUtils.isNotBlank(urlandName)){
				urllist.add(urlandName);
			}
		}
		//文件上传
		List<String> urllist1 = new ArrayList<String>();
		String urlandName1 = "";
		if(fileReason1!=null&&fileReason1.size()>0){
			for (int i = 0; i < fileReason1.size(); i++) {
				File file = fileReason1.get(i);
				if(file!=null){
					String fname = fileReason1FileName.get(i);
					FastDFSClient client = null;
					try {
						client = new FastDFSClient();
					} catch (Exception e) {
						e.printStackTrace();
					}
					String fileurl = client.uploadFile(file, fname);
//					String fileurl = uploadFileService.fileUpload(file, fname, "1", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					if(StringUtils.isNoneBlank(urlandName1)){
						urlandName1 +=";";
					}
					urlandName1 += fname +"#"+fileurl;
				}
			}
			if(StringUtils.isNotBlank(urlandName1)){
				urllist1.add(urlandName1);
			}
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, String[]> map = request.getParameterMap();
		if(!map.isEmpty()){
			for(Map.Entry<String, String[]> entry :map.entrySet()){
				String key = entry.getKey();
				if("id".equals(key)||"businessKey".equals(key)){
					continue;
				}
				String[] values = entry.getValue();
				if(values==null || values.length==0){
					continue;
				}
				StringBuffer sbf= new StringBuffer("");
				for (String str : values) {
					sbf.append(str).append(",");
				}
				sbf.deleteCharAt(sbf.length()-1);
				formParameter.getMultiValueMap().add(key, sbf.toString());
			}
		}
		formParameter.getMultiValueMap().put("fileReason", urllist);
		formParameter.getMultiValueMap().put("fileReason1", urllist1);
		String key = oaBpmConfFormService.saveDraft1(getAccount(), getDept(), bpmProcessName, tenantId, formParameter);//保存草稿
		if(StringUtils.isBlank(businessKey)){
			formParameter.setBusinessKey(key);
		}
		return formParameter;
	}
	
	
	/**
	 * 把数据先保存到记录表(OaKVRecord)中.
	 * @return
	 */
	public FormParameterVo saveRecode(String bpmProcessName){
		FormParameterVo formParameter=new FormParameterVo();
		formParameter.setBusinessKey(businessKey);
		formParameter.setBpmProcessId(id);
		String tenantId = tenantService.getTenantId();//租户id
		//文件上传
		List<String> urllist = new ArrayList<String>();
		String urlandName = "";
		if(fileReason!=null&&fileReason.size()>0){
			for (int i = 0; i < fileReason.size(); i++) {
				File file = fileReason.get(i);
				if(file!=null){
					String fname = fileReasonFileName.get(i);
					FastDFSClient client = null;
					try {
						client = new FastDFSClient();
					} catch (Exception e) {
						e.printStackTrace();
					}
					String fileurl = client.uploadFile(file, fname);
//					String fileurl = uploadFileService.fileUpload(file, fname, "1", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					if(StringUtils.isNoneBlank(urlandName)){
						urlandName +=";";
					}
					urlandName += fname +"#"+fileurl;
				}
			}
			if(StringUtils.isNotBlank(urlandName)){
				urllist.add(urlandName);
			}
		}
		//文件上传
		List<String> urllist1 = new ArrayList<String>();
		String urlandName1 = "";
		if(fileReason1!=null&&fileReason1.size()>0){
			for (int i = 0; i < fileReason1.size(); i++) {
				File file = fileReason1.get(i);
				if(file!=null){
					String fname = fileReason1FileName.get(i);
					FastDFSClient client = null;
					try {
						client = new FastDFSClient();
					} catch (Exception e) {
						e.printStackTrace();
					}
					String fileurl = client.uploadFile(file, fname);
//					String fileurl = uploadFileService.fileUpload(file, fname, "1", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					if(StringUtils.isNoneBlank(urlandName1)){
						urlandName1 +=";";
					}
					urlandName1 += fname +"#"+fileurl;
				}
			}
			if(StringUtils.isNotBlank(urlandName1)){
				urllist1.add(urlandName1);
			}
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, String[]> map = request.getParameterMap();
		if(!map.isEmpty()){
			for(Map.Entry<String, String[]> entry :map.entrySet()){
				String key = entry.getKey();
				if("id".equals(key)||"businessKey".equals(key)){
					continue;
				}
				String[] values = entry.getValue();
				if(values==null || values.length==0){
					continue;
				}
				StringBuffer sbf= new StringBuffer("");
				for (String str : values) {
					sbf.append(str).append(",");
				}
				sbf.deleteCharAt(sbf.length()-1);
				formParameter.getMultiValueMap().add(key, sbf.toString());
			}
		}
		formParameter.getMultiValueMap().put("fileReason", urllist);
		formParameter.getMultiValueMap().put("fileReason1", urllist1);
		String key = oaBpmConfFormService.saveDraft(getAccount(), getDept(), bpmProcessName, tenantId, formParameter);//保存草稿
		if(StringUtils.isBlank(businessKey)){
			formParameter.setBusinessKey(key);
		}
		return formParameter;
	}
	
	public String confirmStartProcess(){
		return "";
	}
	
//-----------------------------------------------未结流程------------------------------------------------	
	/**
	 * 查看流程实例详情
	 */
	@Action(value = "viewHistory", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/operation/viewHistory.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewHistory(){
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstance historicProcessInstance = historyService
                .createHistoricProcessInstanceQuery().processInstanceId(id).singleResult();
        List<HistoricActivityInstance> activitise = historyService.createHistoricActivityInstanceQuery().processInstanceId(id).list();
        if(account.equals(historicProcessInstance.getStartUserId())){
        	
        }
        List<HistoricTaskInstance> historicTasks = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(id).list();
        HttpServletRequest request = ServletActionContext.getRequest();
        request.setAttribute("historicTasks", historicTasks);
        List<HumanTaskVo> list = humanTaskService.findHumanTasksByProcessInstanceId(id);
        List<HumanTaskVo> voList =new ArrayList<>();
        for (HumanTaskVo humanTaskVo : list) {
			if(StringUtils.isNotBlank(humanTaskVo.getParentId())){
				continue;
			}
			String assignees = humanTaskVo.getAssigneeName();
			if(StringUtils.isBlank(assignees)){
				humanTaskVo.setAssigneeName("");
			}else{
				String[] split = assignees.split(",");
				if(split!=null&&split.length>2){
					humanTaskVo.setAssigneeName(split[0]+","+split[1]+"...");
				}
			}
			voList.add(humanTaskVo);
		}
        request.setAttribute("humanTasks", voList);
        request.setAttribute("historyActivities", activitise);
        List<NodeVo> nodeVos = oaOperationService.traceProcessInstance(id);
        request.setAttribute("nodeVos", nodeVos);
        if (historicProcessInstance.getEndTime() == null) {
            request.setAttribute("currentActivities", processEngine
                    .getRuntimeService()
                    .getActiveActivityIds(id));
        } else {
            request.setAttribute("currentActivities", Collections
                    .singletonList(historicProcessInstance.getEndActivityId()));
        }

        Graph graph = processEngine.getManagementService().executeCommand(
                new FindHistoryGraphCmd(id));
        request.setAttribute("graph", graph);
        request.setAttribute("historicProcessInstance", historicProcessInstance);
        request.setAttribute("processInstanceId", id);
        return "list";
	}
	
	/**
	 * 撤回-----未完结的撤回
	 */
	@Action(value="recall")
	public void recall(){
		try{
			String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();//获取当前登陆人
			List<HumanTaskVo> list = humanTaskService.findHumanTasksByProcessInstanceIdRecall(processInstanceId);//获取最新的两条记录
			String result = "";
			if(list!=null&&list.size()>=2){
				HumanTaskVo lastHumanTaskVo = list.get(0);//最新记录
				HumanTaskVo penultHumanTaskVo = list.get(1);//第二新的记录
				if(!"withdraw".equals(penultHumanTaskVo.getStatus())){
					//判断第二新的记录是不是当前登陆人操作的
					if("active".equals(lastHumanTaskVo.getStatus())){//判断最新的状态是否能撤回
						if(account.equals(penultHumanTaskVo.getLastModifier())){
							result = humanTaskService.withdrawTask(penultHumanTaskVo.getTaskId());
						}else{
							result = "下一结点已经通过,不能撤回!";
						}
					}else{
						result = "流程已结束,不能撤回!";
					}
				}else{
					result = "无法撤回!";
				}
			}else{
				result = "网络异常!";
			}
			WebUtils.webSendJSON(JSONUtils.toJson(result));
		}catch(Exception e){
			e.printStackTrace();
			WebUtils.webSendJSON(JSONUtils.toJson("网络异常"));
		}
	}
	/**
	 * 历史记录的撤回
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月2日 上午10:23:48 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月2日 上午10:23:48 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="recallHistory")
	public void recallHistory(){
		try{
			String result = "";
			HumanTaskVo humanTaskVo = humanTaskService.findLastHumanTaskByTaskId(processInstanceId);
			if(!"active".equals(humanTaskVo.getStatus())){
				if(humanTaskVo.getCompleteTime()!=null&&DateUtils.compareDate(completeTime,humanTaskVo.getCompleteTime())==0){
					List<HumanTaskVo> list = humanTaskService.findHumanTasksByProcessInstanceIdRecall(humanTaskVo.getProcessInstanceId());//获取最新的两条记录
					if(list!=null&&list.size()>=2){
						HumanTaskVo penultHumanTaskVo = list.get(1);//第二新的记录
						if(!"withdraw".equals(penultHumanTaskVo.getStatus())){
							result = humanTaskService.withdrawTask(processInstanceId);
						}else{
							result = "上个节点已经开始处理，不能再撤回!";
						}
					}else{
						result = "网络异常!";
					}
				}else{
					result = "下个节点已通过，不能再撤回!";
				}
			}else{
				result = "该节点已撤回，不能再撤回!";
			}
			WebUtils.webSendJSON(JSONUtils.toJson(result));//用processInstanceId来接收taskId
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private static void ListSort(List<HumanTaskVo> list) {  
        Collections.sort(list, new Comparator<HumanTaskVo>() {  
            @Override  
            public int compare(HumanTaskVo o1, HumanTaskVo o2) {  
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                try {
                	Date dt1 = null;
                	if(o1.getCompleteTime()==null){
                    	dt1 = format.parse("00-00-00 00:00:00");
                    }else{
                    	dt1 = format.parse(o1.getCompleteTime().toString());
                    }
                    Date dt2 = null;
                    if(o2.getCompleteTime()==null){
                    	dt2 = format.parse("00-00-00 00:00:00");
                    }else{
                    	dt2 = format.parse(o2.getCompleteTime().toString());
                    }
                    if (dt1.getTime() < dt2.getTime()) {  
                        return 1;  
                    } else if (dt1.getTime() > dt2.getTime()) {  
                        return -1;  
                    } else {  
                        return 0;  
                    }  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
                return 0;  
            }  
        });  
    } 
	/**
	 * 流程跟踪
	 */
	@Action(value="graphHistoryProcessInstance")
	public void graphHistoryProcessInstance()  {
        try {
			Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(id);

			InputStream is = processEngine.getManagementService().executeCommand(
			        cmd);
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("image/png");

			int len = 0;
			byte[] b = new byte[1024];

			while ((len = is.read(b, 0, 1024)) != -1) {
			    response.getOutputStream().write(b, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * 终止运行中的流程实例
	 */
	@Action(value="endProcessInstance")
	public void endProcessInstance(){
		Map<String,Object> map= new HashMap<>();
		try {
			oaOperationService.endProcessInstance(id,getAccount());
			map.put("resMsg", "success");
			map.put("resCode", "操作成功！");
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "操作失败！");
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 催办
	 */
	@Action(value="remind")
	public void remind(){
		
	}
	
	/**
	 * 跳过
	 */
	public void skip(){
		
	}
	
	/**
     * 登录用户
     * @return
     */
    private String getAccount(){
    	String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
    	return account;
    }
    
    /**
     * 登录科室
     * @return
     */
    private String getDept(){
    	SysDepartment department = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//当前科室;
		if(department != null){
			return department.getDeptCode();
		}else{
			return null;
		}
    }
}
