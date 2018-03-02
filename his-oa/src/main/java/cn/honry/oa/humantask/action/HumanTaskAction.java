package cn.honry.oa.humantask.action;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import org.springframework.util.MultiValueMap;

import cn.honry.base.bean.model.EmployeeExtend;
import cn.honry.base.bean.model.OaBpmConfForm;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaFormInfo;
import cn.honry.base.bean.model.OaKVProp;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.fastdfs.FastDFSClient;
import cn.honry.inner.system.upload.service.UploadFileService;
import cn.honry.inner.system.upload.vo.FileResVo;
import cn.honry.oa.activiti.bpm.cmd.FindNextActivitiesCmd;
import cn.honry.oa.activiti.bpm.form.vo.ButtonVo;
import cn.honry.oa.activiti.bpm.form.vo.ButtonVoHelper;
import cn.honry.oa.activiti.bpm.form.vo.ConfFormVo;
import cn.honry.oa.activiti.bpm.form.vo.FormParameterVo;
import cn.honry.oa.activiti.bpm.form.vo.Xform;
import cn.honry.oa.activiti.bpm.form.vo.XformBuilder;
import cn.honry.oa.activiti.task.service.TaskDefaultService;
import cn.honry.oa.activiti.tenant.service.TenantService;
import cn.honry.oa.formInfo.vo.FielVo;
import cn.honry.oa.formInfo.vo.InfoVo;
import cn.honry.oa.formInfo.vo.SectVo;
import cn.honry.oa.humantask.service.HumanTaskService;
import cn.honry.oa.humantask.vo.ActivityVo;
import cn.honry.oa.humantask.vo.HumanTaskConstants;
import cn.honry.oa.humantask.vo.HumanTaskVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 个人任务Action
 * @author luyanshou
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/activiti/humanTask")
@SuppressWarnings({ "all" })
public class HumanTaskAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(HumanTaskAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Resource
	private ProcessEngine processEngine;//工作流引擎
	
	@Autowired
	@Qualifier(value = "tenantService")
	private TenantService tenantService;

	//任务的创建者
	private String humtaskCreateUser;

	public String getHumtaskCreateUser() {
		return humtaskCreateUser;
	}

	public void setHumtaskCreateUser(String humtaskCreateUser) {
		this.humtaskCreateUser = humtaskCreateUser;
	}

	public void setTenantService(TenantService tenantService) {
		this.tenantService = tenantService;
	}
	@Autowired
	@Qualifier(value = "uploadFileService")
	private UploadFileService uploadFileService;
	
	public void setUploadFileService(UploadFileService uploadFileService) {
		this.uploadFileService = uploadFileService;
	}
	@Autowired
	@Qualifier(value = "taskDefaultService")
	private TaskDefaultService taskDefaultService;

	public void setTaskDefaultService(TaskDefaultService taskDefaultService) {
		this.taskDefaultService = taskDefaultService;
	}
	
	@Autowired
	@Qualifier(value = "humanTaskService")
	private HumanTaskService humanTaskService;
	
	public void setHumanTaskService(HumanTaskService humanTaskService) {
		this.humanTaskService = humanTaskService;
	}

	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}

	private int page;//页码
	private int rows;//每页记录数
	private String businessKey;
	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	private String humanTaskId;//任务id
	
	private String processDefinitionId;//流程定义ID
	
	private String activityId;//活动节点ID
	
	private String nextActiviti;//下一个活动节点id
	
	private String processInstanceId;//流程实例id
	
	private String zkhonryState;//驳回状态(0-正常;1-驳回上一节点;其他值代表驳回到某一节点)
	
	private ButtonVoHelper voHelper=new ButtonVoHelper();
	
	private String nextAssignee;//下一节点任务负责人
	
	private String rollbackActivityId;//逐级驳回时选择的回退节点id
	
	/**文件名附件0*/
	private List<File> fileReason;
	private List<String> fileReasonFileName;
	/**文件名,附件1*/
	private List<File> fileReason1;
	private List<String> fileReason1FileName;
	
	private ButtonVoHelper buttonVoHelper= new ButtonVoHelper();
	private String userparaMT;//姓名 科室 职务 
	
	public String getUserparaMT() {
		return userparaMT;
	}

	public void setUserparaMT(String userparaMT) {
		this.userparaMT = userparaMT;
	}
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public String getHumanTaskId() {
		return humanTaskId;
	}

	public void setHumanTaskId(String humanTaskId) {
		this.humanTaskId = humanTaskId;
	}
	
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getZkhonryState() {
		return zkhonryState;
	}

	public void setZkhonryState(String zkhonryState) {
		this.zkhonryState = zkhonryState;
	}

	public String getNextAssignee() {
		return nextAssignee;
	}

	public void setNextAssignee(String nextAssignee) {
		this.nextAssignee = nextAssignee;
	}

	public String getRollbackActivityId() {
		return rollbackActivityId;
	}

	public void setRollbackActivityId(String rollbackActivityId) {
		this.rollbackActivityId = rollbackActivityId;
	}

	public String getNextActiviti() {
		return nextActiviti;
	}

	public void setNextActiviti(String nextActiviti) {
		this.nextActiviti = nextActiviti;
	}

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

	/**
	 * 待办任务
	 * @return
	 */
	@Action(value = "taskInfoView", results = { @Result(
			name = "list", location = "/WEB-INF/pages/oa/activiti/humanTask/taskInfoView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String taskInfoView(){
		return "list";
	}
	
	/**
	 * 待办任务
	 * @return
	 */
	@Action(value = "taskInfoViewPage")
	public void taskInfoViewPage(){
		Map<String,Object> map = new HashMap<>();
		try{
			String tenantId = tenantService.getTenantId();//租户id
			String account = getAccount();//登录账户
			String hql="from OaTaskInfo where assignee like ? and tenantId=? and status='active' and stop_flg=0 and del_flg=0 order by createTime";
			List<OaTaskInfo> list = taskDefaultService.getListByPage(hql,(page-1)*rows,rows,new OaTaskInfo(), "%"+account+"%",tenantId);
			String hql1="select count(id) from OaTaskInfo where assignee like ? and tenantId=? and status='active' and stop_flg=0 and del_flg=0";
			int count = taskDefaultService.getCount(hql1,"%"+account+"%",tenantId);
			map.put("total", count);
			map.put("rows", list);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("YWSP_DBYW", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YWSP_DBYW", "业务审批_待办业务", "3", "1"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 显示任务表单
	 * @return
	 */
	@Action(value = "viewTaskForm", results = { @Result(
			name = "list", location = "/WEB-INF/pages/oa/activiti/humanTask/viewTaskForm.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewTaskForm(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String userName = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		String loginDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName();
		String account=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		EmployeeExtend empExtend = employeeInInterService.getEmpExtend(account);
		String dutiesName = "";
		if(empExtend!=null){
			dutiesName = empExtend.getDutiesName();
		}
		userparaMT = "";
		if(StringUtils.isNoneBlank(userName)){
			userparaMT += userName;
		}
		if(StringUtils.isNoneBlank(loginDept)){
			userparaMT += "-"+loginDept;
		}
		if(StringUtils.isNoneBlank(dutiesName)){
			userparaMT += "-"+dutiesName;
		}
		try{
			if(StringUtils.isBlank(humanTaskId)){
				
				return "list";   
			}
			HumanTaskVo humanTaskVo = humanTaskService.findHumanTask(humanTaskId);
			// 处理转发抄送任务，设置为已读
			if (HumanTaskConstants.CATALOG_COPY.equals(humanTaskVo.getCatalog())){
				humanTaskVo.setStatus("complete");
				humanTaskVo.setAction("read");
				humanTaskService.saveHumanTask(humanTaskVo);
			}
			ConfFormVo formVo=new ConfFormVo();
			
			String taskId = humanTaskVo.getTaskId();
			if(StringUtils.isNotBlank(taskId)){
				formVo = humanTaskService.getFormVo(humanTaskVo);
				
				if(formVo.isRedirect()){//外部表单,应该重定向(暂未实现)
					String url = formVo.getUrl();
				}
				//按钮
				List<ButtonVo> buttons =new ArrayList<>();
				//buttons.add(buttonVoHelper.findButton("saveDraft"));//保存草稿
				List<String> list = formVo.getButtons();
				if(list!=null && list.size()>0){
					for (String str: list) {
						buttons.add(buttonVoHelper.findButton(str));
					}
				}
//				buttons.add(buttonVoHelper.findButton("completeTask"));//完成任务
				request.setAttribute("buttons", buttons);
				request.setAttribute("formVo", formVo);
				request.setAttribute("humanTaskId", humanTaskId);
				request.setAttribute("humanTask", humanTaskVo);
			}
			if(StringUtils.isNotBlank(humanTaskVo.getParentId())){
				HumanTaskVo task = humanTaskService.findHumanTask(humanTaskVo.getParentId());
				request.setAttribute("parentHumanTask", task);
			}
			
			//表单和数据
			String processInstanceId = humanTaskVo.getProcessInstanceId();
			String businessKey = humanTaskVo.getBusinessKey();
			OaKVRecord record = humanTaskService.getRecord(businessKey);
			if(record!=null){
				Map<String,String> fMap = new HashMap<String,String>();
				try {
					String properties = formVo.getProperties();
					JSONObject jsonobject = JSONObject.fromObject(properties);
					Iterator<String> sIterator = jsonobject.keys();
					while(sIterator.hasNext()){  
					    String key = sIterator.next();  
					    JSONObject value = jsonobject.getJSONObject(key);
					    if("false".equals(value.getString("readonly"))){
					    	fMap.put(key, key);
					    }
					}  
				} catch (Exception e) {
					logger.error(e);
				}
				Map<String, OaKVProp> props = record.getProps();
				Map<String,String> map =new HashMap<>(); 
				if(props!=null){
					for(OaKVProp  prop:props.values()){
						String code = prop.getCode();
						String value = prop.getValue();
						if("fileReason".equals(code)){
							List<FileResVo> list = uploadFileService.getFileReason(prop.getValue(),request);
							map.put(code, JSONUtils.toJson(list));
						}else if("fileReason1".equals(code)){
							List<FileResVo> list = uploadFileService.getFileReason(prop.getValue(),request);
							map.put(code, JSONUtils.toJson(list));
						}else{
							map.put(code, value);
						}
					}
				}
				if(fMap.size()>0){
					for(Map.Entry<String, String> entry : fMap.entrySet()){
						if("releaseUser".equals(entry.getKey())){
							map.put(entry.getKey(), userName);
						}
						if("releaseDate".equals(entry.getKey())){//办理时间
							map.put(entry.getKey(), DateUtils.formatDateY_M_D(new Date()));
						}
					}
				}

				//特定条件下回显的数据map2
				Map map2 = new HashMap();
				if(empExtend!=null){
					String eName=empExtend.getEmployeeName();
					map2.put("releaseUser",eName);
					map2.put("releaseDept",empExtend.getDepartment());
					map2.put("audiUser ",eName);//核稿人
					map2.put("printUser",eName);//打印人
					map2.put("inspeUser",eName);//校对人
				}

				String timeNow=DateUtils.formatDateY_M_D(new Date());
				map2.put("releaseDate", timeNow);
				map2.put("approvalDate", timeNow);


				String json2 = JSONUtils.toJson(map2);
				request.setAttribute("json2", json2);

				if(map.size()>0){
					String json = JSONUtils.toJson(map);
					request.setAttribute("json", json);
				}
			}
			
			Xform xform = new XformBuilder().setTaskDefaultService(taskDefaultService)
					.setContent(formVo.getContent()).setRecord(record).build();
			request.setAttribute("xform", xform);
			//审批记录
			List<HumanTaskVo> list = humanTaskService.findHumanTasksByProcessInstanceId(processInstanceId);
			for (HumanTaskVo humanTaskVo2 : list) {
				String assignees = humanTaskVo2.getAssigneeName();
				if(StringUtils.isBlank(assignees)){
					humanTaskVo2.setAssigneeName("");
				}else{
					String[] split = assignees.split(",");
					if(split!=null&&split.length>2){
						humanTaskVo2.setAssigneeName(split[0]+","+split[1]+"...");
					}
				}
			}
			request.setAttribute("logHumanTask", list);
			//沟通记录
			List<HumanTaskVo> children = humanTaskService.getHumanTask(humanTaskId);
			request.setAttribute("children", children);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("YWSP_DBYW", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YWSP_DBYW", "业务审批_待办业务", "3", "1"), e);
		}
		return "list";
	}
	
	/**
	 * 显示任务表单
	 * @return
	 */
	@Action(value = "viewTaskFormYj", results = { @Result(
			name = "list", location = "/WEB-INF/pages/oa/activiti/humanTask/viewTaskFormYj.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewTaskFormYj(){
		HttpServletRequest request = ServletActionContext.getRequest();
		try{
			if(StringUtils.isBlank(humanTaskId)){
				
				return "list";   
			}
			HumanTaskVo humanTaskVo = humanTaskService.findHumanTask(humanTaskId);
			// 处理转发抄送任务，设置为已读
			if (HumanTaskConstants.CATALOG_COPY.equals(humanTaskVo.getCatalog())){
				humanTaskVo.setStatus("complete");
				humanTaskVo.setAction("read");
				humanTaskService.saveHumanTask(humanTaskVo);
			}
			ConfFormVo formVo=new ConfFormVo();
			
			String taskId = humanTaskVo.getTaskId();
			if(StringUtils.isNotBlank(taskId)){
				formVo = humanTaskService.getFormVo(humanTaskVo);
				
				if(formVo.isRedirect()){//外部表单,应该重定向(暂未实现)
					String url = formVo.getUrl();
				}
				//按钮
				List<ButtonVo> buttons =new ArrayList<>();
				//buttons.add(buttonVoHelper.findButton("saveDraft"));//保存草稿
				buttons.add(buttonVoHelper.findButton("completeTask"));//完成任务
				List<String> list = formVo.getButtons();
				if(list!=null && list.size()>0){
					for (String str: list) {
						buttons.add(buttonVoHelper.findButton(str));
					}
				}
				request.setAttribute("buttons", buttons);
				request.setAttribute("formVo", formVo);
				request.setAttribute("humanTaskId", humanTaskId);
				request.setAttribute("humanTask", humanTaskVo);
			}
			if(StringUtils.isNotBlank(humanTaskVo.getParentId())){
				HumanTaskVo task = humanTaskService.findHumanTask(humanTaskVo.getParentId());
				request.setAttribute("parentHumanTask", task);
			}
			
			//表单和数据
			String processInstanceId = humanTaskVo.getProcessInstanceId();
			String businessKey = humanTaskVo.getBusinessKey();
			OaKVRecord record = humanTaskService.getRecord(businessKey);
			if(record!=null){
				Map<String, OaKVProp> props = record.getProps();
				if(props!=null){
					Map<String,String> map =new HashMap<>(); 
					for(OaKVProp  prop:props.values()){
						String code = prop.getCode();
						String value = prop.getValue();
						if("fileReason".equals(code)){
							List<FileResVo> list = uploadFileService.getFileReason(prop.getValue(),request);
							map.put(code, JSONUtils.toJson(list));
						}else if("fileReason1".equals(code)){
							List<FileResVo> list = uploadFileService.getFileReason(prop.getValue(),request);
							map.put(code, JSONUtils.toJson(list));
						}else{
							map.put(code, value);
						}
					}
					String json = JSONUtils.toJson(map);
					request.setAttribute("json", json);
				}
			}
			
			Xform xform = new XformBuilder().setTaskDefaultService(taskDefaultService)
					.setContent(formVo.getContent()).setRecord(record).build();
			request.setAttribute("xform", xform);
			//审批记录
			List<HumanTaskVo> list = humanTaskService.findHumanTasksByProcessInstanceId(processInstanceId);
			for (HumanTaskVo humanTaskVo2 : list) {
				String assignees = humanTaskVo2.getAssigneeName();
				if(StringUtils.isBlank(assignees)){
					humanTaskVo2.setAssigneeName("");
				}else{
					String[] split = assignees.split(",");
					if(split!=null&&split.length>2){
						humanTaskVo2.setAssigneeName(split[0]+","+split[1]+"...");
					}
				}
			}
			request.setAttribute("logHumanTask", list);
			//沟通记录
			List<HumanTaskVo> children = humanTaskService.getHumanTask(humanTaskId);
			request.setAttribute("children", children);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("YWSP_DBYW", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YWSP_DBYW", "业务审批_待办业务", "3", "1"), e);
		}
		return "list";
	}
	
	/**

	 * 显示任务表单(草稿箱编辑)
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Action(value = "viewVKForm", results = { @Result(
			name = "list", location = "/WEB-INF/pages/oa/activiti/operation/viewStartForm.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewVKForm(){
		HttpServletRequest request = ServletActionContext.getRequest();
		try{
			if(StringUtils.isBlank(id)){
				return "list"; 
			}
			FormParameterVo formParameter = new FormParameterVo();
			//查询表单内容
			OaKVRecord kvRecord = humanTaskService.queryOaKVRecordById(id);
			OaBpmProcess bpmProcess = humanTaskService.queryOaBpmProcessById(kvRecord.getCategory());
			
			OaFormInfo formInfo = new OaFormInfo();
			try {
				formInfo = humanTaskService.queryOaFormInfoById(bpmProcess.getFormCode());
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//查询表单数据
			List<OaKVProp> list = humanTaskService.queryOaKVPropById(kvRecord.getId());
			Map<String, Object> map =  new HashMap<String, Object>();
			for (int i = 0; i < list.size(); i++) {
				if("fileReason".equals(list.get(i).getCode())){
					List<FileResVo> fileResVos = uploadFileService.getFileReason(list.get(i).getValue(),request);
					map.put(list.get(i).getCode(), JSONUtils.toJson(fileResVos));
				}else if("fileReason1".equals(list.get(i).getCode())){
					List<FileResVo> fileResVos = uploadFileService.getFileReason(list.get(i).getValue(),request);
					map.put(list.get(i).getCode(), JSONUtils.toJson(fileResVos));
				}else{
					map.put(list.get(i).getCode(), list.get(i).getValue());
				}
			}
			
			//表单属性
			List<OaBpmConfForm> bpmConfForms =  humanTaskService.queryOaBpmConfFormBybaseId(bpmProcess.getConfBaseCode());
			ConfFormVo formVo=new ConfFormVo();
			formVo.setContent(formInfo.getFormInfoStr());
			formVo.setProperties(bpmConfForms.get(0).getProperties());
			String json = JSONUtils.toJson(map);
			
			
			request.setAttribute("initMap", new HashMap<String,String>());//数据
			request.setAttribute("json", json);//数据
			
			request.setAttribute("formVo", formVo);//数据
			
			List<ButtonVo> list1 = new ArrayList<ButtonVo>();
			list1.add(voHelper.findButton("saveDraft"));
			list1.add(voHelper.findButton("confirmStartProcess"));
			request.setAttribute("buttons", list1);//页面显示的按钮列表
			request.setAttribute("bpmProcessId", humanTaskId);
			request.setAttribute("businessKey", kvRecord.getBusinessKey());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("YWSQ_CGX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YWSQ_CGX", "业务申请_草稿箱", "3", "1"), e);
		}
		return "list";
	}
	
	/**
	 * 保存草稿
	 */
	@Action(value="saveDraft")
	public void saveDraft(){
		Map<String,String> map = new HashMap<>();
		try {
			String tenantId = tenantService.getTenantId();//租户id
			String account = getAccount();//登录用户
			FormParameterVo formParameter = getFormParameter();
			formParameter.setHumanTaskId(humanTaskId);
			//文件上传
			List<String> urllist = new ArrayList<String>();
			String urlandName = "";
			if(fileReason!=null&&fileReason.size()>0){
				for (int i = 0; i < fileReason.size(); i++) {
					File file = fileReason.get(i);
					if(file!=null){
						String fname = fileReasonFileName.get(i);
						FastDFSClient client = new FastDFSClient();
						String fileurl = client.uploadFile(file, fname);
//						String fileurl = uploadFileService.fileUpload(file, fname, "1", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
						if(StringUtils.isNoneBlank(urlandName)){
							urlandName +=";";
						}
						urlandName += fname +"#"+fileurl;
					}
				}
				urllist.add(urlandName);
			}
			MultiValueMap<String, String> multiValueMap = formParameter.getMultiValueMap();
			multiValueMap.put("fileReason", urllist);
			//文件上传1
			List<String> urllist1 = new ArrayList<String>();
			String urlandName1 = "";
			if(fileReason1!=null&&fileReason1.size()>0){
				for (int i = 0; i < fileReason1.size(); i++) {
					File file = fileReason1.get(i);
					if(file!=null){
						String fname = fileReason1FileName.get(i);
						FastDFSClient client = new FastDFSClient();
						String fileurl = client.uploadFile(file, fname);
//						String fileurl = uploadFileService.fileUpload(file, fname, "1", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
						if(StringUtils.isNoneBlank(urlandName1)){
							urlandName1 +=";";
						}
						urlandName1 += fname +"#"+fileurl;
					}
				}
				urllist1.add(urlandName1);
			}
			multiValueMap.put("fileReason1", urllist1);
			formParameter.setMultiValueMap(multiValueMap);
			
			humanTaskService.saveDraft(account, getDept(), tenantId, formParameter);//保存草稿
			map.put("resMsg", "success");
			map.put("resCode", "保存成功！");
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "保存失败！");
			e.printStackTrace();
			logger.error("YWSQ_XJYW", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YWSQ_XJYW", "业务申请_新建业务", "3", "1"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 完成任务/回退
	 */
	@Action(value="completeTask")
	public void completeTask(){
		Map<String,String> map = new HashMap<>();
		try {
		String tenantId = tenantService.getTenantId();//租户id
		String account = getAccount();//登录用户
		FormParameterVo formParameter = getFormParameter();
		formParameter.setHumanTaskId(humanTaskId);
//		String key = humanTaskService.saveDraft(account, getDept(), tenantId, formParameter);//保存草稿
		HumanTaskVo humanTaskVo = humanTaskService.findHumanTask(humanTaskId);
//		ConfFormVo formVo = humanTaskService.getFormVo(humanTaskVo);
//		OaKVRecord record = humanTaskService.getRecord(key);
//		Xform xform = new XformBuilder().setTaskDefaultService(taskDefaultService)
//	   			 .setContent(formVo.getContent()).setRecord(record).build();
			Map<String, Object> taskParameters = new HashMap<>();//xform.getMapData();//验证规则
			String user = humanTaskVo.getCreateUser();
			EmployeeExtend empExtend = employeeInInterService.getEmpExtend(user);
			taskParameters.put("zkhonryState", zkhonryState);
			if(empExtend!=null){
				taskParameters.put("employeeType", empExtend.getEmployeeType());
				taskParameters.put("dutiesLevel", empExtend.getDutiesLevel());
				taskParameters.put("deptAreaCode", empExtend.getAreaCode());
				taskParameters.put("departmentCode", empExtend.getDepartmentCode());
			}else{
				taskParameters.put("employeeType", "HLRY");
				taskParameters.put("dutiesLevel", "5");
				taskParameters.put("divisionCode", "5");
			}
//			String processInstanceId = humanTaskVo.getProcessInstanceId();
//			//文件上传
//			List<String> urllist = new ArrayList<String>();
//			List<String> filename = new ArrayList<String>();
//			if(fileReason!=null&&fileReason.size()>0 && fileReasonFileName!=null){
//				for (int i = 0; i < fileReason.size(); i++) {
//					File file = fileReason.get(i);
//					String fname = fileReasonFileName.get(i);
//					String fileurl = uploadFileService.fileUpload(file, fname, "1",getAccount());
//					urllist.add(fileurl);
//					filename.add(fname);
//				}
//			}
//			MultiValueMap<String, String> multiValueMap = formParameter.getMultiValueMap();
//			multiValueMap.put("fileReason", urllist);
//			multiValueMap.put("fileReasonFileName", filename);
//			//文件上传
//			List<String> urllist1 = new ArrayList<String>();
//			List<String> filename1= new ArrayList<String>();
//			if(fileReason1!=null&&fileReason1.size()>0 && fileReason1FileName!=null){
//				for (int i = 0; i < fileReason1.size(); i++) {
//					File file = fileReason1.get(i);
//					String fname = fileReason1FileName.get(i);
//					String fileurl = uploadFileService.fileUpload(file, fname, "1",getAccount());
//					urllist1.add(fileurl);
//					filename.add(fname);
//				}
//			}
//			multiValueMap.put("fileReason1", urllist1);
//			multiValueMap.put("fileReason1FileName", filename1);
			//文件上传
			List<String> urllist = new ArrayList<String>();
			String urlandName = "";
			if(fileReason!=null&&fileReason.size()>0){
				for (int i = 0; i < fileReason.size(); i++) {
					File file = fileReason.get(i);
					if(file!=null){
						String fname = fileReasonFileName.get(i);
						FastDFSClient client = new FastDFSClient();
						String fileurl = client.uploadFile(file, fname);						
//						String fileurl = uploadFileService.fileUpload(file, fname, "1", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
						if(StringUtils.isNoneBlank(urlandName)){
							urlandName +=";";
						}
						urlandName += fname +"#"+fileurl;
					}
				}
				urllist.add(urlandName);
			}
			MultiValueMap<String, String> multiValueMap = formParameter.getMultiValueMap();
			multiValueMap.put("fileReason", urllist);
			//文件上传1
			List<String> urllist1 = new ArrayList<String>();
			String urlandName1 = "";
			if(fileReason1!=null&&fileReason1.size()>0){
				for (int i = 0; i < fileReason1.size(); i++) {
					File file = fileReason1.get(i);
					if(file!=null){
						String fname = fileReason1FileName.get(i);
						FastDFSClient client = new FastDFSClient();
						String fileurl = client.uploadFile(file, fname);
//						String fileurl = uploadFileService.fileUpload(file, fname, "1", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
						if(StringUtils.isNoneBlank(urlandName1)){
							urlandName1 +=";";
						}
						urlandName1 += fname +"#"+fileurl;
					}
				}
				urllist1.add(urlandName1);
			}
			multiValueMap.put("fileReason1", urllist1);
			formParameter.setMultiValueMap(multiValueMap);
			if("0".equals(zkhonryState)){//正常提交的数据
				formParameter.setAssignee(nextAssignee);//指定负责人
				humanTaskService.complete(account, getDept(), tenantId, formParameter, taskParameters);
			}else if("1".equals(zkhonryState)) {//驳回的数据
//				formParameter.setAssignee("starter");
				humanTaskService.complete(account, getDept(), tenantId, formParameter, taskParameters);
			}else{
				taskParameters.put("rollbackActivityId", rollbackActivityId);
				humanTaskService.complete(account, getDept(), tenantId, formParameter, taskParameters);
			}
			map.put("resMsg", "success");
			map.put("resCode", "处理成功！");
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "处理失败！");
			e.printStackTrace();
			logger.error("YWSP_DBYW", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YWSP_DBYW", "业务审批_待办业务", "3", "1"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
	/**
	 * 下一个活动节点
	 */
	@Action(value="findNextActivities")
	public void findNextActivities(){
		List<PvmActivity> list = processEngine.getManagementService().executeCommand(new FindNextActivitiesCmd(
                processDefinitionId, activityId));
		Map<String, String> map = humanTaskService.getAllPrevious(processInstanceId, humanTaskId,activityId);
		List<ActivityVo> activityVos = covertActivityVo(list,map);
		String json = JSONUtils.toJson(activityVos);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 上一个活动节点
	 */
	@Action(value="findPreviousActivities")
	public void findPreviousActivities(){
//		List<PvmActivity> list = processEngine.getManagementService().executeCommand(new FindPreviousActivitiesCmd(
//                processDefinitionId, activityId));
//		
//		List<ActivityVo> activityVos = covertActivityVo(list);
		ActivityVo vo = new ActivityVo();
		String name = humanTaskService.findPreviousActivities(processInstanceId, humanTaskId);
		vo.setName(name);
		WebUtils.webSendJSON(JSONUtils.toJson(vo));
	}
	
	/**
	 * 任务负责人列表
	 */
	@Action(value="selectAssignee")
	public void selectAssignee(){
		if(nextActiviti==null){//下个节点id
			List<PvmActivity> list = processEngine.getManagementService().executeCommand(new FindNextActivitiesCmd(
	                processDefinitionId, activityId));
			Map<String, String> map = humanTaskService.getAllPrevious(processInstanceId, humanTaskId,activityId);
			List<ActivityVo> activityVos = covertActivityVo(list,map);
			nextActiviti=activityVos.get(0).getId();
		}
		List<EmployeeExtend> list = humanTaskService.selectAssignee(processDefinitionId, nextActiviti,humtaskCreateUser,
				processInstanceId,businessKey);
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 获取所有已完成节点(逐级驳回列表)
	 */
	@Action(value="findAllPreviousActivities")
	public void findAllPreviousActivities(){
		List<OaTaskInfo> list = humanTaskService.findAllPreviousActivities(processInstanceId, activityId);
		if(list==null){
			list= new ArrayList<>();
		}
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 撤销任务.
	 */
	@Action(value="withdrawTask")
	public void withdrawTask(){
		String string = humanTaskService.withdrawTask(humanTaskId);
		WebUtils.webSendString(string);
	}
	
	private FormParameterVo getFormParameter(){
		FormParameterVo formParameter=new FormParameterVo();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, String[]> map = request.getParameterMap();
		if(!map.isEmpty()){
			for(Map.Entry<String, String[]> entry :map.entrySet()){
				String key = entry.getKey();
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
		return formParameter;
	}
	
	/**
	 * 转换成VO对象
	 * @param PvmActivities
	 * @return
	 */
	private List<ActivityVo> covertActivityVo(List<PvmActivity> PvmActivities,Map<String,String> map){
		
		List<ActivityVo> list = new ArrayList<ActivityVo>();
		for (PvmActivity pvmActivity : PvmActivities) {
			String id = pvmActivity.getId();
			if(map.get(id)!=null){
				continue;
			}
			ActivityVo activityVo = new ActivityVo();
            activityVo.setId(id);
            activityVo.setName((String) pvmActivity.getProperty("name"));
            
            list.add(activityVo);
		}
		return list;
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
