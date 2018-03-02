package cn.honry.statistics.deptstat.outandinpatient.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiRegisterGrade;
import cn.honry.base.bean.model.InpatientAccountdetail;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.statistics.bi.outpatient.optRecipedetail.service.OptRecipedetailService;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.service.OutpatientWorkloadService;
import cn.honry.statistics.deptstat.outandinpatient.service.OutAndInPatientService;
import cn.honry.statistics.deptstat.outandinpatient.vo.GetOrOutPatient;
import cn.honry.statistics.deptstat.outandinpatient.vo.InPatientVo;
import cn.honry.statistics.deptstat.outandinpatient.vo.OutPatientVo;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**  
 * 
 * <p>患者入出院信息查询</p>
 * @Author: yuke
 * @CreateDate: 2017年7月5日 下午3:54:26 
 * @Modifier: yuke
 * @ModifyDate: 2017年7月5日 下午3:54:26 
 * @ModifyRmk:  
 * @version: V1.0
 * @param:
 * @throws:
 * @return: 
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/outAndInPatient")
@SuppressWarnings({ "all" })
public class OutAndInPatientAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(OutAndInPatientAction.class);
	private String menuAlias;
	private String startTime;
	private String endTime;
	private String dept;
	private String index;
	private String rows;
	private String page;
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
	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
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

	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}

	@Autowired
	@Qualifier("outAndInPatientService")
	private OutAndInPatientService outAndInPatientService;
	public void setOutAndInPatientService(
			OutAndInPatientService outAndInPatientService) {
		this.outAndInPatientService = outAndInPatientService;
	}
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	public void setParameterInnerDAO(ParameterInnerDAO parameterInnerDAO) {
		this.parameterInnerDAO = parameterInnerDAO;
	}
	/**
	 * <p>视图</p>
	 * @Author: yuke
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@SuppressWarnings("showView")
	@Action(value = "showView", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/patientInandOut/patientInandOut.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String showMedicalSkillLabPeriod(){
		//获取时间
		Date date = new Date();
		startTime = DateUtils.formatDateYM(date)+"-01";
		endTime = DateUtils.formatDateY_M_D(date);
		ServletActionContext.getRequest().setAttribute("startTime", startTime);
		ServletActionContext.getRequest().setAttribute("endTime", endTime);
		return "list";
	}
	/**
	 * 
	 * <p>查询入院患者信息</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月7日 下午3:42:09 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月7日 下午3:42:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action("queryInPatientMsg")
	public void queryInPatientMsg(){
		try {
			if(StringUtils.isBlank(startTime)){
				Date date = new Date();
				startTime = DateUtils.formatDateYM(date)+"-01 00:00:00";
			}
			if(StringUtils.isBlank(endTime)){
				Date date = new Date();
				endTime = DateUtils.formatDateY_M_D(date)+" 23:59:59";
			}
			List<InPatientVo> list = outAndInPatientService.queryinPatientMsg(startTime,endTime,dept,menuAlias,page,rows);
			String keys=dept;
			String redKey = menuAlias+"RY:"+startTime+"_"+endTime+"_"+keys.replace(",", "_")+"_"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			Integer totalNum = (Integer) redisUtil.get(redKey);
			if(totalNum==null){
				totalNum = outAndInPatientService.queryinPatientTotal(startTime, endTime, dept, menuAlias);
				redisUtil.set(redKey, totalNum);
			}
			String val = parameterInnerDAO.getParameterByCode("deadTime");
			if(StringUtils.isNotBlank(val)){
				redisUtil.expire(redKey,Integer.valueOf(val));
			}else{
				redisUtil.expire(redKey, 300);
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", totalNum);
			map.put("rows", list);
			String json = JSONUtils.toJson(map,DateUtils.DATETIME_FORMAT);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("KETJ_ZYKETJ_HZRCYXXCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KETJ_ZYKETJ_HZRCYXXCX","科室统计_住院科室统计_患者入出院信息查询", "2", "0"), e);
		}
	}
	
	/**
	 * 
	 * <p>查询出院患者信息</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月7日 下午3:42:09 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月7日 下午3:42:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action("queryOutPatientMsg")
	public void queryOutPatientMsg(){
			try {
				if(StringUtils.isBlank(startTime)){
					Date date = new Date();
					startTime = DateUtils.formatDateYM(date)+"-01 00:00:00";
				}
				if(StringUtils.isBlank(endTime)){
					Date date = new Date();
					endTime = DateUtils.formatDateY_M_D(date)+" 23:59:59";
				}
				List<OutPatientVo> list = outAndInPatientService.queryOutPatientMsg(startTime,endTime,dept,menuAlias,page,rows);
				String keys=dept;
				String redKey = menuAlias+"CY:"+startTime+"_"+endTime+"_"+keys.replace(",", "_")+"_"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
				Integer totalNum = (Integer) redisUtil.get(redKey);
				if(totalNum==null){
					totalNum = outAndInPatientService.queryOutPatientMsg(startTime, endTime, dept, menuAlias);
					redisUtil.set(redKey, totalNum);
				}
				String val = parameterInnerDAO.getParameterByCode("deadTime");
				if(StringUtils.isNotBlank(val)){
					redisUtil.expire(redKey,Integer.valueOf(val));
				}else{
					redisUtil.expire(redKey, 300);
				}
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("total", totalNum);
				map.put("rows", list);
				String json = JSONUtils.toJson(map,DateUtils.DATETIME_FORMAT);
				WebUtils.webSendJSON(json);
			} catch (Exception e) {
				logger.error("KETJ_ZYKETJ_HZRCYXXCX", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KETJ_ZYKETJ_HZRCYXXCX","科室统计_住院科室统计_患者入出院信息查询", "2", "0"), e);
			}
	}
	/**
	 * 
	 * <p>查询转入院患者信息</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月7日 下午3:42:09 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月7日 下午3:42:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action("queryGetInPatientMsg")
	public void queryGetInPatientMsg(){
		
		try {
			if(StringUtils.isBlank(startTime)){
				Date date = new Date();
				startTime = DateUtils.formatDateYM(date)+"-01 00:00:00";
			}
			if(StringUtils.isBlank(endTime)){
				Date date = new Date();
				endTime = DateUtils.formatDateY_M_D(date)+" 23:59:59";
			}
			List<GetOrOutPatient> list = outAndInPatientService.queryGetInPatientMsg(startTime,endTime,dept,menuAlias,page,rows);
			String keys=dept;
			String redKey = menuAlias+"ZR:"+startTime+"_"+endTime+"_"+keys.replace(",", "_")+"_"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			Integer totalNum = (Integer) redisUtil.get(redKey);
			if(totalNum==null){
				totalNum = outAndInPatientService.queryGetInPatientMsg(startTime, endTime, dept, menuAlias);
				redisUtil.set(redKey, totalNum);
			}
			String val = parameterInnerDAO.getParameterByCode("deadTime");
			if(StringUtils.isNotBlank(val)){
				redisUtil.expire(redKey,Integer.valueOf(val));
			}else{
				redisUtil.expire(redKey, 300);
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", totalNum);
			map.put("rows", list);
			String json = JSONUtils.toJson(map,DateUtils.DATETIME_FORMAT);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("KETJ_ZYKETJ_HZRCYXXCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KETJ_ZYKETJ_HZRCYXXCX","科室统计_住院科室统计_患者入出院信息查询", "2", "0"), e);
		}
		
	}
	/**
	 * 
	 * <p>查询转出院患者信息</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月7日 下午3:42:09 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月7日 下午3:42:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action("queryGetOutPatientMsg")
	public void queryGetOutPatientMsg(){
		
		try {
			if(StringUtils.isBlank(startTime)){
				Date date = new Date();
				startTime = DateUtils.formatDateYM(date)+"-01 00:00:00";
			}
			if(StringUtils.isBlank(endTime)){
				Date date = new Date();
				endTime = DateUtils.formatDateY_M_D(date)+" 23:59:59";
			}
			List<GetOrOutPatient> list = outAndInPatientService.queryGetOutPatientMsg(startTime,endTime,dept,menuAlias,page,rows);
			String keys=dept;
			String redKey = menuAlias+"ZC:"+startTime+"_"+endTime+"_"+keys.replace(",", "_")+"_"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			Integer totalNum = (Integer) redisUtil.get(redKey);
			if(totalNum==null){
				totalNum = outAndInPatientService.queryGetOutPatientMsg(startTime, endTime, dept, menuAlias);
				redisUtil.set(redKey, totalNum);
			}
			String val = parameterInnerDAO.getParameterByCode("deadTime");
			if(StringUtils.isNotBlank(val)){
				redisUtil.expire(redKey,Integer.valueOf(val));
			}else{
				redisUtil.expire(redKey, 300);
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", totalNum);
			map.put("rows", list);
			String json = JSONUtils.toJson(map,DateUtils.DATETIME_FORMAT);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("KETJ_ZYKETJ_HZRCYXXCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KETJ_ZYKETJ_HZRCYXXCX","科室统计_住院科室统计_患者入出院信息查询", "2", "0"), e);
		}
		
	}
	/**
	 * 
	 * 
	 * <p>提供导出功能 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月10日 下午4:30:27 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月10日 下午4:30:27 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action("exprotList")
	public void exprotList(){
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			
			String name=null;
			String head ="";
			String[] headMessage=null;
			List<String> record=new ArrayList<String>();
			if("0".equals(index)){//入院患者信息
				List<InPatientVo> list = outAndInPatientService.queryinPatientMsg(startTime,endTime,dept,menuAlias,null,null);
				if (list == null || list.isEmpty()) {
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
				}
				name="入院患者信息";
				headMessage=new String[]{"住院号","姓名","性别","年龄","床位","入院时间","地址","电话","费别","诊断"};
				for(InPatientVo vo:list){
					record.add(vo.toString());
				}
			}else if("1".equals(index)){//出院患者信息
				List<OutPatientVo> list = outAndInPatientService.queryOutPatientMsg(startTime,endTime,dept,menuAlias,null,null);
				if (list == null || list.isEmpty()) {
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
				}
				name="出院患者信息";
				headMessage=new String[]{"住院号","姓名","性别","年龄","床位","主治医生","主管护师","入院日期","出院日期","出院情况","诊断","费别"};
				for(OutPatientVo vo:list){
					record.add(vo.toString());
				}
			}else{
				List<GetOrOutPatient> list=null;
				if("2".equals(index)){//转入院患者信息
					list = outAndInPatientService.queryGetInPatientMsg(startTime,endTime,dept,menuAlias,null,null);
					name="转入院患者信息";
				}else if("3".equals(index)){//转出院患者信息
					list = outAndInPatientService.queryGetOutPatientMsg(startTime,endTime,dept,menuAlias,null,null);
					name="转出院患者信息";
				}
				if (list == null || list.isEmpty()) {
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
				}
				headMessage=new String[]{"住院号","姓名","性别","费别","转前科室","转前床位","转后科室","转后床位","入院日期","转科日期","诊断"};
				for(GetOrOutPatient vo:list){
					record.add(vo.toString());			
				}
			}
			
			FileUtil fUtil = new FileUtil();
			String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
			String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/" + fileName;
			fUtil.setFilePath(filePath);
			
			for (String message : headMessage) {
				head +=  message+",";
			}
			fUtil.write(head);
			for(String reco:record){
				fUtil.write(reco);
			}
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
		} catch (Exception e) {
			logger.error("KETJ_ZYKETJ_HZRCYXXCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KETJ_ZYKETJ_HZRCYXXCX","科室统计_住院科室统计_患者入出院信息查询", "2", "0"), e);
		}
	}
	
}
