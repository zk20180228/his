package cn.honry.outpatient.blacklist.action;

import java.util.List;

import javax.annotation.Resource;

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

import cn.honry.base.bean.model.EmployeeBlacklist;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.outpatient.blacklist.service.BlackService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**   
 *  
 * @className：BlacklistAction
 * @description：  员工黑名单Action
 * @author：wujiao
 * @createDate：2015-6-8 上午09:50:36  
 * @modifier：姓名
 * @modifyDate：2015-6-8 上午09:50:36  
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/register")
@Namespace(value = "/outpatient/blacklist")

public class BlacklistAction extends ActionSupport implements ModelDriven<EmployeeBlacklist>{
	private static final long serialVersionUID = 1L;

	private EmployeeBlacklist blacklist =new EmployeeBlacklist();
	private List<EmployeeBlacklist> employeeBlacklist;
	
	public List<EmployeeBlacklist> getEmployeeBlacklist() {
		return employeeBlacklist;
	}
	public void setEmployeeBlacklist(List<EmployeeBlacklist> employeeBlacklist) {
		this.employeeBlacklist = employeeBlacklist;
	}
	private BlackService blackService;
	// 记录异常日志
	private Logger logger = Logger.getLogger(BlacklistAction.class);
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	@Override
	public EmployeeBlacklist getModel() {
		return blacklist;
	}
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	@Autowired 
	@Qualifier(value = "blackService")
	public void setBlackService(BlackService blackService) {
		this.blackService = blackService;
	}

	private String page;//起始页数
	private String rows;//数据列数
	
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

	/**   
	 *  
	 * @description：  员工黑名单list页面
	 * @author：wujiao
	 * @createDate：2015-6-8 上午09:50:36  
	 * @modifier：姓名
	 * @modifyDate：2015-6-8 上午09:50:36  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"GHYHMD:function:view"}) 
	@Action(value = "listBlack", results = { @Result(name = "list", location = "/WEB-INF/pages/register/blackList/blackList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listBlack() {
		return "list";
	}
	
	/**   
	 *  
	 * @description：  员工黑名单列表信息
	 * @author：wujiao
	 * @createDate：2015-6-8 上午09:50:36  
	 * @modifier：姓名
	 * @modifyDate：2015-6-8 上午09:50:36  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"GHYHMD:function:query"}) 
	@Action(value = "queryBlack", results = { @Result(name = "json", type = "json") })
	public void queryBlack() {
		try {
			SysEmployee  employee  =new  SysEmployee();
			String name=ServletActionContext.getRequest().getParameter("name");
			if(StringUtils.isNotBlank(name)){
				employee.setName(name);
			}
			List<EmployeeBlacklist> employeeBlacklist = blackService.getPage(page, rows, blacklist,employee);
			int total = blackService.getTotal(blacklist,employee);

			String json = JSONUtils.toJson(employeeBlacklist);
			WebUtils.webSendJSON("{\"total\":" + total + ",\"rows\":" + json + "}");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("HMDGL_GHYHMD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("HMDGL_GHYHMD", "黑名单管理_挂号员黑名单", "2", "0"), e);
		}

	}
	
	/**   
	 *  
	 * @description：  员工黑名跳转到添加页面
	 * @author：wujiao
	 * @createDate：2015-6-8 上午09:50:36  
	 * @modifier：姓名
	 * @modifyDate：2015-6-8 上午09:50:36  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"GHYHMD:function:add"}) 
	@Action(value = "addBlack", results = { @Result(name = "add", location = "/WEB-INF/pages/register/blackList/blackEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addBlack() {
		//User user = WebUtils.getSessionUser();
 		ServletActionContext.getRequest().setAttribute("blacklist", blacklist);
 		return "add";
	}
	
	/**   
	 *  
	 * @description：  员工黑名跳转到修改页面
	 * @author：wujiao
	 * @createDate：2015-6-8 上午09:50:36  
	 * @modifier：姓名
	 * @modifyDate：2015-6-8 上午09:50:36  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"GHYHMD:function:edit"}) 
	@Action(value = "editBlack", results = { @Result(name = "editUser", location = "/WEB-INF/pages/register/blackList/blackEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editBlack() {
		try {
			String id=ServletActionContext.getRequest().getParameter("id");
			blacklist = blackService.get(id);
			ServletActionContext.getRequest().setAttribute("blacklist", blacklist);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("HMDGL_GHYHMD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("HMDGL_GHYHMD", "黑名单管理_挂号员黑名单", "2", "0"), e);
		}
		
		return "editUser";
	}
	
	/**   
	 *  
	 * @description：  员工黑名跳转到浏览页面
	 * @author：wujiao
	 * @createDate：2015-6-8 上午09:50:36  
	 * @modifier：姓名
	 * @modifyDate：2015-6-8 上午09:50:36  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"GHYHMD:function:view"}) 
	@Action(value = "viewBlack", results = { @Result(name = "view", location = "/WEB-INF/pages/register/blackList/blackView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewBlack() {
		//User user = WebUtils.getSessionUser();
		try {
			String id=ServletActionContext.getRequest().getParameter("id");
			blacklist = blackService.get(id);
			ServletActionContext.getRequest().setAttribute("blacklist", blacklist);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("HMDGL_GHYHMD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("HMDGL_GHYHMD", "黑名单管理_挂号员黑名单", "2", "0"), e);
		}
		return "view";
	}
	
	/**   
	 *  
	 * @description：  员工黑名跳添加&修改
	 * @author：wujiao
	 * @createDate：2015-6-8 上午09:50:36  
	 * @modifier：姓名
	 * @modifyDate：2015-6-8 上午09:50:36  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "editInfo", results = { @Result(name = "list", location = "/WEB-INF/pages/register/blackList/blackList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editInfo() {
		try {
			blackService.saveOrUpdabalck(blacklist);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("HMDGL_GHYHMD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("HMDGL_GHYHMD", "黑名单管理_挂号员黑名单", "2", "0"), e);
		}
		return "list";
	}
	
	/**   
	 *  
	 * @description：  员工黑名跳删除
	 * @author：wujiao
	 * @createDate：2015-6-8 上午09:50:36  
	 * @modifier：姓名
	 * @modifyDate：2015-6-8 上午09:50:36  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"GHYHMD:function:delete"}) 
	@Action(value = "delBlack", results = { @Result(name = "list", location = "/WEB-INF/pages/register/blackList/blackList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delBlack() {
		try{
			blackService.del(blacklist.getId());
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendString("error");
			e.printStackTrace();
			logger.error("HMDGL_GHYHMD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("HMDGL_GHYHMD", "黑名单管理_挂号员黑名单", "2", "0"), e);
		}
		return "list";
	}
	/**
	 * @Description：   人员下拉框的方法
	 * @Author：lyy
	 * @CreateDate：2015-11-23 下午02:01:36  
	 * @Modifier：lyy
	 * @ModifyDate：2015-11-23 下午02:01:36  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "empCombo", results = { @Result(name = "json", type = "json") })
	public void empCombo() {
		try {
		String name=ServletActionContext.getRequest().getParameter("q");
	    List<SysEmployee>  employeeList = blackService.queryEmpName(name);
		String string = JSONUtils.toExclusionJson(employeeList, false, "yyyy-MM-dd", "hospitalId","userId","deptId");
		WebUtils.webSendJSON(string);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("HMDGL_GHYHMD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("HMDGL_GHYHMD", "黑名单管理_挂号员黑名单", "2", "0"), e);
		}

	}
}
