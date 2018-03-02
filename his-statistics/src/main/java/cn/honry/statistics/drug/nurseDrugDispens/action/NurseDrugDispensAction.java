package cn.honry.statistics.drug.nurseDrugDispens.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.drug.nurseDrugDispens.service.NurseDrugDispensService;
import cn.honry.statistics.drug.nurseDrugDispens.vo.DrugDispensDetailVo;
import cn.honry.statistics.drug.nurseDrugDispens.vo.DrugDispensSumVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/NurseDrugDispens")
@SuppressWarnings({ "all" })
public class NurseDrugDispensAction extends ActionSupport {
	private Logger logger=Logger.getLogger(NurseDrugDispensAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	@Autowired
	@Qualifier(value = "nurseDrugDispensService")
	private NurseDrugDispensService nurseDrugDispensService;

	public void setNurseDrugDispensService(
			NurseDrugDispensService nurseDrugDispensService) {
		this.nurseDrugDispensService = nurseDrugDispensService;
	}


	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;

	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}

	/** 报表打印接口 **/
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;

	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}

	private String menuAlias;// 栏目别名,在主界面中点击栏目时传到action的参数

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	HttpServletRequest request;
	/**
	 * 住院患者信息实体类
	 */
	private InpatientInfoNow inpatientInfo;

	public InpatientInfoNow getInpatientInfo() {
		return inpatientInfo;
	}

	public void setInpatientInfo(InpatientInfoNow inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}

	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 结束时间
	 */
	private String endTime;
	/**
	 * 药品名称|拼音名|五笔码|自定义码
	 */
	private String drugName;
	/**
	 * 住院号查询
	 */
	private String inpatientNoSerc;

	/**
	 * dsfdf:当前登录病区的id
	 */
	private String deptId;

	/**
	 * dsfdf:患者信息树节点Id
	 */
	private String id;

	/**
	 * 判断摆药还是未摆药
	 */
	private String type;

	/**
	 * 患者查询范围标识
	 */
	private String a;
	// 分页
	private String rows;// 每页条数
	private String page;// 页码

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

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
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

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getInpatientNoSerc() {
		return inpatientNoSerc;
	}

	public void setInpatientNoSerc(String inpatientNoSerc) {
		this.inpatientNoSerc = inpatientNoSerc;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @Description:获取list页面
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-22
	 * @return String
	 * @version 1.0
	 **/
	@RequiresPermissions(value = { "STAT-BQBYCX:function:view" })
	@Action(value = "toViewNurseDrugDispens", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/nurseDrugDispens/nurseDrugDispens.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewMedicalFeeDetail(){
		SysDepartment sys = ShiroSessionUtils
				.getCurrentUserLoginNursingStationShiroSession();
		if (sys != null) {
			ServletActionContext.getRequest().setAttribute("deptId",
					sys.getId());// 获取当前病区
		}
		Date date = new Date();
		endTime = DateUtils.formatDateY_M_D(date);
		int y = DateUtils.getYear();
		Integer m = DateUtils.getMonth();
		String month = m.toString();
		if (m < 10) {
			month = "0" + month;
		}
		startTime = y + "-" + month + "-01";
		return "list";
	}

	/**
	 * @Description:查询摆药汇总列表
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-22
	 * @param
	 * @return void
	 * @version 1.0
	 **/
	@Action(value = "queryDrugDispensSum", results = { @Result(name = "json", type = "json") })
	public void queryDrugDispensSum() {
		try{
			if (StringUtils.isBlank(page)) {
				page = "1";
			}
			if (StringUtils.isBlank(rows)) {
				rows = "20";
			}
			List<DrugDispensSumVo> infoList = null;
			if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
				Calendar c = Calendar.getInstance();
				Date date = new Date();
				c.setTime(date);
				c.add(Calendar.YEAR, -1);
				Date y = c.getTime();
				endTime = DateUtils.formatDateY_M_D(date);
				startTime = DateUtils.formatDateY_M_D(y);
			}
			if (StringUtils.isNotBlank(drugName)) {
				drugName = drugName.trim();
			}
			infoList = nurseDrugDispensService.queryDrugDispensSum(inpatientInfo,
					startTime, endTime, drugName, inpatientNoSerc, a, type, page,
					rows);
			int total = nurseDrugDispensService.queryDrugDispensSumTotal(
					inpatientInfo, startTime, endTime, drugName, inpatientNoSerc,
					a, type);
			String json = JSONUtils.toJson(infoList);
			String json2 = "{\"total\":" + total + ",\"rows\":" + json + "}";
			WebUtils.webSendJSON(json2);
		}catch(Exception e){
			logger.error("ZYTJ_BQBYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_BQBYCX", "住院统计_病区摆药查询", "2", "0"), e);
		}
	}

	/**
	 * @Description:打印摆药汇总列表
	 * @Author： cl
	 * @param
	 * @return void
	 * @version 1.0
	 **/
	@Action(value = "printDrugDispensSum")
	public void printDrugDispensSum() {
		try {
			List<DrugDispensSumVo> infoList = new ArrayList<DrugDispensSumVo>(1);
			if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
				Calendar c = Calendar.getInstance();
				Date date = new Date();
				c.setTime(date);
				c.add(Calendar.YEAR, -1);
				Date y = c.getTime();
				endTime = DateUtils.formatDateY_M_D(date);
				startTime = DateUtils.formatDateY_M_D(y);
			}
			if (StringUtils.isNotBlank(drugName)) {
				drugName = drugName.trim();
			}
			DrugDispensSumVo vo = new DrugDispensSumVo();
	
			List<DrugDispensSumVo> list = nurseDrugDispensService
					.queryDrugDispensSum(inpatientInfo, startTime, endTime,
							drugName, inpatientNoSerc, a, type, null, null);
	
			Map<String, String> map2 = nurseDrugDispensService.queryBillNameMap();
			for (int i = 0; i < list.size(); i++) {
				String billclassName = list.get(i).getBillclassName();
				list.get(i).setBillclassName(map2.get(billclassName));
			}
	
			vo.setDrugDispensSumList(list);
			infoList.add(vo);
			// 渲染单位
			Map<String,String> drugpackagingunitMap = innerCodeService.getBusDictionaryMap("packunit");
			for (DrugDispensSumVo vo1 : vo.getDrugDispensSumList()) {
				vo1.setUnit(drugpackagingunitMap.get(vo1.getUnit()));
			}
			request = ServletActionContext.getRequest();
			String root_path = request.getSession().getServletContext()
					.getRealPath("/");
			String reportFilePath = root_path
					+ "WEB-INF/reportFormat/jasper/WBYCFD.jasper";
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			if ("1".equals(type)) {
				parameters.put("name", "已摆药处方单");
			} else {
				parameters.put("name", "未摆药处方单");
			}
			parameters.put("SUBREPORT_DIR", root_path
					+ "WEB-INF/reportFormat/jasper/");
			parameters.put("hospitalName", HisParameters.PREFIXFILENAME);
				iReportService.doReportToJavaBean(request, WebUtils.getResponse(),
						reportFilePath, parameters, new JRBeanCollectionDataSource(
								infoList));
				infoList = null;
				parameters = null;
		} catch (Exception e) {
			logger.error("ZYTJ_BQBYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_BQBYCX", "住院统计_病区摆药查询", "2", "0"), e);
			e.printStackTrace();
		}

	}

	/**
	 * @Description:查询摆药明细列表
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-22
	 * @param
	 * @return void
	 * @version 1.0
	 **/
	@Action(value = "queryDrugDispensDetail")
	public void queryDrugDispensDetail() {
		try{
			if (StringUtils.isBlank(page)) {
				page = "1";
			}
			if (StringUtils.isBlank(rows)) {
				rows = "20";
			}
			List<DrugDispensDetailVo> infoList = null;
			if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
				Calendar c = Calendar.getInstance();
				Date date = new Date();
				c.setTime(date);
				c.add(Calendar.YEAR, -1);
				Date y = c.getTime();
				endTime = DateUtils.formatDateY_M_D(date);
				startTime = DateUtils.formatDateY_M_D(y);
			}
			infoList = nurseDrugDispensService.queryDrugDispensDetail(
					inpatientInfo, startTime, endTime, drugName, inpatientNoSerc,
					a, type, page, rows);
			int total = nurseDrugDispensService.queryDrugDispensDetailTotal(
					inpatientInfo, startTime, endTime, drugName, inpatientNoSerc,
					a, type);
			String json = JSONUtils.toJson(infoList);
			String json2 = "{\"total\":" + total + ",\"rows\":" + json + "}";
			WebUtils.webSendJSON(json2);
		}catch(Exception e){
			logger.error("ZYTJ_BQBYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_BQBYCX", "住院统计_病区摆药查询", "2", "0"), e);
		}
	}

	/**
	 * @Description:打印摆药明细列表
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-22
	 * @param
	 * @return void
	 * @version 1.0
	 **/
	@Action(value = "printDrugDispensDetail")
	public void printDrugDispensDetail() {
		try {
			List<DrugDispensDetailVo> infoList = new ArrayList<DrugDispensDetailVo>(
					1);
			if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
				Calendar c = Calendar.getInstance();
				Date date = new Date();
				c.setTime(date);
				c.add(Calendar.YEAR, -1);
				Date y = c.getTime();
				endTime = DateUtils.formatDateY_M_D(date);
				startTime = DateUtils.formatDateY_M_D(y);
			}
			DrugDispensDetailVo vo = new DrugDispensDetailVo();
			
			List<DrugDispensDetailVo> list = nurseDrugDispensService.queryDrugDispensDetail(inpatientInfo, startTime, endTime,
							drugName, inpatientNoSerc, a, type, null, null);
	
			Map<String, String> map2 = nurseDrugDispensService.queryBillNameMap();
			for (int i = 0; i < list.size(); i++) {
				String billclassName = list.get(i).getBillclassName();
				list.get(i).setBillclassName(map2.get(billclassName));
			}
			
			vo.setDrugDispensDetailList(list);
			
			// 渲染单位
			Map<String,String> drugpackagingunitMap = innerCodeService.getBusDictionaryMap("packunit");
			for (DrugDispensDetailVo vo1 : vo.getDrugDispensDetailList()) {
				vo1.setUnit(drugpackagingunitMap.get(vo1.getUnit()));
			}
			infoList.add(vo);
			request = ServletActionContext.getRequest();
			String root_path = request.getSession().getServletContext()
					.getRealPath("/");
			String reportFilePath = root_path
					+ "WEB-INF/reportFormat/jasper/WBYMXD.jasper";
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			if ("1".equals(type)) {
				parameters.put("name", "已摆药处方单");
			} else {
				parameters.put("name", "未摆药处方单");
			}
			parameters.put("SUBREPORT_DIR", root_path
					+ "WEB-INF/reportFormat/jasper/");
			parameters.put("hospitalName", HisParameters.PREFIXFILENAME);
				iReportService.doReportToJavaBean(request, WebUtils.getResponse(),
						reportFilePath, parameters, new JRBeanCollectionDataSource(
								infoList));
				infoList = null;
				parameters = null;
		} catch (Exception e) {
			logger.error("ZYTJ_BQBYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_BQBYCX", "住院统计_病区摆药查询", "2", "0"), e);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description：本区患者树
	 * @Author：zhuxiaolu
	 * @CreateDate：2016-1-5
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Action(value = "nurseChargeTree")
	public void nurseChargeTree() {
		try{
			if (inpatientInfo == null) {
				inpatientInfo = new InpatientInfoNow();
			}
			if (a == null) {
				a = "0";
			}
			
			List<TreeJson> treeDepat = nurseDrugDispensService.treeNurseCharge(
					deptId, id, type, inpatientInfo, a, startTime, endTime);
			String json = JSONUtils.toJson(treeDepat);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_BQBYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_BQBYCX", "住院统计_病区摆药查询", "2", "0"), e);
		}
	}
	/**
	 * 
	 * @Description：查询摆药单
	 * @Author：wangshujuan
	 * @CreateDate：2017-7-4
	 * @Modifier：
	 * @ModifyDate：2017-7-4
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Action(value = "queryBillNameList", results = { @Result(name = "json", type = "json") })
	public void queryBillNameList() {
		try{
			Map<String, String> map = nurseDrugDispensService.queryBillNameMap();
			String joString = JSONUtils.toJson(map);
			WebUtils.webSendJSON(joString);
		}catch(Exception e){
			logger.error("ZYTJ_BQBYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_BQBYCX", "住院统计_病区摆药查询", "2", "0"), e);
		}
	}
}
