package cn.honry.statistics.operation.operationArrange.action;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
/***
 * 手术安排信息Action
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
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

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.operation.operationArrange.service.OperationArrangeService;
import cn.honry.statistics.operation.operationArrange.vo.OperationArrangeToReport;
import cn.honry.statistics.operation.operationArrange.vo.OperationArrangeToReportSlave;
import cn.honry.statistics.operation.operationArrange.vo.OperationArrangeVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/OperationArrange")
public class OperationArrangeAction extends ActionSupport{
	private Logger logger=Logger.getLogger(OperationArrangeAction.class);
	private static final long serialVersionUID = 1L;
	//hedong 用于报表打印 使用java分隔符 否则 linux系统会找不到相应路径
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	
	/**
	 * 注入手术安排详情Service
	 */
	@Autowired 
	@Qualifier(value = "operationArrangeService")
	private OperationArrangeService operationArrangeService;
	public void setOperationArrangeService(OperationArrangeService operationArrangeService) {
		this.operationArrangeService = operationArrangeService;
	}
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	@Autowired
	@Qualifier(value="iReportService")
	private IReportService iReportService;
	
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	@Autowired
	@Qualifier(value="parameterInnerService")
	private ParameterInnerService parameterInnerService;
	
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	public IReportService getiReportService() {
		return iReportService;
	}

	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
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
	 *手术状态
	 */
	private String status;
	
	/**
	 * 当前页数，用于分页查询
	 */
	private String page;
	
	/**
	 * 身份证号；
	 */
	private String identityCard;
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

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
	/**
	 * 分页条数，用于分页查询
	 */
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/**  
	 *  
	 * @Description：  获取list页面
	 * @Author：tangfeishuai
	 * @CreateDate：2016-5-27上午10:56:35  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"SSAPTJ:function:view"}) 
	@Action(value = "listOperationArrange", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/operation/operationArrange/operationArrangeList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listOperationArrange() {
		startTime=DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));
		endTime=DateUtils.getDate();		
		return "list";
	}
	/**  
	 * 
	 * 手术安排统计列表查询
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:status手术状态
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @param:page当前页数
	 * @param:rows行数
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "queryOperationArrange")
	public void queryOperationArrange() {
		if(StringUtils.isBlank(execDept)){
			SysEmployee empl=ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
			List<SysDepartment> dept=deptInInterService.getDeptByMenutypeAndUserCode(menuAlias, empl.getJobNo());
			for(int i=0;i<dept.size();i++){
				execDept+=dept.get(i).getDeptCode()+",";
			}
			
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Integer totalNum=0;
		List<OperationArrangeVo> list =  new ArrayList<>();
		if(StringUtils.isNotBlank(execDept)){
			execDept=execDept.substring(0, execDept.length()-1);
			if(StringUtils.isNotBlank(endTime)){
				endTime =DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.getCurrentTime(), 1));
			}
			
			String redKey = "SSAPTJ"+startTime+"_"+endTime+"_"+status+"_"+execDept+"_"+identityCard;
			totalNum = (Integer) redisUtil.get(redKey);
			if(totalNum == null){
				totalNum = operationArrangeService.getTotal(startTime, endTime, status, execDept,identityCard);
				redisUtil.set(redKey, totalNum);
			}
			if (totalNum != null && totalNum - 1 >= 0) {
				list = operationArrangeService.getOperationArrangeVo(startTime, endTime, status, execDept,page,rows,identityCard);
			}
			String val=parameterInnerService.getparameter("deadTime");
			if(val!=null&&!"".equals(val)){
				redisUtil.expire(redKey,Integer.valueOf(val));
			}else{
				redisUtil.expire(redKey, 300);
			}
		}
		map.put("total", totalNum);
		map.put("rows", list);
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);

	}
	/**
	 *
	 * @Description：手术名称map
	 * @Author：tangfeishuai
	 * @CreateDate：2016年5月31日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "opNameMap")
	public void opNameMap() {
		Map<String,String> retMap = operationArrangeService.opNameMap();
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 导出手术计费信息汇总
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:status手术状态
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @param:page当前页数
	 * @param:rows行数
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "outOperationArrangeVo")
	public void outOperationArrangeVo() throws Exception {
		if(StringUtils.isBlank(execDept)){
			SysEmployee empl=ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
			List<SysDepartment> dept=deptInInterService.getDeptByMenutypeAndUserCode(menuAlias, empl.getJobNo());
			for(int i=0;i<dept.size();i++){
				execDept+=dept.get(i).getDeptCode()+",";
			}
			
		}
		if(StringUtils.isNotBlank(endTime)){
			  endTime =DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.getCurrentTime(), 1));
		}
		List<OperationArrangeVo> list = operationArrangeService.getAllOperationArrangeVo(startTime, endTime, status, execDept,identityCard);
		if (list == null || list.isEmpty()) {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
		}
		String head = "";

		String name = "手术安排统计";
		String[] headMessage = { "患者所在科室", "手术开始时间", "病历号", "床号","姓名","性别", "年龄","手术名称", "手术室","施手术者","助手", "巡回护士","洗手护士", "麻醉方法","麻醉医生","麻醉助手" };

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
			fUtil = operationArrangeService.export(list, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
			out.write("success");
		} catch (Exception e) {
			out.write("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("SSTJ_SSAPTJ", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSTJ_SSAPTJ", "手术统计_手术安排统计", "2", "0"), e);
		}finally {
			out.flush();
			out.close();
		}
	}
	
	/**
	 *
	 * @Description：获取病床
	 * @Author：zhangjin
	 * @CreateDate：2016年10月21日  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "getBedno")
	public void getBedno() {
		Map<String,Object> retMap = new HashMap<String,Object>();
		List<BusinessHospitalbed> list = operationArrangeService.getBedno();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				retMap.put(list.get(i).getId(), list.get(i).getBedName());
			}
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 手术安排统计报表打印
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:status手术状态
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @param:page当前页数
	 * @param:rows行数
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="iReportOperationArrange")
	public void iReportOperationArrange(){
		if(StringUtils.isBlank(execDept)){
			SysEmployee empl=ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
			List<SysDepartment> dept=deptInInterService.getDeptByMenutypeAndUserCode(menuAlias, empl.getJobNo());
			for(int i=0;i<dept.size();i++){
				execDept+=dept.get(i).getDeptCode()+",";
			}
			
		}
		 try{
			 if(StringUtils.isNotBlank(endTime)){
				 endTime =DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.getCurrentTime(), 1));
			 }
		   //jasper文件名称 不含后缀
		   String fileName = request.getParameter("fileName");
		   String root_path = request.getSession().getServletContext().getRealPath("/");
		   root_path = root_path.replace('\\', '/');
		   String reportFilePath = root_path + webPath +fileName+".jasper";
		   List<OperationArrangeToReportSlave> list = operationArrangeService.getOperationArrangeToReport(startTime, endTime, status, execDept,identityCard);
		   //javaBean数据封装
		   ArrayList<OperationArrangeToReport> voList = new ArrayList<OperationArrangeToReport>();
		   OperationArrangeToReport vo = new OperationArrangeToReport();
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
			  logger.error("SSTJ_SSAPTJ", e);
			  //hedong 20170407 异常信息保存至mongodb
			  hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSTJ_SSAPTJ", "手术统计_手术安排统计", "2", "0"), e);
		  }
		 
	}
	
	/**
	 * @Description 查询科室信息
	 * @author  zxl
	 * @createDate： 2017-4-14 下午7:15:24 
	 * @modifier 
	 * @modifyDate：
	 * @param 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "querysysDeptment")
	public void querysysDeptmen(){
		List<MenuListVO> ds = operationArrangeService.querysysDeptment();
		String json = JSONUtils.toJson(ds);
		WebUtils.webSendJSON(json);
	}
}
