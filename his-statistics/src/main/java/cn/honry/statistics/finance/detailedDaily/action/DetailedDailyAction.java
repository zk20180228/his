package cn.honry.statistics.finance.detailedDaily.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.hospital.service.HospitalInInterService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.finance.detailedDaily.service.DetailedDailyService;
import cn.honry.statistics.finance.detailedDaily.vo.VdetailedDaily;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/***
 * 结账处冲单明细日报
 * @Description:
 * @author: donghe
 * @CreateDate: 2016年6月22日 
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/DetailedDaily")
public class DetailedDailyAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(DetailedDailyAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	@Autowired
    @Qualifier(value = "detailedDailyService")
 	private DetailedDailyService detailedDailyService;
	public void setDetailedDailyService(DetailedDailyService detailedDailyService) {
		this.detailedDailyService = detailedDailyService;
	}
	/**
	 * @see注入报表借口口
	 */
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	/**获取医院名称**/
	@Autowired
	@Qualifier(value="hospitalInInterService")
	private HospitalInInterService hospitalInInterService;
	public void setHospitalInInterService(
			HospitalInInterService hospitalInInterService) {
		this.hospitalInInterService = hospitalInInterService;
	}
	/**
	 * 当前页数，用于分页查询
	 */
	private String page;
	
	/**
	 * 分页条数，用于分页查询
	 */
	private String rows;
	public String stime;
	public String etime;
	
	private HttpServletRequest request;
	
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
	/**
	 * 现在时间
	 */
	private String now;
	public String getNow() {
		return now;
	}
	public void setNow(String now) {
		this.now = now;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getEtime() {
		return etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}
	/***
	 * 
	 * @Description:结账处冲单明细日报  页面跳转
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@SuppressWarnings("deprecation")
	@Action(value="listdetailedDaily",results={ @Result(name = "list",location= "/WEB-INF/pages/stat/detailedDaily/detailedDaily.jsp") },interceptorRefs={@InterceptorRef(value= "manageInterceptor") })
	public String listdetailedDaily(){
		Date date = new Date();
		etime = DateUtils.formatDateY_M_D(date);
		stime = DateUtils.formatDateYM(date)+"-01";
		return "list";
	}
	/***
	 * 
	 * @Description:结账处冲单明细日报List
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@SuppressWarnings("deprecation")
	@Action(value = "queryDetailedDailylist")
	public void queryDetailedDailylist() {
	try{
		if(StringUtils.isBlank(stime)){
			Date date = new Date();
			stime = DateUtils.formatDateYM(date)+"-01";
		}
		if(StringUtils.isBlank(etime)){
			Date date = new Date();
			etime = DateUtils.formatDateY_M_D(date);
		}
		Map<String,Object> retMap = new HashMap<String,Object>();
		List<VdetailedDaily> list = detailedDailyService.queryVdetailedDaily(stime, etime, page, rows);
		int total = detailedDailyService.queryVdetailedDailyTotal(stime, etime);
		retMap.put("total", total);
		retMap.put("rows", list);
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}catch(Exception e){
		logger.error("ZYTJ_JZCCDMXRB", e);
		hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_JZCCDMXRB", "住院统计_住院处冲单明细日报", "2", "0"), e);   
	}	
	}
	/**
	 * @Description:导出 
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 **/
	@Action(value = "expDetailedDailylist", results = { @Result(name = "json", type = "json") })
	public void expDetailedDailylist(){
		PrintWriter out =null;
		try {
			List<VdetailedDaily> list = detailedDailyService.queryVdetailedDaily(stime, etime, page, rows);
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			if (list == null || list.isEmpty()) {
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
			}
			String head = "";
	
			String name = "结账处冲单明细日报";
			String[] headMessage = { "科室", "住院流水号", "姓名", "统计大类","冲单费用", "操作员"};
	
			for (String message : headMessage) {
				head += "," + message;
			}
			head = head.substring(1);
			FileUtil fUtil = new FileUtil();
			String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
			String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/" + fileName;
			fUtil.setFilePath(filePath);
			fUtil.write(head);
			
				out = WebUtils.getResponse().getWriter();
			
				fUtil = detailedDailyService.export(list, fUtil);
				fUtil.close();
				DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
				out.write("success");
		} catch (Exception e) {
			out.write("error");
			logger.error("ZYTJ_JZCCDMXRB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_JZCCDMXRB", "住院统计_结账处冲单明细日报", "2", "0"), e);
		}finally {
			out.flush();
			out.close();
		}
	}
	/**
	 * @author conglin
	 * @See 结账处冲单明细日报打印
	 * @createDate 17-2.27
	 */
	@SuppressWarnings("deprecation")
	@Action(value = "queryDetailedDailylistPDF")
	public void queryDetailedDailylistPDF() {
		try {
			if(StringUtils.isBlank(stime)){
				Date date = new Date();
				stime = DateUtils.formatDateYM(date)+"-01";
			}
			if(StringUtils.isBlank(etime)){
				Date date = new Date();
				etime = DateUtils.formatDateY_M_D(date);
			}
			List<VdetailedDaily> vdetailedDailylist=new ArrayList<VdetailedDaily>(); 
			vdetailedDailylist.add(new VdetailedDaily());
			vdetailedDailylist.get(0).setVdetailedDaily(detailedDailyService.queryVdetailedDaily(stime, etime, page, rows));
			request=ServletActionContext.getRequest();
			String root_path = request.getSession().getServletContext().getRealPath("/");
			 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/VdetailedDaily.jasper";
			 HashMap<String, Object> parameters = new HashMap<String, Object>();
			 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
			 parameters.put("hospitalName", hospitalInInterService.getPresentHospital().getName());
			 parameters.put("serviceHopital", "结账处冲单明细");
			iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(vdetailedDailylist));
		} catch (Exception e) {
			logger.error("ZYTJ_JZCCDMXRB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_JZCCDMXRB", "住院统计_结账处冲单明细日报", "2", "0"), e);
			e.printStackTrace();
		}
	}
}
