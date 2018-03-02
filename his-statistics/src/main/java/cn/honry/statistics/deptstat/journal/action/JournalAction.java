package cn.honry.statistics.deptstat.journal.action;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import cn.honry.statistics.deptstat.journal.service.JournalService;
import cn.honry.statistics.deptstat.journal.vo.JournalReportVo;
import cn.honry.statistics.deptstat.journal.vo.JournalVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;

/** 
* 住院日报统计
* @ClassName: JournalAction 住院日报统计
* @Description: 住院日报统计
* @author dtl
* @date 2017年6月2日
*  
*/
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/deptstat/journal")
public class JournalAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	
	/** 
	* @Fields journalService : 住院日报service
	*/ 
	@Autowired
	@Qualifier(value="journalService")
	private JournalService journalService;
	public void setJournalService(JournalService journalService) {
		this.journalService = journalService;
	}
	private Logger logger=Logger.getLogger(JournalAction.class);
	/**错误日志存储**/
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 查询科室
	 */
	private String dept;
	/**
	 * 开始时间
	 */
	private String time;
	/** 
	* @Fields count : 记录数
	*/ 
	private String count;
	/** 
	* @Fields reportTime : 打印用的时间 
	*/ 
	private String reportTime;
	/** 
	* @Fields reportJson : 打印用的json字符串
	*/ 
	private String reportJson;
	/** 
	* @Fields exportJson : 导出用的json字符串
	*/ 
	private String exportJson;
	/** 
	* @Fields hosiptyal : 医院名称
	*/ 
	private String hosiptyal;
	/**
	 * @Fields campus 院区
	 */
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getReportJson() {
		return reportJson;
	}
	public void setReportJson(String reportJson) {
		this.reportJson = reportJson;
	}
	public String getExportJson() {
		return exportJson;
	}
	public void setExportJson(String exportJson) {
		this.exportJson = exportJson;
	}
	public String getHosiptyal() {
		return hosiptyal;
	}
	public void setHosiptyal(String hosiptyal) {
		this.hosiptyal = hosiptyal;
	}
	@RequiresPermissions(value={"ZYRB:function:view"}) 
	@Action(value = "listJournal", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/journal/journalList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listJournal() {
		hosiptyal = HisParameters.PREFIXFILENAME;
		Date date = new Date();
		time = DateUtils.formatDateY_M_D(date);
		try {
			dept = journalService.getAllInpateintDeptCode();
		} catch (Exception e) {
			logger.error("ZYRB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYRB", "科室统计_住院日报", "2", "0"), e);
		}
		return "list";
	}
	
	/** 根据时间和科室查询住院日志（先从mongoDB取值，没值时再查询数据库）
	* @Title: queryListJournalVo 根据时间和科室查询住院日志（先从mongoDB取值，没值时再查询数据库）
	* @Description: 根据时间和科室查询住院日志（先从mongoDB取值，没值时再查询数据库）
	* @author dtl 
	* @date 2017年6月5日
	*/
	@Action(value = "queryListJournalVo")
	public void queryListJournalVo() {
		try{
			String[] campus=ServletActionContext.getRequest().getParameterValues("campus[]");
			String queryCampus="";
			if(campus!=null){
				for(int i=0,len=campus.length;i<len;i++){
					if(i==0){
						queryCampus+=campus[i];
					}else{
						queryCampus+=","+campus[i];
					}
				}
			}
			List<JournalVo> list=journalService.queryDayReport(time, dept, menuAlias, queryCampus);
//			List<JournalVo> list = journalService.fromMongoDB(time, dept);
//			if (list == null || list.size() == 0) {
//				list = journalService.queryJournals(time, dept);
//			}
//			JournalVo sumVo = journalService.sumVo(list);
//			list.add(sumVo);
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYRB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYRB", "科室统计_住院日报", "2", "0"), e);
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
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀	 
			String root_path = request.getSession().getServletContext().getRealPath("/");
			root_path = root_path.replace('\\', '/');
			String reportFilePath = root_path + webPath+fileName+".jasper";
			List<JournalVo> journalVos = JSONUtils.fromJson(reportJson, new TypeToken<List<JournalVo>>(){});
			//javaBean数据封装（注：数据源可参考该示例各自进行创建）
			JournalReportVo reportVo = new JournalReportVo(HisParameters.PREFIXFILENAME + "住院日报", reportTime, journalVos.size(), journalVos);
			reportVo.setsTime(time);
			List<JournalReportVo> list = new ArrayList<JournalReportVo>();
			list.add(reportVo);
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", root_path + webPath);
			iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,dataSource);
		}catch(Exception e){
			logger.error("ZYRB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYRB", "科室统计_住院日报", "2", "0"), e);
		}
	}
	/** 导出方法
	* @Title: exportList 导出方法
	* @Description: 导出方法
	* @return void    返回类型 
	* @author dtl 
	* @date 2017年6月6日
	*/
	@Action(value = "exportList")
	public void exportList() {
		try{
			List<JournalVo> journalVos = JSONUtils.fromJson(exportJson, new TypeToken<List<JournalVo>>(){});
			
			if(journalVos == null || journalVos.isEmpty()){
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
			}
			String head="";
			String name = "";
			name = "住院日报";
			String[] headMessage ={"科室","原有人数","入院","转入","转出","出院","现有人数","实占床位","挂床日数","开放床位","病床使用率", "危重病人","一级护理","加床" ,"空床"};
			for (String message : headMessage) {
				head +=","+message;
			} 
			head=head.substring(1);
			FileUtil fUtil = new FileUtil();
			String fileName = name+DateUtils.formatDateY_M_D_H_M(new Date())+".csv";
			String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF")+"/downLoad/"+fileName;
			fUtil.setFilePath(filePath);
			fUtil.write(head);
			fUtil = journalService.export(journalVos, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
		}catch(Exception e){
			logger.error("ZYRB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYRB", "科室统计_住院日报", "2", "0"), e);
		}
	}
}
