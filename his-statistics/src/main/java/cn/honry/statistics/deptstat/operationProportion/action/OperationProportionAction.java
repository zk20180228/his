package cn.honry.statistics.deptstat.operationProportion.action;


import java.io.File;
import java.text.DecimalFormat;
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

import net.sf.jasperreports.engine.JRDataSource;
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
import cn.honry.inner.system.userMenuDataJuris.service.DataJurisInInterService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.bi.bistac.monthlyDashboard.service.MonthlyDashboardService;
import cn.honry.statistics.deptstat.operationProportion.service.OperationProportionService;
import cn.honry.statistics.deptstat.operationProportion.vo.OperationProportionReportVo;
import cn.honry.statistics.deptstat.operationProportion.vo.OperationProportionVo;
import cn.honry.statistics.finance.chargeBill.action.ChargeBillAction;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;

/***
 * 盘点日志查询(统计)
 * @Description:
 * @author: zpty
 * @CreateDate: 2016年6月22日 
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/deptstat/operationProportion")
public class OperationProportionAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(ChargeBillAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	@Autowired
	@Qualifier(value = "operationProportionService")
	private OperationProportionService operationProportionService;
	public void setOperationProportionService(
			OperationProportionService operationProportionService) {
		this.operationProportionService = operationProportionService;
	}
	private MonthlyDashboardService monthlyDashboardService;
	public void setMonthlyDashboardService(
			MonthlyDashboardService monthlyDashboardService) {
		this.monthlyDashboardService = monthlyDashboardService;
	}
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
//	private DeptInInterService deptInInterService;
//	public void setDeptInInterService(DeptInInterService deptInInterService) {
//		this.deptInInterService = deptInInterService;
//	}
	
	@Autowired
	@Qualifier(value = "dataJurisInInterService")
	private DataJurisInInterService dataJurisInInterService;
	public void setDataJurisInInterService(DataJurisInInterService dataJurisInInterService) {
		this.dataJurisInInterService = dataJurisInInterService;
	}
	
	/**
	 * 查询科室
	 */
	private String dept;
	/**
	 * 开始时间
	 */
	private String Stime;
	/**
	 * 结束时间
	 */
	private String Etime;
	/**
	 * 药品
	 */
	private String drug;
	/**
	 * 分页工具
	 */
	private String page;
	private String rows;
	
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
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
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
	public String getDrug() {
		return drug;
	}
	public void setDrug(String drug) {
		this.drug = drug;
	}
	@RequiresPermissions(value={"BQSHZBTJ:function:view"}) 
	@Action(value = "listOperationProportion", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/operationProportion/operationProportionList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listOperationProportion() {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM");
		Date date = new Date();
		Etime = format.format(date);
		return "list";
	}
	@Action(value = "queryOperationProportion")
	public void queryOperationProportion(){
	    try {
	    if(StringUtils.isBlank(page)){
	    	page="1";
	    }
	    if(StringUtils.isBlank(rows)){
	    	rows="20";
	    }
		Stime+="-01";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(Stime);
		// 获取Calendar
		Calendar calendar = Calendar.getInstance();
		// 设置时间,当前时间不用设置
		calendar.setTime(date);
		// 设置日期为本月最大日期
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
	    Etime = format.format(calendar.getTime());
	    Map<String,String> map=operationProportionService.queryDeptMap();
	    List<String> codeList=new ArrayList<String>();
	    if(StringUtils.isNotBlank(dept)){
	    	String [] arr=dept.split(",");
	    	for(String c:arr){
	    		dept+="'"+c+"',";
	    		codeList.add(c);
	    	}
	    	dept=dept.substring(0,dept.length()-1);
	    }
	    List<OperationProportionVo> list=new ArrayList<OperationProportionVo>();
	    Boolean b = monthlyDashboardService.isCollection("SSZBTJ");
	    if(b){
	    	list=operationProportionService.queryOperationProportionFromDB(Stime.substring(0, 7),codeList,page,rows);
	    }else{
	    	
	        list=operationProportionService.queryOperationProportion(Stime,Etime,dept,page,rows);
		//获取deptMap
		for(OperationProportionVo vo:list){
			vo.setDeptName(map.get(vo.getDeptCode()));
			if(vo.getTotal()==0){
				vo.setProportion(-1.0);
			}else{
				DecimalFormat df = new DecimalFormat("#.00");
				vo.setProportion(Double.valueOf(df.format((((double)vo.getTotal2())/vo.getTotal())*100)));
			}
		}
	    }
	    int total=0;
		String json=JSONUtils.toJson(list);
		String json2="{\"total\":" +total+ ",\"rows\":" + json + "}";
		WebUtils.webSendJSON(json2);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("KSTJ_SSZBTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_SSZBTJ", "科室统计_手术占比统计", "2", "0"), e); 
		}
	}
	/**
	 * 手术占比统计数据初始化
	 */
	@Action(value = "saveOperationProportionToDB")
	public void saveOperationProportionToDB() {
		try {
		Stime="2003-01-01";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(Stime);
		// 获取Calendar
		Calendar calendar = Calendar.getInstance();
		// 设置时间,当前时间不用设置
		calendar.setTime(date);
		// 设置日期为本月最大日期
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
	    Etime = format.format(calendar.getTime());
	    while(format.parse(Stime).before(new Date())){
	    	operationProportionService.saveOrUpdateToDB(Stime,Etime);
	    	calendar.setTime(format.parse(Etime));
	    	calendar.add(Calendar.MONTH, +1);
	    	calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
	    	Stime = format.format(calendar.getTime());
	    	calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	    	Etime = format.format(calendar.getTime());
	    }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("KSTJ_SSZBTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_SSZBTJ", "科室统计_手术占比统计", "2", "0"), e); 
		}
	}
	/**
	 * 导出
	 */
	@Action(value = "exportList")
	public void exportList() {
		try {
			 String rows = request.getParameter("rows");
			 List<OperationProportionVo> list = JSONUtils.fromJson(rows, new TypeToken<List<OperationProportionVo>>(){});
			 if (list == null || list.isEmpty()) {
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
			}
//			String head = "";
			String name = "手术占比统计";
//			String[] headMessage = { "科室编码", "科室名称", "出院人数", "转出人数","手术人次数", "手术占比(%)"};
//			for (String message : headMessage) {
//				head += "," + message;
//			}
			   HSSFWorkbook workbook = new HSSFWorkbook();   
		       //创建sheet页  
		       HSSFSheet sheet = workbook.createSheet(HisParameters.PREFIXFILENAME+name);   
		       //创建单元格  
		       HSSFRow row = sheet.createRow(0);   
		       HSSFCell c0 = row.createCell(0);   
		       c0.setCellValue(new HSSFRichTextString("科室编码"));   
		       HSSFCell c1 = row.createCell(1);   
		       c1.setCellValue(new HSSFRichTextString("科室名称"));   
		       HSSFCell c2 = row.createCell(2);   
		       c2.setCellValue(new HSSFRichTextString("出院人数"));   
		       HSSFCell c3 = row.createCell(3);   
		       c3.setCellValue(new HSSFRichTextString("转出人数"));  
		       
		       HSSFCell c4 = row.createCell(4);   
		       c4.setCellValue(new HSSFRichTextString("手术人次数")); 
		       HSSFCell c5 = row.createCell(5);   
		       c5.setCellValue(new HSSFRichTextString("手术占比(%)")); 
		       int j=1;
		       for(OperationProportionVo model:list){
		       	HSSFRow hrow = sheet.createRow(j);
//		       	deptCode=vo.getDeptCode();
		       	hrow.createCell(0).setCellValue(model.getDeptCode()==null?"":model.getDeptCode());
		       	hrow.createCell(1).setCellValue(CommonStringUtils.trimToEmpty(model.getDeptName()));
		       	hrow.createCell(2).setCellValue(model.getTotal());
		       	hrow.createCell(3).setCellValue(model.getTotal1());
		       	
		       	hrow.createCell(4).setCellValue(model.getTotal2());
		       	hrow.createCell(5).setCellValue(model.getProportion());
		       		j++;
		       	}
		       
		       	String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".xls";
				String filename = new String(fileName.getBytes(),"ISO8859-1");
				response.setHeader("Content-Disposition", "attachment;filename="+filename);
				
				ServletOutputStream out=response.getOutputStream();
				workbook.write(out);
			
//			head = head.substring(1);
//			FileUtil fUtil = new FileUtil();
//			String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
//			String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/" + fileName;
//			fUtil.setFilePath(filePath);
//			fUtil.write(head);
//			fUtil = operationProportionService.exportList(list, fUtil);
//			fUtil.close();
//			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("KSTJ_SSZBTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_SSZBTJ", "科室统计_手术占比统计", "2", "0"), e); 
		}
	}
	/**
	 *  打印
	 */
	@Action(value="printList")
	public void printList(){
		 try{
			   //jasper文件名称 不含后缀
			   String rows=request.getParameter("rows");
			   String firstData = request.getParameter("start");
			   String deptName = request.getParameter("dept");
			   if(StringUtils.isBlank(deptName)){
				   deptName="外科";
			   }
			   String endData = request.getParameter("end");
			   String year="";
			   String month="";
			   if(StringUtils.isNotBlank(firstData)){
				   String [] arr =firstData.split("-");
				   year=arr[0];
				   month=arr[1];
			   }
			   String root_path = request.getSession().getServletContext().getRealPath("/");
			   root_path = root_path.replace('\\', '/');
			   String reportFilePath = root_path + webPath +"SSZBTJ.jasper";
			   List<OperationProportionVo> list =JSONUtils.fromJson(rows, new TypeToken<List<OperationProportionVo>>(){});
			   //javaBean数据封装
			   ArrayList<OperationProportionReportVo> voList = new ArrayList<OperationProportionReportVo>();
			   OperationProportionReportVo vo = new OperationProportionReportVo();
			   vo.setItemList(list);
			   voList.add(vo);
			   JRDataSource jrd=new JRBeanCollectionDataSource(voList);
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   parameters.put("firstData", firstData.replace("-", "/"));
			   parameters.put("endData", endData.replace("-", "/"));
			   deptName=year+"年"+month+"月"+"手术占比";
			   parameters.put("deptName", deptName);
			   parameters.put("SUBREPORT_DIR", root_path + webPath);
			   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
			  }catch(Exception ex){
			     ex.printStackTrace();
			     logger.error("KSTJ_SSZBTJ", ex);
				 hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_SSZBTJ", "科室统计_手术占比统计", "2", "0"), ex); 
			  }
	}
}
