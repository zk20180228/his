package cn.honry.statistics.deptstat.criticallyIllPatient.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import cn.honry.statistics.deptstat.criticallyIllPatient.service.CriticallyIllPatientService;
import cn.honry.statistics.deptstat.criticallyIllPatient.vo.CriticallyIllPatientVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**  
 * 
 * 病危患者信息统计
 * @Author: wangshujuan
 * @CreateDate: 2017年11月14日 下午4:09:43 
 * @Modifier: wangshujuan
 * @ModifyDate: 2017年11月14日 下午4:09:43 
 * @ModifyRmk:  
 * @version: V1.0
 * @param deptCode 
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/criticallyIllPatient")
@SuppressWarnings({"all"})
public class CriticallyIllPatientAction extends ActionSupport{
	private static final long serialVersionUID = 8535596438329082479L;
	@Autowired
	@Qualifier(value = "criticallyIllPatientService")
	private CriticallyIllPatientService criticallyIllPatientService;
	public void setCriticallyIllPatientService(
			CriticallyIllPatientService criticallyIllPatientService) {
		this.criticallyIllPatientService = criticallyIllPatientService;
	}
	
	private Logger logger=Logger.getLogger(CriticallyIllPatientAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private String deptCode;
	private String menuAlias;
	private String startTime;
	private String endTime;
	private String sex;
	private String page;
	private String rows;
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
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * 病危患者信息统计页面
	 * @return
	 */
	@RequiresPermissions(value={"BWHZXXTJ:function:view"})
	@Action(value = "listCriticallyIllPatient", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/criticallyIllPatient/criticallyIllPatient.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listCriticallyIllPatient() {
		//获取时间，当前年及前两年
		Date date=new Date();
		endTime = DateUtils.formatDateY(date);
		startTime = DateUtils.formatDateY(DateUtils.addYear(date, -2));
		return "list";
	}
	
	
	/**  
	 * 
	 * 病危患者信息统计
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月14日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月14日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	
	@Action(value="queryCriticallyIllPatient")
	public void queryCriticallyIllPatient(){
		try {
			if(StringUtils.isBlank(startTime)){
				Date date = new Date();
				startTime = DateUtils.formatDateY(date);
			}else{
				startTime = startTime;
			}
			if(StringUtils.isBlank(endTime)){
				Date date = new Date();
				endTime = DateUtils.formatDateY(date);
			}else{
				endTime = endTime;
			}
			String queryIds = "(";//过滤后实际查询的科室
			if(StringUtils.isNotBlank(deptCode)){
				String[] idsArray = deptCode.split(",");
				for(int i = 0; i < idsArray.length; i++){
					queryIds = queryIds+"'"+idsArray[i]+"'";
					if(i<idsArray.length-1){
						queryIds=queryIds+",";
					}
				}
				queryIds=queryIds+")";
			}else{
				queryIds=null;
			}
			List<CriticallyIllPatientVo> CriticallyIllPatientList = criticallyIllPatientService.queryCriticallyIllPatient(startTime,endTime,queryIds,sex,menuAlias,page,rows);
			int total=criticallyIllPatientService.getTotalCriticallyIllPatient(startTime,endTime,queryIds,sex,menuAlias);
			Map<Object, Object> map=new HashMap<Object,Object>();
			map.put("total",total);
			map.put("rows", CriticallyIllPatientList);
			String json = JSONUtils.toJson(CriticallyIllPatientList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("FYTJ_BWHZXXTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("FYTJ_BWHZXXTJ", "住院统计分析_病危患者信息统计", "2", "0"), e);
		}
	}
}
