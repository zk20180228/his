package cn.honry.statistics.finance.statistic.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.finance.statistic.service.StatisticService;
import cn.honry.statistics.finance.statistic.vo.ResultVo;
import cn.honry.statistics.finance.statistic.vo.ResultVoToIReport;
import cn.honry.statistics.finance.statistic.vo.StatisticVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**  
 *  住院收入统计汇总Action
 * @Author:luyanshou
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/Statistic")
@SuppressWarnings("serial")
public class StatisticAction extends ActionSupport{
	
	// 记录异常日志
	private Logger logger = Logger.getLogger(StatisticAction.class);
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	@Autowired
	@Qualifier(value="iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	private StatisticService statisticService;
	@Autowired
	@Qualifier(value="parameterInnerService")
	private ParameterInnerService parameterInnerService;
	
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	
	@Autowired
	@Qualifier(value="statisticService")
	public void setStatisticService(StatisticService statisticService) {
		this.statisticService = statisticService;
	}
	
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	private String reportCode;

	public String getReportCode() {
		return reportCode;
	}
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	
	//开始时间
	private String sTime;
	
	//结束时间
	private String eTime;
	
	//科室
	private String ids;
	
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}
	public String geteTime() {
		return eTime;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	@RequiresPermissions(value={"ZZSRTJHZ:function:view"})
	@Action(value = "statisticView", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/incomeStatistic/incomeStatistic.jsp") },
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String statisticView(){//跳转到收入统计汇总页面
		Date date = new Date();
		eTime = DateUtils.formatDateY_M_D(date);
		sTime =DateUtils.formatDateY_M(date)+"-01";
		ServletActionContext.getRequest().setAttribute("eTime",eTime);
		ServletActionContext.getRequest().setAttribute("sTime",sTime);
		return "list";
	}
	
	/**
	 * 加载统计结果信息列表
	 * @author 朱振坤
	 * 
	 */
	@Action(value="statisticData",results = { @Result(name = "json", type = "json") })
	public void statisticData(){
		try {
			List<SysDepartment> dept = this.deptInInterService
					.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
			String permitIds = "";//该用户拥有的权限，能查看的科室
			String queryIds = "";//过滤后实际查询的科室
			for (int i = 0; i < dept.size(); i++) {
				if (i == 0) {
					permitIds = dept.get(i).getDeptCode();
				} else {
					permitIds = permitIds+","+dept.get(i).getDeptCode();
				}
			}
			if (StringUtils.isBlank(ids)) {//如果科室为空，则获取当前登录用户能查看的科室
				queryIds = permitIds;
			} else {//如果科室不为空，则校验前台传来的科室是否是当前登录用户能查看的科室,取出有权限的科室
				int count = 0;
				String[] idsArray = ids.split(",");
				String[] permitIdsArray = permitIds.split(",");
				for(int i = 0; i < idsArray.length; i++){
					for(int j = 0; j < permitIdsArray.length; j++){
						if(idsArray[i].equals(permitIdsArray[j])){
							if (count == 0) {
								queryIds = idsArray[i];
							} else {
								queryIds = queryIds+","+idsArray[i];
							}
							count++;
						}
					}
				}
			}
			List<ResultVo> list = statisticService.statisticDataByES(sTime,eTime,queryIds);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("FYTJ_ZZSRTJHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FYTJ_ZZSRTJHZ", "住院统计分析_住院收入统计汇总", "2","0"), e);
		}
		
	}
	
	@Action(value="getDept",results = { @Result(name = "json", type = "json") })
	public void getDept(){//科室下拉列表
		try {
			List<SysDepartment> list = statisticService.getDept();
			SysDepartment sysDepartment = new SysDepartment();
			sysDepartment.setId("0");
			sysDepartment.setDeptName("全部");
			list.add(sysDepartment);
			String json = JSONUtils.toExposeJson(list, false, null, "id","deptName");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("FYTJ_ZZSRTJHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FYTJ_ZZSRTJHZ", "住院统计分析_住院收入统计汇总", "2","0"), e);
		}
	}
	
