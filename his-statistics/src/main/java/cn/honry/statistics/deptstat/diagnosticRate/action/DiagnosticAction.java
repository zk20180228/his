package cn.honry.statistics.deptstat.diagnosticRate.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.opensymphony.xwork2.ActionSupport;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.deptstat.diagnosticRate.service.DiagnosticRateService;
import cn.honry.statistics.deptstat.diagnosticRate.vo.DiagnosticRateVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

/**
 * 
 * 
 * <p>诊断符合率Action </p>
 * @Author: XCL
 * @CreateDate: 2017年7月12日 上午11:20:36 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月12日 上午11:20:36 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/deptStat/diagnosticRate")
public class DiagnosticAction extends ActionSupport {
	private static final long serialVersionUID = -6074108583698023035L;

	@Autowired
	@Qualifier(value="diagnosticRateService")
    private DiagnosticRateService diagnosticRateService;
	
	private String menuAlias;//栏目别名
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String rows;//行数
	private String page;//页数
	private String depts;//科室
	private String campus;//院区
	
	private Logger logger=Logger.getLogger(DiagnosticAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	
	public void setDiagnosticRateService(DiagnosticRateService diagnosticRateService) {
		this.diagnosticRateService = diagnosticRateService;
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
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

	public String getDepts() {
		return depts;
	}

	public void setDepts(String depts) {
		this.depts = depts;
	}

	@Action(value="diagnosticShowList",results={@Result(name="list",location="/WEB-INF/pages/stat/deptstat/diagnosticRate/diagnosticRateList.jsp")},interceptorRefs = { @InterceptorRef(value = "manageInterceptor")})
	public String diagnosticShowList(){
		
		return "list";
	}
	/**
	 * 
	 * 
	 * <p>查询list列表 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月12日 下午1:37:02 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月12日 下午1:37:02 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="queryList")
	public void queryList(){
		try {
			List<DiagnosticRateVo> list=diagnosticRateService.queryDiagnoSticRate(startTime, endTime, rows, page, menuAlias, depts, campus);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", 0);
			map.put("rows", list);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("KSTJ_ZDFHL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_ZDFHL", "科室统计_诊断符合率", "2", "0"), e);
		}
	}
}
