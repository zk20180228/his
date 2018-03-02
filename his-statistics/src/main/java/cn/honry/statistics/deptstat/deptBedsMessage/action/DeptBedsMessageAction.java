package cn.honry.statistics.deptstat.deptBedsMessage.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
import cn.honry.statistics.deptstat.deptBedsMessage.service.DeptBedsMessageService;
import cn.honry.statistics.deptstat.deptBedsMessage.vo.DeptBedsMessageVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;


@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/deptBedsMessage")
@SuppressWarnings({"all"})
public class DeptBedsMessageAction extends ActionSupport{
	private static final long serialVersionUID = 8535596438329082479L;
	@Autowired
	@Qualifier(value = "deptBedsMessageService")
	private DeptBedsMessageService deptBedsMessageService;
	public void setDeptBedsMessageService(
			DeptBedsMessageService deptBedsMessageService) {
		this.deptBedsMessageService = deptBedsMessageService;
	}
	
	private Logger logger=Logger.getLogger(DeptBedsMessageAction.class);
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
	/**
	 * 跳转到科室床位信息查询页面
	 * @return
	 */
	@RequiresPermissions(value={"KSCWXXCX:function:view"})
	@Action(value = "listDeptBedsMessage", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/deptBedsMessage/deptBedsMessage.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listDeptBedsMessage() {
		
		return "list";
	}
	/**  
	 * 科室床位信息查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	
	@Action(value="queryDeptBedsMessage")
	public void queryDeptBedsMessage(){
		try {
			List<DeptBedsMessageVo> deptBedsMessageList = deptBedsMessageService.queryDeptBedsMessage(deptCode,page,rows,menuAlias);
			int total=deptBedsMessageService.getTotalDeptBedsMessage(deptCode,menuAlias);
			Map<Object, Object> map=new HashMap<Object,Object>();
			map.put("total",total);
			map.put("rows", deptBedsMessageList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("KSTJ_KSCWXXCX", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_KSCWXXCX", "科室统计_科室床位信息查询", "2", "0"), e);
		}
	}
	/**  
	 * 科室床位信息查询   打印
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Action(value="printDeptBedsMessage")
	public void printDeptBedsMessage(){
		try {  
				DeptBedsMessageVo vo=new DeptBedsMessageVo();
				List<DeptBedsMessageVo> list =new  ArrayList<DeptBedsMessageVo>(1);
				vo.setList(deptBedsMessageService.queryDeptBedsMessage(deptCode, page, rows,menuAlias));
				list.add(vo);
				HttpServletRequest request=ServletActionContext.getRequest();
				String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
				String root_path = request.getSession().getServletContext().getRealPath("/");
				String reportFilePath = root_path + webPath +"KSCWXXCX.jasper";
			  	Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("SUBREPORT_DIR", root_path + webPath);
			  	iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,new JRBeanCollectionDataSource(list));
		} catch (Exception e){
			logger.error("KSTJ_KSCWXXCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_KSCWXXCX", "科室统计_科室床位信息查询", "2", "0"), e);
		}
	}
	/**  
	 * 科室床位信息查询   导出
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Action(value="exportDeptBedsMessage")
	public void exportDeptBedsMessage(){
		try {  
			//创建workbook   
		       HttpServletResponse response=ServletActionContext.getResponse();
		       
		       List<DeptBedsMessageVo> list = deptBedsMessageService.queryDeptBedsMessage(deptCode, page, "100000", menuAlias);
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
		       c0.setCellValue(new HSSFRichTextString("床位编号"));   
		       HSSFCell c1 = row.createCell(1);   
		       c1.setCellValue(new HSSFRichTextString("房间号"));   
		       HSSFCell c2 = row.createCell(2);   
		       c2.setCellValue(new HSSFRichTextString("床号"));   
		       HSSFCell c3 = row.createCell(3);   
		       c3.setCellValue(new HSSFRichTextString("科室名称"));   
		       HSSFCell c4 = row.createCell(4);   
		       c4.setCellValue(new HSSFRichTextString("床位等级"));   
		       HSSFCell c6 = row.createCell(5);   
		       c6.setCellValue(new HSSFRichTextString("床位状态"));   
		       int j=1;
		       for(DeptBedsMessageVo vo:list){
		       	HSSFRow hrow = sheet.createRow(j);
		       	deptCode=vo.getDeptCode();
		       	hrow.createCell(0).setCellValue(vo.getBedName());
		       	hrow.createCell(1).setCellValue(vo.getBedWardName());
		       	hrow.createCell(2).setCellValue(vo.getBedNum());
		       	hrow.createCell(3).setCellValue(vo.getDeptName());
		       	hrow.createCell(4).setCellValue(vo.getBedLevel());
		       	hrow.createCell(5).setCellValue(vo.getBedState());
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
