package cn.honry.oa.activiti.bpm.process.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import cn.honry.base.bean.model.OaActivitiDept;
import cn.honry.base.bean.model.OaBpmCategory;
import cn.honry.base.bean.model.OaBpmConfBase;
import cn.honry.base.bean.model.OaBpmConfNode;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.oa.activiti.bpm.base.service.OaBpmConfBaseService;
import cn.honry.oa.activiti.bpm.category.service.OaBpmCategoryService;
import cn.honry.oa.activiti.bpm.node.service.OaBpmConfNodeService;
import cn.honry.oa.activiti.bpm.process.service.OaBpmProcessService;
import cn.honry.oa.activiti.bpm.process.vo.ExtendProcessVo;
import cn.honry.oa.activiti.bpm.utils.ExtendVo;
import cn.honry.oa.activiti.tenant.service.TenantService;
import cn.honry.oa.activitiDept.service.ActivitiDeptService;
import cn.honry.oa.formInfo.service.FormInfoService;
import cn.honry.oa.formInfo.vo.KeyValVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.MyBeanUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 流程配置Action
 * @author luyanshou
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/activiti/process")
@SuppressWarnings({ "all" })
public class OaBpmProcessAction extends ActionSupport {

	
	private static final long serialVersionUID = 1L;
	
	
	@Autowired
	@Qualifier(value = "oaBpmProcessService")
	private OaBpmProcessService oaBpmProcessService;

	public void setOaBpmProcessService(OaBpmProcessService oaBpmProcessService) {
		this.oaBpmProcessService = oaBpmProcessService;
	}

	@Autowired
	@Qualifier(value = "oaBpmCategoryService")
	private OaBpmCategoryService oaBpmCategoryService;

