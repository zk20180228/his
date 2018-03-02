 package cn.honry.outpatient.scheduleModel.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
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
import cn.honry.base.bean.model.RegisterSchedulemodel;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.clinic.service.ClinicInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.outpatient.info.service.RegisterInfoService;
import cn.honry.outpatient.scheduleModel.service.ScheduleModelService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 *  
 * @className：ScheduleModelAction 
 * @Description：    排班模板action
 * @Author：aizhonghua
 * @CreateDate：2015-6-3 下午05:12:55  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-3 下午05:12:55  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/register")
@Namespace(value = "/outpatient/scheduleModel")
public class ScheduleModelAction extends ActionSupport implements ModelDriven<RegisterSchedulemodel>{

	private Logger logger=Logger.getLogger(ScheduleModelAction.class);
	
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value = "scheduleModelService")
	private ScheduleModelService scheduleModelService;
	public void setScheduleModelService(ScheduleModelService scheduleModelService) {
		this.scheduleModelService = scheduleModelService;
	}
	@Autowired
	@Qualifier(value = "registerInfoService")
	private RegisterInfoService registerInfoService;
	public void setRegisterInfoService(RegisterInfoService registerInfoService) {
		this.registerInfoService = registerInfoService;
	}
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	@Autowired
	@Qualifier(value = "clinicInInterService")
	private ClinicInInterService clinicInInterService;
	public void setClinicInInterService(ClinicInInterService clinicInInterService) {
		this.clinicInInterService = clinicInInterService;
	}
	private RegisterSchedulemodel model = new RegisterSchedulemodel();
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	/**
	 *  排班模板List
	 */
	private List<RegisterSchedulemodel> schedulemodelList;
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	
	/**
	 * 部门Id
	 */
	private String deptId;
	
	/**
	 * 部门名称
	 */
	private String deptName;
	
	/**
	 * 星期
	 */
	private Integer week;
	
	/**
	 * 及时查询参数
	 */
	private String q;
	
	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	/**  
	 *  
	 * @Description：  获取list页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-3 下午05:13:07  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-3 下午05:13:07  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHPBMBGL:function:view"})
	@Action(value = "listSchedulemodel", results = { @Result(name = "list", location = "/WEB-INF/pages/register/scheduleModel/scheduleModelList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listSchedulemodel() {
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  查询列表
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-3 下午05:13:16  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-3 下午05:13:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHPBMBGL:function:query"})
	@Action(value = "querySchedulemodel", results = { @Result(name = "json", type = "json") })
	public void querySchedulemodel() {
		try {
			String deptId = ServletActionContext.getRequest().getParameter("deptId");
			String week = ServletActionContext.getRequest().getParameter("week");
			int total = 0;
			if(StringUtils.isBlank(deptId)||StringUtils.isBlank(week)){
				schedulemodelList = new ArrayList<RegisterSchedulemodel>();
			}else{
				SysDepartment dept = deptInInterService.get(deptId);
				model.setDepartment(dept.getDeptCode());
				model.setModelWeek(Integer.parseInt(week));
				model.setSearch(model.getSearch().trim());
				schedulemodelList = scheduleModelService.getPage(request.getParameter("page"),request.getParameter("rows"),model);
				total = scheduleModelService.getTotal(model);
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("total", total);
			map.put("rows", schedulemodelList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：   添加&修改
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-3 下午05:13:26  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-3 下午05:13:26  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "saveSchedulemodel", results = { @Result(name = "json", type = "json") })
	public void saveSchedulemodel()  {
		try {
			if(StringUtils.isNotBlank(model.getId())){
				if(SecurityUtils.getSubject().isPermitted("GHPBMBGL:function:edit")){
					boolean isEdit = scheduleModelService.saveOrUpdateModel(model);
					if(isEdit){
						WebUtils.webSendString("success");
					}else{
						WebUtils.webSendString("exist");
					}
				}else{
					WebUtils.webSendString("error");
				}
			}else{
				if(SecurityUtils.getSubject().isPermitted("GHPBMBGL:function:add")){
					boolean isAdd = scheduleModelService.saveOrUpdateModel(model);
					if(isAdd){
						WebUtils.webSendString("success");
					}else{
						WebUtils.webSendString("exist");
					}
				}else{
					WebUtils.webSendString("error");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  删除
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-3 下午05:13:35  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-3 下午05:13:35  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHPBMBGL:function:delete"})
	@Action(value = "delSchedulemodel", results = { @Result(name = "list", location = "/WEB-INF/pages/register/scheduleModel/scheduleModelList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delSchedulemodel()  {
		try {
			scheduleModelService.del(model.getId());
			WebUtils.webSendString("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  查看
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-17 上午11:56:33  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-17 上午11:56:33  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHPBMBGL:function:view"}) 
	@Action(value = "viewSchedulemodel", results = { @Result(name = "view", location = "/WEB-INF/pages/register/scheduleModel/scheduleModelView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewSchedulemodel() {
		try {
			Map<String,String> gradeMap = registerInfoService.getGradeMap();
			Map<String,String> empMap = employeeInInterService.queryEmpCodeAndNameMap();
			Map<String,String> deptMap = deptInInterService.querydeptCodeAndNameMap();
			Map<String,String> cliMap = clinicInInterService.queryClinicIdAndNameMap();
			request.setAttribute("gradeMap", gradeMap);
			request.setAttribute("empMap", empMap);
			request.setAttribute("deptMap", deptMap);
			request.setAttribute("cliMap", cliMap);
			request.setAttribute("model", scheduleModelService.get(model.getId()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
		return "view";
	}
	/**  
	 *  
	 * @Description： 添加
	 * @Author：lhl
	 * @CreateDate：2015-10-13  下午14:06:16
	 * @Modifier：wanxing
	 * @ModifyDate：2016-03-30 上午10:56:55    
	 * @ModifyRmk：添加部门名称参数  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"GHPBMBGL:function:add"}) 
	@Action(value = "schedulemodelAdd", results = { @Result(name = "add", location = "/WEB-INF/pages/register/scheduleModel/scheduleModelEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String schedulemodelAdd() {
		try {
			String deptId = request.getParameter("deptId");
			SysDepartment dept = deptInInterService.get(deptId);
			String week = request.getParameter("week");
			if(StringUtils.isNotBlank(week)){
				model.setModelWeek(Integer.valueOf(week));
			}
			request.setAttribute("dept", dept);
			request.setAttribute("workdept", dept);
			request.setAttribute("model", model);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
		return "add";
	}
	
	/**  
	 *  
	 * @Description： 跳转修改
	 * @Author：lhl
	 * @CreateDate：2015-10-13  下午14:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"GHPBMBGL:function:edit"}) 
	@Action(value = "schedulemodelEdit", results = { @Result(name = "edit", location = "/WEB-INF/pages/register/scheduleModel/scheduleModelEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String schedulemodelEdit() {
		try {
			model = scheduleModelService.get(model.getId());
			SysDepartment dept = deptInInterService.getByCode(model.getDepartment());
			SysDepartment workdept = deptInInterService.getByCode(model.getModelWorkdept());
			if(StringUtils.isNotEmpty(model.getModelRemark())){
				String remark = "";
				remark = model.getModelRemark().replaceAll("</br>","\r\n");
				model.setModelRemark(remark);
			}
			request.setAttribute("model", model);
			request.setAttribute("dept", dept);
			request.setAttribute("workdept", workdept);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
		return "edit";
	}
	
	/**  
	 *  
	 * @Description：  查询某科室下某星期的全部排班模板
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-19 上午09:05:02  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-19 上午09:05:02  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "getSchedulemodel", results = { @Result(name = "json", type = "json") })
	public void getSchedulemodel() {
		try {
			schedulemodelList = new ArrayList<RegisterSchedulemodel>();
			String deptId = ServletActionContext.getRequest().getParameter("deptId");
			String dateTime = ServletActionContext.getRequest().getParameter("dateTime");
			int total=0;
			if(StringUtils.isNotBlank(deptId)&&StringUtils.isNotBlank(dateTime)){
				SysDepartment dept = deptInInterService.get(deptId);
				model.setDepartment(dept.getDeptCode());
				model.setModelWeek(DateUtils.getWeekOfDay(dateTime));
				schedulemodelList = scheduleModelService.getPage(request.getParameter("page"),request.getParameter("rows"),model);
				total = scheduleModelService.getTotal(model);
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("total", total);
			map.put("rows", schedulemodelList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description： 查询科室树
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-19 上午09:05:02  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-19 上午09:05:02  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "treeDeptSchedule", results = { @Result(name = "json", type = "json") })
	public void treeDepartmen() {
		try {
			List<TreeJson> treeDepar =  deptInInterService.QueryTreeDepartmen(true, null,null,null);
			String json = JSONUtils.toJson(treeDepar);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  查询科室下的员工
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-19 上午09:05:02  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-19 上午09:05:02  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "getEmployeeByDeptId", results = { @Result(name = "json", type = "json") })
	public void getEmployeeByDeptId() {
		try {
			List<SysEmployee> treeDepar =  scheduleModelService.getEmployeeByDeptId(model);
			String json = JSONUtils.toJson(treeDepar);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  跳转到模板页面
	 * @Author：huangbiao
	 * @CreateDate：2016-3-26上午09:55
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "scheduleModelTemp", results = { @Result(name = "ModelTemp", location = "/WEB-INF/pages/register/scheduleModel/scheduleModelTemp.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String scheduleTemp() {
		String deptId = request.getParameter("deptId");
		String weeken = request.getParameter("weeken");
		request.setAttribute("deptId", deptId);
		request.setAttribute("weeken", weeken);
		
		return "ModelTemp";
	}
	
	/**
	 * @Description 挂号排版模板的导入
	 * @Author huangbiao
	 * @CreateDate 2016年3月26日
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk
	 * @version 1.0
	 */
	@Action(value = "schedulModelAddTemp", results = { @Result(name = "json", type = "json") })
	public void schedulModelAddTemp()  {
		String deptId = request.getParameter("deptId");
		String nowWeekStr = request.getParameter("nowWeek");
		String weekStr = request.getParameter("week");
		int nowWeek = Integer.parseInt(nowWeekStr);
		int week = Integer.parseInt(weekStr);
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			map = scheduleModelService.schedulModelAddTemp(model.getId(), deptId, nowWeek, week);
		} catch (Exception e) {
			map.put("resMsg", "error");
			e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	@Action(value = "queryEmpCodeAndNameMap")
	public void queryEmpCodeAndNameMap(){
		try {
			Map<String,String> map = employeeInInterService.queryEmpCodeAndNameMap();
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
	}
	
	@Action(value = "querydeptCodeAndNameMap")
	public void querydeptCodeAndNameMap(){
		try {
			Map<String,String> map = deptInInterService.querydeptCodeAndNameMap();
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
	}
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	@Override
	public RegisterSchedulemodel getModel() {
		return model;
	}

	
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptId() {
		return deptId;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		String deptNameTmp ="";
		try {
			deptNameTmp= URLDecoder.decode(ServletActionContext.getRequest().getParameter("deptName"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
		this.deptName = deptNameTmp;
	}	
	
	/**
	 * 科室及时查询
	 */
	@Action(value = "getDeptByQ")
	public void getDeptByQ(){
		if(StringUtils.isNotBlank(q)){
			List<SysDepartment> list = deptInInterService.getDeptByQ(q);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		}
	}
	
}
