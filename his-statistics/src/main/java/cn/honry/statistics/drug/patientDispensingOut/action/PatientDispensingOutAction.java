package cn.honry.statistics.drug.patientDispensingOut.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.baseinfo.hospital.service.HospitalInInterService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.drug.patientDispensing.vo.VinpatientApplyout;
import cn.honry.statistics.drug.patientDispensingOut.service.PatientDispensingOutService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/***
 * 病区患者摆药查询
 * 
 * @Description:
 * @author: donghe
 * @CreateDate: 2016年6月23日
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/PatientDispensingOut")
public class PatientDispensingOutAction extends ActionSupport {
	private Logger logger = Logger.getLogger(PatientDispensingOutAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;

	public void setHiasExceptionService(
			HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String menuAlias;// 栏目别名,在主界面中点击栏目时传到action的参数

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	private HttpServletRequest request;

	@Qualifier(value = "inpatientInfoInInterService")
	private InpatientInfoInInterService inpatientInfoInInterService;

	public void setInpatientInfoInInterService(
			InpatientInfoInInterService inpatientInfoInInterService) {
		this.inpatientInfoInInterService = inpatientInfoInInterService;
	}

	/**
	 * 当前页数，用于分页查询
	 */
	private String page;

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

	/**
	 * 现在时间
	 */
	private String now;

	public String getNow() {
		return now;
	}

	public void setNow(String now) {
		this.now = now;
	}

	/**
	 * 注入patinentInnerService公共接口
	 */
	private PatinentInnerService patinentInnerService;

	@Autowired
	@Qualifier(value = "patinentInnerService")
	public PatinentInnerService getPatinentInnerService() {
		return patinentInnerService;
	}

	/**
	 * @see注入报表借口口
	 */
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;

	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}

	/***
	 * 注入员工Service
	 */
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;

	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}

	/** 获取医院名称 **/
	@Autowired
	@Qualifier(value = "hospitalInInterService")
	private HospitalInInterService hospitalInInterService;

	public void setHospitalInInterService(
			HospitalInInterService hospitalInInterService) {
		this.hospitalInInterService = hospitalInInterService;
	}

	/***
	 * 注入科室Service
	 */
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;

	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}

	/**
	 * 开始时间
	 */
	private String stime;
	/**
	 * 结束时间
	 */
	private String etime;

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;

	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}

	@Autowired
	@Qualifier(value = "patientDispensingOutService")
	private PatientDispensingOutService patientDispensingOutService;

	public void setPatientDispensingServiceOut(
			PatientDispensingOutService patientDispensingOutService) {
		this.patientDispensingOutService = patientDispensingOutService;
	}

	/**
	 * 住院流水号
	 */
	private String inpatientNo;
	/**
	 * 药品名称
	 */
	private String tradeName;

	public String getInpatientNo() {
		return inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	/**
	 * 在院和出院标记
	 */
	private String flag;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	/***
	 * 
	 * @Description:病区出院患者摆药查询 页面跳转
	 * @author: donghe
	 * @CreateDate: 2016年6月23日
	 * @version 1.0
	 */
	@SuppressWarnings("deprecation")
	@Action(value = "listPatientDispensingOut", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/patientDispensing/patientDispensingOut.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listPatientDispensingOut() {
		// 获取时间
		Date date = new Date();
		etime = DateUtils.formatDateY_M_D(date);
		stime = DateUtils.formatDateYM(date) + "-01";
		ServletActionContext.getRequest().setAttribute("stime", stime);
		ServletActionContext.getRequest().setAttribute("etime", etime);
		return "list";
	}

	/**
	 * @Description： 查询患者树
	 * @throws IOException
	 * @Author：zhenglin
	 */
	@Action(value = "InfoTree")
	public void InfoTree() {
		try {
			SysDepartment dept=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			String json =null;
			if(dept!=null){
				List<TreeJson> patientTree = patientDispensingOutService
						.queryPatientTree(dept.getDeptCode(), flag);
				json= JSONUtils.toJson(patientTree);
			}else{
				Map<String,String> map=new HashMap<String,String>();
				map.put("resCode", "error");
				map.put("resMsg", "请选择登录科室");
				json=JSONUtils.toJson(map);
			}
			WebUtils.webSendJSON(json);
		} catch (Exception ex) {
			logger.error("ZYTJ_BQHZBYCX", ex);
			hiasExceptionService.saveExceptionInfoToMongo(
					new RecordToHIASException("ZYTJ_BQHZBYCX", "住院统计_病区患者摆药查询",
							"2", "0"), ex);
		}
	}

	/***
	 * 
	 * @Description:病区出院患者摆药查询
	 * @author: donghe
	 * @CreateDate: 2016年6月22日
	 * @version 1.0
	 */
	@Action(value = "queryVinpatientApplyoutlist")
	public void queryVinpatientApplyoutlist() {
		try {
			if (StringUtils.isNotBlank(tradeName)) {
				tradeName = tradeName.trim();
			}
			Map<String, Object> retMap = new HashMap<String, Object>();
			String deptId = ShiroSessionUtils
					.getCurrentUserLoginDepartmentFromShiroSession()
					.getDeptCode();
			String type = ShiroSessionUtils
					.getCurrentUserLoginDepartmentFromShiroSession()
					.getDeptType();
			List<VinpatientApplyout> list = patientDispensingOutService
					.queryVinpatientApplyout(deptId, type, page, rows,
							tradeName, inpatientNo, etime, stime, flag, "");
			int total = patientDispensingOutService
					.qqueryVinpatientApplyoutTotal(deptId, type, tradeName,
							inpatientNo, etime, stime, flag);
			retMap.put("total", total);
			retMap.put("rows", list);
			String json = JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		} catch (Exception ex) {
			logger.error("ZYTJ_BQHZBYCX", ex);
			hiasExceptionService.saveExceptionInfoToMongo(
					new RecordToHIASException("ZYTJ_BQHZBYCX", "住院统计_病区患者摆药查询",
							"2", "0"), ex);
		}
	}

	/***
	 * 
	 * @Description:文档打印
	 * @author: wangshujuan
	 * @CreateDate: 2017年7月4日
	 * @version 1.0
	 */
	@SuppressWarnings("deprecation")
	@Action(value = "queryVinpatientApplyoutlistPDf")
	public void queryVinpatientApplyoutlistPDF() {
		try {
			String deptId = ShiroSessionUtils
					.getCurrentUserLoginDepartmentFromShiroSession()
					.getDeptCode();
			String type = ShiroSessionUtils
					.getCurrentUserLoginDepartmentFromShiroSession()
					.getDeptType();
			ArrayList<VinpatientApplyout> vinpatientApplyoutList = new ArrayList<VinpatientApplyout>();
			VinpatientApplyout vinpatientApplyout = new VinpatientApplyout();
			vinpatientApplyout
					.setVinpatientApplyout(patientDispensingOutService
							.queryVinpatientApplyout(deptId, type, page, rows,
									java.net.URLDecoder.decode(tradeName),
									inpatientNo, etime, stime, flag, "ALL"));
			vinpatientApplyout.setPatientName(vinpatientApplyout
					.getVinpatientApplyout().get(0).getPatientName());
			// 渲染人
			Map<String, String> empMap = employeeInInterService
					.queryEmpCodeAndNameMap();
			// 渲染部门
			Map<String, String> deptMap = deptInInterService
					.querydeptCodeAndNameMap();
			// 渲染单位
			List<BusinessDictionary> cstl = innerCodeService
					.getDictionary("nonmedicineencoding");
			Map<String, String> map = new HashMap<String, String>();
			for (BusinessDictionary cst : cstl) {
				map.put(cst.getEncode(), cst.getName());
			}
			for (VinpatientApplyout vo : vinpatientApplyout
					.getVinpatientApplyout()) {
				// 渲染部门
				if (deptMap.get(vo.getDrugDeptCode()) != null) {
					vo.setDrugDeptCode(deptMap.get(vo.getDrugDeptCode()));
				}
				// 渲染发药人
				if (empMap.get(vo.getPrintEmpl()) != null) {
					vo.setPrintEmpl(empMap.get(vo.getPrintEmpl()));
				}
				// 渲染发送人
				if (empMap.get(vo.getApplyOpercode()) != null) {
					vo.setApplyOpercode(empMap.get(vo.getApplyOpercode()));
				}
				// 渲染单位
				if (map.get(vo.getDoseUnit()) != null) {
					vo.setDoseUnit(map.get(vo.getDoseUnit()));
				}

			}
			empMap = null;
			deptMap = null;
			request = ServletActionContext.getRequest();
			String root_path = request.getSession().getServletContext()
					.getRealPath("/");
			String reportFilePath = root_path
					+ "WEB-INF/reportFormat/jasper/VinpatientApplyout.jasper";
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", root_path
					+ "WEB-INF/reportFormat/jasper/");
			parameters.put("hospitalName", hospitalInInterService
					.getPresentHospital().getName());
			vinpatientApplyoutList.add(vinpatientApplyout);
			iReportService.doReportToJavaBean(request, WebUtils.getResponse(),
					reportFilePath, parameters, new JRBeanCollectionDataSource(
							vinpatientApplyoutList));
		} catch (Exception ex) {
			logger.error("ZYTJ_BQHZBYCX", ex);
			hiasExceptionService.saveExceptionInfoToMongo(
					new RecordToHIASException("ZYTJ_BQHZBYCX", "住院统计_病区患者摆药查询",
							"2", "0"), ex);
		}
	}

	/***
	 * 
	 * @Description:就诊卡号查询患者
	 * @author: donghe
	 * @CreateDate: 2016年12月09日
	 * @version 1.0
	 */
	@Action(value = "querypatient")
	public void querypatient() {
		try {
			String medicalrecordId = patinentInnerService
					.getMedicalrecordId(inpatientNo);
			List<InpatientInfoNow> list = patientDispensingOutService.querypatient(
					flag, medicalrecordId);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception ex) {
			logger.error("ZYTJ_BQHZBYCX", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_BQHZBYCX", "住院统计_病区患者摆药查询", "2", "0"), ex);
		}
	}

	/***
	 * 
	 * @Description:人员渲染
	 * @author: wangshujuan
	 * @CreateDate: 2017年7月4日
	 * @version 1.0
	 */
	@Action(value = "queryEmpMapPublic")
	public void queryEmpMapPublic() {
		Map<String, String> map = employeeInInterService
				.queryEmpCodeAndNameMap();
		String joString = JSONUtils.toJson(map);
		WebUtils.webSendJSON(joString);
	}

	/***
	 * 
	 * @Description:科室渲染
	 * @author: wangshujuan
	 * @CreateDate: 2017年7月4日
	 * @version 1.0
	 */
	@Action(value = "queryDeptMapPublic")
	public void queryDeptMapPublic() {
		Map<String, String> map = deptInInterService.querydeptCodeAndNameMap();
		String joString = JSONUtils.toJson(map);
		WebUtils.webSendJSON(joString);
	}

	/***
	 * 
	 * @Description:单位渲染
	 * @author: wangshujuan
	 * @CreateDate: 2017年7月4日
	 * @version 1.0
	 */
	@Action(value = "queryUnitMap")
	public void queryUnitMap() {
		try {
			Map<String, String> map = patientDispensingOutService.queryUnitMap();
			String joString = JSONUtils.toJson(map);
			WebUtils.webSendJSON(joString);
		} catch (Exception ex) {
			logger.error("ZYTJ_BQHZBYCX", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_BQHZBYCX", "住院统计_病区患者摆药查询", "2", "0"), ex);
		}
	}
}
