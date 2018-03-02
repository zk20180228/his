package cn.honry.statistics.deptstat.criticallyPatientsAnalyse.action;

import java.io.File;
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
import cn.honry.statistics.deptstat.criticallyPatientsAnalyse.service.CriticallyPatientsAnalyseService;
import cn.honry.statistics.deptstat.criticallyPatientsAnalyse.vo.CriticallyPatientsVO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;
/**  
 * <p>危重疑难患者人数比例统计分析</p>
 * @Author: yuke
 * @CreateDate: 2017年7月3日 下午4:38:04 
 * @Modifier: yuke
 * @ModifyDate: 2017年7月3日 下午4:38:04 
 * @ModifyRmk:  
 * @version: V1.0
 * @param:
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/CriticallyPatientsAnalyseAction")
public class CriticallyPatientsAnalyseAction extends ActionSupport {
	
	private Logger logger=Logger.getLogger(CriticallyPatientsAnalyseAction.class);

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	
	/**
	 * service注入
	 */
	private CriticallyPatientsAnalyseService  criticallyPatientsAnalyseService;

	public CriticallyPatientsAnalyseService getCriticallyPatientsAnalyseService() {
		return criticallyPatientsAnalyseService;
	}
	@Autowired
    @Qualifier(value = "criticallyPatientsAnalyseService")
	public void setCriticallyPatientsAnalyseService(CriticallyPatientsAnalyseService criticallyPatientsAnalyseService) {
		this.criticallyPatientsAnalyseService = criticallyPatientsAnalyseService;
	}
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}

	/**
	 * 开始时间
	 */
	private String firstData;
	/**
	 * 结束时间
	 */
	private String endData;
	/**
	 * 科室名称
	 */
	private String deptCode;
	private List<CriticallyPatientsVO> list;
	private String menuAlias;

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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getFirstData() {
		return firstData;
	}
	public void setFirstData(String firstData) {
		this.firstData = firstData;
	}
	public String getEndData() {
		return endData;
	}
	public void setEndData(String endData) {
		this.endData = endData;
	}
	
	/**
	* @Title: 访问页面
	* @param @return    参数
	* @return String    返回类型
	* @throws
	*/
	@Action(value="listPatientCost",results={@Result(name="list",location="/WEB-INF/pages/stat/deptstat/criticallyPatientsAnalyse/criticallyPatientsAnalyse.jsp")},interceptorRefs={@InterceptorRef(value="manageInterceptor")})
	public String listPatientCost(){
		//获取时间
		Date date = new Date();
		endData = DateUtils.formatDateY_M_D(date);
		firstData = DateUtils.formatDateYM(date)+"-01";
		return "list";
	}
	/**
	* @Title: 查询
	* @param     参数
	* @return void    返回类型
	* @throws
	*/
	@Action(value="queryCriticallyPatients")
	public void queryCriticallyPatients() {
		try {
		//科室渲染
//		 Map<String,String> map=criticallyPatientsAnalyseService.queryDeptMap();
		 
//		 if(monthlyDashboardService.isCollection("WZYNHZRSBLTJFXDB")){
//			 list = criticallyPatientsAnalyseService.queryCriticallyPatientsFromMongo();
//		 }else {
			list = criticallyPatientsAnalyseService.queryCriticallyPatients(deptCode,firstData,endData,menuAlias);
//		}
//		for (CriticallyPatientsVO vo : list) {
//			if(StringUtils.isNotBlank(vo.getDeptName()))
//			vo.setDeptName(map.get(vo.getDeptName()));
//		}
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
		}catch(Exception ex){
		     ex.printStackTrace();
		     logger.error("KSTJ_WZYNHZRSBLTJFX", ex);
			 hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_WZYNHZRSBLTJFX", "科室统计_危重疑难患者人数比例统计分析", "2", "0"),ex);
		  }
	}
	/**
	* @Title: 数据导出
	* @param     参数
	* @return void    返回类型
	* @throws
	*/
	@Action(value="exportCriticallyPatients")
	public void exportCriticallyPatients(){
		try {
			 String rows = request.getParameter("rows");
			 Gson gson = new Gson();
			 List<CriticallyPatientsVO> list = gson.fromJson(rows, new TypeToken<List<CriticallyPatientsVO>>(){}.getType());
			 if (list == null || list.isEmpty()) {
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
			}
			String head = "";
			String name = "危重疑难患者人数比例统计分析";
			String[] headMessage = { "科室名称", "数量", "治愈情况","好转", "未治愈", "死亡", "其他", "治愈率百分比(%)", 
					"死亡率百分比", "平均住院日", "平均费用", "并发症感染", "全院患者", "占全院比"};
			for (String message : headMessage) {
				head += "," + message;
			}
			head = head.substring(1);
			FileUtil fUtil = new FileUtil();
			String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
			String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/" + fileName;
			fUtil.setFilePath(filePath);
			fUtil.write(head);
			fUtil = criticallyPatientsAnalyseService.exportList(list, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("KSTJ_WZYNHZRSBLTJFX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_WZYNHZRSBLTJFX", "科室统计_危重疑难患者人数比例统计分析", "2", "0"), e);
		}
		
	}
	/**
	* @Title: 报表打印
	* @param     参数
	* @return void    返回类型
	* @throws
	*/
	@Action(value="experCriticallyPatients")
	public void experCriticallyPatients() {
		try{
			   //jasper文件名称 不含后缀
 				 String rows = request.getParameter("rows");
 				 
				 List<CriticallyPatientsVO> list = JSONUtils.fromJson(rows, new TypeToken<List<CriticallyPatientsVO>>(){});
				 if (list == null || list.isEmpty()) {
						response.setContentType("text/html;charset=utf-8");
						response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
				}
			   String root_path = request.getSession().getServletContext().getRealPath("/");
			   root_path = root_path.replace('\\', '/');
			   String reportFilePath = root_path + webPath +"WZYNHZRSBLTJFX.jasper";
			  
			   //javaBean数据封装
			   ArrayList<CriticallyPatientsVO> voList = new ArrayList<CriticallyPatientsVO>();
			   CriticallyPatientsVO vo = new CriticallyPatientsVO();
			   vo.setList(list);
			   voList.add(vo);
			   JRDataSource jrd=new JRBeanCollectionDataSource(voList);
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   parameters.put("SUBREPORT_DIR", root_path + webPath);
			   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
			  }catch(Exception ex)
			  {
			     ex.printStackTrace();
			     logger.error("KSTJ_WZYNHZRSBLTJFX", ex);
				 hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_WZYNHZRSBLTJFX", "科室统计_危重疑难患者人数比例统计分析", "2", "0"),ex);
			  }
		
	}
	
	/**
	 * 
	 * <p>初始化mongo数据</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:36:53 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:36:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="initMongoDb")
	public void initMongoDb(){
		try {
			criticallyPatientsAnalyseService.initMongoDb();
		} catch (Exception e) {
			 logger.error("KSTJ_WZYNHZRSBLTJFX", e);
			 hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_WZYNHZRSBLTJFX", "科室统计_危重疑难患者人数比例统计分析", "2", "0"),e);
		}
	}
}
