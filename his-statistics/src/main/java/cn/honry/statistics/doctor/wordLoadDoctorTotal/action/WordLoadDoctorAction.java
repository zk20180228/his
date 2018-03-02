package cn.honry.statistics.doctor.wordLoadDoctorTotal.action;

import java.util.Date;






import java.util.HashMap;
import java.util.List;






import java.util.Map;

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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.HospitalWork;
import cn.honry.statistics.bi.bistac.monthlyDashboard.service.MonthlyDashboardService;
import cn.honry.statistics.bi.bistac.totalDrugUsed.service.TotalDrugUsedService;
import cn.honry.statistics.bi.bistac.totalUnDrugUsed.service.TotalUnDrugUsedService;
import cn.honry.statistics.deptstat.internalCompare2.service.InternalCompare2Service;
import cn.honry.statistics.doctor.wordLoadDoctorTotal.service.WordLoadService;
import cn.honry.statistics.doctor.wordLoadDoctorTotal.vo.WordLoadVO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**  
 *  
 * @Description：  门诊工作量统计
 * @Author：zpty
 * @CreateDate：2015-8-26 
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/WordLoadDoctorGZLTJ")
public class WordLoadDoctorAction extends ActionSupport  {
	private static final long serialVersionUID = 2220385689324609224L;
	
