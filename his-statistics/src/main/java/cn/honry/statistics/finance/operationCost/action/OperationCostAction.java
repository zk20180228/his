package cn.honry.statistics.finance.operationCost.action;

import java.io.File;
import java.io.PrintWriter;
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
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.finance.operationCost.service.OperationCostService;
import cn.honry.statistics.finance.operationCost.vo.OperationCostVo;
import cn.honry.statistics.finance.operationCost.vo.OperationCostVoToReport;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;


/**
 * 手术费用汇总ACTION
 * @author tangfeishuai
 *@CreateDate 2016年6月8日 上午9:47:41 
 *@version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/OperationCost")
public class OperationCostAction extends ActionSupport{
	private Logger logger=Logger.getLogger(OperationCostAction.class);
	private static final long serialVersionUID = 1L;
	//hedong 用于报表打印 使用java分隔符 否则 linux系统会找不到相应路径
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	/**
	 * 注入手术费用详情Service
	 */
	@Autowired 
	@Qualifier(value = "operationCostService")
	private OperationCostService operationCostService;
	@Autowired 
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier(value="iReportService")
	private IReportService iReportService;
	@Autowired
	@Qualifier(value="parameterInnerService")
	private ParameterInnerService parameterInnerService;
	
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}

	public IReportService getiReportService() {
		return iReportService;
	}

	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	
	/**
	 * 当前页数，用于分页查询
	 */
	private String page;
	
	/**
	 * 分页条数，用于分页查询
	 */
	private String rows;
	
	/**
	 * 开始时间
	 */
	private String startTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	
	/**
	 * 执行科室
	 */
	private String execDept;
	
	/**
	 *住院流水号
	 */
	private String inpatientNo;
	
	/**
	 *身份证号
	 */
	private String identityCard;
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	
	
	public RedisUtil getRedisUtil() {
		return redisUtil;
	}

	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
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


	public String getExecDept() {
		return execDept;
	}


	public void setExecDept(String execDept) {
		this.execDept = execDept;
	}


	public String getInpatientNo() {
		return inpatientNo;
	}


	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public void setOperationCostService(OperationCostService operationCostService) {
		this.operationCostService = operationCostService;
	}

	/**  
	 *  
	 * @Description：  获取list页面
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月8日 上午9:47:41 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZYSSFHZCX:function:view"}) 
	@Action(value = "listOperationCost", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/operation/operationCost/operationCostList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listOperationCost() {
		startTime=DateUtils.formatDateY_M(DateUtils.getCurrentTime())+"-01";
		endTime=DateUtils.getDate();
		return "list";
	}
	
	/**  
	 * 
	 * 手术费用汇总
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:inpatientNo 病历号
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "queryOperationCost")
	public void queryOperationCost() {
		if(StringUtils.isBlank(startTime)){
			Date date = new Date();
			startTime = DateUtils.formatDateYM(date)+"-01";
		}
		if(StringUtils.isBlank(endTime)){
			Date date = new Date();
			endTime = DateUtils.formatDateY_M_D(date);
		}
		List<OperationCostVo> list =new ArrayList<>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		String redKey = "ZYSSFHZCX"+startTime+"_"+endTime+"_"+inpatientNo+"_"+execDept+"_"+identityCard;
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum == null){
			totalNum =   operationCostService.queryOperationCostTotal(startTime, endTime, inpatientNo, execDept,identityCard);
			redisUtil.set(redKey, totalNum);
		}
		if (totalNum != null && totalNum - 1 >= 0) {
			list =   operationCostService.queryOperationCost(startTime, endTime, inpatientNo, execDept,page,rows,identityCard);
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
	 * @Description：科室id与姓名map
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月8日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "getDeptMap")
	public void getDeptMap() {
		Map<String,String> retMap = deptInInterService.querydeptCodeAndNameMap();
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 导出手术费用汇总
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:inpatientNo 病历号
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "outOperationCost")
	public void outOperationCost() throws Exception {
		if(StringUtils.isBlank(startTime)){
			Date date = new Date();
			startTime = DateUtils.formatDateYM(date)+"-01";
		}
		if(StringUtils.isBlank(endTime)){
			Date date = new Date();
			endTime = DateUtils.formatDateY_M_D(date);
		}
		List<OperationCostVo> list = operationCostService.queryOperationCostOther(startTime, endTime, inpatientNo, execDept,identityCard);
		if (list == null || list.isEmpty()) {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
		}
		String head = "";

		String name = "住院手术费汇总查询";
		String[] headMessage = { "病历号", "姓名", "执行科室", "手术费","收费日期" };

		for (String message : headMessage) {
			head += "," + message;
		}
		head = head.substring(1);
		FileUtil fUtil = new FileUtil();
		String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
		String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/" + fileName;
		fUtil.setFilePath(filePath);
		fUtil.write(head);
		
		PrintWriter out = WebUtils.getResponse().getWriter();
		try {
			fUtil = operationCostService.export(list, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
			out.write("success");
		} catch (Exception e) {
			out.write("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("SSTJ_ZYSSFHZ", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSTJ_ZYSSFHZ", "手术统计_住院手术费汇总查询", "3", "0"), e);
		}finally {
			out.flush();
			out.close();
		}
	}
	
	/**  
	 * 
	 * 住院手术费汇总报表打印
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:inpatientNo 病历号
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="iReportOperationCost")
	public void iReportOperationCost(){
		 try{
		   //jasper文件名称 不含后缀
		   String fileName = request.getParameter("fileName");
		   String root_path = request.getSession().getServletContext().getRealPath("/");
		   root_path = root_path.replace('\\', '/');
		   String reportFilePath = root_path + webPath+fileName+".jasper";
		   if(StringUtils.isBlank(startTime)){
				Date date = new Date();
				startTime = DateUtils.formatDateYM(date)+"-01";
			}
			if(StringUtils.isBlank(endTime)){
				Date date = new Date();
				endTime = DateUtils.formatDateY_M_D(date);
			}
		   List<OperationCostVo> list = operationCostService.queryOperationCostOther(startTime, endTime, inpatientNo, execDept,identityCard);
		   //javaBean数据封装
		   ArrayList<OperationCostVoToReport> voList = new ArrayList<OperationCostVoToReport>();
		   OperationCostVoToReport vo = new OperationCostVoToReport();
		   vo.setItemList(list);
		   voList.add(vo);
		   JRDataSource jrd=new JRBeanCollectionDataSource(voList);
		   Map<String, Object> parameters = new HashMap<String, Object>();
		   parameters.put("loginTime", startTime);
		   parameters.put("endTime", endTime);
		   parameters.put("SUBREPORT_DIR", root_path + webPath);
		   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
		  }catch(Exception e){
			//hedong 20170407 异常信息输出至日志文件
			logger.error("SSTJ_ZYSSFHZ", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSTJ_ZYSSFHZ", "手术统计_住院手术费汇总查询", "2", "0"), e);
		  }
		 
	}
	
	/**  
	 * 
	 * 获取科室
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午4:36:23 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午4:36:23 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "getDeptList")
	public void getDeptList(){
		List<MenuListVO> ds = operationCostService.getDeptList();
		String json = JSONUtils.toJson(ds);
		WebUtils.webSendJSON(json);
	}
}
