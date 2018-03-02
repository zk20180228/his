package cn.honry.finance.userGroup.action;

import java.io.PrintWriter;
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

import cn.honry.base.bean.model.FinanceUsergroup;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.finance.userGroup.service.UserGroupService;
import cn.honry.finance.userGroup.vo.EmployeeGroupVo;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**  
 *  
 * @className：UserGroupAction
 * @Description：  财务组信息action
 * @Author：kjh
 * @CreateDate：2015-6-12   
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/finance/userGroup")
//@Namespace(value = "/finance")
public class UserGroupAction extends ActionSupport implements ModelDriven<FinanceUsergroup>{
	private Logger logger=Logger.getLogger(UserGroupAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	@Override
	public FinanceUsergroup getModel() {
		return financeUsergroup;
	}
	@Autowired
	@Qualifier(value = "userGroupService")
	private UserGroupService userGroupService;
	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}
    private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	} 
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	
	public EmployeeInInterService getEmployeeInInterService() {
		return employeeInInterService;
	}
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	private SysEmployee employee = new SysEmployee();
	private FinanceUsergroup financeUsergroup;
	
	public FinanceUsergroup getFinanceUsergroup() {
		return financeUsergroup;
	}

	public void setFinanceUsergroup(FinanceUsergroup financeUsergroup) {
		this.financeUsergroup = financeUsergroup;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	/**  
	 *  
	 * @Description：  返回到userGroupList页面
	 * @Author：kjh
	 * @CreateDate：2015-6-12 
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"CWFZ:function:view"})
	@Action(value = "queryUserGroupUrl", results = { @Result(name = "list", location = "/WEB-INF/pages/finance/userGroup/userGroupList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryUserGroupUrl(){
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  查询数据
	 * @Author：kjh
	 * @CreateDate：2015-6-12  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryUserGroup")
	public void queryUserGroup(){
		try {
			List<FinanceUsergroup> list = userGroupService.queryUsergroup(financeUsergroup);
			int total = userGroupService.getUsergroupCount(financeUsergroup);
			Map map = new HashMap();
			map.put("total", total);
			map.put("rows", list);
			String json = JSONUtils.toJson(map);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception ex) {
			logger.error("CWGL_CWFZ", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_CWFZ", "财务管理_财务分组", "2", "0"), ex);
		}

	}


	/**  
	 *  
	 * @Description：  获取增加/修改数据
	 * @Author：kjh
	 * @CreateDate：2015-6-12   
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"CWFZ:function:add"})
	@Action(value = "addOrUpdateUserGroupUrl", results = { @Result(name = "saveOrupdate", location = "/WEB-INF/pages/finance/userGroup/userGroupEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addOrUpdateUserGroupUrl(){
		if(financeUsergroup==null){
			financeUsergroup = new FinanceUsergroup();
		}		
		if(StringUtils.isNotBlank(financeUsergroup.getGroupName())){
			String groupName = "";		
			try {
				groupName = java.net.URLDecoder.decode(financeUsergroup.getGroupName(), "UTF-8");
				financeUsergroup = userGroupService.findGroup(groupName);
			} catch (Exception e) {
				logger.error("CWGL_CWFZ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_CWFZ", "财务管理_财务分组", "2", "0"), e);
			}
		}
		return "saveOrupdate";
	}
	
	/**  
	 *  
	 * @Description：  保存增加/修改数据
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-16  
	 * @version 1.0
	 *
	 */
	@Action(value = "saveOrupdateUserGroup", results = { @Result(name = "list", location = "/WEB-INF/pages/finance/userGroup/userGroupList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public 	String  saveOrupdateUserGroup(){
			try {
				String retVal="no";
				//获得员工id
				String ids=request.getParameter("ids");
				if(financeUsergroup==null){
					financeUsergroup = new FinanceUsergroup();
				}
				if(StringUtils.isNotBlank(financeUsergroup.getId())){			
					String oldGroupName=request.getParameter("oldGroupName");
				userGroupService.update(ids,financeUsergroup,oldGroupName);
				}else{			
					userGroupService.save(ids,financeUsergroup);			
				}
			} catch (Exception e) {
				logger.error("CWGL_CWFZ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_CWFZ", "财务管理_财务分组", "2", "0"), e);
			}			
		return "list";
	}

	/**  
	 *  
	 * @Description：   删除数据
	 * @Author：kjh
	 * @CreateDate：2015-6-17 
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"CWFZ:function:delete"})
	@Action(value = "deleteUserGroup")
	public String deleteUserGroup(){
		String retVal="no";
		String ids=request.getParameter("ids");
		try {
			userGroupService.delete(ids);
			retVal="yes";
		} catch (Exception e) {
			retVal="no";
			logger.error("CWGL_CWFZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_CWFZ", "财务管理_财务分组", "2", "0"), e);
		}
		String json = JSONUtils.toJson(retVal);  
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception ex) {
			logger.error("CWGL_CWFZ", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_CWFZ", "财务管理_财务分组", "2", "0"), ex);
			System.out.println(ex.getMessage());
		}
		return null;
		
	}
	/**  
	 *  
	 * @Description：   查看
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-20   
	 * @version 1.0
	 *
	 */
	@Action(value = "viewUserGroup", results = { @Result(name = "view", location = "/WEB-INF/pages/finance/userGroup/userGroupView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewUserGroup(){
		String groupName=null;
		if(StringUtils.isNotBlank(financeUsergroup.getGroupName())){
			try {
				groupName = java.net.URLDecoder.decode(financeUsergroup.getGroupName(), "UTF-8");
				financeUsergroup=userGroupService.findGroup(groupName);
			} catch (Exception e) {
				logger.error("CWGL_CWFZ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_CWFZ", "财务管理_财务分组", "2", "0"), e);
			}			
		}
		return "view";
	}
	/**  
	 *  
	 * @Description：   根据 EMPLOYEE_ID查询员工表的记录
	 * @Author：yeguanqun
	 * @CreateDate：2015-6-17   
	 * @version 1.0
	 *
	 */
	@Action(value = "queryEmployeeGroup")
	public void queryEmployeeGroup(){
		try {
			String groupName = java.net.URLDecoder.decode(financeUsergroup.getGroupName(), "UTF-8");
			List<EmployeeGroupVo> list = userGroupService.queryGroupEmployee(groupName);
			String json = JSONUtils.toJson(list);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			logger.error("CWGL_CWFZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_CWFZ", "财务管理_财务分组", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description： 删除财务组下的员工
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-17   
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"CWFZ:function:delete"})
	@Action(value = "deleteGroupEmployee")
	public String deleteGroupEmployee(){
		String employeeIds = "";
		if(financeUsergroup.getEmployee().getId()!=null){
			employeeIds = financeUsergroup.getEmployee().getId();
		}
		String[] arr = employeeIds.split(",");
		String employeeId = "";
		for(int i=0;i<arr.length;i++){
			if(!"".equals(employeeId)){
				employeeId =employeeId+","+"'"+arr[i]+"'";
			}else{
				employeeId ="'"+arr[i]+"'";
			}			
		}
		String retVal="no";		
		String groupName=userGroupService.get(financeUsergroup.getId()).getGroupName();
		try {
			FinanceUsergroup financeUsergroup1=userGroupService.findGroup(groupName);
			userGroupService.delete(employeeId, groupName);
			List<EmployeeGroupVo> list = userGroupService.queryGroupEmployee(groupName);
			if(list==null||list.size()==0){
				FinanceUsergroup financeUsergroup2 = new FinanceUsergroup();
				if(financeUsergroup1!=null){
					financeUsergroup2.setGroupName(financeUsergroup1.getGroupName());
					if(financeUsergroup1.getGroupInputcode()!=null){
						financeUsergroup2.setGroupInputcode(financeUsergroup1.getGroupInputcode());
					}
					if(financeUsergroup1.getStackRemark()!=null){
						financeUsergroup2.setStackRemark(financeUsergroup1.getStackRemark());
					}
				}				
				userGroupService.save("",financeUsergroup2);
			}
			retVal="yes";
		} catch (Exception e) {
			retVal="no";
			logger.error("CWGL_CWFZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_CWFZ", "财务管理_财务分组", "2", "0"), e);
		}
		String json = JSONUtils.toJson(retVal);  
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception e) {
			logger.error("CWGL_CWFZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_CWFZ", "财务管理_财务分组", "2", "0"), e);
		}
		return null;
	}
	/**  
	 *  
	 * @Description：   判断添加财务组名称是否重复  返回yes代表不存在重复的组名；返回no代表组名和数据库中的组名重复
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-9   
	 * @version 1.0
	 *
	 */
	@Action(value = "queryExistGroupName")
	public void queryExistGroupName(){
		String reVal="no";
		try {
			String groupName = "";
			if(financeUsergroup!=null){
				groupName = java.net.URLDecoder.decode(financeUsergroup.getGroupName(), "UTF-8");
				financeUsergroup.setGroupName(groupName);
			}
			FinanceUsergroup financeUsergroups=userGroupService.findGroup(financeUsergroup.getGroupName());
			if(financeUsergroups.getId()==null || financeUsergroups.getId()==""){
				reVal="yes";
			}
			String json = JSONUtils.toJson(reVal);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			logger.error("CWGL_CWFZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_CWFZ", "财务管理_财务分组", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description： 删除财务组并且删除底下的员工
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-9   
	 * @version 1.0
	 * @throws  
	 *
	 */
	@RequiresPermissions(value={"CWFZ:function:delete"})
	@Action(value = "deleteGroup")
	public String deleteGroup(){
		String groupName = "";
		try{
			if(financeUsergroup!=null){
				groupName = java.net.URLDecoder.decode(financeUsergroup.getGroupName(), "UTF-8");				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		String[] arr = groupName.split(",");
		List<FinanceUsergroup> financeUsergroups = new ArrayList<FinanceUsergroup>();
		for(int i=0;i<arr.length;i++){
			financeUsergroup.setGroupName(arr[i]);
			List<FinanceUsergroup> financeUsergroups1;
			try {
				financeUsergroups1 = userGroupService.findGroupAll(financeUsergroup.getGroupName());
				for(int j=0;j<financeUsergroups1.size();j++){
					financeUsergroups.add(financeUsergroups1.get(j));
				}			
			} catch (Exception ex) {
				logger.error("CWGL_CWFZ", ex);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_CWFZ", "财务管理_财务分组", "2", "0"), ex);
			}
		}		
		String ids="";
		for(FinanceUsergroup financeUsergroup:financeUsergroups){
			ids=ids+financeUsergroup.getId()+",";
		}
		String retVal="no";
		try {
			userGroupService.delete(ids);
			retVal="yes";
		} catch (Exception e) {
			retVal="no";
		}
		String json = JSONUtils.toJson(retVal);  
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception e) {
			logger.error("CWGL_CWFZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_CWFZ", "财务管理_财务分组", "2", "0"), e);
		}
		return null;
	}
	/**
	 * 查询财务部下的具有核算权利的员工
	 * @author  kjh
	 * @date 2015-06-10
	 * @version 1.0
	 */
	@Action(value = "findFinaceEmployee", results = { @Result(name = "json", type = "json") },interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String findykList() {
		List<SysEmployee> employeeList=employeeInInterService.findFianceEmployee(request.getParameter("page"),request.getParameter("rows"),employee);
		int total = employeeInInterService.getFianceEmployeeTotal(employee);
		String json = JSONUtils.toJson(employeeList); 
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write("{\"total\":" + total + ",\"rows\":" + json + "}");
		}
		catch (Exception e) {
			logger.error("CWGL_CWFZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_CWFZ", "财务管理_财务分组", "2", "0"), e);
		}
		return null;
	}
	/**  
	 *  
	 * @Description：  跳转用户树页面
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-15 上午09:39:35  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"CWFZ:function:view"}) 
	@Action(value = "queryRoleUser", results = { @Result(name = "queryRoleUser", location = "/WEB-INF/pages/finance/userGroup/userTree.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryRoleUser() {
		return "queryRoleUser";
	}

}
