package cn.honry.statistics.deptstat.antimicrobialDrugAccess.action;

import java.io.File;
import java.util.ArrayList;
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

import com.google.gson.reflect.TypeToken;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.system.userMenuDataJuris.service.DataJurisInInterService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.deptstat.antimicrobialDrugAccess.service.AntimicrobialDrugAccessService;
import cn.honry.statistics.deptstat.antimicrobialDrugAccess.vo.AntimicrobialDrugAccessReportVo;
import cn.honry.statistics.deptstat.antimicrobialDrugAccess.vo.AntimicrobialDrugAccessVo;
import cn.honry.statistics.deptstat.operationProportion.service.OperationProportionService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

/** 
* @ClassName: AntimicrobialDrugAccessAction 
* @Description: 抗菌药物使用权限
* @author qh
* @date 2017年7月6日
*  
*/
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/deptstat/antimicrobialDrugAccessAction")
public class AntimicrobialDrugAccessAction {
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(AntimicrobialDrugAccessAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	@Autowired
	@Qualifier(value = "antimicrobialDrugAccessService")
	private AntimicrobialDrugAccessService antimicrobialDrugAccessService;

	public void setAntimicrobialDrugAccessService(
			AntimicrobialDrugAccessService antimicrobialDrugAccessService) {
		this.antimicrobialDrugAccessService = antimicrobialDrugAccessService;
	}
	
	@Autowired
	@Qualifier(value = "operationProportionService")
	private OperationProportionService operationProportionService;
	public void setOperationProportionService(
			OperationProportionService operationProportionService) {
		this.operationProportionService = operationProportionService;
	}
	
//	private MonthlyDashboardService monthlyDashboardService;
//	public void setMonthlyDashboardService(
//			MonthlyDashboardService monthlyDashboardService) {
//		this.monthlyDashboardService = monthlyDashboardService;
//	}
	@Autowired
	@Qualifier(value = "dataJurisInInterService")
	private DataJurisInInterService dataJurisInInterService;
	public void setDataJurisInInterService(DataJurisInInterService dataJurisInInterService) {
		this.dataJurisInInterService = dataJurisInInterService;
	}
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	private String dept;
	private String type;
	private String page;
	private String rows;
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
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
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@RequiresPermissions(value={"BQSHZBTJ:function:view"}) 
	@Action(value = "antimicrobialDrugAccessListToView", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/antimicrobialDrugAccess/antimicrobialDrugAccessList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String antimicrobialDrugAccessListToView() {
		return "list";
	}
	/** 
	* @Description: 查询员工类型
	* @author qh
	* @date 2017年7月7日
	*  
	*/
	@Action(value = "queryType")
	public void queryType() {
		try{
			List<AntimicrobialDrugAccessVo> list=antimicrobialDrugAccessService.queryType();
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("KSTJ_KJYWSYQX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_KJYWSYQX", "科室统计_抗菌药物使用权限", "2", "0"), e); 
		}
	}
	/** 
	* @Description: 条件查询员工对象抗菌药物使用权限
	* @author qh
	* @date 2017年7月7日
	*  
	*/
	@Action(value = "queryAntimicrobialDrugAccess")
	public void queryAntimicrobialDrugAccess() {
		try{
			//查询当前用户所有权限
			if(StringUtils.isBlank(dept)){
				User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
				List<SysDepartment> deptByauthority = dataJurisInInterService.getJurisDeptList(menuAlias, user.getAccount());
					dept="";
					for(int i=0,len=deptByauthority.size();i<len;i++){
						if(i>0){
							dept+=",";
						}
						dept+=deptByauthority.get(i).getDeptCode();
					}
			}
			dept="'"+dept.replace(",", "','")+"'";
//			Boolean b = monthlyDashboardService.isCollection("KJYWSYQX");
//			if(b){
//				list=antimicrobialDrugAccessService.queryAntimicrobialDrugAccessFromDB(codeList, page, rows,menuAlias);
//			}else{
			List<AntimicrobialDrugAccessVo> list=antimicrobialDrugAccessService.queryAntimicrobialDrugAccess(dept,page,rows,menuAlias);
//			}
			int total=antimicrobialDrugAccessService.queryAntimicrobialDrugAccessTotal(dept,menuAlias);
			String json=JSONUtils.toJson(list);
			String json2="{\"total\":" +total+ ",\"rows\":" + json + "}";
			WebUtils.webSendJSON(json2);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("KSTJ_KJYWSYQX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_KJYWSYQX", "科室统计_抗菌药物使用权限", "2", "0"), e); 
		}
	}
	/** 
	* @Description: 导出功能
	* @author qh
	* @date 2017年7月7日
	*  
	*/
	@Action(value = "exportList")
	public void exportList() {
		try {
			 String rows = request.getParameter("rows");
			 List<AntimicrobialDrugAccessVo> list = JSONUtils.fromJson(rows, new TypeToken<List<AntimicrobialDrugAccessVo>>(){});
			 if (list == null || list.isEmpty()) {
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
			}
//			String head = "";
			String name = "抗菌药物使用权限";
//			String[] headMessage = { "姓名", "工号", "级别", "抗菌药物使用权限"};
//			for (String message : headMessage) {
//				head += "," + message;
//			}
//			
//			
			   HSSFWorkbook workbook = new HSSFWorkbook();   
		       //创建sheet页  
		       HSSFSheet sheet = workbook.createSheet(HisParameters.PREFIXFILENAME+"抗菌药物使用权限");   
		       //创建单元格  
		       HSSFRow row = sheet.createRow(0);   
		       HSSFCell c0 = row.createCell(0);   
		       c0.setCellValue(new HSSFRichTextString("姓名"));   
		       HSSFCell c1 = row.createCell(1);   
		       c1.setCellValue(new HSSFRichTextString("工号"));   
		       HSSFCell c2 = row.createCell(2);   
		       c2.setCellValue(new HSSFRichTextString("级别"));   
		       HSSFCell c3 = row.createCell(3);   
		       c3.setCellValue(new HSSFRichTextString("抗菌药物使用权限"));   
		       int j=1;
		       for(AntimicrobialDrugAccessVo model:list){
		       	HSSFRow hrow = sheet.createRow(j);
//		       	deptCode=vo.getDeptCode();
		       	hrow.createCell(0).setCellValue(model.getEname()==null?"":model.getEname());
		       	hrow.createCell(1).setCellValue(model.getEcode()==null?"":model.getEcode());
		       	hrow.createCell(2).setCellValue(model.getElevel()==null?"":model.getElevel());
		       	hrow.createCell(3).setCellValue(model.getEaccess()==null?"":model.getEaccess());
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
//			fUtil = antimicrobialDrugAccessService.exportList(list, fUtil);
//			fUtil.close();
//			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("KSTJ_KJYWSYQX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_KJYWSYQX", "科室统计_抗菌药物使用权限", "2", "0"), e); 
		}
	}
	
	/** 
	* @Description: 打印功能
	* @author qh
	* @date 2017年7月7日
	*  
	*/
	@Action(value = "printList")
	public void printList() {
		try{
		   //jasper文件名称 不含后缀
		   String rows=request.getParameter("rows");
		   String root_path = request.getSession().getServletContext().getRealPath("/");
		   root_path = root_path.replace('\\', '/');
		   String reportFilePath = root_path + webPath +"KJYWSYQX.jasper";
		   List<AntimicrobialDrugAccessVo> list = JSONUtils.fromJson(rows, new TypeToken<List<AntimicrobialDrugAccessVo>>(){});
		   
		   //javaBean数据封装
		   ArrayList<AntimicrobialDrugAccessReportVo> voList = new ArrayList<AntimicrobialDrugAccessReportVo>();
		   AntimicrobialDrugAccessReportVo vo = new AntimicrobialDrugAccessReportVo();
		   vo.setItemList(list);
		   voList.add(vo);
		   JRDataSource jrd=new JRBeanCollectionDataSource(voList);
		   Map<String, Object> parameters = new HashMap<String, Object>();
		   String deptName="抗菌药物使用权限";
		   parameters.put("deptName", deptName);
		   parameters.put("SUBREPORT_DIR", root_path + webPath);
		   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
		  }catch(Exception ex){
		     ex.printStackTrace();
		     logger.error("KSTJ_KJYWSYQX", ex);
			 hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_KJYWSYQX", "科室统计_抗菌药物使用权限", "2", "0"), ex); 
		  }
	}
}
