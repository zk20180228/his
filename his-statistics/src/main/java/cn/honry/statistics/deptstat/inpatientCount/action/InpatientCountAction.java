package cn.honry.statistics.deptstat.inpatientCount.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.system.userMenuDataJuris.service.DataJurisInInterService;
import cn.honry.statistics.deptstat.inpatientCount.service.InpatientCountService;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

/** 
* @ClassName: InpatientCountAction 
* @Description: 在院患者情况
* @author qh
* @date 2017年7月10日
*  
*/
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/deptstat/inpatientCountAction")
public class InpatientCountAction {
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(InpatientCountAction.class);
	@Autowired
	@Qualifier(value = "inpatientCountService")
	private InpatientCountService inpatientCountService;
	public void setInpatientCountService(InpatientCountService inpatientCountService) {
		this.inpatientCountService = inpatientCountService;
	}
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value = "dataJurisInInterService")
	private DataJurisInInterService dataJurisInInterService;
	public void setDataJurisInInterService(DataJurisInInterService dataJurisInInterService) {
		this.dataJurisInInterService = dataJurisInInterService;
	}
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	private String dept;
	private String menuAlias;
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
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	@RequiresPermissions(value={"BQSHZBTJ:function:view"}) 
	@Action(value = "inpatientCountListToView", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/inpatientCountList/inpatientCountList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String inpatientCountListToView() {
		return "list";
	}
	
	/** 
	* @Description: 条件查询员工对象抗菌药物使用权限
	* @author qh
	* @date 2017年7月7日
	*  
	*/
	@Action(value = "queryInpatientCountList")
	public void queryInpatientCountList() {
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
				dept=dept.substring(0,dept.length()-1);
			}
			dept="'"+dept.replace(",", "','")+"'";
			List<InpatientInfoNow> 	list=inpatientCountService.queryInpatientCountList(dept,page,rows);
//			if(list!=null&&list.size()>0){
//				for(InpatientInfoNow vo:list){
//					vo.setReportAgeunit(vo.getReportAge()+vo.getReportAgeunit());
//				}
//			}
			int total=inpatientCountService.queryTotal(dept);
			String json=JSONUtils.toJson(list);
			String json2="{\"total\":" +total+ ",\"rows\":" + json + "}";
			request.getSession().setAttribute("total", total);
			WebUtils.webSendJSON(json2);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("KSTJ_ZYHZQK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_ZYHZQK", "科室统计_在院患者情况", "2", "0"), e); 
		}
	}
	
	/** 
	* @Description: 导出功能
	* @author qh
	* @date 2017年7月11日
	*  
	*/
	@Action(value = "exportList")
	public void exportList(){
		try {
			 String rows = request.getParameter("rows");
			 List<InpatientInfoNow> list = JSONUtils.fromJson(rows, new TypeToken<List<InpatientInfoNow>>(){});
			 if (list == null || list.isEmpty()) {
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
			}
//			String head = "";
//			String name = "在院患者情况";
//			String[] headMessage = { "住院号", "姓名", "性别", "年龄","科室","床位号","主任医师","主治医师","入院时间"};
			
			
			  HSSFWorkbook workbook = new HSSFWorkbook();   
		       //创建sheet页  
		       HSSFSheet sheet = workbook.createSheet(HisParameters.PREFIXFILENAME+"在院患者情况");   
		       //创建单元格  
		       HSSFRow row = sheet.createRow(0);   
		       HSSFCell c0 = row.createCell(0);   
		       c0.setCellValue(new HSSFRichTextString("住院号"));   
		       HSSFCell c1 = row.createCell(1);   
		       c1.setCellValue(new HSSFRichTextString("姓名"));   
		       HSSFCell c2 = row.createCell(2);   
		       c2.setCellValue(new HSSFRichTextString("性别"));   
		       HSSFCell c3 = row.createCell(3);   
		       c3.setCellValue(new HSSFRichTextString("年龄"));   
		       HSSFCell c4 = row.createCell(4);   
		       c4.setCellValue(new HSSFRichTextString("科室"));   
		       HSSFCell c6 = row.createCell(5);   
		       c6.setCellValue(new HSSFRichTextString("床位号"));  
		       
		       HSSFCell c7 = row.createCell(6);   
		       c7.setCellValue(new HSSFRichTextString("主任医师"));  
		       HSSFCell c8 = row.createCell(7);   
		       c8.setCellValue(new HSSFRichTextString("主治医师"));  
		       HSSFCell c9 = row.createCell(8);   
		       c9.setCellValue(new HSSFRichTextString("入院时间"));  
		       int j=1;
		       SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		       for(InpatientInfoNow model:list){
		       	HSSFRow hrow = sheet.createRow(j);
//		       	deptCode=vo.getDeptCode();
		       	hrow.createCell(0).setCellValue(CommonStringUtils.trimToEmpty(model.getInpatientNo()));
		       	hrow.createCell(1).setCellValue(CommonStringUtils.trimToEmpty(model.getPatientName()));
		       	hrow.createCell(2).setCellValue(CommonStringUtils.trimToEmpty(model.getReportSexName()));
		       	hrow.createCell(3).setCellValue(CommonStringUtils.trimToEmpty(model.getReportAge()==null?"":model.getReportAge()+model.getReportAgeunit()));
		       	hrow.createCell(4).setCellValue(CommonStringUtils.trimToEmpty(model.getDeptName()));
		       	hrow.createCell(5).setCellValue(CommonStringUtils.trimToEmpty(model.getBedName()));
		       	
		       	hrow.createCell(6).setCellValue(CommonStringUtils.trimToEmpty(model.getChiefDocName()));
		       	hrow.createCell(7).setCellValue(CommonStringUtils.trimToEmpty(model.getChargeDocName()));
		       	hrow.createCell(8).setCellValue(format.format(model.getInDate()));
		       		j++;
		       	}
		       	String name =HisParameters.PREFIXFILENAME+"在院患者情况";
		       	String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".xls";
				String filename = new String(fileName.getBytes(),"ISO8859-1");
				response.setHeader("Content-Disposition", "attachment;filename="+filename);
				ServletOutputStream out=response.getOutputStream();
				workbook.write(out);
			
			
			
//			for (String message : headMessage) {
//				head += "," + message;
//			}
//			head = head.substring(1);
//			FileUtil fUtil = new FileUtil();
//			String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
//			String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/" + fileName;
//			fUtil.setFilePath(filePath);
//			fUtil.write(head);
//			fUtil = inpatientCountService.exportList(list, fUtil);
//			fUtil.close();
//			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("KSTJ_ZYHZQK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_ZYHZQK", "科室统计_在院患者情况", "2", "0"), e); 
		}
	}
}
