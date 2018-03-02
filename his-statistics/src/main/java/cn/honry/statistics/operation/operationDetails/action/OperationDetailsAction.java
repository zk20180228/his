package cn.honry.statistics.operation.operationDetails.action;


import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.drug.anesthetic.service.AnestheticService;
import cn.honry.statistics.operation.operaction.service.OperactionActionService;
import cn.honry.statistics.operation.operationDetails.vo.OperationDetailsVo;
import cn.honry.statistics.operation.operationDetails.vo.OperationDetailsVoToIReport;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 手术耗材明细
 * @author Administrator
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value="manageInterceptor")})
@Namespace(value="/statistics/OperationDetails")
public class OperationDetailsAction extends ActionSupport{
	private Logger logger=Logger.getLogger(OperationDetailsAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value="parameterInnerService")
	private ParameterInnerService parameterInnerService;
	
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 开始时间
	 */
	private String login;
	/**
	 * 结束时间
	 */
	private String end;
	/**
	 * 记账编号
	 */
	private String repno;
	/**
	 * 单价
	 */
	private Double price;
	/**
	 * 科室ID
	 */
	private String deptId;
	/**
	 * 科室name
	 */
	private String deptName;
	/**
	 * 分页
	 */
	private String page;
	/**
	 * 行数
	 */
	private String rows;
	//hedong 20170316 报表post提交报表打印示例、
	/**
	 * 报表打印开始时间
	 */
	private String reportToLogin;
	/**
	 * 报表打印结束时间
	 */
	private String reportToEnd;
	/**
	 * 报表文件名
	 */
	private String reportToFileName;
	/**
	 * 报表单价
	 */
	private Double reportToPrice;
	/**
	 * 报表记账编吗
	 */
	private String reportToRepno;
	/**
	 * 身份证号
	 */
	private String identityCard;
	
	
	public RedisUtil getRedisUtil() {
		return redisUtil;
	}

	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	//hedong 用于报表打印 使用java分隔符 否则 linux系统会找不到相应路径
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	
	@Autowired
	@Qualifier(value="operactionActionService")
	private OperactionActionService operactionActionService;
	@Autowired
	@Qualifier(value="anestheticService")
	private AnestheticService anestheticService;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	@Autowired
	@Qualifier(value="iReportService")
	private IReportService iReportService;
	
	public IReportService getiReportService() {
		return iReportService;
	}

	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getRepno() {
		return repno;
	}

