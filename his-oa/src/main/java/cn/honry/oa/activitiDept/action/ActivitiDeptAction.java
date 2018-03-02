package cn.honry.oa.activitiDept.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.oa.activitiDept.service.ActivitiDeptService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 工作流科室维护
 * @author  zpty
 * @date 2017-8-13 15：40
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/activitiDept")
public class ActivitiDeptAction extends ActionSupport{
	private Logger logger=Logger.getLogger(ActivitiDeptAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}

	private static final long serialVersionUID = 1L;
	private ActivitiDeptService activitiDeptService;
	@Autowired
	@Qualifier(value = "activitiDeptService")
	public void setActivitiDeptService(ActivitiDeptService activitiDeptService) {
		this.activitiDeptService = activitiDeptService;
	}
	/**
	 * 工作流科室
	 */
	private OaActivitiDept activitiDept = new OaActivitiDept();

	public OaActivitiDept getActivitiDept() {
		return activitiDept;
	}

	public void setActivitiDept(OaActivitiDept activitiDept) {
		this.activitiDept = activitiDept;
	}

	/**
	 * 工作流科室列表数据
	 */
	private List<OaActivitiDept> activitiDeptList = new ArrayList<OaActivitiDept>();

	public List<OaActivitiDept> getActivitiDeptList() {
		return activitiDeptList;
	}

	public void setActivitiDeptList(List<OaActivitiDept> activitiDeptList) {
		this.activitiDeptList = activitiDeptList;
	}

	/**
	 * 科室列表数据
	 */
	private List<SysDepartment> deptList = new ArrayList<SysDepartment>();
	
