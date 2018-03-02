package cn.honry.statistics.operation.recipelStat.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.operation.recipelStat.service.RecipelStatService;
import cn.honry.statistics.operation.recipelStat.vo.RecipelInfoVo;
import cn.honry.statistics.operation.recipelStat.vo.RecipelStatVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @Description 门诊处方查询 action
 * @author  lyy
 * @createDate： 2016年7月13日 下午8:10:16 
 * @modifier lyy
 * @modifyDate：2016年7月13日 下午8:10:16
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/recipelStat")
public class RecipelStatAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	//hedong 用于报表打印 使用java分隔符 否则 linux系统会找不到相应路径
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	
	@Autowired
	@Qualifier(value="iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	
	// 记录异常日志
	private Logger logger = Logger.getLogger(RecipelStatAction.class);
	
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	

	private HttpServletRequest request = ServletActionContext.getRequest();
	
	/**
	 * @Description:翻页参数page
	 */
	private String page;
	
	/**
	 * @Description:翻页参数rows
	 */
	private String rows;
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	
	/**
	 * 处方号
	 */
	private String recipeNo;
	
	/**
	 * 开始时间
	 */
	private String startTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	
	/**
	 * 类型0全部1病历卡号2发票号3姓名4处方号
	 */
	private String type;

	/**
	 * 查询参数
	 */
	private String para;
	
	/**
	 * 是否模糊查询
	 */
	private String vague;
	
	/**
	 * 详情表明
	 */
	private String tab;
	
	private RecipelStatService recipelStatService;
	
	@Autowired
	@Qualifier(value = "recipelStatService")
	public void setRecipelStatService(RecipelStatService recipelStatService) {
		this.recipelStatService = recipelStatService;
	}
	
	/**
	 * getters and setters
	 */
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPara() {
		return para;
	}
	public void setPara(String para) {
		this.para = para;
	}
	public String getVague() {
		return vague;
	}
	public void setVague(String vague) {
		this.vague = vague;
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
	public String getTab() {
		return tab;
	}
	public void setTab(String tab) {
		this.tab = tab;
	}
	
	/**  
	 *  
	 * 跳转list页面
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"MZCFCX:function:view"}) 
	@Action(value = "listRecipelStat", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/outpatientRecipelStat/recipelStatList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listRecipelStat() {

		endTime=DateUtils.formatDateY_M_D(DateUtils.getCurrentTime());
		startTime=DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));
	
		return "list";
	}

	/**  
	 *  
	 * 查询处方患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value="queryPatientInfo")
	public void queryPatientInfo(){
		
		Map<String,Object> map =null;
		try {
			if(StringUtils.isBlank(startTime)){
				startTime=DateUtils.formatDateY_M_D_H_M_S(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));
			}
			
			if(StringUtils.isBlank(endTime)){
				endTime=DateUtils.formatDateY_M_D_H_M_S(DateUtils.getCurrentTime());	
			}
			map = recipelStatService.getRows(page,rows,startTime,endTime,type,para,vague);
		} catch (Exception e) {
			
			//发生异常时，响应空内容
			map=new HashMap<String, Object>();
			map.put("total",0);
			map.put("rows",new ArrayList<RecipelStatVo>());
			
			e.printStackTrace();
			logger.error("MZCX_MZCFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZCFCX", "门诊查询_门诊医嘱查询", "2","0"), e);
		}
		
		String retJson = JSONUtils.toJson(map);
		
		WebUtils.webSendJSON(retJson);
	}
	
	/**  
	 *  
	 * 根据处方号查询处方信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value="queryRecipelInfo")
	public void queryRecipelInfo(){
		
		Map<String,Object> map = new HashMap<String, Object>();
		List<RecipelInfoVo> rows=null;
		try {
			rows = recipelStatService.getRecipelInfoRows(startTime,endTime,recipeNo);
			
		} catch (Exception e) {
			
			rows= new ArrayList<RecipelInfoVo>();
			
			e.printStackTrace();
			logger.error("MZCX_MZCFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZCFCX", "门诊查询_门诊医嘱查询", "2","0"), e);
		}
		
		map.put("rows",rows);
		String retJson = JSONUtils.toJson(rows);
		
		WebUtils.webSendJSON(retJson);
	}
	
	
	/**  
	 * 
	 *javabeans批量打印
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月17日 上午11:45:55 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月17日 上午11:45:55 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value = "iReportToStatAdvice")
	public void iReportToStatAdvice(){
		
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String no=request.getParameter("RECIPE_NO");

			String root_path = request.getSession().getServletContext().getRealPath("/");
			root_path = root_path.replace('\\', '/');
			String reportFilePath = root_path + webPath +fileName+".jasper";
	
			List<HashMap<String,Object>> o=new ArrayList<HashMap<String,Object>>();
			
			//获取患者信息
			List<RecipelStatVo> recipelStatVos = recipelStatService.getRecipelStatVos(no);			
			Map<String,RecipelStatVo> recipelStatVoMap = new HashMap<String, RecipelStatVo>();
			for (RecipelStatVo recipelStatVo : recipelStatVos) {
				recipelStatVoMap.put(recipelStatVo.getRecipeNo(), recipelStatVo);
			}
			
			//获取处方信息
			List<RecipelInfoVo> recipelInfoVos=recipelStatService.getRecipelInfos(startTime,endTime,no);
			Map<String,List<RecipelInfoVo>> recipelInfoVoMap = new HashMap<String, List<RecipelInfoVo>>();
			List<RecipelInfoVo> voList = null;
			for(RecipelInfoVo recipelInfoVo : recipelInfoVos){
				if(recipelInfoVoMap.get(recipelInfoVo.getRecipeNo())==null){
					voList = new ArrayList<RecipelInfoVo>();
					voList.add(recipelInfoVo);
					recipelInfoVoMap.put(recipelInfoVo.getRecipeNo(), voList);
				}else{
					recipelInfoVoMap.get(recipelInfoVo.getRecipeNo()).add(recipelInfoVo);
				}
			 }
			String[] noArr = no.split(",");
			HashMap<String, Object> parameters = null;
			JRDataSource jrd = null;
			List<RecipelStatVo> recipelStatList= null;
			RecipelInfoVo recipelInfoVo = null;
			for(String n : noArr){
				parameters = new HashMap<String, Object>();
				recipelStatList = new ArrayList<RecipelStatVo>();
				RecipelStatVo vo = recipelStatVoMap.get(n);
				recipelInfoVo = new RecipelInfoVo();
				recipelInfoVo.setGoodsName("合计");
				List<RecipelInfoVo> l = recipelInfoVoMap.get(n);
				Double money = 0d;
				if(l!=null){
					for(RecipelInfoVo info : l){
						money += info.getMoney();
					}
					recipelInfoVo.setMoney(money);
					l.add(recipelInfoVo);
					vo.setList(l);
				}
				recipelStatList.add(vo);
				jrd = new JRBeanCollectionDataSource(recipelStatList);					
				parameters.put("hName", HisParameters.PREFIXFILENAME);
				parameters.put("payDate", DateUtils.formatDateY_M_D_H_M_S(new Date()));
				parameters.put("SUBREPORT_DIR", root_path + webPath);
				parameters.put("dataSource", jrd);
				
				parameters.put("recipeNo", vo.getRecipeNo());
				parameters.put("name", vo.getName());
				parameters.put("sex", vo.getSex());
				parameters.put("age", vo.getAge());
				parameters.put("recordNo", vo.getRecordNo());
				parameters.put("invoiceNo", vo.getInvoiceNo());
				parameters.put("disTable", vo.getDisTable());
				parameters.put("disUser", vo.getDisUser());
			 	parameters.put("disTime", vo.getDisTime());
			 	parameters.put("medTable", vo.getMedTable());
			 	parameters.put("medUser", vo.getMedUser());
			 	parameters.put("medTime", vo.getMedTime());
			 	parameters.put("squareDoc", vo.getSquareDoc());
				 
				o.add(parameters);
			}
		   
			iReportService.doReportToJavaBean2(request,WebUtils.getResponse(),reportFilePath,o);
		} catch (Exception e) {
			
			e.printStackTrace();
			logger.error("MZCX_MZCFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZCFCX", "门诊查询_门诊医嘱查询", "2","0"), e);
		}
	}
	
}
