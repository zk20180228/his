package cn.honry.statistics.finance.pharmacyRefund.action;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang3.StringUtils;
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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.StoTerminal;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.drug.sendWicket.dao.SendWicketInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.finance.pharmacyRefund.service.RefundSataService;
import cn.honry.statistics.finance.pharmacyRefund.vo.RefundToReport;
import cn.honry.statistics.finance.pharmacyRefund.vo.RefundVo;
import cn.honry.statistics.sys.reportForms.dao.ReportFormsDao;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/***
 * 门诊药房退费统计
 * @ClassName: RefundStatAction 
 * @Description: 
 * @author wfj
 * @date 2016年6月27日 下午3:00:57 
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/RefundStat")
@SuppressWarnings({"all"})
public class RefundStatAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	
	// 记录异常日志
	private Logger logger = Logger.getLogger(RefundStatAction.class);
	
	// 存储异常
	@Resource
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
	
	@Autowired
	@Qualifier(value = "refundSataService")
	private RefundSataService refundSataService;	
	public void setRefundSataService(RefundSataService refundSataService) {
		this.refundSataService = refundSataService;
	}
	
	@Autowired
	@Qualifier(value = "reportFormsDAO")
	private ReportFormsDao reportFormsDAO;
	
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	
	@Autowired
	@Qualifier(value = "sendWicketInInterDAO")
	private SendWicketInInterDAO sendWicketInInterDAO;
	
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	
	@Resource
	private ParameterInnerDAO parameterInnerDAO;
	
	@Autowired
	@Qualifier(value="parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}

	//导出excel使用
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
/*---------------------------- 参数 ----------------------------------*/
	
	//栏目别名（action的参数）
	private String menuAlias;
	
	//检索开始时间
	private String beginDate;
	
	//检索结束时间
	private String endDate;
	
	//药品类别
	private String feeStatCode;
	private String page;
	private String rows;
	
