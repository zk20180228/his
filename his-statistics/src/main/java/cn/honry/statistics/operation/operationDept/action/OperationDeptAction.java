package cn.honry.statistics.operation.operationDept.action;

import java.io.File;
import java.io.PrintWriter;
import java.net.URLDecoder;
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
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.operation.operationDept.service.OperationDeptService;
import cn.honry.statistics.operation.operationDept.vo.OpDeptDetailVo;
import cn.honry.statistics.operation.operationDept.vo.OpDeptTotalVo;
import cn.honry.statistics.operation.operationDept.vo.OpDoctorDetailToReport;
import cn.honry.statistics.operation.operationDept.vo.OpDoctorDetailVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 手术安排汇总ACTION
 * @author tangfeishuai
 *@CreateDate 2016-05-27
 *@version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/OperationDept")
public class OperationDeptAction extends ActionSupport{
	private Logger logger=Logger.getLogger(OperationDeptAction.class);
	private static final long serialVersionUID = 1L;
	//hedong 用于报表打印 使用java分隔符 否则 linux系统会找不到相应路径
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
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
	private String beginTime;
	/**
	 * 结束时间
	 */
	private String endTime;
	/**
	 * 手术室
	 */
	private String execDept;
	/**
	 * 手术医生科室
	 */
	private String opcDept;
	/**
	 * 手术医生
	 */
	private String opDoctor;
	