	public void setOaBpmCategoryService(OaBpmCategoryService oaBpmCategoryService) {
		this.oaBpmCategoryService = oaBpmCategoryService;
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
	//表单
	@Autowired
	@Qualifier(value = "formInfoService")
	private FormInfoService formInfoService;
	public void setFormInfoService(FormInfoService formInfoService) {
		this.formInfoService = formInfoService;
	}
	//工作流科室
	private ActivitiDeptService activitiDeptService;
	@Autowired
	@Qualifier(value = "activitiDeptService")
	public void setActivitiDeptService(ActivitiDeptService activitiDeptService) {
		this.activitiDeptService = activitiDeptService;
	}
	
	@Autowired
	@Qualifier(value = "oaBpmConfNodeService")
	private OaBpmConfNodeService oaBpmConfNodeService;
	public void setOaBpmConfNodeService(OaBpmConfNodeService oaBpmConfNodeService) {
		this.oaBpmConfNodeService = oaBpmConfNodeService;
	}
	
	private String id;
	
	private int page;//页码
	private int rows;//每页记录数
	private String name;
	
	private String jsonData;
	
	private OaBpmProcess oaBpmProcess;
	
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

	public OaBpmProcess getOaBpmProcess() {
		return oaBpmProcess;
	}

	public void setOaBpmProcess(OaBpmProcess oaBpmProcess) {
		this.oaBpmProcess = oaBpmProcess;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	/**
	 * 流程配置视图
	 * @return
	 */
	@Action(value = "processView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/process/processView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String processView(){
		return "list";
	}
	
	/**
	 * 流程配置视图
	 * @return
	 */
	@Action(value = "processViewPage")
	public void processViewPage(){
		Map<String,Object> retMap= new HashMap<>();
		String tenantId = tenantService.getTenantId();//租户id
		List<OaBpmProcess> list = oaBpmProcessService.getListByPage(name,tenantId,(page-1)*rows,rows);
		int total = oaBpmProcessService.getTotal(name,tenantId);
		retMap.put("total", total);
		retMap.put("rows", list);
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 编辑流程配置
	 * @return 
	 */
	@Action(value = "editView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/process/editView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editView(){
		if(StringUtils.isNotBlank(id)){
			OaBpmProcess bpmProcess = oaBpmProcessService.get(id);//根据id得到流程定义扩展对象
			ServletActionContext.getRequest().setAttribute("model", bpmProcess);
		}
		List<OaBpmCategory> bpmCategorys = oaBpmCategoryService.getListByPage(null,null, 0, 0);//获取流程分类列表 主要数据：流程分类名，分类的id
		List<OaBpmConfBase> bpmConfBases = oaBpmConfBaseService.getList();//获取流程配置列表，主要数据流程定义的id，流程配置的id
		//表单
		List<KeyValVo> keyValList = formInfoService.getValidFormInfo();//获取表单：表单code和表单名字
		//工作流科室
		List<OaActivitiDept> deptList = activitiDeptService.queryActivitiDept();//获取科室信息列表
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("bpmCategories", bpmCategorys);
		request.setAttribute("bpmConfBases", bpmConfBases);
		request.setAttribute("keyValList", keyValList);
		request.setAttribute("deptList", deptList);
		//关联流程
		List<OaBpmProcess> proList = oaBpmProcessService.getListByPage(null,null, 0, 0);//获取所有流程定义列表，用来绑定
		request.setAttribute("topFlowList", proList);
		request.setAttribute("downFlowList", proList);
		return "list";
	}
	@Action("bpmCategories")
	public void bpmCategories(){
		List<OaBpmCategory> bpmCategorys = oaBpmCategoryService.getListByPage(null,null, 0, 0);//获取流程分类列表 主要数据：流程分类名，分类的id
		WebUtils.webSendJSON(JSONUtils.toJson(bpmCategorys));
	}
	@Action("bpmConfBases")
	public void bpmConfBases(){
		List<OaBpmConfBase> bpmConfBases = oaBpmConfBaseService.getList();//获取流程配置列表，主要数据流程定义的id，流程配置的id
		WebUtils.webSendJSON(JSONUtils.toJson(bpmConfBases));
	}
	@Action("keyValList")
	public void keyValList(){
		List<KeyValVo> keyValList = formInfoService.getValidFormInfo();//获取表单：表单code和表单名字
		WebUtils.webSendJSON(JSONUtils.toJson(keyValList));
	}
	@Action("deptList")
	public void deptList(){
		List<OaActivitiDept> deptList = activitiDeptService.queryActivitiDept();//获取科室信息列表
		WebUtils.webSendJSON(JSONUtils.toJson(deptList));
	}
	@Action("flowList")
	public void flowList(){
		List<OaBpmProcess> flowList = oaBpmProcessService.getListByPage(null,null, 0, 0);//获取所有流程定义列表，用来绑定
		WebUtils.webSendJSON(JSONUtils.toJson(flowList));
	}
	/**
	 * 保存配置
	 */
	@Action(value="save")
	public void save(){
		Map<String,String> map = new HashMap<>();
		try {
			if(oaBpmProcess==null){
				return;
			}
			String oId = oaBpmProcess.getId();
			if(StringUtils.isNotBlank(oId)){
				OaBpmProcess process = oaBpmProcessService.get(oId);
				MyBeanUtils.copyPropertiesButNull(process, oaBpmProcess);
				process.setStop_flg(oaBpmProcess.getStop_flg());
				process.setAction(StringUtils.isBlank(process.getTopFlow())?"askLeaveAction/askLeave.action;askLeaveAction/approveLeave.action":"askLeaveAction/cancelHolidayList.action;askLeaveAction/approveLeave.action");
				process.setUpdateTime(new Date());
				process.setUpdateUser(getAccount());
				oaBpmProcessService.saveOrUpdate(process);
				
			}else{
				String tenantId = tenantService.getTenantId();
				oaBpmProcess.setTenantId(tenantId);
				oaBpmProcess.setCode("");//设置编号
				oaBpmProcess.setCreateTime(new Date());
				oaBpmProcess.setCreateDept(getDept());
				oaBpmProcess.setCreateUser(getAccount());
				oaBpmProcess.setAction(StringUtils.isBlank(oaBpmProcess.getTopFlow())?"askLeaveAction/askLeave.action;askLeaveAction/approveLeave.action":"askLeaveAction/cancelHolidayList.action;askLeaveAction/approveLeave.action");
				oaBpmProcessService.saveOrUpdate(oaBpmProcess);
			}
			map.put("resMsg", "success");
			map.put("resCode", "保存成功！");
			} catch (Exception e) {
				map.put("resMsg", "error");
				map.put("resCode", "保存失败！");
				e.printStackTrace();
			}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
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
    
    @Action(value = "nodeView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/process/nodeView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String nodeView(){
    	List<OaBpmConfNode> dnList = oaBpmConfNodeService.getConfNodeListByConfBaseCode(id);
    	List<ExtendProcessVo> voList = new ArrayList<ExtendProcessVo>();
    	if(dnList!=null&&dnList.size()>0){
    		ExtendProcessVo vo = null;
    		for(OaBpmConfNode cn : dnList){
    			vo = new ExtendProcessVo();
    			if(StringUtils.isNotBlank(cn.getExtend())){
    				Gson gson = new Gson();
    				ExtendVo extendVo = gson.fromJson(cn.getExtend(), ExtendVo.class);
        			vo.setId(cn.getId());
        			vo.setName(cn.getName());
        			vo.setSid(cn.getCode());
        			vo.setIsAssigner(extendVo.getIsAssigner());
        			vo.setMessage(extendVo.getMessage());
        			vo.setReject(extendVo.getReject());
        			vo.setStepreject(extendVo.getStepreject());
        			vo.setUrge(extendVo.getUrge());
        			vo.setWithdept(extendVo.getWithdept());
        			voList.add(vo);
    			}
    		}
    	}
    	HttpServletRequest request = ServletActionContext.getRequest();
    	request.setAttribute("voList", voList);
    	return "list";
    }
    
    @Action(value="saveNode")
	public void saveNode(){
		Map<String,String> retMap = new HashMap<>();
		try {
			if(StringUtils.isNotBlank(jsonData)){
				String data = jsonData.substring(1, jsonData.length()-1).replaceAll("\"", "");
				String[] dataArr = data.split(",");
				Map<String,ExtendVo> dataMap = new HashMap<String, ExtendVo>();
				for(String info : dataArr){
					String[] infoArr = info.split(":");
					String[] infoAttArr = infoArr[0].split("-");
					if(dataMap.get(infoAttArr[0])!=null){
						if("isAssigner".equals(infoAttArr[1])){
							dataMap.get(infoAttArr[0]).setIsAssigner(infoArr[1]);
						}else if("message".equals(infoAttArr[1])){
							dataMap.get(infoAttArr[0]).setMessage(infoArr[1]);
						}else if("reject".equals(infoAttArr[1])){
							dataMap.get(infoAttArr[0]).setReject(infoArr[1]);
						}else if("stepreject".equals(infoAttArr[1])){
							dataMap.get(infoAttArr[0]).setStepreject(infoArr[1]);
						}else if("urge".equals(infoAttArr[1])){
							dataMap.get(infoAttArr[0]).setUrge(infoArr[1]);
						}else if("withdept".equals(infoAttArr[1])){
							dataMap.get(infoAttArr[0]).setWithdept(infoArr[1]);
						}
					}else{
						ExtendVo vo = new ExtendVo();
						if("isAssigner".equals(infoAttArr[1])){
							vo.setIsAssigner(infoArr[1]);
						}else if("message".equals(infoAttArr[1])){
							vo.setMessage(infoArr[1]);
						}else if("reject".equals(infoAttArr[1])){
							vo.setReject(infoArr[1]);
						}else if("stepreject".equals(infoAttArr[1])){
							vo.setStepreject(infoArr[1]);
						}else if("urge".equals(infoAttArr[1])){
							vo.setUrge(infoArr[1]);
						}else if("withdept".equals(infoAttArr[1])){
							vo.setWithdept(infoArr[1]);
						}
						dataMap.put(infoAttArr[0], vo);
					}
				}
				oaBpmConfNodeService.saveNode(dataMap);
				retMap.put("resCode", "success");
				retMap.put("resMsg", "保存成功！");
			}else{
				retMap.put("resCode", "error");
				retMap.put("resMsg", "没有需要保存的信息！");
			}
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "保存失败！");
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
    
}
