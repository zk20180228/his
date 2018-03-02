package cn.honry.oa.activiti.queryFlow.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.base.bean.model.OaBpmCategory;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.base.bean.model.OaReminders;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.oa.activiti.bpm.process.service.OaBpmProcessService;
import cn.honry.oa.activiti.bpm.process.vo.OaProcessVo;
import cn.honry.oa.activiti.queryFlow.service.QueryFlowService;
import cn.honry.oa.activiti.queryFlow.vo.OaTaskInfoVAct;
import cn.honry.oa.activiti.tenant.service.TenantService;
import cn.honry.oa.userPortal.action.UserPortalAction;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 流程查询Action
 * @author donghe
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/activiti/queryFlow")
@SuppressWarnings({ "all" })
public class queryFlowAction extends ActionSupport {
	
	private Logger logger=Logger.getLogger(queryFlowAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	
	@Autowired
	@Qualifier(value = "queryFlowService")
	private QueryFlowService queryFlowService;
	public void setQueryFlowService(QueryFlowService queryFlowService) {
		this.queryFlowService = queryFlowService;
	}
	@Autowired
	@Qualifier(value = "tenantService")
	private TenantService tenantService;
	
	public void setTenantService(TenantService tenantService) {
		this.tenantService = tenantService;
	}
	@Autowired
	@Qualifier(value = "oaBpmProcessService")
	private OaBpmProcessService oaBpmProcessService;

	public void setOaBpmProcessService(OaBpmProcessService oaBpmProcessService) {
		this.oaBpmProcessService = oaBpmProcessService;
	}
	private String code;
	private String name;
	private int page;//页码
	private int rows;//每页记录数
	private String category;
	private String startTime;
	private String endTime;
	private String param;
	
	private String processInstanceId;
	private String attr2;
	private String assignee;
	private String remindcontent;
	private String rowsid;//要删除的行ID
	private String taskInfoId;//task_info表id
	
	public String getTaskInfoId() {
		return taskInfoId;
	}
	public void setTaskInfoId(String taskInfoId) {
		this.taskInfoId = taskInfoId;
	}

	private String title;
	//选中的树节点
	private String treeId;
	
	
	public String getTreeId() {
		return treeId;
	}
	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRowsid() {
		return rowsid;
	}
	public void setRowsid(String rowsid) {
		this.rowsid = rowsid;
	}
	public String getRemindcontent() {
		return remindcontent;
	}
	public void setRemindcontent(String remindcontent) {
		this.remindcontent = remindcontent;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getAttr2() {
		return attr2;
	}
	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public QueryFlowService getQueryFlowService() {
		return queryFlowService;
	}
	public TenantService getTenantService() {
		return tenantService;
	}
	public OaBpmProcessService getOaBpmProcessService() {
		return oaBpmProcessService;
	}
	/**
	 * 事务办理查询跳转页面
	 * @return
	 */
	@Action(value = "processManagementView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/queryFlow/processManagement.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String processManagementView(){
		return "list";
	}
	/**
	 * 我的事务查询跳转页面
	 * @return
	 */
	@Action(value = "myselfFlowView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/queryFlow/myselfFlow.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String myselfFlowView(){
		return "list";
	}
	/**
	 * 新建事务查询跳转页面
	 * @return
	 */
	@Action(value = "createFlowView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/queryFlow/createFlow.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String createFlowView(){
		return "list";
	}
	/**
	 * 我的流程查询跳转页面
	 * @return
	 */
	@Action(value = "queryFlowView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/queryFlow/queryFlow.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryFlowView(){
		/*String tenantId = tenantService.getTenantId();//租户id
		List<OaBpmCategory> list = oaBpmProcessService.getCategoryList();
		ServletActionContext.getRequest().setAttribute("bpmCategories", list);*/
		return "list";
	}
	/**
	 * 跳转到我的催办（详情）
	 * @return
	 */
	@Action(value = "toDetailHasten", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/queryFlow/detialHasten.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toDetailHasten(){
		return "list";
	}
	/**
	 * 
	 * 查询新建事务
	 * @return
	 */
	@Action(value = "listxinian")
	public void listxinian(){
		List<OaProcessVo> list = oaBpmProcessService.getCategoryList1(page,rows,param,category,treeId);
		Integer total = oaBpmProcessService.getCategoryList1Size(param,category,treeId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total",total);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 新建事务左侧树
	 */
	@Action(value="xinjianTree")
	public void xinjianTree(){
		List<TreeJson> list = queryFlowService.getXinJianTree();
		WebUtils.webSendJSON(JSONUtils.toJson(list));
	}
	/**
	 * 
	 * 
	 * @return
	 */
	@Action(value = "listOaTaskInfo")
	public void listOaTaskInfo(){
		List<OaTaskInfo> infos = queryFlowService.queryOaTaskInfo();
		Map<String, Object> map = new HashMap<String, Object>();
		if(infos==null){
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}else{
			for (int i = 0; i < infos.size(); i++) {
				if(infos.get(i).getAttr2()==null){
					map.put(infos.get(i).getProcessInstanceId(), infos.get(i).getAttr2());
				}else{
					map.put(infos.get(i).getProcessInstanceId(), infos.get(i).getAttr2());
				}
			}
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}
	}
	/**
	 * 
	 * 查询草稿箱
	 * @return
	 */
	@Action(value = "listOaKVRecord")
	public void listOaKVRecord(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<OaKVRecord> infos = queryFlowService.queryOaKVRecord(null, page, rows, category, startTime, endTime);
		int total = queryFlowService.queryOaKVRecordtotal(null, category, startTime, endTime);
		map.put("rows", infos);
		map.put("total", total);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * 查询未完箱
	 * @return
	 */
	@Action(value = "listWeiwan")
	public void listWeiwan(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<OaTaskInfo> infos = queryFlowService.queryOaTaskInfoVAct(param, page, rows, category, startTime, endTime);
		int total = queryFlowService.queryOaTaskInfoVActTotal(param, category, startTime, endTime);
		map.put("rows", infos);
		map.put("total", total);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * 查询退箱
	 * @return
	 */
	@Action(value = "listtui")
	public void listtui(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<OaTaskInfo> infos = queryFlowService.tuijianqueryOaTaskInfoVAct(param, page, rows, category, startTime, endTime);
		int total = queryFlowService.tuijianqueryOaTaskInfoVActTotal(param, category, startTime, endTime);
		map.put("rows", infos);
		map.put("total", total);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * 查询催办
	 * @return
	 */
	@Action(value = "listcuiban")
	public void listcuiban(){
		List<OaReminders> infos = queryFlowService.queryList(null,startTime,endTime,param,page,rows);
		if(null==infos||infos.size()==0){
			infos = new ArrayList<OaReminders>();
		}
		int total = queryFlowService.queryListSize(null,startTime,endTime,param);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", infos);
		map.put("total", total);
		String json = JSONUtils.toJson(map, "yyyy-MM-dd HH:mm:ss");
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * 保存催办
	 * @return
	 */
	@Action(value = "savecuiban")
	public void savecuiban(){
		Map<String,Object> retMap = new HashMap<String, Object>();
		try {
			retMap = queryFlowService.savecuiban(processInstanceId, name, attr2,code,assignee,remindcontent,taskInfoId);
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "催办失败!");
		}
		String json=JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询已结
	 * @Author: donghe
	 * @CreateDate: 2017年8月6日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryyijie")
	public void queryyijie(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<OaTaskInfo> list = new ArrayList<OaTaskInfo>();
		try{
			list = queryFlowService.querylistyijie(param, page, rows, category, startTime, endTime);
			int total = queryFlowService.querylistyijieTotal(param, category, startTime, endTime);
			map.put("rows", list);
			map.put("total", total);
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.webSendJSON("error");
			logger.error("SYGL_WDDB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_WDDB", "首页管理_我的待办", "2", "0"), e);
		}
		String json=JSONUtils.toJson(map,"yyyy-MM-dd HH:mm:ss");
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 查询下拉流程分类
	 * @Author: donghe
	 * @CreateDate: 2017年8月6日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryfenlei")
	public void queryfenlei(){
		List<OaBpmCategory> list = new ArrayList<OaBpmCategory>();
		try{
			list = queryFlowService.quert();
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.webSendJSON("error");
			logger.error("SYGL_WDDB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_WDDB", "首页管理_我的待办", "2", "0"), e);
		}
		String json=JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 催办内容跳转
	 * @return
	 */
	@Action(value = "viewremind", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/queryFlow/remindcontent.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewremind(){
		return "list";
	}
	/**
	 * 
	 * 催办 已读按钮操作
	 * @return
	 */
	@Action(value = "cuibanYidu")
	public void cuibanYidu(){
		Map<String,Object> retMap = new HashMap<String, Object>();
		try {
			retMap = queryFlowService.updatecuiban(processInstanceId, code);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("YWSQ_WDCB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YWSQ_WDCB", "业务申请_我的催办", "2", "1"), e);
		}
		String json=JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * 回复按钮操作
	 * @return
	 */
	@Action(value = "cuibanHuifu")
	public void cuibanHuifu(){
		Map<String,Object> retMap = new HashMap<String, Object>();
		try {
			retMap = queryFlowService.updateHuifu(processInstanceId, code,remindcontent);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("YWSQ_WDCB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YWSQ_WDCB", "业务申请_我的催办", "2", "1"), e);
		}
		String json=JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 删除我的催办 根据ID
	 */
	@Action(value="deleteMyCuiBan")
	public void deleteMyCuiBan(){
		Map<String,String> map = new HashMap<String, String>();
		try{
			queryFlowService.deleteMyCuiBan(rowsid);
			map.put("resCode", "success");
			map.put("resMsg", "删除成功!");
		}catch(Exception e){
			map.put("resCode", "error");
			map.put("resMsg", "删除失败!");
			e.printStackTrace();
			logger.error("YWSQ_WDCB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YWSQ_WDCB", "业务申请_我的催办", "2", "1"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**
	 * 根据实例id删除催办
	 */
	@Action(value="deleteMyCuiBanByProcessId")
	public void deleteMyCuiBanByProcessId(){
		Map<String,String> map = new HashMap<String, String>();
		try{
			queryFlowService.deleteMyCuiBanByProcess(rowsid);
			map.put("resCode", "success");
			map.put("resMsg", "删除成功!");
		}catch(Exception e){
			map.put("resCode", "error");
			map.put("resMsg", "删除失败!");
			e.printStackTrace();
			logger.error("YWSQ_WDCB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YWSQ_WDCB", "业务申请_我的催办", "2", "1"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**
	 * 根据实例id获取催办
	 */
	@Action(value="getMyCuiBanByProecssId")
	public void getMyCuiBanByProecssId(){
		List<OaReminders> list = new ArrayList<OaReminders>();
		try{
			list = queryFlowService.getMyCuiBanByProecssId(rowsid);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("YWSQ_WDCB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YWSQ_WDCB", "业务申请_我的催办", "2", "1"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(list));
	}
	@Action(value="deleteMyGao")
	public void deleteMyGao(){
		Map<String,String> map = new HashMap<String, String>();
		try{
			queryFlowService.deleteMyGao(rowsid);
			map.put("resCode", "success");
			map.put("resMsg", "删除成功!");
		}catch(Exception e){
			map.put("resCode", "error");
			map.put("resMsg", "删除失败!");
			e.printStackTrace();
			logger.error("YWSQ_CGX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YWSQ_CGX", "业务申请_草稿箱", "2", "1"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	
	
	/**  
	 * 
	 * 查询我的待办
	 * @Author: zpty
	 * @CreateDate: 2017年8月6日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryAgency")
	public void queryAgency(){
		List<OaTaskInfo> list = new ArrayList<OaTaskInfo>();
		int total = 0;
		try{
			String tenantId = tenantService.getTenantId();//租户id
			String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();//登录账户
			if(StringUtils.isNotBlank(tenantId)){
				list = queryFlowService.getListForTask(account,tenantId,startTime,endTime,title,page,rows);
				total = queryFlowService.getNumberForTask(account,tenantId,startTime,endTime,title);
			}
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.webSendJSON("error");
			logger.error("SYGL_WDDB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_WDDB", "首页管理_我的待办", "2", "0"), e);
		}
//		String json=JSONUtils.toJson(list,"yyyy-MM-dd");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", total);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}


/**  
	 * 
	 * 查询历史
	 * @Author: donghe
	 * @CreateDate: 2017年8月6日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "querylishi")
	public void querylishi(){
		List<OaTaskInfo> list = new ArrayList<OaTaskInfo>();
		int total = 0;
		try{
				list = queryFlowService.querylishijili(startTime,endTime,title,page,rows);
				total = queryFlowService.querylishijiliNum(startTime,endTime,title);
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.webSendJSON("error");
			logger.error("SYGL_WDDB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_WDDB", "首页管理_我的待办", "2", "0"), e);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", total);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}

	/**
	 * 
	 * 查询我收到的催办
	 * @return
	 */
	@Action(value = "listWOcuiban")
	public void listWOcuiban(){
		List<OaReminders> infos = new ArrayList<OaReminders>();
		int total = 0;
		try{
			infos = queryFlowService.queryList1(null,startTime,endTime,title,page,rows);
			total = queryFlowService.queryNum(startTime,endTime,title);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("YWSQ_WDCB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YWSQ_WDCB", "业务申请_我的催办", "2", "1"), e);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", infos);
		map.put("total", total);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
}
