package cn.honry.statistics.deptstat.deptSupervision.action;

import java.util.Date;
import java.util.List;



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
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.deptstat.deptSupervision.service.DeptSupervisionService;
import cn.honry.statistics.deptstat.deptSupervision.vo.MonitorIndicatorsVo;
import cn.honry.statistics.deptstat.journal.action.JournalAction;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;
/**
 * 
 * 
 * <p>科室监督统计Action </p>
 * @Author: XCL
 * @CreateDate: 2017年7月14日 下午5:24:16 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月14日 下午5:24:16 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/dept/DeptSupervision")
public class DeptSupervisionAction {
	private Logger logger=Logger.getLogger(JournalAction.class);
	/**错误日志存储**/
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value="deptSupervisionService")
	private DeptSupervisionService deptSupervisionService;
	public void setDeptSupervisionService(
			DeptSupervisionService deptSupervisionService) {
		this.deptSupervisionService = deptSupervisionService;
	}
	
	private String time;
	private String menuAlias;
	private String campus;
	private String begin;
	private String end;
	private String depts;
	
	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getDepts() {
		return depts;
	}

	public void setDepts(String depts) {
		this.depts = depts;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * 
	 * 
	 * <p>跳转到科室监督统计页面 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月14日 下午5:24:00 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月14日 下午5:24:00 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 *
	 */
	@RequiresPermissions(value={"KSJDZBTJ:function:view"}) 
	@Action(value = "showView", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/deptSupervision/deptSupervisionList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String showView() {
		try {
			Date date = new Date();
			begin = DateUtils.formatDateY_M(date)+"-01";
			end=DateUtils.formatDateY_M_D(date);
		} catch (Exception e) {
			logger.error("KSTJ_ZYKSTJ_KSJDZBTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_ZYKSTJ_KSJDZBTJ", "科室统计_住院科室统计_科室监督指标统计", "2", "0"), e);
		}
		return "list";
	}
	/**
	 * 
	 * 
	 * <p> 查询指标监控数据</p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月22日 上午11:41:23 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月22日 上午11:41:23 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="querySupervisionList")
	public void querySupervisionList(){
		try {
			String[] cam=ServletActionContext.getRequest().getParameterValues("campus[]");
			campus="";
			if(cam!=null){
				for(int i=0,len=cam.length;i<len;i++){
					if(i>0){
						campus+=",";
					}
					campus+=cam[i];
				}
			}
			List<MonitorIndicatorsVo> list = deptSupervisionService.queryDayReport(begin, end, depts, menuAlias, campus);
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("KSTJ_ZYKSTJ_KSJDZBTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_ZYKSTJ_KSJDZBTJ", "科室统计_住院科室统计_科室监督指标统计", "2", "0"), e);
		}
		
	}
}