	private Logger logger=Logger.getLogger(WordLoadDoctorAction.class);
	/**错误日志存储**/
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier("wordLoadService")
	private WordLoadService wordLoadService;
	@Autowired
	@Qualifier("totalUnDrugUsedService")
	private  TotalUnDrugUsedService totalUnDrugUsedService;
	@Autowired
	@Qualifier(value="totalDrugUsedService")
	private TotalDrugUsedService totalDrugUsedService;
	@Autowired
	@Qualifier("monthlyDashboardService")
	private MonthlyDashboardService monthlyDashboardService;
	@Autowired
	@Qualifier(value="internalCompare2Service")
	private InternalCompare2Service internalCompare2Service;
	private String eTime;
	private String sTime;
	private String depts;
	private String expxrt;
	private String date;
	private String dateSign;
	private String begin;
	private String end;
	private String doctors;
	private String menuAlias;
	private String page;
	private String rows;
/********************************************************************************************************/
	@Action(value = "queryWordLoadDoctorTotal")
	public void queryWordLoadDoctorTotal(){
		try {
			List<WordLoadVO> list=wordLoadService.queryInhosWordTotal(sTime, eTime, depts, expxrt);
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYTJFX_ZYYSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJFX_ZYYSGZLTJ", "住院统计分析_住院医生工作量统计", "2", "0"), e);
		}
	}
	/**
	 * 
	 * 
	 * <p> 数据展示(旧)</p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月13日 下午2:51:35 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月13日 下午2:51:35 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 *
	 */
//	@Action(value = "listWordLoadDoctorTotal", results = { @Result(name = "list", location = "/WEB-INF/pages/register/wordLoadDoctorTotal/wordLoadDoctorTotal.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
//	public String listRgisterInfoGzltj()  {
//		Date date = new Date();
//		eTime = DateUtils.formatDateY_M_D(date);
//		sTime = DateUtils.formatDateYM(date)+"-01";
//		return "list";
//	}
	/**
	 * 
	 * 
	 * <p>数据展示（新） </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月13日 下午2:51:53 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月13日 下午2:51:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 *
	 */
	@Action(value = "listWordLoadDoctorTotal", results = { @Result(name = "list", location = "/WEB-INF/pages/register/wordLoadDoctorTotal/wordLoadDoctorList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listRgisterInfoGzltj()  {
		try {
			Date date = new Date();
			eTime = DateUtils.formatDateY_M_D(date);
			sTime = DateUtils.formatDateYM(date)+"-01";
		} catch (Exception e) {
			logger.error("ZYTJFX_ZYYSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJFX_ZYYSGZLTJ", "住院统计分析_住院医生工作量统计", "2", "0"), e);
		}
		return "list";
	}
	/**
	 * 
	 * 
	 * <p>同环比 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月13日 下午2:50:45 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月13日 下午2:50:45 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "queryTotalInpaitent")
	public void queryTotalInpaitent() {
		try {
			List<WordLoadVO> list1=wordLoadService.queryForDBSame(date, dateSign);//同比
			List<WordLoadVO> list2=wordLoadService.queryForDBSque(date, dateSign);//环比
			Map<String,List<WordLoadVO>> map=new HashMap<String,List<WordLoadVO>>();
			map.put("same", list1);
			map.put("sque", list2);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYTJFX_ZYYSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJFX_ZYYSGZLTJ", "住院统计分析_住院医生工作量统计", "2", "0"), e);
		}
	}
	/**
	 * 
	 * 
	 * <p>top医生科室排名 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月13日 下午2:50:28 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月13日 下午2:50:28 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "queryTop")
	public void queryTop(){
		try {
			List<WordLoadVO> listDept=wordLoadService.queryTop(date, dateSign, "dept");//科室Top
			List<WordLoadVO> listDoc=wordLoadService.queryTop(date, dateSign, "doc");//科室Top
			Map<String,List<WordLoadVO>> map=new HashMap<String,List<WordLoadVO>>();
			map.put("dept", listDept);
			map.put("doc", listDoc);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYTJFX_ZYYSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJFX_ZYYSGZLTJ", "住院统计分析_住院医生工作量统计", "2", "0"), e);
		}
	}
	/**
	 * 
	 * 
	 * <p>数据展示 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月13日 下午2:50:17 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月13日 下午2:50:17 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "queryWeekInpaitent")
	public void queryWeekInpaitent(){
		try {
			Map<String,Object> map=wordLoadService.queryForDBList(begin, end,depts,doctors,menuAlias, rows, page);//科室医生
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYTJFX_ZYYSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJFX_ZYYSGZLTJ", "住院统计分析_住院医生工作量统计", "2", "0"), e);
		}
	}
	/**
	 * 
	 * 
	 * <p>获取医生 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月11日 下午8:32:58 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月11日 下午8:32:58 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "getDoctorList")
	public void getDoctorList(){
		try {
			String deptTypes = ServletActionContext.getRequest().getParameter("deptTypes");
			List<cn.honry.inner.vo.MenuListVO> list = wordLoadService.getDoctorList(deptTypes,menuAlias);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYTJFX_ZYYSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJFX_ZYYSGZLTJ", "住院统计分析_住院医生工作量统计", "2", "0"), e);
		}
	}
	/**
	 * 
	 * 
	 * <p>住院医生工作量查询（新） </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月13日 下午2:32:16 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月13日 下午2:32:16 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "queryDoctors")
	public void queryDoctors(){
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			if(new MongoBasicDao().isCollection("ZYYSGZLTJ_EFFECT_DAY")){
				map=wordLoadService.queryHosWorkMap(begin, end, menuAlias, depts, doctors, page, rows);
			}else{
				List<HospitalWork> list=wordLoadService.queryHosWorkTotal(begin, end, menuAlias, depts, doctors, page, rows);
				int total=wordLoadService.queryHosWorkTotal(begin, end, menuAlias, depts, doctors);//总条数
				map.put("total", total);
				map.put("rows", list);
			}
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYTJFX_ZYYSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJFX_ZYYSGZLTJ", "住院统计分析_住院医生工作量统计", "2", "0"), e);
		}
	}
/****************************************************************************************************/
	public String geteTime() {
		return eTime;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDateSign() {
		return dateSign;
	}
	public void setDateSign(String dateSign) {
		this.dateSign = dateSign;
	}
	public void setWordLoadService(WordLoadService wordLoadService) {
		this.wordLoadService = wordLoadService;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
	}
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}
	public String getExpxrt() {
		return expxrt;
	}
	public void setExpxrt(String expxrt) {
		this.expxrt = expxrt;
	}
	public String getBegin() {
		return begin;
	}
	public void setBegin(String begin) {
		this.begin = begin;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getDepts() {
		return depts;
	}
	public void setDepts(String depts) {
		this.depts = depts;
	}
	public String getDoctors() {
		return doctors;
	}
	public void setDoctors(String doctors) {
		this.doctors = doctors;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
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
	
	
}
