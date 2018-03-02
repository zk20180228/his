package cn.honry.statistics.drug.drugDoseCensus.action;

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

import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.drug.drugDoseCensus.service.DrugDoseCensusService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 住院发药量统计action
 * @author  lyy
 * @createDate： 2016年6月20日 上午11:58:10 
 * @modifier lyy
 * @modifyDate：2016年6月20日 上午11:58:10
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/DrugDoseCensus")
public class DrugDoseCensusAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(DrugDoseCensusAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	private DrugDoseCensusService drugDoseService;
	@Autowired
	@Qualifier(value="drugDoseCensusService")
	public void setDrugDoseService(DrugDoseCensusService drugDoseService) {
		this.drugDoseService = drugDoseService;
	}
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}

	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	/**
	 * 开始时间
	 */
	private String startData;
	/**
	 * 结束时间
	 */
	private String endData;
	/**
	 * 住院药房
	 */
	private String drugstore;
	/**
	 * 分页页数
	 */
	private String page;
	/**
	 * 分页条数
	 */
	private String rows;
	private String startTime;
	private String endTime;
	private String menuAlias;
	private String deptCode;//科室
	private String reportJson;//打印
	private String exportJson;//导出
	
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
	public String getDataTime() {
		return dataTime;
	}
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
	/**
	 * 下拉框即使查询参数q
	 */
	private String q;
	/**
	 * 获得当前时间
	 */
	private String dataTime;
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getStartData() {
		return startData;
	}
	public void setStartData(String startData) {
		this.startData = startData;
	}
	public String getEndData() {
		return endData;
	}
	public void setEndData(String endData) {
		this.endData = endData;
	}
	public String getDrugstore() {
		return drugstore;
	}
	public void setDrugstore(String drugstore) {
		this.drugstore = drugstore;
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
	@Action(value = "listDrugDoseCensus", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/drugDoseCensus/drugDoseCensusList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listDrugDoseCensus() {
		Date date = new Date();
		endData = DateUtils.formatDateY_M_D(date);
		startData= DateUtils.formatDateYM(date)+"-01";
		return "list";
	}
	/**
	 * 分页查询住院统计工作量
	 * @author  lyy
	 * @createDate： 2016年6月21日 上午10:38:34 
	 * @modifier lyy
	 * @modifyDate：2016年6月21日 上午10:38:34
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value="queryDrugDoseCensus")
	public void queryDrugDoseCensus(){
		try{
			if(StringUtils.isBlank(startData)){
				Date date = new Date();
				startData = DateUtils.formatDateYM(date)+"-01 00:00:00";
			}
			if(StringUtils.isBlank(endData)){
				Date date = new Date();
				endData = DateUtils.formatDateY_M_D(date)+" 23:59:59";
			}
			List<DrugOutstore> list=drugDoseService.getPageDrugDose(startData,endData,drugstore,page,rows,null);
			int total=drugDoseService.getTatalDrugDose(startData,endData,drugstore,null);
			Map<Object,Object> map=new HashMap<Object,Object>();
			map.put("total", total);
			map.put("rows",list);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_ZYFYGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYFYGZLTJ", "住院统计_住院发药工作量统计", "2", "0"), e);
		}
	}
	
	/**
	 * 打印住院统计工作量
	 * @author  lyy
	 * @createDate： 2016年6月21日 上午10:38:34 
	 * @modifier lyy
	 * @modifyDate：2016年6月21日 上午10:38:34
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value="printDrugDoseCensus")
	public void printDrugDoseCensus(){
		try {
//			if(StringUtils.isBlank(startData)){
//				Date date = new Date();
//				startData = DateUtils.formatDateYM(date)+"-01 00:00:00";
//			}
//			if(StringUtils.isBlank(endData)){
//				Date date = new Date();
//				endData = DateUtils.formatDateY_M_D(date)+" 23:59:59";
//			}
//			
//			Map<String, String> map = drugDoseService.getStoreDEptMap();
			
//			StatVo statVo =drugDoseService.findMaxMin();
			List<DrugOutstore> list= new ArrayList<DrugOutstore>();
			DrugOutstore vo=new DrugOutstore();
			List<DrugOutstore> list2 =JSONUtils.fromJson(reportJson, new TypeToken<List<DrugOutstore>>(){});
//				for (int i = 0; i < list2.size(); i++) {
//					list2.get(i).setDrugDeptCode(map.get(list2.get(i).getDrugDeptCode()));
//				}
			vo.setDrugOutstoreList(list2);
			list.add(vo);
			request=ServletActionContext.getRequest();
			String root_path = request.getSession().getServletContext().getRealPath("/");
			 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/ZYFYFZLTJ.jasper";
			 HashMap<String, Object> parameters = new HashMap<String, Object>();
			 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
			 parameters.put("firstTime", startData);
			 parameters.put("endTime", endData);
			 parameters.put("deptName", drugstore);
			iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(list));
		} catch (Exception e) {
			logger.error("ZYTJ_ZYFYGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYFYGZLTJ", "住院统计_住院发药工作量统计", "2", "0"), e);
			e.printStackTrace();
		}


	}
	/**
	 * 住院科室
	 * @author  lyy
	 * @createDate： 2016年6月21日 上午10:39:01 
	 * @modifier lyy
	 * @modifyDate：2016年6月21日 上午10:39:01
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value="queryStoreDept")
	public void queryStoreDept(){
		try{
			List<SysDepartment> list=drugDoseService.queryStoreDept(q);
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_ZYFYGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYFYGZLTJ", "住院统计_住院发药工作量统计", "2", "0"), e);
		}
	}
	/**
	 * 导出
	 * @author  lyy
	 * @createDate： 2016年6月24日 下午6:42:18 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 下午6:42:18
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value="expDrugDoseCensus")
	public void expDrugDoseCensus(){
		try{
			List<DrugOutstore> list=JSONUtils.fromJson(exportJson, new TypeToken<List<DrugOutstore>>(){});
			if(list==null || list.isEmpty()){
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录!");
			}
			String arrayHead[] ={"床号","姓名","药品名称","规格","频次","用法","总量","最小单位","取药药房","发送类型","摆药单","发送人","发送时间","发药人","发药时间","有效性","拼音码","五笔码","是否摆药"};
		    HSSFWorkbook workbook = new HSSFWorkbook();   
		       //创建sheet页  
		       HSSFSheet sheet = workbook.createSheet(HisParameters.PREFIXFILENAME+"住院发药量统计");   
		       //创建单元格  
		       HSSFRow row = sheet.createRow(0);
		       for(int i=0,len=arrayHead.length;i<len;i++){
		    	   HSSFCell c = row.createCell(i);   
			       c.setCellValue(new HSSFRichTextString(arrayHead[i])); 
		       }
		       
		         
		       int j=1;
		       for(DrugOutstore vo:list){
		       	HSSFRow hrow = sheet.createRow(j);
		       	hrow.createCell(0).setCellValue(vo.getBedName());
		       	hrow.createCell(1).setCellValue(vo.getName());
		       	hrow.createCell(2).setCellValue(vo.getTradeName());
		       	hrow.createCell(3).setCellValue(vo.getSpecs());
		    	hrow.createCell(4).setCellValue(vo.getDfqFreq());
		       	hrow.createCell(5).setCellValue(vo.getUseName());
		       	hrow.createCell(6).setCellValue(vo.getNum());
		       	hrow.createCell(7).setCellValue(vo.getMinUnit());
		       	hrow.createCell(8).setCellValue(vo.getDrugDeptCode());
		       	hrow.createCell(9).setCellValue(vo.getSendType());
		       	hrow.createCell(10).setCellValue(vo.getBillclassCode());
		       	hrow.createCell(11).setCellValue(vo.getApplyOpercode());
		       	hrow.createCell(12).setCellValue(vo.getApplyDate());
		       	hrow.createCell(13).setCellValue(vo.getPrintEmpl());
		       	hrow.createCell(14).setCellValue(vo.getApplyDate());
		       	hrow.createCell(15).setCellValue(vo.getValidState());
		       	hrow.createCell(16).setCellValue(vo.getNamePinyin());
		       	hrow.createCell(17).setCellValue(vo.getNameWb());
		       	hrow.createCell(18).setCellValue(vo.getNotPrintDate());
		       		j++;
		       	}
		       
		       	String name =HisParameters.PREFIXFILENAME+"住院发药量统计";
		       	String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".xls";
				String filename = new String(fileName.getBytes(),"ISO8859-1");
				response.setHeader("Content-Disposition", "attachment;filename="+filename);
				ServletOutputStream out=response.getOutputStream();
				workbook.write(out);
			
//			for (String message: arrayHead) {
//				head+=","+message;
//			}
//			head=head.substring(1);
//			FileUtil fUtil=new FileUtil();
//			String fileName=name+ DateUtils.formatDateY_M_D_H_M(new Date())+ ".csv";
//			String filePath=ServletActionContext.getServletContext().getRealPath("/WEB-INF")+fileName;
//			fUtil.setFilePath(filePath);
//			fUtil.write(head);
//			fUtil=drugDoseService.export(list,fUtil);
//			fUtil.close();
//			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME+fileName);
		}catch (Exception e) {
			logger.error("ZYTJ_ZYFYGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYFYGZLTJ", "住院统计_住院发药工作量统计", "2", "0"), e);
		}
	}
	
	/**  
	 * 分页查询住院统计工作量
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月21日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月21日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	
	@SuppressWarnings("deprecation")
	@Action(value="queryDrugDoseCen")
	public void queryDrugDoseCen(){
		try {
			if(StringUtils.isBlank(startData)){
				Date date = new Date();
				startData = DateUtils.formatDateYM(date)+"-01 00:00:00";
			}

			if(StringUtils.isBlank(endData)){
				Date date = new Date();
				endData = DateUtils.formatDateY_M_D(date)+" 23:59:59";
			}
			List<DrugOutstore> drugDoseCenList = drugDoseService.queryDrugDoseCen(startData,endData,deptCode,menuAlias,page,rows);
			String redKey=menuAlias+":"+startData+"_"+endData+"_"+deptCode;
			Integer totalNum = (Integer) redisUtil.get(redKey);
			if(totalNum==null){
			 	totalNum =drugDoseService.getTotalDrugDoseCen(startData,endData,deptCode,menuAlias);
				redisUtil.set(redKey, totalNum);
			}
			String val=parameterInnerService.getparameter("deadTime");
			if(StringUtils.isNotBlank(val)){
				redisUtil.expire(redKey,Integer.valueOf(val));
			}else{
				redisUtil.expire(redKey, 300);
			}
			Map<Object, Object> map=new HashMap<Object,Object>();
			map.put("total",totalNum);
			map.put("rows", drugDoseCenList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("ZYTJ_ZYFYGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYFYGZLTJ", "住院统计_住院发药工作量统计", "2", "0"), e);
		}
	}
}
