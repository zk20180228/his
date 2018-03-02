package cn.honry.oa.activiti.processDefinitions.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;
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

import cn.honry.oa.activiti.bpm.cmd.ProcessDefinitionDiagramCmd;
import cn.honry.oa.activiti.bpm.cmd.SyncProcessCmd;
import cn.honry.oa.activiti.bpm.cmd.UpdateProcessCmd;
import cn.honry.oa.activiti.bpm.utils.IoUtils;
import cn.honry.oa.activiti.processDefinitions.service.ProcessDefinitionsService;
import cn.honry.oa.activiti.processDefinitions.vo.ProcessDefinitionsVO;
import cn.honry.oa.activiti.tenant.service.TenantService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 流程定义Action
 * @author luyanshou
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/activiti/processDefinitions")
@SuppressWarnings({ "all" })
public class ProcessDefinitionsAction extends ActionSupport {

	
	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier(value = "processDefinitionsService")
	private ProcessDefinitionsService processDefinitionsService;
	
	public void setProcessDefinitionsService(
			ProcessDefinitionsService processDefinitionsService) {
		this.processDefinitionsService = processDefinitionsService;
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
	
	private File file;//上传文件
	private String fileFileName;//上传文件名称
	
	private String processXml;//接收修改流程定义时的XML文件
	
	private Integer pageIndex;
	private Integer pageSize;
	private Integer page;
	private Integer rows;
	private String name;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public File getFile() {
		return file;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	
	public String getProcessXml() {
		return processXml;
	}

	public void setProcessXml(String processXml) {
		this.processXml = processXml;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 流程定义视图
	 * @return
	 */
	@Action(value = "definitionsView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/processDefinitions/definitionsView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String definitionsView(){
		return "list";
	} 
	
	/**
	 * 流程定义视图
	 * @return
	 */
	@Action(value = "definitionsViewPage")
	public void definitionsViewPage(){
		Map<String,Object> retMap =new HashMap<>();
//		ProcessDefinitionQuery query = processEngine.getRepositoryService().createProcessDefinitionQuery();//获取仓库服务对象
//		if(StringUtils.isNotBlank(name)){
//			query.processDefinitionNameLike("%"+name+"%");
//		}
//		long count = query.count();//获取流程定义总数
//		List<ProcessDefinition> list = query.listPage((page-1)*rows,rows);
		List<ProcessDefinitionsVO> list = processDefinitionsService.getProcessDefinitionsList(name, (page-1)*rows, page*rows);
		int count = processDefinitionsService.getProcessDefinitionsTotal(name);
		retMap.put("total", count);
		retMap.put("rows", list);
		String json = JSONUtils.toExposeJson(retMap, false, null, "id","","name","version","createTime");
		WebUtils.webSendJSON(json);
	} 
	
	/**
	 * 流程定义上传页面
	 * @return
	 */
	@Action(value = "uploadView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/processDefinitions/uploadView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String uploadView(){
		return "list";
	}
	
	/**
	 * 流程定义文件上传
	 */
	@Action(value="uploadProcess")
	public void uploadProcess(){
		Map<String,String> map =new HashMap<>();
		String tenantId = tenantService.getTenantId();//租户id
		try {
			InputStream inputStream = new FileInputStream(file);
			Deployment deployment = processEngine.getRepositoryService().createDeployment()
			.addInputStream(fileFileName, inputStream).tenantId(tenantId).deploy();
			
			List<ProcessDefinition> processDefinitions = processEngine
	                .getRepositoryService().createProcessDefinitionQuery()
	                .deploymentId(deployment.getId()).list();
			
			for (ProcessDefinition processDefinition : processDefinitions) {
	            processEngine.getManagementService().executeCommand(
	                    new SyncProcessCmd(processDefinition.getId()));
	        }
			
			map.put("resMsg", "success");
			map.put("resCode", "发布成功！");
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 激活
	 */
	@Action(value="active")
	public void active(){
		Map<String,String> map =new HashMap<>();
		RepositoryService repositoryService = processEngine.getRepositoryService();//获取仓库服务对象
		repositoryService.activateProcessDefinitionById(id, true, null);//激活(恢复)流程定义
		map.put("resMsg", "success");
		map.put("resCode", "激活成功！");
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 挂起
	 */
	@Action(value="suspend")
	public void suspend(){
		Map<String,String> map =new HashMap<>();
		RepositoryService repositoryService = processEngine.getRepositoryService();//获取仓库服务对象
		repositoryService.suspendProcessDefinitionById(id, true, null);//挂起(暂停)流程定义
		map.put("resMsg", "success");
		map.put("resCode", "挂起成功！");
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 查看流程图
	 */
	@Action(value="graphProcessDefinition")
	public void graphProcessDefinition(){
		InputStream inputStream = processEngine.getManagementService().executeCommand(new ProcessDefinitionDiagramCmd(id));
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("image/png");
		try {
			IOUtils.copy(inputStream, response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查看XML
	 */
	@Action(value="viewXml")
	public void viewXml(){
		RepositoryService repositoryService = processEngine.getRepositoryService();//获取仓库服务对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()//创建查询
                .processDefinitionId(id).singleResult();//根据id获取流程定义
        String resourceName = processDefinition.getResourceName();//获取资源名称
        InputStream inputStream = repositoryService.getResourceAsStream(
                processDefinition.getDeploymentId(), resourceName);//获取输入流
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/xml;charset=UTF-8");
        try {
			IOUtils.copy(inputStream, response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 跳转到'编辑流程图'页面
	 */
	@Action(value="editView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/processDefinitions/editView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editView(){
		ProcessDefinition processDefinition = processEngine.getRepositoryService().getProcessDefinition(id);//获取流程定义
		InputStream inputStream = processEngine.getRepositoryService()//获取输入流
		.getResourceAsStream(processDefinition.getDeploymentId(),
                processDefinition.getResourceName());
		try {
			String xml = IoUtils.readString(inputStream);
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("xml", xml);
			request.setAttribute("id", id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "list";
	}
	
	/**
	 * 更新流程定义
	 */
	@Action(value="updateProcess")
	public void updateProcess(){
		Map<String,String> map =new HashMap<>();
		try {
			byte[] bytes = processXml.getBytes("utf-8");
			UpdateProcessCmd updateProcessCmd = new UpdateProcessCmd(id, bytes);//获取更新流程定义命令
			processEngine.getManagementService().executeCommand(updateProcessCmd);//执行更新命令
			map.put("resMsg", "success");
			map.put("resCode", "更新成功！");
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "更新失败！");
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
}