	public void setRepno(String repno) {
		this.repno = repno;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public OperactionActionService getOperactionActionService() {
		return operactionActionService;
	}

	public void setOperactionActionService(
			OperactionActionService operactionActionService) {
		this.operactionActionService = operactionActionService;
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

	public String getReportToLogin() {
		return reportToLogin;
	}

	public void setReportToLogin(String reportToLogin) {
		this.reportToLogin = reportToLogin;
	}

	public String getReportToEnd() {
		return reportToEnd;
	}

	public void setReportToEnd(String reportToEnd) {
		this.reportToEnd = reportToEnd;
	}

	public String getReportToFileName() {
		return reportToFileName;
	}

	public void setReportToFileName(String reportToFileName) {
		this.reportToFileName = reportToFileName;
	}
    
	public Double getReportToPrice() {
		return reportToPrice;
	}

	public void setReportToPrice(Double reportToPrice) {
		this.reportToPrice = reportToPrice;
	}

	public String getReportToRepno() {
		return reportToRepno;
	}

	public void setReportToRepno(String reportToRepno) {
		this.reportToRepno = reportToRepno;
	}

	/***
	 * @Description:手术耗材统计明细
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月12日 
	 * @version 1.0
	 */
	@Action(value="operationDetailsList",results={ @Result(name = "list",location= "/WEB-INF/pages/stat/operation/operationDetails/operationDetails.jsp") },interceptorRefs={@InterceptorRef(value= "manageInterceptor") })
	public String operationDetailsList(){
		SysDepartment sys=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(sys!=null&&StringUtils.isNotBlank(sys.getId())){
			deptName=anestheticService.getDeptName(sys.getId());
		}else{
			deptName="全部";
		}
		login=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		end=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return "list";
	}
	
	/**  
	 * 
	 * 手术耗材统计明细List
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:repno 记账编码
	 * @param:deptId 科室
	 * @param:identityCard 身份证号
	 * @param:page页数
	 * @param:rows行数
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="getoperationDetailsList")
	public void getoperationDetailsList(){
		if(StringUtils.isNotBlank(end)){
			   end = DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.getCurrentTime(), 1));
		}
		List<InpatientItemListNow> list=new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		String redKey = "SSHCTJMX"+login+"_"+end+"_"+price+"_"+repno+"_"+deptId+"_"+identityCard;
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum == null){
			totalNum =operactionActionService.getoperationDetailsTotal(login,end,price,repno,deptId,identityCard);
			redisUtil.set(redKey, totalNum);
		}
		if (totalNum != null && totalNum - 1 >= 0) {
			list = operactionActionService.getoperationDetailsList(login,end,price,repno,deptId,page,rows,identityCard);
		}
		String val=parameterInnerService.getparameter("deadTime");
		if(StringUtils.isNotBlank(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
		map.put("total", totalNum);
		map.put("rows", list);
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}

	/**  
	 * 
	 * 导出手术耗材统计明细List
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:repno 记账编码
	 * @param:deptId 科室
	 * @param:identityCard 身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "expOperactionlist", results = { @Result(name = "json", type = "json") })
	public void expOperactionlist() throws Exception {
		//查询数据
		if(StringUtils.isNotBlank(end)){
			end = DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.getCurrentTime(), 1));
		}
		List<InpatientItemListNow> list = operactionActionService.queryInvLogExpoper(login, end,price, repno, identityCard);
		//生成文件名
		String name = "手术耗材统计";
		String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".xls";
		//下载文件
		try {
			byte[] content = operactionActionService.exportoper(list);
			DownloadUtils.download(request, response, fileName, content);
		} catch (Exception e) {
			//hedong 20170407 异常信息输出至日志文件
			logger.error("SSGL_SSHCTJMX", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSGL_SSHCTJMX", "手术管理_手术耗材统计明细", "2", "0"), e);
		}
	}
	

	/**  
	 * 
	 * 手术耗材统计明细(打印)-20170316 hedong 报表打印 window.open采用post方式提交参数示例
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:repno 记账编码
	 * @param:deptId 科室
	 * @param:identityCard 身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="iReportOperDetailsRecord")
	public void iReportOperDetailsRecord(){
		 try
		  {
			 if(StringUtils.isNotBlank(reportToEnd)){
				 reportToEnd = DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.getCurrentTime(), 1)); 
			} 
		   String deptName="";
		   SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		   if(dept!=null&&dept.getDeptName()!=null){
			 deptName=dept.getDeptName();
		   }else{
			  deptName="全部";
		   }
		   String root_path = request.getSession().getServletContext().getRealPath("/");
		   root_path = root_path.replace('\\', '/');
		   String reportFilePath = root_path + webPath+reportToFileName+".jasper";
		   //javaBean数据封装（注：数据源可参考该示例各自进行创建）
		   List<OperationDetailsVo> list = operactionActionService.queryInvLogDetails(reportToLogin, reportToEnd,reportToPrice, reportToRepno, identityCard);
		   ArrayList<OperationDetailsVoToIReport> voList = new ArrayList<OperationDetailsVoToIReport>();
		   OperationDetailsVoToIReport vo = new OperationDetailsVoToIReport();
		   vo.setItemList(list);
		   voList.add(vo);
		   JRDataSource jrd=new JRBeanCollectionDataSource(voList);
		   Map<String, Object> parameters = new HashMap<String, Object>();
		   parameters.put("loginTime",reportToLogin);
		   parameters.put("endTime",DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(reportToEnd), -1)));
		   parameters.put("deptName", deptName);
		   parameters.put("SUBREPORT_DIR", root_path + webPath);
		   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
		  }catch(Exception e){
			//hedong 20170407 异常信息输出至日志文件
			logger.error("SSGL_SSHCTJMX", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSGL_SSHCTJMX", "手术管理_手术耗材统计明细", "2", "0"), e);
		  }
		 
	}
	
}
