package cn.honry.statistics.deptstat.internalCompare1.action;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
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
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.deptstat.internalCompare1.service.InternalCompare1Service;
import cn.honry.statistics.deptstat.internalCompare1.vo.InternalCompare1Vo;
import cn.honry.statistics.deptstat.internalCompare1.vo.ReportInternalCompare1Vo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;


@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/deptstat/internalCompare1")
public class InternalCompare1Action extends ActionSupport {
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier(value = "internalCompare1Service")
	private InternalCompare1Service internalCompare1Service;
	public void setInternalCompare1Service(
			InternalCompare1Service internalCompare1Service) {
		this.internalCompare1Service = internalCompare1Service;
	}
	private Logger logger=Logger.getLogger(InternalCompare1Action.class);
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
	
	/** 
	* @Fields menuAlias : 栏目别名
	*/ 
	private String menuAlias;
	private String endTime;
	private String dept1;
	private String dept2;
	private String title = HisParameters.PREFIXFILENAME;
	private String exportJson;
	private String Stime;
	private String Etime;

	public String getStime() {
		return Stime;
	}

	public void setStime(String stime) {
		Stime = stime;
	}

	public String getEtime() {
		return Etime;
	}

	public void setEtime(String etime) {
		Etime = etime;
	}
	/** 
	* @Fields reportTime : 打印用到的时间
	*/ 
	private String reportTime;
	/** 
	* @Fields reportJson : 打印集合的字符串
	*/ 
	private String reportJson;
	
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String getDept1() {
		return dept1;
	}

	public void setDept1(String dept1) {
		this.dept1 = dept1;
	}

	public String getDept2() {
		return dept2;
	}

	public void setDept2(String dept2) {
		this.dept2 = dept2;
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

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getExportJson() {
		return exportJson;
	}

	public void setExportJson(String exportJson) {
		this.exportJson = exportJson;
	}

	@RequiresPermissions(value={"YYNKYXBHNEYXBDBBO:function:view"}) 
	@Action(value = "internalCompare1list", results = { 
			@Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/internalCompare1/internalCompare1.jsp") }, 
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String internalCompare1list() {
		Date date = new Date();
		Etime = DateUtils.formatDateY_M(date);
		ServletActionContext.getRequest().setAttribute("Etime", Etime);
		return "list";
	}

	@Action(value = "queryInternalCompare1list", results = { @Result(type = "json") })
	public void queryInternalCompare1list(){
		String[] times = endTime.split("-");
		String prevTime = (Integer.parseInt(times[0])-1) + "-" + times[1];
		//获得相应虚拟科室下的真实科室
		boolean sign=new MongoBasicDao().isCollection("YYNKYXBHNEYXBDBBO");
		String depts1 =null;
		String depts2 =null;
		if(sign){
			depts1=dept1;
			depts2=dept2;
		}else{
			depts1 = internalCompare1Service.getDept(dept1);
			depts2 = internalCompare1Service.getDept(dept2);
		}
		//查询出内科医学部数据
		List<InternalCompare1Vo> list =new ArrayList<InternalCompare1Vo>();
		InternalCompare1Vo vo=new InternalCompare1Vo("内科医学部","内科医学部");
		list.add(vo);
		list.addAll(internalCompare1Service.getInternalCompare1(prevTime, endTime, depts1, "内科医学部",sign));
		InternalCompare1Vo vo1=new InternalCompare1Vo("内二医学部","内二医学部");
		list.add(vo1);
		//查询出内二医学部数据
		list.addAll(internalCompare1Service.getInternalCompare1(prevTime, endTime, depts2, "内二医学部",sign));
		String json=JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	@Action(value = "saveInternalCompare1ListToDB")
	public void initinternalCompare2list() {
		try {
			String dept = dept1 + "_" + dept2;
			String Stime="2016-01-01";
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(Stime);
			// 获取Calendar
			Calendar calendar = Calendar.getInstance();
			// 设置时间,当前时间不用设置
			calendar.setTime(date);
			// 设置日期为本月最大日期
			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
			String Etime = format.format(calendar.getTime());
		    while(format.parse(Stime).before(format.parse("2016-04-30"))){
		    	internalCompare1Service.initCompare1List(Stime, Etime, dept.split("_"));
		    	calendar.setTime(format.parse(Etime));
		    	calendar.add(Calendar.MONTH, +1);
		    	calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		    	Stime = format.format(calendar.getTime());
		    	calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		    	Etime = format.format(calendar.getTime());
		    }
			} catch (ParseException e) {
				WebUtils.webSendJSON("error");
				logger.error("KSTJ_YYNKYXBHNEYXBDBBT", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_YYNKYXBHNEYXBDBBT", "科室统计_医院内科医学部和内二医学部对比表2", "2", "0"), e);
			
			}

	}

	@Action(value = "reportList")
	public void reportList() {
		
		try{
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀	 
			String root_path = request.getSession().getServletContext().getRealPath("/");
			root_path = root_path.replace('\\', '/');
			String reportFilePath = root_path + webPath+fileName+".jasper";
			List<InternalCompare1Vo> compare1Vos = JSONUtils.fromJson(reportJson, new TypeToken<List<InternalCompare1Vo>>(){});
			//javaBean数据封装（注：数据源可参考该示例各自进行创建）
			String nYear = reportTime.split("-")[0];
			String oYear = (Integer.valueOf(nYear) - 1) + "年";
			nYear = nYear + "年";
			String title1 = nYear + reportTime.split("-")[1] + "月" + HisParameters.PREFIXFILENAME + "内科医学部和内二医学部对比表1";
			ReportInternalCompare1Vo journalVo = new ReportInternalCompare1Vo(title1, oYear, nYear, compare1Vos);
			List<ReportInternalCompare1Vo> list = new ArrayList<ReportInternalCompare1Vo>();
			list.add(journalVo);
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", root_path + webPath);
			iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,dataSource);
		}catch(Exception e){
			logger.error("ZYRB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYRB", "内科医学部和内二医学部对比表1", "2", "0"), e);
		}
	}
	
	@Action(value = "excelPort", results = { @Result(name = "json", type = "json") })
	public void excelPort() throws Exception  {
		List<InternalCompare1Vo> journalVos = JSONUtils.fromJson(exportJson, new TypeToken<List<InternalCompare1Vo>>(){});
		if (journalVos == null || journalVos.isEmpty()) {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
		}
		String name =endTime+"月"+HisParameters.PREFIXFILENAME+"内科医学部和内二医学部对比表1";
		String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".xls";
		String filename = new String(fileName.getBytes(),"ISO8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="+filename);
		ServletOutputStream stream = response.getOutputStream();
		internalCompare1Service.exportExcel(stream, journalVos, endTime);
		}

	
	
}
