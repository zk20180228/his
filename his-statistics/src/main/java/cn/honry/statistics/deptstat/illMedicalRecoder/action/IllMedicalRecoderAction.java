package cn.honry.statistics.deptstat.illMedicalRecoder.action;

import java.io.File;
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
import cn.honry.statistics.deptstat.illMedicalRecoder.service.IllMedicalRecoderService;
import cn.honry.statistics.deptstat.illMedicalRecoder.vo.IllMedicalRecoderVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * <p>危重病历人数比例统计分析</p>
 * @Author: yuke
 * @CreateDate: 2017年7月6日 下午2:07:02 
 * @Modifier: yuke
 * @ModifyDate: 2017年7月6日 下午2:07:02 
 * @ModifyRmk:  
 * @version: V1.0
 * @param:
 * @throws:
 * @return: 
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/illMedicalRecoder")
@SuppressWarnings({"all"})
public class IllMedicalRecoderAction extends ActionSupport{
	private static final long serialVersionUID = 8535596438329082479L;
	@Autowired
	@Qualifier(value = "illMedicalRecoderService")
	private IllMedicalRecoderService illMedicalRecoderService;
	public void setIllMedicalRecoderService(
			IllMedicalRecoderService illMedicalRecoderService) {
		this.illMedicalRecoderService = illMedicalRecoderService;
	}
	
	private Logger logger=Logger.getLogger(IllMedicalRecoderAction.class);
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
	private String deptCode;
	private String menuAlias;
	private String startTime;
	private String endTime;
	private String reportJson;
	