/*---------------------------- 参数 ----------------------------------*/
	
	/***
	 * 初始界面
	 * @Title: refundView 
	 * @author  WFJ
	 * @createDate ：2016年6月27日
	 * @return
	 * @return String
	 * @version 1.0
	 */
	@Action(value = "refundView", results = { @Result(name = "view", location = "/WEB-INF/pages/stat/pharmacyRefund/refundView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String refundView(){
		
		Date date = new Date();
		endDate = DateUtils.formatDateY_M_D(date);
		beginDate = DateUtils.formatDateYM(date)+"-01";
		
		return "view";
	}
	
	/***
	 * 检索
	 * @Title: query 
	 * @author  WFJ
	 * @createDate ：2016年6月27日
	 * @return void
	 * @version 1.0
	 */
	@Action(value = "query")
	public void query(){
		
		Map<String, Object> map =null;
		try {
			List<String> invoiceInfoPartName = new ArrayList<String>();
			List<String> cancelPartName = new ArrayList<String>();
			Date sTime = DateUtils.parseDateY_M_D(beginDate);
			Date eTime = DateUtils.parseDateY_M_D(endDate);
			
			//获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			
			//获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime=null;
			try {
				dTime = df.parse(df.format(new Date()));
			}catch (ParseException e) {
				
				e.printStackTrace();
				logger.error("TJFXGL_MZYFTFTJ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZYFTFTJ", "门诊统计分析_门诊药房退费统计", "2","0"), e);
			}
			
			//获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
			
			if(DateUtils.compareDate(sTime, cTime)==-1){//查询历史表
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					
					//获取需要查询的全部分区
					invoiceInfoPartName = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_FINANCE_INVOICEINFO",beginDate,endDate);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beginDate);
					
					//获取相差年分的分区集合，默认加1
					invoiceInfoPartName = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_FINANCE_INVOICEINFO",yNum+1);
					invoiceInfoPartName.add(0,"T_FINANCE_INVOICEINFO_NOW");
				}
			}else{
				invoiceInfoPartName.add("T_FINANCE_INVOICEINFO_NOW");
			}
			if(DateUtils.compareDate(sTime, cTime)==-1){//查询历史表
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询历史表
					cancelPartName.add("T_INPATIENT_CANCELITEM");
				}else{//2.查询历史表和主表
					cancelPartName.add(0,"T_INPATIENT_CANCELITEM_NOW");
					cancelPartName.add(1,"T_INPATIENT_CANCELITEM");
				}
			}else{
				cancelPartName.add("T_INPATIENT_CANCELITEM_NOW");
			}
			
			map = new HashMap<String, Object>();
			
			List<RefundVo> list = refundSataService.query(invoiceInfoPartName, cancelPartName, feeStatCode, beginDate, endDate,page,rows);
			
			String redKey=null;
			Integer total=null;
			try {
				redKey = "MZYFTF"+beginDate+"_"+endDate+"_"+feeStatCode;
				total = (Integer) redisUtil.get(redKey);
				String val=parameterInnerService.getparameter("deadTime");
				if(StringUtils.isNotBlank(val)){
					redisUtil.expire(redKey,Integer.valueOf(val));
				}else{
					redisUtil.expire(redKey, 300);
				}
			} catch (NumberFormatException e) {
				total = refundSataService.queryTotal(invoiceInfoPartName, cancelPartName, feeStatCode, beginDate, endDate);//redis发生异常保证可以从数据读
				
				e.printStackTrace();
				logger.error("TJFXGL_MZYFTFTJ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZYFTFTJ", "门诊统计分析_门诊药房退费统计", "2","0"), e);
			}
			
			if(total==null){
				total = refundSataService.queryTotal(invoiceInfoPartName, cancelPartName, feeStatCode, beginDate, endDate);
				redisUtil.set(redKey, total);
			}
			
			map.put("total", total);
			map.put("rows", list);
			String json = JSONUtils.toJson(map);
			
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			
			//发生异常，返回空内容
			map.put("total", 0);
			map.put("rows", new ArrayList<RefundVo>());
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
			
			e.printStackTrace();
			logger.error("TJFXGL_MZYFTFTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZYFTFTJ", "门诊统计分析_门诊药房退费统计", "2","0"), e);
		}
		
	}
	
	/***
	 * 导出
	 * @Title: exportExcel 
	 * @author  WFJ
	 * @createDate ：2016年6月28日
	 * @return void
	 * @version 1.0
	 * @throws Exception 
	 */
	@Action(value = "exportExcel",results = { @Result(name = "json", type = "json") })
	public void exportExcel(){
		
		try {
			List<String> invoiceInfoPartName = new ArrayList<String>();
			List<String> cancelPartName = new ArrayList<String>();
			Date sTime = DateUtils.parseDateY_M_D(beginDate);
			Date eTime = DateUtils.parseDateY_M_D(endDate);

			//获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			
			//获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime=null;
			try {
				dTime = df.parse(df.format(new Date()));
			} catch (ParseException e) {
				e.printStackTrace();
				logger.error("TJFXGL_MZYFTFTJ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZYFTFTJ", "门诊统计分析_门诊药房退费统计", "2","0"), e);
			}
			
			//获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
			
			if(DateUtils.compareDate(sTime, cTime)==-1){//查询历史表
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					
					//获取需要查询的全部分区
					invoiceInfoPartName = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_FINANCE_INVOICEINFO",beginDate,endDate);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beginDate);
					
					//获取相差年分的分区集合，默认加1
					invoiceInfoPartName = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_FINANCE_INVOICEINFO",yNum+1);
					invoiceInfoPartName.add(0,"T_FINANCE_INVOICEINFO_NOW");
				}
			}else{
				invoiceInfoPartName.add("T_FINANCE_INVOICEINFO_NOW");
			}
			if(DateUtils.compareDate(sTime, cTime)==-1){//查询历史表
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询历史表
					cancelPartName.add("T_INPATIENT_CANCELITEM");
				}else{//2.查询历史表和主表
					cancelPartName.add(0,"T_INPATIENT_CANCELITEM_NOW");
					cancelPartName.add(1,"T_INPATIENT_CANCELITEM");
				}
			}else{
				cancelPartName.add("T_INPATIENT_CANCELITEM_NOW");
			}
			
			if(feeStatCode==null||feeStatCode==""){
					feeStatCode="01,02,03";
			}
			
			List<RefundVo> list = refundSataService.queryByMongo(feeStatCode, beginDate, endDate,page,rows);
			if(list==null){
				list = refundSataService.query(invoiceInfoPartName, cancelPartName, feeStatCode, beginDate, endDate,page,rows);
			}else if(list.size()<=0){
				list = refundSataService.query(invoiceInfoPartName, cancelPartName, feeStatCode, beginDate, endDate,page,rows);
			}
			Map<String,String> map = new HashMap<String,String>();
			List<StoTerminal> liststo = sendWicketInInterDAO.getAllSendWicket();
			for (StoTerminal s : liststo) {
				map.put(s.getCode(), s.getName());
			}
			Map<String,String> feeStatmap = new HashMap<String,String>();
			List<BusinessDictionary> list2 = innerCodeService.getDictionary("drugMinimumcost");
			for (BusinessDictionary dic : list2) {
				feeStatmap.put(dic.getEncode(), dic.getName());
			}
			if (list == null || list.isEmpty()) {
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
				return ;
			}
			for (RefundVo s : list) {
				s.setFeeStatCode(feeStatmap.get(s.getFeeStatCode()));
				if(StringUtils.isNotBlank(s.getSendWin())){
					String winName = "";
					String[] split = s.getSendWin().split(",");
					for (int i = 0; i < split.length; i++) {
						if(StringUtils.isNotBlank(winName)){
							winName+=",";
						}
						if(map.get(split[i])!=null&&!"".equals(map.get(split[i]))){
							winName+=map.get(split[i]);
						}
					}
					s.setSendWin('"'+winName+'"');
				}
			}
			
			String head = "";
			String name = "门诊药房退药统计查询";
			String[] headMessage = { "发票流水号", "患者名称", "退费项目", "退费日期","退费金额", "发药窗口"};

			for (String message : headMessage) {
				head += "," + message;
			}
			head = head.substring(1);
			
			FileUtil fUtil = new FileUtil();
			String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
			String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/" + fileName;
			fUtil.setFilePath(filePath);
			fUtil.write(head);
			
			fUtil = refundSataService.export(list, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
		
		} catch (Exception e) {
			
			e.printStackTrace();
			logger.error("TJFXGL_MZYFTFTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZYFTFTJ", "门诊统计分析_门诊药房退费统计", "2","0"), e);
		} 
	
	}
	
	/**
	 * @Description 获取所有发票科目代码
	 * @author  marongbin
	 * @createDate： 2016年12月23日 下午4:53:22 
	 * @modifier 
	 * @modifyDate：: void
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryFeeStatCode")
	public void queryFeeStatCode(){
		Map<String,String> map= new HashMap<String,String>();
		try {
			
			List<BusinessDictionary> list = innerCodeService.getDictionary("drugMinimumcost");
			for (BusinessDictionary dic : list) {
				map.put(dic.getEncode(), dic.getName());
			}
			String json = JSONUtils.toJson(map);
			
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			//发生异常返回空内容
			String json = JSONUtils.toJson(map);
			
			WebUtils.webSendJSON(json);
			
			e.printStackTrace();
			logger.error("TJFXGL_MZYFTFTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZYFTFTJ", "门诊统计分析_门诊药房退费统计", "2","0"), e);
		}
	}
	
	@Action(value = "queryAllSendWin")
	public void queryAllSendWin(){
		
		Map<String,String> map = new HashMap<String,String>();
		try {
			List<StoTerminal> list = sendWicketInInterDAO.getAllSendWicket();
			for (StoTerminal s : list) {
				map.put(s.getCode(), s.getName());
			}
			String json = JSONUtils.toJson(map);
			
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			//发生异常返回空内容
			String json = JSONUtils.toJson(map);
			
			WebUtils.webSendJSON(json);
			
			e.printStackTrace();
			logger.error("TJFXGL_MZYFTFTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZYFTFTJ", "门诊统计分析_门诊药房退费统计", "2","0"), e);
		}
	}
	
	/***
	 * @Description:门诊药房退费查询表打印
	 */
	@Action(value="iReportPhaRefund")
	public void iReportPhaRefund(){
		 try{ 
			 
		   //jasper文件名称 不含后缀
		   String fileName = request.getParameter("fileName");
		   String root_path = request.getSession().getServletContext().getRealPath("/");
		   root_path = root_path.replace('\\', '/');
		   String reportFilePath = root_path + webPath +fileName+".jasper";
		   List<String> invoiceInfoPartName = new ArrayList<String>();
			List<String> cancelPartName = new ArrayList<String>();
			Date sTime = DateUtils.parseDateY_M_D(beginDate);
			Date eTime = DateUtils.parseDateY_M_D(endDate);
			StatVo invInfoVo = reportFormsDAO.findMaxMinByTabNameAndField("T_FINANCE_INVOICEINFO_NOW", "CREATETIME");
			
			//退费表未分区
			StatVo cancelVo = reportFormsDAO.findMaxMinByTabNameAndField("T_INPATIENT_CANCELITEM_NOW", "CREATETIME");
			
			//获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			
			//获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime=null;
			try {
				dTime = df.parse(df.format(new Date()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
			if(DateUtils.compareDate(sTime, cTime)==-1){//查询历史表
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
				
					//获取需要查询的全部分区
					invoiceInfoPartName = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_FINANCE_INVOICEINFO",beginDate,endDate);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beginDate);
					
					//获取相差年分的分区集合，默认加1
					invoiceInfoPartName = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_FINANCE_INVOICEINFO",yNum+1);
					invoiceInfoPartName.add(0,"T_FINANCE_INVOICEINFO_NOW");
				}
			}else{
				invoiceInfoPartName.add("T_FINANCE_INVOICEINFO_NOW");
			}
			if(DateUtils.compareDate(sTime, cTime)==-1){//查询历史表
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询历史表
					cancelPartName.add("T_INPATIENT_CANCELITEM");
				}else{//2.查询历史表和主表
					cancelPartName.add(0,"T_INPATIENT_CANCELITEM_NOW");
					cancelPartName.add(1,"T_INPATIENT_CANCELITEM");
				}
			}else{
				cancelPartName.add("T_INPATIENT_CANCELITEM_NOW");
			}
		   
			if(feeStatCode==null||feeStatCode==""){
				feeStatCode="01,02,03";
			}
			List<RefundVo> list = refundSataService.queryByMongo(feeStatCode, beginDate, endDate,page,rows);
			if(list==null || list.isEmpty()){
				list = refundSataService.query(invoiceInfoPartName, cancelPartName, feeStatCode, beginDate, endDate,page,rows);
			}else if(list.size()<=0){
				list = refundSataService.query(invoiceInfoPartName, cancelPartName, feeStatCode, beginDate, endDate,page,rows);
			}
			if (list == null || list.isEmpty()) {
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("根据您选择的打印条件，不存在具备您要求的记录！");
			}else{
				
				//渲染退票项目和发票窗口
				Map<String,String> map = new HashMap<String,String>();
				List<BusinessDictionary> dictionaryList = innerCodeService.getDictionary("drugMinimumcost");
				for (BusinessDictionary dic : dictionaryList) {
					map.put(dic.getEncode(), dic.getName());
				}
				Map<String,String> map2 = new HashMap<String,String>();
				List<StoTerminal> stoTerminalList = sendWicketInInterDAO.getAllSendWicket();
				for (StoTerminal s : stoTerminalList) {
					map2.put(s.getCode(), s.getName());
				}
				String[] feeArr = null;
				String fee = null;
				String[] stoArr = null;
				String sto = null;
				for(RefundVo vo : list){
					feeArr = vo.getFeeStatCode().split(",");
					fee = "";
					for(String str : feeArr){
						if(StringUtils.isNotBlank(fee)){
							fee += ",";
						}
						fee += map.get(str);
					}
					vo.setFeeStatCode(fee);
					if(StringUtils.isNotBlank(vo.getSendWin())){
						stoArr = vo.getSendWin().split(",");
						sto = "";
						for(String str : stoArr){
							String string = map2.get(str);
							if(StringUtils.isNotBlank(sto) && string!=null){
								sto += ",";
							}
							if (string==null) {
								string="";
							}
							sto += string;
						}
						vo.setSendWin(sto);
					}
				}
				
				//javaBean数据封装
			   ArrayList<RefundToReport> voList = new ArrayList<RefundToReport>();
			   RefundToReport vo = new RefundToReport();
			   vo.setList(list);
			   voList.add(vo);
			   JRDataSource jrd=new JRBeanCollectionDataSource(voList);
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   parameters.put("hName", HisParameters.PREFIXFILENAME);
			   parameters.put("beginDate", beginDate);
			   parameters.put("endDate", endDate);
			   parameters.put("SUBREPORT_DIR", root_path + webPath);
			   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
			}
		   
		  }catch(Exception e) {
		     
			e.printStackTrace();
			logger.error("TJFXGL_MZYFTFTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZYFTFTJ","门诊统计分析_门诊药房退费统计", "2", "0"), e);
		  }
		 
	}
	
	/**
	 * 
	 * @Description:将门诊药房退费统计数据从mongodb中读出
	 * @param feeStatCode 药品类别(发票科目类型)
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 * List<RefundVO>
	 * @exception:
	 * @author:zhangkui
	 * @time:2017年5月13日 上午10:38:04
	 */
	@Action("queryByMongo")
	public void queryByMongo(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<String> invoiceInfoPartName = new ArrayList<String>();
			List<String> cancelPartName = new ArrayList<String>();
			Date sTime = DateUtils.parseDateY_M_D(beginDate);
			Date eTime = DateUtils.parseDateY_M_D(endDate);

			//获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			
			//获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime=null;
			try {
				dTime = df.parse(df.format(new Date()));
			} catch (ParseException e) {
				
				e.printStackTrace();
				logger.error("TJFXGL_MZYFTFTJ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZYFTFTJ", "门诊统计分析_门诊药房退费统计", "2","0"), e);
			}
			
			//获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
			
			if(DateUtils.compareDate(sTime, cTime)==-1){//查询历史表
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					
					//获取需要查询的全部分区
					invoiceInfoPartName = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_FINANCE_INVOICEINFO",beginDate,endDate);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beginDate);
					
					//获取相差年分的分区集合，默认加1
					invoiceInfoPartName = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_FINANCE_INVOICEINFO",yNum+1);
					invoiceInfoPartName.add(0,"T_FINANCE_INVOICEINFO_NOW");
				}
			}else{
				
				invoiceInfoPartName.add("T_FINANCE_INVOICEINFO_NOW");
			}
			if(DateUtils.compareDate(sTime, cTime)==-1){//查询历史表
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询历史表
					cancelPartName.add("T_INPATIENT_CANCELITEM");
				}else{//2.查询历史表和主表
					cancelPartName.add(0,"T_INPATIENT_CANCELITEM_NOW");
					cancelPartName.add(1,"T_INPATIENT_CANCELITEM");
				}
			}else{
				cancelPartName.add("T_INPATIENT_CANCELITEM_NOW");
			}
			if(feeStatCode==null||feeStatCode==""){
				feeStatCode="01,02,03";
			}
			List<RefundVo> list=null;
			Integer total=null;
			try {
				list = refundSataService.queryByMongo(feeStatCode, beginDate, endDate,page,rows);
				total=refundSataService.queryByMongoCount(feeStatCode, beginDate, endDate);
			} catch (Exception e) {
				list= new ArrayList<RefundVo>();
				e.printStackTrace();
			}
			
			String redKey = "MZYFTF"+beginDate+"_"+endDate+"_"+feeStatCode;
			if(list==null || list.size()==0){
				list = refundSataService.query(invoiceInfoPartName, cancelPartName, feeStatCode, beginDate, endDate,page,rows);
				total = (Integer) redisUtil.get(redKey);
				if(total==null){
					total = refundSataService.queryTotal(invoiceInfoPartName, cancelPartName, feeStatCode, beginDate, endDate);
					redisUtil.set(redKey, total);
				}
			}
			
			String val=parameterInnerService.getparameter("deadTime");
			if(StringUtils.isNotBlank(val)){
				redisUtil.expire(redKey,Integer.valueOf(val));
			}else{
				redisUtil.expire(redKey, 300);
			}
			
			map.put("total", total);
			map.put("rows", list);
			String json = JSONUtils.toJson(map);
			
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			
			map.put("total", 0);
			map.put("rows", new ArrayList());
			String json = JSONUtils.toJson(map);
			
			WebUtils.webSendJSON(json);
			
			e.printStackTrace();
			logger.error("TJFXGL_MZYFTFTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZYFTFTJ", "门诊统计分析_门诊药房退费统计", "2","0"), e);
		}
	}
	
	
/*--------------------------------------------------------- get and set ------------------------------------------------------------------------*/
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
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
	public String getFeeStatCode() {
		return feeStatCode;
	}
	public void setFeeStatCode(String feeStatCode) {
		this.feeStatCode = feeStatCode;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
	
}
