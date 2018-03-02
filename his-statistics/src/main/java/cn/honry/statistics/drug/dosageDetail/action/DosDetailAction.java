package cn.honry.statistics.drug.dosageDetail.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.drug.dosageDetail.service.DosDetailService;
import cn.honry.statistics.drug.dosageDetail.vo.DetailToReport;
import cn.honry.statistics.drug.dosageDetail.vo.DetailVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;
/***
 * 门诊配发药工作量统计
 * @ClassName: DosDetailAction 
 * @Description: 
 * @author wfj
 * @date 2016年6月22日 下午7:00:49 
 */

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value="/statistics/DosDetail")
public class DosDetailAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	//hedong 用于报表打印 使用java分隔符 否则 linux系统会找不到相应路径
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	
	@Autowired
	@Qualifier(value="iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	
	//栏目别名（action的参数）
	private String menuAlias;
	
	// 记录异常日志
	private Logger logger = Logger.getLogger(DosDetailAction.class);
	
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	/***
	 * 注入本类Service
	 */
	@Autowired
	@Qualifier(value = "dosDetailService")
	private DosDetailService dosDetailService;
	public void setDosDetailService(DosDetailService dosDetailService) {
		this.dosDetailService = dosDetailService;
	}
	
	//导出excel使用
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();

	/*--------------------------------------------------------- 参数 ------------------------------------------------------------------------*/
	
	//检索方式： 0药房_终端显示 ；1药房_人员显示；2人员检索
	private String typeView;
	
	//检索方式value
	private String typeValue;
	
	//检索开始时间
	private String beginDate;
	
	//检索终止时间
	private String endDate;
	
	private Map<String, Object> map = new HashMap<String, Object>();
	
	//配发药统计 标识 1 配药；2发药
	private String pfbs;
	
	/**
	 * 代表药房或人员code
	 */
	private String code;
		
	/***
	 * view 配药统计视图
	 * @Title: dosageView 
	 * @author  WFJ
	 * @createDate ：2016年6月22日
	 * @return String
	 * @version 1.0
	 */
	@Action(value = "dosageView", results = { @Result(name = "view", location = "/WEB-INF/pages/stat/dosageDetail/dosageView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String dosageView(){
		
		Calendar calendar = new GregorianCalendar(); 
		pfbs = "1";
		calendar.set(Calendar.DAY_OF_MONTH, 1);  
		Date time = calendar.getTime();
		beginDate=DateUtils.formatDateY_M_D(time);
		endDate=DateUtils.formatDateY_M_D(new Date());
		
		return "view";
	}
	
	/***
	 *  view 发药统计视图
	 * @Title: sendDetail 
	 * @author  WFJ
	 * @createDate ：2016年6月23日
	 * @return String
	 * @version 1.0
	 */
	@Action(value = "sendDetail", results = { @Result(name = "view", location = "/WEB-INF/pages/stat/dosageDetail/dosageView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String sendDetail(){
		
		Calendar calendar = new GregorianCalendar(); 
		pfbs = "2";
		calendar.set(Calendar.DAY_OF_MONTH, 1);  
		Date time = calendar.getTime();
		beginDate=DateUtils.formatDateY_M_D(time);
		endDate=DateUtils.formatDateY_M_D(new Date());
		
		return "view";
	}
	
	/**
	 * <p>查询统计分析 </p>
	 * @Author: WFJ
	 * @CreateDate: 2016年6月23日
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月4日 上午9:56:00 
	 * @ModifyRmk:  添加异常处理，修改注释模板
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action(value = "queryDetail")
	public void queryDetail(){
		
		Map<String,Object> retMap = new HashMap<String, Object>();
		try {
			
			//处方数
			Double recipeCount = 0.0;
			
			//处方中药品数量
			Double recipeQty = 0.0;
			
			//处方金额(零售金额)
			Double recipeCost = 0.0;
			
			//处方内药品剂数合计
			Double sumDays = 0.0;
			
			//检索方式： 0药房_终端显示 ；1药房_人员显示；2人员检索
			List<DetailVo> listSto = dosDetailService.queryDetail(typeView, typeValue, beginDate, endDate,pfbs,code);
			for(DetailVo vo : listSto){
				recipeCount += vo.getRecipeCount();
				recipeQty += vo.getRecipeQty();
				recipeCost += vo.getRecipeCost();
				sumDays += vo.getSumDays();
			}		
			
			//保留两位小数
			String mid = NumberUtil.init().format(recipeCost, 2);
			recipeCost=NumberUtil.init().toDoubleOrZero(mid);
			
			
			//2人员检索
			if("2".equals(typeView)){
				DetailVo vo = new DetailVo();
				vo.setDrugedOper("合计");
				vo.setRecipeCount(recipeCount);
				vo.setRecipeQty(recipeQty);
				vo.setRecipeCost(recipeCost);
				vo.setSumDays(sumDays);
				listSto.add(vo);
				
				//1药房_人员显示
			}else if("1".equals(typeView)){
				DetailVo vo = new DetailVo();
				vo.setJobNo("合计");
				vo.setRecipeCount(recipeCount);
				vo.setRecipeQty(recipeQty);
				vo.setRecipeCost(recipeCost);
				vo.setSumDays(sumDays);
				listSto.add(vo);
			}else{
				
				// 0药房_终端显示
				DetailVo vo = new DetailVo();
				vo.setDrugedTerminal("合计");
				vo.setRecipeCount(recipeCount);
				vo.setRecipeQty(recipeQty);
				vo.setRecipeCost(recipeCost);
				vo.setSumDays(sumDays);
				listSto.add(vo);
			}
			
			retMap.put("rows", listSto);
			retMap.put("resMsg", "success");
		} catch (Exception e) {
			
			List<DetailVo> list= new ArrayList<DetailVo>();
			DetailVo vo = new DetailVo();
			vo.setDrugedTerminal("合计");
			vo.setRecipeCount(0.0);
			vo.setRecipeQty(0.0);
			vo.setRecipeCost(0.0);
			vo.setSumDays(0d);
			list.add(vo);
			
			retMap.put("rows", list);
			retMap.put("resMsg", "error");
			
			e.printStackTrace();
			logger.error("TJFXGL_MZP(F)YGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZP(F)YGZLTJ", "门诊统计分析_门诊配(发)药工作量统计", "2","0"), e);
		}
		
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	

	/**
	 * <p>类型为药房的科室下拉</p>
	 * @Author:  WFJ
	 * @CreateDate: 2016年6月23日 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月4日 上午10:01:40 
	 * @ModifyRmk:  添加异常，修改注释模板
	 * @version: V1.0
	 * @throws:
	 *
	 */
	
	@Action(value = "deptForType")
	public void deptForType(){
		
		List<SysDepartment> list =null;
		try {
			list = dosDetailService.deptForType("P");
		} catch (Exception e) {
			
			//保证发生异常时，可以正常响应空内容
			list = new ArrayList<SysDepartment>();
			
			e.printStackTrace();
			logger.error("TJFXGL_MZP(F)YGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZP(F)YGZLTJ", "门诊统计分析_门诊配(发)药工作量统计", "2","0"), e);
		}
		
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	

	/**
	 * <p> 导出excel操作</p>
	 * @Author:  WFJ
	 * @CreateDate: 2016年6月25日 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月4日 上午10:05:44 
	 * @ModifyRmk:  修改注释模板，添加异常处理
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action(value = "exportExcel", results = { @Result(name = "json", type = "json") })
	public void exportExcel() {

		//处方数
		Double recipeCount = 0.0;
		
		//处方中药品数量
		Double recipeQty = 0.0;
		
		//处方金额(零售金额)
		Double recipeCost = 0.0;
		
		//处方内药品剂数合计
		Double sumDays = 0.0;
		List<DetailVo> listSto = dosDetailService.queryDetail(typeView, typeValue, beginDate, endDate,pfbs,code);
		if (listSto == null || listSto.isEmpty()) {
			response.setContentType("text/html;charset=utf-8");
			try {
				response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
			} catch (Exception e) {
				
				e.printStackTrace();
				logger.error("TJFXGL_MZPYGZLTJ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZPYGZLTJ", "门诊统计分析_门诊配药工作量统计", "2","0"), e);
			}
		}else{
			for(DetailVo vo : listSto){
				recipeCount += vo.getRecipeCount();
				recipeQty += vo.getRecipeQty();
				recipeCost += vo.getRecipeCost();
				sumDays += vo.getSumDays();
			}
			DetailVo vo = new DetailVo();
			vo.setRecipeCount(recipeCount);
			vo.setRecipeQty(recipeQty);
			vo.setRecipeCost(recipeCost);
			vo.setSumDays(sumDays);
			listSto.add(vo);
			
			String head = "";
			String name ;
			
			if("1".equals(pfbs)){
				name = "门诊配药工作量统计";
			}else{
				name = "门诊发药工作量统计";
			}
			
			List<String> liststr = new ArrayList<String>();
			if("0".equals(typeView)){
				if("1".equals(pfbs)){
					liststr.add("配药台名称");
				}else{
					liststr.add("发药窗口名称");
				}
				liststr.add("处方数");
				liststr.add("药品种类");
				liststr.add("处方金额");
				liststr.add("剂数");
				
				listSto.get((listSto.size()-1)).setDrugedTerminal("合计：");
			}else if("1".equals(typeView)){
				liststr.add("工号");
				if("1".equals(pfbs)){
					liststr.add("配药员");
				}else{
					liststr.add("发药员");
				}
				liststr.add("处方数");
				liststr.add("药品种类");
				liststr.add("处方金额");
				liststr.add("剂数");
				
				listSto.get((listSto.size()-1)).setJobNo("合计：");
			}else if("2".equals(typeView)){
				if("1".equals(pfbs)){
					liststr.add("配药人");
					liststr.add("配药台名称");
				}else{
					liststr.add("发药人");
					liststr.add("发药窗口名称");

				}
				liststr.add("处方数");
				liststr.add("药品种类");
				liststr.add("处方金额");
				liststr.add("剂数");
				
				listSto.get((listSto.size()-1)).setDrugedOper("合计：");
			}
			for (String message : liststr) {
				head += "," + message;
			}
			head = head.substring(1);
			
			FileUtil fUtil = new FileUtil();
			String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
			String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/" + fileName;
			try {
				fUtil.setFilePath(filePath);
				fUtil.write(head);
				
				fUtil = dosDetailService.export(listSto, fUtil,typeView);
				fUtil.close();
				DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
			} catch (Exception e) {
				
				e.printStackTrace();
				logger.error("TJFXGL_MZP(F)YGZLTJ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZP(F)YGZLTJ", "门诊统计分析_门诊配(发)药工作量统计", "2","0"), e);
			}
		}
		
		
	}
	
	
	/**
	 * 
	 * <p>配药工作量统计报表打印 </p>
	 * @Author:  WFJ
	 * @CreateDate: 2016年6月25日 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月4日 上午10:05:44 
	 * @ModifyRmk:  修改注释模板，添加异常处理
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action(value="iReportDetail")
	public void iReportDetail(){
		 
		try{
		  
			//jasper文件名称 不含后缀
		   String rows = request.getParameter("rows"); 
		   String pfbs = request.getParameter("pfbs");
		   String typeView = request.getParameter("typeView"); 
		   String typeValue = request.getParameter("typeValue");
		   List<DetailVo> list01 = JSONUtils.fromJson(rows, new TypeToken<List<DetailVo>>(){});
		   List<DetailVo> list =new ArrayList<DetailVo>();
		   if(list01!=null){
			   for(DetailVo d: list01){
				   String str = NumberUtil.init().format(d.getRecipeCost(), 2);
				   d.setRecipeCost(Double.parseDouble(str));
				   list.add(d);
			   }
		   }
		   
		   Map<String, Object> parameters = new HashMap<String, Object>();	
		   if ("1".equals(pfbs)) {
			   parameters.put("name", "配药工作量统计单 ");
			}else{
				parameters.put("name", "发药工作量统计单 ");
			}
		   if ("0".equals(typeView)) {
			   parameters.put("typeName", "药房：");
			   if ("1".equals(pfbs)) {
				   parameters.put("drugedTerminal", "配药台名称 ");
				}else{
					parameters.put("drugedTerminal", "发药窗口名称 ");
				}
		   	   
		   	   parameters.put("viewVal", "按终端显示");
		   }
		   if ("1".equals(typeView)) {
			   parameters.put("typeName", "药房：");
			   parameters.put("viewVal", "按人员显示");
			   parameters.put("jobNo", "工号");
			   if ("1".equals(pfbs)) {
				   parameters.put("drugedOper", "配药员");
				}else{
					parameters.put("drugedOper", "发药员");
				} 
		   }
		   if ("2".equals(typeView)) {
			   parameters.put("typeName", "人员：");
			   if ("1".equals(pfbs)) {
				   parameters.put("jobNo", "配药人");
				   parameters.put("drugedOper", "配药台名称 ");
				}else{
					parameters.put("jobNo", "发药人");
					parameters.put("drugedOper", "发药窗口名称 ");
				}
			for (DetailVo detailVO : list) {
				detailVO.setJobNo(detailVO.getDrugedOper());
				detailVO.setDrugedOper(detailVO.getDrugedTerminal());				
			}
		   }
		   
		   //javaBean数据封装
		   ArrayList<DetailToReport> voList = new ArrayList<DetailToReport>();
		   DetailToReport vo = new DetailToReport();
		   vo.setList(list);
		   voList.add(vo);
		   JRDataSource jrd=new JRBeanCollectionDataSource(voList);
		   String fileName = request.getParameter("fileName");
		   String root_path = request.getSession().getServletContext().getRealPath("/");
		   root_path = root_path.replace('\\', '/');
		   String reportFilePath = root_path + webPath +fileName+".jasper";
		   parameters.put("typeValue", typeValue);
		   parameters.put("hName", HisParameters.PREFIXFILENAME);
		   parameters.put("beginDate", beginDate);
		   parameters.put("endDate", endDate);
		   parameters.put("SUBREPORT_DIR", root_path + webPath);
		   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
		  }catch(Exception e) {
			  
				e.printStackTrace();
				logger.error("TJFXGL_MZP(F)YGZLTJ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZP(F)YGZLTJ", "门诊统计分析_门诊配(发)药工作量统计", "2","0"), e);
		  }
		 
	}
	
	
	
/*--------------------------------------------------------- get and set ------------------------------------------------------------------------*/
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getTypeView() {
		return typeView;
	}

	public void setTypeView(String typeView) {
		this.typeView = typeView;
	}
	public String getTypeValue() {
		return typeValue;
	}
	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public String getPfbs() {
		return pfbs;
	}

	public void setPfbs(String pfbs) {
		this.pfbs = pfbs;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
