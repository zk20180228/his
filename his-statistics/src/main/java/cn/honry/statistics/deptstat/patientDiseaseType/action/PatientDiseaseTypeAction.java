package cn.honry.statistics.deptstat.patientDiseaseType.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import cn.honry.statistics.deptstat.patientDiseaseType.service.PatientDiseaseTypeService;
import cn.honry.statistics.deptstat.patientDiseaseType.vo.PatientDiseaseType;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/patientDiseaseType")
@SuppressWarnings({"all"})
public class PatientDiseaseTypeAction extends ActionSupport{
	private static final long serialVersionUID = 8535596438329082479L;
	private Logger logger=Logger.getLogger(PatientDiseaseTypeAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value = "patientDiseaseTypeService")
	private PatientDiseaseTypeService  patientDiseaseTypeService;
	
	public PatientDiseaseTypeService getPatientDiseaseTypeService() {
		return patientDiseaseTypeService;
	}
	public void setPatientDiseaseTypeService(
			PatientDiseaseTypeService patientDiseaseTypeService) {
		this.patientDiseaseTypeService = patientDiseaseTypeService;
	}
	private String deptCode;//科室
	private String sex;//性别
	private String startTime;//出生日期 
	private String endTime;//出生日期 
	private String page;//当前页数
	private String rows;//分页条数
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
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
	/**
	 * 患者疾病类型统计分析
	 * @return
	 */
	@RequiresPermissions(value={"HZJBLXTJFX:function:view"})
	@Action(value = "patientDiseaseList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/patientDiseaseType/patientDiseaseType.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String patientDiseaseList() {
		return "list";
	}
	/**  
	 * 
	 * 患者疾病类型统计分析list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月13日 下午8:56:54 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月13日 下午8:56:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@RequiresPermissions(value={"HZJBLXTJFX:function:query"})
	@Action(value = "querylistPatientDiseaseType", results = { @Result(name = "json",type = "json") })
	public void querylistPatientDiseaseType(){
		try {
			List<PatientDiseaseType> list = patientDiseaseTypeService.queryPatientDisease(deptCode, sex, startTime, endTime, page, rows);
			int total=patientDiseaseTypeService.queryTotal(deptCode, sex, startTime, endTime);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", list);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("KSTJ_HZJBLXTJFX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_HZJBLXTJFX", "科室统计_患者疾病类型统计分析", "2", "0"), e);
		}
	}
	/**  
	 * 
	 * 未治愈ICD
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月14日 上午10:27:50 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月14日 上午10:27:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value="queryIcdHealed")
	public void queryIcdHealed(){
		List<PatientDiseaseType> list = null;
		try {
			list = patientDiseaseTypeService.queryIcdHealed(deptCode, sex, startTime, endTime);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("KSTJ_HZJBLXTJFX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_HZJBLXTJFX", "科室统计_患者疾病类型统计分析", "2", "0"), e);
		}
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 未死亡ICD
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月14日 上午10:27:50 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月14日 上午10:27:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value="queryIcdDeath")
	public void queryIcdDeath(){
		List<PatientDiseaseType> list = null;
		try {
			list = patientDiseaseTypeService.queryIcdDeath(deptCode, sex, startTime, endTime);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("KSTJ_HZJBLXTJFX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_HZJBLXTJFX", "科室统计_患者疾病类型统计分析", "2", "0"), e);
		}
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
}