	public String getReportJson() {
		return reportJson;
	}
	public void setReportJson(String reportJson) {
		this.reportJson = reportJson;
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
	 * 危重病历人数比例统计分析
	 * @return
	 */
	@RequiresPermissions(value={"WZBLRSBLTJFX:function:view"})
	@Action(value = "listIllMedical", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/illMedicalRecoder/illMedicalRecoder.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listIllMedical() {
		// 获取当月第一天至当天时间段
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		endTime=format.format(date);
		startTime=endTime.substring(0, 7)+"-01";
		return "list";
	}
	/**  
	 * 
	 * 危重病历人数比例统计分析
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	
	@Action(value="queryIllMedicalRecoder")
	public void queryIllMedicalRecoder(){
		try {
			List<IllMedicalRecoderVo> IllMedicalRecoderList = illMedicalRecoderService.queryIllMedicalRecoder(deptCode,menuAlias);
			String json = JSONUtils.toJson(IllMedicalRecoderList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("KSTJ_WZBLRSBLTJFX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_WZBLRSBLTJFX", "科室统计_危重病历人数比例统计分析", "2", "0"), e);
		}
	}
	/**打印**/
	@Action(value="printIllMedicalRecoder")
	public void printIllMedicalRecoder(){
		try {  
				IllMedicalRecoderVo vo=new IllMedicalRecoderVo();
				List<IllMedicalRecoderVo> list =new ArrayList<IllMedicalRecoderVo>();
				vo.setList(JSONUtils.fromJson(reportJson, new TypeToken<List<IllMedicalRecoderVo>>(){}));
				list.add(vo);
				HttpServletRequest request=ServletActionContext.getRequest();
				String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
				String root_path = request.getSession().getServletContext().getRealPath("/");
				String reportFilePath = root_path + webPath +"WZBLRSBLTJFX.jasper";
			  	Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("SUBREPORT_DIR", root_path + webPath);
			  	iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,new JRBeanCollectionDataSource(list));
		} catch (Exception e){
			logger.error("KSTJ_WZBLRSBLTJFX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_WZBLRSBLTJFX", "科室统计_危重病历人数比例统计分析", "2", "0"), e);
		}
	}
	@Action(value="exportMedicalRecoder")
	public void exportMedicalRecoder(){
		try {  
			//创建workbook   
		       HttpServletResponse response=ServletActionContext.getResponse();
		       
		       List<IllMedicalRecoderVo> list = JSONUtils.fromJson(reportJson, new TypeToken<List<IllMedicalRecoderVo>>(){});
		       if (list == null || list.isEmpty()) {
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
		       }
		       HSSFWorkbook workbook = new HSSFWorkbook();   
		       //创建sheet页  
		       HSSFSheet sheet = workbook.createSheet(HisParameters.PREFIXFILENAME+"危重病历人数比例统计分析");   
		       //创建单元格  
		       HSSFRow row = sheet.createRow(0);   
		       HSSFCell c0 = row.createCell(0);   
		       c0.setCellValue(new HSSFRichTextString("部门名称"));   
		       HSSFCell c1 = row.createCell(1);   
		       c1.setCellValue(new HSSFRichTextString("数量"));   
		       HSSFCell c2 = row.createCell(2);   
		       c2.setCellValue(new HSSFRichTextString("治愈情况"));   
		       HSSFCell c3 = row.createCell(3);   
		       c3.setCellValue(new HSSFRichTextString("好转"));   
		       HSSFCell c4 = row.createCell(4);   
		       c4.setCellValue(new HSSFRichTextString("未治愈"));   
		       HSSFCell c6 = row.createCell(5);   
		       c6.setCellValue(new HSSFRichTextString("死亡"));   
		       HSSFCell c7 = row.createCell(6);   
		       c7.setCellValue(new HSSFRichTextString("其他"));   
		       HSSFCell c8 = row.createCell(7);   
		       c8.setCellValue(new HSSFRichTextString("治愈率百分比%"));  
		       HSSFCell c9 = row.createCell(8);   
		       c9.setCellValue(new HSSFRichTextString("死亡率百分比%"));   
		       HSSFCell c10 = row.createCell(9);   
		       c10.setCellValue(new HSSFRichTextString("平均住院日"));   
		       HSSFCell c11 = row.createCell(10);   
		       c11.setCellValue(new HSSFRichTextString("平均费用"));  
		       HSSFCell c12 = row.createCell(11);   
		       c12.setCellValue(new HSSFRichTextString("并发症感染"));   
		       HSSFCell c13 = row.createCell(12);   
		       c13.setCellValue(new HSSFRichTextString("全院患者"));   
		       HSSFCell c14 = row.createCell(13); 
		       c14.setCellValue(new HSSFRichTextString("占全院比"));
		       int j=1;
		       for(IllMedicalRecoderVo vo:list){
		       	HSSFRow hrow = sheet.createRow(j);
		       	deptCode=vo.getDeptCode();
		       	hrow.createCell(0).setCellValue(vo.getDeptName());
		       	hrow.createCell(1).setCellValue(vo.getNum());
		       	hrow.createCell(2).setCellValue(vo.getCured());
		       	hrow.createCell(3).setCellValue(vo.getBetter());
		       	hrow.createCell(4).setCellValue(vo.getNoCured());
		       	hrow.createCell(5).setCellValue(vo.getDeath());
		       	hrow.createCell(6).setCellValue(vo.getOther());
		       	hrow.createCell(7).setCellValue(vo.getCureRate());
		       	hrow.createCell(8).setCellValue(vo.getDeathRate());
		       	hrow.createCell(9).setCellValue(vo.getAverInhost());
		       	hrow.createCell(10).setCellValue(vo.getAverFeeCost());
		       	hrow.createCell(11).setCellValue(vo.getCompliInterface());
		       	hrow.createCell(12).setCellValue(vo.getAllIll());
		       	hrow.createCell(13).setCellValue(vo.getThanFloor());
		       		j++;
		       	}
		       	String name =HisParameters.PREFIXFILENAME+"危重病历人数比例统计分析";
		       	String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".xls";
				String filename = new String(fileName.getBytes(),"ISO8859-1");
				response.setHeader("Content-Disposition", "attachment;filename="+filename);
				ServletOutputStream out=response.getOutputStream();
				workbook.write(out);
		} catch (Exception e){
			logger.error("KSTJ_WZBLRSBLTJFX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_WZBLRSBLTJFX", "科室统计_危重病历人数比例统计分析", "2", "0"), e);
		}
		
	}
	
	
	/**  
	 * 
	 * 危重病历人数比例统计分析
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	
	@Action(value="queryIllMedical")
	public void queryIllMedical(){
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
			List<IllMedicalRecoderVo> IllMedicalRecoderList = illMedicalRecoderService.queryIllMedical(startTime,endTime,deptCode,menuAlias);
			String json = JSONUtils.toJson(IllMedicalRecoderList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("KSTJ_WZBLRSBLTJFX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_WZBLRSBLTJFX", "科室统计_危重病历人数比例统计分析", "2", "0"), e);
		}
	}
}
