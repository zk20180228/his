package cn.honry.oa.activiti.processInstance.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.commons.lang.StringUtils;
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

import cn.honry.oa.activiti.bpm.cmd.MigrateCmd;
import cn.honry.oa.activiti.tenant.service.TenantService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 流程实例Action
 * @author luyanshou
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/activiti/processInstance")
@SuppressWarnings({ "all" })
public class ProcessInstanceAction extends ActionSupport {

	
	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier(value = "tenantService")
	private TenantService tenantService;
	
	public void setTenantService(TenantService tenantService) {
		this.tenantService = tenantService;
	}

	
	@Resource
	private ProcessEngine processEngine;//工作流引擎
	

	private String id;
	private String type;
	
	private int page;//页码
	private int rows;//每页记录数
	private String name;
	
	private String processDefinitionId;//流程定义id
	
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

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}


	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 流程实例视图
	 * @return
	 */
	@Action(value = "instanceView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/processInstance/instanceView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String instanceView(){
		return "list";
	}
	
	/**
	 * 流程实例视图
	 * @return
	 */
	@Action(value = "instanceViewPage", results = { @Result(name = "list", 
			location = "/WEB-INF/s/oa/activiti/processInstance/instanceView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void instanceViewPage(){
		Map<String,Object> retMap =new HashMap<>();
		String tenantId=tenantService.getTenantId();
		ProcessInstanceQuery query = processEngine.getRuntimeService().createProcessInstanceQuery();//获取运行时服务
		if(StringUtils.isNotBlank(name)){
			query.processInstanceNameLike("%"+name+"%");
		}
		long count = query.processInstanceTenantId(tenantId).count();//获取流程实例总数
		List<ProcessInstance> list = query.processInstanceTenantId(tenantId).listPage((page-1)*rows,rows);//获取流程实例列表(分页)信息
		retMap.put("total", count);
		retMap.put("rows", list);
		String json = JSONUtils.toExposeJson(retMap, false, null, "id","name","processDefinitionId","activityId","suspensionState");
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 激活流程实例
	 */
	@Action(value="active")
	public void active(){
		Map<String,String> map =new HashMap<>();
		try {
			RuntimeService runtimeService = processEngine.getRuntimeService();//获取运行时服务
			runtimeService.activateProcessInstanceById(id);//激活流程实例
			map.put("resCode", "success");
			map.put("resMsg", "激活成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "激活失败！");
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 挂起流程实例
	 */
	@Action(value="suspend")
	public void suspend(){
		Map<String,String> map =new HashMap<>();
		try {
			RuntimeService runtimeService = processEngine.getRuntimeService();//获取运行时服务
			runtimeService.suspendProcessInstanceById(id);//挂起流程实例
			map.put("resCode", "success");
			map.put("resMsg", "挂起成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "挂起失败！");
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 删除流程实例
	 */
	@Action(value="del")
	public void del(){
		Map<String,String> map =new HashMap<>();
		try {
			processEngine.getRuntimeService().deleteProcessInstance(id, "delete");
			if(StringUtils.isNotBlank(type)&&"1".equals(type)){
				processEngine.getHistoryService().deleteHistoricProcessInstance(id);//删除历史
			}
	        map.put("resCode", "success");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "删除失败！");
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 跳转到流程实例迁移页面
	 */
	@Action(value = "migrateInputView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/processInstance/migrateInputView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String migrateInputView(){
		List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().list();//获取流程定义列表
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("id", id);
		request.setAttribute("result", list);
		return "list";
	}
	
	/**
	 * 流程实例迁移方法
	 */
	@Action(value="migrateSave")
	public void migrateSave(){
		Map<String,Object> map =new HashMap<>();
		try {
			processEngine.getManagementService().executeCommand(new MigrateCmd(id, processDefinitionId));//执行流程实例迁移命令
			map.put("resMsg", "success");
			map.put("resCode", "迁移成功！");
		} catch (IllegalStateException e) {
			map.put("resMsg", "error");
			map.put("resCode", "迁移失败,"+e.getMessage());
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
}
