package cn.honry.outpatient.registerDeptManage.action;

import java.util.List;

import javax.annotation.Resource;
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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.outpatient.recipedetail.action.RecipedetailAction;
import cn.honry.outpatient.registerDeptManage.service.RegisterDeptManageService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 *  
 * @Description：  栏目功能
 * @Author：zpty
 * @CreateDate：2015-11-16 
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/register/registerDeptManage")
@Namespace(value = "/outpatient/registerDeptManage")
public class RegisterDeptManageAction extends ActionSupport implements ModelDriven<SysDepartment>{

	/**
	 * 栏目功能
	 */
	private static final long serialVersionUID = 1L;
	// 记录异常日志
	private Logger logger = Logger.getLogger(RecipedetailAction.class);
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Override
	public SysDepartment getModel() {
		return sysDepartment;
	}

	private SysDepartment sysDepartment = new SysDepartment();

	public SysDepartment getSysDepartment() {
		return sysDepartment;
	}

	public void setSysDepartment(SysDepartment sysDepartment) {
		this.sysDepartment = sysDepartment;
	}
	
	private RegisterDeptManageService registerDeptManageService;
	@Autowired
	@Qualifier(value = "registerDeptManageService")
	public void setRegisterDeptManageService(RegisterDeptManageService registerDeptManageService) {
		this.registerDeptManageService = registerDeptManageService;
	}

	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private List<SysDepartment> sysDepartmentList;
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 栏目功能
	 * @author  zpty
	 * @date 2015-11-16 14:00
	 * @version 1.0
	 */
	@RequiresPermissions(value={"GHKSGL:function:view"}) 
	@Action(value = "listSysDepartment", results = { @Result(name = "list", location = "/WEB-INF/pages/register/registerDeptManage/registerDeptManageList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listSysDepartment() {
		return "list";
	}
	/**
	 * 栏目功能列表
	 * @author  zpty
	 * @date 2015-11-16 14:00
	 * @version 1.0
	 */
	@RequiresPermissions(value={"GHKSGL:function:query"}) 
	@Action(value = "querySysDepartment", results = { @Result(name = "json", type = "json") })
	public void querySysDepartment() {
		try {
			int total = registerDeptManageService.getTotal(sysDepartment);
			sysDepartmentList = registerDeptManageService.DepartmentList(request.getParameter("page"),request.getParameter("rows"),sysDepartment);
			String json = JSONUtils.toJson(sysDepartmentList);  
	
			WebUtils.webSendString("{\"total\":" + total + ",\"rows\":" + json + "}");
			
		}catch (Exception e) {
		    e.printStackTrace();
			logger.error("GHKSGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHKSGL", "挂号科室管理", "2", "0"), e);
		}
	}

	/**  
	 *  
	 * @Description：  查看
	 * @Author：zpty
	 * @CreateDate：2015-11-16 14:00  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "viewSysDepartment", results = { @Result(name = "view", location = "/WEB-INF/pages/register/registerDeptManage/registerDeptManageView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewSysDepartment() {
		try {
			String id = ServletActionContext.getRequest().getParameter("id");
			sysDepartment = registerDeptManageService.get(id);
			ServletActionContext.getRequest().setAttribute("sysDepartment", sysDepartment);
		} catch (Exception e) {
		    e.printStackTrace();
			logger.error("GHKSGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHKSGL", "挂号科室管理", "2", "0"), e);
		}
		return "view";
	}
	
	/**  
	 *  
	 * @Description：上移下移
	 * @Author：zpty
	 * @CreateDate：2015-11-16 上午5:30 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value="editOrders",results={@Result(name = "editOrder", type = "json") })
	public void editOrders(){
		try {
			String currentId=ServletActionContext.getRequest().getParameter("currentId");
			String otherId=ServletActionContext.getRequest().getParameter("otherId");
			int flag=0;
			if(StringUtils.isNotBlank(currentId)&&StringUtils.isNotBlank(otherId)){
				try {
					registerDeptManageService.editOrder(currentId, otherId);
					flag=1;
				} catch (Exception e) {
					flag=0;
					e.printStackTrace();
					logger.error("GHKSGL", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHKSGL", "挂号科室管理", "2", "0"), e);

				}
			}
			String json = JSONUtils.toJson(flag);
			WebUtils.webSendString(json);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("GHKSGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHKSGL", "挂号科室管理", "2", "0"), e);

		}
	}
	

}
