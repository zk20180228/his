package cn.honry.statistics.operation.operationBillingInfo.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.operation.operationBillingInfo.service.OperationBillingInfoService;
import cn.honry.statistics.operation.operationBillingInfo.vo.OperationBillingInfoVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/***
 * 手术计费信息Action
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/OperationBillingInfo")
public class OperationBillingInfoAction extends ActionSupport{
	private Logger logger=Logger.getLogger(OperationBillingInfoAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	/**
	 * id
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 手术计费service
	 */
	@Autowired
    @Qualifier(value = "operationBillingInfoService")
 	private OperationBillingInfoService operationBillingInfoService;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
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
	 * 手术医生
	 */
	private String opDoctor;
	
	/**
	 * 手术医生科室
	 */
	private String opDoctordept;
	
	/**
	 * 执行科室
	 */
	private String execDept;
	
	/**
	 * 在院状态
	 */
	private String inState;
	
	/**
	 * 手术状态
	 */
	private String opStatus;
	
	/**
	 * 计费状态
	 */
	private String feeState;

	/**
	 * 手术预约开始时间
	 */
	private String beginTime;
	
	/**
	 * 手术预约结束时间
	 */
	private String endTime;
	
	/**
	 * 批费开始时间
	 */
	private String feeBegTime;
	
	/**
	 * 批费结束时间
	 */
	private String feeEndTime;
	
	/**
	 * 分页页数
	 */
	private String page;
	
	/**
	 * 当前记录数
	 */
	private String rows;
	
	/**
	 * 身份证号
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
	public String getOpDoctor() {
		return opDoctor;
	}
	public void setOpDoctor(String opDoctor) {
		this.opDoctor = opDoctor;
	}
	public String getOpDoctordept() {
		return opDoctordept;
	}
	public void setOpDoctordept(String opDoctordept) {
		this.opDoctordept = opDoctordept;
	}
	public String getExecDept() {
		return execDept;
	}
	public void setExecDept(String execDept) {
		this.execDept = execDept;
	}
	public String getInState() {
		return inState;
	}
	public void setInState(String inState) {
		this.inState = inState;
	}
	public String getOpStatus() {
		return opStatus;
	}
	public void setOpStatus(String opStatus) {
		this.opStatus = opStatus;
	}
	public String getFeeState() {
		return feeState;
	}
	public void setFeeState(String feeState) {
		this.feeState = feeState;
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
	public String getFeeBegTime() {
		return feeBegTime;
	}
	public void setFeeBegTime(String feeBegTime) {
		this.feeBegTime = feeBegTime;
	}
	public String getFeeEndTime() {
		return feeEndTime;
	}
	public void setFeeEndTime(String feeEndTime) {
		this.feeEndTime = feeEndTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void setOperationBillingInfoService(OperationBillingInfoService operationBillingInfoService) {
		this.operationBillingInfoService = operationBillingInfoService;
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
	/***
	 * 手术计费信息汇总
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月1日 
	 * @version 1.0
	 */
	@RequiresPermissions(value={"SSJFXXHZ:function:view"}) 
	@Action(value="listOperationBillInfo",results={ @Result(name = "list",location= "/WEB-INF/pages/stat/operation/operationBillingInfo/operationBillingInfoList.jsp") },interceptorRefs={@InterceptorRef(value= "manageInterceptor") })
	public String listOperationBillInfo(){
		beginTime=DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));
		endTime=DateUtils.getDate();
		return "list";
	}
	/**
	 * @Description:根据条件查询手术计费信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间   endTime 结束时间   opStatus 手术状态   execDept 执行科室  feeBegTime 批费开始时间  feeEndTime 批费结束时间
	 * feeState 批费状态  inState 在院状态  opDoctor 手术医生  opDoctordept 手术以医生科室  page 当前页数   rows 分页条数  identityCard身份证号
	 * @return List<OperationBillingInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Action(value = "queryOperationBillingInfo")
	public void queryOperationBillingInfo() {
		List<OperationBillingInfoVo> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		String redKey = "SSJFXXHZ"+beginTime+"_"+endTime+"_"+opStatus+"_"+execDept+"_"+feeBegTime+"_"+feeEndTime+"_"+feeState+"_"+inState+"_"+opDoctor+"_"+opDoctordept+"_"+identityCard;
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum == null){
			totalNum =  operationBillingInfoService.getTotal(beginTime, endTime, opStatus, execDept, feeBegTime, feeEndTime, feeState, inState, opDoctor, opDoctordept,identityCard);
			redisUtil.set(redKey, totalNum);
		}
		if (totalNum != null && totalNum - 1 >= 0) {
			list =  operationBillingInfoService.getOperationBillingInfoVo(beginTime, endTime, opStatus, execDept, feeBegTime, feeEndTime, feeState, inState, opDoctor, opDoctordept, page, rows,identityCard);
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
	 * @Description:得到员工map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月3日
	 * @param:
	 * @return Map<String,String>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Action(value = "getEmpMap")
	public void getEmpMap() {
		Map<String,String> retMap = new HashMap<String,String>();
		retMap = operationBillingInfoService.getEmpMap();
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 导出手术计费信息汇总
	 * @Description:导出手术计费信息汇总
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间   endTime 结束时间   opStatus 手术状态   execDept 执行科室  feeBegTime 批费开始时间  feeEndTime 批费结束时间
	 * feeStates 批费状态  inState 在院状态  opDoctor 手术医生  opDoctordept 手术以医生科室   identityCard身份证号
	 * @return List<OperationBillingInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 * @param identityCard 
	 */
	@Action(value = "outOperationBillingInfoVo")
	public void outOperationBillingInfoVo() throws Exception {
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
		List<OperationBillingInfoVo> list = operationBillingInfoService.getAllOperationBillingInfoVo(beginTime, endTime, opStatus, execDept, feeBegTime, feeEndTime, feeState, inState, opDoctor, opDoctordept,identityCard);
		if (list == null || list.isEmpty()) {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
		}
		String head = "";

		String name = "手术计费信息汇总";
		String[] headMessage = { "病历号", "诊断", "患者姓名", "预约手术日期","手术医生", "医生科室","在院状态", "手术状态","计费状态","收费时间"};

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
			fUtil = operationBillingInfoService.export(list, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
			out.write("success");
		} catch (Exception e) {
			out.write("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("SSTJ_SSJFXXHZ", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSTJ_SSJFXXHZ", "手术统计_手术计费信息汇总", "2", "0"), e);
		}finally {
			out.flush();
			out.close();
		}
	}
	/**
	 * @Description:得到员工
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月20日
	 * @param:
	 * @return 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Action(value = "queryemployeeCombobox")
	public void queryemployeeCombobox() {
		List<SysEmployee> list= operationBillingInfoService.queryemployeeCombobox();
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	/**
	 * @Description:得到科室
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月20日
	 * @param:
	 * @return 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Action(value = "querydeptmentCombobox")
	public void querydeptmentCombobox() {
		List<SysDepartment> list= operationBillingInfoService.querydeptmentCombobox();
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 获取科室
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午4:26:13 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午4:26:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuListVO>
	 *
	 */
	@Action(value = "getDeptList")
	public void getDeptList(){
		List<MenuListVO> ds = operationBillingInfoService.getDeptList();
		String json = JSONUtils.toJson(ds);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 获取医生
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午4:26:13 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午4:26:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuListVO>
	 *
	 */
	@Action(value = "getDoctorList")
	public void getDoctorList(){
		List<MenuListVO> list = operationBillingInfoService.getDoctorList();
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
}
