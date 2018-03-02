package cn.honry.statistics.deptstat.hospitalday.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import cn.honry.statistics.deptstat.hospitalday.service.HospitaldayService;
import cn.honry.statistics.deptstat.hospitalday.vo.HospitaldayVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/hospitalday")
@SuppressWarnings({"all"})
public class HospitaldayAction extends ActionSupport{
	private String startTime;//开始时间
	private String menuAlias;//权限
	private String endTime;//时间参数
	private String parameter;//扩展参数
	private String rows;
	private String page;
	
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	private Logger logger=Logger.getLogger(HospitaldayAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	@Autowired
	@Qualifier(value = "hospitaldayService")
	private HospitaldayService hospitaldayService;
	public void setHospitaldayService(HospitaldayService hospitaldayService) {
		this.hospitaldayService = hospitaldayService;
	}
	/**
	 * 跳转到页面
	 * @return
	 */
	@Action(value = "listHospitalday", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/hospitalday/hospitaldayList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listHospitalday() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); //得到前一天
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		endTime=df.format(date);
		return "list";
	}
	/**
	 * 跳转到页面到经营日报初始化
	 * @return
	 */
	@Action(value = "hospitaldayInit", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/businessDateInit/hospitaldayInit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String hospitaldayInit() {
//		try {
//			Calendar calendar = Calendar.getInstance();
//			calendar.add(Calendar.DATE, -1); //得到前一天
//			Date date = calendar.getTime();
//			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			endTime=df.format(date);
//			startTime=DateUtils.formatDateY_M(calendar.getTime())+"-01";
//		} catch (Exception e) {
//			logger.error("YDPT_YWSJCSH_JYRBCSH", e);
//			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YDPT_YWSJCSH_JYRBCSH", "移动平台_业务数据初始化_业务数据初始化", "2", "0"), e);
//		}
		return "list";
	}
	/**  
	 * @Description： 查询院区日报
	 * @Author：donghe
	 * @CreateDate：2017-07-25 上午9:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Action(value = "queryHospitaldaylist")
	public void queryHospitaldaylist(){
		try {
			Map<String, Object> map =  hospitaldayService.init_YYMRHZ(null, endTime, null);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch (Exception e) {
			logger.error("YYFX_DRJYSJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YYFX_DRJYSJ", "运营分析_当日运营数据", "2", "0"), e);			
		}
	}
	@Action(value = "hospitalMoreDay")
	public void hospitalMoreDay(){
		try {
			Map<String, Object> map =  hospitaldayService.queryDate(startTime, endTime, rows, page);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch (Exception e) {
			logger.error("YYFX_DRJYSJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YYFX_DRJYSJ", "运营分析_当日运营数据", "2", "0"), e);			
		}
	}
	
}
