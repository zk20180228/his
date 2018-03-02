package cn.honry.statistics.deptstat.dischargeStatistics.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.deptstat.deptBedsMessage.action.DeptBedsMessageAction;
import cn.honry.statistics.deptstat.dischargeStatistics.service.DischargeStatisticsService;
import cn.honry.statistics.deptstat.dischargeStatistics.vo.DischargeStatisticsVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/deptstat/dischargeStatistics")
public class DischargeStatisticsAction extends ActionSupport{
	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier(value = "dischargeStatisticsService")
	private DischargeStatisticsService dischargeStatisticsService;	
	
	public void setDischargeStatisticsService(
			DischargeStatisticsService dischargeStatisticsService) {
		this.dischargeStatisticsService = dischargeStatisticsService;
	}
	private Logger logger=Logger.getLogger(DeptBedsMessageAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	HttpServletRequest request=ServletActionContext.getRequest();
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	private String reportJson;
	private String startTime;
	private String endTime;
	private String menuAlias;//权限
	private String page;//页数
	private String rows;//每页数
	private String deptCode;//科室
	
	public String getReportJson() {
		return reportJson;
	}
	public void setReportJson(String reportJson) {
		this.reportJson = reportJson;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
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
	@RequiresPermissions(value={"CYBZTJCX:function:view"})
	@Action(value = "dischargeStatisticslist", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/dischargeStatistics/dischargeStatistics.jsp") },interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String dischargeStatisticslist() {
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		endTime=format.format(date);
		startTime=endTime.substring(0, 7)+"-01";
		return "list";
	}
	/**  
	 * 
	 * @Description：  查询所有疾病name、code
	 * @Author：qh
	 * @ModifyDate：2017-6-2
	 * @version 1.0
	 *
	 */
	@Action(value = "queryIllness")
	public void queryIllness(){
		List<DischargeStatisticsVo> list = dischargeStatisticsService.queryIllness();
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 出院病种统计
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	
	@SuppressWarnings("deprecation")
	@Action(value="queryDischargeStat")
	public void queryDischargeStat(){
		try {
			if(StringUtils.isBlank(startTime)){
				Date date = new Date();
				startTime = DateUtils.formatDateYM(date)+"-01 00:00:00";
			}else{
				startTime = startTime+" 00:00:00";
			}
			if(StringUtils.isBlank(endTime)){
				Date date = new Date();
				endTime = DateUtils.formatDateY_M_D(date)+" 23:59:59";
			}else{
				endTime = endTime+" 23:59:59";
			}
			List<DischargeStatisticsVo> dischargeStatList = dischargeStatisticsService.queryDischargeStat(startTime,endTime,deptCode,page,rows,menuAlias);
			int total=dischargeStatisticsService.getTotalDischargeStat(startTime,endTime,deptCode,page,rows,menuAlias);
			Map<Object, Object> map=new HashMap<Object,Object>();
			map.put("total",total);
			map.put("rows", dischargeStatList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("KSTJ_CYBZTJCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_CYBZTJCX", "科室统计_出院病种统计查询", "2", "0"), e);
		}
	}
	/** 打印方法
	* @Title: reportList 打印方法
	* @Description: 打印方法
	* @return void    返回类型 
	* @author dtl 
	* @date 2017年6月6日
	*/
	@Action(value = "reportList")
	public void reportList() {
		
		try{
			String root_path = request.getSession().getServletContext().getRealPath("/");
			root_path = root_path.replace('\\', '/');
			String reportFilePath = root_path + webPath+"CYBZTJCX.jasper";
			List<DischargeStatisticsVo> journalVos = JSONUtils.fromJson(reportJson, new TypeToken<List<DischargeStatisticsVo>>(){});
			//javaBean数据封装（注：数据源可参考该示例各自进行创建）
			DischargeStatisticsVo reportVo = new DischargeStatisticsVo();
			reportVo.setList(journalVos);
			List<DischargeStatisticsVo> list = new ArrayList<DischargeStatisticsVo>();
			list.add(reportVo);
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", root_path + webPath);
			iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,dataSource);
		}catch(Exception e){
			logger.error("KSTJ_CYBZTJCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_CYBZTJCX", "科室统计_出院病种统计查询", "2", "0"), e);
		}
	}
	
}
