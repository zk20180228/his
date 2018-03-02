package cn.honry.mobile.moOperateLog.action;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import cn.honry.base.bean.model.MSysOperateLog;
import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.inner.system.user.service.UserInInterService;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.mobile.moOperateLog.service.MoOperateLogService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/mosys/moOperateLog")
public class MoOperateLogAction extends ActionSupport{

	private Logger logger = Logger.getLogger(MoOperateLogAction.class);
	@Autowired
	@Qualifier(value = "userInInterService")
	private UserInInterService userInInterService;
	public void setUserInInterService(UserInInterService userInInterService) {
		this.userInInterService = userInInterService;
	}

	@Autowired
	@Qualifier(value = "moOperateLogService")
	private MoOperateLogService moOperateLogService;
	public void setMoOperateLogService(MoOperateLogService moOperateLogService) {
		this.moOperateLogService = moOperateLogService;
	}
	
	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService  exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	
	private MSysOperateLog sysOperateLog;
	
	private String menuAlias;
	
	private String page;
	
	private String rows;
	
	private String queryName;
	
	private String id;

	
	@RequiresPermissions(value={"MOCZRZ:function:view"})
	@Action(value = "operateLogList", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/operateLog/operateLogList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String operateLogList() {
		return "list";
	}
	
	/** 查询信息列表
	* @param request
	* @param response
	* @author zxl 
	* @date 2017年6月15日
	*/
	@RequiresPermissions(value={"MOCZRZ:function:query"})
	@Action(value = "queryOperateLog")
	public void queryOperateLog(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			JSONArray jsonArray  = moOperateLogService.getOperateLogByPage(page, rows, queryName);
			Long total = moOperateLogService.getTotalByPage(queryName);
			map.put("total", total);
			map.put("rows", jsonArray);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("MOCZRZ", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("MOCZRZ","操作日志","2","1"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 跳转查看信息界面
	* @param request
	* @param response
	* @param id 版本id
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"MOCZRZ:function:view"})
	@Action(value = "toViewOperateLog", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/operateLog/operateLogView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewOperateLog(){
		try{
			if(StringUtils.isNotBlank(id)){
				sysOperateLog = moOperateLogService.getOperateLogById(id);
			}else{
				sysOperateLog = new MSysOperateLog();
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("MOCZRZ", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("MOCZRZ","操作日志","2","1"), e);
		}
		
		return "list";
	}
	
	
	/**  
	 *  
	 * @Description：  查询用户信息
	 * @Author：zxl
	 * @CreateDate：2015-6-3 下午05:11:17  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-3 下午05:11:17  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryUserAllMap")
	public void queryUserAllMap() {
		Map<String,String> map = userInInterService.getUserMap();
		String jsonString = JSONUtils.toJson(map);
		WebUtils.webSendJSON(jsonString);
	}
	
	/**  
	 *  
	 * @Description：  查询用户信息
	 * @Author：zxl
	 * @CreateDate：2015-6-3 下午05:11:17  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-3 下午05:11:17  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "updateUser")
	public void updateUser() {
		Map<String,String> map = userInInterService.getUserMap();
		try {
			moOperateLogService.updateUser(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MOCZRZ", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("MOCZRZ","操作日志","2","1"), e);
		}
	}

	public MSysOperateLog getSysOperateLog() {
		return sysOperateLog;
	}

	public void setSysOperateLog(MSysOperateLog sysOperateLog) {
		this.sysOperateLog = sysOperateLog;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
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

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