	/**
	 * 身份证号；
	 */
	private String identityCard;
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;

	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String deptName;

	
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

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
	@Autowired
	@Qualifier(value="employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier(value="iReportService")
	private IReportService iReportService;
	
	public IReportService getiReportService() {
		return iReportService;
	}

	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	/**
	 * 注入手术安排汇总Service
	 */
	@Autowired 
	@Qualifier(value = "operationDeptService")
	private OperationDeptService operationDeptService;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getOpcDept() {
		return opcDept;
	}

	public void setOpcDept(String opcDept) {
		this.opcDept = opcDept;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOpDoctor() {
		return opDoctor;
	}

	public void setOpDoctor(String opDoctor) {
		this.opDoctor = opDoctor;
	}

	public void setOperationDeptService(OperationDeptService operationDeptService) {
		this.operationDeptService = operationDeptService;
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
	@RequiresPermissions(value={"SSKSHZ:function:view"}) 
	@Action(value = "listOperationDept", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/operation/operationDept/operationDeptList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listOperationDept() {
		endTime=DateUtils.getDate();
		beginTime=DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));
		return "list";
	}
	
	/**
	 * 根据条件手术医生明细
	 * @Description:根据条件手术医生明细
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:page 当前页数
	 * @param:rows 分页条数
	 * @param:identityCard 身份证号
	 * @param:opDoctor 手术医生
	 * @param:opcDept 医生科室
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Action(value = "queryOpDoctorDetailVo")
	public void queryOpDoctorDetailVo() {
		List<OpDoctorDetailVo> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		String redKey = "SSKSHZSSYSMX"+beginTime+"_"+endTime+"_"+opcDept+"_"+execDept+"_"+opDoctor+"_"+identityCard;
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum == null){
			totalNum =  operationDeptService.getOpDoctorTotal(beginTime, endTime,opcDept,execDept,opDoctor,identityCard);
			redisUtil.set(redKey, totalNum);
		}
		if (totalNum != null && totalNum - 1 >= 0) {
			list =  operationDeptService.getOpDoctorDetailVo(beginTime, endTime,opcDept,execDept,opDoctor,page, rows,identityCard);
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
	 * 根据条件查询手术科室汇总
	 * @Description:根据条件查询手术科室汇总
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:page 当前页数
	 * @param:rows 分页条数
	 * @param:opcDept 医生科室
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Action(value = "queryOpDeptTotalVo")
	public void queryOpDeptTotalVo() {
		List<OpDeptTotalVo> list = operationDeptService.getOpDeptTotalVo(beginTime, endTime,opcDept,execDept, page, rows);
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	/**
	 *根据条件查询手术科室明细
	 * @Description:根据条件查询手术科室明细
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:page 当前页数
	 * @param:rows 分页条数
	 * @param:identityCard 身份证号
	 * @param:opDoctor 手术医生
	 * @param:opcDept 医生科室
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Action(value = "queryOpDeptDetailVo")
	public void queryOpDeptDetailVo() {
		List<OpDeptDetailVo> list =new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		String redKey ="SSKSHZSSKSMX"+beginTime+"_"+endTime+"_"+opcDept+"_"+execDept+"_"+opDoctor;
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum == null){
			totalNum = operationDeptService.getOpDeptDetailTotal(beginTime, endTime,opcDept, execDept,opDoctor);
			redisUtil.set(redKey, totalNum);
		}
		if (totalNum != null && totalNum - 1 >= 0) {
			list =  operationDeptService.getOpDeptDetailVo(beginTime, endTime,opcDept, execDept,opDoctor, page, rows);
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
	 * 导出手术医生明细
	 * @Description:导出手术医生明细
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:page 当前页数
	 * @param:flg 存放标记，判断全查还是分页查询
	 * @param:identityCard 身份证号
	 * @param:opDoctor 手术医生
	 * @param:opcDept 医生科室
	 * @param:identityCard 身份证号
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Action(value = "outOpDoctorDetailVo")
	public void outOpDoctorDetailVo() throws Exception {
		//不需要分页查询，全查
		String flg = "a";
		List<OpDoctorDetailVo> list = operationDeptService.getOpDoctorDetailVo(beginTime, endTime, opcDept,execDept,opDoctor, page, flg,identityCard);
		if (list == null || list.isEmpty()) {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
		}
		String head = "";

		String name = "手术医生明细";
		String[] headMessage = { "医生科室", "医生名称", "手术时间", "病历号","姓名", "手术名称", "手术费","记账科室","操作员"};

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
			fUtil = operationDeptService.exportOpDoctorDetailVo(list, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
			out.write("success");
		} catch (Exception e) {
			out.write("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("SSTJ_SSKSHZ", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSTJ_SSKSHZ", "手术管理_手术科室汇总", "2", "0"), e);
		}finally {
			out.flush();
			out.close();
		}
	}
	
	/**
	 * @Description:员工MAp
	 * @Author: zhangjin
	 * @CreateDate: 2017年2月20日
	 * @param:
	 * @return 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Action(value = "emplMapIninter")
	public void emplMapIninter() {
		Map<String, String> map =employeeInInterService.queryEmpCodeAndNameMap();
		String json  = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * @Description:科室MAp
	 * @Author: zhangjin
	 * @CreateDate: 2017年2月20日
	 * @param:
	 * @return 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Action(value = "deptMapIninter")
	public void deptMapIninter() {
		Map<String, String> map =deptInInterService.querydeptCodeAndNameMap();
		String json  = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 手术科室汇总>手术医生明细报表打印
	 * @Description:导出手术医生明细
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间     
	 * @param:endTime 结束时间
	 * @param:execDept 执行科室   
	 * @param:identityCard 身份证号
	 * @param:opDoctor 手术医生
	 * @param:opcDept 医生科室
	 * @param:deptMap 部门map
	 * @param:empMap 员工表Map
	 * @param:identityCard 身份证号
	 * @return List<OpDoctorDetailVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Action(value = "OpDoctorDetailReport")
	public void OpDoctorDetailReport() {
	try{
		//jasper文件名称 不含后缀
		String fileName = request.getParameter("fileName");
		String root_path = request.getSession().getServletContext().getRealPath("/");
		root_path = root_path.replace('\\', '/');
		String reportFilePath = root_path + webPath+fileName+".jasper";
		//部门和员工表Map 用于数据的封装
		Map<String, String> deptMap =deptInInterService.querydeptCodeAndNameMap();
		Map<String, String> empMap =employeeInInterService.queryEmpCodeAndNameMap();
		//获得javaBean数据源 list
		List<OpDoctorDetailVo> list = operationDeptService.getOpDoctorDetailToReport(beginTime,endTime,opcDept,execDept,opDoctor,deptMap,empMap,identityCard);
		//javaBean数据源封装   
		ArrayList<OpDoctorDetailToReport> voList = new ArrayList<OpDoctorDetailToReport>();
		OpDoctorDetailToReport vo = new OpDoctorDetailToReport();
		vo.setItemList(list);
		voList.add(vo);
		JRDataSource jrd=new JRBeanCollectionDataSource(voList);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("loginTime", beginTime);
		parameters.put("endTime", endTime);
		parameters.put("deptName", URLDecoder.decode(deptName,"UTF-8"));
		parameters.put("SUBREPORT_DIR", root_path + webPath);
		//调用公共服务进行报表打印
		iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
	}catch(Exception e){
		//hedong 20170407 异常信息输出至日志文件
		logger.error("SSTJ_SSKSHZ", e);
		//hedong 20170407 异常信息保存至mongodb
		hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSTJ_SSKSHZ", "手术管理_手术科室汇总", "2", "0"), e);
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
		List<MenuListVO> ds = operationDeptService.querysysDeptment();
		String json = JSONUtils.toJson(ds);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * @Description 查询医生信息
	 * @author  zxl
	 * @createDate： 2017-4-14 下午7:15:24 
	 * @modifier 
	 * @modifyDate：
	 * @param deptCode科室code
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryDoctor")
	public void queryDoctor(){
		String deptCode = ServletActionContext.getRequest().getParameter("deptCode");
		List<MenuListVO> ds = operationDeptService.queryDoctor(deptCode);
		String json = JSONUtils.toJson(ds);
		WebUtils.webSendJSON(json);
	}
	
}
