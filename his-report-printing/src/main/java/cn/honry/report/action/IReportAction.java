package cn.honry.report.action;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.BirthCertificate;
import cn.honry.base.bean.model.CriticalNotice;
import cn.honry.base.bean.model.DepositReminder;
import cn.honry.base.bean.model.EmrRecordMain;
import cn.honry.base.bean.model.IReportJavaBeanDemo;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.report.service.IReportService;
import cn.honry.report.vo.EmrRecordMainReport;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**  
 * @className：IReportAction 
 * @Description：  报表Action 
 * @Author：hedong
 * @CreateDate：2016-3-7 上午10:41:31  
 * @Modifier：hedong
 * @ModifyDate：2016-3-7 上午10:41:31  
 * @ModifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/iReport/iReportPrint")
public class IReportAction extends ActionSupport{
	private Logger logger=Logger.getLogger(IReportAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	/**
	 * 
	 */	
	private static final long serialVersionUID = -3657706904580719078L;
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	
	/**  
	 *  
	 * @Description：门诊医嘱单打印（打印wanxing制作的报表）
	 * @Author：hedong
	 * @CreateDate：2016-3-10  下午5:14 
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-14  下午13:14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportToMedicalRecord")
	public void iReportToMedicalRecord() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String RECIPE_NO= request.getParameter("RECIPE_NO");//jasper文件所用到的参数 RECIPE_NO
			String isLow = request.getParameter("isLow");//jasper文件所用到的参数 RECIPE_NO
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			request.getSession().getServletContext().getRealPath("/");
			StringBuffer path=new StringBuffer();
			path.append(request.getSession().getServletContext().getRealPath("/"));
			path.append(webPath);
			parameterMap.put("RECIPE_NO", RECIPE_NO);
			parameterMap.put("hName",HisParameters.PREFIXFILENAME);
			parameterMap.put("cId", path+"outpatientAdviceChildrenList.jasper");
			parameterMap.put("PAYDATE",DateUtils.formatDateY_M_D_H_M(new Date()));
			if(StringUtils.isBlank(isLow)){
				parameterMap.put("isLow", "底方");
			}else{
				parameterMap.put("isLow", null);
			}
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_MZYZD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_MZYZD", "报表打印_门诊医嘱单", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：住院接诊(腕带打印)（打印dh制作的报表）
	 * @author  lyy
	 * @createDate： 2016年3月14日 下午4:26:19 
	 * @modifier lyy
	 * @modifyDate：2016年3月14日 下午4:26:19  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "iReportToSpireRecord")
	public void iReportToSpireRecord() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String medicalrecordId= request.getParameter("medicalrecordId");//jasper文件所用到的参数 INPATIENT_NO
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("medicalrecordId", medicalrecordId);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_WD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_WD", "报表打印_腕带", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：手术安排统计
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-29  下午1:14 
	 * @Modifier：tangfeishuai
	 * @ModifyDate：2016-6-29  下午1:14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportOperationArrange")
	public void iReportOperationArrange() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String beginTime= request.getParameter("beginTime");
			String endTime= request.getParameter("endTime");
			String status= request.getParameter("status");
			String execDept= request.getParameter("execDept");
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			StringBuffer path=new StringBuffer();
			path.append(request.getSession().getServletContext().getRealPath("/"));
			path.append(webPath);
			parameterMap.put("cId", path+"SSAPTJC.jasper");
			parameterMap.put("beginTime", beginTime);
			parameterMap.put("endTime", endTime);
			parameterMap.put("status", status);
			parameterMap.put("execDept", execDept);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_SSAP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_SSAP", "报表打印_手术安排", "2", "0"), e);
		}
	}
	/**
	 * @Description：门诊挂号单
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-11  下午5:14 
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-11  下午13:14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportToRegisterInfo")
	public void iReportToRegisterInfo() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("tId", request.getParameter("tId"));
			parameterMap.put("iReportToregisterInfo", HisParameters.PREFIXFILENAME);
			parameterMap.put("hospitalName", HisParameters.PREFIXFILENAME);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_MZGHD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_MZGHD", "报表打印_门诊挂号单", "2", "0"), e);
		}
	}
	
	/**  
	 * 
	 * 报表批量打印
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月15日 下午5:49:42 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月15日 下午5:49:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value = "iReportToStatAdvice")
	public void iReportToStatAdvice() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String no=request.getParameter("RECIPE_NO");
			String[] strings =no.split(",");
			List<HashMap<String,Object>> o=new ArrayList<HashMap<String,Object>>();
			for (String string : strings) {
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				parameterMap.put("RECIPE_NO", string);
				StringBuffer path=new StringBuffer();
				path.append(request.getSession().getServletContext().getRealPath("/"));
				path.append(webPath);
				parameterMap.put("hName",HisParameters.PREFIXFILENAME);
				parameterMap.put("cId", path+"outpatientAdviceStatChildrenList.jasper");
				parameterMap.put("payDate",DateUtils.formatDateY_M_D_H_M(new Date()));
				o.add(parameterMap);
				
			}	
			iReportService.doReport2(request,WebUtils.getResponse(),fileName,o);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_PLDY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_PLDY", "报表打印_批量打印", "2", "0"), e);
		}
	}
	
	/**
	 * @Description：手术申请单
	 * @Author：zhuxiaolu
	 * @CreateDate：2016-1-14
	 * @throws Exception
	 */
	@Action(value = "iReportToOpperactionApply")
	public void iReportToOpperactionApply() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String opId= request.getParameter("opId");//jasper文件所用到的参数 opId
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap parameterMap=new HashMap();
			parameterMap.put("opId", opId);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_SSSQD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_SSSQD", "报表打印_手术申请单", "2", "0"), e);
		}
	}
	/**
	 * @Description：会诊申请单
	 * @Author：tuchuanjiang
	 * @CreateDate：2016-3-14
	 * @throws Exception
	 */
	@Action(value = "iReportConsulation")
	public void iReportConsulation() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String mid= request.getParameter("mainId");//jasper文件所用到的参数 mainId
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("mid", mid);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_HZSQD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_HZSQD", "报表打印_会诊申请单", "2", "0"), e);
		}
	}
	/**
	 * @Description：门诊会诊申请单
	 * @Author：tuchuanjiang
	 * @CreateDate：2016-3-14
	 * @throws Exception
	 */
	@Action(value = "iReportConsulationMZ")
	public void iReportConsulationMZ() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String mid= request.getParameter("mainId");//jasper文件所用到的参数 mainId
			String mid1= request.getParameter("mainId1");//jasper文件所用到的参数 mainId1
			String bir =request.getParameter("birthdays");
			java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date  birthdays = formatter.parse(bir);//出生日期
			String age = calAge(birthdays);
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("mid", mid);
			parameterMap.put("mid1", mid1);
			parameterMap.put("age", age);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_MZHZSQD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_MZHZSQD", "报表打印_门诊会诊申请单", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：门诊检查单
	 * @Author：tangfeishuai
	 * @CreateDate：2016-3-16  下午1:14 
	 * @Modifier：tangfeishuai
	 * @ModifyDate：2016-3-16  下午1:14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	
	@Action(value = "iReportToInspectionCheck")
	public void iReportToInspectionCheck() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String clinicCode= request.getParameter("clinicCode");//jasper文件所用到的参数 clinicCode
			User user = (User)SessionUtils.getCurrentUserFromShiroSession();
			String execDpcd= request.getParameter("RECIPE_NO");//jasper文件所用到的参数 execDpcd
			String applydoc=user.getName();
			Date dt=new Date();
			String newdt=DateUtils.formatDateY_M_D(dt);
			
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			request.getSession().getServletContext().getRealPath("/");
			StringBuffer path=new StringBuffer();
			path.append(request.getSession().getServletContext().getRealPath("/"));
			path.append(webPath);
			parameterMap.put("cId", path+"JCSQDC.jasper");
			parameterMap.put("CLINIC_CODE", clinicCode);
			parameterMap.put("EXEC_DPCD", execDpcd);
			parameterMap.put("APPLYDOC", applydoc);
			parameterMap.put("hospitalName", HisParameters.PREFIXFILENAME);
			parameterMap.put("APPLYDATE", newdt);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_MZJCD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_MZJCD", "报表打印_门诊检查单", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：挂号医生排班工作量统计
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-25  下午1:14 
	 * @Modifier：tangfeishuai
	 * @ModifyDate：2016-6-25  下午1:14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	
	@Action(value = "iReportResDocWork")
	public void iReportResDocWork() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String doctorName= request.getParameter("doctorName");//jasper文件所用到的参数 clinicCode
			
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			request.getSession().getServletContext().getRealPath("/");
			StringBuffer path=new StringBuffer();
			path.append(request.getSession().getServletContext().getRealPath("/"));
			path.append(webPath);
			parameterMap.put("cId", path+"MZGHYSGZLTJC.jasper");
			parameterMap.put("doctorName", doctorName);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_PBGZL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_PBGZL", "报表打印_排班工作量", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：住院用药统计
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-25  下午1:14 
	 * @Modifier：tangfeishuai
	 * @ModifyDate：2016-6-25  下午1:14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	
	@Action(value = "iReportAdmissionStatistics")
	public void iReportAdmissionStatistics() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String beginTime= request.getParameter("startTime");
			String endTime= request.getParameter("endTime");
			String deptCode= request.getParameter("deptCode");
			String storageCode= request.getParameter("storageCode");
			String drugType= request.getParameter("drugType");
			String outType= request.getParameter("outType");
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			request.getSession().getServletContext().getRealPath("/");
			StringBuffer path=new StringBuffer();
			path.append(request.getSession().getServletContext().getRealPath("/"));
			path.append(webPath);
			parameterMap.put("cId", path+"ZYBYYCXTJC.jasper");
			parameterMap.put("beginTime", beginTime);
			parameterMap.put("endTime", endTime);
			parameterMap.put("deptCode", deptCode);
			parameterMap.put("storageCode", storageCode);
			parameterMap.put("drugType", drugType);
			parameterMap.put("outType", outType);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_ZYYY", "报表打印_住院用药", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：护士站领药汇总查询
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-25  下午1:14 
	 * @Modifier：tangfeishuai
	 * @ModifyDate：2016-6-25  下午1:14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	
	@Action(value = "iReportNurseBillHz")
	public void iReportNurseBillHz() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String beginTime= request.getParameter("beginTime");
			String endTime= request.getParameter("endTime");
			String applyState= request.getParameter("applyState");
			String drugName= request.getParameter("bname");
			String billClassCode= request.getParameter("billClassCode");
			String deptCode= request.getParameter("deptCode");
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			request.getSession().getServletContext().getRealPath("/");
			StringBuffer path=new StringBuffer();
			path.append(request.getSession().getServletContext().getRealPath("/"));
			path.append(webPath);
			parameterMap.put("cId", path+"HSZLYYPHZTJC.jasper");
			parameterMap.put("beginTime", beginTime);
			parameterMap.put("endTime", endTime);
			parameterMap.put("deptCode", deptCode);
			parameterMap.put("applyState", applyState);
			parameterMap.put("drugName", drugName);
			parameterMap.put("billClassCode",billClassCode);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_HSZLY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_HSZLY", "报表打印_护士站领药汇总", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：护士站领药明细查询
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-25  下午1:14 
	 * @Modifier：tangfeishuai
	 * @ModifyDate：2016-6-25  下午1:14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	
	@Action(value = "iReportNurseBillMx")
	public void iReportNurseBillMx() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String beginTime= request.getParameter("beginTime");
			String endTime= request.getParameter("endTime");
			String applyState= request.getParameter("applyState");
			String drugName= request.getParameter("bname");
			String billClassCode= request.getParameter("billClassCode");
			String deptCode= request.getParameter("deptCode");
			
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			request.getSession().getServletContext().getRealPath("/");
			StringBuffer path=new StringBuffer();
			path.append(request.getSession().getServletContext().getRealPath("/"));
			path.append(webPath);
			parameterMap.put("cId", path+"HSZLYYPMXTJC.jasper");
			parameterMap.put("beginTime", beginTime);
			parameterMap.put("endTime", endTime);
			parameterMap.put("deptCode", deptCode);
			parameterMap.put("applyState", applyState);
			parameterMap.put("drugName", drugName);
			parameterMap.put("billClassCode",billClassCode);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_HSZLY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_HSZLY", "报表打印_护士站领药明细", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：门诊挂号员日结单
	 * @Author：tangfeishuai
	 * @CreateDate：2016-3-21  下午1:14 
	 * @Modifier：tangfeishuai
	 * @ModifyDate：2016-3-21  下午1:14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportToInspectionDailySettlement")
	public void iReportToInspectionDailySettlement() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String startTime= request.getParameter("startTime");//jasper文件所用到的参数starttime
			String endTime= request.getParameter("endTime");//jasper文件所用到的参数 endtime
			User user = (User) SessionUtils.getCurrentUserFromShiroSession();
			String empName=user.getName();//jasper文件所用到的参数 员工姓名
			String eid=user.getAccount();
			String rid=request.getParameter("rid");//jasper文件所用到的参数 该记录主键id
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			request.getSession().getServletContext().getRealPath("/");
			StringBuffer path=new StringBuffer();
			path.append(request.getSession().getServletContext().getRealPath("/"));
			path.append(webPath);
			parameterMap.put("cID", path+"MZGHYRJC.jasper");
			parameterMap.put("STARTTIMEE", startTime);
			parameterMap.put("ENDTIMEE", endTime);
			parameterMap.put("USERNAME", empName);
			parameterMap.put("RID", rid);
			parameterMap.put("EID", eid);
			parameterMap.put("FS", "1@现金,2@银联卡,3@支票,4@院内账户");
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_GHYRJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_GHYRJ", "报表打印_门诊挂号员日结", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：手术收费通知单打印(固定ID)
	 * @Author：wanxing
	 * @CreateDate：2016-3-15  上午8:57 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportToArrangeList")
	public void iReportToArrangeList() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String opid= request.getParameter("opid");//jasper文件所用到的参数 RECIPE_NO
			String totCost= request.getParameter("totCost");//jasper文件所用到的参数 RECIPE_NO
			String recipeNo= request.getParameter("recipeNo");
			recipeNo=recipeNo.replace(",", "','");
			recipeNo="('"+recipeNo+"')";
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("opid", opid);
			parameterMap.put("recipeNo", recipeNo);
			parameterMap.put("totCost", totCost);
			
			String path = request.getSession().getServletContext().getRealPath("/");
			parameterMap.put("SUBREPORT_DIR", path+webPath);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_SSSFTZD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_SSSFTZD", "报表打印_手术收费通知单", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：药品入库单打印
	 * @Author：dutianliang
	 * @CreateDate：2016-3-17  下午18:03
	 * @ModifyRmk：  zpty
	 * @ModifyDate：2016-5-17  下午18:03
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportToYPRKD")
	public void iReportToYPRKD() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String reportIds = request.getParameter("reportIds");//jasper文件名称 不含后缀
			reportIds = ("('" + reportIds + "')").replaceAll(",", "','");
			String in_list_code = request.getParameter("reportListCodes");//jasper文件名称 不含后缀
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("IN_LIST_CODE", in_list_code);
			parameterMap.put("IDS", reportIds);
			String path = request.getSession().getServletContext().getRealPath("/");
			parameterMap.put("SUBREPORT_DIR", path+webPath);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_YPRKD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_YPRKD", "报表打印_药品入库单", "2", "0"), e);
		}
	}
	

	/**  
	 *  
	 * @Description：手术知情同意书打印
	 * @Author：wanxing
	 * @CreateDate：2016-3-15  下午4:14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportToZQTYS")
	public void iReportToZQTYS() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String mid= request.getParameter("mid");//jasper文件所用到的参数 mid
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("mid", mid);
			parameterMap.put("hospitalName", HisParameters.PREFIXFILENAME);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_ZQTYS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_ZQTYS", "报表打印_手术知情同意书", "2", "0"), e);
		}
	}
	/**
	 * @Description：执行单
	 * @Author：tuchuanjiang
	 * @CreateDate：2016-3-17
	 * @throws Exception
	 */
	@Action(value = "iReportZHIXINGDAN")
	public void iReportZHIXINGDAN() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String tid= request.getParameter("tid");//jasper文件所用到的参数 mainId
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap parameterMap=new HashMap();
			parameterMap.put("TID", tid);
			parameterMap.put("HOSPITALNAME", HisParameters.PREFIXFILENAME);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_ZXD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_ZXD", "报表打印_执行单", "2", "0"), e);
		}
	}
	
	/**
	 * @Description：长期医嘱执行单打印 
	 * @Author：yeguanqun
	 * @CreateDate：2016-3-14  下午5:14 
	 * @throws Exception
	 */
	@Action(value = "iReportToMedicalRecordOrder")
	public void iReportToMedicalRecordOrder() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//药品jasper文件名称 不含后缀
			String inpatientNo= request.getParameter("inpatientNo");//jasper文件所用到的参数 
			Date date = new Date();
			String now=DateUtils.formatDateY_M_D_H_M_S(date);//时间范围的结束时间，功能完成后从页面传
			String onow=DateUtils.formatDateY_M_D_H_M_S(DateUtils.addDay(date, -1));//时间范围的开始时间，功能完成后从页面传
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			Calendar c=Calendar.getInstance();
			String yearNow = String.valueOf(c.get(Calendar.YEAR));
			String monthNow = String.valueOf(c.get(Calendar.MONTH)+1);
			String dayNow = String.valueOf(c.get(Calendar.DATE));
			HashMap parameterMap=new HashMap();
			parameterMap.put("inpatientNo", inpatientNo);
			parameterMap.put("now", now);
			parameterMap.put("onow", onow);
			parameterMap.put("hospitalName", HisParameters.PREFIXFILENAME);
			parameterMap.put("yearNow", yearNow);
			parameterMap.put("monthNow", monthNow);
			parameterMap.put("dayNow", dayNow);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_CQYZZXD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_CQYZZXD", "报表打印_长期医嘱执行单", "2", "0"), e);
		}
	}
	/**
	 * @Description：临时医嘱执行单打印 
	 * @Author：yeguanqun
	 * @CreateDate：2016-3-14  下午5:14 
	 * @throws Exception
	 */
	@Action(value = "iReportToMedicalRecordOrders")
	public void iReportToMedicalRecordOrders() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//药品jasper文件名称 不含后缀
			String inpatientNo= request.getParameter("inpatientNo");//jasper文件所用到的参数 
			Date date = new Date();
			String now=DateUtils.formatDateY_M_D_H_M_S(date);//时间范围的结束时间，功能完成后从页面传
			String onow=DateUtils.formatDateY_M_D_H_M_S(DateUtils.addDay(date, -1));//时间范围的开始时间，功能完成后从页面传
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			Calendar c=Calendar.getInstance();
			String yearNow = String.valueOf(c.get(Calendar.YEAR));
			String monthNow = String.valueOf(c.get(Calendar.MONTH)+1);
			String dayNow = String.valueOf(c.get(Calendar.DATE));
			HashMap parameterMap=new HashMap();
			parameterMap.put("inpatientNo", inpatientNo);
			parameterMap.put("now", now);
			parameterMap.put("onow", onow);
			parameterMap.put("hospitalName", HisParameters.PREFIXFILENAME);
			parameterMap.put("yearNow", yearNow);
			parameterMap.put("monthNow", monthNow);
			parameterMap.put("dayNow", dayNow);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_LSYZZXD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_LSYZZXD", "报表打印_临时医嘱执行单", "2", "0"), e);
		}
	}
	/**
	 * @Description：出生证明表
	 * @Author：zhuxiaolu
	 * @CreateDate：2016-3-14
	 * @throws Exception
	 */
	@Action(value = "iReportToCSZMB")
	public void iReportToCSZMB() throws Exception {
		try{
			   //jasper文件名称 不含后缀
			   String fileName = request.getParameter("fileName");
			   String ID= request.getParameter("ID");//jasper文件所用到的参数 RECIPE_NO
			   java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			   String root_path = request.getSession().getServletContext().getRealPath("/");
			   root_path = root_path.replace('\\', '/');
			   String reportFilePath = root_path + webPath +fileName+".jasper";
			   List<BirthCertificate> list=iReportService.queryBirthCertificate(ID);
			   JRDataSource jrd=new JRBeanCollectionDataSource(list);
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   parameters.put("SUBREPORT_DIR", root_path + webPath);
			   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
			  }catch(Exception e){
				  WebUtils.webSendJSON("error");
					logger.error("IREPORT_CSZM", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_CSZM", "报表打印_出生证明", "2", "0"), e);
			  }

	}
	
	/**  
	 *  
	 * @Description：药品盘点单在前台页面有ID写死部分
	 * @Author：gaoliheng
	 * @CreateDate：2016-3-18  下午11:16 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportToPanDianDan")
	public void iReportToPanDianDan() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String id= request.getParameter("id");
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			StringBuffer path=new StringBuffer();
			path.append(request.getSession().getServletContext().getRealPath("/"));
			path.append(webPath);
			System.out.println(path);
			parameterMap.put("SUBREPORT_DIR", path+"pandiandanzi.jasper");
			parameterMap.put("id", id);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_YPPD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_YPPD", "报表打印_药品盘点单", "2", "0"), e);
		}
	}
	
	/***
	 * 预交金收据
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月16日
	 * @version 1.0
	 */
	@Action(value = "iReportForOutpatientPrepaid")
	public void iReportForOutpatientPrepaid(){
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String tid= request.getParameter("tid");//jasper文件所用到的参数 RECIPE_NO
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			tid=tid.replace(" ","");
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("tid", tid);
			parameterMap.put("hospital", HisParameters.PREFIXFILENAME);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_YJJSJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_YJJSJ", "报表打印_预交金收据", "2", "0"), e);
		}
	}


	
	/***
	 * 
	 * @Description:药品封账单 (固定ID)
	 * @author  wanxing
	 * @date 创建时间：2016年3月18日
	 * @version 1.0
	 */
	@Action(value = "iReportToFSTORE")
	public void iReportToFSTORE(){
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String cdid= request.getParameter("cdid");//jasper文件所用到的参数 cdid
			String deptCode= request.getParameter("deptCode");//jasper文件所用到的参数 deptCode
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("cdid", cdid);
			parameterMap.put("deptCode", deptCode);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_YPFZD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_YPFZD", "报表打印_药品封账单", "2", "0"), e);
		}
	}
	

	
	/**
	 * @Description：打印药品出库单(固定ID)
	 * @Author：wanxing
	 * @throws Exception
	 * @createTime 2016-03-17
	 * @ModifyRmk：  zpty
	 * @createTime 2016-05-17
	 */
	@Action(value = "iReportToDrugOutStore")
	public void iReportToDrugOutStore() throws Exception {
		try {
			String fileName = request.getParameter("fileName");
			String outBillCode=request.getParameter("outBillCode");//出库单据号
			User user = (User) SessionUtils.getCurrentUserFromShiroSession();
			String inputPerson = user.getName();
			HashMap<String, Object> map=new HashMap<String, Object>();
			map.put("outBillCode", outBillCode);
			map.put("inputPerson", inputPerson);
			String path = request.getSession().getServletContext().getRealPath("/");
			map.put("SUBREPORT_DIR", path+webPath);
			iReportService.doReport(request, WebUtils.getResponse(), fileName, map);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_YPCKD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_YPCKD", "报表打印_药品出库单", "2", "0"), e);
		}
	}
	
	
	/**  
	 *  
	 * @Description：特殊管理类抗菌药申请
	 * @Author：dutianliang
	 * @CreateDate：2016-3-15  下午19:16 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportToTSGLLKJYSQ")
	public void iReportToTSGLLKJYSQ() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String id= request.getParameter("mainId");
			String name=request.getParameter("name");
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("HIS_NAME", HisParameters.PREFIXFILENAME);
			parameterMap.put("ID", id);
			parameterMap.put("age", java.net.URLDecoder.decode(request.getParameter("age"),"UTF-8"));
			parameterMap.put("name", java.net.URLDecoder.decode(name,"UTF-8"));
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_KJYSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_KJYSQ", "报表打印_抗菌药申请", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：临时医嘱单
	 * @Author：donghe
	 * @CreateDate：2016-3-16  下午11:00
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportToLinshiOrder")
	public void iReportToLinshiOrder() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String inpid= request.getParameter("inpatientNo");//jasper文件所用到的参数 mid
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("tid", inpid);
			String path = request.getSession().getServletContext().getRealPath("/");
			parameterMap.put("SUBREPORT_DIR", path+webPath);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_LSYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_LSYZ", "报表打印_临时医嘱", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：长期医嘱单
	 * @Author：donghe
	 * @CreateDate：2016-3-16  下午11:00
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportToChangqiOrder")
	public void iReportToChangqiOrder() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String inpid= request.getParameter("inpatientNo");//jasper文件所用到的参数 mid
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("tid", inpid);
			String path = request.getSession().getServletContext().getRealPath("/");
			parameterMap.put("SUBREPORT_DIR", path+webPath);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_CQYZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_CQYZ", "报表打印_长期医嘱单", "2", "0"), e);
		}
	}
	
	/**
	 * 出院计算、中途结算
	 * @author  lyy
	 * @createDate： 2016年3月15日 下午2:46:10 
	 * @modifier lyy
	 * @modifyDate：2016年3月15日 下午2:46:10  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "iReportToBalanceRecord")
	public void iReportToBalanceRecord() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String medicalrecordId= request.getParameter("medicalrecordId");//jasper文件所用到的参数 INPATIENT_NO
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("tid", medicalrecordId);
			parameterMap.put("HOSPITALNAME", HisParameters.PREFIXFILENAME);
			String path = request.getSession().getServletContext().getRealPath("/");
			parameterMap.put("SUBREPORT_DIR", path+webPath);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_CYJS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_CYJS", "报表打印_出院结算", "2", "0"), e);
		}
	}
	
	/**
	 * 摆药单
	 * @author  lyy
	 * @createDate： 2016年3月16日 下午7:35:20 
	 * @modifier lyy
	 * @modifyDate：2016年3月16日 下午7:35:20  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "iReportToBYD")
	public void iReportToBYD() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String yaodan= request.getParameter("yaodan");//jasper文件所用到的参数 yaodan
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			parameterMap.put("drugedbill", yaodan);
			String path = request.getSession().getServletContext().getRealPath("/");
			parameterMap.put("SUBREPORT_DIR", path+webPath);
			iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_BYD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_BYD", "报表打印_摆药单", "2", "0"), e);
		}
	}
	
	/**
	 * @Description：打印押金催款单
	 * @Author：zhenglin
	 * @throws Exception
	 * @createTime 2016-03-15
	 */
	@Action(value = "iReportToDeposit")
	public void iReportToDeposit() throws Exception {
		 try{
			   //jasper文件名称 不含后缀
			   String fileName = request.getParameter("fileName");
			   String inpatientNo= request.getParameter("inpatientNo");//jasper文件所用到的参数 RECIPE_NO
			   inpatientNo=inpatientNo.replace(",", "','");
			   String root_path = request.getSession().getServletContext().getRealPath("/");
			   root_path = root_path.replace('\\', '/');
			   String reportFilePath = root_path + webPath +fileName+".jasper";
			   List<DepositReminder> list=iReportService.queryDepositReminder(inpatientNo);
			   JRDataSource jrd=new JRBeanCollectionDataSource(list);
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   parameters.put("SUBREPORT_DIR", root_path + webPath);
			   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
			  }catch(Exception e){
				  WebUtils.webSendJSON("error");
					logger.error("IREPORT_YJCKD", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_YJCKD", "报表打印_押金催款单", "2", "0"), e);
			  }

	}
	
	
	/**
	 * @Description：打印住院发票
	 * @Author：zhenglin
	 * @throws Exception
	 * @createTime 2016-03-15
	 */
	@Action(value = "iReportToPrintInvo")
	public void iReportToPrintInvo() throws Exception {
		try {
			String fileName = request.getParameter("fileName");
			String inpatientNo=request.getParameter("cardNo");
			HashMap<String, Object> map=new HashMap<String, Object>();
			map.put("tid", inpatientNo);
			String path = request.getSession().getServletContext().getRealPath("/");
			map.put("SUBREPORT_DIR", path+webPath);
			iReportService.doReport(request, WebUtils.getResponse(), fileName, map);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("IREPORT_ZYFP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_ZYFP", "报表打印_住院发票", "2", "0"), e);
		}
	}
	
	
	    /**
		 * 麻醉登记
		 * @Description:
		 * @author  hedong
		 * @date 创建时间：2016-03-18
		 * @version 1.0 
		 */
		@Action(value = "iReportToMZDJ")
		public void iReportToMZDJ(){
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String tid= request.getParameter("tid");//jasper文件所用到的参数 tid
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				parameterMap.put("tid", tid);
				parameterMap.put("hName", HisParameters.PREFIXFILENAME);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_MZDJ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_MZDJ", "报表打印_麻醉登记", "2", "0"), e);
			}
		}
		
		/**  
		 * @Description：门诊收费票据打印（套打+横向迭代子表数据,目前版本为初始版本， 很多数据暂无法确定！只为实现套打及横向迭代技术，页面无js）
		 * @Author：hedong
		 * @CreateDate：2016-3-17  上午09:10
		 * @version 1.0
		 */
		@Action(value = "iReportToMZSFPJ")
		public void iReportToMZSFPJ() throws Exception {
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String mzNumber= request.getParameter("mzNumber");//jasper文件所用到的参数 mzNumber
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				parameterMap.put("mzNumber", mzNumber);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_MZSF", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_MZSF", "报表打印_门诊收费", "2", "0"), e);
			}
		}
		
	/**  
	 *  
	 * @Description 病危通知单|病重通知单
	 * @Author gaoliheng
	 * @CreateDate016-3-15  8:57 
	 * @ModifyRmk
	 * @version 1.0
	 *
	 */
	 @Action(value = "iReportToNurseViewPrint")
	public void iReportToNurseViewPrint() throws Exception {
		 try{
			   //jasper文件名称 不含后缀
			   String fileName = request.getParameter("fileName");
			   String inpatientNo= request.getParameter("inpatientNo");//jasper文件所用到的参数 RECIPE_NO
			   java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			   String root_path = request.getSession().getServletContext().getRealPath("/");
			   root_path = root_path.replace('\\', '/');
			   String reportFilePath = root_path + webPath +fileName+".jasper";
			   List<CriticalNotice> list=iReportService.queryCriticalNotice(inpatientNo);
			   JRDataSource jrd=new JRBeanCollectionDataSource(list);
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   parameters.put("SUBREPORT_DIR", root_path + webPath);
			   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
			  }catch(Exception e){
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_BWTZ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_BWTZ", "报表打印_病危通知单", "2", "0"), e);
			  }

	}
	 
	 
	 /**  
	  *  
	  * @Description 麻醉登记打印
	  * @Author wanxing
	  * @CreateDate 2016-03-21 16:02	
	  * @ModifyRmk
	  * @version 1.0
	  *
	  */
	 @Action(value = "iReportToAnesthesiaRegistration")
	 public void iReportToAnesthesiaRegistration () throws Exception {
		 try {
			 String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			 String clinicCode= StringUtils.isBlank(request.getParameter("clinicCode"))?"":request.getParameter("clinicCode");//jasper文件所用到的参数 clinicCode 住院流水号
			 String beginTime= StringUtils.isBlank(request.getParameter("beginTime"))?"":request.getParameter("beginTime");//jasper文件所用到的参数 beginTime 开始时间
			 String endTime= StringUtils.isBlank(request.getParameter("endTime"))?"":request.getParameter("endTime");//jasper文件所用到的参数 endTime 结束时间
			 String opType= StringUtils.isBlank(request.getParameter("opType"))?"":request.getParameter("opType");//jasper文件所用到的参数 opType 手术类型
 			 String opName= StringUtils.isBlank(request.getParameter("opName"))?"":request.getParameter("opName");//jasper文件所用到的参数 opName 手术项目
			 //设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			 HashMap parameterMap=new HashMap();
			 parameterMap.put("clinicCode", clinicCode);
			 parameterMap.put("beginTime", beginTime);
			 parameterMap.put("endTime", endTime);
			 parameterMap.put("opType", opType);
			 parameterMap.put("opName", opName);
			 String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
			 iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
		 } catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_MZDJ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_MZDJ", "报表打印_麻醉登记", "2", "0"), e);
		 }
	 }
	 

		/**
		 * @Description：药品退库单
		 * @Author：zhuxiaolu
		 * @CreateDate：2016-3-14
		 * @throws Exception
		 */
		@Action(value = "iReportToYPTKD")
		public void iReportToYPTKD() throws Exception {
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String ID= request.getParameter("ID");//jasper文件所用到的参数
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
					HashMap parameterMap=new HashMap();
					parameterMap.put("ID", ID);
					String path = request.getSession().getServletContext().getRealPath("/");
					parameterMap.put("SUBREPORT_DIR", path+webPath);
					iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_YPTK", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_YPTK", "报表打印_药品退库单", "2", "0"), e);
			}
		}
		
		/**  
		 *  
		 * @Description：药品调价单(没有条件传入,直接打印全部数据)
		 * @Author：zpty
		 * @CreateDate：2016-3-16  下午2:14 
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		@Action(value = "iReportToYPTJD")
		public void iReportToYPTJD() throws Exception {
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String ids = request.getParameter("ids");//asper文件所用到的参数 ids
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）			
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				parameterMap.put("hospital", HisParameters.PREFIXFILENAME);
				parameterMap.put("idAll", ids);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_YPTJD", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_YPTJD", "报表打印_药品调价单", "2", "0"), e);
			}
		}
	
		/**
		 * @Description：住院证打印打印
		 * @Author：zhangebi
		 * @CreateDate：2016-3-16下午 
		 * @throws Exception
		 */
		@Action(value = "iReportInpatientProof")
		public void iReportInpatientProof() throws Exception {
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String ids= request.getParameter("CERTIFICATES_NO");//jasper文件所用到的参数 RECIPE_NO
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				HashMap parameterMap=new HashMap();
				parameterMap.put("ids", ids);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_ZYZ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_ZYZ", "报表打印_住院证", "2", "0"), e);
			}
		}
		
		/**
		 * @Description:打印配药标签
		 * @Author: dutianliang
		 * @CreateDate:2016年3月23日
		 * @Version: V 1.0
		 */
		@Action(value = "iReportPYBQ")
		public void iReportPYBQ()throws Exception {
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String id= request.getParameter("id");//jasper文件所用到的参数 RECIPE_NO
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）			
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				parameterMap.put("ID", id);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_PYBQ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_PYBQ", "报表打印_配药标签", "2", "0"), e);
			}
		}
		/**
		 * @Description：门诊收费清单（打印wanxing制作的报表） 
		 * @Author：zhangjin
		 * @CreateDate：2016-3-16 
		 * @throws Exception
		 */
		@Action(value = "iReportMedicinelist")
		public void iReportMedicinelist() throws Exception {
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String INVOICE_NO= request.getParameter("INVOICE_NO");//jasper文件所用到的参数 shouldPay
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				INVOICE_NO=java.net.URLDecoder.decode(request.getParameter("INVOICE_NO"),"UTF-8");
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				parameterMap.put("INVOICE_NO", "('"+INVOICE_NO+"')");
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				String sendDrugWin = iReportService.getSendDrugWin(INVOICE_NO);
				if("null".equals(sendDrugWin)){
					sendDrugWin=null;
				}
				parameterMap.put("WIN", sendDrugWin);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_MZSF", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_MZSF", "报表打印_门诊收费清单", "2", "0"), e);
			}
		}

		/**
		 * @Description:打印处方单
		 * @Author: dutianliang
		 * @CreateDate:2016年3月23日
		 * @Version: V 1.0
		 */
		@Action(value = "iReportCFD")
		public void iReportCFD()throws Exception {
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String id= request.getParameter("ID");
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				parameterMap.put("HIS_NAME", HisParameters.PREFIXFILENAME);
				parameterMap.put("ID", id);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_CFD", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_CFD", "报表打印_处方单", "2", "0"), e);
			}
		}
		
		/**
		 * @Description:收费发票
		 * @Author: ldl
		 * @CreateDate:2016年5月4日
		 * @Version: V 1.0
		 */
		@Action(value = "iReportInvoiceMedicinelist")
		public void iReportInvoiceMedicinelist()throws Exception {
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String id= request.getParameter("INID");
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				parameterMap.put("INID", id);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_SFFP", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_SFFP", "报表打印_收费发票", "2", "0"), e);
			}
		}
		
		/**
		 * @Description:收费发票
		 * @Author: ldl
		 * @CreateDate:2016年5月4日
		 * @Version: V 1.0
		 */
		@Action(value = "iReportToNurseView")
		public void iReportToNurseView()throws Exception {
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String id= request.getParameter("id2");
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				parameterMap.put("id2", id);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_SFFP", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_SFFP", "报表打印_收费发票", "2", "0"), e);
			}
		}
		
		
		/**
		 * 计算年龄，不满一年返回月，不满一月返回天
		 * @Description：计算年龄
		 * @Author：aizhonghua
		 * @CreateDate：2016年4月19日 上午10:09:44 
		 * @Modifier：
		 * @ModifyDate：
		 * @ModifyRmk：  
		 * @version： 1.0
		 * @param birthday
		 * @return：年龄
		 *
		 */
		public static String calAge(Date birthday) {
			try {
				int yearOfAge = 0;
				int monthOfAge = 0;
				int dayOfAge = 0;
				Calendar cal = Calendar.getInstance();
				long nowMillis = cal.getTimeInMillis();
				long birthdayMillis = birthday.getTime();
				if (nowMillis < birthdayMillis) {
					return null;
				}
				
				int yearNow = cal.get(Calendar.YEAR);
				int monthNow = cal.get(Calendar.MONTH) + 1;
				int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

				cal.setTime(birthday);
				int yearBirth = cal.get(Calendar.YEAR);
				int monthBirth = cal.get(Calendar.MONTH) + 1;
				int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

				if (yearNow == yearBirth) {
					monthOfAge = monthNow - monthBirth;
					if (monthNow == monthBirth) {
						dayOfAge = dayOfMonthNow - dayOfMonthBirth;
					} else {
						if (dayOfMonthNow < dayOfMonthBirth) {
							monthOfAge--;
						}
						if (monthOfAge == 0) {
							dayOfAge = (int) TimeUnit.MILLISECONDS.toDays(nowMillis - birthdayMillis);
						}
					}
				} else {
					yearOfAge = yearNow - yearBirth;
					if (monthNow < monthBirth) {
						yearOfAge--;
						if (yearOfAge == 0) {
							monthOfAge = 12 - monthBirth + monthNow;
							if (dayOfMonthNow >= dayOfMonthBirth) {
								monthOfAge++;
							}
						}
					} else if (monthNow == monthBirth) {
						if (dayOfMonthNow < dayOfMonthBirth) {
							yearOfAge--;
							if (yearOfAge == 0) {
								monthOfAge = 11;
							}
						}
					}
				}
				if (yearOfAge > 0) {
					return yearOfAge + "岁";
				} else if (monthOfAge > 0) {
					return monthOfAge + "月";
				} else if (dayOfAge > 0) {
					return dayOfAge + "天";
				}
				return null;
			} catch (Exception e) {
				return null;
			}
		}
		/**
		 * 摆药单打印
		 * @author  lyy
		 * @createDate： 2016年5月13日 下午4:57:25 
		 * @modifier lyy
		 * @modifyDate：2016年5月13日 下午4:57:25
		 * @param：    
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value = "iReportInvoiceBill")
		public void iReportInvoiceBill()throws Exception {
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String drugedbill=request.getParameter("drugedbill");    //摆药单号
				String inpatientNo= request.getParameter("inpatientNo");    //住院流水号
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				parameterMap.put("tid",inpatientNo);
				parameterMap.put("drugedbill",drugedbill);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_BYD", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_BYD", "报表打印_摆药单", "2", "0"), e);
			}
		}
		
		/***
		 * 门诊配药工作量统计报表视图0
		 * @Title: iReportDosageView0 
		 * @author  WFJ
		 * @createDate ：2016年6月24日
		 * @return void
		 * @version 1.0
		 */
		@Action(value = "iReportDosageView0")
		public void iReportDosageView0(){
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String deptid= request.getParameter("did");    //住院流水号
				String beginDate= request.getParameter("beginDate");    
				String endDate= request.getParameter("endDate");    
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				parameterMap.put("hName", HisParameters.PREFIXFILENAME);
				parameterMap.put("did",deptid);
				parameterMap.put("beginDate",beginDate);
				parameterMap.put("endDate",endDate);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_PYGZL", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_PYGZL", "报表打印_配药工作量", "2", "0"), e);
			}
		}
		/**  
		 *  
		 * @Description：门诊检查单
		 * @Author：zpty
		 * @CreateDate：2016-6-27  下午1:14 
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		
		@Action(value = "iReportQueInsto")
		public void iReportQueInsto() throws Exception {
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String STime= request.getParameter("STime");//jasper文件所用到的参数 STime
				String ETime= request.getParameter("ETime");//jasper文件所用到的参数 ETime
				String drug= request.getParameter("drug");//jasper文件所用到的参数 drug
				String company= request.getParameter("company");//jasper文件所用到的参数 company
				String companyName= request.getParameter("companyName");//jasper文件所用到的参数 companyName
				companyName=java.net.URLDecoder.decode(companyName, "UTF-8");
				String user= request.getParameter("user");//jasper文件所用到的参数 user
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath+"YPRKCXD_SUB.jasper");
				if("".equals(STime)){
					parameterMap.put("STime", null);
				}else{
					parameterMap.put("STime", STime);
				}
				if("".equals(ETime)){
					parameterMap.put("ETime", null);
				}else{
					parameterMap.put("ETime", ETime);
				}
				if("".equals(drug)){
					parameterMap.put("drug", null);
				}else{
					parameterMap.put("drug", drug);
				}
				if("".equals(company)){
					parameterMap.put("company", null);
				}else{
					parameterMap.put("company", company);
				}
				if("".equals(companyName)){
					parameterMap.put("companyName", null);
				}else{
					parameterMap.put("companyName", companyName);
				}
				if("".equals(user)){
					parameterMap.put("user", null);
				}else{
					parameterMap.put("user", user);
				}
				parameterMap.put("hospital", HisParameters.PREFIXFILENAME);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_MZJCD", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_MZJCD", "报表打印_门诊检查单", "2", "0"), e);
			}
		}
		 /**  
		  *  
		  * @Description 项目变更
		  * @Author donghe
		  * @CreateDate 2016-06-24
		  * @ModifyRmk
		  * @version 1.0
		  *
		  */
		 @Action(value = "iReportInformationaction")
		 public void iReportInformationaction () throws Exception {
			 try {
				 String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				 String itemName= StringUtils.isBlank(request.getParameter("itemName"))?"":request.getParameter("itemName");//jasper文件所用到的参数
				 itemName=java.net.URLDecoder.decode(itemName, "UTF-8");
				 String beginDate= StringUtils.isBlank(request.getParameter("beginDate"))?"":request.getParameter("beginDate");//jasper文件所用到的参数
				 String endDate= StringUtils.isBlank(request.getParameter("endDate"))?"":request.getParameter("endDate");//jasper文件所用到的参数
				 String neglect= StringUtils.isBlank(request.getParameter("neglect"))?"":request.getParameter("neglect");//jasper文件所用到的参数
				 //设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				 HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				 parameterMap.put("itemName", itemName);
				 parameterMap.put("beginDate", beginDate);
				 parameterMap.put("endDate", endDate);
				 parameterMap.put("neglect", neglect);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_XMBG", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_XMBG", "报表打印_项目变更", "2", "0"), e);
			}
		}
		/**
		 * @Description 病人费用统计
		 * @author  lyy
		 * @createDate： 2016年6月25日 下午7:24:04 
		 * @modifier lyy
		 * @modifyDate：2016年6月25日 下午7:24:04
		 * @param：    
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value="iReportCostStatistice")
		public void iReportCostStatistice(){
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String firstTime= StringUtils.isBlank(request.getParameter("firstTime"))?"":request.getParameter("firstTime");//jasper文件所用到的参数 firstTime 开始时间
				String endTime= StringUtils.isBlank(request.getParameter("endTime"))?"":request.getParameter("endTime");//jasper文件所用到的参数 endTime 结束时间
				String inpatientNo= StringUtils.isBlank(request.getParameter("inpatientNo"))?"":request.getParameter("inpatientNo");//jasper文件所用到的参数 inpatientNo 住院流水号
				SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
				String deptName=dept.getDeptName();
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				if(StringUtils.isNotBlank(firstTime)){
					parameterMap.put("firstTime", firstTime);
				}
				if(StringUtils.isNotBlank(endTime)){
					parameterMap.put("endTime", endTime);
				}
				if(StringUtils.isNotBlank(inpatientNo)){
					parameterMap.put("inpatientNo", inpatientNo);
				}
				parameterMap.put("deptName", deptName);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_BRFY", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_BRFY", "报表打印_病人费用", "2", "0"), e);
			}
		}
		 /**  
		  *  
		  * @Description 手术耗材统计
		  * @Author zhangjin
		  * @CreateDate 2016-06-27
		  * @ModifyRmk
		  * @version 1.0
		  *
		  */
		 @Action(value = "iReportOperactionRecord")
		 public void iReportOperactionRecord () throws Exception {
			 try {
				 String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				 String repnoe= StringUtils.isBlank(request.getParameter("repno"))?"":request.getParameter("repno");//jasper文件所用到的参数 clinicCode 国家编码
				 String loginTime= StringUtils.isBlank(request.getParameter("loginTime"))?"":request.getParameter("loginTime");//jasper文件所用到的参数 beginTime 开始时间
				 String endTime= StringUtils.isBlank(request.getParameter("endTime"))?"":request.getParameter("endTime");//jasper文件所用到的参数 endTime 结束时间
				 String prices= StringUtils.isBlank(request.getParameter("price"))?"0.0":request.getParameter("price");//jasper文件所用到的参数 价格
				 String deptName="";
				 SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
				 if(dept!=null&&dept.getDeptName()!=null){
					 deptName=dept.getDeptName();
				 }else{
					  deptName="全部";
				 }
				 String repno="%"+repnoe;
				 Double price= Double.valueOf(prices);
				 //设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				 HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				 parameterMap.put("loginTime", loginTime);
				 parameterMap.put("repno", repno);
				 parameterMap.put("endTime", endTime);
				 parameterMap.put("price", price);
				 parameterMap.put("deptName", deptName);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				//hedong 20170407 异常信息输出至日志文件
				logger.error("SSGL_SSHCTJ", e);
				//hedong 20170407 异常信息保存至mongodb
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSGL_SSHCTJ", "手术管理_手术耗材统计", "2", "0"), e);
			}
		}
		 /**  
		  *  
		  * @Description 手术耗材明细统计
		  * @Author zhangjin
		  * @CreateDate 2016-11-24
		  * @ModifyRmk
		  * @version 1.0
		  *
		  */
		 @Action(value = "iReportOperDetailsRecord")
		 public void iReportOperDetailsRecord () throws Exception {
			 try {
				 String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				 String repnoe= StringUtils.isBlank(request.getParameter("repno"))?"":request.getParameter("repno");//jasper文件所用到的参数 clinicCode 国家编码
				 String loginTime= StringUtils.isBlank(request.getParameter("loginTime"))?"":request.getParameter("loginTime");//jasper文件所用到的参数 beginTime 开始时间
				 String endTime= StringUtils.isBlank(request.getParameter("endTime"))?"":request.getParameter("endTime");//jasper文件所用到的参数 endTime 结束时间
				 String prices= StringUtils.isBlank(request.getParameter("price"))?"0.0":request.getParameter("price");//jasper文件所用到的参数 价格
				 String deptName="";
				 SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
				 if(dept!=null&&dept.getDeptName()!=null){
					 deptName=dept.getDeptName();
				 }else{
					  deptName="全部";
				 }
				 String repno="%"+repnoe;
				 Double price= Double.valueOf(prices);
				 //设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				 HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				 parameterMap.put("loginTime", loginTime);
				 parameterMap.put("repno", repno);
				 parameterMap.put("endTime", endTime);
				 parameterMap.put("price", price);
				 parameterMap.put("deptName", deptName);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				//hedong 20170407 异常信息输出至日志文件
				logger.error("SSGL_SSHCTJMX", e);
				//hedong 20170407 异常信息保存至mongodb
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSGL_SSHCTJMX", "手术管理_手术耗材统计明细", "2", "0"), e);
			}
		}
		 /**  
		  *  
		  * @Description 长期医嘱查询
		  * @Author donghe
		  * @CreateDate 2016-06-27
		  * @ModifyRmk
		  * @version 1.0
		  *
		  */
		 @Action(value = "iReportInquiry")
		 public void iReportInquiry () throws Exception {
			 try {
				 String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				 String queryText1= request.getParameter("queryText1");//jasper文件所用到的参数 
				 queryText1=java.net.URLDecoder.decode(queryText1, "UTF-8");
				 String typeName= request.getParameter("typeName");//jasper文件所用到的参数
				 String queryState= request.getParameter("queryState");//jasper文件所用到的参数
				 String c= request.getParameter("c");//jasper文件所用到的参数
				 String inpatientNo= request.getParameter("inpatientNo");//jasper文件所用到的参数
				 String deptID= request.getParameter("deptID");//jasper文件所用到的参数
				 if("undefined".equals(c)){
						c=null;
				 }
				 //设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				 HashMap parameterMap=new HashMap();
				 parameterMap.put("queryText1", queryText1);
				 parameterMap.put("typeName", typeName);
				 parameterMap.put("queryState", queryState);
				 parameterMap.put("c", c);
				 parameterMap.put("deptId", deptID);
				 parameterMap.put("inpatientNo", inpatientNo);
				 parameterMap.put("hospital", HisParameters.PREFIXFILENAME);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_CQYZCX", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_CQYZCX", "报表打印_长期医嘱查询", "2", "0"), e);
			}
		}

		 
		/***
		 * 门诊药房退费统计
		 * @Title: iReportDosageView0 
		 * @author  WFJ
		 * @createDate ：2016年6月24日
		 * @return void
		 * @version 1.0
		 */
		@Action(value = "iReportPhaRefund")
		public void iReportPhaRefund(){
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String deptid= request.getParameter("tid");    //住院流水号
				String beginDate = request.getParameter("beginDate");    //日期
				String endDate = request.getParameter("endDate");    
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				parameterMap.put("tid",deptid);
				parameterMap.put("beginDate",beginDate);
				parameterMap.put("endDate",endDate);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_MZYFTF", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_MZYFTF", "报表打印_门诊药房退费", "2", "0"), e);
			}
		}

		 /**  
		  *  
		  * @Description 临时医嘱查询
		  * @Author donghe
		  * @CreateDate 2016-06-27
		  * @ModifyRmk
		  * @version 1.0
		  *
		  */
		 @Action(value = "iReportInquiryLin")
		 public void iReportInquiryLin () throws Exception {
			 try {
				 String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				 String inpatientNo= StringUtils.isBlank(request.getParameter("inpatientNo"))?"":request.getParameter("inpatientNo");//jasper文件所用到的参数
				 String queryText1= StringUtils.isBlank(request.getParameter("queryText1"))?"":request.getParameter("queryText1");//jasper文件所用到的参数 
				 queryText1=java.net.URLDecoder.decode(queryText1, "UTF-8");
				 String typeName= StringUtils.isBlank(request.getParameter("typeName"))?"":request.getParameter("typeName");//jasper文件所用到的参数
				 String queryState= StringUtils.isBlank(request.getParameter("queryState"))?"":request.getParameter("queryState");//jasper文件所用到的参数
				 String c= StringUtils.isBlank(request.getParameter("c"))?"":request.getParameter("c");//jasper文件所用到的参数
				 String deptID= StringUtils.isBlank(request.getParameter("deptID"))?"":request.getParameter("deptID");//jasper文件所用到的参数
				 if("undefined".equals(c)){
						c=null;
				 }
				 //设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				 HashMap parameterMap=new HashMap();
				 parameterMap.put("queryText1", queryText1);
				 parameterMap.put("typeName", typeName);
				 parameterMap.put("queryState", queryState);
				 parameterMap.put("inpatientNo", inpatientNo);
				 parameterMap.put("c", c);
				 parameterMap.put("deptId", deptID);
				 parameterMap.put("hospital", HisParameters.PREFIXFILENAME);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_LSYZCX", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_LSYZCX", "报表打印_临时医嘱查询", "2", "0"), e);
			}
		}
		 /**
			 * @Description：摆药汇总
			 * @Author：yeguanqun
			 * @CreateDate：2016-6-27  下午5:14 
			 * @throws Exception
			 */
			@Action(value = "iReportToDispensSum")
			public void iReportToDispensSum() throws Exception {
				try {
					String fileName = request.getParameter("fileName");//药品jasper文件名称 不含后缀
					String inpatientNo= request.getParameter("inpatientNo");//jasper文件所用到的参数 
					HashMap<String,Object> parameterMap=new HashMap<String,Object>();
					parameterMap.put("inpatientNo", inpatientNo);
					parameterMap.put("hospitalName",HisParameters.PREFIXFILENAME);
					StringBuffer path=new StringBuffer();
					path.append(request.getSession().getServletContext().getRealPath("/"));
					path.append(webPath);
					parameterMap.put("SUBREPORT_DIR", path+"WBYCFD_SUB.jasper");
					iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
				} catch (Exception e) {
					WebUtils.webSendJSON("error");
					logger.error("IREPORT_BYHZ", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_BYHZ", "报表打印_摆药汇总", "2", "0"), e);
				}
			}
			 /**
			 * @Description：摆药汇总
			 * @Author：yeguanqun
			 * @CreateDate：2016-6-27  下午5:14 
			 * @throws Exception
			 */
			@Action(value = "iReportToDispensSum1")
			public void iReportToDispensSum1() throws Exception {
				try {
					String fileName = request.getParameter("fileName");//药品jasper文件名称 不含后缀
					String inpatientNo= request.getParameter("inpatientNo");//jasper文件所用到的参数 
					HashMap<String,Object> parameterMap=new HashMap<String,Object>();
					parameterMap.put("inpatientNo", inpatientNo);
					parameterMap.put("hospitalName",HisParameters.PREFIXFILENAME);
					StringBuffer path=new StringBuffer();
					path.append(request.getSession().getServletContext().getRealPath("/"));
					path.append(webPath);
					parameterMap.put("SUBREPORT_DIR", path+"YBYCFD_SUB.jasper");
					iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
				} catch (Exception e) {
					WebUtils.webSendJSON("error");
					logger.error("IREPORT_BYHZ", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_BYHZ", "报表打印_摆药汇总", "2", "0"), e);
				}
			}
			/**
			 * @Description：摆药明细
			 * @Author：yeguanqun
			 * @CreateDate：2016-6-29  下午5:14 
			 * @throws Exception
			 */
			@Action(value = "iReportToDispensDetail")
			public void iReportToDispensDetail() throws Exception {
				try {
					String fileName = request.getParameter("fileName");//药品jasper文件名称 不含后缀
					String inpatientNo= request.getParameter("inpatientNo");//jasper文件所用到的参数 
					HashMap<String,Object> parameterMap=new HashMap<String,Object>();
					parameterMap.put("inpatientNo", inpatientNo);
					parameterMap.put("hospitalName", HisParameters.PREFIXFILENAME);
					StringBuffer path=new StringBuffer();
					path.append(request.getSession().getServletContext().getRealPath("/"));
					path.append(webPath);
					parameterMap.put("SUBREPORT_DIR", path+"WBYMXD_SUB.jasper");
					iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
				} catch (Exception e) {
					WebUtils.webSendJSON("error");
					logger.error("IREPORT_BYMX", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_BYMX", "报表打印_摆药明细", "2", "0"), e);
				}
			}
			 /**
			 * @Description：收款员缴款单
			 * @Author：tuchuanjiang
			 * @CreateDate：2016-6-27
			 * @throws Exception
			 */
			@Action(value = "iReportUDbalance")
			public void iReportUDbalance() throws Exception {
				try {
					String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
					String TIME= request.getParameter("searchTimeUDBalance");//jasper文件所用到的参数 time
					String TIMEA=TIME+" 00:00:00";
					String TIMEB=TIME+" 23:59:59";
					//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
					HashMap<String,Object> parameterMap=new HashMap<String,Object>();
					parameterMap.put("TIME", TIME);
					parameterMap.put("TIMEA", TIMEA);
					parameterMap.put("TIMEB", TIMEB);
					String path = request.getSession().getServletContext().getRealPath("/");
					parameterMap.put("SUBREPORT_DIR", path+webPath);
					iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
				} catch (Exception e) {
					WebUtils.webSendJSON("error");
					logger.error("IREPORT_SKYJKD", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_SKYJKD", "报表打印_收款员缴款单", "2", "0"), e);
				}
			}
			/**
			 * @Description：医嘱执行单查询   --药品
			 * @Author：donghe
			 * @CreateDate：2016-6-27  下午2:14 
			 * @throws Exception
			 */
			@Action(value = "iReportImplementationDrug")
			public void iReportImplementationDrug() throws Exception {
				try {
					String fileName = request.getParameter("fileName");//药品jasper文件名称 不含后缀
					String inpatientNo= request.getParameter("inpatientNo");//jasper文件所用到的参数 
					String billName= request.getParameter("billName");//jasper文件所用到的参数
					billName=java.net.URLDecoder.decode(billName, "UTF-8");
					String billNo= request.getParameter("billNo");//jasper文件所用到的参数 
					String endDate= request.getParameter("endDate");//jasper文件所用到的参数 
					String beginDate= request.getParameter("beginDate");//jasper文件所用到的参数 
					String drugedFlag= request.getParameter("b");//jasper文件所用到的参数 
					String validFlag= request.getParameter("c");//jasper文件所用到的参数 
					if("undefined".equals(drugedFlag)){
						drugedFlag=null;
					}
					if("undefined".equals(validFlag)){
						validFlag=null;
					}
					HashMap parameterMap=new HashMap();
					parameterMap.put("inpatientNo", inpatientNo);
					parameterMap.put("billNo", billNo);
					parameterMap.put("billName", billName);
					parameterMap.put("endDate", endDate);
					parameterMap.put("beginDate", beginDate);
					parameterMap.put("drugedFlag", drugedFlag);
					parameterMap.put("validFlag", validFlag);
					parameterMap.put("hospitalName", HisParameters.PREFIXFILENAME);
					iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
				} catch (Exception e) {
					WebUtils.webSendJSON("error");
					logger.error("IREPORT_YZZXD", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_YZZXD", "报表打印_医嘱执行单", "2", "0"), e);
				}
			}
			/**
			 * @Description：医嘱执行单查询   --非药品
			 * @Author：donghe
			 * @CreateDate：2016-6-27  下午2:14 
			 * @throws Exception
			 */
			@Action(value = "iReportImplementationUnDrug")
			public void iReportImplementationUnDrug() throws Exception {
				try {
					String fileName = request.getParameter("fileName");//药品jasper文件名称 不含后缀
					String inpatientNo= request.getParameter("inpatientNo");//jasper文件所用到的参数 
					String billName= request.getParameter("billName");//jasper文件所用到的参数
					billName=java.net.URLDecoder.decode(billName, "UTF-8");
					String billNo= request.getParameter("billNo");//jasper文件所用到的参数 
					String endDate= request.getParameter("endDate");//jasper文件所用到的参数 
					String beginDate= request.getParameter("beginDate");//jasper文件所用到的参数 
					String drugedFlag= request.getParameter("b");//jasper文件所用到的参数 
					String validFlag= request.getParameter("c");//jasper文件所用到的参数 
					HashMap parameterMap=new HashMap();
					parameterMap.put("inpatientNo", inpatientNo);
					parameterMap.put("billNo", billNo);
					parameterMap.put("billName", billName);
					parameterMap.put("endDate", endDate);
					parameterMap.put("beginDate", beginDate);
					parameterMap.put("drugedFlag", drugedFlag);
					parameterMap.put("validFlag", validFlag);
					parameterMap.put("hospitalName", HisParameters.PREFIXFILENAME);
					iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
				} catch (Exception e) {
					WebUtils.webSendJSON("error");
					logger.error("IREPORT_YZZXD", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_YZZXD", "报表打印_医嘱执行单", "2", "0"), e);
				}
			}
		/**
		 * @Description 病人停诊统计
		 * @author  lyy
		 * @createDate： 2016年7月2日 下午6:39:50 
		 * @modifier lyy
		 * @modifyDate：2016年7月2日 下午6:39:50
		 * @param：    
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value="iReportMZStop")
		public void iReportMZStop(){
			try {
				String fileName = request.getParameter("fileName");//药品jasper文件名称 不含后缀
				String firstTime=request.getParameter("firstTime");   //jasper文件所用到的firstTime参数  
				String endTime=request.getParameter("endTime");   //jasper文件所用到的endTime参数
				HashMap parameterMap=new HashMap();
				parameterMap.put("firstTime", firstTime);
				parameterMap.put("endTime", endTime);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request, WebUtils.getResponse(), fileName, parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_BRTZ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_BRTZ", "报表打印_病人停诊", "2", "0"), e);
			}
		}
		/**
		 * @Description 住院部取药统计
		 * @author  lyy
		 * @createDate： 2016年7月2日 下午7:22:34 
		 * @modifier lyy
		 * @modifyDate：2016年7月2日 下午7:22:34
		 * @param：    
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value="iReportPharmacy")
		public void iReportPharmacy(){
			try {
				String fileName = request.getParameter("fileName");//药品jasper文件名称 不含后缀
				String firstTime=request.getParameter("firstTime");   //jasper文件所用到的firstTime参数  
				String endTime=request.getParameter("endTime");   //jasper文件所用到的endTime参数
				String drugstore=request.getParameter("drugstore");   //jasper文件所用到的endTime参数
				String deptName=null;
				if(StringUtils.isNotBlank(drugstore)){
					SysDepartment department=iReportService.queryDept(drugstore);
					deptName=department.getDeptName();
				}
				HashMap parameterMap=new HashMap();
				parameterMap.put("firstTime", firstTime);
				parameterMap.put("endTime", endTime);
				parameterMap.put("drugstore", drugstore);
				parameterMap.put("deptName", deptName);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request, WebUtils.getResponse(), fileName, parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_ZYBQY", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_ZYBQY", "报表打印_住院部取药", "2", "0"), e);
			}
		}
		/**
		 * 
		 * @Description  住院发药工作量统计
		 * @author  lyy
		 * @createDate： 2016年7月4日 下午2:00:39 
		 * @modifier lyy
		 * @modifyDate：2016年7月4日 下午2:00:39
		 * @param：    
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value="iReportDoseCensus")
		public void iReportDoseCensus(){
			try {
				String fileName = request.getParameter("fileName");//药品jasper文件名称 不含后缀
				String firstTime=request.getParameter("firstTime");   //jasper文件所用到的firstTime参数  
				String endTime=request.getParameter("endTime");   //jasper文件所用到的endTime参数
				String drugstore=request.getParameter("drugstore");   //jasper文件所用到的endTime参数
				String deptName=null;
				if(StringUtils.isNotBlank(drugstore)){
					SysDepartment department=iReportService.queryDept(drugstore);
					deptName=department.getDeptName();
				}
				HashMap parameterMap=new HashMap();
				parameterMap.put("firstTime", firstTime);
				parameterMap.put("endTime", endTime);
				parameterMap.put("drugstore", drugstore);
				parameterMap.put("deptName", deptName);
				String path = request.getSession().getServletContext().getRealPath("/");
				parameterMap.put("SUBREPORT_DIR", path+webPath);
				iReportService.doReport(request, WebUtils.getResponse(), fileName, parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_FYGZL", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_FYGZL", "报表打印_发药工作量", "2", "0"), e);
			}
		}
		
		
		/**  
		 *  
		 * @Description：住院手术费汇总查询
		 * @Author：tangfeishuai
		 * @CreateDate：2016-6-29  下午1:14 
		 * @Modifier：tangfeishuai
		 * @ModifyDate：2016-6-29  下午1:14 
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		
		@Action(value = "iReportInOperationCost")
		public void iReportInOperationCost() throws Exception {
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String beginTime= request.getParameter("beginTime");
				String endTime= request.getParameter("endTime");
				String inpatientNo= request.getParameter("inpatientNo");
				String execDept= request.getParameter("execDept");
				
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				request.getSession().getServletContext().getRealPath("/");
				StringBuffer path=new StringBuffer();
				path.append(request.getSession().getServletContext().getRealPath("/"));
				path.append(webPath);
				parameterMap.put("cId", path+"ZYSSFHZCXC.jasper");
				parameterMap.put("beginTime", beginTime);
				parameterMap.put("endTime", endTime);
				parameterMap.put("inpatientNo", inpatientNo);
				parameterMap.put("execDept", execDept);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_ZYSSF", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_ZYSSF", "报表打印_住院手术费", "2", "0"), e);
			}
		}
		
		/**
		 * @Description：手术登记表 
		 * @Author：zhangjin
		 * @CreateDate：2016-3-16 
		 * @throws Exception
		 */
		@Action(value = "iReportsstzb")
		public void iReportsstzb() throws Exception {
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String opId= request.getParameter("opId");//jasper文件所用到的参数 shouldPay
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				parameterMap.put("opId", opId);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				WebUtils.webSendJSON("error");
				logger.error("IREPORT_SSDJB", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_SSDJB", "报表打印_手术登记表", "2", "0"), e);
			}
		}
		/**  
		 *  
		 * @Description：手术计费信息汇总
		 * @Author：zhangjin
		 * @CreateDate：2017-2-10   
		 * @Modifier：
		 * @ModifyDate： 
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		
		@Action(value = "iReportOperationMillingInfo")
		public void iReportOperationMillingInfo() throws Exception {
			try {
				String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
				String beginTime= request.getParameter("beginTime");
				String endTime= request.getParameter("endTime");
				String opStatus= request.getParameter("opStatus");
				String execDept= request.getParameter("execDept");
				String feeBegTime= request.getParameter("feeBegTime");
				String feeEndTime= request.getParameter("feeEndTime");
				String feeStates= request.getParameter("feeStates");
				String inState= request.getParameter("inState");
				String opDoctor= request.getParameter("opDoctor");
				String opDoctordept= request.getParameter("opDoctordept");
				String identityCard= request.getParameter("identityCard");
				
				String endTime2="";
				String feeEndTime2="";
				String execDepts=execDept.replace(",","','");
				String opDoctors=opDoctor.replace(",","','");
				String opDoctordepts=opDoctordept.replace(",","','");
				StringBuffer sb=new StringBuffer();
				if(StringUtils.isNotBlank(feeEndTime)){
					feeEndTime2=DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(feeEndTime), 1));
				}
				if(StringUtils.isNotBlank(endTime)){
					endTime2=DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(endTime), 1));
				}
				sb.append("select t.opId,t.patientNo,t.name,t.preDate,t.opDoctor,t.opDoctorId,");
				sb.append( "DECODE(t.opStatus,'1','手术申请','2','手术审批','3','手术安排','4','手术完成','5','手术取消','未知') as opStatus,");
						sb.append( "t.opDoctordeptId,t.opDoctordept,t.feeDate, ");
				sb.append("   t.execDept,");
				sb.append( " DECODE(t.inState,'R','住院登记','I','病房接诊','B','出院登记','O','出院结算','P','预约出院' ， 'N','无费出院','未知') as inState,");
						sb.append( "DECODE(t.feeState, '1', '未计费', '2', '存在计费') as feeState,t.diagName,t.feeDate ");
				sb.append("  from  ( ");
				sb.append(" select distinct app.op_id as opId,app.op_doctordept as opDoctordeptId,app.patient_no as patientNo,app.name as name,app.pre_date as preDate, ");
				sb.append(" app.op_doctor as opDoctorId,app.status as opStatus,td.dept_name as opDoctorDept, e.employee_name as opDoctor,info.in_state as inState,decode(fee.operation_id,null,'1','2') as feeState,");
				sb.append(" fee.fee_date as feeDate,fee.execute_deptcode as execDept, ");
				sb.append(" (select listagg(DIAG_NAME, ',') within group(order by null) as diagName from T_OPERATION_DIAGNOSE op where op.OPERATION_ID=app.op_id) as diagName ");
				sb.append(" from t_operation_apply app ");
				sb.append("  left join t_department td on td.dept_code=app.op_doctordept  left join t_employee e on e.employee_jobno=app.op_doctor ");
				sb.append("  left join t_inpatient_info info on info.inpatient_no=app.clinic_code ");
				sb.append("  left join t_inpatient_itemlist fee on fee.operation_id=app.op_id and fee.inpatient_no=app.clinic_code ");
				if(StringUtils.isNotBlank(feeBegTime)){
					sb.append(" and fee.fee_date >= to_date('"+feeBegTime+"','yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(feeEndTime)){
					sb.append(" and fee.fee_date < to_date('"+feeEndTime2+"','yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(execDept)){
					sb.append(" and fee.execute_deptcode in ('"+execDepts+"')");
				}
				if(StringUtils.isNotBlank(identityCard)){
					sb.append( " inner join  t_patient p on p.MEDICALRECORD_ID=app.PATIENT_NO  and p.PATIENT_CERTIFICATESNO=:certificatesNo ");
				}
				sb.append(" where app.stop_flg=0  ");
				if(StringUtils.isNotBlank(beginTime)){
					sb.append(" and app.pre_date >= to_date('"+beginTime+"','yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(endTime)){
					sb.append(" and app.pre_date< to_date('"+endTime2+"','yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(opDoctor)){
					sb.append(" and app.op_doctor in ('"+opDoctors+"')");
				}
				if(StringUtils.isNotBlank(opDoctordept)){
					sb.append(" and app.op_doctordept in ('"+opDoctordepts+"') ");
				}
				if(StringUtils.isNotBlank(inState)){
					sb.append(" and  info.in_state = '"+inState+"'");
				}
				if(StringUtils.isNotBlank(opStatus)){
					sb.append(" and  app.status = '"+opStatus+"' ");
				}
				sb.append(" union all ");
				sb.append(" select distinct app2.op_id as opId,app2.op_doctordept as opDoctordeptId,app2.patient_no as patientNo,app2.name as name,app2.pre_date as preDate, ");
				sb.append(" app2.op_doctor as opDoctorId,app2.status as opStatus,td2.dept_name as opDoctorDept, e2.employee_name as opDoctor,info2.in_state as inState,decode(fee2.operation_id,null,'1','2') as feeState,");
				sb.append(" fee2.fee_date as feeDate,fee2.execute_deptcode as execDept, ");
				sb.append(" (select listagg(DIAG_NAME, ',') within group(order by null) as diagName from T_OPERATION_DIAGNOSE op where op.OPERATION_ID=app2.op_id) as diagName ");
				sb.append(" from t_operation_apply app2 ");
				sb.append("  left join t_department td2 on td2.dept_code=app2.op_doctordept  left join t_employee e2 on e2.employee_jobno=app2.op_doctor ");
				sb.append("  left join t_inpatient_info_now info2 on info2.inpatient_no=app2.clinic_code ");
				sb.append("  left join t_inpatient_itemlist_now fee2 on fee2.operation_id=app2.op_id and fee2.inpatient_no=app2.clinic_code ");
				if(StringUtils.isNotBlank(feeBegTime)){
					sb.append(" and fee2.fee_date >=to_date('"+feeBegTime+"','yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(feeEndTime)){
					sb.append(" and fee2.fee_date < to_date('"+feeEndTime2+"','yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(execDepts)){
					sb.append(" and fee2.execute_deptcode in ('"+execDepts+"')");
				}
				if(StringUtils.isNotBlank(identityCard)){
					sb.append( " inner join  t_patient p on p.MEDICALRECORD_ID=app2.PATIENT_NO  and p.PATIENT_CERTIFICATESNO=:certificatesNo ");
				}
				sb.append(" where app2.stop_flg=0  ");
				if(StringUtils.isNotBlank(beginTime)){
					sb.append(" and app2.pre_date >=to_date('"+beginTime+"','yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(endTime)){
					sb.append(" and app2.pre_date < to_date('"+endTime2+"','yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(opDoctor)){
					sb.append(" and app2.op_doctor in ('"+opDoctors+"') ");
				}
				if(StringUtils.isNotBlank(opDoctordept)){
					sb.append(" and app2.op_doctordept in ('"+opDoctordepts+"' )");
				}
				if(StringUtils.isNotBlank(inState)){
					sb.append(" and  info2.in_state = '"+inState+"'");
				}
				if(StringUtils.isNotBlank(opStatus)){
					sb.append(" and  app2.status = '"+opStatus+"' ");
				}
				if(StringUtils.isNotBlank(execDept)){
					sb.append(" and fee2.execute_deptcode in ('"+execDepts+"') ");
				}
				sb.append(" ) t where 1=1 ");
				if(StringUtils.isNotBlank(feeBegTime)){
					sb.append(" and t.feeDate >= to_date('"+feeBegTime+"','yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(feeEndTime)){
					sb.append(" and t.feeDate< to_date('"+feeEndTime2+"','yyyy-MM-dd')");
				}
				String sql=sb.toString();
				//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				request.getSession().getServletContext().getRealPath("/");
				StringBuffer path=new StringBuffer();
				path.append(request.getSession().getServletContext().getRealPath("/"));
				path.append(webPath);
				parameterMap.put("cId", path+"SSJFXXHZC.jasper");
				parameterMap.put("beginTime", beginTime);
				parameterMap.put("endTime", endTime);
				parameterMap.put("opStatus", opStatus);
				parameterMap.put("execDept",execDepts);
				parameterMap.put("feeBegTime", feeBegTime);
				parameterMap.put("feeEndTime", feeEndTime);
				parameterMap.put("feeStates", feeStates);
				parameterMap.put("inState", inState);
				parameterMap.put("opDoctor", opDoctors);
				parameterMap.put("opDoctordept", opDoctordepts);
				if(StringUtils.isNotBlank(identityCard)){
					parameterMap.put("certificatesNo",identityCard);
				}
				parameterMap.put("sql",sql);
				iReportService.doReport(request,WebUtils.getResponse(),fileName,parameterMap);
			} catch (Exception e) {
				//hedong 20170407 异常信息保存至mongodb
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSTJ_SSJFXXHZ", "手术统计_手术计费信息汇总", "2", "0"), e);
			}
		}
		/**  
		 * @Description：以javaBean作为报表数据源测试用例
		 * @Author：hedong
		 * @CreateDate：2017-2-17   
		 * @Modifier：
		 * @ModifyDate： 
		 * @ModifyRmk：  
		 * @version 1.0
		 */
		@Action(value = "iReportJavaBeanDemo")
		public void iReportJavaBeanDemo() throws Exception {
			 try
			  {
			   String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀	 
			   HttpServletResponse response = WebUtils.getResponse();
			   String root_path = request.getSession().getServletContext().getRealPath("/");
			   root_path = root_path.replace('\\', '/');
			   String reportFilePath = root_path + webPath+fileName+".jasper";
			   //javaBean数据封装（注：数据源可参考该示例各自进行创建）
			   JRDataSource dataSource = this.createDataSourceToDemo();
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   parameters.put("SUBREPORT_DIR", root_path + webPath);
			   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,dataSource);
			  }catch(Exception e)
			  {
					WebUtils.webSendJSON("error");
					logger.error("IREPORT_CSYL", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("IREPORT_CSYL", "报表打印_测试用例", "2", "0"), e);
			  }
		}
		/**  
		 * @Description：javaBean作为报表数据源的数据封装
		 * @Author：hedong
		 * @CreateDate：2017-2-17   
		 * @Modifier：
		 * @ModifyDate： 
		 * @ModifyRmk：  
		 * @version 1.0
		 */
		private JRDataSource createDataSourceToDemo() {
			ArrayList<IReportJavaBeanDemo> demos = new ArrayList<IReportJavaBeanDemo>();
			IReportJavaBeanDemo demo = new IReportJavaBeanDemo();
			//父表数据封装
			demo.setTestField1("主表动态字段1");
			demo.setTestField2("主表动态字段2");
			//子表数据封装
			ArrayList<User> users = new ArrayList<User>();
			User user1 = new User();
			user1.setName("赵晨");
			user1.setNickName("小赵");
			user1.setPhone("18911205774");
			users.add(user1);
			
			User user2 = new User();
			user2.setName("陈武");
			user2.setNickName("小陈");
			user2.setPhone("17711205745");
			users.add(user2);
			
			User user3 = new User();
			user3.setName("孙芳");
			user3.setNickName("小孙");
			user3.setPhone("18011205312");
			users.add(user3);
			
			
			demo.setUsers(users);
			
			demos.add(demo);
			
			return new JRBeanCollectionDataSource(demos);
		}
		
	//病案查询打印
	@Action("recordPrint")
	public void recordPrint(){
		String fileName = request.getParameter("fileName");//EmrRecordMain
		String deptCode=request.getParameter("deoptCode");
		String recCode=request.getParameter("recCode");
		String patientName=request.getParameter("patientName");
		String sex=request.getParameter("sex");
		String brs=request.getParameter("ageStart");
		String bre=request.getParameter("ageEnd");
		Integer ageStart=null;
		Integer ageEnd=null;
		if(StringUtils.isNotBlank(brs)){
			 ageStart=Integer.parseInt(brs);
		}
		if(StringUtils.isNotBlank(bre)){
			 ageEnd=Integer.parseInt(bre);
		}
		String outDateS=request.getParameter("outDateS");
		String outDateE=request.getParameter("outDateE");
		String digNose=request.getParameter("digNose");
		String birthStart=request.getParameter("birthStart");
		String birthEnd=request.getParameter("birthEnd");
		
		List<EmrRecordMain> list = iReportService.recordPrint(deptCode, recCode, patientName, sex, ageStart, ageEnd, outDateS, outDateE, digNose, birthStart, birthEnd);
		//性别渲染1.男2.女3.未知
		if(list!=null){
			for(EmrRecordMain e :list){
				if("1".equals(e.getPatientSex())){
					e.setPatientSex("男");
				}else if("2".equals(e.getPatientSex())){
					e.setPatientSex("女");
				}else if("3".equals(e.getPatientSex())){
					e.setPatientSex("未知");
				}
			}
		}
		String root_path=request.getSession().getServletContext().getRealPath("/");
		root_path=root_path.replace("\\", "/");
		String reportFilePath = root_path + webPath +fileName+".jasper";
		//javaBean数据封装
		ArrayList<EmrRecordMainReport> EmrRecordMainReportList = new ArrayList<EmrRecordMainReport>();
		EmrRecordMainReport emrRecordMainReport = new EmrRecordMainReport();
		//设置维护的子报表集合
		emrRecordMainReport.setEmrRecordMainList(list);
		//添加到集合中
		EmrRecordMainReportList.add(emrRecordMainReport);
		//添加的数据源
		JRDataSource jrd = new JRBeanCollectionDataSource(EmrRecordMainReportList);
		//给父报表传递德的参数
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String printTime= dateFormat.format(new Date());
		parameters.put("tableTitle", "病案查询表");
		parameters.put("printTime", printTime);
		parameters.put("SUBREPORT_DIR", root_path + webPath);
		try {
			iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SSTJ_BACX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSTJ_BACX", "手术统计_病案查询", "2", "0"), e);
		}
		
	}
		
		
}
