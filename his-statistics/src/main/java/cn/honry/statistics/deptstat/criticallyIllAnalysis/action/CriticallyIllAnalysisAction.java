package cn.honry.statistics.deptstat.criticallyIllAnalysis.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
import cn.honry.report.service.IReportService;
import cn.honry.statistics.deptstat.criticallyIllAnalysis.service.CriticallyIllAnalysisService;
import cn.honry.statistics.deptstat.criticallyIllAnalysis.vo.CriticallyIllAnalysisReportVo;
import cn.honry.statistics.deptstat.criticallyIllAnalysis.vo.CriticallyIllAnalysisVo;
import cn.honry.statistics.deptstat.deptBedsMessage.action.DeptBedsMessageAction;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/criticallyIllAnalysis")
public class CriticallyIllAnalysisAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier(value = "criticallyIllAnalysisService")
	private CriticallyIllAnalysisService criticallyIllAnalysisService;

	public void setCriticallyIllAnalysisService(
			CriticallyIllAnalysisService criticallyIllAnalysisService) {
		this.criticallyIllAnalysisService = criticallyIllAnalysisService;
	}
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	private Logger logger=Logger.getLogger(DeptBedsMessageAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	private String menuAlias;
	private String deptCode;
	private String startTime;
	private String endTime;
	
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
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	/**
	 * 重症患者占比分析view
	 * @return
	 */
	@RequiresPermissions(value={"ZZHZZBFX:function:view"})
	@Action(value = "criticallyIllAnalysislist", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/criticallyIllAnalysis/criticallyIllAnalysis.jsp") },interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String criticallyIllAnalysislist() {
		// 获取当月第一天至当天时间段
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		endTime=format.format(date);
		startTime=endTime.substring(0, 7)+"-01";
		return "list";
	}
	
	
	/**  
	 * 重症患者占比分析
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	
	@Action(value="queryCriticallyIllAnalysis")
	public void queryCriticallyIllAnalysis(){
		try {
			List<CriticallyIllAnalysisVo> criticallyIllAnalysisList = criticallyIllAnalysisService.queryCriticallyIllAnalysis(startTime,endTime,deptCode,menuAlias);
			String json = JSONUtils.toJson(criticallyIllAnalysisList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			//异常信息输出至日志文件
			logger.error("KSTJ_ZZHZZBFX", e);
			//异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_ZZHZZBFX", "科室统计_重症患者占比分析", "2", "0"), e);
		}
	}
	
	/**  
	 * 打印
	 * @Author: qh
	 * @CreateDate: 2017年7月28日 下午3:30:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	
	@Action(value="printList")
	public void printList(){
		try {
			   //jasper文件名称 不含后缀
			   String rows=request.getParameter("rows");
			   String firstData = request.getParameter("start");
			   String endData = request.getParameter("end");
			   String root_path = request.getSession().getServletContext().getRealPath("/");
			   root_path = root_path.replace('\\', '/');
			   String reportFilePath = root_path + webPath +"ZZHZZBFX.jasper";
			   List<CriticallyIllAnalysisVo> list =JSONUtils.fromJson(rows, new TypeToken<List<CriticallyIllAnalysisVo>>(){});
			   //javaBean数据封装
			   ArrayList<CriticallyIllAnalysisReportVo> voList = new ArrayList<CriticallyIllAnalysisReportVo>();
			   CriticallyIllAnalysisReportVo vo = new CriticallyIllAnalysisReportVo();
			   vo.setItemList(list);
			   voList.add(vo);
			   JRDataSource jrd=new JRBeanCollectionDataSource(voList);
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   parameters.put("firstData", firstData.replace("-", "/"));
			   parameters.put("endData", endData.replace("-", "/"));
			   String deptName="重症患者占比分析";
			   parameters.put("itemName", deptName);
			   parameters.put("SUBREPORT_DIR", root_path + webPath);
			   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
		} catch (Exception e) {
			//异常信息输出至日志文件
			logger.error("KSTJ_ZZHZZBFX", e);
			//异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_ZZHZZBFX", "科室统计_重症患者占比分析", "2", "0"), e);
		}
	}
	
}
