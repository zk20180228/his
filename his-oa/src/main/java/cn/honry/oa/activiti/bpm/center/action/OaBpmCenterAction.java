package cn.honry.oa.activiti.bpm.center.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.apache.commons.io.IOUtils;
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

import cn.honry.base.bean.model.OaBpmCategory;
import cn.honry.base.bean.model.OaBpmConfBase;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.oa.activiti.bpm.base.service.OaBpmConfBaseService;
import cn.honry.oa.activiti.bpm.cmd.ProcessDefinitionDiagramCmd;
import cn.honry.oa.activiti.bpm.process.service.OaBpmProcessService;
import cn.honry.oa.activiti.queryFlow.service.QueryFlowService;
import cn.honry.oa.activiti.queryFlow.vo.OaTaskInfoVAct;
import cn.honry.oa.activiti.tenant.service.TenantService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 流程中心Action
 * @author luyanshou
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/activiti/center")
@SuppressWarnings({ "all" })
public class OaBpmCenterAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier(value = "queryFlowService")
	private QueryFlowService queryFlowService;
	public void setQueryFlowService(QueryFlowService queryFlowService) {
		this.queryFlowService = queryFlowService;
	}
	
	@Autowired
	@Qualifier(value = "oaBpmProcessService")
	private OaBpmProcessService oaBpmProcessService;

	public void setOaBpmProcessService(OaBpmProcessService oaBpmProcessService) {
		this.oaBpmProcessService = oaBpmProcessService;
	}
	@Autowired
	@Qualifier(value = "oaBpmConfBaseService")
	private OaBpmConfBaseService oaBpmConfBaseService;
	
	public void setOaBpmConfBaseService(OaBpmConfBaseService oaBpmConfBaseService) {
		this.oaBpmConfBaseService = oaBpmConfBaseService;
	}
	
	@Autowired
	@Qualifier(value = "tenantService")
	private TenantService tenantService;
	
	public void setTenantService(TenantService tenantService) {
		this.tenantService = tenantService;
	}
	
	@Resource
	private ProcessEngine processEngine;//工作流引擎
	
	private String id;
	
	private int page;//页码
	private int rows;//每页记录数
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
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


	//===========================发起流程================================================
	/**
	 * 流程信息列表
	 * @return
	 */
	@Action(value = "processListView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/center/processListView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String processListView(){
		String tenantId = tenantService.getTenantId();//租户id
		List<OaBpmCategory> list = oaBpmProcessService.getCategoryList();
		ServletActionContext.getRequest().setAttribute("bpmCategories", list);
		return "list";
	}
	
	/**
	 * 查看流程图
	 */
	@Action(value="graphProcessDefinition")
	public void graphProcessDefinition(){
		OaBpmProcess bpmProcess = oaBpmProcessService.get(id);
		OaBpmConfBase bpmConfBase = oaBpmConfBaseService.get(bpmProcess.getConfBaseCode());
		String processDefinitionId = bpmConfBase.getProcessDefinitionId();//流程定义id
		InputStream inputStream = processEngine.getManagementService().executeCommand(
				new ProcessDefinitionDiagramCmd(processDefinitionId));
		HttpServletResponse response = ServletActionContext.getResponse();//
		response.setContentType("image/png");
		try {
			IOUtils.copy(inputStream, response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//==================================未结流程==================================
	
	/**
	 * 当前登录员工发起的未结流程(即运行中的流程)列表
	 * 
	 * @return
	 */
	@Action(value = "listRunningProcessInstances", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/center/listRunningProcessInstances.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listRunningProcessInstances(){
		return "list";
	}
	/**
	 * 当前登录员工发起的未结流程(即运行中的流程)列表
	 * 
	 * @return
	 */
	@Action(value = "listRunningProcessInstancesPage")
	public void listRunningProcessInstancesPage(){
		String tenantId = tenantService.getTenantId();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		Map<String, Object> map = getRunningProcessInstances(tenantId, account);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	private Map<String,Object> getRunningProcessInstances(String tenantId,String account){
		Map<String,Object> map = new HashMap<>();
		HistoryService historyService = processEngine.getHistoryService();
		HistoricProcessInstanceQuery query = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceTenantId(tenantId).startedBy(account)
                .unfinished();
		long count = query.count();
		List<HistoricProcessInstance> list = query.listPage((page-1)*rows,rows);
		for (int i = 0; i < list.size(); i++) {
			
		}
		map.put("rows", list);
		map.put("total", count);
		return map;
	}
	/**
	 * 当前登录员工发起的已结流程(即运行中的流程)列表
	 * 
	 * @return
	 */
	@Action(value = "listfinishProcessInstancesPage")
	public void listfinishProcessInstancesPage(){
		String tenantId = tenantService.getTenantId();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		Map<String, Object> map = gefinishProcessInstances(tenantId, account);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	private Map<String,Object> gefinishProcessInstances(String tenantId,String account){
		Map<String,Object> map = new HashMap<>();
		HistoryService historyService = processEngine.getHistoryService();
		HistoricProcessInstanceQuery query = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceTenantId(tenantId).startedBy(account)
                .finished();
		long count = query.count();
		
		List<HistoricProcessInstance> list = query.listPage((page-1)*rows,rows);
		List<OaTaskInfoVAct> acts = new ArrayList<OaTaskInfoVAct>();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getEndTime()!=null){
				OaTaskInfoVAct infoVAct  = new OaTaskInfoVAct();
				infoVAct.setBusinessKey(list.get(i).getBusinessKey());
				infoVAct.setActstarttime(list.get(i).getStartTime());
				infoVAct.setProcessInstanceId(list.get(i).getSuperProcessInstanceId());
				infoVAct.setStatus("1");
				acts.add(infoVAct);
			}
		}
		map.put("rows", acts);
		map.put("total", count);
		return map;
	}
}
