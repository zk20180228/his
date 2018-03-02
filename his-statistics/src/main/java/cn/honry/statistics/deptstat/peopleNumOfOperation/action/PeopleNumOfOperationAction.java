package cn.honry.statistics.deptstat.peopleNumOfOperation.action;

import java.io.File;
import java.text.SimpleDateFormat;
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
import cn.honry.statistics.deptstat.peopleNumOfOperation.service.PeopleNumOfOperationService;
import cn.honry.statistics.deptstat.peopleNumOfOperation.vo.PeopleNumOfOperationReportVo;
import cn.honry.statistics.deptstat.peopleNumOfOperation.vo.PeopleNumOfOperationVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 手术科室手术人数统计(含心内)
 * @author wangshujuan
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/peopleNumOfOperation")
public class PeopleNumOfOperationAction extends ActionSupport{
	private static final long serialVersionUID = 299739107926708273L;
	private HttpServletResponse response = ServletActionContext.getResponse();
	private HttpServletRequest request = ServletActionContext.getRequest();
	private Logger logger=Logger.getLogger(PeopleNumOfOperationAction.class);
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	
	
	/**
	 * 当前页数，用于分页查询
	 */
	private String page;
	
	/**
	 * 分页条数，用于分页查询
	 */
	private String rows;
	
	/**
	 * 开始时间
	 */
	private String begin;
	 /** 
     *  结束时间
     */
	private String end;
	
	 /** 
     * 导出用的json字符串
     */ 
	private String exportJson;
	 /** 
     * 栏目别名,在主界面中点击栏目时传到action的参数
     */
	private String menuAlias;
	/** 
	* 打印用的json字符串
	*/ 
	private String reportJson;
	private String startTime;
	private String endTime;
	private String deptCode;
	
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
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

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
	
	public String getExportJson() {
		return exportJson;
	}

	public void setExportJson(String exportJson) {
		this.exportJson = exportJson;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public String getReportJson() {
		return reportJson;
	}

	public void setReportJson(String reportJson) {
		this.reportJson = reportJson;
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

	@Autowired()
	@Qualifier(value="peopleNumOfOperationService")
	private PeopleNumOfOperationService peopleNumOfOperationService;
	public void setPeopleNumOfOperationService(
			PeopleNumOfOperationService peopleNumOfOperationService) {
		this.peopleNumOfOperationService = peopleNumOfOperationService;
	}

	/**  
	 * 
	 * <p>手术科室手术人数统计(含心内)</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:28:21 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:28:21 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@RequiresPermissions(value={"SSKSSSRSTJ:function:view"})
	@Action(value = "listPeopleNumOfOperation", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/operation/peopleNumOfOperation.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listPeopleNumOfOperation() {
		// 获取当月第一天至当天时间段
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		endTime=format.format(date);
		startTime=endTime.substring(0, 7)+"-01";
		return "list";
	}
	
	@Action("peopleNumOfOperationDatagrid")
	public void peopleNumOfOperationDatagrid(){
		try {
			if(StringUtils.isBlank(begin)){
				begin=DateUtils.formatDateY_M(new Date());
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map= peopleNumOfOperationService.listPeopleNumOfOperation(page,rows,begin);
			WebUtils.webSendJSON(JSONUtils.toJson(map));
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("KSTJ_SSKSSSRSTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_SSKSSSRSTJ", "科室统计_手术科室手术人数统计(含心内)", "2", "0"), e);
		}
	}
	
	/** 导出方法
	* @Title: exportList 导出方法
	* @Description: 导出方法
	* @return void    返回类型 
	* @author dtl 
	* @date 2017年6月6日
	*/
	@Action(value = "exportPeopleNumOfOperation")
	public void exportPeopleNumOfOperation() {
		try{
			List<PeopleNumOfOperationVo> peopleNumOfOperationVo = JSONUtils.fromJson(reportJson, new TypeToken<List<PeopleNumOfOperationVo>>(){});
			
			if(peopleNumOfOperationVo == null || peopleNumOfOperationVo.isEmpty()){
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
			}
			String head="";
			String name = "";
			name = "手术科室手术人数统计（含心内）";
			String[] headMessage ={"科室名称","手术人数","手术例数","出院总数","手术占比","全院患者","手术人数占全院比"};
			for (String message : headMessage) {
				head +=","+message;
			} 
			head=head.substring(1);
			FileUtil fUtil = new FileUtil();
			String fileName = name+DateUtils.formatDateY_M_D_H_M(new Date())+".csv";
			String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF")+"/downLoad/"+fileName;
			fUtil.setFilePath(filePath);
			fUtil.write(head);
			fUtil = peopleNumOfOperationService.export(peopleNumOfOperationVo, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
		}catch(Exception e){
			logger.error("SSKSSSRSTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSKSSSRSTJ", "科室统计_手术科室手术人数统计（含心内）", "2", "0"), e);
		}
	}
   /** 打印方法
	* @Title: reportList 打印方法
	* @Description: 打印方法
	* @return void    返回类型 
	* @author zxl 
	* @date 2017年6月6日
	*/
	@Action(value = "reportPeopleNumOfOperation")
	public void reportPeopleNumOfOperation() {
		try{
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀	 
			String root_path = request.getSession().getServletContext().getRealPath("/");
			root_path = root_path.replace('\\', '/');
			String reportFilePath = root_path + webPath+fileName+".jasper";
			List<PeopleNumOfOperationVo> peopleNumOfOperationVo = JSONUtils.fromJson(reportJson, new TypeToken<List<PeopleNumOfOperationVo>>(){});
			//javaBean数据封装（注：数据源可参考该示例各自进行创建）
			PeopleNumOfOperationReportVo reportVo = new PeopleNumOfOperationReportVo(peopleNumOfOperationVo);
			List<PeopleNumOfOperationReportVo> list = new ArrayList<PeopleNumOfOperationReportVo>();
			list.add(reportVo);
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", root_path + webPath);
			iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,dataSource);
		}catch(Exception e){
			logger.error("SSKSSSRSTJHXN", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSKSSSRSTJHXN", "科室统计_手术科室手术人数统计（含心内）", "2", "0"), e);
		}
	}
	
	/** 初始化方法
	* @Title: reportList 打印方法
	* @Description: 打印方法
	* @return void    返回类型 
	* @author dtl 
	* @date 2017年6月6日
	*/
	@Action("initPeopleNumOfOperation")
	public void initKeyOperationsCheck(){
		try {
			peopleNumOfOperationService.initPeopleNumOfOperation(begin,end);
			System.out.println("success");
			WebUtils.webSendString("初始化成功！");
		} catch (Exception e) {
			logger.error("SSKSSSRSTJHXN", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSKSSSRSTJHXN", "科室统计_手术科室手术人数统计（含心内）", "2", "0"), e);
		}
	}
	
	/**  
	 * 手术科室手术人数统计(含心内)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@SuppressWarnings("deprecation")
	@Action("queryPeopleNumOfOperation")
	public void queryPeopleNumOfOperation(){
		try {
			if(StringUtils.isBlank(startTime)){
				Date date = new Date();
				startTime = DateUtils.formatDateYM(date)+"-01";
			}
			if(StringUtils.isBlank(endTime)){
				Date date = new Date();
				endTime = DateUtils.formatDateY_M_D(date);
			}
			Map<String, Object> map = peopleNumOfOperationService.queryPeopleNumOfOperation(startTime,endTime,deptCode,menuAlias,page,rows);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("KSTJ_SSKSSSRSTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_SSKSSSRSTJ", "科室统计_手术科室手术人数统计(含心内)", "2", "0"), e);
		}
	}
}

