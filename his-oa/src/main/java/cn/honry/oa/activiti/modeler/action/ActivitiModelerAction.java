package cn.honry.oa.activiti.modeler.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
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

import cn.honry.oa.activiti.bpm.cmd.SyncProcessCmd;
import cn.honry.oa.activiti.bpm.utils.ExtendVo;
import cn.honry.oa.activiti.modeler.service.ModelerPageService;
import cn.honry.oa.activiti.modeler.vo.ModelerVO;
import cn.honry.oa.activiti.tenant.service.TenantService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 工作流模型Action
 * @author user
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/activiti/modeler")
@SuppressWarnings({ "all" })
public class ActivitiModelerAction extends ActionSupport {

	private Logger logger=Logger.getLogger(ActivitiModelerAction.class);
	
	private static final long serialVersionUID = 1L;
	
	@Resource
	private ProcessEngine processEngine;//工作流引擎
	
	@Autowired
	@Qualifier(value = "tenantService")
	private TenantService tenantService;
	@Autowired
	@Qualifier(value = "modelerPageService")
	private ModelerPageService modelerPageService;
	
	public void setTenantService(TenantService tenantService) {
		this.tenantService = tenantService;
	}

	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String name;//模型名称
	private String description;//模型描述
	private String json_xml;//json数据
	private String svg_xml;
	private Integer pageIndex;
	private Integer pageSize;
	private Integer page;
	private Integer rows;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJson_xml() {
		return json_xml;
	}

	public void setJson_xml(String json_xml) {
		this.json_xml = json_xml;
	}

	public String getSvg_xml() {
		return svg_xml;
	}

