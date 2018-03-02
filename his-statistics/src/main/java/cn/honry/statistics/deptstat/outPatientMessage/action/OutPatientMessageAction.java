package cn.honry.statistics.deptstat.outPatientMessage.action;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import cn.honry.statistics.deptstat.journal.vo.JournalVo;
import cn.honry.statistics.deptstat.outPatientMessage.service.OutPatientMessageService;
import cn.honry.statistics.deptstat.outPatientMessage.vo.OutPatientMessageVo;
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
@Namespace(value="/statistics/outPatientMessage")
@SuppressWarnings({"all"})
public class OutPatientMessageAction extends ActionSupport{
	private static final long serialVersionUID = 8535596438329082479L;
	@Autowired
	@Qualifier(value = "outPatientMessageService")
	private OutPatientMessageService outPatientMessageService;
	public void setOutPatientMessageService(
			OutPatientMessageService outPatientMessageService) {
		this.outPatientMessageService = outPatientMessageService;
	}
	
	private Logger logger=Logger.getLogger(OutPatientMessageAction.class);
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
	private String deptCode;//科室
	private String menuAlias;//权限
	private String page;//页数
	private String rows;//每页数
	private String startTime;
	private String endTime;
	private String exportJson;//导出
	private String reportJson;//打印
	
	public String getExportJson() {
		return exportJson;
	}
	public void setExportJson(String exportJson) {
		this.exportJson = exportJson;
	}
	public String getReportJson() {
		return reportJson;
	}
	public void setReportJson(String reportJson) {
		this.reportJson = reportJson;
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
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
	 * 跳转到出院患者信息页面
	 * @return
	 */
	@RequiresPermissions(value={"CYHZXXCX:function:view"})
	@Action(value = "listOutPatientMessage", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/outPatientMessage/outPatientMessage.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listDeptBedsMessage() {
		/**
		 * 获取当月第一天至当天时间段
		 */
		Date date = new Date();
		startTime = DateUtils.formatDateYM(date)+"-01";
		endTime = DateUtils.formatDateY_M_D(date);
		ServletActionContext.getRequest().setAttribute("startTime", startTime);
		ServletActionContext.getRequest().setAttribute("endTime", endTime);
		return "list";
	}
	/**  
	 * 出院患者信息查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	
	@Action(value="queryOutPatientMessage")
	public void queryOutPatientMessage(){
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
			List<OutPatientMessageVo> deptBedsMessageList = outPatientMessageService.queryOutPatientMessage(startTime,endTime,deptCode,menuAlias,page,rows);
//			for (OutPatientMessageVo outPatientMessageVo : deptBedsMessageList) {
//				outPatientMessageVo.setAgeunit(outPatientMessageVo.getAge()+outPatientMessageVo.getAgeunit());
//			}
			int total=outPatientMessageService.getTotalOutPatientMessage(startTime,endTime,deptCode,menuAlias);
			Map<Object, Object> map=new HashMap<Object,Object>();
			map.put("total",total);
			map.put("rows", deptBedsMessageList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("KSTJ_CYHZXXCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_CYHZXXCX", "科室统计_出院患者信息查询", "2", "0"), e);
		}
	}
	/**  
	 * 出院患者信息查询  打印
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	
	@Action(value="printOutPatientMessage")
	public void printOutPatientMessage(){
		try {  
				OutPatientMessageVo vo=new OutPatientMessageVo();
				List<OutPatientMessageVo> list =new  ArrayList<OutPatientMessageVo>(1);
				vo.setList(JSONUtils.fromJson(reportJson, new TypeToken<List<OutPatientMessageVo>>(){}));
				list.add(vo);
				HttpServletRequest request=ServletActionContext.getRequest();
				String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
				String root_path = request.getSession().getServletContext().getRealPath("/");
				String reportFilePath = root_path + webPath +"KSCWXXCX.jasper";
			  	Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("SUBREPORT_DIR", root_path + webPath);
			  	iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,new JRBeanCollectionDataSource(list));
		} catch (Exception e){
			logger.error("KSTJ_CYHZXXCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_KSCWXXCX", "科室统计_科室床位信息查询", "2", "0"), e);
		}
	}
	
	/**  
	 * 出院患者信息查询  导出
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Action(value="exportOutPatientMessage")
	public void exportOutPatientMessage(){
		try {  
			//创建workbook   
		       HttpServletResponse response=ServletActionContext.getResponse();
		       
		       List<OutPatientMessageVo> list = JSONUtils.fromJson(exportJson, new TypeToken<List<OutPatientMessageVo>>(){});
		       if (list == null || list.isEmpty()) {
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
				}
		       
		       HSSFWorkbook workbook = new HSSFWorkbook();   
		       //创建sheet页  
		       HSSFSheet sheet = workbook.createSheet(HisParameters.PREFIXFILENAME+"科室床位信息查询");   
		       //创建单元格  
		       HSSFRow row = sheet.createRow(0);   
		       HSSFCell c0 = row.createCell(0);   
		       c0.setCellValue(new HSSFRichTextString("住院流水号"));   
		       HSSFCell c1 = row.createCell(1);   
		       c1.setCellValue(new HSSFRichTextString("患者姓名"));   
		       HSSFCell c2 = row.createCell(2);   
		       c2.setCellValue(new HSSFRichTextString("性别"));   
		       HSSFCell c3 = row.createCell(3);   
		       c3.setCellValue(new HSSFRichTextString("年龄"));   
		       HSSFCell c4 = row.createCell(4);   
		       c4.setCellValue(new HSSFRichTextString("床位"));   
		       HSSFCell c5 = row.createCell(5);   
		       c5.setCellValue(new HSSFRichTextString("主治医师"));   
		       HSSFCell c6 = row.createCell(6);   
		       c6.setCellValue(new HSSFRichTextString("主管护士"));   
		       HSSFCell c7 = row.createCell(7);   
		       c7.setCellValue(new HSSFRichTextString("入院日期"));   
		       HSSFCell c8 = row.createCell(8);   
		       c8.setCellValue(new HSSFRichTextString("出院日期"));   
		       HSSFCell c9 = row.createCell(9);   
		       c9.setCellValue(new HSSFRichTextString("出院状态"));   
		       HSSFCell c10 = row.createCell(10);   
		       c10.setCellValue(new HSSFRichTextString("结算方式"));   
		       HSSFCell c11 = row.createCell(11);   
		       c11.setCellValue(new HSSFRichTextString("诊断名称"));   
		       HSSFCell c12 = row.createCell(12);   
		       c12.setCellValue(new HSSFRichTextString("科室名称"));   
		       int j=1;
		       
		       DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		       
		       for(OutPatientMessageVo vo:list){
		       	HSSFRow hrow = sheet.createRow(j);
		       	deptCode=vo.getDeptCode();
		       	hrow.createCell(0).setCellValue(vo.getInpatientNo());
		       	hrow.createCell(1).setCellValue(vo.getPatientName());
		       	hrow.createCell(2).setCellValue(vo.getSex());
		       	hrow.createCell(3).setCellValue(vo.getAgeunit());
		       	hrow.createCell(4).setCellValue(vo.getBedName());
		       	hrow.createCell(5).setCellValue(vo.getDocName());
		       	hrow.createCell(6).setCellValue(vo.getNurseName());
		       	hrow.createCell(7).setCellValue(vo.getInDate()==null?"":df.format(vo.getInDate()));
		       	hrow.createCell(8).setCellValue(vo.getOutDate()==null?"":df.format(vo.getOutDate()));
		       	if(vo.getOutState()!=null){
		       		hrow.createCell(9).setCellValue(vo.getOutState());
		       	}else{
		       		hrow.createCell(9).setCellValue("");
		       	}
		       	hrow.createCell(10).setCellValue(vo.getPactCode());
		       	hrow.createCell(11).setCellValue(vo.getDiagName());
		       	hrow.createCell(12).setCellValue(vo.getDeptCode());
		       		j++;
		       	}
		       	String name =HisParameters.PREFIXFILENAME+"科室床位信息查询";
		       	String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".xls";
				String filename = new String(fileName.getBytes(),"ISO8859-1");
				response.setHeader("Content-Disposition", "attachment;filename="+filename);
				ServletOutputStream out=response.getOutputStream();
				workbook.write(out);
		} catch (Exception e){
			logger.error("KSTJ_KSCWXXCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_KSCWXXCX", "科室统计_科室床位信息查询", "2", "0"), e);
		}
	}
}