	public List<SysDepartment> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<SysDepartment> deptList) {
		this.deptList = deptList;
	}

	/**
	 * 工作流科室初始化传入的数据
	 */
	private String[] butName;
	
	public String[] getButName() {
		return butName;
	}

	public void setButName(String[] butName) {
		this.butName = butName;
	}

	/**
	 * 列表查询用传递条件
	 */
	private String searchName;//查询条件
	private String parentCode;//树节点
	public String getSearchName() {
		return searchName;
	}
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	/**
	 * @Description:翻页参数
	 */
	public String page;
	public void setPage(String page) {
		this.page = page;
	}
	public String rows;
	public void setRows(String rows) {
		this.rows = rows;
	}
	
	/**
	 * 科室树传过来的多选科室ID
	 */
	private String dId;
	
	public String getdId() {
		return dId;
	}

	public void setdId(String dId) {
		this.dId = dId;
	}

	/**  
	 * 
	 * 工作流科室初始化列表显示
	 * @Author: zpty
	 * @CreateDate: 2017年8月13日 下午5:48:26 
	 * @version: V1.0
	 * @throws:
	 * @return: String 返回值类型
	 *
	 */
	@Action(value = "activitiDeptInitialization", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/activitiDept/activitiDeptInitialization.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String activitiDeptInitialization() {
		try {
			//查询出所有的没有作为工作流科室的科室,从科室表中查
			deptList = activitiDeptService.queryDept();
			//查询所有的工作流科室
			activitiDeptList=activitiDeptService.queryActivitiDept();
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("GZLGL_GZLKSCSH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZLGL_GZLKSCSH", "工作流管理_工作流科室初始化", "2", "0"), e);
		}
		return "list";
	}
	
	/**  
	 * 
	 * 初始化工作流科室
	 * @Author: zpty
	 * @CreateDate: 2017年8月13日 下午5:51:46 
	 * @version: V1.0
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "initializationActivitiDept")
	public void initializationActivitiDept(){
		try{
			//将新传递过来的初始化科室的code数组进行保存
			activitiDeptService.initialization(butName);
			WebUtils.webSendJSON("success");
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("GZLGL_GZLKSCSH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZLGL_GZLKSCSH", "工作流管理_工作流科室初始化", "2", "0"), e);
		}
	}

	
	/**  
	 * 
	 * 工作流科室列表显示--第二版
	 * @Author: zpty
	 * @CreateDate: 2017年8月13日 下午5:48:26 
	 * @version: V1.0
	 * @throws:
	 * @return: String 返回值类型
	 *
	 */
	@Action(value = "activitiDeptList", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/activitiDept/activitiDeptList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String activitiDeptList() {
		return "list";
	}
	
	/**  
	 * 
	 * 工作流科室查询列表--第二版
	 * @Author: zpty
	 * @CreateDate: 2017年8月13日 下午5:48:26 
	 * @version: V1.0
	 * @throws:
	 * @return: String 返回值类型
	 *
	 */
	@Action(value = "queryActivitiDept", results = { @Result(name = "json", type = "json") })
	public void queryActivitiDept() {
		try {
			OaActivitiDept departSearch = new OaActivitiDept();
			if(StringUtils.isNotBlank(searchName)){
				departSearch.setDeptName(searchName);
			}
			if(StringUtils.isNotBlank(parentCode)){
				departSearch.setParentCode(parentCode);
			}
			activitiDeptList = activitiDeptService.getPage(page,rows,departSearch);
			int total = activitiDeptService.getTotal(departSearch);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", activitiDeptList);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("GZLGL_GZLKSCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZLGL_GZLKSCX", "工作流管理_工作流科室查询", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  跳转科室树页面
	 * @Author：zpty
	 * @CreateDate：2017-8-14 上午09:39:35  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryDeptTree", results = { @Result(name = "queryDeptTree", location = "/WEB-INF/pages/oa/activitiDept/deptTree.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryDeptTree() {
		return "queryDeptTree";
	}
	
	/**  
	 *  
	 * @Description：工作流科室的添加
	 * @Author：zpty
	 * @CreateDate：2017-08-14 上午09:39:35   
	 * @version 1.0
	 *
	 */
	@Action(value = "editDept",interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void editDept()throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			//判断传过来的code是否重复
			String flg="0";
			flg=activitiDeptService.searchDouble(activitiDept);
			if(flg.equals("1")){
				map.put("resCode", "double");
				map.put("resMessage", "编码重复");
			}else{
				activitiDeptService.saveActivitiDept(dId,activitiDept);
				map.put("resCode", "success");
				map.put("resMessage", "保存成功");
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			map.put("resCode", "error");
			map.put("resMessage", "保存失败");
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
			logger.error("GZLGL_GZLKSBC", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZLGL_GZLKSBC", "工作流管理_工作流科室保存", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：工作流科室的删除
	 * @Author：zpty
	 * @CreateDate：2017-08-14 上午09:39:35   
	 * @version 1.0
	 *
	 */
	@Action(value = "delActivitiDept",interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void delActivitiDept()throws Exception{
		try{
			//dId不为空的时候,有需要删除的科室
			if(StringUtils.isNotBlank(dId)){
				activitiDeptService.delActivitiDept(dId);
			}
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendString("error");
			logger.error("GZLGL_GZLKSBC", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZLGL_GZLKSBC", "工作流管理_工作流科室保存", "2", "0"), e);
		}
	}
	
	/**  
	 * 
	 * 工作流科室树
	 * @Author: zpty
	 * @CreateDate: 2017-8-21 下午15:30:31
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "treeActivitiDept", results = { @Result(name = "json", type = "json") })
	public void treeActivitiDept() {
		List<TreeJson> treeDepar = activitiDeptService.QueryTree(dId);
		String json = JSONUtils.toJson(treeDepar);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 工作流科室添加跳转
	 * @Author: zpty
	 * @CreateDate: 2017-8-21 下午15:30:31
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "addActivitiDept", results = { @Result(name = "add", location = "/WEB-INF/pages/oa/activitiDept/activitiDeptEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addActivitiDept() throws IOException {
		
		return "add";
	}
	/**  
	 * 
	 * 工作流科室修改跳转
	 * @Author: zpty
	 * @CreateDate: 2017-8-21 下午15:30:31
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "editActivitiDept", results = { @Result(name = "edit", location = "/WEB-INF/pages/oa/activitiDept/activitiDeptEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editActivitiDept() throws IOException {
		if (StringUtils.isNotBlank(dId)) {
			activitiDept = activitiDeptService.get(dId);
		}
		return "edit";
	}
}