	public void setSvg_xml(String svg_xml) {
		this.svg_xml = svg_xml;
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

	/**
	 * 工作流模型视图
	 * @return
	 */
	@Action(value = "modelerView", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/activiti/modeler/modelerView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String modelerView(){
//		List<Model> list = processEngine.getRepositoryService().createModelQuery().list();//获取所有的模型对象
//		ServletActionContext.getRequest().setAttribute("model", list);
		return "list";
	}
	
	/**
	 * 工作流模型视图
	 * @return
	 */
	@Action(value = "modelerPage")
	public void modelerPage(){
		HashMap<String,Object> retMap = new HashMap<String, Object>();
//		List<Model> list = processEngine.getRepositoryService().createModelQuery().list();//获取所有的模型对象
//		ModelQuery query = processEngine.getRepositoryService().createModelQuery();
//		if(StringUtils.isNotBlank(name)){
//			query.modelNameLike("%"+name+"%");
//		}
//		long total =  query.count();
//		query.orderByLastUpdateTime().desc();
//		List<Model> list = query.listPage((page-1)*rows,rows); 
		List<ModelerVO> list = modelerPageService.getModeler(name, page, rows);
		int total = modelerPageService.getModelerTotal(name);
		retMap.put("total", total);
		retMap.put("rows", list);
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 打开编辑器(画流程图)
	 * @return
	 */
	@Action(value = "openEdit", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/activiti/modeler/editor.jsp") },
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String openEdit(){
		RepositoryService repositoryService = processEngine.getRepositoryService();
        Model model = null;
		if(StringUtils.isNotBlank(id)){
			model = repositoryService.getModel(id);
		}
//		if (model == null) {
//            model = repositoryService.newModel();
//            repositoryService.saveModel(model);
//            id = model.getId();
//        }
		ServletActionContext.getRequest().setAttribute("modelId", id);
		return "list";
	}
	
	/**
	 * 删除模型
	 */
	@Action(value="delModeler")
	public void delModeler(){
		Map<String,Object> retMap = new HashMap<String, Object>();
		try {
			if(StringUtils.isNotBlank(id)){
				processEngine.getRepositoryService().deleteModel(id);
				retMap.put("resCode", "success");
				retMap.put("resMsg", "流程删除成功!");
			}else{
				retMap.put("resCode", "error");
				retMap.put("resMsg", "请选择要删除的流程!");
			}
			
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "流程删除失败!");
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 获取模型(流程图)数据在页面展示
	 */
	@Action(value="json")
	public void model(){
		RepositoryService repositoryService = processEngine
                .getRepositoryService();
		
		 Model model = repositoryService.getModel(id);
		 if (model == null) {
	            model = repositoryService.newModel();
	            repositoryService.saveModel(model);
	        }
		 Map<String,Object> root = new HashMap<>();
		 root.put("modelId", model.getId());
	     root.put("name", "name");
	     root.put("revision", 1);
	     root.put("description", "description");
	     byte[] bytes = repositoryService.getModelEditorSource(model.getId());
	     if(bytes!=null){
	    	 try {
				String modelEditorSource = new String(bytes, "utf-8");
				Map map = JSONUtils.fromJson(modelEditorSource, Map.class);
				root.put("model", map);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	 
	     }else{
	    	 Map<String,Object> modelNode = new HashMap<>();
	            modelNode.put("id", "canvas");
	            modelNode.put("resourceId", "canvas");

	            Map<String,Object> stencilSetNode = new HashMap<>();
	            stencilSetNode.put("namespace","http://b3mn.org/stencilset/bpmn2.0#");
	            modelNode.put("stencilset", stencilSetNode);

	            model.setMetaInfo(JSONUtils.toJson(root));
	            model.setName("name");
	            model.setKey("key");

	            root.put("model", modelNode); 
	     }
	   WebUtils.webSendJSON(JSONUtils.toJson(root));  
	}
	
	/**
	 * 读取JSON数据(画流程图界面中的那些属性信息)
	 */
	@Action(value="stencilset")
	public void stencilset(){
		InputStream stencilsetStream = this.getClass().getClassLoader()
                .getResourceAsStream("stencilset.json");
		try {
			if(stencilsetStream!=null){
				String string = IOUtils.toString(stencilsetStream, "utf-8");
				WebUtils.webSendString(string);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存流程图(模型)
	 */
	@Action(value="save")
	public void save(){
		
		if(StringUtils.isBlank(id)){//模型id
			return;
		}
		RepositoryService repositoryService = processEngine.getRepositoryService();//获取仓库服务对象
        Model model = repositoryService.getModel(id);
        model.setName(name);//设置模型名称
        model.setVersion(model.getVersion());
        repositoryService.saveModel(model);//保存模型
        try {
			repositoryService.addModelEditorSource(model.getId(),
			        json_xml.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        WebUtils.webSendString("success");
	}
	
	/**
	 * 发布流程
	 */
	@Action(value="deployModeler")
	public void deployModeler(){
		String tenantId=tenantService.getTenantId();//获取租户id
		Map<String,String> map =new HashMap<>();
		RepositoryService repositoryService = processEngine.getRepositoryService();
        Model modelData = repositoryService.getModel(id);
        byte[] bytes = repositoryService
                .getModelEditorSource(modelData.getId());
        if(bytes == null){
        	map.put("resCode", "error");
			map.put("resMsg", "模型数据为空，请先设计流程并成功保存，再进行发布！");
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
			return ;
        }
        try {
			JsonNode modelNode = (JsonNode) new ObjectMapper().readTree(bytes);
			System.err.println(JSONUtils.toJson(modelNode));
			byte[] bpmnBytes = null;
			BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
			bpmnBytes = new BpmnXMLConverter().convertToXML(model);
			System.err.println(new String(bpmnBytes));
			String processName = modelData.getName() + ".bpmn20.xml";
			Deployment deployment = repositoryService.createDeployment()
					.name(modelData.getName())
					.addString(processName, new String(bpmnBytes, "UTF-8"))
					.tenantId(tenantId).deploy();
			modelData.setDeploymentId(deployment.getId());
			repositoryService.saveModel(modelData);
			List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
			Map<String,String> hashMap = new HashMap<String, String>();
			JsonNode childShapes = modelNode.get("childShapes");
			if(childShapes!=null && childShapes.size()>0){
				ExtendVo extendVo = null;
				for(int i=0;i<childShapes.size();i++){
					if("\"UserTask\"".equals(childShapes.get(i).get("stencil").get("id").toString())){
						extendVo = new ExtendVo();
						extendVo.setIsAssigner(childShapes.get(i).get("properties").get("zkhonryextendisassigner").toString().replace("\"", ""));
						extendVo.setMessage(childShapes.get(i).get("properties").get("zkhonryextendmessage").toString().replace("\"", ""));
						extendVo.setReject(childShapes.get(i).get("properties").get("zkhonryextendreject").toString().replace("\"", ""));
						extendVo.setStepreject(childShapes.get(i).get("properties").get("zkhonryextendstepreject").toString().replace("\"", ""));
						extendVo.setUrge(childShapes.get(i).get("properties").get("zkhonryextendurge").toString().replace("\"", ""));
						extendVo.setWithdept(childShapes.get(i).get("properties").get("zkhonryextendwithdept").toString().replace("\"", ""));
						hashMap.put(childShapes.get(i).get("resourceId").toString().replace("\"", ""),JSONUtils.toJson(extendVo));
					}
				}
			}
			if(processDefinitions!=null && processDefinitions.size()>0){
				for (ProcessDefinition processDefinition : processDefinitions) {
					processEngine.getManagementService().executeCommand(new SyncProcessCmd(processDefinition.getId(),hashMap));
				}
			}
			map.put("resCode", "success");
			map.put("resMsg", "发布成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "发布失败！");
		}
        String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
}
