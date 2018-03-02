package cn.honry.outpatient.webPreregister.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.Tab;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.outpatient.webPreregister.service.WebPreregisterService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 网络预约挂号(对外)
 * @author user
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/outpatient/webPreregister")
public class WebPreregisterAction extends ActionSupport {
	
	// 记录异常日志
	private Logger logger = Logger.getLogger(WebPreregisterAction.class);
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();

	@Autowired
	@Qualifier(value = "webPreregisterService")
	private WebPreregisterService webPreregisterService;

	public void setWebPreregisterService(WebPreregisterService webPreregisterService) {
		this.webPreregisterService = webPreregisterService;
	}
	
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	
	private int page;//页码
	private int rows;//每页记录数

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
	
	
	private RegisterPreregisterNow t;
	
	public RegisterPreregisterNow getT() {
		return t;
	}
	public void setT(RegisterPreregisterNow t) {
		this.t = t;
	}
	
	@Action(value = "getRegisterView", results = { @Result(name = "list", location = "/WEB-INF/pages/register/webPreregister/webPreregister.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String getRegisterView(){
		try {
			String dayShu = parameterInnerService.getparameter("webPreregisterDay");
			if(StringUtils.isNotBlank(dayShu)){
				Integer dayS = Integer.parseInt(dayShu);
				List<Tab> dayXxList = new ArrayList<Tab>();
				for (int i = 1; i <= dayS; i++) {
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
				logger.error("GHGL_WLYYGH", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_WLYYGH", "挂号管理_网络预约挂号", "2", "0"), e);
		}
		return "list";
	}
	
	@Action(value = "registerAdd", results = { @Result(name = "add", location = "/WEB-INF/pages/register/webPreregister/registerEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String registerAdd(){
		return "add";
	}
	
	/**
	 * 获取门诊下的挂号科室
	 */
	@Action(value="getDeptTree")
	public void getDeptTree(){
		String json;
		try {
			List<TreeJson> list = deptInInterService.QueryTreeDepartmen(false, "C",null,null);
			json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
		  	e.printStackTrace();
			logger.error("GHGL_WLYYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_WLYYGH", "挂号管理_网络预约挂号", "2", "0"), e);
		}
		
	}
	
	
	/**
	 * 获取医生预约信息
	 */
	@Action(value="getList")
	public void getList(){
		try {
			String deptId = request.getParameter("deptId");
			String rq=request.getParameter("rq");
			List<RegisterScheduleNow> list=null;
			if(StringUtils.isNotBlank(deptId) && StringUtils.isNotBlank(rq)){
				SysDepartment dept = deptInInterService.get(deptId);
				String deptCode = dept.getDeptCode();
				int firstResult=(page-1)*rows;
				list = webPreregisterService.getRegisterList(deptCode,rq, firstResult, rows);
				
			}
			if(list==null){
				list=new ArrayList<RegisterScheduleNow>();
			}
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
		  	e.printStackTrace();
			logger.error("GHGL_WLYYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_WLYYGH", "挂号管理_网络预约挂号", "2", "0"), e);
		}
	}
	
	/**
	 * 预约挂号
	 */
	@Action(value="savePreregister")
	public void savePreregister(){
		try {
			String flag="-1";
			if(StringUtils.isNotBlank(t.getPreregisterDeptname())  && 
					StringUtils.isNotBlank(t.getPreregisterDept()) &&
					StringUtils.isNotBlank(t.getPreregisterExpert())&& t.getMidday()!=null ){
				t.setPreregisterIsnet(1);
				t.setCreateTime(new Date());
				String preNo = webPreregisterService.getPreNo();
				t.setPreregisterNo(preNo);
				flag = webPreregisterService.savePreregister(t);
			}
			WebUtils.webSendString(flag);
		} catch (Exception e) {
		  	e.printStackTrace();
			logger.error("GHGL_WLYYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_WLYYGH", "挂号管理_网络预约挂号", "2", "0"), e);
		}
	}
}
