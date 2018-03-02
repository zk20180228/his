package cn.honry.migrate.outInterfaceManager.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import cn.honry.base.bean.model.ExterInter;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.ServiceManagement;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.migrate.outInterfaceManager.service.OutInterfaceManagerService;
import cn.honry.migrate.serviceManagement.service.ServiceManagementService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/migrate/outInterfaceManager")
public class OutInterfaceManagerAction extends ActionSupport implements ModelDriven<ExterInter> {
	
	@Autowired
	@Qualifier(value="outInterfaceManagerService")
	private OutInterfaceManagerService outInterfaceManagerService;
	public void setOutInterfaceManagerService(
			OutInterfaceManagerService outInterfaceManagerService) {
		this.outInterfaceManagerService = outInterfaceManagerService;
	}
	@Autowired
	@Qualifier(value = "serviceManagementService")
	private ServiceManagementService serviceManagementService;
	public void setServiceManagementService(
			ServiceManagementService serviceManagementService) {
		this.serviceManagementService = serviceManagementService;
	}

	private static final long serialVersionUID = 1L;
	//分页
	private String page;
	private String rows;
	private String menuAlias;//权限
	private String queryCode;
	private String fireCode;//厂商code
	private ExterInter exterInter = new ExterInter();
	private HttpServletRequest request = ServletActionContext.getRequest();
	private Logger logger=Logger.getLogger(OutInterfaceManagerAction.class);
	private String serviceState;
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	

	
	public String getServiceState() {
		return serviceState;
	}



	public void setServiceState(String serviceState) {
		this.serviceState = serviceState;
	}



	public String getFireCode() {
		return fireCode;
	}



	public void setFireCode(String fireCode) {
		this.fireCode = fireCode;
	}



	public String getQueryCode() {
		return queryCode;
	}

	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public ExterInter getExterInter() {
		return exterInter;
	}

	public void setExterInter(ExterInter exterInter) {
		this.exterInter = exterInter;
	}

