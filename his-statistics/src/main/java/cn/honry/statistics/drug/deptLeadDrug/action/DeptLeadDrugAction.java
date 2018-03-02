package cn.honry.statistics.drug.deptLeadDrug.action;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.statistics.drug.deptLeadDrug.service.DeptLeadDrugService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @className：
 * @Description：各科室领药情况Action
 * @Author：tuchunajiang
 * @CreateDate：2016-6-25
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/DeptLeadDrug")
// @Namespace(value = "/stat/deptLeadDrug")
public class DeptLeadDrugAction extends ActionSupport {
	private Logger logger = Logger.getLogger(DeptLeadDrugAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;

	public void setHiasExceptionService(
			HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;

	}

	@Autowired
	@Qualifier(value = "deptLeadDrugService")
	private DeptLeadDrugService deptLeadDrugService;

	public void setDeptLeadDrugService(DeptLeadDrugService deptLeadDrugService) {
		this.deptLeadDrugService = deptLeadDrugService;
	}

	/**
	 * 注入inpatientInfoInInterService公共接口
	 */
	private InpatientInfoInInterService inpatientInfoInInterService;

	@Autowired
	@Qualifier(value = "inpatientInfo InInterService")
	public void setInpatientInfoInInterService(
			InpatientInfoInInterService inpatientInfoInInterService) {
		this.inpatientInfoInInterService = inpatientInfoInInterService;
	}

	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;

	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}

	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;

	public void setParameterInnerService(
			ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}

	/**
	 * 当前页数，用于分页查询
	 */
	private String page;

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;

	/**
	 * 领药科室
	 */
	private String drugDept;
	/**
	 * 药品性质
	 */
	private String drugxz;
	/**
	 * 药品名称
	 */
	private String drugName;
	/**
	 * 导出操作时，列表中数据json字符串
	 */
	private String rows;
	/**
	 * 开始时间
	 */
	private String sTime;
	/**
	 * 结束时间
	 */
	private String eTime;

	public String getsTime() {
		return sTime;
	}

	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

