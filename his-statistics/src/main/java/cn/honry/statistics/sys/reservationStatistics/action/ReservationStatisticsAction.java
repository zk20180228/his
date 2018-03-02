package cn.honry.statistics.sys.reservationStatistics.action;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.statistics.sys.reservationStatistics.service.ReservationStatisticsService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/ReservationStatistics")
@SuppressWarnings({"all"})
public class ReservationStatisticsAction extends ActionSupport implements ModelDriven<RegisterPreregister>{
	
	private static final long serialVersionUID = 1L;
	@Override
	public RegisterPreregister getModel() {
		return register;
	}
	// 记录异常日志
	private Logger logger = Logger.getLogger(ReservationStatisticsAction.class);
	
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	@Autowired
	@Qualifier(value = "reservationStatisticsService")
	private ReservationStatisticsService reservationStatisticsService;
	private RegisterPreregister register=new RegisterPreregister();
	public void setReservationStatisticsService(
			ReservationStatisticsService reservationStatisticsService) {
		this.reservationStatisticsService = reservationStatisticsService;
	}
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	private String Etime;
	private String Stime;
	private String page;
	private String rows;
	
	
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
	public String getEtime() {
		return Etime;
	}
	public void setEtime(String etime) {
		Etime = etime;
	}
	public String getStime() {
		return Stime;
	}
	public void setStime(String stime) {
		Stime = stime;
	}
	
	/**  
	 *  
	 * @Description：  预约统计列表
	 * @Author：wujiao
	 * @CreateDate：2015-12-24 下午10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"YYTJ:function:view"}) 
	@Action(value = "listReservationCount", results = { @Result(name = "list", location = "/WEB-INF/pages/sys/reportForms/reservationStatistics.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listReservationCount() {
		
		Date date = new Date();
		Etime = DateUtils.formatDateY_M_D(date);
		Stime = DateUtils.formatDateYM(date)+"-01";
		
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  预约统计查询
	 * @Author：wujiao
	 * @CreateDate：2015-12-24 下午10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"KSGZLTJ:function:query"}) 
	@Action(value = "listCountqueryReservaion", results = { @Result(name = "json", type = "json") })
	public void listCountqueryReservaion() {
		
		Map<String,Object> map =null;
		try {
			String Stime = request.getParameter("Stime");
			String Etime = request.getParameter("Etime");
			String dept = request.getParameter("dept");
			if(StringUtils.isBlank(dept)||"all".equals(dept)){
				List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
				if(deptList != null && deptList.size() < 900){
					dept = "";
					for (SysDepartment deptCode : deptList) {
						if(StringUtils.isNotBlank(dept)){
							dept += "," ;
						}
						dept += deptCode.getDeptCode(); 
					}
				}
			}
			if(StringUtils.isNotBlank(dept)){
				if(StringUtils.isBlank(Etime)&&StringUtils.isBlank(Stime)){
					Date date = new Date();
					Etime = DateUtils.formatDateY_M_D(date);
					Stime = DateUtils.formatDateYM(date)+"-01";
				}
				String jobNo = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
				map = reservationStatisticsService.getInfoNow(dept,Stime,Etime,page,rows,jobNo,menuAlias);
			}else{
				map = new HashMap<String,Object>();
				map.put("rows", new ArrayList());
				map.put("total", 0);
			}
			String json = JSONUtils.toJson(map);
			
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			//发生异常后，响应空内容
			map = new HashMap<String,Object>();
			map.put("rows", new ArrayList());
			map.put("total", 0);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
			e.printStackTrace();
			logger.error("TJFXGL_YYTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_YYTJ", "门诊统计分析_预约统计", "2", "0"), e);
		}
		
	}

	/**  
	 *  
	 * @Description：  科室下拉框(挂号科室)
	 * @Author：wujiao
	 * @CreateDate：2015-12-25 下午05:11:49  
	 * @Modifier：aizhonghua,zhangkui
	 * @ModifyDate：2015-12-25 下午05:11:49,2017-07-05 
	 * @ModifyRmk：  2017-07-05:已经不再使用的方法了，科室下拉框的url是："<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes="
	 * @version 1.0
	 *
	 */
	@Deprecated
	@Action(value = "departmentComboboxRegister", results = { @Result(name = "json", type = "json") })
	public void departmentComboboxRegister() {
		List<SysDepartment> deptList = null;
		try {
			deptList = reservationStatisticsService.getComboboxdept();
			String json = JSONUtils.toJson(deptList);
			
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			deptList=new ArrayList<SysDepartment>();
			String json = JSONUtils.toJson(deptList);
			
			WebUtils.webSendJSON(json);
			
			e.printStackTrace();
			logger.error("TJFXGL_YYTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_YYTJ", "门诊统计分析_预约统计", "2", "0"), e);
		}
		
	}

}