	/**
	 * 
	 * 
	 * <p>进入页面</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月21日 下午7:30:03 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月21日 下午7:30:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@RequiresPermissions(value={"WBJKGL:function:list"})
	@Action(value = "toList", results = { @Result(name = "list", location = "/WEB-INF/pages/migrate/outInterfaceManager/outInterfaceList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toList(){
		return "list";
	}
	/**
	 * 
	 * <p>查看方法</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月21日 下午7:30:30 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月21日 下午7:30:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@RequiresPermissions(value={"WBJKGL:function:list"})
	@Action(value = "interfaceList")
	public void interfaceList() {
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			List<ExterInter> list = null;
			list =  outInterfaceManagerService.findAll(queryCode,menuAlias,page,rows,serviceState);
			int total = outInterfaceManagerService.getTotal(queryCode,menuAlias,serviceState);
			map.put("rows", list);
			map.put("total", total);
		} catch (Exception e) {
			map.put("rows", new ArrayList<ExterInter>());
			map.put("total", 0);
			logger.error("JKXXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "接口信息管理", "2", "0"), e);
		}
		
		String json = JSONUtils.toJson(map,"yyyy-MM-dd HH:mm:ss");
		WebUtils.webSendString(json);
	}
	/**
	 * 
	 * <p>跳到添加页面</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月21日 下午7:30:56 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月21日 下午7:30:56 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@RequiresPermissions(value={"WBJKGL:function:list"})
	@Action(value = "addInter", results = { @Result(name = "add", location = "/WEB-INF/pages/migrate/outInterfaceManager/outInterfaceEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addInter() {
		exterInter = new ExterInter();
		ServletActionContext.getRequest().setAttribute("exterInter", exterInter);
		return "add";
	}
	
	/**
	 * 
	 * <p>跳到修改页面</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月21日 下午7:30:56 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月21日 下午7:30:56 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@RequiresPermissions(value={"WBJKGL:function:list"})
	@Action(value = "editInter", results = { @Result(name = "edit", location = "/WEB-INF/pages/migrate/outInterfaceManager/outInterfaceEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editInter() {
		try {
			String id =  request.getParameter("id");
			exterInter = outInterfaceManagerService.findById(id);
			if(exterInter!=null){
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(exterInter.getAuthStime()!=null){
					exterInter.setAuthStimeSDF(df.format(exterInter.getAuthStime()));
				}
				if(exterInter.getAuthEtime()!=null){
					exterInter.setAuthEtimeSDF(df.format(exterInter.getAuthEtime()));
				}
			}
			ServletActionContext.getRequest().setAttribute("exterInter", exterInter);
		} catch (Exception e) {
			logger.error("JKXXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "接口信息管理", "2", "0"), e);
		}
		return "edit";
	}
	
	@RequiresPermissions(value={"WBJKGL:function:view"})
	@Action(value = "viewInter", results = { @Result(name = "view", location = "/WEB-INF/pages/migrate/outInterfaceManager/outInterfaceView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewInter() {
		try {
			String id =  request.getParameter("id");
			exterInter = outInterfaceManagerService.findById(id);
			if(exterInter!=null){
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(exterInter.getAuthStime()!=null){
					exterInter.setAuthStimeSDF(df.format(exterInter.getAuthStime()));
				}
				if(exterInter.getAuthEtime()!=null){
					exterInter.setAuthEtimeSDF(df.format(exterInter.getAuthEtime()));
				}
			}
			ServletActionContext.getRequest().setAttribute("exterInter", exterInter);
		} catch (Exception e) {
			logger.error("JKXXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "接口信息管理", "2", "0"), e);
		}
		return "view";
	}
	/**
	 * 
	 * <p>添加和修改方法</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月21日 下午7:30:56 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月21日 下午7:30:56 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@RequiresPermissions(value={"WBJKGL:function:list"})
	@Action(value = "saveInter")
	public void saveInter() {
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			if (exterInter != null) {
					outInterfaceManagerService.saveOrUpdateExterInter(exterInter);
					map.put("resCode", "success");
					map.put("resMsg", "保存成功！");
			}
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "保存失败！");
			logger.error("JKXXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "接口信息管理", "2", "0"), e);
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * <p>删除方法</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月21日 下午7:34:19 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月21日 下午7:34:19 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "delInter")
	public void delInter() {
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			String ids =  request.getParameter("ids");
			outInterfaceManagerService.delInter(ids);
			map.put("resCode", "success");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "删除失败！");
			logger.error("JKXXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "接口信息管理", "2", "0"), e);
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * 
	 * <p> 查询主备服务管理</p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月26日 下午8:53:45 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月26日 下午8:53:45 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "findAllServer")
	public void findAllServer(){
		List<ServiceManagement> list = serviceManagementService.queryServiceManagement(queryCode);
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * 
	 * <p>渲染服务 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月26日 下午6:46:45 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月26日 下午6:46:45 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "renderServer")
	public void renderServer(){
		List<ServiceManagement> list = serviceManagementService.queryServiceManagement(null);
		Map<String,String> serverMap=new HashMap<String,String>();
		for(ServiceManagement vo:list){
			serverMap.put(vo.getCode(), vo.getName());
		}
		String json = JSONUtils.toJson(serverMap);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * 
	 * <p>获取所有厂商 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月27日 上午10:12:09 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月27日 上午10:12:09 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "findAllFired")
	public void findAllFired(){
		List<ExterInter> list = outInterfaceManagerService.getfireCode(fireCode);
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * 
	 * <p>渲染厂商 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月27日 上午10:12:24 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月27日 上午10:12:24 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "renderFired")
	public void renderFired(){
		Map<String,String> map=outInterfaceManagerService.getfireCodeRender(fireCode);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	@Override
	public ExterInter getModel() {
		return exterInter;
	}
	
	@Action(value="updateState")
	public void updateState(){
		Map<String,Object> map=new HashMap<String,Object>();
		try{
			outInterfaceManagerService.updateState(exterInter.getId(), exterInter.getState().toString());
			if(exterInter==null){
				throw new Exception("参数异常");
			}
			String tip=null;
			if(exterInter.getState()==null){
				throw new Exception("数据同步状态转换失败");
			}
			if("1".equals(exterInter.getState().toString())){
				tip="停用";
			}else{
				tip="启用";
			}
			map.put("resCode", "success");
			map.put("resMsg", tip+"成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "修改失败！");
			e.printStackTrace();
			logger.error("JKXXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "接口信息管理", "2", "0"), e);
		}
		String json=JSONUtils.toJson(map,"yyyy-MM-dd HH:mm:ss");
		WebUtils.webSendJSON(json);
	}

}
