package cn.honry.statistics.deptstat.operaCondtion.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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





import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.deptstat.journal.action.JournalAction;
import cn.honry.statistics.deptstat.operaCondtion.service.OperaCondService;
import cn.honry.statistics.deptstat.operaCondtion.vo.OperaCondVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;
/**
 * 
 * 
 * <p>手术情况查询Action </p>
 * @Author: XCL
 * @CreateDate: 2017年7月15日 下午4:10:22 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月15日 下午4:10:22 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/operaCondtion")
public class OperaCondAction {
	
	private String startTime;//开始时间
	private String endTime;//结束时间
	private Logger logger=Logger.getLogger(JournalAction.class);
	/**错误日志存储**/
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value="operaCondService")
	private OperaCondService operaCondService;
	
	public void setOperaCondService(OperaCondService operaCondService) {
		this.operaCondService = operaCondService;
	}
	private String menuAlias;//栏目别名
	private String page;//页数
	private String rows;//条数
	private String depts;//科室code
	
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * 
	 * 
	 * <p>跳转到手术情况查询页面 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月15日 下午5:24:15 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月15日 下午5:24:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 *
	 */
	@RequiresPermissions(value={"SSQKCX:function:view"}) 
	@Action(value = "operaCondtionList", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/operation/conditionQuery.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String operaCondtionList() {
		try {
			//获取时间
			Date date = new Date();
			startTime = DateUtils.formatDateY_M(date)+"-01";
			endTime = DateUtils.formatDateY_M_D(date);
		} catch (Exception e) {
			logger.error("KSTJ_SSKSTJ_SSQKCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_SSKSTJ_SSQKCX", "科室统计_手术科室统计_手术情况查询", "2", "0"), e);
		}
		return "list";
	}
	/**
	 * 
	 * 
	 * <p> 手术情况查询</p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月15日 下午5:45:03 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月15日 下午5:45:03 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action("queryOperaCondList")
	public void queryOperaCondList(){
		try {
			List<OperaCondVo> list=operaCondService.queryOperaList(startTime, endTime, menuAlias, depts, page, rows);
			int total=operaCondService.queryOperaTotal(startTime, endTime, menuAlias, depts);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", total);
			map.put("rows", list);
			String json=JSONUtils.toJson(map, DateUtils.DATETIME_FORMAT);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("KSTJ_SSKSTJ_SSQKCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_SSKSTJ_SSQKCX", "科室统计_手术科室统计_手术情况查询", "2", "0"), e);
		}
	}
}
