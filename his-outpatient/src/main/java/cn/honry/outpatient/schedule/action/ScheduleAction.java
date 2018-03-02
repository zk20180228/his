package cn.honry.outpatient.schedule.action;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.Tab;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.clinic.service.ClinicInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.outpatient.info.service.RegisterInfoService;
import cn.honry.outpatient.schedule.service.ScheduleService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 *  
 * @className：ScheduleAction 
 * @Description：  排班信息action
 * @Author：aizhonghua
 * @CreateDate：2015-6-3 下午05:11:17  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-3 下午05:11:17  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/register")
@Namespace(value = "/outpatient/schedule")
public class ScheduleAction extends ActionSupport implements ModelDriven<RegisterScheduleNow>{
	
	private Logger logger=Logger.getLogger(ScheduleAction.class);
	
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Override
	public RegisterScheduleNow getModel() {
		return schedule;
	}
	// 排班
	RegisterScheduleNow schedule = new RegisterScheduleNow();
	// 排班List
	private List<RegisterScheduleNow> scheduleList;
	private HttpServletRequest request = ServletActionContext.getRequest();
	@Autowired
	@Qualifier(value = "scheduleService")
	private ScheduleService scheduleService;
	public void setScheduleService(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
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
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	//部门编号
	private String deptId;
	//模板日期
	private String mdateTime;
	//当前日期
	private String dateTime;
	
	
	
	
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getMdateTime() {
		return mdateTime;
	}
	public void setMdateTime(String mdateTime) {
		this.mdateTime = mdateTime;
	}
	/**  
	 *  
	 * @Description：  获取list页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-3 下午05:11:49  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-3 下午05:11:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHPBGL:function:view"})
	@Action(value = "listSchedule", results = { @Result(name = "list", location = "/WEB-INF/pages/register/schedule/scheduleList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listSchedule() {
		try {
			String dayShu = parameterInnerService.getparameter(HisParameters.SCHEDULEDAY);
			if(StringUtils.isNotBlank(dayShu)){
				Integer dayS = Integer.parseInt(dayShu);
				List<Tab> dayXxList = new ArrayList<Tab>();
				for (int i = 0; i < dayS; i++) {
					Date date = DateUtils.addDay(DateUtils.getCurrentTime(), i);
					String xDt = DateUtils.formatDateNYR(date);//得到年月日格式的日期  用于显示
					String d = DateUtils.formatDateY_M_D(date);//得到-格式的日期  用于得到星期
					Tab tab = new Tab();
					tab.setId(d);
					tab.setName(xDt+"<br>"+DateUtils.getCNWeekOfDay(d));
					dayXxList.add(tab);
				}
				request.setAttribute("dayXxList", dayXxList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBGL", "挂号排班管理_挂号排班管理", "2", "0"), e);
		}
		
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  查询list列表
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-3 下午05:12:01  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-3 下午05:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHPBGL:function:query"})
	@Action(value = "querySchedule", results = { @Result(name = "json", type = "json") })
	public void querySchedule() {
		String deptId = request.getParameter("deptId");
		String dateTime = request.getParameter("dateTime");
		int total = 0;
		if(StringUtils.isBlank(deptId.trim())||StringUtils.isBlank(dateTime.trim())){//如果没有选择明确的部门 则不去数据库中查询
			scheduleList = new ArrayList<RegisterScheduleNow>();
		}else{
			SysDepartment dept = deptInInterService.get(deptId);
			schedule.setDepartment(dept.getDeptCode());
			SimpleDateFormat df = new SimpleDateFormat();
			df.applyPattern("yyyy-MM-dd");
			try {
				schedule.setDate(df.parse(dateTime.trim()));
				schedule.setSearch(schedule.getSearch().trim());
				scheduleList = scheduleService.getPage(request.getParameter("page"),request.getParameter("rows"),schedule);
				total = scheduleService.getTotal(schedule);
			} catch (ParseException e) {
				scheduleList = new ArrayList<RegisterScheduleNow>();
				e.printStackTrace();
				logger.error("GHPBGL_GHPBGL", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBGL", "挂号排班管理_挂号排班管理", "2", "0"), e);
			}
		}
		Map<String,Object> map =new HashMap<>();
		map.put("total", total);
		map.put("rows", scheduleList);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  添加&修改
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-3 下午05:12:16  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-3 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "scheduleSave", results = { @Result(name = "json", type = "json") })
	public void scheduleSave()  {
		try {
			if(StringUtils.isNotBlank(schedule.getId())){
				if(SecurityUtils.getSubject().isPermitted("GHPBGL:function:edit")){
					boolean isEdit = scheduleService.saveOrUpdateSchedule(schedule);
					if(isEdit){
						WebUtils.webSendString("success");
					}else{
						WebUtils.webSendString("exist");
					}
				}else{
					WebUtils.webSendString("error");
				}
			}else{
				if(SecurityUtils.getSubject().isPermitted("GHPBGL:function:add")){
					boolean isAdd = scheduleService.saveOrUpdateSchedule(schedule);
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
			logger.error("GHPBGL_GHPBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBGL", "挂号排班管理_挂号排班管理", "2", "0"), e);
		}
	}
	
	
	/**  
	 *  
	 * @Description：  删除
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-3 下午05:12:30  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-3 下午05:12:30  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHPBGL:function:delete"})
	@Action(value = "delSchedul", results = { @Result(name = "list", location = "/WEB-INF/pages/register/schedule/scheduleList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delSchedul()  {
		try{
			String info= scheduleService.del(schedule.getId());
			WebUtils.webSendString(info);
		}catch(Exception e){
			WebUtils.webSendString("error");
			e.printStackTrace();
			logger.error("GHPBGL_GHPBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBGL", "挂号排班管理_挂号排班管理", "2", "0"), e);
		}
		return "list";
	}
	
	@RequiresPermissions(value={"GHPBGL:function:view"}) 
	@Action(value = "viewSchedule", results = { @Result(name = "view", location = "/WEB-INF/pages/register/schedule/scheduleView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewSchedule() {
		
		try {
			//工作科室没有冗余字段     挂号级别没有接口
			Map<String,String> gradeMap = registerInfoService.getGradeMap();
			Map<String,String> deptMap = deptInInterService.querydeptCodeAndNameMap();
			request.setAttribute("deptMap", deptMap);
			request.setAttribute("gradeMap", gradeMap);
			request.setAttribute("schedule", scheduleService.get(schedule.getId()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBGL", "挂号排班管理_挂号排班管理", "2", "0"), e);
		}
		return "view";
	}
	
	public String middyParameter(){
		String json=null;
			try {
				//从参数表中获取午别参数
					List<HospitalParameter> middyParameter = parameterInnerService.getMiddyParameter(HisParameters.WORKTIME);
					Map<String,String> mpMap = new HashMap<String,String>();
					for(HospitalParameter hp : middyParameter){
						String middyNum = hp.getParameterValue();
						String parameterTime = hp.getParameterDownlimit()+","+hp.getParameterUplimit();
						
						switch (middyNum) {
						case "1":
							mpMap.put("1", parameterTime);
							break;
						case "2":
							mpMap.put("2", parameterTime);
							break;
						case "3":
							mpMap.put("3", parameterTime);
							break;
						default:
							break;
						}
					}
					json = JSONUtils.toJson(mpMap);
					
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("GHPBGL_GHPBGL", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBGL", "挂号排班管理_挂号排班管理", "2", "0"), e);
			}
			return json;
	}
	
	/**  
	 *  
	 * @Description： 跳转到添加页面 
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-18 下午02:38:52  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-18 下午02:38:52  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHPBGL:function:add"}) 
	@Action(value = "scheduleAdd", results = { @Result(name = "add", location = "/WEB-INF/pages/register/schedule/scheduleEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String scheduleAdd() {
		try {
			String deptId = request.getParameter("deptId");
			SysDepartment dept = deptInInterService.get(deptId);
			String dateTime = request.getParameter("dateTime");
			schedule.setDate(DateUtils.parseDateY_M_D(dateTime));
			request.setAttribute("dept", dept);
			request.setAttribute("workdept", dept);
			request.setAttribute("schedule", schedule);
			request.setAttribute("dayParam", parameterInnerService.getparameter(HisParameters.SCHEDULEDAY));
			
			String json = middyParameter();
			ServletActionContext.getRequest().setAttribute("mpMap", json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBGL", "挂号排班管理_挂号排班管理", "2", "0"), e);
		}
		return "add";
	}
	
	/**  
	 *  
	 * @Description：  跳转到修改页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-18 下午02:38:32  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-18 下午02:38:32  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHPBGL:function:edit"}) 
	@Action(value = "scheduleEdit", results = { @Result(name = "edit", location = "/WEB-INF/pages/register/schedule/scheduleEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String scheduleEdit() {
		try {
			schedule = scheduleService.get(schedule.getId());
			SysDepartment dept = deptInInterService.getByCode(schedule.getDepartment());
			SysDepartment workdept = deptInInterService.getByCode(schedule.getScheduleWorkdept());
			if(StringUtils.isNotEmpty(schedule.getStoprEason())){
				String stopreason = "";
				stopreason = schedule.getStoprEason().replaceAll("</br>","\r\n");
				schedule.setStoprEason(stopreason);
			}
			if(StringUtils.isNotEmpty(schedule.getRemark())){
				String remark = "";
				remark = schedule.getRemark().replaceAll("</br>","\r\n");
				schedule.setRemark(remark);
			}
			request.setAttribute("schedule", schedule);
			request.setAttribute("dept", dept);
			request.setAttribute("workdept", workdept);
			request.setAttribute("dayParam", parameterInnerService.getparameter(HisParameters.SCHEDULEDAY));
			
			String json = middyParameter();
			ServletActionContext.getRequest().setAttribute("mpMap", json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBGL", "挂号排班管理_挂号排班管理", "2", "0"), e);
		}
		return "edit";
	}
	
	/**  
	 *  
	 * @Description：  排班同步
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-3 下午05:12:16  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-3 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "schedulSynch", results = { @Result(name = "json", type = "json") })
	public void schedulSynch() {
		try {
			String deptId = request.getParameter("deptId");
			Map<String,Object> map=new HashMap<String,Object>();
			try {
				int retVal = scheduleService.schedulSynch(deptId);
				if(retVal>0){
					map.put("resCode", retVal);
					map.put("resMsg", "success");
				}else{
					map.put("resCode", 0);
					map.put("resMsg", "notsynch");
				}
			} catch (Exception e) {
				map.put("resMsg", "error");
				logger.error("GHPBGL_GHPBGL", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBGL", "挂号排班管理_挂号排班管理", "2", "0"), e);
			}
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBGL", "挂号排班管理_挂号排班管理", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  跳转到模板页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-18 下午02:38:32  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-18 下午02:38:32  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "scheduleTemp", results = { @Result(name = "Temp", location = "/WEB-INF/pages/register/schedule/scheduleTemp.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String scheduleTemp() {
		try {
			String deptId = request.getParameter("deptId");
			String dateTime = request.getParameter("dateTime");
			request.setAttribute("deptId", deptId);
			request.setAttribute("dateTime", dateTime);
			String dayShu = parameterInnerService.getparameter(HisParameters.SCHEDULEDAY);
			if(StringUtils.isNotBlank(dayShu)){
				Integer dayS = Integer.parseInt(dayShu);
				List<Tab> dayXxList = new ArrayList<Tab>();
				for (int i = 0; i < dayS; i++) {
					Date date = DateUtils.addDay(DateUtils.getCurrentTime(), i);
					String xDt = DateUtils.formatDateNYR(date);//得到年月日格式的日期  用于显示
					String d = DateUtils.formatDateY_M_D(date);//得到-格式的日期  用于得到星期
					Tab tab = new Tab();
					tab.setId("1"+d);
					tab.setName(xDt+"<br>"+DateUtils.getCNWeekOfDay(d));
					dayXxList.add(tab);
				}
				request.setAttribute("dayXxList", dayXxList);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBGL", "挂号排班管理_挂号排班管理", "2", "0"), e);
		}
		
		return "Temp";
	}
	
	/**  
	 *  
	 * @Description：  排班导入
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-3 下午05:12:16  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-3 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "schedulAddTemp", results = { @Result(name = "json", type = "json") })
	public void schedulAddTemp(){
		String deptId = request.getParameter("deptId");
		String dateTime = request.getParameter("dateTime");
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			map = scheduleService.schedulAddTemp(schedule.getId(),deptId,dateTime);
		} catch (Exception e) {
			map.put("resMsg", "error");
			e.printStackTrace();
			logger.error("GHPBGL_GHPBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBGL", "挂号排班管理_挂号排班管理", "2", "0"), e);
		}
		try {
			WebUtils.webSendString(JSONUtils.toJson(map));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBGL", "挂号排班管理_挂号排班管理", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：  排班导入
	 * @Author：tangfeishuai
	 * @CreateDate：2016-3-28 下午02:12:16  
	 * @Modifier：tangfeishuai
	 * @ModifyDate：2016-3-28 下午02:12:16  
	 * @ModifyRmk：  
	 * @version 2.0
	 *
	 */
	@Action(value = "schedulAddTTemp")
	public void schedulAddTTemp()  {
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			map = scheduleService.schedulAddTTemp(schedule.getId(),deptId,dateTime,mdateTime);
			WebUtils.webSendString(JSONUtils.toJson(map));
		} catch (Exception e) {
			map.put("resMsg", "error");
			WebUtils.webSendString(JSONUtils.toJson(map));
			e.printStackTrace();
			logger.error("GHPBGL_GHPBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBGL", "挂号排班管理_挂号排班管理", "2", "0"), e);
		}
		
	}
	
}