	/**
	 * 加载报表类别信息
	 */
	@Action(value="getReport",results = { @Result(name = "json", type = "json") })
	public void getReport(){
		try {
			List<StatisticVo> list = statisticService.getreport();
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("FYTJ_ZZSRTJHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FYTJ_ZZSRTJHZ", "住院统计分析_住院收入统计汇总", "2","0"), e);
		}
	}
	
	/**
	 * 加载统计大类信息
	 */
	@Action(value="getMinfee",results = { @Result(name = "json", type = "json") })
	public void getMinfee(){
		try {
			List<StatisticVo> list = statisticService.getfeetStat(reportCode);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("FYTJ_ZZSRTJHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FYTJ_ZZSRTJHZ", "住院统计分析_住院收入统计汇总", "2","0"), e);
		}
	}
	
	/**
	 * 加载统计费用名称
	 */
	@Action(value="getStatName",results = { @Result(name = "json", type = "json") })
	public void getStatName(){
		try {
			List<StatisticVo> list = statisticService.getfeeStatName(reportCode);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("FYTJ_ZZSRTJHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FYTJ_ZZSRTJHZ", "住院统计分析_住院收入统计汇总", "2","0"), e);
		}
	}
	/**
	 * @Description 查询统计大类
	 * @author  donghe
	 * @createDate： 2017-4-14 下午7:15:24 
	 * @modifier 
	 * @modifyDate：
	 * @param 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getStatName1")
	public void getStatName1(){
		try{
			List<MenuListVO> ds = statisticService.getDeptList();
			String json = JSONUtils.toJson(ds);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("FYTJ_ZZSRTJHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FYTJ_ZZSRTJHZ", "住院统计分析_住院收入统计汇总", "2","0"), e);
		}
	}
	/**  
	 * 
	 * 住院收入统计汇总表打印
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月19日 下午3:00:42 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月19日 下午3:00:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
//	@Action(value="iReportStatistic")
	@Action(value="iReportStatistic_pdf")
	public void iReportStatistic(){
		 try{
		   //jasper文件名称 不含后缀
		   String rows = request.getParameter("rows"); 
		   String fileName = request.getParameter("fileName");
		   String start = request.getParameter("start");
		   String end = request.getParameter("end");
//		   String queryDept = request.getParameter("queryDept");
//		   String statType = request.getParameter("statType");
		   
		   String root_path = request.getSession().getServletContext().getRealPath("/");
		   root_path = root_path.replace('\\', '/');
		   String reportFilePath = root_path + webPath +fileName+".jasper";
		   List<ResultVo> list = JSONUtils.fromJson(rows, new TypeToken<List<ResultVo>>(){});
		   for (ResultVo vo : list) {
				 vo.setTotCost(vo.getTotCost01()+vo.getTotCost02()+vo.getTotCost03()+vo.getTotCost04()+vo.getTotCost05()+vo.getTotCost07()+vo.getTotCost08()+vo.getTotCost09()+vo.getTotCost10()+vo.getTotCost11()+vo.getTotCost12()+vo.getTotCost13()+vo.getTotCost14()+vo.getTotCost15()+vo.getTotCost16());
			}
		   //javaBean数据封装
		   ArrayList<ResultVoToIReport> voList = new ArrayList<ResultVoToIReport>();
		   ResultVoToIReport vo = new ResultVoToIReport();
		   vo.setList(list);
		   voList.add(vo);
		   JRDataSource jrd=new JRBeanCollectionDataSource(voList);
		   Map<String, Object> parameters = new HashMap<String, Object>();
		   parameters.put("hName", HisParameters.PREFIXFILENAME);
		   parameters.put("start", start);
		   parameters.put("end", end);
//		   parameters.put("queryDept", queryDept);
//		   parameters.put("statType", statType);
		   parameters.put("SUBREPORT_DIR", root_path + webPath);
		   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
		  }catch(Exception e) {
		     e.printStackTrace();
		     logger.error("FYTJ_ZZSRTJHZ", e);
			 hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FYTJ_ZZSRTJHZ", "住院统计分析_住院收入统计汇总", "2","0"), e);
		  }
		 
	}
	/**  
	 * 
	 * 住院收入统计汇总表导出
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月19日 下午4:41:08 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月19日 下午4:41:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="statisticInfo")
	public void statisticInfo() throws IOException{
		try{
			String rows = request.getParameter("rows");
			List<ResultVo> list = JSONUtils.fromJson(rows, new TypeToken<List<ResultVo>>(){});
			for (ResultVo vo : list) {
				vo.setTotCost(vo.getTotCost01()+vo.getTotCost02()+vo.getTotCost03()+vo.getTotCost04()+vo.getTotCost05()+vo.getTotCost07()+vo.getTotCost08()+vo.getTotCost09()+vo.getTotCost10()+vo.getTotCost11()+vo.getTotCost12()+vo.getTotCost13()+vo.getTotCost14()+vo.getTotCost15()+vo.getTotCost16());
			}
			if (list == null || list.isEmpty()) {
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
			}
			String head = "";
			String name = "住院收入统计汇总";
			String[] headMessage = { "住院科室", "西药费", "中成药费", "中草药费","床位费", "治疗费","检查费","放射费","化验费","手术费","输血费","输氧费","材料费","其他费","护理费","诊察费","总计"};
			for (String message : headMessage) {
				head += "," + message;
			}
			head = head.substring(1);
			FileUtil fUtil = new FileUtil();
			String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
			String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/" + fileName;
			fUtil.setFilePath(filePath);
			fUtil.write(head);
			
			fUtil = statisticService.export(list, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
		}catch(Exception e){
			logger.error("FYTJ_ZZSRTJHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FYTJ_ZZSRTJHZ", "住院统计分析_住院收入统计汇总", "2","0"), e);
		}
	}
}