	public String geteTime() {
		return eTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getDrugDept() {
		return drugDept;
	}

	public void setDrugDept(String drugDept) {
		this.drugDept = drugDept;
	}

	public String getDrugxz() {
		return drugxz;
	}

	public void setDrugxz(String drugxz) {
		this.drugxz = drugxz;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	/***
	 * 跳转各科室领药情况页面
	 * 
	 * @author 涂川江
	 * @createDate ：2016年6月25日
	 * @version 1.0
	 */
	@RequiresPermissions(value = { "GKSLYQK:function:view" })
	@Action(value = "listdeptLeadDrug", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptLeadDrug/deptLeadDrugList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listdeptLeadDrug() {
		// 获取系统参数
		String param = parameterInnerService.getparameter("qygjzwcxtjsj");
		boolean flag = false;
		if ("1".equals(param)) {//
			// 职务验证
			SysEmployee sys = ShiroSessionUtils
					.getCurrentEmployeeFromShiroSession();
			String post = sys.getPost();

			if (null != post) {
				if ("1".equals(post) || "2".equals(post)) {// 院长副院长判断
					flag = true;
				} else {
					SysDepartment dept = ShiroSessionUtils
							.getCurrentUserLoginDepartmentFromShiroSession();
					ServletActionContext.getRequest().setAttribute(
							"loginDeptCode", dept.getDeptCode());
					ServletActionContext.getRequest().setAttribute(
							"loginDeptName", dept.getDeptName());
				}
			}
		} else {
			flag = true;
		}
		// 获取时间
		Date date = new Date();
		eTime = DateUtils.formatDateY_M_D(date);
		sTime = DateUtils.formatDateYM(date) + "-01";
		ServletActionContext.getRequest().setAttribute("sTime", sTime);
		ServletActionContext.getRequest().setAttribute("eTime", eTime);
		ServletActionContext.getRequest().setAttribute("flag", flag);
		return "list";
	}

	/***
	 * 查询各科室领药记录
	 * 
	 * @author 涂川江
	 * @createDate ：2016年6月25日
	 * @version 1.0
	 */
	@Action(value = "queryTableList")
	public void queryTableList() throws Exception {
		String sTime = ServletActionContext.getRequest().getParameter("sTime");
		String eTime = ServletActionContext.getRequest().getParameter("eTime");
		String drugDept = ServletActionContext.getRequest().getParameter(
				"drugDept");
		String drugxz = ServletActionContext.getRequest()
				.getParameter("drugxz");
		String drugName = ServletActionContext.getRequest().getParameter(
				"drugName");
		if (StringUtils.isBlank(sTime)) {
			Date date = new Date();
			sTime = DateUtils.formatDateYM(date) + "-01";
		}
		if (StringUtils.isBlank(eTime)) {
			Date date = new Date();
			eTime = DateUtils.formatDateY_M_D(date);
		}
		if (StringUtils.isNotBlank(drugDept)) {
			drugDept = "('" + drugDept.replace(",", "','") + "')";
		} else {
			List<SysDepartment> deptList = deptInInterService
					.getDeptByMenutypeAndUserCode(menuAlias, ShiroSessionUtils
							.getCurrentEmployeeFromShiroSession().getJobNo());
			if (deptList != null && deptList.size() > 0) {
				drugDept = "";
				for (SysDepartment sys : deptList) {
					if (StringUtils.isNotBlank(drugDept)) {
						drugDept += "','";
					}
					drugDept += sys.getDeptCode();
				}
				drugDept = "('" + drugDept + "')";
			}
		}

		Map<String, Object> retMap = new HashMap<String, Object>();
		int total = deptLeadDrugService.queryTableListTotal(sTime, eTime,
				drugDept, drugxz, drugName, null);
		List<DrugApplyoutNow> oprl = deptLeadDrugService.queryTableList(sTime,
				eTime, drugDept, drugxz, drugName, page, rows, null);
		retMap.put("total", total);
		retMap.put("rows", oprl);
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}

	/***
	 * 查询科室list
	 * 
	 * @author 涂川江
	 * @createDate ：2016年6月25日
	 * @version 1.0
	 */
	@Action(value = "querydrugDept")
	public void querydrugDept() throws Exception {
		List<SysDepartment> empl = inpatientInfoInInterService
				.queryDeptMapPublic();
		String json = JSONUtils.toExposeJson(empl, false, null, "deptCode",
				"deptName");
		WebUtils.webSendJSON(json);
	}

	/***
	 * 查询科室map
	 * 
	 * @author qh
	 * @createDate ：2017年5月17日
	 * @version 1.0
	 */
	@Action(value = "queryDeptMap")
	public void queryDeptMap() throws Exception {
		List<SysDepartment> empl = inpatientInfoInInterService
				.queryDeptMapPublic();
		Map<String, String> map = new HashMap<String, String>();
		for (SysDepartment e : empl) {
			map.put(e.getDeptCode(), e.getDeptName());
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}

	/***
	 * 查询药品性质list
	 * 
	 * @author 涂川江
	 * @createDate ：2016年6月25日
	 * @version 1.0
	 */
	@Action(value = "querydrugxz")
	public void querydrugxz() throws Exception {
		List<BusinessDictionary> empl = deptLeadDrugService.querydrugxz();
		String json = JSONUtils.toExposeJson(empl, false, null, "encode",
				"name");
		WebUtils.webSendJSON(json);
	}

	/***
	 * 查询药品名称list
	 * 
	 * @author 涂川江
	 * @createDate ：2016年6月25日
	 * @version 1.0
	 */
	@Action(value = "querydrugName")
	public void querydrugName() throws Exception {
		List<DrugInfo> empl = deptLeadDrugService.querydrugName();
		String json = JSONUtils.toExposeJson(empl, false, null, "code", "name");
		WebUtils.webSendJSON(json);
	}

	/***
	 * 导出
	 * 
	 * @author 涂川江
	 * @createDate ：2016年6月25日
	 * @version 1.0
	 */
	@SuppressWarnings("deprecation")
	@Action(value = "exportout")
	public void exportout() {
		List<DrugApplyout> datelist = new ArrayList<DrugApplyout>();
		if (StringUtils.isNotEmpty(rows) && !"[]".equals(rows)) {
			rows = rows.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
			try {
				datelist = JSONUtils.fromJson(rows,
						new TypeToken<List<DrugApplyout>>() {
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("各科室领药情况");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("领药科室");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("通用编码");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("国家编码");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("药品编码");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("药品名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("规格");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("包装单位");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
		cell.setCellValue("数量");
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);
		cell.setCellValue("金额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 9);
		cell.setCellValue("产地");
		cell.setCellStyle(style);
		for (int i = 0; i < datelist.size(); i++) {
			row = sheet.createRow((int) i + 1);
			row.createCell((short) 0).setCellValue(
					datelist.get(i).getDeptCode());
			row.createCell((short) 1).setCellValue(
					datelist.get(i).getDrugCnameinputcode());
			row.createCell((short) 3).setCellValue(
					datelist.get(i).getDrugCode());
			row.createCell((short) 4).setCellValue(
					datelist.get(i).getTradeName());
			if (StringUtils.isNotBlank(datelist.get(i).getSpecs())) {
				row.createCell((short) 5).setCellValue(
						datelist.get(i).getSpecs());
			} else {
				row.createCell((short) 5).setCellValue("");
			}

			if (StringUtils.isNotBlank(datelist.get(i).getPackUnit())) {
				row.createCell((short) 6).setCellValue(
						datelist.get(i).getPackUnit());
			} else {
				row.createCell((short) 6).setCellValue("");
			}

			if (datelist.get(i).getApplyNum() != null) {
				row.createCell((short) 7).setCellValue(
						datelist.get(i).getApplyNum());
			} else {
				row.createCell((short) 7).setCellValue("");
			}

			if (datelist.get(i).getSumCost() != null) {
				row.createCell((short) 8).setCellValue(
						datelist.get(i).getSumCost());
			} else {
				row.createCell((short) 8).setCellValue("");
			}
		}
		try {
			FileOutputStream fout = new FileOutputStream("E:/各科室领药情况.xls");
			wb.write(fout);
			fout.close();
		} catch (Exception e) {
			logger.error("ZYTJ_GKSLYQK", e);
			hiasExceptionService.saveExceptionInfoToMongo(
					new RecordToHIASException("ZYTJ_GKSLYQK", "住院统计_各科室领药情况",
							"2", "0"), e);
		}
		String result = "success";
		WebUtils.webSendString(result);
	}
}
