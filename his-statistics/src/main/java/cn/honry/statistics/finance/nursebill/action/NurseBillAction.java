package cn.honry.statistics.finance.nursebill.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.vo.StatVo;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.drug.nurseDrugDispens.service.NurseDrugDispensService;
import cn.honry.statistics.finance.nursebill.service.NurseBillService;
import cn.honry.statistics.finance.nursebill.vo.NurseBillHzVo;
import cn.honry.statistics.finance.nursebill.vo.NurseBillMxVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * <p>
 * 护士站领药查询
 * </p>
 * 
 * @Author: yuke
 * @CreateDate: 2017年7月4日 下午3:30:44
 * @Modifier: yuke
 * @ModifyDate: 2017年7月4日 下午3:30:44
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
@Namespace(value = "/statistics/nursebill")
public class NurseBillAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(NurseBillAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;

	public void setHiasExceptionService(
			HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;

	}

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

	@Autowired
	@Qualifier(value = "nurseDrugDispensService")
	private NurseDrugDispensService nurseDrugDispensService;

	public void setNurseDrugDispensService(
			NurseDrugDispensService nurseDrugDispensService) {
		this.nurseDrugDispensService = nurseDrugDispensService;
	}

	@Autowired
	@Qualifier(value = "inpatientInfoInInterService")
	private InpatientInfoInInterService inpatientInfoInInterService;

	public void setInpatientInfoInInterService(
			InpatientInfoInInterService inpatientInfoInInterService) {
		this.inpatientInfoInInterService = inpatientInfoInInterService;
	}

	/**
	 * 护士站领药service
	 */
	@Autowired
	@Qualifier(value = "nurseBillService")
	private NurseBillService nurseBillService;
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;

	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}

	/**
	 * 查询科室
	 */
	private String deptCode;

	/**
	 * 摆药单id
	 */
	private String billClassCode;

	/**
	 * 申请状态
	 */
	private String applyState;

	/**
	 * 开始时间
	 */
	private String beginTime;

	/**
	 * 结束时间
	 */
	private String endTime;

	/**
	 * 页数
	 */
	private String page;

	/**
	 * 记录数
	 */
	private String rows;

	/**
	 * ' 查询名称
	 */
	private String bname;

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getBillClassCode() {
		return billClassCode;
	}

	public void setBillClassCode(String billClassCode) {
		this.billClassCode = billClassCode;
	}

	public String getApplyState() {
		return applyState;
	}

	public void setApplyState(String applyState) {
		this.applyState = applyState;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setNurseBillService(NurseBillService nurseBillService) {
		this.nurseBillService = nurseBillService;
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

	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	/**
	 * 
	 * @Description： 获取list页面
	 * @Author：tangfeishuai
	 * @CreateDate：2016-5-27上午10:56:35
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@SuppressWarnings("deprecation")
	@RequiresPermissions(value = { "HSZLYCX:function:view" })
	@Action(value = "listNurseBill", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/nurseBill/nurseBillList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listNurseBill() {
		// 获取时间
		Date date = new Date();
		endTime = DateUtils.formatDateY_M_D(date);
		beginTime = DateUtils.formatDateYM(date) + "-01";
		return "list";
	}

	/**
	 *
	 * @Description：护士站取药汇总查询
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月22日 上午9:47:41
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version： 1.0：
	 *
	 */
	@SuppressWarnings("deprecation")
	@Action(value = "queryNurseBillHz")
	public void queryNurseBillHz() {
		try {
			if (StringUtils.isBlank(beginTime)) {
				Date date = new Date();
				beginTime = DateUtils.formatDateYM(date) + "-01";
			}
			if (StringUtils.isBlank(endTime)) {
				Date date = new Date();
				endTime = DateUtils.formatDateY_M_D(date);
			}
			if (StringUtils.isNotBlank(bname)) {
				bname = bname.trim();
			}
			Map<String, Object> retMap = new HashMap<String, Object>();
			if (StringUtils.isBlank(billClassCode)) {
				List<NurseBillHzVo> list = new ArrayList<NurseBillHzVo>();
				int total = 0;
				retMap.put("total", total);
				retMap.put("rows", list);
				String json = JSONUtils.toJson(retMap);
				WebUtils.webSendJSON(json);
			} else {
				List<NurseBillHzVo> list = nurseBillService.getNurseBillHz(
						deptCode, billClassCode, applyState, bname, page, rows,
						endTime, beginTime);
				int total = nurseBillService.getHzTotal(deptCode,
						billClassCode, bname, applyState, endTime, beginTime);
				retMap.put("total", total);
				retMap.put("rows", list);
				String json = JSONUtils.toJson(retMap);
				ServletActionContext.getRequest().getSession()
						.setAttribute("page", page);
				ServletActionContext.getRequest().getSession()
						.setAttribute("rows", rows);
				WebUtils.webSendJSON(json);
			}
		} catch (Exception ex) {
			logger.error("ZYTJ_HSZLY", ex);
			hiasExceptionService.saveExceptionInfoToMongo(
					new RecordToHIASException("ZYTJ_HSZLY", "住院统计_护士站领药", "2",
							"0"), ex);
		}
	}

	/**
	 *
	 * @throws UnsupportedEncodingException
	 * @Description：护士站取药汇总打印
	 * @Author：
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version： 1.0：
	 *
	 */
	@SuppressWarnings("deprecation")
	@Action(value = "printNurseBillHz")
	public void printNurseBillHz() throws UnsupportedEncodingException {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			if (StringUtils.isBlank(beginTime)) {
				Date date = new Date();
				beginTime = DateUtils.formatDateYM(date) + "-01";
			}
			if (StringUtils.isBlank(endTime)) {
				Date date = new Date();
				endTime = DateUtils.formatDateY_M_D(date);
			}
			if (StringUtils.isNotBlank(bname)) {
				bname = bname.trim();
			}
			/** 住院药房渲染 **/
			List<SysDepartment> deptl = inpatientInfoInInterService
					.queryDeptMapPublic();
			Map<String, String> deptMap = new HashMap<String, String>();
			for (SysDepartment dept : deptl) {
				deptMap.put(dept.getDeptCode(), dept.getDeptName());
			}
			List<NurseBillHzVo> list = new ArrayList<NurseBillHzVo>(1);
			page = request.getSession().getAttribute("page").toString();
			rows = request.getSession().getAttribute("rows").toString();
			if (StringUtils.isBlank(page)) {
				page = "1";
			}
			if (StringUtils.isBlank(rows)) {
				rows = "20";
			}
			NurseBillHzVo vo = new NurseBillHzVo();
			bname = new String(bname.getBytes("ISO-8859-1"), "utf-8");
			List<NurseBillHzVo> list2 = nurseBillService.getNurseBillHz(
					deptCode, billClassCode, applyState, bname, page, rows,
					endTime, beginTime);
			Map<String, String> unitMap = nurseBillService.queryPackUnitMap();
			for (NurseBillHzVo v : list2) {
				if (StringUtils.isNotBlank(v.getMinUnit())) {
					if (StringUtils.isNotBlank(unitMap.get(v.getMinUnit()))) {
						v.setMinUnit(unitMap.get(v.getMinUnit()));
					}
				}
			}
			vo.setNurseBillHzList(list2);
			Map<String, String> map = nurseDrugDispensService
					.queryBillNameMap();
			for (NurseBillHzVo pojo : vo.getNurseBillHzList()) {
				if (map.containsKey(pojo.getBillClassName())) {
					pojo.setBillClassName(map.get(pojo.getBillClassName()));
				}
				if (deptMap.get(pojo.getDrugDept()) != null) {
					pojo.setDrugDept(deptMap.get(pojo.getDrugDept()));
				}

				if (deptMap.get(pojo.getApplyDept()) != null) {
					pojo.setApplyDept(deptMap.get(pojo.getApplyDept()));
				}
			}
			map = null;
			list.add(vo);
			request = ServletActionContext.getRequest();
			String root_path = request.getSession().getServletContext()
					.getRealPath("/");
			String reportFilePath = root_path
					+ "WEB-INF/reportFormat/jasper/HSZLYYPHZTJ.jasper";
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", root_path
					+ "WEB-INF/reportFormat/jasper/");

			iReportService.doReportToJavaBean(request, WebUtils.getResponse(),
					reportFilePath, parameters, new JRBeanCollectionDataSource(
							list));
		} catch (Exception ex) {
			logger.error("ZYTJ_HSZLY", ex);
			hiasExceptionService.saveExceptionInfoToMongo(
					new RecordToHIASException("ZYTJ_HSZLY", "住院统计_护士站领药", "2",
							"0"), ex);
		}

	}

	/**
	 *
	 * @Description：护士站取药明细查询
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月22日 上午9:47:41
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version： 1.0：
	 *
	 */
	@Action(value = "queryNurseBillMx")
	public void queryNurseBillMx() {
		try {
			Map<String, Object> retMap = new HashMap<String, Object>();
			List<NurseBillMxVo> list = nurseBillService.getNurseBillMx(
					deptCode, billClassCode, applyState, page, rows, bname,
					beginTime, endTime, null);
			int total = nurseBillService.getMxTotal(deptCode, billClassCode,
					beginTime, endTime, applyState, bname, null);
			retMap.put("total", total);
			retMap.put("rows", list);
			String json = JSONUtils.toJson(retMap);
			ServletActionContext.getRequest().getSession()
					.setAttribute("page", page);
			ServletActionContext.getRequest().getSession()
					.setAttribute("rows", rows);
			WebUtils.webSendJSON(json);
		} catch (Exception ex) {
			logger.error("ZYTJ_HSZLY", ex);
			hiasExceptionService.saveExceptionInfoToMongo(
					new RecordToHIASException("ZYTJ_HSZLY", "住院统计_护士站领药", "2",
							"0"), ex);
		}
	}

	/**
	 *
	 * @throws Exception
	 * @Description：护士站取药明细打印
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月22日 上午9:47:41
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version： 1.0：
	 *
	 */
	@Action(value = "printNurseBillMx")
	public void printNurseBillMx() throws UnsupportedEncodingException {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			StatVo statVo = nurseBillService.findMaxMin();
			/** 住院药房渲染 **/
			List<SysDepartment> deptl = inpatientInfoInInterService
					.queryDeptMapPublic();
			Map<String, String> deptMap = new HashMap<String, String>();
			for (SysDepartment dept : deptl) {
				deptMap.put(dept.getDeptCode(), dept.getDeptName());
			}

			List<NurseBillMxVo> list = new ArrayList<NurseBillMxVo>(1);

			NurseBillMxVo vo = new NurseBillMxVo();
			page = request.getSession().getAttribute("page").toString();
			rows = request.getSession().getAttribute("rows").toString();
			if (StringUtils.isBlank(page)) {
				page = "1";
			}
			if (StringUtils.isBlank(rows)) {
				rows = "20";
			}
			bname = new String(bname.getBytes("ISO-8859-1"), "utf-8");
			vo.setNurseBillMxList(nurseBillService.getNurseBillMx(deptCode,
					billClassCode, applyState, page, rows, bname, beginTime,
					endTime, statVo));
			Map<String, String> map = nurseDrugDispensService
					.queryBillNameMap();
			for (NurseBillMxVo pojo : vo.getNurseBillMxList()) {
				if (map.containsKey(pojo.getBillClassName())) {
					pojo.setBillClassName(map.get(pojo.getBillClassName()));
				}
				if (deptMap.get(pojo.getDrugDept()) != null) {
					pojo.setDrugDept(deptMap.get(pojo.getDrugDept()));
				}
				if (deptMap.get(pojo.getApplyDept()) != null) {
					pojo.setApplyDept(deptMap.get(pojo.getApplyDept()));
				}
			}
			map = null;
			list.add(vo);
			request = ServletActionContext.getRequest();
			String root_path = request.getSession().getServletContext()
					.getRealPath("/");
			String reportFilePath = root_path
					+ "WEB-INF/reportFormat/jasper/HSZLYYPMXTJ.jasper";
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", root_path
					+ "WEB-INF/reportFormat/jasper/");
			iReportService.doReportToJavaBean(request, WebUtils.getResponse(),
					reportFilePath, parameters, new JRBeanCollectionDataSource(
							list));
		} catch (Exception ex) {
			logger.error("ZYTJ_HSZLY", ex);
			hiasExceptionService.saveExceptionInfoToMongo(
					new RecordToHIASException("ZYTJ_HSZLY", "住院统计_护士站领药", "2",
							"0"), ex);
		}
	}

	/**
	 * 病区下对应科室下的摆药单树
	 * 
	 * @author tangfeishuai
	 * @createDate：
	 * @modifier
	 * @modifyDate：2016年6月12日 上午11:00:02
	 * @modifyRmk：
	 * @version 1.0
	 */
	@Action(value = "getNurseBillTree")
	public void getNurseBillTree() throws Exception {
		SysDepartment sysDept=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String treeBillSearchjson =null;
		if(sysDept!=null){
			List<TreeJson> treeBillSearch = nurseBillService.treeNurseBillSearch();
			treeBillSearchjson = JSONUtils.toJson(treeBillSearch);
		}else{
			Map<String,String> map=new HashMap<String,String>();
			map.put("resCode", "error");
			map.put("resMsg", "请选择登录科室");
			treeBillSearchjson=JSONUtils.toJson(map);
		}
		WebUtils.webSendJSON(treeBillSearchjson);
	}
}
